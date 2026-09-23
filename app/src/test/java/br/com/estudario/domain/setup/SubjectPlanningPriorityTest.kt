package br.com.estudario.domain.setup

import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.StudyDimensions
import br.com.estudario.domain.planner.StudyEvidence
import br.com.estudario.domain.planner.StudyNeedCalculator
import br.com.estudario.domain.planner.rotationWeight
import br.com.estudario.domain.planner.toExamPriority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guarda a separação dos eixos.
 *
 * Este arquivo substitui a versão anterior, que verificava o comportamento de
 * `effectivePriority(official, difficulty)`, a função que elevava a prioridade da prova quando a
 * pessoa marcava a matéria como difícil. Aquele comportamento era o defeito, não o contrato: ele
 * fazia Banco de Dados (muito importante, fácil) e Português (importância média, muito difícil)
 * chegarem ao motor como a mesma coisa.
 *
 * O que se testa agora é o contrário: que os dois eixos chegam **separados** ao planejamento e que
 * a dificuldade não mexe no peso que o edital dá à matéria.
 */
class SubjectPlanningPriorityTest {

    @Test
    fun `o peso do rodizio depende so da prioridade da prova`() {
        val pesos = PersonalDifficulty.entries.map { ExamPriority.HIGH.rotationWeight() }
        assertEquals(
            "a dificuldade pessoal não pode alterar o peso do edital",
            1,
            pesos.distinct().size,
        )
        assertTrue(ExamPriority.VERY_HIGH.rotationWeight() > ExamPriority.MEDIUM.rotationWeight())
        assertTrue(ExamPriority.MEDIUM.rotationWeight() > ExamPriority.VERY_LOW.rotationWeight())
    }

    @Test
    fun `materias com perfis opostos nao colapsam no mesmo planejamento`() {
        // O caso que motivou a separação.
        val bancoDeDados = StudyDimensions(
            examPriority = ExamPriority.VERY_HIGH,
            personalDifficulty = PersonalDifficulty.EASY,
        )
        val portugues = StudyDimensions(
            examPriority = ExamPriority.MEDIUM,
            personalDifficulty = PersonalDifficulty.VERY_HARD,
        )

        assertNotEquals(bancoDeDados.examPriority, portugues.examPriority)
        assertNotEquals(bancoDeDados.personalDifficulty, portugues.personalDifficulty)
        assertNotEquals(
            "as duas precisam continuar distinguíveis no peso do rodízio",
            bancoDeDados.examPriority.rotationWeight(),
            portugues.examPriority.rotationWeight(),
        )

        val needBanco = StudyNeedCalculator.evaluate(1L, dimensions = bancoDeDados, evidence = StudyEvidence.EMPTY)
        val needPortugues = StudyNeedCalculator.evaluate(2L, dimensions = portugues, evidence = StudyEvidence.EMPTY)
        assertTrue(
            "o edital continua mandando na fila mesmo com a outra sendo mais difícil",
            needBanco.score > needPortugues.score,
        )
    }

    @Test
    fun `a escala de cinco faixas do edital converte sem perder ordem`() {
        val faixas = PriorityLevel.entries.map { it.toExamPriority() }
        assertEquals(PriorityLevel.entries.size, faixas.distinct().size)
        assertEquals(ExamPriority.VERY_HIGH, PriorityLevel.VERY_HIGH.toExamPriority())
        assertEquals(ExamPriority.VERY_LOW, PriorityLevel.VERY_LOW.toExamPriority())
        assertEquals(ExamPriority.VERY_HIGH, examPriorityOf(PriorityLevel.VERY_HIGH))
    }

    @Test
    fun `os quatro buckets do alocador continuam consistentes`() {
        assertEquals(PlanPriority.CRITICAL, PriorityLevel.VERY_HIGH.toPlanPriority())
        assertEquals(PlanPriority.HIGH, PriorityLevel.HIGH.toPlanPriority())
        assertEquals(PlanPriority.MEDIUM, PriorityLevel.MEDIUM.toPlanPriority())
        assertEquals(PlanPriority.LOW, PriorityLevel.LOW.toPlanPriority())
        assertEquals(PlanPriority.LOW, PriorityLevel.VERY_LOW.toPlanPriority())

        val ranks = listOf(PlanPriority.LOW, PlanPriority.MEDIUM, PlanPriority.HIGH, PlanPriority.CRITICAL)
            .map { it.planningRank() }
        assertEquals(ranks.sorted(), ranks)
    }

    @Test
    fun `a dificuldade de tres niveis converte nos dois sentidos`() {
        SubjectDifficulty.entries.forEach { original ->
            assertEquals(original, original.toPersonalDifficulty().toSubjectDifficulty())
        }
        assertEquals(PersonalDifficulty.HARD, SubjectDifficulty.HARD.toPersonalDifficulty())
        assertEquals(PersonalDifficulty.NORMAL, SubjectDifficulty.MEDIUM.toPersonalDifficulty())
        assertEquals(PersonalDifficulty.EASY, SubjectDifficulty.EASY.toPersonalDifficulty())
        // A escala de cinco é mais rica; os extremos caem nos três níveis sem inventar faixa nova.
        assertEquals(SubjectDifficulty.HARD, PersonalDifficulty.VERY_HARD.toSubjectDifficulty())
        assertEquals(SubjectDifficulty.EASY, PersonalDifficulty.VERY_EASY.toSubjectDifficulty())
    }
}
