package br.com.estudario.domain

import br.com.estudario.domain.planner.PlanTaskType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ProgressEngineTest {
    private val hoje = LocalDate.of(2026, 9, 18)

    private fun input(
        days: List<DailyActivity> = emptyList(),
        planWork: List<ProgressEngine.PlanWork> = emptyList(),
        goal: DailyGoal = DailyGoal(20),
        totalQuestions: Int = 0,
        correctQuestions: Int = 0,
        reviews: Int = 0,
        topicsStudied: Int = 0,
        topicsTotal: Int = 0,
        bestStreak: Int = 0,
    ) = ProgressEngine.ProgressInput(
        today = hoje,
        days = days,
        goal = goal,
        planWork = planWork,
        bestStreak = bestStreak,
        currentStreak = bestStreak,
        totalQuestions = totalQuestions,
        correctQuestions = correctQuestions,
        reviewsCompleted = reviews,
        topicsStudied = topicsStudied,
        topicsTotal = topicsTotal,
    )

    @Test
    fun `tarefa do plano rende mais que a mesma questao solta`() {
        // 20 questões dentro de uma tarefa do plano (50 min, 14 certas) contra as mesmas 20
        // questões respondidas fora do cronograma.
        val dentroDoPlano = ProgressEngine.planTaskXp(PlanTaskType.QUESTIONS, 50, 14)
        val foraDoPlano = 20 * 1 + 14 * 1

        assertTrue("plano=$dentroDoPlano avulso=$foraDoPlano", dentroDoPlano > foraDoPlano)
    }

    @Test
    fun `o plano é a maior fatia do xp de quem segue o cronograma`() {
        val semana = (0..6).map { offset ->
            DailyActivity(hoje.minusDays(offset.toLong()), questions = 25, correct = 18, planTasks = 2, minutes = 120)
        }
        val tarefas = (0..6).flatMap { offset ->
            listOf(
                ProgressEngine.PlanWork(hoje.minusDays(offset.toLong()), PlanTaskType.THEORY, 50, 0),
                ProgressEngine.PlanWork(hoje.minusDays(offset.toLong()), PlanTaskType.QUESTIONS, 40, 18),
            )
        }
        val resumo = ProgressEngine.evaluate(input(days = semana, planWork = tarefas))

        assertEquals("Tarefas do plano", resumo.sources.first().label)
    }

    @Test
    fun `simulado vale bem mais que teoria curta`() {
        assertTrue(
            ProgressEngine.planTaskXp(PlanTaskType.SIMULATION, 150, 50) >
                ProgressEngine.planTaskXp(PlanTaskType.THEORY, 50, 0) * 3,
        )
    }

    @Test
    fun `questoes tem teto diario para nao virar farm`() {
        val dentroDoTeto = ProgressEngine.evaluate(input(days = listOf(DailyActivity(hoje, questions = 120, correct = 120)))).totalXp
        val muitoAlem = ProgressEngine.evaluate(input(days = listOf(DailyActivity(hoje, questions = 900, correct = 900)))).totalXp

        assertEquals(dentroDoTeto, muitoAlem)
    }

    @Test
    fun `meta do dia e sequencia entram no xp do dia`() {
        val dias = (0..4).map { DailyActivity(hoje.minusDays(it.toLong()), questions = 30, correct = 20) }
        val resumo = ProgressEngine.evaluate(input(days = dias))

        assertTrue(resumo.xpToday > 0)
        assertTrue(resumo.sources.any { it.label == "Metas diárias e sequência" })
    }

    @Test
    fun `curva de nivel cresce e nunca anda para tras`() {
        assertEquals(1, ProgressEngine.levelFor(0))
        assertEquals(2, ProgressEngine.levelFor(100))
        assertEquals(1, ProgressEngine.levelFor(99))
        (1..40).forEach { level -> assertTrue(ProgressEngine.xpForLevel(level + 1) > ProgressEngine.xpForLevel(level)) }
    }

    @Test
    fun `emblema de edital acompanha a cobertura`() {
        val resumo = ProgressEngine.evaluate(input(topicsStudied = 50, topicsTotal = 100))
        val meio = resumo.badges.first { it.badge.id == "edital-50" }

        assertTrue(meio.earned)
        assertTrue(resumo.badges.first { it.badge.id == "edital-75" }.earned.not())
    }

    @Test
    fun `emblema de pontaria so conta com amostra suficiente`() {
        val poucas = ProgressEngine.evaluate(input(totalQuestions = 50, correctQuestions = 50))
        val muitas = ProgressEngine.evaluate(input(totalQuestions = 400, correctQuestions = 340))

        assertTrue(poucas.badges.first { it.badge.id == "acerto-70" }.earned.not())
        assertTrue(muitas.badges.first { it.badge.id == "acerto-70" }.earned)
    }

    @Test
    fun `emblemas de simulado saem das tarefas do plano`() {
        val resumo = ProgressEngine.evaluate(
            input(planWork = List(5) { ProgressEngine.PlanWork(hoje.minusDays(it.toLong()), PlanTaskType.SIMULATION, 150, 60) }),
        )

        assertTrue(resumo.badges.first { it.badge.id == "simulado-5" }.earned)
        assertTrue(resumo.badges.first { it.badge.id == "simulado-20" }.earned.not())
    }
}
