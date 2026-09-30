package br.com.estudario.domain.simulation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulationBlueprintTest {
    private fun topics(subject: Long, name: String, count: Int, weight: Int = 50, studied: Int = 0) = (1..count).map { index ->
        BlueprintTopic(subject * 100 + index, subject, name, listOf(name, "Tópico $index"), weight, studied = index <= studied, hasContent = false)
    }

    @Test fun `total is exact and split into parts of at most 30`() {
        val pool = topics(1, "Português", 12) + topics(2, "Direito", 20) + topics(3, "Informática", 8)
        val blueprint = SimulationBlueprint.build(SimulationMode.FULL, pool, 120)
        assertEquals(120, blueprint.total)
        assertEquals(4, blueprint.parts.size)
        assertTrue(blueprint.parts.all { it.size <= 30 })
        assertTrue(blueprint.parts.flatMap { it.items }.all { it.count in 1..SimulationBlueprint.MAX_PER_ITEM })
    }

    @Test fun `subjects with more topics get more questions and every subject appears`() {
        val pool = topics(1, "Português", 5) + topics(2, "Direito", 20) + topics(3, "Ética", 1)
        val perSubject = SimulationBlueprint.build(SimulationMode.FULL, pool, 60).perSubject.toMap()
        assertTrue(perSubject.getValue("Direito") > perSubject.getValue("Português"))
        assertTrue(perSubject.getValue("Ética") >= 1)
        assertEquals(60, perSubject.values.sum())
    }

    @Test fun `coverage before repetition`() {
        val pool = topics(1, "Direito", 10)
        val items = SimulationBlueprint.build(SimulationMode.FULL, pool, 10).parts.flatMap { it.items }
        assertEquals(10, items.size)
        assertTrue(items.all { it.count == 1 })
    }

    @Test fun `studied mode uses only studied topics`() {
        val pool = topics(1, "Português", 10, studied = 3) + topics(2, "Direito", 10)
        val items = SimulationBlueprint.build(SimulationMode.STUDIED, pool, 20).parts.flatMap { it.items }
        assertTrue(items.all { it.subjectName == "Português" })
        assertEquals(3, items.map { it.topicId }.distinct().size)
        assertEquals(20, items.sumOf { it.count })
    }

    @Test fun `more subjects than questions keeps the heaviest`() {
        val pool = (1L..30L).flatMap { topics(it, "M$it", 1, weight = if (it <= 5) 100 else 0) }
        val blueprint = SimulationBlueprint.build(SimulationMode.DIAGNOSTIC, pool, 20)
        assertEquals(20, blueprint.total)
        assertTrue(blueprint.perSubject.map { it.first }.containsAll((1..5).map { "M$it" }))
    }

    @Test fun `unlock rules`() {
        val fresh = SimulationReadiness(leafTopics = 40, studiedTopics = 0, coveredTopics = 0, studyDays = 0, wrongInFinished = 0)
        assertTrue(SimulationUnlock.lock(SimulationMode.DIAGNOSTIC, fresh).unlocked)
        assertFalse(SimulationUnlock.lock(SimulationMode.STUDIED, fresh).unlocked)
        assertFalse(SimulationUnlock.lock(SimulationMode.REMATCH, fresh).unlocked)
        val studying = fresh.copy(studiedTopics = 6, coveredTopics = 6, studyDays = 4)
        assertTrue(SimulationUnlock.lock(SimulationMode.STUDIED, studying).unlocked)
        assertFalse(SimulationUnlock.lock(SimulationMode.FULL, studying).unlocked)
        assertTrue(SimulationUnlock.lock(SimulationMode.FULL, studying.copy(coveredTopics = 20, studyDays = 10)).unlocked)
    }

    @Test fun `board style and time`() {
        assertEquals("TRUE_FALSE", BoardStyle.styleFor("CEBRASPE"))
        assertEquals("FIVE_OPTIONS", BoardStyle.styleFor("FGV"))
        assertEquals("FIVE_OPTIONS", BoardStyle.styleFor(null))
        assertEquals(180, BoardStyle.minutesFor("FIVE_OPTIONS", 60))
        assertEquals(4, SimulationUnlock.partsFor(120))
    }
}
