package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.time.minusDays
import br.com.estudario.web.Theme
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// ------------------------------------------------------------------ desempenho

@Composable
fun StatsScreen() {
    val data = Store.data
    val today = Queries.todayDate()
    val activity = Queries.dailyActivity(data).associateBy { it.date }
    val streak = Queries.streak(data)
    val topics = data.topics.associateBy { it.id }
    val questions = data.questions.associateBy { it.id }
    val subjects = Queries.subjectsOf(data, Queries.primaryCompetition(data)?.id)
    val last30 = (0 until 30).map { today.minusDays(it) }
    val attempts30 = data.attempts.filter { Queries.dateOf(it.answeredAt) in last30 }
    val minutes30 = last30.sumOf { activity[it]?.minutes ?: 0 }
    val progress = progressSummary()

    PageHead("Desempenho", "Últimos 30 dias")
    Div({ classes("grid", "cols-4") }) {
        Card(extra = "flat") { Stat(Queries.minutesLabel(minutes30), "de estudo") }
        Card(extra = "flat") { Stat("${attempts30.size}", "questões respondidas") }
        Card(extra = "flat") { Stat(if (attempts30.isEmpty()) "–" else "${percent(attempts30.count { it.correct }, attempts30.size)}%", "de acerto") }
        Card(extra = "flat") { Stat("${streak.current}", "dias seguidos (recorde ${streak.best})") }
    }

    Div({ classes("grid", "main-side") }) {
        Card {
            CardHead("Minutos por dia")
            val max = last30.maxOf { activity[it]?.minutes ?: 0 }.coerceAtLeast(30)
            Div({ attr("style", "display:flex;align-items:flex-end;gap:4px;height:160px;padding-top:8px") }) {
                last30.reversed().forEach { date ->
                    val value = activity[date]?.minutes ?: 0
                    val height = (value * 100 / max).coerceAtLeast(if (value > 0) 4 else 2)
                    Div({
                        attr("title", "${Queries.shortDate(date)}: ${Queries.minutesLabel(value)}")
                        attr("style", "flex:1;height:$height%;border-radius:6px 6px 2px 2px;background:${if (date == today) "var(--primary)" else if (value > 0) "color-mix(in srgb, var(--primary) 55%, transparent)" else "var(--soft-2)"}")
                    })
                }
            }
            Div({ classes("row", "between", "small", "muted"); attr("style", "margin-top:6px") }) {
                Span { Text(Queries.shortDate(last30.last())) }
                Span { Text("hoje") }
            }
        }
        Card {
            CardHead("Nível")
            Div({ classes("stack", "tight") }) {
                Span({ classes("strong"); attr("style", "font-size:22px") }) { Text("Nível ${progress.level} · ${progress.levelTitle}") }
                ProgressBar(progress.levelProgress.toDouble())
                P({ classes("small", "muted") }) { Text("${progress.xpIntoLevel} de ${progress.xpForNextLevel} XP para o próximo nível · ${progress.totalXp} XP no total") }
            }
        }
    }

    Card {
        CardHead("Acerto por matéria")
        val bySubject = data.attempts.groupBy { topics[questions[it.questionId]?.topicId]?.subjectId }
        if (data.attempts.isEmpty()) P({ classes("muted") }) { Text("Resolva questões para ver seu acerto por matéria.") }
        Div({ classes("stack") }) {
            subjects.forEach { subject ->
                val list = bySubject[subject.id].orEmpty()
                if (list.isEmpty()) return@forEach
                val rate = percent(list.count { it.correct }, list.size)
                Div({ classes("stack", "tight") }) {
                    Div({ classes("row", "between") }) {
                        Span({ classes("strong") }) { Text(subject.name) }
                        Span({ classes("small", "muted") }) { Text("$rate% · ${list.size} questões") }
                    }
                    ProgressBar(rate / 100.0, if (rate >= 70) "green" else null)
                }
            }
        }
    }

    Card {
        CardHead("Constância (26 semanas)")
        Div({ attr("style", "display:grid;grid-auto-flow:column;grid-template-rows:repeat(7,14px);gap:3px;overflow-x:auto;padding-bottom:4px") }) {
            streak.calendar.forEach { day ->
                val level = br.com.estudario.domain.StreakEngine.level(day, streak.goal)
                val color = when (level) { 0 -> "var(--soft-2)"; 1 -> "color-mix(in srgb, var(--green) 35%, transparent)"; 2 -> "color-mix(in srgb, var(--green) 65%, transparent)"; else -> "var(--green)" }
                Div({ attr("title", "${Queries.shortDate(day.date)}: ${day.questions} questões, ${day.minutes} min"); attr("style", "width:14px;height:14px;border-radius:3px;background:$color") })
            }
        }
    }
}

