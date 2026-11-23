package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.WebConfig
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.httpRequest
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Pagamento pelo site (Mercado Pago), para quem não tem Google Play (iPhone, computador).
 * O plano fica na conta: vale aqui e no app. Quem decide se pagou é sempre o servidor (mp-billing),
 * que consulta o Mercado Pago.
 */
object WebBilling {
    class Sub(val id: String, val kind: String, val tier: String, val period: String, val state: String, val expiresAt: String?, val autoRenewing: Boolean, val amountCents: Int?)
    class Result(val planTier: String?, val subs: List<Sub>, val error: String?, val url: String?)

    suspend fun call(action: String, extra: Map<String, String> = emptyMap()): Result {
        val token = Auth.accessToken() ?: return Result(null, emptyList(), "Sua sessão expirou. Entre de novo.", null)
        val body = buildJsonObject { put("action", action); extra.forEach { (k, v) -> put(k, v) } }.toString()
        val response = runCatching {
            httpRequest(
                "POST", "${WebConfig.SUPABASE_URL}/functions/v1/mp-billing",
                mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json", "x-estudario-client" to "web"),
                body, timeoutMillis = 25_000,
            )
        }.getOrNull() ?: return Result(null, emptyList(), "Sem conexão com o servidor agora.", null)
        val root = runCatching { Json.parseToJsonElement(response.body).jsonObject }.getOrNull()
            ?: return Result(null, emptyList(), "Resposta inesperada do servidor.", null)
        fun JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull
        if (!response.ok) {
            val err = root["error"] as? JsonObject
            return Result(null, emptyList(), err?.s("message") ?: "Não deu para falar com o pagamento agora.", null)
        }
        val subs = (root["subscriptions"] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }.map { o ->
            Sub(o.s("id") ?: "", o.s("kind") ?: "", o.s("plan_tier") ?: "", o.s("period") ?: "", o.s("state") ?: "", o.s("expires_at"),
                (o["auto_renewing"] as? JsonPrimitive)?.booleanOrNull ?: false, (o["amount_cents"] as? JsonPrimitive)?.intOrNull)
        }
        return Result(root.s("planTier"), subs, null, root.s("url"))
    }

    /**
     * Na volta do Mercado Pago (?pagamento=...&preapproval_id=... ou &payment_id=...): confere na hora
     * e avisa. Tira os parâmetros da barra de endereço para um recarregar não repetir.
     */
    fun handleReturn() {
        val params = org.w3c.dom.url.URLSearchParams(window.location.search)
        val kind = params.get("pagamento") ?: return
        val pre = params.get("preapproval_id")
        val pay = params.get("payment_id") ?: params.get("collection_id")
        window.history.replaceState(null, "", window.location.pathname + window.location.hash)
        MainScope().launch {
            val extra = buildMap { pre?.let { put("preapprovalId", it) }; pay?.takeIf { it != "null" }?.let { put("paymentId", it) } }
            val r = call("confirm", extra)
            val active = r.planTier == "ESSENCIAL" || r.planTier == "PRO"
            Toast.show(when {
                r.error != null -> r.error
                active -> "Pagamento confirmado! Seu plano já vale aqui e no app."
                kind == "falhou" -> "O pagamento não foi concluído. Nada foi cobrado."
                else -> "Recebemos o pedido. Assim que o Mercado Pago confirmar (no Pix leva segundos), o plano libera sozinho."
            })
        }
    }
}

