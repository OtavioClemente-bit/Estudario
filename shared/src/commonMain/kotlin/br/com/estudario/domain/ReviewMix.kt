package br.com.estudario.domain

import kotlin.random.Random

/** De onde veio o cartão da revisão: do baralho de flashcards, da memorização ativa ou de um grifo da pessoa. */
enum class MixKind { CARD, RECALL, CLOZE }

/** Frente e verso de um cartão do material (flashcard do baralho ou pergunta de memorização ativa). */
data class MixSource(val front: String, val back: String)

/** Um cartão da revisão. [hard]: a pessoa não lembrou dele na revisão anterior, então ele volta primeiro. */
data class MixCard(val kind: MixKind, val front: String, val back: String, val key: String, val hard: Boolean = false)

/** Um grifo com uma lacuna: [text] é o trecho com o buraco e [answer] é o que foi tirado. */
data class Cloze(val text: String, val answer: String)

/**
 * A mistura de cartões de uma revisão espaçada. Antes a revisão só usava as perguntas de memorização
 * ativa, sempre as mesmas. Agora cada revisão mistura três fontes (o baralho de flashcards, a
 * memorização ativa e os grifos da própria pessoa, com uma lacuna), troca os cartões a cada etapa
 * e traz primeiro os que a pessoa esqueceu na revisão anterior.
 */
object ReviewMix {
    const val GAP = "[ … ]"
    private const val HARD_LIMIT = 40

    fun key(kind: MixKind, front: String): String = "${kind.name.first()}${front.trim().lowercase().hashCode()}"

    fun cards(
        deck: List<MixSource>,
        prompts: List<MixSource>,
        quotes: List<String>,
        count: Int,
        stage: Int,
        seed: Long,
        shuffle: Boolean = true,
        hard: Set<String> = emptySet(),
    ): List<MixCard> {
        if (count <= 0) return emptyList()
        fun pool(kind: MixKind, items: List<MixSource>) = items
            .filter { it.front.isNotBlank() && it.back.isNotBlank() }
            // o baralho às vezes traz a marca de citação do resumo ("> termo"): ela não faz parte do cartão
            .map { it.copy(front = it.front.trim().trimStart('>', ' ')) }
            .filter { it.front.isNotBlank() }
            .map { MixCard(kind, it.front, it.back.trim(), key(kind, it.front)) }
            .distinctBy { it.key }
        val deckPool = pool(MixKind.CARD, deck)
        val promptPool = pool(MixKind.RECALL, prompts)
        val clozePool = quotes.mapNotNull { quote -> cloze(quote)?.let { MixCard(MixKind.CLOZE, it.text, "**${it.answer}**\n\n${quote.trim()}", key(MixKind.CLOZE, quote)) } }.distinctBy { it.key }

        // Até um quarto da revisão vem dos grifos; o resto se divide entre baralho e memorização ativa.
        var clozeQuota = if (count >= 3) minOf(clozePool.size, maxOf(1, count / 4)) else 0
        val rest = count - clozeQuota
        var deckQuota = minOf(deckPool.size, (rest + 1) / 2)
        val promptQuota = minOf(promptPool.size, rest - deckQuota)
        deckQuota = minOf(deckPool.size, rest - promptQuota)
        clozeQuota = minOf(clozePool.size, count - deckQuota - promptQuota)

        // Os esquecidos na última entram primeiro; os outros giram a cada etapa, para não repetir sempre os mesmos.
        fun pick(pool: List<MixCard>, quota: Int): List<MixCard> {
            if (quota <= 0 || pool.isEmpty()) return emptyList()
            val (weak, others) = pool.partition { it.key in hard }
            val shift = if (others.isEmpty()) 0 else ((stage.coerceAtLeast(1) - 1) * quota) % others.size
            val rotated = others.drop(shift) + others.take(shift)
            return (weak.map { it.copy(hard = true) } + rotated).take(quota)
        }
        val picked = pick(deckPool, deckQuota) + pick(promptPool, promptQuota) + pick(clozePool, clozeQuota)
        val (weak, others) = picked.partition { it.hard }
        val mixed = if (shuffle) others.shuffled(Random(seed)) else interleave(others)
        return weak + mixed
    }

    /** Sem embaralhar: um de cada tipo por vez, na ordem do material. */
    private fun interleave(cards: List<MixCard>): List<MixCard> {
        val groups = MixKind.entries.map { kind -> cards.filter { it.kind == kind } }
        val size = groups.maxOfOrNull { it.size } ?: 0
        return (0 until size).flatMap { i -> groups.mapNotNull { it.getOrNull(i) } }
    }

    /** Os pontos-chave do aquecimento "de cabeça": os títulos do baralho, girando a cada etapa. */
    fun points(deck: List<MixSource>, stage: Int, max: Int = 6): List<String> {
        val fronts = deck.map { it.front.trim().trimStart('>', ' ') }.filter { it.isNotBlank() && it.length <= 90 }.distinct()
        if (fronts.size <= max) return fronts
        val shift = ((stage.coerceAtLeast(1) - 1) * max) % fronts.size
        return (fronts.drop(shift) + fronts.take(shift)).take(max)
    }

    /** O que fica marcado como difícil para a próxima revisão: sai o que apareceu e foi lembrado, entra o que foi esquecido. */
    fun nextHard(previous: Set<String>, shown: List<MixCard>, forgotten: Set<String>): Set<String> {
        val seen = shown.mapTo(hashSetOf()) { it.key }
        return (previous.filter { it !in seen } + forgotten).distinct().takeLast(HARD_LIMIT).toSet()
    }

    /**
     * Abre uma lacuna num grifo: tira o primeiro número (prazo, artigo, quantidade: o que a prova troca)
     * ou, sem número, a palavra mais longa. Trechos curtos demais, longos demais ou com tabela ficam de fora.
     */
    fun cloze(quote: String): Cloze? {
        val text = quote.trim()
        if (text.length !in 30..320 || '|' in text || '\n' in text) return null
        // palavras: letras e dígitos seguidos; número pode ter ponto ou vírgula no meio (1.000, 2,5)
        val words = mutableListOf<IntRange>()
        var i = 0
        while (i < text.length) {
            if (!text[i].isLetterOrDigit()) { i++; continue }
            var j = i
            while (j < text.length && (text[j].isLetterOrDigit() || ((text[j] == '.' || text[j] == ',') && j > i && text[j - 1].isDigit() && j + 1 < text.length && text[j + 1].isDigit()))) j++
            words += i until j
            i = j
        }
        if (words.size < 5) return null
        val number = words.firstOrNull { range -> text[range.first].isDigit() }
        val target = number ?: words.drop(1).filter { it.count() >= 7 }.maxByOrNull { it.count() } ?: return null
        return Cloze(text.replaceRange(target, GAP), text.substring(target))
    }
}
