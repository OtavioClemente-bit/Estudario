package br.com.estudario.ui.screens

import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Style
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.DropdownMenuItem
import br.com.estudario.ui.brand.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import br.com.estudario.ui.brand.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.UserNoteEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.FlashcardDeckDialog
import br.com.estudario.ui.components.RecallCard
import br.com.estudario.ui.components.SavedFlashcards
import br.com.estudario.ui.components.SummaryFlashcardDeck
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class NotebookTab(val label: String, val icon: ImageVector) {
    MARKS("Grifos", Icons.Outlined.BorderColor),
    QUESTIONS("Questões", Icons.Outlined.Quiz),
    FLASHCARDS("Flashcards", Icons.Outlined.Style),
    TIPS("Dicas", Icons.Outlined.Lightbulb),
    NOTES("Anotações", Icons.Outlined.EditNote),
}

/** Onde cada coisa guardada mora: o tópico dela (e a matéria, para o cabeçalho). */
private data class Place(val topic: TopicEntity?, val subject: SubjectEntity?) {
    val key: String get() = topic?.id?.toString() ?: "none"
    val title: String get() = topic?.title ?: "Sem tópico"
    val subtitle: String? get() = subject?.name
}

/**
 * Caderno de estudo: tudo o que a pessoa guardou, separado por tipo (grifos, questões,
 * flashcards, dicas, anotações) e, dentro de cada tipo, pelo tópico de onde veio. Cada tópico é
 * um bloco que abre e fecha, com a ação que faz sentido para ele (treinar, praticar, abrir).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyNotebookScreen(
    viewModel: AppViewModel,
    onOpenTheory: (theoryId: Long, block: Int) -> Unit,
    onOpenTopic: (Long) -> Unit,
    onTrainFavorites: () -> Unit,
    onTrainTopicFavorites: (Long) -> Unit = { onTrainFavorites() },
) {
    val marks by viewModel.theoryMarks.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    var tab by rememberSaveable { mutableStateOf(NotebookTab.MARKS) }
    var query by rememberSaveable { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<UserNoteEntity?>(null) }
    var deckFor by remember { mutableStateOf<SummaryEntity?>(null) }
    var cardsFor by remember { mutableStateOf<Place?>(null) }
    // Blocos fechados pela pessoa (o padrão é tudo aberto).
    val collapsed = remember { mutableStateListOf<String>() }

    val topicById = remember(topics) { topics.associateBy { it.id } }
    val subjectById = remember(subjects) { subjects.associateBy { it.id } }
    val theoryById = remember(theories) { theories.associateBy { it.id } }
    fun place(topicId: Long?): Place {
        val topic = topicId?.let(topicById::get)
        return Place(topic, topic?.let { subjectById[it.subjectId] })
    }
    val needle = remember(query) { fold(query.trim()) }
    fun matches(vararg texts: String?) = needle.isEmpty() || texts.any { it != null && fold(it).contains(needle) }

    val savedCards = snippets.filter(SavedFlashcards::isSavedCard)
    val savedDecks = summaries.filter { it.kind == SummaryKind.RAPIDO && it.isFavorite }
    val favoriteQuestions = questions.filter { it.question.isFavorite }
    val tips = snippets.filter { it.isFavorite && !SavedFlashcards.isSavedCard(it) }
    val counts = mapOf(
        NotebookTab.MARKS to marks.size,
        NotebookTab.QUESTIONS to favoriteQuestions.size,
        NotebookTab.FLASHCARDS to savedCards.size + savedDecks.size,
        NotebookTab.TIPS to tips.size,
        NotebookTab.NOTES to notes.size,
    )

    deckFor?.let { deck -> SummaryFlashcardDeck(viewModel, deck, topicById[deck.topicId]?.title ?: deck.title, onDismiss = { deckFor = null }) }
    cardsFor?.let { target ->
        val cards = savedCards.filter { place(it.topicId).key == target.key }
        FlashcardDeckDialog(
            title = target.title,
            cards = cards.map(SavedFlashcards::card),
            saved = false,
            onToggleSave = null,
            onDismiss = { cardsFor = null },
            isCardSaved = { card -> cards.any { it.text == card.front && it.answer.orEmpty() == card.back } },
            onToggleCard = { card -> cards.firstOrNull { it.text == card.front && it.answer.orEmpty() == card.back }?.let(viewModel::deleteSnippet) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Tudo o que você guardou, arrumado por tópico. Revise antes da prova sem precisar caçar nada.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NotebookTab.entries.forEach { value -> SectionTile(value, counts.getValue(value), tab == value, Modifier.weight(1f)) { tab = value } }
                    }
                    OutlinedTextField(
                        query,
                        { query = it },
                        Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Outlined.Search, null) },
                        placeholder = { Text("Buscar em ${tab.label.lowercase()}") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                    )
                }
            }
            when (tab) {
                NotebookTab.MARKS -> {
                    val rows = marks.filter { matches(it.quote, it.note, topicById[theoryById[it.theoryId]?.topicId]?.title) }
                    if (rows.isEmpty()) emptyItem(NotebookTab.MARKS.icon, "Nenhum grifo ainda", "Na leitura da teoria, toque em qualquer parágrafo e escolha Grifar. Ele aparece aqui, no tópico de onde veio.", needle.isNotEmpty())
                    grouped(rows, { place(theoryById[it.theoryId]?.topicId) }, collapsed, onOpenTopic) { mark ->
                        ElevatedCard(onClick = { onOpenTheory(mark.theoryId, mark.blockIndex) }, modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.height(IntrinsicSize.Min)) {
                                Box(Modifier.width(5.dp).fillMaxHeight().background(Color(0xFFF2A900)))
                                Column(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(plainPreview(mark.quote), maxLines = 6, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                    if (mark.note.isNotBlank()) Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(10.dp)) {
                                        Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Icon(Icons.Outlined.EditNote, null, Modifier.size(18.dp))
                                            Text(mark.note, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                    Text("Abrir na teoria", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                                }
                                IconButton(onClick = { viewModel.deleteTheoryMark(mark) }) { Icon(Icons.Outlined.DeleteOutline, "Tirar grifo", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                            }
                        }
                    }
                }
                NotebookTab.QUESTIONS -> {
                    val rows = favoriteQuestions.filter { matches(it.question.statement, topicById[it.question.topicId]?.title) }
                    if (favoriteQuestions.isNotEmpty() && needle.isEmpty()) item(key = "train-all") {
                        Button(onClick = onTrainFavorites, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Treinar as ${favoriteQuestions.size} questões salvas")
                        }
                    }
                    if (rows.isEmpty()) emptyItem(NotebookTab.QUESTIONS.icon, "Nenhuma questão salva", "No treino, toque no coração das questões que você quer rever: as difíceis, as que sempre caem, as que você errou por detalhe.", needle.isNotEmpty())
                    grouped(rows, { place(it.question.topicId) }, collapsed, onOpenTopic, action = { target ->
                        target.topic?.let { topic -> TextButton(onClick = { onTrainTopicFavorites(topic.id) }) { Text("Treinar") } }
                    }) { row ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.Top) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val meta = listOfNotNull(row.question.board, row.question.year?.toString(), row.question.difficulty?.name?.lowercase()?.replaceFirstChar(Char::uppercase))
                                    if (meta.isNotEmpty()) Text(meta.joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    Text(plainPreview(row.question.statement), maxLines = 4, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                    if (row.question.answerCount > 0) Text(
                                        "${row.question.correctCount} acerto(s) em ${row.question.answerCount} tentativa(s)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleQuestionFavorite(row.question) }) { Icon(Icons.Outlined.Favorite, "Tirar dos salvos", tint = MaterialTheme.colorScheme.primary) }
                            }
                        }
                    }
                }
                NotebookTab.FLASHCARDS -> {
                    val cards = savedCards.filter { matches(it.text, it.answer, topicById[it.topicId]?.title) }
                    val decks = savedDecks.filter { matches(it.title, topicById[it.topicId]?.title) }
                    if (cards.isEmpty() && decks.isEmpty()) emptyItem(NotebookTab.FLASHCARDS.icon, "Nenhum flashcard salvo", "Nos tópicos, abra os Flashcards (aba REVISÃO). Salve o baralho inteiro no marcador do topo, ou só o cartão que interessa em \"Salvar este cartão\".", needle.isNotEmpty())
                    val entries = cards.map { Triple(place(it.topicId), it, null as SummaryEntity?) } + decks.map { Triple(place(it.topicId), null, it) }
                    grouped(entries, { it.first }, collapsed, onOpenTopic, action = { target ->
                        if (cards.any { place(it.topicId).key == target.key }) TextButton(onClick = { cardsFor = target }) { Text("Praticar") }
                    }) { (_, card, deck) ->
                        if (deck != null) {
                            val count = remember(deck.markdown) { br.com.estudario.ui.components.FlashcardParser.parse(deck.markdown).size }
                            ElevatedCard(onClick = { deckFor = deck }, modifier = Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.primary)
                                    Column(Modifier.weight(1f)) {
                                        Text("Baralho completo", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                        Text("$count cartões", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Icon(Icons.Outlined.PlayArrow, "Praticar", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        } else if (card != null) {
                            RecallCard(card.text, card.answer, revealKey = card.id) {
                                IconButton(onClick = { viewModel.deleteSnippet(card) }) { Icon(Icons.Outlined.DeleteOutline, "Tirar dos salvos") }
                            }
                        }
                    }
                }
                NotebookTab.TIPS -> {
                    val rows = tips.filter { matches(it.text, it.answer, topicById[it.topicId]?.title) }
                    if (rows.isEmpty()) emptyItem(NotebookTab.TIPS.icon, "Nenhuma dica salva", "Nos tópicos, na aba DICAS, toque na estrela das dicas, pegadinhas e perguntas de memorização que você quer levar para a véspera da prova.", needle.isNotEmpty())
                    grouped(rows.sortedBy { it.kind }, { place(it.topicId) }, collapsed, onOpenTopic) { snippet ->
                        if (snippet.kind == SnippetKind.RECUPERACAO) {
                            RecallCard(snippet.text, snippet.answer, revealKey = snippet.id) {
                                IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Tirar do caderno", tint = MaterialTheme.colorScheme.primary) }
                            }
                        } else {
                            val trap = snippet.kind == SnippetKind.PEGADINHA
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.Top) {
                                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        KindPill(if (trap) "PEGADINHA" else "DICA", if (trap) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer)
                                        br.com.estudario.ui.components.StudyInlineText(snippet.text, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(onClick = { viewModel.saveSnippet(snippet.copy(isFavorite = false)) }) { Icon(Icons.Outlined.Star, "Tirar do caderno", tint = MaterialTheme.colorScheme.primary) }
                                }
                            }
                        }
                    }
                }
                NotebookTab.NOTES -> {
                    val rows = notes.filter { matches(it.text, it.topicId?.let(topicById::get)?.title) }
                    if (rows.isEmpty()) emptyItem(NotebookTab.NOTES.icon, "Nenhuma anotação ainda", "Escreva com suas palavras: resumos, macetes, dúvidas para tirar. Ligue ao tópico para achar depois.", needle.isNotEmpty())
                    grouped(rows, { place(it.topicId) }, collapsed, onOpenTopic) { note ->
                        ElevatedCard(onClick = { editingNote = note }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(note.text, maxLines = 8, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                Text(dateFormat.format(Date(note.createdAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Agrupa por tópico, na ordem das matérias, com um cabeçalho que abre e fecha o bloco. */
