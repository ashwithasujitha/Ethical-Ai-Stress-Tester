import axios from "axios";

const BASE_URL = "http://localhost:8000";

const client = axios.create({
  baseURL: BASE_URL,
});

/**
 * Converts any error thrown by axios into a short, friendly message
 * that can be shown directly in the UI.
 */
function toFriendlyMessage(error) {
  if (error.code === "ERR_NETWORK" || !error.response) {
    return "Cannot connect to the AI Bias Tester backend. Please start FastAPI on http://localhost:8000.";
  }

  const { status, data } = error.response;

  const backendDetail =
    (data && (data.detail || data.message)) ||
    (Array.isArray(data?.detail) ? data.detail[0]?.msg : null);

  if (status === 400) {
    return backendDetail || "The audit request was invalid. Please check your uploaded files.";
  }
  if (status === 422) {
    return backendDetail || "Please upload a valid model (.pkl/.joblib) and/or dataset (.csv).";
  }
  if (status === 500) {
    return backendDetail || "The AI Bias Tester backend ran into an error while processing this audit.";
  }

  return backendDetail || `Request failed with status ${status}.`;
}

export async function checkHealth() {
  try {
    const res = await client.get("/api/health");
    return { ok: true, data: res.data };
  } catch (error) {
    return { ok: false, error: toFriendlyMessage(error) };
  }
}

export async function runAudit({ modelFile, datasetFile, sensitiveFeature }) {
  const formData = new FormData();

  if (modelFile) formData.append("model", modelFile);
  if (datasetFile) formData.append("dataset", datasetFile);
  if (sensitiveFeature) formData.append("sensitive_feature", sensitiveFeature);

  try {
    const res = await client.post("/api/audit", formData);
    return { ok: true, data: res.data };
  } catch (error) {
    return { ok: false, error: toFriendlyMessage(error) };
  }
}
