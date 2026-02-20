import { ShortLink } from "./types";

const defaultBase = "/v1/api";
const envBase = import.meta.env.VITE_API_BASE as string | undefined;
const API_BASE = (envBase ?? defaultBase).replace(/\/$/, "");

export async function createShortUrl(url: string): Promise<ShortLink> {
  const response = await fetch(`${API_BASE}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url })
  });

  if (!response.ok) {
    const message = await safeMessage(response);
    throw new Error(message);
  }

  return (await response.json()) as ShortLink;
}

async function safeMessage(res: Response) {
  try {
    const body = await res.json();
    return body?.message ?? body?.error ?? res.statusText;
  } catch (err) {
    return res.statusText || "Request failed";
  }
}
