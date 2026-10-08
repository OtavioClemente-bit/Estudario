// Assinaturas do Google Play.
//
// POST /play-billing  { purchaseToken, productId }
//   O app manda o token da compra (nova ou já existente, ao "restaurar"). Aqui a compra é
//   conferida direto com a API do Google Play (purchases.subscriptionsv2), nunca pelo que o app
//   diz, e o plano da conta é atualizado com o que o Google respondeu. Depois a compra é
//   reconhecida (acknowledge) para o Google não estornar em 3 dias.
//
// Segredos: PLAY_SERVICE_ACCOUNT_JSON (conta de serviço com acesso ao Play Console, papel
// "Ver dados financeiros"/"Gerenciar pedidos e assinaturas") e PLAY_PACKAGE_NAME. Sem eles a
// função responde BILLING_UNAVAILABLE e o app mostra os planos como "em breve".
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";

const PACKAGE = Deno.env.get("PLAY_PACKAGE_NAME")?.trim() || "br.com.estudario";

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}
function fail(code: string, status: number, message?: string): Response {
  return json({ error: { code, message: message ?? code } }, status);
}

// ------------------------------------------------------------------ token OAuth da conta de serviço
interface ServiceAccount { client_email: string; private_key: string; token_uri?: string }

function base64url(bytes: Uint8Array | string): string {
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

async function accessToken(account: ServiceAccount): Promise<string> {
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

// ------------------------------------------------------------------ Play Developer API
interface PlaySubscription {
  subscriptionState?: string;
  acknowledgementState?: string;
  linkedPurchaseToken?: string;
  lineItems?: { productId?: string; expiryTime?: string; offerDetails?: { basePlanId?: string }; autoRenewingPlan?: { autoRenewEnabled?: boolean } }[];
}

async function fetchSubscription(token: string, purchaseToken: string): Promise<PlaySubscription | null> {
  const url = `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${PACKAGE}/purchases/subscriptionsv2/tokens/${encodeURIComponent(purchaseToken)}`;
  const response = await fetch(url, { headers: { authorization: `Bearer ${token}` } });
  if (response.status === 404 || response.status === 410) return null;
  if (!response.ok) throw new Error(`play_api_${response.status}`);
  return await response.json() as PlaySubscription;
}

async function acknowledge(token: string, productId: string, purchaseToken: string): Promise<void> {
  const url = `https://androidpublisher.googleapis.com/androidpublisher/v3/applications/${PACKAGE}/purchases/subscriptions/${encodeURIComponent(productId)}/tokens/${encodeURIComponent(purchaseToken)}:acknowledge`;
  await fetch(url, { method: "POST", headers: { authorization: `Bearer ${token}`, "content-type": "application/json" }, body: "{}" });
}

// ------------------------------------------------------------------ banco (service role)
function supabase(): { url: string; serviceKey: string } {
  const url = Deno.env.get("SUPABASE_URL")?.trim();
  const serviceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!url || !serviceKey) throw new Error("supabase_env_missing");
  return { url: url.replace(/\/$/, ""), serviceKey };
}

async function tierForProduct(productId: string): Promise<string | null> {
  const { url, serviceKey } = supabase();
  const response = await fetch(`${url}/rest/v1/plan_catalog?select=plan_tier&play_product_id=eq.${encodeURIComponent(productId)}`, {
    headers: { apikey: serviceKey, authorization: `Bearer ${serviceKey}`, accept: "application/json" },
  });
  if (!response.ok) throw new Error(`catalog_${response.status}`);
  const rows = await response.json() as { plan_tier?: string }[];
  return rows[0]?.plan_tier ?? null;
}

async function apply(params: Record<string, unknown>): Promise<void> {
  const { url, serviceKey } = supabase();
  const response = await fetch(`${url}/rest/v1/rpc/apply_play_subscription`, {
    method: "POST",
    headers: { apikey: serviceKey, authorization: `Bearer ${serviceKey}`, "content-type": "application/json" },
    body: JSON.stringify(params),
  });
  if (!response.ok) {
    const text = await response.text();
    if (text.includes("purchase_belongs_to_another_account")) throw new Error("PURCHASE_OWNED_BY_OTHER");
    throw new Error(`apply_${response.status}`);
  }
}

// ------------------------------------------------------------------ handler
async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return fail("METHOD_NOT_ALLOWED", 405);
  let userId: string;
  try {
    userId = (await authenticateSupabaseRequest(request)).userId;
  } catch (error) {
    if (error instanceof AuthError) return fail(error.code, error.status);
    return fail("AUTH_UNAVAILABLE", 503);
  }

  const raw = Deno.env.get("PLAY_SERVICE_ACCOUNT_JSON")?.trim();
  if (!raw) return fail("BILLING_UNAVAILABLE", 503, "As assinaturas ainda não estão abertas.");
  let account: ServiceAccount;
  try { account = JSON.parse(raw) as ServiceAccount; } catch { return fail("BILLING_UNAVAILABLE", 503); }

  let body: { purchaseToken?: unknown; productId?: unknown };
  try { body = await request.json(); } catch { return fail("INVALID_BODY", 400); }
  const purchaseToken = typeof body.purchaseToken === "string" ? body.purchaseToken.trim() : "";
  const productId = typeof body.productId === "string" ? body.productId.trim() : "";
  if (!purchaseToken || purchaseToken.length > 4096 || !productId || productId.length > 200) return fail("INVALID_BODY", 400);

  try {
    const token = await accessToken(account);
    const sub = await fetchSubscription(token, purchaseToken);
    if (!sub) return fail("PURCHASE_NOT_FOUND", 404, "O Google não reconheceu esta compra.");
    const line = sub.lineItems?.[0];
    const liveProduct = line?.productId ?? productId;
    const tier = await tierForProduct(liveProduct);
    if (!tier) return fail("UNKNOWN_PRODUCT", 400);
    const state = (sub.subscriptionState ?? "").replace(/^SUBSCRIPTION_STATE_/, "") || "UNKNOWN";
    const expiresAt = line?.expiryTime ?? null;
    await apply({
      p_user_id: userId,
      p_purchase_token: purchaseToken,
      p_product_id: liveProduct,
      p_base_plan_id: line?.offerDetails?.basePlanId ?? null,
      p_plan_tier: tier,
      p_state: state,
      p_expires_at: expiresAt,
      p_auto_renewing: line?.autoRenewingPlan?.autoRenewEnabled === true,
    });
    if (sub.acknowledgementState === "ACKNOWLEDGEMENT_STATE_PENDING") {
      try { await acknowledge(token, liveProduct, purchaseToken); } catch { /* o app também reconhece pelo lado dele */ }
    }
    const active = (state === "ACTIVE" || state === "IN_GRACE_PERIOD") && !!expiresAt && new Date(expiresAt).getTime() > Date.now();
    return json({ planTier: active ? tier : "FREE", state, expiresAt, active });
  } catch (error) {
    const message = error instanceof Error ? error.message : "";
    if (message === "PURCHASE_OWNED_BY_OTHER") return fail("PURCHASE_OWNED_BY_OTHER", 409, "Esta assinatura já está ligada a outra conta Estudário.");
    console.error("play-billing", message);
    return fail("BILLING_VERIFY_FAILED", 502, "Não deu para confirmar a compra com o Google agora. Tente de novo em instantes.");
  }
}

if (import.meta.main) Deno.serve(handle);
