package br.com.estudario.domain

import br.com.estudario.domain.NotebookSearch.Item
import br.com.estudario.domain.NotebookSearch.Kind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NotebookSearchTest {
    private val items = listOf(
        Item(Kind.TOPIC, "Licitações e contratos", "Direito Administrativo", ref = 1),
        Item(Kind.TOPIC, "Atos administrativos", "Direito Administrativo", ref = 2),
        Item(Kind.TOPIC, "Crase", "Língua Portuguesa", ref = 3),
        Item(Kind.MARK, "A licitação é dispensável", "Licitações e contratos", "A licitação é dispensável nos casos previstos em lei.", ref = 4),
        Item(Kind.NOTE, "Revisar prazos", "Geral", "Revisar os prazos de recurso na licitação.", ref = 5),
        Item(Kind.QUESTION, "Julgue o item sobre crase", "Crase", "Julgue o item: ocorre crase antes de palavra masculina.", ref = 6),
    )

    private fun refs(q: String) = NotebookSearch.search(q, items).map { it.item.ref }

    @Test fun foldTiraAcentoMaiusculaEEspacos() {
        assertEquals("licitacao e acao", NotebookSearch.fold("  LICITAÇÃO   e  Ação "))
        assertEquals("lingua portuguesa", NotebookSearch.fold("Língua Portuguesa"))
    }

    @Test fun termosVaziosNaoBuscam() {
        assertEquals(emptyList(), NotebookSearch.terms("   "))
        assertTrue(NotebookSearch.search(" ", items).isEmpty())
        assertTrue(NotebookSearch.matches(emptyList(), "qualquer"))
    }

    @Test fun semAcentoAchaComAcento() {
        // singular acha plural ("licitacao" → "Licitações"); título antes do texto
        assertEquals(listOf(4, 1, 5), refs("licitacao"))
        assertEquals("licitac", NotebookSearch.root("licitacoes"))
        assertEquals("crase", NotebookSearch.root("crase"))
        assertTrue(3 in refs("LINGUA"))
    }

    @Test fun pedacoDePalavra() {
        // "admin" pega os dois tópicos pela matéria e pelo título
        assertEquals(listOf(2, 1), refs("admin"))
    }

    @Test fun variasPalavrasEmQualquerOrdemTodasPrecisam() {
        // o tópico pelo título; o grifo pelo tópico onde mora
        assertEquals(listOf(1, 4), refs("contratos licita"))
        assertTrue(refs("crase inexistente").isEmpty())
    }

    @Test fun tituloVemAntesDoTexto() {
        val hits = NotebookSearch.search("crase", items)
        assertEquals(3, hits.first().item.ref) // título igual
        assertEquals(setOf(3, 6), hits.map { it.item.ref }.toSet())
    }

    @Test fun agrupaNaOrdemDosGrupos() {
        val groups = NotebookSearch.grouped(NotebookSearch.search("licitacao", items))
        assertEquals(listOf(NotebookSearch.Group.TOPICS, NotebookSearch.Group.MARKS_NOTES), groups.map { it.first })
        assertEquals(listOf(4, 5), groups[1].second.map { it.item.ref }.sorted())
    }

    @Test fun trechoEmVoltaDaPalavra() {
        val long = "Começo do texto " + "palavra ".repeat(30) + "aqui está a Licitação importante " + "fim ".repeat(40)
        val ex = assertNotNull(NotebookSearch.excerpt(long, NotebookSearch.terms("licitacao"), radius = 20))
        assertTrue(ex.contains("Licitação"))
        assertTrue(ex.startsWith("…") && ex.endsWith("…"))
        assertNull(NotebookSearch.excerpt("nada a ver", NotebookSearch.terms("crase")))
    }

    @Test fun matchesJuntaOsTextos() {
        assertTrue(NotebookSearch.matches(NotebookSearch.terms("direito crase"), "Crase", "Direito"))
        assertFalse(NotebookSearch.matches(NotebookSearch.terms("direito crase"), "Crase", null))
    }
}
