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
    fun `revisao espacada nao vira cartao no plano, ela fica na aba Revisoes`() {
        val estudado = topics(1L, 1, studied = true) + topics(1L, 30).drop(1)
        val reviews = listOf(
            ReviewDemand("1", 1L, 1_000L, today.minusDays(2), 30),
            ReviewDemand("2", 1L, 1_000L, today, 30),
        )
        val result = StudyPlanBlueprint.build(input(topics = estudado, reviews = reviews))

        assertTrue(result.demands.none { it.type == PlanTaskType.REVIEW })
        assertTrue(result.notes.any { "Revisões" in it })
    }

    @Test
    fun `o motor separa o tempo das revisoes do dia, sem passar de um terco do dia`() {
        val snapshot = StudyPlannerSnapshot(
            planId = "p", revision = 1, today = today,
            availability = DayOfWeek.entries.associateWith { DailyCapacity(it, 120) },
            subjects = emptyList(), demands = emptyList(),
            reviews = (1L..5L).map { ReviewDemand("$it", 1L, it, today.minusDays(1), 30) } +
                (6L..60L).map { ReviewDemand("$it", 1L, it, today.plusDays(1), 30) },
        )
        val reserve = StudyPlannerEngine().reviewReserve(snapshot, mapOf(today to 120, today.plusDays(1) to 120))

        assertEquals(5 * StudyPlannerEngine.REVIEW_MINUTES, reserve[today])
        assertEquals(120 * StudyPlannerEngine.REVIEW_SHARE / 100, reserve[today.plusDays(1)])
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
    fun `depois de ver o conteudo as questoes se espalham por todas as materias`() {
        val result = StudyPlanBlueprint.build(
            input(
                subjects = listOf(subject(1L, 5, 0), subject(2L, 1, 1)),
                topics = topics(1L, 10, studied = true) + topics(2L, 10, studied = true),
            ),
        )
        val treino = result.demands.filter { it.id.startsWith("practice:") }.groupBy { it.subjectId }

        assertTrue(treino.getValue(1L).isNotEmpty() && treino.getValue(2L).isNotEmpty())
        assertTrue(result.demands.none { it.id.startsWith("battery:") })
    }

    @Test
    fun `quem diz dominar a materia faz diagnostico de questoes em vez de teoria`() {
        val sabe = BlueprintSubject(1L, "Matéria 1", PlanPriority.MEDIUM, 3, 0, dimensions = StudyDimensions(initialKnowledge = InitialKnowledge.STRONG))
        val result = StudyPlanBlueprint.build(input(subjects = listOf(sabe), topics = topics(1L, 10)))

        assertTrue(result.demands.none { it.type == PlanTaskType.THEORY })
        assertTrue(result.demands.any { it.id.startsWith("diagnostic:") && it.questions == StudyPlanBlueprint.DIAGNOSTIC_QUESTIONS })
    }

    @Test
    fun `materia dificil ganha mais tempo de teoria`() {
        fun teoria(d: PersonalDifficulty) = StudyPlanBlueprint.build(
            input(subjects = listOf(BlueprintSubject(1L, "M", PlanPriority.MEDIUM, 3, 0, dimensions = StudyDimensions(personalDifficulty = d)))),
        ).demands.first { it.type == PlanTaskType.THEORY }.minutes

        assertTrue(teoria(PersonalDifficulty.VERY_HARD) > teoria(PersonalDifficulty.NORMAL))
        assertTrue(teoria(PersonalDifficulty.EASY) < teoria(PersonalDifficulty.NORMAL))
    }

    @Test
    fun `materia de peso maior anda mais rapido e todas aparecem`() {
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

        assertTrue(porMateria.getValue(1L) >= porMateria.getValue(2L))
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

        // Ponto fraco volta como bateria de questões do próprio tópico (o app e o site abrem as questões dele).
        assertTrue(result.demands.any { it.id.startsWith("practice:") && it.type == PlanTaskType.QUESTIONS && it.topicId != null })
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
