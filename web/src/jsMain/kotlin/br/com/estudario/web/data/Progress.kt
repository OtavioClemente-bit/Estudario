package br.com.estudario.web.data

import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.domain.planner.PlanTaskType
import kotlinx.browser.localStorage

/** Preferências deste navegador (no app ficam em Ajustes). */
object Prefs {
    private fun read(key: String): String? = runCatching { localStorage.getItem(key) }.getOrNull()
    private fun write(key: String, value: String) { runCatching { localStorage.setItem(key, value) } }

    /** Meta diária de questões (a sequência conta o dia com essa meta, uma tarefa do plano ou uma revisão). */
    var dailyGoal: Int
        get() = read("estudario.goal")?.toIntOrNull()?.coerceIn(DailyGoal.MIN, DailyGoal.MAX) ?: DailyGoal.DEFAULT.questions
        set(value) = write("estudario.goal", value.coerceIn(DailyGoal.MIN, DailyGoal.MAX).toString())

    /** Mostrar a explicação logo após responder (como no app). */
    var explanationRightAway: Boolean
        get() = read("estudario.explain") != "0"
        set(value) = write("estudario.explain", if (value) "1" else "0")

    var focusMinutes: Int
        get() = read("estudario.focus")?.toIntOrNull()?.coerceIn(5, 180) ?: 50
        set(value) = write("estudario.focus", value.coerceIn(5, 180).toString())
}

data class ProgressView(
    val streak: StreakSummary,
    val progress: ProgressEngine.ProgressSummary,
)

/** XP, nível, sequência e emblemas: os mesmos ProgressEngine/StreakEngine do app, calculados uma vez por foto. */
object Progress {
    private var lastData: Snapshot? = null
    private var lastGoal: Int = -1
    private var last: ProgressView? = null

    fun of(data: Snapshot): ProgressView {
        val goalQuestions = Prefs.dailyGoal
        last?.let { if (lastData === data && lastGoal == goalQuestions) return it }
        val goal = DailyGoal(goalQuestions)
        val today = Queries.todayDate()
        val days = Queries.dailyActivity(data)
        val streak = StreakEngine.summarize(days, goal, today)
        val typeById = data.tasks.associate { it.id to it.type }
        val planWork = data.executions.filter { it.taskId != null }.groupBy { it.taskId!! }.mapNotNull { (taskId, runs) ->
            val type = typeById[taskId]?.let { runCatching { PlanTaskType.valueOf(it) }.getOrNull() } ?: return@mapNotNull null
            ProgressEngine.PlanWork(Queries.dateOf(runs.maxOf { it.completedAt }), type, runs.sumOf { it.minutes }, runs.sumOf { it.correct }, runs.sumOf { it.questions })
        }
        val leaves = Queries.leafTopics(data)
        val progress = ProgressEngine.evaluate(
            ProgressEngine.ProgressInput(
                today = today,
                days = days,
                goal = goal,
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
        return ProgressView(streak, progress).also { lastData = data; lastGoal = goalQuestions; last = it }
    }

    /** XP que uma tarefa do plano rende (mostrado na tarefa, como no app). */
    fun taskXp(task: PlanTask): Int {
        val type = runCatching { PlanTaskType.valueOf(task.type) }.getOrNull() ?: return 0
        return ProgressEngine.previewPlanTask(type, task.minutes, task.questions).base
    }

}
