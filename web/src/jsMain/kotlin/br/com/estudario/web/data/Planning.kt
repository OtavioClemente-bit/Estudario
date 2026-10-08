package br.com.estudario.web.data

import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanOrigin
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.planner.PlannerSnapshotBuilder
import br.com.estudario.domain.planner.PlannerTask
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.planner.StudyMethod
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyPlannerEngine
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.domain.planner.TaskExecution
import br.com.estudario.time.isoDayOfWeek
import br.com.estudario.time.toEpochDay
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.js.Date
import kotlin.random.Random

/**
 * Criar o plano e replanejar no app web, com as mesmas regras do StudyPlanApplicationService do
 * Android: o retrato vem do [PlannerSnapshotBuilder] e as tarefas do [StudyPlannerEngine], os dois
 * no módulo comum.
 */
object Planning {
    data class SubjectChoice(
        val subjectId: Long,
        val name: String,
        val priority: PlanPriority = PlanPriority.MEDIUM,
        val weight: Int = 3,
        val personalDifficulty: PersonalDifficulty = PersonalDifficulty.NORMAL,
        val initialKnowledge: InitialKnowledge = InitialKnowledge.NONE,
    )

    data class NewPlan(
        val competitionId: Long,
        val name: String,
        val objective: String,
        val startDate: LocalDate,
        val examDate: LocalDate?,
        /** Minutos por dia da semana, de segunda (índice 0) a domingo (6); 0 = folga. */
        val weeklyMinutes: List<Int>,
        val subjects: List<SubjectChoice>,
        val method: StudyMethodConfig,
    )

    private fun now(): Long = Date.now().toLong()

    private fun uuid(): String {
        val bytes = Random.nextBytes(16)
        bytes[6] = ((bytes[6].toInt() and 0x0f) or 0x40).toByte()
        bytes[8] = ((bytes[8].toInt() and 0x3f) or 0x80).toByte()
        val hex = bytes.joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
        return "${hex.substring(0, 8)}-${hex.substring(8, 12)}-${hex.substring(12, 16)}-${hex.substring(16, 20)}-${hex.substring(20)}"
    }

    /** StudyPlanApplicationService.createPlan + ativar + primeiro planejamento. Devolve (foto, id do plano). */
    fun create(data: Snapshot, input: NewPlan): Pair<Snapshot, String> {
        require(input.name.isNotBlank()) { "Informe um nome para o plano." }
        require(input.weeklyMinutes.any { it > 0 }) { "Informe pelo menos um dia disponível." }
        require(input.subjects.isNotEmpty()) { "Escolha pelo menos uma matéria." }
        val id = uuid()
        val at = now()
        val method = input.method
        val weekly = input.weeklyMinutes.sum()
        val phases = StudyMethod.phases(input.startDate, input.examDate, method.profile)
        val startMonth = "${input.startDate.year}-${(input.startDate.month.ordinal + 1).toString().padStart(2, '0')}"
        val weekStart = input.startDate.toEpochDay() - (input.startDate.isoDayOfWeek - 1)
        var next = data
            // Um plano ativo por concurso, como em activateOnly no app.
            .edit(Keys.PLANS) { if ((it["competitionId"] as? JsonPrimitive)?.longOrNull == input.competitionId) it.with("active" to JsonPrimitive(false)) else it }
            .appendAll(
                mapOf(
                    Keys.PLANS to listOf(
                        jsonOf(
                            "id" to id, "competitionId" to input.competitionId, "name" to input.name.trim(), "objective" to input.objective.trim().ifBlank { input.name.trim() },
                            "start" to input.startDate.toEpochDay(), "exam" to input.examDate?.toEpochDay(), "active" to true, "master" to false, "archived" to false,
                            "revision" to 0, "createdAt" to at, "updatedAt" to at, "profile" to method.profile.name, "block" to method.blockMinutes,
                            "weeklyQuestions" to method.weeklyQuestionsTarget, "topicQuestions" to method.questionsPerTopic, "simulations" to method.simulationsPerMonth,
                            "discursives" to method.discursivesPerMonth, "interleave" to method.interleaveSubjects, "dailyShare" to method.dailySubjectSharePercent,
                        ),
                    ),
                    Keys.PLAN_REVISIONS to listOf(jsonOf("planId" to id, "revision" to 0, "base" to 0, "reason" to "CREATED", "proposalId" to null, "summary" to "Plano criado.", "createdAt" to at)),
                    Keys.AVAILABILITY to input.weeklyMinutes.mapIndexed { index, minutes ->
                        jsonOf("planId" to id, "day" to index + 1, "minutes" to minutes.coerceAtLeast(0), "unavailable" to (minutes <= 0), "mode" to "FIXED")
                    },
                    Keys.PLAN_SUBJECTS to input.subjects.mapIndexed { index, subject ->
                        jsonOf(
                            "planId" to id, "subjectId" to subject.subjectId, "name" to subject.name, "priority" to subject.priority.name, "paused" to false,
                            "maintenance" to 0, "weight" to subject.weight.coerceIn(1, 5), "position" to index,
                            "personalDifficulty" to subject.personalDifficulty.name, "initialKnowledge" to subject.initialKnowledge.name,
                        )
                    },
                    "annualPhases" to phases.mapIndexed { index, phase ->
                        jsonOf(
                            "id" to uuid(), "planId" to id, "position" to index, "name" to phase.kind.label, "objective" to phase.objective, "criteria" to phase.criteria,
                            "start" to phase.start.toEpochDay(), "end" to phase.end.toEpochDay(), "minutes" to weekly * phase.days / 7,
                            "questions" to method.weeklyQuestionsTarget * phase.days / 7, "discursives" to method.discursivesPerMonth * phase.days / 30, "percent" to 100,
                            "fromRevision" to 0, "untilRevision" to null,
                        )
                    },
                    "monthlyPlans" to listOf(
                        jsonOf("id" to uuid(), "planId" to id, "yearMonth" to startMonth, "focus" to input.objective.trim().ifBlank { input.name.trim() }, "minutes" to weekly * 4, "questions" to method.weeklyQuestionsTarget * 4, "discursives" to method.discursivesPerMonth, "percent" to 100, "fromRevision" to 0, "untilRevision" to null),
                    ),
                    "weeklyPlans" to listOf(
                        jsonOf("id" to uuid(), "planId" to id, "weekStart" to weekStart, "objective" to input.objective.trim().ifBlank { input.name.trim() }, "minutes" to weekly, "questions" to 0, "discursives" to 0, "fromRevision" to 0, "untilRevision" to null),
                    ),
                ),
            )
        next = replan(next, id, ReplanReason.INITIAL)
        return next to id
    }

