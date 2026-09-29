package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.UserNoteEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.RecallCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class NotebookTab(val label: String) { MARKS("Marcados"), FLASHCARDS("Flashcards"), NOTES("Anotações"), FAVORITES("Favoritos") }

/**
 * Caderno de estudo: o que a pessoa guardou enquanto estudava, num lugar só. Trechos marcados
 * abrem direto no ponto da teoria; anotações ficam por tópico; favoritos viram treino.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyNotebookScreen(
    viewModel: AppViewModel,
    onOpenTheory: (theoryId: Long, block: Int) -> Unit,
    onOpenTopic: (Long) -> Unit,
    onTrainFavorites: () -> Unit,
) {
    val marks by viewModel.theoryMarks.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val savedDecks = summaries.filter { it.kind == br.com.estudario.data.local.SummaryKind.RAPIDO && it.isFavorite }
    var deckFor by remember { mutableStateOf<br.com.estudario.data.local.SummaryEntity?>(null) }
    var tab by rememberSaveable { mutableStateOf(NotebookTab.MARKS) }
    var editingNote by remember { mutableStateOf<UserNoteEntity?>(null) }
    val topicById = remember(topics) { topics.associateBy { it.id } }
    val theoryById = remember(theories) { theories.associateBy { it.id } }
    val favoriteQuestions = questions.filter { it.question.isFavorite }
    val favoriteSnippets = snippets.filter { it.isFavorite }

    deckFor?.let { deck ->
        val current = summaries.firstOrNull { it.id == deck.id } ?: deck
        br.com.estudario.ui.components.FlashcardDeckDialog(
            title = topicById[current.topicId]?.title ?: current.title,
            cards = remember(current.markdown) { br.com.estudario.ui.components.FlashcardParser.parse(current.markdown) },
            saved = current.isFavorite,
            onToggleSave = { viewModel.updateSummary(current.copy(isFavorite = !current.isFavorite)) },
            onDismiss = { deckFor = null },
        )
    }
    editingNote?.let { note ->
        NoteEditor(note, topics, onDismiss = { editingNote = null }, onSave = { viewModel.saveNote(it); editingNote = null }, onDelete = { viewModel.deleteNote(note); editingNote = null })
    }

    Scaffold(
        floatingActionButton = {
            if (tab == NotebookTab.NOTES) ExtendedFloatingActionButton(onClick = { editingNote = UserNoteEntity(text = "") }, icon = { Icon(Icons.Outlined.Add, null) }, text = { Text("Nova anotação") })
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Tudo o que você guardou para revisar, sem precisar abrir tópico por tópico.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NotebookTab.entries.forEach { value ->
                            val count = when (value) {
                                NotebookTab.MARKS -> marks.size
                                NotebookTab.FLASHCARDS -> savedDecks.size
                                NotebookTab.NOTES -> notes.size
                                NotebookTab.FAVORITES -> favoriteQuestions.size + favoriteSnippets.size
                            }
                            FilterChip(selected = tab == value, onClick = { tab = value }, label = { Text("${value.label} ($count)") })
                        }
                    }
                }
            }
            when (tab) {
                NotebookTab.MARKS -> {
                    if (marks.isEmpty()) item { EmptyHint(Icons.Outlined.Bookmark, "Nenhum trecho marcado", "Na leitura da teoria, toque no marcador ao lado de um trecho ou em \"Marcar este ponto\". Ele aparece aqui para você revisar.") }
                    val grouped = marks.groupBy { theoryById[it.theoryId]?.topicId }
                    grouped.forEach { (topicId, topicMarks) ->
                        item(key = "marks-topic-$topicId") { TopicHeader(topicId?.let(topicById::get)?.title ?: "Teoria removida") }
                        items(topicMarks.sortedBy { it.blockIndex }, key = { "mark-${it.id}" }) { mark ->
                            ElevatedCard(onClick = { onOpenTheory(mark.theoryId, mark.blockIndex) }, modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(plainPreview(mark.quote), maxLines = 5, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                    if (mark.note.isNotBlank()) Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                                        Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Icon(Icons.Outlined.EditNote, null, Modifier.size(18.dp))
                                            Text(mark.note, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Abrir no trecho", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                                        IconButton(onClick = { viewModel.deleteTheoryMark(mark) }) { Icon(Icons.Outlined.DeleteOutline, "Remover marcação") }
                                    }
                                }
                            }
                        }
                    }
                }
                NotebookTab.FLASHCARDS -> {
                    if (savedDecks.isEmpty()) item { EmptyHint(Icons.Outlined.Style, "Nenhum baralho salvo", "Nos tópicos, abra os Flashcards na aba REVISÃO e toque no marcador para salvar o baralho aqui.") }
                    items(savedDecks, key = { "deck-${it.id}" }) { deck ->
                        val count = remember(deck.markdown) { br.com.estudario.ui.components.FlashcardParser.parse(deck.markdown).size }
                        ElevatedCard(onClick = { deckFor = deck }, modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.primary)
                                Column(Modifier.weight(1f)) {
                                    Text(topicById[deck.topicId]?.title ?: deck.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("$count cartões", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Icon(Icons.Outlined.PlayArrow, "Praticar", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                NotebookTab.NOTES -> {
                    if (notes.isEmpty()) item { EmptyHint(Icons.Outlined.EditNote, "Nenhuma anotação ainda", "Escreva resumos com suas palavras, macetes e dúvidas. Ligue cada anotação a um tópico para achar depois.") }
                    items(notes, key = { "note-${it.id}" }) { note ->
                        ElevatedCard(onClick = { editingNote = note }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        note.topicId?.let(topicById::get)?.title ?: "Sem tópico",
                                        Modifier.weight(1f),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(dateFormat.format(Date(note.createdAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(note.text, maxLines = 8, overflow = TextOverflow.Ellipsis)
                                if (note.topicId != null && topicById[note.topicId] != null) TextButton(onClick = { onOpenTopic(note.topicId) }) { Text("Abrir tópico") }
                            }
                        }
                    }
                }
                NotebookTab.FAVORITES -> {
                    item {
                        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Favorite, null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(10.dp))
                                    Text("${favoriteQuestions.size} questões favoritas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                }
                                Text("Favorite as questões que você quer rever antes da prova: as mais difíceis, as que caem sempre, as que você errou por detalhe.", style = MaterialTheme.typography.bodySmall)
                                Button(onClick = onTrainFavorites, enabled = favoriteQuestions.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Treinar favoritas")
                                }
                            }
                        }
                    }
                    if (favoriteSnippets.isEmpty() && favoriteQuestions.isEmpty()) item { EmptyHint(Icons.Outlined.Star, "Nada favoritado ainda", "Toque na estrela das dicas, pegadinhas e perguntas de memorização, ou no coração das questões, para juntar aqui o que mais importa.") }
                    favoriteSnippets.groupBy { it.kind }.forEach { (kind, values) ->
                        item(key = "fav-kind-$kind") { TopicHeader(when (kind) { SnippetKind.BIZU -> "Dicas"; SnippetKind.PEGADINHA -> "Pegadinhas"; SnippetKind.RECUPERACAO -> "Memorização" }) }
                        items(values, key = { "fav-snippet-${it.id}" }) { snippet ->
                            val topicTitle = topicById[snippet.topicId]?.title
                            if (kind == SnippetKind.RECUPERACAO) {
                                RecallCard(snippet.text, snippet.answer, revealKey = snippet.id) {
                                    IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Desfavoritar", tint = MaterialTheme.colorScheme.primary) }
                                }
                            } else {
                                ElevatedCard(
                                    onClick = { onOpenTopic(snippet.topicId) },
                                    colors = CardDefaults.elevatedCardColors(containerColor = if (kind == SnippetKind.PEGADINHA) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(Modifier.padding(start = 14.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            if (topicTitle != null) Text(topicTitle, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                            Text(snippet.text)
                                        }
                                        IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Desfavoritar") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

/** Trecho de teoria sem a sintaxe do Markdown, para a prévia do caderno. */
internal fun plainPreview(markdown: String): String = markdown
    .lines()
    .filterNot { it.trim().matches(Regex("""^\|?\s*:?-{3,}.*""")) }
    .joinToString(" ") { it.trim().removePrefix(">").trim() }
    .replace(Regex("""^#+\s*|\s#+\s"""), " ")
    .replace(Regex("""\*\*|__|`|\$\$"""), "")
    .replace(Regex("""\s*\|\s*"""), " · ")
    .replace(Regex("""(\s*·\s*)+"""), " · ")
    .replace(Regex("""\s+"""), " ")
    .trim()
    .trim('·', ' ')

