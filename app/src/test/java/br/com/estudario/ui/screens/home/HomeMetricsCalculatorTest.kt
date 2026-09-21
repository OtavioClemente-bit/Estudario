package br.com.estudario.ui.screens.home

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeMetricsCalculatorTest {
    @Test
    fun calculatorReturnsEverySubjectInEditalOrder() {
        val competition = CompetitionEntity(id = 1L, name = "TRT 3ª Região", isPrimary = true)
        val subjects = listOf(
            SubjectEntity(id = 10L, competitionId = competition.id, name = "LÍNGUA PORTUGUESA (NÍVEL MÉDIO/SUPERIOR)", position = 0),
            SubjectEntity(id = 11L, competitionId = competition.id, name = "ANALISTA JUDICIÁRIO – ÁREA APOIO ESPECIALIZADO – TECNOLOGIA DA INFORMAÇÃO", position = 1),
            SubjectEntity(id = 12L, competitionId = competition.id, name = "Direito Administrativo", position = 2),
            SubjectEntity(id = 13L, competitionId = competition.id, name = "Banco de Dados", position = 3),
            SubjectEntity(id = 14L, competitionId = competition.id, name = "Redes de Computadores", position = 4),
        )
        val topics = subjects.mapIndexed { index, subject ->
            TopicEntity(id = 100L + index, subjectId = subject.id, title = "Tópico ${index + 1}")
        }

        val result = calculateHomeMetrics(competition.id, subjects, topics, emptyList(), emptyList(), emptyList(), emptyList())

        assertEquals(subjects.map { it.name }, result.subjects.map { it.name })
        assertEquals(5, result.subjects.size)
    }

    @Test
    fun calculatorCountsZeroAndFullyStudiedTopicsWithoutHidingSubjects() {
        val subjects = listOf(
            SubjectEntity(id = 20L, competitionId = 2L, name = "Português", position = 0),
            SubjectEntity(id = 21L, competitionId = 2L, name = "Direito", position = 1),
        )
        val topics = listOf(
            TopicEntity(id = 200L, subjectId = 20L, title = "Gramática", status = TopicStatus.ESTUDADO),
            TopicEntity(id = 201L, subjectId = 20L, title = "Texto"),
            TopicEntity(id = 202L, subjectId = 21L, title = "Princípios", status = TopicStatus.DOMINADO),
        )

        val result = calculateHomeMetrics(2L, subjects, topics, emptyList(), emptyList(), emptyList(), emptyList())

        assertEquals(2, result.subjects.size)
        assertEquals(66, result.coverage)
        assertEquals(listOf(50, 100), result.subjects.map { it.percent })
    }

    @Test
    fun calculatorKeepsSubjectThatHasNoTopicsYet() {
        val subjects = listOf(
            SubjectEntity(id = 30L, competitionId = 3L, name = "Português", position = 0),
            SubjectEntity(id = 31L, competitionId = 3L, name = "Legislação nova", position = 1),
        )
        val topics = listOf(TopicEntity(id = 300L, subjectId = 30L, title = "Gramática"))

        val result = calculateHomeMetrics(3L, subjects, topics, emptyList(), emptyList(), emptyList(), emptyList())

        assertEquals(listOf("Português", "Legislação nova"), result.subjects.map { it.name })
        assertEquals(listOf(0, 0), result.subjects.map { it.percent })
    }
}
