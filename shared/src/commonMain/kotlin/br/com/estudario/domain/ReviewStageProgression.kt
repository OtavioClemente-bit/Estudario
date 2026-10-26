package br.com.estudario.domain

import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.time.daysBetween
import br.com.estudario.time.epochMillisToDate
import br.com.estudario.time.plusDays
import br.com.estudario.time.startOfDay
import kotlinx.datetime.TimeZone

/** Estado de uma etapa, independente do armazenamento Android/web. */
data class ReviewStageState(
    val id: Long,
    val stage: Int,
    val dueAt: Long,
    val completedAt: Long? = null,
    val ignoredAt: Long? = null,
    /** Intervalo planejado desde a etapa anterior, sem incluir tentativas difíceis. */
    val intervalDays: Long? = null,
) {
    val pending: Boolean get() = completedAt == null && ignoredAt == null
}

data class ReviewStageChange(val stages: List<ReviewStageState>, val newStage: ReviewStageState? = null)

/** Difícil mantém a etapa; datas futuras conservam seus intervalos, a partir do contato atual. */
object ReviewStageProgression {
    fun record(
        stages: List<ReviewStageState>,
        reviewId: Long,
        difficulty: ReviewDifficulty,
        now: Long,
        zone: TimeZone,
        correct: Int = 0,
        total: Int = 0,
    ): ReviewStageChange? {
        val current = stages.firstOrNull { it.id == reviewId } ?: return null
        val today = epochMillisToDate(now, zone)
        if (!current.pending || epochMillisToDate(current.dueAt, zone) > today) return null
        if (stages.any { it.stage < current.stage && it.pending }) return null

        // Backups antigos não têm intervalDays: capture os intervalos antes de deslocar as datas.
        val ordered = stages.sortedBy { it.stage }
        val normalized = ordered.mapIndexed { index, stage ->
            val inferred = if (index == 0) ReviewIntervals.days.first() else daysBetween(
                epochMillisToDate(ordered[index - 1].dueAt, zone), epochMillisToDate(stage.dueAt, zone),
            ).coerceAtLeast(1)
            stage.copy(intervalDays = stage.intervalDays ?: inferred)
        }
        val repeat = difficulty == ReviewDifficulty.DIFICIL
        var cursor = if (repeat) today.plusDays(1) else today
        var firstNext = true
        val updates = normalized.map { stage ->
            when {
                stage.id == reviewId -> stage.copy(
                    dueAt = if (repeat) cursor.startOfDay(zone).toEpochMilliseconds() else stage.dueAt,
                    completedAt = if (repeat) null else now,
                )
                stage.stage > current.stage && stage.pending -> {
                    val interval = if (!repeat && firstNext && difficulty == ReviewDifficulty.FACIL) {
                        (stage.intervalDays!! * 1.35).toLong().coerceIn(1L, ReviewIntervals.MAX_DAYS)
                    } else stage.intervalDays!!.coerceAtLeast(1)
                    firstNext = false
                    cursor = cursor.plusDays(interval)
                    stage.copy(dueAt = cursor.startOfDay(zone).toEpochMilliseconds(), intervalDays = interval)
                }
                else -> stage
            }
        }
        if (repeat || updates.any { it.stage > current.stage && it.pending }) return ReviewStageChange(updates)

        val interval = ReviewIntervals.adjustedIntervalDays(
            normalized.first { it.id == reviewId }.intervalDays!!, difficulty, correct, total,
        )
        return ReviewStageChange(updates, ReviewStageState(
            id = 0, stage = (ordered.maxOfOrNull { it.stage } ?: current.stage) + 1,
            dueAt = today.plusDays(interval).startOfDay(zone).toEpochMilliseconds(), intervalDays = interval,
        ))
    }
}
