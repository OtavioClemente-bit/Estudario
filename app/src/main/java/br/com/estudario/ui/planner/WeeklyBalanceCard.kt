package br.com.estudario.ui.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.domain.planner.EditalRoadmapResult
import br.com.estudario.domain.planner.RoadmapVerdict
import br.com.estudario.domain.planner.WeeklyBalance
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.Surface
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

/**
 * "Sua semana": o balanço dos últimos 7 dias, com os números que mostram o avanço (horas, dias, tópicos
 * novos, acerto e a tendência) e a previsão de quando o edital fecha no ritmo atual.
 */
@Composable
fun WeeklyBalanceCard(balance: WeeklyBalance, roadmap: EditalRoadmapResult?) {
    val tone = if (balance.idle) Color(0xFFE08A00) else Color(0xFF2EA56F)
    val day = DateTimeFormatter.ofPattern("dd/MM")
    Surface(shape = RoundedCornerShape(24.dp), color = tone.copy(alpha = 0.10f), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Sua semana", style = MaterialTheme.typography.labelLarge, color = tone, fontWeight = FontWeight.Bold)
            Text(balance.headline(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            // Os sete dias, um ponto por dia estudado.
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Stat("${WeeklyBalance.hours(balance.minutesStudied)}", "estudadas", Modifier.weight(1f))
                Stat("${balance.daysStudied}/7", "dias", Modifier.weight(1f))
                Stat("${balance.newTopics}", if (balance.newTopics == 1) "tópico novo" else "tópicos novos", Modifier.weight(1f))
                balance.accuracy?.let { Stat("$it%", "acerto", Modifier.weight(1f)) }
            }
            balance.details().forEach { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            LinearProgressIndicator(progress = { balance.seenPercent / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = tone)
            roadmap?.let { r ->
                val text = when {
                    r.verdict != RoadmapVerdict.DOES_NOT_FIT && r.coverageDate != null ->
                        "No seu ritmo, o edital fecha em ${r.coverageDate!!.toJavaLocalDate().format(day)}" +
                            (if (r.slackDays > 0) ", ${r.slackDays} dias antes da reta final. Você vai conseguir." else ", colado na reta final.")
                    r.verdict == RoadmapVerdict.DOES_NOT_FIT -> "No ritmo de hoje ficam ${r.topicsLeftOut} tópicos de fora; veja abaixo como resolver."
                    else -> null
                }
                text?.let { Text(it, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold) }
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), modifier = modifier) {
        Column(Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}
