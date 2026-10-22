package br.com.estudario.web.ui

import br.com.estudario.domain.FolhaBond
import br.com.estudario.domain.FolhaMemory
import br.com.estudario.web.data.ProgressView
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store

/**
 * O Folha como amigo, no site: o nome da pessoa e a memória dele na tela inicial (FolhaMemory). A
 * amizade (FolhaBond) não aparece em lugar nenhum: só libera gestos novos com o tempo.
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
                name = firstName(), bond = FolhaBond.level(view.streak.activeDays).index,
                daysAway = FolhaBond.daysAway(view.streak.calendar, Queries.todayDate()), todayDone = view.streak.todayDone,
                streak = view.streak.current, daysToExam = daysToExam?.toInt(), lastSubject = last?.first, lastSubjectDaysAgo = last?.second,
                hour = hour, seed = seed,
            ),
        )
    }
}

