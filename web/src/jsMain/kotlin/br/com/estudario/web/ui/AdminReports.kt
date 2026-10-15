package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.web.WebConfig
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.httpRequest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** A conta do dono do Estudário: só ela vê a página de reportes (o banco confere de novo). */
const val OWNER_USER_ID = "1f75cd98-6341-4cc2-b606-e8ecd53a6c8a"

fun isOwner(): Boolean = !Store.demo && Store.session?.userId == OWNER_USER_ID

private class ReportRow(
    val id: Long, val kind: String, val reason: String, val competition: String?, val topic: String?,
    val excerpt: String, val comment: String, val version: String, val status: String, val createdAt: String, val email: String?,
) {
    val isComment get() = version.contains("comentário")
}

private val kindLabel = mapOf("THEORY" to "Teoria", "SUMMARY" to "Resumo", "FLASHCARD" to "Flashcard", "QUESTION" to "Questão", "TIP" to "Dica", "OTHER" to "Material")
private val reasonLabel = mapOf(
    "WRONG_ANSWER" to "Gabarito errado", "WRONG_FACT" to "Informação errada", "OUTDATED_LAW" to "Lei desatualizada",
    "OFF_TOPIC" to "Fora do tópico", "GENERIC" to "Muito genérico", "OTHER" to "Outro",
)

private suspend fun rpc(name: String, body: String): Pair<Int, String> {
    val token = Auth.accessToken() ?: return 401 to ""
    val response = httpRequest(
        "POST", "${WebConfig.SUPABASE_URL}/rest/v1/rpc/$name",
        mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json", "Accept" to "application/json"),
        body = body,
    )
    return response.status to response.body
}

private fun parseRows(body: String): List<ReportRow> = (Json.parseToJsonElement(body) as? JsonArray).orEmpty().map { it.jsonObject }.map { o ->
    fun s(k: String) = (o[k] as? JsonPrimitive)?.contentOrNull
    ReportRow(
        (o["id"] as? JsonPrimitive)?.longOrNull ?: 0, s("kind") ?: "OTHER", s("reason") ?: "OTHER", s("competition"), s("topic"),
        s("excerpt").orEmpty(), s("comment").orEmpty(), s("app_version").orEmpty(), s("status") ?: "OPEN", s("created_at").orEmpty(), s("reporter_email"),
    )
}

private fun when_(iso: String): String = if (iso.length < 16) iso else "${iso.substring(8, 10)}/${iso.substring(5, 7)} ${iso.substring(11, 16)}"

/** Os reportes de erro e os comentários que chegam do app e do site, para conferir e marcar. */
@Composable
fun AdminReportsScreen() {
    if (!isOwner()) {
        Card { Empty("lock", "Página restrita", "Esta página é só da equipe do Estudário.") }
        return
    }
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("OPEN") }
    var type by remember { mutableStateOf("ALL") }
    var rows by remember { mutableStateOf<List<ReportRow>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var reload by remember { mutableStateOf(0) }
    LaunchedEffect(status, reload) {
        error = null
        val (code, body) = rpc("admin_content_reports", """{"p_status":${if (status == "ALL") "null" else "\"$status\""},"p_limit":500}""")
        if (code in 200..299) rows = runCatching { parseRows(body) }.getOrElse { error = "Resposta inesperada."; emptyList() }
        else error = if ("ADMIN_ONLY" in body) "O banco recusou: esta conta não é a do administrador." else "Não deu para carregar ($code)."
    }
    fun setStatus(row: ReportRow, to: String) {
        scope.launch {
            val (code, _) = rpc("admin_set_report_status", """{"p_id":${row.id},"p_status":"$to"}""")
            if (code in 200..299) {
                rows = rows?.let { list -> if (status == "ALL") list.map { if (it.id == row.id) ReportRow(it.id, it.kind, it.reason, it.competition, it.topic, it.excerpt, it.comment, it.version, to, it.createdAt, it.email) else it } else list.filterNot { it.id == row.id } }
                Toast.show(when (to) { "FIXED" -> "Marcado como corrigido"; "DISMISSED" -> "Descartado"; else -> "Reaberto" })
            } else Toast.show("Não deu para salvar ($code).")
        }
    }

    PageHead("Reportes", "Erros e comentários enviados pelo app e pelo site") {
        Btn("Atualizar", { reload++ }, style = "outline", icon = "refresh", small = true)
    }
    val all = rows.orEmpty()
    val shown = all.filter { when (type) { "ERRORS" -> !it.isComment; "COMMENTS" -> it.isComment; else -> true } }
    Div({ classes("admin-stats") }) {
        AdminStat("${all.count { !it.isComment }}", "erros", "flag", "#E5484D")
        AdminStat("${all.count { it.isComment }}", "comentários", "forum", "var(--primary)")
        AdminStat("${all.count { it.kind == "QUESTION" }}", "em questões", "quiz", "#22C59A")
        AdminStat("${all.count { it.reason == "WRONG_ANSWER" }}", "gabaritos", "rule", "#F2A900")
    }
    Div({ classes("row", "wrap", "between") }) {
        Div({ classes("filter-chips") }) {
            listOf("OPEN" to "Abertos", "FIXED" to "Corrigidos", "DISMISSED" to "Descartados", "ALL" to "Todos").forEach { (k, label) ->
                Button({ classes(*listOfNotNull("fchip", if (status == k) "on" else null).toTypedArray()); onClick { status = k } }) { Text(label) }
            }
        }
        Div({ classes("filter-chips") }) {
            listOf("ALL" to "Tudo", "ERRORS" to "Erros", "COMMENTS" to "Comentários").forEach { (k, label) ->
                Button({ classes(*listOfNotNull("fchip", if (type == k) "on" else null).toTypedArray()); onClick { type = k } }) { Text(label) }
            }
        }
    }
    error?.let { Div({ classes("banner", "error") }) { Text(it) } }
    when {
        rows == null && error == null -> Card { P({ classes("muted") }) { Text("Carregando…") } }
        shown.isEmpty() -> Card { Empty("inbox", "Nada por aqui", if (status == "OPEN") "Nenhum reporte aberto. Quando alguém reportar ou comentar, aparece aqui." else "Nenhum item neste filtro.") }
        else -> Div({ classes("stack") }) {
            shown.forEach { row -> key(row.id) { ReportCard(row) { to -> setStatus(row, to) } } }
        }
    }
}

