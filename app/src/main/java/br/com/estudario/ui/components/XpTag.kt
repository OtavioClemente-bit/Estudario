package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.domain.ProgressEngine

/**
 * Selo de recompensa. Aparece antes de a pessoa fazer a coisa ("o que eu ganho com isso") e
 * depois, quando a tarefa já foi concluída ("o que eu ganhei"). O número vem direto das regras de
 * XP, então nunca promete diferente do que paga.
 */
@Composable
fun XpTag(
    reward: ProgressEngine.XpReward,
    modifier: Modifier = Modifier,
    earned: Boolean = false,
    showBonus: Boolean = true,
) {
    val cor = if (earned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
    Row(
        modifier
            .clip(RoundedCornerShape(50))
            .background(cor.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Icon(Icons.Rounded.Bolt, null, Modifier.size(13.dp), tint = cor)
        Text(
            if (earned) "${reward.base} XP" else "+${reward.base} XP",
            color = cor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
        )
        if (showBonus && reward.hasBonus && !earned) {
            Text("a +${reward.max}", color = cor.copy(alpha = 0.72f), fontSize = 11.sp)
        }
    }
}

/** Versão em uma linha, para listas e descrições. */
@Composable
fun XpLine(reward: ProgressEngine.XpReward, prefix: String = "Recompensa", modifier: Modifier = Modifier) {
    val cor = MaterialTheme.colorScheme.tertiary
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(Icons.Rounded.Bolt, null, Modifier.size(14.dp), tint = cor)
        Text(
            buildString {
                append(prefix).append(": +").append(reward.base).append(" XP")
                if (reward.hasBonus) {
                    append(" (até ").append(reward.max)
                    reward.bonusLabel?.let { append(", ").append(it) }
                    append(")")
                }
            },
            style = MaterialTheme.typography.labelMedium,
            color = cor,
        )
    }
}
