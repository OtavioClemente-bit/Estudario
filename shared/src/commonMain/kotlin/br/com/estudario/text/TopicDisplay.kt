package br.com.estudario.text

/**
 * Como mostrar os tópicos do edital na tela (só exibição: o texto guardado não muda).
 * O edital vem cortado nos ";" ("relações de subordinação entre orações;"), então cada pedaço
 * ganha letra maiúscula, perde a pontuação solta do fim e os espaços repetidos.
 * Usado no passo "Confere comigo o edital" do app e do site.
 */
object TopicDisplay {
    private val SPACES = Regex("\\s+")
    private val LEADING_BULLET = Regex("^[-–—•·*]+\\s*")
    private val TRAILING_PUNCT = Regex("[\\s;.,:]+$")
    private val PERCENT = Regex("(\\d+)% da nota")

    /** Texto do tópico pronto para a tela: sem ";" "." "," no fim, primeira letra maiúscula, espaços únicos. */
    fun clean(raw: String): String {
        var text = raw.replace(SPACES, " ").trim()
        text = text.replace(LEADING_BULLET, "")
        // "; e uso da crase" vira "Uso da crase": o "e" era a ligação com o pedaço anterior.
        if (text.startsWith("e ") && text.length > 4) text = text.substring(2)
        // Reticências do edital ("etc...") também saem: no fim de um item soltas, só confundem.
        text = text.replace(TRAILING_PUNCT, "")
        if (text.isEmpty()) return raw.trim()
        val first = text[0]
        // Não mexe em siglas escritas de propósito em minúscula ("eSocial", "iPhone").
        val keepCase = text.length > 1 && text[1].isUpperCase()
        return if (first.isLowerCase() && !keepCase) first.uppercaseChar() + text.substring(1) else text
    }

    /** Nível do tópico pela indentação de dois espaços (como o site guarda a árvore achatada). */
    fun depthOf(raw: String): Int = (raw.length - raw.trimStart(' ').length) / 2

    /**
     * Etiqueta do peso da matéria na prova ("15 questões · 25% da nota"), ou null sem quadro do edital.
     * [questions] é o número de questões da matéria; [description] a frase guardada da evidência
     * (de onde sai a parte da nota).
     */
    fun weightLabel(questions: Int?, description: String?): String? {
        val percent = description?.let { PERCENT.find(it)?.groupValues?.get(1) }
        if (questions == 0) return "Fora da prova objetiva"
        val parts = listOfNotNull(
            questions?.let { if (it == 1) "1 questão" else "$it questões" },
            percent?.let { "$it% da nota" },
        )
        return parts.takeIf { it.isNotEmpty() }?.joinToString(" · ")
    }
}
