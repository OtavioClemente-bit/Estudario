package br.com.estudario.domain

import br.com.estudario.domain.simulation.BlueprintTopic
import br.com.estudario.domain.simulation.SimulationBlueprint
import br.com.estudario.domain.simulation.SimulationMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExamWeightsTest {
    @Test
    fun heavySubjectsBecomeHighPriorityAndEvidenceRoundTrips() {
        val all = ExamWeights.assessAll(listOf(40, 10, 10, 5).map { ExamWeights.Share(it) })
        assertEquals(PriorityLevel.VERY_HIGH, all[0].level)
        // 10 de 65 (15%) com 4 matérias (média de 25%) fica abaixo da média.
        assertEquals(PriorityLevel.LOW, all[1].level)
        assertTrue(all[3].level == PriorityLevel.LOW || all[3].level == PriorityLevel.VERY_LOW)
        assertEquals(PrioritySource.OFFICIAL_EXAM_STRUCTURE, all[0].source)
        assertEquals(40, ExamWeights.questionsOfJson(ExamWeights.evidenceJson(all[0].evidence)))
        assertEquals(emptyList(), ExamWeights.assessAll(listOf(ExamWeights.Share(10), null)))
    }

    @Test
    fun fullExamFollowsRealWeightsAndStudiedFollowsPriority() {
        // Matéria A tem 3 tópicos mas 30 questões na prova; B tem 9 tópicos e 10 questões.
        val topics = (11L..13L).map { BlueprintTopic(it, 1, "A", listOf("a$it"), 50, true, true, subjectExamQuestions = 30) } +
            (2L..10L).map { BlueprintTopic(it, 2, "B", listOf("b$it"), 50, true, true, subjectExamQuestions = 10) }
        val full = SimulationBlueprint.build(SimulationMode.FULL, topics, 40).perSubject.toMap()
        assertEquals(30, full["A"])
        assertEquals(10, full["B"])
        val studied = SimulationBlueprint.build(SimulationMode.STUDIED, topics, 40).perSubject.toMap()
        assertTrue(studied.getValue("B") > studied.getValue("A"))
    }
}
