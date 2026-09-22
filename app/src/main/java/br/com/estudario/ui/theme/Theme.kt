package br.com.estudario.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4E1FF),
    secondary = Color(0xFF087F5B),
    tertiary = Color(0xFF9A4D00),
    background = Color(0xFFF8F9FC),
    surface = Color(0xFFF8F9FC),
    surfaceVariant = Color(0xFFEDEEF4),
    error = Color(0xFFBA1A1A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFC5C0FF),
    onPrimary = Color(0xFF25206D),
    primaryContainer = Color(0xFF39358A),
    secondary = Color(0xFF70DBB2),
    tertiary = Color(0xFFFFB77D),
    background = Color(0xFF111318),
    surface = Color(0xFF111318),
    surfaceVariant = Color(0xFF2B2D34),
    error = Color(0xFFFFB4AB),
)

@Composable
fun EstudarioTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content,
    )
}

data class EstudarioExtendedColors(
    val completed: Color,
    val onCompleted: Color,
    val current: Color,
    val onCurrent: Color,
    val upcoming: Color,
    val attention: Color,
    val subjectPalette: List<Color>,
)

private val LightExtendedColors = EstudarioExtendedColors(
    completed = Color(0xFF087F5B), onCompleted = Color.White,
    current = Color(0xFF4F46E5), onCurrent = Color.White,
    upcoming = Color(0xFF6B6B70), attention = Color(0xFF9A4D00),
    subjectPalette = listOf(Color(0xFF4F46E5), Color(0xFF087F5B), Color(0xFF9A4D00), Color(0xFF1D6FA5), Color(0xFFA23E6B)),
)
private val LocalEstudarioColors = staticCompositionLocalOf { LightExtendedColors }

@Composable
fun estudarioColors(): EstudarioExtendedColors = LocalEstudarioColors.current
