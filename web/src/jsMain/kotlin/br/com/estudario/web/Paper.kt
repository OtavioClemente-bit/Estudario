package br.com.estudario.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.browser.localStorage

/**
 * O fundo do caderno, do jeito da pessoa (Ajustes > Aparência): o padrão do papel, a cor da tinta
 * (que também pinta o botão principal), a luz no canto, a força das linhas e o grão. Vale no claro e no
 * escuro: o CSS (camada "Caderno de projeto") lê os atributos e variáveis que ficam no <html>.
 */
object Paper {
    private const val KEY = "estudario.papel"

    val patterns = listOf("milimetrado" to "Milimetrado", "quadriculado" to "Quadriculado", "pontos" to "Pontilhado", "pautado" to "Pautado", "isometrico" to "Isométrico", "liso" to "Liso")
    /** Nome, RGB e a cor para a amostra. */
    val inks = listOf(
        Triple("Azul caneta", "47, 91, 211", "#2F5BD3"), Triple("Verde", "35, 134, 79", "#23864F"), Triple("Roxo", "124, 77, 255", "#7C4DFF"),
        Triple("Framboesa", "214, 64, 108", "#D6406C"), Triple("Laranja", "214, 110, 20", "#D66E14"), Triple("Grafite", "70, 74, 86", "#464A56"),
    )
    val lights = listOf(
        Triple("Luminária", "255, 210, 63", "#FFD23F"), Triple("Azul", "92, 134, 255", "#5C86FF"), Triple("Pôr do sol", "255, 127, 168", "#FF7FA8"),
        Triple("Menta", "76, 208, 138", "#4CD08A"), Triple("Sem luz", "", "transparent"),
    )

    var pattern by mutableStateOf("milimetrado"); private set
    var ink by mutableStateOf(inks.first().second); private set
    var light by mutableStateOf(lights.first().second); private set
    var strength by mutableStateOf(1.0); private set
    var grain by mutableStateOf(1.0); private set

    init {
        runCatching { localStorage.getItem(KEY) }.getOrNull()?.split("|")?.let { p ->
            p.getOrNull(0)?.takeIf { v -> patterns.any { it.first == v } }?.let { pattern = it }
            p.getOrNull(1)?.takeIf { v -> inks.any { it.second == v } }?.let { ink = it }
            p.getOrNull(2)?.takeIf { v -> lights.any { it.second == v } }?.let { light = it }
            p.getOrNull(3)?.toDoubleOrNull()?.let { strength = it.coerceIn(0.0, 2.0) }
            p.getOrNull(4)?.toDoubleOrNull()?.let { grain = it.coerceIn(0.0, 2.0) }
        }
    }

    fun set(pattern: String = this.pattern, ink: String = this.ink, light: String = this.light, strength: Double = this.strength, grain: Double = this.grain) {
        this.pattern = pattern; this.ink = ink; this.light = light; this.strength = strength; this.grain = grain
        runCatching { localStorage.setItem(KEY, listOf(pattern, ink, light, strength, grain).joinToString("|")) }
        apply()
    }

    fun reset() = set("milimetrado", inks.first().second, lights.first().second, 1.0, 1.0)

    fun apply() {
        val root = document.documentElement ?: return
        root.setAttribute("data-paper", pattern)
        if (light.isEmpty()) root.setAttribute("data-light", "off") else root.removeAttribute("data-light")
        val style = root.asDynamic().style
        style.setProperty("--tinta-rgb", ink)
        if (light.isNotEmpty()) style.setProperty("--luz-rgb", light)
        style.setProperty("--forca", strength.toString())
        style.setProperty("--grao", grain.toString())
    }
}
