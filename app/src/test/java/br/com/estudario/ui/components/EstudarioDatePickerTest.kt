package br.com.estudario.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class EstudarioDatePickerTest {
    @Test fun gradeTem42CasasComecandoNaSegunda() {
        // Novembro de 2026 começa num domingo: seis casas vazias antes do dia 1.
        val grid = monthGrid(YearMonth.of(2026, 11))
        assertEquals(42, grid.size)
        assertTrue(grid.take(6).all { it == null })
        assertEquals(LocalDate.of(2026, 11, 1), grid[6])
        assertEquals(LocalDate.of(2026, 11, 30), grid[35])
        assertEquals(30, grid.count { it != null })
    }

    @Test fun mesQueComecaNaSegundaNaoTemCasaVazia() {
        val grid = monthGrid(YearMonth.of(2026, 6))
        assertEquals(LocalDate.of(2026, 6, 1), grid[0])
    }

    @Test fun fevereiroBissexto() {
        assertEquals(29, monthGrid(YearMonth.of(2028, 2)).count { it != null })
    }

    @Test fun atalhosDaProva() {
        val today = LocalDate.of(2026, 11, 30)
        assertEquals(LocalDate.of(2027, 2, 28), quickDate(today, 3))
        assertEquals(LocalDate.of(2027, 5, 30), quickDate(today, 6))
        assertEquals(LocalDate.of(2027, 1, 10), quickDate(today, 6, maxDate = LocalDate.of(2027, 1, 10)))
    }

    @Test fun limitesDeEscolha() {
        val min = LocalDate.of(2026, 11, 24)
        assertFalse(isSelectable(min.minusDays(1), min, null))
        assertTrue(isSelectable(min, min, null))
        assertTrue(isSelectable(min.plusYears(3), min, null))
        assertFalse(isSelectable(min.plusDays(2), null, min.plusDays(1)))
    }

    @Test fun periodoPorDoisToques() {
        val a = LocalDate.of(2026, 12, 10); val b = LocalDate.of(2026, 12, 20)
        assertEquals(a to null, nextRange(null, null, a))
        assertEquals(a to b, nextRange(a, null, b))
        assertEquals(b to null, nextRange(a, b, b))
        val (s, e) = nextRange(b, null, a)
        assertEquals(a, s); assertNull(e)
    }

    @Test fun faixaDeMesesETitulo() {
        val today = LocalDate.of(2026, 11, 24)
        assertEquals(YearMonth.of(2026, 11) to YearMonth.of(2036, 11), monthRange(today, today, null))
        assertEquals("Novembro de 2026", monthTitle(YearMonth.of(2026, 11)))
    }
}
