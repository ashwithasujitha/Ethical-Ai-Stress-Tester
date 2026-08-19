from model_loader import load_model

model = load_model("model.pkl")

print("Model loaded successfully!")
print("Model type:", type(model).__name__)