package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.io.File
import kotlin.random.Random
import kotlin.test.Test

/**
 * Auditoria do plano: alunos de perfis diferentes seguem o plano dia a dia, do primeiro dia até a prova,
 * e o relatório mostra o que o plano entregou (edital coberto, divisão teoria/questões/revisão, tempo nas
 * matérias que a pessoa já sabe, atrasos). Só roda com AUDIT=1 (é lento); grava em build/plan-audit.txt.
 */
class PlanAuditSimulation {
    data class Sub(val name: String, val topics: Int, val priority: PlanPriority, val knowledge: InitialKnowledge = InitialKnowledge.NONE, val difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL)

    data class Student(
        val name: String,
        val profile: StudyProfile,
        val subjects: List<Sub>,
        val weeklyMinutes: Int,
        val examInDays: Int,
        /** Chance de estudar num dia que tem tarefa. */
        val adherence: Double = 1.0,
        /** Matérias marcadas como "já estudei" no início (todos os tópicos). */
        val markStudied: Set<String> = emptySet(),
    )

    private val trt = listOf(
        Sub("Português", 14, PlanPriority.CRITICAL), Sub("Dir. Constitucional", 12, PlanPriority.HIGH),
        Sub("Dir. Administrativo", 14, PlanPriority.HIGH), Sub("Dir. do Trabalho", 16, PlanPriority.CRITICAL),
        Sub("Proc. do Trabalho", 12, PlanPriority.HIGH), Sub("Engenharia de Software", 12, PlanPriority.CRITICAL),
        Sub("Banco de Dados", 10, PlanPriority.HIGH), Sub("Redes", 10, PlanPriority.MEDIUM),
        Sub("Segurança da Informação", 10, PlanPriority.HIGH), Sub("Governança de TI", 8, PlanPriority.MEDIUM),
        Sub("Raciocínio Lógico", 6, PlanPriority.LOW), Sub("Legislação TRT", 4, PlanPriority.LOW),
    )

    private fun students(): List<Student> {
        val known = setOf("Português", "Dir. Constitucional", "Dir. Administrativo", "Raciocínio Lógico")
        return listOf(
            Student("1. Do zero, 20h/sem, prova em 6 meses, segue tudo", StudyProfile.DO_ZERO, trt, 20 * 60, 180),
            Student("2. Do zero, 20h/sem, 6 meses, falta 1 dia em cada 4", StudyProfile.DO_ZERO, trt, 20 * 60, 180, adherence = 0.75),
            Student("3. Do zero, 10h/sem, 4 meses (não cabe)", StudyProfile.DO_ZERO, trt, 10 * 60, 120),
            Student(
                "4. Já sabe 4 matérias (declarou 'domino'), 15h/sem, 5 meses",
                StudyProfile.APROFUNDANDO,
                trt.map { if (it.name in known) it.copy(knowledge = InitialKnowledge.STRONG, difficulty = PersonalDifficulty.EASY) else it },
                15 * 60, 150,
            ),
            Student(
                "5. Já sabe 4 matérias e MARCOU como estudadas, 15h/sem, 5 meses",
                StudyProfile.APROFUNDANDO,
                trt.map { if (it.name in known) it.copy(knowledge = InitialKnowledge.STRONG, difficulty = PersonalDifficulty.EASY) else it },
                15 * 60, 150, markStudied = known,
            ),
            Student(
                "6. TI difícil (Eng. Software/BD/Redes 'muito difícil'), 20h/sem, 6 meses",
                StudyProfile.DO_ZERO,
                trt.map { if (it.name in setOf("Engenharia de Software", "Banco de Dados", "Redes")) it.copy(difficulty = PersonalDifficulty.VERY_HARD) else it },
                20 * 60, 180,
            ),
            Student("7. Reta final: prova em 5 semanas, 25h/sem, já viu tudo", StudyProfile.RETA_FINAL, trt.map { it.copy(knowledge = InitialKnowledge.SOLID) }, 25 * 60, 35, markStudied = trt.map { it.name }.toSet()),
        )
    }

    @Test
    fun audit() {
        if (System.getenv("AUDIT") != "1") return
        val out = StringBuilder()
        students().forEach { out.append(run(it)).append("\n\n") }
        File("build/plan-audit.txt").writeText(out.toString())
        println(out)
    }

