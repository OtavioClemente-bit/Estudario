package br.com.estudario.ui.simulation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.catalog.ExamBoardInfo
import br.com.estudario.data.catalog.examBoardInfo
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.ui.brand.TextButton
import kotlinx.coroutines.launch

/**
 * "Banca: X, trocar". A banca escolhida vale para tudo do concurso: questões dos treinos e simulados,
 * formato, tempo e nota. Edital antigo mostra a banca dele só como sugestão (a banca pode mudar).
 */
@Composable
fun ExamBoardRow(competitionId: Long, competitionName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as EstudarioApplication
    val service = app.simulationService
    val profiles by service.examProfiles.collectAsState(initial = emptyList())
    val profile = profiles.firstOrNull { it.competitionId == competitionId }
    var info by remember(competitionId) { mutableStateOf<ExamBoardInfo?>(null) }
    LaunchedEffect(competitionId, competitionName, profile?.board, profile?.boardPending) {
        info = runCatching { examBoardInfo(context, competitionId, competitionName) }.getOrNull()
    }
    var editing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val current = info
    val board = current?.board
    val oldNote = current?.oldEditalBoard?.let { old ->
        "O último edital${current.oldEditalYear?.let { " ($it)" }.orEmpty()} foi da $old. A banca pode mudar; escolha a que você espera."
    }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Banca: ${board ?: "a definir"}",
                Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            TextButton(onClick = { editing = true }) { Text(if (board == null) "escolher" else "trocar") }
        }
        Text(
            when {
                board != null -> BoardStyle.styleLabel(BoardStyle.styleFor(board)) +
                    (if (BoardStyle.wrongCancelsRight(board)) " · uma errada anula uma certa" else "") +
                    " · treinos e simulados seguem esta banca"
                oldNote != null -> oldNote
                else -> "Sem banca definida, as questões vêm no padrão geral das provas."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    if (editing) BoardDialog(board ?: current?.oldEditalBoard, onDismiss = { editing = false }, note = oldNote) { picked ->
        editing = false
        scope.launch { service.saveBoard(competitionId, picked) }
    }
}
