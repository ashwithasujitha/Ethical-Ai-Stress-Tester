import { useEffect, useState } from "react";
import Header from "./components/Header.jsx";
import AuditForm from "./components/AuditForm.jsx";
import AuditResults from "./components/AuditResults.jsx";
import SignIn from "./pages/SignIn.jsx";
import SignUp from "./pages/SignUp.jsx";
import { checkHealth, runAudit } from "./services/api.js";

export default function App() {
  const [currentPage, setCurrentPage] = useState("signin");
  const [user, setUser] = useState(null);

  const [modelFile, setModelFile] = useState(null);
  const [datasetFile, setDatasetFile] = useState(null);
  const [sensitiveFeature, setSensitiveFeature] = useState("");

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);
  const [backendStatus, setBackendStatus] = useState("checking");

  useEffect(() => {
    let cancelled = false;
    checkHealth().then(({ ok }) => {
      if (!cancelled) setBackendStatus(ok ? "online" : "offline");
    });
    
    // Check if user is already logged in from localStorage
    const storedEmail = localStorage.getItem("userEmail");
    if (storedEmail && !cancelled) {
      setUser({ email: storedEmail });
    }
    
    return () => {
      cancelled = true;
    };
  }, []);

  async function handleSubmit() {
    setError(null);

    if (!modelFile && !datasetFile) {
      setError("Please upload a model, a dataset, or both before running an audit.");
      return;
    }

    setIsLoading(true);
    setResult(null);

    const response = await runAudit({ modelFile, datasetFile, sensitiveFeature });

    setIsLoading(false);

    if (!response.ok) {
      setError(response.error);
      return;
    }

    setResult(response.data);
    if (backendStatus !== "online") setBackendStatus("online");
  }

  function handleLogout() {
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userPassword");
    localStorage.removeItem("userName");
    setUser(null);
  }

  return (
    <>
      {!user ? (
        // Show auth pages when not logged in
        <>
          {currentPage === "signin" ? (
            <SignIn
              onSignInSuccess={(userData) => {
                setUser(userData);
                setCurrentPage("audit");
              }}
              onSwitchToSignUp={() => setCurrentPage("signup")}
            />
          ) : (
            <SignUp
              onSignUpSuccess={(userData) => {
                setUser(userData);
                setCurrentPage("audit");
              }}
              onSwitchToSignIn={() => setCurrentPage("signin")}
            />
          )}
        </>
      ) : (
        // Show main app when logged in
        <div className="app-shell">
          <Header backendStatus={backendStatus} user={user} onLogout={handleLogout} />

          <AuditForm
            modelFile={modelFile}
            datasetFile={datasetFile}
            sensitiveFeature={sensitiveFeature}
            onModelSelect={(file) => {
              setModelFile(file);
              setError(null);
            }}
            onModelRemove={() => setModelFile(null)}
            onDatasetSelect={(file) => {
              setDatasetFile(file);
              setError(null);
            }}
            onDatasetRemove={() => setDatasetFile(null)}
            onSensitiveFeatureChange={setSensitiveFeature}
            onSubmit={handleSubmit}
            isLoading={isLoading}
          />

          {error && (
            <div className="notice notice--error">
              <span className="notice__icon">!</span>
              <span>{error}</span>
            </div>
          )}

          {result && <AuditResults result={result} />}

          <p className="footer-note">
            AI Bias Detection Tester — results are generated live by your FastAPI backend at
            http://localhost:8000
          </p>
        </div>
      )}
    </>
  );
}
