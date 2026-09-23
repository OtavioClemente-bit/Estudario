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
