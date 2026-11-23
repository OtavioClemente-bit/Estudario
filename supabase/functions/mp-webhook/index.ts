// Avisos do Mercado Pago (pagamento aprovado, assinatura autorizada, cancelada, estorno...).
//
// Quem chama é o Mercado Pago, sem conta. O aviso só diz "mudou o recurso X": o estado verdadeiro é
// sempre buscado de novo na API com o token do vendedor, então um aviso falso não libera nada.
// Responde 200 rápido para o Mercado Pago não ficar repetindo; erro de verdade responde 500 para
// ele tentar de novo.
import { mpToken, syncPayment, syncPreapproval, mp, validSignature } from "../_shared/mercadopago.ts";

async function handle(request: Request): Promise<Response> {
  if (request.method !== "POST") return new Response("ok");
  if (!mpToken()) return new Response("billing off", { status: 200 });
  const url = new URL(request.url);
  let body: Record<string, any> = {};
  try { body = await request.json(); } catch { /* alguns avisos vêm só na URL */ }
  const type = String(body.type ?? body.topic ?? url.searchParams.get("type") ?? url.searchParams.get("topic") ?? "");
  const id = String(body.data?.id ?? url.searchParams.get("data.id") ?? url.searchParams.get("id") ?? "");
  if (!id || !/^[\w-]{1,80}$/.test(id)) return new Response("ignored");
  if (!(await validSignature(request, id))) return new Response("bad signature", { status: 401 });

  try {
    if (type === "payment") await syncPayment(id);
    else if (type === "subscription_preapproval" || type === "preapproval") await syncPreapproval(id);
    else if (type === "subscription_authorized_payment") {
      // cobrança mensal da assinatura: atualiza a assinatura dona dela (o prazo anda junto)
      const r = await mp(`/authorized_payments/${encodeURIComponent(id)}`);
      if (r.ok) {
        const ap = await r.json() as { preapproval_id?: string };
        if (ap.preapproval_id) await syncPreapproval(ap.preapproval_id);
      }
    }
    return new Response("ok");
  } catch (error) {
    console.error("mp-webhook", type, error instanceof Error ? error.message : error);
    return new Response("retry", { status: 500 });
  }
}

if (import.meta.main) Deno.serve(handle);
export { handle };
