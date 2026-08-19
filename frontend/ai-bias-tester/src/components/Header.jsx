export default function Header({ backendStatus, user, onLogout }) {
  const badgeClass =
    backendStatus === "online"
      ? "app-header__badge app-header__badge--online"
      : backendStatus === "offline"
      ? "app-header__badge app-header__badge--offline"
      : "app-header__badge";

  const badgeText =
    backendStatus === "online"
      ? "● backend online"
      : backendStatus === "offline"
      ? "● backend offline"
      : "checking backend…";

  return (
    <header className="app-header">
      <div>
        <div className="app-header__eyebrow">
          <span className="app-header__eyebrow-dot" />
          audit console
        </div>
        <h1>AI Bias Detection Tester</h1>
        <p>Pre-deployment AI fairness and explainability audit</p>
      </div>
      <div className="app-header__right">
        {user && (
          <div className="app-header__user-section">
            <span className="app-header__user-email">{user.email}</span>
            <button onClick={onLogout} className="app-header__logout-btn">
              Sign out
            </button>
          </div>
        )}
        <span className={badgeClass}>{badgeText}</span>
      </div>
    </header>
  );
}
