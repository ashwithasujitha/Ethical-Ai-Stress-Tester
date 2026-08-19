import os
import pandas as pd
from model_loader import load_model
from model_predictor import predict

COMPAS_PATH = r"C:\Users\hp\Downloads\archive\compas-scores-raw.csv"
COMPAS_MODEL_PATH = "model_compas.pkl"
TARGET_COL = "DecileScore"
SENSITIVE_COL = "Ethnic_Code_Text"


def load_compas_test_data(csv_path):
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"COMPAS dataset not found: {csv_path}")

    df = pd.read_csv(csv_path)
    columns_to_use = [SENSITIVE_COL, "Sex_Code_Text", TARGET_COL]
    if "RecSupervisionLevel" in df.columns:
        columns_to_use.append("RecSupervisionLevel")

    df_clean = df[columns_to_use].dropna().copy()
    sensitive_features = df_clean[SENSITIVE_COL].reset_index(drop=True)
    X = df_clean.drop(columns=[TARGET_COL, SENSITIVE_COL])

    return X, sensitive_features


def main():
    print("=" * 40)
    print("MODEL PREDICTION TEST")
    print("=" * 40)

    if not os.path.exists(COMPAS_MODEL_PATH):
        raise FileNotFoundError(
            f"COMPAS-compatible model not found: {COMPAS_MODEL_PATH}."
        )

    X_test, sensitive_features = load_compas_test_data(COMPAS_PATH)
    model = load_model(COMPAS_MODEL_PATH)
    y_pred = predict(model, X_test)

    print(f"\nModel: {type(model).__name__}")
    print(f"Test samples: {len(X_test)}")
    print("Predictions generated successfully!")
    print(f"Number of predictions: {len(y_pred)}")
    print("\nFirst 10 predictions:")
    print(list(y_pred[:10]))

    if len(y_pred) != len(X_test):
        raise ValueError(
            f"Prediction count {len(y_pred)} does not match X_test count {len(X_test)}."
        )

    print(f"\nSensitive feature preserved: {SENSITIVE_COL}")


if __name__ == "__main__":
    main()
