package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.ReviewQuestionOrder
import br.com.estudario.ui.brand.BrandCounter
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph

@Composable
internal fun ReviewPracticeSettings(initial: ReviewPracticeOptions, onClose: () -> Unit, onSave: (ReviewPracticeOptions) -> Unit) {
    var draft by remember { mutableStateOf(initial) }
    Dialog(onDismissRequest = onClose) {
        Surface(shape = RoundedCornerShape(26.dp), modifier = Modifier.fillMaxWidth().heightIn(max = (LocalConfiguration.current.screenHeightDp - 64).coerceAtLeast(300).dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BrandIcon(Glyph.Gear, size = 44.dp)
                    Column(Modifier.weight(1f)) {
                        Text("Seu ritmo de revisão", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text("Personalize as próximas sessões", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Questões por revisão", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    BrandCounter(draft.questionCount, 0..50, 1, "questões", Glyph.Clipboard,
                        onChange = { draft = draft.copy(questionCount = it) }, quickValues = listOf(0, 5, 10, 20),
                        mood = { if (it == 0) "Sem questões nesta revisão" else "Usa até $it questões disponíveis no tópico" })
                    Text("Flashcards para lembrar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    BrandCounter(draft.flashcardCount, 0..30, 1, "flashcards", Glyph.Cards,
                        onChange = { draft = draft.copy(flashcardCount = it) }, quickValues = listOf(0, 4, 8, 16),
                        mood = { if (it == 0) "Sem flashcards nesta revisão" else "Tente lembrar antes de consultar o material" })
                    Text("Ordem das questões", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(ReviewQuestionOrder.ERRORS_FIRST to "Erros primeiro", ReviewQuestionOrder.UNSEEN_FIRST to "Inéditas primeiro", ReviewQuestionOrder.MIXED to "Tudo misturado").forEach { (order, label) ->
                            FilterChip(selected = draft.order == order, onClick = { draft = draft.copy(order = order) }, label = { Text(label) })
                        }
                    }
                    ReviewOption("Embaralhar flashcards", "Desligado: segue a ordem do material.", draft.shuffleFlashcards) { draft = draft.copy(shuffleFlashcards = it) }
                    ReviewOption("Resumo rápido", "Consultar depois de tentar lembrar.", draft.showSummary) { draft = draft.copy(showSummary = it) }
                    ReviewOption("Oferecer meus grifos", "Leitor opcional, sem sair da revisão ou editar.", draft.offerHighlights) { draft = draft.copy(offerHighlights = it) }
                    if (draft.offerHighlights) ReviewOption("Notas junto dos grifos", "Mostra também o que você anotou no trecho.", draft.showHighlightNotes) { draft = draft.copy(showHighlightNotes = it) }
                    Text("A sessão em andamento mantém suas escolhas iniciais. Difícil continua repetindo a mesma etapa amanhã.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { draft = ReviewPracticeOptions() }) { Text("Restaurar padrão") }
                    br.com.estudario.ui.brand.Button(onClick = { onSave(draft.bounded()) }) { Text("Salvar") }
                }
                TextButton(onClick = onClose, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun ReviewOption(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange, modifier = Modifier.semantics { contentDescription = title })
    }
}
