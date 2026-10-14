package br.com.estudario.domain.planner

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import br.com.estudario.time.*
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Auditoria da previsão: o caminho inteiro, como no app e no site. O plano é montado do edital
 * (PlannerSnapshotBuilder + motor), a pessoa estuda todo dia o que o plano manda (tarefa por tarefa,
 * replanejando a cada conclusão) e, no fim, o dia em que o último tópico foi estudado de fato é
 * comparado com o que a previsão (EditalRoadmap) prometeu no primeiro dia.
 */
class StudyPlanForecastAuditTest {
    private val engine = StudyPlannerEngine()
    private val start = LocalDate(2026, 10, 12) // segunda
    private val config = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO)
    // 98 min de segunda a sexta, 3 h no sábado, domingo livre: 670 min por semana.
    private val week = listOf(98, 98, 98, 98, 98, 180, 0)

    private data class Outcome(val promised: EditalRoadmapResult, val lastTopicDay: LocalDate?, val studiedByExam: Int, val total: Int, val maxDay: Int)

    @Test
    fun `apertado mas cabendo a previsao nao da alarme falso`() {
        val outcome = run(subjects = 6, topicsPerSubject = 10, examInDays = 75)
        println("Previsão: ${outcome.promised.verdict}, de fora ${outcome.promised.topicsLeftOut} · plano real: ${outcome.studiedByExam}/${outcome.total} até a prova")
        assertTrue("O plano real cobriu tudo; a previsão não pode dizer que não cabe", outcome.promised.verdict != RoadmapVerdict.DOES_NOT_FIT)
        assertTrue(outcome.studiedByExam == outcome.total)
    }

    @Test
    fun `com folga a previsao bate com o dia em que o plano de verdade fecha o edital`() {
        val outcome = run(subjects = 6, topicsPerSubject = 10, examInDays = 300)
        println("Previsão: ${outcome.promised.coverageDate} (${outcome.promised.verdict}) · plano real: ${outcome.lastTopicDay}")
        val promised = outcome.promised.coverageDate
        assertTrue("A previsão disse que não cabe com ${outcome.promised.weeklyCapacityMinutes} min/semana", promised != null)
        assertTrue("O plano real não cobriu o edital (${outcome.studiedByExam}/${outcome.total})", outcome.lastTopicDay != null)
        val gap = daysBetween(promised!!, outcome.lastTopicDay!!)
        // A previsão pode errar alguns dias (é uma média diária), mas nunca prometer semanas antes do real.
        assertTrue("Plano real fechou $gap dias depois da previsão ($promised → ${outcome.lastTopicDay})", gap <= 7)
        assertTrue("Plano real fechou ${-gap} dias antes da previsão ($promised → ${outcome.lastTopicDay})", gap >= -7)
        assertTrue("Dia com ${outcome.maxDay} min para no máximo 180 + bloco", outcome.maxDay <= 180 + config.blockMinutes)
    }

    @Test
    fun `quando nao cabe a previsao avisa e o que fica de fora bate com o plano real`() {
        val outcome = run(subjects = 6, topicsPerSubject = 10, examInDays = 35)
        println("Previsão: ${outcome.promised.verdict}, de fora ${outcome.promised.topicsLeftOut} · plano real: ${outcome.studiedByExam}/${outcome.total} até a prova")
        assertTrue("A previsão deveria avisar que não cabe", outcome.promised.verdict == RoadmapVerdict.DOES_NOT_FIT)
        val realLeftOut = outcome.total - outcome.studiedByExam
        val diff = kotlin.math.abs(realLeftOut - outcome.promised.topicsLeftOut)
        assertTrue("Plano real deixou $realLeftOut de fora; a previsão disse ${outcome.promised.topicsLeftOut}", diff <= 3)
        assertTrue("Faltando tópicos, a previsão precisa dizer quantos minutos a mais por dia resolveriam", outcome.promised.extraMinutesPerDayToFit > 0)
    }

    private fun run(subjects: Int, topicsPerSubject: Int, examInDays: Int): Outcome {
        val exam = start.plusDays(examInDays)
        val planSubjects = (1..subjects).map { s ->
            PlannerSnapshotBuilder.PlanSubject(
                subjectId = s.toLong(), name = "Matéria $s", priority = if (s <= 2) PlanPriority.HIGH else PlanPriority.MEDIUM,
                paused = false, minimumMaintenanceMinutes = 0, weightOverride = null, position = s,
                personalDifficulty = PersonalDifficulty.NORMAL, initialKnowledge = InitialKnowledge.NONE,
            )
        }
        val studied = mutableMapOf<Long, LocalDate>()
        val topicIds = (1..subjects).flatMap { s -> (1..topicsPerSubject).map { t -> s.toLong() to (s * 100L + t) } }
        fun topics() = topicIds.map { (s, id) ->
            PlannerSnapshotBuilder.Topic(id, s, null, "Tópico $id", (id % 100).toInt(), studied.containsKey(id), studied[id]?.let { it.toEpochDays() * 86_400_000L })
        }
        // Revisões D+1, D+7 e D+30 de cada tópico estudado, como o app agenda (id, tópico, dia, feita).
        data class Review(val id: Long, val topicId: Long, val due: LocalDate, var done: Boolean = false)
        val reviews = mutableListOf<Review>()
        val tasks = mutableListOf<PlannerTask>()
        val executions = mutableListOf<TaskExecution>()
        var revision = 0L
        val plan = PlannerSnapshotBuilder.Plan("plan", 0, start.toEpochDays().toLong(), exam.toEpochDays().toLong(), config)

        fun input(day: LocalDate) = PlannerSnapshotBuilder.Input(
                    plan = plan.copy(revision = revision), planSubjects = planSubjects, topics = topics(),
                    questionsByTopic = emptyMap(), attempts = emptyList(),
                    pendingReviews = reviews.filter { !it.done }.map { PlannerSnapshotBuilder.PendingReview(it.id, it.topicId, it.due.toEpochDays() * 86_400_000L, 0) },
                    tasks = tasks.toList(), executions = executions.toList(),
                    availability = week.mapIndexed { i, m -> PlannerSnapshotBuilder.Availability(i + 1, m, m == 0) },
                    dayOverrides = emptyList(), today = day, zone = TimeZone.UTC,
                )
        fun replan(day: LocalDate, reason: ReplanReason): PlanningProposal {
            val snapshot = PlannerSnapshotBuilder.build(input(day))
            val proposal = engine.plan(snapshot, reason)
            proposal.transitions.forEach { t -> tasks.replaceAll { if (it.id == t.taskId) it.copy(status = t.to) else it } }
            tasks.removeAll { it.status == PlanTaskStatus.REPROGRAMADA || it.status == PlanTaskStatus.NAO_REALIZADA }
            tasks += proposal.newTasks
            revision++
            return proposal
        }

        // A promessa do primeiro dia, com a mesma conta do app e do site.
        val estimate = WorkloadEstimator.estimate(
            pendingTopics = topics().map { BlueprintTopic(it.id, it.subjectId, it.title, it.position) },
            config = config, needs = emptyMap(), includeRevisions = false,
        ).items.associateBy { it.topicId }
        val promised = EditalRoadmap.build(
            start, exam, true, config.profile, week.sum(),
            planSubjects.map { RoadmapSubject(it.subjectId, it.name, StudyMethod.weightOf(it.priority, null)) },
            topics().map { RoadmapTopic(it.id, it.subjectId, it.position, false, estimate.getValue(it.id).let { e -> e.theoryMinutes + e.questionMinutes }, estimate.getValue(it.id).theoryMinutes) },
            projected = PlanProjection.project(input(start), exam),
        )

        var day = start
        var maxDay = 0
        replan(day, ReplanReason.INITIAL)
        while (day < exam) {
            if (day != start) replan(day, ReplanReason.DAY_MISSED)
            var spent = 0
            var guard = 0
            while (guard++ < 40) {
                val task = tasks.firstOrNull { it.date == day && it.status == PlanTaskStatus.PLANEJADA } ?: break
                tasks.replaceAll { if (it.id == task.id) it.copy(status = PlanTaskStatus.CONCLUIDA) else it }
                executions += TaskExecution("e-${task.id}", task.id, task.subjectId, task.topicId, day, task.plannedMinutes, task.plannedQuestions, 0)
                spent += task.plannedMinutes
                if (task.type == PlanTaskType.THEORY && task.topicId != null && task.topicId !in studied) {
                    studied[task.topicId!!] = day
                    listOf(1, 7, 30).forEach { d -> reviews += Review(reviews.size + 1L, task.topicId!!, day.plusDays(d)) }
                }
                if (task.type == PlanTaskType.REVIEW && task.topicId != null) {
                    reviews.filter { it.topicId == task.topicId && !it.done && !it.due.isAfter(day.plusDays(1)) }.minByOrNull { it.due }?.done = true
                }
                replan(day, ReplanReason.TASK_COMPLETED)
            }
            assertTrue("Dia $day não terminou", guard < 40)
            maxDay = maxOf(maxDay, spent)
            day = day.plusDays(1)
        }
        val byType = executions.groupBy { e -> tasks.firstOrNull { it.id == e.taskId }?.type }.mapValues { (_, l) -> l.sumOf { it.minutes } }
        println("Prova $exam · reta final a partir de ${promised.contentDeadline} · fases ${promised.phases.map { it.kind.name + ":" + it.start + "/" + it.mix.theoryPercent }} · minutos por tipo $byType · último tópico ${studied.values.maxOrNull()} · estudado/dia ~${executions.sumOf { it.minutes } / daysBetween(start, exam)}")
        val all = topicIds.map { it.second }
        val lastTopicDay = if (all.all(studied::containsKey)) studied.values.max() else null
        return Outcome(promised, lastTopicDay, studied.size, all.size, maxDay)
    }
}
