package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReviewMixTest {
    private val deck = (1..12).map { MixSource("Conceito $it", "Explicação $it") }
    private val prompts = (1..6).map { MixSource("Pergunta $it?", "Resposta $it") }
    private val quotes = listOf(
        "O prazo para interpor o recurso é de 15 dias, contados da intimação da decisão.",
        "A responsabilidade do Estado é objetiva, fundada na teoria do risco administrativo.",
    )

    @Test fun mixesTheThreeSources() {
        val cards = ReviewMix.cards(deck, prompts, quotes, count = 8, stage = 1, seed = 1)
        assertEquals(8, cards.size)
        assertEquals(setOf(MixKind.CARD, MixKind.RECALL, MixKind.CLOZE), cards.map { it.kind }.toSet())
        assertEquals(8, cards.map { it.key }.distinct().size)
    }

    @Test fun fillsFromWhatTheTopicHas() {
        val cards = ReviewMix.cards(deck, emptyList(), emptyList(), count = 8, stage = 1, seed = 1)
        assertEquals(8, cards.size)
        assertTrue(cards.all { it.kind == MixKind.CARD })
        assertEquals(2, ReviewMix.cards(emptyList(), prompts.take(2), emptyList(), 8, 1, 1).size)
    }

    @Test fun rotatesCardsBetweenStages() {
        val first = ReviewMix.cards(deck, prompts, emptyList(), 6, stage = 1, seed = 1).map { it.key }.toSet()
        val second = ReviewMix.cards(deck, prompts, emptyList(), 6, stage = 2, seed = 1).map { it.key }.toSet()
        assertNotEquals(first, second)
    }

    @Test fun forgottenCardsComeBackFirst() {
        val hard = setOf(ReviewMix.key(MixKind.CARD, "Conceito 11"))
        val cards = ReviewMix.cards(deck, prompts, emptyList(), 6, stage = 1, seed = 1, hard = hard)
        assertEquals("Conceito 11", cards.first().front)
        assertTrue(cards.first().hard)
        val next = ReviewMix.nextHard(hard, cards, forgotten = setOf(cards.last().key))
        assertEquals(setOf(cards.last().key), next)
    }

    @Test fun clozeHidesTheNumberOrTheLongestWord() {
        val number = ReviewMix.cloze(quotes[0])!!
        assertEquals("15", number.answer)
        assertTrue(ReviewMix.GAP in number.text && "15" !in number.text)
        assertEquals("responsabilidade", ReviewMix.cloze("Aqui a responsabilidade do Estado é sempre objetiva e direta.")!!.answer)
        assertNull(ReviewMix.cloze("Curto demais."))
        assertNull(ReviewMix.cloze("| a | b | tabela não vira lacuna de jeito nenhum, nem com 15 dias"))
    }

    @Test fun pointsRotateByStage() {
        assertEquals(6, ReviewMix.points(deck, 1).size)
        assertNotEquals(ReviewMix.points(deck, 1), ReviewMix.points(deck, 2))
    }
}
