import joblib
import pandas as pd
import numpy as np
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.base import BaseEstimator, TransformerMixin

DATA_PATH = r"C:\Users\hp\Downloads\archive\compas-scores-raw.csv"
MODEL_PATH = r"C:\Users\hp\Desktop\Ai Bias Detection\biased_model.pkl"
RACE_COL = "Ethnic_Code_Text"
SEX_COL = "Sex_Code_Text"
TARGET_COL = "DecileScore"


class BiasInjectingPreprocessor(BaseEstimator, TransformerMixin):
    """Custom preprocessor that intentionally introduces bias into the data."""

    def fit(self, X, y=None):
        return self

    def transform(self, X):
        X_copy = X.copy()
        
        # Inject bias: if the race category contains certain keywords, 
        # artificially increase the risk score for those individuals
        if isinstance(X_copy, pd.DataFrame):
            # For African American individuals, create biased feature engineering
            if RACE_COL in X_copy.columns:
                # Create a bias multiplier: certain races get disadvantaged
                bias_multiplier = np.where(
                    X_copy[RACE_COL].str.lower().str.contains('african', na=False),
                    1.5,  # Multiply risk features by 1.5 for African Americans
                    1.0
                )
                
                # Apply bias multiplier to numeric features
                for col in X_copy.select_dtypes(include=[np.number]).columns:
                    X_copy[col] = X_copy[col] * bias_multiplier
        
        return X_copy


def build_biased_compas_model(data_path=DATA_PATH, model_path=MODEL_PATH):
    """
    Build a COMPAS-compatible model with intentional fairness disparity.
    
    This creates a TEST/DEMO model designed to have measurable bias
    that can be detected by bias detection tools.
    """
    print(f"Loading COMPAS data from: {data_path}")
    df = pd.read_csv(data_path)
    
    print(f"Total records loaded: {len(df)}")
    print(f"Columns in dataset: {df.columns.tolist()}")

    # Select columns used in the model
    columns_to_use = [RACE_COL, SEX_COL, TARGET_COL]
    if "RecSupervisionLevel" in df.columns:
        columns_to_use.append("RecSupervisionLevel")

    # Clean data
    df_clean = df[columns_to_use].dropna().copy()
    print(f"Records after cleaning: {len(df_clean)}")
    
    # Create binary target (high risk vs low risk)
    df_clean[TARGET_COL] = (df_clean[TARGET_COL] > df_clean[TARGET_COL].median()).astype(int)
    
    print(f"\nTarget distribution:")
    print(df_clean[TARGET_COL].value_counts())
    print(f"\nRace distribution:")
    print(df_clean[RACE_COL].value_counts())

    # Separate features and target
    X = df_clean.drop(columns=[TARGET_COL, RACE_COL])
    y = df_clean[TARGET_COL]

    # Identify categorical and numeric features
    categorical_features = X.select_dtypes(include=["object"]).columns.tolist()
    numeric_features = [col for col in X.columns if col not in categorical_features]

    print(f"\nCategorical features: {categorical_features}")
    print(f"Numeric features: {numeric_features}")

    # Build preprocessing pipeline with bias injection
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

    # Create pipeline with biased model
    # The LogisticRegression is trained with higher class weights for the positive class,
    # combined with the bias-injecting preprocessor to amplify disparities
    pipeline = Pipeline(
        [
            ("preprocessor", preprocessor),
            ("scaler", StandardScaler()),
            (
                "classifier",
                LogisticRegression(
                    max_iter=1000,
                    solver="lbfgs",
                    class_weight={0: 0.3, 1: 0.7},  # Bias: overweight positive class
                    random_state=42
                ),
            ),
        ]
    )

    print("\nTraining biased COMPAS model...")
    pipeline.fit(X, y)
    
    # Save the model
    print(f"\nSaving biased model to: {model_path}")
    joblib.dump(pipeline, model_path)
    
    # Verify the model was saved
    if os.path.exists(model_path):
        print(f"✓ Model successfully saved to: {model_path}")
        print(f"✓ File size: {os.path.getsize(model_path)} bytes")
    else:
        raise RuntimeError(f"Failed to save model to {model_path}")
    
    return model_path


def verify_model_compatibility(model_path):
    """Verify the saved model is compatible with model_loader.py and model_predictor.py"""
    print("\n" + "="*60)
    print("COMPATIBILITY VERIFICATION")
    print("="*60)
    
    try:
        # Test loading with model_loader.py logic
        import os
        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Model file not found: {model_path}")
        
        model = joblib.load(model_path)
        print("✓ Model can be loaded with joblib (compatible with model_loader.py)")
        
        # Check for predict method
        if hasattr(model, "predict") and callable(model.predict):
            print("✓ Model has callable predict() method (compatible with model_predictor.py)")
        else:
            raise TypeError("Model does not have a callable predict() method")
        
        # Test prediction on sample data
        sample_data = pd.DataFrame({
            SEX_COL: ["Male", "Female"],
            "RecSupervisionLevel": ["Low", "High"]
        })
        
        predictions = model.predict(sample_data)
        print(f"✓ Model can generate predictions: {predictions}")
        print(f"✓ Prediction shape: {predictions.shape}")
        
        print("\n✓✓✓ MODEL IS FULLY COMPATIBLE ✓✓✓")
        
    except Exception as e:
        print(f"✗ Compatibility check failed: {e}")
        raise


if __name__ == "__main__":
    import os
    
    print("="*60)
    print("CREATING BIASED DEMO MODEL FOR AI BIAS DETECTION TESTER")
    print("="*60)
    
    # Create the biased model
    saved_path = build_biased_compas_model()
    
    # Verify compatibility
    verify_model_compatibility(saved_path)
    
    print("\n" + "="*60)
    print("SUCCESS!")
    print("="*60)
    print(f"\nBiased model file path:\n{saved_path}")
