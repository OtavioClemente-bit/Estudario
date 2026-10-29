package br.com.estudario.domain

import kotlin.random.Random

/**
 * O Caderno de estudo que se escreve sozinho: tudo o que a pessoa já estudou vira página (o resumo
 * em frases curtas, os grifos, as dicas, as pegadinhas, os cartões e as questões que ela errou). A
 * "Folheada do dia" separa poucas páginas do que está esfriando. Igual no app e no site.
 *
 * O que é da pessoa (grifo, nota, erro) vem antes do que veio da biblioteca, e o que veio da
 * biblioteca só entra se passar num mínimo (cartão com resposta de verdade, frase de tamanho bom).
 */
object StudyNotebook {
    /** Um tópico já estudado, com o que ele tem. Datas em epoch ms. */
    data class Topic(
        val id: Long,
        val subjectId: Long,
        val title: String,
        val status: String,
        val lastSeenAt: Long?,
        val summary: String? = null,
        val tips: List<String> = emptyList(),
        val traps: List<String> = emptyList(),
        val recall: List<Pair<String, String>> = emptyList(),
        val marks: List<Mark> = emptyList(),
        /** Questões erradas ainda para refazer (ids). */
        val wrongQuestions: List<Long> = emptyList(),
        val notes: List<String> = emptyList(),
    )

    data class Mark(val quote: String, val note: String, val createdAt: Long)

    enum class Firmness { FIRM, MID, COLD }

    /** Firme: revisto há pouco e sem erro pendente. Esfriando: muito tempo sem ver ou com erros para refazer. */
    fun firmness(topic: Topic, now: Long): Firmness {
        val days = topic.lastSeenAt?.let { ((now - it) / DAY).toInt() } ?: 999
        return when {
            topic.wrongQuestions.size >= 2 || days > 30 -> Firmness.COLD
            topic.status == "DOMINADO" || (topic.status == "REVISANDO" && days <= 14) || (days <= 7 && topic.wrongQuestions.isEmpty()) -> Firmness.FIRM
            else -> Firmness.MID
        }
    }

    sealed interface Page {
        val topicId: Long
        data class Key(override val topicId: Long, val title: String, val lines: List<String>) : Page
        data class MarkPage(override val topicId: Long, val mark: Mark) : Page
        data class Trap(override val topicId: Long, val text: String) : Page
        data class Recall(override val topicId: Long, val question: String, val answer: String) : Page
        data class Question(override val topicId: Long, val questionId: Long) : Page
    }

