package br.com.estudario.domain.setup

import java.time.LocalDate

data class PlanCoverageTopic(val id: String, val name: String)

data class PlanCoverageSubject(
    val id: String,
    val name: String,
    val topics: List<PlanCoverageTopic>,
)

data class PlanCoverageTask(
    val subjectId: String?,
    val topicId: String?,
    val date: LocalDate,
    val minutes: Int,
)

data class MissingTopic(
    val subjectId: String,
    val topicId: String,
    val subjectName: String,
    val topicName: String,
)

data class PlanCoverageResult(
    val missingTopics: List<MissingTopic>,
    val missingSubjects: List<String>,
    val overCapacityDates: List<LocalDate>,
    /** Weekday indexes are Monday=1 through Sunday=7. */
    val availabilityMismatchDays: List<Int>,
) {
    val isComplete: Boolean
        get() = missingTopics.isEmpty() && missingSubjects.isEmpty() && overCapacityDates.isEmpty() && availabilityMismatchDays.isEmpty()
}

/** Checks syllabus links and capacity against the person's setup, without mutating the imported plan. */
object PlanCoverageValidator {
    fun validate(
        sourceSubjects: List<PlanCoverageSubject>,
        importedTasks: List<PlanCoverageTask>,
        dayMinutes: List<Int>,
        importedDayMinutes: List<Int> = dayMinutes,
    ): PlanCoverageResult {
        val missingTopics = sourceSubjects.flatMap { subject ->
            subject.topics.filterNot { topic ->
                importedTasks.any { task ->
                    task.subjectId == subject.id && task.topicId == topic.id && task.minutes > 0
                }
            }.map { topic -> MissingTopic(subject.id, topic.id, subject.name, topic.name) }
        }
        val missingSubjects = sourceSubjects
            .filter { it.topics.isEmpty() }
            .filterNot { subject -> importedTasks.any { it.subjectId == subject.id && it.minutes > 0 } }
            .map { it.name }

        val overCapacityDates = importedTasks
            .groupBy { it.date }
            .filter { (date, tasks) ->
                val availableMinutes = dayMinutes.getOrElse(date.dayOfWeek.value - 1) { 0 }.coerceAtLeast(0)
                tasks.sumOf { it.minutes.coerceAtLeast(0) } > availableMinutes
            }
            .keys
            .sorted()

        val availabilityMismatchDays = (0..6)
            .filter { index ->
                dayMinutes.getOrElse(index) { 0 }.coerceAtLeast(0) !=
                    importedDayMinutes.getOrElse(index) { 0 }.coerceAtLeast(0)
            }
            .map { it + 1 }

        return PlanCoverageResult(missingTopics, missingSubjects, overCapacityDates, availabilityMismatchDays)
    }
}
