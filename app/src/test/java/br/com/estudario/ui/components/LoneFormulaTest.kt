package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class LoneFormulaTest {
    private val d = "$"

    @Test
    fun formulaSozinhaNaLinhaViraBloco() {
        val entrada = "Calcule:\n${d}${d}x^2 + 1${d}${d}\nE responda."
        assertEquals("Calcule:\n\n${d}${d}\nx^2 + 1\n${d}${d}\n\nE responda.", centerLoneFormulas(entrada))
    }

    @Test
    fun formulaNoMeioDaFraseFicaNaLinha() {
        val entrada = "O valor de ${d}${d}x${d}${d} é positivo."
        assertEquals(entrada, centerLoneFormulas(entrada))
    }

    @Test
    fun blocoDeCodigoNaoMexe() {
        val entrada = "```grafico\n${d}${d}x${d}${d}\n```"
        assertEquals(entrada, centerLoneFormulas(entrada))
    }
}
