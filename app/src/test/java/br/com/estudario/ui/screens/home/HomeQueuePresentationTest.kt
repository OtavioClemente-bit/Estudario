package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.QueueWithTopic
import br.com.estudario.data.local.StudyQueueEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeQueuePresentationTest {
    @Test
    fun queueTopicIsShownAsCurrentBeforePlanTask() {
        val queue = StudyTaskUi("queue:1", 1L, "Direito", "Constitucional", "Fila de estudos", "", "Abrir tópico", false)
        val plan = StudyTaskUi("plan:1", 2L, "Português", "Concordância", "Plano", "30 min", "Continuar", true)

        assertEquals(queue, currentHomeTask(queue, plan))
    }

    @Test
    fun planTaskIsFallbackWhenNoQueueTopicIsAvailable() {
        val plan = StudyTaskUi("plan:1", 2L, "Português", "Concordância", "Plano", "30 min", "Continuar", true)

        assertEquals(plan, currentHomeTask(queueTask = null, planTask = plan))
    }

    @Test
    fun queueTopicCanBeCurrentWithoutAPlan() {
        val queue = StudyTaskUi("queue:1", 1L, "Direito", "Constitucional", "Fila de estudos", "", "Abrir tópico", false)

        assertEquals(queue, currentHomeTask(queueTask = queue, planTask = null))
    }

    @Test
    fun queueTopicMapsSubjectAndTopicForTheHomeCard() {
        val mapped = queueTopicUi(queueRow(1, 0, status = TopicStatus.EM_ESTUDO), "Direito")

        assertEquals("queue:1", mapped.id)
        assertEquals("Direito", mapped.subjectName)
        assertEquals("Tópico 1", mapped.topicName)
        assertEquals(1L, mapped.topicId)
        assertEquals("Fila de estudos", mapped.activityLabel)
        assertEquals("", mapped.durationLabel)
        assertEquals("Abrir tópico", mapped.ctaLabel)
        assertEquals(false, mapped.scheduledForToday)
    }

    private fun queueRow(id: Long, position: Int, paused: Boolean = false, status: TopicStatus) = QueueWithTopic(
        item = StudyQueueEntity(id = id, topicId = id, position = position, paused = paused),
        topic = TopicEntity(id = id, subjectId = 1, title = "Tópico $id", status = status),
    )
}
