package br.com.estudario.web.data

import br.com.estudario.domain.ReviewIntervals
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.SyncedSettings
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

/**
 * Os ajustes que valem na conta inteira (tema, explicação logo após responder, placar, revisão
 * padrão ou intensiva, opções da revisão, leitura da teoria): os mesmos campos "settings" e
 * "settingsAt" de "userPrefs" que o app lê e escreve (SyncedPrefs.kt, domain/SyncedSettings.kt).
 *
 * Cada troca feita aqui vai para a conta com a data; a que chega da conta mais nova que a deste
 * navegador é adotada. Ajuste que o site não conhece (só do app) passa intacto.
 */
object AccountSettings {
    private const val AT_PREFIX = "estudario.sync.at."
    private var adopting = false

    private fun localAt(name: String): Long = runCatching { kotlinx.browser.localStorage.getItem(AT_PREFIX + name)?.toLongOrNull() }.getOrNull() ?: 0L
    private fun setLocalAt(name: String, at: Long) { runCatching { kotlinx.browser.localStorage.setItem(AT_PREFIX + name, at.toString()) } }

    /** Os ajustes de um bloco userPrefs. */
    fun read(synced: JsonObject): Map<String, SyncedSettings.Setting> {
        val values = synced[SyncedSettings.SETTINGS] as? JsonObject ?: return emptyMap()
        val at = synced[SyncedSettings.SETTINGS_AT] as? JsonObject
        return values.mapNotNull { (key, value) ->
            val text = (value as? JsonPrimitive)?.contentOrNull ?: return@mapNotNull null
            key to SyncedSettings.Setting(text, (at?.get(key) as? JsonPrimitive)?.longOrNull ?: 0L)
        }.toMap()
    }

    /** [synced] com [settings] gravados por cima (os outros ajustes e campos ficam como estavam). */
    fun write(synced: JsonObject, settings: Map<String, SyncedSettings.Setting>): JsonObject {
        val all = read(synced) + settings
        val values = JsonObject((synced[SyncedSettings.SETTINGS] as? JsonObject).orEmpty() + all.mapValues { JsonPrimitive(it.value.value) })
        val at = JsonObject((synced[SyncedSettings.SETTINGS_AT] as? JsonObject).orEmpty() + all.mapValues { JsonPrimitive(it.value.at) })
        return JsonObject(synced + mapOf(SyncedSettings.SETTINGS to values, SyncedSettings.SETTINGS_AT to at))
    }

    /** A pessoa trocou um ajuste neste navegador: vale a partir de agora em todos os aparelhos. */
    fun changed(name: String, value: String) {
        if (adopting || Store.demo) return
        val at = kotlin.js.Date.now().toLong()
        setLocalAt(name, at)
        Store.update { data -> data.withObject(Prefs.SYNCED_KEY, write(Prefs.synced(data), mapOf(name to SyncedSettings.Setting(value, at)))) }
    }

    /** Ajustes da conta mudados depois dos deste navegador (no app ou em outro navegador): valem aqui. */
    fun adopt(synced: JsonObject) {
        val local = read(synced).keys.associateWith { SyncedSettings.Setting("", localAt(it)) }
        val newer = SyncedSettings.newer(local, read(synced))
        if (newer.isEmpty()) return
        adopting = true
        try {
            newer.forEach { (name, setting) -> if (apply(name, setting.value)) setLocalAt(name, setting.at) }
        } finally {
            adopting = false
        }
    }

    private fun apply(name: String, value: String): Boolean {
        when (name) {
            SyncedSettings.THEME -> value.takeIf { it in setOf("SYSTEM", "LIGHT", "DARK") }?.let { br.com.estudario.web.Theme.set(it) } ?: return false
            SyncedSettings.EXPLAIN_RIGHT_AWAY -> SyncedSettings.flag(value)?.let { Prefs.explanationRightAway = it } ?: return false
            SyncedSettings.HUD_TIMER -> SyncedSettings.flag(value)?.let { Prefs.hudTimer = it } ?: return false
            SyncedSettings.HUD_SCORE -> SyncedSettings.flag(value)?.let { Prefs.hudScore = it } ?: return false
            SyncedSettings.REVIEW_PRACTICE -> ReviewPreferences.save(ReviewPracticeOptions.decode(value))
            SyncedSettings.READER_SIZE -> value.toIntOrNull()?.takeIf { it in 0..3 }?.let { br.com.estudario.web.ui.ReaderPrefs.setSize(it) } ?: return false
            SyncedSettings.READER_FONT -> br.com.estudario.web.ui.ReaderPrefs.setSerif(value != "simples")
            SyncedSettings.READER_TONE -> value.takeIf { it in SyncedSettings.readerTones }?.let { br.com.estudario.web.ui.ReaderPrefs.setTone(it) } ?: return false
            SyncedSettings.READER_QUIET -> SyncedSettings.flag(value)?.let { br.com.estudario.web.ui.ReaderPrefs.setQuiet(it) } ?: return false
            // Revisão padrão/intensiva e questões por sessão: lidas da conta na hora (reviewIntervals).
            else -> return true
        }
        return true
    }

    /** Os dias das revisões escolhidos no app (padrão ou intensivo); sem escolha, os de sempre. */
    fun reviewIntervals(data: Snapshot = Store.data): List<Long> =
        SyncedSettings.intervals(read(Prefs.synced(data))[SyncedSettings.REVIEW_INTERVALS]?.value) ?: ReviewIntervals.days
}
