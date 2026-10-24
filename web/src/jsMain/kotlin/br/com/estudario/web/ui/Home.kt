package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Actions
import br.com.estudario.web.data.PlanTask
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/**
 * Início, na ordem da Home do app: o cartão do concurso (prova, edital coberto, sequência), o
 * "Agora" com o Folha falando, a missão do dia e, ao lado, previsão, desempenho, nível e o que está
 * pedindo atenção. Em tela larga vira duas colunas; no celular, a mesma ordem do app.
 */
@Composable
fun HomeScreen() {
    val data = Store.data
    val plan = Queries.activePlan(data)
    val competition = Queries.primaryCompetition(data)
    val todayEpoch = Queries.todayEpoch()
    val tasks = plan?.let { Queries.tasksOn(data, it.id, todayEpoch) }.orEmpty().filter { it.status != "REPROGRAMADA" && it.status != "CANCELADA" }
    val view = Progress.of(data)
    val streak = view.streak
    val progress = view.progress
    val subjects = Queries.subjectsOf(data, competition?.id)
    val leaves = Queries.leafTopics(data, subjects.mapTo(hashSetOf()) { it.id })
    val studied = leaves.count(Queries::isStudied)
    val session = Store.session
    var completing by remember { mutableStateOf<PlanTask?>(null) }
    var generatingFor by remember { mutableStateOf<Long?>(null) }
    val seed = rememberSeed()

    val hour = Date().getHours()
    val greeting = when (hour) { in 0..4 -> "Boa madrugada"; in 5..11 -> "Bom dia"; in 12..17 -> "Boa tarde"; else -> "Boa noite" }
    val firstName = session?.name?.substringBefore(' ')?.takeIf { it.isNotBlank() } ?: if (Store.demo) "estudante" else "estudante"
    val daysToExam = plan?.exam?.let { it - todayEpoch }?.takeIf { it >= 0 }
    val coverage = percent(studied, leaves.size)

    // ---------- o cartão do concurso
    Div({ classes("home-hero", "rise"); onClick { Router.go(Route.Edital) } }) {
        Div({ classes("hh-top") }) {
            Div({ classes("grow") }) {
                P({ classes("hh-greet") }) { Text("$greeting, $firstName") }
                H2({ classes("hh-title") }) { Text(competition?.name ?: "Seu concurso") }
                plan?.objective?.takeIf { it.isNotBlank() && it != competition?.name }?.let { P({ classes("hh-sub") }) { Text(it) } }
            }
            if (daysToExam != null) Div({ classes("hh-count") }) {
                B { Text("$daysToExam") }
                Span { Text(if (daysToExam == 1L) "dia para a prova" else "dias para a prova") }
            }
        }
        Div({ classes("hh-cover") }) {
            Div({ classes("row", "between") }) {
                Span { Text("Edital coberto") }
                B { Text("$coverage% · $studied/${leaves.size} tópicos") }
            }
            Div({ classes("hh-bar") }) { Span({ attr("style", "width:${coverage.coerceAtLeast(2)}%") }) }
        }
        Div({ classes("hh-pills") }) {
            Span({ classes("hh-pill", "link"); onClick { it.stopPropagation(); Router.go(Route.Plan) } }) { Icon("local_fire_department"); Text(if (streak.current == 0) "Comece a sequência hoje" else "${streak.current} ${if (streak.current == 1) "dia" else "dias"} seguidos") }
            Span({ classes("hh-pill", "link"); onClick { it.stopPropagation(); Router.go(Route.Achievements) } }) { Icon("military_tech"); Text("Nível ${progress.level} · ${progress.levelTitle}") }
            Span({ classes("hh-pill", "link"); onClick { it.stopPropagation(); Router.go(Route.Stats) } }) { Icon("bolt"); Text("+${progress.xpToday} XP hoje") }
        }
    }

    Div({ classes("grid", "main-side") }) {
        Div({ classes("stack", "loose") }) {
            if (plan != null && data.attempts.size < 5) FirstSteps(data.attempts.size)
            NowPanel(data, plan != null, tasks, hour, seed, daysToExam, onGenerate = { generatingFor = it })
            WeekReportCard(data, view)
            WeakSpotCard(data)
            if (plan != null && tasks.isNotEmpty()) DailyMission(tasks) { completing = it }
            AttentionPanel(data, plan?.id)
            HomeSyllabus(data, subjects)
        }
        Div({ classes("stack", "loose") }) {
            PaceCard(data, plan?.id, plan?.exam)
            PerformanceAndStanding(data, streak.current, streak.best)
            LevelCard(progress)
            StreakWeekCard(streak)
        }
    }

    completing?.let { task -> CompleteTaskDialog(task) { completing = null } }
    generatingFor?.let { id -> GenerateDialog(id) { generatingFor = null } }
}

private fun nextUp(tasks: List<PlanTask>): PlanTask? =
    tasks.firstOrNull { it.status == "EM_ANDAMENTO" } ?: tasks.firstOrNull { it.status == "PLANEJADA" }

private fun hasMaterial(data: Snapshot, topicId: Long): Boolean =
    data.theories.any { it.topicId == topicId } || data.questions.any { it.topicId == topicId } || data.summaries.any { it.topicId == topicId }

