package br.com.estudario.domain

import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.time.plusDays
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProgressInputsTest {
    private val zone = TimeZone.UTC
    private val day0 = LocalDate(2026, 11, 2)
    private fun at(day: Int, hour: Int, minute: Int = 0): Long =
        day0.atStartOfDayIn(zone).toEpochMilliseconds() + day * 86_400_000L + hour * 3_600_000L + minute * 60_000L

    @Test
    fun topicoNovoContaUmaVezEOTempoNaoDobra() {
        val s = ProgressInputs.Sources(
            attempts = listOf(ProgressInputs.Attempt(at(0, 10), true), ProgressInputs.Attempt(at(0, 10, 1), false)),
            sessions = listOf(ProgressInputs.Session(1, 7, at(0, 9), at(0, 9, 30)), ProgressInputs.Session(2, 7, at(1, 9), at(1, 9, 30))),
            focus = listOf(ProgressInputs.Focus(at(0, 9), at(0, 9, 30), 30 * 60, free = true)),
        )
        val days = ProgressInputs.dailyActivity(s, zone).associateBy { it.date }
        assertEquals(2, days.getValue(day0).questions)
        assertEquals(1, days.getValue(day0).correct)
        assertEquals(1, days.getValue(day0).studySessions)
        assertEquals(0, days.getValue(day0.plusDays(1)).studySessions)
        assertEquals(30, days.getValue(day0).freeFocusMinutes)
    }

    @Test
    fun tarefaComVariosRegistrosViraUmaSo() {
        val s = ProgressInputs.Sources(
            executions = listOf(
                ProgressInputs.Execution("t1", at(0, 8), 10, 5, 4),
                ProgressInputs.Execution("t1", at(1, 8), 20, 5, 3),
                ProgressInputs.Execution("sem-tipo", at(1, 9), 30, 0, 0),
            ),
            taskTypes = mapOf("t1" to PlanTaskType.entries.first()),
        )
        val work = ProgressInputs.planWork(s, zone)
        assertEquals(1, work.size)
        assertEquals(30, work[0].minutes)
        assertEquals(7, work[0].correct)
        assertEquals(10, work[0].questions)
        assertEquals(day0.plusDays(1), work[0].date)
    }

    /**
     * Caso real de 23/11 (teoria de Literatura): cada "Concluir" com menos tempo que o planejado deixava a
     * tarefa em andamento, a reorganização trocava o resto por uma tarefa nova (outro id) e o XP base era
     * pago de novo a cada toque. As partes remarcadas são a mesma tarefa: o XP base entra uma vez só.
     */
    @Test
    fun partesRemarcadasDaMesmaTarefaPagamOXpUmaVezSo() {
        val theory = PlanTaskType.THEORY
        val s = ProgressInputs.Sources(
            executions = listOf(
                ProgressInputs.Execution("t1", at(0, 8), 5, 0, 0),
                ProgressInputs.Execution("t2", at(0, 9), 5, 0, 0),
                ProgressInputs.Execution("t3", at(0, 10), 5, 0, 0),
            ),
            taskTypes = mapOf("t1" to theory, "t2" to theory, "t3" to theory),
            replannedFrom = mapOf("t2" to "t1", "t3" to "t2"),
        )
        val work = ProgressInputs.planWork(s, zone)
        assertEquals(1, work.size)
        assertEquals(15, work[0].minutes)
        assertEquals(ProgressEngine.planTaskXp(theory, 15, 0), work.sumOf { ProgressEngine.planTaskXp(it.type, it.minutes, it.correct) })
    }

    @Test
    fun editalContaSoOsTopicosFinaisDoPrincipal() {
        val s = ProgressInputs.Sources(
            topics = listOf(
                ProgressInputs.TopicRow(1, 10, null, studied = true), // pai: não conta
                ProgressInputs.TopicRow(2, 10, 1, studied = true),
                ProgressInputs.TopicRow(3, 10, 1, studied = false),
                ProgressInputs.TopicRow(4, 20, null, studied = true), // outro edital
            ),
            primarySubjectIds = setOf(10),
        )
        assertEquals(2 to 1, ProgressInputs.syllabus(s))
    }

    @Test
    fun xpDePlanosApagadosEExtrasEntram() {
        val s = ProgressInputs.Sources(
            attempts = listOf(ProgressInputs.Attempt(at(0, 5), true), ProgressInputs.Attempt(at(0, 23), true)),
            marks = 3, errorsFixed = 2, flashDecks = 4, retiredPlanXp = 150,
        )
        val r = ProgressInputs.evaluate(s, DailyGoal.DEFAULT, day0, zone)
        assertEquals(150, r.input.retiredPlanXp)
        assertEquals(BadgeExtras(marks = 3, errorsFixed = 2, flashDecks = 4, earlyDays = 1, lateDays = 1), r.input.extras)
        val semRetirado = ProgressInputs.evaluate(s.copy(retiredPlanXp = 0), DailyGoal.DEFAULT, day0, zone)
        assertEquals(150, r.progress.totalXp - semRetirado.progress.totalXp)
    }

    @Test
    fun mesmasLinhasDaoOMesmoResultado() {
        val s = ProgressInputs.Sources(
            attempts = (0 until 30).map { ProgressInputs.Attempt(at(it % 5, 12), it % 3 != 0) },
            reviews = listOf(ProgressInputs.Review(at(2, 15))),
        )
        assertEquals(
            ProgressInputs.evaluate(s, DailyGoal.DEFAULT, day0.plusDays(5), zone).progress,
            ProgressInputs.evaluate(s.copy(attempts = s.attempts.reversed()), DailyGoal.DEFAULT, day0.plusDays(5), zone).progress,
        )
    }

    @Test
    fun imagemDoPerfilValeAMaisNova() {
        assertEquals("avatar:chama" to 20L, ProfileImage.pick("avatar:folha" to 10L, "avatar:chama" to 20L))
        assertEquals("avatar:folha" to 10L, ProfileImage.pick("avatar:folha" to 10L, "google" to 10L))
        assertEquals("google" to 5L, ProfileImage.pick("avatar:inexistente" to 50L, "google" to 5L))
        assertNull(ProfileImage.pick(null to 0L, "photo:http://x" to 9L))
        assertEquals("cafe", ProfileImage.presetKey("avatar:cafe"))
        assertEquals("data:image/jpeg;base64,AA", ProfileImage.photoDataUrl("photo:data:image/jpeg;base64,AA"))
    }
}
