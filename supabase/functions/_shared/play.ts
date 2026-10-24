// Conversa com a API do Google Play (assinaturas), usada por play-billing (o app manda a compra) e
// play-rtdn (o Google avisa mudanças: renovação, cancelamento, reembolso). O que vale é sempre o que
// o Google responde, nunca o que veio no pedido.

export const PACKAGE = Deno.env.get("PLAY_PACKAGE_NAME")?.trim() || "br.com.estudario";

export interface ServiceAccount { client_email: string; private_key: string; token_uri?: string }

/** A conta de serviço do Play Console (segredo PLAY_SERVICE_ACCOUNT_JSON), ou null sem ela. */
export function serviceAccount(): ServiceAccount | null {
  const raw = Deno.env.get("PLAY_SERVICE_ACCOUNT_JSON")?.trim();
  if (!raw) return null;
  try {
    return JSON.parse(raw) as ServiceAccount;
  } catch {
    return null;
  }
}

export function base64url(bytes: Uint8Array | string): string {
  const raw = typeof bytes === "string" ? new TextEncoder().encode(bytes) : bytes;
  let s = "";
  for (const b of raw) s += String.fromCharCode(b);
  return btoa(s).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

async function importPrivateKey(pem: string): Promise<CryptoKey> {
  const body = pem.replace(/-----BEGIN PRIVATE KEY-----|-----END PRIVATE KEY-----|\s/g, "");
  const der = Uint8Array.from(atob(body), (c) => c.charCodeAt(0));
  return await crypto.subtle.importKey("pkcs8", der, { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" }, false, ["sign"]);
}

let cachedToken: { value: string; expiresAt: number } | null = null;

/** Token OAuth da conta de serviço para a Android Publisher API. */
export async function accessToken(account: ServiceAccount): Promise<string> {
  if (cachedToken && cachedToken.expiresAt > Date.now() + 60_000) return cachedToken.value;
  const now = Math.floor(Date.now() / 1000);
  const header = base64url(JSON.stringify({ alg: "RS256", typ: "JWT" }));
  const claims = base64url(JSON.stringify({
    iss: account.client_email,
    scope: "https://www.googleapis.com/auth/androidpublisher",
    aud: account.token_uri ?? "https://oauth2.googleapis.com/token",
    iat: now,
    exp: now + 3600,
  }));
  const key = await importPrivateKey(account.private_key);
  const signature = await crypto.subtle.sign("RSASSA-PKCS1-v1_5", key, new TextEncoder().encode(`${header}.${claims}`));
  const assertion = `${header}.${claims}.${base64url(new Uint8Array(signature))}`;
  const response = await fetch(account.token_uri ?? "https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "content-type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({ grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer", assertion }),
  });
  if (!response.ok) throw new Error(`google_token_${response.status}`);
  const body = await response.json() as { access_token?: string; expires_in?: number };
  if (!body.access_token) throw new Error("google_token_missing");
  cachedToken = { value: body.access_token, expiresAt: Date.now() + (body.expires_in ?? 3600) * 1000 };
  return body.access_token;
}

export interface PlaySubscription {
  subscriptionState?: string;
  acknowledgementState?: string;
  linkedPurchaseToken?: string;
  lineItems?: { productId?: string; expiryTime?: string; offerDetails?: { basePlanId?: string }; autoRenewingPlan?: { autoRenewEnabled?: boolean } }[];
}

export async function fetchSubscription(token: string, purchaseToken: string): Promise<PlaySubscription | null> {
  const url = `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${PACKAGE}/purchases/subscriptionsv2/tokens/${encodeURIComponent(purchaseToken)}`;
  const response = await fetch(url, { headers: { authorization: `Bearer ${token}` } });
  if (response.status === 404 || response.status === 410) return null;
  if (!response.ok) throw new Error(`play_api_${response.status}`);
  return await response.json() as PlaySubscription;
}

export async function acknowledge(token: string, productId: string, purchaseToken: string): Promise<void> {
  const url = `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${PACKAGE}/purchases/subscriptions/${encodeURIComponent(productId)}/tokens/${encodeURIComponent(purchaseToken)}:acknowledge`;
  await fetch(url, { method: "POST", headers: { authorization: `Bearer ${token}`, "content-type": "application/json" }, body: "{}" });
}

// ------------------------------------------------------------------ banco (service role)
export function supabase(): { url: string; serviceKey: string } {
  const url = Deno.env.get("SUPABASE_URL")?.trim();
  const serviceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !serviceKey) throw new Error("supabase_env_missing");
  return { url: url.replace(/\/$/, ""), serviceKey };
}

export async function rest(path: string, init: RequestInit = {}): Promise<Response> {
  const { url, serviceKey } = supabase();
  return await fetch(`${url}/rest/v1/${path}`, {
    ...init,
    headers: { apikey: serviceKey, authorization: `Bearer ${serviceKey}`, accept: "application/json", "content-type": "application/json", ...(init.headers ?? {}) },
  });
}

export async function tierForProduct(productId: string): Promise<string | null> {
  const response = await rest(`plan_catalog?select=plan_tier&play_product_id=eq.${encodeURIComponent(productId)}`);
  if (!response.ok) throw new Error(`catalog_${response.status}`);
  const rows = await response.json() as { plan_tier?: string }[];
  return rows[0]?.plan_tier ?? null;
}

export async function applySubscription(params: Record<string, unknown>): Promise<void> {
  const response = await rest("rpc/apply_play_subscription", { method: "POST", body: JSON.stringify(params) });
  if (!response.ok) {
    const text = await response.text();
    if (text.includes("purchase_belongs_to_another_account")) throw new Error("PURCHASE_OWNED_BY_OTHER");
    throw new Error(`apply_${response.status}`);
  }
}

export interface AppliedSubscription { tier: string; state: string; expiresAt: string | null; active: boolean; productId: string }

/**
 * Lê a assinatura no Google e grava para [userId]. null quando o Google não conhece o token.
 * [fallbackProduct]: o produto que o app disse, usado só se o Google não trouxer o item.
 */
export async function syncSubscription(token: string, userId: string, purchaseToken: string, fallbackProduct: string): Promise<AppliedSubscription | null | "UNKNOWN_PRODUCT"> {
  const sub = await fetchSubscription(token, purchaseToken);
  if (!sub) return null;
  const line = sub.lineItems?.[0];
  const productId = line?.productId ?? fallbackProduct;
  const tier = await tierForProduct(productId);
  if (!tier) return "UNKNOWN_PRODUCT";
  const state = (sub.subscriptionState ?? "").replace(/^SUBSCRIPTION_STATE_/, "") || "UNKNOWN";
  const expiresAt = line?.expiryTime ?? null;
  await applySubscription({
    p_user_id: userId,
    p_purchase_token: purchaseToken,
    p_product_id: productId,
    p_base_plan_id: line?.offerDetails?.basePlanId ?? null,
    p_plan_tier: tier,
    p_state: state,
    p_expires_at: expiresAt,
    p_auto_renewing: line?.autoRenewingPlan?.autoRenewEnabled === true,
  });
  if (sub.acknowledgementState === "ACKNOWLEDGEMENT_STATE_PENDING") {
    try { await acknowledge(token, productId, purchaseToken); } catch { /* o app também reconhece pelo lado dele */ }
  }
  const active = (state === "ACTIVE" || state === "IN_GRACE_PERIOD") && !!expiresAt && new Date(expiresAt).getTime() > Date.now();
  return { tier, state, expiresAt, active, productId };
}