/** O "Agora" do app: o Folha conversa com a pessoa e aponta a próxima coisa a fazer. */
@Composable
private fun NowPanel(data: Snapshot, hasPlan: Boolean, tasks: List<PlanTask>, hour: Int, seed: Long, daysToExam: Long?, onGenerate: (Long) -> Unit) {
    val task = nextUp(tasks)
    Div({ classes("now-panel", "rise") }) {
        when {
            !hasPlan -> {
                FolhaTalking("Você não precisa organizar sua vida de estudos sozinho. Me diz seu concurso e sua rotina, que eu cuido da agenda.", 96)
                Span({ classes("eyebrow") }) { Text("COMECE POR AQUI") }
                H2({ classes("now-title") }) { Text("Vamos montar seu plano") }
                Btn("Criar meu plano", { Router.go(Route.Setup) }, block = true, size = "lg", icon = "auto_awesome")
                if (data.questions.isNotEmpty()) Btn("Ou responda 10 questões enquanto isso", { Router.go(Route.Quiz("rapido-10")) }, style = "ghost", block = true)
            }
            tasks.isEmpty() -> {
                FolhaTalking("Hoje não tem nada agendado. Descanso também é parte do plano, mas se quiser adiantar, eu topo!", 96)
                Span({ classes("eyebrow") }) { Text("HOJE") }
                H2({ classes("now-title") }) { Text("Nada agendado para hoje") }
                P({ classes("muted") }) { Text("Seu plano continua amanhã. Se quiser aproveitar o tempo livre, responda algumas questões.") }
                Btn("Treinar questões", { Router.go(Route.Train) }, style = "outline", block = true, icon = "gps_fixed")
            }
            task == null -> {
                val minutes = tasks.sumOf { it.minutes }
                Div({ classes("now-done") }) {
                    // Comemora ao chegar na página; depois volta ao normal (pisca, olha, pensa, reage ao toque).
                    var doneMood by remember { mutableStateOf("happy") }
                    androidx.compose.runtime.LaunchedEffect(Unit) { kotlinx.coroutines.delay(4500); doneMood = "idle" }
                    Folha(doneMood, 120)
                    Div({ classes("grow") }) {
                        Span({ classes("eyebrow") }) { Text("DIA CONCLUÍDO") }
                        H2({ classes("now-title") }) { Text("Missão cumprida por hoje") }
                        P({ classes("muted") }) { Text("${tasks.size} ${if (tasks.size == 1) "missão concluída" else "missões concluídas"} · ${Queries.minutesLabel(minutes)} de estudo. ${FolhaLines.forDayDone(seed)}") }
                    }
                }
            }
            else -> {
                val continuing = task.status == "EM_ANDAMENTO"
                val needsMaterial = task.topicId != null && !hasMaterial(data, task.topicId) && !Store.demo
                // quando ele tem algo pessoal para dizer (saudade, prova chegando, o que você estudou ontem), fala isso
                val memory = if (needsMaterial) null else FolhaFriend.homeLine(data, Progress.of(data), daysToExam, hour, seed)
                // missão do ponto fraco cumprida: ele comemora o antes e o depois aqui no balão
                val mission = androidx.compose.runtime.remember(data.attempts.size, Mission.version) { Mission.result(studyAnswers(data)) }
                FolhaTalking(
                    mission?.let { Mission.line(it) } ?: memory?.text ?: FolhaLines.forHome(continuing, needsMaterial, hour, seed, daysToExam),
                    96,
                    onFolhaClick = { if (needsMaterial) onGenerate(task.topicId!!) else openTask(task) },
                    restMood = if (mission != null) (if (mission.delta >= 0) "proud" else "happy") else memory?.mood?.takeIf { it == "sad" || it == "happy" || it == "proud" } ?: "idle",
                )
                Span({ classes("eyebrow") }) { Text(if (continuing) "CONTINUANDO" else "AGORA") }
                Div({ classes("now-task") }) {
                    Span({ classes("subject-bar"); attr("style", "background:${subjectColor(task.subjectName)}") })
                    Div({ classes("grow") }) {
                        P({ classes("now-subject", "clamp-2") }) { Text(task.subjectName.ifBlank { "Plano de estudos" }) }
                        // Título longo (tópico de edital com lista de siglas) fica menor, para caber sem virar um muro de letras.
                        val title = taskTitle(task)
                        H2({ classes(*listOfNotNull("now-title", "clamp-3", when { title.length > 80 -> "xlong"; title.length > 44 -> "long"; else -> null }).toTypedArray()) }) { Text(title) }
                    }
                }
                P({ classes("muted") }) { Text("${Queries.taskTypeLabel(task.type)} · ${Queries.minutesLabel(task.minutes)}${if (task.questions > 0) " · ${task.questions} questões" else ""}") }
                if (needsMaterial) {
                    P({ classes("small", "muted") }) { Text("Este tópico ainda não tem material. Gere a teoria, os flashcards e as questões em um clique.") }
                    Button({ classes("btn", "primary", "lg", "block"); onClick { onGenerate(task.topicId!!) } }) { Folha("happy", 30); Text("Gerar o material deste tópico") }
                    Btn(if (continuing) "Continuar" else "Começar mesmo assim", { openTask(task) }, style = "outline", block = true)
                } else Btn(if (continuing) "Continuar de onde parei" else "Começar agora", { openTask(task) }, block = true, size = "lg", icon = "play_circle")
            }
        }
    }
}

/** Missão do dia: o dia inteiro do plano numa trilha, com fatias que se enchem. */
@Composable
private fun DailyMission(tasks: List<PlanTask>, onComplete: (PlanTask) -> Unit) {
    val done = tasks.count { it.status == "CONCLUIDA" }
    val total = tasks.size
    val complete = done == total
    val current = nextUp(tasks)
    val planned = tasks.sumOf { it.minutes }
    val doneMinutes = tasks.filter { it.status == "CONCLUIDA" }.sumOf { it.minutes }
    val earned = tasks.filter { it.status == "CONCLUIDA" }.sumOf { Progress.taskXp(it) }
    val possible = tasks.sumOf { Progress.taskXp(it) }
    Card(extra = "mission-card") {
        Div({ classes("row", "between") }) {
            Div({ classes("grow") }) {
                H3 { Text("Missão do dia") }
                P({ classes("small", "muted") }) {
                    Text(if (complete) "Tudo feito. Você cumpriu o plano de hoje." else "${Queries.minutesLabel(doneMinutes)} de ${Queries.minutesLabel(planned)} · faltam ${total - done}")
                }
            }
            B({ classes("mission-count", if (complete) "ok" else "on") }) { Text("$done/$total") }
        }
        Div({ classes("slices") }) {
            tasks.forEach { t -> key(t.id) { Span({ classes(*listOfNotNull(if (t.status == "CONCLUIDA") "done" else if (t.id == current?.id) "now" else null, if (complete) "ok" else null).toTypedArray()) }) } }
        }
        Div({ classes("trail") }) {
            tasks.forEach { t ->
                key(t.id) {
                    val isDone = t.status == "CONCLUIDA"
                    val isNow = t.id == current?.id
                    Div({ classes(*listOfNotNull("trail-row", if (isDone) "done" else null, if (isNow) "now" else null).toTypedArray()) }) {
                        Span({ classes("trail-mark") })
                        Div({ classes("grow"); attr("role", "button"); attr("tabindex", "0"); onClick { openTask(t) } }) {
                            Div({ classes("row"); attr("style", "gap:8px") }) {
                                Span({ classes("trail-title") }) { Text(taskTitle(t)) }
                                if (isNow) Span({ classes("now-tag") }) { Text("Agora") }
                            }
                            Div({ classes("xs", "muted") }) { Text(listOfNotNull(Queries.taskTypeLabel(t.type), Queries.minutesLabel(t.minutes), t.subjectName.takeIf { t.topicName != null && it.isNotBlank() }).joinToString(" · ")) }
                        }
                        Button({
                            classes(*listOfNotNull("check", if (isDone) "on" else null).toTypedArray())
                            attr("aria-label", if (isDone) "Concluída" else "Concluir tarefa"); attr("title", if (isDone) "Concluída" else "Concluir")
                            if (isDone) attr("disabled", "")
                            onClick { onComplete(t) }
                        }) { Icon("check", plain = true) }
                    }
                }
            }
        }
        Div({ classes("row", "between", "wrap"); attr("style", "margin-top:4px") }) {
            Div({ classes("row") }) { Xp(earned, green = complete); if (earned < possible) Span({ classes("xs", "muted") }) { Text("até +$possible se fechar tudo") } }
            Btn("Abrir o plano", { Router.go(Route.Plan) }, style = "ghost", small = true, icon = "calendar_month")
        }
    }
}

