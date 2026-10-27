package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionOptionEntity

private val OkMint = Color(0xFF2FBF8A)
private val NoRed = Color(0xFFE5484D)

/**
 * Alternativas no padrão da revisão: tocar só escolhe (nada é gravado), o botão ao lado risca,
 * e a resposta vale quando a pessoa aperta [ConfirmAnswerButton]. Depois de responder mostra
 * certa/errada e, se [previousKey] vier, avisa qual tinha sido marcada da última vez.
 */
@Composable
fun AnswerOptions(
    options: List<QuestionOptionEntity>,
    picked: String?,
    crossed: Set<String>,
    answered: Boolean?,
    onPick: (String) -> Unit,
    onToggleCross: (String) -> Unit,
    previousKey: String? = null,
    surface: Color = MaterialTheme.colorScheme.surface,
    ink: Color = MaterialTheme.colorScheme.onSurface,
    line: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    val scheme = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.sortedBy { it.position }.forEachIndexed { pos, option ->
            val isCrossed = option.key in crossed && answered == null
            val tint = when {
                answered != null && option.isCorrect -> OkMint
                answered == false && option.key == picked -> NoRed
                answered == null && option.key == picked -> scheme.primary
                else -> null
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(surface)
                        .background(tint?.copy(alpha = 0.14f) ?: Color.Transparent)
                        .border(2.dp, tint ?: line, RoundedCornerShape(16.dp))
                        .graphicsLayer { alpha = if (isCrossed) 0.45f else 1f }
                        .clickable(enabled = answered == null) { if (isCrossed) onToggleCross(option.key) else onPick(option.key) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(30.dp).clip(CircleShape).background(tint ?: scheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        Text(
                            when { answered != null && option.isCorrect -> "✓"; answered == false && option.key == picked -> "✕"; else -> "ABCDEF"[pos].toString() },
                            fontWeight = FontWeight.Black, color = if (tint != null) Color.White else scheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        StudyInlineText(
                            option.text, Modifier.fillMaxWidth(),
                            onTap = { if (answered == null) { if (isCrossed) onToggleCross(option.key) else onPick(option.key) } },
                            style = MaterialTheme.typography.bodyLarge.copy(color = ink, textDecoration = if (isCrossed) TextDecoration.LineThrough else null),
                        )
                        if (answered != null && previousKey == option.key) {
                            Text("você tinha marcado esta da última vez", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                        }
                    }
                }
                if (answered == null) IconButton(
                    onClick = { onToggleCross(option.key) },
                    modifier = Modifier.size(40.dp).semantics { contentDescription = if (isCrossed) "Desfazer risco" else "Riscar alternativa" },
                ) {
                    Icon(Icons.Outlined.FormatStrikethrough, null, tint = if (isCrossed) scheme.primary else scheme.onSurfaceVariant.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
fun ConfirmAnswerButton(enabled: Boolean, onConfirm: () -> Unit) {
    Button(enabled = enabled, onClick = onConfirm, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Confirmar", fontWeight = FontWeight.Bold) }
}
