package br.com.estudario

import br.com.estudario.domain.OptionShuffle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionShuffleTest {
    private val plain = listOf("Ser reversível", "Resistência a colisões", "Saída variável", "Usar chave pública", "Ser lenta")

    @Test
    fun embaralhaAlternativasComunsEMudaAOrdem() {
        assertTrue(OptionShuffle.canShuffle(plain))
        val order = OptionShuffle.order(plain, 42L)
        assertEquals(plain.indices.toSet(), order.toSet())
        assertNotEquals(plain.indices.toList(), order)
    }

    @Test
    fun naoEmbaralhaQuandoAOrdemFazParteDaQuestao() {
        assertFalse(OptionShuffle.canShuffle(listOf("Certo", "Errado")))
        assertFalse(OptionShuffle.canShuffle(plain.dropLast(1) + "Todas as anteriores"))
        assertFalse(OptionShuffle.canShuffle(plain.dropLast(1) + "Nenhuma das alternativas"))
        assertFalse(OptionShuffle.canShuffle(plain.dropLast(1) + "A e B estão corretas"))
        assertFalse(OptionShuffle.canShuffle(plain.dropLast(1) + "Apenas C"))
        assertEquals(listOf(0, 1), OptionShuffle.order(listOf("Certo", "Errado"), 1L))
    }

    @Test
    fun explicacaoGanhaAsLetrasNovas() {
        // A original C passa a ser mostrada como A, e a original A como C.
        val order = listOf(2, 1, 0, 3, 4)
        val text = "A alternativa C está correta. A letra A erra ao dizer que é reversível.\nB) incorreta."
        val out = OptionShuffle.relabel(text, listOf("A", "B", "C", "D", "E"), order)
        assertEquals("A alternativa A está correta. A letra C erra ao dizer que é reversível.\nB) incorreta.", out)
        assertEquals("A", OptionShuffle.displayLetter(order, 2))
    }
}
