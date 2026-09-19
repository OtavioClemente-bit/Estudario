package br.com.estudario.domain

import br.com.estudario.data.local.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

enum class ComputedReviewStatus { FUTURA, DISPONIVEL, ATRASADA, CONCLUIDA, IGNORADA }

object ReviewPolicy {
    val defaultIntervals = listOf(1L, 7L, 30L)

    fun status(review: ReviewScheduleEntity, now: Long = System.currentTimeMillis(), availableWindowHours: Int = 24): ComputedReviewStatus = when {
        review.completedAt != null -> ComputedReviewStatus.CONCLUIDA
        review.ignoredAt != null -> ComputedReviewStatus.IGNORADA
        review.dueAt < now - availableWindowHours * 3_600_000L -> ComputedReviewStatus.ATRASADA
        review.dueAt <= now + availableWindowHours * 3_600_000L -> ComputedReviewStatus.DISPONIVEL
        else -> ComputedReviewStatus.FUTURA
    }
}

data class SmartQuestionCandidate(
    val questionId: Long,
    val topicId: Long,
    val mastery: Int,
    val previousErrors: Int,
    val recurrentError: Boolean,
    val overdueReview: Boolean,
    val answerCount: Int,
    val daysSinceAnswer: Int,
)

data class ScoredQuestion(val questionId: Long, val topicId: Long, val score: Double)

object SmartQuestionSelector {
    fun score(candidate: SmartQuestionCandidate): Double =
        (candidate.previousErrors.coerceAtMost(4) * 12.0) +
            (if (candidate.recurrentError) 35.0 else 0.0) +
            ((100 - candidate.mastery).coerceIn(0, 100) * 0.35) +
            (if (candidate.overdueReview) 24.0 else 0.0) +
            (if (candidate.answerCount == 0) 22.0 else 0.0) +
            candidate.daysSinceAnswer.coerceIn(0, 90) / 6.0 + 1.0

    /** Sorteio ponderado sem reposição; a seed deixa o comportamento reproduzível em testes. */
    fun select(candidates: List<SmartQuestionCandidate>, count: Int, seed: Long): List<ScoredQuestion> {
        val random = Random(seed)
        val pool = candidates.map { ScoredQuestion(it.questionId, it.topicId, score(it)) }.toMutableList()
        val selected = mutableListOf<ScoredQuestion>()
        repeat(count.coerceAtMost(pool.size)) {
            val total = pool.sumOf { it.score.coerceAtLeast(0.1) }
            var target = random.nextDouble(total)
            val index = pool.indexOfFirst { candidate -> target -= candidate.score.coerceAtLeast(0.1); target <= 0 }.let { if (it < 0) pool.lastIndex else it }
            selected += pool.removeAt(index)
        }
        return selected
    }
}

data class SyllabusProgress(val coverage: Int, val mastery: Int)

object ProgressCalculator {
    fun calculate(studied: List<Boolean>, masteryScores: List<Int>): SyllabusProgress {
        if (studied.isEmpty()) return SyllabusProgress(0, 0)
        return SyllabusProgress(
            coverage = studied.count { it } * 100 / studied.size,
            mastery = masteryScores.takeIf { it.isNotEmpty() }?.average()?.toInt()?.coerceIn(0, 100) ?: 0,
        )
    }
}

data class Streak(val current: Int, val best: Int)

object StreakCalculator {
    fun calculate(activityDays: Set<LocalDate>, today: LocalDate = LocalDate.now()): Streak {
        if (activityDays.isEmpty()) return Streak(0, 0)
        val sorted = activityDays.sorted()
        var best = 1
        var run = 1
        for (index in 1 until sorted.size) {
            run = if (sorted[index - 1].plusDays(1) == sorted[index]) run + 1 else 1
            best = maxOf(best, run)
        }
        var cursor = if (today in activityDays) today else today.minusDays(1)
        var current = 0
        while (cursor in activityDays) { current++; cursor = cursor.minusDays(1) }
        return Streak(current, best)
    }

    fun day(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): LocalDate = Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
}

object SessionTypeMapper {
    fun fromMode(mode: String, topicId: Long?, subjectId: Long?): QuestionSessionType = when (mode) {
        "smart" -> QuestionSessionType.SMART
        "errors", "most_errors" -> QuestionSessionType.ERROR_REVIEW
        "simulation" -> QuestionSessionType.SIMULATION
        "daily" -> QuestionSessionType.DAILY_CHALLENGE
        "review" -> QuestionSessionType.REVIEW
        else -> when { topicId != null -> QuestionSessionType.TOPIC; subjectId != null -> QuestionSessionType.SUBJECT; else -> QuestionSessionType.QUICK }
    }
}
