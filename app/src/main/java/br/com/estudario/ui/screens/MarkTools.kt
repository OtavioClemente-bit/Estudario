package br.com.estudario.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import br.com.estudario.ui.brand.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.components.MarkColors

/*
 * Ferramentas do grifo no app: a folha que abre ao tocar em "Grifar" num trecho selecionado (cores com
 * nome e significado, anotar, flashcard, revisão, copiar), a folha para montar o flashcard e, no Caderno,
 * a seção "Seus grifos" (etiqueta da cor, filtro por cor, trecho no marca-texto da cor). O site faz igual.
 */

/** A folha do trecho selecionado. [current] é a cor do grifo que o trecho já tem (ganha o check). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkSelectionSheet(
    quote: String,
    current: Int?,
    onColor: (Int) -> Unit,
    onNote: () -> Unit,
    onCard: () -> Unit,
    onReview: () -> Unit,
    onCopy: () -> Unit,
    onDismiss: () -> Unit,
) {
    var focus by remember { mutableStateOf(current ?: 0) }
    val scheme = MaterialTheme.colorScheme
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 26.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Grifar o trecho", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("“" + plainPreview(quote).take(200) + "”", style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant, maxLines = 3)
            // As cores como bolinhas, cada uma com o nome embaixo; a escolhida ganha o check.
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MarkColors.all.forEach { tone ->
                    val on = tone.id == current
                    val grow by animateFloatAsState(if (tone.id == focus) 1.12f else 1f, label = "dot")
                    Column(
                        Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).clickable(role = Role.Button) { focus = tone.id; onColor(tone.id) }
                            .semantics { contentDescription = "Grifar: ${tone.label}. ${tone.hint}"; selected = on }.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Box(
                            Modifier.size(40.dp).scale(grow).border(if (on) 3.dp else 0.dp, tone.color.copy(alpha = .5f), CircleShape).padding(if (on) 4.dp else 0.dp)
                                .clip(CircleShape).background(tone.color),
                            contentAlignment = Alignment.Center,
                        ) { if (on) Icon(Icons.Outlined.Check, null, Modifier.size(20.dp), tint = Color.White) }
                        Text(tone.label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = scheme.onSurface, maxLines = 1)
                    }
                }
            }
            val tone = MarkColors.of(focus)
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(tone.color.copy(alpha = .12f)).padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(Modifier.size(10.dp).clip(CircleShape).background(tone.color))
                Text(tone.label, fontWeight = FontWeight.Black, fontSize = 13.sp, color = scheme.onSurface)
                Text(tone.hint, fontSize = 13.sp, color = scheme.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SheetAction(Icons.Outlined.EditNote, "Anotar", Modifier.weight(1f), onNote)
                SheetAction(Icons.Outlined.Style, "Flashcard", Modifier.weight(1f), onCard)
                SheetAction(Icons.Outlined.EventRepeat, "Revisão", Modifier.weight(1f), onReview)
                SheetAction(Icons.Outlined.ContentCopy, "Copiar", Modifier.weight(1f), onCopy)
            }
        }
    }
}

@Composable
private fun SheetAction(icon: ImageVector, label: String, modifier: Modifier, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(scheme.surfaceVariant.copy(alpha = .6f)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(icon, null, Modifier.size(24.dp), tint = Color(0xFFE0A100))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = scheme.onSurface)
    }
}

/** Montar um flashcard com o trecho: frente e verso editáveis, com duas sugestões de montagem. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteFlashcardSheet(quote: String, onDismiss: () -> Unit, onSave: (front: String, back: String) -> Unit) {
    val plain = remember(quote) { plainPreview(quote) }
    var front by remember { mutableStateOf(plain) }
    var back by remember { mutableStateOf("") }
    var flipped by remember { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 26.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Novo flashcard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PresetChip("Trecho na frente", !flipped) { flipped = false; front = plain; back = "" }
                PresetChip("Pergunta na frente, trecho no verso", flipped) { flipped = true; front = ""; back = plain }
            }
            OutlinedTextField(front, { front = it.take(2000) }, Modifier.fillMaxWidth(), label = { Text("Frente") }, placeholder = { Text("Uma pergunta: o que diz o artigo sobre…?") }, minLines = 2, maxLines = 6)
            OutlinedTextField(back, { back = it.take(4000) }, Modifier.fillMaxWidth(), label = { Text("Verso") }, placeholder = { Text("A resposta ou um complemento (opcional)") }, minLines = 2, maxLines = 6)
            Text("O cartão fica nos flashcards deste tópico e em Salvos, no Caderno de estudo.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(kind = br.com.estudario.ui.brand.NoteKind.Review, onClick = { onSave(front.trim(), back.trim()) }, modifier = Modifier.fillMaxWidth(), enabled = front.isNotBlank()) { Text("Salvar flashcard") }
        }
    }
}

@Composable
private fun PresetChip(label: String, on: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val bg by animateColorAsState(if (on) scheme.primary.copy(alpha = .14f) else scheme.surface, label = "chip")
    Text(
        label,
        Modifier.clip(RoundedCornerShape(50)).background(bg).border(1.5.dp, if (on) scheme.primary else scheme.outlineVariant, RoundedCornerShape(50))
            .clickable(role = Role.RadioButton, onClick = onClick).padding(horizontal = 12.dp, vertical = 7.dp),
        fontSize = 13.sp, fontWeight = FontWeight.Bold, color = scheme.onSurface,
    )
}

/** A cor escolhida no filtro de cada página (sem valor = todas). */
private val markFilter = mutableStateMapOf<Long, Int>()

