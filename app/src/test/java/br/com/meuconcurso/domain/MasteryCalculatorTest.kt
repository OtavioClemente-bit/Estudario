package br.com.meuconcurso.domain

import br.com.meuconcurso.data.local.TopicStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MasteryCalculatorTest {
    @Test fun unseenTopicStartsAtZero() {
        assertEquals(0, MasteryCalculator.percent(MasteryInput(TopicStatus.NAO_ESTUDADO, 0, 0, 0, 0)))
    }

    @Test fun strongPracticeAndReviewsProduceHighMastery() {
        val result = MasteryCalculator.percent(MasteryInput(TopicStatus.ESTUDADO, 20, 19, 0, 3, recentAnswered = 10, recentCorrect = 10))
        assertTrue(result >= 90)
        assertEquals("Forte", MasteryCalculator.label(result))
    }

    @Test fun tinyPerfectSampleDoesNotClaimHighMastery() {
        val result = MasteryCalculator.percent(MasteryInput(TopicStatus.ESTUDADO, 2, 2, 0, 0, recentAnswered = 2, recentCorrect = 2))
        assertTrue(result < 50)
    }

    @Test fun recurringErrorsReduceMastery() {
        val clean = MasteryCalculator.percent(MasteryInput(TopicStatus.ESTUDADO, 10, 8, 0, 1))
        val errors = MasteryCalculator.percent(MasteryInput(TopicStatus.ESTUDADO, 10, 8, 4, 1))
        assertTrue(errors < clean)
    }
}
