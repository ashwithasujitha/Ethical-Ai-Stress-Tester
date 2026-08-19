import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.preprocessing import StandardScaler
from fairlearn.metrics import MetricFrame
from model_loader import load_model
import warnings
warnings.filterwarnings('ignore')

df = pd.read_csv("C:\\Users\\hp\\Downloads\\archive\\compas-scores-raw.csv")

print("="*50)
print("STEP 1: Data Loaded Successfully!")
print("="*50)
print(f"Total rows: {len(df)}")
print(f"Total columns: {len(df.columns)}")

print("\n" + "="*50)
print("STEP 2: Checking Column Names")
print("="*50)
print("All columns:")
print(df.columns.tolist())



print("\n" + "="*50)
print("STEP 3: Identifying Correct Columns")
print("="*50)


race_col = 'Ethnic_Code_Text'  
sex_col = 'Sex_Code_Text'
recid_col = 'DecileScore' 

print(f"Using target column: {recid_col}")
print(f"Using race column: {race_col}")


print("\n" + "="*50)
print("STEP 4: Cleaning Data")
print("="*50)


columns_to_use = [race_col, sex_col, recid_col]


useful_cols = ['Age', 'PriorsCount', 'ChargeDegree']
for col in useful_cols:
    if col in df.columns:
        columns_to_use.append(col)


if 'RecSupervisionLevel' in df.columns:
    columns_to_use.append('RecSupervisionLevel')

print(f"Using columns: {columns_to_use}")


df_clean = df[columns_to_use].copy()


df_clean = df_clean.dropna()
print(f"Rows after dropping missing values: {len(df_clean)}")


print("\n" + "="*50)
print("STEP 5: Converting Target to Binary")
print("="*50)

median_score = df_clean[recid_col].median()
y = (df_clean[recid_col] > median_score).astype(int)
print(f"DecileScore median: {median_score}")
print(f"Target distribution (high risk vs low risk):\n{y.value_counts()}")
print(f"Final target classes: {y.unique()}")


print("\n" + "="*50)
print("STEP 6: Preparing Features")
print("="*50)

# Drop target from features
X = df_clean.drop(columns=[recid_col])


race_feature = df_clean[race_col].reset_index(drop=True)

print(f"Feature columns before encoding: {X.columns.tolist()}")


print("\n" + "="*50)
print("STEP 7: Encoding Categorical Data")
print("="*50)

categorical_cols = X.select_dtypes(include=['object']).columns.tolist()
print(f"Categorical columns: {categorical_cols}")

X_encoded = pd.get_dummies(X, columns=categorical_cols, drop_first=True)
print(f"X shape after encoding: {X_encoded.shape}")


print("\n" + "="*50)
print("STEP 8: Train-Test Split")
print("="*50)

X_train, X_test, y_train, y_test = train_test_split(
    X_encoded, y, test_size=0.3, random_state=42
)

train_indices = X_train.index
test_indices = X_test.index

print(f"Training samples: {len(X_train)}")
print(f"Test samples: {len(X_test)}")

print("\n" + "="*50)
print("STEP 9: Scaling Data")
print("="*50)

scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

print(f"X scaled shape: {X_train_scaled.shape}")


print("\n" + "="*50)
print("STEP 10: Training Model")
print("="*50)

model = LogisticRegression(max_iter=1000, solver='lbfgs')
model.fit(X_train_scaled, y_train)

y_pred = model.predict(X_test_scaled)

overall_accuracy = accuracy_score(y_test, y_pred)
print(f"Overall Accuracy: {overall_accuracy:.3f}")

# ============================================
# STEP 11: CHECK FOR BIAS
# ============================================
print("\n" + "="*50)
print("STEP 11: Bias Detection Results")
print("="*50)

# Get race for test set using the saved indices
X_test_race = race_feature.iloc[test_indices]

print(f"Race groups in test data: {X_test_race.unique()}")

# Create metric frame
metric_frame = MetricFrame(
    metrics={
        "accuracy": accuracy_score,
        "selection_rate": lambda y_true, y_pred: y_pred.mean()
    },
    y_true=y_test,
    y_pred=y_pred,
    sensitive_features=X_test_race
)

print("\nBias Metrics by Race Group:")
print(metric_frame.by_group)

# Calculate disparity
accuracy_values = metric_frame.by_group['accuracy']
if len(accuracy_values) > 1:
    max_accuracy = accuracy_values.max()
    min_accuracy = accuracy_values.min()
    disparity = (max_accuracy - min_accuracy) * 100

    print(f"\nHighest accuracy group: {max_accuracy:.3f}")
    print(f"Lowest accuracy group: {min_accuracy:.3f}")
    print(f"Disparity: {disparity:.1f}%")

    if disparity > 10:
        print("⚠️ SIGNIFICANT BIAS DETECTED! Disparity > 10%")
        print("This model is NOT SAFE for deployment.")
    else:
        print("✅ Bias within acceptable range (< 10%)")
        print("This model is SAFE for deployment.")
    
    # Show which groups are most affected
    print("\nDetailed breakdown:")
    for group in accuracy_values.index:
        print(f"  {group}: {accuracy_values[group]:.3f}")
    
    # Show selection rates (how often each group is predicted as high risk)
    selection_rates = metric_frame.by_group['selection_rate']
    print("\nSelection Rates (Predicted High Risk):")
    for group in selection_rates.index:
        print(f"  {group}: {selection_rates[group]:.3f}")
else:
    print("Not enough race groups to analyze disparity.")

print("\n" + "="*50)
print("STEP 12: Model Loader Test")
print("="*50)

try:
    model_path = input("Enter model path: ").strip()
    if model_path:
        model = load_model(model_path)
        print("Model loaded successfully!")
        print(f"Model type: {type(model).__name__}")
    else:
        print("No model path provided. Skipping model loader test.")
except Exception as exc:
    print(f"Model loader error: {exc}")

print("\n" + "="*50)
print("PROJECT COMPLETE!")
print("="*50)