@Composable
private fun TopicHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun EmptyHint(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, body: String) {
    Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, null, Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteEditor(note: UserNoteEntity, topics: List<TopicEntity>, onDismiss: () -> Unit, onSave: (UserNoteEntity) -> Unit, onDelete: () -> Unit) {
    var text by remember(note.id) { mutableStateOf(note.text) }
    var topicId by remember(note.id) { mutableStateOf(note.topicId) }
    var pickerOpen by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (note.id == 0L) "Nova anotação" else "Anotação") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(expanded = pickerOpen, onExpandedChange = { pickerOpen = it }) {
                    OutlinedTextField(
                        value = topics.firstOrNull { it.id == topicId }?.title ?: "Sem tópico",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tópico") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(pickerOpen) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    )
                    ExposedDropdownMenu(expanded = pickerOpen, onDismissRequest = { pickerOpen = false }) {
                        DropdownMenuItem(text = { Text("Sem tópico") }, onClick = { topicId = null; pickerOpen = false })
                        topics.sortedBy { it.title }.forEach { topic ->
                            DropdownMenuItem(text = { Text(topic.title, maxLines = 2, overflow = TextOverflow.Ellipsis) }, onClick = { topicId = topic.id; pickerOpen = false })
                        }
                    }
                }
                OutlinedTextField(text, { text = it }, Modifier.fillMaxWidth(), label = { Text("Sua anotação") }, minLines = 5, maxLines = 12)
            }
        },
        confirmButton = { TextButton(onClick = { onSave(note.copy(text = text, topicId = topicId)) }, enabled = text.isNotBlank()) { Text("Salvar") } },
        dismissButton = {
            Row {
                if (note.id != 0L) TextButton(onClick = onDelete) { Text("Excluir") }
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        },
    )
}
