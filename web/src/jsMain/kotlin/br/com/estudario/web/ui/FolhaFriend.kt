package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import br.com.estudario.domain.FolhaBond
import br.com.estudario.domain.FolhaMemory
import br.com.estudario.web.data.ProgressView
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * O Folha como amigo, no site: o nível da amizade (FolhaBond), a memória dele na tela inicial
 * (FolhaMemory) e o cartão do vínculo no perfil.
 */
object FolhaFriend {
    /** Primeiro nome da pessoa (ou nulo na demonstração e sem nome). */
    fun firstName(): String? = Store.session?.name?.substringBefore(' ')?.takeIf { it.isNotBlank() }

    /** A matéria estudada por último (questão respondida ou tarefa do plano) e há quantos dias. */
    fun lastSubject(data: Snapshot): Pair<String, Int>? {
        val lastAttempt = data.attempts.maxByOrNull { it.answeredAt }
        val lastExec = data.executions.maxByOrNull { it.completedAt }
        val (at, subjectId) = when {
            lastAttempt == null && lastExec == null -> return null
            lastExec == null || (lastAttempt != null && lastAttempt.answeredAt >= lastExec.completedAt) -> {
                val q = data.questions.firstOrNull { it.id == lastAttempt!!.questionId } ?: return null
                lastAttempt!!.answeredAt to data.topics.firstOrNull { it.id == q.topicId }?.subjectId
            }
            else -> lastExec.completedAt to lastExec.subjectId
        }
        val name = data.subjects.firstOrNull { it.id == subjectId }?.name ?: return null
        val start = Queries.startOfToday()
        val daysAgo = if (at >= start) 0 else ((start - at) / 86_400_000.0).let { kotlin.math.ceil(it).toInt() }
        return name to daysAgo
    }

    /** O que o Folha tem de pessoal para dizer na tela inicial agora (nulo: a frase de sempre). */
    fun homeLine(data: Snapshot, view: ProgressView, daysToExam: Long?, hour: Int, seed: Long): FolhaMemory.Line? {
        val last = lastSubject(data)
        return FolhaMemory.home(
            FolhaMemory.Context(
                name = firstName(), bond = FolhaBond.level(view.streak.activeDays).index, bondUp = null,
                daysAway = FolhaBond.daysAway(view.streak.calendar, Queries.todayDate()), todayDone = view.streak.todayDone,
                streak = view.streak.current, daysToExam = daysToExam?.toInt(), lastSubject = last?.first, lastSubjectDaysAgo = last?.second,
                hour = hour, seed = seed,
            ),
        )
    }
}

/** Cartão do Perfil: o vínculo com o Folha, os corações da amizade e o que o próximo nível libera. */
@Composable
fun BondCard(view: ProgressView) {
    val days = view.streak.activeDays
    val level = FolhaBond.level(days)
    val next = FolhaBond.next(days)
    Div({ classes("card", "pad-lg", "bond-card") }) {
        Div({ classes("bond-top") }) {
            Div({ classes("bond-folha") }) { Folha(if (level.index >= 3) "happy" else "wave", 72) }
            Div({ classes("grow") }) {
                Div({ classes("eyebrow") }) { Text("AMIZADE COM O FOLHA") }
                B({ classes("bond-name") }) { Text(level.name) }
                Div({ classes("bond-hearts"); attr("aria-label", "Nível ${level.index + 1} de ${FolhaBond.levels.size}") }) {
                    FolhaBond.levels.forEach { l -> Span({ classes(*listOfNotNull("bond-heart", if (l.index <= level.index) "on" else null).toTypedArray()) }) }
                }
            }
        }
        if (next != null) {
            Div({ classes("bond-bar") }) { Span({ attr("style", "width:${(FolhaBond.progress(days) * 100).toInt()}%") }) }
            P({ classes("small", "muted"); attr("style", "margin:6px 0 0") }) {
                Text("Mais ${next.days - days} ${if (next.days - days == 1) "dia" else "dias"} estudando juntos para virar "); B { Text(next.name) }; Text(". ${next.unlock}")
            }
        } else P({ classes("small", "muted"); attr("style", "margin:6px 0 0") }) { Text("Vocês chegaram ao topo da amizade. ${level.unlock}") }
        P({ classes("xs", "faint"); attr("style", "margin:6px 0 0") }) { Text("Conta cada dia com estudo, mesmo que não seja seguido. Ninguém perde amizade por faltar.") }
    }
}

/** Marca o nível de amizade já comemorado neste navegador. */
internal object BondSeen {
    private const val KEY = "estudario.folha.amizade"
    fun read(): Int? = runCatching { localStorage.getItem(KEY) }.getOrNull()?.toIntOrNull()
    fun write(v: Int) { runCatching { localStorage.setItem(KEY, v.toString()) } }
}
