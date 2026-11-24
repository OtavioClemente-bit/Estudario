package br.com.estudario.domain.planner

import br.com.estudario.domain.SnapshotRowsMerge
import br.com.estudario.time.*
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * O plano não muda sozinho e os dois aparelhos (app e site) chegam ao mesmo plano. Cada "aparelho" aqui
 * grava as propostas do motor como o app (StudyPlanApplicationService.replan) e o site (Planning.replan):
 * PlanStability.stabilize, nada gravado sem mudança, nada gravado que esvazie o plano. A sincronização
 * junta as tarefas pelas regras da foto da conta (SnapshotRowsMerge + StaleCopies).
 */
class PlanStabilityTest {
    private val engine = StudyPlannerEngine()
    private val monday = LocalDate(2026, 11, 23)
    private val topics = (1L..12L).toList()

    /** Linha da tabela planTasks como vai na foto: o status em texto (CANCELADA não existe no enum) e o horário da edição. */
    private data class Row(val task: PlannerTask, val status: String, val at: Long)

    private class Device(var rows: List<Row>, var revision: Long, val revisions: MutableList<Long> = mutableListOf()) {
        fun copy() = Device(rows.toList(), revision, revisions.toMutableList())
        fun live() = rows.filter { it.status == "PLANEJADA" || it.status == "EM_ANDAMENTO" }
    }

