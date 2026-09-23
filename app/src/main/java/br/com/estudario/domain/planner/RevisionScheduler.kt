package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Revisão espaçada adaptada a concurso.
 *
 * A escada fixa D+1 / D+7 / D+30 é um bom começo e um péssimo fim: ela trata igual o tópico que a
 * pessoa domina e o que ela erra sempre. Aqui o intervalo sai da escada base multiplicado por uma
 * **facilidade** derivada do que o motor já sabe sobre o conteúdo, domínio alto afasta a próxima
 * revisão, dificuldade e erro aproximam.
 *
 * Não é um algoritmo acadêmico completo de repetição espaçada, e nem deveria ser: numa preparação
 * com data marcada, o intervalo também precisa respeitar a prova. Um tópico não pode ter a próxima
 * revisão agendada para depois do dia da prova.
 */
data class RevisionPlan(
    val stage: Int,
    val intervalDays: Long,
    val dueDate: LocalDate,
    val reasons: List<PlannerReason>,
)

object RevisionScheduler {

    /**
     * Escada base, em dias. Os primeiros contatos são próximos porque é ali que se perde conteúdo;
     * depois o intervalo abre. A facilidade calculada ajusta cada degrau.
     */
    val BASE_INTERVAL_DAYS: List<Long> = listOf(1, 3, 7, 16, 35, 70)

    /** Teto: passar disso é praticamente esquecer o tópico de novo. */
    const val MAX_INTERVAL_DAYS: Long = 120

    private const val MIN_EASE = 0.55
    private const val MAX_EASE = 1.80

    /** A partir daqui a revisão encosta na prova e o intervalo passa a ser cortado. */
    private const val EXAM_TAIL_DAYS = 45L

    /**
     * Facilidade do conteúdo em 0,55..1,80.
     *
     * Domínio alto e dificuldade baixa esticam o intervalo; o contrário encurta. Um tópico difícil
     * com desempenho ruim volta em cerca de metade do tempo de um tópico fácil e dominado, que é
     * exatamente o comportamento pedido, sem inventar um modelo de memória.
     */
    fun ease(need: StudyNeed): Double {
        val masteryPull = (need.effectiveMastery - 0.5) * 2.0 * 0.60
        val difficultyPull = need.effectiveDifficulty * 0.50
        return (1.0 + masteryPull - difficultyPull).coerceIn(MIN_EASE, MAX_EASE)
    }

    /**
     * Próxima revisão de um tópico.
     *
     * @param stage quantas revisões o tópico já completou (0 = acabou de ser estudado).
     * @param lastContact dia do último contato, base da contagem.
     * @param overdue se a revisão anterior venceu sem ser feita; nesse caso o estágio não avança,
     *   porque avançar seria premiar o atraso com um intervalo maior.
     * @param examDate data da prova, quando existir; o intervalo nunca ultrapassa a véspera.
     */
    fun next(
        stage: Int,
        lastContact: LocalDate,
        need: StudyNeed,
        today: LocalDate,
        overdue: Boolean = false,
        examDate: LocalDate? = null,
    ): RevisionPlan {
        val effectiveStage = if (overdue) (stage - 1).coerceAtLeast(0) else stage.coerceAtLeast(0)
        val base = BASE_INTERVAL_DAYS.getOrElse(effectiveStage) { BASE_INTERVAL_DAYS.last() }
        val easeFactor = ease(need)
        var interval = Math.round(base * easeFactor).coerceIn(1L, MAX_INTERVAL_DAYS)

        val reasons = mutableListOf<PlannerReason>()
        if (easeFactor < 0.9) {
            reasons += PlannerReason(PlannerReasonCode.HIGH_PERSONAL_DIFFICULTY, 1.0 - easeFactor, easeFactor)
        }
        if (need.effectiveMastery >= PlannerWeights.STRONG_ACCURACY && need.performanceConfidence >= 0.5) {
            reasons += PlannerReason(PlannerReasonCode.HIGH_RECENT_ACCURACY, easeFactor - 1.0, need.effectiveMastery)
        }
        if (overdue) {
            reasons += PlannerReason(PlannerReasonCode.REVISION_OVERDUE, 1.0, effectiveStage.toDouble())
        }

        // Perto da prova as revisões se apertam: não adianta agendar para depois do dia D.
        if (examDate != null) {
            val daysLeft = ChronoUnit.DAYS.between(today, examDate)
            if (daysLeft > 0 && daysLeft <= EXAM_TAIL_DAYS) {
                val squeezed = (daysLeft / 3).coerceAtLeast(1L)
                if (squeezed < interval) {
                    interval = squeezed
                    reasons += PlannerReason(PlannerReasonCode.EXAM_APPROACHING, 1.0, daysLeft.toDouble())
                }
            } else if (daysLeft > 0) {
                interval = interval.coerceAtMost(daysLeft)
            }
        }

        // A revisão nunca cai no passado: atrasada volta hoje, não em uma data que já passou.
        val computed = lastContact.plusDays(interval)
        val dueDate = if (computed.isBefore(today)) today else computed
        val cappedDueDate = examDate?.takeIf { it.isAfter(today) }?.let { exam ->
            if (dueDate.isAfter(exam.minusDays(1))) exam.minusDays(1) else dueDate
        } ?: dueDate

        return RevisionPlan(
            stage = if (overdue) effectiveStage else stage + 1,
            intervalDays = interval,
            dueDate = cappedDueDate,
            reasons = reasons,
        )
    }

    /** Minutos de uma sessão de revisão: tópico difícil ou fraco merece um pouco mais de tempo. */
    fun minutesFor(need: StudyNeed, blockMinutes: Int): Int {
        val half = (blockMinutes / 2).coerceAtLeast(10)
        val factor = 1.0 + (need.score - 0.5).coerceIn(-0.5, 0.5) * 0.6
        return Math.round(half * factor).toInt().coerceIn(10, blockMinutes)
    }
}
