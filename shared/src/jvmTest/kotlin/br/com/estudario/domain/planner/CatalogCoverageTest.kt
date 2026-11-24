package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Cobertura primeiro, com os editais reais do catálogo (Banco do Brasil e PMMG, casos reais de 23/11/2026: o plano
 * marcava a primeira leitura de 18 a 30% dos tópicos, só as 4 semanas do plano rolante). Todo tópico do edital tem de
 * ter a primeira leitura (teoria) no plano criado: sem prova sempre; com prova em 3, 6 ou 12 meses e 1h, 2h ou 4h por
 * dia, ou todos cabem (encurtando cada tópico se for preciso, antes da reta final) ou o plano diz que não cabe e por quê.
 */
class CatalogCoverageTest {
    private val today = LocalDate(2026, 11, 23)
    private val editais = listOf("bb-2022--agente-comercial", "bb-2022--agente-de-tecnologia", "pmmg-cfsd-qppm-2025", "pmmg-cfo-2027--oficial-cfo")

    private class Outcome(val topics: Int, val firstStudy: Map<Long, LocalDate>, val snapshot: StudyPlannerSnapshot, val proposal: PlanningProposal, val coverage: CoverageCheck?)

    private fun plan(id: String, minutes: Int, examInDays: Int?, free: Boolean = false, profile: StudyProfile = StudyProfile.DO_ZERO): Outcome {
        val input = CatalogEditalFixtures.input(CatalogEditalFixtures.load(id), today, minutes, examInDays, profile = profile, free = free)
        val snapshot = PlannerSnapshotBuilder.build(input)
        val proposal = StudyPlannerEngine().plan(snapshot, ReplanReason.INITIAL)
        val first = proposal.newTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null }
            .groupBy { it.topicId!! }.mapValues { (_, l) -> l.minOf { it.date } }
        return Outcome(input.topics.size, first, snapshot, proposal, snapshot.coverage)
    }

    @Test fun semProvaTodoTopicoTemPrimeiraLeituraNoPlano() {
        for (id in editais) for (minutes in listOf(60, 120, 240)) for (free in listOf(false, true)) for (profile in listOf(StudyProfile.DO_ZERO, StudyProfile.APROFUNDANDO)) {
            val o = plan(id, minutes, null, free, profile)
            assertEquals(o.topics, o.firstStudy.size, "$id ${minutes}min free=$free $profile: ${o.firstStudy.size}/${o.topics} tópicos com primeira leitura")
            // Questão nunca antes da teoria do tópico.
            o.proposal.newTasks.filter { it.type == PlanTaskType.QUESTIONS && it.topicId != null }.forEach { q ->
                val theory = o.firstStudy[q.topicId]
                assertTrue(theory == null || !q.date.isBefore(theory), "$id: questão do tópico ${q.topicId} em ${q.date}, teoria em $theory")
            }
        }
    }

    @Test fun comProvaTodoTopicoCabeAntesDaRetaFinalOuOPlanoDizQueNao() {
        for (id in editais) for (months in listOf(3, 6, 12)) for (minutes in listOf(60, 120, 240)) for (free in listOf(false, true)) {
            val days = months * 30
            val o = plan(id, minutes, days, free)
            val phases = StudyMethod.phases(today, today.plusDays(days.toLong()), StudyProfile.DO_ZERO)
            val retaFinal = phases.first { it.kind == PlanPhaseKind.RETA_FINAL }.start
            val coverage = assertNotNull(o.coverage, "$id: sem a conta da cobertura")
            if (coverage.impossible) {
                assertNotNull(coverage.reason)
                assertTrue(o.snapshot.notes.any { it == coverage.reason }, "$id: o motivo não chegou à pessoa")
                assertTrue(o.firstStudy.size >= coverage.fittingTopics * 9 / 10, "$id: prometeu ${coverage.fittingTopics}, marcou ${o.firstStudy.size}")
            } else {
                assertEquals(o.topics, o.firstStudy.size, "$id ${months}m ${minutes}min free=$free: ${o.firstStudy.size}/${o.topics} tópicos com primeira leitura")
                val late = o.firstStudy.filterValues { !it.isBefore(retaFinal) }
                assertTrue(late.isEmpty(), "$id ${months}m ${minutes}min: ${late.size} tópicos só na reta final ($retaFinal)")
            }
        }
    }

    @Test fun tempoMenorQueOMinimoPorTopicoEhDitoComClarezaEPrioizaOPeso() {
        // 30 min por dia e prova em 2 meses: nem 20 min de teoria por tópico cabe para os 106 do BB.
        val o = plan("bb-2022--agente-comercial", 30, 60)
        val coverage = assertNotNull(o.coverage)
        assertTrue(coverage.impossible)
        assertTrue(coverage.fittingTopics in 1 until o.topics)
        assertTrue(o.snapshot.notes.any { it.startsWith("Não dá para ver todos os 106 tópicos") })
        // As matérias de mais peso ficam com uma parte maior dos seus tópicos do que as de menos peso.
        val subjects = CatalogEditalFixtures.load("bb-2022--agente-comercial")
        fun shareOf(name: String): Double {
            val index = subjects.indexOfFirst { it.name == name }
            val ids = subjects[index].topics.indices.map { 1000L * (index + 1) + it }
            return ids.count { it in o.firstStudy } / ids.size.toDouble()
        }
        assertTrue(shareOf("Língua Portuguesa") >= shareOf("Matemática"), "peso: Português ${shareOf("Língua Portuguesa")} x Matemática ${shareOf("Matemática")}")
    }

    @Test fun comPoucoTempoCadaTopicoFicaMaisCurtoEmVezDeSumir() {
        val o = plan("bb-2022--agente-comercial", 60, 90)
        val coverage = assertNotNull(o.coverage)
        assertTrue(coverage.compressed && !coverage.impossible)
        assertEquals(o.topics, o.firstStudy.size)
        val theory = o.proposal.newTasks.filter { it.type == PlanTaskType.THEORY }.groupBy { it.topicId }.mapValues { (_, l) -> l.sumOf { it.plannedMinutes } }
        assertTrue(theory.values.all { it >= StudyPlanBlueprint.MIN_FIRST_THEORY_MINUTES }, "teoria abaixo do mínimo: ${theory.values.minOrNull()}")
        assertTrue(theory.values.average() < 50.0)
    }

    @Test fun planoLongoContinuaEstavelReorganizarSemMudancaNaoGravaNada() {
        // Com o edital inteiro marcado, reorganizar sem nada mudar (abrir o app, sincronizar) não mexe na semana à
        // vista, nunca tira tópico do plano e, em no máximo duas acomodações lá na frente, para de gravar.
        for (id in editais) {
            val input = CatalogEditalFixtures.input(CatalogEditalFixtures.load(id), today, 120, null)
            var tasks = plan(id, 120, null).proposal.newTasks
            val week = tasks.filter { it.date < today.plusDays(7) }.map { it.id }.toSet()
            var revision = 1L
            var writes = 0
            repeat(4) {
                val snap = PlannerSnapshotBuilder.build(input.copy(plan = input.plan.copy(revision = revision), tasks = tasks))
                val proposal = PlanStability.stabilize(StudyPlannerEngine().plan(snap, ReplanReason.DAY_MISSED), snap.tasks)
                if (PlanStability.isNoop(proposal)) return@repeat
                writes++
                revision++
                val moved = proposal.transitions.mapTo(hashSetOf()) { it.taskId }
                tasks = tasks.filter { it.id !in moved } + proposal.newTasks
            }
            assertTrue(writes <= 2, "$id: $writes reorganizações sem mudança gravaram algo")
            assertTrue(tasks.map { it.id }.containsAll(week), "$id: a semana à vista mudou")
            assertEquals(input.topics.size, tasks.filter { it.type == PlanTaskType.THEORY }.mapNotNull { it.topicId }.toSet().size, "$id: tópico saiu do plano")
        }
    }

    @Test fun projecaoEMapaDoEditalMostramTudo() {
        for (id in editais) {
            val input = CatalogEditalFixtures.input(CatalogEditalFixtures.load(id), today, 120, null)
            val p = PlanProjection.project(input, today.plusYears(3))
            assertEquals(input.topics.size, p.studiedOn.size, id)
            assertNotNull(p.coverageDate, id)
        }
    }
}

