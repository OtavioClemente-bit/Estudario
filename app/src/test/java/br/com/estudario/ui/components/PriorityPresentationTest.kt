package br.com.estudario.ui.components

import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PrioritySource
import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityPresentationTest {
    @Test fun everyLevelHasHumanReadableText() {
        assertEquals("Muito alta", PriorityPresentation.label(PriorityLevel.VERY_HIGH))
        assertEquals("Alta", PriorityPresentation.label(PriorityLevel.HIGH))
        assertEquals("Média", PriorityPresentation.label(PriorityLevel.MEDIUM))
        assertEquals("Baixa", PriorityPresentation.label(PriorityLevel.LOW))
        assertEquals("Muito baixa", PriorityPresentation.label(PriorityLevel.VERY_LOW))
    }

    @Test fun overrideSourceTakesPrecedenceOverAutomaticSource() {
        assertEquals("Definida por você", PriorityPresentation.sourceLabel(PrioritySource.AI_INFERENCE, true))
        assertEquals("Sugerida automaticamente", PriorityPresentation.sourceLabel(PrioritySource.AI_INFERENCE, false))
    }

    @Test fun missingAssessmentInheritsParentPriorityButOwnOverrideWins() {
        val inherited = PriorityPresentation.state(50, PrioritySource.DEFAULT, 0f, null, "[]", false, null, PriorityLevel.HIGH)
        assertEquals(PriorityLevel.HIGH, inherited.effectivePriority)

        val overridden = PriorityPresentation.state(50, PrioritySource.DEFAULT, 0f, null, "[]", false, PriorityLevel.LOW, PriorityLevel.HIGH)
        assertEquals(PriorityLevel.LOW, overridden.effectivePriority)
    }
}
