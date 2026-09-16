package br.com.meuconcurso.domain.planner

object StudyPlanProgressCalculator {
    fun calculate(
        plannedMinutes: Int,
        completedPlannedMinutes: Int,
        actualMinutes: Int,
        questions: Int,
        correct: Int,
        syllabusCoveragePercent: Int,
    ): PlanMetrics {
        require(plannedMinutes >= 0)
        require(completedPlannedMinutes >= 0)
        require(actualMinutes >= 0)
        require(questions >= 0)
        require(correct in 0..questions)
        require(syllabusCoveragePercent in 0..100)

        val adherence = if (plannedMinutes == 0) 0
        else (completedPlannedMinutes * 100 / plannedMinutes).coerceIn(0, 100)
        val accuracy = if (questions == 0) null else correct * 100 / questions

        return PlanMetrics(
            plannedMinutes = plannedMinutes,
            actualMinutes = actualMinutes,
            adherencePercent = adherence,
            questions = questions,
            correct = correct,
            accuracyPercent = accuracy,
            syllabusCoveragePercent = syllabusCoveragePercent,
            overtimeMinutes = (actualMinutes - completedPlannedMinutes).coerceAtLeast(0),
        )
    }
}
