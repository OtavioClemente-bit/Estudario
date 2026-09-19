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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryReaderScreen(viewModel: AppViewModel, theoryId: Long, onBack: () -> Unit) {
    val theories by viewModel.theories.collectAsState()
    val allMarks by viewModel.theoryMarks.collectAsState()
    val theory = theories.firstOrNull { it.id == theoryId }
    if (theory == null) { EmptyState("Teoria não encontrada", "O livro pode ter sido removido.", "Voltar", onBack); return }
    val blocks = remember(theory.markdown) { theory.markdown.split(Regex("\\n\\s*\\n")).map(String::trim).filter(String::isNotBlank) }
    val marks = allMarks.filter { it.theoryId == theoryId }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = theory.lastReadBlock.coerceIn(0, (blocks.size - 1).coerceAtLeast(0)))
    var textScale by remember { mutableFloatStateOf(1f) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    val reachedEnd by remember { derivedStateOf { listState.layoutInfo.totalItemsCount > 0 && !listState.canScrollForward } }
    val currentBlock by remember { derivedStateOf { if (reachedEnd) blocks.lastIndex.coerceAtLeast(0) else listState.firstVisibleItemIndex } }
    val progress by remember { derivedStateOf { if (blocks.isEmpty()) 0f else if (reachedEnd) 1f else (listState.firstVisibleItemIndex + 1f) / blocks.size } }

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

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Column { Text(theory.title, maxLines = 1); Text("${(progress * 100).toInt()}% lido • ${marks.size} marcação(ões)", style = MaterialTheme.typography.labelSmall) } },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") } },
                    actions = { IconButton(onClick = { textScale = (textScale - .1f).coerceAtLeast(.8f) }) { Text("A−", fontWeight = FontWeight.Bold) }; IconButton(onClick = { textScale = (textScale + .1f).coerceAtMost(1.5f) }) { Text("A+", fontWeight = FontWeight.Bold) } },
                )
                LinearProgressIndicator({ progress.coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
            }
        },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = { viewModel.updateTheoryProgress(theory, currentBlock) }, icon = { Icon(Icons.Outlined.BookmarkAdded, null) }, text = { Text(if (reachedEnd) "Leitura concluída" else "Salvar página") }) },
    ) { padding ->
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            blocks.forEachIndexed { index, block ->
                item(key = index) {
                    val mark = marks.firstOrNull { it.blockIndex == index }
                    Row(Modifier.fillMaxWidth().background(if (mark != null) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .55f) else MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp)).padding(start = 12.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.Top) {
                        val density = LocalDensity.current
                        CompositionLocalProvider(LocalDensity provides Density(density.density, textScale)) { MarkdownText(block, Modifier.weight(1f)) }
                        IconButton(onClick = { editingIndex = index }) { Icon(if (mark == null) Icons.Outlined.BookmarkAdd else Icons.Outlined.Bookmark, if (mark == null) "Marcar" else "Editar observação", tint = if (mark == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary) }
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
