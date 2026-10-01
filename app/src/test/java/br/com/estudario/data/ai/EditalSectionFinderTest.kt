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

    @Test
    fun anexoComLetraEntreAspasDaPmmg() {
        val pages = listOf(
            "Edital DRH/CRS nº 11/2026 CFO/2027\nCONCURSO PÚBLICO ${filler(100)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\nSUMÁRIO\nANEXO “B” - PROGRAMA DE MATÉRIAS ............ 95\nANEXO “C” - CIDADES ........ 104",
            "Edital DRH/CRS nº 11/2026 CFO/2027\n7.8 Disciplinas da prova ${filler(300)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\nANEXO “A” - CALENDÁRIO DE ATIVIDADES ${filler(200)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\nANEXO “B” - PROGRAMA DE MATÉRIAS\n1. LÍNGUA PORTUGUESA ${filler(300)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\n4. DIREITO CONSTITUCIONAL ${filler(300)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\nANEXO “C” - CIDADES DE OPÇÃO ${filler(200)}",
            "Edital DRH/CRS nº 11/2026 CFO/2027\nANEXO “D” - MODELO DE DECLARAÇÃO ${filler(200)}",
        )
        val result = EditalSectionFinder.select(pages)!!
        assertEquals("1, 5-6", result.pages)
        assertTrue(result.focused)
        assertFalse(result.text.contains("CIDADES DE OPÇÃO"))
    }

    @Test
    fun anexoQueComecaNoMeioDaPaginaNaoLevaAsRegras() {
        val pages = listOf(
            "Capa ${filler(100)}",
            "19.9 Dos dados pessoais ${filler(200)}\nANEXO I \u2013 CONTEÚDO PROGRAMÁTICO\nCONHECIMENTOS BÁSICOS\nLÍNGUA PORTUGUESA ${filler(300)}",
            "NOÇÕES DE DIREITO ADMINISTRATIVO ${filler(300)}\nANEXO II - REQUISITOS E ATRIBUIÇÕES DO CARGO ${filler(100)}",
        )
        val result = EditalSectionFinder.select(pages)!!
        assertEquals("1-3", result.pages)
        assertFalse(result.text.contains("Dos dados pessoais"))
        assertFalse(result.text.contains("REQUISITOS"))
        assertTrue(result.text.contains("DIREITO ADMINISTRATIVO"))
    }

    @Test
    fun editaisReais() {
        val dir = java.io.File(System.getenv("EDITAIS_DIR") ?: "none")
        if (!dir.exists()) return
        (1..4).forEach { n ->
            val pages = java.io.File(dir, "e$n.txt").readText().split('\u000c').dropLast(1)
            val r = EditalSectionFinder.select(pages)!!
            println("REAL e$n pages=${r.pages} focused=${r.focused} chars=${r.text.length} start=${r.text.substringAfter("---\n").lines().drop(1).take(2)} end=${r.text.takeLast(120).replace('\n', ' ')}")
        }
    }
}
