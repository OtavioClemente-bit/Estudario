package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Aluno com pouco tempo (1h por dia útil, 12 matérias, prova em 6 meses) segue o plano por 4 meses.
 * Nenhuma matéria pode sumir do plano por mais de 35 dias (o rodízio garante contato), sem questão
 * antes da teoria do tópico e sem dia acima das horas.
 */
class LowTimeSubjectContactTest {
    private data class Sub(val name: String, val topics: Int, val priority: PlanPriority)

    private val trt = listOf(
        Sub("Português", 14, PlanPriority.CRITICAL), Sub("Dir. Constitucional", 12, PlanPriority.HIGH),
        Sub("Dir. Administrativo", 14, PlanPriority.HIGH), Sub("Dir. do Trabalho", 16, PlanPriority.CRITICAL),
        Sub("Proc. do Trabalho", 12, PlanPriority.HIGH), Sub("Engenharia de Software", 12, PlanPriority.CRITICAL),
        Sub("Banco de Dados", 10, PlanPriority.HIGH), Sub("Redes", 10, PlanPriority.MEDIUM),
        Sub("Segurança da Informação", 10, PlanPriority.HIGH), Sub("Governança de TI", 8, PlanPriority.MEDIUM),
        Sub("Raciocínio Lógico", 6, PlanPriority.LOW), Sub("Legislação TRT", 4, PlanPriority.LOW),
    )

    @Test
    fun lowTimeStudentKeepsEverySubjectInContact() {
        val rnd = Random(7)
        val zone = TimeZone.UTC
        val start = LocalDate(2026, 11, 2)
        val exam = start.plusDays(180)
        val simEnd = start.plusDays(120)
        val dayMs = 86_400_000L
        val subjects = trt.mapIndexed { i, s ->
            PlannerSnapshotBuilder.PlanSubject(i + 1L, s.name, s.priority, false, 0, null, i, PersonalDifficulty.NORMAL, InitialKnowledge.NONE)
        }
        var topics = mutableListOf<PlannerSnapshotBuilder.Topic>()
        val questionsByTopic = mutableMapOf<Long, Set<Long>>()
        var qid = 1L
        trt.forEachIndexed { i, s ->
            repeat(s.topics) { p ->
                val id = 1000L * (i + 1) + p
                topics += PlannerSnapshotBuilder.Topic(id, i + 1L, null, "${s.name} ${p + 1}", p, false, null)
                questionsByTopic[id] = (0 until 40).map { qid++ }.toSet()
            }
        }
        val attempts = mutableListOf<PlannerSnapshotBuilder.Attempt>()
        val usedQ = mutableMapOf<Long, Int>()
        var tasks = mutableListOf<PlannerTask>()
        val executions = mutableListOf<TaskExecution>()
        // 1h por dia útil, nada no fim de semana.
        val availability = (1..7).map { PlannerSnapshotBuilder.Availability(it, if (it <= 5) 60 else 0, it > 5) }
        val capOf = { d: LocalDate -> if (d.dayOfWeek.ordinal < 5) 60 else 0 }
        val config = StudyMethodConfig.forProfile(StudyProfile.DO_ZERO)
        var revision = 1L
        var nextId = 1L
        val engine = StudyPlannerEngine()
        val theoryDone = mutableMapOf<Long, LocalDate>()
        val lastContact = mutableMapOf<Long, LocalDate>()
        val maxGap = mutableMapOf<Long, Int>()
        var questionBeforeTheory = 0
        val examples = mutableListOf<String>()

        fun replan(day: LocalDate, reason: ReplanReason) {
            val input = PlannerSnapshotBuilder.Input(
                PlannerSnapshotBuilder.Plan("p", revision, start.toEpochDays().toLong(), exam.toEpochDays().toLong(), config),
                subjects, topics.toList(), questionsByTopic, attempts.toList(), emptyList(), tasks.toList(), executions.toList(),
                availability, emptyList(), day, zone,
            )
            val proposal = engine.plan(PlannerSnapshotBuilder.build(input), reason)
            revision++
            val moved = proposal.transitions.associateBy { it.taskId }
            tasks = tasks.map { t -> moved[t.id]?.let { t.copy(status = it.to) } ?: t }.toMutableList()
            tasks += proposal.newTasks
        }

        var day = start
        replan(day, ReplanReason.INITIAL)
        while (day.isBefore(simEnd)) {
            if (day != start) replan(day, ReplanReason.DAY_MISSED)
            var guard = 0
            while (guard++ < 40) {
                val task = tasks.filter { it.date == day && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }.minByOrNull { it.sequence } ?: break
                tasks[tasks.indexOfFirst { it.id == task.id }] = task.copy(status = PlanTaskStatus.CONCLUIDA)
                val tid = task.topicId
                if (task.type == PlanTaskType.QUESTIONS && tid != null && tid !in theoryDone) {
                    questionBeforeTheory++; examples += "$day tópico $tid"
                }
                var correct = 0
                val seen = topics.filter { (task.type == PlanTaskType.SIMULATION || it.subjectId == task.subjectId) && it.studied }.map { it.id }
                repeat(task.plannedQuestions) {
                    val t = tid ?: seen.randomOrNull(rnd)
                    if (t != null) {
                        val n = usedQ.getOrElse(t) { 0 }
                        usedQ[t] = n + 1
                        val ok = rnd.nextDouble() < 0.6
                        if (ok) correct++
                        attempts += PlannerSnapshotBuilder.Attempt(questionsByTopic.getValue(t).elementAt(n % 40), ok, day.toEpochDays() * dayMs + 3_600_000L, null, null)
                    }
                }
                executions += TaskExecution("e${nextId++}", task.id, task.subjectId, tid, day, task.plannedMinutes, task.plannedQuestions, correct)
                if (task.type == PlanTaskType.SIMULATION) subjects.forEach { lastContact[it.subjectId] = day }
                task.subjectId?.let { lastContact[it] = day }
                if (task.type == PlanTaskType.THEORY && tid != null && tid !in theoryDone) {
                    theoryDone[tid] = day
                    topics = topics.map { if (it.id == tid) it.copy(studied = true, lastStudiedAt = day.toEpochDays() * dayMs) else it }.toMutableList()
                }
                replan(day, ReplanReason.TASK_COMPLETED)
            }
            subjects.forEach { s ->
                val gap = daysBetween(lastContact[s.subjectId] ?: start, day).toInt()
                if (gap > (maxGap[s.subjectId] ?: 0)) maxGap[s.subjectId] = gap
            }
            day = day.plusDays(1)
        }

        val overDays = tasks.filter { it.status == PlanTaskStatus.PLANEJADA || it.status == PlanTaskStatus.CONCLUIDA }
            .filter { it.date < simEnd }.groupBy { it.date }.count { (d, l) -> l.sumOf { it.plannedMinutes } > capOf(d) }
        val report = subjects.joinToString("; ") { s -> "${s.name}: ${maxGap[s.subjectId] ?: 0}d, ${theoryDone.keys.count { it / 1000 == s.subjectId }}/${trt[s.subjectId.toInt() - 1].topics}" }
        println("1h/dia útil, 120 dias: vistos ${theoryDone.size}/${topics.size}; maior intervalo sem contato: $report")
        assertEquals(0, questionBeforeTheory, "questão antes da teoria: ${examples.take(3)}")
        assertEquals(0, overDays, "dias acima das horas")
        val worst = subjects.filter { (maxGap[it.subjectId] ?: 0) > 35 }
        assertTrue(worst.isEmpty(), "matérias sem contato por mais de 35 dias: $report")
    }
}
