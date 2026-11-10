package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReviewMixTest {
    // Dez ideias diferentes no baralho; as três primeiras também aparecem na memorização ativa, com outras palavras.
    private val deck = listOf(
        MixSource("O que é risco residual?", "Exposição que permanece depois dos controles aplicados."),
        MixSource("Para que serve cobertura morta?", "Reduz evaporação e limita plantas invasoras no canteiro."),
        MixSource("Hash garante confidencialidade?", "Não. Garante integridade; quem esconde é a cifra."),
        MixSource("Prazo do recurso de apelação?", "Quinze dias úteis contados da intimação."),
        MixSource("Quem julga o governador por crime comum?", "O Superior Tribunal de Justiça."),
        MixSource("O que é efeito avalanche?", "Um bit alterado na entrada muda boa parte do resumo."),
        MixSource("Princípio da anterioridade tributária?", "Tributo novo só pode ser cobrado no exercício seguinte."),
        MixSource("Quórum para emenda constitucional?", "Três quintos, em dois turnos, nas duas casas."),
        MixSource("Diferença entre dolo e culpa?", "No dolo há vontade do resultado; na culpa, descuido."),
        MixSource("O que é licitação dispensável?", "A lei autoriza contratar direto, mas competir seria possível."),
    )
    private val prompts = listOf(
        MixSource("Explique a diferença entre risco inerente e risco residual.", "O inerente é sem controles; o residual é o que permanece depois dos controles."),
        MixSource("Como a cobertura morta auxilia um canteiro?", "Reduz a evaporação e a emergência de invasoras."),
        MixSource("Por que hash não garante confidencialidade?", "Porque só comprova integridade; esconder é papel da cifra."),
        MixSource("Quais são as fases do orçamento público?", "Elaboração, aprovação, execução e controle."),
    )

    @Test fun sameIdeaNeverShowsTwiceInOneSession() {
        // 10 ideias no baralho + 1 só na memorização ativa = 11 ideias; pedir 30 traz 11, não 14.
        val cards = ReviewMix.cards(deck, prompts, emptyList(), count = 30, stage = 1, seed = 1)
        assertEquals(11, cards.size)
        cards.forEachIndexed { i, a -> cards.drop(i + 1).forEach { b -> assertFalse(ReviewMix.similar(a, b), "${a.front} / ${b.front}") } }
        assertEquals(setOf(MixKind.CARD, MixKind.RECALL), cards.map { it.kind }.toSet())
    }

    @Test fun nextStageBringsTheOtherWording() {
        val first = ReviewMix.cards(deck, prompts, emptyList(), 30, stage = 1, seed = 1).first { "residual" in it.front }
        val second = ReviewMix.cards(deck, prompts, emptyList(), 30, stage = 2, seed = 1).first { "residual" in it.front }
        assertNotEquals(first.kind, second.kind)
    }

    @Test fun rotatesIdeasBetweenStages() {
        val first = ReviewMix.cards(deck, prompts, emptyList(), 5, stage = 1, seed = 1).map { it.key }.toSet()
        val second = ReviewMix.cards(deck, prompts, emptyList(), 5, stage = 2, seed = 1).map { it.key }.toSet()
        assertEquals(5, first.size)
        assertTrue(first.intersect(second).isEmpty())
    }

    @Test fun fillsFromWhatTheTopicHas() {
        assertEquals(8, ReviewMix.cards(deck, emptyList(), emptyList(), 8, 1, 1).size)
        assertEquals(2, ReviewMix.cards(emptyList(), prompts.take(2), emptyList(), 8, 1, 1).size)
        assertTrue(ReviewMix.cards(emptyList(), emptyList(), emptyList(), 8, 1, 1).isEmpty())
    }

    @Test fun forgottenCardsComeBackFirst() {
        val hard = setOf(ReviewMix.key(MixKind.CARD, "Quórum para emenda constitucional?"))
        val cards = ReviewMix.cards(deck, prompts, emptyList(), 5, stage = 1, seed = 1, hard = hard)
        assertEquals("Quórum para emenda constitucional?", cards.first().front)
        assertTrue(cards.first().hard)
        assertEquals(1, cards.count { "emenda" in it.front })
        val next = ReviewMix.nextHard(hard, cards, forgotten = setOf(cards.last().key))
        assertEquals(setOf(cards.last().key), next)
    }

    @Test fun warmupPointsAreNotTheSessionCards() {
        val cards = ReviewMix.cards(deck, prompts, emptyList(), 5, stage = 1, seed = 1)
        val points = ReviewMix.points(deck, prompts, cards, stage = 1)
        assertEquals(6, points.size)
        points.forEach { point -> assertTrue(cards.none { ReviewMix.similar(it, MixCard(MixKind.CARD, point, it.back, "x")) && it.front == point }, point) }
        assertTrue(cards.map { it.front }.intersect(points.toSet()).isEmpty())
    }

    @Test fun clozeHidesTheNumberOrTheLongestWord() {
        val number = ReviewMix.cloze("O prazo para interpor o recurso é de 15 dias, contados da intimação da decisão.")!!
        assertEquals("15", number.answer)
        assertTrue(ReviewMix.GAP in number.text && "15" !in number.text)
        assertEquals("responsabilidade", ReviewMix.cloze("Aqui a responsabilidade do Estado é sempre objetiva e direta.")!!.answer)
        assertNull(ReviewMix.cloze("Curto demais."))
    }
}
