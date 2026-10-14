package br.com.estudario.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------------------------
// Paleta base: tinta e papel. As cores saem do próprio Folha: a tinta índigo do traço, o dourado do
// capelo e o verde-menta da folha. No claro, o fundo é um papel com um sopro de índigo (não o bege
// acinzentado de antes, que não era de ninguém) e os cartões ficam brancos por cima; no escuro, o
// fundo é a tinta azul-noite do capelo, não preto. Cada papel do Material 3 é definido explicitamente.
// ---------------------------------------------------------------------------------------------

val EstudarioLightColorScheme = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE3E0FF),
    onPrimaryContainer = Color(0xFF1B1464),
    inversePrimary = Color(0xFFB9B3FF),

    secondary = Color(0xFF0E8F66),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDF5E3),
    onSecondaryContainer = Color(0xFF00351F),

    tertiary = Color(0xFF8F5B00),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE7A8),
    onTertiaryContainer = Color(0xFF2C1A00),

    error = Color(0xFFC0262D),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD7),
    onErrorContainer = Color(0xFF410004),

    background = Color(0xFFF5F4FC),
    onBackground = Color(0xFF16142E),
    surface = Color(0xFFF5F4FC),
    onSurface = Color(0xFF16142E),
    surfaceVariant = Color(0xFFE2E0F2),
    onSurfaceVariant = Color(0xFF4A4769),

    outline = Color(0xFF79769A),
    outlineVariant = Color(0xFFCCC9E2),
    scrim = Color(0xFF0B0A1F),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFEFEDFA),
    surfaceContainer = Color(0xFFE9E7F7),
    surfaceContainerHigh = Color(0xFFE3E1F3),
    surfaceContainerHighest = Color(0xFFDCDAEE),
    surfaceDim = Color(0xFFD6D4E8),
    surfaceBright = Color(0xFFF5F4FC),

    inverseSurface = Color(0xFF26215C),
    inverseOnSurface = Color(0xFFF1EFFF),
)

val EstudarioDarkColorScheme = darkColorScheme(
    primary = Color(0xFFB9B3FF),
    onPrimary = Color(0xFF1E1868),
    primaryContainer = Color(0xFF3A33A3),
    onPrimaryContainer = Color(0xFFE6E3FF),
    inversePrimary = Color(0xFF4F46E5),

    secondary = Color(0xFF7EE0B8),
    onSecondary = Color(0xFF003824),
    secondaryContainer = Color(0xFF0B5A40),
    onSecondaryContainer = Color(0xFFA6F2D3),

    tertiary = Color(0xFFF5B83D),
    onTertiary = Color(0xFF3D2700),
    tertiaryContainer = Color(0xFF5C3F00),
    onTertiaryContainer = Color(0xFFFFE3A8),

    error = Color(0xFFFFB4AC),
    onError = Color(0xFF690008),
    errorContainer = Color(0xFF93000F),
    onErrorContainer = Color(0xFFFFDAD7),

    background = Color(0xFF0E0C24),
    onBackground = Color(0xFFECEAFF),
    surface = Color(0xFF0E0C24),
    onSurface = Color(0xFFECEAFF),
    surfaceVariant = Color(0xFF2B275B),
    onSurfaceVariant = Color(0xFFBDB9E0),

    outline = Color(0xFF8C88B3),
    outlineVariant = Color(0xFF39346E),
    scrim = Color(0xFF000000),

    surfaceContainerLowest = Color(0xFF090818),
    surfaceContainerLow = Color(0xFF151236),
    surfaceContainer = Color(0xFF1A1740),
    surfaceContainerHigh = Color(0xFF221E4D),
    surfaceContainerHighest = Color(0xFF2B275B),
    surfaceDim = Color(0xFF0E0C24),
    surfaceBright = Color(0xFF332E66),

    inverseSurface = Color(0xFFECEAFF),
    inverseOnSurface = Color(0xFF26215C),
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
    completed = Color(0xFF0E8F66),
    onCompleted = Color(0xFFFFFFFF),
    current = Color(0xFF4F46E5),
    onCurrent = Color(0xFFFFFFFF),
    upcoming = Color(0xFF79769A),
    attention = Color(0xFF8F5B00),
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
    current = Color(0xFFB9B3FF),
    onCurrent = Color(0xFF1E1868),
    upcoming = Color(0xFF8C88B3),
    attention = Color(0xFFF5B83D),
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
