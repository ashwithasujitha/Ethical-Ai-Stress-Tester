import os
import joblib


def load_model(model_path):
    """Load a trained ML model from a .pkl or .joblib file."""

    if not isinstance(model_path, str) or not model_path.strip():
        raise ValueError("Model path must be a non-empty string.")

    model_path = model_path.strip()

    if not os.path.exists(model_path):
        raise FileNotFoundError(
            f"Model file not found: {model_path}"
        )

    _, ext = os.path.splitext(model_path)
    ext = ext.lower()

    if ext not in {".pkl", ".joblib"}:
        raise ValueError(
            f"Unsupported model file extension '{ext}'. "
            "Supported extensions are .pkl and .joblib"
        )

    try:
        model = joblib.load(model_path)
    except Exception as exc:
        raise ValueError(
            f"Failed to load model from '{model_path}': {exc}"
        ) from exc

    return model