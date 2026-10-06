package br.com.estudario.time

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Duration.Companion.seconds

/**
 * Operações de data com os mesmos nomes do java.time, para o código comum (Android e web)
 * continuar legível depois da troca para kotlinx-datetime.
 */
val MAX_DATE: LocalDate = LocalDate(9999, 12, 31)

fun today(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = Clock.System.todayIn(zone)

fun LocalDate.plusDays(days: Long): LocalDate = plus(days, DateTimeUnit.DAY)
fun LocalDate.plusDays(days: Int): LocalDate = plus(days, DateTimeUnit.DAY)
fun LocalDate.minusDays(days: Long): LocalDate = minus(days, DateTimeUnit.DAY)
fun LocalDate.minusDays(days: Int): LocalDate = minus(days, DateTimeUnit.DAY)
fun LocalDate.plusWeeks(weeks: Long): LocalDate = plus(weeks, DateTimeUnit.WEEK)
fun LocalDate.minusWeeks(weeks: Long): LocalDate = minus(weeks, DateTimeUnit.WEEK)
fun LocalDate.plusMonths(months: Long): LocalDate = plus(months, DateTimeUnit.MONTH)

fun LocalDate.isAfter(other: LocalDate): Boolean = this > other
fun LocalDate.isBefore(other: LocalDate): Boolean = this < other
fun LocalDate.toEpochDay(): Long = toEpochDays().toLong()

/** Equivale a ChronoUnit.DAYS.between(start, end). */
fun daysBetween(start: LocalDate, end: LocalDate): Long = start.daysUntil(end).toLong()

/** Equivale a ChronoUnit.WEEKS.between(start, end). */
fun weeksBetween(start: LocalDate, end: LocalDate): Long = daysBetween(start, end) / 7

/** Dia da semana ISO: segunda = 1 ... domingo = 7. */
val LocalDate.isoDayOfWeek: Int get() = dayOfWeek.ordinal + 1

/** dd/MM */
fun LocalDate.formatDayMonth(): String =
    "${day.toString().padStart(2, '0')}/${month.ordinal.plus(1).toString().padStart(2, '0')}"

fun LocalDate.startOfDay(zone: TimeZone): Instant = atStartOfDayIn(zone)
fun Instant.toLocalDate(zone: TimeZone): LocalDate = toLocalDateTime(zone).date
fun epochMillisToDate(epochMillis: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    Instant.fromEpochMilliseconds(epochMillis).toLocalDate(zone)

fun LocalDate.toYearMonth(): YearMonth = yearMonth
fun YearMonth.plusMonths(months: Long): YearMonth = plus(months, DateTimeUnit.MONTH)
fun LocalDate.plusMonths(months: Int): LocalDate = plus(months, DateTimeUnit.MONTH)
fun LocalDate.plusYears(years: Int): LocalDate = plus(years, DateTimeUnit.YEAR)

/** Número do mês (1 a 12), como java.time.LocalDate.monthValue. */
val LocalDate.monthValue: Int get() = month.ordinal + 1
fun YearMonth.isAfter(other: YearMonth): Boolean = this > other
fun Instant.plusSeconds(seconds: Long): Instant = this + seconds.seconds
