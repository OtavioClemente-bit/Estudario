package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.WebConfig
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.httpRequest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.jetbrains.compose.web.attributes.maxLength
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

/**
 * Reportar erro e comentar, como o ReportErrorDialog do app: vai para a mesma tabela
 * (content_reports), que só a equipe lê. Comentário é um reporte com motivo OTHER marcado como
 * comentário do site (app_version "web · comentário"), para separar do erro.
 */
enum class ReportKindWeb { THEORY, SUMMARY, FLASHCARD, QUESTION, TIP, OTHER }

private class Reason(val key: String, val label: String, val hint: String, val icon: String)

private val reasons = listOf(
    Reason("WRONG_ANSWER", "Gabarito errado", "A alternativa certa não é essa", "rule"),
    Reason("WRONG_FACT", "Informação errada", "Algum dado ou conceito está incorreto", "error"),
    Reason("OUTDATED_LAW", "Lei desatualizada", "A norma mudou ou foi revogada", "gavel"),
    Reason("OFF_TOPIC", "Fora do tópico", "Não é sobre o que o tópico pede", "wrong_location"),
    Reason("GENERIC", "Muito genérico", "Raso demais para a prova", "blur_on"),
    Reason("OTHER", "Outro problema", "Conte no campo abaixo", "more_horiz"),
)

private val reactions = listOf("Ótima" to "thumb_up", "Confusa" to "help", "Fácil demais" to "trending_down", "Difícil demais" to "trending_up", "Explicação fraca" to "sentiment_dissatisfied")

/** O que está sendo reportado ou comentado (aberto por qualquer tela; a janela mora no App). */
class ReportTarget(val kind: ReportKindWeb, val excerpt: String, val topic: String?, val comment: Boolean)

object Reports {
    var target by mutableStateOf<ReportTarget?>(null)
    fun report(kind: ReportKindWeb, excerpt: String, topic: String?) { target = ReportTarget(kind, excerpt, topic, comment = false) }
    fun comment(kind: ReportKindWeb, excerpt: String, topic: String?) { target = ReportTarget(kind, excerpt, topic, comment = true) }

    /** null = enviado; senão, a mensagem de erro. */
    suspend fun send(target: ReportTarget, reason: String, comment: String): String? {
        if (Store.demo) return "Entre com sua conta para enviar."
        val token = Auth.accessToken() ?: return "Sua sessão expirou. Entre de novo."
        val body = buildJsonObject {
            put("kind", JsonPrimitive(target.kind.name))
            put("reason", JsonPrimitive(reason))
            put("excerpt", JsonPrimitive(target.excerpt.trim().take(4000).ifBlank { "(sem trecho)" }))
            put("comment", JsonPrimitive(comment.trim().take(1000)))
            put("competition", br.com.estudario.web.data.Queries.primaryCompetition(Store.data)?.name?.take(200)?.let { JsonPrimitive(it) } ?: JsonNull)
            put("topic", target.topic?.take(500)?.let { JsonPrimitive(it) } ?: JsonNull)
            put("app_version", JsonPrimitive(if (target.comment) "web · comentário" else "web"))
        }
        val response = httpRequest(
            "POST", "${WebConfig.SUPABASE_URL}/rest/v1/content_reports",
            mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json", "Prefer" to "return=minimal"),
            body = body.toString(),
        )
        return when {
            response.ok -> null
            response.status == 0 -> "Sem internet. Tente de novo."
            "CONTENT_REPORT_RATE_LIMITED" in response.body -> "Você já enviou muitos hoje. Tente amanhã."
            response.status == 401 -> "Sua sessão expirou. Entre de novo."
            else -> "Não deu para enviar agora. Tente de novo."
        }
    }
}

