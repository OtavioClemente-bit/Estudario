package br.com.estudario.domain.planner

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/**
 * A previsão pelo próprio plano: roda o motor de verdade semana a semana, supondo que a pessoa faça
 * tudo o que ele marca (com as revisões D+1, D+7 e D+30 que o app agenda), e anota o dia em que cada
 * tópico é estudado. Assim a data prometida é a do plano real, não uma conta paralela: a conta
 * antiga, por fatias fixas de cada fase, errava semanas para mais ou para menos conforme o tamanho
 * do edital e o peso das revisões.
 */
object PlanProjection {
    data class Result(
        /** Dia em que cada tópico (folha) é estudado; só os que couberam até [until]. */
        val studiedOn: Map<Long, LocalDate>,
        /** Dia do último tópico pendente; null quando não cabe tudo até [until]. */
        val coverageDate: LocalDate?,
        val pendingTopics: Int,
    )

    private val reviewOffsets = listOf(1, 7, 30)
    /**
     * O plano de verdade se refaz todo dia (e a cada tarefa concluída); simular semana a semana adiantava
     * conteúdo que o plano real não adianta, e a previsão prometia semanas antes. Por isso a
     * simulação anda dia a dia (até um ano); depois, semana a semana (é longe, e a conta fica leve).
     */
    private const val DAILY_DAYS = 400

    fun project(input: PlannerSnapshotBuilder.Input, until: LocalDate, engine: StudyPlannerEngine = StudyPlannerEngine()): Result {
        val subjectIds = input.planSubjects.filterNot { it.paused }.mapTo(hashSetOf()) { it.subjectId }
        val parents = input.topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val leaves = input.topics.filter { it.subjectId in subjectIds && it.id !in parents }
        val pendingIds = leaves.filterNot { it.studied }.mapTo(linkedSetOf()) { it.id }
        val studiedOn = mutableMapOf<Long, LocalDate>()
        if (pendingIds.isEmpty()) return Result(studiedOn, input.today, 0)

        val dayMillis = 86_400_000L
        var topics = input.topics
        val reviews = input.pendingReviews.toMutableList()
        var nextReviewId = -1L
        val tasks = input.tasks.filter { it.status != PlanTaskStatus.REPROGRAMADA && it.status != PlanTaskStatus.NAO_REALIZADA }.toMutableList()
        val executions = input.executions.toMutableList()
        var revision = input.plan.revision
        var day = input.today
        var first = true
        while (!day.isAfter(until) && studiedOn.size < pendingIds.size) {
            val snapshot = PlannerSnapshotBuilder.build(
                input.copy(
                    plan = input.plan.copy(revision = revision), topics = topics, pendingReviews = reviews.toList(),
                    tasks = tasks.toList(), executions = executions.toList(), today = day,
                ),
            )
            val proposal = engine.plan(snapshot, if (first) ReplanReason.SUBJECT_CHANGED else ReplanReason.DAY_MISSED)
            first = false
            revision++
            val moved = proposal.transitions.mapTo(hashSetOf()) { it.taskId }
            tasks.removeAll { it.id in moved }
            tasks += proposal.newTasks

            // A semana feita do jeito que o plano mandou, dia por dia.
            val weekEnd = if (daysBetween(input.today, day) < DAILY_DAYS) day else day.plusDays(6)
            val due = tasks.filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) && !it.date.isBefore(day) && !it.date.isAfter(minOf(weekEnd, until)) }
                .sortedWith(compareBy<PlannerTask>({ it.date }, { it.sequence }))
            for (task in due) {
                val index = tasks.indexOfFirst { it.id == task.id }
                if (index >= 0) tasks[index] = tasks[index].copy(status = PlanTaskStatus.CONCLUIDA)
                executions += TaskExecution("proj-${task.id}", task.id, task.subjectId, task.topicId, task.date, task.plannedMinutes, task.plannedQuestions, 0)
                val topicId = task.topicId ?: continue
                if (task.type == PlanTaskType.THEORY && topicId in pendingIds && topicId !in studiedOn) {
                    studiedOn[topicId] = task.date
                    topics = topics.map { if (it.id == topicId) it.copy(studied = true, lastStudiedAt = task.date.toEpochDays() * dayMillis) else it }
                    reviewOffsets.forEach { offset ->
                        reviews += PlannerSnapshotBuilder.PendingReview(nextReviewId--, topicId, task.date.plusDays(offset).toEpochDays() * dayMillis, 0)
                    }
                }
                if (task.type == PlanTaskType.REVIEW) {
                    reviews.filter { it.topicId == topicId && it.dueAt <= (task.date.toEpochDays() + 1) * dayMillis }
                        .minByOrNull { it.dueAt }?.let(reviews::remove)
                }
            }
            day = weekEnd.plusDays(1)
        }
        val coverage = if (studiedOn.size == pendingIds.size) studiedOn.values.max() else null
        return Result(studiedOn, coverage, pendingIds.size)
    }
}