/**
 * "Seus grifos" na página do tópico no Caderno: os grifos uma vez só (antes apareciam também em "Salvos
 * deste assunto"), na ordem do texto, com a etiqueta da cor e o trecho no marca-texto da cor.
 */
@Composable
fun TopicMarksPanel(viewModel: AppViewModel, topicId: Long, onOpenTheory: (theoryId: Long, block: Int) -> Unit, onOpenTopic: (Long) -> Unit) {
    val theories by viewModel.theories.collectAsState()
    val all by viewModel.theoryMarks.collectAsState()
    val order = remember(theories, topicId) { theories.withIndex().filter { it.value.topicId == topicId || topicId == NOTEBOOK_GENERAL }.associate { it.value.id to it.index } }
    val known = remember(theories) { theories.associate { it.id to it.topicId } }
    val marks = remember(all, order, known) {
        all.filter { m -> if (topicId == NOTEBOOK_GENERAL) known[m.theoryId] == null else m.theoryId in order }
            .sortedWith(compareBy({ order[it.theoryId] ?: 0 }, { it.blockIndex }, { it.createdAt }))
    }
    if (topicId == NOTEBOOK_GENERAL && marks.isEmpty()) return
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)
    val filter = markFilter[topicId]?.takeIf { c -> marks.any { it.color == c } }
    val shown = if (filter == null) marks else marks.filter { it.color == filter }
    Column(
        Modifier.fillMaxWidth().clip(shape).background(scheme.surface).border(2.dp, scheme.outlineVariant, shape).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF2B300)), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.BorderColor, null, Modifier.size(22.dp), tint = Color(0xFF3E3300))
            }
            Column(Modifier.weight(1f)) {
                Text("Seus grifos", fontWeight = FontWeight.Black, fontSize = 19.sp, color = scheme.onSurface)
                Text(if (marks.isEmpty()) "Nenhum trecho grifado ainda" else "${marks.size} ${if (marks.size == 1) "trecho" else "trechos"}, na ordem do texto", fontSize = 13.sp, color = scheme.onSurfaceVariant)
            }
        }
        if (marks.isEmpty()) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(scheme.surfaceVariant.copy(alpha = .5f)).padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { MarkColors.all.forEach { Box(Modifier.size(14.dp).clip(CircleShape).background(it.color)) } }
                Text("Na teoria, selecione um trecho e toque em Grifar: cada cor tem um sentido (cai muito, entendi, revisar, lei e artigo, pegadinha). Os grifos ficam aqui, separados por cor.", fontSize = 14.sp, lineHeight = 21.sp, color = scheme.onSurfaceVariant)
                if (topicId >= 0) TextButton(kind = br.com.estudario.ui.brand.NoteKind.Study, onClick = { onOpenTopic(topicId) }) { Icon(Icons.Outlined.MenuBook, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Grifar na teoria") }
            }
            return@Column
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToneChip("Todas", null, marks.size, filter == null) { markFilter.remove(topicId) }
            MarkColors.all.forEach { tone ->
                val n = marks.count { it.color == tone.id }
                if (n > 0) ToneChip(tone.label, tone.color, n, filter == tone.id) { if (filter == tone.id) markFilter.remove(topicId) else markFilter[topicId] = tone.id }
            }
        }
        shown.forEach { m -> androidx.compose.runtime.key(m.id) { MarkCard(m, viewModel, onOpenTheory) } }
    }
}

