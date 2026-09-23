package br.com.estudario.data.transfer.planner

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlanContextExporterTest {
    @Test
    fun `compact context contains planning signals but no learning content`() {
        val context = PlanContext(
            competition = "TRT-3: TI",
            objective = "Aprovação",
            annualPhase = "Fase 1",
            currentMonth = "2026-09",
            currentWeek = "2026-09-14",
            weeklyCapacityMinutes = 1_320,
            plannedMinutes = 210,
            actualMinutes = 80,
            questions = 20,
            correct = 15,
            adherencePercent = 38,
            capacityDeficitMinutes = 90,
            subjects = listOf(ContextSubject("Segurança", "CRITICAL", 120, 80)),
            weakTopics = listOf(ContextWeakTopic("Criptografia", 52, 25)),
            missedTasks = listOf(ContextTask("Segurança", "Hash", "REVIEW", 30, "2026-09-14")),
            futureTasks = listOf(ContextTask("Banco de Dados", "Normalização", "QUESTIONS", 45, "2026-09-16")),
            alerts = listOf("Segurança não recebe estudo há 9 dias"),
        )

        val json = PlanContextExporter.exportJson(context)
        val text = PlanContextExporter.exportText(context)

        assertTrue(json.contains("\"weeklyCapacityMinutes\":1320"))
        assertTrue(json.contains("Criptografia"))
        assertTrue(text.contains("Déficit: 90 min"))
        assertFalse(json.contains("statement"))
        assertFalse(json.contains("markdown"))
        assertFalse(text.contains("enunciado"))
    }
}
