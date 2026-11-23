// Mercado Pago: pagamento pelo site para quem não tem Google Play.
//
// Segredos: MP_ACCESS_TOKEN (credencial de produção ou de teste, "APP_USR-..." / "TEST-...") e,
// opcional, MP_WEBHOOK_SECRET (assinatura dos avisos). Sem MP_ACCESS_TOKEN as funções respondem
// BILLING_UNAVAILABLE e o site mostra que o pagamento está fechado.
//
// Nunca se confia no que chega do navegador ou do aviso: todo estado vem de uma consulta direta à
// API do Mercado Pago com o token do vendedor.
import { rest } from "./play.ts";

const API = "https://api.mercadopago.com";
export const SITE = "https://app.estudario.com.br";

export type Tier = "ESSENCIAL" | "PRO";
export type Period = "MONTH" | "YEAR";

export function mpToken(): string | null {
  return Deno.env.get("MP_ACCESS_TOKEN")?.trim() || null;
}

export async function mp(path: string, init: RequestInit = {}): Promise<Response> {
  const token = mpToken();
  if (!token) throw new Error("mp_token_missing");
  return await fetch(`${API}${path}`, {
    ...init,
    headers: { authorization: `Bearer ${token}`, "content-type": "application/json", accept: "application/json", ...(init.headers ?? {}) },
  });
}

/** Preço do plano em centavos, do catálogo (a mesma tabela que o app mostra). */
export async function priceCents(tier: Tier, period: Period): Promise<{ cents: number; name: string } | null> {
  const r = await rest(`plan_catalog?select=name,price_month_cents,price_year_cents&plan_tier=eq.${tier}`);
  if (!r.ok) throw new Error(`catalog_${r.status}`);
  const row = (await r.json() as Record<string, unknown>[])[0];
  if (!row) return null;
  const cents = Number(period === "YEAR" ? row.price_year_cents : row.price_month_cents);
  return cents > 0 ? { cents, name: String(row.name ?? tier) } : null;
}

/** external_reference: quem pagou e o quê. Formato: "<kind>|<user>|<tier>|<period>". */
export function reference(kind: "sub" | "once", userId: string, tier: Tier, period: Period): string {
  return `${kind}|${userId}|${tier}|${period}`;
}
export function parseReference(ref: unknown): { kind: "sub" | "once"; userId: string; tier: Tier; period: Period } | null {
  const parts = String(ref ?? "").split("|");
  if (parts.length !== 4) return null;
  const [kind, userId, tier, period] = parts;
  if ((kind !== "sub" && kind !== "once") || !/^[0-9a-f-]{36}$/.test(userId)) return null;
  if (tier !== "ESSENCIAL" && tier !== "PRO") return null;
  if (period !== "MONTH" && period !== "YEAR") return null;
  return { kind, userId, tier, period };
}

export function addPeriod(from: Date, period: Period): Date {
  const d = new Date(from);
  if (period === "YEAR") d.setUTCFullYear(d.getUTCFullYear() + 1);
  else d.setUTCMonth(d.getUTCMonth() + 1);
  return d;
}

async function apply(row: {
  id: string; userId: string; kind: "SUBSCRIPTION" | "ONE_TIME"; tier: Tier; period: Period;
  state: string; expiresAt: string | null; autoRenewing: boolean; amountCents: number | null;
}): Promise<void> {
  const r = await rest("rpc/apply_web_subscription", {
    method: "POST",
    body: JSON.stringify({
      p_id: row.id, p_user_id: row.userId, p_kind: row.kind, p_plan_tier: row.tier, p_period: row.period,
      p_state: row.state, p_expires_at: row.expiresAt, p_auto_renewing: row.autoRenewing, p_amount_cents: row.amountCents,
    }),
  });
  if (!r.ok) throw new Error(`apply_${r.status}_${await r.text()}`);
}

