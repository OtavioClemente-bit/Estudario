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
 * A mistura de cartões de uma revisão espaçada.
 *
 * O material traz flashcards e perguntas de memorização ativa, e os dois costumam cobrar a mesma
 * coisa com outras palavras ("O que é risco residual?" e "Explique a diferença entre risco inerente
 * e residual"). Passar os dois na mesma sessão dá a sensação de cartão repetido. Por isso os cartões
 * são agrupados por IDEIA: de cada ideia entra um cartão só por revisão, e a cada etapa a mesma ideia
 * volta com a outra formulação. As ideias giram a cada etapa e as que a pessoa esqueceu na revisão
 * anterior vêm primeiro.
 */
object ReviewMix {
    const val GAP = "[ … ]"
    private const val HARD_LIMIT = 40
    private const val GROUP_LIMIT = 4

    fun key(kind: MixKind, front: String): String = "${kind.name.first()}${front.trim().lowercase().hashCode()}"

    // ------------------------------------------------------------------ cartões parecidos

    private val STOP = setOf(
        "que", "qual", "quais", "como", "para", "por", "com", "sem", "uma", "uns", "umas", "dos", "das", "nos", "nas",
        "entre", "sobre", "quando", "onde", "isso", "esse", "essa", "este", "esta", "seu", "sua", "seus", "suas", "sao",
        "ser", "foi", "tem", "ter", "nao", "sim", "mais", "menos", "pelo", "pela", "pelos", "pelas", "ele", "ela", "num",
        "numa", "cada", "todo", "toda", "pode", "deve", "caso", "explique", "defina", "cite", "diferenca", "exemplo",
    )
    private val SPLIT = Regex("[^a-z0-9]+")

    private fun fold(c: Char): Char = when (c) {
        'á', 'à', 'â', 'ã', 'ä' -> 'a'
        'é', 'ê', 'è' -> 'e'
        'í', 'ï' -> 'i'
        'ó', 'ô', 'õ' -> 'o'
        'ú', 'ü' -> 'u'
        'ç' -> 'c'
        else -> c
    }

    /** As palavras que carregam o assunto: sem acento, sem palavras de ligação, só o começo de cada uma (pega plural e flexão). */
    internal fun tokens(text: String): Set<String> =
        buildString { text.lowercase().forEach { append(fold(it)) } }.split(SPLIT)
            .filter { (it.length > 2 || it.any(Char::isDigit)) && it !in STOP }
            .mapTo(linkedSetOf()) { it.take(6) }

    private fun overlap(a: Set<String>, b: Set<String>, min: Int): Double =
        if (a.size < min || b.size < min) 0.0 else a.count { it in b }.toDouble() / minOf(a.size, b.size)

    private class Entry(val card: MixCard) {
        val front = tokens(card.front)
        val all = tokens(card.front + " " + card.back)
    }

    /** Dois cartões cobram a mesma ideia: as perguntas se parecem, ou pergunta e resposta juntas se parecem. */
    private fun sameIdea(a: Entry, b: Entry): Boolean =
        a.card.front.equals(b.card.front, ignoreCase = true) || overlap(a.front, b.front, 2) >= 0.6 || overlap(a.all, b.all, 4) >= 0.65

    /** Cartões parecidos são a mesma ideia com outras palavras (para a tela não mostrar os dois na mesma sessão). */
    fun similar(a: MixCard, b: MixCard): Boolean = sameIdea(Entry(a), Entry(b))

    /** Os cartões agrupados por ideia, alternando baralho e memorização ativa na ordem do material. */
    private fun ideas(deck: List<MixSource>, prompts: List<MixSource>): List<List<MixCard>> {
        fun pool(kind: MixKind, items: List<MixSource>) = items
            // o baralho às vezes traz a marca de citação do resumo ("> termo"): ela não faz parte do cartão
            .map { it.copy(front = it.front.trim().trimStart('>', ' ')) }
            .filter { it.front.isNotBlank() && it.back.isNotBlank() }
            .map { MixCard(kind, it.front, it.back.trim(), key(kind, it.front)) }
            .distinctBy { it.key }
        val cards = pool(MixKind.CARD, deck)
        val asked = pool(MixKind.RECALL, prompts)
        val ordered = (0 until maxOf(cards.size, asked.size)).flatMap { i -> listOfNotNull(cards.getOrNull(i), asked.getOrNull(i)) }
        val groups = mutableListOf<MutableList<Entry>>()
        ordered.forEach { card ->
            val entry = Entry(card)
            val home = groups.firstOrNull { group -> group.size < GROUP_LIMIT && group.any { sameIdea(it, entry) } }
            if (home != null) home += entry else groups += mutableListOf(entry)
        }
        return groups.map { group -> group.map { it.card } }
    }

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
        val turn = stage.coerceAtLeast(1) - 1
        val clozes = quotes.mapNotNull { quote -> cloze(quote)?.let { listOf(MixCard(MixKind.CLOZE, it.text, "**${it.answer}**\n\n${quote.trim()}", key(MixKind.CLOZE, quote))) } }.distinctBy { it.first().key }
        val groups = ideas(deck, prompts) + clozes
        if (groups.isEmpty()) return emptyList()
        // As ideias esquecidas na última entram primeiro, com o mesmo cartão que ela não lembrou.
        val (weak, others) = groups.partition { group -> group.any { it.key in hard } }
        val weakCards = weak.map { group -> group.first { it.key in hard }.copy(hard = true) }
        // As outras giram a cada etapa; dentro da ideia, a formulação também troca (flashcard numa, pergunta de memória na outra).
        val shift = if (others.isEmpty()) 0 else (turn * count) % others.size
        val rotated = (others.drop(shift) + others.take(shift)).map { group -> group[turn % group.size] }
        // Última checagem: mesmo em ideias diferentes, não entra cartão parecido com um que já está na sessão.
        val picked = mutableListOf<MixCard>()
        val signed = mutableListOf<Entry>()
        for (card in weakCards + rotated) {
            if (picked.size >= count) break
            val entry = Entry(card)
            if (signed.any { sameIdea(it, entry) }) continue
            picked += card
            signed += entry
        }
        val (first, rest) = picked.partition { it.hard }
        return first + if (shuffle) rest.shuffled(Random(seed)) else rest
    }

    /**
     * Os pontos-chave do aquecimento "de cabeça": ideias do tópico que NÃO viram cartão nesta sessão
     * (senão a pessoa via a pergunta no aquecimento e de novo no cartão). Giram a cada etapa.
     */
    fun points(deck: List<MixSource>, prompts: List<MixSource>, shown: List<MixCard>, stage: Int, max: Int = 6): List<String> {
        val used = shown.mapTo(hashSetOf()) { it.key }
        val fronts = ideas(deck, prompts).filter { group -> group.none { it.key in used } }
            .map { group -> group.minBy { it.front.length }.front }.filter { it.length <= 90 }.distinct()
        if (fronts.size <= max) return fronts
        val shift = ((stage.coerceAtLeast(1) - 1) * max) % fronts.size
        return (fronts.drop(shift) + fronts.take(shift)).take(max)
    }

    /** O estudo rápido de uma revisão: o essencial em pontos curtos, as dicas de prova e as pegadinhas. */
    data class QuickStudy(val points: List<String>, val tips: List<String>, val traps: List<String>) {
        val isEmpty get() = points.isEmpty() && tips.isEmpty() && traps.isEmpty()
    }

    /**
     * Monta o estudo rápido com o que o tópico tem, em pedaços que dá para ler em um minuto: o resumo
     * quebrado em pontos (antes ia o resumo cru, num parágrafo só), dicas e pegadinhas. As dicas e as
     * pegadinhas giram a cada etapa, para a leitura não ser sempre a mesma.
     */
    fun quickStudy(summary: String?, tips: List<String>, traps: List<String>, stage: Int): QuickStudy {
        fun turn(items: List<String>, max: Int): List<String> {
            val good = items.map { it.trim() }.filter { it.length in 15..420 }.distinct()
            if (good.size <= max) return good
            val shift = ((stage.coerceAtLeast(1) - 1) * max) % good.size
            return (good.drop(shift) + good.take(shift)).take(max)
        }
        return QuickStudy(StudyNotebook.summaryLines(summary, max = 8), turn(tips, 4), turn(traps, 3))
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