@Composable
private fun AdminStat(value: String, label: String, icon: String, color: String) {
    Div({ classes("admin-stat"); attr("style", "--c:$color") }) {
        Span({ classes("admin-stat-ic") }) { Icon(icon) }
        Div { B { Text(value) }; Span({ classes("xs", "muted") }) { Text(label) } }
    }
}

@Composable
private fun ReportCard(row: ReportRow, onStatus: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Div({ classes(*listOfNotNull("admin-report", if (row.isComment) "comment" else "error", row.status.lowercase()).toTypedArray()) }) {
        Div({ classes("admin-report-top") }) {
            Span({ classes("admin-tag", if (row.isComment) "c" else "e") }) { Icon(if (row.isComment) "forum" else "flag", filled = true); Text(if (row.isComment) "Comentário" else reasonLabel[row.reason] ?: row.reason) }
            Span({ classes("admin-tag") }) { Text(kindLabel[row.kind] ?: row.kind) }
            Span({ classes("admin-tag") }) { Text(if (row.version.startsWith("web")) "Site" else "App ${row.version}") }
            if (row.status != "OPEN") Span({ classes("admin-tag", "st") }) { Text(if (row.status == "FIXED") "Corrigido" else "Descartado") }
            Span({ classes("grow") })
            Span({ classes("xs", "muted") }) { Text(when_(row.createdAt)) }
        }
        listOfNotNull(row.competition, row.topic).takeIf { it.isNotEmpty() }?.let { Div({ classes("admin-where") }) { Icon("location_on"); Text(it.joinToString("  ›  ")) } }
        if (row.comment.isNotBlank()) Div({ classes("admin-comment") }) { Text("“${row.comment}”") }
        Div({ classes(*listOfNotNull("admin-excerpt", if (open) "open" else null).toTypedArray()); onClick { open = !open } }) { Text(row.excerpt) }
        Div({ classes("admin-report-foot") }) {
            Span({ classes("xs", "faint") }) { Text(row.email ?: "sem e-mail") }
            Span({ classes("grow") })
            Btn(if (open) "Recolher" else "Ver trecho todo", { open = !open }, style = "ghost", small = true)
            Btn("Copiar", { kotlinx.browser.window.navigator.asDynamic().clipboard?.writeText(row.excerpt + if (row.comment.isNotBlank()) "\n\nComentário: " + row.comment else ""); Toast.show("Copiado") }, style = "ghost", small = true, icon = "content_copy")
            if (row.status == "OPEN") {
                Btn("Descartar", { onStatus("DISMISSED") }, style = "outline", small = true, icon = "close")
                Btn(if (row.isComment) "Lido" else "Corrigido", { onStatus("FIXED") }, style = "primary", small = true, icon = "check")
            } else Btn("Reabrir", { onStatus("OPEN") }, style = "outline", small = true, icon = "undo")
        }
    }
}
