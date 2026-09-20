package br.com.estudario.ui.planner

import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerPresentationTest {
    @Test
    fun taskTypesUsePortugueseLabels() {
        assertEquals("Recordação ativa", PlanTaskType.ACTIVE_RECALL.displayNamePtBr())
        assertEquals("Questões", PlanTaskType.QUESTIONS.displayNamePtBr())
    }

    @Test
    fun statusesAndActionsUsePortugueseLabels() {
        assertEquals("Em andamento", PlanTaskStatus.EM_ANDAMENTO.displayNamePtBr())
        assertEquals("Continuar", completionActionPtBr(PlanTaskStatus.EM_ANDAMENTO))
        assertEquals("Começar", completionActionPtBr(PlanTaskStatus.PLANEJADA))
        assertEquals("Alta", PlanPriority.HIGH.displayNamePtBr())
    }

    @Test
    fun minutesAreReadableForShortAndLongSessions() {
        assertEquals("45 min", minutesLabelPtBr(45))
        assertEquals("1h 20min", minutesLabelPtBr(80))
    }
}
