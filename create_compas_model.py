import joblib
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler

DATA_PATH = r"C:\Users\hp\Downloads\archive\compas-scores-raw.csv"
MODEL_PATH = "model_compas.pkl"
RACE_COL = "Ethnic_Code_Text"
SEX_COL = "Sex_Code_Text"
TARGET_COL = "DecileScore"


def build_compas_model(data_path=DATA_PATH, model_path=MODEL_PATH):
    df = pd.read_csv(data_path)

    columns_to_use = [RACE_COL, SEX_COL, TARGET_COL]
    if "RecSupervisionLevel" in df.columns:
        columns_to_use.append("RecSupervisionLevel")

    df_clean = df[columns_to_use].dropna().copy()
    df_clean[TARGET_COL] = (df_clean[TARGET_COL] > df_clean[TARGET_COL].median()).astype(int)

    X = df_clean.drop(columns=[TARGET_COL, RACE_COL])
    y = df_clean[TARGET_COL]

    categorical_features = X.select_dtypes(include=["object"]).columns.tolist()
    numeric_features = [col for col in X.columns if col not in categorical_features]

    preprocessor = ColumnTransformer(
        transformers=[
            (
                "categorical",
                OneHotEncoder(drop="first", sparse_output=False, handle_unknown="ignore"),
                categorical_features,
            ),
            ("numeric", "passthrough", numeric_features),
        ]
    )

    pipeline = Pipeline(
        [
            ("preprocessor", preprocessor),
            ("scaler", StandardScaler()),
            ("classifier", LogisticRegression(max_iter=1000, solver="lbfgs")),
        ]
    )

    pipeline.fit(X, y)
    joblib.dump(pipeline, model_path)
    return model_path


if __name__ == "__main__":
    saved_path = build_compas_model()
    print(f"COMPAS-compatible model saved to: {saved_path}")
