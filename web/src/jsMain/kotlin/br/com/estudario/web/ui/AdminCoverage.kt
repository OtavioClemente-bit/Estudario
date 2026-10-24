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

    var tab by remember { mutableStateOf("uso") }
    PageHead("Cobertura dos editais", "Os editais que as pessoas estão usando e quanto de cada um já tem matéria pronta")
    Div({ classes("filter-chips"); attr("style", "margin-bottom:14px") }) {
        listOf("uso" to "Em uso pelas contas", "catalogo" to "Todo o catálogo", "magros" to "Recortes magros").forEach { (k, label) ->
            Button({ classes(*listOfNotNull("fchip", if (tab == k) "on" else null).toTypedArray()); onClick { tab = k } }) { Text(label) }
        }
    }
    val all = entries
    if (tab == "magros") { ThinSlices(); return }
    if (tab == "uso") {
        EditaisInUse { id -> all?.firstOrNull { it.id == id }?.let { open = it } }
        open?.let { entry -> CoverageModal(entry) { open = null } }
        return
    }
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

/** Um edital em uso, somado de todas as contas (admin-editais-usage). */
private class InUse(
    val catalogId: String?, val title: String, val details: String, val users: Int, val primaryUsers: Int, val paidUsers: Int,
    val topicCount: Int, val readyPercent: Int?, val studiedPercent: Int, val lastActive: String?,
)

/**
 * Os editais que as contas estão usando agora, lidos da cópia na nuvem de cada conta: quantas
 * pessoas, quantas têm ele como principal, quantas pagam, quanto já estudaram e quanto da biblioteca
 * cobre. Tocar num edital do catálogo abre os tópicos que faltam (mesma janela da outra aba).
 */
