package br.com.estudario.data.sync

import android.content.Context
import br.com.estudario.EstudarioApplication
import br.com.estudario.domain.SyncedSettings
import br.com.estudario.domain.FocusPrefs
import br.com.estudario.domain.FocusRules
import br.com.estudario.domain.FocusRunning
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject

/**
 * Ajustes que não moram no banco mas precisam ser os mesmos no celular e no site (chave "userPrefs"
 * da foto sincronizada, a mesma do site, web/.../data/Progress.kt):
 *
 * - a meta diária e o histórico dela: com metas diferentes, o mesmo dia fechava a meta num aparelho e
 *   não no outro, e o celular gastava o marca-página num dia estudado no computador;
 * - o que já foi visto ou comemorado (peças do armário, presentes do Folha, emblemas): sem isso, o que
 *   foi ganho num aparelho aparecia como "NOVO" e era comemorado de novo no outro.
 *
 * As listas só crescem: junta-se o que os dois viram.
 */
object SyncedPrefs {
    const val SNAPSHOT_KEY = "userPrefs"
    private const val FOLHA_PREFS = "folha_look"
    /** Lista na foto -> chave em folha_look (as mesmas de WardrobeScreen e FolhaGift). */
    private val folhaSets = mapOf("wardrobeSeen" to "vistos", "celebratedPieces" to "celebrated", "flashDecks" to br.com.estudario.data.FlashDeckLog.KEY, "wardrobeEarned" to br.com.estudario.data.FolhaLookStore.KEY_OWNED, br.com.estudario.domain.Celebrated.SYNCED to CelebratedStore.KEY)
    private const val BADGES = "celebratedBadges"

    private fun app(context: Context) = context.applicationContext as EstudarioApplication
    private fun folha(context: Context) = context.getSharedPreferences(FOLHA_PREFS, Context.MODE_PRIVATE)
    private fun folhaSet(context: Context, key: String): Set<String>? =
        folha(context).getString(key, null)?.split(",")?.filter { it.isNotBlank() }?.toSet()

    private fun JSONObject.set(key: String): Set<String> =
        optJSONArray(key)?.let { array -> (0 until array.length()).map { array.optString(it) }.filter { it.isNotBlank() }.toSet() }.orEmpty()

    suspend fun toJson(context: Context): JSONObject {
        val (goal, history, at) = app(context).preferences.syncedGoal()
        val out = JSONObject().put("dailyGoal", goal).put("goalHistory", history).put("goalUpdatedAt", at)
        folhaSets.forEach { (name, key) -> out.put(name, JSONArray(folhaSet(context, key).orEmpty().sorted())) }
        out.put(BADGES, JSONArray(app(context).preferences.earnedBadges.first().sorted()))
        // O fundo do caderno (o mesmo do site): vale a escolha mais nova.
        br.com.estudario.ui.brand.PaperStyle.load(context)
        val paperAt = br.com.estudario.ui.brand.PaperStyle.updatedAt(context)
        if (paperAt > 0) out.put("paperPattern", br.com.estudario.ui.brand.PaperStyle.pattern).put("paperLight", br.com.estudario.ui.brand.PaperStyle.light).put("paperUpdatedAt", paperAt)
        // O nome (o mesmo do site): vale o escolhido por último; o que veio do Google conta como 0.
        val (name, nameAt) = app(context).preferences.syncedUserName()
        if (name.isNotBlank()) out.put("userName", name).put("userNameUpdatedAt", nameAt)
        // Tema, explicação, placar, revisão, leitura: o valor de agora, com a data da última troca.
        val record = SyncedSettings.stamp(settingsRecord(context), currentSettings(context), System.currentTimeMillis())
        saveSettingsRecord(context, record)
        putSettings(out, record)
        putFocus(context, out)
        putProfile(context, out)
        // Campos que esta versão não conhece (vindos do site ou de um app mais novo) voltam como vieram.
        extras(context).let { extra -> extra.keys().forEach { key -> if (!out.has(key)) out.put(key, extra.get(key)) } }
        return out
    }

