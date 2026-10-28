package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import br.com.estudario.ui.brand.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.ReviewQuestionOrder
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.studio.Gx
import br.com.estudario.ui.studio.GxCounter
import br.com.estudario.ui.studio.GxKicker
import br.com.estudario.ui.studio.GxLaunch
import br.com.estudario.ui.studio.GxOption
import br.com.estudario.ui.studio.GxPanel
import br.com.estudario.ui.studio.GxSection
import br.com.estudario.ui.studio.GxSeg
import br.com.estudario.ui.studio.GxSwitchRow

/** Os ajustes da revisão no estúdio (como no site): contadores grandes, a ordem em cartões e chaves largas. */
@Composable
internal fun ReviewPracticeSettings(initial: ReviewPracticeOptions, onClose: () -> Unit, onSave: (ReviewPracticeOptions) -> Unit) {
    var draft by remember { mutableStateOf(initial) }
    br.com.estudario.ui.studio.GxDialog(onClose) {
        run {
            Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 10.dp, top = 16.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    GxKicker("Ajustes da revisão")
                    Text("Seu ritmo de revisão", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = br.com.estudario.ui.studio.Gx.strong)
                    Text("Personalize as próximas sessões", style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                }
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(13.dp)).background(br.com.estudario.ui.studio.Gx.tint(0.06f)).clickable(onClick = onClose), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Close, "Fechar", tint = Gx.strong)
                }
            }
            Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                GxSection(Glyph.Clipboard, "Questões por revisão", color = Gx.mint) {
                    GxCounter(draft.questionCount, { draft = draft.copy(questionCount = it) }, 0..50, unit = "questões", quick = listOf(0, 5, 10, 20),
                        mood = { if (it == 0) "Sem questões nesta revisão" else "Usa até $it questões disponíveis no tópico" })
                }
                GxSection(Glyph.Cards, "Flashcards para lembrar", color = Gx.gold) {
                    GxCounter(draft.flashcardCount, { draft = draft.copy(flashcardCount = it) }, 0..30, unit = "flashcards", quick = listOf(0, 4, 8, 16),
                        mood = { if (it == 0) "Sem flashcards nesta revisão" else "Tente lembrar antes de consultar o material" })
                }
                GxSection(Glyph.Cycle, "Ordem das questões", color = Gx.orange) {
                    GxSeg(listOf(
                        GxOption(ReviewQuestionOrder.ERRORS_FIRST, "Erros primeiro", "O que você errou vem antes", color = Gx.red),
                        GxOption(ReviewQuestionOrder.UNSEEN_FIRST, "Inéditas primeiro", "O que você ainda não viu", color = Gx.mint),
                        GxOption(ReviewQuestionOrder.MIXED, "Tudo misturado", "Sem ordem fixa", color = Gx.violet),
                    ), draft.order, columns = 3, onPick = { draft = draft.copy(order = it) })
                }
                GxSection(Glyph.Gear, "Durante a revisão", color = Gx.cyan) {
                    GxSwitchRow("Embaralhar flashcards", "Desligado: segue a ordem do material.", draft.shuffleFlashcards) { draft = draft.copy(shuffleFlashcards = it) }
                    GxSwitchRow("Resumo rápido", "Consultar depois de tentar lembrar.", draft.showSummary) { draft = draft.copy(showSummary = it) }
                    GxSwitchRow("Oferecer meus grifos", "Leitor opcional, sem sair da revisão ou editar.", draft.offerHighlights) { draft = draft.copy(offerHighlights = it) }
                    if (draft.offerHighlights) GxSwitchRow("Notas junto dos grifos", "Mostra também o que você anotou no trecho.", draft.showHighlightNotes) { draft = draft.copy(showHighlightNotes = it) }
                }
                Text("A sessão em andamento mantém suas escolhas iniciais. Difícil continua repetindo a mesma etapa amanhã.", style = MaterialTheme.typography.bodySmall, color = Gx.mute)
            }
            Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                GxLaunch("Salvar configurações", glyph = Glyph.Check) { onSave(draft.bounded()) }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { draft = ReviewPracticeOptions() }) { Text("Restaurar padrão", color = Gx.mute) }
                    TextButton(onClick = onClose) { Text("Cancelar", color = Gx.mute) }
                }
            }
        }
    }
}
