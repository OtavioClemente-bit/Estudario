package br.com.meuconcurso.data.planner

import androidx.room.withTransaction
import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.local.planner.PlanSubjectEntity
import br.com.meuconcurso.data.local.planner.StudyAvailabilityEntity
import br.com.meuconcurso.data.local.planner.StudyPlanEntity
import br.com.meuconcurso.data.local.planner.StudyPlanRevisionEntity
import br.com.meuconcurso.data.local.planner.AnnualPhaseEntity
import br.com.meuconcurso.data.local.planner.MonthlyPlanEntity
import br.com.meuconcurso.data.local.planner.WeeklyPlanEntity
import br.com.meuconcurso.data.local.planner.PlanTaskDependencyEntity
import br.com.meuconcurso.data.local.planner.StudyDayOverrideEntity
import br.com.meuconcurso.domain.planner.PlanPriority
import br.com.meuconcurso.domain.planner.PlanningProposal
import br.com.meuconcurso.domain.planner.ReplanReason
import br.com.meuconcurso.domain.planner.StudyPlannerEngine
import java.time.LocalDate
import java.util.UUID
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import java.time.YearMonth

data class PlanSubjectInput(
    val subjectId: Long,
    val name: String,
    val priority: PlanPriority,
    val minimumMaintenanceMinutes: Int,
    val paused: Boolean,
    val position: Int,
)

data class CreatePlanInput(
    val competitionId: Long,
    val name: String,
    val objective: String,
    val startDate: LocalDate,
    val examDate: LocalDate?,
    val availability: List<StudyAvailabilityEntity>,
    val subjects: List<PlanSubjectInput>,
    val active: Boolean = false,
    val masterPlan: Boolean = false,
)

class StalePlanningProposalException : IllegalStateException("O plano mudou desde o cálculo. Recalculando com os dados atuais.")

