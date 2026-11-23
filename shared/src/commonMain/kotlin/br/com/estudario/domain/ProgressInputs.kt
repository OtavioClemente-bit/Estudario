package br.com.estudario.domain

import br.com.estudario.domain.planner.PlanTaskType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * A montagem única dos dados do perfil (sequência, XP, nível e emblemas), usada pelo app e pelo site.
 * Antes cada lado montava a sua: o site contava os tópicos de todos os editais e o app só os do
 * principal (com os tópicos-pai juntos), e o site não somava o XP de planos apagados. Com o mesmo
 * histórico na foto da conta, o nível e os emblemas tinham de sair iguais e não saíam.
 *
 * Cada lado só converte as suas linhas para os tipos simples abaixo e chama [evaluate].
 */
object ProgressInputs {
    data class Attempt(val answeredAt: Long, val correct: Boolean)
    data class Review(val reviewedAt: Long)
    data class Session(val id: Long, val topicId: Long, val startedAt: Long, val completedAt: Long)
    data class Focus(val startedAt: Long, val completedAt: Long, val durationSeconds: Long, val free: Boolean)
    data class Execution(val taskId: String?, val completedAt: Long, val minutes: Int, val questions: Int, val correct: Int)
    data class QuestionRun(val startedAt: Long, val completedAt: Long, val questionCount: Int)
    data class TopicRow(val id: Long, val subjectId: Long, val parentTopicId: Long?, val studied: Boolean)

    data class Sources(
        val attempts: List<Attempt> = emptyList(),
        val reviews: List<Review> = emptyList(),
        val sessions: List<Session> = emptyList(),
        val focus: List<Focus> = emptyList(),
        val executions: List<Execution> = emptyList(),
        val questionRuns: List<QuestionRun> = emptyList(),
        /** Tipo de cada tarefa do plano, pelo id (tarefa sem tipo conhecido não rende XP de plano). */
        val taskTypes: Map<String, PlanTaskType> = emptyMap(),
        /**
         * De qual tarefa cada uma nasceu ao reorganizar (replannedFrom): a parte que sobra de uma tarefa
         * em andamento vira outra tarefa, com outro id, e continua sendo a mesma tarefa para o XP.
         */
        val replannedFrom: Map<String, String> = emptyMap(),
        val topics: List<TopicRow> = emptyList(),
        /** Matérias do edital principal: o emblema de edital conta só os tópicos dele. */
        val primarySubjectIds: Set<Long> = emptySet(),
        /** Grifos + anotações. */
        val marks: Int = 0,
        /** Questões do caderno de erros acertadas depois. */
        val errorsFixed: Int = 0,
        /** Baralhos terminados ("flashDecks" da conta). */
        val flashDecks: Int = 0,
        /** XP de planos apagados ("retiredPlanXp" da conta). */
        val retiredPlanXp: Int = 0,
    )

    data class Result(
        val days: List<DailyActivity>,
        val streak: StreakSummary,
        val input: ProgressEngine.ProgressInput,
        val progress: ProgressEngine.ProgressSummary,
    )

    /** O dia a dia: questões, revisões, tópico novo (uma vez por tópico), foco, tarefas e tempo real. */
    fun dailyActivity(s: Sources, zone: TimeZone): List<DailyActivity> = DailyActivityBuilder.build(
        answers = s.attempts.map { DailyActivityBuilder.Answer(it.answeredAt, it.correct) },
        reviewedAt = s.reviews.map { it.reviewedAt },
        sessions = s.sessions.map { DailyActivityBuilder.Session(it.id, it.topicId, it.completedAt) },
        focusSessions = s.focus.map { DailyActivityBuilder.Focus(it.completedAt, it.durationSeconds, it.free) },
        executionsCompletedAt = s.executions.map { it.completedAt },
        questionRuns = s.questionRuns.map { DailyActivityBuilder.QuestionRun(it.startedAt, it.completedAt, it.questionCount) },
        zone = zone,
    )