    private fun run(st: Student): String {
        val rnd = Random(42)
        val zone = TimeZone.UTC
        val start = LocalDate(2026, 11, 2)
        val exam = start.plusDays(st.examInDays.toLong())
        val dayMs = 86_400_000L
        var nextId = 1L
        val subjects = st.subjects.mapIndexed { i, s ->
            PlannerSnapshotBuilder.PlanSubject(i + 1L, s.name, s.priority, false, 0, null, i, s.difficulty, s.knowledge)
        }
        val subByName = st.subjects.associateBy { it.name }
        var topics = mutableListOf<PlannerSnapshotBuilder.Topic>()
        val questionsByTopic = mutableMapOf<Long, Set<Long>>()
        var qid = 1L
        st.subjects.forEachIndexed { i, s ->
            repeat(s.topics) { p ->
                val id = 1000L * (i + 1) + p
                val marked = s.name in st.markStudied
                topics += PlannerSnapshotBuilder.Topic(id, i + 1L, null, "${s.name} ${p + 1}", p, marked, if (marked) start.minusDays(30).toEpochDays() * dayMs else null)
                questionsByTopic[id] = (0 until 40).map { qid++ }.toSet()
            }
        }
        val subjectOfTopic = topics.associate { it.id to it.subjectId }
        val subOf = subjects.associate { it.subjectId to subByName.getValue(it.name) }
        val attempts = mutableListOf<PlannerSnapshotBuilder.Attempt>()
        val usedQ = mutableMapOf<Long, Int>()
        val reviews = mutableListOf<PlannerSnapshotBuilder.PendingReview>()
        var tasks = mutableListOf<PlannerTask>()
        val executions = mutableListOf<TaskExecution>()
        val availability = (1..7).map { PlannerSnapshotBuilder.Availability(it, if (it == 7) 0 else st.weeklyMinutes / 6, it == 7) }
        val config = StudyMethodConfig.forProfile(st.profile)
        var revision = 1L
        val engine = StudyPlannerEngine()
        val studiedOn = mutableMapOf<Long, LocalDate>()

        fun replan(day: LocalDate, reason: ReplanReason) {
            val input = PlannerSnapshotBuilder.Input(
                PlannerSnapshotBuilder.Plan("p", revision, start.toEpochDays().toLong(), exam.toEpochDays().toLong(), config),
                subjects, topics.toList(), questionsByTopic, attempts.toList(), reviews.toList(), tasks.toList(), executions.toList(),
                availability, emptyList(), day, zone,
            )
            val snap = PlannerSnapshotBuilder.build(input)
            val proposal = engine.plan(snap, reason)
            revision++
            val moved = proposal.transitions.associateBy { it.taskId }
            tasks = tasks.map { t -> moved[t.id]?.let { t.copy(status = it.to) } ?: t }.toMutableList()
            tasks += proposal.newTasks
        }

        fun pCorrect(topicId: Long): Double {
            val s = subOf.getValue(subjectOfTopic.getValue(topicId))
            val studied = topics.first { it.id == topicId }.studied
            return (0.30 + 0.45 * s.knowledge.normalized + (if (studied) 0.22 else 0.0) - 0.20 * (s.difficulty.normalized - 0.5)).coerceIn(0.15, 0.95)
        }

        data class Week(var theory: Int = 0, var questions: Int = 0, var review: Int = 0, var recall: Int = 0, var other: Int = 0, var newTopics: Int = 0, var overdueTasks: Int = 0, var overdueReviews: Int = 0, var horizon: Long = 0, var emptyDays: Int = 0)
        val weeks = mutableListOf<Week>()
        val minutesBySubject = mutableMapOf<String, Int>()
        val theoryOnKnown = intArrayOf(0)
        val lastInterval = mutableMapOf<Long, Int>()
        var reviewsDone = 0
        var day = start
        replan(day, ReplanReason.INITIAL)
        while (day.isBefore(exam)) {
            val wIdx = (daysBetween(start, day) / 7).toInt()
            while (weeks.size <= wIdx) weeks += Week()
            val w = weeks[wIdx]
            val today = tasks.filter { it.date == day && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }.sortedBy { it.sequence }
            val capacityToday = availability.first { it.isoDayOfWeek == (day.dayOfWeek.ordinal + 1) }.minutes
            if (today.isEmpty() && capacityToday > 0) w.emptyDays++
            val studies = rnd.nextDouble() < st.adherence
            // As revisões da aba Revisões, antes do plano: a primeira pendente de cada tópico vencida até
            // hoje. Errando muito a pessoa marca Difícil (volta amanhã); terminada a escada D+1/D+7/D+30,
            // a seguinte cai no dobro do último intervalo.
            if (studies) {
                val due = reviews.groupBy { it.topicId }.mapNotNull { (_, l) -> l.minByOrNull { it.dueAt } }.filter { it.dueAt <= day.toEpochDays() * dayMs }
                due.forEach { r ->
                    w.review += StudyPlannerEngine.REVIEW_MINUTES
                    reviewsDone++
                    reviews.remove(r)
                    if (rnd.nextDouble() < (1 - pCorrect(r.topicId)) * 0.6) {
                        reviews += r.copy(dueAt = day.plusDays(1).toEpochDays() * dayMs)
                    } else if (reviews.none { it.topicId == r.topicId }) {
                        val next = (lastInterval[r.topicId] ?: 30) * 2
                        lastInterval[r.topicId] = next
                        reviews += PlannerSnapshotBuilder.PendingReview(nextId++, r.topicId, day.plusDays(next.toLong()).toEpochDays() * dayMs, 0)
                    }
                }
            }
            if (studies && today.isNotEmpty()) {
                // Uma de cada vez, relendo o dia depois de cada replanejamento (ele troca as tarefas por cópias novas).
                var guard = 0
                while (guard++ < 40) {
                    val task = tasks.filter { it.date == day && it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO) }.minByOrNull { it.sequence } ?: break
                    val idx = tasks.indexOfFirst { it.id == task.id }
                    tasks[idx] = task.copy(status = PlanTaskStatus.CONCLUIDA)
                    var correct = 0
                    val tid = task.topicId
                    // Revisão de matéria (sem tópico): questões dos tópicos já vistos da matéria.
                    val seenOfSubject = topics.filter { it.subjectId == task.subjectId && it.studied }.map { it.id }
                    repeat(task.plannedQuestions) {
                        val tid = tid ?: seenOfSubject.randomOrNull(rnd)
                        if (tid != null) {
                            val n = usedQ.getOrElse(tid) { 0 }
                            val q = questionsByTopic.getValue(tid).elementAt(n % 40)
                            usedQ[tid] = n + 1
                            val ok = rnd.nextDouble() < pCorrect(tid)
                            if (ok) correct++
                            attempts += PlannerSnapshotBuilder.Attempt(q, ok)
                        }
                    }
                    executions += TaskExecution("e${nextId++}", task.id, task.subjectId, tid, day, task.plannedMinutes, task.plannedQuestions, correct)
                    val subName = subjects.first { it.subjectId == task.subjectId }.name
                    minutesBySubject[subName] = minutesBySubject.getOrElse(subName) { 0 } + task.plannedMinutes
                    when (task.type) {
                        PlanTaskType.THEORY -> {
                            w.theory += task.plannedMinutes
                            if (subOf.getValue(task.subjectId!!).knowledge >= InitialKnowledge.SOLID) theoryOnKnown[0] += task.plannedMinutes
                            if (tid != null && tid !in studiedOn) {
                                studiedOn[tid] = day; w.newTopics++
                                topics = topics.map { if (it.id == tid) it.copy(studied = true, lastStudiedAt = day.toEpochDays() * dayMs) else it }.toMutableList()
                                listOf(1, 7, 30).forEach { o -> reviews += PlannerSnapshotBuilder.PendingReview(nextId++, tid, day.plusDays(o.toLong()).toEpochDays() * dayMs, 0) }
                            }
                        }
                        PlanTaskType.QUESTIONS -> w.questions += task.plannedMinutes
                        PlanTaskType.REVIEW -> {
                            w.review += task.plannedMinutes
                            reviews.filter { it.topicId == tid && it.dueAt <= (day.toEpochDays() + 1) * dayMs }.minByOrNull { it.dueAt }?.let(reviews::remove)
                        }
                        PlanTaskType.ACTIVE_RECALL -> w.recall += task.plannedMinutes
                        else -> w.other += task.plannedMinutes
                    }
                    replan(day, ReplanReason.TASK_COMPLETED)
                }
            }
            day = day.plusDays(1)
            // O que o aluno vê ao abrir o app no dia seguinte (o app não replaneja dia perdido sozinho).
            val wk = weeks[wIdx]
            wk.overdueTasks = tasks.count { it.date.isBefore(day) && it.status == PlanTaskStatus.PLANEJADA }
            wk.overdueReviews = reviews.count { it.dueAt < day.toEpochDays() * dayMs }
            wk.horizon = (tasks.filter { it.status == PlanTaskStatus.PLANEJADA }.maxOfOrNull { it.date }?.let { daysBetween(day, it) } ?: -1)
        }

