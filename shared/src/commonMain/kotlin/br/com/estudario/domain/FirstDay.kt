package br.com.estudario.domain

import kotlinx.datetime.LocalDate

/**
 * Os primeiros minutos de quem acabou de montar o plano, iguais no app e no site: o que o plano
 * montou (contas feitas só com as tarefas de verdade) e o roteiro "Seu primeiro dia" (ler o
 * primeiro tópico, responder as primeiras questões, ver os primeiros flashcards), que termina numa
 * comemoração que aparece uma vez só na conta.
 *
 * As marcas moram na lista "celebrated" da conta ([Celebrated]), então o roteiro começado no celular
 * continua no site e a comemoração vista num aparelho não se repete no outro.
 */
object FirstDay {
    /** Gravada ao montar o primeiro plano da conta: só quem começa agora vê o roteiro. */
    val STARTED = Celebrated.tour("first-day-start")
    /** O dia 1 foi comemorado (o roteiro some). */
    val DONE = Celebrated.tour("first-day")
    /** A pessoa pulou o roteiro. */
    val SKIPPED = Celebrated.tour("first-day-skip")
    /** Abriu os flashcards pela primeira vez (flashcards vistos não deixam outro rastro nos dados). */
    val FLASHCARDS = Celebrated.tour("first-flashcards")

    const val QUESTIONS_GOAL = 3

    enum class Step(val title: String, val hint: String, val action: String) {
        THEORY("Ler o primeiro tópico", "A teoria do primeiro tópico do seu plano, no seu ritmo.", "Ler agora"),
        QUESTIONS("Responder $QUESTIONS_GOAL questões", "Questões para fixar o que você acabou de ler. Errar aqui também ensina.", "Responder"),
        FLASHCARDS("Ver os primeiros flashcards", "Cartões rápidos que voltam na hora certa para você não esquecer.", "Abrir cartões"),
    }

    const val TITLE = "Seu primeiro dia"
    const val SUBTITLE = "Três passos curtos para ver o método funcionando com o seu edital."
    const val SKIP = "Pular o roteiro"
    const val ALL_DONE = "Tudo feito! Hora de comemorar."

    /** O que já foi feito, com dados reais da conta. */
    data class State(val done: Set<Step>) {
        val count: Int get() = done.size
        val complete: Boolean get() = done.size == Step.entries.size
        /** O próximo passo, na ordem do roteiro (null quando terminou). */
        val next: Step? get() = Step.entries.firstOrNull { it !in done }
        val progressLabel: String get() = "$count de ${Step.entries.size}"
    }

    fun state(studiedTopics: Int, answeredQuestions: Int, sawFlashcards: Boolean): State = State(buildSet {
        if (studiedTopics > 0) add(Step.THEORY)
        if (answeredQuestions >= QUESTIONS_GOAL) add(Step.QUESTIONS)
        if (sawFlashcards) add(Step.FLASHCARDS)
    })

    /**
     * O roteiro aparece para quem montou o primeiro plano agora (ou tem plano e ainda não estudou nada)
     * e ainda não terminou nem pulou.
     */
    fun visible(celebrated: Set<String>, hasPlan: Boolean, untouched: Boolean = false): Boolean =
        hasPlan && (STARTED in celebrated || untouched) && DONE !in celebrated && SKIPPED !in celebrated

    // ------------------------------------------------------------ a comemoração do dia 1

    const val CELEBRATION_FROM = "COMEÇO"
    const val CELEBRATION_TO = "DIA 1"
    const val CELEBRATION_WORD = "DIA 1!"
    const val CELEBRATION_SUB = "Primeiro dia completo"
    const val CELEBRATION_LINE = "Você leu, praticou e revisou. É esse ciclo que o plano repete até a prova."
    const val CELEBRATION_BUTTON = "Ver meu plano"

    /** Os prêmios da comemoração, só com números de verdade. */
    fun celebrationLoot(answered: Int, correct: Int, nextStudyEpochDay: Long?): List<Pair<String, String>> = buildList {
        add("PRIMEIRO TÓPICO" to "Teoria lida e marcada no edital")
        add("QUESTÕES" to (if (answered == 1) "1 respondida" else "$answered respondidas") + " · $correct ${if (correct == 1) "certa" else "certas"}")
        nextStudyEpochDay?.let { add("PRÓXIMO ESTUDO" to dateLabel(it)) }
    }

    // ------------------------------------------------------------ o que o plano montou

    /**
     * Resumo do plano recém-montado. [coverageEpochDay] só existe quando todo tópico do edital tem
     * leitura marcada no plano (senão não dá para prometer data). [weights] é a parte do tempo do plano
     * de cada matéria, em porcentagem, da maior para a menor.
     */
    data class Reveal(
        val subjects: Int,
        val topics: Int,
        val plannedTopics: Int,
        val coverageEpochDay: Long?,
        val examEpochDay: Long?,
        val weights: List<Pair<String, Int>>,
    ) {
        val headline: String get() = "${plural(subjects, "matéria", "matérias")} e ${plural(topics, "tópico", "tópicos")} organizados para você"

        val coverageLine: String get() {
            val end = coverageEpochDay
            val exam = examEpochDay
            return when {
                end == null && plannedTopics > 0 -> "Até a prova cabem ${plural(plannedTopics, "tópico", "tópicos")} de $topics. Mais minutos por dia fazem caber mais."
                end == null -> "Seu plano começa pelos tópicos de mais peso."
                exam == null -> "Você termina de ver o edital todo em ${dateLabel(end)}."
                end <= exam -> "Você termina de ver o edital todo em ${dateLabel(end)}, ${plural((exam - end).toInt(), "dia", "dias")} antes da prova."
                else -> "No ritmo de hoje, o edital todo fica pronto em ${dateLabel(end)}, depois da prova. Mais minutos por dia adiantam essa data."
            }
        }
    }

    /**
     * [theoryDays] é (id do tópico, dia) de cada leitura marcada no plano; [minutesBySubject] é o tempo
     * total do plano em cada matéria.
     */
    fun reveal(
        subjects: Int,
        topics: Int,
        theoryDays: List<Pair<Long, Long>>,
        minutesBySubject: Map<String, Int>,
        examEpochDay: Long?,
    ): Reveal {
        val planned = theoryDays.map { it.first }.toSet().size
        val coverage = if (topics > 0 && planned >= topics) theoryDays.maxOfOrNull { it.second } else null
        val total = minutesBySubject.values.sum().coerceAtLeast(1)
        val weights = minutesBySubject.entries.filter { it.value > 0 }.sortedByDescending { it.value }
            .map { it.key to ((it.value * 100.0) / total).let { p -> kotlin.math.round(p).toInt().coerceAtLeast(1) } }
        return Reveal(subjects, topics, planned.coerceAtMost(topics), coverage, examEpochDay, weights)
    }

    private val months = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")

    /** "12 de mar. de 2027". */
    fun dateLabel(epochDay: Long): String {
        val d = LocalDate.fromEpochDays(epochDay.toInt())
        return "${d.day} de ${months[d.month.ordinal]}. de ${d.year}"
    }

    private fun plural(n: Int, one: String, many: String) = if (n == 1) "1 $one" else "$n $many"
}
