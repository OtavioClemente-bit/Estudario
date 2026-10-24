package br.com.estudario.domain

import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

/**
 * O que o histórico de estudo diz, em forma de ação: o ponto fraco da semana (vira missão), o resumo
 * da semana comparado com a anterior e o horário em que a pessoa costuma estudar (para o lembrete
 * chegar na hora certa). Mesmas contas no app e no site.
 */
object StudyInsights {
    private const val DAY_MS = 86_400_000L

    /** Uma questão respondida: quando, se acertou e a matéria. */
    data class Answer(val at: Long, val correct: Boolean, val subjectId: Long?, val subject: String?)

    /** A matéria que mais derrubou a pessoa nos últimos dias. */
    data class WeakSpot(val subjectId: Long, val subject: String, val wrong: Int, val total: Int) {
        val accuracy: Int get() = if (total == 0) 0 else (total - wrong) * 100 / total
    }

    /**
     * O ponto fraco dos últimos [days] dias: a matéria com pelo menos 5 questões, 3 erros e menos de
     * 70% de acerto; entre elas, a de pior acerto (no empate, a que tem mais erros). Nulo se nada
     * preocupa: aí a missão não aparece.
     */
    fun weakSpot(answers: List<Answer>, now: Long, days: Int = 7): WeakSpot? =
        answers.asSequence()
            .filter { it.at >= now - days * DAY_MS && it.subjectId != null }
            .groupBy { it.subjectId!! }
            .map { (id, list) -> WeakSpot(id, list.firstNotNullOfOrNull { it.subject } ?: "", list.count { !it.correct }, list.size) }
            .filter { it.total >= 5 && it.wrong >= 3 && it.accuracy < 70 && it.subject.isNotBlank() }
            .sortedWith(compareBy<WeakSpot> { it.accuracy }.thenByDescending { it.wrong })
            .firstOrNull()

    /**
     * A missão do ponto fraco, depois de feita: ao tocar em "Treinar", o app guarda a matéria, o acerto
     * daquele momento e a hora; quando a pessoa responde pelo menos [MISSION_MIN] questões dela depois
     * disso, a missão conta como cumprida e o card mostra o antes e o depois.
     */
    data class Mission(val subjectId: Long, val subject: String, val before: Int, val startedAt: Long) {
        fun encode(): String = "$subjectId|$before|$startedAt|$subject"
        companion object {
            fun decode(raw: String?): Mission? {
                val p = raw?.split("|", limit = 4) ?: return null
                if (p.size < 4) return null
                return Mission(p[0].toLongOrNull() ?: return null, p[3], p[1].toIntOrNull() ?: return null, p[2].toLongOrNull() ?: return null)
            }
        }
    }
    data class MissionResult(val mission: Mission, val after: Int, val answered: Int) {
        val delta: Int get() = after - mission.before
    }
    const val MISSION_MIN = 5

    /** O acerto da matéria nos últimos [days] dias (nulo se ainda não tem questão nela). */
    fun subjectAccuracy(answers: List<Answer>, subjectId: Long, now: Long, days: Int = 14): Int? {
        val list = answers.filter { it.subjectId == subjectId && it.at >= now - days * DAY_MS }
        return if (list.isEmpty()) null else list.count { it.correct } * 100 / list.size
    }

    /** A missão guardada já foi cumprida? Devolve o antes e o depois, ou nulo se ainda falta. */
    fun missionResult(mission: Mission?, answers: List<Answer>, now: Long): MissionResult? {
        mission ?: return null
        val answered = answers.count { it.subjectId == mission.subjectId && it.at >= mission.startedAt }
        if (answered < MISSION_MIN) return null
        return MissionResult(mission, subjectAccuracy(answers, mission.subjectId, now) ?: mission.before, answered)
    }

