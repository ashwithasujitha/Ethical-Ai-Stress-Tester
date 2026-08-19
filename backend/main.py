import os
import sys
import shutil
import tempfile
from pathlib import Path
from typing import Any, Dict, List, Optional

from fastapi import FastAPI, File, Form, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
import pandas as pd

ROOT_DIR = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT_DIR))

from model_loader import load_model
from model_predictor import predict
from model_bias_detector import detect_bias
from risk_engine import calculate_risk_score
from explainability import explain_model


app = FastAPI(
    title="AI Bias Tester Backend",
    description="Backend service for auditing model fairness, risk, and SHAP explainability.",
)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def validate_file_extension(filename: str, allowed_extensions: List[str]) -> bool:
    return os.path.splitext(filename.lower())[1] in allowed_extensions


def infer_target_column(df: pd.DataFrame, sensitive_feature: str) -> str:
    candidates = ["target", "label", "y", "DecileScore", "class", "Class"]
    for candidate in candidates:
        if candidate in df.columns and candidate != sensitive_feature:
            return candidate
    return ""


def ensure_binary_target(y: pd.Series) -> pd.Series:
    if y.nunique() == 2 and set(pd.Series(y).dropna().unique()) <= {0, 1}:
        return y.astype(int)

    if y.name == "DecileScore" and pd.api.types.is_numeric_dtype(y):
        return (y > y.median()).astype(int)

    if pd.api.types.is_numeric_dtype(y) and y.nunique() == 2:
        return pd.Series(pd.Categorical(y).codes, index=y.index).astype(int)

    if pd.api.types.is_object_dtype(y) and y.nunique() == 2:
        return pd.Series(pd.Categorical(y).codes, index=y.index).astype(int)

    raise ValueError(
        "Target column must be binary or convertible to binary. "
        "Include a binary target column such as 'target', 'label', 'y', or 'DecileScore'."
    )


def select_features(
    df: pd.DataFrame,
    model: Any,
    sensitive_feature: str,
    target_column: str,
) -> pd.DataFrame:
    """Return the feature frame expected by a fitted model.

    sklearn estimators fitted from a DataFrame expose ``feature_names_in_``.
    Using it prevents dataset-only columns, including the sensitive attribute,
    from being passed to a model during an audit.
    """
    expected_features = getattr(model, "feature_names_in_", None)
    if expected_features is not None:
        feature_columns = list(expected_features)
    else:
        feature_columns = [
            column
            for column in df.columns
            if column not in {sensitive_feature, target_column}
        ]

    missing_columns = [column for column in feature_columns if column not in df.columns]
    if missing_columns:
        raise ValueError(
            "Dataset is missing model feature columns: " + ", ".join(missing_columns)
        )

    if not feature_columns:
        raise ValueError("Unable to determine feature columns for the uploaded model.")

    features = df[feature_columns]
    if features.isnull().any().any():
        null_columns = features.columns[features.isnull().any()].tolist()
        raise ValueError(
            "Dataset contains missing values in model feature columns: "
            + ", ".join(null_columns)
        )
    return features


def load_uploaded_file(upload_file: UploadFile, temp_dir: str) -> str:
    file_path = os.path.join(temp_dir, upload_file.filename)
    with open(file_path, "wb") as out_file:
        out_file.write(upload_file.file.read())
    return file_path


def run_dataset_bias_detection(df: pd.DataFrame, sensitive_feature: str) -> Dict[str, Any]:
    if sensitive_feature not in df.columns:
        raise ValueError(f"Sensitive feature column '{sensitive_feature}' was not found in the uploaded dataset.")

    target_column = infer_target_column(df, sensitive_feature)
    if not target_column:
        raise ValueError(
            "Unable to infer a target column from the uploaded dataset. "
            "Please include a binary target column such as 'target', 'label', 'y', or 'DecileScore'."
        )

    y_test = ensure_binary_target(df[target_column])
    # Reuse existing bias detection logic even when only labels are available.
    bias_result = detect_bias(y_test, y_test, df[sensitive_feature])
    affected_groups = [
        {
            "metric": d["metric"],
            "max_group": d["max_group"],
            "min_group": d["min_group"],
            "gap": d["gap"],
        }
        for d in bias_result.get("disparities", [])
    ]

    return {
        "mode": "dataset_only",
        "dataset_samples": int(df.shape[0]),
        "bias_detected": bool(bias_result.get("bias_detected", False)),
        "accuracy_gap": bias_result["gaps"].get("accuracy"),
        "selection_rate_gap": bias_result["gaps"].get("selection_rate"),
        "tpr_gap": bias_result["gaps"].get("tpr"),
        "fpr_gap": bias_result["gaps"].get("fpr"),
        "affected_groups": affected_groups,
        "target_column": target_column,
    }


@app.get("/api/health")
def health_check() -> Dict[str, str]:
    return {"status": "AI Bias Tester backend is running"}


