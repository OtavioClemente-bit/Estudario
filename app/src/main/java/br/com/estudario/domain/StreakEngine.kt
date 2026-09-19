package br.com.estudario.domain

import java.time.LocalDate

/** Tudo o que a pessoa fez em um dia, somando questões, revisões, tarefas do plano e sessões. */
data class DailyActivity(
    val date: LocalDate,
    val questions: Int = 0,
    val correct: Int = 0,
    val reviews: Int = 0,
    val planTasks: Int = 0,
    val studySessions: Int = 0,
    val minutes: Int = 0,
) {
    val hasAnything: Boolean get() = questions + reviews + planTasks + studySessions > 0
}

/**
 * Meta do dia: o dia entra na sequência quando a pessoa responde [questions] questões OU conclui
 * pelo menos uma tarefa do plano OU fecha pelo menos uma revisão. Assim um dia de teoria pelo plano
 * vale tanto quanto um dia de questões.
 */
data class DailyGoal(val questions: Int = 20) {
    companion object {
        const val MIN = 5
        const val MAX = 100
        val DEFAULT = DailyGoal()
    }
}

data class StreakDay(val date: LocalDate, val done: Boolean, val partial: Boolean, val future: Boolean)

data class StreakSummary(
    val current: Int = 0,
    val best: Int = 0,
    val todayDone: Boolean = false,
    val todayQuestions: Int = 0,
    val todayPlanTasks: Int = 0,
    val todayReviews: Int = 0,
    val goal: DailyGoal = DailyGoal.DEFAULT,
    val week: List<StreakDay> = emptyList(),
    val calendar: List<DailyActivity> = emptyList(),
    val activeDays: Int = 0,
    val totalQuestions: Int = 0,
    val totalMinutes: Int = 0,
) {
    /** 0f..1f — quanto falta para fechar o dia. Tarefa do plano ou revisão já fecham sozinhas. */
    val todayProgress: Float
        get() = when {
            todayDone -> 1f
            goal.questions <= 0 -> 0f
            else -> (todayQuestions.toFloat() / goal.questions).coerceIn(0f, 1f)
        }
}

object StreakEngine {
    /** O dia conta para a sequência? */
    fun isDone(day: DailyActivity, goal: DailyGoal): Boolean =
        day.questions >= goal.questions || day.planTasks > 0 || day.reviews > 0

    /** 0 = nada, 1..4 = intensidade crescente, para o mapa de frequência. */
    fun level(day: DailyActivity?, goal: DailyGoal): Int {
        if (day == null || !day.hasAnything) return 0
        val weight = day.questions + day.reviews * goal.questions / 2 + day.planTasks * goal.questions
        val ratio = if (goal.questions <= 0) 1f else weight.toFloat() / goal.questions
        return when {
            ratio >= 2f -> 4
            ratio >= 1f -> 3
            ratio >= 0.5f -> 2
            else -> 1
        }
    }

    /**
     * Monta o resumo da sequência. [days] pode vir em qualquer ordem e só precisa conter os dias
     * com atividade; os dias vazios são tratados como zerados.
     */
    fun summarize(
        days: Collection<DailyActivity>,
        goal: DailyGoal,
        today: LocalDate = LocalDate.now(),
        calendarWeeks: Int = 26,
    ): StreakSummary {
        val byDate = days.associateBy { it.date }
        val doneDays = byDate.values.filter { isDone(it, goal) }.mapTo(hashSetOf()) { it.date }

        // Sequência atual: o dia de hoje ainda em aberto não quebra a sequência de ontem.
        var cursor = if (today in doneDays) today else today.minusDays(1)
        var current = 0
        while (cursor in doneDays) { current++; cursor = cursor.minusDays(1) }

        var best = 0
        var run = 0
        val sorted = doneDays.sorted()
        sorted.forEachIndexed { index, date ->
            run = if (index > 0 && sorted[index - 1].plusDays(1) == date) run + 1 else 1
            best = maxOf(best, run)
        }

        val weekStart = today.minusDays((today.dayOfWeek.value - 1).toLong())
        val week = (0..6).map { offset ->
            val date = weekStart.plusDays(offset.toLong())
            val activity = byDate[date]
            StreakDay(
                date = date,
                done = date in doneDays,
                partial = date !in doneDays && activity?.hasAnything == true,
                future = date.isAfter(today),
            )
        }

        val calendarStart = today.minusDays((today.dayOfWeek.value - 1).toLong()).minusWeeks((calendarWeeks - 1).toLong())
        val calendar = generateSequence(calendarStart) { it.plusDays(1) }
            .takeWhile { !it.isAfter(today) }
            .map { byDate[it] ?: DailyActivity(it) }
            .toList()

        return StreakSummary(
            current = current,
            best = maxOf(best, current),
            todayDone = today in doneDays,
            todayQuestions = byDate[today]?.questions ?: 0,
            todayPlanTasks = byDate[today]?.planTasks ?: 0,
            todayReviews = byDate[today]?.reviews ?: 0,
            goal = goal,
            week = week,
            calendar = calendar,
            activeDays = byDate.values.count { it.hasAnything },
            totalQuestions = byDate.values.sumOf { it.questions },
            totalMinutes = byDate.values.sumOf { it.minutes },
        )
    }
}
