package br.com.estudario.data.simulation

import br.com.estudario.data.catalog.OfficialOption
import br.com.estudario.data.catalog.OfficialQuestion
import br.com.estudario.domain.simulation.BlueprintTopic
import org.junit.Assert.assertEquals
import org.junit.Test

class OfficialTopicMatcherTest {
    private fun leaf(id: Long, subject: String, title: String) =
        BlueprintTopic(topicId = id, subjectId = subject.hashCode().toLong(), subjectName = subject, path = listOf(title), weight = 50, studied = false, hasContent = false)

    private fun question(subject: String, topic: String?, statement: String = "Julgue o item a seguir.") =
        OfficialQuestion(n = 1, subject = subject, topic = topic, format = "CERTO_ERRADO", statement = statement, options = listOf(OfficialOption("C", "Certo", true), OfficialOption("E", "Errado", false)), explanation = "x")

    private val leaves = listOf(
        leaf(1, "Língua Portuguesa", "Ortografia oficial."),
        leaf(2, "Língua Portuguesa", "Concordância verbal e nominal."),
        leaf(3, "Língua Portuguesa", "Regência verbal e nominal."),
        leaf(4, "Direito Administrativo", "Atos administrativos: conceito, requisitos e atributos."),
        leaf(5, "Direito Administrativo", "Licitações e contratos."),
    )

    @Test
    fun sameTextWins() {
        assertEquals(2L, OfficialTopicMatcher.topicFor(question("Português", "Concordância verbal e nominal."), leaves))
    }

    @Test
    fun overlapInsideTheSubject() {
        assertEquals(3L, OfficialTopicMatcher.topicFor(question("Língua Portuguesa", "Regência do verbo assistir"), leaves))
        assertEquals(4L, OfficialTopicMatcher.topicFor(question("Direito Administrativo", null, "Os atributos do ato administrativo incluem a presunção de legitimidade."), leaves))
    }

    @Test
    fun unknownSubjectFallsBackToOverlapOrFirst() {
        assertEquals(5L, OfficialTopicMatcher.topicFor(question("Legislação", "Contratos e licitações públicas"), leaves))
        assertEquals(1L, OfficialTopicMatcher.topicFor(question("Raciocínio Lógico", "Proposições"), leaves))
    }
}
