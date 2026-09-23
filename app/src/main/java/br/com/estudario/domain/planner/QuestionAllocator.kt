package br.com.estudario.domain.planner

/**
 * Maturidade do conteúdo. É o que decide o *tipo* de bateria de questões, antes do tamanho.
 *
 * Cobertura e domínio são coisas diferentes e as duas entram aqui: um tópico pode estar coberto
 * (já passei por ele) e mesmo assim ser fraco (não retenho), e é esse cruzamento que define se a
 * pessoa precisa de primeiro contato, consolidação, resgate ou só manutenção.
 */
enum class ContentMaturity(val label: String) {
    /** Nunca visto: questões servem para fixar o que acabou de ser estudado. */
    NEW("Recém-aprendido"),

    /** Visto, ainda sem desempenho que sustente conclusão: é a hora do volume. */
    CONSOLIDATING("Consolidando"),

    /** Visto e com desempenho ruim comprovado: mais questões e retorno rápido. */
    WEAK("Ponto fraco"),

    /** Visto e com desempenho alto comprovado: manutenção, sem desperdiçar tempo. */
    STRONG("Dominado"),
}

data class QuestionAllocation(
    val questions: Int,
    val maturity: ContentMaturity,
    val reasons: List<PlannerReason>,
)

/**
 * Decide quantas questões cada conteúdo recebe.
 *
 * Questão não é uma aba separada do app: ela é parte do plano, e o volume dela é uma decisão do
 * motor, não um número fixo no formulário. A regra tem duas camadas:
 *
 * 1. A maturidade define a faixa (primeiro contato pede bateria pequena; ponto fraco pede bateria
 *    grande).
 * 2. O NeedScore modula dentro da faixa, para que prioridade e dificuldade também apareçam.
 *
 * E há um freio importante: **um erro isolado não pode bagunçar o plano**. Enquanto a amostra for
 * menor que [PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE], o conteúdo não é classificado como fraco
 * nem como dominado, ele fica em consolidação, que é a faixa neutra.
 */
object QuestionAllocator {

    /** Fator de volume por maturidade, aplicado sobre a base escolhida no assistente. */
    private val VOLUME_BY_MATURITY = mapOf(
        ContentMaturity.NEW to 0.60,
        ContentMaturity.CONSOLIDATING to 1.00,
        ContentMaturity.WEAK to 1.60,
        ContentMaturity.STRONG to 0.45,
    )

    /** Quanto o NeedScore pode esticar ou encolher o volume dentro da faixa da maturidade. */
    private const val NEED_MODULATION = 0.50

    private const val MIN_QUESTIONS = 5
    private const val MAX_QUESTIONS = 60

    /**
     * Classifica o conteúdo.
     *
     * @param covered se o conteúdo já foi estudado ao menos uma vez.
     */
    fun maturityOf(need: StudyNeed, evidence: StudyEvidence, covered: Boolean): ContentMaturity {
        if (!covered && !evidence.hasSample) return ContentMaturity.NEW
        // Amostra pequena não autoriza conclusão em nenhuma direção.
        if (evidence.answered < PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) {
            return if (covered) ContentMaturity.CONSOLIDATING else ContentMaturity.NEW
        }
        return confidentBand(evidence)
    }

    /**
     * Classificação por **confiança nos dois sentidos**, e é aqui que mora a diferença entre um
     * classificador honesto e um alarmista.
     *
     * O conteúdo só é chamado de fraco quando o intervalo de confiança inteiro está abaixo do
     * limiar, e só é chamado de dominado quando o intervalo inteiro está acima. Tudo que o
     * intervalo não decide fica em consolidação, a faixa neutra.
     *
     * Na prática: acertar 30 de 40 (75%) não vira "ponto fraco" só porque a estimativa
     * conservadora é 60%, mas acertar 24 de 60 (40%) vira, porque aí não há dúvida.
     */
    fun confidentBand(evidence: StudyEvidence): ContentMaturity {
        if (evidence.answered < PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) return ContentMaturity.CONSOLIDATING
        val useRecent = evidence.recentAnswered >= PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE
        val correct = if (useRecent) evidence.recentCorrect else evidence.correct
        val answered = if (useRecent) evidence.recentAnswered else evidence.answered
        val (lower, upper) = StudyNeedCalculator.wilsonBounds(correct, answered)
        return when {
            upper < PlannerWeights.WEAK_ACCURACY -> ContentMaturity.WEAK
            lower >= PlannerWeights.STRONG_ACCURACY -> ContentMaturity.STRONG
            else -> ContentMaturity.CONSOLIDATING
        }
    }

    /**
     * Quantidade de questões para um conteúdo.
     *
     * @param baseQuestions a base por tópico escolhida no assistente ([StudyMethodConfig.questionsPerTopic]).
     */
    fun allocate(
        baseQuestions: Int,
        need: StudyNeed,
        evidence: StudyEvidence,
        covered: Boolean,
    ): QuestionAllocation {
        val maturity = maturityOf(need, evidence, covered)
        if (baseQuestions <= 0) return QuestionAllocation(0, maturity, emptyList())

        val maturityFactor = VOLUME_BY_MATURITY.getValue(maturity)
        val needFactor = 1.0 + (need.score - 0.5).coerceIn(-0.5, 0.5) * 2.0 * NEED_MODULATION
        val raw = baseQuestions * maturityFactor * needFactor
        val questions = Math.round(raw).toInt().coerceIn(
            MIN_QUESTIONS.coerceAtMost(baseQuestions),
            MAX_QUESTIONS.coerceAtLeast(baseQuestions),
        )

        val reasons = buildList {
            when (maturity) {
                ContentMaturity.NEW -> add(PlannerReason(PlannerReasonCode.CONTENT_NOT_STARTED, 1.0))
                ContentMaturity.WEAK -> add(
                    PlannerReason(PlannerReasonCode.LOW_RECENT_ACCURACY, 1.0, StudyNeedCalculator.rawAccuracy(evidence)),
                )
                ContentMaturity.STRONG -> add(
                    PlannerReason(PlannerReasonCode.HIGH_RECENT_ACCURACY, 1.0, StudyNeedCalculator.rawAccuracy(evidence)),
                )
                ContentMaturity.CONSOLIDATING -> Unit
            }
            if (evidence.hasSample && evidence.answered < PlannerWeights.MINIMUM_MEANINGFUL_SAMPLE) {
                add(PlannerReason(PlannerReasonCode.INSUFFICIENT_SAMPLE, 0.0, evidence.answered.toDouble()))
            }
        }
        return QuestionAllocation(questions, maturity, reasons)
    }

    /**
     * Um conteúdo fraco precisa voltar antes. Devolve em quantos dias a bateria seguinte deve cair
     *, `null` quando não há motivo para antecipar nada.
     */
    fun returnIntervalDays(maturity: ContentMaturity, need: StudyNeed): Int? = when (maturity) {
        ContentMaturity.WEAK -> if (need.score >= 0.65) 2 else 4
        ContentMaturity.CONSOLIDATING -> 7
        ContentMaturity.NEW -> null
        ContentMaturity.STRONG -> null
    }
}