@Composable
private fun ToneChip(label: String, color: Color?, n: Int?, on: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val accent = color ?: scheme.onSurface
    val chip = RoundedCornerShape(50)
    Row(
        Modifier.clip(chip).background(if (on) accent.copy(alpha = .16f) else scheme.surface).border(2.dp, if (on) accent else scheme.outlineVariant, chip)
            .clickable(role = Role.Button, onClick = onClick).semantics { selected = on }.padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (color != null) Box(Modifier.size(10.dp).clip(CircleShape).background(color))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = scheme.onSurface)
        if (n != null) Text("$n", Modifier.clip(chip).background(scheme.surfaceVariant).padding(horizontal = 7.dp, vertical = 1.dp), fontSize = 11.sp, fontWeight = FontWeight.Black, color = scheme.onSurface)
    }
}

@Composable
private fun MarkCard(m: TheoryMarkEntity, viewModel: AppViewModel, onOpenTheory: (Long, Int) -> Unit) {
    val tone = MarkColors.of(m.color)
    val scheme = MaterialTheme.colorScheme
    val text = remember(m.quote) { plainPreview(m.quote) }
    val long = text.length > 320
    var open by remember(m.id) { mutableStateOf(false) }
    var recolor by remember(m.id) { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    Row(Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min).clip(shape).background(tone.color.copy(alpha = .07f)).border(1.5.dp, tone.color.copy(alpha = .35f), shape)) {
        Box(Modifier.width(5.dp).fillMaxHeight().background(tone.color))
        Column(Modifier.weight(1f).padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier.clip(RoundedCornerShape(50)).background(tone.color.copy(alpha = .2f)).clickable(role = Role.Button) { recolor = !recolor }
                        .semantics { contentDescription = "Cor ${tone.label}. Tocar para trocar" }.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(Modifier.size(9.dp).clip(CircleShape).background(tone.color))
                    Text(tone.label.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = .5.sp, color = scheme.onSurface)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onOpenTheory(m.theoryId, m.blockIndex) }) { Icon(Icons.Outlined.MenuBook, "Abrir na teoria", tint = scheme.onSurfaceVariant) }
                IconButton(onClick = { viewModel.deleteTheoryMark(m) }) { Icon(Icons.Outlined.DeleteOutline, "Apagar grifo", tint = scheme.onSurfaceVariant) }
            }
            if (recolor) Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                MarkColors.all.forEach { t -> ToneChip(t.label, t.color, null, t.id == m.color) { if (t.id != m.color) viewModel.saveTheoryMark(m.copy(color = t.id)); recolor = false } }
            }
            // O trecho com o marca-texto da cor (fundo na faixa de baixo da linha, como caneta de verdade).
            Box(if (long && !open) Modifier.heightIn(max = 150.dp).clipToBounds() else Modifier) {
                Text(
                    androidx.compose.ui.text.buildAnnotatedString {
                        pushStyle(androidx.compose.ui.text.SpanStyle(background = tone.color.copy(alpha = .30f)))
                        append(text); pop()
                    },
                    fontSize = 16.sp, lineHeight = 25.sp, color = scheme.onSurface,
                )
            }
            if (long) Text(if (open) "recolher" else "ver o trecho inteiro", Modifier.clickable { open = !open }, color = Color(0xFF2F5BD3), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            if (m.note.isNotBlank()) Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFF4B8)).padding(horizontal = 10.dp, vertical = 7.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.EditNote, null, Modifier.size(18.dp), tint = Color(0xFF3E3300))
                Text(br.com.estudario.ui.components.NoteTags.split(m.note).second, fontSize = 15.sp, lineHeight = 21.sp, color = Color(0xFF3E3300))
            }
        }
    }
}
