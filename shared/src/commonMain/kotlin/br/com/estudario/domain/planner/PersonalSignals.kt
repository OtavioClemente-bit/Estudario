package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

/**
 * O ritmo de verdade da pessoa, medido no que ela já fez no plano: quanto tempo a teoria leva de fato
 * e quantos minutos ela gasta por questão. O plano usa isso para dimensionar as próximas tarefas, e o
 * dia fica do tamanho que a pessoa consegue cumprir (nem estoura, nem sobra).
 */
data class PersonalPace(
    /** Teoria real sobre a teoria padrão (1.0 = no padrão; 1.3 = leva 30% a mais). */
    val theoryFactor: Double = 1.0,
    /** Tópicos usados na medida da teoria. */
    val theorySamples: Int = 0,
    /** Minutos por questão medidos; null enquanto a amostra é pequena (vale o padrão). */
    val minutesPerQuestion: Int? = null,
    val questionSamples: Int = 0,
) {
    val measured: Boolean get() = theorySamples >= MIN_THEORY_TOPICS || minutesPerQuestion != null

    companion object {
        const val MIN_THEORY_TOPICS = 3
        const val MIN_QUESTIONS = 40
        private const val RECENT_TOPICS = 8
        private const val RECENT_QUESTION_TASKS = 15
        private const val MIN_FACTOR = 0.7
        private const val MAX_FACTOR = 1.6

        /**
         * [baseTheoryMinutes] diz a teoria padrão de um tópico (sem o ritmo pessoal). A medida compara o
         * tempo real com esse padrão, não com o que estava planejado: o planejado já vem corrigido pelo
         * ritmo, e comparar com ele faria a correção somar em cima dela mesma a cada semana.
         */
        fun measure(
            tasks: List<PlannerTask>,
            executions: List<TaskExecution>,
            baseTheoryMinutes: (topicId: Long) -> Int?,
        ): PersonalPace {
            // Só conta o que foi medido de verdade. Fica de fora o registro que é só o tempo planejado
            // repetido (a tela de conclusão já vem com ele preenchido; contar isso faria o ritmo confirmar
            // a si mesmo) e o tempo absurdo (a teoria marcada como feita em 2 min, depois de estudar fora).
            val taskById = tasks.associateBy { it.id }
            val measured = executions.filter { e ->
                val task = e.taskId?.let(taskById::get) ?: return@filter false
                e.minutes > 0 && e.minutes != task.plannedMinutes
            }
            val minutesByTask = measured.groupBy { it.taskId!! }.mapValues { (_, l) -> l.sumOf { it.minutes } }
            // Concluída ou feita em parte (registrar menos que o planejado deixa a tarefa "em andamento"):
            // sem a feita em parte, só as passadas lentas entravam e o ritmo só subia.
            val worked = tasks.filter { (it.status == PlanTaskStatus.CONCLUIDA || it.status == PlanTaskStatus.EM_ANDAMENTO) && it.id in minutesByTask }

            // Teoria: só a primeira passada de cada tópico (a retomada é curta e distorceria a conta);
            // teoria partida em dois dias seguidos conta inteira.
            val theoryByTopic = worked.filter { it.type == PlanTaskType.THEORY && it.topicId != null }.groupBy { it.topicId!! }
            val samples = theoryByTopic.mapNotNull { (topicId, list) ->
                val first = list.minOf { it.date }
                val pass = list.filter { daysBetween(first, it.date) <= 2 }
                val base = baseTheoryMinutes(topicId)?.takeIf { it > 0 } ?: return@mapNotNull null
                val real = pass.sumOf { minutesByTask.getValue(it.id) }
                if (real * 10 < base * 3 || real > base * 3) return@mapNotNull null
                Triple(first, real, base)
            }.sortedByDescending { it.first }.take(RECENT_TOPICS)
            val factor = if (samples.size < MIN_THEORY_TOPICS) 1.0 else {
                val ratio = (samples.sumOf { it.second }.toDouble() / samples.sumOf { it.third }).coerceIn(MIN_FACTOR, MAX_FACTOR)
                // Com poucos tópicos, a medida puxa só parte do caminho (um dia ruim não muda o plano todo).
                val trust = (samples.size / 4.0).coerceAtMost(1.0)
                1.0 + (ratio - 1.0) * trust
            }

            val questionTasks = worked.filter { it.type == PlanTaskType.QUESTIONS }
                .mapNotNull { task ->
                    val q = measured.filter { it.taskId == task.id }.sumOf { it.questions }.takeIf { it > 0 } ?: return@mapNotNull null
                    val m = minutesByTask.getValue(task.id)
                    // Entre 20 s e 8 min por questão; fora disso não é tempo de questão.
                    if (m * 3 < q || m > q * 8) null else Triple(task.date, m, q)
                }
                .sortedByDescending { it.first }.take(RECENT_QUESTION_TASKS)
            val answered = questionTasks.sumOf { it.third }
            val perQuestion = if (answered < MIN_QUESTIONS) null
                else kotlin.math.round(questionTasks.sumOf { it.second }.toDouble() / answered).toInt().coerceIn(1, 5)
            return PersonalPace(factor, samples.size, perQuestion, answered)
        }
    }
}