/** Assinatura com cartão (preapproval): consulta no Mercado Pago e grava. */
export async function syncPreapproval(id: string): Promise<{ state: string } | null> {
  const r = await mp(`/preapproval/${encodeURIComponent(id)}`);
  if (r.status === 404) return null;
  if (!r.ok) throw new Error(`mp_preapproval_${r.status}`);
  const p = await r.json() as Record<string, any>;
  const ref = parseReference(p.external_reference);
  if (!ref || ref.kind !== "sub") return null;
  const status = String(p.status ?? "");
  const next = p.next_payment_date ? new Date(p.next_payment_date) : null;
  // Ativa: vale até a próxima cobrança + 3 dias para a cobrança cair. Cancelada/pausada: vale até o
  // que já foi pago (o banco guarda o maior prazo já visto). Pendente: ainda não pagou nada.
  const state = status === "authorized" ? "ACTIVE" : status === "pending" ? "PENDING" : "CANCELLED";
  const expires = status === "authorized" && next ? new Date(next.getTime() + 3 * 86_400_000).toISOString() : null;
  const amount = Number(p.auto_recurring?.transaction_amount);
  await apply({
    id: `sub:${p.id}`, userId: ref.userId, kind: "SUBSCRIPTION", tier: ref.tier, period: ref.period,
    state, expiresAt: expires, autoRenewing: status === "authorized", amountCents: Number.isFinite(amount) ? Math.round(amount * 100) : null,
  });
  return { state };
}

/** Pagamento único (Pix, cartão ou boleto): consulta e grava. Soma ao período que ainda falta. */
export async function syncPayment(id: string): Promise<{ state: string } | null> {
  const r = await mp(`/v1/payments/${encodeURIComponent(id)}`);
  if (r.status === 404) return null;
  if (!r.ok) throw new Error(`mp_payment_${r.status}`);
  const p = await r.json() as Record<string, any>;
  const ref = parseReference(p.external_reference);
  if (!ref || ref.kind !== "once") return null; // cobranças da assinatura chegam pelo preapproval
  const status = String(p.status ?? "");
  const rowId = `pay:${p.id}`;
  let state: string, expires: string | null = null;
  if (status === "approved") {
    // Já registrado: mantém o prazo calculado na primeira vez (o aviso pode chegar repetido).
    const existing = await rest(`web_subscriptions?select=expires_at&id=eq.${encodeURIComponent(rowId)}`);
    const known = existing.ok ? (await existing.json() as { expires_at?: string }[])[0]?.expires_at : undefined;
    if (known) expires = known;
    else {
      const approved = new Date(p.date_approved ?? Date.now());
      const live = await rest(`web_subscriptions?select=expires_at&user_id=eq.${ref.userId}&plan_tier=eq.${ref.tier}&state=in.(ACTIVE,CANCELLED)&order=expires_at.desc&limit=1`);
      const current = live.ok ? (await live.json() as { expires_at?: string }[])[0]?.expires_at : undefined;
      const base = current && new Date(current) > approved ? new Date(current) : approved;
      expires = addPeriod(base, ref.period).toISOString();
    }
    state = "ACTIVE";
  } else if (status === "refunded" || status === "charged_back") {
    state = "VOIDED"; expires = new Date().toISOString();
  } else if (status === "pending" || status === "in_process" || status === "authorized") {
    state = "PENDING";
  } else {
    state = "EXPIRED";
  }
  const amount = Number(p.transaction_amount);
  await apply({
    id: rowId, userId: ref.userId, kind: "ONE_TIME", tier: ref.tier, period: ref.period,
    state, expiresAt: expires, autoRenewing: false, amountCents: Number.isFinite(amount) ? Math.round(amount * 100) : null,
  });
  return { state };
}

/** Confere a assinatura do aviso (x-signature), quando MP_WEBHOOK_SECRET existe. */
export async function validSignature(request: Request, dataId: string): Promise<boolean> {
  const secret = Deno.env.get("MP_WEBHOOK_SECRET")?.trim();
  if (!secret) return true; // sem segredo, a segurança vem de reconsultar tudo na API
  const header = request.headers.get("x-signature") ?? "";
  // Aviso no formato antigo (IPN, do notification_url) vem sem assinatura: aceita, porque todo
  // estado é reconsultado na API. Só recusa assinatura presente e errada.
  if (!header) return true;
  const parts = Object.fromEntries(header.split(",").map((kv) => kv.trim().split("=", 2) as [string, string]));
  if (!parts.ts || !parts.v1) return false;
  const manifest = `id:${dataId.toLowerCase()};request-id:${request.headers.get("x-request-id") ?? ""};ts:${parts.ts};`;
  const key = await crypto.subtle.importKey("raw", new TextEncoder().encode(secret), { name: "HMAC", hash: "SHA-256" }, false, ["sign"]);
  const mac = new Uint8Array(await crypto.subtle.sign("HMAC", key, new TextEncoder().encode(manifest)));
  const hex = Array.from(mac).map((b) => b.toString(16).padStart(2, "0")).join("");
  return hex === parts.v1;
}
