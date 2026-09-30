package br.com.estudario.data.catalog

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.transfer.EstudoPackageParser
import br.com.estudario.data.transfer.allTopics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContestCatalogTest {
    private val competition = CompetitionEntity(id = 1, name = "PMMG")
    private val subjects = listOf(
        SubjectEntity(id = 2, competitionId = 1, name = "Língua Portuguesa", position = 0),
        SubjectEntity(id = 3, competitionId = 1, name = "Direitos Humanos", position = 1),
        SubjectEntity(id = 9, competitionId = 99, name = "De outro concurso"),
    )
    private val topics = listOf(
        TopicEntity(id = 10, subjectId = 2, title = "Crase", position = 0),
        TopicEntity(id = 11, subjectId = 2, title = "Crase antes de masculino", parentTopicId = 10),
        TopicEntity(id = 12, subjectId = 3, title = "Declaração Universal"),
    )

    @Test
    fun pacoteDoCatalogoLevaSoAEstruturaDoConcursoEOImportadorAceita() {
        val estudo = CatalogEstudo.build(competition, subjects, topics, "PMMG - Soldado (2026)")
        val plan = EstudoPackageParser.parse(estudo.toString())
        assertEquals(listOf("Língua Portuguesa", "Direitos Humanos"), CatalogEstudo.subjectNames(estudo))
        assertEquals(3, plan.allTopics().size)
        assertTrue(plan.allTopics().all { it.theories.isEmpty() && it.questions.isEmpty() })
    }

    @Test
    fun cadaEnvioGanhaIdsProprios() {
        val a = CatalogEstudo.build(competition, subjects, topics, "A").getString("packageId")
        val b = CatalogEstudo.build(competition, subjects, topics, "B").getString("packageId")
        assertTrue(a != b)
    }

    @Test
    fun comparacaoConfereQuandoMateriasEAnoEstaoNoPdf() {
        val pdf = "EDITAL 2026 ANEXO II CONTEÚDO PROGRAMÁTICO LINGUA PORTUGUESA: crase. DIREITOS HUMANOS: declaração."
        val result = EditalComparison.compare(listOf("Língua Portuguesa", "Direitos Humanos"), 2026, pdf)
        assertTrue(result.matches)
    }

    @Test
    fun comparacaoApontaMateriaQueNaoEstaNoPdfEAnoDiferente() {
        val pdf = "EDITAL 2024 LÍNGUA PORTUGUESA: crase."
        val result = EditalComparison.compare(listOf("Língua Portuguesa", "Direito Penal Militar"), 2026, pdf)
        assertFalse(result.matches)
        assertEquals(listOf("Direito Penal Militar"), result.missing)
        assertFalse(result.yearInPdf)
    }

    @Test
    fun editalDeAnoAnteriorAparecePossivelmenteDesatualizado() {
        val contest = CatalogContest("id", "PMMG", "Soldado", null, null, 2024, null, 5, 40)
        assertTrue(contest.possiblyOutdated(2026))
        assertFalse(contest.copy(year = 2026).possiblyOutdated(2026))
    }
}
