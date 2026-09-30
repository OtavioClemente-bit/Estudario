package br.com.estudario.data.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditalSectionFinderTest {
    private fun filler(words: Int) = List(words) { "regra" }.joinToString(" ")

    /** Edital típico: regras, sumário que cita o anexo, anexo II de conteúdo e anexo III do teste físico. */
    private val edital = listOf(
        "EDITAL Nº 10/2026 CURSO DE FORMAÇÃO DE SOLDADOS ${filler(50)}",
        "1 DAS INSCRIÇÕES ${filler(300)} conforme o conteúdo programático do Anexo II ${filler(50)}",
        "2 DA PROVA ${filler(300)}",
        "ANEXO I \u2013 CRONOGRAMA ${filler(200)}",
        "ANEXO II \u2013 CONTEÚDO PROGRAMÁTICO\nLÍNGUA PORTUGUESA: Compreensão de textos. ${filler(250)}",
        "MATEMÁTICA: Frações e porcentagem. ${filler(250)}",
        "ANEXO III \u2013 TABELA E CRITÉRIOS PARA APLICAÇÃO DO TCF ${filler(250)}",
        "ANEXO IV \u2013 MODELO DE REQUERIMENTO ${filler(250)}",
    )

    @Test
    fun enviaSoACapaEOAnexoDeConteudo() {
        val result = EditalSectionFinder.select(edital)!!
        assertEquals("1, 5-6", result.pages)
        assertTrue(result.focused)
        assertEquals(8, result.totalPages)
        assertTrue(result.text.contains("LÍNGUA PORTUGUESA"))
        assertTrue(result.text.contains("MATEMÁTICA"))
        assertFalse(result.text.contains("TCF"))
        assertFalse(result.text.contains("CRONOGRAMA"))
    }

    @Test
    fun citacaoDoAnexoNoMeioDasRegrasNaoEOInicio() {
        // A página 2 cita "conteúdo programático" no meio do texto: não é o anexo.
        assertEquals("1, 5-6", EditalSectionFinder.select(edital)!!.pages)
    }

    @Test
    fun semConteudoProgramaticoMandaOTextoTodo() {
        val result = EditalSectionFinder.select(listOf("Edital ${filler(300)}", "Regras ${filler(300)}"))!!
        assertEquals("1-2", result.pages)
        assertFalse(result.focused)
    }

    @Test
    fun conteudoAteOFimDoArquivo() {
        val pages = listOf("Capa ${filler(100)}", "CONTEÚDOS PROGRAMÁTICOS\nDIREITO ${filler(300)}", "INFORMÁTICA ${filler(300)}")
        assertEquals("1-3", EditalSectionFinder.select(pages)!!.pages)
    }

    @Test
    fun marcaAsPaginasNoTexto() {
        val text = EditalSectionFinder.select(edital)!!.text
        assertTrue(text.contains("--- Página 5 ---"))
    }

    @Test
    fun intervalosDePaginas() {
        assertEquals("1, 51-54, 60", EditalSectionFinder.ranges(listOf(1, 51, 52, 53, 54, 60)))
    }
}
