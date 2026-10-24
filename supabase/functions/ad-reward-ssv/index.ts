// Recompensa de anúncio confirmada pelo Google (AdMob, verificação do lado do servidor).
//
// GET /ad-reward-ssv?ad_network=…&ad_unit=…&custom_data=…&reward_amount=…&reward_item=…&timestamp=…
//                    &transaction_id=…&user_id=…&signature=…&key_id=…
//   Quem chama é o próprio AdMob, quando a pessoa assiste o anúncio até o fim. A chamada é assinada
//   pelo Google (ECDSA P-256); a assinatura é conferida com as chaves públicas dele, então um app
//   alterado não consegue inventar que viu o anúncio. Cada anúncio vira um crédito (transaction_id
//   único) para adiantar uma matéria do plano, dentro do limite do Grátis (2 por dia, 10 por mês).
//   Sempre responde 200 para chamada assinada (senão o AdMob repete); o resto, 400.
//
// No AdMob: bloco de anúncio premiado → "Verificação do lado do servidor" → URL desta função.
// O app passa o id da conta (user_id) nas opções de verificação do anúncio.
import { adCreditAllowed, brasiliaBounds } from "../_shared/free-allowance.ts";

const KEYS_URL = "https://www.gstatic.com/admob/reward/verifier-keys.json";
const UUID = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

let keyCache: { at: number; keys: Map<string, CryptoKey> } | null = null;

function fromBase64(value: string): Uint8Array<ArrayBuffer> {
  const clean = value.replace(/-/g, "+").replace(/_/g, "/");
  const padded = clean + "=".repeat((4 - clean.length % 4) % 4);
  return Uint8Array.from(atob(padded), (c) => c.charCodeAt(0));
}

/** As chaves públicas do Google, guardadas por um dia (ele troca de tempos em tempos). */
async function verifierKeys(fetcher: typeof fetch = fetch): Promise<Map<string, CryptoKey>> {
  if (keyCache && Date.now() - keyCache.at < 86_400_000) return keyCache.keys;
  const response = await fetcher(KEYS_URL);
  if (!response.ok) throw new Error("KEYS_UNAVAILABLE");
  const body = await response.json() as { keys?: { keyId: number | string; base64: string }[] };
  const keys = new Map<string, CryptoKey>();
  for (const key of body.keys ?? []) {
    const imported = await crypto.subtle.importKey("spki", fromBase64(key.base64), { name: "ECDSA", namedCurve: "P-256" }, false, ["verify"]);
    keys.set(String(key.keyId), imported);
  }
  keyCache = { at: Date.now(), keys };
  return keys;
}

/** Assinatura DER (como o Google manda) para o formato r||s de 64 bytes do WebCrypto. */
export function derToRaw(der: Uint8Array): Uint8Array<ArrayBuffer> {
  if (der[0] !== 0x30) throw new Error("BAD_SIGNATURE");
  let offset = 2;
  if (der[1] & 0x80) offset = 2 + (der[1] & 0x7f);
  const readInt = (): Uint8Array<ArrayBuffer> => {
    if (der[offset] !== 0x02) throw new Error("BAD_SIGNATURE");
    const length = der[offset + 1];
    let value = der.slice(offset + 2, offset + 2 + length);
    offset += 2 + length;
    while (value.length > 32 && value[0] === 0) value = value.slice(1);
    if (value.length > 32) throw new Error("BAD_SIGNATURE");
    const out = new Uint8Array(32);
    out.set(value, 32 - value.length);
    return out;
  };
  const r = readInt(), s = readInt();
  const raw = new Uint8Array(64);
  raw.set(r, 0);
  raw.set(s, 32);
  return raw;
}

