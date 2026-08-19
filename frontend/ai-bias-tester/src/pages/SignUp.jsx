import { useState } from "react";

export default function SignUp({ onSignUpSuccess, onSwitchToSignIn }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (!name || !email || !password || !confirmPassword) {
      setError("Please fill in all fields.");
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    if (password.length < 8) {
      setError("Password must be at least 8 characters long.");
      return;
    }

    setIsLoading(true);

    try {
      // Store credentials in localStorage
      localStorage.setItem("userEmail", email);
      localStorage.setItem("userPassword", password);
      localStorage.setItem("userName", name);

      // TODO: Replace with actual API call
      console.log("Sign up attempt:", { name, email, password });

      // Simulated successful signup
      setTimeout(() => {
        onSignUpSuccess({ name, email });
        setIsLoading(false);
      }, 500);
    } catch (err) {
      setError(err.message || "Sign up failed. Please try again.");
      setIsLoading(false);
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="auth-title">Sign Up</h1>
          <p className="auth-subtitle">Create your AI Bias Detection account</p>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="name" className="form-label">
              Full Name
            </label>
            <input
              id="name"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="John Doe"
              className="form-input"
              disabled={isLoading}
            />
          </div>

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
            <p className="form-hint">At least 8 characters</p>
          </div>

          <div className="form-group">
            <label htmlFor="confirm-password" className="form-label">
              Confirm Password
            </label>
            <input
              id="confirm-password"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
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
            {isLoading ? "Creating account..." : "Sign Up"}
          </button>
        </form>

        <div className="auth-footer">
          <p className="auth-footer__text">
            Already have an account?{" "}
            <button
              type="button"
              onClick={onSwitchToSignIn}
              className="auth-link"
            >
              Sign in here
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}
