package br.com.estudario.web.data

import br.com.estudario.domain.DailyGoal
import br.com.estudario.domain.ProgressEngine
import br.com.estudario.domain.StreakEngine
import br.com.estudario.domain.StreakSummary
import br.com.estudario.domain.planner.PlanTaskType
import kotlinx.browser.localStorage
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

/** Preferências deste navegador (no app ficam em Ajustes). */
object Prefs {
    private fun read(key: String): String? = runCatching { localStorage.getItem(key) }.getOrNull()
    private fun write(key: String, value: String) { runCatching { localStorage.setItem(key, value) } }

    /**
     * Meta diária de questões (a sequência conta o dia com essa meta, uma tarefa do plano ou uma revisão).
     * Vai na foto da conta (chave "userPrefs", a mesma do app, SyncedPrefs.kt) com o histórico das
     * mudanças: com metas diferentes, o mesmo dia fechava aqui e não no celular, que gastava o
     * marca-página num dia estudado. Vale a meta mudada por último, em qualquer aparelho.
     */
    var dailyGoal: Int
        get() = goal().questions
        set(value) {
            val next = value.coerceIn(DailyGoal.MIN, DailyGoal.MAX)
            val old = goal()
            val today = Queries.todayEpoch()
            val history = (old.history.ifEmpty { listOf(0L to old.questions) }.filter { it.first != today } + (today to next)).sortedBy { it.first }
            val at = kotlin.js.Date.now().toLong()
            write("estudario.goal", next.toString())
            write("estudario.goal.updatedAt", at.toString())
            Store.update { it.withObject(SYNCED_KEY, synced(it).withGoal(next, history.joinToString(",") { (d, q) -> "$d:$q" }, at)) }
        }

    /** A meta com o histórico: a da conta, se mudou depois da deste navegador. */
    fun goal(): DailyGoal {
        val cloud = Store.data.obj(SYNCED_KEY)
        val cloudAt = cloud?.long("goalUpdatedAt") ?: 0L
        val localAt = read("estudario.goal.updatedAt")?.toLongOrNull() ?: 0L
        if (cloud != null && cloud.long("dailyGoal") != null && cloudAt >= localAt) {
            val history = (cloud["goalHistory"] as? JsonPrimitive)?.contentOrNull.orEmpty().split(',').mapNotNull { entry ->
                val parts = entry.split(':')
                val day = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
                val q = parts.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
                day to q
            }.sortedBy { it.first }
            return DailyGoal(cloud.long("dailyGoal")!!.toInt().coerceIn(DailyGoal.MIN, DailyGoal.MAX), history)
        }
        return DailyGoal(read("estudario.goal")?.toIntOrNull()?.coerceIn(DailyGoal.MIN, DailyGoal.MAX) ?: DailyGoal.DEFAULT.questions)
    }

    /** Chave da foto com a meta e as peças vistas do armário (a mesma do app). */
    const val SYNCED_KEY = "userPrefs"

    fun synced(data: Snapshot): JsonObject = data.obj(SYNCED_KEY) ?: JsonObject(emptyMap())

    /**
     * Uma lista do que já foi visto ou comemorado em qualquer aparelho da conta ("wardrobeSeen",
     * "celebratedPieces", "celebratedBadges"; as mesmas do app). Só cresce.
     */
    fun syncedSet(name: String): Set<String> = (synced(Store.data)[name] as? kotlinx.serialization.json.JsonArray)
        ?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull?.takeIf(String::isNotBlank) }?.toSet().orEmpty()

    /** Soma [values] à lista da conta (o app recebe na próxima sincronização). */
    fun addSynced(name: String, values: Set<String>) {
        if (syncedSet(name).containsAll(values)) return
        Store.update { data ->
            val current = (synced(data)[name] as? kotlinx.serialization.json.JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.contentOrNull }.orEmpty()
            val list = kotlinx.serialization.json.JsonArray((current + values).filter { it.isNotBlank() }.distinct().sorted().map { JsonPrimitive(it) })
            data.withObject(SYNCED_KEY, JsonObject(synced(data) + (name to list)))
        }
    }

    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.withGoal(questions: Int, history: String, at: Long) =
        JsonObject(this + mapOf("dailyGoal" to JsonPrimitive(questions), "goalHistory" to JsonPrimitive(history), "goalUpdatedAt" to JsonPrimitive(at)))

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
    private var lastGoal: DailyGoal? = null
    private var last: ProgressView? = null

    fun of(data: Snapshot): ProgressView {
        val goal = Prefs.goal()
        last?.let { if (lastData === data && lastGoal == goal) return it }
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
                extras = run {
                    val (early, late) = br.com.estudario.domain.BadgeExtras.hourDays(data.attempts.map { it.answeredAt } + data.focusSessions.map { it.startedAt } + data.sessions.map { it.startedAt } + data.reviewHistory.map { it.reviewedAt })
                    br.com.estudario.domain.BadgeExtras(
                        focusSessions = data.focusSessions.size,
                        marks = data.theoryMarks.size + data.notes.size,
                        errorsFixed = data.errors.count { it.retryCorrectCount > 0 },
                        streakSaves = streak.protectedDays.size,
                        earlyDays = early, lateDays = late,
                    )
                },
            ),
        )
        return ProgressView(streak, progress).also { lastData = data; lastGoal = goal; last = it }
    }

    /** XP que uma tarefa do plano rende (mostrado na tarefa, como no app). */
    fun taskXp(task: PlanTask): Int {
        val type = runCatching { PlanTaskType.valueOf(task.type) }.getOrNull() ?: return 0
        return ProgressEngine.previewPlanTask(type, task.minutes, task.questions).base
    }

}
