package br.com.estudario.ui.planner

import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType

internal fun matchingPendingTopicTask(
    current: ActivePlanUiState,
    topicId: Long,
    preferredTaskId: String? = null,
    type: PlanTaskType = PlanTaskType.THEORY,
): PlannerTaskUi? {
    val planId = current.activePlan?.id ?: return null
    val candidates = current.tasks.filter { row ->
        row.entity.planId == planId && row.entity.topicId == topicId && row.entity.type == type &&
            row.entity.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
    }
    // Uma tela antiga de uma tarefa já concluída não pode fechar a próxima tarefa do tópico.
    val preferred = current.tasks.firstOrNull { it.entity.id == preferredTaskId }
    if (preferred?.entity?.type == type) return candidates.firstOrNull { it.entity.id == preferredTaskId }
    return candidates.minWithOrNull(compareBy({ it.entity.scheduledEpochDay != current.today.toEpochDay() }, { it.entity.scheduledEpochDay }, { it.entity.sequence }))
}
