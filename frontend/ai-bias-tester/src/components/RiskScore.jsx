const LEVEL_STYLES = {
  LOW: { color: "var(--risk-low)", soft: "var(--risk-low-soft)" },
  MODERATE: { color: "var(--risk-moderate)", soft: "var(--risk-moderate-soft)" },
  MEDIUM: { color: "var(--risk-medium)", soft: "var(--risk-medium-soft)" },
  HIGH: { color: "var(--risk-high)", soft: "var(--risk-high-soft)" },
  CRITICAL: { color: "var(--risk-critical)", soft: "var(--risk-critical-soft)" },
};

function styleForLevel(level) {
  const key = (level || "").toUpperCase();
  return LEVEL_STYLES[key] || LEVEL_STYLES.MODERATE;
}

const LEVEL_COPY = {
  LOW: "Minimal disparity detected across the tested groups. Continue routine monitoring.",
  MODERATE: "Some disparity present. Review the fairness metrics below before deployment.",
  MEDIUM: "Meaningful disparity detected. Mitigation is recommended before deployment.",
  HIGH: "Significant disparity detected. This model carries substantial fairness risk.",
  CRITICAL: "Severe disparity detected. Deployment is not recommended without remediation.",
};

export default function RiskScore({ riskScore, riskLevel }) {
  const score = Math.max(0, Math.min(100, Number(riskScore) || 0));
  const { color, soft } = styleForLevel(riskLevel);

  const radius = 80;
  const halfCircumference = Math.PI * radius;
  const filled = (score / 100) * halfCircumference;
  const remainder = halfCircumference - filled;

  return (
    <div className="card risk-card">
      <div className="risk-gauge">
        <svg viewBox="0 0 200 118" width="200" height="118">
          <path
            d="M 20 100 A 80 80 0 0 1 180 100"
            fill="none"
            stroke="var(--border-strong)"
            strokeWidth="14"
            strokeLinecap="round"
          />
          <path
            d="M 20 100 A 80 80 0 0 1 180 100"
            fill="none"
            stroke={color}
            strokeWidth="14"
            strokeLinecap="round"
            strokeDasharray={`${filled} ${remainder}`}
          />
        </svg>
        <div className="risk-gauge__value" style={{ color }}>
          {score.toFixed(1)}
        </div>
        <div className="risk-gauge__scale">
          <span>0</span>
          <span>100</span>
        </div>
      </div>

      <div className="risk-summary">
        <span
          className="risk-summary__level"
          style={{ color, borderColor: color, background: soft }}
        >
          {(riskLevel || "unknown").toString().toLowerCase()} risk
        </span>
        <p>
          {LEVEL_COPY[(riskLevel || "").toUpperCase()] ||
            "Risk level reported by the audit backend."}
        </p>
      </div>
    </div>
  );
}
