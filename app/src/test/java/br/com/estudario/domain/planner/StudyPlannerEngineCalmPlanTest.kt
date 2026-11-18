package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/**
 * Regras que dão tranquilidade a quem segue o plano, achadas no teste de estresse (PlanStressSimulation):
 * a teoria de um tópico é uma leitura só, nada de caco, questão nunca antes da teoria e o ritmo pessoal
 * não se distorce com "fiz uma parte".
 */
class StudyPlannerEngineCalmPlanTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate(2026, 9, 14)
    private val policy = PlannerPolicy(horizonDays = 7, minimumBlockMinutes = 25, preferredBlockMinutes = 50, maxFlexibleSharePercent = 100)

    @Test
    fun `teoria de um topico nao e cortada para completar o dia`() {
        // dia de 80 min: a primeira teoria (50) deixa 30 livres. Antes a segunda era cortada em 25 hoje + 25 amanhã.
        val proposal = engine.plan(snapshot(80, demands = listOf(theory(1), theory(2))), ReplanReason.INITIAL)

        val second = proposal.newTasks.filter { it.topicId == 2L }
        assertEquals(1, second.size)
        assertEquals(50, second.single().plannedMinutes)
        assertEquals(monday.plusDays(1), second.single().date)
    }

    @Test
    fun `teoria maior que qualquer dia ainda e dividida`() {
        val proposal = engine.plan(snapshot(60, demands = listOf(theory(1, minutes = 120))), ReplanReason.INITIAL)

        assertEquals(120, proposal.newTasks.sumOf { it.plannedMinutes })
        assertTrue(proposal.newTasks.all { it.plannedMinutes >= 25 })
        assertTrue(proposal.newTasks.groupBy { it.date }.values.all { day -> day.sumOf { it.plannedMinutes } <= 60 })
    }

    @Test
    fun `tarefa que nao cabe inteira nem em dois blocos minimos nao deixa caco`() {
        // dia de 79 min: teoria de 50 e sobram 29. As questões de 45 min viravam 29 hoje + 16 amanhã.
        val proposal = engine.plan(snapshot(79, demands = listOf(theory(1), questions(2, minutes = 45, order = 1))), ReplanReason.INITIAL)

        val pieces = proposal.newTasks.filter { it.topicId == 2L }
        assertEquals(listOf(45), pieces.map { it.plannedMinutes })
        assertEquals(monday.plusDays(1), pieces.single().date)
    }

    @Test
    fun `questoes da semana estavel acompanham a teoria que voltou como demanda nova`() {
        // a teoria foi pulada ontem e volta daqui a 3 dias; as questões do tópico, marcadas para amanhã, não ficam antes dela
        val skipped = task("teoria-pulada", monday.minusDays(1), PlanTaskType.THEORY, 50, topic = 1, status = PlanTaskStatus.NAO_REALIZADA)
        val pending = task("questoes", monday.plusDays(1), PlanTaskType.QUESTIONS, 30, topic = 1)
        val back = theory(1).copy(anchorDate = monday.plusDays(3))
        val proposal = engine.plan(snapshot(120, demands = listOf(back), tasks = listOf(skipped, pending)), ReplanReason.DAY_MISSED)

        assertFalse("questoes" in proposal.preservedTaskIds)
        val theory = proposal.newTasks.single { it.type == PlanTaskType.THEORY }
        val questions = proposal.newTasks.single { it.type == PlanTaskType.QUESTIONS }
        assertTrue(questions.date > theory.date || (questions.date == theory.date && questions.sequence >= theory.sequence))
    }

    @Test
    fun `questoes de um topico esperam quando a teoria dele nao coube no periodo`() {
        // 2 dias de 60 min: a teoria do tópico 2 não cabe inteira. As questões dele (10 min) cabiam e entravam sozinhas.
        val demands = listOf(theory(1), questions(1, minutes = 30, order = 1), theory(2).copy(order = 2), questions(2, minutes = 10, order = 3))
        val proposal = engine.plan(snapshot(60, demands = demands).let { it.copy(policy = it.policy.copy(horizonDays = 2)) }, ReplanReason.INITIAL)

        assertTrue(proposal.newTasks.none { it.topicId == 2L && it.type == PlanTaskType.QUESTIONS })
        assertTrue(proposal.capacity.unallocated.any { it.demandId == "q2" })
    }

    @Test
    fun `tarefa nova entra no dia depois da teoria que ja estava marcada nele`() {
        // a tela ordena o dia pela sequência: as questões novas do tópico não podem ficar acima da teoria dele
        val kept = task("teoria", monday.plusDays(1), PlanTaskType.THEORY, 50, topic = 1).copy(sequence = 40)
        val proposal = engine.plan(snapshot(120, demands = listOf(questions(1, minutes = 30).copy(anchorDate = monday.plusDays(1))), tasks = listOf(kept)), ReplanReason.DAY_MISSED)

        val added = proposal.newTasks.single()
        assertEquals(monday.plusDays(1), added.date)
        assertTrue(added.sequence > 40)
    }

    @Test
    fun `copia da mesma teoria vinda de outro aparelho sai do plano`() {
        // dois aparelhos reorganizaram sem sincronizar: a teoria de 50 min do tópico ficou marcada duas vezes
        val mine = task("daqui", monday.plusDays(8), PlanTaskType.THEORY, 50, topic = 1)
        val theirs = task("de-la", monday.plusDays(9), PlanTaskType.THEORY, 50, topic = 1)
        val base = snapshot(120, tasks = listOf(mine, theirs))
        val proposal = engine.plan(base.copy(policy = policy.copy(horizonDays = 14), theoryNeedByTopic = mapOf(1L to 50)), ReplanReason.DAY_MISSED)

        assertTrue(proposal.transitions.any { it.taskId == "de-la" })
        assertEquals(50, proposal.newTasks.filter { it.topicId == 1L }.sumOf { it.plannedMinutes } + listOf(mine).filter { it.id in proposal.preservedTaskIds }.sumOf { it.plannedMinutes })
    }

    @Test
    fun `partes de uma teoria ainda nao comecada voltam a ser uma leitura so ao remarcar`() {
        // a teoria tinha sido cortada em 25 + 25 em dias seguidos; ao remarcar, com espaço, ela volta inteira num dia
        val first = task("parte-1", monday.plusDays(8), PlanTaskType.THEORY, 25, topic = 1)
        val second = task("parte-2", monday.plusDays(9), PlanTaskType.THEORY, 25, topic = 1)
        val base = snapshot(120, tasks = listOf(first, second))
        val proposal = engine.plan(base.copy(policy = policy.copy(horizonDays = 14)), ReplanReason.DAY_MISSED)

        assertEquals(listOf(50), proposal.newTasks.filter { it.topicId == 1L }.map { it.plannedMinutes })
    }

    @Test
    fun `dois cartoes iguais no mesmo dia viram um`() {
        val a = task("a", monday.plusDays(1), PlanTaskType.QUESTIONS, 30, topic = 1)
        val b = task("b", monday.plusDays(1), PlanTaskType.QUESTIONS, 30, topic = 1).copy(sequence = 5)
        val proposal = engine.plan(snapshot(120, tasks = listOf(a, b)), ReplanReason.DAY_MISSED)

        assertTrue("a" in proposal.preservedTaskIds)
        assertEquals(listOf("b"), proposal.transitions.map { it.taskId })
        assertTrue(proposal.newTasks.isEmpty())
    }

    @Test
    fun `dia da semana estavel acima das horas solta as ultimas questoes`() {
        // amanhã ficou com 120 min num dia de 100 (dois aparelhos, plano antigo): as questões do fim saem para outro dia
        val tomorrow = monday.plusDays(1)
        val tasks = listOf(
            task("teoria", tomorrow, PlanTaskType.THEORY, 50, topic = 1),
            task("questoes-1", tomorrow, PlanTaskType.QUESTIONS, 30, topic = 1).copy(sequence = 1),
            task("questoes-2", tomorrow, PlanTaskType.QUESTIONS, 40, topic = 2).copy(sequence = 2),
        )
        val proposal = engine.plan(snapshot(100, tasks = tasks), ReplanReason.DAY_MISSED)

        assertTrue(proposal.preservedTaskIds.containsAll(listOf("teoria", "questoes-1")))
        val moved = proposal.newTasks.single { it.replannedFromTaskId == "questoes-2" }
        assertTrue(moved.date != tomorrow)
        assertEquals(40, moved.plannedMinutes)
    }

    @Test
    fun `resto curto de uma teoria feita em parte continua marcado`() {
        // leu 40 de 50 min: os 10 que faltam ficam no dia. Antes o resto sumia e a teoria inteira voltava semanas depois.
        val started = task("teoria", monday, PlanTaskType.THEORY, 50, topic = 1, status = PlanTaskStatus.EM_ANDAMENTO)
        val execution = TaskExecution("e1", "teoria", 10L, 1L, monday, 40, 0, 0)
        val proposal = engine.plan(snapshot(120, tasks = listOf(started), executions = listOf(execution)), ReplanReason.TASK_PARTIAL)

        assertEquals(10, proposal.newTasks.single { it.replannedFromTaskId == "teoria" }.plannedMinutes)
    }

    @Test
    fun `teoria lida em parte e depois pulada volta sem o que ja foi lido`() {
        // leu 20 de 50 min e pulou o resto: a teoria volta com os 30 que faltam, não com os 50 de novo
        val started = task("teoria", monday.minusDays(2), PlanTaskType.THEORY, 50, topic = 1, status = PlanTaskStatus.REPROGRAMADA)
        val skipped = task("resto", monday.minusDays(1), PlanTaskType.THEORY, 30, topic = 1, status = PlanTaskStatus.NAO_REALIZADA)
        val execution = TaskExecution("e1", "teoria", 10L, 1L, monday.minusDays(2), 20, 0, 0)
        val proposal = engine.plan(snapshot(120, demands = listOf(theory(1)), tasks = listOf(started, skipped), executions = listOf(execution)), ReplanReason.DAY_MISSED)

        assertEquals(30, proposal.newTasks.filter { it.topicId == 1L }.sumOf { it.plannedMinutes })
    }

    @Test
    fun `ritmo pessoal conta a teoria feita em parte e depois remarcada`() {
        // 4 tópicos de 50 min lidos em duas vezes (30 + 22): quem lê no ritmo padrão não vira "rápido" (0,7)
        val tasks = (1L..4L).flatMap { topic ->
            listOf(
                task("a$topic", monday.plusDays(topic.toInt()), PlanTaskType.THEORY, 50, topic = topic, status = PlanTaskStatus.REPROGRAMADA),
                task("b$topic", monday.plusDays(topic.toInt()), PlanTaskType.THEORY, 20, topic = topic, status = PlanTaskStatus.CONCLUIDA),
            )
        }
        val executions = (1L..4L).flatMap { topic ->
            listOf(TaskExecution("ea$topic", "a$topic", 10L, topic, monday, 30, 0, 0), TaskExecution("eb$topic", "b$topic", 10L, topic, monday, 22, 0, 0))
        }
        val pace = PersonalPace.measure(tasks, executions) { 50 }

        assertEquals(4, pace.theorySamples)
        assertEquals(1.04, pace.theoryFactor, 0.001)
    }

    @Test
    fun `ritmo pessoal ignora o topico com a leitura pela metade`() {
        val done = (1L..3L).map { task("t$it", monday, PlanTaskType.THEORY, 50, topic = it, status = PlanTaskStatus.CONCLUIDA) }
        val half = task("meio", monday, PlanTaskType.THEORY, 50, topic = 9, status = PlanTaskStatus.EM_ANDAMENTO)
        val executions = (1L..3L).map { TaskExecution("e$it", "t$it", 10L, it, monday, 55, 0, 0) } + TaskExecution("em", "meio", 10L, 9L, monday, 16, 0, 0)
        val pace = PersonalPace.measure(done + half, executions) { 50 }

        assertEquals(3, pace.theorySamples)
    }

    private fun theory(topic: Long, minutes: Int = 50) = TaskDemand(
        id = "t$topic", subjectId = 10L, topicId = topic, type = PlanTaskType.THEORY, minutes = minutes, priority = PlanPriority.HIGH, order = 0, topicPosition = topic.toInt(),
    )

    private fun questions(topic: Long, minutes: Int, order: Int = 0) = TaskDemand(
        id = "q$topic", subjectId = 10L, topicId = topic, type = PlanTaskType.QUESTIONS, minutes = minutes, questions = minutes / 2, priority = PlanPriority.HIGH, order = order, topicPosition = topic.toInt(),
    )

    private fun task(id: String, date: LocalDate, type: PlanTaskType, minutes: Int, topic: Long, status: PlanTaskStatus = PlanTaskStatus.PLANEJADA) = PlannerTask(
        id = id, planId = "plan-1", subjectId = 10L, topicId = topic, date = date, type = type, plannedMinutes = minutes,
        plannedQuestions = if (type == PlanTaskType.QUESTIONS) minutes / 2 else 0, priority = PlanPriority.HIGH, status = status, locked = false,
    )

    private fun snapshot(
        dailyMinutes: Int,
        demands: List<TaskDemand> = emptyList(),
        tasks: List<PlannerTask> = emptyList(),
        executions: List<TaskExecution> = emptyList(),
    ) = StudyPlannerSnapshot(
        planId = "plan-1",
        revision = 3,
        today = monday,
        availability = DayOfWeek.entries.associateWith { day -> DailyCapacity(day, dailyMinutes) },
        subjects = listOf(SubjectDemand(10L, "Português", PlanPriority.HIGH)),
        demands = demands,
        tasks = tasks,
        executions = executions,
        remainingPhaseMinutes = demands.sumOf { it.minutes },
        policy = policy,
    )
}