/** Previsão: quando o plano termina o conteúdo, perto ou longe da prova. Uma frase, sem alarme. */
@Composable
private fun PaceCard(data: Snapshot, planId: String?, exam: Long?) {
    val todayEpoch = Queries.todayEpoch()
    val pending = if (planId == null) emptyList() else data.tasks.filter { it.planId == planId && (it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO") }
    // Previsão do edital inteiro (como no app); as tarefas marcadas só cobrem as próximas 4 semanas.
    val projection = data.plans.firstOrNull { it.id == planId }?.let { br.com.estudario.web.data.Forecast.of(data, it) }
    val forecast = when {
        projection == null -> pending.filter { it.type == "THEORY" }.maxOfOrNull { it.day } ?: pending.maxOfOrNull { it.day }
        projection.remainingTopics == 0 -> null
        else -> projection.date?.toEpochDays()?.toLong() ?: pending.maxOfOrNull { it.day }
    }
    val (tone, title, detail) = when {
        planId == null -> Triple("var(--faint)", "Sem previsão ainda.", "Monte seu plano e o Estudário calcula quando o edital fecha.")
        forecast == null -> Triple("var(--green)", "Conteúdo previsto coberto.", "Daqui para frente o plano vira revisão e questões.")
        exam == null -> Triple("var(--primary)", "Edital coberto até ${Queries.dayLabel(LocalDate.fromEpochDays(forecast))}.", "Cadastre a data da prova no plano para ver quanto tempo sobra para revisão.")
        exam - forecast >= 14 -> Triple("var(--green)", "Bom ritmo.", "Nesse ritmo, o edital fecha ${exam - forecast} dias antes da prova, em ${Queries.dayLabel(LocalDate.fromEpochDays(forecast))}.")
        exam - forecast >= 0 -> Triple("#F2A900", "Ritmo apertado.", "A previsão de cobertura é ${Queries.dayLabel(LocalDate.fromEpochDays(forecast))}, perto demais da prova para sobrar tempo de revisão.")
        else -> Triple("var(--red)", "O ritmo atual não fecha o edital.", "Sobra conteúdo para ${forecast - exam} dias depois da prova. Ajustar as horas da semana ou as prioridades muda essa conta.")
    }
    Div({ classes("insight", "clickable"); onClick { if (planId == null) Router.go(Route.Setup) else { PlanEntry.overview = true; Router.go(Route.Plan) } } }) {
        Span({ classes("insight-mark"); attr("style", "background:$tone") })
        Div({ classes("grow") }) {
            // Contagem para a prova: os dias que faltam e quanto do edital já foi visto.
            val daysLeft = exam?.let { it - todayEpoch }?.takeIf { it >= 0 }
            if (daysLeft != null) {
                val competition = CompetitionFilter.current(data)
                val leaves = Queries.leafTopics(data, Queries.subjectsOf(data, competition?.id).mapTo(hashSetOf()) { it.id })
                val seen = if (leaves.isEmpty()) null else percent(leaves.count(Queries::isStudied), leaves.size)
                Div({ classes("exam-count") }) {
                    B { Text(if (daysLeft == 0L) "Hoje" else "$daysLeft") }
                    Span { Text(if (daysLeft == 0L) "é o dia da prova" else if (daysLeft == 1L) "dia para a prova" else "dias para a prova") }
                    if (seen != null) Span({ classes("exam-seen") }) { Text("$seen% do edital visto") }
                }
            }
            Span({ classes("insight-label") }) { Text("Previsão") }
            B({ classes("insight-title") }) { Text(title) }
            P({ classes("small", "muted") }) { Text(detail) }
        }
        if (forecast != null && exam != null) Div({ classes("pace-dial") }) {
            val span = (exam - todayEpoch).coerceAtLeast(1)
            val pos = ((forecast - todayEpoch).toDouble() / span).coerceIn(0.0, 1.1)
            Div({ classes("pace-track") }) {
                Span({ classes("pace-fill"); attr("style", "width:${(pos.coerceAtMost(1.0) * 100).toInt()}%;background:$tone") })
                Span({ classes("pace-flag") }) { Icon("flag") }
            }
        }
    }
}

/** Desempenho e constância lado a lado: o que as questões dizem e o que a rotina diz. */
@Composable
private fun PerformanceAndStanding(data: Snapshot, streakDays: Int, best: Int) {
    val now = Date.now()
    val day = 86_400_000.0
    val recent = data.attempts.filter { it.answeredAt >= now - 14 * day }
    val before = data.attempts.filter { it.answeredAt < now - 14 * day && it.answeredAt >= now - 28 * day }
    val accuracy = if (recent.isEmpty()) null else percent(recent.count { it.correct }, recent.size)
    val delta = if (accuracy != null && before.size >= 5) accuracy - percent(before.count { it.correct }, before.size) else null
    Div({ classes("grid", "cols-2"); attr("style", "gap:12px") }) {
        Card(extra = "clickable mini-stat", attrs = { onClick { Router.go(Route.Stats) } }) {
            Span({ classes("insight-label") }) { Text("Desempenho") }
            if (accuracy == null) {
                P({ classes("small", "muted") }) { Text("Ainda sem questões nos últimos 14 dias.") }
                Span({ classes("small", "primary-ink", "strong") }) { Text("Resolver questões →") }
            } else {
                B({ classes("big-num"); attr("style", "color:${accuracyColor(accuracy)}") }) { Text("$accuracy%") }
                P({ classes("xs", "muted") }) { Text("de acerto em ${recent.size} questões (14 dias)") }
                delta?.let {
                    Span({ classes("xs", "strong"); attr("style", "color:${if (it > 0) "#2FA36B" else if (it < 0) "#E5484D" else "var(--muted)"}") }) {
                        Text(when { it > 0 -> "↑ $it pontos"; it < 0 -> "↓ ${-it} pontos"; else -> "estável" })
                    }
                }
            }
        }
        // Constância: "Comece hoje" leva ao plano do dia.
        Card(extra = "clickable mini-stat", attrs = { onClick { Router.go(Route.Plan) } }) {
            Span({ classes("insight-label") }) { Text("Constância") }
            if (streakDays == 0) {
                B({ classes("big-num") }) { Text("Comece hoje") }
                P({ classes("xs", "muted") }) { Text("Estude para formar sua sequência.") }
            } else {
                Div({ classes("row"); attr("style", "gap:6px") }) { Icon("local_fire_department"); B({ classes("big-num") }) { Text("$streakDays ${if (streakDays == 1) "dia" else "dias"}") } }
                P({ classes("xs", "muted") }) { Text("em sequência${if (best > streakDays) " · recorde $best" else ""}") }
            }
        }
    }
}

@Composable
private fun LevelCard(progress: br.com.estudario.domain.ProgressEngine.ProgressSummary) {
    // Com a cara da comemoração de subir de nível: o selo dourado com o número, o título e a barra que enche.
    val pct = (progress.levelProgress * 100).toInt().coerceIn(0, 100)
    val missing = progress.xpForNextLevel - progress.xpIntoLevel
    Div({ classes("lvl-card"); attr("role", "button"); attr("tabindex", "0"); onClick { Router.go(Route.Profile) } }) {
        Div({ classes("lvl-top") }) {
            Div({ classes("lvl-seal"); attr("style", "--ray:${levelMetal(progress.level).second}") }) {
                org.jetbrains.compose.web.dom.Img(src = levelCrestSvg(progress.level), alt = "", attrs = { attr("aria-hidden", "true") })
            }
            Div({ classes("grow") }) {
                Span({ classes("lvl-eyebrow") }) { Text("SEU NÍVEL") }
                B({ classes("lvl-title") }) { Text("Nível ${progress.level} · ${progress.levelTitle}") }
                Div({ classes("lvl-sub") }) { Text("${progress.totalXp} XP no total") }
            }
            if (progress.xpToday > 0) Span({ classes("lvl-today") }) { Icon("bolt", filled = true); Text("+${progress.xpToday} XP hoje") }
        }
        Div({ classes("lvl-bar") }) {
            Span({ classes("lvl-fill"); attr("style", "width:${pct.coerceAtLeast(4)}%") })
            B { Text("${progress.xpIntoLevel} / ${progress.xpForNextLevel} XP") }
        }
        Div({ classes("lvl-next") }) { Text("Faltam $missing XP para o nível ${progress.level + 1}") }
    }
}

/** O metal do nível (claro, médio, escuro), por faixa: bronze até o 9, prata, ouro, safira, rubi e esmeralda do 50 em diante. */
internal fun levelMetal(level: Int): Triple<String, String, String> = when ((level / 10).coerceIn(0, 5)) {
    0 -> Triple("#FFD9B3", "#C8743A", "#7A3F19")
    1 -> Triple("#FFFFFF", "#AEB9C6", "#5B6572")
    2 -> Triple("#FFF1B8", "#F2B320", "#9A6400")
    3 -> Triple("#D6E6FF", "#5B8CFF", "#1F3FA8")
    4 -> Triple("#FFD3DD", "#F0446A", "#8E1236")
    else -> Triple("#C9FFE6", "#2FD39A", "#0B6E4C")
}

/**
 * O brasão do nível, no estilo das medalhas das Conquistas: coroa de louros no metal da faixa,
 * joia no alto, medalhão com o número e a fita "NÍVEL" embaixo. Os raios girando ficam no CSS.
 */
internal fun levelCrestSvg(level: Int): String {
    val (light, mid, dark) = levelMetal(level)
    val leaves = StringBuilder()
    // um ramo de cada lado, subindo da fita até perto da joia
    for (side in listOf(-1, 1)) for (i in 0 until 9) {
        val deg = 112.0 + i * 16.0
        val a = deg * kotlin.math.PI / 180
        val x = (80 - side * kotlin.math.cos(a) * 60).toInt()
        val y = (84 + kotlin.math.sin(a) * 60).toInt()
        val rot = (if (side < 0) deg + 118 else -deg - 118).toInt()
        val s = 1.0 - i * 0.035
        leaves.append("""<ellipse cx="$x" cy="$y" rx="${(12 * s).toInt()}" ry="${(55 * s).toInt() / 10.0}" transform="rotate($rot $x $y)" fill="url(#m)" stroke="$dark" stroke-width="1.4"/>""")
    }
    val size = if (level >= 100) 26 else if (level >= 10) 34 else 40
    val svg = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 160 160">
<defs><linearGradient id="m" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="$light"/><stop offset=".55" stop-color="$mid"/><stop offset="1" stop-color="$dark"/></linearGradient>
<linearGradient id="c" x1="0" y1="0" x2="1" y2="1"><stop offset="0" stop-color="#6A4CF0"/><stop offset="1" stop-color="#22187A"/></linearGradient>
<linearGradient id="f" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#3D3AB8"/><stop offset="1" stop-color="#1D1670"/></linearGradient></defs>
$leaves
<circle cx="80" cy="80" r="42" fill="url(#m)" stroke="$dark" stroke-width="3"/>
<circle cx="80" cy="80" r="33" fill="url(#c)" stroke="$light" stroke-width="2.5"/>
<path d="M80 22 L91 34 L80 46 L69 34 Z" fill="$mid" stroke="$dark" stroke-width="2.5" stroke-linejoin="round"/><path d="M80 26 L86 34 L80 34 Z" fill="#fff" fill-opacity=".7"/>
<path d="M26 118 L44 112 L44 136 L32 132 Z" fill="#141058"/><path d="M134 118 L116 112 L116 136 L128 132 Z" fill="#141058"/>
<path d="M40 110 Q80 122 120 110 L120 134 Q80 146 40 134 Z" fill="url(#f)" stroke="$mid" stroke-width="2"/>
<text x="80" y="134" text-anchor="middle" font-family="Nunito, Arial, sans-serif" font-weight="900" font-size="12" letter-spacing="2" fill="$light">NÍVEL</text>
<text x="80" y="${if (level >= 10) 92 else 94}" text-anchor="middle" font-family="Nunito, Arial, sans-serif" font-weight="900" font-size="$size" fill="#FFFFFF">$level</text>
<path d="M58 64 q8 -10 20 -11" stroke="#FFFFFF" stroke-opacity=".5" stroke-width="4" fill="none" stroke-linecap="round"/></svg>"""
    return "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(svg) as String
}

/**
 * A semana da sequência: cada dia feito é uma chama (a fênix), o dia salvo é o marca-página do Folha.
 * Embaixo, os marca-páginas guardados (até 2) e quanto falta para ganhar o próximo.
 */
@Composable
private fun StreakWeekCard(streak: br.com.estudario.domain.StreakSummary) {
    val todayIndex = Queries.todayDate().isoDayOfWeek - 1
    Div({ classes("sw-card") }) {
        Div({ classes("sw-head") }) {
            org.jetbrains.compose.web.dom.Img(src = "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(FLAME_SVG) as String, alt = "", attrs = { classes("sw-flame-big"); attr("aria-hidden", "true") })
            Div({ classes("grow") }) {
                Span({ classes("sw-eyebrow") }) { Text("SEQUÊNCIA DA FÊNIX") }
                B({ classes("sw-count") }) { Text(if (streak.current == 0) "Comece hoje" else "${streak.current} ${if (streak.current == 1) "dia" else "dias"}") }
                Div({ classes("sw-sub") }) { Text("${streak.week.count { it.done }} de 7 dias no alvo · recorde ${streak.best}") }
            }
        }
        Div({ classes("sw-week") }) {
            streak.week.forEachIndexed { i, day ->
                val state = when {
                    day.done -> "fire"
                    day.protected -> "ice"
                    day.partial -> "partial"
                    i == todayIndex -> "today"
                    day.future -> "future"
                    else -> "miss"
                }
                Div({ classes("sw-day", state); attr("title", when (state) { "fire" -> "Dia cumprido"; "ice" -> "O Folha guardou este dia com um marca-página"; "partial" -> "Estudou, mas não fechou a meta"; "today" -> "Hoje: ainda dá tempo"; "future" -> ""; else -> "Sem estudo" }) }) {
                    Span({ classes("sw-letter") }) { Text(Queries.weekdayShort[i].take(1).uppercase()) }
                    Div({ classes("sw-token") }) {
                        when (state) {
                            "fire" -> org.jetbrains.compose.web.dom.Img(src = "data:image/svg+xml;charset=utf-8," + js("encodeURIComponent")(FLAME_SVG) as String, alt = "")
                            "ice" -> org.jetbrains.compose.web.dom.Img(src = bookmarkSrc(), alt = "")
                            else -> Unit
                        }
                    }
                }
            }
        }
        // Os marca-páginas do Folha: até 2 guardados e quanto falta para o próximo.
        Div({ classes("sw-ice"); attr("title", "A cada 7 dias seguidos o Folha guarda um marca-página (até 2). Num dia sem estudo, ele marca aquele dia e a sequência não apaga.") }) {
            Div({ classes("sw-ice-slots") }) {
                repeat(br.com.estudario.domain.StreakEngine.MAX_FREEZES) { k ->
                    Span({ classes(*listOfNotNull("sw-slot", if (k < streak.freezes) "full" else null).toTypedArray()) }) {
                        org.jetbrains.compose.web.dom.Img(src = bookmarkSrc(), alt = "")
                    }
                }
            }
            Div({ classes("grow") }) {
                B { Text(if (streak.freezes > 0) "${streak.freezes} ${if (streak.freezes == 1) "marca-página do Folha" else "marca-páginas do Folha"}" else "Nenhum marca-página guardado") }
                val next = streak.daysToNextFreeze
                if (next > 0) {
                    val done = (br.com.estudario.domain.StreakEngine.DAYS_PER_FREEZE - next).coerceAtLeast(0)
                    Div({ classes("sw-ice-bar") }) { Span({ attr("style", "width:${done * 100 / br.com.estudario.domain.StreakEngine.DAYS_PER_FREEZE}%") }) }
                    Div({ classes("sw-sub") }) { Text("Mais $next ${if (next == 1) "dia seguido" else "dias seguidos"} e o Folha guarda outro") }
                } else Div({ classes("sw-sub") }) { Text("Cheio: o Folha protege 2 dias ruins") }
            }
        }
        if (streak.week.any { it.protected }) Div({ classes("sw-saved") }) { Text("O Folha guardou seu lugar num dia desta semana.") }
    }
}

private const val FLAME_SVG = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><defs><linearGradient id="f" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FF4D2E"/><stop offset=".55" stop-color="#FF9A1F"/><stop offset="1" stop-color="#FFD84A"/></linearGradient><linearGradient id="i" x1="0" y1="1" x2="0" y2="0"><stop offset="0" stop-color="#FFB13B"/><stop offset="1" stop-color="#FFF4B8"/></linearGradient></defs><path d="M33 4c3 10 15 16 15 31a16 16 0 0 1-32 0c0-8 4-12 7-16 1 5 3 8 6 9-2-9 0-17 4-24z" fill="url(#f)"/><path d="M32 30c2 5 8 8 8 15a8 8 0 0 1-16 0c0-4 2-6 4-8 0 3 2 5 3 5-1-5 0-9 1-12z" fill="url(#i)"/></svg>"""


/** Pedindo atenção: revisões, erros, tarefas atrasadas e o tópico mais fraco. Só o que existe. */
@Composable
private fun AttentionPanel(data: Snapshot, planId: String?) {
    val due = Queries.dueReviews(data)
    val errors = Queries.pendingErrors(data)
    val todayEpoch = Queries.todayEpoch()
    val overdue = if (planId == null) emptyList() else data.tasks.filter { it.planId == planId && it.day < todayEpoch && (it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO") }
    val questions = data.questions.associateBy { it.id }
    val weak = data.attempts.groupBy { questions[it.questionId]?.topicId }
        .filterKeys { it != null }
        .mapValues { (_, list) -> list.size to percent(list.count { it.correct }, list.size) }
        .filter { it.value.first >= 5 && it.value.second < 60 }
        .minByOrNull { it.value.second }
    val weakTopic = weak?.key?.let { id -> data.topics.firstOrNull { it.id == id } }
    Card {
        CardHead("Pedindo atenção")
        Div({ classes("attn-grid") }) {
            AttentionTile("autorenew", "Revisões", due.size, if (due.isEmpty()) "Nenhuma para hoje" else "para hoje", "#2EA8F5") { Router.go(Route.Reviews) }
            AttentionTile("error_med", "Caderno de erros", errors.size, if (errors.isEmpty()) "Tudo em dia" else "para rever", "#FF5C4D") { Router.go(Route.Errors) }
            if (planId != null) AttentionTile("event_busy", "Atrasadas", overdue.size, if (overdue.isEmpty()) "Plano em dia" else "no plano", "#F2A900") { PlanEntry.overdue = overdue.isNotEmpty(); Router.go(Route.Plan) }
            AttentionTile("bookmarks", "Caderno de estudo", null, "Grifos, salvos e notas", "#1FB574") { Router.go(Route.Notebook) }
        }
        if (weakTopic != null) Button({ classes("weak-row"); onClick { Router.go(Route.Topic(weakTopic.id)) } }) {
            Icon("trending_down")
            Div({ classes("grow") }) {
                Span({ classes("xs", "muted") }) { Text("Seu ponto mais fraco agora") }
                B({ classes("clamp-2") }) { Text(weakTopic.title) }
            }
            B({ attr("style", "color:${accuracyColor(weak.value.second)}") }) { Text("${weak.value.second}%") }
            Icon("chevron_right", extraClass = "faint")
        }
    }
}

@Composable
private fun AttentionTile(icon: String, title: String, count: Int?, hint: String, color: String, onClick: () -> Unit) {
    val latest = androidx.compose.runtime.rememberUpdatedState(onClick)
    Button({ classes("attn-tile"); attr("style", "--tile:$color"); onClick { latest.value() } }) {
        Icon(icon)
        Div({ classes("grow") }) {
            B { Text(title) }
            Span({ classes("xs", "muted") }) { Text(hint) }
        }
        if (count != null && count > 0) Span({ classes("attn-count") }) { Text("$count") }
    }
}

@Composable
fun WeekDots(days: List<Pair<Boolean, Boolean>>, todayIndex: Int) {
    Div({ classes("week-dots") }) {
        days.forEachIndexed { index, (done, partial) ->
            Div({ classes("wd") }) {
                Span({ classes("xs", "muted", "strong") }) { Text(Queries.weekdayShort[index].take(1).uppercase()) }
                val style = when {
                    done -> "background:var(--green);color:var(--paper)"
                    partial -> "background:var(--amber-soft);color:var(--on-amber-soft)"
                    index == todayIndex -> "border:2px solid var(--primary)"
                    else -> "background:var(--soft-2)"
                }
                Div({ classes("circle"); attr("style", style) }) { if (done) Icon("check", plain = true) }
            }
        }
    }
}

/**
 * Uma tarefa do plano, como o MissionCard do app: a matéria na cor do estado, o tipo, o título,
 * tempo e questões, os botões e o XP.
 */
@Composable
fun TaskRow(task: PlanTask, onComplete: () -> Unit) {
    val done = task.status == "CONCLUIDA"
    val todayEpoch = Queries.todayEpoch()
    val pending = task.status == "PLANEJADA" || task.status == "EM_ANDAMENTO"
    val overdue = pending && task.day < todayEpoch
    val statusColor = when {
        done -> "var(--green)"
        task.status == "EM_ANDAMENTO" -> "var(--primary)"
        overdue -> "var(--red)"
        pending -> "var(--faint)"
        else -> "var(--line-strong)"
    }
    val latest = androidx.compose.runtime.rememberUpdatedState(onComplete)
    Div({ classes(*listOfNotNull("mission", if (done) "done" else null).toTypedArray()); attr("style", "--status:$statusColor;--subject:${subjectColor(task.subjectName)}") }) {
        Div({ classes("mission-top") }) {
            Div({ classes("row", "grow"); attr("style", "gap:8px;min-width:0") }) {
                Span({ classes("status-dot") })
                // Com mais de um concurso, o nome dele vai junto da matéria.
                val competitionName = Store.data.competitions.takeIf { it.size > 1 }?.firstOrNull { it.id == task.competitionId }?.name
                Span({ classes("mission-subject", "clamp-2") }) { Text((task.subjectName.ifBlank { "Plano" } + (competitionName?.let { " · $it" } ?: "")).uppercase()) }
            }
            Span({ classes("type-chip") }) { Icon(taskIcon(task.type)); Text(Queries.taskTypeLabel(task.type)) }
        }
        // O nome abre o conteúdo do tópico (concluída ou não); os botões continuam começando a tarefa.
        Div({ classes("mission-title"); attr("role", "button"); attr("tabindex", "0"); attr("title", if (task.topicId != null) "Abrir o conteúdo" else ""); onClick { if (task.topicId != null) Router.go(Route.Topic(task.topicId)) else openTask(task) } }) { Text(taskTitle(task)) }
        Div({ classes("mission-meta") }) {
            Span { Text(Queries.minutesLabel(task.minutes)) }
            if (task.questions > 0) Span { Text("${task.questions} questões") }
            Span { Text("Prioridade ${priorityWord(task.priority)}") }
            when {
                overdue -> Span({ classes("late") }) { Text("atrasada") }
                task.status == "EM_ANDAMENTO" -> Span({ classes("going") }) { Text("em andamento") }
                task.status == "NAO_REALIZADA" -> Span { Text("não realizada") }
            }
        }
        // Questões de um tópico: onde está a teoria dele (feita, atrasada ou marcada para outro dia).
        if (task.type == "QUESTIONS" && task.topicId != null && !done) {
            val theory = Store.data.tasks.filter { it.topicId == task.topicId && it.planId == task.planId && it.type == "THEORY" && it.status != "REPROGRAMADA" && it.status != "CANCELADA" }.maxByOrNull { it.day }
            val studied = Store.data.topics.firstOrNull { it.id == task.topicId }?.let(Queries::isStudied) == true
            fun date(day: Long) = Queries.shortDate(kotlinx.datetime.LocalDate.fromEpochDays(day.toInt()))
            val (icon, text, warn) = when {
                theory == null -> if (studied) Triple("task_alt", "Tópico já estudado: questões para fixar", false) else Triple("info", "A teoria deste tópico não está no plano: leia o conteúdo antes", true)
                theory.status == "CONCLUIDA" -> Triple("task_alt", "Teoria feita em ${date(theory.day)}", false)
                theory.status == "NAO_REALIZADA" -> Triple("warning", "Teoria pulada em ${date(theory.day)}: leia o conteúdo antes", true)
                theory.day < todayEpoch -> Triple("warning", "Teoria atrasada desde ${date(theory.day)}: faça ela antes", true)
                theory.day > task.day -> Triple("event", "Teoria marcada para ${date(theory.day)}", true)
                else -> Triple("menu_book", "Teoria no mesmo dia, antes destas questões", false)
            }
            Div({ classes(*listOfNotNull("mission-theory", if (warn) "warn" else null).toTypedArray()); attr("role", "button"); attr("tabindex", "0"); onClick { Router.go(Route.Topic(task.topicId)) } }) { Icon(icon); Text(text) }
        }
        Div({ classes("mission-actions") }) {
            Div({ classes("row", "wrap"); attr("style", "gap:8px") }) {
                if (done) Span({ classes("done-pill") }) { Icon("check_circle"); Text("Concluída") }
                else if (task.status != "NAO_REALIZADA") {
                    // Tarefa de um dia que ainda vem: "Adiantar" (no Grátis, adiantar segue a regra dele, mostrada ao tocar).
                    if (task.status == "PLANEJADA" && task.day > todayEpoch) Btn("Adiantar", { advanceTask(task) }, small = true, style = "primary", icon = "fast_forward")
                    else Btn(if (task.status == "EM_ANDAMENTO") "Continuar" else if (overdue) "Fazer agora" else "Começar", { openTask(task) }, small = true, style = "primary")
                    Btn("Concluir", { latest.value() }, small = true, style = "tonal", icon = "check")
                    if (!task.locked && !Store.demo) Btn("Reprogramar", { Reprogram.task = task }, small = true, style = if (overdue) "outline" else "ghost", icon = "event_repeat")
                }
            }
            Xp(Progress.taskXp(task), green = done)
        }
    }
}

private fun priorityWord(priority: String) = when (priority) {
    "VERY_HIGH" -> "muito alta"
    "HIGH" -> "alta"
    "LOW" -> "baixa"
    "VERY_LOW" -> "muito baixa"
    else -> "média"
}

fun taskIcon(type: String) = when (type) {
    "THEORY" -> "menu_book"
    "QUESTIONS" -> "checklist"
    "REVIEW" -> "autorenew"
    "ACTIVE_RECALL" -> "psychology"
    "FLASHCARDS" -> "style"
    "SIMULATION" -> "assignment"
    "DISCURSIVE" -> "edit_note"
    else -> "task_alt"
}

fun taskColors(type: String): Pair<String, String> = when (type) {
    "THEORY" -> "var(--primary-soft)" to "var(--primary)"
    "QUESTIONS" -> "var(--green-soft)" to "var(--green)"
    "REVIEW", "ACTIVE_RECALL", "FLASHCARDS" -> "var(--amber-soft)" to "var(--amber-strong)"
    else -> "var(--soft-2)" to "var(--faint)"
}

/** Concluir uma tarefa: tempo e acertos com o contador de mais e menos, como no app. */
@Composable
fun CompleteTaskDialog(task: PlanTask, onClose: () -> Unit) {
    // Foco rodando desta tarefa (ou do tópico dela): concluir encerra o foco e usa o tempo medido.
    val focusOfTask = TopicFocus.active && (TopicFocus.taskId == task.id || (TopicFocus.taskId == null && TopicFocus.topicId != null && TopicFocus.topicId == task.topicId))
    var minutes by remember { mutableStateOf(if (focusOfTask) TopicFocus.elapsedMinutes.coerceAtLeast(1) else task.minutes) }
    var questions by remember { mutableStateOf(task.questions) }
    var correct by remember { mutableStateOf(0) }
    Modal(onDismiss = onClose) {
        Div {
            Span({ classes("eyebrow") }) { Text(Queries.taskTypeLabel(task.type)) }
            H2 { Text(task.topicName ?: task.subjectName) }
            P({ classes("muted", "small") }) { Text(task.subjectName) }
        }
        Div({ classes("day-row") }) {
            Div { Div({ classes("name") }) { Text("Tempo estudado") }; Div({ classes("hint") }) { Text("Planejado: ${Queries.minutesLabel(task.minutes)}") } }
            Stepper(minutes, { minutes = it }, step = 5, min = 0, max = 600, format = Queries::minutesLabel, label = "Tempo estudado")
        }
        if (task.type == "QUESTIONS" || task.questions > 0) {
            Div({ classes("day-row") }) {
                Div({ classes("name") }) { Text("Questões feitas") }
                Stepper(questions, { questions = it; if (correct > it) correct = it }, step = 1, min = 0, max = 300, label = "Questões feitas")
            }
            Div({ classes("day-row") }) {
                Div({ classes("name") }) { Text("Acertos") }
                Stepper(correct, { correct = it }, step = 1, min = 0, max = questions, label = "Acertos")
            }
        }
        if (focusOfTask) Div({ classes("banner", "info") }) { Icon("timer"); Text("O modo foco desta tarefa está rodando (${Queries.minutesLabel(TopicFocus.elapsedMinutes.coerceAtLeast(1))}). Ao concluir, ele é encerrado e esse tempo entra aqui.") }
        if (minutes < task.minutes) Div({ classes("banner", "info") }) { Icon("info"); Text("Menos tempo que o planejado: a tarefa fica em andamento para você continuar depois, como no app.") }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Concluir", {
                if (focusOfTask) TopicFocus.stop()
                Store.update { data ->
                    val worked = Actions.completeTask(data, task.id, minutes, questions, correct.coerceIn(0, questions))
                    // Teoria concluída no plano também marca o tópico como estudado (e agenda as revisões).
                    val done = if (task.type == "THEORY" && task.topicId != null && minutes >= task.minutes) Actions.completeStudy(worked, task.topicId) else worked
                    // Como no app: concluir reorganiza o plano.
                    runCatching { Planning.replan(done, task.planId, ReplanReason.TASK_COMPLETED) }.getOrDefault(done)
                }
                if (minutes >= task.minutes) som("concluir")
                Toast.show(if (minutes >= task.minutes) "Tarefa concluída! +${Progress.taskXp(task)} XP" else "Progresso registrado")
                onClose()
            }, icon = "check")
        }
    }
}

fun openTask(task: PlanTask) {
    // Quem ainda não respondeu nenhuma questão começa pela teoria do tópico: o primeiro contato é
    // ler e depois acertar, não errar logo de cara.
    val beginner = Store.data.attempts.isEmpty()
    when {
        beginner && task.type == "QUESTIONS" && task.topicId != null -> Router.go(Route.Topic(task.topicId))
        // Tarefa de questões de um tópico abre as questões dele (antes abria só a página do tópico).
        task.type == "QUESTIONS" && task.topicId != null && Store.data.questions.any { it.topicId == task.topicId && !it.hidden } ->
            planQuiz(task, "topico-${task.topicId}-${task.questions.coerceAtLeast(5)}")
        task.topicId != null -> Router.go(Route.Topic(task.topicId))
        task.type == "SIMULATION" -> Router.go(Route.Simulations)
        task.type == "QUESTIONS" && task.subjectId != null -> planQuiz(task, "materia-${task.subjectId}-${task.questions.coerceAtLeast(5)}")
        task.type == "REVIEW" -> Router.go(Route.Reviews)
        task.type == "FLASHCARDS" || task.type == "ACTIVE_RECALL" -> Router.go(Route.Flashcards(null))
        else -> Router.go(Route.Focus)
    }
}

/** taskTitlePtBr do app: o tópico, ou uma ordem clara quando a tarefa é da matéria toda. */
fun taskTitle(task: PlanTask): String {
    task.topicName?.takeIf { it.isNotBlank() }?.let { return it }
    val subject = task.subjectName.ifBlank { "todas as matérias" }
    val base = when (task.type) {
        "THEORY" -> "Teoria de $subject"
        "QUESTIONS" -> "Questões de $subject"
        "REVIEW" -> "Revisão de $subject"
        "ACTIVE_RECALL" -> "Recordação ativa de $subject"
        "FLASHCARDS" -> "Flashcards de $subject"
        "SIMULATION" -> "Simulado de $subject"
        "DISCURSIVE" -> "Discursiva de $subject"
        else -> subject
    }
    return if (task.questions > 0 && task.type == "QUESTIONS") "$base · ${task.questions} questões" else base
}

/** A tarefa a reprogramar (qualquer tela abre a mesma janela, montada no App). */
object Reprogram { var task by androidx.compose.runtime.mutableStateOf<PlanTask?>(null) }

/** Adiantar um tópico ainda sem matéria: a geração abre direto, com o saldo conferido na frente. */
object AdvanceGenerate { var topicId by androidx.compose.runtime.mutableStateOf<Long?>(null) }

@Composable
fun AdvanceGenerateHost() {
    val id = AdvanceGenerate.topicId ?: return
    GenerateDialog(id) { AdvanceGenerate.topicId = null }
}

/** Tocar em Adiantar: sem matéria no aparelho, abre a geração (com a checagem); com matéria, abre a tarefa. */
fun advanceTask(task: PlanTask) {
    val topicId = task.topicId
    if (topicId != null && task.type == "THEORY" && Store.data.theories.none { it.topicId == topicId }) AdvanceGenerate.topicId = topicId
    else openTask(task)
}

/**
 * Para onde levar uma tarefa: três caminhos claros, como o ReprogramDialog do app. O que já foi
 * feito dela continua registrado.
 */
@Composable
fun ReprogramHost() {
    val task = Reprogram.task ?: return
    val close = { Reprogram.task = null }
    fun pick(day: Long?) {
        Store.update { Actions.reprogramTask(it, task.id, day) }
        Toast.show(when (day) { null -> "O plano encaixou a atividade no próximo espaço livre."; Queries.todayEpoch() -> "Atividade trazida para hoje."; else -> "Atividade movida para amanhã." })
        close()
    }
    Modal(onDismiss = close) {
        Span({ classes("eyebrow") }) { Text("REPROGRAMAR") }
        org.jetbrains.compose.web.dom.H2 { Text(taskTitle(task)) }
        P({ classes("small", "muted") }) { Text("O que você já fez dela continua registrado.") }
        Div({ classes("reprog") }) {
            ReprogramOption("#7C5CFF", "auto_awesome", "Deixar o plano encaixar", "Vai para o primeiro espaço livre do seu cronograma") { pick(null) }
            ReprogramOption("#22C59A", "event", "Amanhã", "Entra no plano de amanhã") { pick(Queries.todayEpoch() + 1) }
            ReprogramOption("#F2852B", "today", "Hoje", "Fazer ainda hoje, junto com o que já estava") { pick(Queries.todayEpoch()) }
        }
        Div({ classes("row", "end") }) { Btn("Cancelar", close, style = "ghost") }
    }
}

@Composable
private fun ReprogramOption(color: String, icon: String, title: String, detail: String, onClick: () -> Unit) {
    org.jetbrains.compose.web.dom.Button({ classes("reprog-opt"); attr("style", "--c:$color"); onClick { onClick() } }) {
        Span({ classes("reprog-ic") }) { Icon(icon) }
        Div({ classes("grow") }) { B { Text(title) }; Span({ classes("small", "muted") }) { Text(detail) } }
        Icon("chevron_right", extraClass = "faint")
    }
}

/** A tarefa do plano que abriu o treino: ao terminar, ela é concluída com o resultado. */
object PlanQuiz {
    var taskId: String? = null
    var scope: String? = null
}

/** Abre o treino com a quantidade que a tarefa pede (não as questões todas da pessoa). */
private fun planQuiz(task: PlanTask, scope: String) {
    PlanQuiz.taskId = task.id
    PlanQuiz.scope = scope
    Router.go(Route.Quiz(scope))
}

/**
 * Os primeiros 3 minutos: o concurso já escolhido, ver o plano montado e responder 5 questões com o
 * Folha comemorando no fim. Some sozinho depois das 5 questões (ou quando a pessoa fecha).
 */
@Composable
private fun FirstSteps(answered: Int) {
    fun read(k: String) = runCatching { kotlinx.browser.window.localStorage.getItem(k) == "1" }.getOrDefault(false)
    fun write(k: String) { runCatching { kotlinx.browser.window.localStorage.setItem(k, "1") } }
    var hidden by remember { mutableStateOf(read("estudario.first.hidden")) }
    var planSeen by remember { mutableStateOf(read("estudario.first.plan")) }
    if (hidden) return
    val left = listOf(true, planSeen, answered >= 5).count { !it }
    Div({ classes("first-steps") }) {
        Div({ classes("row"); attr("style", "gap:12px;align-items:center") }) {
            Folha("talking", 56)
            Div({ classes("grow") }) {
                B { Text("Seus primeiros 3 minutos") }
                Div({ classes("small", "muted") }) { Text("Eu te mostro o caminho. Faltam $left ${if (left == 1) "passo" else "passos"}.") }
            }
            Btn("Fechar", { hidden = true; write("estudario.first.hidden") }, style = "ghost", small = true)
        }
        Div({ classes("first-list") }) {
            FirstStepRow(1, "Escolher o concurso", true, null) {}
            FirstStepRow(2, "Ver o seu plano montado", planSeen, "Ver plano") { planSeen = true; write("estudario.first.plan"); Router.go(Route.Plan) }
            FirstStepRow(3, if (answered > 0) "Responder 5 questões ($answered de 5)" else "Responder 5 questões", answered >= 5, "Começar") { Router.go(Route.Quiz("rapido-5")) }
        }
    }
}

@Composable
private fun FirstStepRow(n: Int, label: String, done: Boolean, action: String?, onClick: () -> Unit) {
    Div({ classes(*listOfNotNull("first-step", if (done) "done" else null).toTypedArray()) }) {
        Span({ classes("first-n") }) { Text(if (done) "✓" else "$n") }
        Span({ classes("grow", "strong") }) { Text(label) }
        if (!done && action != null) Btn(action, onClick, style = "primary", small = true)
    }
}
