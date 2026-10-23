package br.com.estudario.domain

import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * O tempo de estudo de verdade, igual no site e no app: o que foi cronometrado no modo foco mais o tempo
 * das baterias de questões. Antes o dia somava também o "estudo concluído" (contado desde que a página
 * do tópico abriu) e a tarefa do plano, e a mesma hora entrava duas ou três vezes. Agora cada trecho de
 * tempo é um intervalo, e os que se sobrepõem (questões feitas com o foco ligado) contam uma vez só.
 */
object StudyTime {
    /** Um trecho de tempo estudado, em milissegundos [start, end). */
    data class Span(val start: Long, val end: Long)

    /** Uma bateria de questões não conta mais que isso por questão (aba esquecida aberta não vira hora de estudo). */
    private const val MAX_MS_PER_QUESTION = 4 * 60_000L

    /** Sessão de foco: termina em [completedAt] e durou [durationSeconds] (sem as pausas). */
    fun focus(completedAt: Long, durationSeconds: Long): Span? =
        durationSeconds.takeIf { it > 0 }?.let { Span(completedAt - it * 1000, completedAt) }

    /** Bateria de questões, do início ao fim, limitada a alguns minutos por questão respondida. */
    fun questions(startedAt: Long, completedAt: Long, questionCount: Int): Span? {
        if (questionCount <= 0 || completedAt <= startedAt) return null
        return Span(maxOf(startedAt, completedAt - questionCount * MAX_MS_PER_QUESTION), completedAt)
    }

    /** Junta os trechos que se sobrepõem. */
    fun merge(spans: List<Span>): List<Span> {
        val out = mutableListOf<Span>()
        for (s in spans.filter { it.end > it.start }.sortedBy { it.start }) {
            val last = out.lastOrNull()
            if (last != null && s.start <= last.end) out[out.size - 1] = Span(last.start, maxOf(last.end, s.end)) else out += s
        }
        return out
    }

    /** Minutos estudados em cada dia (um trecho que passa da meia-noite é dividido entre os dois dias). */
    fun minutesByDay(spans: List<Span>, zone: TimeZone): Map<LocalDate, Int> {
        val ms = HashMap<LocalDate, Long>()
        for (span in merge(spans)) {
            var s = span.start
            while (s < span.end) {
                val date = Instant.fromEpochMilliseconds(s).toLocalDateTime(zone).date
                val nextDay = date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(zone).toEpochMilliseconds()
                val e = minOf(span.end, nextDay)
                ms[date] = (ms[date] ?: 0L) + (e - s)
                s = e
            }
        }
        return ms.mapValues { (it.value / 60_000L).toInt() }
    }

    /** Minutos estudados entre [from] e [to] (milissegundos). */
    fun minutesBetween(spans: List<Span>, from: Long, to: Long): Long =
        merge(spans).sumOf { (minOf(it.end, to) - maxOf(it.start, from)).coerceAtLeast(0L) } / 60_000L
}
