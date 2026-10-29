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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/*
 * Equipe › Cobertura › Contas: cada conta com o plano (e quando renova), o uso dos limites do período,
 * os editais que usa (com quanto já estudou), as matérias geradas, as questões e quando estudou e
 * sincronizou pela última vez. Vem da função admin-editais-usage (o resumo de cada conta fica
 * guardado no servidor e só é refeito para quem sincronizou depois).
 */

private data class AccQuota(val feature: String, val period: String, val limit: Int, val used: Int)
private data class AccEdital(val name: String, val primary: Boolean, val topics: Int, val studied: Int)
private data class AccMaterial(val topic: String, val subject: String, val competition: String, val questions: Int)
private data class AccountRow(
    val email: String?, val createdAt: String?, val lastSignIn: String?, val lastSync: String?, val plan: String, val planRenewsAt: String?,
    val quotas: List<AccQuota>, val jobs: Int, val contentJobs: Int, val failedJobs: Int,
    val editais: List<AccEdital>, val materials: List<AccMaterial>, val questions: Int, val answered: Int, val errors: Int, val lastStudyAt: Long?,
    val hasSummary: Boolean,
)

private fun JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull?.takeIf { it != "null" }
private fun JsonObject.i(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull?.toDoubleOrNull()?.toInt()
private fun JsonObject.o(k: String) = this[k] as? JsonObject
private fun JsonObject.a(k: String) = (this[k] as? JsonArray).orEmpty().mapNotNull { it as? JsonObject }

private val FEATURE_LABEL = mapOf(
    "CONTENT_GENERATION" to "Matérias", "QUESTION_BATCH" to "Lotes de questões", "PLAN_GENERATION" to "Planos",
    "SIMULATION_GENERATION" to "Simulados", "SYLLABUS_GENERATION" to "Editais por PDF", "SAVED_EDITAIS" to "Editais salvos",
)
private val PERIOD_LABEL = mapOf("MONTHLY" to "no mês", "DAILY" to "hoje", "LIFETIME" to "no total")

private fun parse(body: String): Pair<List<AccountRow>, Int> {
    val root = snapshotJson.parseToJsonElement(body).jsonObject
    val pending = root.o("totals")?.i("pending") ?: 0
    return root.a("accounts").map { a ->
        val s = a.o("summary")
        val jobs = a.o("jobs30d")
        AccountRow(
            a.s("email"), a.s("createdAt"), a.s("lastSignIn"), a.s("lastSync"), a.s("plan") ?: "FREE", a.s("planRenewsAt"),
            a.a("quotas").map { AccQuota(it.s("feature").orEmpty(), it.s("period").orEmpty(), it.i("limit") ?: 0, it.i("used") ?: 0) },
            jobs?.i("total") ?: 0, jobs?.i("content") ?: 0, jobs?.i("failed") ?: 0,
            s?.a("editais").orEmpty().map { AccEdital(it.s("name").orEmpty(), it.s("primary") == "true", it.i("topics") ?: 0, it.i("studied") ?: 0) },
            s?.a("materials").orEmpty().map { AccMaterial(it.s("topic").orEmpty(), it.s("subject").orEmpty(), it.s("competition").orEmpty(), it.i("questions") ?: 0) },
            s?.i("questions") ?: 0, s?.i("answered") ?: 0, s?.i("errors") ?: 0, s?.s("lastStudyAt")?.toDoubleOrNull()?.toLong(),
            s != null,
        )
    } to pending
}

private fun ago(iso: String?): String {
    val ms = iso?.let { Date.parse(it) }?.takeIf { !it.isNaN() } ?: return "—"
    return agoMs(ms.toLong())
}
private fun agoMs(ms: Long?): String {
    if (ms == null) return "—"
    val minutes = ((Date.now() - ms) / 60_000).toLong()
    return when {
        minutes < 2 -> "agora"
        minutes < 60 -> "há $minutes min"
        minutes < 48 * 60 -> "há ${minutes / 60} h"
        else -> "há ${minutes / 1440} dias"
    }
}
private fun day(iso: String?): String = iso?.let { Date(it).toLocaleDateString("pt-BR") } ?: "—"

@Composable
internal fun AdminAccounts() {
    var rows by remember { mutableStateOf<List<AccountRow>?>(null) }
    var pending by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    var attempt by remember { mutableStateOf(0) }
    var query by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("ALL") }
    var open by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(attempt) {
        error = null
        val token = Auth.accessToken() ?: run { error = "Entre de novo."; return@LaunchedEffect }
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/admin-editais-usage", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json"))
        }.getOrNull()
        if (response == null || !response.ok) { error = "A lista de contas não respondeu (HTTP ${response?.status ?: "sem resposta"}). Tente de novo."; return@LaunchedEffect }
        runCatching { parse(response.body) }.onSuccess { (list, p) -> rows = list; pending = p }.onFailure { error = "Resposta inesperada do servidor." }
    }
    val list = rows
    if (list == null) {
        if (error != null) Card {
            FolhaSays("thinking") { B { Text(error!!) } }
            Btn("Tentar de novo", { attempt++ }, style = "outline", icon = "refresh", small = true)
        } else CenterMessage { Folha("thinking", 80); Text("Juntando as contas…") }
        return
    }
    val paid = list.count { it.plan != "FREE" }
    val active7 = list.count { r -> listOfNotNull(r.lastSync?.let { Date.parse(it) }, r.lastStudyAt?.toDouble()).any { Date.now() - it < 7 * 86_400_000.0 } }
    Div({ classes("admin-stats") }) {
        AccStat("group", "${list.size}", "contas", "#5B4BF5")
        AccStat("workspace_premium", "$paid", "no plano pago", "#FFB421")
        AccStat("bolt", "$active7", "ativas em 7 dias", "#2FBF8A")
        AccStat("auto_stories", "${list.sumOf { it.materials.size }}", "matérias geradas", "#38B2FF")
    }
    if (pending > 0) Div({ classes("acc-note") }) {
        Text("$pending ${if (pending == 1) "conta ainda está" else "contas ainda estão"} com o resumo sendo atualizado. ")
        Button({ classes("cb-link"); onClick { attempt++ } }) { Text("Atualizar") }
    }
    Div({ classes("acc-tools") }) {
        Input(InputType.Search) { classes("input"); attr("placeholder", "Buscar por e-mail, edital ou matéria"); value(query); onInput { query = it.value } }
        Div({ classes("filter-chips") }) {
            listOf("ALL" to "Todas", "FREE" to "Grátis", "ESSENCIAL" to "Essencial", "PRO" to "Pro").forEach { (k, label) ->
                Button({ classes(*listOfNotNull("fchip", if (plan == k) "on" else null).toTypedArray()); onClick { plan = k } }) { Text(label) }
            }
        }
        Btn("Atualizar", { rows = null; attempt++ }, style = "ghost", icon = "refresh", small = true)
    }
    val q = query.trim().lowercase()
    val shown = list.filter { r ->
        (plan == "ALL" || r.plan == plan) &&
            (q.isEmpty() || (r.email ?: "").lowercase().contains(q) || r.editais.any { it.name.lowercase().contains(q) } || r.materials.any { it.topic.lowercase().contains(q) || it.subject.lowercase().contains(q) })
    }
    if (shown.isEmpty()) Card { Empty("search_off", "Nenhuma conta", "Nada com esse filtro.") }
    shown.forEach { r ->
        val id = r.email ?: r.createdAt ?: r.hashCode().toString()
        key(id) {
            val expanded = open == id
            Div({ classes("acc-card") }) {
                Button({ classes("acc-head"); onClick { open = if (expanded) null else id } }) {
                    Div({ classes("grow") }) {
                        B { Text(r.email ?: "(sem e-mail)") }
                        Span({ classes("acc-sub") }) {
                            Text("Criada em ${day(r.createdAt)} · entrou ${ago(r.lastSignIn)} · sincronizou ${ago(r.lastSync)} · estudou ${agoMs(r.lastStudyAt)}")
                        }
                    }
                    Span({ classes("acc-plan", "p-${r.plan.lowercase()}") }) { Text(when (r.plan) { "ESSENCIAL" -> "Essencial"; "PRO" -> "Pro"; else -> "Grátis" }) }
                    Icon(if (expanded) "expand_less" else "expand_more", extraClass = "faint")
                }
                Div({ classes("acc-quick") }) {
                    Span { B { Text("${r.editais.size}") }; Text(if (r.editais.size == 1) " edital" else " editais") }
                    Span { B { Text("${r.materials.size}") }; Text(" matérias geradas") }
                    Span { B { Text("${r.answered}/${r.questions}") }; Text(" questões respondidas") }
                    Span { B { Text("${r.errors}") }; Text(" erros abertos") }
                    Span { B { Text("${r.contentJobs}") }; Text(" gerações em 30 dias") }
                }
                if (expanded) Div({ classes("acc-body") }) {
                    if (r.planRenewsAt != null) Span({ classes("acc-sub") }) { Text("Plano renova em ${day(r.planRenewsAt)}") }
                    B { Text("Limites do plano") }
                    Div({ classes("acc-quotas") }) {
                        r.quotas.sortedBy { FEATURE_LABEL.keys.indexOf(it.feature) }.forEach { qv ->
                            val pct = if (qv.limit > 0) (qv.used * 100 / qv.limit).coerceIn(0, 100) else 0
                            Div({ classes("acc-quota") }) {
                                Span { Text("${FEATURE_LABEL[qv.feature] ?: qv.feature} ${PERIOD_LABEL[qv.period].orEmpty()}") }
                                Div({ classes("vr-bar") }) { I({ attr("style", "width:$pct%;--c:${if (pct >= 90) "#E5484D" else if (pct >= 60) "#FFB421" else "#2FBF8A"}") }) }
                                B { Text("${qv.used}/${qv.limit}") }
                            }
                        }
                    }
                    if (r.failedJobs > 0) Span({ classes("acc-sub") }) { Text("${r.failedJobs} ${if (r.failedJobs == 1) "geração falhou" else "gerações falharam"} nos últimos 30 dias") }
                    B { Text("Editais") }
                    if (r.editais.isEmpty()) Span({ classes("acc-sub") }) { Text(if (r.hasSummary) "Nenhum edital." else "Ainda sem cópia na nuvem.") }
                    r.editais.forEach { e ->
                        Div({ classes("acc-edital") }) {
                            Span({ classes("grow") }) { Text(e.name); if (e.primary) Span({ classes("acc-tag") }) { Text("principal") } }
                            Span({ classes("acc-sub") }) { Text("${e.studied} de ${e.topics} tópicos estudados") }
                        }
                    }
                    B { Text("Matérias geradas (${r.materials.size})") }
                    if (r.materials.isEmpty()) Span({ classes("acc-sub") }) { Text("Nenhuma ainda.") }
                    Div({ classes("acc-materials") }) {
                        r.materials.sortedWith(compareBy({ it.competition }, { it.subject }, { it.topic })).forEach { m ->
                            Div({ classes("acc-material") }) {
                                Span({ classes("grow") }) { B { Text(m.topic) }; Span({ classes("acc-sub") }) { Text(listOf(m.subject, m.competition).filter { it.isNotBlank() }.joinToString(" · ")) } }
                                Span({ classes("acc-sub") }) { Text("${m.questions} questões") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccStat(icon: String, value: String, label: String, color: String) {
    Div({ classes("admin-stat"); attr("style", "--c:$color") }) {
        Span({ classes("admin-stat-ic") }) { Icon(icon) }
        Div { B { Text(value) }; Span({ classes("xs", "muted") }) { Text(label) } }
    }
}
