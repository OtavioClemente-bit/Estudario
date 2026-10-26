package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class StudyNotebookRestoreTest {
    private val table = "| Precedência | Operadores |\n|---|---|\n| Curto-circuito | &&, depois \\|\\| |\n| Condicional | ? : |"

    @Test
    fun grifoArrastadoNaTabelaVoltaASerTabela() {
        val flat = "Precedência Operadores Curto-circuito &&, depois || Condicional ? :"
        assertEquals(table, StudyNotebook.restoreQuote(flat, table))
    }

    @Test
    fun grifoDeParagrafoFicaComoEsta() {
        assertEquals("um trecho qualquer de texto", StudyNotebook.restoreQuote("um trecho qualquer de texto", "Parágrafo sem tabela."))
        assertEquals("frase que não está na tabela nenhuma", StudyNotebook.restoreQuote("frase que não está na tabela nenhuma", table))
    }
}
