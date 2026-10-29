package br.com.estudario.domain

import br.com.estudario.domain.EditalSync.CatalogSubject
import br.com.estudario.domain.EditalSync.Subject
import br.com.estudario.domain.EditalSync.Topic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EditalSyncTest {
    private val mine = listOf(
        Subject(1, "Língua Portuguesa", listOf(Topic(10, "Ortografia oficial."), Topic(11, "Acentuação gráfica."), Topic(12, "Emprego do sinal indicativo de crase."), Topic(13, "Meu tópico extra"))),
        Subject(2, "Noções de Informática", listOf(Topic(20, "Windows 10."), Topic(21, "Microsoft Word 2016."))),
    )
    private val old = mapOf(
        "lingua portuguesa" to listOf("Ortografia oficial.", "Acentuação gráfica.", "Emprego do sinal indicativo de crase."),
        "nocoes de informatica" to listOf("Windows 10.", "Microsoft Word 2016."),
    )

    @Test
    fun acrescentaRenomeiaEMarcaOQueSaiuSemTocarNoDaPessoa() {
        val catalog = listOf(
            CatalogSubject("Língua Portuguesa", listOf("Ortografia oficial.", "Emprego do sinal indicativo da crase.", "Pontuação.")),
            CatalogSubject("Noções de Informática", listOf("Windows 10.", "Microsoft Word 2016.", "Microsoft Excel 2016.")),
            CatalogSubject("Raciocínio Lógico", listOf("Proposições.")),
        )
        val p = EditalSync.plan(mine, catalog, old)
        assertEquals(listOf(12L to "Emprego do sinal indicativo da crase."), p.renamedTopics)
        assertEquals(listOf("Pontuação.", "Microsoft Excel 2016."), p.newTopics.map { it.second })
        assertEquals(listOf("Raciocínio Lógico"), p.newSubjects.map { it.name })
        assertEquals(setOf(11L), p.removed) // acentuação saiu; o tópico extra da pessoa (13) não
        assertTrue(13L !in p.inEdital && 13L !in p.removed)
    }

    @Test
    fun primeiraVezSemListaAnteriorNaoMarcaNadaComoSaido() {
        val catalog = listOf(CatalogSubject("Língua Portuguesa", listOf("Ortografia oficial.")), CatalogSubject("Noções de Informática", listOf("Windows 10.")))
        val p = EditalSync.plan(mine, catalog, null)
        assertEquals(emptySet(), p.removed)
        assertEquals(0, p.changes)
    }

    @Test
    fun materiaRenomeadaMantemOsTopicos() {
        val catalog = listOf(
            CatalogSubject("Português", listOf("Ortografia oficial.", "Acentuação gráfica.", "Emprego do sinal indicativo de crase.")),
            CatalogSubject("Noções de Informática", listOf("Windows 10.", "Microsoft Word 2016.")),
        )
        val p = EditalSync.plan(mine, catalog, old)
        assertEquals(listOf(1L to "Português"), p.renamedSubjects)
        assertEquals(emptyList(), p.newSubjects)
        assertEquals(emptySet(), p.removed)
    }

    @Test
    fun editalDePdfComOMesmoNomeNaoEDoCatalogo() {
        val catalog = listOf(CatalogSubject("Língua Portuguesa", listOf("Ortografia oficial.", "Acentuação gráfica.", "Emprego do sinal indicativo de crase.")), CatalogSubject("Noções de Informática", listOf("Windows 10.", "Microsoft Word 2016.")))
        assertTrue(EditalSync.cameFrom(mine, catalog))
        val pdf = listOf(Subject(9, "Português", listOf(Topic(90, "Interpretação de textos"), Topic(91, "Coesão e coerência"), Topic(92, "Ortografia oficial."))))
        assertTrue(!EditalSync.cameFrom(pdf, catalog))
    }

    @Test
    fun voltouAoCatalogoEstaNoEdital() {
        val catalog = listOf(CatalogSubject("Língua Portuguesa", listOf("Ortografia oficial.", "Acentuação gráfica.")), CatalogSubject("Noções de Informática", listOf("Windows 10.", "Microsoft Word 2016.")))
        val p = EditalSync.plan(mine, catalog, old)
        assertTrue(11L in p.inEdital)
        assertEquals(setOf(12L), p.removed)
    }
}
