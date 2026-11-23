// Avisos do Mercado Pago (pagamento aprovado, assinatura autorizada, cancelada, estorno...).
//
// Quem chama é o Mercado Pago, sem conta. O aviso só diz "mudou o recurso X": o estado verdadeiro é
// sempre buscado de novo na API com o token do vendedor, então um aviso falso não libera nada.
// Responde 200 rápido para o Mercado Pago não ficar repetindo; erro de verdade responde 500 para
// ele tentar de novo. Cada aviso fica anotado em mp_webhook_log (tipo, id e resultado).
import { mpToken, syncPayment, syncPreapproval, mp, validSignature } from "../_shared/mercadopago.ts";
import { rest } from "../_shared/play.ts";

async function note(type: string, id: string, signatureOk: boolean | null, outcome: string): Promise<void> {
  try {
    await rest("mp_webhook_log", { method: "POST", body: JSON.stringify({ type, resource_id: id, signature_ok: signatureOk, outcome: outcome.slice(0, 300) }) });
  } catch { /* o registro nunca derruba o aviso */ }
}

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return new Response("ok");
  if (!mpToken()) return new Response("billing off", { status: 200 });
  const url = new URL(request.url);
  let body: Record<string, any> = {};
  try { body = await request.json(); } catch { /* alguns avisos vêm só na URL */ }
  const type = String(body.type ?? body.topic ?? url.searchParams.get("type") ?? url.searchParams.get("topic") ?? "");
  // O Mercado Pago assina usando o data.id da URL; o corpo pode trazer o mesmo id.
  const id = String(url.searchParams.get("data.id") ?? body.data?.id ?? url.searchParams.get("id") ?? "");
  if (!id || !/^[\w-]{1,80}$/.test(id)) { await note(type, id, null, "ignored: no id"); return new Response("ignored"); }
  const signatureOk = await validSignature(request, id);
  if (!signatureOk) { await note(type, id, false, "bad signature"); return new Response("bad signature", { status: 401 }); }

  try {
    let outcome = "ignored type";
    if (type === "payment") outcome = "payment " + JSON.stringify(await syncPayment(id));
    else if (type === "subscription_preapproval" || type === "preapproval") outcome = "preapproval " + JSON.stringify(await syncPreapproval(id));
    else if (type === "subscription_authorized_payment") {
      // cobrança mensal da assinatura: atualiza a assinatura dona dela (o prazo anda junto)
      const r = await mp(`/authorized_payments/${encodeURIComponent(id)}`);
      outcome = "authorized_payment " + r.status;
      if (r.ok) {
        const ap = await r.json() as { preapproval_id?: string };
        if (ap.preapproval_id) outcome += " " + JSON.stringify(await syncPreapproval(ap.preapproval_id));
      }
    }
    await note(type, id, true, outcome);
    return new Response("ok");
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error);
    console.error("mp-webhook", type, message);
    await note(type, id, true, "error " + message);
    return new Response("retry", { status: 500 });
  }
}

if (import.meta.main) Deno.serve(handle);
export { handle };
