package br.com.estudario.web.data

import br.com.estudario.domain.SyncedSettings
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

/** Os ajustes da conta (userPrefs.settings) lidos e gravados pelo site do jeito que o app escreve. */
class AccountSettingsTest {
    private val fromApp = snapshotJson.parseToJsonElement(
        """{"dailyGoal":20,"userName":"Ana","campoNovo":{"x":1},
           "settings":{"theme":"DARK","reviewIntervals":"1,3,7,14,30","defaultQuestionCount":"15"},
           "settingsAt":{"theme":100,"reviewIntervals":200,"defaultQuestionCount":1}}""",
    ).jsonObject

    @Test
    fun leOQueOAppEscreveu() {
        val settings = AccountSettings.read(fromApp)
        assertEquals(SyncedSettings.Setting("DARK", 100), settings[SyncedSettings.THEME])
        assertEquals("15", settings[SyncedSettings.DEFAULT_QUESTION_COUNT]?.value)
    }

    @Test
    fun gravarUmAjusteNaoApagaOsOutrosNemCamposDesconhecidos() {
        val next = AccountSettings.write(fromApp, mapOf(SyncedSettings.READER_TONE to SyncedSettings.Setting("sepia", 300)))
        val settings = AccountSettings.read(next)
        assertEquals(SyncedSettings.Setting("sepia", 300), settings[SyncedSettings.READER_TONE])
        assertEquals(SyncedSettings.Setting("DARK", 100), settings[SyncedSettings.THEME])
        assertEquals(SyncedSettings.Setting("1,3,7,14,30", 200), settings[SyncedSettings.REVIEW_INTERVALS])
        assertEquals(JsonPrimitive("Ana"), next["userName"])
        assertEquals(JsonPrimitive(1), (next["campoNovo"] as JsonObject)["x"])
    }

    @Test
    fun revisaoIntensivaEscolhidaNoAppValeNoSite() {
        val data = Snapshot.parse("""{"format":"estudario-backup","version":8,"userPrefs":$fromApp}""")
        assertEquals(listOf(1L, 3L, 7L, 14L, 30L), AccountSettings.reviewIntervals(data))
        assertEquals(listOf(1L, 7L, 30L), AccountSettings.reviewIntervals(Snapshot.empty()))
    }
}
