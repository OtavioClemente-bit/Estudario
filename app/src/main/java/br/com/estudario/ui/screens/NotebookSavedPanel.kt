package br.com.estudario.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PestControlRodent
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.local.UserNoteEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.components.SavedFlashcards

/*
 * "Salvos deste assunto": dentro da página de cada tópico do Caderno, o que a pessoa guardou dele
 * (grifos, questões, dicas, pegadinhas, flashcards e notas), por tipo. Substitui a antiga aba
 * "Grifos e salvos". O que não tem tópico fica no "Geral" (página -1). O site faz igual.
 */

/** Id da página "Geral": o que foi guardado sem tópico (ou de tópico que não existe mais). */
const val NOTEBOOK_GENERAL = -1L

class SavedOfTopic(
    val marks: List<TheoryMarkEntity>,
    val questions: List<QuestionWithOptions>,
    val tips: List<TopicSnippetEntity>,
    val traps: List<TopicSnippetEntity>,
    val cards: List<TopicSnippetEntity>,
    val decks: List<SummaryEntity>,
    val notes: List<UserNoteEntity>,
) {
    val markNotes get() = marks.filter { it.note.isNotBlank() }
    val total get() = marks.size + questions.size + tips.size + traps.size + cards.size + decks.size + notes.size
}

/** Tudo o que foi guardado, por tópico (NOTEBOOK_GENERAL para sem tópico). Nada fica de fora. */
@Composable
fun rememberSavedByTopic(viewModel: AppViewModel): Map<Long, SavedOfTopic> {
    val topics by viewModel.topics.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val marks by viewModel.theoryMarks.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val questions by viewModel.questions.collectAsState()
    return remember(topics, theories, marks, snippets, summaries, notes, questions) {
        val known = topics.mapTo(hashSetOf()) { it.id }
        fun key(t: Long?) = t?.takeIf { it in known } ?: NOTEBOOK_GENERAL
        val theoryTopic = theories.associate { it.id to it.topicId }
        val byMark = marks.groupBy { key(theoryTopic[it.theoryId]) }
        val byQuestion = questions.filter { it.question.isFavorite }.groupBy { key(it.question.topicId) }
        val cardsAll = snippets.filter { SavedFlashcards.isSavedCard(it) || (it.isFavorite && it.kind == SnippetKind.RECUPERACAO) }
        val cardIds = cardsAll.mapTo(hashSetOf()) { it.id }
        val others = snippets.filter { it.isFavorite && it.id !in cardIds }
        val byCard = cardsAll.groupBy { key(it.topicId) }
        val byTrap = others.filter { it.kind == SnippetKind.PEGADINHA }.groupBy { key(it.topicId) }
        val byTip = others.filter { it.kind != SnippetKind.PEGADINHA }.groupBy { key(it.topicId) }
        val byDeck = summaries.filter { it.kind == SummaryKind.RAPIDO && it.isFavorite }.groupBy { key(it.topicId) }
        val byNote = notes.groupBy { key(it.topicId) }
        val ids = byMark.keys + byQuestion.keys + byCard.keys + byTrap.keys + byTip.keys + byDeck.keys + byNote.keys
        ids.associateWith { id ->
            SavedOfTopic(
                byMark[id].orEmpty().sortedBy { it.blockIndex }, byQuestion[id].orEmpty(), byTip[id].orEmpty(), byTrap[id].orEmpty(),
                byCard[id].orEmpty(), byDeck[id].orEmpty(), byNote[id].orEmpty().sortedByDescending { it.createdAt },
            )
        }
    }
}

private enum class SavedKind(val label: String, val icon: ImageVector) {
    MARKS("Grifos", Icons.Outlined.BorderColor), QUESTIONS("Questões", Icons.Outlined.Quiz), TIPS("Dicas", Icons.Outlined.Lightbulb),
    TRAPS("Pegadinhas", Icons.Outlined.PestControlRodent), CARDS("Flashcards", Icons.Outlined.Style), NOTES("Notas", Icons.Outlined.EditNote),
}

private fun SavedOfTopic.count(k: SavedKind) = when (k) {
    SavedKind.MARKS -> marks.size; SavedKind.QUESTIONS -> questions.size; SavedKind.TIPS -> tips.size
    SavedKind.TRAPS -> traps.size; SavedKind.CARDS -> cards.size + decks.size; SavedKind.NOTES -> notes.size + markNotes.size
}

/** O tipo aberto em cada tópico (sobrevive a ir à teoria e voltar). */
private val openKind = mutableStateMapOf<Long, SavedKind>()

private val Accent = Color(0xFF2F5BD3)

