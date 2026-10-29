package br.com.estudario.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.EstudarioApplication
import br.com.estudario.ui.brand.Icon

private val Ok = Color(0xFF2FD39A)
private val OkDark = Color(0xFF1FA97A)
private val No = Color(0xFFFF5A7A)
private val NoDark = Color(0xFFE0476A)
private val Now = Color(0xFFFFE07A)

/**
 * O placar de questões, igual em todas as telas que fazem questões (Treinar, questões do conteúdo,
 * revisão espaçada, missão do caderno de erros e folheada do caderno de estudo), o mesmo do site: a barra
 * de XP de jogo, uma casa por questão que fica verde (certa) ou vermelha (errada), a atual piscando, o
 * "3/10" e o XP subindo a cada resposta; em cima, o relógio e as certas/erradas, que a pessoa esconde em
 * Mais > Questões. O que vem depois de responder (virada, lembrei, explicação) continua sendo de cada tela.
 */
@Composable
fun QuestionHud(
    index: Int, total: Int, results: List<Boolean>, startedAt: Long, modifier: Modifier = Modifier,
    title: String? = null, exitLabel: String = "Sair", onExit: (() -> Unit)? = null,
) {
    val prefs = (LocalContext.current.applicationContext as EstudarioApplication).preferences
    val showTimer by prefs.hudTimer.collectAsState(initial = true)
    val showScore by prefs.hudScore.collectAsState(initial = true)
    var elapsed by remember(startedAt) { mutableLongStateOf((System.currentTimeMillis() - startedAt) / 1_000) }
    if (showTimer) LaunchedEffect(startedAt) { while (true) { elapsed = (System.currentTimeMillis() - startedAt) / 1_000; kotlinx.coroutines.delay(1_000) } }
    val right = results.count { it }
    val wrong = results.size - right
    val xp = results.size * 5 + right * 5
    val scheme = MaterialTheme.colorScheme
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onExit != null) TextButton(onClick = onExit) { Icon(Icons.Outlined.Close, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text(exitLabel) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (index >= total) "Pronto" else "Questão ${index + 1} de $total", fontWeight = FontWeight.Black, fontSize = 15.sp, textAlign = TextAlign.Center)
                if (title != null) Text(title, style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                if (showTimer) HudChip(scheme.surfaceVariant, scheme.onSurface) { Icon(Icons.Outlined.Timer, "Tempo desta sessão", Modifier.size(14.dp), tint = scheme.onSurface); Text("%02d:%02d".format(elapsed / 60, elapsed % 60), fontWeight = FontWeight.Black, fontSize = 12.sp) }
                if (showScore) {
                    key("ok", right) { Bump { HudChip(Ok.copy(alpha = 0.16f), OkDark) { Icon(Icons.Outlined.Check, "Certas", Modifier.size(14.dp), tint = OkDark); Text("$right", fontWeight = FontWeight.Black, fontSize = 12.sp, color = OkDark) } } }
                    key("no", wrong) { Bump { HudChip(No.copy(alpha = 0.14f), NoDark) { Icon(Icons.Outlined.Close, "Erradas", Modifier.size(14.dp), tint = NoDark); Text("$wrong", fontWeight = FontWeight.Black, fontSize = 12.sp, color = NoDark) } } }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // a barra: a parte cheia corre por baixo das casas, com o brilho de barra de XP
            val fill by animateFloatAsState(if (total == 0) 0f else results.size / total.toFloat(), tween(550), label = "hud-fill")
            val blink by rememberInfiniteTransition(label = "hud-now").animateFloat(0.45f, 1f, infiniteRepeatable(tween(550), RepeatMode.Reverse), label = "hud-now-a")
            Box(Modifier.weight(1f).height(18.dp).clip(RoundedCornerShape(50)).background(scheme.surfaceVariant)) {
                Box(Modifier.fillMaxWidth(fill).fillMaxHeight().clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFF6158F0), Color(0xFF7EE0B8), Now))).background(Color.White.copy(alpha = 0.55f)))
                Row(Modifier.fillMaxWidth().fillMaxHeight().padding(3.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(total) { i ->
                        val cell = when {
                            i < results.size -> if (results[i]) Brush.verticalGradient(listOf(Color(0xFF5BE6B4), OkDark)) else Brush.verticalGradient(listOf(Color(0xFFFF8FA3), NoDark))
                            i == index -> Brush.verticalGradient(listOf(Now.copy(alpha = blink), Now.copy(alpha = blink * 0.8f)))
                            else -> Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.18f), Color.White.copy(alpha = 0.12f)))
                        }
                        val pop by animateFloatAsState(if (i < results.size) 1f else 0.85f, spring(dampingRatio = 0.4f, stiffness = 500f), label = "hud-cell")
                        Box(Modifier.weight(1f).fillMaxHeight().scale(1f, pop).clip(RoundedCornerShape(50)).background(cell))
                    }
                }
            }
            Text(buildString { append(results.size); append('/'); append(total) }, fontWeight = FontWeight.Black, fontSize = 13.sp)
            key("xp", xp) {
                Bump {
                    Row(
                        Modifier.clip(RoundedCornerShape(50)).background(Brush.linearGradient(listOf(Color(0xFFFFE59A), Color(0xFFF5B83D)))).padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Outlined.Bolt, null, Modifier.size(13.dp), tint = Color(0xFF5A3803))
                        Text("$xp XP", fontWeight = FontWeight.Black, fontSize = 11.sp, color = Color(0xFF5A3803))
                    }
                }
            }
        }
    }
}

@Composable
private fun HudChip(bg: Color, line: Color, content: @Composable () -> Unit) {
    Row(
        Modifier.height(28.dp).clip(RoundedCornerShape(50)).background(bg).border(1.dp, line.copy(alpha = 0.3f), RoundedCornerShape(50)).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) { content() }
}

/** Um salto quando o valor muda (quem chama troca a chave e o salto roda de novo). */
@Composable
private fun Bump(content: @Composable () -> Unit) {
    var started by remember { androidx.compose.runtime.mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val s by animateFloatAsState(if (started) 1f else 1.25f, spring(dampingRatio = 0.35f, stiffness = 600f), label = "hud-bump")
    Box(Modifier.scale(s)) { content() }
}
