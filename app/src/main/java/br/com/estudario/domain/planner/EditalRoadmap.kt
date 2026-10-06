package br.com.estudario.domain.planner

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*
import kotlinx.datetime.YearMonth
import kotlin.math.ceil

/**
 * O mapa do edital até a prova: em que mês cada parte do edital é vista pela primeira vez.
 *
 * As tarefas do plano só existem algumas semanas à frente (o plano é rolante e se refaz quando a
 * rotina muda). Para a pessoa enxergar o caminho inteiro, este cálculo projeta o resto: distribui
 * os tópicos que faltam pelos dias disponíveis, matéria por matéria na proporção do peso, com a
 * mesma regra das fases (base com mais teoria, aprofundamento com mais questões, reta final sem
 * conteúdo novo). É uma projeção honesta, não promessa: refaz a cada abertura com o que mudou.
 */
data class RoadmapSubject(val id: Long, val name: String, val weight: Int)

data class RoadmapTopic(
    val id: Long,
    val subjectId: Long,
    val position: Int,
    val studied: Boolean,
    /** Teoria + questões do tópico; a revisão sai do tempo reservado de cada fase. */
    val contentMinutes: Int,
)

data class RoadmapSubjectSlice(val subjectId: Long, val name: String, val newTopics: Int)

data class RoadmapMonth(
    val month: YearMonth,
    val phases: List<PlanPhaseKind>,
    val slices: List<RoadmapSubjectSlice>,
    val newTopics: Int,
    /** Parte do edital vista até o fim deste mês, de 0 a 100. */
    val coveragePercent: Int,
) {
    /** Mês sem conteúdo novo: só revisão, questões e simulados. */
    val reviewOnly: Boolean get() = newTopics == 0
}

enum class RoadmapVerdict { COMFORTABLE, TIGHT, DOES_NOT_FIT }

data class EditalRoadmapResult(
    val start: LocalDate,
    val end: LocalDate,
    val hasExamDate: Boolean,
    val phases: List<StudyPhase>,
    val months: List<RoadmapMonth>,
    val totalTopics: Int,
    val studiedTopics: Int,
    /** Dia em que o último tópico pendente é visto; null quando não cabe tudo. */
    val coverageDate: LocalDate?,
    /** Último dia para conteúdo novo (antes da reta final). */
    val contentDeadline: LocalDate,
    val verdict: RoadmapVerdict,
    val topicsLeftOut: Int,
    /** Matérias com tópicos que ficam de fora, para a pessoa saber o que está em risco. */
    val atRiskSubjects: List<String>,
    /** Minutos a mais por dia que fariam tudo caber até [contentDeadline]. */
    val extraMinutesPerDayToFit: Int,
    val weeklyCapacityMinutes: Int,
) {
    val slackDays: Long get() = coverageDate?.let { daysBetween(it, contentDeadline) } ?: 0
}

object EditalRoadmap {
    /** Fatia de cada dia que vai para conteúdo novo; o resto é revisão, questões e simulados. */
    fun contentShare(kind: PlanPhaseKind): Double = when (kind) {
        PlanPhaseKind.BASE -> 0.8
        PlanPhaseKind.APROFUNDAMENTO -> 0.55
        PlanPhaseKind.RETA_FINAL -> 0.0
    }

    /** Folga mínima (dias entre ver o último tópico e a reta final) para chamar de confortável. */
    const val COMFORTABLE_SLACK_DAYS = 14