@app.post("/api/audit")
async def audit_model(
    model: Optional[UploadFile] = File(None),
    dataset: Optional[UploadFile] = File(None),
    sensitive_feature: Optional[str] = Form(None),
) -> Dict[str, Any]:
    if model is None and dataset is None:
        raise HTTPException(
            status_code=400,
            detail="Please upload a model, dataset, or both.",
        )

    if model is not None and model.filename:
        if not validate_file_extension(model.filename, [".pkl", ".joblib"]):
            raise HTTPException(
                status_code=400,
                detail="Model file must be a .pkl or .joblib file.",
            )

    if dataset is not None and dataset.filename:
        if not validate_file_extension(dataset.filename, [".csv"]):
            raise HTTPException(
                status_code=400,
                detail="Dataset file must be a .csv file.",
            )

    if dataset is not None and sensitive_feature is None:
        raise HTTPException(
            status_code=400,
            detail="sensitive_feature is required when dataset is provided.",
        )

    temp_dir = tempfile.mkdtemp(prefix="ai_bias_audit_")
    try:
        dataset_df = None
        trained_model = None

        if dataset is not None:
            dataset_path = os.path.join(temp_dir, dataset.filename)
            with open(dataset_path, "wb") as dataset_file:
                dataset_file.write(await dataset.read())
            try:
                dataset_df = pd.read_csv(dataset_path)
            except Exception as exc:
                raise HTTPException(
                    status_code=400,
                    detail=f"Failed to read CSV dataset: {exc}",
                ) from exc

            if dataset_df.empty:
                raise HTTPException(status_code=400, detail="Uploaded dataset is empty.")

        if model is not None:
            model_path = os.path.join(temp_dir, model.filename)
            with open(model_path, "wb") as model_file:
                model_file.write(await model.read())

            try:
                trained_model = load_model(model_path)
            except Exception as exc:
                raise HTTPException(status_code=400, detail=str(exc)) from exc

        if model is None and dataset is not None:
            try:
                result = run_dataset_bias_detection(dataset_df, sensitive_feature)
            except Exception as exc:
                raise HTTPException(status_code=400, detail=str(exc)) from exc
            return result

        if model is not None and dataset is None:
            model_type = type(trained_model).__name__
            return {
                "mode": "model_only",
                "model_type": model_type,
                "message": (
                    "Model uploaded without dataset. A fairness evaluation requires evaluation data. "
                    "Upload a dataset or configure a reference dataset to run bias detection."
                ),
            }

        if model is not None and dataset is not None:
            if sensitive_feature not in dataset_df.columns:
                raise HTTPException(
                    status_code=400,
                    detail=f"Sensitive feature column '{sensitive_feature}' was not found in the uploaded dataset.",
                )

            target_column = infer_target_column(dataset_df, sensitive_feature)
            if not target_column:
                raise HTTPException(
                    status_code=400,
                    detail=(
                        "Unable to infer a target column from the uploaded dataset. "
                        "Please include a binary target column such as 'target', 'label', 'y', or 'DecileScore'."
                    ),
                )

            try:
                y_test = ensure_binary_target(dataset_df[target_column])
            except ValueError as exc:
                raise HTTPException(status_code=400, detail=str(exc)) from exc

            try:
                X_test = select_features(dataset_df, trained_model, sensitive_feature, target_column)
            except ValueError as exc:
                raise HTTPException(status_code=400, detail=str(exc)) from exc

            if X_test.shape[0] == 0:
                raise HTTPException(status_code=400, detail="No rows available after preprocessing the dataset.")
            if X_test.shape[1] == 0:
                raise HTTPException(
                    status_code=400,
                    detail="No feature columns remain after excluding the target and sensitive feature.",
                )

            try:
                y_pred = predict(trained_model, X_test)
            except Exception as exc:
                raise HTTPException(status_code=400, detail=str(exc)) from exc

            try:
                bias_result = detect_bias(y_test, y_pred, dataset_df[sensitive_feature])
            except Exception as exc:
                raise HTTPException(status_code=400, detail=f"Bias detection failed: {exc}") from exc

            try:
                risk_result = calculate_risk_score(bias_result["gaps"])
            except Exception as exc:
                raise HTTPException(status_code=400, detail=f"Risk scoring failed: {exc}") from exc

            try:
                explain_result = explain_model(
                    trained_model,
                    X_test,
                    max_display=10,
                    plot_path=os.path.join(temp_dir, "shap_summary.png"),
                )
            except Exception as exc:
                raise HTTPException(status_code=400, detail=f"Explainability failed: {exc}") from exc

            affected_groups = [
                {
                    "metric": disparity["metric"],
                    "max_group": disparity["max_group"],
                    "min_group": disparity["min_group"],
                    "gap": disparity["gap"],
                }
                for disparity in bias_result.get("disparities", [])
            ]

            return {
                "mode": "model_dataset",
                "model_type": type(trained_model).__name__,
                "test_samples": int(X_test.shape[0]),
                "bias_detected": bool(bias_result.get("bias_detected", False)),
                "risk_score": risk_result["risk_score"],
                "risk_level": risk_result["risk_level"],
                "accuracy_gap": bias_result["gaps"].get("accuracy"),
                "selection_rate_gap": bias_result["gaps"].get("selection_rate"),
                "tpr_gap": bias_result["gaps"].get("tpr"),
                "fpr_gap": bias_result["gaps"].get("fpr"),
                "affected_groups": affected_groups,
                "top_features": [
                    {"feature": name, "importance": float(value)}
                    for name, value in explain_result.get("top_features", [])
                ],
            }
    finally:
        shutil.rmtree(temp_dir, ignore_errors=True)


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("backend.main:app", host="0.0.0.0", port=8000, log_level="info")
