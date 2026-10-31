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
import br.com.estudario.web.data.httpRequest
import br.com.estudario.web.data.snapshotJson
import kotlinx.browser.window
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private fun JsonElement?.obj(): JsonObject? = this as? JsonObject
private fun JsonObject?.o(k: String): JsonObject? = this?.get(k) as? JsonObject
private fun JsonObject?.a(k: String): List<JsonObject> = (this?.get(k) as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }
private fun JsonObject?.n(k: String): Long = (this?.get(k) as? JsonPrimitive)?.content?.toDoubleOrNull()?.toLong() ?: 0L
private fun JsonObject?.s(k: String): String = (this?.get(k) as? JsonPrimitive)?.content?.takeIf { it != "null" }.orEmpty()
private fun pct(part: Long, total: Long): Int = if (total <= 0) 0 else (part * 100 / total).toInt()
private fun fmtN(v: Long): String = v.toString().reversed().chunked(3).joinToString(".").reversed()

/**
 * O painel da equipe: tudo o que importa da biblioteca e do catálogo num lugar, com os números
 * grandes em cima e, embaixo, o que precisa de trabalho (matérias sem ligação, editais sem nenhuma
 * matéria, o que as pessoas pedem e não tem, reportes, contas e geração). Vem de uma consulta só,
 * admin_team_dashboard, que só a equipe pode chamar.
 */
