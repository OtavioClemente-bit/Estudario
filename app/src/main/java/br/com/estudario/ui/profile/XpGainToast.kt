package br.com.estudario.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.AppViewModel
import kotlinx.coroutines.delay

private val Violet = Color(0xFF5B3FD6)
private val Emerald = Color(0xFF0E9F7A)

/**
 * "+N XP" que sobe e some no topo da tela, toda vez que a pessoa ganha XP fora da comemoração.
 */
@Composable
fun XpGainToast(gain: AppViewModel.XpGain?) {
    var visible by remember { mutableStateOf<AppViewModel.XpGain?>(null) }
    LaunchedEffect(gain?.id) {
        if (gain == null) return@LaunchedEffect
        visible = gain
        delay(1_800)
        visible = null
    }
    Box(Modifier.fillMaxSize().statusBarsPadding().padding(top = 64.dp), contentAlignment = Alignment.TopCenter) {
        AnimatedVisibility(
            visible = visible != null,
            enter = slideInVertically(tween(380, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(500)) { -it } + fadeOut(tween(500)),
        ) {
            val amount = visible?.amount ?: gain?.amount ?: 0
            Row(
                Modifier.clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("+$amount", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Spacer(Modifier.size(6.dp))
                Text("XP", color = Color(0xFFE6DCFF), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
