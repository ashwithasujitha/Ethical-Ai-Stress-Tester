import FileUpload from "./FileUpload.jsx";

export default function AuditForm({
  modelFile,
  datasetFile,
  sensitiveFeature,
  onModelSelect,
  onModelRemove,
  onDatasetSelect,
  onDatasetRemove,
  onSensitiveFeatureChange,
  onSubmit,
  isLoading,
}) {
  const modelOnlyWarning = Boolean(modelFile) && !datasetFile;

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        onSubmit();
      }}
    >
      <div className="upload-grid">
        <FileUpload
          inputId="model-upload"
          title="Trained Model"
          hint="Optional — .pkl or .joblib"
          accept=".pkl,.joblib"
          file={modelFile}
          onSelect={onModelSelect}
          onRemove={onModelRemove}
        />
        <FileUpload
          inputId="dataset-upload"
          title="Test Dataset"
          hint="Optional — .csv"
          accept=".csv"
          file={datasetFile}
          onSelect={onDatasetSelect}
          onRemove={onDatasetRemove}
        />
      </div>

      {modelOnlyWarning && (
        <div className="notice notice--info">
          <span className="notice__icon">i</span>
          <span>
            A test dataset is required for model fairness testing. Upload a
            CSV dataset alongside the model to run the full audit.
          </span>
        </div>
      )}

      <div className="card form-card">
        <div className="field">
          <label htmlFor="sensitive-feature">Sensitive Feature</label>
          <input
            id="sensitive-feature"
            type="text"
            placeholder="e.g. Ethnic_Code_Text"
            value={sensitiveFeature}
            onChange={(e) => onSensitiveFeatureChange(e.target.value)}
          />
          <p className="field__hint">
            The column in your dataset to test for disparate impact across
            groups.
          </p>
        </div>

        <button type="submit" className="run-button" disabled={isLoading}>
          {isLoading ? (
            <>
              <span className="spinner" />
              Running audit…
            </>
          ) : (
            "Run AI Audit"
          )}
        </button>
      </div>
    </form>
  );
}
