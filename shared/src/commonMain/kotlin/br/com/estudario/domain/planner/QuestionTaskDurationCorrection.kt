package br.com.estudario.domain.planner

/**
 * Correção compatível com planos salvos pela estimativa antiga, que arredondava questões para um
 * bloco inteiro de teoria. Preserva tarefas iniciadas, bloqueadas, manuais ou já ajustadas.
 */
object QuestionTaskDurationCorrection {
    fun correctedMinutes(
        type: PlanTaskType,
        status: PlanTaskStatus,
        origin: PlanOrigin,
        locked: Boolean,
        plannedMinutes: Int,
        plannedQuestions: Int,
        minutesPerQuestion: Int,
    ): Int? {
        if (type != PlanTaskType.QUESTIONS || status != PlanTaskStatus.PLANEJADA ||
            origin != PlanOrigin.ENGINE || locked || plannedQuestions <= 0 || minutesPerQuestion <= 0
        ) return null

        val estimate = plannedQuestions.toLong() * minutesPerQuestion
        if (estimate <= 0 || estimate >= plannedMinutes) return null
        return estimate.toInt()
    }
}