@Composable
private fun EditaisInUse(onOpenCatalog: (String) -> Unit) {
    var items by remember { mutableStateOf<List<InUse>?>(null) }
    var totals by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    var sort by remember { mutableStateOf("users") }
    var attempt by remember { mutableStateOf(0) }
    LaunchedEffect(attempt) {
        error = null
        val token = Auth.accessToken() ?: run { error = "Entre de novo."; return@LaunchedEffect }
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/admin-editais-usage", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json"))
        }.getOrNull()
        if (response == null || !response.ok) { error = "A contagem não respondeu (a função admin-editais-usage está publicada?)."; return@LaunchedEffect }
        runCatching {
            val root = snapshotJson.parseToJsonElement(response.body).jsonObject
            fun kotlinx.serialization.json.JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.takeIf { it.isString }?.content
            fun kotlinx.serialization.json.JsonObject.i(k: String) = (this[k] as? JsonPrimitive)?.content?.toIntOrNull()
            totals = (root["totals"] as? kotlinx.serialization.json.JsonObject).orEmpty().mapNotNull { (k, v) -> (v as? JsonPrimitive)?.content?.toIntOrNull()?.let { k to it } }.toMap()
            items = (root["items"] as? JsonArray).orEmpty().map { it.jsonObject }.map { o ->
                InUse(o.s("catalogId"), o.s("title").orEmpty(), o.s("details").orEmpty(), o.i("users") ?: 0, o.i("primaryUsers") ?: 0, o.i("paidUsers") ?: 0,
                    o.i("topicCount") ?: 0, o.i("readyPercent"), o.i("studiedPercent") ?: 0, o.s("lastActive"))
            }
        }.onFailure { error = "Resposta inesperada do servidor." }
    }
    val list = items
    if (list == null) {
        if (error != null) Card {
            FolhaSays("thinking") { B { Text(error!!) } }
            Btn("Tentar de novo", { attempt++ }, style = "outline", icon = "refresh", small = true)
        } else CenterMessage { Folha("thinking", 80); Text("Somando os editais de todas as contas…") }
        return
    }
    Div({ classes("admin-stats") }) {
        CoverageStat("group", "${totals["withEditais"] ?: 0}", "contas com edital (de ${totals["accounts"] ?: 0})", "#5B5BD6")
        CoverageStat("menu_book", "${totals["editais"] ?: 0}", "editais diferentes em uso", "#0E9AA7")
        CoverageStat("verified", "${totals["fromCatalog"] ?: 0}", "do catálogo", "#2FA36B")
        CoverageStat("workspace_premium", "${list.sumOf { it.paidUsers }.coerceAtMost(totals["withEditais"] ?: 0)}", "usos por contas pagas", "#E8590C")
    }
    Div({ classes("row", "wrap"); attr("style", "gap:10px;margin:12px 0") }) {
        Input(InputType.Search) {
            classes("input"); attr("style", "flex:1;min-width:220px"); attr("placeholder", "Buscar edital")
            value(query); onInput { query = it.value }
        }
        Div({ classes("filter-chips") }) {
            listOf("users" to "Mais usados", "missing" to "Menos cobertos", "recent" to "Uso recente").forEach { (k, label) ->
                Button({ classes(*listOfNotNull("fchip", if (sort == k) "on" else null).toTypedArray()); onClick { sort = k } }) { Text(label) }
            }
        }
        Btn("Atualizar", { items = null; attempt++ }, style = "ghost", small = true, icon = "refresh")
    }
    val q = query.trim().lowercase()
    val shown = list.filter { q.isEmpty() || q in it.title.lowercase() || q in it.details.lowercase() }.let { rows ->
        when (sort) {
            "missing" -> rows.sortedWith(compareBy<InUse> { it.readyPercent ?: -1 }.thenByDescending { it.users })
            "recent" -> rows.sortedByDescending { it.lastActive.orEmpty() }
            else -> rows
        }
    }
    Card {
        Div({ classes("stack") }) {
            shown.take(300).forEach { item ->
                Button({ classes("pick-row", "flat"); onClick { item.catalogId?.let(onOpenCatalog) } }) {
                    Span({ classes("grow") }) {
                        B { Text(item.title) }
                        Span({ classes("hint") }) {
                            Text(listOfNotNull(
                                item.details.ifBlank { null },
                                "${item.topicCount} tópicos",
                                "${item.studiedPercent}% estudado em média",
                                item.lastActive?.takeIf { it.length >= 10 }?.let { "último uso ${it.substring(8, 10)}/${it.substring(5, 7)}" },
                            ).joinToString(" · "))
                        }
                    }
                    Span({ attr("style", "min-width:150px;text-align:right") }) {
                        B { Text("${item.users} ${if (item.users == 1) "pessoa" else "pessoas"}") }
                        Span({ classes("hint"); attr("style", "display:block") }) {
                            Text(listOfNotNull("${item.primaryUsers} principal", item.paidUsers.takeIf { it > 0 }?.let { "$it pagante${if (it == 1) "" else "s"}" }).joinToString(" · "))
                        }
                    }
                    item.readyPercent?.let { p -> Span({ classes("chip", if (p >= 90) "green" else if (p >= 60) "primary" else "red") }) { Text("$p%") } }
                        ?: Span({ classes("chip") }) { Text("PDF") }
                }
            }
            if (shown.isEmpty()) P({ classes("muted") }) { Text("Nenhum edital em uso ainda.") }
            (totals["unreadable"] ?: 0).takeIf { it > 0 }?.let { P({ classes("xs", "muted") }) { Text("$it conta(s) com a cópia ilegível ficaram de fora.") } }
        }
    }
}

/** Um recorte magro: o pedaço de uma matéria que alguns tópicos abrem, com o que sobra nele. */
private class Thin(
    val topicId: String, val title: String, val chapters: String, val flashcards: Int, val recall: Int,
    val totalFlashcards: Int, val totalRecall: Int, val topics: List<String>, val topicCount: Int,
)

/**
 * Tópicos que abrem só um pedaço de uma matéria e, filtrados pelo recorte, ficam com poucos
 * flashcards ou sem memorização ativa. A lista vem de scripts/biblioteca/recortes-magros.ts (a
 * mesma montagem do servidor). "Copiar pedido" monta o texto para escrever os itens que faltam.
 */