/** XP e nível com o mesmo ProgressEngine do app. */
private fun progressSummary(): ProgressEngine.ProgressSummary {
    val data = Store.data
    val today = Queries.todayDate()
    val days = Queries.dailyActivity(data)
    val streak = Queries.streak(data)
    val typeById = data.tasks.associate { it.id to it.type }
    val planWork = data.executions.filter { it.taskId != null }.groupBy { it.taskId!! }.mapNotNull { (taskId, runs) ->
        val type = typeById[taskId]?.let { runCatching { PlanTaskType.valueOf(it) }.getOrNull() } ?: return@mapNotNull null
        ProgressEngine.PlanWork(Queries.dateOf(runs.maxOf { it.completedAt }), type, runs.sumOf { it.minutes }, runs.sumOf { it.correct }, runs.sumOf { it.questions })
    }
    val leaves = Queries.leafTopics(data)
    return ProgressEngine.evaluate(
        ProgressEngine.ProgressInput(
            today = today,
            days = days,
            goal = streak.goal,
            planWork = planWork,
            bestStreak = streak.best,
            currentStreak = streak.current,
            totalQuestions = data.attempts.size,
            correctQuestions = data.attempts.count { it.correct },
            reviewsCompleted = data.reviewHistory.size,
            topicsStudied = leaves.count(Queries::isStudied),
            topicsTotal = leaves.size,
        ),
    )
}

// ------------------------------------------------------------------ perfil

@Composable
fun ProfileScreen() {
    val session = Store.session
    val data = Store.data
    PageHead("Perfil e ajustes")
    Div({ classes("grid", "cols-2") }) {
        Card {
            Div({ classes("row") }) {
                Span({ classes("avatar"); attr("style", "width:56px;height:56px;font-size:22px") }) {
                    val photo = session?.avatarUrl
                    if (photo != null) Img(src = photo, alt = "") else Text((session?.name ?: session?.email ?: "?").take(1).uppercase())
                }
                Div {
                    H2 { Text(session?.name ?: "Sua conta") }
                    P({ classes("muted") }) { Text(session?.email ?: "") }
                }
            }
            Div({ classes("row", "wrap"); attr("style", "margin-top:16px") }) {
                Btn("Sair", { Store.signOut() }, style = "outline", icon = "logout")
            }
        }
        Card {
            H3 { Text("Aparência") }
            Div({ classes("segmented"); attr("style", "margin-top:12px") }) {
                listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) ->
                    Button({ classes(*listOfNotNull(if (Theme.mode == key) "on" else null).toTypedArray()); onClick { Theme.set(key) } }) { Text(label) }
                }
            }
        }
        Card {
            H3 { Text("Sincronização") }
            P({ classes("muted"); attr("style", "margin-top:8px") }) {
                Text("O que você faz aqui vai para a sua conta em segundos e aparece no celular na próxima sincronização (ao abrir o app). Se o mesmo dado mudar nos dois lugares, o app pergunta qual versão manter.")
            }
            P({ classes("small", "muted"); attr("style", "margin-top:8px") }) {
                Text("${data.competitions.size} concurso(s) · ${data.topics.size} tópicos · ${data.questions.size} questões · ${data.tasks.size} tarefas no plano")
            }
            Div({ classes("row"); attr("style", "margin-top:12px") }) { Btn("Recarregar da conta", { Store.start() }, style = "outline", icon = "refresh") }
        }
        Card {
            H3 { Text("App no celular") }
            P({ classes("muted"); attr("style", "margin-top:8px") }) {
                Text("Gerar material com IA, montar o plano, simulados, modo foco e notificações ficam no app. ")
                A(href = "https://estudario.com.br", { attr("target", "_blank") }) { Text("Conheça o Estudário") }
            }
        }
    }
    P({ classes("small", "muted") }) { Text("Estudário web · seus dados ficam na sua conta e só você tem acesso.") }
}

