from fairlearn.metrics import MetricFrame
from sklearn.metrics import accuracy_score


def detect_bias(y_test, y_pred, sensitive_features, bias_threshold=0.10):
    """
    Detect bias in model predictions across sensitive demographic groups.
    
    Parameters:
    -----------
    y_test : array-like
        Ground truth labels.
    y_pred : array-like
        Model predictions.
    sensitive_features : array-like or Series
        Sensitive feature (e.g., Ethnic_Code_Text).
    bias_threshold : float
        Threshold for bias detection (default 10% = 0.10).
    
    Returns:
    --------
    dict with keys:
        - "bias_detected": bool
        - "threshold": float
        - "metrics_frame": MetricFrame
        - "gaps": dict of metric gaps by group
        - "disparities": list of (metric, gap, max_group, min_group) with gap > threshold
    """
    
    def selection_rate(y_true, y_pred):
        return y_pred.mean()
    
    def tpr(y_true, y_pred):
        if (y_true == 1).sum() == 0:
            return 0.0
        return ((y_true == 1) & (y_pred == 1)).sum() / (y_true == 1).sum()
    
    def fpr(y_true, y_pred):
        if (y_true == 0).sum() == 0:
            return 0.0
        return ((y_true == 0) & (y_pred == 1)).sum() / (y_true == 0).sum()
    
    metric_frame = MetricFrame(
        metrics={
            "accuracy": accuracy_score,
            "selection_rate": selection_rate,
            "tpr": tpr,
            "fpr": fpr,
        },
        y_true=y_test,
        y_pred=y_pred,
        sensitive_features=sensitive_features,
    )
    
    gaps = {}
    disparities = []
    bias_detected = False
    
    for metric_name in ["accuracy", "selection_rate", "tpr", "fpr"]:
        metric_values = metric_frame.by_group[metric_name]
        max_val = metric_values.max()
        min_val = metric_values.min()
        gap = max_val - min_val
        gaps[metric_name] = gap
        
        if gap > bias_threshold:
            bias_detected = True
            max_group = metric_values.idxmax()
            min_group = metric_values.idxmin()
            disparities.append({
                "metric": metric_name,
                "gap": gap,
                "max_group": max_group,
                "max_value": max_val,
                "min_group": min_group,
                "min_value": min_val,
            })
    
    return {
        "bias_detected": bias_detected,
        "threshold": bias_threshold,
        "metrics_frame": metric_frame,
        "gaps": gaps,
        "disparities": disparities,
    }
