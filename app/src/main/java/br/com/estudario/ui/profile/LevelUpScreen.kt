package br.com.estudario.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button

/**
 * A subida de nível (a mesma cena do site): orbes de XP enchem o anel, o emblema antigo racha e
 * explode, o número novo cai em ouro num brasão que abre asas, e o Folha dá um pulão. Depois entram o
 * título, o selo de título novo e a barra de XP.
 */
@Composable
fun LevelUpScreen(event: AppViewModel.LevelUp, onClose: () -> Unit) {
    SceneCelebration(
        scene = listOf("tipo" to "nivel", "nivel" to event.level.toString()),
        reveal = 2.5f,
        onDismiss = onClose,
        text = {
            CelEyebrow("SUBIU DE NÍVEL")
            CelTitle("Nível ${event.level} · ${event.title}")
            if (event.newTitle) CelChip("Novo título!", background = CelGold, color = CelMid)
            Box(Modifier.width(280.dp).height(26.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Box(Modifier.fillMaxSize().clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFF5B4BF5), CelGold))))
                Text("${event.totalXp} XP", color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
        },
        actions = {
            Button(onClick = onClose, modifier = Modifier.widthIn(min = 200.dp).heightIn(min = 52.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CelGold, contentColor = CelMid)) { Text("Continuar", fontWeight = FontWeight.Black) }
        },
    )
}
