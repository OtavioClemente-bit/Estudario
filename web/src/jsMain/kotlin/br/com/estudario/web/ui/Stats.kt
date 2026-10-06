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
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// ------------------------------------------------------------------ conquistas

@Composable
fun AchievementsScreen() {
    val progress = Progress.of(Store.data).progress
    BackToProfile()
    PageHead("Conquistas", "${progress.earnedBadges.size} de ${progress.badges.size} emblemas. Tudo vem do seu histórico real.")
    Div({ classes("badges") }) {
        (progress.earnedBadges + progress.nextBadges).forEach { item ->
            androidx.compose.runtime.key(item.badge.id) {
                Div({ classes(*listOfNotNull("badge", if (item.earned) "earned" else null).toTypedArray()); attr("title", item.badge.requirement) }) {
                    Div({ classes("medal") }) { Icon(if (item.earned) "military_tech" else "lock", filled = item.earned) }
                    Span({ classes("name") }) { Text(item.badge.name) }
                    Span({ classes("xs", "muted") }) { Text(item.badge.requirement) }
                    if (!item.earned) Div({ attr("style", "width:100%") }) {
                        ProgressBar(item.percent.toDouble())
                        Span({ classes("xs", "muted") }) { Text("${item.current} / ${item.target} ${item.badge.unit}") }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ desempenho

@Composable
fun StatsScreen() {
    val data = Store.data
    val today = Queries.todayDate()
    val activity = Queries.dailyActivity(data).associateBy { it.date }
    val view = Progress.of(data)
    val streak = view.streak
    val topics = data.topics.associateBy { it.id }
    val questions = data.questions.associateBy { it.id }
    val subjects = Queries.subjectsOf(data, Queries.primaryCompetition(data)?.id)
    var days by remember { mutableStateOf(30) }
    val window = (0 until days).map { today.minusDays(it) }
    val windowSet = window.toSet()
    val attempts = data.attempts.filter { Queries.dateOf(it.answeredAt) in windowSet }
    val minutes = window.sumOf { activity[it]?.minutes ?: 0 }

    PageHead("Desempenho", "Como está indo o seu estudo") {
        Div({ classes("segmented") }) {
            listOf(7 to "7 dias", 30 to "30 dias", 90 to "90 dias").forEach { (n, label) ->
                Button({ classes(*listOfNotNull(if (days == n) "on" else null).toTypedArray()); onClick { days = n } }) { Text(label) }
            }
        }
    }
    Div({ classes("grid", "cols-4") }) {
        Card(extra = "flat") { Stat(Queries.minutesLabel(minutes), "de estudo") }
        Card(extra = "flat") { Stat("${attempts.size}", "questões respondidas") }
        Card(extra = "flat") { Stat(if (attempts.isEmpty()) "–" else "${percent(attempts.count { it.correct }, attempts.size)}%", "de acerto") }
        Card(extra = "flat") { Stat("${streak.current}", "dias seguidos · recorde ${streak.best}") }
    }
    Card {
        CardHead("Minutos por dia")
        val shown = window.take(minOf(days, 30)).reversed()
        val max = shown.maxOf { activity[it]?.minutes ?: 0 }.coerceAtLeast(30)
        Div({ classes("bars") }) {
            shown.forEach { date ->
                val value = activity[date]?.minutes ?: 0
                I({
                    classes(*listOfNotNull(if (date == today) "today" else null, if (value == 0) "zero" else null).toTypedArray())
                    attr("title", "${Queries.shortDate(date)}: ${Queries.minutesLabel(value)}")
                    attr("style", "height:${(value * 100 / max).coerceAtLeast(if (value > 0) 4 else 2)}%")
                })
            }
        }
        Div({ classes("row", "between", "xs", "muted"); attr("style", "margin-top:6px") }) {
            Span { Text(Queries.shortDate(shown.first())) }
            Span { Text("hoje") }
        }
    }
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Acerto por matéria")
            val bySubject = attempts.groupBy { topics[questions[it.questionId]?.topicId]?.subjectId }
            if (attempts.isEmpty()) P({ classes("muted", "small") }) { Text("Resolva questões para ver seu acerto por matéria.") }
            Div({ classes("stack") }) {
                subjects.forEach { subject ->
                    val list = bySubject[subject.id].orEmpty()
                    if (list.isEmpty()) return@forEach
                    val rate = percent(list.count { it.correct }, list.size)
                    androidx.compose.runtime.key(subject.id) {
                        Div({ classes("stack", "tight") }) {
                            Div({ classes("row", "between") }) {
                                B({ classes("small", "clamp-2") }) { Text(subject.name) }
                                Span({ classes("small", "muted", "nowrap") }) { Text("$rate% · ${list.size}") }
                            }
                            ProgressBar(rate / 100.0, if (rate >= 70) "green" else null)
                        }
                    }
                }
            }
        }
        Card {
            CardHead("Constância")
            Div({ classes("heat") }) {
                streak.calendar.forEach { day ->
                    val level = StreakEngine.level(day, streak.goal)
                    I({ classes(*listOfNotNull(if (level > 0) "l${level.coerceAtMost(3)}" else null).toTypedArray()); attr("title", "${Queries.shortDate(day.date)}: ${day.questions} questões, ${day.minutes} min") })
                }
            }
            P({ classes("small", "muted"); attr("style", "margin-top:10px") }) { Text("${streak.activeDays} dias com estudo · cada quadrado é um dia, mais verde = mais perto da meta") }
        }
    }
}

// ------------------------------------------------------------------ ajustes

@Composable
fun SettingsScreen() {
    var goal by remember { mutableStateOf(Prefs.dailyGoal) }
    var explain by remember { mutableStateOf(Prefs.explanationRightAway) }
    BackToProfile()
    PageHead("Ajustes", "Deixe o Estudário do seu jeito")
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Aparência")
            Div({ classes("segmented") }) {
                listOf("SYSTEM" to "Sistema", "LIGHT" to "Claro", "DARK" to "Escuro").forEach { (key, label) ->
                    Button({ classes(*listOfNotNull(if (Theme.mode == key) "on" else null).toTypedArray()); onClick { Theme.set(key) } }) { Text(label) }
                }
            }
        }
        Card {
            CardHead("Meta diária")
            Div({ classes("day-row") }) {
                Div({ classes("grow") }) {
                    Div({ classes("name") }) { Text("Questões por dia") }
                    Div({ classes("hint") }) { Text("Ou uma tarefa do plano, ou uma revisão, fecham o dia") }
                }
                Stepper(goal, { goal = it; Prefs.dailyGoal = it }, step = 5, min = 5, max = 100, label = "Meta diária de questões")
            }
        }
        Card {
            CardHead("Questões")
            Div({ classes("setting") }) {
                Icon("visibility")
                Div({ classes("grow") }) { B { Text("Explicação logo após responder") }; Div({ classes("small", "muted") }) { Text("Desligue para ver a correção só quando quiser.") } }
                Switch(explain, "Explicação logo após responder") { explain = it; Prefs.explanationRightAway = it }
            }
        }
        Card {
            CardHead("Sincronização")
            P({ classes("small", "muted") }) { Text("O que você faz aqui vai para a sua conta em segundos e aparece no app do celular na próxima abertura. Se o mesmo dado mudar nos dois lugares, o Estudário pergunta qual versão manter.") }
            Div({ classes("row", "wrap"); attr("style", "margin-top:12px") }) {
                Btn("Recarregar da conta", { Store.start() }, style = "outline", icon = "refresh", small = true)
                A(href = "https://estudario.com.br/privacidade.html", { attr("target", "_blank"); classes("btn", "ghost", "small") }) { Text("Privacidade") }
            }
        }
    }
    P({ classes("xs", "faint"); attr("style", "text-align:center") }) { Text("Estudário web · seus dados ficam na sua conta e só você tem acesso.") }
}

