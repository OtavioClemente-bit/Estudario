package br.com.estudario.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

/**
 * O tema do Estudário. Cor, tipografia e forma são definidas de propósito em [EstudarioColors],
 * [EstudarioTypography] e [EstudarioShapes], nunca os padrões que o Material 3 preenche sozinho,
 * e a marca não depende de Dynamic Color: o app tem a mesma identidade em qualquer aparelho,
 * independentemente do papel de parede do Android.
 *
 * A escala (fonte e densidade) já chega ajustada pela Activity, ver [EstudarioAdaptiveScale]. Aqui só
 * publicamos [LocalEstudarioLayout] para as telas decidirem entre enfileirar e empilhar.
 */
@Composable
fun EstudarioTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors
    val configuration = LocalConfiguration.current
    val layout = remember(configuration.screenWidthDp, configuration.screenHeightDp, configuration.fontScale) {
        EstudarioLayout.from(configuration)
    }
    // O visual "console" (perfil de jogo, estúdio, missão do dia) lê o tema por aqui, até dentro do desenho.
    androidx.compose.runtime.SideEffect { CxTheme.dark = darkTheme }
    CxTheme.dark = darkTheme
    CompositionLocalProvider(
        LocalEstudarioColors provides extendedColors,
        LocalEstudarioLayout provides layout,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) EstudarioDarkColorScheme else EstudarioLightColorScheme,
            typography = EstudarioTypography,
            shapes = EstudarioMaterialShapes,
            content = content,
        )
    }
}

/**
 * Acesso conveniente aos papéis de cor exclusivos do Estudário (estado concluído, atividade atual,
 * paleta de matérias), no mesmo espírito de `MaterialTheme.colorScheme`. Nome de função, não de
 * objeto, para não colidir com o composable [EstudarioTheme] que já ocupa esse identificador.
 */
@Composable
fun estudarioColors(): EstudarioExtendedColors = LocalEstudarioColors.current

/**
 * Se o app está no tema escuro, para o visual "console" (Gx e Game): no claro os painéis ficam
 * brancos e lilás com tinta índigo; no escuro, o painel noturno de antes. É estado observável, então
 * trocar o tema redesenha esses painéis.
 */
object CxTheme {
    var dark by androidx.compose.runtime.mutableStateOf(false)
    /** Branco sobre o escuro; tinta índigo sobre o claro (preenchimentos, trilhos, grades). */
    fun tint(alpha: Float): androidx.compose.ui.graphics.Color =
        if (dark) androidx.compose.ui.graphics.Color.White.copy(alpha = alpha) else androidx.compose.ui.graphics.Color(0xFF26234A).copy(alpha = alpha)
}
