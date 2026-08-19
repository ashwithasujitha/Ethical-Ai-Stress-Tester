import joblib
import sys
sys.path.insert(0, r"C:\Users\hp\Desktop\Ai Bias Detection")
from model_loader import load_model
from model_predictor import predict
import pandas as pd

print("="*60)
print("VERIFYING BIASED MODEL COMPATIBILITY")
print("="*60)

# Load the biased model using existing model_loader
biased_model = load_model(r"C:\Users\hp\Desktop\Ai Bias Detection\biased_model.pkl")
print("\n✓ Biased model loaded successfully with model_loader.py")

# Prepare test data with proper structure
X_test = pd.DataFrame({
    'Sex_Code_Text': ['Male', 'Female', 'Male', 'Female'],
    'RecSupervisionLevel': [1, 2, 1, 3]
})

# Generate predictions using existing model_predictor
y_pred = predict(biased_model, X_test)
print(f"✓ Model generates predictions using model_predictor.py")
print(f"✓ Predictions: {y_pred}")
print(f"✓ Predictions shape: {y_pred.shape}")

print("\n" + "="*60)
print("BIASED MODEL VERIFICATION COMPLETE")
print("="*60)
print(f"\n✓✓✓ Model is FULLY COMPATIBLE with your existing code ✓✓✓")
print(f"\nExact saved file path:\nC:\\Users\\hp\\Desktop\\Ai Bias Detection\\biased_model.pkl")
