package br.com.estudario.focus

import br.com.estudario.data.preferences.FocusSessionPrefs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FocusSessionPrefsTest {
    private val inicio = 1_700_000_000_000L

    @Test fun sessaoZeradaNaoEstaAtiva() {
        val vazia = FocusSessionPrefs()
        assertFalse(vazia.active)
        assertEquals(0, vazia.elapsedMinutes(inicio))
    }

    @Test fun tempoDecorridoContaMinutosInteiros() {
        val sessao = FocusSessionPrefs(startedAt = inicio, title = "Direito Constitucional")
        assertTrue(sessao.active)
        assertEquals(0, sessao.elapsedMinutes(inicio + 59_000L))
        assertEquals(1, sessao.elapsedMinutes(inicio + 60_000L))
        assertEquals(50, sessao.elapsedMinutes(inicio + 50 * 60_000L))
    }

    @Test fun relogioDoAparelhoAtrasadoNaoGeraTempoNegativo() {
        val sessao = FocusSessionPrefs(startedAt = inicio)
        assertEquals(0, sessao.elapsedMinutes(inicio - 600_000L))
    }

    @Test fun semFiltroAnteriorConhecidoNadaEhRestaurado() {
        assertEquals(FocusSessionPrefs.FILTER_UNKNOWN, FocusSessionPrefs(startedAt = inicio).previousFilter)
    }
}
