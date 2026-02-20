import { useEffect, useMemo, useState } from "react";
import { ShortLink } from "../types";

const STORAGE_KEY = "urlshortner.recent";
const LIMIT = 6;

export function useRecentLinks(initial?: ShortLink | null) {
  const [links, setLinks] = useState<ShortLink[]>(() => loadSaved());

  useEffect(() => {
    if (initial) {
      setLinks((prev) => persist([initial, ...prev.filter((l) => l.shortUrl !== initial.shortUrl)]));
    }
  }, [initial]);

  useEffect(() => {
    persist(links);
  }, [links]);

  const metrics = useMemo(() => {
    const unique = new Set(links.map((l) => l.shortUrl)).size;
    return { total: links.length, unique };
  }, [links]);

  return { links, setLinks, metrics };
}

function loadSaved(): ShortLink[] {
  if (typeof localStorage === "undefined") return [];
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return [];
  try {
    return JSON.parse(raw) as ShortLink[];
  } catch {
    return [];
  }
}

function persist(items: ShortLink[]) {
  const pruned = items.slice(0, LIMIT);
  if (typeof localStorage !== "undefined") {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(pruned));
  }
  return pruned;
}
