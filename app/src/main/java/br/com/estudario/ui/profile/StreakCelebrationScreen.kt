package br.com.estudario.ui.profile

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.brand.Button

/**
 * A sequência do dia (a mesma cena do site): o varal de lanternas da semana, com os dias feitos
 * acesos; o Folha lança uma faísca, acende a lanterna de hoje e uma chama grande cresce atrás dele
 * com o número (o de ontem sobe e o de hoje entra). Uma vez por dia, ao fechar a meta.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    val letters = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val week = summary.week.filter { !it.future }.takeLast(7)
        .joinToString(",") { letters[it.date.dayOfWeek.ordinal] + if (it.done) "1" else "0" }
    SceneCelebration(
        scene = listOf("tipo" to "sequencia", "dias" to summary.current.coerceAtLeast(1).toString(), "semana" to week),
        reveal = 2.7f,
        onDismiss = onClose,
        text = {
            CelEyebrow(if (summary.current == 1) "1 DIA DE SEQUÊNCIA" else "${summary.current} DIAS DE SEQUÊNCIA")
            CelTitle(
                when {
                    summary.current <= 1 -> "A chama acendeu"
                    summary.current >= summary.best -> "Recorde! Ninguém te para."
                    else -> "Mais um dia aceso"
                },
            )
            CelBody(
                when {
                    summary.current <= 1 -> "Volte amanhã para manter o fogo aceso."
                    summary.current >= summary.best -> "Sua maior sequência até agora. Amanhã tem mais."
                    else -> "Faltam ${summary.best - summary.current} para o seu recorde de ${summary.best} dias."
                },
            )
            if (celebration.xpToday > 0) CelChip("+${celebration.xpToday} XP hoje")
        },
        actions = {
            Button(onClick = onClose, modifier = Modifier.widthIn(min = 200.dp).heightIn(min = 52.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = CelMid)) { Text("Bora continuar", fontWeight = FontWeight.Black) }
        },
    )
}
