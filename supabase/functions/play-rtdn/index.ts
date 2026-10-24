// Avisos do Google Play em tempo real (Real-time developer notifications, via Pub/Sub push).
//
// POST /play-rtdn  (chamado pelo Pub/Sub, nunca pelo app)
//   Cada mudança numa assinatura (renovou, cancelou, venceu, foi revogada) e cada compra
//   reembolsada chega aqui. A assinatura é relida no Google e o plano da conta dona do token é
//   atualizado; reembolso (voidedPurchaseNotification) derruba o plano na hora e não volta mais.
//   Token que nenhuma conta ligou ainda é ignorado: o app liga ao abrir (play-billing).
//
// Quem chama prova que é o Pub/Sub com o token OIDC do Google no cabeçalho Authorization:
// assinatura RS256 conferida com as chaves públicas do Google, emissor accounts.google.com,
// público (aud) = PLAY_RTDN_AUDIENCE e e-mail = PLAY_RTDN_SERVICE_ACCOUNT. Sem esses segredos a
// função não aceita nada.
import { accessToken, PACKAGE, rest, serviceAccount, syncSubscription } from "../_shared/play.ts";

function text(body: string, status = 200): Response {
  return new Response(body, { status, headers: { "content-type": "text/plain; charset=utf-8", "cache-control": "no-store" } });
}

function fromBase64Url(value: string): Uint8Array<ArrayBuffer> {
  const padded = value.replace(/-/g, "+").replace(/_/g, "/") + "===".slice((value.length + 3) % 4);
  return Uint8Array.from(atob(padded), (c) => c.charCodeAt(0));
}

let jwksCache: { keys: Map<string, CryptoKey>; expiresAt: number } | null = null;

async function googleKeys(): Promise<Map<string, CryptoKey>> {
  if (jwksCache && jwksCache.expiresAt > Date.now()) return jwksCache.keys;
  const response = await fetch("https://www.googleapis.com/oauth2/v3/certs");
  if (!response.ok) throw new Error(`jwks_${response.status}`);
  const body = await response.json() as { keys?: (JsonWebKey & { kid?: string })[] };
  const keys = new Map<string, CryptoKey>();
  for (const jwk of body.keys ?? []) {
    if (!jwk.kid || jwk.kty !== "RSA") continue;
    keys.set(jwk.kid, await crypto.subtle.importKey("jwk", jwk, { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" }, false, ["verify"]));
  }
  jwksCache = { keys, expiresAt: Date.now() + 3_600_000 };
  return keys;
}

export interface OidcExpectation { audience: string; email: string; now?: number }

/** Confere o token OIDC do Pub/Sub: assinatura do Google, emissor, público, e-mail e validade. */
export async function verifyPubSubToken(jwt: string, expected: OidcExpectation, keys: Map<string, CryptoKey>): Promise<boolean> {
  const parts = jwt.split(".");
  if (parts.length !== 3) return false;
  let header: { alg?: string; kid?: string }, claims: { iss?: string; aud?: string; email?: string; email_verified?: boolean; exp?: number; iat?: number };
  try {
    header = JSON.parse(new TextDecoder().decode(fromBase64Url(parts[0])));
    claims = JSON.parse(new TextDecoder().decode(fromBase64Url(parts[1])));
  } catch {
    return false;
  }
  if (header.alg !== "RS256" || !header.kid) return false;
  const key = keys.get(header.kid);
  if (!key) return false;
  const ok = await crypto.subtle.verify("RSASSA-PKCS1-v1_5", key, fromBase64Url(parts[2]), new TextEncoder().encode(`${parts[0]}.${parts[1]}`)).catch(() => false);
  if (!ok) return false;
  const now = Math.floor((expected.now ?? Date.now()) / 1000);
  return (claims.iss === "https://accounts.google.com" || claims.iss === "accounts.google.com") &&
    claims.aud === expected.audience &&
    claims.email === expected.email && claims.email_verified === true &&
    typeof claims.exp === "number" && claims.exp > now - 60;
}

export interface Notification {
  packageName?: string;
  subscriptionNotification?: { notificationType?: number; purchaseToken?: string; subscriptionId?: string };
  voidedPurchaseNotification?: { purchaseToken?: string; orderId?: string; productType?: number };
  testNotification?: unknown;
}

/** O conteúdo do aviso, que o Pub/Sub manda em base64 dentro de message.data. */
export function notificationOf(body: unknown): Notification | null {
  const data = (body as { message?: { data?: unknown } } | null)?.message?.data;
  if (typeof data !== "string" || data.length > 20_000) return null;
  try {
    return JSON.parse(new TextDecoder().decode(fromBase64Url(data))) as Notification;
  } catch {
    return null;
  }
}

async function ownerOf(purchaseToken: string): Promise<{ user_id: string; product_id: string } | null> {
  const response = await rest(`play_subscriptions?select=user_id,product_id&purchase_token=eq.${encodeURIComponent(purchaseToken)}&limit=1`);
  if (!response.ok) throw new Error(`owner_${response.status}`);
  return ((await response.json()) as { user_id: string; product_id: string }[])[0] ?? null;
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return text("method", 405);
  const audience = Deno.env.get("PLAY_RTDN_AUDIENCE")?.trim(), email = Deno.env.get("PLAY_RTDN_SERVICE_ACCOUNT")?.trim();
  if (!audience || !email) return text("not configured", 503);
  const jwt = request.headers.get("authorization")?.replace(/^Bearer\s+/i, "").trim() ?? "";
  let keys: Map<string, CryptoKey>;
  try {
    keys = await googleKeys();
  } catch {
    return text("keys", 503); // o Pub/Sub tenta de novo
  }
  if (!jwt || !await verifyPubSubToken(jwt, { audience, email }, keys)) return text("unauthorized", 401);

  const notification = notificationOf(await request.json().catch(() => null));
  // Aviso que não dá para ler, de outro app ou de teste: confirma para o Pub/Sub não repetir.
  if (!notification || notification.packageName !== PACKAGE || notification.testNotification) return text("ok");

  try {
    const voided = notification.voidedPurchaseNotification?.purchaseToken;
    if (voided) {
      // Reembolso: derruba na hora, mesmo que o Google ainda mostre a assinatura como ativa.
      const response = await rest("rpc/void_play_subscription", { method: "POST", body: JSON.stringify({ p_purchase_token: voided }) });
      if (!response.ok) return text("unavailable", 503);
      return text("ok");
    }
    const purchaseToken = notification.subscriptionNotification?.purchaseToken;
    if (!purchaseToken) return text("ok");
    const owner = await ownerOf(purchaseToken);
    if (!owner) return text("ok");
    const account = serviceAccount();
    if (!account) return text("not configured", 503);
    const token = await accessToken(account);
    await syncSubscription(token, owner.user_id, purchaseToken, notification.subscriptionNotification?.subscriptionId ?? owner.product_id);
    return text("ok");
  } catch (error) {
    console.error("play-rtdn", error instanceof Error ? error.message : "");
    return text("unavailable", 503); // o Pub/Sub tenta de novo
  }
}

if (import.meta.main) Deno.serve(handle);
