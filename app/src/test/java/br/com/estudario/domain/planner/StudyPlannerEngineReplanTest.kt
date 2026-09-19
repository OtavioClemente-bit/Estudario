package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class StudyPlannerEngineReplanTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate.of(2026, 9, 14)

    @Test
    fun `reducing four hours to two hours moves only future remainder within daily capacity`() {
        val original = task("task-1", monday, 240)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(original)), ReplanReason.AVAILABILITY_CHANGED)

        assertEquals(PlanTaskStatus.REPROGRAMADA, proposal.transitions.single().to)
        assertEquals(240, proposal.newTasks.sumOf { it.plannedMinutes })
        assertEquals(setOf(monday, monday.plusDays(1)), proposal.newTasks.map { it.date }.toSet())
        assertTrue(proposal.newTasks.groupBy { it.date }.values.all { day -> day.sumOf { it.plannedMinutes } <= 120 })
    }

    @Test
    fun `missed work is spread across future days instead of stacked tomorrow`() {
        val missedMonday = task("missed", monday, 180)
        val proposal = engine.plan(snapshot(today = monday.plusDays(1), dailyMinutes = 60, tasks = listOf(missedMonday)), ReplanReason.DAY_MISSED)

        assertEquals(PlanTaskStatus.NAO_REALIZADA, proposal.transitions.single().to)
        assertEquals(180, proposal.newTasks.sumOf { it.plannedMinutes })
        assertEquals(3, proposal.newTasks.map { it.date }.distinct().size)
        assertTrue(proposal.newTasks.none { it.plannedMinutes > 60 })
    }

    @Test
    fun `partial execution preserves actual minutes and schedules only the remainder`() {
        val original = task("task-1", monday, 60, PlanTaskStatus.EM_ANDAMENTO)
        val execution = TaskExecution("execution-1", "task-1", 10L, 100L, monday, 25, 0, 0)
        val snapshot = snapshot(today = monday, dailyMinutes = 120, tasks = listOf(original), executions = listOf(execution))

        val proposal = engine.plan(snapshot, ReplanReason.TASK_PARTIAL)

        val successor = proposal.newTasks.single { it.replannedFromTaskId == "task-1" }
        assertEquals(35, successor.plannedMinutes)
        assertEquals(PlanTaskStatus.REPROGRAMADA, proposal.transitions.single().to)
        assertEquals(25, snapshot.executions.single().minutes)
    }

    @Test
    fun `completed task is preserved and never generated again`() {
        val completed = task("done", monday, 60, PlanTaskStatus.CONCLUIDA)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(completed)), ReplanReason.TASK_COMPLETED)

        assertEquals(setOf("done"), proposal.preservedTaskIds)
        assertTrue(proposal.transitions.isEmpty())
        assertTrue(proposal.newTasks.isEmpty())
    }

    @Test
    fun `locked task remains on its original day`() {
        val locked = task("locked", monday.plusDays(1), 60).copy(locked = true)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 30, tasks = listOf(locked)), ReplanReason.AVAILABILITY_CHANGED)

        assertEquals(setOf("locked"), proposal.preservedTaskIds)
        assertTrue(proposal.transitions.isEmpty())
        assertTrue(proposal.newTasks.isEmpty())
    }

    @Test
    fun `task on locked day remains untouched even when task itself is unlocked`() {
        val date = monday.plusDays(1)
        val fixed = task("fixed-day", date, 60)
        val base = snapshot(today = monday, dailyMinutes = 120, tasks = listOf(fixed))
        val proposal = engine.plan(
            base.copy(dayOverrides = mapOf(date to DayCapacityOverride(date = date, locked = true))),
            ReplanReason.AVAILABILITY_CHANGED,
        )

        assertEquals(setOf("fixed-day"), proposal.preservedTaskIds)
        assertTrue(proposal.transitions.isEmpty())
        assertFalse(proposal.newTasks.any { it.replannedFromTaskId == "fixed-day" })
    }

    @Test
    fun `more availability schedules more pending demand without touching completed work`() {
        val completed = task("done", monday.minusDays(1), 60, PlanTaskStatus.CONCLUIDA)
        val demand = TaskDemand("pending", 10L, 100L, PlanTaskType.THEORY, 600, priority = PlanPriority.CRITICAL)
        val smaller = engine.plan(snapshot(today = monday, dailyMinutes = 60, tasks = listOf(completed), demands = listOf(demand)), ReplanReason.AVAILABILITY_CHANGED)
        val larger = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(completed), demands = listOf(demand)), ReplanReason.AVAILABILITY_CHANGED)

        assertTrue(larger.newTasks.sumOf { it.plannedMinutes } > smaller.newTasks.sumOf { it.plannedMinutes })
        assertEquals(setOf("done"), larger.preservedTaskIds)
    }

    private fun snapshot(
        today: LocalDate,
        dailyMinutes: Int,
        tasks: List<PlannerTask>,
        executions: List<TaskExecution> = emptyList(),
        demands: List<TaskDemand> = emptyList(),
    ) = StudyPlannerSnapshot(
        planId = "plan-1",
        revision = 3,
        today = today,
        availability = DayOfWeek.entries.associateWith { day -> DailyCapacity(day, dailyMinutes) },
        subjects = listOf(SubjectDemand(10L, "Segurança", PlanPriority.CRITICAL)),
        demands = demands,
        tasks = tasks,
        executions = executions,
        remainingPhaseMinutes = demands.sumOf { it.minutes } + tasks.filter { it.status != PlanTaskStatus.CONCLUIDA }.sumOf { it.plannedMinutes },
        policy = PlannerPolicy(horizonDays = 7, maxFlexibleSharePercent = 100),
    )

    private fun task(
        id: String,
        date: LocalDate,
        minutes: Int,
        status: PlanTaskStatus = PlanTaskStatus.PLANEJADA,
    ) = PlannerTask(
        id = id,
        planId = "plan-1",
        subjectId = 10L,
        topicId = 100L,
        date = date,
        type = PlanTaskType.THEORY,
        plannedMinutes = minutes,
        plannedQuestions = 0,
        priority = PlanPriority.CRITICAL,
        status = status,
        locked = false,
    )
}
