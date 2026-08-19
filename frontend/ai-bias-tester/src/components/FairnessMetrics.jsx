const METRIC_LABELS = {
  accuracy_gap: "Accuracy Gap",
  selection_rate_gap: "Selection Rate Gap",
  tpr_gap: "TPR Gap",
  fpr_gap: "FPR Gap",
};

const METRIC_ORDER = [
  "accuracy_gap",
  "selection_rate_gap",
  "tpr_gap",
  "fpr_gap",
];

const DATASET_ONLY_METRICS = ["selection_rate_gap"];

function toPercent(value) {
  const num = Number(value) || 0;
  return num * 100;
}

export default function FairnessMetrics({ metrics = {}, mode }) {
  const isDatasetOnly = mode === "dataset_only";

  const metricsToShow = isDatasetOnly
    ? DATASET_ONLY_METRICS
    : METRIC_ORDER;

  const title = isDatasetOnly
    ? "Dataset Bias Metrics"
    : "Fairness Metrics";

  return (
    <div className="card metrics-card">
      <h3 className="metrics-card__title">{title}</h3>

      {metricsToShow.map((key) => {
        const value = metrics[key];

        if (value === undefined || value === null) {
          return null;
        }

        const pct = toPercent(value);
        const barWidth = Math.max(0, Math.min(100, pct));

        return (
          <div className="metric-row" key={key}>
            <span className="metric-row__label">
              {METRIC_LABELS[key]}
            </span>

            <span
              className="metric-row__bar-track"
              style={{
                display: "block",
                width: "100%",
                height: "8px",
                background: "#e2e4de",
                borderRadius: "999px",
                overflow: "hidden",
              }}
            >
              <span
                className="metric-row__bar-fill"
                style={{
                  display: "block",
                  width: `${barWidth}%`,
                  height: "100%",
                  minWidth: barWidth > 0 ? "2px" : "0",
                  background: "#2f6f5e",
                  borderRadius: "999px",
                  transition: "width 0.5s ease",
                }}
              />
            </span>

            <span className="metric-row__value">
              {pct.toFixed(2)}%
            </span>
          </div>
        );
      })}

      {isDatasetOnly && (
        <>
          {["accuracy_gap", "tpr_gap", "fpr_gap"].map((key) => (
            <div className="metric-row" key={key}>
              <span className="metric-row__label">
                {METRIC_LABELS[key]}
              </span>

              <span
                className="metric-row__bar-track"
                style={{
                  display: "block",
                  width: "100%",
                  height: "8px",
                  background: "#e2e4de",
                  borderRadius: "999px",
                  overflow: "hidden",
                }}
              >
                <span
                  className="metric-row__bar-fill"
                  style={{
                    display: "block",
                    width: "0%",
                    height: "100%",
                    background: "#2f6f5e",
                  }}
                />
              </span>

              <span className="metric-row__value metric-row__value--unavailable">
                N/A
              </span>
            </div>
          ))}
        </>
      )}
    </div>
  );
}