@Composable
private fun ThinSlices() {
    var rows by remember { mutableStateOf<List<Thin>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val token = Auth.accessToken() ?: run { error = "Entre de novo."; return@LaunchedEffect }
        val response = runCatching {
            httpRequest("POST", "${WebConfig.SUPABASE_URL}/rest/v1/rpc/admin_thin_slices", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json", "Accept" to "application/json"), "{}")
        }.getOrNull()
        if (response == null || !response.ok) { error = "A lista ainda não foi gerada (rode scripts/biblioteca/recortes-magros.ts com --gravar)."; return@LaunchedEffect }
        rows = runCatching {
            (snapshotJson.parseToJsonElement(response.body) as? JsonArray).orEmpty().map { it.jsonObject }.map { o ->
                fun s(k: String) = (o[k] as? JsonPrimitive)?.content.orEmpty()
                fun i(k: String) = (o[k] as? JsonPrimitive)?.content?.toIntOrNull() ?: 0
                Thin(s("topic_id"), s("title"), s("chapters"), i("flashcards"), i("recall"), i("total_flashcards"), i("total_recall"),
                    (o["topics"] as? JsonArray).orEmpty().mapNotNull { (it as? JsonPrimitive)?.content }, i("topic_count"))
            }
        }.getOrElse { error = "Resposta inesperada do servidor."; null }
    }
    val list = rows
    if (list == null) { CenterMessage { Folha("thinking", 80); Text(error ?: "Carregando os recortes…") }; return }
    Div({ classes("admin-stats") }) {
        CoverageStat("content_cut", "${list.size}", "recortes magros", "#E8590C")
        CoverageStat("psychology", "${list.count { it.recall == 0 }}", "sem memorização", "#E5484D")
        CoverageStat("style", "${list.count { it.flashcards <= 1 }}", "com 0 ou 1 flashcard", "#5B5BD6")
        CoverageStat("topic", "${list.sumOf { it.topicCount }}", "tópicos de edital afetados", "#0E9AA7")
    }
    P({ classes("small", "muted") }) { Text("Tópicos que abrem só um pedaço de uma matéria e, nesse pedaço, ficam com menos de 4 flashcards ou de 2 itens de memorização. Escrever cartões e memorização desses capítulos resolve todos os tópicos da linha.") }
    Input(InputType.Search) {
        classes("input"); attr("style", "width:100%;margin:8px 0 12px"); attr("placeholder", "Buscar matéria, capítulo ou tópico")
        value(query); onInput { query = it.value }
    }
    val q = query.trim().lowercase()
    val shown = list.filter { q.isEmpty() || q in it.title.lowercase() || q in it.chapters.lowercase() || it.topics.any { t -> q in t.lowercase() } }
    Card {
        Div({ classes("stack") }) {
            shown.take(300).forEach { item ->
                Div({ classes("pick-row", "flat") }) {
                    Span({ classes("grow") }) {
                        B { Text("${item.title} › ${item.chapters}") }
                        Span({ classes("hint") }) { Text("${item.topicCount} tópico(s): " + item.topics.take(3).joinToString(" · ") { it.take(80) }) }
                    }
                    Span({ attr("style", "min-width:150px;text-align:right") }) {
                        B { Text("${item.flashcards} de ${item.totalFlashcards} cartões") }
                        Span({ classes("hint"); attr("style", "display:block") }) { Text("${item.recall} de ${item.totalRecall} de memorização") }
                    }
                    Btn("Copiar pedido", {
                        val text = buildString {
                            append("Matéria: ${item.title} (${item.topicId})\n")
                            append("Capítulos do recorte: ${item.chapters}\n")
                            append("Hoje o recorte entrega ${item.flashcards} flashcards e ${item.recall} itens de memorização ativa.\n")
                            append("Escrever flashcards e memorização ativa sobre esses capítulos (que citem os termos deles).\n")
                            append("Tópicos que abrem esse recorte:\n")
                            item.topics.forEach { append("- $it\n") }
                        }
                        window.navigator.asDynamic().clipboard?.writeText(text)
                        Toast.show("Pedido copiado")
                    }, style = "ghost", small = true, icon = "content_copy")
                }
            }
            if (shown.isEmpty()) P({ classes("muted") }) { Text("Nenhum recorte encontrado.") }
        }
    }
}
