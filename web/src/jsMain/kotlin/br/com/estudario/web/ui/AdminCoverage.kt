package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.WebConfig
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Catalog
import br.com.estudario.web.data.CatalogEntry
import br.com.estudario.web.data.httpRequest
import br.com.estudario.web.data.snapshotJson
import kotlinx.browser.window
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Um tópico do edital que ainda não tem matéria pronta na biblioteca. */
private class Missing(val subject: String, val topic: String)

/**
 * Cobertura da biblioteca nos editais do catálogo, só para a equipe. A lista vem do catálogo, com o
 * quanto de cada edital tem matéria pronta; abrindo um edital, os tópicos que faltam são conferidos
 * ao vivo (library-ready, a mesma regra do gerador), já contando o que foi publicado. "Copiar
 * pedido" monta a lista para pedir as matérias em lote.
 */
@Composable
fun AdminCoverageScreen() {
    if (!isOwner()) {
        Card { Empty("lock", "Página restrita", "Esta página é só da equipe do Estudário.") }
        return
    }
    var entries by remember { mutableStateOf<List<CatalogEntry>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    var sort by remember { mutableStateOf("missing") }
    var open by remember { mutableStateOf<CatalogEntry?>(null) }
    LaunchedEffect(Unit) { entries = runCatching { Catalog.entries() }.getOrElse { error = it.message ?: "Não deu para carregar o catálogo."; emptyList() } }

    PageHead("Cobertura dos editais", "Quanto de cada edital já tem matéria pronta na biblioteca")
    val all = entries
    if (all == null) { CenterMessage { Folha("thinking", 80); Text(error ?: "Carregando o catálogo…") }; return }

    val topics = all.sumOf { it.topicCount }
    val ready = all.sumOf { it.readyTopicCount }
    val full = all.count { it.topicCount > 0 && it.readyTopicCount >= it.topicCount }
    Div({ classes("admin-stats") }) {
        CoverageStat("menu_book", "${all.size}", "editais no catálogo", "#5B5BD6")
        CoverageStat("task_alt", "${if (topics == 0) 0 else ready * 100 / topics}%", "dos tópicos com matéria", "#2FA36B")
        CoverageStat("verified", "$full", "editais completos", "#0E9AA7")
        CoverageStat("pending", "${topics - ready}", "tópicos faltando", "#E8590C")
    }

    Div({ classes("row", "wrap"); attr("style", "gap:10px;margin:12px 0") }) {
        Input(InputType.Search) {
            classes("input"); attr("style", "flex:1;min-width:220px"); attr("placeholder", "Buscar concurso, órgão, cargo ou banca")
            value(query); onInput { query = it.value }
        }
        Div({ classes("filter-chips") }) {
            listOf("missing" to "Mais faltando", "percent" to "Menor %", "name" to "Nome").forEach { (k, label) ->
                Button({ classes(*listOfNotNull("fchip", if (sort == k) "on" else null).toTypedArray()); onClick { sort = k } }) { Text(label) }
            }
        }
    }
    val q = query.trim().lowercase()
    val shown = all
        .filter { q.isEmpty() || q in it.searchNorm || q in it.title.lowercase() || q in (it.board ?: "").lowercase() }
        .sortedWith(when (sort) {
            "percent" -> compareBy<CatalogEntry> { it.readyPercent }.thenByDescending { it.topicCount }
            "name" -> compareBy { it.title }
            else -> compareByDescending<CatalogEntry> { it.topicCount - it.readyTopicCount }.thenBy { it.title }
        })
        .take(200)
    Card {
        Div({ classes("stack") }) {
            shown.forEach { entry ->
                key(entry.id) {
                    val missing = (entry.topicCount - entry.readyTopicCount).coerceAtLeast(0)
                    Button({ classes("pick-row", "flat"); onClick { open = entry } }) {
                        Span({ classes("grow") }) {
                            B { Text(entry.title) }
                            Span({ classes("hint") }) { Text(listOfNotNull(entry.details.ifBlank { null }, "${entry.topicCount} tópicos").joinToString(" · ")) }
                        }
                        Span({ classes("small", "strong"); attr("style", "min-width:120px;text-align:right") }) {
                            Text(if (missing == 0) "completo" else "faltam $missing")
                        }
                        Span({ classes("chip", if (entry.readyPercent >= 90) "green" else if (entry.readyPercent >= 60) "primary" else "red") }) { Text("${entry.readyPercent}%") }
                    }
                }
            }
            if (shown.isEmpty()) P({ classes("muted") }) { Text("Nenhum edital encontrado.") }
            if (all.size > 200 && shown.size == 200) P({ classes("xs", "muted") }) { Text("Mostrando os 200 primeiros. Use a busca para achar os outros.") }
        }
    }
    open?.let { entry -> CoverageModal(entry) { open = null } }
}

@Composable
private fun CoverageStat(icon: String, value: String, label: String, color: String) {
    Div({ classes("admin-stat"); attr("style", "--c:$color") }) {
        Span({ classes("admin-stat-ic") }) { Icon(icon) }
        B { Text(value) }
        Span { Text(label) }
    }
}

/** Os tópicos do edital que ainda não têm matéria pronta, conferidos agora no servidor. */
@Composable
private fun CoverageModal(entry: CatalogEntry, onClose: () -> Unit) {
    var missing by remember(entry.id) { mutableStateOf<List<Missing>?>(null) }
    var total by remember(entry.id) { mutableStateOf(0) }
    var error by remember(entry.id) { mutableStateOf<String?>(null) }
    LaunchedEffect(entry.id) {
        val subjects = runCatching { Catalog.subjects(entry.id) }.getOrElse { error = "Não deu para abrir o edital."; return@LaunchedEffect }
        val items = subjects.flatMapIndexed { s, subject -> subject.topics.mapIndexed { t, topic -> Triple("$s-$t", subject.name, topic) } }
        total = items.size
        val token = Auth.accessToken() ?: run { error = "Entre de novo."; return@LaunchedEffect }
        val ready = hashSetOf<String>()
        for (chunk in items.chunked(400)) {
            val body = buildJsonObject {
                put("items", buildJsonArray {
                    chunk.forEach { (id, subject, topic) -> add(buildJsonObject { put("id", id); put("subjectName", subject); put("topicPath", buildJsonArray { add(JsonPrimitive(topic)) }) }) }
                })
            }.toString()
            val response = runCatching {
                httpRequest("POST", "${WebConfig.SUPABASE_URL}/functions/v1/library-ready", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "Content-Type" to "application/json"), body)
            }.getOrNull()
            if (response == null || !response.ok) { error = "A conferência na biblioteca não respondeu (a função library-ready está publicada?)."; return@LaunchedEffect }
            (runCatching { snapshotJson.parseToJsonElement(response.body).jsonObject["ready"] as? JsonArray }.getOrNull()).orEmpty()
                .mapNotNullTo(ready) { (it as? JsonPrimitive)?.content }
        }
        missing = items.filter { it.first !in ready }.map { Missing(it.second, it.third) }
    }
    Modal(onDismiss = onClose, wide = true) {
        org.jetbrains.compose.web.dom.H2 { Text(entry.title) }
        P({ classes("small", "muted") }) { Text(entry.details) }
        val list = missing
        when {
            error != null -> Div({ classes("banner", "error") }) { Icon("error"); Text(error!!) }
            list == null -> CenterMessage { Folha("thinking", 64); Text("Conferindo os tópicos na biblioteca…") }
            list.isEmpty() -> FolhaSays("happy", 64) { B { Text("Edital completo: todos os $total tópicos têm matéria pronta.") } }
            else -> {
                P { B { Text("Faltam ${list.size} de $total tópicos") } }
                Div({ classes("stack", "tight"); attr("style", "max-height:50vh;overflow:auto") }) {
                    list.groupBy { it.subject }.forEach { (subject, rows) ->
                        B({ classes("small") }) { Text("$subject (${rows.size})") }
                        rows.forEach { row -> Span({ classes("small", "muted"); attr("style", "padding-left:12px") }) { Text("• ${row.topic}") } }
                    }
                }
                Div({ classes("row", "end"); attr("style", "margin-top:12px") }) {
                    Btn("Copiar pedido", {
                        val text = buildString {
                            append("Edital: ${entry.title}").append('\n')
                            entry.details.takeIf { it.isNotBlank() }?.let { append(it).append('\n') }
                            append("Tópicos sem matéria pronta (${list.size}):").append('\n')
                            list.forEach { append("- ${it.subject} › ${it.topic}").append('\n') }
                        }
                        window.navigator.asDynamic().clipboard?.writeText(text)
                        Toast.show("Lista copiada: ${list.size} tópicos")
                    }, style = "primary", icon = "content_copy")
                }
            }
        }
        Div({ classes("row", "end"); attr("style", "margin-top:8px") }) { Btn("Fechar", onClose, style = "ghost") }
    }
}
