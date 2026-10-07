package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import br.com.estudario.time.*

class StudyPlanBlueprintTest {
    private val today = LocalDate(2026, 9, 14)

    private fun subject(id: Long, weight: Int, position: Int) =
        BlueprintSubject(id, "Matéria $id", PlanPriority.MEDIUM, weight, position)

    private fun topics(subjectId: Long, count: Int, studied: Boolean = false, accuracy: Int? = null, answered: Int = 0) =
        (0 until count).map { index ->
            BlueprintTopic(
                topicId = subjectId * 1_000 + index,
                subjectId = subjectId,
                title = "Tópico $index",
                position = index,
                studied = studied,
                answered = answered,
                accuracyPercent = accuracy,
            )
        }

    private fun input(
        config: StudyMethodConfig = StudyMethodConfig(),
        subjects: List<BlueprintSubject> = listOf(subject(1L, 3, 0)),
        topics: List<BlueprintTopic> = topics(1L, 30),
        reviews: List<ReviewDemand> = emptyList(),
        weekly: Int = 720,
        exam: LocalDate? = null,
    ) = BlueprintInput(
        today = today,
        planStart = today,
        examDate = exam,
        config = config,
        subjects = subjects,
        topics = topics,
        pendingReviews = reviews,
        weeklyCapacityMinutes = weekly,
        heaviestDay = DayOfWeek.SATURDAY,
    )

    @Test
    fun `revisao atrasada entra antes de qualquer conteudo novo`() {
        val atrasada = ReviewDemand("9", 1L, 1_000L, today.minusDays(3), 30)
        val estudado = topics(1L, 1, studied = true) + topics(1L, 30).drop(1)
        val result = StudyPlanBlueprint.build(input(topics = estudado, reviews = listOf(atrasada)))

        val primeira = result.demands.minByOrNull { it.order }
        assertEquals(PlanTaskType.REVIEW, primeira?.type)
        assertEquals(today.minusDays(3), primeira?.deadline)
    }

    @Test
    fun `revisoes do mesmo topico viram uma so e topico nao estudado nao revisa`() {
        val estudado = topics(1L, 1, studied = true) + topics(1L, 30).drop(1)
        val reviews = listOf(
            ReviewDemand("1", 1L, 1_000L, today.minusDays(2), 30),
            ReviewDemand("2", 1L, 1_000L, today.minusDays(1), 30),
            ReviewDemand("3", 1L, 1_000L, today, 30),
            // Tópico 1001 não foi estudado (plano começando do zero): não entra.
            ReviewDemand("4", 1L, 1_001L, today, 30),
        )
        val result = StudyPlanBlueprint.build(input(topics = estudado, reviews = reviews))

        val revisoes = result.demands.filter { it.type == PlanTaskType.REVIEW }
        assertEquals(1, revisoes.size)
        assertEquals(1_000L, revisoes.single().topicId)
    }

    @Test
    fun `simulado fica fora do plano por enquanto`() {
        val result = StudyPlanBlueprint.build(input(config = StudyMethodConfig(simulationsPerMonth = 4)))
        assertTrue(result.demands.none { it.type == PlanTaskType.SIMULATION })
    }

    @Test
    fun `cada topico novo vem com questoes do proprio topico logo depois`() {
        val result = StudyPlanBlueprint.build(input())
        val teoria = result.demands.first { it.type == PlanTaskType.THEORY }
        val questoes = result.demands.first { it.type == PlanTaskType.QUESTIONS && it.topicId == teoria.topicId }

        assertEquals(questoes.order, teoria.order + 1)
        assertEquals(15, questoes.questions)
    }

    @Test
    fun `bateria de quinze questoes usa dois minutos por questao sem arredondar para bloco`() {
        val result = StudyPlanBlueprint.build(input())
        val questoes = result.demands.first { it.type == PlanTaskType.QUESTIONS && it.topicId != null }

        assertEquals(30, questoes.minutes)
    }

