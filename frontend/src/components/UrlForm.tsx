import React from "react";

type Props = {
  url: string;
  onUrlChange: (value: string) => void;
  onSubmit: () => void;
  loading: boolean;
};

export function UrlForm({ url, onUrlChange, onSubmit, loading }: Props) {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit();
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="label-row">
        <label htmlFor="url-input">Paste a link to shorten</label>
      </div>
      <div className="input-row">
        <input
          id="url-input"
          type="url"
          placeholder="https://example.com/article/123"
          value={url}
          onChange={(e) => onUrlChange(e.target.value)}
          required
          autoFocus
          pattern="https?://.*"
        />
        <button className="btn-primary" type="submit" disabled={loading}>
          {loading ? "Working..." : "Shorten"}
        </button>
      </div>
      <p className="status">
        {loading ? (
          <>
            <span className="dots">
              <span />
              <span />
              <span />
            </span>
            Creating your link...
          </>
        ) : (
          <>The service returns a 302 redirect and tracks clicks for each short code.</>
        )}
      </p>
    </form>
  );
}
