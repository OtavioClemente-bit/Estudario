package br.com.estudario.ui.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Vitrine de debug: o Folha grande pensando e cada humor em tamanho de uso, sem precisar de login. */
@Composable
fun FolhaShowcase() {
    Column(
        Modifier.fillMaxSize().background(Color(0xFFEEEDFB)).verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pensando", fontSize = 16.sp, color = Color(0xFF26215C))
        Folha(300.dp, mood = FolhaMood.THINKING)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (mood in listOf(FolhaMood.IDLE, FolhaMood.TALKING, FolhaMood.HAPPY, FolhaMood.SAD)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Folha(84.dp, mood = mood, onClick = {})
                    Text(mood.name.lowercase(), fontSize = 11.sp, color = Color(0xFF26215C))
                }
            }
        }
        Text("Pequenos (loader, banner)", fontSize = 12.sp, color = Color(0xFF26215C))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Folha(36.dp, mood = FolhaMood.THINKING)
            Folha(48.dp, mood = FolhaMood.IDLE)
            Folha(150.dp, mood = FolhaMood.IDLE, onClick = {})
        }
    }
}
