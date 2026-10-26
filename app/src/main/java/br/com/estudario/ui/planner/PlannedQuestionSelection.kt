package br.com.estudario.ui.planner

import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.planner.PlanTaskEntity

/** Questões que já estão salvas e pertencem ao escopo da tarefa planejada. */
fun savedQuestionsForPlanTask(
    task: PlanTaskEntity,
    questions: List<QuestionWithOptions>,
    topics: List<TopicEntity>,
): List<QuestionWithOptions> {
    val subjectId = task.subjectId ?: return emptyList()
    val topicId = task.topicId
    return questions.filter { question ->
        if (topicId != null) question.question.topicId == topicId
        else topics.any { it.id == question.question.topicId && it.subjectId == subjectId }
    }
}

/**
 * A bateria da tarefa do plano sem repetir acerto (PlanQuestionPool): não respondidas do tópico, os
 * erros para refazer e as não respondidas dos tópicos já começados da mesma matéria. O site usa a
 * mesma regra.
 */
fun planTaskQuestionIds(
    topicId: Long?,
    subjectId: Long?,
    questions: List<QuestionWithOptions>,
    topics: List<TopicEntity>,
    pendingErrorQuestionIds: Set<Long>,
    count: Int,
): List<Long> {
    val topicById = topics.associateBy { it.id }
    val subject = subjectId ?: topicId?.let { topicById[it]?.subjectId }
    val studied = topics.filter { it.status != br.com.estudario.data.local.TopicStatus.NAO_ESTUDADO }.mapTo(hashSetOf()) { it.id }
    val candidates = questions.filter { !it.question.isHidden && it.question.simulationId == null }.map {
        br.com.estudario.domain.planner.PlanQuestionCandidate(
            id = it.question.id,
            topicId = it.question.topicId,
            subjectId = topicById[it.question.topicId]?.subjectId,
            answered = it.question.answerCount > 0,
            pendingError = it.question.id in pendingErrorQuestionIds,
        )
    }
    return br.com.estudario.domain.planner.PlanQuestionPool.pick(candidates, topicId, subject, studied, count)
}
