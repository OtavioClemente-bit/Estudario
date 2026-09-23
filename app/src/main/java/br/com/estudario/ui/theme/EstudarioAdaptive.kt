package br.com.estudario.ui.theme

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Escala adaptativa do Estudário: o app inteiro fica proporcional ao aparelho, seja qual for o
 * tamanho de fonte e o "tamanho da tela" (zoom de exibição) que a pessoa escolheu no Android.
 *
 * O problema que isto resolve: o layout foi desenhado olhando um celular com fonte pequena. Com a
 * fonte padrão (ou maior), ou num aparelho estreito, os textos cresciam mas a tela não, cabeçalhos
 * ocupavam metade da altura, linhas de texto se atropelavam e botões cortavam o rótulo.
 *
 * Duas correções, aplicadas uma única vez na Activity (ver [adjust]), para que TUDO herde, telas,
 * diálogos, menus, bottom sheets, date pickers, e não só o que está dentro de um Compose específico:
 *
 * 1. **Fonte amortecida.** A preferência da pessoa é respeitada (fonte maior continua maior), mas o
 *    crescimento é suavizado e tem teto: `1 + (sistema - 1) × [FONT_GROWTH_DAMPING]`, preso entre
 *    [MIN_FONT_SCALE] e [MAX_FONT_SCALE]. Fonte 1,15 vira ~1,09; fonte 1,3 vira ~1,18; fonte 2,0
 *    chega no teto. Em telas muito estreitas o teto é menor ([MAX_FONT_SCALE_NARROW]).
 * 2. **Densidade proporcional.** Se a largura útil fica abaixo de [REFERENCE_WIDTH_DP] (aparelho
 *    pequeno ou "tamanho da tela" aumentado), a densidade é reduzida na mesma proporção, tudo encolhe
 *    junto (texto, espaçamento, ícones), como um zoom, até o piso [MIN_DENSITY_SCALE]. Telas normais
 *    e grandes não são tocadas.
 *
 * Todos os números moram aqui. Mudar o comportamento do app inteiro é mudar uma constante.
 */
object EstudarioAdaptiveScale {
    /** Largura (dp) para a qual o layout foi desenhado. Abaixo disso, a densidade é compensada. */
    const val REFERENCE_WIDTH_DP = 360f

    /** Altura (dp) mínima confortável. Telas mais baixas (ex.: 16:9 pequeno) também são compensadas. */
    const val REFERENCE_HEIGHT_DP = 600f

    /** Quanto a densidade pode ser reduzida no máximo (0,85 = até 15% menor). */
    const val MIN_DENSITY_SCALE = 0.85f

    /** Fração do aumento de fonte do sistema que o app aplica. */
    const val FONT_GROWTH_DAMPING = 0.6f

    const val MIN_FONT_SCALE = 0.85f
    const val MAX_FONT_SCALE = 1.30f
    const val MAX_FONT_SCALE_NARROW = 1.20f

    /** Fator de densidade para a tela, em dp "crus" do sistema. Nunca amplia: só compensa telas pequenas. */
    fun densityScale(screenWidthDp: Int, screenHeightDp: Int): Float {
        if (screenWidthDp <= 0 || screenHeightDp <= 0) return 1f
        // Em paisagem a referência de largura vale para a altura (o lado curto).
        val shortSide = min(screenWidthDp, screenHeightDp).toFloat()
        val longSide = maxOf(screenWidthDp, screenHeightDp).toFloat()
        val byShort = shortSide / REFERENCE_WIDTH_DP
        val byLong = longSide / REFERENCE_HEIGHT_DP
        return min(1f, min(byShort, byLong)).coerceAtLeast(MIN_DENSITY_SCALE)
    }

    /** Escala de fonte efetiva a partir da escolhida no sistema. [effectiveWidthDp] já considera a densidade ajustada. */
    fun fontScale(systemFontScale: Float, effectiveWidthDp: Float): Float {
        if (systemFontScale <= 0f) return 1f
        val damped = if (systemFontScale <= 1f) systemFontScale else 1f + (systemFontScale - 1f) * FONT_GROWTH_DAMPING
        val ceiling = if (effectiveWidthDp < REFERENCE_WIDTH_DP + 20f) MAX_FONT_SCALE_NARROW else MAX_FONT_SCALE
        return damped.coerceIn(MIN_FONT_SCALE, ceiling)
    }

