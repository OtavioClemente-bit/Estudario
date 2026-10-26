package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ErrorViradaTest {
    private val day = 86_400_000L
    private val now = 1_800_000_000_000L
    private fun e(id: Long, streak: Int, next: Long?, comment: String = "") =
        ErrorVirada.Entry(id, id, 1, 1, streak, next, now - day, now - 10 * day, comment)

    @Test
    fun soViraDepoisDosTresDegraus() {
        assertEquals(ErrorVirada.Stage.TABLE, ErrorVirada.stage(e(1, 0, now)))
        assertEquals(ErrorVirada.Stage.TURNING, ErrorVirada.stage(e(1, 1, now + 10 * day)))
        assertEquals(ErrorVirada.Stage.TURNED, ErrorVirada.stage(e(1, 3, null)))
    }

    @Test
    fun missaoPegaOsQueVenceramInclusiveOsDoMeioDaEscada() {
        val list = listOf(e(1, 0, now - day), e(2, 1, now - 2 * day), e(3, 2, now + day), e(4, 3, null), e(5, 0, now + 3 * day))
        // o erro novo (degrau de 3 dias ainda longe) entra: na mesa dá para tentar virar já
        assertEquals(listOf(2L, 1L, 5L), ErrorVirada.mission(list, now).map { it.id })
    }

    @Test
    fun motivoNaPrimeiraLinhaDaNota() {
        val c = ErrorVirada.withReason("antes de masculino não tem crase", ErrorVirada.Reason.LI_RAPIDO)
        assertEquals("Motivo: Li rápido\nantes de masculino não tem crase", c)
        assertEquals(ErrorVirada.Reason.LI_RAPIDO, ErrorVirada.reasonOf(c))
        assertEquals("antes de masculino não tem crase", ErrorVirada.noteOf(c))
        // trocar o motivo não duplica a linha
        val d = ErrorVirada.withReason(c, ErrorVirada.Reason.PEGADINHA)
        assertEquals(1, d.lines().count { it.startsWith("Motivo:") })
        assertNull(ErrorVirada.reasonOf("só uma nota"))
    }

    @Test
    fun padraoDosMotivos() {
        val list = listOf(e(1, 0, now, "Motivo: Li rápido"), e(2, 0, now, "Motivo: Li rápido"), e(3, 0, now, "Motivo: Não sabia"), e(4, 0, now))
        val stats = ErrorVirada.reasonStats(list)
        assertEquals(ErrorVirada.Reason.LI_RAPIDO to 2, stats.first())
        assertTrue(stats.sumOf { it.second } == 3)
    }
}
