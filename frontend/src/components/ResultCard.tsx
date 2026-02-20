import React from "react";
import { ShortLink } from "../types";

type Props = {
  link: ShortLink | null;
  onCopy: (value: string) => void;
};

export function ResultCard({ link, onCopy }: Props) {
  if (!link) {
    return (
      <div className="empty">
        Create your first link to see it here.
      </div>
    );
  }

  const { shortUrl, originalUrl, clickCount, createdDate } = link;

  return (
    <div className="result">
      <div className="chips">
        <span className="pill">
          Created {new Date(createdDate).toLocaleDateString()}
        </span>
        <span className="pill">
          Clicks <strong>{clickCount}</strong>
        </span>
      </div>
      <div className="result-actions">
        <button className="btn-primary" onClick={() => onCopy(shortUrl)}>
          Copy short link
        </button>
        <a className="btn-ghost" href={shortUrl} target="_blank" rel="noreferrer">
          Open redirect
        </a>
      </div>
      <div className="history-item">
        <div className="history-row">
          <span className="badge">Short</span>
          <span className="small">{shortUrl}</span>
        </div>
        <div className="history-row">
          <span className="badge" style={{ background: "rgba(245, 158, 11, 0.14)", borderColor: "rgba(245,158,11,0.25)", color: "#fbbf24" }}>
            Original
          </span>
          <span className="muted">{originalUrl}</span>
        </div>
      </div>
    </div>
  );
}