    /** StudyPlanApplicationService.replan + applyProposal. */
    fun replan(data: Snapshot, planId: String, reason: ReplanReason): Snapshot {
        val planRow = data.array(Keys.PLANS).firstOrNull { (it as? JsonObject)?.str("id") == planId } as? JsonObject ?: return data
        if (planRow.bool("archived") == true) return data
        val revision = planRow.long("revision") ?: 0
        val taskRows = data.array(Keys.TASKS).mapNotNull { it as? JsonObject }.filter { it.str("planId") == planId }
        val tasks = taskRows.mapNotNull { it.toPlannerTask() }
        val today = Queries.todayDate()
        val snapshot = PlannerSnapshotBuilder.build(
            PlannerSnapshotBuilder.Input(
                plan = PlannerSnapshotBuilder.Plan(planId, revision, planRow.long("start") ?: today.toEpochDay(), planRow.long("exam"), planRow.methodConfig()),
                planSubjects = data.array(Keys.PLAN_SUBJECTS).mapNotNull { it as? JsonObject }.filter { it.str("planId") == planId }.map { row ->
                    PlannerSnapshotBuilder.PlanSubject(
                        subjectId = row.long("subjectId") ?: 0,
                        name = row.str("name") ?: "",
                        priority = enumOr(row.str("priority"), PlanPriority.MEDIUM),
                        paused = row.bool("paused") ?: false,
                        minimumMaintenanceMinutes = row.int("maintenance") ?: 0,
                        weightOverride = row.int("weight"),
                        position = row.int("position") ?: 0,
                        personalDifficulty = enumOr(row.str("personalDifficulty"), PersonalDifficulty.NORMAL),
                        initialKnowledge = enumOr(row.str("initialKnowledge"), InitialKnowledge.NONE),
                    )
                },
                topics = data.topics.map { PlannerSnapshotBuilder.Topic(it.id, it.subjectId, it.parentTopicId, it.title, it.position, it.status != "NAO_ESTUDADO", it.lastStudiedAt) },
                questionsByTopic = data.questions.groupBy({ it.topicId }, { it.id }).mapValues { it.value.toSet() },
                attempts = data.attempts.map { PlannerSnapshotBuilder.Attempt(it.questionId, it.correct) },
                pendingReviews = data.reviews.filter { it.completedAt == null && it.ignoredAt == null }.map { PlannerSnapshotBuilder.PendingReview(it.id, it.topicId, it.dueAt, it.questionTotal) },
                tasks = tasks,
                executions = data.executions.filter { it.planId == planId }.map {
                    TaskExecution(it.id, it.taskId, it.subjectId, it.topicId, Queries.dateOf(it.completedAt), it.minutes.coerceAtLeast(0), it.questions.coerceAtLeast(0), it.correct.coerceIn(0, it.questions.coerceAtLeast(0)))
                },
                availability = data.availability.filter { it.planId == planId }.map { PlannerSnapshotBuilder.Availability(it.day, it.minutes, it.unavailable) },
                dayOverrides = data.array("studyDayOverrides").mapNotNull { it as? JsonObject }.filter { it.str("planId") == planId }.map {
                    PlannerSnapshotBuilder.DayOverride(it.long("day") ?: 0, it.int("minutes"), it.bool("unavailable") ?: false, it.bool("locked") ?: false)
                },
                today = today,
                zone = Queries.zone,
            ),
        )
        val computed = StudyPlannerEngine().plan(snapshot, reason)
        // Plano trazido da IA é da pessoa: o motor não acrescenta tarefas nem mexe nas importadas.
        val imported = tasks.filter { it.origin == PlanOrigin.IMPORTED }.mapTo(hashSetOf()) { it.id }
        val proposal = if (imported.isEmpty()) computed else computed.copy(newTasks = emptyList(), transitions = computed.transitions.filterNot { it.taskId in imported })

        val nextRevision = revision + 1
        val at = now()
        val transitions = proposal.transitions.associateBy { it.taskId }
        val subjectNames = data.subjects.associate { it.id to it.name }
        val topicNames = data.topics.associate { it.id to it.title }
        val competitionId = planRow.long("competitionId") ?: 0
        // A tarefa pertence ao concurso da matéria (o plano pode juntar vários concursos).
        val subjectCompetition = data.subjects.associate { it.id to it.competitionId }
        val newRows = proposal.newTasks.map { task ->
            jsonOf(
                "id" to task.id, "planId" to planId, "competitionId" to (task.subjectId?.let { subjectCompetition[it] } ?: competitionId), "annualPhaseId" to null, "monthlyPlanId" to null, "weeklyPlanId" to null,
                "subjectId" to task.subjectId, "topicId" to task.topicId, "subjectName" to (task.subjectId?.let { subjectNames[it] } ?: "Matéria não vinculada"),
                "topicName" to task.topicId?.let { topicNames[it] }, "day" to task.date.toEpochDay(), "type" to task.type.name, "minutes" to task.plannedMinutes,
                "questions" to task.plannedQuestions, "priority" to task.priority.name, "status" to task.status.name, "origin" to task.origin.name, "notes" to "",
                "locked" to task.locked, "progressNote" to "", "replannedFrom" to task.replannedFromTaskId, "createdRevision" to nextRevision,
                "updatedRevision" to nextRevision, "createdAt" to at, "updatedAt" to at, "sequence" to task.sequence,
            )
        }
        val summary = (listOf("${newRows.size} tarefa(s) planejada(s).") + proposal.explanations.map { it.message }).joinToString("\n")
        return data
            .edit(Keys.TASKS) { row ->
                val change = transitions[row.str("id")]
                if (change == null || row.str("planId") != planId || row.bool("locked") == true || row.str("status") != change.from.name) row
                else row.with("status" to JsonPrimitive(change.to.name), "updatedRevision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(at))
            }
            .edit(Keys.PLANS) { if (it.str("id") == planId) it.with("revision" to JsonPrimitive(nextRevision), "updatedAt" to JsonPrimitive(at)) else it }
            .appendAll(
                mapOf(
                    Keys.TASKS to newRows,
                    Keys.PLAN_REVISIONS to listOf(jsonOf("planId" to planId, "revision" to nextRevision, "base" to revision, "reason" to "PROPOSAL_APPLIED", "proposalId" to proposal.id, "summary" to summary, "createdAt" to at)),
                ),
            )
    }

    /** Quais concursos o plano cobre: os das matérias dele. */
    fun competitionsOf(data: Snapshot, planId: String): Set<Long> {
        val subjectCompetition = data.subjects.associate { it.id to it.competitionId }
        val fromSubjects = data.planSubjects.filter { it.planId == planId }.mapNotNull { subjectCompetition[it.subjectId] }
        val own = data.plans.firstOrNull { it.id == planId }?.competitionId
        return (fromSubjects + listOfNotNull(own)).toSet()
    }

    /**
     * Um plano para quantos concursos a pessoa quiser: entra com as matérias de cada concurso
     * marcado (prioridade média, peso 3; a pessoa ajusta depois) e sai com as dos desmarcados. O
     * concurso principal do plano fica sempre. Os outros planos ativos desses concursos são
     * desativados, para o dia ter um plano só.
     */
    fun setCompetitions(data: Snapshot, planId: String, competitionIds: Set<Long>): Snapshot {
        val plan = data.plans.firstOrNull { it.id == planId } ?: return data
        val wanted = competitionIds + plan.competitionId
        val subjectCompetition = data.subjects.associate { it.id to it.competitionId }
        val current = data.planSubjects.filter { it.planId == planId }
        val keepIds = current.filter { subjectCompetition[it.subjectId] in wanted }.mapTo(hashSetOf()) { it.subjectId }
        val removedIds = current.filter { it.subjectId !in keepIds }.mapTo(hashSetOf()) { it.subjectId }
        val missing = data.subjects.filter { it.competitionId in wanted && it.id !in keepIds }.sortedWith(compareBy({ it.competitionId }, { it.position }))
        var position = (current.maxOfOrNull { it.position } ?: -1) + 1
        var next = data
            .edit(Keys.PLAN_SUBJECTS) { if (it.str("planId") == planId && it.long("subjectId") in removedIds) null else it }
            // Tarefas ainda não feitas das matérias que saíram vão embora; as concluídas ficam no histórico.
            .edit(Keys.TASKS) { if (it.str("planId") == planId && it.long("subjectId") in removedIds && it.str("status") in setOf("PLANEJADA", "EM_ANDAMENTO")) null else it }
            .edit(Keys.PLANS) { if (it.str("id") != planId && it.long("competitionId") in wanted && it.bool("active") == true) it.with("active" to JsonPrimitive(false)) else it }
            .appendAll(
                mapOf(
                    Keys.PLAN_SUBJECTS to missing.map { subject ->
                        jsonOf(
                            "planId" to planId, "subjectId" to subject.id, "name" to subject.name, "priority" to PlanPriority.MEDIUM.name, "paused" to false,
                            "maintenance" to 0, "weight" to 3, "position" to position++, "personalDifficulty" to PersonalDifficulty.NORMAL.name, "initialKnowledge" to InitialKnowledge.NONE.name,
                        )
                    },
                ),
            )
        next = bumpRevision(next, planId, "SUBJECT_CHANGED", "Concursos do plano: ${wanted.size}.")
        return replan(next, planId, ReplanReason.SUBJECT_CHANGED)
    }

    /** Apaga o plano e tudo que é dele (tarefas, execuções, horas, matérias, revisões, fases). */
    fun deletePlan(data: Snapshot, planId: String): Snapshot {
        var next = data
        listOf(Keys.TASKS, Keys.EXECUTIONS, Keys.AVAILABILITY, Keys.PLAN_SUBJECTS, Keys.PLAN_REVISIONS, "annualPhases", "monthlyPlans", "weeklyPlans", "studyDayOverrides")
            .forEach { key -> next = next.edit(key) { if (it.str("planId") == planId) null else it } }
        return next.edit(Keys.PLANS) { if (it.str("id") == planId) null else it }
    }

    /** Uma linha de revisão e o plano na revisão seguinte (o claimRevision do app). */
    private fun bumpRevision(data: Snapshot, planId: String, reason: String, summary: String): Snapshot {
        val plan = data.plans.firstOrNull { it.id == planId } ?: return data
        val at = now()
        return data
            .edit(Keys.PLANS) { if (it.str("id") == planId) it.with("revision" to JsonPrimitive(plan.revision + 1), "updatedAt" to JsonPrimitive(at)) else it }
            .append(Keys.PLAN_REVISIONS, jsonOf("planId" to planId, "revision" to plan.revision + 1, "base" to plan.revision, "reason" to reason, "proposalId" to null, "summary" to summary, "createdAt" to at))
    }

    /** StudyPlanApplicationService.updateAvailability: horas por dia (segunda = índice 0) e replanejamento. */
    fun updateAvailability(data: Snapshot, planId: String, weeklyMinutes: List<Int>): Snapshot {
        require(weeklyMinutes.any { it > 0 }) { "Mantenha pelo menos um dia disponível." }
        val rows = weeklyMinutes.mapIndexed { index, minutes -> jsonOf("planId" to planId, "day" to index + 1, "minutes" to minutes.coerceAtLeast(0), "unavailable" to (minutes <= 0), "mode" to "FIXED") }
        val without = data.edit(Keys.AVAILABILITY) { if (it.str("planId") == planId) null else it }.appendAll(mapOf(Keys.AVAILABILITY to rows))
        return replan(bumpRevision(without, planId, "AVAILABILITY_CHANGED", "Disponibilidade semanal atualizada."), planId, ReplanReason.AVAILABILITY_CHANGED)
    }

    /** StudyPlanApplicationService.updateSubject: prioridade na prova e pausa, e replanejamento. */
    fun updateSubject(data: Snapshot, planId: String, subjectId: Long, priority: PlanPriority, paused: Boolean): Snapshot {
        var name = ""
        val changed = data.edit(Keys.PLAN_SUBJECTS) {
            if (it.str("planId") == planId && it.long("subjectId") == subjectId) { name = it.str("name").orEmpty(); it.with("priority" to JsonPrimitive(priority.name), "paused" to JsonPrimitive(paused)) } else it
        }
        return replan(bumpRevision(changed, planId, "SUBJECT_CHANGED", name), planId, ReplanReason.SUBJECT_CHANGED)
    }

    /** Data da prova e momento do estudo (o assistente do app regrava o método e replaneja). */
    fun updateMethod(data: Snapshot, planId: String, examDate: LocalDate?, profile: StudyProfile): Snapshot {
        val base = StudyMethodConfig.forProfile(profile)
        val changed = data.edit(Keys.PLANS) {
            if (it.str("id") != planId) it
            else it.with(
                "exam" to (examDate?.toEpochDay()?.let { day -> JsonPrimitive(day) } ?: kotlinx.serialization.json.JsonNull),
                "profile" to JsonPrimitive(profile.name),
                "weeklyQuestions" to JsonPrimitive(base.weeklyQuestionsTarget),
                "simulations" to JsonPrimitive(base.simulationsPerMonth),
            )
        }
        return replan(bumpRevision(changed, planId, "SUBJECT_CHANGED", "Método do plano atualizado."), planId, ReplanReason.SUBJECT_CHANGED)
    }

    private fun JsonObject.long(key: String): Long? = (this[key] as? JsonPrimitive)?.longOrNull
    private fun JsonObject.int(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull
    private fun JsonObject.bool(key: String): Boolean? = (this[key] as? JsonPrimitive)?.booleanOrNull

    private inline fun <reified T : Enum<T>> enumOr(value: String?, fallback: T): T =
        value?.let { raw -> enumValues<T>().firstOrNull { it.name == raw } } ?: fallback

    /** StudyPlanEntity.methodConfig no app. */
    private fun JsonObject.methodConfig() = StudyMethodConfig(
        profile = enumOr(str("profile"), StudyProfile.DO_ZERO),
        blockMinutes = (int("block") ?: 50).coerceIn(15, 180),
        weeklyQuestionsTarget = (int("weeklyQuestions") ?: 100).coerceAtLeast(0),
        questionsPerTopic = (int("topicQuestions") ?: 15).coerceAtLeast(0),
        simulationsPerMonth = (int("simulations") ?: 2).coerceIn(0, 8),
        discursivesPerMonth = (int("discursives") ?: 0).coerceIn(0, 12),
        interleaveSubjects = bool("interleave") ?: true,
        dailySubjectSharePercent = (int("dailyShare") ?: 60).coerceIn(20, 100),
    )

    private fun JsonObject.toPlannerTask(): PlannerTask? = runCatching {
        PlannerTask(
            id = str("id")!!,
            planId = str("planId")!!,
            subjectId = long("subjectId"),
            topicId = long("topicId"),
            date = LocalDate.fromEpochDays(long("day")!!),
            type = enumValueOf<PlanTaskType>(str("type")!!),
            plannedMinutes = int("minutes") ?: 0,
            plannedQuestions = int("questions") ?: 0,
            priority = enumOr(str("priority"), PlanPriority.MEDIUM),
            status = enumValueOf<PlanTaskStatus>(str("status")!!),
            locked = bool("locked") ?: false,
            origin = enumOr(str("origin"), PlanOrigin.ENGINE),
            replannedFromTaskId = str("replannedFrom"),
            sequence = int("sequence") ?: 0,
        )
    }.getOrNull()
}
