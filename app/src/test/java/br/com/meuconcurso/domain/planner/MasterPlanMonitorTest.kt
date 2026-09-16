package br.com.meuconcurso.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.LocalDate

class MasterPlanMonitorTest {
    @Test
    fun `critical master subject warns after configured inactivity`() {
        val alerts = MasterPlanMonitor.evaluate(
            today = LocalDate.of(2026, 9, 15),
            inactivityThresholdDays = 7,
            subjects = listOf(MasterSubject(10L, "Segurança da Informação", PlanPriority.CRITICAL)),
            lastExecutionBySubject = mapOf(10L to LocalDate.of(2026, 9, 6)),
        )

        assertEquals(9, alerts.single().inactiveDays)
        assertEquals(LocalDate.of(2026, 9, 6), alerts.single().lastExecutionDate)
        assertFalse(alerts.single().blocking)
    }
}