private fun <T> LazyListScope.grouped(
    rows: List<T>,
    placeOf: (T) -> Place,
    collapsed: MutableList<String>,
    onOpenTopic: (Long) -> Unit,
    action: @Composable (Place) -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    val groups = rows.groupBy(placeOf).entries.sortedWith(compareBy({ it.key.topic == null }, { it.key.subject?.position ?: Int.MAX_VALUE }, { it.key.topic?.position ?: 0 }, { it.key.title }))
    groups.forEach { (target, items) ->
        val open = target.key !in collapsed
        item(key = "group-${target.key}") {
            PlaceHeader(target, items.size, open, onToggle = { if (open) collapsed.add(target.key) else collapsed.remove(target.key) }, onOpenTopic, action)
        }
        if (open) items.forEachIndexed { index, row ->
            item(key = "group-${target.key}-$index-${row.hashCode()}") { Box(Modifier.padding(start = 10.dp)) { content(row) } }
        }
    }
}

@Composable
private fun PlaceHeader(target: Place, count: Int, open: Boolean, onToggle: () -> Unit, onOpenTopic: (Long) -> Unit, action: @Composable (Place) -> Unit) {
    Surface(onClick = onToggle, shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(Modifier.padding(start = 14.dp, end = 4.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                target.subtitle?.let { Text(it.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                Text(target.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (target.topic != null && open) Text(
                    "Abrir tópico",
                    Modifier.padding(top = 2.dp).clickable { onOpenTopic(target.topic.id) },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                Text("$count", Modifier.padding(horizontal = 10.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
            action(target)
            Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (open) "Fechar" else "Abrir", Modifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun SectionTile(tab: NotebookTab, count: Int, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val content = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier,
    ) {
        Column(Modifier.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(tab.icon, null, Modifier.size(16.dp), tint = if (selected) content else MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text("$count", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = content)
            }
            Text(tab.label, style = MaterialTheme.typography.labelSmall, color = if (selected) content else MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun KindPill(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(label, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

private fun LazyListScope.emptyItem(icon: ImageVector, title: String, body: String, searching: Boolean) {
    item(key = "empty") {
        if (searching) EmptyHint(Icons.Outlined.Search, "Nada encontrado", "Nenhum item desta seção tem esse texto.")
        else EmptyHint(icon, title, body)
    }
}

/** Busca sem acento e sem diferença de maiúscula. */
private fun fold(value: String): String = Normalizer.normalize(value.lowercase(), Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "")

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

/** Trecho de teoria sem a sintaxe do Markdown, para a prévia do caderno. */
internal fun plainPreview(markdown: String): String = br.com.estudario.ui.components.plainFormulaText(withoutCodeBlocks(markdown))
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

/**
 * Gráficos, figuras e tabelas de código chegam no material como um bloco de dados. Na prévia do
 * Caderno ninguém quer ler JSON: o bloco vira só o nome dele ("Gráfico: Evolução da inflação").
 */
private val CodeBlock = Regex("""```([^\n]*)\n([\s\S]*?)(```|\z)""")
private val ChartTitle = Regex(""""(?:titulo|title)"\s*:\s*"([^"]+)"""")

private fun withoutCodeBlocks(markdown: String): String = CodeBlock.replace(markdown) { m ->
    val kind = m.groupValues[1].trim().lowercase()
    val name = when (kind) {
        "geometria", "figura" -> "Figura"
        "", "text", "txt" -> "Trecho"
        else -> "Gráfico"
    }
    val title = ChartTitle.find(m.groupValues[2])?.groupValues?.get(1)
    if (title != null) " $name: $title. " else " $name. "
}

@Composable
private fun TopicHeader(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun EmptyHint(@Suppress("UNUSED_PARAMETER") icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, body: String) {
    br.com.estudario.ui.components.EmptyState(title, body)
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
