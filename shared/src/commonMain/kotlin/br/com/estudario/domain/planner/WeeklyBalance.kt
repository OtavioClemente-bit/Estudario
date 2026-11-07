package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * O balanço da semana (os últimos 7 dias, hoje incluído): quanto a pessoa estudou, o que viu de novo,
 * como foi nas questões comparado à semana anterior e quanto do edital já está visto. É o que mostra que
 * ela está avançando e vai terminar, no plano do app e do site.
 */
data class WeeklyBalance(
    val from: LocalDate,
    val to: LocalDate,
    val daysStudied: Int,
    val minutesStudied: Int,
    /** Tarefas do plano marcadas na semana (sem as remarcadas/canceladas) e as feitas. */
    val tasksPlanned: Int,
    val tasksDone: Int,
    val newTopics: Int,
    val answered: Int,
    val correct: Int,
    val previousAnswered: Int,
    val previousCorrect: Int,
    val seenTopics: Int,
    val totalTopics: Int,
) {
    val accuracy: Int? get() = if (answered == 0) null else correct * 100 / answered
    val previousAccuracy: Int? get() = if (previousAnswered < 10) null else previousCorrect * 100 / previousAnswered
    /** Pontos de acerto em relação à semana anterior (null = sem base para comparar). */
    val accuracyChange: Int? get() = accuracy?.takeIf { answered >= 10 }?.let { a -> previousAccuracy?.let { a - it } }
    val seenPercent: Int get() = if (totalTopics == 0) 0 else seenTopics * 100 / totalTopics
    val idle: Boolean get() = minutesStudied == 0 && answered == 0

    /** A frase principal do balanço. */
    fun headline(): String = when {
        idle -> "Semana parada. Uma tarefa hoje já põe o plano de volta no trilho."
        daysStudied >= 5 -> "Semana forte: ${hours(minutesStudied)} em $daysStudied dias."
        else -> "Você estudou ${hours(minutesStudied)} em $daysStudied ${if (daysStudied == 1) "dia" else "dias"}."
    }

    /** As linhas de apoio: tópicos novos, questões e acerto (com a tendência). */
    fun details(): List<String> = buildList {
        if (newTopics > 0) add("$newTopics ${if (newTopics == 1) "tópico novo visto" else "tópicos novos vistos"}; edital $seenPercent% visto ($seenTopics de $totalTopics).")
        else add("Edital $seenPercent% visto ($seenTopics de $totalTopics tópicos).")
        accuracy?.let { a ->
            val trend = accuracyChange?.let { c -> when { c >= 3 -> " (+$c pontos)"; c <= -3 -> " ($c pontos)"; else -> " (estável)" } } ?: ""
            add("$a% de acerto em $answered ${if (answered == 1) "questão" else "questões"}$trend.")
        }
        if (tasksPlanned > 0) add("$tasksDone de $tasksPlanned tarefas do plano feitas.")
    }

    companion object {
        private val inactive = setOf(PlanTaskStatus.REPROGRAMADA)

        fun hours(minutes: Int): String = when {
            minutes < 60 -> "$minutes min"
            minutes % 60 == 0 -> "${minutes / 60}h"
            else -> "${minutes / 60}h${(minutes % 60).toString().padStart(2, '0')}"
        }

        fun from(input: PlannerSnapshotBuilder.Input, today: LocalDate = input.today, zone: TimeZone = input.zone): WeeklyBalance {
            val from = today.minusDays(6)
            fun inWeek(d: LocalDate) = !d.isBefore(from) && !d.isAfter(today)
            val previousFrom = today.minusDays(13); val previousTo = today.minusDays(7)
            val executions = input.executions.filter { inWeek(it.date) }
            val tasks = input.tasks.filter { inWeek(it.date) && it.status !in inactive }
            // Tópico novo: a primeira teoria do tópico concluída nesta semana.
            val firstTheory = input.tasks.filter { it.type == PlanTaskType.THEORY && it.status == PlanTaskStatus.CONCLUIDA && it.topicId != null }
                .groupBy { it.topicId!! }.mapValues { (_, l) -> l.minOf { it.date } }
            val leaves = input.topics.filter { t -> input.topics.none { it.parentTopicId == t.id } }
            val attempts = input.attempts.filter { it.answeredAt != null }
            fun dayOf(ms: Long) = epochMillisToDate(ms, zone)
            // Retomada não é tópico novo: tópico com questão respondida antes da semana já tinha sido estudado.
            val topicOfQuestion = HashMap<Long, Long>().apply { input.questionsByTopic.forEach { (t, ids) -> ids.forEach { put(it, t) } } }
            val seenBefore = attempts.filter { dayOf(it.answeredAt!!).isBefore(from) }.mapNotNullTo(hashSetOf()) { it.topicId ?: topicOfQuestion[it.questionId] }
            val week = attempts.filter { inWeek(dayOf(it.answeredAt!!)) }
            val previous = attempts.filter { dayOf(it.answeredAt!!).let { d -> !d.isBefore(previousFrom) && !d.isAfter(previousTo) } }
            val studiedDays = (executions.map { it.date } + week.map { dayOf(it.answeredAt!!) }).toSet()
            return WeeklyBalance(
                from = from, to = today,
                daysStudied = studiedDays.size,
                minutesStudied = executions.sumOf { it.minutes },
                tasksPlanned = tasks.size,
                tasksDone = tasks.count { it.status == PlanTaskStatus.CONCLUIDA },
                newTopics = firstTheory.count { (topic, day) -> inWeek(day) && topic !in seenBefore },
                answered = week.size, correct = week.count { it.correct },
                previousAnswered = previous.size, previousCorrect = previous.count { it.correct },
                seenTopics = leaves.count { it.studied }, totalTopics = leaves.size,
            )
        }
    }
}
