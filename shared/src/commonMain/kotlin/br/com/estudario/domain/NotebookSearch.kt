package br.com.estudario.domain

import br.com.estudario.text.stripAccents

/**
 * A busca geral do Caderno de estudo (app e site usam esta mesma regra): a pessoa escreve uma
 * matéria, um tópico ou um pedaço do que guardou e acha. Sem diferença de acento nem de maiúscula
 * ("licitacao" acha "Licitação"), com pedaço de palavra ("admin" acha "Administrativo") e com várias
 * palavras em qualquer ordem: todas precisam aparecer no item (no título, na matéria ou no texto).
 */
object NotebookSearch {
    /** O que pode aparecer na busca. */
    enum class Kind { TOPIC, MARK, NOTE, TIP, TRAP, CARD, DECK, QUESTION }

    /** Os três grupos da lista de resultados, nesta ordem. */
    enum class Group(val label: String) {
        TOPICS("Matérias e tópicos"),
        MARKS_NOTES("Grifos e anotações"),
        SAVED("Questões, flashcards e dicas salvos"),
    }

    fun groupOf(kind: Kind): Group = when (kind) {
        Kind.TOPIC -> Group.TOPICS
        Kind.MARK, Kind.NOTE -> Group.MARKS_NOTES
        else -> Group.SAVED
    }

    /**
     * Um item buscável. [title] é o nome (do tópico) ou o começo do texto guardado; [context] é onde ele
     * mora (matéria, tópico); [body] é o texto inteiro (trecho grifado, nota, enunciado...).
     */
    data class Item<T>(val kind: Kind, val title: String, val context: String? = null, val body: String? = null, val ref: T)

    /** Um resultado: o item, a nota de relevância e, se o texto bateu no [Item.body], o pedaço em volta. */
    data class Hit<T>(val item: Item<T>, val score: Int, val excerpt: String?)

    private val SPACES = Regex("\\s+")

    /** Minúsculas, sem acento e com os espaços juntados: a forma em que tudo é comparado. */
    fun fold(value: String): String = stripAccents(value.lowercase()).replace(SPACES, " ").trim()

    /** As palavras da busca, já dobradas (vazio = sem busca). */
    fun terms(query: String): List<String> = fold(query).split(' ').filter { it.isNotEmpty() }.distinct()

    /** Todas as palavras aparecem em algum dos textos (juntos)? Sem palavras, tudo passa. */
    fun matches(terms: List<String>, vararg texts: String?): Boolean {
        if (terms.isEmpty()) return true
        val hay = texts.filterNotNull().joinToString(" ") { fold(it) }
        return terms.all { has(hay, it) }
    }

    private val ENDINGS = listOf("oes", "aes", "ao", "es", "s")

    /** A raiz de singular/plural ("licitacao" e "licitacoes" → "licitac"), só em palavra de 5 letras ou mais. */
    internal fun root(term: String): String {
        if (term.length < 5) return term
        val end = ENDINGS.firstOrNull { term.endsWith(it) && term.length - it.length >= 4 } ?: return term
        return term.dropLast(end.length)
    }

    /** O texto (já dobrado) tem a palavra, ou a raiz dela (singular acha plural e vice-versa)? */
    private fun has(hay: String, term: String): Boolean = hay.contains(term) || hay.contains(root(term))

    private fun wordStarts(words: List<String>, term: String): Boolean = words.any { it.startsWith(term) || it.startsWith(root(term)) }

    /**
     * Busca nos [items] e devolve os que têm todas as palavras, do mais relevante para o menos: o título
     * igual à busca, depois o título que começa com ela, palavras do título que começam com as da busca,
     * título que contém, matéria/tópico que contém e, por último, só o texto. Empate: título em ordem.
     */
    fun <T> search(query: String, items: List<Item<T>>): List<Hit<T>> {
        val terms = terms(query)
        if (terms.isEmpty()) return emptyList()
        val whole = terms.joinToString(" ")
        return items.mapNotNull { item ->
            val title = fold(item.title)
            val context = item.context?.let(::fold).orEmpty()
            val body = item.body?.let(::fold).orEmpty()
            // as palavras podem estar espalhadas entre título, matéria e texto
            val hay = "$title $context $body"
            if (!terms.all { has(hay, it) }) return@mapNotNull null
            val words = title.split(' ')
            val score = when {
                title == whole -> 100
                title.startsWith(whole) -> 80
                terms.all { wordStarts(words, it) } -> 60
                terms.all { has(title, it) } -> 40
                terms.all { has(title, it) || has(context, it) } -> 20
                else -> 10
            }
            val excerpt = if (score <= 10 || item.kind != Kind.TOPIC) item.body?.let { excerpt(it, terms) } else null
            Hit(item, score, excerpt)
        }.sortedWith(compareByDescending<Hit<T>> { it.score }.thenBy { fold(it.item.title) })
    }

    /** Os resultados separados pelos três grupos (só os grupos com algo), na ordem de [Group]. */
    fun <T> grouped(hits: List<Hit<T>>): List<Pair<Group, List<Hit<T>>>> =
        Group.entries.mapNotNull { g -> hits.filter { groupOf(it.item.kind) == g }.takeIf { it.isNotEmpty() }?.let { g to it } }

    /**
     * O pedaço do [text] em volta da primeira palavra da busca encontrada (com "…" nas pontas cortadas),
     * para a pessoa ver por que o item apareceu. Sem palavra encontrada, null.
     */
    fun excerpt(text: String, terms: List<String>, radius: Int = 60): String? {
        val flat = text.replace(SPACES, " ").trim()
        if (flat.isEmpty() || terms.isEmpty()) return null
        // Dobra letra por letra, guardando de que letra original veio cada letra dobrada.
        val folded = StringBuilder()
        val origin = ArrayList<Int>(flat.length)
        flat.forEachIndexed { i, c ->
            val f = stripAccents(c.toString().lowercase())
            for (ch in f) { folded.append(ch); origin.add(i) }
        }
        val at = terms.flatMap { listOf(it, root(it)) }.mapNotNull { t -> folded.indexOf(t).takeIf { it >= 0 } }.minOrNull() ?: return null
        val start = origin[at]
        var from = (start - radius).coerceAtLeast(0)
        var to = (start + radius * 2).coerceAtMost(flat.length)
        // não corta palavra no meio
        if (from > 0) flat.indexOf(' ', from).takeIf { it in from until start }?.let { from = it + 1 }
        if (to < flat.length) flat.lastIndexOf(' ', to).takeIf { it > start }?.let { to = it }
        return (if (from > 0) "…" else "") + flat.substring(from, to).trim() + (if (to < flat.length) "…" else "")
    }
}
