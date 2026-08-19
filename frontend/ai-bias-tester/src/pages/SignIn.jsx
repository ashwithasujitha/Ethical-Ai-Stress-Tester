import { useState, useEffect } from "react";

export default function SignIn({ onSignInSuccess, onSwitchToSignUp }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  // Load stored credentials on component mount
  useEffect(() => {
    const storedEmail = localStorage.getItem("userEmail");
    if (storedEmail) {
      setEmail(storedEmail);
    }
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (!email || !password) {
      setError("Please fill in all fields.");
      return;
    }

    setIsLoading(true);

    try {
      // Store credentials in localStorage
      localStorage.setItem("userEmail", email);
      localStorage.setItem("userPassword", password);

      // TODO: Replace with actual API call
      console.log("Sign in attempt:", { email, password });

      // Simulated successful login
      setTimeout(() => {
        onSignInSuccess({ email });
        setIsLoading(false);
      }, 500);
    } catch (err) {
      setError(err.message || "Sign in failed. Please try again.");
      setIsLoading(false);
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="auth-title">Sign In</h1>
          <p className="auth-subtitle">Access your AI Bias Detection account</p>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="email" className="form-label">
              Email Address
            </label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
              className="form-input"
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              className="form-input"
              disabled={isLoading}
            />
          </div>

          {error && (
            <div className="form-error">
              <span className="form-error__icon">!</span>
              <span>{error}</span>
            </div>
          )}

          <button type="submit" className="btn btn--primary btn--full" disabled={isLoading}>
            {isLoading ? "Signing in..." : "Sign In"}
          </button>
        </form>

        <div className="auth-footer">
          <p className="auth-footer__text">
            Don't have an account?{" "}
            <button
              type="button"
              onClick={onSwitchToSignUp}
              className="auth-link"
            >
              Sign up here
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}
