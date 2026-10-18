package br.com.estudario.domain.simulation

import kotlin.test.Test
import kotlin.test.assertEquals

class OfficialTopicMatcherTest {
    private fun leaf(id: Long, subject: String, title: String) =
        BlueprintTopic(topicId = id, subjectId = subject.length.toLong(), subjectName = subject, path = listOf(title), weight = 50, studied = false, hasContent = false)

    private val leaves = listOf(
        leaf(1, "Língua Portuguesa", "Ortografia oficial."),
        leaf(2, "Língua Portuguesa", "Concordância verbal e nominal."),
        leaf(3, "Língua Portuguesa", "Regência verbal e nominal."),
        leaf(4, "Direito Administrativo", "Atos administrativos: conceito, requisitos e atributos."),
        leaf(5, "Direito Administrativo", "Licitações e contratos."),
    )

    @Test
    fun sameTextWins() {
        assertEquals(2L, OfficialTopicMatcher.topicFor("Português", "Concordância verbal e nominal.", "Julgue o item.", leaves))
    }

    @Test
    fun overlapInsideTheSubject() {
        assertEquals(3L, OfficialTopicMatcher.topicFor("Língua Portuguesa", "Regência do verbo assistir", "Julgue o item.", leaves))
        assertEquals(4L, OfficialTopicMatcher.topicFor("Direito Administrativo", null, "Os atributos do ato administrativo incluem a presunção de legitimidade.", leaves))
    }

    @Test
    fun unknownSubjectFallsBackToOverlapOrFirst() {
        assertEquals(5L, OfficialTopicMatcher.topicFor("Legislação", "Contratos e licitações públicas", "Julgue o item.", leaves))
        assertEquals(1L, OfficialTopicMatcher.topicFor("Raciocínio Lógico", "Proposições", "Julgue o item.", leaves))
    }
}
