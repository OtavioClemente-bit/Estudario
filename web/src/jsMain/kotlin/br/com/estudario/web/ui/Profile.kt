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
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
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

/**
 * O Folha. Do tamanho de personagem para cima ele é o mesmo do app, vivo (folha-live.js: respira,
 * pisca, olha em volta, pula ao toque); miniatura dentro de botão fica como desenho parado (o mesmo
 * desenho, um quadro só: folhaPng), para uma lista cheia de botões não virar dezenas de animações.
 */
@Composable
fun Folha(mood: String = "happy", size: Int = 96, extraStyle: String = "", live: Boolean = size >= LIVE_FOLHA_MIN_SIZE, say: String? = null) {
    if (live) {
        TagElement<org.w3c.dom.HTMLElement>(
            elementBuilder = ElementBuilder.createBuilder("estudario-folha"),
            applyAttrs = {
                classes("folha", "folha-live")
                attr("mood", mood)
                attr("size", size.toString())
                // Falando: a boca acompanha as letras do texto (folha-live.js).
                say?.let { attr("say", it) }
                if (extraStyle.isNotBlank()) attr("style", extraStyle)
            },
            content = null,
        )
        return
    }
    val src = remember(mood, size, FolhaLook.version) {
        // O mesmo desenho do Folha vivo (folha-live.js), parado; o SVG antigo fica de reserva.
        runCatching { window.asDynamic().folhaPng(kotlin.js.json("mood" to mood, "size" to size)) as? String }.getOrNull()
            ?: runCatching { window.asDynamic().folhaSvg(kotlin.js.json("mood" to mood, "size" to size)) as? String }.getOrNull()
                ?.let { "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(it) as String }
            ?: "icon.png"
    }
    Img(src = src, alt = "", attrs = {
        classes("folha")
        attr("style", "width:${size}px;height:${size}px;$extraStyle")
        attr("aria-hidden", "true")
    })
}

private const val LIVE_FOLHA_MIN_SIZE = 48

/**
 * Fala do Folha: o mascote com um balão, como nas telas do app. Falando, ele mexe a boca pelo
 * tempo de ler a fala e depois fica ouvindo (o FolhaTalking do app).
 */
@Composable
fun FolhaSays(mood: String = "talking", size: Int = 72, content: @Composable () -> Unit) {
    var shown by remember(mood) { mutableStateOf(mood) }
    LaunchedEffect(mood) {
        if (mood == "talking") {
            kotlinx.coroutines.delay(2200)
            shown = "idle"
        }
    }
    Div({ classes("folha-says") }) {
        key(size) { Folha(shown, size) }
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
    val seed = rememberSeed()
    // tocar numa medalha (vitrine ou "quase lá") abre a medalha em destaque, como nas Conquistas
    var openMedal by remember { mutableStateOf<String?>(null) }
    openMedal?.let { id -> progress.badges.firstOrNull { it.badge.id == id }?.let { MedalDetail(it, progress.badges, onPick = { openMedal = it }, onClose = { openMedal = null }) } }

    // ---- o cabeçalho: avatar no anel do nível, nome, progresso e o Folha comentando
    Div({ classes("pf-hero", "rise") }) {
        Div({ classes("pf-id") }) {
            Div({ classes("pf-avatar"); attr("style", "--p:${(progress.levelProgress * 100).toInt()}"); attr("title", "${(progress.levelProgress * 100).toInt()}% do nível ${progress.level}") }) {
                Span({ classes("pf-photo") }) {
                    val photo = session?.avatarUrl
                    if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
                }
                Span({ classes("pf-level") }) { Text("${progress.level}") }
            }
            Div({ classes("grow"); attr("style", "min-width:0") }) {
                Span({ classes("pf-title") }) { Text("NÍVEL ${progress.level} · ${progress.levelTitle.uppercase()}") }
                H2({ classes("pf-name") }) { Text(session?.name ?: if (Store.demo) "Demonstração" else "Estudante") }
                P({ classes("pf-mail", "clamp-1") }) { Text(session?.email ?: "Dados de exemplo") }
                Div({ classes("pf-xpbar") }) { Span({ attr("style", "width:${(progress.levelProgress * 100).toInt()}%") }) }
                P({ classes("pf-xpline") }) { Text("${progress.xpIntoLevel} de ${progress.xpForNextLevel} XP neste nível · faltam ${progress.xpForNextLevel - progress.xpIntoLevel} para o nível ${progress.level + 1}") }
            }
        }
        Div({ classes("pf-folha") }) {
            FolhaTalking(FolhaLines.forProfile(progress.level, streak.current, progress.xpToday, seed), 104, onFolhaClick = { Router.go(Route.Wardrobe) })
        }
    }

    // ---- os números do caminho
    Div({ classes("pf-kpis") }) {
        Kpi("local_fire_department", "#FF8A3D", "${streak.current}", if (streak.current == 1) "dia seguido" else "dias seguidos", "Recorde: ${streak.best}")
        Kpi("bolt", "#8B7CFF", "${progress.totalXp}", "XP no total", if (progress.xpToday > 0) "+${progress.xpToday} hoje" else "${progress.xpThisWeek} XP esta semana", good = progress.xpToday > 0)
        Kpi("military_tech", "#F5B83D", "${progress.earnedBadges.size}", "de ${progress.badges.size} emblemas", progress.nextBadges.firstOrNull()?.let { "Próximo: ${it.badge.name}" } ?: "Coleção completa")
        A(href = "#/${Route.Stats.path}", { classes("pf-kpi", "goal") }) {
            Div({ classes("pf-goal-ring"); attr("style", "--p:${(doneToday * 100 / goal.coerceAtLeast(1)).coerceAtMost(100)}") }) { B { Text("$doneToday") } }
            Div({ classes("grow"); attr("style", "min-width:0") }) {
                B({ classes("pf-kpi-label") }) { Text(if (doneToday >= goal) "Meta do dia batida!" else "Meta do dia") }
                Span({ classes("pf-kpi-sub") }) { Text(if (doneToday >= goal) "$doneToday questões hoje" else "$doneToday de $goal questões") }
            }
        }
    }

    Div({ classes("grid", "main-side"); attr("style", "margin-top:16px") }) {
        Div({ classes("stack", "loose") }) {
            // ---- a vitrine: as medalhas conquistadas, desenhadas
            Card(extra = "pad-lg pf-showcase") {
                Div({ classes("row", "between") }) {
                    Div {
                        Div({ classes("eyebrow") }) { Text("SUA VITRINE") }
                        B({ classes("pf-card-title") }) { Text(if (progress.earnedBadges.isEmpty()) "Sua primeira medalha está perto" else "${progress.earnedBadges.size} medalhas conquistadas") }
                    }
                    A(href = "#/${Route.Achievements.path}", { classes("btn", "ghost", "small") }) { Text("Ver todas"); Icon("chevron_right") }
                }
                val shelf = progress.earnedBadges.sortedWith(compareByDescending<br.com.estudario.domain.BadgeProgress> { it.badge.tier }).take(8)
                if (shelf.isEmpty()) P({ classes("small", "muted"); attr("style", "margin:10px 0 0") }) { Text("Estude, resolva questões e mantenha a sequência: cada emblema vira uma medalha aqui na vitrine.") }
                else Div({ classes("pf-shelf") }) {
                    shelf.forEach { item -> key(item.badge.id) { MedalButton(item, 76) { openMedal = item.badge.id } } }
                }
            }
            // ---- quase lá: as medalhas mais perto, com o quanto falta
            if (progress.nextBadges.isNotEmpty()) Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("QUASE LÁ") }
                Div({ classes("pf-next") }) {
                    progress.nextBadges.take(3).forEach { item ->
                        key(item.badge.id) {
                            Button({ classes("pf-next-row"); onClick { openMedal = item.badge.id } }) {
                                MedalImg(item.badge, earned = false, size = 56)
                                Div({ classes("grow"); attr("style", "min-width:0") }) {
                                    Div({ classes("row", "between") }) {
                                        B { Text(item.badge.name) }
                                        Span({ classes("pf-next-pct") }) { Text("${(item.percent * 100).toInt()}%") }
                                    }
                                    ProgressBar(item.percent.toDouble())
                                    Span({ classes("xs", "muted") }) { Text("Faltam ${item.remaining} ${item.badge.unit} · ${item.badge.requirement}") }
                                }
                            }
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
            WardrobeCard(progress)
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
            Card {
                ProfileLink("emoji_events", "Conquistas", "${progress.earnedBadges.size} emblemas", Route.Achievements)
                ProfileLink("query_stats", "Desempenho", "Acerto, minutos e constância", Route.Stats)
                ProfileLink("fact_check", "Histórico e fontes", "De onde veio o seu material", Route.Sources)
                ProfileLink("history", "Histórico do foco", "Tempo por matéria e sessões", Route.FocusHistory)
                ProfileLink("notifications_active", "Notificações", "Aviso quando o material ficar pronto", Route.Notifications)
                ProfileLink("workspace_premium", "Planos e uso", "Seu plano, seu mês e a comparação", Route.PlanLimits)
                ProfileLink("tune", "Ajustes", "Aparência, sons, questões por dia e sincronização", Route.Settings)
            }
            Card(extra = "pad-lg") {
                Div({ classes("eyebrow") }) { Text("SUA CONTA") }
                P({ classes("small", "muted"); attr("style", "margin:6px 0 12px") }) { Text(if (Store.demo) "Você está na demonstração." else "Conta Google · dados sincronizados com o app do celular.") }
                Btn(if (Store.demo) "Sair da demonstração" else "Sair da conta", { Store.signOut() }, style = "outline", icon = "logout", block = true)
            }
        }
    }
}

/** Um número do perfil com o ícone colorido e uma linha de contexto. */
@Composable
private fun Kpi(icon: String, color: String, value: String, label: String, sub: String, good: Boolean = false) {
    Div({ classes("pf-kpi"); attr("style", "--k:$color") }) {
        Span({ classes("pf-kpi-ic") }) { Icon(icon, filled = true) }
        Div({ classes("grow"); attr("style", "min-width:0") }) {
            Div({ classes("pf-kpi-value") }) { B { Text(value) }; Span { Text(label) } }
            Span({ classes(*listOfNotNull("pf-kpi-sub", if (good) "good" else null).toTypedArray()) }) { Text(sub) }
        }
    }
}

/** A arte do emblema (a cena ilustrada de cada conquista, em WebP leve); trancado sai apagado. */
@Composable
fun MedalImg(badge: br.com.estudario.domain.Badge, earned: Boolean, size: Int) {
    Img(src = "emblemas/${badge.id}.webp", alt = "", attrs = {
        classes(*listOfNotNull("medal-img", if (earned) null else "locked").toTypedArray())
        attr("style", "width:${size}px;height:${size}px"); attr("aria-hidden", "true"); attr("loading", "lazy"); attr("decoding", "async")
    })
}

@Composable
private fun MedalButton(item: br.com.estudario.domain.BadgeProgress, size: Int, onOpen: () -> Unit) {
    Button({ classes("pf-medal"); attr("title", item.badge.name); attr("aria-label", "Medalha ${item.badge.name}"); onClick { onOpen() } }) {
        MedalImg(item.badge, earned = true, size = size)
        Span({ classes("clamp-2") }) { Text(item.badge.name) }
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
    if (!byImport) { ByTopic(all.groupBy { it.topicId }) { src -> SourceItem(src) }; return }
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
    val scopeTopics = CompetitionFilter.topicIds(data)
    val sessions = data.focusSessions.filter { s -> s.topicId == null || s.topicId in scopeTopics }.sortedByDescending { it.startedAt }
    val totalMin = (sessions.sumOf { it.durationSeconds } / 60).toInt()
    BackToProfile()
    PageHead("Histórico do foco", "Quanto tempo você ficou concentrado") {
        Btn("Iniciar foco", { Router.go(Route.Focus) }, style = "primary", icon = "timer", small = true)
    }
    CompetitionTabs()
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
                            Div({ classes("row"); attr("style", "gap:4px;flex:none") }) {
                                Chip(Queries.minutesLabel((s.durationSeconds / 60).toInt()), icon = "timer")
                                IconButton("delete", "Excluir esta sessão de foco") {
                                    if (window.confirm("Excluir esta sessão de foco do histórico? Isso vale no celular também.")) {
                                        Store.update { snapshot -> br.com.estudario.web.data.Actions.deleteFocusSession(snapshot, s.id) }
                                        Toast.show("Sessão excluída")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Notificações

/**
 * Notificações do computador: só o aviso que vale a pena. Ninguém deixa o site aberto esperando o
 * lembrete de estudar (esse fica com o app do celular); o que importa aqui é saber, de outra aba,
 * que o material que você pediu ficou pronto.
 */
@Composable
fun NotificationsScreen() {
    var on by remember { mutableStateOf(Notify.materialReadyOn) }
    var perm by remember { mutableStateOf(Notify.permission()) }
    BackToProfile()
    PageHead("Notificações", "Um aviso só, e só quando importa")
    FolhaTalking(
        when {
            perm == "granted" && on -> "Combinado! Quando o seu material ficar pronto, eu te chamo, mesmo se você estiver em outra aba."
            perm == "denied" -> "O navegador bloqueou meus avisos. Se quiser, libere nas configurações do site e eu volto a te chamar."
            else -> "Pede o material e vai fazer outra coisa: quando ele ficar pronto, eu te aviso aqui no computador."
        },
        88,
    )
    Card(extra = "pad-lg") {
        Div({ classes("setting") }) {
            Icon("auto_awesome")
            Div({ classes("grow") }) {
                B { Text("Material pronto") }
                Div({ classes("small", "muted") }) { Text("Quando a teoria, os flashcards e as questões de um tópico ficarem prontos, mesmo com o Estudário em outra aba.") }
            }
            Switch(on, "Avisar quando o material ficar pronto") { value ->
                on = value; Notify.materialReadyOn = value
                if (value && perm == "default") Notify.request { perm = it }
            }
        }
        when (perm) {
            "default" -> Div({ classes("row", "wrap"); attr("style", "margin-top:14px") }) {
                Btn("Permitir avisos neste navegador", { Notify.request { perm = it } }, style = "primary", icon = "notifications")
                Span({ classes("xs", "muted") }) { Text("O navegador pergunta uma vez.") }
            }
            "denied" -> Div({ classes("banner"); attr("style", "margin-top:14px") }) { Icon("block", plain = true); Text("Os avisos estão bloqueados neste navegador. Libere nas configurações do site (cadeado ao lado do endereço).") }
            "granted" -> Div({ classes("banner", "ok"); attr("style", "margin-top:14px") }) { Icon("check_circle"); Text("Avisos liberados neste navegador.") }
            else -> Div({ classes("banner"); attr("style", "margin-top:14px") }) { Icon("info"); Text("Este navegador não mostra avisos; o aviso aparece aqui na tela, e o título da aba pisca.") }
        }
    }
    Card(extra = "soft") {
        Div({ classes("row") }) {
            Icon("phone_android")
            Div({ classes("grow") }) {
                B { Text("Lembrete de estudar e revisões") }
                P({ classes("small", "muted") }) { Text("Ficam no app do celular, que te acompanha o dia todo. No app: Mais › Notificações.") }
            }
        }
    }
}

// ------------------------------------------------------------------ Planos e uso

private class Usage(val feature: String, val limit: Int, val used: Int, val remaining: Int, val resetAt: String?)
private class PlanLimit(val feature: String, val periodKind: String, val quota: Int, val maxPerRequest: Int?)
private class Pricing(val name: String, val tagline: String, val monthCents: Int, val yearCents: Int, val featured: Boolean)
private class PlanInfo(val tier: String, val renewsAt: String?, val plans: List<Pair<String, List<PlanLimit>>>, val usage: List<Usage>, val pricing: Map<String, Pricing>, val playEnabled: Boolean, val free: FreeInfo? = null, val planToday: Pair<Int, Int>? = null)

private fun brl(cents: Int): String = "R$ " + (cents / 100).toString().reversed().chunked(3).joinToString(".").reversed() + "," + (cents % 100).toString().padStart(2, '0')

private fun featureLabel(feature: String) = when (feature) {
    "SYLLABUS_GENERATION" -> "Edital pelo seu PDF"
    "PLAN_GENERATION" -> "Planos de estudo"
    "CONTENT_GENERATION" -> "Matérias extras (além do plano do dia)"
    "QUESTION_BATCH" -> "Lotes de 10 questões extras"
    "SAVED_EDITAIS" -> "Editais salvos"
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
    "SAVED_EDITAIS" -> "bookmarks"
    "PLAN_GENERATION" -> "event_note"
    else -> "auto_awesome"
}

/** O plano de estudo é de todos os planos, então não entra como saldo nem na comparação. */
private val visibleFeatures = listOf("CONTENT_GENERATION", "QUESTION_BATCH", "SIMULATION_GENERATION", "SYLLABUS_GENERATION", "SAVED_EDITAIS")
private fun planName(tier: String) = when (tier) { "ESSENCIAL" -> "Essencial"; "PRO" -> "Pro"; else -> "Grátis" }
private fun planTagline(tier: String) = when (tier) {
    "ESSENCIAL" -> "Para estudar todo dia, matéria por matéria"
    "PRO" -> "Para a reta final, com folga para estudar tudo"
    else -> "Para conhecer o Estudário"
}
private fun limitCopy(l: PlanLimit): String {
    if (l.feature == "SAVED_EDITAIS") return if (l.quota >= 1000) "sem limite" else "até ${l.quota}"
    if (l.quota == 0) return "não incluso"
    return listOfNotNull(
        when (l.periodKind) { "LIFETIME" -> if (l.feature == "SIMULATION_GENERATION" || l.feature == "PLAN_GENERATION") "${l.quota} (uma vez)" else "${l.quota} no total"; "DAILY" -> "${l.quota} por dia"; else -> "${l.quota} por mês" },
        when (l.feature) {
            "CONTENT_GENERATION" -> l.maxPerRequest?.let { "até $it questões ao criar" }
            "QUESTION_BATCH" -> if (l.periodKind == "MONTHLY" && l.quota <= 3) "1 por matéria" else null
            else -> null
        },
    ).joinToString(" · ")
}
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
    val pricing = (root["pricing"] as? JsonArray).orEmpty().map { it.jsonObject }.associate { p ->
        (p.s("planTier") ?: "FREE") to Pricing(p.s("name") ?: "", p.s("tagline") ?: "", p.i("priceMonthCents") ?: 0, p.i("priceYearCents") ?: 0, (p["featured"] as? JsonPrimitive)?.contentOrNull == "true")
    }
    val playEnabled = ((root["billing"] as? JsonObject)?.get("playEnabled") as? JsonPrimitive)?.contentOrNull == "true"
    val free = (root["freeAllowance"] as? JsonObject)?.let { f ->
        fun count(k: String) = f[k] as? JsonObject
        FreeInfo(
            count("planDay")?.i("used") ?: 0, count("planDay")?.i("limit") ?: 2,
            count("extras")?.i("used") ?: 0, count("extras")?.i("limit") ?: 0,
            count("weeklyAdvance")?.i("used") ?: 0, count("weeklyAdvance")?.i("limit") ?: 1,
        )
    }
    // Pagos: matérias do plano recebidas hoje sem gastar a cota (usadas, limite).
    val planToday = (root["planToday"] as? JsonObject)?.let { (it.i("used") ?: 0) to (it.i("limit") ?: 4) }
    return PlanInfo(root.s("planTier") ?: "FREE", root.s("planRenewsAt"), plans, usage, pricing, playEnabled, free, planToday)
}

/** As regras de matéria do Grátis, contadas no servidor (dia do plano, extras, adiantar). */
class FreeInfo(val planDayUsed: Int, val planDayLimit: Int, val extrasUsed: Int, val extrasLimit: Int, val weeklyUsed: Int, val weeklyLimit: Int)

/** A conta está no Grátis? null na demonstração ou se não deu para ler. */
suspend fun isFreeTier(): Boolean? = fetchPlanInfo()?.tier?.let { it == "FREE" }

/** O uso do Grátis; null em plano pago, na demonstração ou se não deu para ler. */
suspend fun freeAllowance(): FreeInfo? = fetchPlanInfo()?.free

/** Pagos: matérias do plano de hoje que não gastaram a cota (usadas, limite); null no Grátis ou sem resposta. */
suspend fun planToday(): Pair<Int, Int>? = fetchPlanInfo()?.planToday

/** O saldo de um recurso no plano da conta, como o medidor do app (AiGenerationScreen). */
class Quota(val remaining: Int, val limit: Int, val monthly: Boolean, val resetLabel: String?, val maxPerRequest: Int?)

/** Saldo de [feature] na conta; null na demonstração ou se não deu para ler. */
suspend fun quotaFor(feature: String): Quota? {
    val info = fetchPlanInfo() ?: return null
    val limit = info.plans.firstOrNull { it.first == info.tier }?.second?.firstOrNull { it.feature == feature }
    val usage = info.usage.firstOrNull { it.feature == feature } ?: return null
    return Quota(usage.remaining, usage.limit, limit?.periodKind == "MONTHLY", shortIso(usage.resetAt), limit?.maxPerRequest)
}

private suspend fun fetchPlanInfo(): PlanInfo? {
    if (Store.demo) return null
    val token = Auth.accessToken() ?: return null
    val response = runCatching {
        httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/ai-plan", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "x-estudario-client" to "web"))
    }.getOrNull()?.takeIf { it.ok } ?: return null
    return runCatching { parsePlan(response.body) }.getOrNull()
}

/** Questões ao criar a matéria no plano da conta (Grátis 10, Essencial 20, Pro 30); null se não deu para ler. */
suspend fun materialQuestionLimit(): Int? {
    val info = fetchPlanInfo() ?: return null
    return info.plans.firstOrNull { it.first == info.tier }?.second?.firstOrNull { it.feature == "CONTENT_GENERATION" }?.maxPerRequest
}

/** Limite de um recurso num plano, como está no banco (ai_plan_limits). */
private fun PlanInfo.limitOf(tier: String, feature: String): PlanLimit? = plans.firstOrNull { it.first == tier }?.second?.firstOrNull { it.feature == feature }

/** Cor de cada plano (a mesma do app e dos cartões de oferta). */
private fun tierClass(tier: String) = "tier-" + tier.lowercase()

/**
 * Na demonstração (sem conta), a página mostra o plano Grátis com os números de verdade dos planos,
 * para quem está conhecendo o Estudário ver tudo o que cada um tem.
 */
private fun demoPlanInfo(): PlanInfo {
    fun l(feature: String, period: String, quota: Int, perRequest: Int? = null) = PlanLimit(feature, period, quota, perRequest)
    val plans = listOf(
        "FREE" to listOf(l("CONTENT_GENERATION", "MONTHLY", 120, 10), l("QUESTION_BATCH", "MONTHLY", 3), l("SIMULATION_GENERATION", "LIFETIME", 1), l("SYLLABUS_GENERATION", "LIFETIME", 0), l("SAVED_EDITAIS", "LIFETIME", 5), l("PLAN_GENERATION", "LIFETIME", 1)),
        "ESSENCIAL" to listOf(l("CONTENT_GENERATION", "MONTHLY", 25, 20), l("QUESTION_BATCH", "MONTHLY", 10), l("SIMULATION_GENERATION", "MONTHLY", 3), l("SYLLABUS_GENERATION", "MONTHLY", 2), l("SAVED_EDITAIS", "LIFETIME", 15), l("PLAN_GENERATION", "MONTHLY", 3)),
        "PRO" to listOf(l("CONTENT_GENERATION", "MONTHLY", 40, 30), l("QUESTION_BATCH", "MONTHLY", 25), l("SIMULATION_GENERATION", "MONTHLY", 6), l("SYLLABUS_GENERATION", "MONTHLY", 5), l("SAVED_EDITAIS", "LIFETIME", 1000), l("PLAN_GENERATION", "MONTHLY", 10)),
    )
    val pricing = mapOf(
        "FREE" to Pricing("Grátis", "Para conhecer o Estudário", 0, 0, false),
        "ESSENCIAL" to Pricing("Essencial", "Para estudar todo dia, matéria por matéria", 2490, 24900, true),
        "PRO" to Pricing("Pro", "Para a reta final, com folga para estudar tudo", 4990, 49900, false),
    )
    val usage = listOf(Usage("QUESTION_BATCH", 3, 1, 2, null), Usage("SIMULATION_GENERATION", 1, 0, 1, null))
    return PlanInfo("FREE", null, plans, usage, pricing, false, FreeInfo(1, 3, 3, 10, 0, 1))
}

/** Uma linha da comparação: o nome, uma explicação curta e o valor em cada plano. */
private class Compare(val label: String, val hint: String, val value: (String) -> String)

private fun comparisonGroups(info: PlanInfo): List<Pair<String, List<Compare>>> {
    fun quota(tier: String, feature: String) = info.limitOf(tier, feature)?.quota ?: 0
    fun perMonth(n: Int) = if (n == 1) "1 por mês" else "$n por mês"
    return listOf(
        "Estudar todo dia" to listOf(
            Compare("Matéria do plano de hoje", "Teoria, resumo, flashcards e questões do que o seu plano marcou para hoje.") { "Livre" },
            Compare("Adiantar o plano", "Estudar hoje a matéria de amanhã.") { if (it == "FREE") "Com anúncio curto" else "Livre" },
            Compare("Matérias novas por dia", "Quantas matérias novas o plano traz em cada dia.") { if (it == "FREE") "Até 2" else "Até 4, e mais se quiser" },
            Compare("Matérias fora do plano", "Qualquer tópico do seu edital, na hora que você quiser.") { if (it == "FREE") "—" else perMonth(quota(it, "CONTENT_GENERATION")) },
        ),
        "Treinar" to listOf(
            Compare("Questões em cada matéria", "Comentadas, no estilo da sua banca.") { "${info.limitOf(it, "CONTENT_GENERATION")?.maxPerRequest ?: if (it == "FREE") 10 else 20}" },
            Compare("Mais 10 questões", "Para treinar o tópico até ficar firme.") { if (it == "FREE") "3 por mês" else perMonth(quota(it, "QUESTION_BATCH")) },
            Compare("Simulados da sua banca", "Com cronômetro e nota por matéria, no formato da prova.") { if (it == "FREE") "1 diagnóstico" else perMonth(quota(it, "SIMULATION_GENERATION")) },
        ),
        "Organizar" to listOf(
            Compare("Edital pelo seu PDF", "Para concurso que não está no nosso catálogo: as matérias e os tópicos saem organizados.") { if (quota(it, "SYLLABUS_GENERATION") == 0) "—" else perMonth(quota(it, "SYLLABUS_GENERATION")) },
            Compare("Editais salvos", "Concursos que você acompanha ao mesmo tempo.") { quota(it, "SAVED_EDITAIS").let { n -> if (n >= 1000) "Sem limite" else "Até $n" } },
            Compare("Planos de estudo", "Plano montado para a sua rotina e a data da prova.") { if (it == "FREE") "1" else perMonth(quota(it, "PLAN_GENERATION")) },
            Compare("Anúncios", "No Grátis, só quando você escolhe adiantar.") { if (it == "FREE") "Só se você quiser" else "Nenhum" },
        ),
    )
}

/** O que vem em todos os planos, inclusive o Grátis. */
private val everyPlan = listOf(
    "event_repeat" to "Revisões no tempo certo",
    "style" to "Flashcards e caderno de erros",
    "timer" to "Modo foco e desempenho",
    "devices" to "No app e no site, na mesma conta",
)

private val faq = listOf(
    "Como eu assino?" to "Pelo app do Estudário no celular, com a Google Play. O plano vale na hora na sua conta, no app e aqui no site.",
    "Posso cancelar quando quiser?" to "Sim. A assinatura é da Google Play e você cancela por lá, sem multa. O plano continua até o fim do período pago.",
    "O que é matéria fora do plano?" to "O plano marca o que estudar em cada dia. Fora do plano é qualquer outro tópico do seu edital que você queira estudar antes, na hora que quiser: isso é do Essencial e do Pro. No Grátis, você estuda o que o plano traz e pode adiantar as próximas.",
    "O que acontece com o que eu já estudei se eu mudar de plano?" to "Nada se perde: matérias, questões, revisões e o seu histórico continuam iguais em qualquer plano.",
)

@Composable
fun PlanLimitsScreen() {
    var info by remember { mutableStateOf<PlanInfo?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var attempt by remember { mutableStateOf(0) }
    LaunchedEffect(attempt) {
        error = null
        if (Store.demo) { info = demoPlanInfo(); return@LaunchedEffect }
        val token = Auth.accessToken() ?: run { error = "Sua sessão expirou. Entre de novo."; return@LaunchedEffect }
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/ai-plan", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "x-estudario-client" to "web"))
        }.getOrNull()
        if (response == null || !response.ok) { error = "Não consegui carregar o seu plano agora."; return@LaunchedEffect }
        info = runCatching { parsePlan(response.body) }.getOrNull() ?: run { error = "Resposta inesperada do servidor."; null }
    }
    BackToProfile()
    val current = info
    if (current == null) {
        PageHead("Planos e uso", "")
        if (error != null) Card {
            FolhaSays("thinking") { B { Text(error!!) } }
            Btn("Tentar de novo", { attempt++ }, style = "outline", icon = "refresh", small = true)
        } else CenterMessage { Folha("thinking", 80); Text("Carregando seu plano…") }
        return
    }
    val tiers = listOf("FREE", "ESSENCIAL", "PRO").filter { t -> current.plans.any { it.first == t } }
    var yearly by remember { mutableStateOf(false) }

    // ---------- seu plano
    Div({ classes("pl-hero", tierClass(current.tier)) }) {
        Div({ classes("pl-hero-glow") })
        Div({ classes("pl-hero-text") }) {
            Span({ classes("pl-eyebrow") }) { Text("SEU PLANO") }
            org.jetbrains.compose.web.dom.H1 { Text(current.pricing[current.tier]?.name?.ifBlank { null } ?: planName(current.tier)) }
            P { Text(current.pricing[current.tier]?.tagline?.ifBlank { null } ?: planTagline(current.tier)) }
            Span({ classes("pl-status") }) {
                Icon(if (current.tier == "FREE") "favorite" else "verified", filled = true)
                Text(when {
                    Store.demo -> "Demonstração · entre com sua conta para ver o seu uso"
                    current.tier == "FREE" -> "Grátis para sempre"
                    else -> shortIso(current.renewsAt)?.let { "Ativo · renova em $it" } ?: "Ativo"
                })
            }
        }
        Folha(if (current.tier == "FREE") "wave" else "proud", 120, "margin:-12px -6px -22px 0")
    }

    // ---------- seu mês
    Div({ classes("pl-section-head") }) {
        org.jetbrains.compose.web.dom.H2 { Text("Seu mês") }
        current.usage.firstNotNullOfOrNull { shortIso(it.resetAt) }?.let { Span({ classes("pl-muted") }) { Text("renova em $it") } }
    }
    Div({ classes("pl-usage") }) {
        val free = current.free
        if (free != null) {
            UsageCard("today", "Matéria do plano de hoje", "Livre", "${free.planDayUsed} estudada${if (free.planDayUsed == 1) "" else "s"} hoje", null, "green")
            UsageCard("fast_forward", "Adiantar o plano", if (free.weeklyUsed < free.weeklyLimit) "1" else "0", "livre nesta semana · no app, mais 2 por dia com anúncio curto", null, null)
        } else {
            UsageCard("today", "Plano de hoje e adiantar", "Livre", "estude e adiante o seu plano à vontade", null, "green")
            current.usage.firstOrNull { it.feature == "CONTENT_GENERATION" }?.let { u ->
                UsageCard("menu_book", "Matérias fora do plano", "${u.remaining}", "de ${u.limit} restantes no mês", u.remaining.toDouble() / u.limit.coerceAtLeast(1), null)
            }
        }
        current.usage.firstOrNull { it.feature == "QUESTION_BATCH" && it.limit > 0 }?.let { u ->
            UsageCard("quiz", "Mais 10 questões", "${u.remaining}", "de ${u.limit} restantes no mês", u.remaining.toDouble() / u.limit.coerceAtLeast(1), null)
        }
        current.usage.firstOrNull { it.feature == "SIMULATION_GENERATION" && it.limit > 0 }?.let { u ->
            UsageCard("workspace_premium", if (current.tier == "FREE") "Simulado diagnóstico" else "Simulados", "${u.remaining}", if (current.tier == "FREE") (if (u.remaining > 0) "disponível para você" else "já feito") else "de ${u.limit} restantes no mês", u.remaining.toDouble() / u.limit.coerceAtLeast(1), null)
        }
        current.usage.firstOrNull { it.feature == "SYLLABUS_GENERATION" && it.limit > 0 }?.let { u ->
            UsageCard("description", "Edital pelo seu PDF", "${u.remaining}", "de ${u.limit} restantes no mês", u.remaining.toDouble() / u.limit.coerceAtLeast(1), null)
        }
        current.limitOf(current.tier, "SAVED_EDITAIS")?.let { l ->
            val saved = Store.data.competitions.size
            UsageCard("bookmarks", "Editais salvos", "$saved", if (l.quota >= 1000) "sem limite" else "de ${l.quota}", if (l.quota >= 1000) null else (l.quota - saved).coerceAtLeast(0).toDouble() / l.quota.coerceAtLeast(1), null)
        }
    }

    // ---------- os planos
    Div({ classes("pl-section-head") }) {
        org.jetbrains.compose.web.dom.H2 { Text("Escolha o seu ritmo") }
        Div({ classes("pl-period") }) {
            Button({ classes(*listOfNotNull(if (!yearly) "on" else null).toTypedArray()); onClick { yearly = false } }) { Text("Mensal") }
            Button({ classes(*listOfNotNull(if (yearly) "on" else null).toTypedArray()); onClick { yearly = true } }) { Text("Anual"); Span({ classes("pl-save") }) { Text("2 meses grátis") } }
        }
    }
    Div({ classes("pl-cards") }) {
        tiers.forEach { tier ->
            key(tier) {
                val price = current.pricing[tier]
                val paid = (price?.monthCents ?: 0) > 0
                val mine = tier == current.tier
                Div({ classes(*listOfNotNull("pl-card", tierClass(tier), if (price?.featured == true) "featured" else null, if (mine) "mine" else null).toTypedArray()) }) {
                    if (mine) Span({ classes("pl-ribbon") }) { Text("Seu plano") } else if (price?.featured == true) Span({ classes("pl-ribbon") }) { Text("Mais escolhido") }
                    Div({ classes("pl-card-top") }) {
                        Span({ classes("pl-card-icon") }) { Icon(when (tier) { "PRO" -> "diamond"; "ESSENCIAL" -> "star"; else -> "menu_book" }, filled = true) }
                        B({ classes("pl-card-name") }) { Text(price?.name?.ifBlank { null } ?: planName(tier)) }
                        Span({ classes("pl-card-tag") }) { Text(price?.tagline?.ifBlank { null } ?: planTagline(tier)) }
                    }
                    Div({ classes("pl-price") }) {
                        B { Text(if (!paid) "R$ 0" else if (yearly) brl(price!!.yearCents / 12) else brl(price!!.monthCents)) }
                        Span { Text(if (!paid) "para sempre" else "por mês") }
                    }
                    Span({ classes("pl-price-note") }) { Text(if (!paid) "Sem cartão, sem prazo" else if (yearly) "${brl(price!!.yearCents)} cobrados uma vez por ano" else "ou ${brl(price!!.yearCents)} por ano") }
                    Div({ classes("pl-highlights") }) {
                        val highlights = when (tier) {
                            "FREE" -> listOf("Matéria do plano de hoje, todo dia", "Até 2 matérias novas por dia", "Adiantar com anúncio curto", "Simulado diagnóstico")
                            else -> listOf("Plano de hoje e adiantar à vontade", "${current.limitOf(tier, "CONTENT_GENERATION")?.quota ?: 0} matérias fora do plano por mês", "Qualquer tópico do edital, pronto na hora", "${current.limitOf(tier, "SIMULATION_GENERATION")?.quota ?: 0} simulados por mês, sem anúncios")
                        }
                        highlights.forEach { h -> Div({ classes("pl-hl") }) { Icon("check_circle", filled = true); Span { Text(h) } } }
                    }
                    when {
                        mine -> Span({ classes("pl-cta", "ghost") }) { Text("Você está aqui") }
                        !paid -> Span({ classes("pl-cta", "ghost") }) { Text("Já incluso na sua conta") }
                        else -> A(href = PLAY_URL, { attr("target", "_blank"); attr("rel", "noopener"); classes("pl-cta") }) { Text("Assinar o ${price?.name?.ifBlank { null } ?: planName(tier)}") }
                    }
                }
            }
        }
    }
    P({ classes("pl-fine") }) { Text("A assinatura é feita no app, pela Google Play, e vale na hora na mesma conta, no celular e aqui no site.") }

    // ---------- a comparação completa
    Div({ classes("pl-section-head") }) { org.jetbrains.compose.web.dom.H2 { Text("Tudo o que cada plano tem") } }
    Div({ classes("pl-table-wrap") }) {
        Div({ classes("pl-table") }) {
            Div({ classes("pl-row", "pl-head") }) {
                Div({ classes("pl-cell", "pl-label") })
                tiers.forEach { t -> Div({ classes("pl-cell", "pl-col-head", tierClass(t), *listOfNotNull(if (t == current.tier) "mine" else null).toTypedArray()) }) { B { Text(current.pricing[t]?.name?.ifBlank { null } ?: planName(t)) } } }
            }
            comparisonGroups(current).forEach { (group, rows) ->
                Div({ classes("pl-row", "pl-group") }) { Div({ classes("pl-cell") }) { Text(group) } }
                rows.forEach { row ->
                    Div({ classes("pl-row") }) {
                        Div({ classes("pl-cell", "pl-label") }) { B { Text(row.label) }; Span { Text(row.hint) } }
                        tiers.forEach { t ->
                            val v = row.value(t)
                            Div({ classes("pl-cell", "pl-val", tierClass(t), *listOfNotNull(if (t == current.tier) "mine" else null, if (v == "—") "none" else null, if (v == "Livre" || v == "Pronto na hora" || v == "Sem limite" || v == "Nenhum") "strong" else null).toTypedArray()) }) { Text(v) }
                        }
                    }
                }
            }
            Div({ classes("pl-row", "pl-group") }) { Div({ classes("pl-cell") }) { Text("Em todos os planos") } }
            everyPlan.forEach { (icon, label) ->
                Div({ classes("pl-row") }) {
                    Div({ classes("pl-cell", "pl-label", "with-icon") }) { Icon(icon); B { Text(label) } }
                    tiers.forEach { t -> Div({ classes("pl-cell", "pl-val", tierClass(t), *listOfNotNull(if (t == current.tier) "mine" else null).toTypedArray()) }) { Icon("check", extraClass = "pl-check") } }
                }
            }
        }
    }

    // ---------- perguntas rápidas
    Div({ classes("pl-section-head") }) { org.jetbrains.compose.web.dom.H2 { Text("Perguntas rápidas") } }
    Div({ classes("pl-faq") }) {
        faq.forEach { (q, a) ->
            key(q) {
                var open by remember { mutableStateOf(false) }
                Div({ classes(*listOfNotNull("pl-q", if (open) "open" else null).toTypedArray()) }) {
                    Button({ classes("pl-q-head"); attr("aria-expanded", "$open"); onClick { open = !open } }) { B { Text(q) }; Icon(if (open) "remove" else "add") }
                    if (open) P { Text(a) }
                }
            }
        }
    }
}

/** Um cartão do "Seu mês": ícone, o que é, o número grande, a frase e, quando tem limite, a barra. */
@Composable
private fun UsageCard(icon: String, title: String, big: String, line: String, left: Double?, tone: String?) {
    val low = left != null && left <= 0.0
    Div({ classes(*listOfNotNull("pl-use", tone, if (low) "empty" else null).toTypedArray()) }) {
        Div({ classes("pl-use-top") }) { Span({ classes("pl-use-icon") }) { Icon(icon) }; Span({ classes("pl-use-title") }) { Text(title) } }
        B({ classes("pl-use-big") }) { Text(big) }
        Span({ classes("pl-use-line") }) { Text(line) }
        if (left != null) Div({ classes("pl-use-bar") }) { Span({ attr("style", "width:${(left * 100).coerceIn(0.0, 100.0).toInt()}%") }) }
    }
}

@Composable
private fun SourceItem(src: br.com.estudario.web.data.ContentSource) {
    Div({ classes("source-row", "saved-item"); attr("style", "flex-direction:column;align-items:flex-start") }) {
        Chip(if (src.kind == "OFICIAL") "FONTE OFICIAL" else "FONTE COMPLEMENTAR", tone = if (src.kind == "OFICIAL") "green" else null)
        B({ classes("small") }) { Text(src.title) }
        val meta = listOf(src.publisher, src.reference, src.accessedAt.takeIf { it.isNotBlank() }?.let { "acesso em $it" }).filter { !it.isNullOrBlank() }.joinToString(" · ")
        if (meta.isNotBlank()) Div({ classes("xs", "muted") }) { Text(meta) }
        src.url?.let { url -> A(href = url, { attr("target", "_blank"); attr("rel", "noopener"); classes("md-link", "xs") }) { Text(url.substringAfter("://").substringBefore('/')) } }
    }
}
