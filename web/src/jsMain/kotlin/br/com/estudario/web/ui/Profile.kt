package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.WebConfig
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.httpRequest
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// ------------------------------------------------------------------ Folha (o mascote)

/** O Folha desenhado pelo mesmo gerador do site (folha.js). */
@Composable
fun Folha(mood: String = "happy", size: Int = 96, extraStyle: String = "") {
    val src = remember(mood, size) {
        val svg = runCatching { window.asDynamic().folhaSvg(kotlin.js.json("mood" to mood, "size" to size)) as? String }.getOrNull()
        svg?.let { "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(it) as String } ?: "icon.png"
    }
    Img(src = src, alt = "", attrs = {
        classes("folha")
        attr("style", "width:${size}px;height:${size}px;$extraStyle")
        attr("aria-hidden", "true")
    })
}

/** Fala do Folha: o mascote com um balão, como nas telas do app. */
@Composable
fun FolhaSays(mood: String = "talking", size: Int = 72, content: @Composable () -> Unit) {
    Div({ classes("folha-says") }) {
        key(mood) { Folha(mood, size) }
        Div({ classes("bubble") }) { content() }
    }
}

// ------------------------------------------------------------------ Perfil

@Composable
fun ProfileScreen() {
    val session = Store.session
    val data = Store.data
    val view = Progress.of(data)
    val progress = view.progress
    val streak = view.streak
    val today = Queries.todayDate()
    val activity = Queries.dailyActivity(data).associateBy { it.date }
    val todayRow = activity[today]
    val goal = Prefs.dailyGoal
    val doneToday = todayRow?.questions ?: 0

    // Cabeçalho colorido com avatar, nível e barra de XP.
    Div({ classes("profile-hero") }) {
        Span({ classes("hero-avatar") }) {
            val photo = session?.avatarUrl
            if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
        }
        Div({ classes("grow"); attr("style", "min-width:0") }) {
            H2 { Text(session?.name ?: if (Store.demo) "Demonstração" else "Estudante") }
            P({ classes("small", "clamp-2"); attr("style", "opacity:.85") }) { Text(session?.email ?: "Dados de exemplo") }
            Div({ classes("row", "wrap"); attr("style", "margin-top:10px;gap:8px") }) {
                Span({ classes("hero-pill") }) { Icon("military_tech", filled = true); Text("Nível ${progress.level} · ${progress.levelTitle}") }
                Span({ classes("hero-pill") }) { Icon("local_fire_department", filled = true); Text("${streak.current} dias seguidos") }
            }
        }
        Folha("happy", 96, "margin:-8px -4px -14px 0")
    }

    Div({ classes("grid", "main-side") }) {
        Div({ classes("stack", "loose") }) {
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("NÍVEL") }
                Div({ classes("row", "between"); attr("style", "margin-top:4px") }) {
                    B({ attr("style", "font-size:28px") }) { Text("${progress.totalXp} XP") }
                    Chip("+${progress.xpToday} hoje", tone = "green")
                }
                Div({ attr("style", "margin-top:12px") }) { ProgressBar(progress.levelProgress.toDouble()) }
                P({ classes("small", "muted"); attr("style", "margin-top:8px") }) { Text("Faltam ${progress.xpForNextLevel - progress.xpIntoLevel} XP para o nível ${progress.level + 1} · ${progress.xpThisWeek} XP esta semana") }
            }
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("DE ONDE VEIO SEU XP") }
                val sources = progress.sources.filter { it.xp > 0 }
                val total = sources.sumOf { it.xp }.coerceAtLeast(1)
                if (sources.isEmpty()) P({ classes("small", "muted") }) { Text("Estude, resolva questões e revise para ganhar XP.") }
                Div({ classes("stack"); attr("style", "margin-top:10px") }) {
                    sources.forEach { source ->
                        val pct = source.xp * 100 / total
                        Div({ classes("stack", "tight") }) {
                            Div({ classes("row", "between") }) { Span({ classes("small") }) { Text(source.label) }; B({ classes("small") }) { Text("$pct% · ${source.xp} XP") } }
                            ProgressBar(pct / 100.0)
                        }
                    }
                }
            }
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("FREQUÊNCIA DE ESTUDO") }
                Div({ classes("grid", "cols-3"); attr("style", "margin:10px 0") }) {
                    Stat("${streak.current}", "sequência atual")
                    Stat("${streak.best}", "recorde")
                    Stat("${streak.activeDays}", "dias com estudo")
                }
                Div({ classes("heat") }) {
                    streak.calendar.forEach { day ->
                        val level = br.com.estudario.domain.StreakEngine.level(day, streak.goal)
                        I({ classes(*listOfNotNull(if (level > 0) "l${level.coerceAtMost(3)}" else null).toTypedArray()); attr("title", "${Queries.shortDate(day.date)}: ${day.questions} questões, ${day.minutes} min") })
                    }
                }
            }
        }
        Div({ classes("stack", "loose") }) {
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("META DO DIA") }
                Div({ classes("row"); attr("style", "margin-top:8px") }) {
                    Ring((doneToday.toDouble() / goal).coerceIn(0.0, 1.0)) { B { Text("$doneToday") } }
                    Div({ classes("grow") }) {
                        B { Text(if (doneToday >= goal) "Meta batida!" else "$doneToday de $goal questões") }
                        P({ classes("small", "muted") }) { Text("Uma tarefa do plano ou uma revisão também fecham o dia.") }
                    }
                }
            }
            Card(extra = "pad-lg") {
                Div({ classes("row", "between") }) {
                    Div({ classes("eyebrow") }) { Text("EMBLEMAS · QUASE LÁ") }
                    A(href = "#/perfil/conquistas", { classes("small") }) { Text("Ver todos") }
                }
                Div({ classes("stack"); attr("style", "margin-top:10px") }) {
                    progress.nextBadges.take(3).forEach { item ->
                        key(item.badge.id) {
                            Div({ classes("row") }) {
                                Span({ classes("mini-medal") }) { Icon("military_tech") }
                                Div({ classes("grow") }) {
                                    B({ classes("small") }) { Text(item.badge.name) }
                                    ProgressBar(item.percent.toDouble())
                                    Span({ classes("xs", "muted") }) { Text("${item.current} / ${item.target} ${item.badge.unit}") }
                                }
                            }
                        }
                    }
                    P({ classes("xs", "muted") }) { Text("${progress.earnedBadges.size} de ${progress.badges.size} emblemas conquistados") }
                }
            }
            Card {
                ProfileLink("emoji_events", "Conquistas", "${progress.earnedBadges.size} emblemas", Route.Achievements)
                ProfileLink("query_stats", "Desempenho", "Acerto, minutos e constância", Route.Stats)
                ProfileLink("fact_check", "Histórico e fontes", "De onde veio o seu material", Route.Sources)
                ProfileLink("history", "Histórico do foco", "Tempo por matéria e sessões", Route.FocusHistory)
                ProfileLink("notifications_active", "Notificações", "Lembrete diário e pendências", Route.Notifications)
                ProfileLink("workspace_premium", "Planos e uso", "Limites de IA do seu plano", Route.PlanLimits)
                ProfileLink("tune", "Ajustes", "Aparência, meta diária e questões", Route.Settings)
            }
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("SUA CONTA") }
                P({ classes("small", "muted"); attr("style", "margin:6px 0 12px") }) { Text(if (Store.demo) "Você está na demonstração." else "Conta Google · dados sincronizados com o app do celular.") }
                Btn(if (Store.demo) "Sair da demonstração" else "Sair da conta", { Store.signOut() }, style = "outline", icon = "logout", block = true)
            }
        }
    }
}

