package br.com.estudario.domain.planner

/**
 * Os três eixos do Smart Planner.
 *
 * Eles são deliberadamente independentes e nunca podem ser guardados numa propriedade só:
 *
 * - [ExamPriority] responde "quanto essa matéria vale na prova".
 * - [PersonalDifficulty] responde "quanto ela custa para esta pessoa".
 * - [InitialKnowledge] responde "quanto ela já sabe disso hoje".
 *
 * Banco de Dados pode ser prioridade muito alta e dificuldade baixa; Português pode ser prioridade
 * média e dificuldade muito alta. Fundir os dois num "peso" perde exatamente a informação que faz o
 * plano parecer pensado.
 *
 * Internamente tudo vira `Double` em 0..1 para o motor combinar sem degraus. A UI traduz de volta
 * para as categorias.
 */

/** Importância da matéria/tópico para a prova. Origem: edital, análise, metadados ou o usuário. */
enum class ExamPriority(val normalized: Double, val label: String) {
    VERY_LOW(0.10, "Muito baixa"),
    LOW(0.30, "Baixa"),
    MEDIUM(0.50, "Média"),
    HIGH(0.75, "Alta"),
    VERY_HIGH(1.00, "Muito alta");

    companion object {
        /** Converte um score 0..100 (o que o edital/análise produz) na faixa correspondente. */
        fun fromScore(score: Int): ExamPriority = when (score.coerceIn(0, 100)) {
            in 81..100 -> VERY_HIGH
            in 61..80 -> HIGH
            in 41..60 -> MEDIUM
            in 21..40 -> LOW
            else -> VERY_LOW
        }

        /** Aceita uma prioridade contínua já normalizada e devolve a faixa mais próxima. */
        fun fromNormalized(value: Double): ExamPriority =
            entries.minBy { kotlin.math.abs(it.normalized - value.coerceIn(0.0, 1.0)) }
    }
}

/** Quanto a matéria custa de esforço para esta pessoa. Declarada no assistente, revista pelos dados. */
enum class PersonalDifficulty(val normalized: Double, val label: String) {
    VERY_EASY(0.00, "Muito fácil"),
    EASY(0.25, "Fácil"),
    NORMAL(0.50, "Normal"),
    HARD(0.75, "Difícil"),
    VERY_HARD(1.00, "Muito difícil");

    companion object {
        /** O assistente começa todas em [NORMAL]; a pessoa só mexe no que precisa. */
        val DEFAULT = NORMAL
    }
}

/** Quanto a pessoa já conhece da matéria antes de o plano começar. Não é o mesmo que dificuldade. */
enum class InitialKnowledge(val normalized: Double, val label: String) {
    NONE(0.00, "Nunca estudei"),
    CONTACT(0.25, "Já tive contato"),
    BASIC(0.50, "Tenho uma base"),
    SOLID(0.75, "Tenho boa base"),
    STRONG(1.00, "Domino boa parte");

    companion object {
        val DEFAULT = NONE
    }
}

/**
 * O perfil de estudo de uma matéria: os três eixos juntos, mas guardados separados.
 *
 * Um tópico sem configuração própria herda o perfil da matéria, é o que evita obrigar alguém a
 * classificar 120 tópicos no primeiro setup.
 */
data class StudyDimensions(
    val examPriority: ExamPriority = ExamPriority.MEDIUM,
    val personalDifficulty: PersonalDifficulty = PersonalDifficulty.DEFAULT,
    val initialKnowledge: InitialKnowledge = InitialKnowledge.DEFAULT,
) {
    companion object {
        val DEFAULT = StudyDimensions()
    }
}

/**
 * Adaptadores entre a escala de cinco faixas ([ExamPriority], que é a do edital e a que a UI
 * mostra) e as quatro do alocador ([PlanPriority]).
 *
 * A conversão para [PlanPriority] perde a distinção entre baixa e muito baixa, por isso o caminho
 * preferido é sempre [br.com.estudario.domain.PriorityLevel] → [ExamPriority], que é 1 para 1. O
 * [PlanPriority] fica onde ele sempre esteve: como bucket de alocação, não como fonte da verdade.
 */
fun PlanPriority.toExamPriority(): ExamPriority = when (this) {
    PlanPriority.CRITICAL -> ExamPriority.VERY_HIGH
    PlanPriority.HIGH -> ExamPriority.HIGH
    PlanPriority.MEDIUM -> ExamPriority.MEDIUM
    PlanPriority.LOW -> ExamPriority.LOW
}

fun ExamPriority.toPlanPriority(): PlanPriority = when (this) {
    ExamPriority.VERY_HIGH -> PlanPriority.CRITICAL
    ExamPriority.HIGH -> PlanPriority.HIGH
    ExamPriority.MEDIUM -> PlanPriority.MEDIUM
    ExamPriority.LOW, ExamPriority.VERY_LOW -> PlanPriority.LOW
}

fun br.com.estudario.domain.PriorityLevel.toExamPriority(): ExamPriority = when (this) {
    br.com.estudario.domain.PriorityLevel.VERY_HIGH -> ExamPriority.VERY_HIGH
    br.com.estudario.domain.PriorityLevel.HIGH -> ExamPriority.HIGH
    br.com.estudario.domain.PriorityLevel.MEDIUM -> ExamPriority.MEDIUM
    br.com.estudario.domain.PriorityLevel.LOW -> ExamPriority.LOW
    br.com.estudario.domain.PriorityLevel.VERY_LOW -> ExamPriority.VERY_LOW
}

