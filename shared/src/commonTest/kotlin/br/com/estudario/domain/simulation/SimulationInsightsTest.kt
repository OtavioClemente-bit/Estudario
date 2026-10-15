package br.com.estudario.domain.simulation

import br.com.estudario.domain.simulation.SimulationInsights.Answered
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SimulationInsightsTest {
    @Test
    fun topicWithMostLostPointsComesFirstWithItsTrap() {
        val reading = SimulationInsights.read(
            listOf(
                Answered(1, "FACIL", "Explicação.\n\n> **Armadilha:** Confundir taxa com juros.", false),
                Answered(1, "MEDIA", "Explicação.", null),
                Answered(2, "MEDIA", "Explicação.\n\n**Pegadinha:** Trocar regra por exceção", false),
                Answered(3, "DIFICIL", "Explicação.", true),
            ),
        )
        assertEquals(listOf(1L, 2L), reading.lostTopics.map { it.topicId })
        assertEquals(2, reading.lostTopics[0].lost)
        assertEquals("Confundir taxa com juros", reading.lostTopics[0].trap)
        assertEquals("Trocar regra por exceção", reading.lostTopics[1].trap)
        assertEquals(listOf("Fáceis", "Médias", "Difíceis"), reading.levels.map { it.label })
    }

    @Test
    fun missingEasyQuestionsGetsAdvice() {
        val answers = List(4) { Answered(it.toLong(), "FACIL", "x", it == 0) } + List(3) { Answered(9, "DIFICIL", "x", true) }
        val advice = assertNotNull(SimulationInsights.read(answers).advice)
        assertTrue("fáceis" in advice)
    }
}
