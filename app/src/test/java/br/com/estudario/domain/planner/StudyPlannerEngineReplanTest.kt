package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import br.com.estudario.time.*

class StudyPlannerEngineReplanTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate(2026, 9, 14)

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
    fun `daily reorganization keeps the tasks already planned for today`() {
        val today = monday.plusDays(1)
        // um tópico por tarefa: três teorias do mesmo tópico no mesmo dia são cópias (dois aparelhos) e o motor fica com uma
        val planned = listOf(task("a", today, 50, topicId = 101L), task("b", today, 40, topicId = 102L), task("c", today, 30, topicId = 103L))
        val proposal = engine.plan(snapshot(today = today, dailyMinutes = 120, tasks = planned), ReplanReason.DAY_MISSED)

        assertTrue(proposal.transitions.none { it.taskId in setOf("a", "b", "c") })
        assertTrue(proposal.newTasks.none { it.date == today })
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
    fun `concluir a teoria do dia nao puxa a atrasada nem tira as questoes de hoje`() {
        val late = task("late-theory", monday.minusDays(2), 50, topicId = 101L)
        val done = task("today-theory", monday, 50, PlanTaskStatus.CONCLUIDA)
        val questions = (1..4).map { task("q$it", monday, 20, topicId = 200L + it) }
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(late, done) + questions), ReplanReason.TASK_COMPLETED)

        assertTrue(proposal.transitions.isEmpty())
        assertTrue(proposal.newTasks.none { it.date == monday })
        assertTrue(proposal.preservedTaskIds.containsAll(listOf("late-theory", "q1", "q2", "q3", "q4")))
    }

    @Test
    fun `teoria concluida tira as outras partes da mesma teoria do plano`() {
        // a leitura dividida em 3 dias: a pessoa leu tudo no primeiro; as partes 2 e 3 saem, a de outro tópico fica
        val done = task("parte-1", monday, 60, PlanTaskStatus.CONCLUIDA, topicId = 131L)
        val p2 = task("parte-2", monday.plusDays(1), 65, topicId = 131L)
        val p3 = task("parte-3", monday.plusDays(2), 60, topicId = 131L)
        val other = task("outro", monday.plusDays(1), 50, topicId = 65L)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(done, p2, p3, other)), ReplanReason.TASK_COMPLETED)

        assertTrue(proposal.newTasks.none { it.topicId == 131L && it.type == PlanTaskType.THEORY })
        assertTrue(proposal.transitions.map { it.taskId }.containsAll(listOf("parte-2", "parte-3")))
        assertTrue("outro" in proposal.preservedTaskIds)
    }

    @Test
    fun `parte da teoria depois da semana estavel nao vira a teoria inteira de novo`() {
        // teoria de 75 min em duas partes: 50 dentro da semana estável (fica) e 25 logo depois dela (é remarcada).
        // Antes a parte de 25, por ser a única remarcada, ganhava o tamanho da teoria inteira (75) e o tópico
        // passava a ter 125 min; a cada dia a conta se repetia e a teoria chegava a 180 min em 4 partes.
        val inside = task("parte-1", monday.plusDays(6), 50, topicId = 131L)
        val outside = task("parte-2", monday.plusDays(7), 25, topicId = 131L)
        val base = snapshot(today = monday, dailyMinutes = 120, tasks = listOf(inside, outside))
        val proposal = engine.plan(base.copy(policy = base.policy.copy(horizonDays = 14), theoryMinutesByTopic = mapOf(131L to 75)), ReplanReason.DAY_MISSED)

        assertTrue("parte-1" in proposal.preservedTaskIds)
        assertEquals(25, proposal.newTasks.filter { it.topicId == 131L }.sumOf { it.plannedMinutes })
    }

    @Test
    fun `teoria de retomada marcada nao sai do plano por causa da primeira leitura`() {
        // o tópico foi lido há 40 dias e a pessoa segue errando: a teoria curta de retomada fica onde está
        val first = task("leitura", monday.minusDays(40), 50, PlanTaskStatus.CONCLUIDA, topicId = 131L)
        val retake = task("retomada", monday.plusDays(1), 25, topicId = 131L)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(first, retake)), ReplanReason.TASK_COMPLETED)

        assertTrue("retomada" in proposal.preservedTaskIds)
        assertTrue(proposal.transitions.isEmpty())
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

    @Test
    fun `completing work today does not pull tomorrow into today`() {
        val done = task("done", monday, 90, PlanTaskStatus.CONCLUIDA)
        val pending = TaskDemand("pending", 10L, 200L, PlanTaskType.THEORY, 600, priority = PlanPriority.CRITICAL)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 120, tasks = listOf(done), demands = listOf(pending)), ReplanReason.TASK_COMPLETED)

        // Concluir não traz matéria nova para hoje, mesmo sobrando tempo no dia: o resto fica para amanhã em diante.
        assertEquals(0, proposal.newTasks.filter { it.date == monday }.sumOf { it.plannedMinutes })
        assertTrue(proposal.newTasks.isNotEmpty())
    }

    @Test
    fun `minutes already studied in a partial task still count against today`() {
        val started = task("started", monday, 60, PlanTaskStatus.EM_ANDAMENTO)
        val execution = TaskExecution("execution-1", "started", 10L, 100L, monday, 40, 0, 0)
        val pending = TaskDemand("pending", 10L, 200L, PlanTaskType.THEORY, 600, priority = PlanPriority.CRITICAL)
        val proposal = engine.plan(
            snapshot(today = monday, dailyMinutes = 120, tasks = listOf(started), executions = listOf(execution), demands = listOf(pending)),
            ReplanReason.TASK_PARTIAL,
        )

        // Hoje fica só o que falta da tarefa começada (60 - 40 = 20); a pendente não é puxada para hoje.
        assertEquals(20, proposal.newTasks.filter { it.date == monday }.sumOf { it.plannedMinutes })
    }

    @Test
    fun `finishing every task of the day keeps the day finished`() {
        val first = task("first", monday, 60, PlanTaskStatus.CONCLUIDA)
        val second = task("second", monday, 30, PlanTaskStatus.CONCLUIDA)
        val tomorrow = task("tomorrow", monday.plusDays(1), 60)
        val proposal = engine.plan(snapshot(today = monday, dailyMinutes = 240, tasks = listOf(first, second, tomorrow)), ReplanReason.TASK_COMPLETED)

        assertTrue(proposal.newTasks.none { it.date == monday })
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
        topicId: Long = 100L,
    ) = PlannerTask(
        id = id,
        planId = "plan-1",
        subjectId = 10L,
        topicId = topicId,
        date = date,
        type = PlanTaskType.THEORY,
        plannedMinutes = minutes,
        plannedQuestions = 0,
        priority = PlanPriority.CRITICAL,
        status = status,
        locked = false,
    )
}