        val total = topics.size
        // Visto = estudado ou dispensado pelo diagnóstico (8+ questões com 75%+ de acerto).
        val byQuestion = attempts.groupBy { a -> questionsByTopic.entries.first { a.questionId in it.value }.key }
        val leaves = topics.count { t -> t.studied || byQuestion[t.id].orEmpty().let { it.size >= 8 && it.count { a -> a.correct } * 4 >= it.size * 3 } }
        val phases = StudyMethod.phases(start, exam, st.profile)
        val contentDeadline = phases.firstOrNull { it.kind == PlanPhaseKind.RETA_FINAL }?.start ?: exam
        val coveredByDeadline = studiedOn.values.count { it.isBefore(contentDeadline) } + topics.count { it.id !in studiedOn && it.studied }
        val acc = st.subjects.mapIndexed { i, s ->
            val ids = topics.filter { it.subjectId == i + 1L }.flatMap { questionsByTopic.getValue(it.id) }.toSet()
            val a = attempts.filter { it.questionId in ids }
            "${s.name}: ${minutesBySubject[s.name] ?: 0} min, ${a.size} questões, ${if (a.isEmpty()) "-" else "${a.count { it.correct } * 100 / a.size}%"} acerto"
        }
        val tot = weeks.fold(Week()) { a, w -> a.apply { theory += w.theory; questions += w.questions; review += w.review; recall += w.recall; other += w.other } }
        val allMin = (tot.theory + tot.questions + tot.review + tot.recall + tot.other).coerceAtLeast(1)
        return buildString {
            appendLine("=== ${st.name}")
            appendLine("Fases: " + phases.joinToString(" | ") { "${it.kind.label} ${it.start}..${it.end} (${it.mix.describe()})" })
            appendLine("Edital: $leaves/$total tópicos vistos até a prova; $coveredByDeadline/$total antes da reta final ($contentDeadline).")
            appendLine("Último tópico novo em: ${studiedOn.values.maxOrNull() ?: "-"}; depois da reta final: " + studiedOn.filterValues { !it.isBefore(contentDeadline) }.map { (id, d) -> topics.first { it.id == id }.title + " " + d }.joinToString(", ") + "; nunca vistos: " + topics.filter { t -> !t.studied && t.id !in studiedOn }.joinToString(", ") { it.title })
            appendLine("Tempo feito: teoria ${tot.theory * 100 / allMin}% · questões ${tot.questions * 100 / allMin}% · revisão ${tot.review * 100 / allMin}% · reforço ${tot.recall * 100 / allMin}% · outros ${tot.other * 100 / allMin}% (total ${allMin / 60} h de ${st.weeklyMinutes * st.examInDays / 7 / 60} h disponíveis)")
            appendLine("Teoria gasta em matérias que a pessoa disse dominar: ${theoryOnKnown[0] / 60} h · revisões espaçadas feitas: $reviewsDone")
            appendLine("Semana | teoria questões revisão reforço | tópicos novos | tarefas vencidas | revisões vencidas | dias vazios | horizonte (dias)")
            weeks.forEachIndexed { i, w -> appendLine("S${(i + 1).toString().padStart(2)}    | ${w.theory / 60}h ${w.questions / 60}h ${w.review / 60}h ${w.recall / 60}h | ${w.newTopics} | ${w.overdueTasks} | ${w.overdueReviews} | ${w.emptyDays} | ${w.horizon}") }
            appendLine("Por matéria:"); acc.forEach { appendLine("  $it") }
        }
    }
}
