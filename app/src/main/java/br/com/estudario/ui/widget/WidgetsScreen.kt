package br.com.estudario.ui.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.R
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.cutPanel

/** Um widget do app: o que ele mostra, a foto dele e onde ele mora no Android. */
private data class WidgetOption(
    val title: String,
    val size: String,
    val description: String,
    val receiver: Class<*>,
    val glyph: Glyph,
    val preview: Int,
)

/**
 * Widgets: cada um com um papel diferente, a foto real de como ele fica (tirada do próprio widget) e
 * o botão que pede ao Android para colocá-lo na tela inicial. Cartões no mesmo recorte de adesivo do app.
 */
@Composable
fun WidgetsScreen() {
    val context = LocalContext.current
    val g = br.com.estudario.ui.profile.Game
    val groups = listOf(
        "Seu plano" to listOf(
            WidgetOption("Plano do dia", "4 × 3", "A sequência de hoje. Toque num passo e ele abre ali mesmo, com Abrir e Focar.", DayPlanWidgetReceiver::class.java, Glyph.Calendar, R.drawable.widget_preview_day),
            WidgetOption("Semana de estudos", "4 × 3", "Os próximos 7 dias. Toque num dia e os estudos dele aparecem no próprio widget.", WeekWidgetReceiver::class.java, Glyph.Calendar, R.drawable.widget_preview_week),
            WidgetOption("Revisões de hoje", "4 × 2", "O que vence hoje e o que atrasou. Toque numa revisão e ela começa.", ReviewsWidgetReceiver::class.java, Glyph.Cycle, R.drawable.widget_preview_reviews),
        ),
        "Praticar" to listOf(
            WidgetOption("Questão do dia", "4 × 3", "Responda na tela inicial, veja se acertou e peça outra. Conta no seu Desempenho.", QuestionWidgetReceiver::class.java, Glyph.Bulb, R.drawable.widget_preview_question),
            WidgetOption("Foco", "3 × 2", "Comece, pause e retome o foco sem abrir o app, já na próxima tarefa do plano.", FocusWidgetReceiver::class.java, Glyph.Timer, R.drawable.widget_preview_focus),
        ),
        "Motivação" to listOf(
            WidgetOption("Próxima missão", "3 × 2", "O Folha com a fala do dia e o próximo estudo. Toque nele e ele responde.", EstudarioWidgetReceiver::class.java, Glyph.Wave, R.drawable.widget_preview_next),
            WidgetOption("Constância", "4 × 2", "Suas últimas 15 semanas: quanto mais verde, mais você estudou naquele dia.", StreakWidgetReceiver::class.java, Glyph.Fire, R.drawable.widget_preview_streak),
            WidgetOption("Contagem para a prova", "2 × 2", "Dias para a prova, quanto do edital você já viu e sua sequência.", CountdownWidgetReceiver::class.java, Glyph.Target, R.drawable.widget_preview_countdown),
        ),
    )
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BrandIcon(Glyph.Widgets, size = 44.dp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Widgets", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = g.ink)
                    Text("Seu estudo na tela inicial do celular, sem abrir o app.", style = MaterialTheme.typography.bodyMedium, color = g.muted)
                }
            }
        }
        groups.forEach { (group, options) ->
            item { Text(group.uppercase(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = g.muted, letterSpacing = 1.6.sp, modifier = Modifier.padding(top = 6.dp)) }
            options.forEach { option -> item(key = option.title) { WidgetCard(context, option) } }
        }
        item {
            Text(
                "Não apareceu o pedido? Segure um espaço vazio da tela inicial, toque em Widgets e procure por Estudário.",
                style = MaterialTheme.typography.bodySmall, color = g.muted,
            )
        }
    }
}

@Composable
private fun WidgetCard(context: Context, option: WidgetOption) {
    val g = br.com.estudario.ui.profile.Game
    val shape = RoundedCornerShape(24.dp)
    Column(
        Modifier.fillMaxWidth().cutPanel(shape).clip(shape).background(g.bg2).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandIcon(option.glyph, size = 36.dp)
            Spacer(Modifier.width(10.dp))
            Text(option.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = g.ink, modifier = Modifier.weight(1f))
            Text(option.size, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = g.muted)
        }
        // A foto real do widget, como ele aparece na tela inicial.
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painterResource(option.preview), contentDescription = "Como fica o widget ${option.title}",
                contentScale = ContentScale.Fit, modifier = Modifier.widthIn(max = 340.dp).heightIn(max = 300.dp),
            )
        }
        Text(option.description, style = MaterialTheme.typography.bodyMedium, color = g.ink)
        Button(kind = br.com.estudario.ui.brand.NoteKind.Primary, glyph = br.com.estudario.ui.brand.Glyph.Widgets, onClick = { pin(context, option.receiver) }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar à tela inicial") }
    }
}

private fun pin(context: Context, receiver: Class<*>) {
    val manager = AppWidgetManager.getInstance(context)
    val ok = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && manager.isRequestPinAppWidgetSupported &&
        runCatching { manager.requestPinAppWidget(ComponentName(context, receiver), null, null) }.getOrDefault(false)
    if (!ok) android.widget.Toast.makeText(
        context,
        "Seu celular não deixa adicionar por aqui. Segure um espaço vazio da tela inicial, toque em Widgets e procure por Estudário.",
        android.widget.Toast.LENGTH_LONG,
    ).show()
}
