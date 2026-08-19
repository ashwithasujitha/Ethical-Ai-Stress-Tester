def calculate_risk_score(gaps, max_score=100):
    """Calculate a custom project risk score from fairness gaps using equal weighting."""
    if not isinstance(gaps, dict):
        raise TypeError("gaps must be provided as a dictionary.")

    required_metrics = ["accuracy", "selection_rate", "tpr", "fpr"]
    missing = [m for m in required_metrics if m not in gaps]
    if missing:
        raise ValueError(f"Missing gap values for metrics: {missing}")

    normalized_values = []
    for metric_name in required_metrics:
        gap_value = float(gaps[metric_name])
        if gap_value < 0:
            raise ValueError(f"Gap for {metric_name} must be non-negative.")
        normalized_values.append(gap_value)

    average_gap = sum(normalized_values) / len(normalized_values)
    risk_score = min(max_score, average_gap * max_score)
    risk_score = round(risk_score, 2)

    if risk_score <= 20:
        level = "LOW"
    elif risk_score <= 40:
        level = "MODERATE"
    elif risk_score <= 60:
        level = "MEDIUM"
    elif risk_score <= 80:
        level = "HIGH"
    else:
        level = "CRITICAL"

    biggest_contributors = sorted(
        [(metric_name, gaps[metric_name]) for metric_name in required_metrics],
        key=lambda item: item[1],
        reverse=True,
    )[:2]

    return {
        "risk_score": risk_score,
        "risk_level": level,
        "biggest_contributors": biggest_contributors,
        "average_gap": round(average_gap, 4),
    }


def format_risk_report(gaps):
    """Create a human-readable risk report using the fairness gaps."""
    result = calculate_risk_score(gaps)

    print("\n" + "=" * 60)
    print("CUSTOM PROJECT RISK SCORE — NOT A UNIVERSAL FAIRNESS STANDARD")
    print("=" * 60)
    print("Metric gaps used:")
    for metric_name in ["accuracy", "selection_rate", "tpr", "fpr"]:
        print(f"  - {metric_name}: {gaps[metric_name]:.4f} ({gaps[metric_name] * 100:.2f}%)")

    print(f"\nRisk Score: {result['risk_score']:.2f} / 100")
    print(f"Risk Level: {result['risk_level']}")
    print("Top contributors to risk:")
    for metric_name, gap_value in result["biggest_contributors"]:
        print(f"  - {metric_name}: {gap_value:.4f} ({gap_value * 100:.2f}%)")

    if result["risk_score"] <= 20:
        bias_risk = "LOW"
    elif result["risk_score"] <= 40:
        bias_risk = "MODERATE"
    elif result["risk_score"] <= 60:
        bias_risk = "MEDIUM"
    elif result["risk_score"] <= 80:
        bias_risk = "HIGH"
    else:
        bias_risk = "CRITICAL"

    print(f"BIAS RISK: {bias_risk}")
    return result
