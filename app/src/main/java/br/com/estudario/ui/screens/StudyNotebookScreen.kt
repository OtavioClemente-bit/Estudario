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
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Close
import androidx.compose.foundation.verticalScroll
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

/** Tudo o que a pessoa guardou de um tópico, junto. */
private class TopicBag(
    val marks: List<br.com.estudario.data.local.TheoryMarkEntity>,
    val notes: List<UserNoteEntity>,
    val tips: List<br.com.estudario.data.local.TopicSnippetEntity>,
    val cards: List<br.com.estudario.data.local.TopicSnippetEntity>,
    val decks: List<SummaryEntity>,
    val questions: List<br.com.estudario.data.local.QuestionWithOptions>,
) {
    val markNotes get() = marks.filter { it.note.isNotBlank() }
    val total get() = marks.size + notes.size + tips.size + cards.size + decks.size + questions.size
}

/**
 * Caderno de estudo, organizado como a pessoa estuda: escolhe a matéria, depois o tópico (Java,
 * dentro de Linguagens de Programação), e ali está tudo o que guardou dele, junto: grifos,
 * anotações, dicas, flashcards e questões. No topo, a revisão completa passa por todos os grifos,
 * todos os flashcards etc., um por um.
 */
@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun StudyNotebookScreen(
    viewModel: AppViewModel,
    onOpenTheory: (theoryId: Long, block: Int) -> Unit,
    onOpenTopic: (Long) -> Unit,
    onTrainFavorites: () -> Unit,
    onTrainTopicFavorites: (Long) -> Unit = { onTrainFavorites() },
) {
    val marksAll by viewModel.theoryMarks.collectAsState()
    // Filtro de cor dos grifos (null = todas): cai muito, entendi, revisar.
    var colorFilter by rememberSaveable { mutableStateOf<Int?>(null) }
    val marks = marksAll.filter { colorFilter == null || it.color == colorFilter }
    var markCards by remember { mutableStateOf<List<br.com.estudario.data.local.TheoryMarkEntity>?>(null) }
    val theories by viewModel.theories.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    // O tópico aberto (-1 = itens sem tópico); null = a lista de matérias.
    var openTopic by rememberSaveable { mutableStateOf<Long?>(null) }
    var editingNote by remember { mutableStateOf<UserNoteEntity?>(null) }
    var editingMarkNote by remember { mutableStateOf<br.com.estudario.data.local.TheoryMarkEntity?>(null) }
    var viewing by remember { mutableStateOf<Any?>(null) }
    var deckFor by remember { mutableStateOf<SummaryEntity?>(null) }
    var cardsFor by remember { mutableStateOf<Pair<String, List<br.com.estudario.data.local.TopicSnippetEntity>>?>(null) }
    var reviewing by remember { mutableStateOf<Pair<String, List<ReviewCard>>?>(null) }
    val expanded = remember { mutableStateListOf<Long>() }

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
    fun markTopic(m: br.com.estudario.data.local.TheoryMarkEntity) = theoryById[m.theoryId]?.topicId

    // Tudo agrupado por tópico (-1 = sem tópico).
    val bags: Map<Long, TopicBag> = run {
        val ids = (marks.map { markTopic(it) } + notes.map { it.topicId } + tips.map { it.topicId } + savedCards.map { it.topicId } + savedDecks.map { it.topicId } + favoriteQuestions.map { it.question.topicId })
            .map { it ?: -1L }.toSet()
        ids.associateWith { id ->
            fun same(t: Long?) = (t ?: -1L) == id
            TopicBag(
                marks.filter { same(markTopic(it)) }.sortedBy { it.blockIndex },
                notes.filter { same(it.topicId) },
                tips.filter { same(it.topicId) },
                savedCards.filter { same(it.topicId) },
                savedDecks.filter { same(it.topicId) },
                favoriteQuestions.filter { same(it.question.topicId) },
            )
        }
    }
    fun order(topicId: Long?) = place(topicId?.takeIf { it >= 0 }).let { (it.subject?.position ?: Int.MAX_VALUE).toLong() * 100_000L + (it.topic?.position ?: 0) }

    // ---- Revisão um por um ----
    fun markCard(m: br.com.estudario.data.local.TheoryMarkEntity, kicker: String = "GRIFO") =
        ReviewCard(kicker, place(markTopic(m)).title, plainPreview(m.quote), m.note.ifBlank { null }) { onOpenTheory(m.theoryId, m.blockIndex) }
    fun noteCard(n: UserNoteEntity) = ReviewCard("ANOTAÇÃO", place(n.topicId).title, null, n.text)
    fun tipCard(t: br.com.estudario.data.local.TopicSnippetEntity) = ReviewCard(
        when (t.kind) { SnippetKind.PEGADINHA -> "PEGADINHA"; SnippetKind.RECUPERACAO -> "PERGUNTA"; else -> "DICA" },
        place(t.topicId).title, null, listOfNotNull(t.text, t.answer?.takeIf { it.isNotBlank() }?.let { "Resposta: $it" }).joinToString("\n\n"),
    )
    fun bagCards(bag: TopicBag) = bag.marks.map { markCard(it) } + bag.notes.map(::noteCard) + bag.tips.map(::tipCard)
    val orderedBags = bags.entries.sortedBy { order(it.key) }

    reviewing?.let { (title, cards) -> NotebookReview(title, cards) { reviewing = null } }
    markCards?.let { list ->
        MarkCardsReview(list, { m -> place(markTopic(m)).title }, onColor = { m, c -> viewModel.saveTheoryMark(m.copy(color = c)) }) { markCards = null }
    }
    deckFor?.let { deck -> SummaryFlashcardDeck(viewModel, deck, topicById[deck.topicId]?.title ?: deck.title, onDismiss = { deckFor = null }) }
    cardsFor?.let { (title, list) ->
        FlashcardDeckDialog(
            title = title,
            cards = list.map(SavedFlashcards::card),
            saved = false,
            onToggleSave = null,
            onDismiss = { cardsFor = null },
            isCardSaved = { card -> list.any { it.text == card.front && it.answer.orEmpty() == card.back } },
            onToggleCard = { card -> list.firstOrNull { it.text == card.front && it.answer.orEmpty() == card.back }?.let(viewModel::deleteSnippet) },
        )
    }
    // Nota feita num trecho da teoria: edita só a nota; apagar tira a nota e mantém o grifo.
    editingMarkNote?.let { mark ->
        br.com.estudario.ui.components.NotePaperEditor(
            initialText = mark.note, isNew = mark.note.isBlank(), createdAt = mark.createdAt,
            onDismiss = { editingMarkNote = null },
            onSave = { text, _ -> viewModel.saveTheoryMark(mark.copy(note = text)); editingMarkNote = null },
            onDelete = if (mark.note.isNotBlank()) ({ viewModel.saveTheoryMark(mark.copy(note = "")); editingMarkNote = null }) else null,
            quote = plainPreview(mark.quote),
            fixedPlace = topicById[markTopic(mark)]?.title ?: "Teoria",
            deleteLabel = "Apagar a nota (o grifo fica)",
        )
    }
    when (val v = viewing) {
        is UserNoteEntity -> br.com.estudario.ui.components.NoteViewSheet(
            text = v.text, createdAt = v.createdAt, place = place(v.topicId).title,
            onDismiss = { viewing = null },
            onEdit = { viewing = null; editingNote = v },
            onDelete = { viewModel.deleteNote(v); viewing = null },
        )
        is br.com.estudario.data.local.TheoryMarkEntity -> br.com.estudario.ui.components.NoteViewSheet(
            text = v.note, createdAt = v.createdAt, place = place(markTopic(v)).title,
            onDismiss = { viewing = null },
            onEdit = { viewing = null; editingMarkNote = v },
            onDelete = { viewModel.saveTheoryMark(v.copy(note = "")); viewing = null },
            quote = plainPreview(v.quote),
            onOpen = { viewing = null; onOpenTheory(v.theoryId, v.blockIndex) },
        )
    }
    editingNote?.let { note ->
        NoteEditor(note, topics, onDismiss = { editingNote = null }, onSave = { viewModel.saveNote(it); editingNote = null }, onDelete = { viewModel.deleteNote(note); editingNote = null })
    }

    val opened = openTopic
    val bag = opened?.let { bags[it] }
    androidx.activity.compose.BackHandler(enabled = opened != null) { openTopic = null }

    // ============================== O TÓPICO ABERTO ==============================
    if (opened != null && bag != null) {
        val here = place(opened.takeIf { it >= 0 })
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = { editingNote = UserNoteEntity(topicId = here.topic?.id, text = "") }, icon = { Icon(Icons.Outlined.Add, null) }, text = { Text("Anotar") })
            },
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item(key = "head") {
                    TopicBagHeader(
                        subject = here.subject?.name ?: "Geral",
                        title = here.title,
                        bag = bag,
                        onBack = { openTopic = null },
                        onReview = bagCards(bag).takeIf { it.isNotEmpty() }?.let { cards -> { reviewing = here.title to cards } },
                        onCards = bag.cards.takeIf { it.isNotEmpty() }?.let { list -> { cardsFor = here.title to list } },
                        onQuestions = here.topic?.takeIf { bag.questions.isNotEmpty() }?.let { t -> { onTrainTopicFavorites(t.id) } },
                        onOpenContent = here.topic?.let { t -> { onOpenTopic(t.id) } },
                    )
                }
                val topicMarksAll = marksAll.filter { (markTopic(it) ?: -1L) == opened }
                if (topicMarksAll.isNotEmpty()) item(key = "colors") { ColorFilterChips(topicMarksAll, colorFilter) { colorFilter = it } }
                if (bag.marks.isNotEmpty()) {
                    item(key = "s-marks") { BagSection(Icons.Outlined.BorderColor, "Grifos", bag.marks.size) }
                    bag.marks.forEach { mark ->
                        item(key = "m-${mark.id}") {
                            ElevatedCard(onClick = { if (mark.note.isNotBlank()) viewing = mark else onOpenTheory(mark.theoryId, mark.blockIndex) }, modifier = Modifier.fillMaxWidth()) {
                                Row(Modifier.height(IntrinsicSize.Min)) {
                                    Box(Modifier.width(5.dp).fillMaxHeight().background(br.com.estudario.ui.components.MarkColors.of(mark.color).color))
                                    Column(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(plainPreview(mark.quote), maxLines = 6, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                        if (mark.note.isNotBlank()) Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(10.dp)) {
                                            Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(Icons.Outlined.EditNote, null, Modifier.size(18.dp))
                                                Text(br.com.estudario.ui.components.NoteTags.split(mark.note).second, style = MaterialTheme.typography.bodySmall, maxLines = 3, overflow = TextOverflow.Ellipsis)
                                            }
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                            Text("Abrir na teoria", Modifier.clickable { onOpenTheory(mark.theoryId, mark.blockIndex) }, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                                            if (mark.note.isBlank()) Text("Anotar", Modifier.clickable { editingMarkNote = mark }, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteTheoryMark(mark) }) { Icon(Icons.Outlined.DeleteOutline, "Tirar grifo", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                                }
                            }
                        }
                    }
                }
                if (bag.notes.isNotEmpty()) {
                    item(key = "s-notes") { BagSection(Icons.Outlined.EditNote, "Anotações", bag.notes.size) }
                    bag.notes.sortedByDescending { it.createdAt }.forEach { note ->
                        item(key = "n-${note.id}") { br.com.estudario.ui.components.NotePaperCard(note.text, note.createdAt, onClick = { viewing = note }) }
                    }
                }
                if (bag.tips.isNotEmpty()) {
                    item(key = "s-tips") { BagSection(Icons.Outlined.Lightbulb, "Dicas e pegadinhas", bag.tips.size) }
                    bag.tips.sortedBy { it.kind }.forEach { snippet ->
                        item(key = "t-${snippet.id}") {
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
                }
                if (bag.cards.isNotEmpty() || bag.decks.isNotEmpty()) {
                    item(key = "s-cards") { BagSection(Icons.Outlined.Style, "Flashcards", bag.cards.size + bag.decks.size) }
                    bag.decks.forEach { deck ->
                        item(key = "d-${deck.id}") {
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
                        }
                    }
                    bag.cards.forEach { card ->
                        item(key = "c-${card.id}") {
                            RecallCard(card.text, card.answer, revealKey = card.id) {
                                IconButton(onClick = { viewModel.deleteSnippet(card) }) { Icon(Icons.Outlined.DeleteOutline, "Tirar dos salvos") }
                            }
                        }
                    }
                }
                if (bag.questions.isNotEmpty()) {
                    item(key = "s-questions") { BagSection(Icons.Outlined.Quiz, "Questões salvas", bag.questions.size) }
                    bag.questions.forEach { row ->
                        item(key = "q-${row.question.id}") {
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(start = 14.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.Top) {
                                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        val meta = listOfNotNull(row.question.board, row.question.year?.toString(), row.question.difficulty?.name?.lowercase()?.replaceFirstChar(Char::uppercase))
                                        if (meta.isNotEmpty()) Text(meta.joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                        Text(plainPreview(row.question.statement), maxLines = 4, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                                        if (row.question.answerCount > 0) Text("${row.question.correctCount} acerto(s) em ${row.question.answerCount} tentativa(s)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.toggleQuestionFavorite(row.question) }) { Icon(Icons.Outlined.Favorite, "Tirar dos salvos", tint = MaterialTheme.colorScheme.primary) }
                                }
                            }
                        }
                    }
                }
            }
        }
        return
    }
    if (opened != null && bag == null) openTopic = null

    // ============================== AS MATÉRIAS ==============================
    val allCards = orderedBags.flatMap { it.value.cards }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { editingNote = UserNoteEntity(text = "") }, icon = { Icon(Icons.Outlined.Add, null) }, text = { Text("Nova anotação") })
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val total = bags.values.sumOf { it.total }
            val subjectsWithItems = bags.keys.mapNotNull { place(it.takeIf { id -> id >= 0 }).subject?.id }.distinct().size
            item(key = "hero") { NotebookHero(total, subjectsWithItems) }
            if (total > 0) item(key = "review-all") {
                // Revisão completa: passar por todos os grifos, todas as anotações, todos os flashcards...
                val allMarks = orderedBags.flatMap { it.value.marks }
                val allNotes = orderedBags.flatMap { it.value.notes }
                val allTips = orderedBags.flatMap { it.value.tips }
                ReviewAllCard(
                    listOfNotNull(
                        Triple(Icons.Outlined.PlayArrow, "Tudo, um por um", (allMarks.size + allNotes.size + allTips.size)).takeIf { it.third > 0 }?.let { it to { reviewing = "Revisão completa" to orderedBags.flatMap { e -> bagCards(e.value) } } },
                        Triple(Icons.Outlined.BorderColor, "Grifos", allMarks.size).takeIf { it.third > 0 }?.let { it to { reviewing = "Todos os grifos" to allMarks.map { m -> markCard(m) } } },
                        br.com.estudario.ui.components.MarkColors.dueToday(marksAll).let { due -> Triple(Icons.Outlined.Style, "Grifos de hoje, em cartão", due.size).takeIf { it.third > 0 }?.let { it to { markCards = due } } },
                        Triple(Icons.Outlined.EditNote, "Anotações", allNotes.size + allMarks.count { it.note.isNotBlank() }).takeIf { it.third > 0 }?.let { it to { reviewing = "Todas as anotações" to (allNotes.map(::noteCard) + allMarks.filter { m -> m.note.isNotBlank() }.map { m -> markCard(m, "NOTA NUM TRECHO") }) } },
                        Triple(Icons.Outlined.Lightbulb, "Dicas", allTips.size).takeIf { it.third > 0 }?.let { it to { reviewing = "Todas as dicas" to allTips.map(::tipCard) } },
                        Triple(Icons.Outlined.Style, "Flashcards", allCards.size).takeIf { it.third > 0 }?.let { it to { cardsFor = "Todos os flashcards" to allCards } },
                        Triple(Icons.Outlined.Quiz, "Questões", favoriteQuestions.size).takeIf { it.third > 0 }?.let { it to onTrainFavorites },
                    ),
                )
            }
            if (marksAll.isNotEmpty()) item(key = "colors") { ColorFilterChips(marksAll, colorFilter) { colorFilter = it } }
            item(key = "search") {
                OutlinedTextField(
                    query, { query = it }, Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    placeholder = { Text("Buscar matéria, tópico ou o que você guardou") },
                    singleLine = true, shape = RoundedCornerShape(16.dp),
                )
            }
            // Busca: tópicos cujo nome ou conteúdo guardado tem o texto.
            fun bagMatches(id: Long, b: TopicBag): Boolean {
                if (needle.isEmpty()) return true
                val p = place(id.takeIf { it >= 0 })
                return matches(p.title, p.subtitle) || b.marks.any { matches(it.quote, it.note) } || b.notes.any { matches(it.text) } ||
                    b.tips.any { matches(it.text, it.answer) } || b.cards.any { matches(it.text, it.answer) } || b.questions.any { matches(it.question.statement) }
            }
            val visible = orderedBags.filter { bagMatches(it.key, it.value) }
            if (total == 0) item(key = "empty") { EmptyHint(Icons.Outlined.EditNote, "Seu caderno está em branco", "Na leitura da teoria, grife trechos e escreva notas. Salve questões, dicas e flashcards nos tópicos. Tudo aparece aqui, por matéria.") }
            else if (visible.isEmpty()) item(key = "nothing") { EmptyHint(Icons.Outlined.Search, "Nada encontrado", "Nenhuma matéria, tópico ou item guardado tem esse texto.") }
            item(key = "label") { if (visible.isNotEmpty()) Text("SUAS MATÉRIAS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp)) }
            visible.groupBy { place(it.key.takeIf { id -> id >= 0 }).subject }.forEach { (subject, entries) ->
                val sid = subject?.id ?: -1L
                // Buscando, as matérias já vêm abertas.
                val open = needle.isNotEmpty() || sid in expanded
                item(key = "subj-$sid") {
                    SubjectCard(
                        name = subject?.name ?: "Geral",
                        bags = entries.map { it.value },
                        open = open,
                        onToggle = { if (sid in expanded) expanded.remove(sid) else expanded.add(sid) },
                    )
                }
                if (open) entries.forEach { (id, b) ->
                    item(key = "topic-$sid-$id") {
                        TopicRow(place(id.takeIf { it >= 0 }).title, b) { openTopic = id }
                    }
                }
            }
        }
    }
}

/** Uma linha de contagens por tipo ("12 grifos · 3 notas · 5 cards"). */
private fun bagSummary(bags: List<TopicBag>): String {
    val parts = listOf(
        bags.sumOf { it.marks.size } to ("grifo" to "grifos"),
        bags.sumOf { it.notes.size } to ("anotação" to "anotações"),
        bags.sumOf { it.tips.size } to ("dica" to "dicas"),
        bags.sumOf { it.cards.size + it.decks.size } to ("flashcard" to "flashcards"),
        bags.sumOf { it.questions.size } to ("questão" to "questões"),
    ).filter { it.first > 0 }
    return parts.joinToString(" · ") { (n, w) -> "$n ${if (n == 1) w.first else w.second}" }
}

/** A matéria: o desenho dela, o nome, o que tem guardado e quantos tópicos. Toca para abrir os tópicos. */
@Composable
private fun SubjectCard(name: String, bags: List<TopicBag>, open: Boolean, onToggle: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val color = br.com.estudario.ui.components.subjectAccentColor(name, br.com.estudario.ui.theme.estudarioColors().subjectPalette)
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = if (open) color.copy(alpha = .10f) else scheme.surfaceContainerHigh,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (open) color.copy(alpha = .45f) else scheme.outlineVariant.copy(alpha = .5f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(start = 12.dp, end = 8.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(name), color, 50.dp)
            Column(Modifier.weight(1f).padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(bagSummary(bags), style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("${bags.size} ${if (bags.size == 1) "tópico" else "tópicos"}", style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
            }
            Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (open) "Fechar" else "Abrir")
        }
    }
}

/** O tópico dentro da matéria aberta: o nome, o que tem guardado, e a seta para abrir tudo dele. */
@Composable
private fun TopicRow(title: String, bag: TopicBag, onOpen: () -> Unit) {
    Surface(onClick = onOpen, shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .6f)), modifier = Modifier.fillMaxWidth().padding(start = 18.dp)) {
        Row(Modifier.padding(start = 14.dp, end = 8.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(bagSummary(listOf(bag)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                Text("${bag.total}", Modifier.padding(horizontal = 10.dp, vertical = 2.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
            }
            Icon(Icons.Outlined.ChevronRight, "Abrir", Modifier.padding(start = 4.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

/** A revisão completa: um botão por tipo (todos os grifos, todos os flashcards...) e "tudo, um por um". */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ReviewAllCard(actions: List<Pair<Triple<ImageVector, String, Int>, () -> Unit>>) {
    if (actions.isEmpty()) return
    val scheme = MaterialTheme.colorScheme
    Surface(shape = RoundedCornerShape(20.dp), color = scheme.surfaceContainerHigh, border = androidx.compose.foundation.BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = .5f)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Revisão completa", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
            Text("Passe por tudo o que guardou, de todas as matérias, na ordem do edital.", style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
            androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                actions.forEachIndexed { i, (info, run) ->
                    val (icon, label, count) = info
                    val first = i == 0
                    Surface(onClick = run, shape = RoundedCornerShape(50), color = if (first) scheme.primary else scheme.surface, border = if (first) null else androidx.compose.foundation.BorderStroke(1.dp, scheme.outlineVariant)) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(icon, null, Modifier.size(18.dp), tint = if (first) scheme.onPrimary else scheme.primary)
                            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = if (first) scheme.onPrimary else scheme.onSurface)
                            Text("$count", style = MaterialTheme.typography.labelMedium, color = if (first) scheme.onPrimary.copy(alpha = .8f) else scheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

/** O topo do tópico aberto: voltar, a matéria, o tópico, o resumo e as ações de estudo. */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun TopicBagHeader(
    subject: String,
    title: String,
    bag: TopicBag,
    onBack: () -> Unit,
    onReview: (() -> Unit)?,
    onCards: (() -> Unit)?,
    onQuestions: (() -> Unit)?,
    onOpenContent: (() -> Unit)?,
) {
    val scheme = MaterialTheme.colorScheme
    val color = br.com.estudario.ui.components.subjectAccentColor(subject, br.com.estudario.ui.theme.estudarioColors().subjectPalette)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 4.dp)) {
            Icon(Icons.Outlined.ChevronLeft, null); Text("Todas as matérias")
        }
        Surface(shape = RoundedCornerShape(22.dp), color = color.copy(alpha = .12f), border = androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = .4f)), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(subject), color, 56.dp)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(subject.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(bagSummary(listOf(bag)), style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
                    }
                }
                if (onReview != null) Button(onClick = onReview, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Revisar este tópico, um por um")
                }
                androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (onCards != null) androidx.compose.material3.OutlinedButton(onClick = onCards) { Icon(Icons.Outlined.Style, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Praticar flashcards") }
                    if (onQuestions != null) androidx.compose.material3.OutlinedButton(onClick = onQuestions) { Icon(Icons.Outlined.Quiz, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Treinar questões") }
                    if (onOpenContent != null) androidx.compose.material3.OutlinedButton(onClick = onOpenContent) { Icon(Icons.Outlined.MenuBook, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Abrir conteúdo") }
                }
            }
        }
    }
}

/** O título de uma parte do tópico aberto (Grifos, Anotações...). */
@Composable
private fun BagSection(icon: ImageVector, label: String, count: Int) {
    Row(Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
            Text("$count", Modifier.padding(horizontal = 8.dp, vertical = 1.dp), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}

/** Um cartão do modo revisão do Caderno. */
internal data class ReviewCard(val kicker: String, val place: String, val quote: String?, val body: String?, val open: (() -> Unit)? = null)

/**
 * Revisar o Caderno um item por vez, como um fichário: o trecho grifado, a nota, a dica, em tela
 * cheia e na ordem do edital. Anterior / Próximo, e ir para a teoria quando quiser o contexto.
 */
@Composable
private fun NotebookReview(title: String, cards: List<ReviewCard>, onClose: () -> Unit) {
    br.com.estudario.ui.HoldCelebrations()
    var index by remember(cards) { mutableStateOf(0) }
    val card = cards.getOrNull(index) ?: return
    val scheme = MaterialTheme.colorScheme
    androidx.compose.ui.window.Dialog(onDismissRequest = onClose, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = scheme.surface) {
            Column(Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 12.dp, bottom = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Fechar revisão") }
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("${index + 1} de ${cards.size}", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                    }
                }
                androidx.compose.material3.LinearProgressIndicator(
                    progress = { (index + 1f) / cards.size },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(6.dp),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = scheme.primaryContainer) {
                        Text(card.kicker, Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                    Text(card.place, style = MaterialTheme.typography.labelLarge, color = scheme.primary, fontWeight = FontWeight.Bold)
                    if (!card.quote.isNullOrBlank()) Row(Modifier.fillMaxWidth().background(Color(0x33FFC857), RoundedCornerShape(12.dp)).height(IntrinsicSize.Min)) {
                        Box(Modifier.width(5.dp).fillMaxHeight().background(Color(0xFFF2A900)))
                        Text(card.quote, Modifier.padding(14.dp), style = MaterialTheme.typography.bodyLarge.copy(lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.15f))
                    }
                    if (!card.body.isNullOrBlank()) {
                        if (card.quote != null) Text("SUA NOTA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = scheme.onSurfaceVariant)
                        br.com.estudario.ui.components.StudyInlineText(br.com.estudario.ui.components.NoteTags.split(card.body).let { (tag, body) -> (tag?.let { "[$it] " } ?: "") + body }, style = MaterialTheme.typography.bodyLarge)
                    }
                    card.open?.let { open -> TextButton(onClick = { onClose(); open() }) { Text("Abrir na teoria") } }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    androidx.compose.material3.OutlinedButton(onClick = { index-- }, enabled = index > 0, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Anterior") }
                    Button(onClick = { if (index < cards.lastIndex) index++ else onClose() }, modifier = Modifier.weight(1f).height(52.dp)) { Text(if (index < cards.lastIndex) "Próximo" else "Concluir") }
                }
            }
        }
    }
}

/** O topo do Caderno: quanto já está guardado, em quantas matérias, e o convite para revisar. */
@Composable
private fun NotebookHero(total: Int, subjects: Int) {
    val scheme = MaterialTheme.colorScheme
    Surface(shape = RoundedCornerShape(20.dp), color = scheme.primary, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            br.com.estudario.ui.assistant.Folha(64.dp, mood = br.com.estudario.ui.assistant.FolhaMood.HAPPY)
            Column(Modifier.weight(1f)) {
                Text(if (total == 0) "Seu caderno está em branco" else "$total ${if (total == 1) "coisa guardada" else "coisas guardadas"}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = scheme.onPrimary)
                Text(
                    if (total == 0) "Grife trechos, salve questões e escreva anotações: tudo vem para cá, arrumado por matéria."
                    else "Em $subjects ${if (subjects == 1) "matéria" else "matérias"}. Abra uma matéria ou revise tudo um por um antes da prova.",
                    style = MaterialTheme.typography.bodySmall, color = scheme.onPrimary.copy(alpha = .85f),
                )
            }
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
/** A anotação livre abre na folha de caderno do Estudário (a mesma da nota de um trecho). */
@Composable
internal fun NoteEditor(note: UserNoteEntity, topics: List<TopicEntity>, onDismiss: () -> Unit, onSave: (UserNoteEntity) -> Unit, onDelete: () -> Unit) {
    br.com.estudario.ui.components.NotePaperEditor(
        initialText = note.text,
        isNew = note.id == 0L,
        createdAt = note.createdAt,
        onDismiss = onDismiss,
        onSave = { text, topicId -> onSave(note.copy(text = text, topicId = topicId)) },
        onDelete = if (note.id != 0L) onDelete else null,
        topics = topics,
        initialTopicId = note.topicId,
    )
}

/** Filtrar os grifos por cor: todas, cai muito, entendi, revisar (com a contagem de cada uma). */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ColorFilterChips(marks: List<br.com.estudario.data.local.TheoryMarkEntity>, selected: Int?, onPick: (Int?) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("GRIFOS POR COR", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant)
        androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorChip("Todas", null, marks.size, selected == null) { onPick(null) }
            br.com.estudario.ui.components.MarkColors.all.forEach { tone ->
                ColorChip(tone.label, tone.color, marks.count { it.color == tone.id }, selected == tone.id) { onPick(if (selected == tone.id) null else tone.id) }
            }
        }
    }
}

@Composable
private fun ColorChip(label: String, color: Color?, count: Int, on: Boolean, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Surface(onClick = onClick, shape = RoundedCornerShape(50), color = if (on) scheme.primaryContainer else scheme.surface, border = androidx.compose.foundation.BorderStroke(1.dp, if (on) scheme.primary else scheme.outlineVariant)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (color != null) Box(Modifier.size(11.dp).background(color, RoundedCornerShape(50)))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text("$count", style = MaterialTheme.typography.labelMedium, color = scheme.onSurfaceVariant)
        }
    }
}

/**
 * Os grifos voltando como cartão, igual aos flashcards: aparece o tópico, o trecho fica escondido até
 * a pessoa tentar lembrar; "Lembrei" pinta de verde (entendi) quando estava rosa, e "Não lembrei" pinta
 * de rosa (revisar), que volta todo dia até virar verde.
 */
@Composable
private fun MarkCardsReview(
    cards: List<br.com.estudario.data.local.TheoryMarkEntity>,
    topicOf: (br.com.estudario.data.local.TheoryMarkEntity) -> String,
    onColor: (br.com.estudario.data.local.TheoryMarkEntity, Int) -> Unit,
    onClose: () -> Unit,
) {
    var index by remember(cards) { mutableStateOf(0) }
    var shown by remember(cards) { mutableStateOf(false) }
    var remembered by remember(cards) { mutableStateOf(0) }
    br.com.estudario.ui.HoldCelebrations()
    val scheme = MaterialTheme.colorScheme
    androidx.compose.ui.window.Dialog(onDismissRequest = onClose, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = scheme.surface) {
            val card = cards.getOrNull(index)
            if (card == null) {
                Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    br.com.estudario.ui.assistant.Folha(130.dp, mood = br.com.estudario.ui.assistant.FolhaMood.HAPPY)
                    Spacer(Modifier.height(12.dp))
                    Text("Grifos de hoje revisados!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    Text("Você lembrou de $remembered de ${cards.size}. Os que ficaram em rosa voltam amanhã.", style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onClose) { Text("Fechar") }
                }
                return@Surface
            }
            Column(Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 12.dp, bottom = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Fechar") }
                    Column(Modifier.weight(1f)) {
                        Text("Grifos de hoje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${index + 1} de ${cards.size}", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                    }
                }
                androidx.compose.material3.LinearProgressIndicator(progress = { (index + 1f) / cards.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(6.dp), strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = scheme.primaryContainer) {
                        Text("GRIFO", Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                    Text(topicOf(card), style = MaterialTheme.typography.titleMedium, color = scheme.primary, fontWeight = FontWeight.Bold)
                    if (!shown) {
                        Text("Você grifou um trecho deste tópico. Lembra o que ele dizia? Pense um pouco antes de ver.", style = MaterialTheme.typography.bodyLarge, color = scheme.onSurfaceVariant)
                        Surface(onClick = { shown = true }, shape = RoundedCornerShape(16.dp), color = scheme.surfaceVariant, modifier = Modifier.fillMaxWidth().height(140.dp)) {
                            Box(contentAlignment = Alignment.Center) { Text("Toque para ver o trecho", fontWeight = FontWeight.Bold, color = scheme.primary) }
                        }
                    } else {
                        Row(Modifier.fillMaxWidth().background(br.com.estudario.ui.components.MarkColors.of(card.color).color.copy(alpha = .16f), RoundedCornerShape(12.dp)).height(IntrinsicSize.Min)) {
                            Box(Modifier.width(5.dp).fillMaxHeight().background(br.com.estudario.ui.components.MarkColors.of(card.color).color))
                            Text(plainPreview(card.quote), Modifier.padding(14.dp), style = MaterialTheme.typography.bodyLarge)
                        }
                        if (card.note.isNotBlank()) Text("Sua nota: " + br.com.estudario.ui.components.NoteTags.split(card.note).second, style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant)
                    }
                }
                if (shown) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    androidx.compose.material3.OutlinedButton(onClick = { onColor(card, 2); shown = false; index++ }, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Não lembrei") }
                    Button(onClick = { if (card.color == 2) onColor(card, 1); remembered++; shown = false; index++ }, modifier = Modifier.weight(1f).height(52.dp)) { Text("Lembrei") }
                } else Button(onClick = { shown = true }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Mostrar o trecho") }
            }
        }
    }
}
