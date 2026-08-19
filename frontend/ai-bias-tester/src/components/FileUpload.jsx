import { useRef, useState } from "react";

export default function FileUpload({
  title,
  hint,
  accept,
  file,
  onSelect,
  onRemove,
  inputId,
}) {
  const inputRef = useRef(null);
  const [dragActive, setDragActive] = useState(false);

  function handleFiles(fileList) {
    if (fileList && fileList.length > 0) {
      onSelect(fileList[0]);
    }
  }

  function handleDrop(e) {
    e.preventDefault();
    setDragActive(false);
    handleFiles(e.dataTransfer.files);
  }

  return (
    <div className="card upload-card">
      <div className="upload-card__top">
        <span className="upload-card__title">{title}</span>
      </div>
      <p className="upload-card__hint">{hint}</p>

      {file ? (
        <div className="file-chip">
          <span className="file-chip__name" title={file.name}>
            {file.name}
          </span>
          <button
            type="button"
            className="file-chip__remove"
            onClick={onRemove}
            aria-label={`Remove ${file.name}`}
          >
            ✕
          </button>
        </div>
      ) : (
        <label
          htmlFor={inputId}
          className={dragActive ? "dropzone dropzone--active" : "dropzone"}
          onDragOver={(e) => {
            e.preventDefault();
            setDragActive(true);
          }}
          onDragLeave={() => setDragActive(false)}
          onDrop={handleDrop}
        >
          <span className="dropzone__icon">＋</span>
          <span className="dropzone__text">
            <strong>Click to upload</strong> or drag file here
          </span>
          <input
            ref={inputRef}
            id={inputId}
            type="file"
            accept={accept}
            onChange={(e) => handleFiles(e.target.files)}
          />
        </label>
      )}
    </div>
  );
}
