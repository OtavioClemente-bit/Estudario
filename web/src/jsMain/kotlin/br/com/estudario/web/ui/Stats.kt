package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.StreakEngine
import br.com.estudario.time.minusDays
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.Theme
import br.com.estudario.web.data.Prefs
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.svg.Circle
import org.jetbrains.compose.web.svg.Svg
import androidx.compose.runtime.key
import br.com.estudario.time.toEpochDay
import br.com.estudario.time.isoDayOfWeek
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// ------------------------------------------------------------------ conquistas

@Composable
fun AchievementsScreen() {
    val progress = Progress.of(Store.data).progress
    // tocar num emblema abre a medalha em destaque
    var open by remember { mutableStateOf<String?>(null) }
    open?.let { id -> progress.badges.firstOrNull { it.badge.id == id }?.let { MedalDetail(it, progress.badges, onPick = { open = it }, onClose = { open = null }) } }
    BackToProfile()
    PageHead("Conquistas", "${progress.earnedBadges.size} de ${progress.badges.size} emblemas. Continue estudando para liberar os próximos.")
    Div({ classes("badges") }) {
        (progress.earnedBadges + progress.nextBadges).forEach { item ->
            androidx.compose.runtime.key(item.badge.id) {
                Div({
                    classes(*listOfNotNull("badge", "tappable", if (item.earned) "earned" else null).toTypedArray()); attr("title", item.badge.requirement)
                    attr("role", "button"); attr("tabindex", "0"); onClick { open = item.badge.id }
                    onKeyDown { if (it.key == "Enter" || it.key == " ") { it.preventDefault(); open = item.badge.id } }
                }) {
                    // a medalha do emblema (folha-live.js, folhaMedal): metal do nível, símbolo cunhado e moldura que cresce com o nível
                    Div({ classes("medal") }) {
                        MedalImg(item.badge, item.earned, 96)
                        if (!item.earned) Span({ classes("medal-lock") }) { Icon("lock", filled = true) }
                    }
                    Span({ classes("name") }) { Text(item.badge.name) }
                    Span({ classes("xs", "muted") }) { Text(item.badge.requirement) }
                    // O que esse emblema libera no armário do Folha.
                    wardrobeUnlockedBy(item.badge.id).forEach { gift ->
                        A(href = "#/${Route.Wardrobe.path}", { classes("wd-gift", if (item.earned) "on" else "off") }) { Icon("checkroom"); Text("${gift.slot.label}: ${gift.name}") }
                    }
                    if (!item.earned) Div({ attr("style", "width:100%") }) {
                        ProgressBar(item.percent.toDouble())
                        Span({ classes("xs", "muted") }) { Text("${item.current} / ${item.target} ${item.badge.unit}") }
                    }
                }
            }
        }
    }
}

private val TIER_COLOR = mapOf(1 to "#C27E45", 2 to "#9AA2B5", 3 to "#F5B83D", 4 to "#5E9BFF", 5 to "#F0506B", 6 to "#3FD69F")

/**
 * A medalha em destaque: a medalha viva (folha-live.js, <estudario-medalha>) girando como moeda, que dá
 * para girar com o dedo; o nível, o que pede, o progresso até ela e a escada da categoria inteira (do
 * Bronze à Esmeralda), onde dá para tocar nas outras.
 */