/** O bloco "Salvos deste assunto" de um tópico: abas com contador e a lista do tipo escolhido. */
@Composable
fun TopicSavedPanel(
    viewModel: AppViewModel,
    topicId: Long,
    onOpenTheory: (theoryId: Long, block: Int) -> Unit,
    onOpenTopic: (Long) -> Unit,
    onTrainFavorites: (Long) -> Unit,
    // Na página do tópico os grifos (e as notas deles) ficam em "Seus grifos": aqui não se repetem.
    withMarks: Boolean = true,
) {
    val saved = rememberSavedByTopic(viewModel)[topicId]?.let { s -> if (withMarks) s else SavedOfTopic(emptyList(), s.questions, s.tips, s.traps, s.cards, s.decks, s.notes) }
    val topics by viewModel.topics.collectAsState()
    val place = topics.firstOrNull { it.id == topicId }?.title ?: "Geral"
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)
    var viewing by remember { mutableStateOf<Any?>(null) }
    var editingNote by remember { mutableStateOf<UserNoteEntity?>(null) }
    var deckFor by remember { mutableStateOf<SummaryEntity?>(null) }
    deckFor?.let { d -> br.com.estudario.ui.components.SummaryFlashcardDeck(viewModel, d, place, onDismiss = { deckFor = null }) }
    when (val v = viewing) {
        is UserNoteEntity -> br.com.estudario.ui.components.NoteViewSheet(
            text = v.text, createdAt = v.createdAt, place = place, onDismiss = { viewing = null },
            onEdit = { viewing = null; editingNote = v }, onDelete = { viewModel.deleteNote(v); viewing = null },
        )
        is TheoryMarkEntity -> br.com.estudario.ui.components.NoteViewSheet(
            text = v.note, createdAt = v.createdAt, place = place, onDismiss = { viewing = null },
            onEdit = { viewing = null; onOpenTheory(v.theoryId, v.blockIndex) },
            onDelete = { viewModel.saveTheoryMark(v.copy(note = "")); viewing = null },
            quote = plainPreview(v.quote), onOpen = { viewing = null; onOpenTheory(v.theoryId, v.blockIndex) },
        )
    }
    editingNote?.let { n -> NoteEditor(n, topics, onDismiss = { editingNote = null }, onSave = { viewModel.saveNote(it); editingNote = null }, onDelete = { viewModel.deleteNote(n); editingNote = null }) }

    Column(
        Modifier.fillMaxWidth().clip(shape).background(scheme.surface).border(2.dp, scheme.outlineVariant, shape).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Outlined.Bookmarks, null, Modifier.size(26.dp), tint = Color(0xFFE0A100))
            Column(Modifier.weight(1f)) {
                Text("Salvos deste assunto", fontWeight = FontWeight.Black, fontSize = 19.sp, color = scheme.onSurface)
                Text(if (saved == null || saved.total == 0) "Nada guardado ainda" else "${saved.total} ${if (saved.total == 1) "item guardado" else "itens guardados"}", fontSize = 13.sp, color = scheme.onSurfaceVariant)
            }
        }
        if (saved == null || saved.total == 0) {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(scheme.surfaceVariant.copy(alpha = 0.5f)).padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Outlined.TipsAndUpdates, null, tint = Accent)
                Text("Nas questões, dicas, pegadinhas e flashcards, toque na estrela para guardar. Na teoria, selecione um trecho e crie um flashcard. Tudo deste tópico aparece aqui (os grifos ficam em Seus grifos).", fontSize = 15.sp, lineHeight = 22.sp, color = scheme.onSurfaceVariant)
            }
            return@Column
        }
        val kinds = SavedKind.entries.filter { saved.count(it) > 0 }
        val chosen = openKind[topicId]?.takeIf { it in kinds } ?: kinds.first()
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            kinds.forEach { k ->
                val on = k == chosen
                val chip = RoundedCornerShape(50)
                Row(
                    Modifier.clip(chip).background(if (on) Accent else scheme.surface).border(2.dp, if (on) Accent else scheme.outlineVariant, chip)
                        .clickable { openKind[topicId] = k }.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(k.icon, null, Modifier.size(18.dp), tint = if (on) Color.White else scheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    Text(k.label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (on) Color.White else scheme.onSurface)
                    Spacer(Modifier.width(6.dp))
                    Text("${saved.count(k)}", Modifier.clip(chip).background(if (on) Color.White.copy(alpha = 0.25f) else scheme.surfaceVariant).padding(horizontal = 7.dp, vertical = 1.dp), fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (on) Color.White else scheme.onSurface)
                }
            }
        }
        when (chosen) {
            SavedKind.MARKS -> saved.marks.forEach { m ->
                PaperItem("GRIFO", br.com.estudario.ui.components.MarkColors.of(m.color).color, plainPreview(m.quote), m.note.takeIf { it.isNotBlank() }?.let { br.com.estudario.ui.components.NoteTags.split(it).second }) {
                    TextButton(onClick = { onOpenTheory(m.theoryId, m.blockIndex) }) { Icon(Icons.Outlined.MenuBook, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Abrir na teoria") }
                    IconButton(onClick = { viewModel.deleteTheoryMark(m) }) { Icon(Icons.Outlined.DeleteOutline, "Apagar grifo", tint = scheme.onSurfaceVariant) }
                }
            }
            SavedKind.QUESTIONS -> {
                saved.questions.forEach { row ->
                    val meta = listOfNotNull(row.question.board, row.question.year?.toString()).joinToString(" · ").ifBlank { null }
                    PaperItem("QUESTÃO", Accent, plainPreview(row.question.statement), meta, maxLines = 6) {
                        if (topicId >= 0) TextButton(onClick = { onTrainFavorites(topicId) }) { Icon(Icons.Outlined.PlayArrow, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Resolver") }
                        IconButton(onClick = { viewModel.toggleQuestionFavorite(row.question) }) { Icon(Icons.Outlined.Star, "Tirar dos salvos", tint = Accent) }
                    }
                }
                if (topicId >= 0) Button(onClick = { onTrainFavorites(topicId) }, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text(if (saved.questions.size == 1) "Treinar a questão salva" else "Treinar as ${saved.questions.size} salvas") }
            }
            SavedKind.TIPS, SavedKind.TRAPS -> {
                val trap = chosen == SavedKind.TRAPS
                (if (trap) saved.traps else saved.tips).forEach { s ->
                    PaperItem(if (trap) "PEGADINHA" else "DICA", if (trap) Color(0xFFEE6A4B) else Color(0xFF1FA97F), s.text, s.answer?.takeIf { it.isNotBlank() }?.let { "Resposta: $it" }, markdown = true) {
                        s.topicId?.let { t -> TextButton(onClick = { onOpenTopic(t) }) { Icon(Icons.Outlined.MenuBook, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Abrir no tópico") } }
                        IconButton(onClick = { viewModel.saveSnippet(s.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Tirar dos salvos", tint = Accent) }
                    }
                }
            }
            SavedKind.CARDS -> {
                saved.decks.forEach { d ->
                    val n = remember(d.markdown) { br.com.estudario.ui.components.FlashcardParser.parse(d.markdown).size }
                    PaperItem("BARALHO", Accent, d.title.ifBlank { "Flashcards" }, "$n cartões") {
                        TextButton(onClick = { deckFor = d }) { Icon(Icons.Outlined.PlayArrow, null, Modifier.size(18.dp)); Spacer(Modifier.width(4.dp)); Text("Praticar") }
                        IconButton(onClick = { viewModel.updateSummary(d.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Tirar dos salvos", tint = Accent) }
                    }
                }
                saved.cards.forEachIndexed { i, c ->
                    var flipped by remember(c.id) { mutableStateOf(false) }
                    Column {
                        br.com.estudario.ui.components.NotebookPaperCard(c.text, c.answer.orEmpty(), flipped, onFlip = { flipped = !flipped }, counter = "${i + 1} de ${saved.cards.size}", minHeight = 200.dp, questionSp = 20f, answerSp = 18f)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { if (SavedFlashcards.isSavedCard(c)) viewModel.deleteSnippet(c) else viewModel.saveSnippet(c.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Tirar dos salvos", tint = Accent) }
                        }
                    }
                }
            }
            SavedKind.NOTES -> {
                saved.notes.forEach { n -> br.com.estudario.ui.components.NotePaperCard(n.text, n.createdAt, onClick = { viewing = n }) }
                saved.markNotes.forEach { m -> br.com.estudario.ui.components.NotePaperCard(br.com.estudario.ui.components.NoteTags.split(m.note).second, m.createdAt, onClick = { viewing = m }, quote = plainPreview(m.quote)) }
            }
        }
    }
}

/** Um item salvo como ficha de caderno: etiqueta, texto em letra confortável e as ações embaixo. */
@Composable
private fun PaperItem(tag: String, accent: Color, text: String, extra: String?, maxLines: Int = 12, markdown: Boolean = false, actions: @Composable () -> Unit) {
    val night = br.com.estudario.ui.brand.isCadernoNight()
    val paper = if (night) Color(0xFF1E1D2B) else Color(0xFFFFFDF6)
    val ink = if (night) Color(0xFFEAF1FF) else Color(0xFF14285E)
    val shape = br.com.estudario.ui.brand.BrandCardShape
    Row(Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min).clip(shape).background(paper).border(2.dp, if (night) Color(0x55FFFFFF) else Color(0xFF14285E), shape)) {
        Box(Modifier.width(7.dp).fillMaxHeight().background(accent))
        Column(Modifier.weight(1f).padding(start = 14.dp, end = 6.dp, top = 12.dp, bottom = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(tag, Modifier.clip(RoundedCornerShape(8.dp)).background(accent.copy(alpha = 0.18f)).padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = ink)
            if (markdown) br.com.estudario.ui.components.StudyInlineText(text, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp, lineHeight = 25.sp, color = ink))
            else Text(text, fontSize = 17.sp, lineHeight = 25.sp, color = ink, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
            if (extra != null) Text(extra, fontSize = 14.sp, lineHeight = 20.sp, color = ink.copy(alpha = 0.7f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) { actions() }
        }
    }
}


/** Na entrada do Caderno: os assuntos com algo guardado, para chegar a cada "Salvos deste assunto". */
@Composable
fun SavedTopicsShelf(viewModel: AppViewModel, onPage: (Long) -> Unit) {
    val all = rememberSavedByTopic(viewModel)
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val entries = all.entries.filter { it.value.total > 0 }
    if (entries.isEmpty()) return
    val topicById = topics.associateBy { it.id }
    val subjectById = subjects.associateBy { it.id }
    val sorted = entries.sortedWith(compareBy({ if (it.key < 0) 1 else 0 }, { topicById[it.key]?.let { t -> subjectById[t.subjectId]?.position } ?: 0 }, { topicById[it.key]?.position ?: 0 }))
    val scheme = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("Seus salvos, por assunto", Modifier.weight(1f), fontWeight = FontWeight.Black, fontSize = 17.sp, color = scheme.onSurface)
            Text(sorted.sumOf { it.value.total }.let { n -> "$n ${if (n == 1) "item" else "itens"}" }, fontSize = 12.sp, color = scheme.onSurfaceVariant)
        }
        sorted.forEach { (id, s) ->
            val t = topicById[id]
            val subject = t?.let { subjectById[it.subjectId]?.name }
            val shape = RoundedCornerShape(14.dp)
            Row(
                Modifier.fillMaxWidth().clip(shape).background(scheme.surface).border(2.dp, scheme.outlineVariant, shape).clickable { onPage(id) }.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (t == null) Icon(Icons.Outlined.FolderOpen, null, Modifier.size(34.dp), tint = scheme.onSurfaceVariant)
                else br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(subject.orEmpty()), br.com.estudario.ui.components.subjectAccentColor(subject.orEmpty(), br.com.estudario.ui.theme.estudarioColors().subjectPalette), 38.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    if (subject != null) Text(subject.uppercase(), fontSize = 10.5.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(t?.title ?: "Geral", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = scheme.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SavedKind.entries.filter { s.count(it) > 0 }.forEach { k ->
                            Row(verticalAlignment = Alignment.CenterVertically) { Icon(k.icon, null, Modifier.size(15.dp), tint = scheme.onSurfaceVariant); Spacer(Modifier.width(2.dp)); Text("${s.count(k)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant) }
                        }
                    }
                }
                Icon(Icons.Outlined.ChevronRight, null, tint = scheme.onSurfaceVariant)
            }
        }
    }
}

/** Página do Caderno de um tópico ainda não estudado (ou do Geral) que tem coisa guardada. */
@Composable
fun SavedOnlyPage(viewModel: AppViewModel, topicId: Long, onOpenTheory: (Long, Int) -> Unit, onOpenTopic: (Long) -> Unit, onTrainFavorites: (Long) -> Unit) {
    val topics by viewModel.topics.collectAsState()
    val t = topics.firstOrNull { it.id == topicId }
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(t?.title ?: "Geral", fontWeight = FontWeight.Black, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface)
        if (t == null) Text("O que você guardou sem um tópico do edital.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        TopicMarksPanel(viewModel, topicId, onOpenTheory, onOpenTopic)
        TopicSavedPanel(viewModel, topicId, onOpenTheory, onOpenTopic, onTrainFavorites, withMarks = false)
        if (t != null) OutlinedButton(onClick = { onOpenTopic(topicId) }, Modifier.fillMaxWidth()) { Icon(Icons.Outlined.MenuBook, null); Spacer(Modifier.width(6.dp)); Text("Abrir o tópico") }
    }
}
