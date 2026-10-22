package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.StudyInsights
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.ProgressView
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** As questões respondidas, com a matéria de cada uma (para o ponto fraco e o resumo da semana). */
fun studyAnswers(data: Snapshot): List<StudyInsights.Answer> {
    val topicSubject = data.topics.associate { it.id to it.subjectId }
    val subjectName = data.subjects.associate { it.id to it.name }
    val questionTopic = data.questions.associate { it.id to it.topicId }
    return data.attempts.map { a ->
        val sid = questionTopic[a.questionId]?.let { topicSubject[it] }
        StudyInsights.Answer(a.answeredAt.toLong(), a.correct, sid, sid?.let { subjectName[it] })
    }
}

/**
 * A missão do Folha: a matéria que mais derrubou a pessoa nos últimos 7 dias, com o botão que já abre
 * 10 questões só dela. Não aparece quando nada preocupa. O Folha já está no card do "Agora", logo acima:
 * aqui no lugar dele fica o anel do acerto na matéria, com a marca da meta de 70%.
 */
@Composable
fun WeakSpotCard(data: Snapshot) {
    val answers = remember(data.attempts.size) { studyAnswers(data) }
    val done = remember(data.attempts.size, Mission.version) { Mission.result(answers) }
    if (done != null) MissionDoneCard(done, answers)
    val spot = remember(data.attempts.size) { val now = kotlin.js.Date.now().toLong(); StudyInsights.weakSpot(answers, now) ?: StudyInsights.weakSpot(answers, now, 14) } ?: return
    // a missão acabou de ser cumprida nesta matéria: o card de cima já fala dela
    if (done != null && done.mission.subjectId == spot.subjectId) return
    Div({ classes("card", "pad-lg", "insight-card", "weak", "rise") }) {
        Div({ classes("insight-top") }) {
            Div({ classes("insight-ring"); attr("style", "--p:${spot.accuracy}"); attr("title", "Acerto de ${spot.accuracy}% (meta: 70%)") }) {
                B { Text("${spot.accuracy}%") }
                Span { Text("acerto") }
            }
            Div({ classes("grow") }) {
                Span({ classes("eyebrow") }) { Text("MISSÃO DO FOLHA") }
                B({ classes("insight-title") }) { Text("Reforçar ${spot.subject}") }
                P({ classes("small", "muted"); attr("style", "margin:2px 0 0") }) {
                    Text("Você errou ${spot.wrong} de ${spot.total} questões dessa matéria nas últimas semanas (${spot.accuracy}% de acerto). Treinar o ponto fraco é o que mais sobe a nota.")
                }
            }
        }
        Btn("Treinar 10 questões de ${spot.subject}", { Mission.start(spot.subjectId, spot.subject, answers, spot.accuracy) }, block = true, icon = "gps_fixed")
    }
}

/**
 * A missão do ponto fraco, guardada ao tocar em "Treinar": a matéria, o acerto daquele momento e a hora.
 * Quando a pessoa responde as questões dela, o Início mostra o antes e o depois e o Folha comemora.
 */
object Mission {
    private const val KEY = "estudario.missao"
    var version by mutableStateOf(0)
    fun stored(): StudyInsights.Mission? = StudyInsights.Mission.decode(runCatching { localStorage.getItem(KEY) }.getOrNull())
    fun result(answers: List<StudyInsights.Answer>): StudyInsights.MissionResult? = StudyInsights.missionResult(stored(), answers, kotlin.js.Date.now().toLong())
    fun start(subjectId: Long, subject: String, answers: List<StudyInsights.Answer>, fallback: Int) {
        val now = kotlin.js.Date.now().toLong()
        val before = StudyInsights.subjectAccuracy(answers, subjectId, now) ?: fallback
        runCatching { localStorage.setItem(KEY, StudyInsights.Mission(subjectId, subject, before, now).encode()) }
        version++
        Router.go(Route.Quiz("materia-${subjectId}-10"))
    }
    fun clear() { runCatching { localStorage.removeItem(KEY) }; version++ }
    /** A fala do Folha sobre a missão cumprida (o cartão do "Agora" usa no lugar da frase do dia). */
    fun line(r: StudyInsights.MissionResult): String = br.com.estudario.domain.FolhaFalas.mission(r.mission.subject, r.mission.before, r.after, r.mission.startedAt / 1000)
}

/**
 * Missão cumprida: o anel sai do acerto de antes e corre até o de agora, com a diferença em pontos, a
 * fala do Folha e, se a matéria ainda pede reforço, o botão para mais uma rodada.
 */
