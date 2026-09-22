package br.com.estudario.data.transfer.planner

import androidx.room.withTransaction
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.planner.*
import br.com.estudario.domain.planner.PlanTaskStatus
import java.time.LocalDate
import java.util.UUID

enum class PlanImportMode { CREATE, MERGE, REPLACE_FUTURE }

data class StudyPlanImportPreview(
    val planId: String,
    val planName: String,
    val existingPlan: Boolean,
    val unresolvedReferences: List<String>,
    val importedTaskCount: Int,
    val protectedLocalTaskCount: Int,
    val requestsActive: Boolean,
    val requestsMaster: Boolean,
)

data class StudyPlanImportResult(
    val planId: String,
    val created: Boolean,
    val tasksInserted: Int,
    val tasksUpdated: Int,
    val tasksProtected: Int,
)

class StudyPlanTransferService(
    private val db: AppDatabase,
    private val codec: StudyPlanCodec = StudyPlanCodec(),
) {
    private val planner = db.plannerDao()
    private val resolver = StudyPlanImportResolver(db)

    suspend fun preview(text: String): StudyPlanImportPreview {
        val resolved = resolver.resolve(codec.decode(text))
        val existing = planner.plan(resolved.file.planId)
        val protected = existing?.let { plan ->
            val executed = planner.executionsForOnce(plan.id).mapNotNullTo(hashSetOf()) { it.taskId }
            planner.tasksForOnce(plan.id).count { it.locked || it.status == PlanTaskStatus.CONCLUIDA || it.id in executed }
        } ?: 0
        return StudyPlanImportPreview(
            planId = resolved.file.planId,
            planName = resolved.file.name,
            existingPlan = existing != null,
            unresolvedReferences = resolved.unresolvedReferences,
            importedTaskCount = resolved.file.tasks.size,
            protectedLocalTaskCount = protected,
            requestsActive = resolved.file.active,
            requestsMaster = resolved.file.masterPlan,
        )
    }

    suspend fun `import`(
        text: String,
        mode: PlanImportMode,
        confirmActive: Boolean = false,
        confirmMaster: Boolean = false,
        today: LocalDate = LocalDate.now(),
    ): StudyPlanImportResult = db.withTransaction {
        val resolved = resolver.resolve(codec.decode(text))
        require(resolved.unresolvedReferences.isEmpty()) { "Referências não resolvidas: ${resolved.unresolvedReferences.joinToString()}." }
        val competition = resolved.competition ?: error("Concurso não encontrado.")
        val existing = planner.plan(resolved.file.planId)
        if (mode != PlanImportMode.CREATE) require(existing != null) { "O plano a mesclar não existe neste aparelho." }
        val create = mode == PlanImportMode.CREATE
        val planId = if (create && existing != null) UUID.randomUUID().toString() else resolved.file.planId
        val idMap = if (planId == resolved.file.planId) emptyMap() else buildMap {
            resolved.file.annualPhases.forEach { put(it.id, UUID.randomUUID().toString()) }
            resolved.file.monthlyPlans.forEach { put(it.id, UUID.randomUUID().toString()) }
            resolved.file.weeklyPlans.forEach { put(it.id, UUID.randomUUID().toString()) }
            resolved.file.tasks.forEach { put(it.id, UUID.randomUUID().toString()) }
        }
        val currentRevision = if (create) 0 else existing!!.revision
        val nextRevision = if (create) 0 else currentRevision + 1
        if (create) {
            planner.insertPlan(
                StudyPlanEntity(
                    id = planId,
                    competitionId = competition.id,
                    name = resolved.file.name,
                    objective = resolved.file.objective,
                    startEpochDay = resolved.file.startDate.toEpochDay(),
                    examEpochDay = resolved.file.examDate?.toEpochDay(),
                    active = confirmActive && resolved.file.active,
                    masterPlan = confirmMaster && resolved.file.masterPlan,
                    profile = resolved.file.configuration.profile.name,
                    blockMinutes = resolved.file.configuration.blockMinutes,
                ),
            )
            planner.insertRevision(StudyPlanRevisionEntity(planId, 0, 0, "IMPORTED_CREATE", summary = "Plano importado."))
        } else {
            if (resolved.file.baseRevision != null && resolved.file.baseRevision != currentRevision && mode == PlanImportMode.REPLACE_FUTURE) {
                throw IllegalStateException("A revisão importada diverge da revisão local. Gere uma nova prévia antes de substituir o futuro.")
            }
            if (planner.claimRevision(planId, currentRevision, System.currentTimeMillis()) == 0) error("O plano mudou durante a importação.")
            planner.insertRevision(StudyPlanRevisionEntity(planId, nextRevision, currentRevision, "IMPORTED_${mode.name}", summary = "Planejamento futuro importado."))
        }
        planner.upsertAvailability(resolved.file.configuration.days.map { day -> StudyAvailabilityEntity(planId, day.day, day.minutes, day.unavailable, resolved.file.configuration.mode) })
        planner.upsertPlanSubjects(resolved.file.subjects.map { subject ->
            val local = resolved.subjects.getValue(subject.externalId)
            PlanSubjectEntity(planId, local.id, local.name, subject.priority, subject.paused, subject.maintenanceMinutes, position = subject.position)
        })
        planner.insertAnnualPhases(resolved.file.annualPhases.mapIndexed { position, phase ->
            AnnualPhaseEntity(idMap[phase.id] ?: phase.id, planId, position, phase.name, phase.objective, phase.completionCriteria, phase.startDate.toEpochDay(), phase.endDate.toEpochDay(), phase.targetMinutes, phase.targetQuestions, phase.targetDiscursives, phase.targetPercent, nextRevision)
        })
        planner.insertMonthlyPlans(resolved.file.monthlyPlans.map { month -> MonthlyPlanEntity(idMap[month.id] ?: month.id, planId, month.yearMonth, month.focus, month.targetMinutes, month.targetQuestions, month.targetDiscursives, month.targetPercent, nextRevision) })
        planner.insertWeeklyPlans(resolved.file.weeklyPlans.map { week -> WeeklyPlanEntity(idMap[week.id] ?: week.id, planId, week.weekStart.toEpochDay(), week.objective, week.targetMinutes, week.targetQuestions, week.targetDiscursives, nextRevision) })

        val localTasks = if (create) emptyList() else planner.tasksForOnce(planId)
        val localById = localTasks.associateBy { it.id }
        val localExecutions = if (create) emptyList() else planner.executionsForOnce(planId)
        val executionsByTask = localExecutions.filter { it.taskId != null }.groupBy { it.taskId }
        val executedTaskIds = executionsByTask.keys.filterNotNull().toSet()
        val protectedIds = localTasks.filter { it.locked || it.status == PlanTaskStatus.CONCLUIDA || it.id in executedTaskIds }.mapTo(hashSetOf()) { it.id }
        val remainderSources = localTasks.mapNotNullTo(hashSetOf()) { it.replannedFromTaskId }
        if (mode == PlanImportMode.REPLACE_FUTURE) {
            localTasks.filter { it.scheduledEpochDay >= today.toEpochDay() && it.id !in protectedIds && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.PAUSADA) }
                .forEach { planner.updateTask(it.copy(status = PlanTaskStatus.REPROGRAMADA, updatedRevision = nextRevision, updatedAt = System.currentTimeMillis())) }
        }
        var inserted = 0
        var updated = 0
        var protected = 0
        val acceptedTaskIds = hashSetOf<String>()
        resolved.file.tasks.forEach { imported ->
            val id = idMap[imported.id] ?: imported.id
            val old = localById[id]
            if (old != null && old.id in protectedIds) {
                protected++
                val done = executionsByTask[old.id].orEmpty()
                val remainingMinutes = (old.plannedMinutes - done.sumOf { it.actualMinutes }).coerceAtLeast(0)
                if (!old.locked && old.status != PlanTaskStatus.CONCLUIDA && remainingMinutes > 0 && old.id !in remainderSources) {
                    planner.updateTask(old.copy(status = PlanTaskStatus.REPROGRAMADA, updatedRevision = nextRevision, updatedAt = System.currentTimeMillis()))
                    val remainingQuestions = (old.plannedQuestions - done.sumOf { it.questionsDone }).coerceAtLeast(0)
                    val successor = old.copy(
                        id = UUID.randomUUID().toString(),
                        scheduledEpochDay = maxOf(imported.date.toEpochDay(), today.toEpochDay()),
                        plannedMinutes = remainingMinutes,
                        plannedQuestions = remainingQuestions,
                        status = PlanTaskStatus.PLANEJADA,
                        origin = br.com.estudario.domain.planner.PlanOrigin.IMPORTED,
                        locked = false,
                        progressNote = "Saldo preservado após execução parcial.",
                        replannedFromTaskId = old.id,
                        createdRevision = nextRevision,
                        updatedRevision = nextRevision,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                    )
                    planner.insertTasks(listOf(successor))
                    inserted++
                    updated++
                }
                acceptedTaskIds += id
                return@forEach
            }
            if (!create && imported.date < today) return@forEach
            val subject = imported.subjectExternalId?.let(resolved.subjects::get)
            val topic = imported.topicExternalId?.let(resolved.topics::get)
            val entity = PlanTaskEntity(
                id = id,
                planId = planId,
                competitionId = competition.id,
                subjectId = subject?.id,
                topicId = topic?.id,
                subjectNameSnapshot = subject?.name ?: imported.subjectName ?: "Matéria não vinculada",
                topicNameSnapshot = topic?.title ?: imported.topicName,
                scheduledEpochDay = imported.date.toEpochDay(),
                type = imported.type,
                plannedMinutes = imported.minutes,
                plannedQuestions = imported.questions,
                priority = imported.priority,
                status = if (create && imported.status == PlanTaskStatus.CONCLUIDA) PlanTaskStatus.PLANEJADA else imported.status,
                origin = imported.origin,
                notes = imported.notes,
                locked = imported.locked,
                createdRevision = old?.createdRevision ?: nextRevision,
                updatedRevision = nextRevision,
                createdAt = old?.createdAt ?: System.currentTimeMillis(),
            )
            if (old == null) { planner.insertTasks(listOf(entity)); inserted++ } else { planner.updateTask(entity); updated++ }
            acceptedTaskIds += id
        }
        val dependencies = resolved.file.tasks.flatMap { task ->
            val taskId = idMap[task.id] ?: task.id
            task.dependencies.mapNotNull { dependency ->
                val dependencyId = idMap[dependency] ?: dependency
                if (taskId in acceptedTaskIds && dependencyId in acceptedTaskIds) PlanTaskDependencyEntity(taskId, dependencyId) else null
            }
        }
        if (dependencies.isNotEmpty()) planner.insertDependencies(dependencies)
        if (!create) {
            if (confirmActive && resolved.file.active) planner.activateOnly(competition.id, planId)
            if (confirmMaster && resolved.file.masterPlan) planner.markOnlyMaster(competition.id, planId)
        }
        StudyPlanImportResult(planId, create, inserted, updated, protected)
    }

    suspend fun exportPlan(planId: String): String = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        val competition = db.dao().competitionsOnce().first { it.id == plan.competitionId }
        val subjects = db.dao().subjectsOnce().associateBy { it.id }
        val topics = db.dao().topicsOnce().associateBy { it.id }
        val priorities = planner.subjectsFor(planId).mapNotNull { row -> subjects[row.subjectId]?.externalId?.let { PlanSubjectDto(it, row.subjectNameSnapshot, row.priority, row.minimumMaintenanceMinutes, row.paused, row.position) } }
        val dependencies = planner.dependenciesFor(planId).groupBy { it.taskId }
        val tasks = planner.tasksForOnce(planId).map { task ->
            PlanTaskDto(task.id, task.subjectId?.let { subjects[it]?.externalId }, task.topicId?.let { topics[it]?.externalId }, task.subjectNameSnapshot, task.topicNameSnapshot, LocalDate.ofEpochDay(task.scheduledEpochDay), task.type, task.plannedMinutes, task.plannedQuestions, task.priority, task.status, task.origin, task.locked, task.notes, dependencies[task.id].orEmpty().map { it.dependsOnTaskId })
        }
        codec.encode(
            StudyPlanFileV1(
                plan.id,
                PlanCompetitionDto(competition.externalId ?: error("O concurso precisa de externalId para exportar .plano."), competition.name),
                plan.name,
                plan.objective,
                plan.active,
                plan.masterPlan,
                LocalDate.ofEpochDay(plan.startEpochDay),
                plan.examEpochDay?.let(LocalDate::ofEpochDay),
                plan.revision,
                PlanConfigurationDto(planner.availabilityFor(planId).firstOrNull()?.mode ?: AvailabilityMode.ADVANCED, planner.availabilityFor(planId).map { PlanDayDto(it.dayOfWeek, it.availableMinutes, it.unavailable) }, 0, 0),
                priorities,
                planner.currentAnnualPhases(planId).map { AnnualPhaseDto(it.id, it.name, it.objective, it.completionCriteria, LocalDate.ofEpochDay(it.startEpochDay), LocalDate.ofEpochDay(it.endEpochDay), it.targetMinutes, it.targetQuestions, it.targetDiscursives, it.targetPercent) },
                planner.currentMonthlyPlans(planId).map { MonthlyPlanDto(it.id, it.yearMonth, it.focus, it.targetMinutes, it.targetQuestions, it.targetDiscursives, it.targetPercent) },
                planner.currentWeeklyPlans(planId).map { WeeklyPlanDto(it.id, LocalDate.ofEpochDay(it.weekStartEpochDay), it.objective, it.targetMinutes, it.targetQuestions, it.targetDiscursives) },
                tasks,
                mapOf("exportedBy" to "Estudário"),
            ),
        )
    }
}
