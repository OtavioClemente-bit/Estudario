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
    var editingMarkNote by remember { mutableStateOf<br.com.estudario.data.local.TheoryMarkEntity?>(null) }
    var deckFor by remember { mutableStateOf<SummaryEntity?>(null) }
    var cardsFor by remember { mutableStateOf<Place?>(null) }
    var reviewing by remember { mutableStateOf<Pair<String, List<ReviewCard>>?>(null) }
    // Blocos fechados pela pessoa (o padrão é tudo aberto).
    val expanded = remember { mutableStateListOf<String>() }

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
        NotebookTab.NOTES to notes.size + marks.count { it.note.isNotBlank() },
    )

    reviewing?.let { (title, cards) -> NotebookReview(title, cards) { reviewing = null } }
    // Os cartões do modo revisão de uma aba (e, se pedido, de uma matéria), na ordem do edital.
    fun reviewCards(target: NotebookTab, subjectId: Long?): List<ReviewCard> {
        fun order(topicId: Long?) = place(topicId).let { (it.subject?.position ?: Int.MAX_VALUE) * 100_000L + (it.topic?.position ?: 0) }
        fun keep(topicId: Long?) = subjectId == null || (place(topicId).subject?.id ?: -1L) == subjectId
        return when (target) {
            NotebookTab.MARKS -> marks.filter { keep(theoryById[it.theoryId]?.topicId) }.sortedWith(compareBy({ order(theoryById[it.theoryId]?.topicId) }, { it.blockIndex })).map { m ->
                ReviewCard("GRIFO", place(theoryById[m.theoryId]?.topicId).title, plainPreview(m.quote), m.note.ifBlank { null }) { onOpenTheory(m.theoryId, m.blockIndex) }
            }
            NotebookTab.NOTES -> notes.filter { keep(it.topicId) }.sortedBy { order(it.topicId) }.map { ReviewCard("ANOTAÇÃO", place(it.topicId).title, null, it.text) } +
                marks.filter { it.note.isNotBlank() && keep(theoryById[it.theoryId]?.topicId) }.sortedBy { order(theoryById[it.theoryId]?.topicId) }.map { m ->
                    ReviewCard("NOTA NUM TRECHO", place(theoryById[m.theoryId]?.topicId).title, plainPreview(m.quote), m.note) { onOpenTheory(m.theoryId, m.blockIndex) }
                }
            NotebookTab.TIPS -> tips.filter { keep(it.topicId) }.sortedBy { order(it.topicId) }.map { t ->
                ReviewCard(when (t.kind) { SnippetKind.PEGADINHA -> "PEGADINHA"; SnippetKind.RECUPERACAO -> "PERGUNTA"; else -> "DICA" }, place(t.topicId).title, null, listOfNotNull(t.text, t.answer?.takeIf { it.isNotBlank() }?.let { "Resposta: $it" }).joinToString("\n\n"))
            }
            else -> emptyList()
        }
    }
    val reviewable = tab == NotebookTab.MARKS || tab == NotebookTab.NOTES || tab == NotebookTab.TIPS
    val onReviewSubject: ((SubjectEntity?) -> Unit)? = if (!reviewable) null else { subject ->
        val cards = reviewCards(tab, subject?.id ?: -1L)
        if (cards.isNotEmpty()) reviewing = ((subject?.name ?: "Geral") + " · " + tab.label) to cards
    }
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
    // Nota feita num trecho da teoria: edita só a nota; apagar tira a nota e mantém o grifo.
    editingMarkNote?.let { mark ->
        br.com.estudario.ui.components.NotePaperEditor(
            initialText = mark.note, isNew = false, createdAt = mark.createdAt,
            onDismiss = { editingMarkNote = null },
            onSave = { text, _ -> viewModel.saveTheoryMark(mark.copy(note = text)); editingMarkNote = null },
            onDelete = { viewModel.saveTheoryMark(mark.copy(note = "")); editingMarkNote = null },
            quote = plainPreview(mark.quote),
            fixedPlace = topicById[theoryById[mark.theoryId]?.topicId]?.title ?: "Teoria",
            deleteLabel = "Apagar a nota (o grifo fica)",
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
                    NotebookHero(counts.values.sum(), subjects = (marks.mapNotNull { theoryById[it.theoryId]?.topicId } + notes.mapNotNull { it.topicId } + favoriteQuestions.map { it.question.topicId } + tips.mapNotNull { it.topicId }).mapNotNull { topicById[it]?.subjectId }.distinct().size)
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
                    // Estudar de verdade: revisar a aba inteira um item por vez, na ordem do edital.
                    if (reviewable && needle.isEmpty() && counts.getValue(tab) > 0) Button(
                        onClick = { reviewing = ("Caderno · " + tab.label) to reviewCards(tab, null) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        Icon(Icons.Outlined.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text("Revisar ${tab.label.lowercase()} um por um")
                    }
                }
            }
            when (tab) {
                NotebookTab.MARKS -> {
                    val rows = marks.filter { matches(it.quote, it.note, topicById[theoryById[it.theoryId]?.topicId]?.title) }
                    if (rows.isEmpty()) emptyItem(NotebookTab.MARKS.icon, "Nenhum grifo ainda", "Na leitura da teoria, toque em qualquer parágrafo e escolha Grifar. Ele aparece aqui, no tópico de onde veio.", needle.isNotEmpty())
                    grouped(rows, { place(theoryById[it.theoryId]?.topicId) }, expanded, onOpenTopic, onReview = onReviewSubject) { mark ->
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
                    grouped(rows, { place(it.question.topicId) }, expanded, onOpenTopic, action = { target ->
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
                    grouped(entries, { it.first }, expanded, onOpenTopic, action = { target ->
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
                    grouped(rows.sortedBy { it.kind }, { place(it.topicId) }, expanded, onOpenTopic, onReview = onReviewSubject) { snippet ->
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
                    // As anotações livres e as notas feitas nos trechos da teoria, juntas e mais novas primeiro.
                    val rows = (notes.filter { matches(it.text, it.topicId?.let(topicById::get)?.title) }.map { it as Any } +
                        marks.filter { it.note.isNotBlank() && matches(it.note, it.quote, topicById[theoryById[it.theoryId]?.topicId]?.title) }.map { it as Any })
                        .sortedByDescending { (it as? UserNoteEntity)?.createdAt ?: (it as br.com.estudario.data.local.TheoryMarkEntity).createdAt }
                    if (rows.isEmpty()) emptyItem(NotebookTab.NOTES.icon, "Nenhuma anotação ainda", "Escreva com suas palavras: macetes, resumos, dúvidas. Na leitura, selecione um trecho e escolha Grifar com nota: ela também aparece aqui.", needle.isNotEmpty())
                    grouped(rows, { if (it is UserNoteEntity) place(it.topicId) else place(theoryById[(it as br.com.estudario.data.local.TheoryMarkEntity).theoryId]?.topicId) }, expanded, onOpenTopic, onReview = onReviewSubject) { row ->
                        if (row is UserNoteEntity) br.com.estudario.ui.components.NotePaperCard(row.text, row.createdAt, onClick = { editingNote = row })
                        else (row as br.com.estudario.data.local.TheoryMarkEntity).let { mark ->
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                br.com.estudario.ui.components.NotePaperCard(mark.note, mark.createdAt, onClick = { editingMarkNote = mark }, quote = plainPreview(mark.quote))
                                Text("Abrir na teoria", Modifier.padding(start = 12.dp).clickable { onOpenTheory(mark.theoryId, mark.blockIndex) }, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Agrupa por matéria (fechada ao entrar, só o cabeçalho com a contagem) e, dentro dela, por tópico
 * na ordem do edital. Com muitas matérias o Caderno fica um índice limpo: abre só o que vai estudar.
 */
private fun <T> LazyListScope.grouped(
    rows: List<T>,
    placeOf: (T) -> Place,
    expanded: MutableList<String>,
    onOpenTopic: (Long) -> Unit,
    action: @Composable (Place) -> Unit = {},
    onReview: ((SubjectEntity?) -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val bySubject = rows.groupBy { placeOf(it).subject }.entries.sortedWith(compareBy({ it.key == null }, { it.key?.position ?: Int.MAX_VALUE }, { it.key?.name ?: "" }))
    bySubject.forEach { (subject, list) ->
        val key = "s-${subject?.id ?: "none"}"
        val open = key in expanded
        val byTopic = list.groupBy(placeOf).entries.sortedWith(compareBy({ it.key.topic == null }, { it.key.topic?.position ?: 0 }, { it.key.title }))
        item(key = "subject-$key") {
            SubjectHeader(subject?.name ?: "Geral", list.size, byTopic.size, open, onToggle = { if (open) expanded.remove(key) else expanded.add(key) }, onReview = onReview?.let { r -> { r(subject) } })
        }
        if (open) byTopic.forEach { (target, items) ->
            item(key = "topic-$key-${target.key}") { TopicLabel(target, items.size, onOpenTopic, action) }
            items.forEachIndexed { index, row ->
                item(key = "row-$key-${target.key}-$index-${row.hashCode()}") { Box(Modifier.padding(start = 12.dp)) { content(row) } }
            }
        }
    }
}

/** Cabeçalho da matéria: a inicial num selo, o nome, quantos itens e tópicos, revisar e abrir. */
@Composable
private fun SubjectHeader(name: String, count: Int, topics: Int, open: Boolean, onToggle: () -> Unit, onReview: (() -> Unit)?) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(18.dp),
        color = if (open) scheme.primaryContainer.copy(alpha = .55f) else scheme.surfaceContainerHigh,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (open) scheme.primary.copy(alpha = .35f) else scheme.outlineVariant.copy(alpha = .5f)),
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
    ) {
        Row(Modifier.padding(start = 12.dp, end = 6.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(scheme.primary, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Text(name.trim().take(1).uppercase(), color = scheme.onPrimary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
            }
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("$count ${if (count == 1) "item" else "itens"} · $topics ${if (topics == 1) "tópico" else "tópicos"}", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
            }
            if (onReview != null) IconButton(onClick = onReview) { Icon(Icons.Outlined.PlayArrow, "Revisar esta matéria", tint = scheme.primary) }
            Icon(if (open) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, if (open) "Fechar" else "Abrir", Modifier.padding(end = 6.dp))
        }
    }
}

/** O tópico dentro da matéria aberta: uma linha discreta com a contagem, abrir e a ação do tipo. */
@Composable
private fun TopicLabel(target: Place, count: Int, onOpenTopic: (Long) -> Unit, action: @Composable (Place) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(start = 12.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50)))
        Text(
            target.title,
            Modifier.weight(1f).padding(start = 8.dp).then(if (target.topic != null) Modifier.clickable { onOpenTopic(target.topic.id) } else Modifier),
            style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis,
        )
        Text("$count", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 6.dp))
        action(target)
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
