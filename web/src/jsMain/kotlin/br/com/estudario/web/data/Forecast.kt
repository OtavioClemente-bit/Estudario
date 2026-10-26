package br.com.estudario.web.data

import br.com.estudario.domain.planner.BlueprintTopic
import br.com.estudario.domain.planner.EditalRoadmap
import br.com.estudario.domain.planner.EditalRoadmapResult
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanProjection
import br.com.estudario.domain.planner.RoadmapSubject
import br.com.estudario.domain.planner.RoadmapTopic
import br.com.estudario.domain.planner.StudyMethod
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.domain.planner.WorkloadEstimator
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import br.com.estudario.time.*

/**
 * Previsão do edital inteiro, igual ao roadmapFor do app (StudyPlanViewModel): distribui os tópicos
 * que faltam pelos dias disponíveis até a prova. As tarefas do plano só existem 4 semanas à frente;
 * a conta antiga, feita só com elas, dizia "edital coberto em 4 semanas" faltando meses.
 */
object Forecast {
    @Serializable
    data class Result(
        val roadmap: RoadmapSummary,
        /** Quando o edital termina no ritmo atual, sem prazo (passa da prova quando não cabe). */
        val finishAtPace: LocalDate?,
    ) {
        val date: LocalDate? get() = roadmap.coverageDate ?: finishAtPace
        val remainingTopics: Int get() = roadmap.totalTopics - roadmap.studiedTopics
    }

    /** Só os números usados na interface atravessam a fronteira do worker. */
    @Serializable
    data class RoadmapSummary(
        val totalTopics: Int,
        val studiedTopics: Int,
        val coverageDate: LocalDate?,
        val contentDeadline: LocalDate,
        val verdict: br.com.estudario.domain.planner.RoadmapVerdict,
        val topicsLeftOut: Int,
        val atRiskSubjects: List<String>,
        val extraMinutesPerDayToFit: Int,
    ) {
        val slackDays: Long get() = coverageDate?.let { daysBetween(it, contentDeadline) } ?: 0
    }

    private fun summary(r: EditalRoadmapResult) = RoadmapSummary(r.totalTopics, r.studiedTopics, r.coverageDate, r.contentDeadline, r.verdict, r.topicsLeftOut, r.atRiskSubjects, r.extraMinutesPerDayToFit)

    /** Executado exclusivamente no worker, nunca durante a composição das telas. */
    internal fun compute(data: Snapshot, plan: StudyPlan, freeRules: Boolean, excludedTopicIds: Set<Long>): Result? {
        val today = Queries.todayDate()
        val weekly = data.availability.filter { it.planId == plan.id && !it.unavailable }.sumOf { it.minutes }
        val profile = plan.profile?.let { runCatching { StudyProfile.valueOf(it) }.getOrNull() } ?: StudyProfile.DO_ZERO
        val active = data.planSubjects.filter { it.planId == plan.id && !it.paused }
        if (active.isEmpty()) return null
        val subjects = active.map {
            val priority = runCatching { PlanPriority.valueOf(it.priority) }.getOrDefault(PlanPriority.MEDIUM)
            RoadmapSubject(it.subjectId, it.name, StudyMethod.weightOf(priority, null))
        }
        val leaves = Queries.leafTopics(data, active.mapTo(hashSetOf()) { it.subjectId })
        val base = StudyMethodConfig.forProfile(profile)
        val config = plan.block?.let { base.copy(blockMinutes = it) } ?: base
        val estimate = WorkloadEstimator.estimate(
            pendingTopics = leaves.map { BlueprintTopic(topicId = it.id, subjectId = it.subjectId, title = it.title, position = it.position, studied = false) },
            config = config,
            needs = emptyMap(),
            includeRevisions = false,
        ).items.associate { it.topicId to (it.theoryMinutes + it.questionMinutes to it.theoryMinutes) }
        val topics = leaves.map { RoadmapTopic(it.id, it.subjectId, it.position, Queries.isStudied(it), estimate[it.id]?.first ?: config.blockMinutes, estimate[it.id]?.second ?: config.blockMinutes) }
        // O próprio plano simulado semana a semana (PlanProjection), como no app.
        val input = Planning.inputFor(data, plan.id, freeRules, excludedTopicIds)
        fun project(end: LocalDate, withExam: Boolean) = input?.let {
            runCatching { PlanProjection.project(if (withExam) it else it.copy(plan = it.plan.copy(examEpochDay = null)), end) }.getOrNull()
        }
        fun build(end: LocalDate, hasExam: Boolean, projected: PlanProjection.Result?) = EditalRoadmap.build(today, end, hasExam, profile, weekly, subjects, topics, projected, maxTopicsPerDay = br.com.estudario.domain.planner.PlannerSnapshotBuilder.topicsPerDay(freeRules))
        val exam = plan.exam?.let { LocalDate.fromEpochDays(it.toInt()) }?.takeIf { it > today }
        if (exam != null) {
            val roadmap = build(exam, hasExam = true, project(exam, withExam = true))
            val finish = roadmap.coverageDate ?: project(today.plusYears(3), withExam = false)?.coverageDate
            return Result(summary(roadmap), finish)
        }
        val open = project(today.plusYears(3), withExam = false)
        val finishAtPace = open?.coverageDate
        return Result(summary(build(maxOf(today.plusDays(60), (finishAtPace ?: today.plusMonths(6)).plusDays(21)), hasExam = false, open)), finishAtPace)
    }
}