@Composable
private fun MissionDoneCard(r: StudyInsights.MissionResult, answers: List<StudyInsights.Answer>) {
    var shown by remember(r.mission.startedAt) { mutableStateOf(r.mission.before) }
    androidx.compose.runtime.LaunchedEffect(r.mission.startedAt, r.after) { kotlinx.coroutines.delay(450); shown = r.after }
    val up = r.delta > 0
    Div({ classes("card", "pad-lg", "insight-card", "mission-done", "rise") }) {
        Div({ classes("insight-top") }) {
            Div({ classes("insight-ring", "anim"); attr("style", "--p:$shown") }) {
                B { Text("${r.after}%") }
                Span { Text("acerto") }
            }
            Div({ classes("grow") }) {
                Span({ classes("eyebrow") }) { Text("MISSÃO CUMPRIDA") }
                B({ classes("insight-title") }) { Text(r.mission.subject) }
                Div({ classes("mission-delta", if (up) "up" else if (r.delta < 0) "down" else "same") }) {
                    Text("${r.mission.before}% → ${r.after}%")
                    if (r.delta != 0) Span { Text(if (up) "+${r.delta} pts" else "${r.delta} pts") }
                }
                P({ classes("small", "muted"); attr("style", "margin:4px 0 0") }) { Text("${r.answered} questões respondidas nessa matéria desde a missão.") }
            }
        }
        Div({ classes("mission-actions") }) {
            if (r.after < 70) Btn("Mais 10 questões", { Mission.start(r.mission.subjectId, r.mission.subject, answers, r.after) }, icon = "gps_fixed")
            Btn("Fechar", { Mission.clear() }, style = "ghost")
        }
    }
}

private const val WEEK_SEEN = "estudario.semana.vista"

/**
 * O resumo da semana: aparece no domingo e na segunda (até a pessoa fechar), com os dias, o tempo, as
 * questões e o acerto comparados com a semana anterior, a matéria mais forte e a que pede atenção, e
 * um comentário do Folha.
 */
@Composable
fun WeekReportCard(data: Snapshot, view: ProgressView) {
    val today = Queries.todayDate()
    if (today.dayOfWeek.ordinal !in setOf(6, 0)) return   // domingo ou segunda
    val week = remember(data.attempts.size, view.streak.calendar.size) {
        StudyInsights.week(studyAnswers(data), view.streak.calendar, today, Queries.startOfToday().toLong())
    }
    val key = week.from.toString()
    var seen by remember { mutableStateOf(runCatching { localStorage.getItem(WEEK_SEEN) }.getOrNull() == key) }
    if (seen || (week.days == 0 && week.prevDays == 0)) return
    Div({ classes("card", "pad-lg", "insight-card", "week", "rise") }) {
        Div({ classes("insight-top") }) {
            Div({ classes("insight-icon") }) { Icon("date_range", filled = true) }
            Div({ classes("grow") }) {
                Span({ classes("eyebrow") }) { Text("SUA SEMANA") }
                B({ classes("insight-title") }) { Text(StudyInsights.weekLine(week)) }
            }
            Button({ classes("icon-btn", "insight-close"); attr("aria-label", "Fechar o resumo"); onClick { runCatching { localStorage.setItem(WEEK_SEEN, key) }; seen = true } }) { Icon("close") }
        }
        Div({ classes("week-stats") }) {
            WeekStat("${week.days}", if (week.days == 1) "dia de estudo" else "dias de estudo", delta(week.days - week.prevDays))
            WeekStat(hm(week.minutes), "de estudo", (week.minutes - week.prevMinutes).let { d -> if (d == 0) null else (if (d > 0) "+" else "−") + hm(kotlin.math.abs(d)) })
            WeekStat("${week.questions}", "questões", delta(week.questions - week.prevQuestions))
            WeekStat(week.accuracy?.let { "$it%" } ?: "–", "de acerto", week.accuracyDelta?.let { delta(it, " pts") })
        }
        if (week.best != null || week.worst != null) Div({ classes("week-subjects") }) {
            week.best?.let { (name, acc) -> Span({ classes("week-chip", "good") }) { Icon("trending_up"); Text("Mais forte: $name ($acc%)") } }
            week.worst?.let { (name, acc) -> Span({ classes("week-chip", "bad") }) { Icon("trending_down"); Text("Pede atenção: $name ($acc%)") } }
        }
    }
}

/** Tempo curto para as caixinhas: "45 min", "2h", "13h43". */
private fun hm(min: Int): String = when { min < 60 -> "$min min"; min % 60 == 0 -> "${min / 60}h"; else -> "${min / 60}h${(min % 60).toString().padStart(2, '0')}" }

/** Diferença para a semana anterior, com sinal ("+3", "−20 min"); nulo quando é zero. */
private fun delta(d: Int, unit: String = ""): String? = when {
    d > 0 -> "+$d$unit"
    d < 0 -> "−${-d}$unit"
    else -> null
}

@Composable
private fun WeekStat(value: String, label: String, delta: String?) {
    Div({ classes("week-stat") }) {
        B { Text(value) }
        Span({ classes("xs", "muted") }) { Text(label) }
        if (delta != null) Span({ classes("week-delta", if (delta.startsWith("+")) "up" else "down") }) { Text(delta) }
    }
}
