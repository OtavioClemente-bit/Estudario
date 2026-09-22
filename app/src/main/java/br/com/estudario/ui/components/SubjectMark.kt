package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.estudarioColors

fun subjectAccentColor(subjectName: String, palette: List<Color>): Color {
    if (palette.isEmpty()) return Color.Gray
    val hash = subjectName.trim().lowercase().fold(7) { acc, char -> acc * 31 + char.code }
    return palette[((hash % palette.size) + palette.size) % palette.size]
}

@Composable
fun SubjectDot(subjectName: String, modifier: Modifier = Modifier, size: Dp = 8.dp) {
    val palette = estudarioColors().subjectPalette
    val color = remember(subjectName, palette) { subjectAccentColor(subjectName, palette) }
    Box(modifier.size(size).clip(CircleShape).background(color))
}

@Composable
fun SubjectBar(subjectName: String, modifier: Modifier = Modifier, height: Dp = 36.dp) {
    val palette = estudarioColors().subjectPalette
    val color = remember(subjectName, palette) { subjectAccentColor(subjectName, palette) }
    Box(modifier.width(4.dp).height(height).clip(RoundedCornerShape(50)).background(color))
}