/** A janela de reportar ou comentar. Montada no App. */
@Composable
fun ReportHost() {
    val target = Reports.target ?: return
    val scope = rememberCoroutineScope()
    var reason by remember(target) { mutableStateOf(if (target.kind == ReportKindWeb.QUESTION) "" else "") }
    var reaction by remember(target) { mutableStateOf<String?>(null) }
    var text by remember(target) { mutableStateOf("") }
    var sending by remember(target) { mutableStateOf(false) }
    var error by remember(target) { mutableStateOf<String?>(null) }
    var sent by remember(target) { mutableStateOf(false) }
    val close = { Reports.target = null }
    val what = when (target.kind) {
        ReportKindWeb.QUESTION -> "questão"; ReportKindWeb.THEORY -> "teoria"; ReportKindWeb.SUMMARY -> "resumo"
        ReportKindWeb.FLASHCARD -> "flashcard"; ReportKindWeb.TIP -> "dica"; ReportKindWeb.OTHER -> "material"
    }
    val canSend = !sending && if (target.comment) (text.isNotBlank() || reaction != null) else reason.isNotBlank() && (reason != "OTHER" || text.isNotBlank())

    Modal(onDismiss = close) {
        if (sent) {
            Div({ classes("report-done") }) {
                Folha("happy", 96)
                H2 { Text(if (target.comment) "Obrigado pelo comentário!" else "Obrigado por avisar!") }
                P({ classes("muted") }) { Text(if (target.comment) "Ele chega direto para a equipe do Estudário e ajuda a melhorar cada $what." else "A equipe vai conferir esta $what. Se estiver errada, ela é corrigida para todo mundo.") }
                Btn("Fechar", close, style = "primary", block = true)
            }
            return@Modal
        }
        Div({ classes("report-head") }) {
            Span({ classes("report-ic", if (target.comment) "chat" else "flag") }) { Icon(if (target.comment) "forum" else "flag", filled = true) }
            Div {
                Span({ classes("eyebrow") }) { Text(if (target.comment) "COMENTAR" else "REPORTAR ERRO") }
                H2 { Text(if (target.comment) "O que você achou desta $what?" else "O que há de errado nesta $what?") }
            }
        }
        if (target.excerpt.isNotBlank()) Div({ classes("report-quote") }) { Text(target.excerpt.take(260) + if (target.excerpt.length > 260) "…" else "") }
        if (target.comment) {
            Div({ classes("report-reactions") }) {
                reactions.forEach { (label, icon) ->
                    Button({ classes(*listOfNotNull("report-chip", if (reaction == label) "on" else null).toTypedArray()); onClick { reaction = if (reaction == label) null else label } }) { Icon(icon); Text(label) }
                }
            }
        } else {
            Div({ classes("report-reasons") }) {
                reasons.filter { target.kind == ReportKindWeb.QUESTION || it.key != "WRONG_ANSWER" }.forEach { r ->
                    Button({ classes(*listOfNotNull("report-reason", if (reason == r.key) "on" else null).toTypedArray()); onClick { reason = r.key } }) {
                        Icon(r.icon)
                        Span { B { Text(r.label) }; Span({ classes("xs", "muted") }) { Text(r.hint) } }
                    }
                }
            }
        }
        TextArea(text) {
            classes("input", "report-text")
            attr("rows", "4"); maxLength(1000)
            attr("placeholder", if (target.comment) "O que poderia melhorar? Escreva à vontade." else if (reason == "OTHER") "Conte o que está errado (obrigatório)" else "Se quiser, explique o que está errado (opcional)")
            onInput { text = it.value }
        }
        Div({ classes("row", "between") }) {
            Span({ classes("xs", "faint") }) { Text("${text.length}/1000 · vai só para a equipe do Estudário") }
        }
        error?.let { Div({ classes("banner", "error") }) { Text(it) } }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", close, style = "ghost")
            Btn(if (sending) "Enviando…" else "Enviar", {
                sending = true; error = null
                scope.launch {
                    val full = if (target.comment) listOfNotNull(reaction?.let { "[$it]" }, text.trim().ifBlank { null }).joinToString(" ") else text
                    val result = Reports.send(target, if (target.comment) "OTHER" else reason, full)
                    sending = false
                    if (result == null) sent = true else error = result
                }
            }, style = "primary", icon = "send", enabled = canSend)
        }
    }
}