/**
 * O que o último simulado mostrou, matéria por matéria. Nas semanas seguintes, as matérias em que a
 * pessoa foi mal ganham mais treino e as questões que ela errou no simulado voltam primeiro.
 */
data class SimuladoFocus(
    val date: LocalDate,
    /** Por matéria: respondidas e certas no simulado. */
    val bySubject: Map<Long, Pair<Int, Int>>,
    val weakSubjects: Set<Long>,
    /** Tópicos com questão errada no simulado. */
    val missedTopics: Set<Long>,
) {
    /** Até quando o reforço do simulado vale. */
    val until: LocalDate get() = date.plusDays(DAYS.toLong())

    fun percent(subjectId: Long): Int? = bySubject[subjectId]?.let { (a, c) -> if (a == 0) null else c * 100 / a }

    companion object {
        const val DAYS = 21
        const val MIN_PER_SUBJECT = 3
        const val WEAK_BELOW_PERCENT = 60
        /** Quanto a matéria fraca no simulado pesa a mais no rodízio e no treino. */
        const val WEIGHT_BOOST = 1.5

        fun from(
            attempts: List<PlannerSnapshotBuilder.Attempt>,
            questionsByTopic: Map<Long, Set<Long>>,
            subjectByTopic: Map<Long, Long>,
            today: LocalDate,
            zone: TimeZone,
        ): SimuladoFocus? {
            val sessions = attempts.filter { it.sessionId?.startsWith("simulado-") == true && it.answeredAt != null }.groupBy { it.sessionId!! }
            val last = sessions.values.maxByOrNull { l -> l.maxOf { it.answeredAt!! } } ?: return null
            val date = epochMillisToDate(last.maxOf { it.answeredAt!! }, zone)
            if (date.isAfter(today) || daysBetween(date, today) > DAYS) return null
            val topicOfQuestion = HashMap<Long, Long>()
            questionsByTopic.forEach { (topic, ids) -> if (topic in subjectByTopic) ids.forEach { topicOfQuestion[it] = topic } }
            // A questão do simulado é uma cópia (outro id, fora do banco do tópico): o tópico vem na resposta.
            val counted = last.mapNotNull { a -> (a.topicId?.takeIf { it in subjectByTopic } ?: topicOfQuestion[a.questionId])?.let { it to a.correct } }
            if (counted.isEmpty()) return null
            val bySubject = counted.groupBy { subjectByTopic.getValue(it.first) }
                .mapValues { (_, l) -> l.size to l.count { it.second } }
            val weak = bySubject.filter { (_, v) -> v.first >= MIN_PER_SUBJECT && v.second * 100 < v.first * WEAK_BELOW_PERCENT }.keys
            return SimuladoFocus(date, bySubject, weak, counted.filter { !it.second }.mapTo(hashSetOf()) { it.first })
        }
    }
}
