import RiskScore from "./RiskScore.jsx";
import FairnessMetrics from "./FairnessMetrics.jsx";
import AffectedGroups from "./AffectedGroups.jsx";

const METRIC_LABELS = {
  accuracy_gap: "Accuracy",
  selection_rate_gap: "Selection Rate",
  tpr_gap: "TPR",
  fpr_gap: "FPR",
};

export default function AuditResults({ result }) {
  const isModelMode =
    result.mode === "model_dataset" || result.mode === "model_and_dataset" || result.mode === "model_only";
  const biasDetected = Boolean(result.bias_detected);

  // Ensure all metric keys exist with default 0 values
  const metrics = {
    accuracy_gap: result.accuracy_gap ?? 0,
    selection_rate_gap: result.selection_rate_gap ?? 0,
    tpr_gap: result.tpr_gap ?? 0,
    fpr_gap: result.fpr_gap ?? 0,
  };

  const matrixRows = [
    ["accuracy_gap", METRIC_LABELS.accuracy_gap],
    ["selection_rate_gap", METRIC_LABELS.selection_rate_gap],
    ["tpr_gap", METRIC_LABELS.tpr_gap],
    ["fpr_gap", METRIC_LABELS.fpr_gap],
  ].map(([key, label]) => {
    const value = Number(metrics[key]) || 0;
    const percent = value * 100;
    const status = percent > 20 ? "High" : percent > 10 ? "Moderate" : "Low";

    return {
      key,
      label,
      value: percent,
      status,
    };
  });

  return (
    <div className="results">
      <div className="results__header">
        <h2 className="results__title">Audit Results</h2>
        <span
          className={
            biasDetected ? "status-pill status-pill--bias" : "status-pill status-pill--clear"
          }
        >
          <span className="status-dot" />
          {biasDetected ? "Bias Detected" : "No Bias Detected"}
        </span>
      </div>

      {isModelMode && (
        <RiskScore riskScore={result.risk_score} riskLevel={result.risk_level} />
      )}

      <FairnessMetrics metrics={metrics} mode={result.mode} />

      <div className="card score-matrix-card">
        <h3 className="score-matrix-card__title">Fairness Score Matrix</h3>
        <div className="score-matrix">
          <div className="score-matrix__header">
            <span>Metric</span>
            <span>Score</span>
            <span>Status</span>
          </div>

          {matrixRows.map((row) => (
            <div className="score-matrix__row" key={row.key}>
              <span className="score-matrix__metric">{row.label}</span>
              <span className="score-matrix__value">{row.value.toFixed(2)}%</span>
              <span className={`score-matrix__status score-matrix__status--${row.status.toLowerCase()}`}>
                {row.status}
              </span>
            </div>
          ))}
        </div>
      </div>

      <AffectedGroups groups={result.affected_groups} />

      {isModelMode ? (
        <div className="info-grid">
          <div className="card info-card">
            <h3 className="info-card__title">Model Information</h3>
            <div className="info-stat">
              <span className="info-stat__label">Model type</span>
              <span className="info-stat__value">{result.model_type || "—"}</span>
            </div>
            <div className="info-stat">
              <span className="info-stat__label">Test samples</span>
              <span className="info-stat__value">
                {result.test_samples != null ? result.test_samples.toLocaleString() : "—"}
              </span>
            </div>
          </div>

          <div className="card info-card">
            <h3 className="info-card__title">Explainable AI — Top Features</h3>
            {Array.isArray(result.top_features) && result.top_features.length > 0 ? (
              <ul className="feature-list">
               {result.top_features.map((item, idx) => (
  <li key={`${item.feature}-${idx}`}>
    <span className="feature-list__rank">{idx + 1}</span>

    <span className="feature-list__name">
      {typeof item === "object" ? item.feature : item}
    </span>

    {typeof item === "object" && item.importance != null && (
      <span className="feature-list__importance">
        Importance: {Number(item.importance).toFixed(6)}
      </span>
    )}
  </li>
))}
              </ul>
            ) : (
              <p className="empty-note">No feature importance data was returned.</p>
            )}
          </div>
        </div>
      ) : (
        <div className="card info-card">
          <h3 className="info-card__title">Dataset Information</h3>
          <div className="info-stat">
            <span className="info-stat__label">Dataset samples</span>
            <span className="info-stat__value">
              {result.dataset_samples != null ? result.dataset_samples.toLocaleString() : "—"}
            </span>
          </div>
          <div className="info-stat">
            <span className="info-stat__label">Target column</span>
            <span className="info-stat__value">{result.target_column || "—"}</span>
          </div>
        </div>
      )}
    </div>
  );
}
