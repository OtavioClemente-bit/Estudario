package br.com.estudario.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------------------------
// Paleta base. Índigo (foco, tecnologia) + verde (domínio/conclusão) + âmbar (atenção), sobre um
// neutro levemente quente, não o cinza-azulado frio de app financeiro, nem o branco puro de
// template. Cada papel do Material 3 é definido explicitamente: o app não pode depender dos tons
// neutros genéricos que o Compose deriva sozinho quando só primary/secondary são informados.
// ---------------------------------------------------------------------------------------------

val EstudarioLightColorScheme = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE6E3FF),
    onPrimaryContainer = Color(0xFF1B1464),
    inversePrimary = Color(0xFFC5C0FF),

    secondary = Color(0xFF0B7A56),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFF3E1),
    onSecondaryContainer = Color(0xFF00341F),

    tertiary = Color(0xFF8A5300),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE3B8),
    onTertiaryContainer = Color(0xFF2B1700),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFAF9F5),
    onBackground = Color(0xFF1B1B18),
    surface = Color(0xFFFAF9F5),
    onSurface = Color(0xFF1B1B18),
    surfaceVariant = Color(0xFFE7E4DC),
    onSurfaceVariant = Color(0xFF48473F),

    outline = Color(0xFF79776D),
    outlineVariant = Color(0xFFC9C6BB),
    scrim = Color(0xFF000000),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF4F3EE),
    surfaceContainer = Color(0xFFEEEDE7),
    surfaceContainerHigh = Color(0xFFE8E7E1),
    surfaceContainerHighest = Color(0xFFE2E1DB),
    surfaceDim = Color(0xFFDAD9D3),
    surfaceBright = Color(0xFFFAF9F5),

    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color(0xFFF2F0EA),
)

val EstudarioDarkColorScheme = darkColorScheme(
    primary = Color(0xFFC5C0FF),
    onPrimary = Color(0xFF25206D),
    primaryContainer = Color(0xFF3B3591),
    onPrimaryContainer = Color(0xFFE4E1FF),
    inversePrimary = Color(0xFF4F46E5),

    secondary = Color(0xFF7EE0B8),
    onSecondary = Color(0xFF003824),
    secondaryContainer = Color(0xFF005138),
    onSecondaryContainer = Color(0xFFA6F2D3),

    tertiary = Color(0xFFFFB870),
    onTertiary = Color(0xFF472A00),
    tertiaryContainer = Color(0xFF653D00),
    onTertiaryContainer = Color(0xFFFFDDB3),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF131311),
    onBackground = Color(0xFFE6E3DC),
    surface = Color(0xFF131311),
    onSurface = Color(0xFFE6E3DC),
    surfaceVariant = Color(0xFF48473F),
    onSurfaceVariant = Color(0xFFC9C6BB),

    outline = Color(0xFF938F85),
    outlineVariant = Color(0xFF48473F),
    scrim = Color(0xFF000000),

    surfaceContainerLowest = Color(0xFF0D0D0B),
    surfaceContainerLow = Color(0xFF1B1B18),
    surfaceContainer = Color(0xFF1F1F1C),
    surfaceContainerHigh = Color(0xFF2A2A26),
    surfaceContainerHighest = Color(0xFF353531),
    surfaceDim = Color(0xFF131311),
    surfaceBright = Color(0xFF3A3A35),

    inverseSurface = Color(0xFFE6E3DC),
    inverseOnSurface = Color(0xFF303030),
)

/**
 * Papéis de cor que o Material 3 não tem e o Estudário precisa: o estado "concluído" de uma
 * missão, o destaque da atividade atual, o tom de atenção, e a paleta controlada usada para
 * identificar matérias visualmente (nunca uma cor aleatória por matéria, sempre uma destas).
 */
data class EstudarioExtendedColors(
    val completed: Color,
    val onCompleted: Color,
    val current: Color,
    val onCurrent: Color,
    val upcoming: Color,
    val attention: Color,
    val subjectPalette: List<Color>,
)

val LightExtendedColors = EstudarioExtendedColors(
    completed = Color(0xFF0B7A56),
    onCompleted = Color(0xFFFFFFFF),
    current = Color(0xFF4F46E5),
    onCurrent = Color(0xFFFFFFFF),
    upcoming = Color(0xFF79776D),
    attention = Color(0xFF8A5300),
    subjectPalette = listOf(
        Color(0xFF4F46E5), // índigo, marca
        Color(0xFF0B7A56), // verde
        Color(0xFF8A5300), // âmbar
        Color(0xFF1D6FA5), // azul-petróleo
        Color(0xFFA23E6B), // vinho
        Color(0xFF6B5E00), // oliva
        Color(0xFF6E5AC7), // violeta
        Color(0xFF9B4A1E), // terracota
    ),
)

val DarkExtendedColors = EstudarioExtendedColors(
    completed = Color(0xFF7EE0B8),
    onCompleted = Color(0xFF003824),
    current = Color(0xFFC5C0FF),
    onCurrent = Color(0xFF25206D),
    upcoming = Color(0xFF938F85),
    attention = Color(0xFFFFB870),
    subjectPalette = listOf(
        Color(0xFFC5C0FF),
        Color(0xFF7EE0B8),
        Color(0xFFFFB870),
        Color(0xFF7FC2ED),
        Color(0xFFE597BE),
        Color(0xFFD0C267),
        Color(0xFFBDA9FF),
        Color(0xFFE2A27E),
    ),
)

val LocalEstudarioColors = staticCompositionLocalOf { LightExtendedColors }
