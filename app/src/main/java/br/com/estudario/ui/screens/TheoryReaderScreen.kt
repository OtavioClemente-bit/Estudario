package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.components.EmptyState
import br.com.estudario.ui.components.MarkdownText
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryReaderScreen(viewModel: AppViewModel, theoryId: Long, showInternalTopBar: Boolean = true, initialBlock: Int? = null, onBack: () -> Unit) {
    val theories by viewModel.theories.collectAsState()
    val allMarks by viewModel.theoryMarks.collectAsState()
    val theory = theories.firstOrNull { it.id == theoryId }
    if (theory == null) { EmptyState("Teoria não encontrada", "O livro pode ter sido removido.", "Voltar", onBack); return }
    val blocks = remember(theory.markdown) { br.com.estudario.ui.components.studyBlocks(theory.markdown) }
    val marks = allMarks.filter { it.theoryId == theoryId }
    // Dentro do app o cabeçalho é um item da lista: os blocos começam depois dele.
    val headerOffset = if (showInternalTopBar) 0 else 1
    val startBlock = (initialBlock ?: theory.lastReadBlock).coerceIn(0, (blocks.size - 1).coerceAtLeast(0))
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startBlock + if (startBlock > 0) headerOffset else 0)
    val chapters = remember(blocks) { blocks.withIndex().filter { it.value.trimStart().startsWith("#") }.map { it.index to it.value.trimStart().trimStart('#').trim().lineSequence().first() } }
    var tocOpen by remember { mutableStateOf(false) }
    var textScale by rememberSaveable { mutableFloatStateOf(1f) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val readerPrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    var markHintSeen by remember { mutableStateOf(readerPrefs.getBoolean(MARK_HINT_SEEN, false)) }
    val scope = rememberCoroutineScope()
    val reachedEnd by remember { derivedStateOf { listState.layoutInfo.totalItemsCount > 0 && !listState.canScrollForward } }
    val currentBlock by remember { derivedStateOf { if (reachedEnd) blocks.lastIndex.coerceAtLeast(0) else (listState.firstVisibleItemIndex - headerOffset).coerceIn(0, blocks.lastIndex.coerceAtLeast(0)) } }
    val progress by remember { derivedStateOf { if (blocks.isEmpty()) 0f else if (reachedEnd) 1f else (currentBlock + 1f) / blocks.size } }

    // Retomada visível: quem volta à leitura sabe que caiu onde parou e pode recomeçar.
    LaunchedEffect(theory.id) {
        if (initialBlock == null && startBlock > 0 && blocks.isNotEmpty()) {
            val result = snackbar.showSnackbar("Você voltou para onde parou (${((startBlock + 1) * 100) / blocks.size}%).", actionLabel = "Do início", duration = SnackbarDuration.Long)
            if (result == SnackbarResult.ActionPerformed) listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(theory.id, blocks.size) {
        snapshotFlow { currentBlock }.distinctUntilChanged().collect { viewModel.updateTheoryProgress(theory, it) }
    }

    editingIndex?.let { index ->
        val existing = marks.firstOrNull { it.blockIndex == index }
        var note by remember(index, existing?.id) { mutableStateOf(existing?.note.orEmpty()) }
        AlertDialog(
            onDismissRequest = { editingIndex = null },
            title = { Text(if (existing == null) "Marcar trecho" else "Observação do trecho") },
            text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { Text(blocks.getOrElse(index) { "" }.replace("#", "").take(240), color = MaterialTheme.colorScheme.onSurfaceVariant); OutlinedTextField(note, { note = it }, label = { Text("Sua observação (opcional)") }, minLines = 3, maxLines = 7) } },
            confirmButton = { TextButton(onClick = { viewModel.saveTheoryMark(existing?.copy(note = note) ?: TheoryMarkEntity(theoryId = theoryId, blockIndex = index, quote = blocks[index], note = note)); editingIndex = null }) { Text("Salvar marcação") } },
            dismissButton = { Row { if (existing != null) TextButton(onClick = { viewModel.deleteTheoryMark(existing); editingIndex = null }) { Text("Remover") }; TextButton(onClick = { editingIndex = null }) { Text("Cancelar") } } },
        )
    }

    if (tocOpen) ModalBottomSheet(onDismissRequest = { tocOpen = false }) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Índice", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            chapters.forEach { (blockIndex, title) ->
                val active = chapters.lastOrNull { it.first <= currentBlock }?.first == blockIndex
                Surface(
                    onClick = { tocOpen = false; scope.launch { listState.animateScrollToItem(blockIndex + headerOffset) } },
                    color = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(title, Modifier.weight(1f), fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal)
                        if (marks.any { mark -> mark.blockIndex >= blockIndex && mark.blockIndex < (chapters.firstOrNull { it.first > blockIndex }?.first ?: Int.MAX_VALUE) }) Icon(Icons.Outlined.Bookmark, "Tem marcações", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (showInternalTopBar) Column {
                TopAppBar(
                    title = { Column { Text(theory.title, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis); Text("${(progress * 100).toInt()}% lido • ${marks.size} marcação(ões)", style = MaterialTheme.typography.labelSmall) } },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") } },
                    actions = { if (chapters.size > 1) IconButton(onClick = { tocOpen = true }) { Icon(Icons.Outlined.Toc, "Índice") }; IconButton(onClick = { textScale = (textScale - .1f).coerceAtLeast(.8f) }) { Text("A−", fontWeight = FontWeight.Bold) }; IconButton(onClick = { textScale = (textScale + .1f).coerceAtMost(1.5f) }) { Text("A+", fontWeight = FontWeight.Bold) } },
                )
                LinearProgressIndicator({ progress.coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
        // O progresso já é salvo sozinho; o botão marca o trecho atual para revisar depois no
        // Caderno de estudo, e avisa, em vez de repetir um salvamento que ninguém vê.
        floatingActionButton = {
            val markedHere = marks.any { it.blockIndex == currentBlock }
            ExtendedFloatingActionButton(
                onClick = {
                    if (markedHere || blocks.isEmpty()) editingIndex = currentBlock.takeIf { blocks.isNotEmpty() }
                    else {
                        viewModel.saveTheoryMark(TheoryMarkEntity(theoryId = theoryId, blockIndex = currentBlock, quote = blocks[currentBlock], note = ""))
                        scope.launch { snackbar.showSnackbar("Trecho marcado. Revise em Caderno de estudo.") }
                    }
                },
                icon = { Icon(if (markedHere) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkAdd, null) },
                text = { Text(if (markedHere) "Anotar neste trecho" else "Marcar este ponto") },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // Explica o gesto uma única vez; some ao fechar ou na primeira marcação.
        if (!markHintSeen && blocks.isNotEmpty()) {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(Modifier.padding(start = 14.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.TouchApp, null, tint = MaterialTheme.colorScheme.primary)
                    Text("Dica: segure o dedo sobre um trecho para marcá-lo e anotar. Os marcados ficam no Caderno de estudo.", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { markHintSeen = true; readerPrefs.edit().putBoolean(MARK_HINT_SEEN, true).apply() }) { Icon(Icons.Outlined.Close, "Fechar dica") }
                }
            }
        }
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            if (!showInternalTopBar) item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(theory.title, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        if (chapters.size > 1) IconButton(onClick = { tocOpen = true }) { Icon(Icons.Outlined.Toc, "Índice") }
                        IconButton(onClick = { textScale = (textScale - .1f).coerceAtLeast(.8f) }) { Text("A−", fontWeight = FontWeight.Bold) }
                        IconButton(onClick = { textScale = (textScale + .1f).coerceAtMost(1.5f) }) { Text("A+", fontWeight = FontWeight.Bold) }
                    }
                    Text("${(progress * 100).toInt()}% lido • ${marks.size} marcação(ões)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LinearProgressIndicator({ progress.coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
                }
            }
            blocks.forEachIndexed { index, block ->
                item(key = index) {
                    val mark = marks.firstOrNull { it.blockIndex == index }
                    Row(Modifier.fillMaxWidth().background(if (mark != null) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .55f) else MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp)).padding(start = 12.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.Top) {
                        br.com.estudario.ui.components.StudyMarkdown(
                            block,
                            Modifier.weight(1f).padding(end = 12.dp),
                            textSizeSp = MaterialTheme.typography.bodyLarge.fontSize.value * textScale,
                            onLongPress = { editingIndex = index; markHintSeen = true; readerPrefs.edit().putBoolean(MARK_HINT_SEEN, true).apply() },
                        )
                        // Sem ícone em cada trecho (poluía teorias longas): só o marcado ganha um sinal.
                        if (mark != null) Icon(Icons.Outlined.Bookmark, "Trecho marcado", Modifier.padding(end = 8.dp).size(18.dp), tint = MaterialTheme.colorScheme.tertiary)
                    }
                    if (!mark?.note.isNullOrBlank()) Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(start = 12.dp, top = 4.dp)) { Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { Icon(Icons.Outlined.EditNote, null); Text(mark!!.note, Modifier.weight(1f)) } }
                }
            }
            item {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.TaskAlt, null, Modifier.size(38.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("Fim da teoria", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Ao chegar até aqui, o progresso é registrado automaticamente como 100%.")
                    }
                }
            }
            item { Spacer(Modifier.height(70.dp)) }
        }
        }
    }
}

private const val MARK_HINT_SEEN = "reader_mark_hint_seen"
