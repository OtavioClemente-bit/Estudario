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
    fun grifoDoSiteComCodigoEVirgulaVoltaASerTabela() {
        val java = "Texto antes.\n\n| Precedência aproximada, da maior à menor | Exemplos e observações |\n|---|---|\n| Pós-fixados e acesso | `x++`, `a[i]`, `obj.m()` |\n| Unários | `++x`, `!`, `~`, casts pertinentes |\n| Deslocamentos | `<<`, `>>`, `>>>` |\n| Curto-circuito | `&&`, depois `\\|\\|` |\n| Atribuição | `=`, `+=`, `-=`, entre outras |"
        val blocks = java.split("\n\n")
        val flat = "Precedência aproximada, da maior à menor\tExemplos e observações\nPós-fixados e acesso\tx++, a[i], obj.m()\nUnários\t++x, !, ~, casts pertinentes\nDeslocamentos\t<<, >>, >>>\nCurto-circuito\t&&, depois ||\nAtribuição\t=, +=, -=, entre outras"
        assertEquals(blocks[1].trim(), StudyNotebook.restoreQuote(flat, blocks, 0))
    }

    @Test
    fun grifoDeParagrafoFicaComoEsta() {
        assertEquals("um trecho qualquer de texto", StudyNotebook.restoreQuote("um trecho qualquer de texto", "Parágrafo sem tabela."))
        assertEquals("frase que não está na tabela nenhuma", StudyNotebook.restoreQuote("frase que não está na tabela nenhuma", table))
    }
}
