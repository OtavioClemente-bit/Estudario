package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/** Teto de matérias novas por dia (Grátis 2, pagos 4) e, no Grátis, o dia de hoje não muda ao replanejar. */
class StudyPlannerEngineFreeRulesTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate(2026, 9, 14)
    private val free = PlannerPolicy(horizonDays = 7, maxFlexibleSharePercent = 100, preferredBlockMinutes = 60, maxTheoryTopicsPerDay = PlannerSnapshotBuilder.FREE_TOPICS_PER_DAY, keepTodayTasks = true)
    private val paid = free.copy(maxTheoryTopicsPerDay = PlannerSnapshotBuilder.PAID_TOPICS_PER_DAY, keepTodayTasks = false)

    @Test
    fun `many hours in a free day still bring at most two new topics`() {
        val demands = (1..10).map { theory(it.toLong()) }
        val proposal = engine.plan(snapshot(policy = free, dailyMinutes = 600, demands = demands), ReplanReason.INITIAL)

        val perDay = proposal.newTasks.filter { it.type == PlanTaskType.THEORY }.groupBy { it.date }.mapValues { (_, t) -> t.mapNotNull { it.topicId }.toSet().size }
        assertTrue(perDay.values.all { it <= 2 })
        assertEquals(2, perDay.getValue(monday))
        assertEquals(10, proposal.newTasks.mapNotNull { it.topicId }.toSet().size)
    }

    @Test
    fun `paid plans bring at most four new topics a day, however many hours`() {
        val demands = (1..10).map { theory(it.toLong()) }
        val proposal = engine.plan(snapshot(policy = paid, dailyMinutes = 600, demands = demands), ReplanReason.INITIAL)

        assertEquals(4, proposal.newTasks.filter { it.date == monday }.mapNotNull { it.topicId }.toSet().size)
        assertEquals(10, proposal.newTasks.mapNotNull { it.topicId }.toSet().size)
    }

    @Test
    fun `replanning a free plan keeps today's topics and brings nothing new today`() {
        val today = listOf(task("hoje-1", 1L), task("hoje-2", 2L))
        val demands = (3..6).map { theory(it.toLong()) }
        val proposal = engine.plan(snapshot(policy = free, dailyMinutes = 600, demands = demands, tasks = today), ReplanReason.AVAILABILITY_CHANGED)

        assertTrue(proposal.preservedTaskIds.containsAll(setOf("hoje-1", "hoje-2")))
        assertTrue(proposal.transitions.isEmpty())
        assertTrue(proposal.newTasks.none { it.date == monday })
    }

    private fun theory(topic: Long) = TaskDemand(
        id = "t$topic", subjectId = 10L, topicId = topic, type = PlanTaskType.THEORY, minutes = 60, priority = PlanPriority.CRITICAL, topicPosition = topic.toInt(),
    )

    private fun task(id: String, topic: Long) = PlannerTask(
        id = id, planId = "plan-1", subjectId = 10L, topicId = topic, date = monday, type = PlanTaskType.THEORY,
        plannedMinutes = 60, plannedQuestions = 0, priority = PlanPriority.CRITICAL, status = PlanTaskStatus.PLANEJADA, locked = false,
    )

    private fun snapshot(policy: PlannerPolicy, dailyMinutes: Int, demands: List<TaskDemand>, tasks: List<PlannerTask> = emptyList()) = StudyPlannerSnapshot(
        planId = "plan-1",
        revision = 3,
        today = monday,
        availability = DayOfWeek.entries.associateWith { day -> DailyCapacity(day, dailyMinutes) },
        subjects = listOf(SubjectDemand(10L, "Português", PlanPriority.CRITICAL)),
        demands = demands,
        tasks = tasks,
        remainingPhaseMinutes = demands.sumOf { it.minutes },
        policy = policy,
    )
}