class StudyPlanApplicationService(
    private val db: AppDatabase,
    private val engine: StudyPlannerEngine,
) {
    private val planner = db.plannerDao()
    private val snapshotFactory = StudyPlanSnapshotFactory(db)

    suspend fun createPlan(input: CreatePlanInput): String = db.withTransaction {
        require(input.name.isNotBlank()) { "Informe um nome para o plano." }
        require(input.objective.isNotBlank()) { "Informe o objetivo do plano." }
        require(input.availability.any { !it.unavailable && it.availableMinutes > 0 }) { "Informe pelo menos um dia disponível." }
        val id = UUID.randomUUID().toString()
        planner.insertPlan(
            StudyPlanEntity(
                id = id,
                competitionId = input.competitionId,
                name = input.name.trim(),
                objective = input.objective.trim(),
                startEpochDay = input.startDate.toEpochDay(),
                examEpochDay = input.examDate?.toEpochDay(),
                active = input.active,
                masterPlan = input.masterPlan,
            ),
        )
        planner.insertRevision(StudyPlanRevisionEntity(id, 0, 0, "CREATED", summary = "Plano criado."))
        planner.upsertAvailability(input.availability.map { it.copy(planId = id) })
        planner.upsertPlanSubjects(input.subjects.map { subject ->
            PlanSubjectEntity(id, subject.subjectId, subject.name, subject.priority, subject.paused, subject.minimumMaintenanceMinutes, position = subject.position)
        })
        val weeklyMinutes = input.availability.sumOf { if (it.unavailable) 0 else it.availableMinutes }
        val phaseEnd = input.examDate ?: input.startDate.plusYears(1).minusDays(1)
        planner.insertAnnualPhases(listOf(AnnualPhaseEntity(UUID.randomUUID().toString(), id, 0, "Ciclo principal", input.objective.trim(), "Concluir os tópicos priorizados e manter revisões em dia.", input.startDate.toEpochDay(), phaseEnd.toEpochDay(), weeklyMinutes * 4, 0, 0, 100, 0)))
        planner.insertMonthlyPlans(listOf(MonthlyPlanEntity(UUID.randomUUID().toString(), id, YearMonth.from(input.startDate).toString(), input.objective.trim(), weeklyMinutes * 4, 0, 0, 100, 0)))
        planner.insertWeeklyPlans(listOf(WeeklyPlanEntity(UUID.randomUUID().toString(), id, input.startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toEpochDay(), input.objective.trim(), weeklyMinutes, 0, 0, 0)))
        id
    }

    suspend fun activate(planId: String) = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        require(!plan.archived) { "Restaure o plano antes de ativá-lo." }
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.activateOnly(plan.competitionId, planId)
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "ACTIVATED", summary = "Plano ativado."))
    }

    suspend fun markMaster(planId: String) = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        require(!plan.archived) { "Restaure o plano antes de marcá-lo como Mestre." }
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.markOnlyMaster(plan.competitionId, planId)
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "MASTER_SELECTED", summary = "Plano marcado como Mestre."))
    }

    suspend fun archive(planId: String) = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.archive(planId, System.currentTimeMillis())
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "ARCHIVED", summary = "Plano arquivado."))
    }

    suspend fun restore(planId: String) = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        require(plan.archived) { "O plano já está disponível." }
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.restore(planId, System.currentTimeMillis())
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "RESTORED", summary = "Plano restaurado sem ativação automática."))
    }

    suspend fun setTaskLocked(taskId: String, locked: Boolean) = db.withTransaction {
        val task = planner.task(taskId) ?: error("Tarefa não encontrada.")
        val plan = planner.plan(task.planId) ?: error("Plano não encontrado.")
        require(task.status != br.com.meuconcurso.domain.planner.PlanTaskStatus.CONCLUIDA) { "Tarefas concluídas já são imutáveis." }
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        planner.updateTask(task.copy(locked = locked, updatedRevision = plan.revision + 1, updatedAt = System.currentTimeMillis()))
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, if (locked) "TASK_LOCKED" else "TASK_UNLOCKED", summary = task.id))
    }

    suspend fun setDayLocked(planId: String, date: LocalDate, locked: Boolean) = db.withTransaction {
        val plan = planner.plan(planId) ?: error("Plano não encontrado.")
        require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
        val current = planner.dayOverridesFor(planId).firstOrNull { it.epochDay == date.toEpochDay() }
        planner.upsertDayOverride((current ?: StudyDayOverrideEntity(planId, date.toEpochDay())).copy(locked = locked))
        planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, if (locked) "DAY_LOCKED" else "DAY_UNLOCKED", summary = date.toString()))
    }

    suspend fun updateAvailability(planId: String, values: List<StudyAvailabilityEntity>) {
        require(values.any { !it.unavailable && it.availableMinutes > 0 }) { "Mantenha pelo menos um dia disponível." }
        db.withTransaction {
            val plan = planner.plan(planId) ?: error("Plano não encontrado.")
            require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
            planner.upsertAvailability(values.map { it.copy(planId = planId) })
            planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "AVAILABILITY_CHANGED", summary = "Disponibilidade semanal atualizada."))
        }
        replan(planId, ReplanReason.AVAILABILITY_CHANGED)
    }

    suspend fun updateSubject(planId: String, subjectId: Long, priority: PlanPriority, paused: Boolean) {
        db.withTransaction {
            val plan = planner.plan(planId) ?: error("Plano não encontrado.")
            val subject = planner.subjectsFor(planId).firstOrNull { it.subjectId == subjectId } ?: error("Matéria não pertence ao plano.")
            require(planner.claimRevision(plan.id, plan.revision, System.currentTimeMillis()) == 1)
            planner.upsertPlanSubjects(listOf(subject.copy(priority = priority, paused = paused)))
            planner.insertRevision(StudyPlanRevisionEntity(plan.id, plan.revision + 1, plan.revision, "SUBJECT_CHANGED", summary = subject.subjectNameSnapshot))
        }
        replan(planId, ReplanReason.SUBJECT_CHANGED)
    }

    suspend fun applyProposal(proposal: PlanningProposal) = db.withTransaction {
        val plan = planner.plan(proposal.planId) ?: error("Plano não encontrado.")
        require(!plan.archived) { "Plano arquivado não pode ser replanejado." }
        val nextRevision = proposal.baseRevision + 1
        if (planner.claimRevision(plan.id, proposal.baseRevision, System.currentTimeMillis()) == 0) {
            throw StalePlanningProposalException()
        }
        proposal.transitions.forEach { transition ->
            val task = planner.task(transition.taskId) ?: error("Tarefa ${transition.taskId} não encontrada.")
            require(!task.locked) { "Tarefa bloqueada não pode ser alterada." }
            require(task.status == transition.from) { "A tarefa mudou desde o cálculo." }
            require(task.status != br.com.meuconcurso.domain.planner.PlanTaskStatus.CONCLUIDA) { "Tarefa concluída não pode ser alterada." }
            planner.updateTask(task.withStatus(transition.to, nextRevision))
        }
        val subjects = db.dao().subjectsOnce().associateBy { it.id }
        val topics = db.dao().topicsOnce().associateBy { it.id }
        val entities = proposal.newTasks.map { task ->
            task.toEntity(
                competitionId = plan.competitionId,
                revision = nextRevision,
                subjectName = task.subjectId?.let { subjects[it]?.name } ?: "Matéria não vinculada",
                topicName = task.topicId?.let { topics[it]?.title },
            )
        }
        if (entities.isNotEmpty()) planner.insertTasks(entities)
        planner.insertRevision(
            StudyPlanRevisionEntity(plan.id, nextRevision, proposal.baseRevision, "PROPOSAL_APPLIED", proposal.id, "${entities.size} tarefa(s) planejada(s)."),
        )
    }

    suspend fun replan(planId: String, reason: ReplanReason, today: LocalDate = LocalDate.now()): PlanningProposal {
        val proposal = engine.plan(snapshotFactory.create(planId, today), reason)
        applyProposal(proposal)
        return proposal
    }

    suspend fun duplicate(planId: String, name: String): String = db.withTransaction {
        val source = planner.plan(planId) ?: error("Plano não encontrado.")
        val newId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        planner.insertPlan(source.copy(id = newId, name = name.trim(), active = false, masterPlan = false, archived = false, revision = 0, createdAt = now, updatedAt = now))
        planner.insertRevision(StudyPlanRevisionEntity(newId, 0, 0, "DUPLICATED", summary = "Duplicado de $planId."))
        planner.upsertAvailability(planner.availabilityFor(planId).map { it.copy(planId = newId) })
        planner.upsertPlanSubjects(planner.subjectsFor(planId).map { it.copy(planId = newId) })
        val phaseIds = planner.currentAnnualPhases(planId).associate { it.id to UUID.randomUUID().toString() }
        val monthIds = planner.currentMonthlyPlans(planId).associate { it.id to UUID.randomUUID().toString() }
        val weekIds = planner.currentWeeklyPlans(planId).associate { it.id to UUID.randomUUID().toString() }
        planner.insertAnnualPhases(planner.currentAnnualPhases(planId).map { it.copy(id = phaseIds.getValue(it.id), planId = newId, validFromRevision = 0, validUntilRevision = null) })
        planner.insertMonthlyPlans(planner.currentMonthlyPlans(planId).map { it.copy(id = monthIds.getValue(it.id), planId = newId, validFromRevision = 0, validUntilRevision = null) })
        planner.insertWeeklyPlans(planner.currentWeeklyPlans(planId).map { it.copy(id = weekIds.getValue(it.id), planId = newId, validFromRevision = 0, validUntilRevision = null) })
        val oldTasks = planner.tasksForOnce(planId)
        val ids = oldTasks.associate { it.id to UUID.randomUUID().toString() }
        if (oldTasks.isNotEmpty()) {
            planner.insertTasks(oldTasks.map { task ->
                task.copy(
                    id = ids.getValue(task.id),
                    planId = newId,
                    annualPhaseId = task.annualPhaseId?.let(phaseIds::get),
                    monthlyPlanId = task.monthlyPlanId?.let(monthIds::get),
                    weeklyPlanId = task.weeklyPlanId?.let(weekIds::get),
                    replannedFromTaskId = task.replannedFromTaskId?.let(ids::get),
                    status = br.com.meuconcurso.domain.planner.PlanTaskStatus.PLANEJADA,
                    progressNote = "",
                    createdRevision = 0,
                    updatedRevision = 0,
                    createdAt = now,
                    updatedAt = now,
                )
            })
            val dependencies = planner.dependenciesFor(planId).mapNotNull { row ->
                val task = ids[row.taskId]; val dependency = ids[row.dependsOnTaskId]
                if (task != null && dependency != null) PlanTaskDependencyEntity(task, dependency) else null
            }
            if (dependencies.isNotEmpty()) planner.insertDependencies(dependencies)
        }
        newId
    }
}
