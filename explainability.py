import os
import numpy as np
import pandas as pd


def _get_shap():
    try:
        import shap
    except ImportError as exc:
        raise ImportError("SHAP is not installed. Install it with: pip install shap") from exc
    return shap


def explain_model(model, X_test, max_display=10, plot_path="shap_summary.png", sample_size=1000):
    """Generate SHAP explanations for a scikit-learn pipeline model."""
    if model is None:
        raise ValueError("Model must not be None.")

    if X_test is None:
        raise ValueError("X_test must not be None.")

    if hasattr(X_test, "shape") and X_test.shape[0] == 0:
        raise ValueError("X_test must contain at least one sample.")

    if isinstance(X_test, pd.DataFrame) and len(X_test) > sample_size:
        X_explain = X_test.iloc[:sample_size].copy()
    else:
        X_explain = X_test.copy()

    shap = _get_shap()

    if isinstance(X_explain, pd.DataFrame):
        feature_names = list(X_explain.columns)
        X_explain_for_shap = X_explain.copy()
        for col in X_explain_for_shap.select_dtypes(include=['object', 'string']).columns:
            X_explain_for_shap[col] = X_explain_for_shap[col].astype('category').cat.codes
    else:
        X_explain_for_shap = X_explain
        feature_names = [f"feature_{i}" for i in range(X_explain.shape[1])]

    try:
        if hasattr(model, "named_steps") and 'classifier' in model.named_steps:
            transformed = model.named_steps['preprocessor'].transform(X_explain)
            transformed = model.named_steps['scaler'].transform(transformed)
            explainer = shap.Explainer(model.named_steps['classifier'], transformed)
            shap_values = explainer(transformed)
            if hasattr(model, 'named_steps'):
                feature_names = [f"feature_{i}" for i in range(transformed.shape[1])]
        elif hasattr(model, "predict_proba"):
            explainer = shap.Explainer(model.predict_proba, X_explain_for_shap)
            shap_values = explainer(X_explain_for_shap)
        else:
            explainer = shap.Explainer(model.predict, X_explain_for_shap)
            shap_values = explainer(X_explain_for_shap)
    except Exception as exc:
        raise RuntimeError(f"SHAP explanation failed: {exc}") from exc

    if hasattr(shap_values, "values"):
        values = shap_values.values
    else:
        values = shap_values

    if isinstance(values, list):
        values = values[0]

    if isinstance(values, np.ndarray):
        if values.ndim == 3:
            values = values[:, :, 1] if values.shape[2] > 1 else values[:, :, 0]
        elif values.ndim > 2:
            values = values.reshape(values.shape[0], -1)

    if hasattr(model, "named_steps"):
        preprocessor = model.named_steps.get("preprocessor")
        if preprocessor is not None and hasattr(preprocessor, "get_feature_names_out"):
            feature_names = list(preprocessor.get_feature_names_out())

    if values.ndim == 1:
        values = values.reshape(-1, 1)

    if len(feature_names) != values.shape[1]:
        feature_names = [f"feature_{i}" for i in range(values.shape[1])]

    mean_abs = np.mean(np.abs(values), axis=0)
    ranked = sorted(zip(feature_names, mean_abs), key=lambda item: item[1], reverse=True)
    top_features = ranked[:max_display]

    try:
        import matplotlib
        matplotlib.use("Agg")
        import matplotlib.pyplot as plt
        shap.summary_plot(values, transformed if 'transformed' in locals() else X_explain_for_shap, feature_names=feature_names, show=False)
        plt.savefig(plot_path, bbox_inches="tight")
        plt.close()
    except Exception as exc:
        raise RuntimeError(f"SHAP plot generation failed: {exc}") from exc

    return {
        "top_features": top_features,
        "plot_path": plot_path,
        "feature_names": feature_names,
    }