    /**
     * O resumo em frases curtas, uma por linha do caderno: o texto da biblioteca costuma vir num
     * parágrafo só, pesado de ler no celular. Tira títulos e marcação, quebra nas frases e nos itens.
     */
    fun summaryLines(markdown: String?, max: Int = 12): List<String> {
        if (markdown.isNullOrBlank()) return emptyList()
        val clean = markdown.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("|") && !it.startsWith("```") }
            .joinToString(" ") { it.removePrefix("- ").removePrefix("* ").removePrefix("• ") }
            .replace(Regex("""\s+"""), " ")
        // quebra depois de ponto, ponto e vírgula ou dois-pontos seguidos de maiúscula; não quebra "art.", "§", "Lei n."
        // (ou antes de um termo em negrito, que costuma abrir a ideia seguinte)
        val parts = Regex("""(?<=[.;!?])\s+(?=[A-ZÁÉÍÓÚÂÊÔÃÕÇ0-9"(]|\*\*)""").split(clean)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        // Frase longa só quebra no ponto e vírgula (antes quebrava também na vírgula e descartava os
        // pedaços curtos: o resumo saía picotado, com partes sumindo). Pedaço curto junta ao anterior.
        val pieces = parts.flatMap { sentence ->
            if (sentence.length <= 260) listOf(sentence) else sentence.split(Regex("""(?<=;)\s+""")).map { it.trim() }.filter { it.isNotEmpty() }
        }
        val merged = mutableListOf<String>()
        pieces.forEach { p -> if (merged.isNotEmpty() && (p.length < 40 || merged.last().length < 40)) merged[merged.lastIndex] = merged.last() + " " + p else merged += p }
        return merged.filter { it.length >= 25 }.take(max)
    }

    private fun goodText(s: String, min: Int = 20, max: Int = 420) = s.trim().length in min..max
    private fun goodRecall(q: String, a: String) = goodText(q, 10, 300) && goodText(a, 15, 600)

    /**
     * As páginas da Folheada do dia ([count], em geral 5): uma questão que a pessoa errou, um grifo
     * dela, uma pegadinha, um cartão para lembrar e um ponto-chave, tirados dos tópicos que estão
     * esfriando (mais tempo sem ver primeiro). Muda a cada dia ([daySeed]), não repete tópico mais
     * de duas vezes. Vazio quando nada foi estudado ainda.
     */
    fun folheada(topics: List<Topic>, now: Long, daySeed: Long, count: Int = 5): List<Page> {
        val studied = topics.filter { it.status != "NAO_ESTUDADO" }
        if (studied.isEmpty()) return emptyList()
        val random = Random(daySeed)
        val order = studied.sortedWith(compareBy<Topic>({ firmness(it, now) != Firmness.COLD }, { it.lastSeenAt ?: 0L }))
        val used = mutableMapOf<Long, Int>()
        val pages = mutableListOf<Page>()
        // com poucos tópicos estudados, um tópico pode dar mais páginas (senão a Folheada ficava com 3)
        val maxPerTopic = if (studied.size >= 3) 2 else count
        fun canUse(t: Topic) = (used[t.id] ?: 0) < maxPerTopic
        fun add(p: Page) { pages += p; used[p.topicId] = (used[p.topicId] ?: 0) + 1 }

        // 1. a questão que errou
        order.firstOrNull { canUse(it) && it.wrongQuestions.isNotEmpty() }?.let { t -> add(Page.Question(t.id, t.wrongQuestions.random(random))) }
        // 2. um grifo dela (o mais antigo dos tópicos que esfriam)
        order.filter { canUse(it) }.flatMap { t -> t.marks.filter { goodText(it.quote, 15, 500) }.map { t to it } }
            .minByOrNull { it.second.createdAt }?.let { (t, m) -> add(Page.MarkPage(t.id, m)) }
        // 3. uma pegadinha
        order.firstOrNull { canUse(it) && it.traps.any { s -> goodText(s) } }?.let { t -> add(Page.Trap(t.id, t.traps.filter { goodText(it) }.random(random))) }
        // 4. um cartão para lembrar
        order.firstOrNull { canUse(it) && it.recall.any { (q, a) -> goodRecall(q, a) } }?.let { t ->
            val (q, a) = t.recall.filter { (q, a) -> goodRecall(q, a) }.random(random); add(Page.Recall(t.id, q, a))
        }
        // 5. um ponto-chave: três frases do resumo
        order.firstOrNull { canUse(it) && summaryLines(it.summary).size >= 2 }?.let { t ->
            val lines = summaryLines(t.summary)
            val start = random.nextInt(0, (lines.size - 2).coerceAtLeast(1))
            add(Page.Key(t.id, t.title, lines.drop(start).take(3)))
        }
        // faltou algum tipo: completa com o que os outros tópicos tiverem (dica, pegadinha, cartão,
        // resumo), dando uma volta por todos antes de repetir um tópico
        val taken = pages.map { pageText(it) }.toMutableSet()
        repeat(maxPerTopic) {
            for (t in order) {
                if (pages.size >= count) break
                if (!canUse(t)) continue
                val options = buildList<Page> {
                    t.tips.filter { goodText(it) }.forEach { add(Page.Key(t.id, t.title, listOf(it))) }
                    t.traps.filter { goodText(it) }.forEach { add(Page.Trap(t.id, it)) }
                    t.recall.filter { (q, a) -> goodRecall(q, a) }.forEach { (q, a) -> add(Page.Recall(t.id, q, a)) }
                    summaryLines(t.summary).takeIf { it.size >= 2 }?.let { add(Page.Key(t.id, t.title, it.take(3))) }
                }.filter { pageText(it) !in taken }
                options.randomOrNull(random)?.let { add(it); taken += pageText(it) }
            }
        }
        // a ordem de virar: começa leve (ponto-chave, grifo), termina na questão
        val rank = { p: Page -> when (p) { is Page.Key -> 0; is Page.MarkPage -> 1; is Page.Trap -> 2; is Page.Recall -> 3; is Page.Question -> 4 } }
        return pages.take(count).sortedBy(rank)
    }

    /**
     * O trecho grifado como markdown legível. O grifo guarda o texto como estava na teoria, às vezes
     * numa linha só: tabela com as linhas coladas ("| a | b | | --- | --- | | c | d |"), título
     * ("### NGAV - Vai além...") e itens ("... - Atua na prevenção"). Aqui as linhas voltam a ser
     * linhas, para a tabela virar tabela e a lista virar lista.
     */
    /**
     * Grifo arrastado por cima de uma tabela fica achatado (as células viram uma frase só, e "||"
     * de código vira lixo). Se o trecho grifado está dentro da tabela do bloco, mostra a tabela.
     */
    fun restoreQuote(quote: String, block: String?): String {
        // já é tabela em markdown; a seleção do site traz quebras de linha e tabs, e também é achatada
        if (block == null || Regex("""\|\s*:?-{3,}""").containsMatchIn(quote)) return quote
        if (block.lines().count { it.trimStart().startsWith("|") } < 2) return restoreFormatted(quote, block)
        fun flat(s: String) = s.replace(Regex("""\\\|"""), "|").replace(Regex("""^\s*\|?\s*:?-{3,}.*$""", RegexOption.MULTILINE), " ")
            .replace(Regex("""[|`*_,;]"""), " ").replace(Regex("""\s+"""), " ").trim().lowercase()
        val q = flat(quote)
        if (q.length < 12) return quote
        val words = q.split(' ').filter { it.length > 2 }
        val inBlock = flat(block)
        val hit = words.count { it in inBlock }
        return if (words.isNotEmpty() && hit * 10 >= words.size * 8) block.trim() else quote
    }

    /**
     * Lista, caixa ou parágrafo com negrito: a seleção vem achatada (itens grudados, "Termos explicadosArquitetura").
     * Se o grifo pega quase o bloco todo, vale o bloco original, com a formatação; trecho de uma frase fica como está.
     */
    private fun restoreFormatted(quote: String, block: String): String {
        val structured = block.lines().size > 1 || block.contains("**") || block.trimStart().startsWith(">")
        if (!structured) return quote
        fun words(s: String) = s.lowercase().replace(Regex("""[^\p{L}\p{N}]+"""), " ").split(' ').filter { it.length > 2 }
        val q = words(quote)
        val b = words(block)
        if (q.size < 4 || b.isEmpty()) return quote
        val inBlock = b.toSet()
        val hit = q.count { it in inBlock }
        // quase tudo do grifo está no bloco e o grifo cobre a maior parte dele
        return if (hit * 10 >= q.size * 9 && q.size * 10 >= b.size * 6) block.trim() else quote
    }

    /** Igual, mas procura a tabela em todos os blocos (o grifo feito no site pode apontar outro bloco no app). */
    fun restoreQuote(quote: String, blocks: List<String>, index: Int): String {
        restoreQuote(quote, blocks.getOrNull(index)).let { if (it != quote) return it }
        if (quote.length < 40) return quote
        for (b in blocks) restoreQuote(quote, b).let { if (it != quote) return it }
        return quote
    }

    fun quoteMarkdown(quote: String): String {
        var s = quote.trim()
        // tabela colada: cada "| |" é a quebra entre duas linhas da tabela
        if (Regex("""\|\s*-{3,}\s*\|""").containsMatchIn(s)) s = s.replace(Regex("""\|\s+\|"""), "|\n|")
        // título no meio do texto vira linha própria (em negrito: título grande num grifo pesa demais)
        s = s.replace(Regex("""\s*#{1,6}\s+([^\n]+?)(?=\s+-\s|\n|$)""")) { "\n\n**" + it.groupValues[1].trim() + "**\n" }
        // item de lista colado depois de frase ou título
        s = s.replace(Regex("""(?<=[.:!?*])\s+-\s+(?=\S)"""), "\n- ")
        // citação colada
        s = s.replace(Regex("""\s+>\s+(?=\S)"""), "\n\n> ")
        return s.replace(Regex("""\n{3,}"""), "\n\n").trim()
    }

    /** O texto de uma página (para não repetir a mesma na Folheada e para o "tem erro aqui"). */
    fun pageText(p: Page): String = when (p) {
        is Page.Key -> p.lines.joinToString(" ")
        is Page.MarkPage -> p.mark.quote
        is Page.Trap -> p.text
        is Page.Recall -> p.question + " — " + p.answer
        is Page.Question -> "Questão ${p.questionId}"
    }

    /** Minutos estimados da Folheada. */
    fun minutes(pages: List<Page>): Int = pages.sumOf { if (it is Page.Question) 2.0 else 1.0 }.toInt().coerceAtLeast(1)

    private const val DAY = 86_400_000L
}