    /**
     * Configuração de override para a Activity (ou `null` se nada precisa mudar). Só os campos
     * definidos aqui sobrescrevem os do sistema, o resto (idioma, tema, orientação) segue intacto.
     */
    fun adjust(base: Configuration): Configuration? {
        val scale = densityScale(base.screenWidthDp, base.screenHeightDp)
        // O teto de fonte olha o lado curto da tela (em pé, a largura; deitado, a altura).
        val shortSide = listOf(base.screenWidthDp, base.screenHeightDp).filter { it > 0 }.minOrNull()
        val effectiveShortSide = shortSide?.let { it / scale } ?: REFERENCE_WIDTH_DP
        val font = fontScale(base.fontScale, effectiveShortSide)
        val densityChanges = scale < 0.999f && base.densityDpi > 0
        val fontChanges = kotlin.math.abs(font - base.fontScale) > 0.001f
        if (!densityChanges && !fontChanges) return null
        // Configuration() começa "indefinida": só o que for atribuído abaixo sobrescreve o sistema.
        return Configuration().apply {
            if (fontChanges) fontScale = font
            if (densityChanges) {
                densityDpi = (base.densityDpi * scale).roundToInt()
                screenWidthDp = (base.screenWidthDp / scale).roundToInt()
                screenHeightDp = (base.screenHeightDp / scale).roundToInt()
                smallestScreenWidthDp = (base.smallestScreenWidthDp / scale).roundToInt()
            }
        }
    }
}

/**
 * O que as telas precisam saber sobre o espaço disponível, já na escala ajustada. Use para
 * decidir entre enfileirar e empilhar, não para calcular tamanhos à mão.
 */
@Immutable
data class EstudarioLayout(
    val widthDp: Int,
    val heightDp: Int,
    val fontScale: Float,
) {
    /** Celular estreito: menos de 380dp úteis de largura. */
    val isCompactWidth: Boolean get() = widthDp < 380

    /** Tela baixa (ex.: celular pequeno, paisagem, teclado aberto): cabeçalhos devem encolher. */
    val isShortHeight: Boolean get() = heightDp < 700

    /** Tablet ou celular deitado: o conteúdo ganha largura máxima e margem maior. */
    val isExpandedWidth: Boolean get() = widthDp >= 600

    /** Fonte acima do padrão: textos lado a lado devem poder quebrar para a linha de baixo. */
    val isLargeText: Boolean get() = fontScale >= 1.1f

    /**
     * Largura "em caracteres": tela estreita OU fonte grande. Quando verdadeiro, pares de botões
     * e rótulos lado a lado devem ir para linhas separadas.
     */
    val prefersStacking: Boolean get() = widthDp / fontScale < 360f

    /** Margem horizontal padrão da tela, proporcional à largura. */
    val screenGutter: Dp
        get() = when {
            widthDp < 360 -> 12.dp
            widthDp < 400 -> 16.dp
            widthDp >= 600 -> 28.dp
            else -> 20.dp
        }

    /** Largura máxima de leitura do conteúdo (tablets/paisagem não esticam cards de ponta a ponta). */
    val contentMaxWidth: Dp get() = 720.dp

    /** Margem lateral extra que centraliza o conteúdo em [contentMaxWidth]; zero em celular em pé. */
    fun centeredContentInset(): Dp = ((widthDp.dp - contentMaxWidth) / 2).coerceAtLeast(0.dp)

    companion object {
        val Default = EstudarioLayout(widthDp = 400, heightDp = 800, fontScale = 1f)

        fun from(configuration: Configuration): EstudarioLayout = EstudarioLayout(
            widthDp = configuration.screenWidthDp.takeIf { it > 0 } ?: Default.widthDp,
            heightDp = configuration.screenHeightDp.takeIf { it > 0 } ?: Default.heightDp,
            fontScale = configuration.fontScale.takeIf { it > 0f } ?: 1f,
        )
    }
}

val LocalEstudarioLayout = staticCompositionLocalOf { EstudarioLayout.Default }

/** Atalho no mesmo espírito de `MaterialTheme.colorScheme`. */
@Composable
@ReadOnlyComposable
fun estudarioLayout(): EstudarioLayout = LocalEstudarioLayout.current
