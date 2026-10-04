package br.com.estudario.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.ElevatedCard
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.OutlinedButton

/**
 * Pergunta de memorização, como uma carta: na frente a pergunta, a pessoa tenta lembrar e vira.
 * Atrás está a resposta e ela diz, com honestidade, se lembrou. Sem resposta cadastrada (pacotes
 * antigos), o verso orienta a conferir na teoria.
 */
@Composable
fun RecallCard(
    question: String,
    answer: String?,
    modifier: Modifier = Modifier,
    revealKey: Any = question,
    trailing: @Composable (() -> Unit)? = null,
) {
    var flipped by rememberSaveable(revealKey) { mutableStateOf(false) }
    // 0 = ainda não respondeu, 1 = lembrou, 2 = não lembrou.
    var verdict by rememberSaveable(revealKey) { mutableIntStateOf(0) }
    val turn by animateFloatAsState(if (flipped) 180f else 0f, tween(420), label = "recall-flip")
    val density = LocalDensity.current.density
    val showBack = turn > 90f

    ElevatedCard(
        modifier.fillMaxWidth().graphicsLayer { rotationY = turn; cameraDistance = 14f * density },
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (showBack) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Box {
            // Os dois lados ocupam o mesmo lugar: a carta tem a altura do maior e não pula ao virar.
            Column(
                Modifier.alpha(if (showBack) 0f else 1f).padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    BrandIcon(Glyph.Brain, size = 40.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("TENTE LEMBRAR", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                        StudyInlineText(question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    trailing?.invoke()
                }
                Button(onClick = { flipped = true }, enabled = !showBack, modifier = Modifier.fillMaxWidth().padding(end = 8.dp)) { Text("Virar a carta") }
            }
            Column(
                Modifier
                    .alpha(if (showBack) 1f else 0f)
                    .graphicsLayer { rotationY = 180f }
                    .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandIcon(if (verdict == 1) Glyph.Check else Glyph.Bulb, size = 32.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("RESPOSTA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (answer.isNullOrBlank()) {
                    Text("Este material não trouxe a resposta. Confira na teoria do tópico.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    MarkdownText(answer)
                }
                when (verdict) {
                    0 -> Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = { verdict = 2 }, enabled = showBack, modifier = Modifier.weight(1f)) { Text("Não lembrei") }
                        Button(onClick = { verdict = 1 }, enabled = showBack, modifier = Modifier.weight(1f)) { Text("Lembrei") }
                    }
                    1 -> Text("Boa! Essa já está na cabeça.", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    else -> Text("Sem problema. Leia de novo com calma e tente de novo mais tarde.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = { flipped = false; verdict = 0 }, enabled = showBack) { Text("Virar de volta") }
            }
        }
    }
}
