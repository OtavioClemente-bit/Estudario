package br.com.estudario.domain

import br.com.estudario.domain.SyncedSettings.Setting
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SyncedSettingsTest {
    @Test
    fun cadaAjusteFicaComOMudadoPorUltimo() {
        val phone = mapOf(SyncedSettings.THEME to Setting("DARK", 200), SyncedSettings.READER_SIZE to Setting("1", 100))
        val site = mapOf(SyncedSettings.THEME to Setting("LIGHT", 100), SyncedSettings.READER_SIZE to Setting("3", 300), "novo" to Setting("x", 5))
        for ((a, b) in listOf(phone to site, site to phone)) {
            val merged = SyncedSettings.merge(a, b)
            assertEquals("DARK", merged.getValue(SyncedSettings.THEME).value)
            assertEquals("3", merged.getValue(SyncedSettings.READER_SIZE).value)
            assertEquals("x", merged.getValue("novo").value, "ajuste desconhecido não some")
        }
    }

    @Test
    fun registroDataSoOQueMudou() {
        val first = SyncedSettings.stamp(emptyMap(), mapOf(SyncedSettings.HUD_TIMER to "1"), now = 50)
        assertEquals(1L, first.getValue(SyncedSettings.HUD_TIMER).at, "escolha antiga perde para qualquer escolha datada")
        val same = SyncedSettings.stamp(first, mapOf(SyncedSettings.HUD_TIMER to "1"), now = 90)
        assertEquals(1L, same.getValue(SyncedSettings.HUD_TIMER).at)
        val changed = SyncedSettings.stamp(same, mapOf(SyncedSettings.HUD_TIMER to "0"), now = 120)
        assertEquals(Setting("0", 120), changed.getValue(SyncedSettings.HUD_TIMER))
        val kept = SyncedSettings.stamp(mapOf("doOutro" to Setting("y", 7)), emptyMap(), now = 1)
        assertEquals(Setting("y", 7), kept["doOutro"])
    }

    @Test
    fun adotaSoOMaisNovo() {
        val local = mapOf(SyncedSettings.THEME to Setting("DARK", 200), SyncedSettings.HUD_SCORE to Setting("1", 1))
        val remote = mapOf(SyncedSettings.THEME to Setting("LIGHT", 150), SyncedSettings.HUD_SCORE to Setting("0", 160), SyncedSettings.READER_TONE to Setting("sepia", 1))
        assertEquals(setOf(SyncedSettings.HUD_SCORE, SyncedSettings.READER_TONE), SyncedSettings.newer(local, remote).keys)
        assertTrue(SyncedSettings.newer(remote, remote).isEmpty())
    }

    @Test
    fun intervalosDaRevisao() {
        assertEquals(listOf(1L, 3L, 7L, 14L, 30L), SyncedSettings.intervals("1,3,7,14,30"))
        assertEquals(null, SyncedSettings.intervals("lixo"))
    }
}
