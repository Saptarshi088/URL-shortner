import { useEffect, useMemo, useState } from "react";
import { createShortUrl } from "./api";
import { UrlForm } from "./components/UrlForm";
import { ResultCard } from "./components/ResultCard";
import { LinkHistory } from "./components/LinkHistory";
import { useRecentLinks } from "./hooks/useRecentLinks";
import { ShortLink } from "./types";

type Theme = "light" | "dark";
const THEME_KEY = "urlshortner.theme";

export default function App() {
  const [url, setUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [current, setCurrent] = useState<ShortLink | null>(null);
  const [copied, setCopied] = useState(false);
  const [theme, setTheme] = useState<Theme>(() => {
    const saved = localStorage.getItem(THEME_KEY) as Theme | null;
    if (saved === "light" || saved === "dark") return saved;
    const prefersDark = window.matchMedia?.("(prefers-color-scheme: dark)").matches;
    return prefersDark ? "dark" : "light";
  });

  const { links, metrics } = useRecentLinks(current);

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem(THEME_KEY, theme);
  }, [theme]);

  useEffect(() => {
    if (!copied) return;
    const id = setTimeout(() => setCopied(false), 1600);
    return () => clearTimeout(id);
  }, [copied]);

  const handleSubmit = async () => {
    if (!url.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const result = await createShortUrl(url.trim());
      setCurrent(result);
      setUrl("");
    } catch (err) {
      const message = err instanceof Error ? err.message : "Could not create link";
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = (value: string) => {
    if (navigator?.clipboard) {
      navigator.clipboard.writeText(value).then(() => setCopied(true));
    } else {
      setCopied(true);
    }
  };

  const themeLabel = useMemo(() => (theme === "dark" ? "Dark" : "Light"), [theme]);

  return (
    <div className="page">
      <div className="orb orb-1" />
      <div className="orb orb-2" />

      <header className="topbar">
        <div className="brand">
          <div className="brand-icon">↗</div>
          <div>
            <div className="brand-kicker">SparkURL</div>
            <div className="brand-title">Saptarshi shortener</div>
          </div>
        </div>
        <button
          className="mode-toggle"
          type="button"
          aria-label="Toggle color theme"
          onClick={() => setTheme((prev) => (prev === "dark" ? "light" : "dark"))}
        >
          <span className="mode-icon" data-active={theme === "light"}>
            ☀️
          </span>
          <span className="mode-track">
            <span className="mode-thumb" data-theme={theme} />
          </span>
          <span className="mode-icon" data-active={theme === "dark"}>
            🌙
          </span>
          <span className="mode-label">{themeLabel} mode</span>
        </button>
      </header>

      <div className="hero card pop">
        <div>
          <div className="eyebrow">Instant redirects</div>
          <h1 className="title">Share-ready short links</h1>
          <p className="subtitle">
            Paste any HTTP/HTTPS URL and get a neat short code backed by your Spring Boot redirector. Copy it,
            ship it, track it.
          </p>
          <div className="chip-row">
            <span className="pill accent">Recent saved {metrics.total}</span>
            <span className="pill">Unique {metrics.unique}</span>
          </div>
        </div>
        <div className="stat-grid">
          <div className="stat-card">
            <div className="stat-kicker">Engine</div>
            <div className="stat-value">Spring Boot</div>
            <p className="muted">302 redirects + click tracking</p>
          </div>
          <div className="stat-card">
            <div className="stat-kicker">Clipboard ready</div>
            <div className="stat-value">1-click copy</div>
            <p className="muted">Snappy, haptic-like feedback</p>
          </div>
        </div>
      </div>

      <div className="card glass pop">
        <UrlForm url={url} onUrlChange={setUrl} onSubmit={handleSubmit} loading={loading} />
        {error && <p className="status error">⚠️ {error}</p>}
        {copied && <p className="status success">Copied to clipboard</p>}
        <ResultCard link={current} onCopy={handleCopy} />
      </div>

      <div className="card glass">
        <div className="section-header">
          <div>
            <h3>Recent links</h3>
            <p className="muted">Stored locally in this browser. We keep the latest six entries handy.</p>
          </div>
          <div className="tiny-pill">Local only</div>
        </div>
        <LinkHistory items={links} />
      </div>
    </div>
  );
}
