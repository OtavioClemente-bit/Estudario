package br.com.estudario.ui.brand

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import br.com.estudario.R

/**
 * Os sons do Estudário (scripts/som/gerar-sons.mjs: sintetizados do zero para o app, os mesmos do site),
 * tocados pelo SoundPool. Ficam carregados desde a abertura do app, então tocam na hora. Não tocam com
 * o celular no silencioso ou na vibração, nem com a opção desligada em Ajustes.
 */
object EstudarioSom {
    private val FILES = mapOf(
        "toque" to R.raw.som_toque, "acerto" to R.raw.som_acerto, "erro" to R.raw.som_erro, "combo" to R.raw.som_combo,
        "resultado" to R.raw.som_resultado, "missao" to R.raw.som_missao,
        "cena_nivel" to R.raw.som_cena_nivel, "cena_emblema" to R.raw.som_cena_emblema, "cena_sequencia" to R.raw.som_cena_sequencia, "cena_peca" to R.raw.som_cena_peca,
    )
    private var pool: SoundPool? = null
    private val ids = mutableMapOf<String, Int>()
    private val ready = java.util.Collections.synchronizedSet(mutableSetOf<Int>())

    fun init(context: Context) {
        if (pool != null) return
        val attrs = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        val p = SoundPool.Builder().setMaxStreams(4).setAudioAttributes(attrs).build()
        p.setOnLoadCompleteListener { _, id, status -> if (status == 0) ready += id }
        FILES.forEach { (name, res) -> ids[name] = p.load(context.applicationContext, res, 1) }
        pool = p
    }

    private fun prefs(context: Context) = context.getSharedPreferences("estudario_som", Context.MODE_PRIVATE)
    fun isOn(context: Context): Boolean = prefs(context).getBoolean("on", true)
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
        if (id in ready) pool?.play(id, volume, volume, 1, 0, 1f)
    }
}