    // ----- ajustes de estudo e aparência (SyncedSettings) -----

    private const val SETTINGS_PREFS = "synced_settings"
    private const val RECORD = "record"
    private const val EXTRAS = "extras"
    private fun settingsPrefs(context: Context) = context.applicationContext.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)
    private fun ui(context: Context) = context.applicationContext.getSharedPreferences("estudario_ui", Context.MODE_PRIVATE)

    /** Os quatro tamanhos de letra do leitor (TheoryReaderScreen), na ordem dos do site. */
    private val readerScales = listOf(0.9f, 1.05f, 1.2f, 1.38f)

    /** Campos de "userPrefs" que esta versão lê e escreve; o resto é guardado e devolvido intacto. */
    private val knownKeys: Set<String>
        get() = setOf(
            "dailyGoal", "goalHistory", "goalUpdatedAt", BADGES, "paperPattern", "paperLight", "paperUpdatedAt",
            "userName", "userNameUpdatedAt", SyncedSettings.SETTINGS, SyncedSettings.SETTINGS_AT,
        ) + folhaSets.keys + focusKeys + profileKeys

    // ----- perfil: imagem (ProfileImage) e XP de planos apagados, os mesmos do site -----

    private const val RETIRED_XP = "retiredPlanXp"
    private val profileKeys = setOf(br.com.estudario.domain.ProfileImage.KEY, br.com.estudario.domain.ProfileImage.UPDATED_AT, RETIRED_XP)

    private suspend fun putProfile(context: Context, out: JSONObject) {
        ProfileImageSync.localValue(context)?.let { (value, at) -> out.put(br.com.estudario.domain.ProfileImage.KEY, value).put(br.com.estudario.domain.ProfileImage.UPDATED_AT, at) }
        val retired = app(context).preferences.retiredPlanXp.first()
        if (retired > 0) out.put(RETIRED_XP, retired)
    }

    private suspend fun applyProfile(context: Context, json: JSONObject) {
        if (json.has(br.com.estudario.domain.ProfileImage.KEY)) {
            runCatching { ProfileImageSync.applyRemote(context, json.optString(br.com.estudario.domain.ProfileImage.KEY), json.optLong(br.com.estudario.domain.ProfileImage.UPDATED_AT, 0L)) }
        }
        app(context).preferences.applySyncedRetiredPlanXp(json.optInt(RETIRED_XP, 0))
    }

    private fun mergeProfile(a: JSONObject, b: JSONObject, out: JSONObject) {
        val k = br.com.estudario.domain.ProfileImage.KEY
        val t = br.com.estudario.domain.ProfileImage.UPDATED_AT
        br.com.estudario.domain.ProfileImage.pick(a.optString(k).ifBlank { null } to a.optLong(t, 0L), b.optString(k).ifBlank { null } to b.optLong(t, 0L))
            ?.let { (value, at) -> out.put(k, value).put(t, at) }
        val retired = maxOf(a.optInt(RETIRED_XP, 0), b.optInt(RETIRED_XP, 0))
        if (retired > 0) out.put(RETIRED_XP, retired)
    }

    // ----- modo foco (o mesmo do site, domain/FocusSync.kt) -----

    private val focusKeys = setOf(FocusRules.KEY_RUNNING, FocusRules.KEY_MINUTES, FocusRules.KEY_ALERT, FocusRules.KEY_CLOCK_HIDDEN, FocusRules.KEY_PREFS_AT)
    private fun JSONObject.getter(): (String) -> Any? = { k -> if (isNull(k)) null else opt(k) }
    private fun JSONObject.focusRunning(): FocusRunning? = optJSONObject(FocusRules.KEY_RUNNING)?.let { FocusRunning.from(it.getter()) }
    private fun focusJson(r: FocusRunning): JSONObject = JSONObject().apply { r.fields().forEach { (k, v) -> if (v != null) put(k, v) } }

    /** Preferências e sessão em andamento (ou a marca de que a última acabou), para o outro aparelho. */
    private suspend fun putFocus(context: Context, out: JSONObject) {
        val prefs = app(context).preferences
        prefs.focusPrefs.first().takeIf { it.updatedAt > 0L }?.fields()?.forEach { (k, v) -> out.put(k, v) }
        (prefs.focusSession.first().toRunning() ?: prefs.focusEnded.first())?.let { out.put(FocusRules.KEY_RUNNING, focusJson(it)) }
    }

    private suspend fun applyFocus(context: Context, json: JSONObject) {
        FocusPrefs.from(json.getter())?.let { app(context).preferences.setFocusPrefs(it, onlyIfNewer = true) }
        runCatching { br.com.estudario.focus.FocusSessionManager.applyRemote(context, json.focusRunning()) }
    }

    private fun mergeFocus(a: JSONObject, b: JSONObject, out: JSONObject) {
        FocusPrefs.newer(FocusPrefs.from(a.getter()), FocusPrefs.from(b.getter()))?.fields()?.forEach { (k, v) -> out.put(k, v) }
        FocusRunning.newer(a.focusRunning(), b.focusRunning())?.let { out.put(FocusRules.KEY_RUNNING, focusJson(it)) }
    }

    private fun extras(context: Context): JSONObject =
        runCatching { settingsPrefs(context).getString(EXTRAS, null)?.let(::JSONObject) }.getOrNull() ?: JSONObject()

    private fun saveExtras(context: Context, json: JSONObject) {
        val extra = JSONObject()
        json.keys().forEach { key -> if (key !in knownKeys) extra.put(key, json.get(key)) }
        settingsPrefs(context).edit().putString(EXTRAS, extra.toString()).apply()
    }

    private suspend fun currentSettings(context: Context): Map<String, String> {
        val prefs = app(context).preferences
        val ui = ui(context)
        val scale = ui.getFloat("reader_scale", 1.05f)
        return mapOf(
            SyncedSettings.THEME to prefs.themeMode.first(),
            SyncedSettings.EXPLAIN_RIGHT_AWAY to SyncedSettings.flag(prefs.showExplanation.first()),
            SyncedSettings.HUD_TIMER to SyncedSettings.flag(prefs.hudTimer.first()),
            SyncedSettings.HUD_SCORE to SyncedSettings.flag(prefs.hudScore.first()),
            SyncedSettings.REVIEW_INTERVALS to prefs.reviewIntervals.first().joinToString(","),
            SyncedSettings.REVIEW_PRACTICE to prefs.reviewPractice.first().encode(),
            SyncedSettings.DEFAULT_QUESTION_COUNT to prefs.defaultQuestionCount.first().toString(),
            SyncedSettings.READER_SIZE to readerScales.indices.minBy { kotlin.math.abs(readerScales[it] - scale) }.toString(),
            SyncedSettings.READER_FONT to if (ui.getBoolean("reader_book", true)) "livro" else "simples",
            SyncedSettings.READER_TONE to SyncedSettings.readerTones.getOrElse(ui.getInt("reader_tone", 0)) { "normal" },
            SyncedSettings.READER_QUIET to SyncedSettings.flag(ui.getBoolean("reader_quiet", false)),
        )
    }

    /** Grava aqui um ajuste que veio da conta; valor que esta versão não entende é ignorado. */
    private suspend fun applySetting(context: Context, key: String, value: String): Boolean {
        val prefs = app(context).preferences
        val ui = ui(context)
        when (key) {
            SyncedSettings.THEME -> value.takeIf { it in setOf("SYSTEM", "LIGHT", "DARK") }?.let { prefs.setThemeMode(it) } ?: return false
            SyncedSettings.EXPLAIN_RIGHT_AWAY -> SyncedSettings.flag(value)?.let { prefs.setShowExplanation(it) } ?: return false
            SyncedSettings.HUD_TIMER -> SyncedSettings.flag(value)?.let { prefs.setHudTimer(it) } ?: return false
            SyncedSettings.HUD_SCORE -> SyncedSettings.flag(value)?.let { prefs.setHudScore(it) } ?: return false
            SyncedSettings.REVIEW_INTERVALS -> SyncedSettings.intervals(value)?.let { prefs.setReviewIntervals(it) } ?: return false
            SyncedSettings.REVIEW_PRACTICE -> prefs.setReviewPractice(br.com.estudario.domain.ReviewPracticeOptions.decode(value))
            SyncedSettings.DEFAULT_QUESTION_COUNT -> value.toIntOrNull()?.takeIf { it in 1..200 }?.let { prefs.setDefaultQuestionCount(it) } ?: return false
            SyncedSettings.READER_SIZE -> value.toIntOrNull()?.let(readerScales::getOrNull)?.let { ui.edit().putFloat("reader_scale", it).apply() } ?: return false
            SyncedSettings.READER_FONT -> ui.edit().putBoolean("reader_book", value != "simples").apply()
            SyncedSettings.READER_TONE -> SyncedSettings.readerTones.indexOf(value).takeIf { it >= 0 }?.let { ui.edit().putInt("reader_tone", it).apply() } ?: return false
            SyncedSettings.READER_QUIET -> SyncedSettings.flag(value)?.let { ui.edit().putBoolean("reader_quiet", it).apply() } ?: return false
            else -> return true // ajuste de versão mais nova: só passa adiante
        }
        return true
    }

    private fun settingsRecord(context: Context): Map<String, SyncedSettings.Setting> =
        runCatching { settingsPrefs(context).getString(RECORD, null)?.let { readSettings(JSONObject().put(SyncedSettings.SETTINGS, JSONObject(it).optJSONObject("v")).put(SyncedSettings.SETTINGS_AT, JSONObject(it).optJSONObject("at"))) } }.getOrNull().orEmpty()

    private fun saveSettingsRecord(context: Context, record: Map<String, SyncedSettings.Setting>) {
        val holder = JSONObject()
        putSettings(holder, record)
        val text = JSONObject().put("v", holder.optJSONObject(SyncedSettings.SETTINGS)).put("at", holder.optJSONObject(SyncedSettings.SETTINGS_AT)).toString()
        settingsPrefs(context).edit().putString(RECORD, text).apply()
    }

    /** Lê "settings"/"settingsAt" de um bloco userPrefs (foto sem eles: vazio). */
    fun readSettings(json: JSONObject?): Map<String, SyncedSettings.Setting> {
        val values = json?.optJSONObject(SyncedSettings.SETTINGS) ?: return emptyMap()
        val at = json.optJSONObject(SyncedSettings.SETTINGS_AT)
        return values.keys().asSequence().mapNotNull { key ->
            val value = values.opt(key)?.takeIf { it != JSONObject.NULL }?.toString() ?: return@mapNotNull null
            key to SyncedSettings.Setting(value, at?.optLong(key, 0L) ?: 0L)
        }.toMap()
    }

    fun putSettings(json: JSONObject, settings: Map<String, SyncedSettings.Setting>) {
        if (settings.isEmpty()) return
        val values = JSONObject()
        val at = JSONObject()
        settings.keys.sorted().forEach { key -> values.put(key, settings.getValue(key).value); at.put(key, settings.getValue(key).at) }
        json.put(SyncedSettings.SETTINGS, values).put(SyncedSettings.SETTINGS_AT, at)
    }

    /** Aplica o que veio da conta: a meta se for mais nova, e soma o que já foi visto ou comemorado. */
    suspend fun applyRemote(context: Context, json: JSONObject?) {
        if (json == null) return
        if (json.has("dailyGoal")) {
            app(context).preferences.applySyncedGoal(json.optInt("dailyGoal", 20), json.optString("goalHistory"), json.optLong("goalUpdatedAt", 0L))
        }
        folhaSets.forEach { (name, key) ->
            val remote = json.set(name)
            val local = folhaSet(context, key)
            // Sem lista aqui, a tela marca tudo o que já existe na primeira vez; a da conta já serve de começo.
            if (remote.isNotEmpty() && (local == null || !local.containsAll(remote))) {
                folha(context).edit().putString(key, (local.orEmpty() + remote).sorted().joinToString(",")).apply()
            }
        }
        br.com.estudario.data.FlashDeckLog.refresh(context)
        if (json.has("paperUpdatedAt")) br.com.estudario.ui.brand.PaperStyle.adopt(context, json.optString("paperPattern"), json.optString("paperLight"), json.optLong("paperUpdatedAt", 0L))
        json.set(BADGES).takeIf { it.isNotEmpty() }?.let { app(context).preferences.markBadgesEarned(it) }
        if (json.optString("userName").isNotBlank()) app(context).preferences.applySyncedUserName(json.optString("userName"), json.optLong("userNameUpdatedAt", 0L))
        // Ajustes mudados por último no outro aparelho: valem aqui (e o registro guarda a data de lá).
        val record = SyncedSettings.stamp(settingsRecord(context), currentSettings(context), System.currentTimeMillis())
        val adopted = SyncedSettings.newer(record, readSettings(json)).filter { (key, setting) -> applySetting(context, key, setting.value) }
        saveSettingsRecord(context, record + adopted)
        saveExtras(context, json)
        applyFocus(context, json)
        applyProfile(context, json)
    }

    /** Entre duas fotos: a meta mudada por último e tudo o que foi visto ou comemorado nos dois. */
    fun merge(a: JSONObject?, b: JSONObject?): JSONObject? {
        if (a == null) return b
        if (b == null) return a
        val goal = if (b.optLong("goalUpdatedAt", 0L) > a.optLong("goalUpdatedAt", 0L)) b else a
        val out = JSONObject()
        // Campos que esta versão não conhece: ficam os dos dois lados (no empate, os de [a]).
        listOf(b, a).forEach { side -> side.keys().forEach { key -> if (key !in knownKeys) out.put(key, side.get(key)) } }
        putSettings(out, SyncedSettings.merge(readSettings(a), readSettings(b)))
        out
            .put("dailyGoal", goal.optInt("dailyGoal", 20)).put("goalHistory", goal.optString("goalHistory")).put("goalUpdatedAt", goal.optLong("goalUpdatedAt", 0L))
        (folhaSets.keys + BADGES).forEach { name -> out.put(name, JSONArray((a.set(name) + b.set(name)).sorted())) }
        val paper = if (b.optLong("paperUpdatedAt", 0L) > a.optLong("paperUpdatedAt", 0L)) b else a
        if (paper.has("paperUpdatedAt")) out.put("paperPattern", paper.optString("paperPattern")).put("paperLight", paper.optString("paperLight")).put("paperUpdatedAt", paper.optLong("paperUpdatedAt", 0L))
        val named = listOf(a, b).filter { it.optString("userName").isNotBlank() }.maxByOrNull { it.optLong("userNameUpdatedAt", 0L) }
        if (named != null) out.put("userName", named.optString("userName")).put("userNameUpdatedAt", named.optLong("userNameUpdatedAt", 0L))
        mergeFocus(a, b, out)
        mergeProfile(a, b, out)
        return out
    }
}

/**
 * As animações que aparecem uma vez só (chaves de [br.com.estudario.domain.Celebrated], as mesmas do site).
 * Vai na foto da conta como "celebrated" (SyncedPrefs): visto lá, não se repete aqui, e vice-versa.
 */
object CelebratedStore {
    const val KEY = "celebrated_keys"
    private fun prefs(context: Context) = context.applicationContext.getSharedPreferences("folha_look", Context.MODE_PRIVATE)
    private fun list(context: Context, key: String): Set<String> =
        prefs(context).getString(key, null)?.split(",")?.filter { it.isNotBlank() }?.toSet().orEmpty()

    /** Visto aqui ou em outro aparelho da conta (inclui a lista antiga das peças do presente). */
    fun has(context: Context, key: String): Boolean =
        key in br.com.estudario.domain.Celebrated.all(list(context, KEY), legacyPieces = list(context, "celebrated"))

    fun mark(context: Context, keys: Collection<String>) {
        val current = list(context, KEY)
        if (current.containsAll(keys)) return
        prefs(context).edit().putString(KEY, br.com.estudario.domain.Celebrated.merge(current, keys.toSet()).joinToString(",")).apply()
    }
}
