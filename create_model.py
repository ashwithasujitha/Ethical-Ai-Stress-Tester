from sklearn.datasets import load_iris
from sklearn.linear_model import LogisticRegression
import joblib

# Load sample dataset
data = load_iris()

X = data.data
y = data.target

# Train model
model = LogisticRegression(max_iter=1000)
model.fit(X, y)

# Save trained model
joblib.dump(model, "model.pkl")

print("Model saved successfully as model.pkl")