    /** A semana (últimos 7 dias, hoje incluído) comparada com os 7 dias anteriores. */
    data class Week(
        val from: LocalDate, val to: LocalDate,
        val days: Int, val minutes: Int, val questions: Int, val accuracy: Int?,
        val prevDays: Int, val prevMinutes: Int, val prevQuestions: Int, val prevAccuracy: Int?,
        /** Melhor e pior matéria da semana (nome e acerto), com pelo menos 5 questões cada. */
        val best: Pair<String, Int>?, val worst: Pair<String, Int>?,
    ) {
        val accuracyDelta: Int? get() = if (accuracy != null && prevAccuracy != null) accuracy - prevAccuracy else null
    }

    fun week(answers: List<Answer>, calendar: List<DailyActivity>, today: LocalDate, startOfToday: Long): Week {
        val from = today.minusDays(6); val prevFrom = today.minusDays(13)
        val weekStart = startOfToday - 6 * DAY_MS; val prevStart = startOfToday - 13 * DAY_MS
        fun inDays(d: LocalDate, a: LocalDate, b: LocalDate) = d.toEpochDay() in a.toEpochDay()..b.toEpochDay()
        val cur = calendar.filter { inDays(it.date, from, today) }
        val prev = calendar.filter { inDays(it.date, prevFrom, from.minusDays(1)) }
        val wa = answers.filter { it.at >= weekStart }
        val pa = answers.filter { it.at in prevStart until weekStart }
        fun acc(l: List<Answer>) = if (l.size < 5) null else l.count { it.correct } * 100 / l.size
        val bySubject = wa.filter { it.subject != null }.groupBy { it.subject!! }.filterValues { it.size >= 5 }.mapValues { (_, l) -> l.count { it.correct } * 100 / l.size }
        val best = bySubject.maxByOrNull { it.value }?.toPair()
        val worst = bySubject.minByOrNull { it.value }?.toPair()?.takeIf { bySubject.size >= 2 && it.first != best?.first }
        fun minutes(l: List<DailyActivity>) = l.sumOf { maxOf(it.minutes, it.focusMinutes) }
        return Week(
            from, today,
            cur.count { it.hasAnything }, minutes(cur), wa.size, acc(wa),
            prev.count { it.hasAnything }, minutes(prev), pa.size, acc(pa),
            best, worst,
        )
    }

    /** O comentário do Folha sobre a semana (curto, de amigo, sem sermão). */
    fun weekLine(w: Week): String = when {
        w.days == 0 -> "Semana parada, tudo bem. Um tópico hoje já recomeça o ritmo."
        w.days >= 6 -> "Você estudou em ${w.days} dias. Isso é constância de aprovado!"
        (w.accuracyDelta ?: 0) >= 5 -> "Seu acerto subiu ${w.accuracyDelta} pontos. Tá dando resultado!"
        (w.accuracyDelta ?: 0) <= -5 -> "O acerto caiu um pouco. Vale revisar os erros antes de avançar."
        w.days > w.prevDays -> "Mais dias de estudo que na semana passada. Bora manter!"
        w.worst != null -> "${w.worst.first} pede uma atenção extra na próxima semana."
        else -> "Semana firme. Bora fazer a próxima ainda melhor?"
    }

    /**
     * O horário em que a pessoa costuma começar a estudar (minuto do dia), a partir do primeiro estudo
     * de cada dia recente. Precisa de pelo menos 4 dias; usa a mediana, para um dia fora da curva não
     * puxar o horário.
     */
    fun usualStart(firstMinutes: List<Int>): Int? {
        if (firstMinutes.size < 4) return null
        val sorted = firstMinutes.sorted()
        return sorted[sorted.size / 2]
    }

    /**
     * O lembrete chega um pouco antes (15 min) do horário de costume, entre 5h e 22h, e nunca depois
     * dele. Quem costuma começar antes das 5h (madrugada) fica com o horário fixo: null. Antes o piso
     * era 7h, e quem começava às 5h recebia o aviso às 7h, já estudando.
     */
    fun reminderMinute(usualStart: Int): Int? =
        if (usualStart < 5 * 60) null else minOf((usualStart - 15).coerceIn(5 * 60, 22 * 60), usualStart)
}
