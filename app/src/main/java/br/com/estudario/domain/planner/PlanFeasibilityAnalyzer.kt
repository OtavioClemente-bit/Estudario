package br.com.estudario.domain.planner

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

/**
 * O plano cabe no tempo que existe?
 *
 * Esta é a pergunta que um planejador honesto precisa responder antes de montar qualquer
 * cronograma. Se o edital não cabe até a prova, o app **não finge que cabe**: ele diz, mostra a
 * conta e oferece caminhos. E não decide sozinho, quem escolhe entre aumentar a carga e priorizar
 * o que mais vale é a pessoa.
 */
enum class FeasibilityVerdict {
    /** Sem data de prova: a projeção existe, a viabilidade não faz sentido. */
    NO_EXAM_DATE,

    /** Termina o conteúdo antes da meta e ainda sobra fase de consolidação. */
    COMFORTABLE,

    /** Termina depois da meta de cobertura, mas antes da prova. */
    TIGHT,

    /** Não termina antes da prova com a carga atual. */
    INFEASIBLE,
}

/** Um caminho oferecido à pessoa. O motor calcula o efeito; a decisão é dela. */
sealed interface FeasibilityOption {
    /**
     * Aumentar a carga semanal. [extraWeeklyMinutes] é o acréscimo mínimo que resolve, e
     * [projectedCoverageDate] é a data de conclusão resultante.
     */
    data class IncreaseWeeklyLoad(
        val extraWeeklyMinutes: Int,
        val projectedCoverageDate: LocalDate,
        val daysSaved: Long,
    ) : FeasibilityOption

    /**
     * Manter a carga e concentrar no que mais vale. [coveredShare] é a fração do edital que cabe,
     * ordenada por necessidade, o resto **não some**: fica visível como conteúdo em risco.
     */
    data class PrioritizeByWeight(
        val coveredShare: Double,
        val atRiskSubjectIds: List<Long>,
    ) : FeasibilityOption

    /** Adiar a meta de cobertura, reduzindo a fase final de consolidação até o mínimo seguro. */
    data class ShortenConsolidation(
        val newConsolidationDays: Int,
        val newTargetCoverageDate: LocalDate,
    ) : FeasibilityOption
}

data class FeasibilityReport(
    val verdict: FeasibilityVerdict,
    val weeklyCapacityMinutes: Int,
    val estimatedRemainingWorkloadMinutes: Int,
    val examDate: LocalDate?,
    /** Data em que o conteúdo precisa terminar para sobrar a fase de consolidação. */
    val targetCoverageDate: LocalDate?,
    /** Data em que o conteúdo termina de fato, com a carga atual. */
    val projectedCoverageDate: LocalDate?,
    val consolidationDays: Int,
    val deficitMinutes: Int,
    val options: List<FeasibilityOption>,
    val reasons: List<PlannerReason>,
)

object PlanFeasibilityAnalyzer {

    /**
     * Margem antes da prova, em dias.
     *
     * Nada de "sempre 30 dias". A margem é uma fração do tempo total de preparação, presa entre um
     * mínimo que faz diferença e um máximo que não desperdiça preparação longa, e cresce um pouco
     * com o tamanho do edital, porque mais conteúdo exige mais tempo de revisão final.
     */
    fun consolidationDays(totalPreparationDays: Long, topicCount: Int): Int {
        if (totalPreparationDays <= 0) return MIN_CONSOLIDATION_DAYS
        val proportional = totalPreparationDays * CONSOLIDATION_SHARE
        val syllabusBonus = (topicCount / TOPICS_PER_EXTRA_DAY.toDouble())
        return (proportional + syllabusBonus).toInt().coerceIn(MIN_CONSOLIDATION_DAYS, MAX_CONSOLIDATION_DAYS)
    }