/** Confere a chamada: o conteúdo assinado é tudo antes de "&signature=". */
export async function verifyRewardCall(rawQuery: string, keys: Map<string, CryptoKey>): Promise<boolean> {
  const at = rawQuery.indexOf("&signature=");
  if (at < 0) return false;
  const params = new URLSearchParams(rawQuery);
  const signature = params.get("signature"), keyId = params.get("key_id");
  const key = keyId ? keys.get(keyId) : undefined;
  if (!signature || !key) return false;
  try {
    return await crypto.subtle.verify({ name: "ECDSA", hash: "SHA-256" }, key, derToRaw(fromBase64(signature)), new TextEncoder().encode(rawQuery.slice(0, at)));
  } catch {
    return false;
  }
}

/** Bloco premiado do Estudário (só o número final do id do AdMob). ADMOB_REWARDED_AD_UNIT troca. */
const AD_UNIT = (Deno.env.get("ADMOB_REWARDED_AD_UNIT")?.trim() || "ca-app-pub-6794476146182030/9507247864").split("/").pop()!;

export function isOurAdUnit(adUnit: string | null, expected = AD_UNIT): boolean {
  const unit = (adUnit ?? "").trim().split("/").pop() ?? "";
  return unit !== "" && unit === expected;
}

function text(body: string, status = 200): Response {
  return new Response(body, { status, headers: { "content-type": "text/plain; charset=utf-8", "cache-control": "no-store" } });
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "GET") return text("method", 405);
  const url = new URL(request.url);
  const rawQuery = url.search.replace(/^\?/, "");
  let keys: Map<string, CryptoKey>;
  try {
    keys = await verifierKeys();
  } catch {
    return text("keys", 503); // o AdMob tenta de novo
  }
  if (!await verifyRewardCall(rawQuery, keys)) return text("signature", 400);
  const params = new URLSearchParams(rawQuery);
  const userId = params.get("user_id") ?? "", transaction = params.get("transaction_id") ?? "";
  const timestamp = Number(params.get("timestamp"));
  // O teste de URL do próprio AdMob vem sem conta: confere a assinatura e responde que está tudo certo.
  if (!UUID.test(userId) || !transaction) return text("ok");
  // Sem horário, ou de mais de um dia atrás, não vira crédito (repetição tardia).
  if (!Number.isFinite(timestamp) || Date.now() - timestamp > 86_400_000) return text("ok");
  // Só o nosso bloco premiado: o Google assina o retorno de qualquer app AdMob, e alguém poderia
  // apontar o bloco de outro app para cá.
  if (!isOurAdUnit(params.get("ad_unit"))) return text("ok");

  const supabaseUrl = Deno.env.get("SUPABASE_URL")?.trim(), serviceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")?.trim();
  if (!supabaseUrl || !serviceKey) return text("unavailable", 503);
  const rest = (path: string, init: RequestInit = {}) =>
    fetch(`${supabaseUrl.replace(/\/$/, "")}/rest/v1/${path}`, {
      ...init,
      headers: { apikey: serviceKey, authorization: `Bearer ${serviceKey}`, "content-type": "application/json", accept: "application/json", ...(init.headers ?? {}) },
    });
  const user = encodeURIComponent(userId);
  const { dayStart, monthStart } = brasiliaBounds(new Date());
  const credits = await rest(`ad_reward_credits?select=created_at&user_id=eq.${user}&created_at=gte.${encodeURIComponent(monthStart)}&limit=50`);
  if (!credits.ok) return text("unavailable", 503);
  const rows = await credits.json() as { created_at: string }[];
  const today = rows.filter((row) => Date.parse(row.created_at) >= Date.parse(dayStart)).length;
  // Passou do limite do dia ou do mês: o anúncio foi visto, mas não vira crédito (o app já avisa antes).
  if (!adCreditAllowed({ adCreditsToday: today, adCreditsMonth: rows.length })) return text("ok");
  const insert = await rest("ad_reward_credits", {
    method: "POST",
    headers: { prefer: "resolution=ignore-duplicates,return=minimal" },
    body: JSON.stringify({ id: transaction.slice(0, 200), user_id: userId, ad_unit: (params.get("ad_unit") ?? "").slice(0, 200) }),
  });
  await insert.body?.cancel().catch(() => {});
  return insert.ok ? text("ok") : text("unavailable", 503);
}

if (import.meta.main) Deno.serve(handle);
