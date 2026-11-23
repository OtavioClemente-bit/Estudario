// Pagamento pelo site (Mercado Pago), para quem não tem Google Play.
//
// POST /mp-billing (com o token da conta)
//   { action: "checkout", tier: "ESSENCIAL"|"PRO", period: "MONTH"|"YEAR", method: "card"|"once" }
//       card: assinatura no cartão que renova sozinha; once: paga uma vez (Pix, cartão ou boleto).
//       Responde { url } do checkout do Mercado Pago.
//   { action: "confirm", preapprovalId?, paymentId? }
//       Na volta do checkout: consulta na hora (sem esperar o aviso) e responde o plano.
//   { action: "status" }   assinaturas do site desta conta.
//   { action: "cancel", id: "sub:<preapproval>" }   cancela a renovação; vale até o fim do período pago.
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";
import { withCors } from "../_shared/cors.ts";
import { rest, supabase } from "../_shared/play.ts";
import { mp, mpToken, Period, priceCents, reference, SITE, syncPayment, syncPreapproval, Tier } from "../_shared/mercadopago.ts";

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}
function fail(code: string, status: number, message?: string): Response {
  return json({ error: { code, message: message ?? code } }, status);
}

async function emailOf(userId: string): Promise<string | null> {
  const { url, serviceKey } = supabase();
  const r = await fetch(`${url}/auth/v1/admin/users/${userId}`, { headers: { apikey: serviceKey, authorization: `Bearer ${serviceKey}` } });
  if (!r.ok) return null;
  return (await r.json() as { email?: string }).email ?? null;
}

async function profileTier(userId: string): Promise<string> {
  const r = await rest(`profiles?select=plan_tier&user_id=eq.${userId}`);
  return r.ok ? String((await r.json() as { plan_tier?: string }[])[0]?.plan_tier ?? "FREE") : "FREE";
}

async function subscriptions(userId: string) {
  const r = await rest(`web_subscriptions?select=id,kind,plan_tier,period,state,expires_at,auto_renewing,amount_cents&user_id=eq.${userId}&state=in.(ACTIVE,CANCELLED,PENDING)&order=created_at.desc&limit=10`);
  return r.ok ? await r.json() : [];
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return fail("METHOD_NOT_ALLOWED", 405);
  let userId: string;
  try {
    userId = (await authenticateSupabaseRequest(request)).userId;
  } catch (error) {
    if (error instanceof AuthError) return fail(error.code, error.status);
    return fail("AUTH_UNAVAILABLE", 503);
  }
  if (!mpToken()) return fail("BILLING_UNAVAILABLE", 503, "O pagamento pelo site ainda não está aberto.");

  let body: Record<string, unknown>;
  try { body = await request.json(); } catch { return fail("INVALID_BODY", 400); }
  const action = String(body.action ?? "");

  try {
    if (action === "status") {
      return json({ planTier: await profileTier(userId), subscriptions: await subscriptions(userId) });
    }

    if (action === "confirm") {
      const pre = typeof body.preapprovalId === "string" ? body.preapprovalId.trim() : "";
      const pay = typeof body.paymentId === "string" ? body.paymentId.trim() : "";
      if (pre && /^[\w-]{1,80}$/.test(pre)) await syncPreapproval(pre);
      if (pay && /^\d{1,30}$/.test(pay)) await syncPayment(pay);
      return json({ planTier: await profileTier(userId), subscriptions: await subscriptions(userId) });
    }

    if (action === "cancel") {
      const id = String(body.id ?? "");
      if (!id.startsWith("sub:")) return fail("INVALID_BODY", 400);
      const own = await rest(`web_subscriptions?select=id&id=eq.${encodeURIComponent(id)}&user_id=eq.${userId}`);
      if (!own.ok || !(await own.json() as unknown[]).length) return fail("NOT_FOUND", 404);
      const r = await mp(`/preapproval/${encodeURIComponent(id.slice(4))}`, { method: "PUT", body: JSON.stringify({ status: "cancelled" }) });
      if (!r.ok) return fail("CANCEL_FAILED", 502, "Não deu para cancelar agora. Tente de novo em instantes.");
      await syncPreapproval(id.slice(4));
      return json({ planTier: await profileTier(userId), subscriptions: await subscriptions(userId) });
    }

    if (action === "checkout") {
      const tier = body.tier as Tier, period = body.period as Period, method = String(body.method ?? "");
      if ((tier !== "ESSENCIAL" && tier !== "PRO") || (period !== "MONTH" && period !== "YEAR") || (method !== "card" && method !== "once")) {
        return fail("INVALID_BODY", 400);
      }
      const current = await profileTier(userId);
      // Uma assinatura que renova por conta: não deixa assinar de novo o mesmo plano (ou um menor).
      if (method === "card" && (current === tier || current === "PRO")) {
        return fail("ALREADY_SUBSCRIBED", 409, "Você já tem este plano. Ele vale no app e aqui no site.");
      }
      const price = await priceCents(tier, period);
      if (!price) return fail("UNKNOWN_PLAN", 400);
      const email = await emailOf(userId);
      if (!email) return fail("EMAIL_REQUIRED", 400, "Sua conta precisa de um e-mail para pagar pelo site.");
      const amount = price.cents / 100;
      const label = `Estudário ${price.name} · ${period === "YEAR" ? "anual" : "mensal"}`;
      const notify = `${supabase().url}/functions/v1/mp-webhook`;

      if (method === "card") {
        const r = await mp("/preapproval", {
          method: "POST",
          body: JSON.stringify({
            reason: label,
            external_reference: reference("sub", userId, tier, period),
            payer_email: email,
            back_url: `${SITE}/?pagamento=assinatura`,
            notification_url: notify,
            status: "pending",
            auto_recurring: { frequency: period === "YEAR" ? 12 : 1, frequency_type: "months", transaction_amount: amount, currency_id: "BRL" },
          }),
        });
        const p = await r.json() as Record<string, unknown>;
        if (!r.ok || !p.init_point) { console.error("mp-billing preapproval", r.status, JSON.stringify(p)); return fail("CHECKOUT_FAILED", 502, "Não deu para abrir o pagamento agora."); }
        return json({ url: p.init_point });
      }

      const r = await mp("/checkout/preferences", {
        method: "POST",
        body: JSON.stringify({
          items: [{ id: `${tier}-${period}`, title: label, quantity: 1, unit_price: amount, currency_id: "BRL" }],
          payer: { email },
          external_reference: reference("once", userId, tier, period),
          back_urls: { success: `${SITE}/?pagamento=ok`, pending: `${SITE}/?pagamento=pendente`, failure: `${SITE}/?pagamento=falhou` },
          auto_return: "approved",
          notification_url: notify,
          statement_descriptor: "ESTUDARIO",
          payment_methods: { installments: period === "YEAR" ? 12 : 1 },
        }),
      });
      const p = await r.json() as Record<string, unknown>;
      if (!r.ok || !p.init_point) { console.error("mp-billing preference", r.status, JSON.stringify(p)); return fail("CHECKOUT_FAILED", 502, "Não deu para abrir o pagamento agora."); }
      return json({ url: p.init_point });
    }

    return fail("INVALID_ACTION", 400);
  } catch (error) {
    console.error("mp-billing", error instanceof Error ? error.message : error);
    return fail("BILLING_FAILED", 502, "Não deu para falar com o Mercado Pago agora. Tente de novo em instantes.");
  }
}

if (import.meta.main) Deno.serve(withCors(handle));
export { handle };
