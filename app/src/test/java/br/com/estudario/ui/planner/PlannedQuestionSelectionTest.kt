package br.com.estudario.ui.planner

import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskType
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannedQuestionSelectionTest {
    @Test
    fun `tarefa de topico seleciona apenas as questoes salvas naquele topico`() {
        val task = task(topicId = 10, plannedQuestions = 15)
        val saved = listOf(question(1, 10), question(2, 11))
        val topics = listOf(topic(10, subjectId = 1), topic(11, subjectId = 1))

        val selected = savedQuestionsForPlanTask(task, saved, topics)

        assertEquals(listOf(1L), selected.map { it.question.id })
    }

    @Test
    fun `bateria da materia seleciona as questoes salvas nos topicos da materia`() {
        val task = task(topicId = null, subjectId = 1, plannedQuestions = 15)
        val saved = listOf(question(1, 10), question(2, 11), question(3, 20))
        val topics = listOf(topic(10, subjectId = 1), topic(11, subjectId = 1), topic(20, subjectId = 2))

        val selected = savedQuestionsForPlanTask(task, saved, topics)

        assertEquals(listOf(1L, 2L), selected.map { it.question.id })
    }

    @Test
    fun `tarefa sem questoes salvas nao inicia uma bateria vazia`() {
        val task = task(topicId = 10, plannedQuestions = 15)

        assertEquals(emptyList<QuestionWithOptions>(), savedQuestionsForPlanTask(task, emptyList(), listOf(topic(10, 1))))
    }

    private fun task(topicId: Long?, subjectId: Long? = 1, plannedQuestions: Int) = PlanTaskEntity(
        id = "task-questions",
        planId = "plan",
        competitionId = 1,
        subjectId = subjectId,
        topicId = topicId,
        subjectNameSnapshot = "Direito",
        topicNameSnapshot = "Constitucional",
        scheduledEpochDay = 1,
        type = PlanTaskType.QUESTIONS,
        plannedMinutes = 30,
        plannedQuestions = plannedQuestions,
        priority = PlanPriority.HIGH,
        createdRevision = 0,
        updatedRevision = 0,
    )

    private fun question(id: Long, topicId: Long) = QuestionWithOptions(
        QuestionEntity(id = id, topicId = topicId, statement = "Pergunta $id", explanation = "Explicação"),
        emptyList(),
    )

    private fun topic(id: Long, subjectId: Long) = TopicEntity(id = id, subjectId = subjectId, title = "Tópico $id")
}
