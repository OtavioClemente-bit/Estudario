// Assinaturas do Google Play.
//
// POST /play-billing  { purchaseToken, productId }
//   O app manda o token da compra (nova ou já existente, ao "restaurar"). Aqui a compra é
//   conferida direto com a API do Google Play (purchases.subscriptionsv2), nunca pelo que o app
//   diz, e o plano da conta é atualizado com o que o Google respondeu. Depois a compra é
//   reconhecida (acknowledge) para o Google não estornar em 3 dias. As mudanças seguintes
//   (renovação, cancelamento, reembolso) chegam pelo aviso do Google em play-rtdn.
//
// Segredos: PLAY_SERVICE_ACCOUNT_JSON (conta de serviço com acesso ao Play Console, papel
// "Ver dados financeiros"/"Gerenciar pedidos e assinaturas") e PLAY_PACKAGE_NAME. Sem eles a
// função responde BILLING_UNAVAILABLE e o app mostra os planos como "em breve".
import { authenticateSupabaseRequest, AuthError } from "../_shared/auth.ts";
import { accessToken, serviceAccount, syncSubscription } from "../_shared/play.ts";

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json; charset=utf-8", "cache-control": "no-store" } });
}
function fail(code: string, status: number, message?: string): Response {
  return json({ error: { code, message: message ?? code } }, status);
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

  const account = serviceAccount();
  if (!account) return fail("BILLING_UNAVAILABLE", 503, "As assinaturas ainda não estão abertas.");

  let body: { purchaseToken?: unknown; productId?: unknown };
  try { body = await request.json(); } catch { return fail("INVALID_BODY", 400); }
  const purchaseToken = typeof body.purchaseToken === "string" ? body.purchaseToken.trim() : "";
  const productId = typeof body.productId === "string" ? body.productId.trim() : "";
  if (!purchaseToken || purchaseToken.length > 4096 || !productId || productId.length > 200) return fail("INVALID_BODY", 400);

  try {
    const token = await accessToken(account);
    const result = await syncSubscription(token, userId, purchaseToken, productId);
    if (result === null) return fail("PURCHASE_NOT_FOUND", 404, "O Google não reconheceu esta compra.");
    if (result === "UNKNOWN_PRODUCT") return fail("UNKNOWN_PRODUCT", 400);
    return json({ planTier: result.active ? result.tier : "FREE", state: result.state, expiresAt: result.expiresAt, active: result.active });
  } catch (error) {
    const message = error instanceof Error ? error.message : "";
    if (message === "PURCHASE_OWNED_BY_OTHER") return fail("PURCHASE_OWNED_BY_OTHER", 409, "Esta assinatura já está ligada a outra conta Estudário.");
    console.error("play-billing", message);
    return fail("BILLING_VERIFY_FAILED", 502, "Não deu para confirmar a compra com o Google agora. Tente de novo em instantes.");
  }
}

if (import.meta.main) Deno.serve(handle);
