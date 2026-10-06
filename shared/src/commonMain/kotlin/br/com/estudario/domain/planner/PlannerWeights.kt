package br.com.estudario.domain.planner

/**
 * Todos os números do Smart Planner moram aqui.
 *
 * Nenhum `0.4`, `1.5` ou `0.2` solto em outro arquivo: quando o plano mudar de comportamento, ele
 * muda neste arquivo e em mais nenhum. Isso é o que torna o motor testável (um teste fixa os pesos
 * e verifica o efeito) e versionável (um plano gravado sabe com quais regras nasceu).
 */
data class PlannerWeights(
    /** Peso da importância da matéria na prova. É o maior: o edital manda. */
    val examPriority: Double = 0.30,
    /** Peso da dificuldade pessoal, já corrigida pela evidência. */
    val difficulty: Double = 0.18,
    /** Peso do quanto falta dominar (desempenho), separado do quanto falta ver (cobertura). */
    val masteryDeficit: Double = 0.20,
    /** Peso do conteúdo ainda não visto. */
    val remainingCoverage: Double = 0.16,
    /** Peso das revisões vencidas. */
    val revisionDue: Double = 0.08,
    /** Peso das tarefas que a pessoa deixou passar. */
    val overdue: Double = 0.04,
    /** Peso do tempo sem tocar no assunto. */
    val staleness: Double = 0.04,
) {
    init {
        require(components().all { it >= 0.0 }) { "Nenhum peso do planejador pode ser negativo." }
        require(total > 0.0) { "A soma dos pesos precisa ser maior que zero." }
    }

    fun components(): List<Double> =
        listOf(examPriority, difficulty, masteryDeficit, remainingCoverage, revisionDue, overdue, staleness)

    val total: Double get() = components().sum()

    companion object {
        /**
         * Versão do algoritmo. Sobe sempre que pesos, fórmula ou regras mudarem de forma que o
         * mesmo perfil produziria outro plano. Fica gravada em cada plano gerado, sem isso não dá
         * para saber se um plano antigo nasceu com as regras de hoje.
         */
        const val ALGORITHM_VERSION: Int = 1

        /**
         * Confiança estatística: com quantas respostas a evidência passa a valer mais que a
         * declaração. Em `k` respostas a confiança é 50%; em 4×k já passa de 80%.
         */
        const val CONFIDENCE_HALF_SAMPLE: Double = 25.0

        /** Abaixo disso a amostra é pequena demais para o motor chamar alguém de forte ou fraco. */
        const val MINIMUM_MEANINGFUL_SAMPLE: Int = 10

        /** Acerto abaixo disso conta como ponto fraco (quando a amostra permite afirmar). */
        const val WEAK_ACCURACY: Double = 0.65

        /** Acerto acima disso conta como domínio consolidado. */
        const val STRONG_ACCURACY: Double = 0.85

        /** Dias sem contato a partir dos quais a necessidade satura por esquecimento. */
        const val STALENESS_SATURATION_DAYS: Int = 45

        /** Tarefas perdidas a partir das quais o fator de atraso satura. */
        const val OVERDUE_SATURATION_TASKS: Int = 5

        /** Revisões vencidas a partir das quais o fator de revisão satura. */
        const val REVISION_SATURATION_COUNT: Int = 4

        /**
         * Amplitude da modulação por prioridade. O NeedScore é uma média ponderada, mas duas
         * matérias com necessidade idêntica e prioridades diferentes precisam se separar. O
         * multiplicador varia de (1 - AMPLITUDE) a (1 + AMPLITUDE) conforme a prioridade, sem
         * nunca zerar nem explodir uma matéria.
         */
        const val PRIORITY_AMPLITUDE: Double = 0.40

        /**
         * Piso de necessidade de qualquer matéria ativa do edital. Matéria de peso baixo recebe
         * menos atenção, nunca zero, o plano não some silenciosamente com conteúdo cobrado.
         */
        const val MINIMUM_ACTIVE_NEED: Double = 0.05

        /** Perfis de peso por fase. A fase muda a ênfase, não o algoritmo. */
        fun forPhase(kind: PlanPhaseKind): PlannerWeights = when (kind) {
            // Construção: o que ainda não foi visto domina a decisão.
            PlanPhaseKind.BASE -> PlannerWeights(
                examPriority = 0.30,
                difficulty = 0.14,
                masteryDeficit = 0.12,
                remainingCoverage = 0.30,
                revisionDue = 0.08,
                overdue = 0.03,
                staleness = 0.03,
            )
            // Consolidação: desempenho e revisão passam a pesar mais que cobertura.
            PlanPhaseKind.APROFUNDAMENTO -> PlannerWeights(
                examPriority = 0.28,
                difficulty = 0.18,
                masteryDeficit = 0.24,
                remainingCoverage = 0.14,
                revisionDue = 0.10,
                overdue = 0.03,
                staleness = 0.03,
            )
            // Reta final: ponto fraco e revisão vencida decidem quase tudo.
            PlanPhaseKind.RETA_FINAL -> PlannerWeights(
                examPriority = 0.30,
                difficulty = 0.16,
                masteryDeficit = 0.30,
                remainingCoverage = 0.04,
                revisionDue = 0.14,
                overdue = 0.03,
                staleness = 0.03,
            )
        }

        val DEFAULT = PlannerWeights()
    }
}