@Composable
internal fun MedalDetail(item: br.com.estudario.domain.BadgeProgress, all: List<br.com.estudario.domain.BadgeProgress>, onPick: (String) -> Unit, onClose: () -> Unit) {
    androidx.compose.runtime.DisposableEffect(Unit) {
        val esc: (org.w3c.dom.events.Event) -> Unit = { e -> if ((e as org.w3c.dom.events.KeyboardEvent).key == "Escape") onClose() }
        kotlinx.browser.window.addEventListener("keydown", esc)
        onDispose { kotlinx.browser.window.removeEventListener("keydown", esc) }
    }
    val b = item.badge
    val cat = b.id.substringBefore("-")
    val color = TIER_COLOR[b.tier] ?: "#F5B83D"
    val ladder = all.filter { it.badge.category == b.category }.sortedBy { it.badge.tier }
    Div({ classes("medal-modal"); onClick { onClose() } }) {
        Div({ classes("medal-sheet", "rise"); attr("role", "dialog"); attr("aria-label", b.name); onClick { it.stopPropagation() } }) {
            Button({ classes("medal-close"); attr("aria-label", "Fechar"); onClick { onClose() } }) { Icon("close") }
            // a arte grande do emblema, entrando com brilho
            androidx.compose.runtime.key(b.id) {
                Div({ classes(*listOfNotNull("medal-hero", if (item.earned) "earned" else "locked").toTypedArray()) }) {
                    Img(src = "emblemas/g/${b.id}.webp", alt = "Emblema ${b.name}", attrs = { attr("decoding", "async") })
                    if (item.earned) Span({ classes("medal-shine") })
                }
            }
            Span({ classes("medal-tier"); attr("style", "--tc:$color") }) { Text("${tierName(b.tier).uppercase()} · ${b.category.label.uppercase()}") }
            H2({ classes("medal-name") }) { Text(b.name) }
            P({ classes("muted"); attr("style", "margin:0") }) { Text(b.requirement) }
            if (item.earned) Span({ classes("medal-done") }) { Icon("verified", filled = true); Text("Conquistado") }
            else Div({ classes("medal-progress") }) {
                ProgressBar(item.percent.toDouble())
                Span({ classes("small", "muted") }) { Text(item.hint ?: "${item.current} de ${item.target} ${b.unit} · faltam ${item.remaining}") }
            }
            wardrobeUnlockedBy(b.id).forEach { gift ->
                A(href = "#/${Route.Wardrobe.path}", { classes("wd-gift", if (item.earned) "on" else "off") }) { Icon("checkroom"); Text("Libera no armário: ${gift.name}") }
            }
            if (ladder.size > 1) {
                Span({ classes("eyebrow"); attr("style", "margin-top:6px") }) { Text("A ESCADA DE ${b.category.label.uppercase()}") }
                Div({ classes("medal-ladder") }) {
                    ladder.forEach { step ->

                        Button({
                            classes(*listOfNotNull("medal-step", if (step.badge.id == b.id) "on" else null, if (step.earned) "earned" else null).toTypedArray())
                            attr("aria-label", "${step.badge.name}${if (step.earned) ", conquistado" else ""}"); onClick { onPick(step.badge.id) }
                        }) {
                            MedalImg(step.badge, step.earned, 56)
                            // a meta de cada degrau (algumas categorias têm mais degraus que metais)
                            Span { Text("${step.badge.target} ${step.badge.unit}") }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ desempenho

/**
 * Desempenho, como o StatisticsScreen do app: a pessoa entende como está sem precisar ler. Um anel
 * com o acerto e a tendência, os números-chave, o ritmo dia a dia em barras com o acerto em pontos,
 * o edital em rosca, as matérias em ranking colorido e os pontos fortes e fracos lado a lado.
 */
@Composable
fun StatsScreen() {
    val data = Store.data
    val today = Queries.todayDate()
    val activity = Queries.dailyActivity(data).associateBy { it.date }
    val view = Progress.of(data)
    val streak = view.streak
    val topics = data.topics.associateBy { it.id }
    val questions = data.questions.associateBy { it.id }
    val competition = CompetitionFilter.current(data)
    val subjects = Queries.subjectsOf(data, competition?.id)
    CompetitionTabs()
    var days by remember { mutableStateOf(30) }
    val seed = rememberSeed()
    val span = if (days == 0) (activity.keys.minOfOrNull { today.toEpochDay() - it.toEpochDay() }?.toInt()?.plus(1) ?: 1).coerceAtLeast(7) else days
    val window = (0 until span).map { today.minusDays(it) }
    val windowSet = window.toSet()
    val previousSet = if (days == 0) emptySet() else (span until span * 2).map { today.minusDays(it) }.toSet()
    val attempts = data.attempts.filter { Queries.dateOf(it.answeredAt) in windowSet }
    val previous = data.attempts.filter { Queries.dateOf(it.answeredAt) in previousSet }
    val accuracy = if (attempts.isEmpty()) null else percent(attempts.count { it.correct }, attempts.size)
    val delta = if (accuracy != null && previous.size >= 5) accuracy - percent(previous.count { it.correct }, previous.size) else null
    val minutes = window.sumOf { activity[it]?.minutes ?: 0 }
    val activeDays = window.count { activity[it]?.hasAnything == true }

    PageHead("Desempenho", "Seu estudo em números") {
        Div({ classes("filter-chips") }) {
            listOf(7 to "7 dias", 30 to "30 dias", 90 to "90 dias", 0 to "Tudo").forEach { (n, label) ->
                Button({ classes(*listOfNotNull("fchip", if (days == n) "on" else null).toTypedArray()); onClick { days = n } }) { Text(label) }
            }
        }
    }

    // ---------- anel de acerto
    Div({ classes("stats-hero", "rise") }) {
        Div({ classes("donut-wrap") }) {
            Donut(listOf((accuracy ?: 0) to accuracyColor(accuracy)), 100, 132, 16, "rgba(255,255,255,.55)")
            Div({ classes("donut-center") }) { B { Text(accuracy?.let { "$it%" } ?: "--") }; Span { Text("acerto") } }
        }
        Div({ classes("grow", "stack", "tight") }) {
            H2 {
                Text(
                    when {
                        accuracy == null -> "Responda questões para medir seu acerto"
                        accuracy >= 70 -> "Nível de aprovação"
                        accuracy >= 50 -> "No caminho certo"
                        else -> "Hora de reforçar a base"
                    },
                )
            }
            delta?.let { d ->
                val color = if (d > 0) "#2FA36B" else if (d < 0) "#E5484D" else "#9AA0A6"
                Span({ classes("trend"); attr("style", "--trend:$color") }) {
                    Icon(if (d >= 0) "trending_up" else "trending_down")
                    Text(when { d > 0 -> "+$d pontos"; d < 0 -> "$d pontos"; else -> "estável" } + " vs. antes")
                }
            }
            Span({ classes("small") }) { Text("${attempts.count { it.correct }} certas de ${attempts.size}") }
        }
        Div({ classes("stats-folha") }) { FolhaTalking(FolhaLines.forStats(accuracy, seed), 72) }
    }

    // ---------- números-chave
    Div({ classes("kpis") }) {
        Kpi("schedule", Queries.minutesLabel(minutes), "estudadas", "#5B5BD6")
        Kpi("quiz", "${attempts.size}", "questões", "#0E9AA7")
        Kpi("calendar_month", if (days == 0) "$activeDays" else "$activeDays/$span", "dias ativos", "#8E4EC6")
        Kpi("local_fire_department", "${streak.current}", "sequência", "#E8590C")
    }

    // ---------- ritmo diário
    Card(extra = "pad-lg") {
        val shown = window.take(minOf(span, 31)).reversed()
        val max = shown.maxOf { activity[it]?.minutes ?: 0 }.coerceAtLeast(30)
        Div({ classes("panel-title") }) {
            H3 { Text("Seu ritmo") }
            P({ classes("small", "muted") }) { Text("Últimos ${shown.size} dias · ${shown.count { activity[it]?.hasAnything == true }} de ${shown.size} com estudo") }
        }
        // Destaques do período e o dia apontado (passar o mouse ou tocar numa barra).
        val studiedDays = shown.filter { (activity[it]?.minutes ?: 0) > 0 }
        val average = if (studiedDays.isEmpty()) 0 else studiedDays.sumOf { activity[it]!!.minutes } / studiedDays.size
        val best = studiedDays.maxByOrNull { activity[it]!!.minutes }
        var hovered by remember(shown.size) { mutableStateOf<kotlinx.datetime.LocalDate?>(null) }
        Div({ classes("rhythm-stats") }) {
            Div({ classes("rstat") }) { Span { Text("Média por dia de estudo") }; B { Text(if (average > 0) Queries.minutesLabel(average) else "--") } }
            Div({ classes("rstat") }) { Span { Text("Melhor dia") }; B { Text(best?.let { "${Queries.minutesLabel(activity[it]!!.minutes)} · ${Queries.shortDate(it)}" } ?: "--") } }
            Div({ classes("rstat") }) { Span { Text("Total no período") }; B { Text(Queries.minutesLabel(shown.sumOf { activity[it]?.minutes ?: 0 })) } }
        }
        Div({ classes("rhythm"); onMouseLeave { hovered = null } }) {
            if (average > 0) I({ classes("ravg"); attr("style", "bottom:${average * 100 / max}%") }) { Span { Text("média") } }
            hovered?.let { date ->
                val day = activity[date]
                val value = day?.minutes ?: 0
                val acc = day?.takeIf { it.questions > 0 }?.let { percent(it.correct, it.questions) }
                val index = shown.indexOf(date)
                val leftPct = (index * 100.0 + 50.0) / shown.size
                Div({ classes(*listOfNotNull("rtip", if (leftPct > 70) "right" else if (leftPct < 30) "left" else null).toTypedArray()); attr("style", "left:${leftPct}%") }) {
                    Div({ classes("rtip-date") }) { Text(Queries.weekdayShort[date.isoDayOfWeek - 1].replaceFirstChar { it.uppercase() } + ", " + Queries.shortDate(date) + if (date == today) " · hoje" else if (date == best) " · melhor dia" else "") }
                    B({ classes("rtip-main") }) { Text(if (value > 0) Queries.minutesLabel(value) else if (day?.hasAnything == true) "Estudou" else "Sem estudo") }
                    if (day != null && day.focusMinutes > 0) Div({ classes("rtip-row") }) { Icon("timer"); Text("Foco: ${Queries.minutesLabel(day.focusMinutes)}") }
                    if (day != null && day.questionMinutes > 0) Div({ classes("rtip-row") }) { Icon("quiz"); Text("Em questões: ${Queries.minutesLabel(day.questionMinutes)}") }
                    if (day != null && day.questions > 0) Div({ classes("rtip-row") }) { Icon("check_circle"); Text("${day.questions} ${if (day.questions == 1) "questão" else "questões"}" + (acc?.let { " · $it% de acerto" } ?: "")) }
                    if (day != null && day.reviews > 0) Div({ classes("rtip-row") }) { Icon("history"); Text("${day.reviews} ${if (day.reviews == 1) "revisão" else "revisões"}") }
                    if (day != null && day.planTasks > 0) Div({ classes("rtip-row") }) { Icon("task_alt"); Text("${day.planTasks} ${if (day.planTasks == 1) "tarefa" else "tarefas"} do plano") }
                    if (average > 0 && value > 0) Div({ classes("rtip-cmp", if (value >= average) "up" else "down") }) { Text(if (value >= average) "+${Queries.minutesLabel(value - average)} acima da média" else "${Queries.minutesLabel(average - value)} abaixo da média") }
                }
            }
            shown.forEach { date ->
                val day = activity[date]
                val value = day?.minutes ?: 0
                val active = day?.hasAnything == true
                val acc = day?.takeIf { it.questions > 0 }?.let { percent(it.correct, it.questions) }
                // Passar o mouse: o dia inteiro, o tempo (foco e questões) e o que foi feito.
                val tip = buildList {
                    add("${Queries.shortDate(date)}: ${if (value > 0) Queries.minutesLabel(value) + " de estudo" else "sem tempo cronometrado"}")
                    if (day != null && day.focusMinutes > 0) add("foco ${Queries.minutesLabel(day.focusMinutes)}")
                    if (day != null && day.questionMinutes > 0) add("questões ${Queries.minutesLabel(day.questionMinutes)}")
                    if (day != null && day.questions > 0) add("${day.questions} ${if (day.questions == 1) "questão" else "questões"}${acc?.let { ", $it% de acerto" } ?: ""}")
                    if (day != null && day.reviews > 0) add("${day.reviews} ${if (day.reviews == 1) "revisão" else "revisões"}")
                    if (day != null && day.planTasks > 0) add("${day.planTasks} ${if (day.planTasks == 1) "tarefa" else "tarefas"} do plano")
                }.joinToString(" · ")
                Div({
                    classes(*listOfNotNull("rcol", if (date == today) "today" else null, if (date == hovered) "on" else null).toTypedArray())
                    attr("aria-label", tip)
                    onMouseEnter { hovered = date }
                    onClick { hovered = if (hovered == date) null else date }
                }) {
                    if (acc != null) I({ classes("rdot"); attr("style", "background:${accuracyColor(acc)}") })
                    I({ classes(*listOfNotNull("rbar", if (!active) "idle" else null).toTypedArray()); attr("style", "height:${if (value > 0) (value * 100 / max).coerceAtLeast(5) else if (active) 5 else 3}%") })
                }
            }
        }
        if (shown.size <= 14) Div({ classes("rlabels") }) { shown.forEach { d -> Span { Text(Queries.weekdayShort[d.isoDayOfWeek - 1].take(1).uppercase()) } } }
        else Div({ classes("row", "between", "xs", "muted") }) { Span { Text(Queries.shortDate(shown.first())) }; Span { Text("hoje") } }
        Div({ classes("row", "wrap", "xs", "muted"); attr("style", "gap:14px;margin-top:8px") }) {
            Span({ classes("legend") }) { Span({ classes("sw"); attr("style", "background:var(--primary)") }); Text("minutos de estudo") }
            Span({ classes("legend") }) { Span({ classes("sw", "round"); attr("style", "background:#2FA36B") }); Text("acerto do dia") }
            if (average > 0) Span({ classes("legend") }) { Span({ classes("sw"); attr("style", "background:#F2A900;height:2px") }); Text("média dos dias de estudo") }
        }
    }

    Div({ classes("grid", "cols-2") }) {
        // ---------- edital em rosca
        val leaves = Queries.leafTopics(data, subjects.mapTo(hashSetOf()) { it.id })
        if (leaves.isNotEmpty()) Card(extra = "pad-lg") {
            val theoryIds = data.theories.mapTo(hashSetOf()) { it.topicId }
            val studied = leaves.count(Queries::isStudied)
            val withMaterial = leaves.count { !Queries.isStudied(it) && it.id in theoryIds }
            val remaining = (leaves.size - studied - withMaterial).coerceAtLeast(0)
            Div({ classes("panel-title") }) { H3 { Text("Seu edital") }; P({ classes("small", "muted", "clamp-2") }) { Text(competition?.name.orEmpty()) } }
            Div({ classes("row"); attr("style", "gap:20px;align-items:center") }) {
                Div({ classes("donut-wrap", "sm") }) {
                    Donut(listOf(percent(studied, leaves.size) to "#2FA36B", percent(withMaterial, leaves.size) to "var(--primary)"), 100, 120, 18, "var(--soft-2)")
                    Div({ classes("donut-center") }) { B { Text("${percent(studied, leaves.size)}%") }; Span { Text("estudado") } }
                }
                Div({ classes("stack", "tight") }) {
                    DonutLegend("#2FA36B", "$studied", "estudados")
                    DonutLegend("var(--primary)", "$withMaterial", "com material, a estudar")
                    DonutLegend("var(--soft-2)", "$remaining", "sem material ainda")
                }
            }
        }
        // ---------- plano do período
        val plan = Queries.activePlan(data)
        val planTasks = plan?.let { p -> data.tasks.filter { it.planId == p.id && kotlinx.datetime.LocalDate.fromEpochDays(it.day) in windowSet && it.status != "REPROGRAMADA" && it.status != "CANCELADA" } }.orEmpty()
        if (planTasks.isNotEmpty()) Card(extra = "pad-lg") {
            val done = planTasks.count { it.status == "CONCLUIDA" }
            val missed = planTasks.count { it.status == "NAO_REALIZADA" || (it.day < today.toEpochDay() && (it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO")) }
            val pending = (planTasks.size - done - missed).coerceAtLeast(0)
            val pct = percent(done, planTasks.size)
            Div({ classes("panel-title") }) { H3 { Text("Plano de estudos") }; P({ classes("small", "muted") }) { Text("Tarefas do período") } }
            Div({ classes("row"); attr("style", "gap:10px") }) {
                Icon("event_available")
                B({ attr("style", "font-size:30px;color:${accuracyColor(pct)}") }) { Text("$pct%") }
                Span({ classes("muted") }) { Text("cumprido") }
            }
            Div({ classes("seg-bar") }) {
                if (done > 0) Span({ attr("style", "flex:$done;background:#2FA36B") })
                if (missed > 0) Span({ attr("style", "flex:$missed;background:#E5484D") })
                if (pending > 0) Span({ attr("style", "flex:$pending;background:var(--soft-2)") })
            }
            Div({ classes("row", "wrap", "xs", "muted"); attr("style", "gap:14px") }) {
                Span({ classes("legend") }) { Span({ classes("sw"); attr("style", "background:#2FA36B") }); Text("$done feitas") }
                Span({ classes("legend") }) { Span({ classes("sw"); attr("style", "background:#E5484D") }); Text("$missed perdidas") }
                Span({ classes("legend") }) { Span({ classes("sw"); attr("style", "background:var(--soft-2)") }); Text("$pending a fazer") }
            }
        }
    }

    // ---------- matérias e tópicos
    val bySubject = attempts.groupBy { topics[questions[it.questionId]?.topicId]?.subjectId }
    val ranked = subjects.mapNotNull { s -> bySubject[s.id]?.takeIf { it.isNotEmpty() }?.let { list -> Triple(s, list.size, percent(list.count { it.correct }, list.size)) } }.sortedByDescending { it.third }
    val byTopic = attempts.groupBy { questions[it.questionId]?.topicId }.mapNotNull { (id, list) ->
        val topic = topics[id] ?: return@mapNotNull null
        if (list.size < 3) null else Triple(topic, list.size, percent(list.count { it.correct }, list.size))
    }.sortedByDescending { it.third }
    Div({ classes("grid", "cols-2") }) {
        Card(extra = "pad-lg") {
            Div({ classes("panel-title") }) { H3 { Text("Acerto por matéria") }; P({ classes("small", "muted") }) { Text("Da mais forte para a mais fraca") } }
            if (ranked.isEmpty()) P({ classes("muted", "small") }) { Text("Resolva questões para ver seu acerto por matéria.") }
            Div({ classes("stack") }) {
                ranked.forEach { (subject, count, rate) ->
                    key(subject.id) {
                        val color = accuracyColor(if (count >= 5) rate else null)
                        Div({ classes("stack", "tight") }) {
                            Div({ classes("row", "between") }) {
                                B({ classes("small", "clamp-2") }) { Text(subject.name) }
                                B({ classes("nowrap"); attr("style", "color:$color") }) { Text("$rate%") }
                            }
                            Div({ classes("bar") }) { Span({ attr("style", "width:${rate.coerceAtLeast(3)}%;background:$color") }) }
                            Span({ classes("xs", "muted") }) { Text(if (count >= 5) "${count * rate / 100} de $count certas" else "Poucos dados · $count resposta(s)") }
                        }
                    }
                }
            }
            if (ranked.isNotEmpty()) Div({ classes("row", "wrap", "xs", "muted"); attr("style", "gap:12px;margin-top:12px") }) {
                Span({ classes("legend") }) { Span({ classes("sw", "round"); attr("style", "background:#2FA36B") }); Text("70% ou mais") }
                Span({ classes("legend") }) { Span({ classes("sw", "round"); attr("style", "background:#F2A900") }); Text("50 a 69%") }
                Span({ classes("legend") }) { Span({ classes("sw", "round"); attr("style", "background:#E5484D") }); Text("abaixo de 50%") }
            }
        }
        Div({ classes("stack") }) {
            if (byTopic.size >= 2) Div({ classes("grid", "cols-2"); attr("style", "gap:10px") }) {
                val strong = byTopic.take(3)
                val weak = byTopic.takeLast(3).reversed().filterNot { it in strong }
                TopicColumn("Pontos fortes", "trending_up", "#2FA36B", strong)
                TopicColumn("Para reforçar", "trending_down", "#E5484D", weak)
            }
            Card {
                Div({ classes("panel-title") }) { H3 { Text("Constância") }; P({ classes("small", "muted") }) { Text("${streak.activeDays} dias com estudo · recorde de ${streak.best} seguidos") } }
                ActivityCalendar(streak.calendar, streak.goal, tone = "fire")
            }
        }
    }
}

/** Rosca: fatias em porcentagem, na ordem, sobre um trilho. */
@Composable
private fun Donut(slices: List<Pair<Int, String>>, total: Int, size: Int, stroke: Int, track: String) {
    val r = (size - stroke) / 2.0
    val circ = 2 * kotlin.math.PI * r
    Svg(viewBox = "0 0 $size $size", attrs = { classes("donut"); attr("width", "$size"); attr("height", "$size") }) {
        Circle(size / 2, size / 2, r, { attr("fill", "none"); attr("stroke", track); attr("stroke-width", "$stroke") })
        var offset = 0.0
        slices.forEach { (value, color) ->
            if (value <= 0) return@forEach
            val len = circ * value / total
            Circle(size / 2, size / 2, r, {
                classes("donut-slice")
                attr("fill", "none"); attr("stroke", color); attr("stroke-width", "$stroke")
                attr("stroke-linecap", if (slices.size == 1) "round" else "butt")
                attr("stroke-dasharray", "${(len - if (slices.size > 1) 2 else 0).coerceAtLeast(0.5)} ${circ}")
                attr("stroke-dashoffset", "${-offset}")
                attr("transform", "rotate(-90 ${size / 2} ${size / 2})")
            })
            offset += len
        }
    }
}

@Composable
private fun DonutLegend(color: String, value: String, label: String) {
    Div({ classes("row"); attr("style", "gap:8px") }) {
        Span({ classes("sw-lg"); attr("style", "background:$color") })
        B { Text(value) }
        Span({ classes("small", "muted") }) { Text(label) }
    }
}

@Composable
private fun Kpi(icon: String, value: String, label: String, color: String) {
    Div({ classes("kpi"); attr("style", "--kpi:$color") }) {
        Icon(icon)
        B { Text(value) }
        Span { Text(label) }
    }
}

@Composable
private fun TopicColumn(title: String, icon: String, color: String, list: List<Triple<br.com.estudario.web.data.Topic, Int, Int>>) {
    Div({ classes("topic-col"); attr("style", "--col:$color") }) {
        Div({ classes("row"); attr("style", "gap:6px") }) { Icon(icon); B({ attr("style", "color:$color") }) { Text(title) } }
        if (list.isEmpty()) P({ classes("small", "muted") }) { Text("Sem dados suficientes") }
        list.forEach { (topic, _, rate) ->
            key(topic.id) {
                A(href = "#/${Route.Topic(topic.id).path}", { classes("topic-col-item") }) {
                    Span({ classes("small", "clamp-2") }) { Text(topic.title) }
                    B({ attr("style", "color:$color;font-size:18px") }) { Text("$rate%") }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ ajustes

@Composable
fun SettingsScreen() {
    var goal by remember { mutableStateOf(Prefs.dailyGoal) }
    var som by remember { mutableStateOf(runCatching { kotlinx.browser.window.asDynamic().estudarioSom.enabled as Boolean }.getOrDefault(true)) }
    BackToProfile()
    PageHead("Ajustes", "Deixe o Estudário do seu jeito")
    // Os mesmos grupos do app, na ordem de uso: aparência, estudo, conta e sobre.
    Div({ classes("set-wrap") }) {
        SetGroup("Aparência e sons") {
            SetRow("dark_mode", "Tema", "Sistema acompanha o computador") {
                Div({ classes("segmented") }) {
                    listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) ->
                        Button({ classes(*listOfNotNull(if (Theme.mode == key) "on" else null).toTypedArray()); onClick { Theme.set(key) } }) { Text(label) }
                    }
                }
            }
            SetRow("favorite", "Sons do Estudário", "Acerto, erro, toque no Folha, tarefas e comemorações") {
                Switch(som, "Sons do Estudário") { v -> som = v; runCatching { kotlinx.browser.window.asDynamic().estudarioSom.set(v); if (v) kotlinx.browser.window.asDynamic().estudarioSom.play("acerto") } }
            }
        }
        SetGroup("Estudo") {
            Div({ classes("set-row", "col") }) {
                Div({ classes("set-row-text") }) { B { Text("Meta de questões por dia") }; Span { Text("Uma tarefa do plano ou uma revisão também fecham o dia. Vale no celular também.") } }
                BrandCounter(goal, { goal = it; Prefs.dailyGoal = it }, step = 5, min = 5, max = 100, unit = "questões por dia", chips = listOf(10, 20, 30, 50), mood = ::goalMood, label = "Meta diária de questões")
            }
            SetLink("timer", "Modo foco", "Cronômetro e aviso do bloco de estudo") { Router.go(Route.Focus) }
            SetLink("notifications", "Notificações", "Aviso quando o material ficar pronto") { Router.go(Route.Notifications) }
        }
        SetGroup("Conta e sincronização") {
            val state = Store.save
            SetRow("cloud_sync", "Sincronização com o celular", when (state) {
                br.com.estudario.web.data.SaveState.Saved -> "Tudo salvo na sua conta"
                br.com.estudario.web.data.SaveState.Offline -> "Sem internet: guardado aqui e enviado quando voltar"
                else -> "Enviando as últimas mudanças…"
            }) {
                Btn("Recarregar da conta", {
                    // Recarregar troca tudo pela versão da conta: o que ainda não foi enviado se perde.
                    val unsent = Store.save != br.com.estudario.web.data.SaveState.Saved
                    if (!unsent || kotlinx.browser.window.confirm("Há mudanças feitas aqui que ainda não chegaram à conta. Recarregar descarta essas mudanças. Continuar?")) Store.start()
                }, style = "outline", icon = "refresh", small = true)
            }
            P({ classes("xs", "muted", "set-note") }) { Text("O que você faz aqui chega ao celular em segundos. Se os dois mudarem ao mesmo tempo, o Estudário junta as duas mudanças sem perder nada.") }
        }
        SetGroup("Sobre") {
            SetLink("explore", "Rever o tour", "O giro rápido pelas partes principais") { replayTour() }
            Div({ classes("set-row", "link") }) {
                Icon("shield")
                A(href = "https://estudario.com.br/privacidade.html", { attr("target", "_blank"); classes("set-row-text") }) { B { Text("Privacidade") }; Span { Text("Como o Estudário cuida dos seus dados") } }
            }
        }
        P({ classes("xs", "faint"); attr("style", "text-align:center") }) { Text("Estudário no computador · app.estudario.com.br") }
    }
}

@Composable
private fun SetGroup(title: String, content: @Composable () -> Unit) {
    Div({ classes("set-group") }) {
        H3({ classes("set-title") }) { Text(title) }
        Div({ classes("set-card") }) { content() }
    }
}

@Composable
private fun SetRow(icon: String, title: String, desc: String, control: @Composable () -> Unit) {
    Div({ classes("set-row") }) {
        Icon(icon)
        Div({ classes("set-row-text") }) { B { Text(title) }; Span { Text(desc) } }
        Div({ classes("set-ctrl") }) { control() }
    }
}

@Composable
private fun SetLink(icon: String, title: String, desc: String, onClick: () -> Unit) {
    Button({ classes("set-row", "link"); onClick { onClick() } }) {
        Icon(icon)
        Div({ classes("set-row-text") }) { B { Text(title) }; Span { Text(desc) } }
        Icon("chevron_right", extraClass = "faint")
    }
}

private fun goalMood(n: Int): String = when {
    n <= 10 -> "Leve: dá para manter todo dia."
    n <= 25 -> "Firme: um bom ritmo de treino."
    n <= 50 -> "Puxado: ritmo de reta final."
    else -> "Maratona: só se o dia render."
}