    private fun snapshot(device: Device, today: LocalDate): StudyPlannerSnapshot {
        val tasks = device.rows.filter { it.status != "CANCELADA" }.map { it.task.copy(status = PlanTaskStatus.valueOf(it.status)) }
        // Como o montador do app: entra como demanda a teoria (e as questões) do tópico que ainda não está no plano.
        val covered = tasks.filter { it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO, PlanTaskStatus.CONCLUIDA) }.mapNotNullTo(hashSetOf()) { it.topicId to it.type }
        val demands = topics.flatMap { topic ->
            listOfNotNull(
                TaskDemand("theory:$topic", 10L + topic % 3, topic, PlanTaskType.THEORY, 50, priority = PlanPriority.HIGH, topicPosition = topic.toInt(), subjectPosition = (topic % 3).toInt())
                    .takeIf { (topic to PlanTaskType.THEORY) !in covered },
                TaskDemand("questions:$topic", 10L + topic % 3, topic, PlanTaskType.QUESTIONS, 20, questions = 10, priority = PlanPriority.HIGH, topicPosition = topic.toInt(), subjectPosition = (topic % 3).toInt())
                    .takeIf { (topic to PlanTaskType.QUESTIONS) !in covered },
            )
        }
        return StudyPlannerSnapshot(
            planId = "plan-1",
            revision = device.revision,
            today = today,
            availability = DayOfWeek.entries.associateWith { DailyCapacity(it, 90) },
            subjects = (10L..12L).map { SubjectDemand(it, "Matéria $it", PlanPriority.HIGH, position = (it - 10).toInt()) },
            demands = demands,
            tasks = tasks,
            remainingPhaseMinutes = demands.sumOf { it.minutes },
            policy = PlannerPolicy(horizonDays = 21, maxFlexibleSharePercent = 100),
        )
    }

    /** Grava como o app: devolve true se gravou uma revisão nova. */
    private fun replan(device: Device, reason: ReplanReason, today: LocalDate, at: Long): Boolean {
        val snap = snapshot(device, today)
        val proposal = PlanStability.stabilize(engine.plan(snap, reason), snap.tasks)
        if (PlanStability.isNoop(proposal)) return false
        if (PlanStability.wouldEmptyPlan(proposal, snap.tasks, today, snap.subjects.any { !it.paused }, reason)) return false
        val changes = proposal.transitions.associateBy { it.taskId }
        device.revision += 1
        device.revisions += device.revision
        device.rows = device.rows.map { row -> changes[row.task.id]?.let { row.copy(status = it.to.name, at = at) } ?: row } +
            proposal.newTasks.map { Row(it, it.status.name, at) }
        return true
    }

    private fun complete(device: Device, taskId: String, at: Long) {
        device.rows = device.rows.map { if (it.task.id == taskId) it.copy(status = "CONCLUIDA", at = at) else it }
    }

    /** A junção de três vias da sincronização, com as regras da foto da conta (as do app e do site). */
    private fun merge(base: Device, local: Device, remote: Device): Device {
        fun canonical(r: Row) = "${r.task}|${r.status}|${r.at}"
        val rows = SnapshotRowsMerge.rows(base.rows, local.rows, remote.rows, false, { it.task.id }, ::canonical) { l, r ->
            SnapshotRowsMerge.winner("planTasks", l, r, { it.status }, { it.at })
        }
        val stale = StaleCopies.ids(rows.map { StaleCopies.Row(it.task.id, it.task.replannedFromTaskId, it.status, "${it.task.date.toEpochDay()}|${it.task.type}") })
        return Device(rows.map { if (it.task.id in stale) it.copy(status = "CANCELADA") else it }, maxOf(local.revision, remote.revision))
    }

    private fun overdue(device: Device, today: LocalDate): List<Row> {
        val replaced = PlanRules.replaced(device.rows.map { it.task.replannedFromTaskId })
        return device.rows.filter { it.task.id !in replaced && PlanRules.isOverdue(it.task.date.toEpochDay(), it.status, today.toEpochDay()) }
    }

    private fun dayOf(device: Device, date: LocalDate) = device.live().filter { it.task.date == date }.map { it.task.id }.sorted()

    private fun assertNoDuplicates(device: Device) {
        val live = device.live().map { it.task }
        val slots = live.groupBy { Triple(it.topicId, it.type, it.date) }.filterValues { it.size > 1 }
        assertTrue(slots.isEmpty(), "tarefas duplicadas: ${slots.keys}")
        assertEquals(device.rows.size, device.rows.map { it.task.id }.distinct().size)
    }

    private fun fresh(): Device = Device(emptyList(), 0).also { assertTrue(replan(it, ReplanReason.INITIAL, monday, 1)) }

    @Test fun mesmoSnapshotMesmaDataGeraAsMesmasTarefas() {
        val device = fresh()
        val snap = snapshot(device, monday.plusDays(1))
        val a = engine.plan(snap, ReplanReason.DAY_MISSED)
        val b = engine.plan(snap, ReplanReason.DAY_MISSED)
        assertEquals(a, b)
        // A ordem das linhas na foto (cada aparelho exporta numa ordem) não muda os ids nem os dias.
        val c = engine.plan(snap.copy(tasks = snap.tasks.reversed(), demands = snap.demands.reversed()), ReplanReason.DAY_MISSED)
        assertEquals(a.newTasks.map { it.id to it.date }.toSet(), c.newTasks.map { it.id to it.date }.toSet())
        assertEquals(a.transitions.toSet(), c.transitions.toSet())
    }

    @Test fun reorganizarSemNadaMudarNaoGravaRevisaoNemIdNovo() {
        val device = fresh()
        val ids = device.rows.map { it.task.id }.toSet()
        val revision = device.revision
        // Abrir o app, voltar para ele, sincronizar sem novidade: o mesmo dia, nada mudou.
        repeat(3) { assertFalse(replan(device, ReplanReason.DAY_MISSED, monday, 10L + it)) }
        assertEquals(revision, device.revision)
        assertEquals(ids, device.rows.map { it.task.id }.toSet())
        assertTrue(device.rows.none { it.status == "REPROGRAMADA" })
    }

    @Test fun virarODiaSemAtrasoNaoRemarcaAsTarefas() {
        val device = fresh()
        // A pessoa fez tudo de segunda; na terça o plano não refaz a semana.
        dayOf(device, monday).forEach { complete(device, it, 5) }
        val before = device.live().map { it.task.id }.toSet()
        replan(device, ReplanReason.DAY_MISSED, monday.plusDays(1), 20)
        val after = device.live().map { it.task.id }.toSet()
        assertTrue(after.containsAll(before), "tarefas remarcadas: ${before - after}")
        assertTrue(device.rows.none { it.status == "REPROGRAMADA" })
        // E de novo no mesmo dia: nada.
        assertFalse(replan(device, ReplanReason.DAY_MISSED, monday.plusDays(1), 21))
    }

    @Test fun concluirUmaTarefaNaoMexeNoRestoDeHoje() {
        val device = fresh()
        val today = dayOf(device, monday)
        assertTrue(today.size >= 2)
        complete(device, today.first(), 5)
        replan(device, ReplanReason.TASK_COMPLETED, monday, 6)
        assertEquals(today.drop(1), dayOf(device, monday))
        // O replanejamento repetido pela mesma conclusão (três vezes no mesmo minuto no log real) não grava nada.
        assertFalse(replan(device, ReplanReason.TASK_COMPLETED, monday, 7))
        assertFalse(replan(device, ReplanReason.TASK_COMPLETED, monday, 8))
    }

    @Test fun planoVazioNaoEhGravadoQuandoHaEstudoMarcado() {
        val device = fresh()
        val snap = snapshot(device, monday)
        val live = snap.tasks.filter { it.status == PlanTaskStatus.PLANEJADA }
        val wipe = engine.plan(snap, ReplanReason.DAY_MISSED).copy(
            transitions = live.map { TaskTransition(it.id, it.status, PlanTaskStatus.REPROGRAMADA, "DAY_MISSED") },
            newTasks = emptyList(),
        )
        assertTrue(PlanStability.wouldEmptyPlan(wipe, snap.tasks, monday, hasActiveSubjects = true, reason = ReplanReason.DAY_MISSED))
        // O REBUILD esvazia de propósito (o DAY_MISSED seguinte remonta) e plano sem matéria ativa pode ficar vazio.
        assertFalse(PlanStability.wouldEmptyPlan(wipe, snap.tasks, monday, hasActiveSubjects = true, reason = ReplanReason.REBUILD))
        assertFalse(PlanStability.wouldEmptyPlan(wipe, snap.tasks, monday, hasActiveSubjects = false, reason = ReplanReason.DAY_MISSED))
    }

    @Test fun copiaIgualVoltaASerAOriginal() {
        val device = fresh()
        val snap = snapshot(device, monday)
        val target = snap.tasks.first { it.date == monday.plusDays(10) }
        val proposal = PlanningProposal(
            id = "p", planId = "plan-1", baseRevision = device.revision, preservedTaskIds = emptySet(),
            transitions = listOf(TaskTransition(target.id, PlanTaskStatus.PLANEJADA, PlanTaskStatus.REPROGRAMADA, "DAY_MISSED")),
            newTasks = listOf(target.copy(id = "copia", replannedFromTaskId = target.id, sequence = 99)),
            capacity = CapacityReport(0, 0, 0, emptyMap(), emptyList()), explanations = emptyList(),
            forecast = ForecastResult(null, 0, 0),
        )
        val stable = PlanStability.stabilize(proposal, snap.tasks)
        assertTrue(PlanStability.isNoop(stable))
        assertTrue(target.id in stable.preservedTaskIds)
        // Mudou de dia de verdade: continua como cópia.
        val moved = proposal.copy(newTasks = listOf(target.copy(id = "copia", date = monday.plusDays(11), replannedFromTaskId = target.id)))
        assertEquals(moved, PlanStability.stabilize(moved, snap.tasks))
    }

    @Test fun doisAparelhosConcluirNoSiteNaoVoltaComoAtrasadaNoCelular() {
        val base = fresh()
        val site = base.copy()
        val phone = base.copy()
        val monTasks = dayOf(base, monday)

        // Segunda: a pessoa faz tudo no site, que reorganiza depois de cada conclusão.
        monTasks.forEachIndexed { i, id -> complete(site, id, 100L + i); replan(site, ReplanReason.TASK_COMPLETED, monday, 100L + i) }
        // O celular abre na segunda, ainda sem a sincronização: nada muda nele (nenhuma revisão nova).
        assertFalse(replan(phone, ReplanReason.DAY_MISSED, monday, 150))
        assertEquals(monTasks, dayOf(phone, monday))

        // Terça: o celular abriu antes de a sincronização terminar e reorganizou com os dados velhos.
        val tuesday = monday.plusDays(1)
        replan(phone, ReplanReason.DAY_MISSED, tuesday, 200)
        // O site também abriu na terça (já sem atraso): nada a reorganizar.
        assertFalse(replan(site, ReplanReason.DAY_MISSED, tuesday, 210))

        val merged = merge(base, phone, site)
        // O que foi concluído no site continua concluído, e nada de segunda aparece como atrasado.
        monTasks.forEach { id -> assertEquals("CONCLUIDA", merged.rows.single { it.task.id == id }.status) }
        assertTrue(overdue(merged, tuesday).isEmpty(), "atrasadas: ${overdue(merged, tuesday).map { it.task.id }}")
        assertNoDuplicates(merged)
        // A teoria feita no site não volta como tarefa nova (a cópia velha do celular sai).
        val doneTopics = merged.rows.filter { it.status == "CONCLUIDA" && it.task.type == PlanTaskType.THEORY }.map { it.task.topicId }.toSet()
        assertTrue(merged.live().none { it.task.type == PlanTaskType.THEORY && it.task.topicId in doneTopics })
        // A terça do site é a terça da junção.
        assertEquals(dayOf(site, tuesday), dayOf(merged, tuesday))
        // Abrir depois da junção: no máximo uma reorganização (o buraco que a cópia velha cancelada deixou lá na
        // frente é preenchido), sem tocar na semana de hoje; depois disso, nada muda mais.
        val again = merged.copy()
        val week = (0..6).map { tuesday.plusDays(it) }
        val weekBefore = week.map { dayOf(again, it) }
        replan(again, ReplanReason.DAY_MISSED, tuesday, 300)
        assertEquals(weekBefore, week.map { dayOf(again, it) })
        assertNoDuplicates(again)
        assertFalse(replan(again, ReplanReason.DAY_MISSED, tuesday, 301))
        assertFalse(replan(again, ReplanReason.DAY_MISSED, tuesday, 302))
    }

    @Test fun doisAparelhosQueReorganizamIgualChegamAosMesmosIds() {
        val base = fresh()
        val a = base.copy()
        val b = base.copy()
        // Os dois viram o dia sem ver o outro, partindo do mesmo plano (com segunda por fazer).
        val tuesday = monday.plusDays(1)
        replan(a, ReplanReason.DAY_MISSED, tuesday, 50)
        replan(b, ReplanReason.DAY_MISSED, tuesday, 60)
        assertEquals(a.rows.map { it.task.id to it.status }.toSet(), b.rows.map { it.task.id to it.status }.toSet())
        val merged = merge(base, a, b)
        assertNoDuplicates(merged)
        assertEquals(a.live().map { it.task.id }.toSet(), merged.live().map { it.task.id }.toSet())
    }
}
