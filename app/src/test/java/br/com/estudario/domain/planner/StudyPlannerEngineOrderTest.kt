package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A ordem do dia: a teoria inteira de um tópico vem antes das questões dele, e blocos da mesma
 * atividade no mesmo dia são uma tarefa só. Antes o rodízio punha as questões entre as duas metades
 * da teoria e a tela mostrava "Teoria" duas vezes.
 */
class StudyPlannerEngineOrderTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate.of(2026, 9, 14)

    @Test
    fun `theory of a topic is fully scheduled before its questions`() {
        val proposal = engine.plan(snapshot(dailyMinutes = 150, demands = topicUnits(topics = 3)), ReplanReason.INITIAL)

        val ordered = proposal.newTasks.sortedWith(compareBy({ it.date }, { it.sequence }))
        for (topic in listOf(100L, 101L, 102L)) {
            val theory = ordered.indexOfLast { it.topicId == topic && it.type == PlanTaskType.THEORY }
            val questions = ordered.indexOfFirst { it.topicId == topic && it.type == PlanTaskType.QUESTIONS }
            if (theory >= 0 && questions >= 0) assertTrue("tópico $topic: questões antes da teoria", theory < questions)
        }
    }

    @Test
    fun `blocks of the same activity on the same day become one task`() {
        val proposal = engine.plan(snapshot(dailyMinutes = 150, demands = topicUnits(topics = 1, theoryMinutes = 50)), ReplanReason.INITIAL)

        val theoryOnFirstDay = proposal.newTasks.filter { it.type == PlanTaskType.THEORY && it.date == monday }
        assertEquals(1, theoryOnFirstDay.size)
        assertEquals(50, theoryOnFirstDay.single().plannedMinutes)
    }

    @Test
    fun `sequence is unique and follows generation order`() {
        val proposal = engine.plan(snapshot(dailyMinutes = 150, demands = topicUnits(topics = 4)), ReplanReason.INITIAL)

        val sequences = proposal.newTasks.map { it.sequence }
        assertEquals(sequences.size, sequences.toSet().size)
        proposal.newTasks.groupBy { it.date }.forEach { (date, tasks) ->
            assertTrue(tasks.sumOf { it.plannedMinutes } <= proposal.capacity.availableByDate.getValue(date))
        }
    }

    private fun topicUnits(topics: Int, theoryMinutes: Int = 50): List<TaskDemand> {
        var order = 0
        return (0 until topics).flatMap { index ->
            val topicId = 100L + index
            listOf(
                TaskDemand(
                    id = "theory:$topicId", subjectId = 10L, topicId = topicId, type = PlanTaskType.THEORY,
                    minutes = theoryMinutes, priority = PlanPriority.HIGH, subjectPosition = 0, topicPosition = index, order = order++,
                ),
                TaskDemand(
                    id = "topic-questions:$topicId", subjectId = 10L, topicId = topicId, type = PlanTaskType.QUESTIONS,
                    minutes = 30, questions = 10, priority = PlanPriority.HIGH, subjectPosition = 0, topicPosition = index, order = order++,
                ),
            )
        }
    }

    private fun snapshot(dailyMinutes: Int, demands: List<TaskDemand>) = StudyPlannerSnapshot(
        planId = "plan-1",
        revision = 1,
        today = monday,
        availability = DayOfWeek.entries.associateWith { day -> DailyCapacity(day, dailyMinutes, false) },
        subjects = listOf(SubjectDemand(10L, "Português", PlanPriority.HIGH)),
        demands = demands,
        remainingPhaseMinutes = demands.sumOf { it.minutes },
        policy = PlannerPolicy(horizonDays = 7, preferredBlockMinutes = 25, minimumBlockMinutes = 15),
    )
}
