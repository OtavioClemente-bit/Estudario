package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.planner.PersonalDifficulty

/**
 * Quanto um assunto pede de atenção: o peso dele na prova somado à dificuldade que a pessoa disse
 * ter. É a mesma conta que o plano usa para dar mais tempo, mostrada em cor no edital.
 */
enum class Attention(val label: String, val short: String) {
    HIGH("Atenção alta", "alta"),
    MEDIUM("Atenção média", "média"),
    LOW("Atenção leve", "leve"),
    ;

    companion object {
        fun of(priority: PriorityLevel, difficulty: PersonalDifficulty? = null): Attention {
            val weight = when (priority) {
                PriorityLevel.VERY_HIGH -> 2.0
                PriorityLevel.HIGH -> 1.5
                PriorityLevel.MEDIUM -> 1.0
                PriorityLevel.LOW -> 0.5
                PriorityLevel.VERY_LOW -> 0.0
            }
            // Dificuldade empurra meio nível para cima ou para baixo.
            val score = weight + ((difficulty ?: PersonalDifficulty.NORMAL).normalized - 0.5) * 2
            return when {
                score >= 1.5 -> HIGH
                score >= 0.75 -> MEDIUM
                else -> LOW
            }
        }
    }
}

/** Cores fixas da escala (as mesmas no tema claro e no escuro, legíveis nos dois). */
@Composable
fun Attention.color(): Color = when (this) {
    Attention.HIGH -> Color(0xFFE5484D)
    Attention.MEDIUM -> Color(0xFFF2A900)
    Attention.LOW -> Color(0xFF2FA36B)
}

@Composable
fun AttentionDot(attention: Attention, size: Int = 8) {
    Box(Modifier.size(size.dp).background(attention.color(), CircleShape))
}

/** Legenda curta da escala, para a cor não precisar de explicação em outro lugar. */
@Composable
fun AttentionLegend(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Attention.entries.forEach { attention ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                AttentionDot(attention)
                Spacer(Modifier.width(5.dp))
                Text(attention.short.replaceFirstChar(Char::uppercase), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("· prova + sua dificuldade", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