/**
 * Peso de rodízio (1 a 5) derivado **só** da prioridade da prova.
 *
 * Substitui o antigo `StudyMethod.weightOf`, que recebia a prioridade já contaminada pela
 * dificuldade pessoal. Aqui o peso responde a uma pergunta só: quanto essa matéria vale na prova.
 * O esforço extra que a dificuldade exige entra pelo NeedScore, não por aqui.
 */
fun ExamPriority.rotationWeight(): Int = when (this) {
    ExamPriority.VERY_HIGH -> 5
    ExamPriority.HIGH -> 4
    ExamPriority.MEDIUM -> 3
    ExamPriority.LOW -> 2
    ExamPriority.VERY_LOW -> 1
}

/**
 * Evidência acumulada sobre uma matéria ou tópico. É o que permite ao plano aprender sem IA: se a
 * pessoa declarou "Segurança é difícil" mas acerta 95% em 200 questões, a evidência vence a
 * declaração, progressivamente, na medida da confiança estatística.
 */
data class StudyEvidence(
    /** Total de questões respondidas na vida. */
    val answered: Int = 0,
    val correct: Int = 0,
    /** Janela recente (as últimas [recentAnswered] respostas), usada para detectar tendência. */
    val recentAnswered: Int = 0,
    val recentCorrect: Int = 0,
    /** Tópicos vistos / tópicos totais. Cobertura, não domínio. */
    val coveredTopics: Int = 0,
    val totalTopics: Int = 0,
    /** Revisões vencidas hoje. */
    val overdueReviews: Int = 0,
    /** Tarefas planejadas que não foram feitas no horizonte recente. */
    val missedTasks: Int = 0,
    /** Dias desde o último contato com o conteúdo. */
    val daysSinceContact: Int? = null,
) {
    init {
        require(answered >= 0) { "Respostas não podem ser negativas." }
        require(correct in 0..answered) { "Acertos precisam estar entre zero e o total respondido." }
        require(recentAnswered in 0..answered) { "A janela recente não pode ser maior que o histórico." }
        require(recentCorrect in 0..recentAnswered) { "Acertos recentes precisam caber na janela recente." }
        require(coveredTopics >= 0 && totalTopics >= 0) { "Contagem de tópicos não pode ser negativa." }
        require(overdueReviews >= 0 && missedTasks >= 0) { "Contadores de atraso não podem ser negativos." }
    }

    val hasSample: Boolean get() = answered > 0

    companion object {
        val EMPTY = StudyEvidence()
    }
}

/**
 * Códigos de motivo. O motor emite estes códigos e a UI decide como mostrar, é o que substitui a
 * frase que uma IA geraria, sem depender de IA e sem virar caixa preta.
 *
 * Os nomes são dados: renomear quebra explicações já gravadas em planos antigos.
 */
enum class PlannerReasonCode {
    HIGH_EXAM_PRIORITY,
    LOW_EXAM_PRIORITY,
    HIGH_PERSONAL_DIFFICULTY,
    DIFFICULTY_RELAXED_BY_EVIDENCE,
    DIFFICULTY_RAISED_BY_EVIDENCE,
    LOW_RECENT_ACCURACY,
    HIGH_RECENT_ACCURACY,
    INSUFFICIENT_SAMPLE,
    CONTENT_NOT_STARTED,
    CONTENT_REMAINING,
    CONTENT_COVERED,
    STRONG_PRIOR_KNOWLEDGE,
    REVISION_DUE,
    REVISION_OVERDUE,
    LONG_TIME_WITHOUT_CONTACT,
    TASKS_MISSED,
    EXAM_APPROACHING,
    MAINTENANCE_ONLY,
}

/**
 * Uma razão com força relativa. [weight] é a fração do NeedScore que esse fator explica, o que
 * permite à UI mostrar só os dois ou três motivos que realmente decidiram.
 */
data class PlannerReason(
    val code: PlannerReasonCode,
    val weight: Double,
    /** Valor observado que originou o motivo (61.0 para "61% de acerto", por exemplo). */
    val observed: Double? = null,
) {
    init { require(weight.isFinite() && weight >= 0.0) { "O peso de um motivo não pode ser negativo." } }
}

/**
 * O resultado do cálculo de necessidade para uma matéria ou tópico.
 *
 * [score] em 0..1 decide fila, frequência, número de sessões, volume de questões e proximidade das
 * revisões. [reasons] existe para que qualquer número na tela possa ser justificado.
 */
data class StudyNeed(
    val subjectId: Long,
    val topicId: Long? = null,
    val score: Double,
    val effectiveDifficulty: Double,
    val effectiveMastery: Double,
    val masteryDeficit: Double,
    val remainingCoverage: Double,
    val performanceConfidence: Double,
    val reasons: List<PlannerReason>,
    val algorithmVersion: Int = PlannerWeights.ALGORITHM_VERSION,
) {
    init {
        require(score in 0.0..1.0) { "NeedScore precisa estar normalizado entre 0 e 1." }
        require(performanceConfidence in 0.0..1.0) { "A confiança precisa estar entre 0 e 1." }
    }

    /** Os motivos que mais pesaram, do mais forte para o mais fraco. */
    fun topReasons(limit: Int = 3): List<PlannerReason> =
        reasons.sortedWith(compareByDescending<PlannerReason> { it.weight }.thenBy { it.code.name }).take(limit)
}