@Composable
internal fun TeamDashboard() {
    var data by remember { mutableStateOf<JsonObject?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var reload by remember { mutableStateOf(0) }
    LaunchedEffect(reload) {
        data = null; error = null
        val token = Auth.accessToken() ?: run { error = "Entre de novo para ver o painel."; return@LaunchedEffect }
        var failure: String? = null
        val response = runCatching {
            httpRequest("POST", "${WebConfig.SUPABASE_URL}/rest/v1/rpc/admin_team_dashboard", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Content-Type" to "application/json", "Accept" to "application/json"), "{}")
        }.onFailure { failure = it.message ?: it::class.simpleName }.getOrNull()
        if (response == null || !response.ok) {
            // Mostra o que o servidor respondeu, para saber se é permissão, função ausente ou erro na consulta.
            val detail = response?.body?.let { body -> runCatching { snapshotJson.parseToJsonElement(body).obj()?.let { o -> listOf(o.s("code"), o.s("message"), o.s("hint")).filter { it.isNotBlank() }.joinToString(" · ") } }.getOrNull() ?: body.take(300) }
            error = when {
                response == null -> "Não consegui falar com o servidor: ${failure ?: "sem resposta"}."
                response.status == 404 -> "O servidor ainda não achou a consulta do painel (HTTP 404). Pode levar um minuto depois da migração; tente de novo. ${detail.orEmpty()}"
                detail?.contains("FORBIDDEN") == true || detail?.contains("42501") == true -> "Sua conta não está na lista da equipe (app_admins) no servidor. ${detail.orEmpty()}"
                else -> "O servidor respondeu HTTP ${response.status}: ${detail.orEmpty()}"
            }
            return@LaunchedEffect
        }
        data = runCatching { snapshotJson.parseToJsonElement(response.body).obj() }.getOrNull() ?: run { error = "Resposta inesperada do servidor."; null }
    }
    val d = data
    if (d == null) {
        CenterMessage { Folha("thinking", 80); Text(error ?: "Montando o painel…") }
        if (error != null) Div({ attr("style", "display:flex;justify-content:center;margin-top:12px") }) { Btn("Tentar de novo", { reload++ }, style = "primary", icon = "autorenew") }
        return
    }

    val lib = d.o("library"); val routes = d.o("routes"); val cat = d.o("catalog"); val unl = d.o("unlinked"); val misses = d.o("misses")
    val rec = d.o("recortes")
    val reports = d.o("reports"); val sims = d.o("simulados"); val users = d.o("users"); val subs = d.o("subscriptions"); val syl = d.o("userSyllabi"); val jobs = d.o("jobs7d")
    val catPct = pct(cat.n("ready"), cat.n("topics"))
    val linkedPct = pct(routes.n("full") + routes.n("partial"), routes.n("total"))

    Div({ classes("tdash") }) {
        // ---- o topo: os números que mais importam
        Div({ classes("gx-panel", "td-hero") }) {
            Div({ classes("td-hero-top") }) {
                Div({ classes("gx-top-id") }) {
                    Span({ classes("gx-kicker") }) { Text("PAINEL DA EQUIPE") }
                    H2 { Text("Biblioteca e catálogo") }
                    // O painel vem pronto (o servidor recalcula a cada 15 minutos): mostra de quando é.
                    val at = d.s("cachedAt").ifBlank { d.s("generatedAt") }
                    val time = runCatching { kotlin.js.Date(at).toLocaleTimeString("pt-BR").take(5) }.getOrDefault(at.take(16))
                    P({ classes("gx-topic") }) { Text(if (d.s("cachedAt").isNotBlank()) "Calculado às $time · o servidor atualiza a cada 15 minutos" else "Atualizado agora · $time") }
                }
                Button({ classes("gx-ghost", "small"); attr("type", "button"); onClick { reload++ } }) { Icon("autorenew"); Span { Text("Atualizar") } }
            }
            Div({ classes("td-kpis") }) {
                TdKpi("menu_book", fmtN(lib.n("materias")), "matérias na biblioteca", "${lib.n("subjects")} matérias-mãe · ${lib.n("drafts")} rascunhos", "#A78BFF")
                // Questões das matérias + as dos recortes por banca (onde os geradores acrescentam).
                TdKpi("quiz", fmtN(lib.n("questions") + rec.n("questions")), "questões na biblioteca", "${fmtN(lib.n("questions"))} nas matérias · ${fmtN(rec.n("questions"))} nos recortes", "#2FD39A")
                TdKpi("content_cut", fmtN(rec.n("count")), "recortes por banca", "${rec.n("boards")} bancas · +${fmtN(rec.n("questions7d"))} questões em recortes da semana", "#FFC94D")
                TdKpi("folder_open", fmtN(cat.n("editais")), "editais no catálogo", "${fmtN(cat.n("topics"))} tópicos · ${cat.n("boards")} bancas", "#22D3EE")
                TdKpi("check_circle", "$catPct%", "dos tópicos com matéria", "${fmtN(cat.n("ready"))} de ${fmtN(cat.n("topics"))}", if (catPct >= 70) "#2FD39A" else if (catPct >= 40) "#FFC94D" else "#FF5A7A")
                TdKpi("autorenew", "$linkedPct%", "das ligações com matéria", "${fmtN(routes.n("total"))} tópicos ligados", "#6F9BFF")
                TdKpi("warning", fmtN(unl.n("count")), "matérias sem ligação", "publicadas que nenhum tópico usa", "#FF8A3D")
                TdKpi("lock", fmtN(cat.n("zero")), "editais sem nenhuma matéria", "${cat.n("full")} editais completos", "#FF5A7A")
                TdKpi("flag", fmtN(reports.n("open")), "reportes abertos", "${reports.n("week")} nesta semana", "#F472B6")
            }
        }

        Div({ classes("td-grid") }) {
            // ---- cobertura do catálogo
            TdPanel("check_circle", "Cobertura do catálogo", "Quanto dos tópicos dos editais já tem matéria pronta", "#2FD39A") {
                TdBar("Tópicos com matéria", cat.n("ready"), cat.n("topics"), "#2FD39A")
                TdBar("Editais completos", cat.n("full"), cat.n("editais"), "#22D3EE")
                TdBar("Editais sem nenhuma matéria", cat.n("zero"), cat.n("editais"), "#FF5A7A")
                P({ classes("gx-note") }) { Text("${cat.n("subjects")} matérias diferentes nos editais · ${cat.n("drafts")} editais em rascunho") }
            }
            // ---- ligações (rotas tópico do edital → matéria)
            TdPanel("autorenew", "Ligações", "Tópico de edital → matéria da biblioteca", "#6F9BFF") {
                val total = routes.n("total")
                Div({ classes("td-stack") }) {
                    listOf(Triple("Completa", routes.n("full"), "#2FD39A"), Triple("Parcial", routes.n("partial"), "#FFC94D"), Triple("Sem matéria", routes.n("none"), "#FF5A7A")).forEach { (label, v, c) ->
                        if (v > 0) Span({ attr("style", "flex:$v;background:$c"); attr("title", "$label: $v") })
                    }
                }
                Div({ classes("td-legend") }) {
                    listOf(Triple("Completa", routes.n("full"), "#2FD39A"), Triple("Parcial", routes.n("partial"), "#FFC94D"), Triple("Sem matéria", routes.n("none"), "#FF5A7A")).forEach { (label, v, c) ->
                        Span({ attr("style", "--c:$c") }) { I {}; B { Text(fmtN(v)) }; Text(" $label · ${pct(v, total)}%") }
                    }
                }
                P({ classes("gx-note") }) { Text("${fmtN(routes.n("aliases"))} apelidos de tópico ligados direto a uma matéria") }
            }
            // ---- biblioteca: qualidade
            TdPanel("menu_book", "Biblioteca", "O tamanho e o que falta nas matérias", "#A78BFF") {
                val m = lib.n("materias")
                TdBar("Matérias sem questões", lib.n("noQuestions"), m, "#FF5A7A")
                TdBar("Com poucas questões (1 a 9)", lib.n("fewQuestions"), m, "#FFC94D")
                TdBar("Nunca usadas por ninguém", lib.n("neverUsed"), m, "#7F7BB5")
                P({ classes("gx-note") }) { Text("${fmtN(lib.n("hits"))} vezes servidas · ${lib.n("updated7d")} atualizadas nos últimos 7 dias · ${d.n("thinSlices")} recortes magros · média de ${if (m > 0) lib.n("questions") / m else 0} questões por matéria") }
            }
            // ---- contas e uso
            TdPanel("person", "Contas e uso", "Quem está usando e o que estão gerando", "#22D3EE") {
                Div({ classes("td-mini") }) {
                    TdMini(fmtN(users.n("total")), "contas", "+${users.n("new7d")} na semana")
                    TdMini(fmtN(subs.n("active")), "assinaturas ativas", subs.o("byTier")?.entries?.joinToString(" · ") { "${it.key.lowercase()} ${(it.value as? JsonPrimitive)?.content}" }.orEmpty().ifBlank { "—" })
                    TdMini(fmtN(syl.n("total")), "editais próprios", "${syl.n("owners")} contas · ${syl.n("pendingCatalog")} para revisar")
                    TdMini(fmtN(jobs.n("total")), "gerações na semana", "${jobs.n("failed")} falharam")
                    TdMini(fmtN(sims.n("count")), "simulados prontos", "${sims.n("editais")} editais · ${fmtN(sims.n("items"))} questões")
                    TdMini(fmtN(misses.n("count")), "tópicos pedidos sem matéria", "${fmtN(misses.n("requests"))} pedidos")
                }
                val byFeature = jobs.o("byFeature")
                if (byFeature != null && byFeature.isNotEmpty()) P({ classes("gx-note") }) { Text("Gerações por tipo: " + byFeature.entries.sortedByDescending { (it.value as? JsonPrimitive)?.content?.toIntOrNull() ?: 0 }.joinToString(" · ") { "${featureName(it.key)} ${(it.value as? JsonPrimitive)?.content}" }) }
            }
        }

        // ---- listas do que precisa de trabalho
        TdList("warning", "Matérias sem ligação", "Publicadas, mas nenhum tópico de edital aponta para elas: ligue a um tópico (apelido) ou revise o nome.", "#FF8A3D", unl.a("items"),
            rowTitle = { it.s("title") }, rowSub = { "${it.s("subject")} · ${it.n("questions")} questões · ${it.s("id")}" }, rowCopy = { it.s("id") + " · " + it.s("subject") + " › " + it.s("title") }, total = unl.n("count"))
        TdList("lock", "Editais sem nenhuma matéria", "Nenhum tópico destes editais tem matéria pronta ainda: os maiores primeiro.", "#FF5A7A", d.a("catalogZero"),
            rowTitle = { "${it.s("name")} · ${it.s("role")}" }, rowSub = { listOf(it.s("board"), "${it.n("topics")} tópicos").filter { s -> s.isNotBlank() }.joinToString(" · ") }, rowCopy = { "${it.s("name")} · ${it.s("role")} (${it.n("topics")} tópicos)" }, total = cat.n("zero"))
        TdList("chart", "Editais com menos cobertura", "Já têm alguma matéria, mas faltam muitos tópicos.", "#FFC94D", d.a("catalogLeast"),
            rowTitle = { "${it.s("name")} · ${it.s("role")}" }, rowSub = { "${pct(it.n("ready"), it.n("topics"))}% · ${it.n("ready")} de ${it.n("topics")} tópicos" + it.s("board").let { b -> if (b.isBlank()) "" else " · $b" } },
            meter = { pct(it.n("ready"), it.n("topics")) }, rowCopy = { "${it.s("name")} · ${it.s("role")}: ${it.n("ready")}/${it.n("topics")}" })
        TdList("search", "O que as pessoas pedem e não tem", "Tópicos pedidos que não acharam matéria na biblioteca, dos mais pedidos para os menos.", "#22D3EE", misses.a("top"),
            rowTitle = { it.s("topic") }, rowSub = { "${it.s("subject")}${it.s("board").let { b -> if (b.isBlank()) "" else " · $b" }} · ${it.n("requests")} pedidos" }, rowCopy = { "${it.s("subject")} › ${it.s("topic")}" }, total = misses.n("count"))
        TdList("library_books", "Matérias por área", "Quantas matérias, questões e usos em cada matéria-mãe.", "#A78BFF", d.a("bySubject"),
            rowTitle = { it.s("subject") }, rowSub = { "${it.n("materias")} matérias · ${fmtN(it.n("questions"))} questões · ${fmtN(it.n("hits"))} usos" },
            meter = { val max = d.a("bySubject").maxOfOrNull { x -> x.n("materias") } ?: 1L; pct(it.n("materias"), max) }, rowCopy = { "${it.s("subject")}: ${it.n("materias")} matérias" })
        TdList("star", "Matérias mais usadas", "As que mais foram servidas às pessoas.", "#FFC94D", d.a("topHits"),
            rowTitle = { it.s("title") }, rowSub = { "${it.s("subject")} · ${fmtN(it.n("hits"))} usos · ${it.n("questions")} questões" }, rowCopy = { it.s("id") })
        val byKind = reports.o("byKind")
        if (byKind != null && byKind.isNotEmpty()) TdPanel("flag", "Reportes abertos por tipo", "Veja e resolva em Equipe › Reportes", "#F472B6") {
            byKind.entries.sortedByDescending { (it.value as? JsonPrimitive)?.content?.toLongOrNull() ?: 0 }.forEach { (k, v) ->
                TdBar(when (k) { "THEORY" -> "Teoria"; "SUMMARY" -> "Resumo"; "FLASHCARD" -> "Flashcard"; "QUESTION" -> "Questão"; "TIP" -> "Dica"; else -> "Outro" }, (v as? JsonPrimitive)?.content?.toLongOrNull() ?: 0, reports.n("open"), "#F472B6")
            }
        }
    }
}

@Composable
private fun TdKpi(icon: String, value: String, label: String, sub: String, color: String) {
    Div({ classes("td-kpi"); attr("style", "--c:$color") }) {
        Span({ classes("td-kpi-ic") }) { Icon(icon, filled = true) }
        Div {
            B { Text(value) }
            Span({ classes("td-kpi-l") }) { Text(label) }
            Span({ classes("td-kpi-s") }) { Text(sub) }
        }
    }
}

@Composable
private fun TdPanel(icon: String, title: String, hint: String, color: String, content: @Composable () -> Unit) {
    Div({ classes("gx-panel", "td-panel") }) { GxSection(icon, title, hint, color) { content() } }
}

@Composable
private fun TdBar(label: String, value: Long, total: Long, color: String) {
    val p = pct(value, total)
    Div({ classes("td-bar"); attr("style", "--c:$color") }) {
        Div({ classes("td-bar-top") }) { Span { Text(label) }; B { Text("${fmtN(value)} · $p%") } }
        Div({ classes("td-bar-track") }) { Span({ attr("style", "width:${p.coerceAtLeast(if (value > 0) 2 else 0)}%") }) }
    }
}

@Composable
private fun TdMini(value: String, label: String, sub: String) {
    Div({ classes("td-mini-item") }) { B { Text(value) }; Span { Text(label) }; I { Text(sub) } }
}

/** Uma lista de trabalho: busca, as primeiras 8 e "ver todas", e copiar a lista inteira para pedir em lote. */
@Composable
private fun TdList(
    icon: String, title: String, hint: String, color: String, items: List<JsonObject>,
    rowTitle: (JsonObject) -> String, rowSub: (JsonObject) -> String, rowCopy: (JsonObject) -> String, meter: ((JsonObject) -> Int)? = null, total: Long? = null,
) {
    var query by remember(items) { mutableStateOf("") }
    var all by remember(items) { mutableStateOf(false) }
    val q = query.trim().lowercase()
    val shownItems = items.filter { q.isEmpty() || (rowTitle(it) + " " + rowSub(it)).lowercase().contains(q) }
    Div({ classes("gx-panel", "td-panel") }) {
        GxSection(icon, title, hint, color, aside = fmtN(total ?: items.size.toLong())) {
            if (items.isEmpty()) P({ classes("gx-note") }) { Text("Nada aqui. Tudo certo.") }
            else {
                if (items.size > 8) Div({ classes("gx-board", "search") }) {
                    Icon("Lupa")
                    org.jetbrains.compose.web.dom.Input(org.jetbrains.compose.web.attributes.InputType.Search) {
                        classes("gx-input"); value(query); attr("placeholder", "Buscar nesta lista")
                        onInput { query = it.value }
                    }
                    if (query.isNotBlank()) Button({ classes("gx-board-x"); attr("type", "button"); onClick { query = "" } }) { Text("✕") }
                }
                Div({ classes("td-rows") }) {
                    (if (all || q.isNotEmpty()) shownItems else shownItems.take(8)).forEachIndexed { i, it ->
                        key(i, rowTitle(it)) {
                            Div({ classes("td-row"); attr("style", "--c:$color") }) {
                                Span({ classes("td-row-n") }) { Text("${i + 1}") }
                                Div({ classes("td-row-txt") }) {
                                    B { Text(rowTitle(it)) }
                                    Span { Text(rowSub(it)) }
                                    meter?.let { m -> Div({ classes("td-bar-track", "thin") }) { Span({ attr("style", "width:${m(it).coerceIn(2, 100)}%") }) } }
                                }
                            }
                        }
                    }
                }
                Div({ classes("td-actions") }) {
                    if (q.isEmpty() && shownItems.size > 8) Button({ classes("gx-more"); attr("type", "button"); onClick { all = !all } }) { Text(if (all) "Mostrar menos" else "Ver todas (${shownItems.size})") }
                    Button({ classes("gx-ghost", "small"); attr("type", "button"); onClick {
                        val text = shownItems.joinToString("\n") { rowCopy(it) }
                        window.navigator.asDynamic().clipboard?.writeText(text)
                        Toast.show("${shownItems.size} itens copiados")
                    } }) { Icon("content_copy", plain = true); Span { Text("Copiar a lista") } }
                }
            }
        }
    }
}

private fun featureName(k: String): String = when (k) {
    "CONTENT_GENERATION" -> "matérias"
    "QUESTION_BATCH" -> "lotes de questões"
    "SYLLABUS_GENERATION" -> "editais por PDF"
    "PLAN_GENERATION" -> "planos"
    "SIMULATION", "SIMULATION_GENERATION" -> "simulados"
    else -> k.lowercase().replace('_', ' ')
}