    /**
     * Uma tarefa é uma só, com o tempo e os acertos de todos os registros somados, no dia do último.
     * Conta também as partes remarcadas dela: salvar menos tempo que o planejado deixa a tarefa em
     * andamento e a reorganização troca o resto por uma tarefa nova (outro id). Agrupando só pelo id,
     * cada toque em "Concluir" pagava o XP base de novo e a tarefa nunca saía do dia (caso real de 23/11).
     */
    fun planWork(s: Sources, zone: TimeZone): List<ProgressEngine.PlanWork> =
        s.executions.filter { it.taskId != null }.groupBy { rootTask(it.taskId!!, s.replannedFrom) }.mapNotNull { (taskId, runs) ->
            val type = s.taskTypes[taskId] ?: runs.firstNotNullOfOrNull { s.taskTypes[it.taskId] } ?: return@mapNotNull null
            ProgressEngine.PlanWork(
                date = br.com.estudario.time.epochMillisToDate(runs.maxOf { it.completedAt }, zone),
                type = type,
                minutes = runs.sumOf { it.minutes },
                correct = runs.sumOf { it.correct },
                questions = runs.sumOf { it.questions },
            )
        }

    /** A primeira tarefa da cadeia de remarcações (com trava contra ciclo vindo de dados ruins). */
    private fun rootTask(taskId: String, replannedFrom: Map<String, String>): String {
        var current = taskId
        val seen = hashSetOf(current)
        while (true) {
            val parent = replannedFrom[current] ?: return current
            if (!seen.add(parent)) return current
            current = parent
        }
    }

    /** (total, estudados): os tópicos finais (sem subtópicos) do edital principal. */
    fun syllabus(s: Sources): Pair<Int, Int> {
        val scoped = s.topics.filter { it.subjectId in s.primarySubjectIds }
        val parents = scoped.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val leaves = scoped.filter { it.id !in parents }
        return leaves.size to leaves.count { it.studied }
    }

    fun extras(s: Sources, streak: StreakSummary, zone: TimeZone): BadgeExtras {
        val (early, late) = BadgeExtras.hourDays(
            s.attempts.map { it.answeredAt } + s.focus.map { it.startedAt } + s.sessions.map { it.startedAt } + s.reviews.map { it.reviewedAt },
            zone,
        )
        return BadgeExtras(
            focusSessions = s.focus.size,
            marks = s.marks.coerceAtLeast(0),
            errorsFixed = s.errorsFixed.coerceAtLeast(0),
            streakSaves = streak.protectedDays.size,
            earlyDays = early,
            lateDays = late,
            flashDecks = s.flashDecks.coerceAtLeast(0),
        )
    }

    fun input(s: Sources, days: List<DailyActivity>, streak: StreakSummary, goal: DailyGoal, today: LocalDate, zone: TimeZone): ProgressEngine.ProgressInput {
        val (topicsTotal, topicsStudied) = syllabus(s)
        return ProgressEngine.ProgressInput(
            today = today,
            days = days,
            goal = goal,
            planWork = planWork(s, zone),
            bestStreak = streak.best,
            currentStreak = streak.current,
            totalQuestions = s.attempts.size,
            correctQuestions = s.attempts.count { it.correct },
            reviewsCompleted = s.reviews.size,
            topicsStudied = topicsStudied,
            topicsTotal = topicsTotal,
            retiredPlanXp = s.retiredPlanXp.coerceAtLeast(0),
            extras = extras(s, streak, zone),
        )
    }

    /** Tudo de uma vez: os dias, a sequência, a entrada do ProgressEngine e o resultado. */
    fun evaluate(s: Sources, goal: DailyGoal, today: LocalDate, zone: TimeZone): Result {
        val days = dailyActivity(s, zone)
        val streak = StreakEngine.summarize(days, goal, today)
        val input = input(s, days, streak, goal, today, zone)
        return Result(days, streak, input, ProgressEngine.evaluate(input))
    }
}
