package br.com.estudario.data

import android.content.Context
import br.com.estudario.domain.FolhaItem
import br.com.estudario.domain.FolhaSlot
import br.com.estudario.domain.FolhaWardrobe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

/**
 * As peças do armário do Folha escolhidas pela pessoa. Ficam neste aparelho e vão junto com a foto
 * sincronizada da conta (chave "folhaLook", com o horário da última troca), então o visual escolhido
 * no app aparece no site e vice-versa; vale sempre a troca mais recente.
 */
object FolhaLookStore {
    /** Chave na foto sincronizada (a mesma do site, web/.../ui/Wardrobe.kt). */
    const val SNAPSHOT_KEY = "folhaLook"
    private const val PREFS = "folha_look"
    private const val KEY_UPDATED = "updated_at"

    private val _changes = MutableStateFlow(0)
    /** Muda a cada troca (daqui ou vinda de outro aparelho): a tela e o desenho releem. */
    val changes: StateFlow<Int> = _changes.asStateFlow()

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun read(context: Context, slot: FolhaSlot): String? = prefs(context).getString(slot.name.lowercase(), null)
        // escolha da versão antiga (um "acessório" só) vale para a categoria nova
        ?: FolhaWardrobe.legacyAccessory(prefs(context).getString("acessorio", null))?.takeIf { it.slot == slot }?.id

    /** Troca peças e agenda a sincronização (o site recebe em seguida). */
    fun write(context: Context, items: Collection<FolhaItem>) {
        prefs(context).edit().apply {
            items.forEach { putString(it.slot.name.lowercase(), it.id) }
            putLong(KEY_UPDATED, System.currentTimeMillis())
        }.apply()
        _changes.value++
        runCatching { br.com.estudario.data.sync.CloudSyncWorker.syncSoon(context, delaySeconds = 20) }
    }

    /** O visual para a foto sincronizada; nulo enquanto a pessoa nunca escolheu nada. */
    fun toJson(context: Context): JSONObject? {
        val p = prefs(context)
        val updated = p.getLong(KEY_UPDATED, 0L)
        val pieces = FolhaSlot.entries.mapNotNull { slot -> read(context, slot)?.let { slot.key to it } }
        if (updated == 0L && pieces.isEmpty()) return null
        return JSONObject().apply { pieces.forEach { (k, v) -> put(k, v) }; put("updatedAt", updated) }
    }

    /** Aplica o visual que veio da conta, se for mais novo que o deste aparelho. */
    fun applyRemote(context: Context, json: JSONObject?) {
        if (json == null) return
        val at = json.optLong("updatedAt", 0L)
        val p = prefs(context)
        if (at <= p.getLong(KEY_UPDATED, 0L)) return
        p.edit().apply {
            FolhaSlot.entries.forEach { slot -> json.optString(slot.key).takeIf { it.isNotBlank() }?.let { putString(slot.name.lowercase(), it) } }
            putLong(KEY_UPDATED, at)
        }.apply()
        _changes.value++
    }

    /** Entre duas fotos, o visual trocado por último. */
    fun newer(a: JSONObject?, b: JSONObject?): JSONObject? = when {
        a == null -> b
        b == null -> a
        b.optLong("updatedAt", 0L) > a.optLong("updatedAt", 0L) -> b
        else -> a
    }
}
