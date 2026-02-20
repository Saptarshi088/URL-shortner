import React from "react";
import { ShortLink } from "../types";

type Props = {
  items: ShortLink[];
};

export function LinkHistory({ items }: Props) {
  if (!items.length) {
    return (
      <div className="history">
        <div className="empty">No recent links yet.</div>
      </div>
    );
  }

  return (
    <div className="history">
      {items.map((link) => (
        <div className="history-item" key={link.shortUrl}>
          <div className="history-row">
            <span className="badge">Short</span>
            <span className="small">{link.shortUrl}</span>
          </div>
          <div className="history-row">
            <span className="badge" style={{ background: "rgba(245, 158, 11, 0.14)", borderColor: "rgba(245,158,11,0.25)", color: "#fbbf24" }}>
              Target
            </span>
            <span className="muted">{link.originalUrl}</span>
          </div>
          <div className="history-row">
            <span className="badge" style={{ background: "rgba(255,255,255,0.08)", borderColor: "rgba(255,255,255,0.12)", color: "#cbd5e1" }}>
              Created
            </span>
            <span className="small">{new Date(link.createdDate).toLocaleString()}</span>
          </div>
        </div>
      ))}
    </div>
  );
}
