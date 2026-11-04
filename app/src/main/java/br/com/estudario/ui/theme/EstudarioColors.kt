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
    // "Caderno de projeto" (11/2026): papel milimetrado creme e tinta azul de caneta, como o site.
    primary = Color(0xFF2F5BD3),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE6FB),
    onPrimaryContainer = Color(0xFF14204A),
    inversePrimary = Color(0xFFA9C3FF),

    secondary = Color(0xFF1F8A55),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFEFDB),
    onSecondaryContainer = Color(0xFF07301B),

    tertiary = Color(0xFF9A6200),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFF0B0),
    onTertiaryContainer = Color(0xFF2A2100),

    error = Color(0xFFC2302F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFD9D6),
    onErrorContainer = Color(0xFF5C0A07),

    background = Color(0xFFF8F5EC),
    onBackground = Color(0xFF1C1E25),
    surface = Color(0xFFF8F5EC),
    onSurface = Color(0xFF1C1E25),
    surfaceVariant = Color(0xFFE9E3D2),
    onSurfaceVariant = Color(0xFF464A57),

    outline = Color(0xFF6A6F7D),
    outlineVariant = Color(0xFF1C1E25),
    scrim = Color(0xFF0B0E16),

    surfaceContainerLowest = Color(0xFFFFFDF8),
    surfaceContainerLow = Color(0xFFF2EDE0),
    surfaceContainer = Color(0xFFEFE9DA),
    surfaceContainerHigh = Color(0xFFE9E3D2),
    surfaceContainerHighest = Color(0xFFE2DBC9),
    surfaceDim = Color(0xFFDDD5C3),
    surfaceBright = Color(0xFFF8F5EC),

    inverseSurface = Color(0xFF14223F),
    inverseOnSurface = Color(0xFFEAF1FF),
)

val EstudarioDarkColorScheme = darkColorScheme(
    // No escuro, papel de planta azul de engenheiro com tinta clara.
    primary = Color(0xFF8FB0FF),
    onPrimary = Color(0xFF071028),
    primaryContainer = Color(0xFF233F7A),
    onPrimaryContainer = Color(0xFFE6EEFF),
    inversePrimary = Color(0xFF2F5BD3),

    secondary = Color(0xFF6FE3A6),
    onSecondary = Color(0xFF05301B),
    secondaryContainer = Color(0xFF1A4636),
    onSecondaryContainer = Color(0xFFBFF3D6),

    tertiary = Color(0xFFFFD978),
    onTertiary = Color(0xFF2A2100),
    tertiaryContainer = Color(0xFF4D4320),
    onTertiaryContainer = Color(0xFFFFF0B0),

    error = Color(0xFFFF9A9E),
    onError = Color(0xFF5C0A07),
    errorContainer = Color(0xFF7A1A1F),
    onErrorContainer = Color(0xFFFFD9D6),

    background = Color(0xFF0E1A33),
    onBackground = Color(0xFFEAF1FF),
    surface = Color(0xFF0E1A33),
    onSurface = Color(0xFFEAF1FF),
    surfaceVariant = Color(0xFF1D2F54),
    onSurfaceVariant = Color(0xFFB6C5E3),

    outline = Color(0xFF8DA0C6),
    outlineVariant = Color(0xFF05091A),
    scrim = Color(0xFF000000),

    surfaceContainerLowest = Color(0xFF0B1529),
    surfaceContainerLow = Color(0xFF14223F),
    surfaceContainer = Color(0xFF172745),
    surfaceContainerHigh = Color(0xFF1B2B4D),
    surfaceContainerHighest = Color(0xFF1D2F54),
    surfaceDim = Color(0xFF0E1A33),
    surfaceBright = Color(0xFF23365E),

    inverseSurface = Color(0xFFEAF1FF),
    inverseOnSurface = Color(0xFF14223F),
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
    current = Color(0xFF2F5BD3),
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
    current = Color(0xFF8FB0FF),
    onCurrent = Color(0xFF071028),
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
