package br.com.estudario.ui.profile

import androidx.compose.foundation.layout.PaddingValues
import br.com.estudario.ui.components.FitOrScrollColumn
import br.com.estudario.ui.theme.estudarioLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.domain.StreakDay
import br.com.estudario.domain.StreakSummary
import br.com.estudario.ui.components.AppMark
import br.com.estudario.ui.components.AppMarkBackground
import kotlinx.coroutines.delay

private val Verde = Color(0xFF20B486)

/**
 * Comemoração da sequência: aparece uma vez por dia, quando a pessoa fecha a meta. O livro do ícone
 * entra, o certo verde é carimbado em cima e o número da sequência sobe.
 */
@Composable
fun StreakCelebrationScreen(celebration: StreakCelebration, onClose: () -> Unit) {
    val summary = celebration.summary
    var stage by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { stage = 1; delay(380); stage = 2; delay(320); stage = 3 }

    val markScale by animateFloatAsState(if (stage >= 1) 1f else 0.55f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), label = "mark")
    val markAlpha by animateFloatAsState(if (stage >= 1) 1f else 0f, tween(300), label = "markAlpha")
    val checkScale by animateFloatAsState(if (stage >= 2) 1f else 0f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium), label = "check")
    val textAlpha by animateFloatAsState(if (stage >= 3) 1f else 0f, tween(400), label = "text")
    val streakValue by animateIntAsState(
        if (stage >= 3) summary.current else (summary.current - 1).coerceAtLeast(0),
        tween(650),
        label = "streak",
    )

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(AppMarkBackground, Color(0xFF0E2E63), AppMarkBackground))),
        ) {
            // Centralizado quando cabe; rola quando a tela é baixa ou a fonte é grande (antes o topo e
            // o fim eram cortados). O rodapé de 108dp deixa espaço para o botão fixo embaixo.
            FitOrScrollColumn(
                Modifier.fillMaxSize().systemBarsPadding(),
                contentPadding = PaddingValues(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 108.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // A "questão certa" em verde, em cima do livro.
                Row(
                    Modifier
                        .graphicsLayer { alpha = checkScale }
                        .clip(RoundedCornerShape(50))
                        .background(Verde.copy(alpha = 0.16f))
                        .border(1.dp, Verde.copy(alpha = 0.5f), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Rounded.Check, null, Modifier.size(18.dp), tint = Verde)
                    Text(celebration.reason, color = Verde, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                }

                Spacer(Modifier.height(if (estudarioLayout().isShortHeight) 20.dp else 36.dp))

                Box(contentAlignment = Alignment.BottomEnd) {
                    AppMark(
                        size = if (estudarioLayout().isShortHeight) 124.dp else 168.dp,
                        modifier = Modifier.graphicsLayer { scaleX = markScale; scaleY = markScale; alpha = markAlpha },
                    )
                    Box(
                        Modifier
                            .graphicsLayer { scaleX = checkScale; scaleY = checkScale }
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Verde)
                            .border(4.dp, AppMarkBackground, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Rounded.Check, null, Modifier.size(30.dp), tint = Color.White)
                    }
                }

                Spacer(Modifier.height(if (estudarioLayout().isShortHeight) 20.dp else 36.dp))

                Text(
                    "$streakValue",
                    fontSize = 68.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer { alpha = markAlpha },
                )
                Text(
                    if (summary.current == 1) "dia de sequência" else "dias de sequência",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.titleMedium,
                )

                if (celebration.xpToday > 0) {
                    Spacer(Modifier.height(14.dp))
                    Row(
                        Modifier
                            .graphicsLayer { alpha = textAlpha }
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text("+${celebration.xpToday} XP", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("hoje", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(Modifier.height(28.dp))

                Row(
                    Modifier.fillMaxWidth().graphicsLayer { alpha = textAlpha },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) { summary.week.forEach { day -> WeekDot(day) } }

                Spacer(Modifier.height(24.dp))

                Text(
                    if (summary.current >= summary.best && summary.current > 1) "Essa é a sua melhor sequência até agora. Continue amanhã."
                    else "Melhor sequência: ${summary.best} dia(s). Volte amanhã para não zerar.",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer { alpha = textAlpha },
                )
            }

            Button(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .systemBarsPadding()
                    .padding(28.dp)
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .graphicsLayer { alpha = textAlpha },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde, contentColor = Color.White),
            ) { Text("Continuar", fontWeight = FontWeight.Bold) }
        }
    }
}

private val DIAS = listOf("S", "T", "Q", "Q", "S", "S", "D")

@Composable
private fun WeekDot(day: StreakDay) {
    val index = day.date.dayOfWeek.value - 1
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(DIAS.getOrElse(index) { "" }, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        Box(
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    when {
                        day.done -> Verde
                        day.partial -> Verde.copy(alpha = 0.3f)
                        else -> Color.White.copy(alpha = 0.12f)
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (day.done) Icon(Icons.Rounded.Check, null, Modifier.size(17.dp), tint = Color.White)
        }
    }
}
