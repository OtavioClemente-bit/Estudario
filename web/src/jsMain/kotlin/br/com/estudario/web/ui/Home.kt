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
                H2({ classes("hh-title", "clamp-3") }) { Text(competition?.name ?: "Seu concurso") }
                plan?.objective?.takeIf { it.isNotBlank() && it != competition?.name }?.let { P({ classes("hh-sub", "clamp-2") }) { Text(it) } }
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
            NowPanel(data, plan != null, tasks, hour, seed, onGenerate = { generatingFor = it })
            if (plan != null && tasks.isNotEmpty()) DailyMission(tasks) { completing = it }
            AttentionPanel(data, plan?.id)
        }
        Div({ classes("stack", "loose") }) {
            PaceCard(data, plan?.id, plan?.exam)
            PerformanceAndStanding(data, streak.current, streak.best)
            LevelCard(progress)
            Card {
                CardHead("Ritmo da semana") { Span({ classes("small", "muted", "strong") }) { Text("recorde: ${streak.best}") } }
                P({ classes("small", "muted"); attr("style", "margin:-8px 0 14px") }) { Text("${streak.week.count { it.done }} de 7 dias no alvo") }
                WeekDots(streak.week.map { it.done to it.partial }, Queries.todayDate().isoDayOfWeek - 1)
            }
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
private fun NowPanel(data: Snapshot, hasPlan: Boolean, tasks: List<PlanTask>, hour: Int, seed: Long, onGenerate: (Long) -> Unit) {
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
                    Folha("happy", 120)
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
                FolhaTalking(
                    FolhaLines.forHome(continuing, needsMaterial, hour, seed),
                    96,
                    onFolhaClick = { if (needsMaterial) onGenerate(task.topicId!!) else openTask(task) },
                )
                Span({ classes("eyebrow") }) { Text(if (continuing) "CONTINUANDO" else "AGORA") }
                Div({ classes("now-task") }) {
                    Span({ classes("subject-bar"); attr("style", "background:${subjectColor(task.subjectName)}") })
                    Div({ classes("grow") }) {
                        P({ classes("now-subject", "clamp-2") }) { Text(task.subjectName.ifBlank { "Plano de estudos" }) }
                        H2({ classes("now-title", "clamp-3") }) { Text(taskTitle(task)) }
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
    val forecast = pending.filter { it.type == "THEORY" }.maxOfOrNull { it.day } ?: pending.maxOfOrNull { it.day }
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
    Card(extra = "clickable", attrs = { onClick { Router.go(Route.Profile) } }) {
        Div({ classes("row", "between") }) {
            Div({ classes("row") }) {
                Span({ classes("level-badge") }) { Text("${progress.level}") }
                Div {
                    B { Text("Nível ${progress.level} · ${progress.levelTitle}") }
                    Div({ classes("xs", "muted") }) { Text("${progress.totalXp} XP no total") }
                }
            }
            Xp(progress.xpToday, green = progress.xpToday > 0)
        }
        Div({ attr("style", "margin-top:12px") }) { ProgressBar(progress.levelProgress.toDouble()) }
        P({ classes("xs", "muted"); attr("style", "margin-top:6px") }) { Text("Faltam ${progress.xpForNextLevel - progress.xpIntoLevel} XP para o nível ${progress.level + 1}") }
    }
}

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
            if (planId != null) AttentionTile("event_busy", "Atrasadas", overdue.size, if (overdue.isEmpty()) "Plano em dia" else "no plano", "#F2A900") { Router.go(Route.Plan) }
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
                Span({ classes("mission-subject", "clamp-2") }) { Text(task.subjectName.ifBlank { "Plano" }.uppercase()) }
            }
            Span({ classes("type-chip") }) { Icon(taskIcon(task.type)); Text(Queries.taskTypeLabel(task.type)) }
        }
        Div({ classes("mission-title"); attr("role", "button"); attr("tabindex", "0"); onClick { openTask(task) } }) { Text(taskTitle(task)) }
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
        Div({ classes("mission-actions") }) {
            Div({ classes("row", "wrap"); attr("style", "gap:8px") }) {
                if (done) Span({ classes("done-pill") }) { Icon("check_circle"); Text("Concluída") }
                else if (task.status != "NAO_REALIZADA") {
                    Btn(if (task.status == "EM_ANDAMENTO") "Continuar" else if (overdue) "Fazer agora" else "Começar", { openTask(task) }, small = true, style = if (overdue) "outline" else "primary")
                    Btn("Concluir", { latest.value() }, small = true, style = "tonal", icon = "check")
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
    var minutes by remember { mutableStateOf(task.minutes) }
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
        if (minutes < task.minutes) Div({ classes("banner", "info") }) { Icon("info"); Text("Menos tempo que o planejado: a tarefa fica em andamento para você continuar depois, como no app.") }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Concluir", {
                Store.update { data ->
                    val done = Actions.completeTask(data, task.id, minutes, questions, correct.coerceIn(0, questions))
                    // Como no app: concluir reorganiza o plano.
                    runCatching { Planning.replan(done, task.planId, ReplanReason.TASK_COMPLETED) }.getOrDefault(done)
                }
                Toast.show(if (minutes >= task.minutes) "Tarefa concluída! +${Progress.taskXp(task)} XP" else "Progresso registrado")
                onClose()
            }, icon = "check")
        }
    }
}

fun openTask(task: PlanTask) {
    when {
        task.topicId != null -> Router.go(Route.Topic(task.topicId))
        task.type == "SIMULATION" -> Router.go(Route.Simulations)
        task.type == "QUESTIONS" && task.subjectId != null -> Router.go(Route.Quiz("materia-${task.subjectId}-${task.questions.coerceAtLeast(10)}"))
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
