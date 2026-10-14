package br.com.estudario.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------------------------
// Paleta base: "caderno vivo", a mesma do site. No claro, papel creme de caderno (pontilhado ao
// fundo, em EstudarioApp) com cartões quase brancos; no escuro, o caderno fica azul-noite índigo, a
// cor em que a turma do Folha foi desenhada. Dourado do capelo e verde-menta como destaques.
// Mudou aqui, muda em web/src/jsMain/resources/styles.css (bloco CADERNO VIVO).
// ---------------------------------------------------------------------------------------------

val EstudarioLightColorScheme = lightColorScheme(
    primary = Color(0xFF5B4BF5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE7E3FF),
    onPrimaryContainer = Color(0xFF241A8F),
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

    background = Color(0xFFF7F3E9),
    onBackground = Color(0xFF26234A),
    surface = Color(0xFFF7F3E9),
    onSurface = Color(0xFF26234A),
    surfaceVariant = Color(0xFFE9E3D3),
    onSurfaceVariant = Color(0xFF4E4B6B),

    outline = Color(0xFF7D7A93),
    outlineVariant = Color(0xFFCFC5B0),
    scrim = Color(0xFF0B0A1F),

    surfaceContainerLowest = Color(0xFFFFFDF8),
    surfaceContainerLow = Color(0xFFF1ECDF),
    surfaceContainer = Color(0xFFECE6D8),
    surfaceContainerHigh = Color(0xFFE9E3D3),
    surfaceContainerHighest = Color(0xFFE4DCCB),
    surfaceDim = Color(0xFFDDD5C3),
    surfaceBright = Color(0xFFF7F3E9),

    inverseSurface = Color(0xFF26215C),
    inverseOnSurface = Color(0xFFF1EFFF),
)

val EstudarioDarkColorScheme = darkColorScheme(
    primary = Color(0xFFABA2FF),
    onPrimary = Color(0xFF1D1670),
    primaryContainer = Color(0xFF2F2A78),
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

    background = Color(0xFF110F26),
    onBackground = Color(0xFFEDEBFF),
    surface = Color(0xFF110F26),
    onSurface = Color(0xFFEDEBFF),
    surfaceVariant = Color(0xFF27235A),
    onSurfaceVariant = Color(0xFFC3BFE6),

    outline = Color(0xFF8F8AB8),
    outlineVariant = Color(0xFF3D3878),
    scrim = Color(0xFF000000),

    surfaceContainerLowest = Color(0xFF0B0A1C),
    surfaceContainerLow = Color(0xFF17143A),
    surfaceContainer = Color(0xFF1B1838),
    surfaceContainerHigh = Color(0xFF27235A),
    surfaceContainerHighest = Color(0xFF2F2A68),
    surfaceDim = Color(0xFF110F26),
    surfaceBright = Color(0xFF37317A),

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