/** A escolha de como pagar: cartão que renova sozinho ou pagar uma vez (Pix, cartão ou boleto). */
@Composable
fun PayChooser(tier: String, yearly: Boolean, planName: String, priceText: String, onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    var busy by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    fun go(method: String) {
        if (busy != null) return
        busy = method; error = null
        scope.launch {
            val r = WebBilling.call("checkout", mapOf("tier" to tier, "period" to if (yearly) "YEAR" else "MONTH", "method" to method))
            if (r.url != null) window.location.href = r.url else { error = r.error ?: "Não deu para abrir o pagamento."; busy = null }
        }
    }
    Div({ classes("pay-back"); onClick { if (busy == null) onDismiss() } }) {
        Div({ classes("pay-sheet"); attr("role", "dialog"); attr("aria-modal", "true"); onClick { it.stopPropagation() } }) {
            H2 { Text("Assinar o $planName") }
            P({ classes("pay-sub") }) { Text("$priceText · ${if (yearly) "plano anual" else "plano mensal"}. Vale aqui no site e no app, na mesma conta.") }
            Button({ classes("pay-opt"); if (busy != null) attr("disabled", ""); onClick { go("card") } }) {
                Icon("credit_card")
                Span { B { Text(if (busy == "card") "Abrindo…" else "Cartão de crédito") }; Span { Text(if (yearly) "Renova sozinho a cada ano. Cancela quando quiser." else "Renova sozinho todo mês. Cancela quando quiser.") } }
            }
            Button({ classes("pay-opt"); if (busy != null) attr("disabled", ""); onClick { go("once") } }) {
                Icon("pix")
                Span { B { Text(if (busy == "once") "Abrindo…" else "Pix ou pagamento único") }; Span { Text(if (yearly) "Paga uma vez e vale 1 ano. Não renova sozinho." else "Paga uma vez e vale 1 mês. Não renova sozinho.") } }
            }
            error?.let { P({ classes("pay-err") }) { Text(it) } }
            P({ classes("pay-fine") }) { Text("Pagamento seguro pelo Mercado Pago. O Estudário não vê os dados do seu cartão.") }
            Button({ classes("btn", "small", "k-paper"); onClick { onDismiss() } }) { Text("Voltar") }
        }
    }
}

/** As assinaturas feitas pelo site, com o cancelamento da renovação. */
@Composable
fun WebSubscriptionBox() {
    if (Store.demo) return
    val scope = rememberCoroutineScope()
    var subs by remember { mutableStateOf<List<WebBilling.Sub>>(emptyList()) }
    var busy by remember { mutableStateOf(false) }
    var confirmCancel by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) { subs = WebBilling.call("status").subs }
    val shown = subs.filter { it.state == "ACTIVE" || it.state == "CANCELLED" }
    if (shown.isEmpty()) return
    Div({ classes("pay-mine") }) {
        B { Text("Sua assinatura pelo site") }
        shown.forEach { s ->
            val until = s.expiresAt?.take(10)?.split("-")?.let { (y, m, d) -> "$d/$m/$y" }
            Div({ classes("pay-mine-row") }) {
                Span {
                    Text("${if (s.tier == "PRO") "Pro" else "Essencial"} ${if (s.period == "YEAR") "anual" else "mensal"} · ")
                    Text(when {
                        s.kind == "SUBSCRIPTION" && s.state == "ACTIVE" -> "renova sozinho${until?.let { " até $it" } ?: ""}"
                        else -> "vale até ${until ?: "—"}"
                    })
                }
                if (s.kind == "SUBSCRIPTION" && s.state == "ACTIVE") {
                    if (confirmCancel == s.id) {
                        Button({ classes("btn", "small", "k-danger"); if (busy) attr("disabled", ""); onClick {
                            busy = true
                            scope.launch {
                                val r = WebBilling.call("cancel", mapOf("id" to s.id))
                                busy = false; confirmCancel = null
                                if (r.error != null) Toast.show(r.error) else { subs = r.subs; Toast.show("Renovação cancelada. O plano vale até o fim do período pago.") }
                            }
                        } }) { Text(if (busy) "Cancelando…" else "Confirmar cancelamento") }
                    } else Button({ classes("btn", "small", "k-paper"); onClick { confirmCancel = s.id } }) { Text("Cancelar renovação") }
                }
            }
        }
    }
}
