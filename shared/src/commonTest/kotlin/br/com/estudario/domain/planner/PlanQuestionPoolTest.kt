package br.com.estudario.domain.planner

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlanQuestionPoolTest {
    private fun q(id: Long, topic: Long, answered: Boolean = false, error: Boolean = false, subject: Long = 1) =
        PlanQuestionCandidate(id, topic, subject, answered, error)

    @Test
    fun naoRepeteAcertoEUsaErrosEOutrosTopicosComecados() {
        val all = listOf(
            q(1, 10), q(2, 10, answered = true), q(3, 10, answered = true, error = true),
            q(4, 20), q(5, 30), // 20 já começado, 30 não
            q(6, 40, subject = 2), // outra matéria
        )
        val picked = PlanQuestionPool.pick(all, topicId = 10, subjectId = 1, studiedTopicIds = setOf(10, 20), count = 10, random = Random(1))
        assertEquals(listOf(1L, 3L, 4L), picked)
    }

    @Test
    fun tudoFeitoECertoDevolveVazio() {
        val all = listOf(q(1, 10, answered = true), q(2, 10, answered = true))
        assertTrue(PlanQuestionPool.pick(all, 10, 1, setOf(10), 10).isEmpty())
    }
}
