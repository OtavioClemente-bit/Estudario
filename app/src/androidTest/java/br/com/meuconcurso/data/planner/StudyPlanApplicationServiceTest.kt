package br.com.meuconcurso.data.planner

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.local.CompetitionEntity
import br.com.meuconcurso.data.local.SubjectEntity
import br.com.meuconcurso.data.local.TopicEntity
import br.com.meuconcurso.data.local.planner.StudyAvailabilityEntity
import br.com.meuconcurso.domain.planner.CapacityReport
import br.com.meuconcurso.domain.planner.ForecastResult
import br.com.meuconcurso.domain.planner.PlanningProposal
import br.com.meuconcurso.domain.planner.PlanPriority
import br.com.meuconcurso.domain.planner.PlanTaskStatus
import br.com.meuconcurso.domain.planner.PlanTaskType
import br.com.meuconcurso.domain.planner.PlannerTask
import br.com.meuconcurso.domain.planner.StudyPlannerEngine
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class StudyPlanApplicationServiceTest {
    private lateinit var db: AppDatabase
    private lateinit var service: StudyPlanApplicationService
    private var competitionId = 0L
    private var subjectId = 0L
    private var topicId = 0L

    @Before
    fun setup() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        competitionId = db.dao().insertCompetition(CompetitionEntity(name = "Concurso", externalId = "competition-1"))
        subjectId = db.dao().insertSubject(SubjectEntity(competitionId = competitionId, name = "TI", externalId = "subject-1"))
        topicId = db.dao().insertTopic(TopicEntity(subjectId = subjectId, title = "Segurança", externalId = "topic-1"))
        service = StudyPlanApplicationService(db, StudyPlannerEngine())
    }

    @After fun close() = db.close()

    @Test
    fun activatingAndMarkingMasterKeepsOnlyOneOfEachPerCompetition() = runBlocking {
        val first = service.createPlan(input("Primeiro"))
        val second = service.createPlan(input("Segundo"))

        service.activate(first)
        service.activate(second)
        service.markMaster(first)
        service.markMaster(second)

        val plans = db.plannerDao().plansOnce()
        assertEquals(listOf(second), plans.filter { it.active }.map { it.id })
        assertEquals(listOf(second), plans.filter { it.masterPlan }.map { it.id })
    }

    @Test
    fun staleProposalIsRejectedWithoutChangingTasksOrRevision() = runBlocking {
        val planId = service.createPlan(input("Plano"))
        val proposal = proposal(planId, baseRevision = 0)
        service.applyProposal(proposal)

        val error = runCatching { service.applyProposal(proposal) }.exceptionOrNull()

        assertTrue(error is StalePlanningProposalException)
        assertEquals(1L, db.plannerDao().plan(planId)?.revision)
        assertEquals(1, db.plannerDao().tasksForOnce(planId).size)
    }

    @Test
    fun duplicationCopiesStrategyButNeverExecutionHistory() = runBlocking {
        val original = service.createPlan(input("Original"))
        service.applyProposal(proposal(original, baseRevision = 0))
        val taskId = db.plannerDao().tasksForOnce(original).single().id
        StudyExecutionService(db, service).complete(
            taskId,
            CompleteTaskInput(startedAt = 1_000, completedAt = 1_500, actualMinutes = 25, questions = 5, correct = 4),
        )

        val duplicate = service.duplicate(original, "Cópia")

        assertFalse(duplicate == original)
        assertTrue(db.plannerDao().tasksForOnce(duplicate).all { it.id != taskId })
        assertTrue(db.plannerDao().executionsForOnce(duplicate).isEmpty())
        assertEquals(1, db.plannerDao().executionsForOnce(original).size)
        assertTrue(db.plannerDao().tasksForOnce(duplicate).all { it.status == PlanTaskStatus.PLANEJADA })
    }

    @Test
    fun partialExecutionKeepsHistoryAndPlansOnlyTheRemainder() = runBlocking {
        val planId = service.createPlan(input("Parcial"))
        service.applyProposal(proposal(planId, baseRevision = 0))
        val taskId = db.plannerDao().tasksForOnce(planId).single().id
        val executionService = StudyExecutionService(db, service)
        executionService.complete(taskId, CompleteTaskInput(1_000, 1_500, 25))

        val proposal = service.replan(planId, br.com.meuconcurso.domain.planner.ReplanReason.TASK_PARTIAL, LocalDate.of(2026, 9, 15))

        assertEquals(25, db.plannerDao().executionsForOnce(planId).single().actualMinutes)
        assertEquals(35, proposal.newTasks.single { it.replannedFromTaskId == taskId }.plannedMinutes)
        assertEquals(PlanTaskStatus.REPROGRAMADA, db.plannerDao().task(taskId)?.status)
    }

    private fun input(name: String) = CreatePlanInput(
        competitionId = competitionId,
        name = name,
        objective = "Aprovação",
        startDate = LocalDate.of(2026, 9, 15),
        examDate = null,
        availability = (1..7).map { StudyAvailabilityEntity("", it, 120) },
        subjects = listOf(PlanSubjectInput(subjectId, "TI", PlanPriority.CRITICAL, 60, false, 0)),
    )

    private fun proposal(planId: String, baseRevision: Long) = PlanningProposal(
        id = "proposal-$baseRevision",
        planId = planId,
        baseRevision = baseRevision,
        preservedTaskIds = emptySet(),
        transitions = emptyList(),
        newTasks = listOf(
            PlannerTask(
                id = "task-$baseRevision",
                planId = planId,
                subjectId = subjectId,
                topicId = topicId,
                date = LocalDate.of(2026, 9, 15),
                type = PlanTaskType.THEORY,
                plannedMinutes = 60,
                plannedQuestions = 0,
                priority = PlanPriority.CRITICAL,
                status = PlanTaskStatus.PLANEJADA,
                locked = false,
            ),
        ),
        capacity = CapacityReport(60, 120, 0, emptyMap(), emptyList()),
        explanations = emptyList(),
        forecast = ForecastResult(null, 0, 0),
    )
}
