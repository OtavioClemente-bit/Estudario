package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.theme.estudarioColors

/**
 * A linguagem visual de matérias do Estudário: cada matéria recebe sempre a mesma cor da paleta
 * controlada do design system (nunca uma cor aleatória), o índice vem de um hash estável do nome,
 * então "Direito Constitucional" é sempre a mesma cor em qualquer tela, sem precisar guardar nada
 * no banco.
 */
fun subjectAccentColor(subjectName: String, palette: List<Color>): Color {
    if (palette.isEmpty()) return Color.Gray
    val key = subjectName.trim().lowercase()
    val hash = key.fold(7) { acc, char -> acc * 31 + char.code }
    val index = ((hash % palette.size) + palette.size) % palette.size
    return palette[index]
}

/** Ponto compacto, usado em listas e metadados onde só a identificação por cor importa. */
@Composable
fun SubjectDot(subjectName: String, modifier: Modifier = Modifier, size: Dp = 8.dp) {
    val palette = estudarioColors().subjectPalette
    val color = remember(subjectName, palette) { subjectAccentColor(subjectName, palette) }
    Box(modifier.size(size).clip(CircleShape).background(color))
}

/** Traço vertical, a assinatura de matéria usada ao lado de títulos (painel "Agora", trilha do dia). */
@Composable
fun SubjectBar(subjectName: String, modifier: Modifier = Modifier, height: Dp = 36.dp) {
    val palette = estudarioColors().subjectPalette
    val color = remember(subjectName, palette) { subjectAccentColor(subjectName, palette) }
    Box(modifier.width(4.dp).height(height).clip(RoundedCornerShape(50)).background(color))
}