@Composable
private fun ProfileLink(icon: String, title: String, subtitle: String, route: Route) {
    A(href = "#/${route.path}", { classes("setting", "link-row") }) {
        Icon(icon)
        Div({ classes("grow") }) { B { Text(title) }; Div({ classes("small", "muted") }) { Text(subtitle) } }
        Icon("chevron_right", extraClass = "faint")
    }
}

@Composable
fun BackToProfile() {
    Div({ classes("row") }) { Btn("Perfil", { Router.go(Route.Profile) }, style = "ghost", small = true, icon = "arrow_back") }
}

// ------------------------------------------------------------------ Histórico e fontes

@Composable
fun SourcesScreen() {
    val data = Store.data
    var byImport by remember { mutableStateOf(false) }
    var officialOnly by remember { mutableStateOf(false) }
    val topics = data.topics.associateBy { it.id }
    val all = data.contentSources.filter { !officialOnly || it.kind == "OFICIAL" }
    BackToProfile()
    PageHead("Histórico e fontes", "De onde veio o seu material")
    Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat("${data.contentSources.size}", "fontes registradas") }
        Card(extra = "flat") { Stat("${data.contentSources.count { it.kind == "OFICIAL" }}", "fontes oficiais") }
        Card(extra = "flat") { Stat("${data.importPackages.size}", "importações") }
    }
    Div({ classes("row", "between", "wrap") }) {
        Div({ classes("segmented") }) {
            Button({ classes(*listOfNotNull(if (!byImport) "on" else null).toTypedArray()); onClick { byImport = false } }) { Text("Por tópico") }
            Button({ classes(*listOfNotNull(if (byImport) "on" else null).toTypedArray()); onClick { byImport = true } }) { Text("Por importação") }
        }
        Switch(officialOnly, "Só fontes oficiais") { officialOnly = it }
    }
    if (all.isEmpty()) {
        Empty("fact_check", "Nenhuma fonte ainda", "Quando você gerar ou importar material, as fontes usadas aparecem aqui.")
        return
    }
    val groups = if (byImport) {
        val names = data.importPackages.associate { it.packageId to (it.fileName.ifBlank { "Importação" } + " · ${it.createdCount} itens") }
        all.groupBy { names[it.packageId] ?: if (it.packageId.isBlank()) "Gerado no app" else "Pacote ${it.packageId.take(8)}" }
    } else all.groupBy { it.topicId?.let { id -> topics[id]?.title } ?: "Geral" }
    Div({ classes("stack") }) {
        groups.forEach { (title, list) ->
            key(title) {
                Card {
                    CardHead(title)
                    Div({ classes("stack") }) {
                        list.forEach { src ->
                            key(src.id) {
                                Div({ classes("source-row") }) {
                                    Chip(if (src.kind == "OFICIAL") "FONTE OFICIAL" else "FONTE COMPLEMENTAR", tone = if (src.kind == "OFICIAL") "green" else null)
                                    B({ classes("small") }) { Text(src.title) }
                                    val meta = listOf(src.publisher, src.reference, src.accessedAt.takeIf { it.isNotBlank() }?.let { "acesso em $it" }).filter { !it.isNullOrBlank() }.joinToString(" · ")
                                    if (meta.isNotBlank()) Div({ classes("xs", "muted") }) { Text(meta) }
                                    src.url?.let { url -> A(href = url, { attr("target", "_blank"); attr("rel", "noopener"); classes("xs") }) { Text("Abrir fonte") } }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Histórico do foco

@Composable
fun FocusHistoryScreen() {
    val data = Store.data
    val topics = data.topics.associateBy { it.id }
    val subjects = data.subjects.associateBy { it.id }
    val sessions = data.focusSessions.sortedByDescending { it.startedAt }
    val totalMin = (sessions.sumOf { it.durationSeconds } / 60).toInt()
    BackToProfile()
    PageHead("Histórico do foco", "Quanto tempo você ficou concentrado") {
        Btn("Iniciar foco", { Router.go(Route.Focus) }, style = "primary", icon = "timer", small = true)
    }
    if (sessions.isEmpty()) {
        Empty("timer", "Nenhuma sessão de foco", "Use o Modo foco para cronometrar o estudo; cada sessão aparece aqui.")
        return
    }
    Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat(Queries.minutesLabel(totalMin), "no total") }
        Card(extra = "flat") { Stat("${sessions.size}", "sessões") }
        Card(extra = "flat") { Stat(Queries.minutesLabel(totalMin / sessions.size.coerceAtLeast(1)), "por sessão") }
    }
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Tempo por matéria")
            val bySubject = sessions.groupBy { s -> s.topicId?.let { topics[it]?.subjectId }?.let { subjects[it]?.name } ?: "Foco livre" }
                .mapValues { (_, list) -> (list.sumOf { it.durationSeconds } / 60).toInt() }
                .entries.sortedByDescending { it.value }
            val max = bySubject.maxOf { it.value }.coerceAtLeast(1)
            Div({ classes("stack") }) {
                bySubject.forEach { (name, minutes) ->
                    key(name) {
                        Div({ classes("stack", "tight") }) {
                            Div({ classes("row", "between") }) { B({ classes("small", "clamp-2") }) { Text(name) }; Span({ classes("small", "muted", "nowrap") }) { Text(Queries.minutesLabel(minutes)) } }
                            ProgressBar(minutes.toDouble() / max)
                        }
                    }
                }
            }
        }
        Card {
            CardHead("Sessões")
            Div({ classes("stack") }) {
                sessions.take(40).forEach { s ->
                    key(s.id) {
                        Div({ classes("row", "between") }) {
                            Div({ attr("style", "min-width:0") }) {
                                B({ classes("small", "clamp-2") }) { Text(s.title.ifBlank { s.topicId?.let { topics[it]?.title } ?: "Foco livre" }) }
                                Div({ classes("xs", "muted") }) { Text(Queries.dayLabel(Queries.dateOf(s.startedAt))) }
                            }
                            Chip(Queries.minutesLabel((s.durationSeconds / 60).toInt()), icon = "timer")
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Notificações

private object NotifyPrefs {
    private fun read(key: String) = runCatching { localStorage.getItem(key) }.getOrNull()
    private fun write(key: String, value: String) { runCatching { localStorage.setItem(key, value) } }
    var daily: Boolean
        get() = read("estudario.notify.daily") == "1"
        set(v) = write("estudario.notify.daily", if (v) "1" else "0")
    var minutes: Int
        get() = read("estudario.notify.time")?.toIntOrNull() ?: (19 * 60)
        set(v) = write("estudario.notify.time", v.toString())
    var pending: Boolean
        get() = read("estudario.notify.pending") != "0"
        set(v) = write("estudario.notify.pending", if (v) "1" else "0")
}

private fun permission(): String = runCatching { js("typeof Notification === 'undefined' ? 'unsupported' : Notification.permission") as String }.getOrDefault("unsupported")

@Composable
fun NotificationsScreen() {
    var daily by remember { mutableStateOf(NotifyPrefs.daily) }
    var minutes by remember { mutableStateOf(NotifyPrefs.minutes) }
    var pending by remember { mutableStateOf(NotifyPrefs.pending) }
    var perm by remember { mutableStateOf(permission()) }
    BackToProfile()
    PageHead("Notificações", "O Folha te lembra de estudar")
    FolhaSays("point") {
        B { Text("Quer que eu te chame na hora de estudar?") }
        P({ classes("small", "muted") }) { Text("No navegador, eu só consigo avisar com o Estudário aberto em alguma aba. Os lembretes no celular continuam pelo app.") }
    }
    if (perm == "default") Btn("Permitir notificações", {
        js("Notification.requestPermission()").then { result: dynamic -> perm = result as String; null }
    }, style = "primary", icon = "notifications")
    if (perm == "denied") Div({ classes("banner") }) { Icon("block"); Text("As notificações estão bloqueadas neste navegador. Libere nas configurações do site.") }
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Lembrete diário")
            Div({ classes("setting") }) {
                Icon("alarm")
                Div({ classes("grow") }) { B { Text("Lembrar de estudar") }; Div({ classes("small", "muted") }) { Text("Um aviso por dia, no horário escolhido") } }
                Switch(daily, "Lembrete diário") { daily = it; NotifyPrefs.daily = it }
            }
            Div({ classes("day-row") }) {
                Div({ classes("grow") }) { Div({ classes("name") }) { Text("Horário") } }
                Stepper(minutes, { minutes = it; NotifyPrefs.minutes = it }, step = 15, min = 5 * 60, max = 23 * 60 + 45, format = { "%02d:%02d".let { _ -> "${(it / 60).toString().padStart(2, '0')}:${(it % 60).toString().padStart(2, '0')}" } }, label = "Horário do lembrete")
            }
        }
        Card {
            CardHead("Pendências e revisões")
            Div({ classes("setting") }) {
                Icon("autorenew")
                Div({ classes("grow") }) { B { Text("Avisar revisões e tarefas atrasadas") }; Div({ classes("small", "muted") }) { Text("Quando houver revisões vencidas ou tarefas do plano para hoje") } }
                Switch(pending, "Pendências e revisões") { pending = it; NotifyPrefs.pending = it }
            }
        }
    }
}

// ------------------------------------------------------------------ Planos e uso

private class Usage(val feature: String, val limit: Int, val used: Int, val remaining: Int, val resetAt: String?)
private class PlanLimit(val feature: String, val periodKind: String, val quota: Int, val maxPerRequest: Int?)
private class PlanInfo(val tier: String, val renewsAt: String?, val plans: List<Pair<String, List<PlanLimit>>>, val usage: List<Usage>)

private fun featureLabel(feature: String) = when (feature) {
    "SYLLABUS_GENERATION" -> "Editais organizados"
    "PLAN_GENERATION" -> "Planos de estudo"
    "CONTENT_GENERATION" -> "Materiais completos"
    "QUESTION_BATCH" -> "Lotes de questões extras"
    "AD_REWARD" -> "Bônus por anúncio"
    "SIMULATION_GENERATION" -> "Simulados no estilo da banca"
    else -> feature
}

private fun featureIcon(feature: String) = when (feature) {
    "SYLLABUS_GENERATION" -> "description"
    "CONTENT_GENERATION" -> "menu_book"
    "QUESTION_BATCH" -> "quiz"
    "SIMULATION_GENERATION" -> "workspace_premium"
    "AD_REWARD" -> "ondemand_video"
    else -> "auto_awesome"
}

private val visibleFeatures = listOf("CONTENT_GENERATION", "SYLLABUS_GENERATION", "SIMULATION_GENERATION", "QUESTION_BATCH")
private fun planName(tier: String) = when (tier) { "ESSENCIAL" -> "Essencial"; "PRO" -> "Pro"; else -> "Grátis" }
private fun planTagline(tier: String) = when (tier) {
    "ESSENCIAL" -> "Para estudar todo dia, matéria por matéria"
    "PRO" -> "Para a reta final, com folga para estudar tudo"
    else -> "Para conhecer o Estudário"
}
private fun limitCopy(l: PlanLimit) = listOfNotNull(
    when (l.periodKind) { "LIFETIME" -> "${l.quota} no total"; "DAILY" -> "${l.quota} por dia"; else -> "${l.quota} por mês" },
    l.maxPerRequest?.let { "até $it questões por lote" },
).joinToString(" · ")
private fun shortIso(iso: String?) = iso?.takeIf { it.length >= 10 }?.let { "${it.substring(8, 10)}/${it.substring(5, 7)}" }

private fun parsePlan(body: String): PlanInfo {
    val root = Json.parseToJsonElement(body).jsonObject
    fun JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull
    fun JsonObject.i(k: String) = (this[k] as? JsonPrimitive)?.intOrNull
    val plans = (root["plans"] as? JsonArray).orEmpty().map { it.jsonObject }.map { p ->
        (p.s("planTier") ?: "FREE") to (p["limits"] as? JsonArray).orEmpty().map { it.jsonObject }.map { l ->
            PlanLimit(l.s("feature") ?: "", l.s("periodKind") ?: "MONTHLY", l.i("quotaLimit") ?: 0, l.i("maxPerRequest"))
        }
    }
    val usage = (root["usage"] as? JsonArray).orEmpty().map { it.jsonObject }.map { u ->
        Usage(u.s("feature") ?: "", u.i("limit") ?: 0, u.i("used") ?: 0, u.i("remaining") ?: 0, u.s("resetAt"))
    }
    return PlanInfo(root.s("planTier") ?: "FREE", root.s("planRenewsAt"), plans, usage)
}

@Composable
fun PlanLimitsScreen() {
    var info by remember { mutableStateOf<PlanInfo?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var attempt by remember { mutableStateOf(0) }
    LaunchedEffect(attempt) {
        error = null
        if (Store.demo) { error = "Entre com sua conta para ver o uso do seu plano."; return@LaunchedEffect }
        val token = Auth.accessToken() ?: run { error = "Sua sessão expirou. Entre de novo."; return@LaunchedEffect }
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/ai-plan", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "x-estudario-client" to "web"))
        }.getOrNull()
        if (response == null || !response.ok) { error = "Não consegui carregar o seu plano agora."; return@LaunchedEffect }
        info = runCatching { parsePlan(response.body) }.getOrNull() ?: run { error = "Resposta inesperada do servidor."; null }
    }
    BackToProfile()
    PageHead("Planos e uso", "Quanto da IA você já usou neste período")
    val current = info
    if (current == null) {
        if (error != null) Card {
            FolhaSays("thinking") { B { Text(error!!) } }
            if (!Store.demo) Btn("Tentar de novo", { attempt++ }, style = "outline", icon = "refresh", small = true)
        } else CenterMessage { Folha("thinking", 80); Text("Carregando seu plano…") }
        return
    }
    Div({ classes("profile-hero") }) {
        Div({ classes("grow") }) {
            Div({ classes("eyebrow"); attr("style", "color:inherit;opacity:.8") }) { Text("SEU PLANO") }
            H2 { Text(planName(current.tier)) }
            P({ classes("small"); attr("style", "opacity:.85") }) { Text(planTagline(current.tier) + (shortIso(current.renewsAt)?.let { " · renova em $it" } ?: "")) }
        }
        Folha("wink", 88, "margin:-8px 0 -14px")
    }
    Card {
        CardHead("Uso deste período")
        Div({ classes("stack") }) {
            current.usage.filter { it.feature in visibleFeatures }.sortedBy { visibleFeatures.indexOf(it.feature) }.forEach { u ->
                key(u.feature) {
                    Div({ classes("row") }) {
                        Span({ classes("mini-medal") }) { Icon(featureIcon(u.feature)) }
                        Div({ classes("grow") }) {
                            Div({ classes("row", "between") }) {
                                B({ classes("small") }) { Text(featureLabel(u.feature)) }
                                Span({ classes("small", "muted") }) { Text("${u.used} de ${u.limit}") }
                            }
                            ProgressBar(if (u.limit == 0) 0.0 else u.used.toDouble() / u.limit, if (u.remaining == 0) "red" else null)
                            Span({ classes("xs", "muted") }) { Text(if (u.remaining == 0) "Acabou" + (shortIso(u.resetAt)?.let { " · volta em $it" } ?: "") else "Restam ${u.remaining}" + (shortIso(u.resetAt)?.let { " · renova em $it" } ?: "")) }
                        }
                    }
                }
            }
        }
    }
    Div({ classes("grid", "cols-3") }) {
        current.plans.forEach { (tier, limits) ->
            key(tier) {
                Card(extra = if (tier == current.tier) "selected" else null) {
                    Div({ classes("row", "between") }) {
                        H2 { Text(planName(tier)) }
                        if (tier == current.tier) Chip("Seu plano", tone = "green")
                    }
                    P({ classes("small", "muted") }) { Text(planTagline(tier)) }
                    Div({ classes("stack", "tight"); attr("style", "margin-top:10px") }) {
                        limits.filter { it.feature in visibleFeatures }.sortedBy { visibleFeatures.indexOf(it.feature) }.forEach { l ->
                            Div({ classes("row") }) { Icon("check", extraClass = "ok"); Span({ classes("small") }) { B { Text(featureLabel(l.feature)) }; Text(" · ${limitCopy(l)}") } }
                        }
                    }
                }
            }
        }
    }
    P({ classes("xs", "faint"); attr("style", "text-align:center") }) { Text("Assinaturas abrem depois do teste fechado. O plano de estudo é montado sem IA e não tem limite.") }
}
