package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import java.io.File
import kotlin.test.Test

/**
 * Sonda (PROBE=1): quanto do edital do catálogo o plano cobre, pelo mesmo caminho do app (motor, projeção e
 * mapa do edital). Relatório em build/catalog-coverage.txt. PROBE_IDS, PROBE_MIN, PROBE_EXAM, PROBE_PROF escolhem.
 */
class CatalogCoverageProbe {
    @Test fun probe() {
        if (System.getenv("PROBE") != "1") return
        val today = LocalDate(2026, 11, 23)
        val out = StringBuilder()
        val ids = (System.getenv("PROBE_IDS") ?: "bb-2022--agente-comercial,pmmg-cfsd-qppm-2025").split(",")
        val minutesList = (System.getenv("PROBE_MIN") ?: "120").split(",").map { it.toInt() }
        val exams = (System.getenv("PROBE_EXAM") ?: "none").split(",").map { it.toIntOrNull() }
        val profiles = (System.getenv("PROBE_PROF") ?: "DO_ZERO").split(",").map { StudyProfile.valueOf(it) }
        for (id in ids) for (minutes in minutesList) for (exam in exams) for (free in listOf(false, true)) for (profile in profiles) {
            val input = CatalogEditalFixtures.input(CatalogEditalFixtures.load(id), today, minutes, exam, profile = profile, free = free)
            val t0 = System.currentTimeMillis()
            val snapshot = PlannerSnapshotBuilder.build(input)
            val first = StudyPlannerEngine().plan(snapshot, ReplanReason.INITIAL)
            val theoryTopics = first.newTasks.filter { it.type == PlanTaskType.THEORY }.mapNotNull { it.topicId }.toSet().size
            val theory28 = first.newTasks.filter { it.type == PlanTaskType.THEORY && it.date < today.plusDays(28) }.mapNotNull { it.topicId }.toSet().size
            val lastTheory = first.newTasks.filter { it.type == PlanTaskType.THEORY }.maxOfOrNull { it.date }
            val t1 = System.currentTimeMillis()
            val until = exam?.let { today.plusDays(it.toLong()) } ?: today.plusYears(3)
            val p = PlanProjection.project(input, until)
            val t2 = System.currentTimeMillis()
            val rm = roadmap(input, today, exam, p, free)
            out.append("$id ${minutes}min exam=${exam ?: "-"} free=$free $profile: topics=${input.topics.size} theoryInPlan=$theoryTopics (28d=$theory28, last=$lastTheory) projected=${p.studiedOn.size} coverage=${p.coverageDate}")
            out.append(" | roadmap ${rm.verdict} left=${rm.topicsLeftOut} | plan ${t1 - t0}ms proj ${t2 - t1}ms\n")
            snapshot.notes.filter { it.contains("tópicos") && (it.contains("curto") || it.contains("Não dá") || it.contains("Sem data")) }.forEach { out.append("    nota: $it\n") }
        }
        File("build/catalog-coverage.txt").writeText(out.toString())
        println(out)
    }

    /** O mapa do edital como o app monta (StudyPlanViewModel.roadmapFor). */
    private fun roadmap(input: PlannerSnapshotBuilder.Input, today: LocalDate, exam: Int?, projected: PlanProjection.Result?, free: Boolean): EditalRoadmapResult {
        val subjects = input.planSubjects.map { RoadmapSubject(it.subjectId, it.name, StudyMethod.weightOf(it.priority, null)) }
        val estimate = WorkloadEstimator.estimate(
            pendingTopics = input.topics.map { BlueprintTopic(topicId = it.id, subjectId = it.subjectId, title = it.title, position = it.position, studied = false) },
            config = input.plan.config, needs = emptyMap(), includeRevisions = false,
        ).items.associate { it.topicId to (it.theoryMinutes + it.questionMinutes to it.theoryMinutes) }
        val topics = input.topics.map { RoadmapTopic(it.id, it.subjectId, it.position, false, estimate.getValue(it.id).first, estimate.getValue(it.id).second) }
        val weekly = input.availability.sumOf { if (it.unavailable) 0 else it.minutes }
        val end = exam?.let { today.plusDays(it.toLong()) }
            ?: (projected?.coverageDate ?: today.plusMonths(6)).let { maxOf(today.plusDays(60), it.plusDays(daysBetween(today, it) / 4 + 21)) }
        return EditalRoadmap.build(today, end, exam != null, input.plan.config.profile, weekly, subjects, topics, projected, maxTopicsPerDay = PlannerSnapshotBuilder.topicsPerDay(free))
    }
}
