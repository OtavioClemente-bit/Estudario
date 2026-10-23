package br.com.estudario.ui.brand

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import br.com.estudario.R

/**
 * Os sons do Estudário (scripts/som: gravações reais CC0 afinadas juntas em Ré maior, os mesmos do site),
 * tocados pelo SoundPool. Ficam carregados desde a abertura do app, então tocam na hora. Vêm desligados:
 * só tocam depois que a pessoa liga em Ajustes, e nunca com o celular no silencioso ou na vibração.
 */
object EstudarioSom {
    private val FILES = mapOf(
        "toque" to R.raw.som_toque, "acerto" to R.raw.som_acerto, "erro" to R.raw.som_erro, "combo" to R.raw.som_combo,
        "resultado" to R.raw.som_resultado, "missao" to R.raw.som_missao,
        "concluir" to R.raw.som_concluir, "lembrei" to R.raw.som_lembrei, "nao_lembrei" to R.raw.som_nao_lembrei,
        "cena_nivel" to R.raw.som_cena_nivel, "cena_emblema" to R.raw.som_cena_emblema, "cena_sequencia" to R.raw.som_cena_sequencia, "cena_peca" to R.raw.som_cena_peca,
    )
    private var pool: SoundPool? = null
    private val ids = mutableMapOf<String, Int>()
    private val ready = java.util.Collections.synchronizedSet(mutableSetOf<Int>())
    /** Pedidos feitos antes do som terminar de carregar (id -> quando pediu): toca assim que ficar pronto, se ainda fizer sentido. */
    private val pending = java.util.concurrent.ConcurrentHashMap<Int, Pair<Long, Float>>()

    fun init(context: Context) {
        if (pool != null) return
        val attrs = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        val p = SoundPool.Builder().setMaxStreams(4).setAudioAttributes(attrs).build()
        p.setOnLoadCompleteListener { pool, id, status ->
            if (status != 0) return@setOnLoadCompleteListener
            ready += id
            // pedido de antes de carregar (a cena abriu primeiro): toca se foi há menos de 1,5 s
            pending.remove(id)?.let { (at, vol) -> if (System.currentTimeMillis() - at < 1500) pool.play(id, vol, vol, 1, 0, 1f) }
        }
        FILES.forEach { (name, res) -> ids[name] = p.load(context.applicationContext, res, 1) }
        pool = p
    }

    private fun prefs(context: Context) = context.getSharedPreferences("estudario_som", Context.MODE_PRIVATE)
    fun isOn(context: Context): Boolean = prefs(context).getBoolean("on", false)
    fun setOn(context: Context, on: Boolean) = prefs(context).edit().putBoolean("on", on).apply()

    /** Liga e, com o celular no som normal, pode tocar agora. */
    fun canPlay(context: Context): Boolean {
        if (!isOn(context)) return false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return true
        return am.ringerMode == AudioManager.RINGER_MODE_NORMAL
    }

    fun play(context: Context, name: String, volume: Float = 0.8f) {
        init(context)
        if (!canPlay(context)) return
        val id = ids[name] ?: return
        if (id in ready) pool?.play(id, volume, volume, 1, 0, 1f) else pending[id] = System.currentTimeMillis() to volume
    }
}
