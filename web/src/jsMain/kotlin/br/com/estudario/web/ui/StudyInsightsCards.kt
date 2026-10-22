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
 * 10 questões só dela. Não aparece quando nada preocupa.
 */
@Composable
fun WeakSpotCard(data: Snapshot) {
    val spot = remember(data.attempts.size) { studyAnswers(data).let { a -> val now = kotlin.js.Date.now().toLong(); StudyInsights.weakSpot(a, now) ?: StudyInsights.weakSpot(a, now, 14) } } ?: return
    Div({ classes("card", "pad-lg", "insight-card", "weak", "rise") }) {
        Div({ classes("insight-top") }) {
            Div({ classes("insight-folha") }) { Folha("point", 64, live = false) }
            Div({ classes("grow") }) {
                Span({ classes("eyebrow") }) { Text("MISSÃO DO FOLHA") }
                B({ classes("insight-title") }) { Text("Reforçar ${spot.subject}") }
                P({ classes("small", "muted"); attr("style", "margin:2px 0 0") }) {
                    Text("Você errou ${spot.wrong} de ${spot.total} questões dessa matéria nas últimas semanas (${spot.accuracy}% de acerto). Treinar o ponto fraco é o que mais sobe a nota.")
                }
            }
        }
        Div({ classes("insight-meter") }) {
            Span({ classes("insight-meter-fill"); attr("style", "width:${spot.accuracy}%") })
            Span({ classes("insight-meter-goal"); attr("title", "Meta: 70%") })
        }
        Btn("Treinar 10 questões de ${spot.subject}", { Router.go(Route.Quiz("materia-${spot.subjectId}-10")) }, block = true, icon = "gps_fixed")
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
            Div({ classes("insight-folha") }) { Folha(if (week.days >= 4) "proud" else "wave", 64, live = false) }
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
