package br.com.estudario.ui.setup

import br.com.estudario.domain.setup.InitialSetupSnapshot
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SetupRollbackTest {
    private val baseline = SetupBaseline(
        competitionIds = setOf(1L, 2L),
        subjectIds = setOf(10L, 11L, 20L),
        planIds = setOf("plano-a"),
        primaryCompetitionId = 2L,
        activePlanIds = setOf("plano-a"),
        masterPlanIds = setOf("plano-a"),
    )

    @Test
    fun `retrato sobrevive a gravar e ler das preferencias`() {
        assertEquals(baseline, SetupBaseline.decode(baseline.encode()))
        assertEquals(SetupBaseline(), SetupBaseline.decode(SetupBaseline().encode()))
        assertNull(SetupBaseline.decode(null))
        assertNull(SetupBaseline.decode("lixo"))
    }

    @Test
    fun `cancelar apaga o concurso escolhido no catalogo com as materias e o plano`() {
        // Escolheu o Bombeiro (concurso 3, matérias 30-32) e montou o plano antes de cancelar.
        val plan = SetupRollback.plan(
            baseline,
            competitionIds = listOf(1L, 2L, 3L),
            subjects = listOf(10L to 1L, 11L to 1L, 20L to 2L, 30L to 3L, 31L to 3L, 32L to 3L),
            planIds = listOf("plano-a", "plano-bombeiro"),
        )
        assertEquals(listOf(3L), plan.competitionIds)
        // As matérias do concurso novo saem junto com ele, não uma a uma.
        assertEquals(emptyList<Long>(), plan.subjectIds)
        assertEquals(listOf("plano-bombeiro"), plan.planIds)
    }

    @Test
    fun `materia nova num concurso que ja existia sai, as antigas ficam`() {
        val plan = SetupRollback.plan(
            baseline,
            competitionIds = listOf(1L, 2L),
            subjects = listOf(10L to 1L, 11L to 1L, 20L to 2L, 40L to 2L),
            planIds = listOf("plano-a"),
        )
        assertEquals(emptyList<Long>(), plan.competitionIds)
        assertEquals(listOf(40L), plan.subjectIds)
        assertTrue(plan.planIds.isEmpty())
    }

    @Test
    fun `nada criado nao apaga nada`() {
        val plan = SetupRollback.plan(baseline, listOf(1L, 2L), listOf(10L to 1L, 11L to 1L, 20L to 2L), listOf("plano-a"))
        assertTrue(plan.isEmpty)
    }

    @Test
    fun `concurso que ja existia nunca e apagado, mesmo fora do retrato`() {
        // Um id menor que o maior do retrato não foi criado agora (os ids só crescem).
        val plan = SetupRollback.plan(baseline.copy(competitionIds = setOf(5L)), listOf(4L, 5L, 6L), emptyList(), emptyList())
        assertEquals(listOf(6L), plan.competitionIds)
    }

    @Test
    fun `a tela segue a etapa nova logo apos o mais Novo, nao a tela final da configuracao anterior`() {
        val stale = InitialSetupSnapshot(status = InitialSetupStatus.COMPLETED, step = InitialSetupStep.READY, competitionId = 2L)
        val fresh = stale.copy(status = InitialSetupStatus.IN_PROGRESS, step = InitialSetupStep.COMPETITION, competitionId = null)
        val shown = SetupFreshState.effectiveSnapshot(fresh, stale)
        assertEquals(InitialSetupStep.COMPETITION, shown.step)
        assertEquals(InitialSetupStatus.IN_PROGRESS, shown.status)
        assertTrue(SetupFreshState.isStale(fresh, stale))
        assertFalse(SetupFreshState.isStale(fresh, fresh))
        // Sem a preferência carregada ainda, vale o estado calculado.
        assertEquals(stale, SetupFreshState.effectiveSnapshot(null, stale))
    }
}
