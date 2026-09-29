package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlashcardParserTest {
    @Test
    fun listaComTermoEmNegritoViraFrenteEVerso() {
        val cards = FlashcardParser.parse("- **Crase**: fusão da preposição a com o artigo a.\n- **Antes de masculino**: não ocorre.")
        assertEquals(listOf("Crase", "Antes de masculino"), cards.map { it.front })
        assertEquals("fusão da preposição a com o artigo a.", cards.first().back)
    }

    @Test
    fun tabelaViraUmCartaoPorLinhaComCabecalhoNoVerso() {
        val cards = FlashcardParser.parse("| Regime | Fórmula |\n|---|---|\n| Simples | \$\$J = Cit\$\$ |\n| Composto | \$\$M = C(1+i)^t\$\$ |")
        assertEquals(listOf("Simples", "Composto"), cards.map { it.front })
        assertTrue(cards.first().back.contains("**Fórmula**"))
    }

    @Test
    fun secoesComTituloViramCartoes() {
        val cards = FlashcardParser.parse("## Conceito\nTexto do conceito.\n\n## Exceção\nQuando não se aplica.")
        assertEquals(2, cards.size)
        assertEquals("Exceção", cards[1].front)
        assertEquals("Quando não se aplica.", cards[1].back)
    }

    @Test
    fun paragrafoSemTermoViraCartaoDeLeitura() {
        val cards = FlashcardParser.parse("Revise os prazos antes da prova.")
        assertEquals(1, cards.size)
        assertEquals("", cards.single().back)
    }
}
