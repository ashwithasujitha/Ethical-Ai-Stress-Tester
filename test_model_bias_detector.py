import os
import pandas as pd
from model_loader import load_model
from model_predictor import predict
from model_bias_detector import detect_bias
from risk_engine import format_risk_report

COMPAS_PATH = r"C:\Users\hp\Downloads\archive\compas-scores-raw.csv"
COMPAS_MODEL_PATH = "model_compas.pkl"
TARGET_COL = "DecileScore"
SENSITIVE_COL = "Ethnic_Code_Text"
BIAS_THRESHOLD = 0.10


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
    
    y_test = (df_clean[TARGET_COL] > df_clean[TARGET_COL].median()).astype(int).reset_index(drop=True)

    return X, y_test, sensitive_features


def main():
    print("=" * 50)
    print("MODEL BIAS DETECTION TEST")
    print("=" * 50)

    if not os.path.exists(COMPAS_MODEL_PATH):
        raise FileNotFoundError(
            f"COMPAS-compatible model not found: {COMPAS_MODEL_PATH}."
        )

    X_test, y_test, sensitive_features = load_compas_test_data(COMPAS_PATH)
    model = load_model(COMPAS_MODEL_PATH)
    y_pred = predict(model, X_test)

    print(f"\nModel: {type(model).__name__}")
    print(f"Test samples: {len(X_test)}")
    print(f"Sensitive feature: {SENSITIVE_COL}")
    print(f"Bias threshold: {BIAS_THRESHOLD * 100}%")

    result = detect_bias(y_test, y_pred, sensitive_features, bias_threshold=BIAS_THRESHOLD)

    print("\n" + "=" * 50)
    print("BIAS DETECTION RESULTS")
    print("=" * 50)

    if result["bias_detected"]:
        print("\nBIAS DETECTED: YES")
    else:
        print("\nBIAS DETECTED: NO")

    print("\nMetric Gaps (by demographic group):")
    for metric_name, gap in result["gaps"].items():
        print(f"  {metric_name}: {gap:.4f} ({gap * 100:.2f}%)")

    if result["disparities"]:
        print("\nDisparities exceeding threshold:")
        for disp in result["disparities"]:
            print(f"\n  Metric: {disp['metric']}")
            print(f"    Gap: {disp['gap']:.4f} ({disp['gap'] * 100:.2f}%)")
            print(f"    Highest group: {disp['max_group']} ({disp['max_value']:.4f})")
            print(f"    Lowest group: {disp['min_group']} ({disp['min_value']:.4f})")

    print("\n" + "=" * 50)
    print("Detailed Metrics by Group")
    print("=" * 50)
    print(result["metrics_frame"].by_group)

    format_risk_report(result["gaps"])

    print("\n" + "=" * 50)
    print("ANALYSIS COMPLETE")
    print("=" * 50)


if __name__ == "__main__":
    main()
