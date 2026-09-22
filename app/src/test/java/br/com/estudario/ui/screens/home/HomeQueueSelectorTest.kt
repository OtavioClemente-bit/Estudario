package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.QueueWithTopic
import br.com.estudario.data.local.StudyQueueEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeQueueSelectorTest {
    @Test
    fun skipsPausedFirstRow() {
        val paused = queueRow(id = 1, position = 0, paused = true, status = TopicStatus.NAO_ESTUDADO)
        val ready = queueRow(id = 2, position = 1, status = TopicStatus.EM_ESTUDO)

        assertEquals(2L, firstEligibleQueueTopic(listOf(paused, ready))?.item?.topicId)
    }

    @Test
    fun skipsCompletedTopicAndChoosesNext() {
        val complete = queueRow(id = 1, position = 0, status = TopicStatus.ESTUDADO)
        val ready = queueRow(id = 2, position = 1, status = TopicStatus.NAO_ESTUDADO)

        assertEquals(2L, firstEligibleQueueTopic(listOf(complete, ready))?.item?.topicId)
    }

    @Test
    fun returnsNullWhenEveryQueueItemIsPaused() {
        val items = listOf(
            queueRow(id = 1, position = 0, paused = true, status = TopicStatus.NAO_ESTUDADO),
            queueRow(id = 2, position = 1, paused = true, status = TopicStatus.EM_ESTUDO),
        )

        assertNull(firstEligibleQueueTopic(items))
    }

    @Test
    fun returnsNullWhenEveryQueueItemIsCompleted() {
        val items = listOf(
            queueRow(id = 1, position = 0, status = TopicStatus.ESTUDADO),
            queueRow(id = 2, position = 1, status = TopicStatus.DOMINADO),
        )

        assertNull(firstEligibleQueueTopic(items))
    }

    @Test
    fun choosesLowestPositionAmongEligibleRows() {
        val later = queueRow(id = 3, position = 3, status = TopicStatus.NAO_ESTUDADO)
        val first = queueRow(id = 2, position = 1, status = TopicStatus.EM_ESTUDO)
        val paused = queueRow(id = 1, position = 0, paused = true, status = TopicStatus.NAO_ESTUDADO)

        assertEquals(2L, firstEligibleQueueTopic(listOf(later, first, paused))?.item?.topicId)
    }

    private fun queueRow(
        id: Long,
        position: Int,
        paused: Boolean = false,
        status: TopicStatus,
    ) = QueueWithTopic(
        item = StudyQueueEntity(id = id, topicId = id, position = position, paused = paused),
        topic = TopicEntity(id = id, subjectId = 1, title = "Tópico $id", status = status),
    )
}
