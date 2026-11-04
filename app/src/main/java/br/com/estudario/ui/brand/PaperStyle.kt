package br.com.estudario.ui.brand

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * O fundo do caderno do jeito da pessoa (Ajustes > Aparência), igual ao do site: o padrão do papel e a
 * luz no canto. Vale no claro e no escuro. Fica guardado no celular.
 */
object PaperStyle {
    private const val PREFS = "estudario_ui"
    private const val KEY = "papel"

    val patterns = listOf("milimetrado" to "Milimetrado", "quadriculado" to "Quadriculado", "pontos" to "Pontilhado", "pautado" to "Pautado", "liso" to "Liso")
    val lights = listOf("luminaria" to "Luminária", "azul" to "Azul", "por-do-sol" to "Pôr do sol", "menta" to "Menta", "sem" to "Sem luz")

    var pattern by mutableStateOf("milimetrado"); private set
    var light by mutableStateOf("luminaria"); private set
    private var loaded = false

    fun load(context: Context) {
        if (loaded) return
        loaded = true
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null)?.split("|") ?: return
        p.getOrNull(0)?.takeIf { v -> patterns.any { it.first == v } }?.let { pattern = it }
        p.getOrNull(1)?.takeIf { v -> lights.any { it.first == v } }?.let { light = it }
    }

    /** Quando a escolha foi feita (vale a mais nova entre o app e o site). */
    fun updatedAt(context: Context): Long = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getLong(KEY + "_em", 0L)

    fun set(context: Context, pattern: String = this.pattern, light: String = this.light, at: Long = System.currentTimeMillis()) {
        this.pattern = pattern; this.light = light
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, "$pattern|$light").putLong(KEY + "_em", at).apply()
    }

    /** Veio da conta uma escolha mais nova (feita no site): usa ela. O site tem o isométrico, que aqui vira milimetrado. */
    fun adopt(context: Context, pattern: String?, light: String?, at: Long) {
        load(context)
        if (at <= updatedAt(context)) return
        val p = pattern?.takeIf { v -> patterns.any { it.first == v } } ?: "milimetrado"
        val l = light?.takeIf { v -> lights.any { it.first == v } } ?: this.light
        set(context, p, l, at)
    }
}
