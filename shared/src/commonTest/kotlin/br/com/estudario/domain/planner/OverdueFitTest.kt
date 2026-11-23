package br.com.estudario.domain.planner

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OverdueFitTest {
    private val today = 20_000L

    @Test fun diaDaSemanaIso() {
        assertEquals(4, OverdueFit.isoDayOfWeek(0)) // 1970-01-01, quinta
        assertEquals(1, OverdueFit.isoDayOfWeek(4)) // segunda
        assertEquals(7, OverdueFit.isoDayOfWeek(3)) // domingo
    }

    @Test fun cadaAtrasadaVaiParaOPrimeiroDiaEmQueCabe() {
        val late = listOf(OverdueFit.Item("a", 60), OverdueFit.Item("b", 60), OverdueFit.Item("c", 60))
        val moved = OverdueFit.distribute(late, today, { 120 }, mapOf(today to 60))
        assertEquals(mapOf("a" to today, "b" to today + 1, "c" to today + 1), moved)
    }

    @Test fun pulaDiaSemEstudo() {
        val moved = OverdueFit.distribute(listOf(OverdueFit.Item("a", 30)), today, { if (it == today) 0 else 60 }, emptyMap())
        assertEquals(today + 1, moved["a"])
    }

    @Test fun nadaFicaNoPassadoMesmoSemEspaco() {
        val moved = OverdueFit.distribute(listOf(OverdueFit.Item("a", 500), OverdueFit.Item("b", 500)), today, { 60 }, emptyMap(), horizonDays = 3)
        assertTrue(moved.values.all { it >= today })
        assertEquals(2, moved.values.toSet().size) // a segunda vai para outro dia, o menos carregado
    }

    @Test fun semNenhumDiaDeEstudoVaiParaHoje() {
        assertEquals(today, OverdueFit.distribute(listOf(OverdueFit.Item("a", 30)), today, { 0 }, emptyMap())["a"])
    }
}
