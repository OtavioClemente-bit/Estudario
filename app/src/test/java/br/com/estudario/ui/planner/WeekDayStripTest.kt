package br.com.estudario.ui.planner

import br.com.estudario.domain.planner.PlanTaskStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class WeekDayStripTest {
    @Test fun posicaoNaSemana() {
        val monday = LocalDate.of(2026, 11, 23)
        assertEquals(0, weekDayIndex(monday, monday))
        assertEquals(6, weekDayIndex(monday, monday.plusDays(6)))
        assertNull(weekDayIndex(monday, monday.plusDays(7)))
        assertNull(weekDayIndex(monday, monday.minusDays(1)))
    }

    @Test fun marcaDoDia() {
        assertEquals(WeekDayMark.NONE, dayMark(emptyList()))
        assertEquals(WeekDayMark.DONE, dayMark(listOf(PlanTaskStatus.CONCLUIDA, PlanTaskStatus.CONCLUIDA)))
        assertEquals(WeekDayMark.PENDING, dayMark(listOf(PlanTaskStatus.CONCLUIDA, PlanTaskStatus.PLANEJADA)))
    }
}
