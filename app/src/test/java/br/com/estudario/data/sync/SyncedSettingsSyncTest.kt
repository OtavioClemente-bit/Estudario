package br.com.estudario.data.sync

import br.com.estudario.domain.SyncedSettings
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

/** Os ajustes de "userPrefs" vão e voltam entre o app e o site sem que um lado apague o do outro. */
class SyncedSettingsSyncTest {
    private fun prefs(vararg settings: Pair<String, SyncedSettings.Setting>) =
        JSONObject().put("dailyGoal", 20).put("goalUpdatedAt", 0L).also { SyncedPrefs.putSettings(it, settings.toMap()) }

    @Test
    fun idaEVoltaDosAjustes() {
        val original = mapOf(SyncedSettings.THEME to SyncedSettings.Setting("DARK", 10), SyncedSettings.READER_TONE to SyncedSettings.Setting("sepia", 20))
        val json = JSONObject()
        SyncedPrefs.putSettings(json, original)
        assertEquals(original, SyncedPrefs.readSettings(JSONObject(json.toString())))
        assertEquals(emptyMap<String, SyncedSettings.Setting>(), SyncedPrefs.readSettings(JSONObject().put("dailyGoal", 20)))
    }

    @Test
    fun juntarFicaComOMaisNovoDeCadaAjuste() {
        val phone = prefs(SyncedSettings.THEME to SyncedSettings.Setting("DARK", 300), SyncedSettings.EXPLAIN_RIGHT_AWAY to SyncedSettings.Setting("1", 1))
        val site = prefs(SyncedSettings.THEME to SyncedSettings.Setting("LIGHT", 200), SyncedSettings.EXPLAIN_RIGHT_AWAY to SyncedSettings.Setting("0", 400))
        for ((a, b) in listOf(phone to site, site to phone)) {
            val merged = SyncedPrefs.readSettings(SyncedPrefs.merge(a, b))
            assertEquals("DARK", merged.getValue(SyncedSettings.THEME).value)
            assertEquals("0", merged.getValue(SyncedSettings.EXPLAIN_RIGHT_AWAY).value)
        }
    }

    @Test
    fun juntarNaoApagaCampoQueOAppNaoConhece() {
        val site = prefs().put("campoDoSite", JSONObject().put("x", 1)).put("listaNova", JSONArray(listOf("a")))
        val phone = prefs()
        for ((a, b) in listOf(phone to site, site to phone)) {
            val merged = SyncedPrefs.merge(a, b)!!
            assertEquals(1, merged.getJSONObject("campoDoSite").getInt("x"))
            assertEquals("a", merged.getJSONArray("listaNova").getString(0))
        }
    }

    @Test
    fun fotoAntigaSemAjustesContinuaValendo() {
        val old = JSONObject().put("dailyGoal", 30).put("goalUpdatedAt", 5L)
        val phone = prefs(SyncedSettings.HUD_TIMER to SyncedSettings.Setting("0", 9))
        val merged = SyncedPrefs.merge(old, phone)!!
        assertEquals(30, merged.getInt("dailyGoal"))
        assertEquals("0", SyncedPrefs.readSettings(merged).getValue(SyncedSettings.HUD_TIMER).value)
    }
}
