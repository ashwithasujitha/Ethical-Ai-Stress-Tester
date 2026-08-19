"""Create a deliberately biased, COMPAS-compatible demo model.

This script is intentionally for testing fairness detection only. It does not
replace or modify any existing project model or application source.
"""

from pathlib import Path
import sys

import joblib
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler


PROJECT_ROOT = Path(__file__).resolve().parents[1]
DEFAULT_DATA_PATH = Path(r"C:\Users\hp\Downloads\archive\compas-scores-raw.csv")
OUTPUT_PATH = PROJECT_ROOT / "biased_model.pkl"

FEATURE_COLUMNS = ["Sex_Code_Text", "RecSupervisionLevel"]
RACE_COLUMN = "Ethnic_Code_Text"


def create_biased_model(data_path: Path = DEFAULT_DATA_PATH) -> Path:
    """Train and save a controlled model with a detectable demographic gap.

    The demo label intentionally treats RecSupervisionLevel >= 2 as high risk.
    In this COMPAS dataset that proxy has substantially different distributions
    across race groups, giving the bias detector a repeatable selection-rate
    disparity without requiring race as an inference-time model feature.
    """
    data_path = Path(data_path)
    if not data_path.is_file():
        raise FileNotFoundError(f"COMPAS CSV not found: {data_path}")

    data = pd.read_csv(data_path, usecols=[RACE_COLUMN, *FEATURE_COLUMNS]).dropna()
    if data.empty:
        raise ValueError("No complete COMPAS records were available for training.")

    features = data[FEATURE_COLUMNS]
    # Intentional controlled bias for fairness-detector demonstration.
    demo_target = (data["RecSupervisionLevel"] >= 2).astype(int)

    preprocessor = ColumnTransformer(
        transformers=[
            (
                "sex",
                OneHotEncoder(drop="first", handle_unknown="ignore", sparse_output=False),
                ["Sex_Code_Text"],
            ),
            ("supervision_level", "passthrough", ["RecSupervisionLevel"]),
        ],
        remainder="drop",
    )
    model = Pipeline(
        steps=[
            ("preprocessor", preprocessor),
            ("scaler", StandardScaler()),
            ("classifier", LogisticRegression(max_iter=1000, random_state=42)),
        ]
    )
    model.fit(features, demo_target)

    joblib.dump(model, OUTPUT_PATH)

    # Verify the exact interfaces used by the existing loader and predictor.
    sys.path.insert(0, str(PROJECT_ROOT))
    from model_loader import load_model
    from model_predictor import predict

    loaded_model = load_model(str(OUTPUT_PATH))
    predictions = predict(loaded_model, features.iloc[:4])
    if len(predictions) != 4:
        raise RuntimeError("Compatibility prediction verification failed.")

    print(str(OUTPUT_PATH))
    return OUTPUT_PATH


if __name__ == "__main__":
    create_biased_model()
