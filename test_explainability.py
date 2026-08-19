import os
import pandas as pd
from model_loader import load_model
from model_predictor import predict
from explainability import explain_model

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
    X = df_clean.drop(columns=[TARGET_COL, SENSITIVE_COL])
    return X


def main():
    print("=" * 40)
    print("EXPLAINABLE AI - SHAP")
    print("=" * 40)

    if not os.path.exists(COMPAS_MODEL_PATH):
        raise FileNotFoundError(f"Trained model not found: {COMPAS_MODEL_PATH}")

    X_test = load_compas_test_data(COMPAS_PATH)
    model = load_model(COMPAS_MODEL_PATH)
    y_pred = predict(model, X_test)

    print(f"\nModel: {type(model).__name__}")
    print(f"Test samples: {len(X_test)}")
    print(f"Predictions generated: {len(y_pred)}")

    result = explain_model(model, X_test, max_display=10, plot_path="shap_summary.png")

    print("\nTop Feature Importance:")
    for index, (feature_name, importance) in enumerate(result["top_features"], start=1):
        print(f"{index}. {feature_name:<20} {importance:.6f}")

    print(f"\nSHAP summary plot saved to: {result['plot_path']}")
    print("\nFeature influence is shown separately from fairness results.")
    print("Ethnic_Code_Text remains available as a sensitive feature for later fairness analysis.")


if __name__ == "__main__":
    main()