    /**
     * Analisa a viabilidade do plano.
     *
     * @param needsBySubject necessidade por matéria, usada só para dizer *qual* conteúdo entra em
     *   risco quando o tempo não dá, nunca para excluir nada em silêncio.
     */
    fun analyze(
        today: LocalDate,
        examDate: LocalDate?,
        planStart: LocalDate,
        weeklyCapacityMinutes: Int,
        workload: WorkloadEstimate,
        topicCount: Int,
        needsBySubject: Map<Long, StudyNeed> = emptyMap(),
    ): FeasibilityReport {
        val remaining = workload.totalMinutes
        val dailyCapacity = weeklyCapacityMinutes / 7.0

        val projected = if (dailyCapacity <= 0.0 || remaining <= 0) null
        else today.plusDays(ceil(remaining / dailyCapacity).toLong())

        if (examDate == null || !examDate.isAfter(today)) {
            return FeasibilityReport(
                verdict = FeasibilityVerdict.NO_EXAM_DATE,
                weeklyCapacityMinutes = weeklyCapacityMinutes,
                estimatedRemainingWorkloadMinutes = remaining,
                examDate = examDate,
                targetCoverageDate = null,
                projectedCoverageDate = projected,
                consolidationDays = 0,
                deficitMinutes = 0,
                options = emptyList(),
                reasons = emptyList(),
            )
        }

        val totalPreparationDays = ChronoUnit.DAYS.between(planStart, examDate).coerceAtLeast(1)
        val consolidation = consolidationDays(totalPreparationDays, topicCount)
        val target = examDate.minusDays(consolidation.toLong()).let { if (it.isBefore(today)) today else it }

        val daysToTarget = ChronoUnit.DAYS.between(today, target).coerceAtLeast(0)
        val daysToExam = ChronoUnit.DAYS.between(today, examDate).coerceAtLeast(0)
        val capacityToTarget = (dailyCapacity * daysToTarget).toInt()
        val capacityToExam = (dailyCapacity * daysToExam).toInt()
        val deficit = (remaining - capacityToTarget).coerceAtLeast(0)

        val verdict = when {
            remaining <= capacityToTarget -> FeasibilityVerdict.COMFORTABLE
            remaining <= capacityToExam -> FeasibilityVerdict.TIGHT
            else -> FeasibilityVerdict.INFEASIBLE
        }

        val reasons = buildList {
            if (verdict != FeasibilityVerdict.COMFORTABLE) {
                add(PlannerReason(PlannerReasonCode.EXAM_APPROACHING, 1.0, daysToExam.toDouble()))
            }
            if (verdict == FeasibilityVerdict.INFEASIBLE) {
                add(PlannerReason(PlannerReasonCode.CONTENT_REMAINING, 1.0, remaining.toDouble()))
            }
        }

        val options = if (verdict == FeasibilityVerdict.COMFORTABLE) emptyList() else buildList {
            // 1. Aumentar a carga: quanto a mais por semana faz o conteúdo caber na meta.
            if (daysToTarget > 0) {
                val requiredDaily = remaining.toDouble() / daysToTarget
                val requiredWeekly = ceil(requiredDaily * 7).toInt()
                val extra = (requiredWeekly - weeklyCapacityMinutes).coerceAtLeast(0)
                if (extra > 0) {
                    val newDaily = requiredWeekly / 7.0
                    val newProjection = today.plusDays(ceil(remaining / newDaily).toLong())
                    val saved = projected?.let { ChronoUnit.DAYS.between(newProjection, it) } ?: 0
                    add(
                        FeasibilityOption.IncreaseWeeklyLoad(
                            extraWeeklyMinutes = roundToQuarterHour(extra),
                            projectedCoverageDate = newProjection,
                            daysSaved = saved.coerceAtLeast(0),
                        ),
                    )
                }
            }
            // 2. Manter a carga e priorizar peso. Nada é excluído: o que não cabe fica sinalizado.
            if (remaining > 0) {
                val coveredShare = (capacityToTarget.toDouble() / remaining).coerceIn(0.0, 1.0)
                val atRisk = atRiskSubjects(workload, needsBySubject, capacityToTarget)
                add(FeasibilityOption.PrioritizeByWeight(coveredShare, atRisk))
            }
            // 3. Encurtar a consolidação, até o mínimo que ainda protege a reta final.
            if (consolidation > MIN_CONSOLIDATION_DAYS && verdict == FeasibilityVerdict.TIGHT) {
                val shorter = MIN_CONSOLIDATION_DAYS
                add(
                    FeasibilityOption.ShortenConsolidation(
                        newConsolidationDays = shorter,
                        newTargetCoverageDate = examDate.minusDays(shorter.toLong()),
                    ),
                )
            }
        }

        return FeasibilityReport(
            verdict = verdict,
            weeklyCapacityMinutes = weeklyCapacityMinutes,
            estimatedRemainingWorkloadMinutes = remaining,
            examDate = examDate,
            targetCoverageDate = target,
            projectedCoverageDate = projected,
            consolidationDays = consolidation,
            deficitMinutes = deficit,
            options = options,
            reasons = reasons,
        )
    }

    /**
     * Quais matérias ficam em risco se a carga não mudar.
     *
     * A ordem é a da necessidade: o motor preenche a capacidade com o que mais precisa e o que
     * sobra é o conteúdo que a pessoa precisa saber que está descoberto. Isso é o oposto de
     * excluir matéria em silêncio.
     */
    private fun atRiskSubjects(
        workload: WorkloadEstimate,
        needsBySubject: Map<Long, StudyNeed>,
        capacityMinutes: Int,
    ): List<Long> {
        val bySubject = workload.items
            .groupBy { it.subjectId }
            .mapValues { (_, items) -> items.sumOf { it.totalMinutes } }
        val ordered = bySubject.keys.sortedWith(
            compareByDescending<Long> { needsBySubject[it]?.score ?: 0.0 }.thenBy { it },
        )
        var budget = capacityMinutes
        val atRisk = mutableListOf<Long>()
        ordered.forEach { subjectId ->
            val cost = bySubject.getValue(subjectId)
            if (budget >= cost) budget -= cost else atRisk += subjectId
        }
        return atRisk
    }

    private fun roundToQuarterHour(minutes: Int): Int =
        ((minutes + 14) / 15) * 15

    /** Fração do tempo total reservada para consolidação antes da prova. */
    private const val CONSOLIDATION_SHARE = 0.18

    /** Abaixo disso a reta final deixa de existir de verdade. */
    const val MIN_CONSOLIDATION_DAYS = 14

    /** Acima disso vira tempo ocioso mesmo em preparação longa. */
    const val MAX_CONSOLIDATION_DAYS = 50

    /** A cada tantos tópicos, um dia a mais de consolidação. */
    private const val TOPICS_PER_EXTRA_DAY = 12
}
