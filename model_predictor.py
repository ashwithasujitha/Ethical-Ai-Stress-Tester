def predict(model, X_test):
    """Generate predictions from a loaded ML model using test features."""
    if model is None:
        raise ValueError("The model must not be None.")

    if not hasattr(model, "predict") or not callable(model.predict):
        raise TypeError("The provided model does not have a callable predict() method.")

    if X_test is None:
        raise ValueError("X_test must not be None.")

    try:
        n_samples = len(X_test)
    except TypeError:
        raise ValueError("X_test must be a sequence or array-like with a length.")

    if n_samples == 0:
        raise ValueError("X_test must contain at least one sample.")

    try:
        y_pred = model.predict(X_test)
    except Exception as exc:
        raise RuntimeError(f"Prediction failed: {exc}") from exc

    try:
        n_pred = len(y_pred)
    except TypeError:
        raise RuntimeError("Prediction output is not sequence-like.")

    if n_pred != n_samples:
        raise ValueError(
            f"Prediction length {n_pred} does not match input sample count {n_samples}."
        )

    return y_pred