    fun build(
        today: LocalDate,
        end: LocalDate,
        hasExamDate: Boolean,
        profile: StudyProfile,
        weeklyCapacityMinutes: Int,
        subjects: List<RoadmapSubject>,
        topics: List<RoadmapTopic>,
    ): EditalRoadmapResult {
        val safeEnd = if (end.isAfter(today)) end else today.plusDays(1)
        val phases = StudyMethod.phases(today, safeEnd, profile)
        val dailyCapacity = weeklyCapacityMinutes.coerceAtLeast(0) / 7.0
        val contentDeadline = phases.firstOrNull { it.kind == PlanPhaseKind.RETA_FINAL }?.start?.minusDays(1)
            ?.takeIf { !it.isBefore(today) } ?: safeEnd
        val subjectById = subjects.associateBy { it.id }
        val known = topics.filter { it.subjectId in subjectById }
        val pending = known.filter { !it.studied }

        // Fila por matéria na ordem do edital; a próxima vem da matéria mais "atrasada" em relação
        // ao peso dela (rodízio proporcional, igual ao do motor que monta as tarefas).
        val queues = pending.groupBy { it.subjectId }.mapValues { (_, list) -> ArrayDeque(list.sortedBy { it.position }) }.toMutableMap()
        val given = subjects.associate { it.id to 0.0 }.toMutableMap()
        fun nextTopic(): RoadmapTopic? {
            val subject = queues.filterValues { it.isNotEmpty() }.keys.minWithOrNull(
                compareBy<Long> { given.getValue(it) / subjectById.getValue(it).weight.coerceAtLeast(1) }.thenBy { it },
            ) ?: return null
            val topic = queues.getValue(subject).removeFirst()
            given[subject] = given.getValue(subject) + topic.contentMinutes.coerceAtLeast(1)
            return topic
        }

        val assignedOn = mutableMapOf<Long, LocalDate>()
        var budget = 0.0
        var current = nextTopic()
        var day = today
        var contentCapacityDays = 0.0
        while (!day.isAfter(contentDeadline)) {
            val share = contentShare(StudyMethod.phaseAt(phases, day).kind)
            contentCapacityDays += share
            budget += dailyCapacity * share
            while (current != null && budget >= current.contentMinutes.coerceAtLeast(1)) {
                budget -= current.contentMinutes.coerceAtLeast(1)
                assignedOn[current.id] = day
                current = nextTopic()
            }
            day = day.plusDays(1)
        }
        val leftOut = pending.filter { it.id !in assignedOn }
        val coverageDate = if (leftOut.isEmpty()) assignedOn.values.maxOrNull() ?: today else null

        val studiedCount = known.count { it.studied }
        var seen = studiedCount
        val months = generateSequence(today.toYearMonth()) { it.plusMonths(1) }
            .takeWhile { it <= safeEnd.toYearMonth() }
            .map { month ->
                val inMonth = pending.filter { assignedOn[it.id]?.toYearMonth() == month }
                seen += inMonth.size
                val monthStart = maxOf(month.firstDay, today)
                val monthEnd = minOf(month.lastDay, safeEnd)
                RoadmapMonth(
                    month = month,
                    phases = phases.filter { !it.end.isBefore(monthStart) && !it.start.isAfter(monthEnd) }.map { it.kind }.distinct(),
                    slices = inMonth.groupBy { it.subjectId }
                        .map { (id, list) -> RoadmapSubjectSlice(id, subjectById.getValue(id).name, list.size) }
                        .sortedByDescending { it.newTopics },
                    newTopics = inMonth.size,
                    coveragePercent = if (known.isEmpty()) 0 else seen * 100 / known.size,
                )
            }.toList()

        val pendingMinutes = pending.sumOf { it.contentMinutes.coerceAtLeast(1) }
        val extraPerDay = if (leftOut.isEmpty() || contentCapacityDays <= 0.0) 0
        else ceil(((pendingMinutes - dailyCapacity * contentCapacityDays) / contentCapacityDays).coerceAtLeast(0.0)).toInt()
        val verdict = when {
            leftOut.isNotEmpty() -> RoadmapVerdict.DOES_NOT_FIT
            coverageDate != null && daysBetween(coverageDate, contentDeadline) >= COMFORTABLE_SLACK_DAYS -> RoadmapVerdict.COMFORTABLE
            else -> RoadmapVerdict.TIGHT
        }
        return EditalRoadmapResult(
            start = today,
            end = safeEnd,
            hasExamDate = hasExamDate,
            phases = phases,
            months = months,
            totalTopics = known.size,
            studiedTopics = studiedCount,
            coverageDate = coverageDate,
            contentDeadline = contentDeadline,
            verdict = verdict,
            topicsLeftOut = leftOut.size,
            atRiskSubjects = leftOut.groupBy { it.subjectId }.entries.sortedByDescending { it.value.size }.map { subjectById.getValue(it.key).name },
            extraMinutesPerDayToFit = extraPerDay,
            weeklyCapacityMinutes = weeklyCapacityMinutes,
        )
    }
}
