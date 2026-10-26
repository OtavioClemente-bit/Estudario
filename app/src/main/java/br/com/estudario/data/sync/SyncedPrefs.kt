package br.com.estudario.data.sync

import android.content.Context
import br.com.estudario.EstudarioApplication
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
    private val folhaSets = mapOf("wardrobeSeen" to "vistos", "celebratedPieces" to "celebrated")
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
        return out
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
        json.set(BADGES).takeIf { it.isNotEmpty() }?.let { app(context).preferences.markBadgesEarned(it) }
    }

    /** Entre duas fotos: a meta mudada por último e tudo o que foi visto ou comemorado nos dois. */
    fun merge(a: JSONObject?, b: JSONObject?): JSONObject? {
        if (a == null) return b
        if (b == null) return a
        val goal = if (b.optLong("goalUpdatedAt", 0L) > a.optLong("goalUpdatedAt", 0L)) b else a
        val out = JSONObject()
            .put("dailyGoal", goal.optInt("dailyGoal", 20)).put("goalHistory", goal.optString("goalHistory")).put("goalUpdatedAt", goal.optLong("goalUpdatedAt", 0L))
        (folhaSets.keys + BADGES).forEach { name -> out.put(name, JSONArray((a.set(name) + b.set(name)).sorted())) }
        return out
    }
}
