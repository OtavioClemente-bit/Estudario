package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class StudyPlannerEngineAllocationTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate.of(2026, 9, 14)

    @Test
    fun `same snapshot always creates identical proposal`() {
        val snapshot = snapshot(
            dailyMinutes = 180,
            demands = listOf(
                demand("b", 20L, 200L, 90, PlanPriority.HIGH),
                demand("a", 10L, 100L, 90, PlanPriority.CRITICAL),
            ),
        )

        assertEquals(
            engine.plan(snapshot, ReplanReason.INITIAL),
            engine.plan(snapshot, ReplanReason.INITIAL),
        )
    }

    @Test
    fun `critical maintenance is reserved without monopolizing flexible time`() {
        val snapshot = snapshot(
            dailyMinutes = 120,
            subjects = listOf(
                SubjectDemand(10L, "Segurança", PlanPriority.CRITICAL, minimumMaintenanceMinutes = 120, position = 0),
                SubjectDemand(20L, "Português", PlanPriority.HIGH, minimumMaintenanceMinutes = 60, position = 1),
            ),
            demands = listOf(
                demand("security", 10L, 100L, 900, PlanPriority.CRITICAL, subjectPosition = 0),
                demand("portuguese", 20L, 200L, 900, PlanPriority.HIGH, subjectPosition = 1),
            ),
            policy = PlannerPolicy(horizonDays = 7, maxFlexibleSharePercent = 40),
        )

        val proposal = engine.plan(snapshot, ReplanReason.INITIAL)
        val minutesBySubject = proposal.newTasks.groupBy { it.subjectId }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }

        assertTrue(minutesBySubject.getValue(10L) >= 120)
        assertTrue(minutesBySubject.getValue(20L) >= 60)
        assertTrue(minutesBySubject.getValue(20L) > 0)
        assertTrue(minutesBySubject.getValue(10L) <= 384)
    }

    @Test
    fun `capacity deficit is explicit and no day exceeds availability`() {
        val snapshot = snapshot(
            dailyMinutes = 180,
            demands = listOf(demand("too-much", 10L, 100L, 1_500, PlanPriority.CRITICAL)),
            policy = PlannerPolicy(horizonDays = 7),
        )

        val proposal = engine.plan(snapshot, ReplanReason.INITIAL)

        assertEquals(1_080, proposal.capacity.availableMinutes)
        assertEquals(420, proposal.capacity.deficitMinutes)
        proposal.newTasks.groupBy { it.date }.forEach { (date, tasks) ->
            assertTrue(tasks.sumOf { it.plannedMinutes } <= proposal.capacity.availableByDate.getValue(date))
        }
    }

    @Test
    fun `dependencies wait until their prerequisite is completed`() {
        val blocked = demand("advanced", 10L, 101L, 60, PlanPriority.CRITICAL)
            .copy(dependsOnTaskIds = setOf("foundation"))
        val withoutCompletion = engine.plan(
            snapshot(dailyMinutes = 120, demands = listOf(blocked)),
            ReplanReason.INITIAL,
        )
        val withCompletion = engine.plan(
            snapshot(dailyMinutes = 120, demands = listOf(blocked), completedDependencies = setOf("foundation")),
            ReplanReason.INITIAL,
        )

        assertTrue(withoutCompletion.newTasks.isEmpty())
        assertEquals(60, withCompletion.newTasks.single().plannedMinutes)
    }

    private fun snapshot(
        dailyMinutes: Int,
        subjects: List<SubjectDemand> = listOf(SubjectDemand(10L, "Segurança", PlanPriority.CRITICAL)),
        demands: List<TaskDemand>,
        policy: PlannerPolicy = PlannerPolicy(horizonDays = 7),
        completedDependencies: Set<String> = emptySet(),
    ) = StudyPlannerSnapshot(
        planId = "plan-1",
        revision = 7,
        today = monday,
        availability = DayOfWeek.entries.associateWith { day ->
            DailyCapacity(day, if (day == DayOfWeek.SUNDAY) 0 else dailyMinutes, day == DayOfWeek.SUNDAY)
        },
        subjects = subjects,
        demands = demands,
        completedDependencyIds = completedDependencies,
        remainingPhaseMinutes = demands.sumOf { it.minutes },
        policy = policy,
    )

    private fun demand(
        id: String,
        subjectId: Long,
        topicId: Long,
        minutes: Int,
        priority: PlanPriority,
        subjectPosition: Int = 0,
    ) = TaskDemand(
        id = id,
        subjectId = subjectId,
        topicId = topicId,
        type = PlanTaskType.THEORY,
        minutes = minutes,
        priority = priority,
        subjectPosition = subjectPosition,
        topicPosition = 0,
    )
}