    @Test
    fun `bateria semanal usa o tempo calculado por questao sem arredondar para bloco`() {
        val result = StudyPlanBlueprint.build(
            input(
                config = StudyMethodConfig(blockMinutes = 50, weeklyQuestionsTarget = 15, questionsPerTopic = 0, simulationsPerMonth = 0),
                topics = emptyList(),
                weekly = 600,
            ).copy(horizonDays = 7),
        )
        val bateria = result.demands.single { it.type == PlanTaskType.QUESTIONS }

        assertEquals(15, bateria.questions)
        assertEquals(30, bateria.minutes)
    }

    @Test
    fun `materia de peso maior recebe mais tempo e todas aparecem`() {
        val result = StudyPlanBlueprint.build(
            input(
                subjects = listOf(subject(1L, 5, 0), subject(2L, 1, 1)),
                topics = topics(1L, 30) + topics(2L, 30),
            ),
        )
        val porMateria = result.demands
            .filter { it.type == PlanTaskType.THEORY }
            .groupBy { it.subjectId }
            .mapValues { (_, list) -> list.sumOf { it.minutes } }

        assertTrue(porMateria.getValue(1L) > porMateria.getValue(2L) * 2)
        assertTrue(porMateria.getValue(2L) > 0)
    }

    @Test
    fun `as materias se alternam em vez de sair uma inteira antes da outra`() {
        val result = StudyPlanBlueprint.build(
            input(
                subjects = listOf(subject(1L, 3, 0), subject(2L, 3, 1)),
                topics = topics(1L, 20) + topics(2L, 20),
            ),
        )
        val ordem = result.demands.filter { it.type == PlanTaskType.THEORY }.sortedBy { it.order }.map { it.subjectId }

        assertTrue(ordem.size >= 4)
        assertTrue("esperava alternância entre matérias", ordem.take(4).toSet().size > 1)
    }

    @Test
    fun `reta final nao agenda teoria nova`() {
        val result = StudyPlanBlueprint.build(
            input(config = StudyMethodConfig.forProfile(StudyProfile.RETA_FINAL)),
        )

        assertTrue(result.demands.none { it.type == PlanTaskType.THEORY })
        assertEquals(PlanPhaseKind.RETA_FINAL, result.currentPhase.kind)
    }

    @Test
    fun `topico fraco volta como reforco`() {
        val result = StudyPlanBlueprint.build(
            input(
                config = StudyMethodConfig.forProfile(StudyProfile.APROFUNDANDO),
                topics = topics(1L, 5, studied = true, accuracy = 40, answered = 30),
            ),
        )

        assertNotNull(result.demands.firstOrNull { it.type == PlanTaskType.ACTIVE_RECALL })
    }

    @Test
    fun `toda tarefa é multiplo do bloco escolhido`() {
        val result = StudyPlanBlueprint.build(input(config = StudyMethodConfig(blockMinutes = 25)))
        val blocosInteiros = result.demands.filter { it.type !in setOf(PlanTaskType.REVIEW, PlanTaskType.QUESTIONS) }

        assertTrue(blocosInteiros.isNotEmpty())
        assertTrue(blocosInteiros.all { it.minutes % 25 == 0 })
    }

    @Test
    fun `com data de prova o plano ganha base aprofundamento e reta final`() {
        val result = StudyPlanBlueprint.build(input(exam = today.plusMonths(8)))

        assertEquals(
            listOf(PlanPhaseKind.BASE, PlanPhaseKind.APROFUNDAMENTO, PlanPhaseKind.RETA_FINAL),
            result.phases.map { it.kind },
        )
        assertTrue(result.phases.zipWithNext().all { (a, b) -> a.end < b.start })
    }

    @Test
    fun `o mesmo pedido gera sempre o mesmo plano`() {
        assertEquals(
            StudyPlanBlueprint.build(input()).demands,
            StudyPlanBlueprint.build(input()).demands,
        )
    }
}
