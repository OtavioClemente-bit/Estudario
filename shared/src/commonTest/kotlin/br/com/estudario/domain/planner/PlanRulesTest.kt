package br.com.estudario.domain.planner

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlanRulesTest {
    private val today = 20_000L

    @Test fun atrasadaEhDiaPassadoAindaPorFazer() {
        assertTrue(PlanRules.isOverdue(today - 1, "PLANEJADA", today))
        assertTrue(PlanRules.isOverdue(today - 5, "EM_ANDAMENTO", today))
        assertFalse(PlanRules.isOverdue(today, "PLANEJADA", today))
        assertFalse(PlanRules.isOverdue(today + 1, "PLANEJADA", today))
    }

    @Test fun reprogramadaCanceladaConcluidaNaoAtrasam() {
        listOf("REPROGRAMADA", "CANCELADA", "CONCLUIDA", "NAO_REALIZADA", "PAUSADA").forEach {
            assertFalse(PlanRules.isOverdue(today - 1, it, today), it)
        }
        assertFalse(PlanRules.isCurrent("REPROGRAMADA"))
        assertFalse(PlanRules.isCurrent("CANCELADA"))
        assertTrue(PlanRules.isCurrent("CONCLUIDA"))
    }

    @Test fun planoAtivoPrefereConcursoPrincipal() {
        val plans = listOf(
            PlanRules.PlanRef("a", active = true, archived = false, competitionId = 1),
            PlanRules.PlanRef("b", active = true, archived = false, competitionId = 2),
        )
        assertEquals("b", PlanRules.activePlan(plans, 2))
        assertEquals("a", PlanRules.activePlan(plans, null))
        assertEquals("a", PlanRules.activePlan(plans, 9))
    }

    @Test fun semPlanoAtivoNaoInventaUm() {
        val plans = listOf(
            PlanRules.PlanRef("a", active = false, archived = false, competitionId = 1),
            PlanRules.PlanRef("b", active = true, archived = true, competitionId = 1),
        )
        assertNull(PlanRules.activePlan(plans, 1))
    }
}
