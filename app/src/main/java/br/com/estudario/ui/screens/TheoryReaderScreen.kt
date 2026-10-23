package br.com.estudario.ui.screens

import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import br.com.estudario.ui.brand.LinearProgressIndicator
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.brand.Icon
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
    // O cabeçalho fica fixo fora da lista (como no modo foco): os blocos começam no item 0.
    val headerOffset = 0
    val startBlock = (initialBlock ?: theory.lastReadBlock).coerceIn(0, (blocks.size - 1).coerceAtLeast(0))
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startBlock + if (startBlock > 0) headerOffset else 0)
    val chapters = remember(blocks) { blocks.withIndex().filter { it.value.trimStart().startsWith("#") }.map { it.index to it.value.trimStart().trimStart('#').trim().lineSequence().first() } }
    var tocOpen by remember { mutableStateOf(false) }
    val uiPrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    // Ajustes de leitura (como num leitor de livros): tamanho e fonte, lembrados entre as aberturas.
    var textScale by remember { mutableFloatStateOf(uiPrefs.getFloat("reader_scale", 1.05f)) }
    var bookFont by remember { mutableStateOf(uiPrefs.getBoolean("reader_book", true)) }
    var readerSettings by remember { mutableStateOf(false) }
    // Anotação aberta: o parágrafo e o trecho (o parágrafo inteiro ou só a parte selecionada).
    var editing by remember { mutableStateOf<Pair<Int, String>?>(null) }
    // Nota aberta para ler (tocando no trecho grifado ou no balão da nota).
    var viewingId by remember { mutableStateOf<Long?>(null) }
    var selected by remember(theoryId) { mutableStateOf<Int?>(null) }
    var reportIndex by remember { mutableStateOf<Int?>(null) }
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
    val snackbar = remember { SnackbarHostState() }
    val readerPrefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences("estudario_ui", android.content.Context.MODE_PRIVATE)
    var markHintSeen by remember { mutableStateOf(readerPrefs.getBoolean(MARK_HINT_SEEN, false)) }
    val scope = rememberCoroutineScope()
    val reachedEnd by remember { derivedStateOf { listState.layoutInfo.totalItemsCount > 0 && !listState.canScrollForward } }
    val currentBlock by remember { derivedStateOf { if (reachedEnd) blocks.lastIndex.coerceAtLeast(0) else (listState.firstVisibleItemIndex - headerOffset).coerceIn(0, blocks.lastIndex.coerceAtLeast(0)) } }
    // Reporta o trecho que está na tela: é o que a pessoa acabou de ler e achou errado.
    val ReportTheoryButton: @Composable () -> Unit = {
        br.com.estudario.ui.components.ReportErrorButton(
            br.com.estudario.data.remote.ReportKind.THEORY,
            excerpt = { blocks.getOrNull(currentBlock).orEmpty().ifBlank { theory.title } },
            topic = theory.title,
        )
    }
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

    reportIndex?.let { index ->
        br.com.estudario.ui.components.ReportErrorDialog(br.com.estudario.data.remote.ReportKind.THEORY, blocks.getOrElse(index) { "" }, theory.title, null, onDismiss = { reportIndex = null })
    }
    viewingId?.let { id ->
        val m = marks.firstOrNull { it.id == id }
        if (m == null) viewingId = null
        else br.com.estudario.ui.components.NoteViewSheet(
            text = m.note, createdAt = m.createdAt, place = theory.title,
            onDismiss = { viewingId = null },
            onEdit = { viewingId = null; editing = m.blockIndex to m.quote },
            onDelete = { viewModel.saveTheoryMark(m.copy(note = "")); viewingId = null; scope.launch { snackbar.showSnackbar("Nota excluída. O trecho continua grifado.") } },
            quote = plainPreview(m.quote),
            onRemoveHighlight = { viewModel.deleteTheoryMark(m); viewingId = null; scope.launch { snackbar.showSnackbar("Grifo e nota tirados.") } },
        )
    }
    editing?.let { (index, quote) ->
        val existing = marks.firstOrNull { it.blockIndex == index && it.quote == quote }
        var note by remember(index, quote, existing?.id) { mutableStateOf(existing?.note.orEmpty()) }
        // A nota do trecho abre na folha de caderno; salvar grifa o trecho e guarda a nota em Anotações.
        br.com.estudario.ui.components.NotePaperEditor(
            initialText = note,
            isNew = existing?.note.isNullOrBlank(),
            createdAt = existing?.createdAt,
            onDismiss = { editing = null },
            onSave = { text, _ ->
                // trecho novo por cima de grifos antigos do parágrafo: ele substitui os que encostam nele
                if (existing == null) marks.filter { it.blockIndex == index && br.com.estudario.text.MarkOverlap.overlaps(blocks.getOrElse(index) { quote }, it.quote, quote) }.forEach { viewModel.deleteTheoryMark(it) }
                viewModel.saveTheoryMark(existing?.copy(note = text) ?: TheoryMarkEntity(theoryId = theoryId, blockIndex = index, quote = quote, note = text))
                editing = null
                scope.launch { snackbar.showSnackbar("Nota salva. Está no Caderno de estudo, em Anotações.") }
            },
            onDelete = existing?.let { e -> { viewModel.deleteTheoryMark(e); editing = null } },
            quote = plainPreview(quote),
            fixedPlace = theory.title,
            deleteLabel = "Tirar o grifo e a nota",
        )
    }

    if (readerSettings) ReaderSettingsSheet(
        scale = textScale, book = bookFont,
        onScale = { textScale = it; uiPrefs.edit().putFloat("reader_scale", it).apply() },
        onBook = { bookFont = it; uiPrefs.edit().putBoolean("reader_book", it).apply() },
        onDismiss = { readerSettings = false },
    )
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
                    title = { Column { Text(theory.title, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis); Text("${(progress * 100).toInt()}% lido • ${marks.size} grifo(s)", style = MaterialTheme.typography.labelSmall) } },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar") } },
                    actions = { if (chapters.size > 1) IconButton(onClick = { tocOpen = true }) { Icon(Icons.Outlined.Toc, "Índice") }; IconButton(onClick = { readerSettings = true }) { Text("Aa", fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Serif) }; ReportTheoryButton() },
                )
                ReadLine(progress)
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
        // Tocar num parágrafo seleciona; a barra de ações só existe enquanto há seleção. Nada de
        // ícone em cada trecho: a leitura fica limpa e o gesto é o mesmo de qualquer leitor de livro.
        bottomBar = {
            val index = selected
            if (index != null && index in blocks.indices) {
                val blockMarks = marks.filter { it.blockIndex == index }
                // Grifo do parágrafo inteiro e grifos de trechos são independentes: ter um trecho grifado
                // não impede grifar outro trecho (segurando e arrastando) nem o parágrafo todo.
                val whole = blockMarks.firstOrNull { it.quote == blocks[index] }
                val pieces = blockMarks.filter { it.quote != blocks[index] }
                SelectionBar(
                    highlighted = whole != null,
                    pieces = pieces.size,
                    onRemovePieces = {
                        pieces.forEach { viewModel.deleteTheoryMark(it) }
                        scope.launch { snackbar.showSnackbar(if (pieces.size > 1) "Trechos grifados removidos." else "Trecho grifado removido.") }
                        selected = null
                    },
                    onHighlight = {
                        if (whole != null) {
                            viewModel.deleteTheoryMark(whole)
                            scope.launch { snackbar.showSnackbar("Grifo do parágrafo removido.") }
                        } else {
                            viewModel.saveTheoryMark(TheoryMarkEntity(theoryId = theoryId, blockIndex = index, quote = blocks[index], note = ""))
                            markHintSeen = true
                            readerPrefs.edit().putBoolean(MARK_HINT_SEEN, true).apply()
                            scope.launch { snackbar.showSnackbar("Grifado. Fica no Caderno de estudo, no tópico desta teoria.") }
                        }
                        selected = null
                    },
                    onNote = { editing = index to (blockMarks.firstOrNull()?.quote ?: blocks[index]); selected = null },
                    onCopy = {
                        clipboard.setText(androidx.compose.ui.text.AnnotatedString(plainPreview(blocks[index])))
                        scope.launch { snackbar.showSnackbar("Trecho copiado.") }
                        selected = null
                    },
                    onReport = { reportIndex = index; selected = null },
                    onClose = { selected = null },
                )
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // Cabeçalho fixo: título, capítulos, ajuste de letra e a linha de progresso não saem do alto ao rolar.
            if (!showInternalTopBar) Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp) {
                Column {
                    Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 4.dp, top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(theory.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            val chapterAt = chapters.indexOfLast { it.first <= currentBlock }
                            Text(
                                listOfNotNull(
                                    if (chapters.size > 1 && chapterAt >= 0) "Capítulo ${chapterAt + 1} de ${chapters.size}" else null,
                                    "${(progress * 100).toInt()}% lido",
                                    if (marks.isNotEmpty()) "${marks.size} grifo(s)" else null,
                                ).joinToString(" • "),
                                style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (chapters.size > 1) IconButton(onClick = { tocOpen = true }) { Icon(Icons.Outlined.Toc, "Capítulos") }
                        IconButton(onClick = { readerSettings = true }) { Text("Aa", fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Serif) }
                        ReportTheoryButton()
                    }
                    Spacer(Modifier.height(6.dp))
                    ReadLine(progress)
                }
            }
            // Explica o gesto uma única vez; some ao fechar ou na primeira marcação.
        if (!markHintSeen && blocks.isNotEmpty()) {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(Modifier.padding(start = 14.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    br.com.estudario.ui.assistant.Folha(40.dp, mood = br.com.estudario.ui.assistant.FolhaMood.TALKING)
                    Text("Segure uma palavra e arraste para grifar só um trecho. Tocar no parágrafo grifa ele inteiro. Tudo vai para o Caderno de estudo.", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { markHintSeen = true; readerPrefs.edit().putBoolean(MARK_HINT_SEEN, true).apply() }) { Icon(Icons.Outlined.Close, "Fechar dica") }
                }
            }
        }
        androidx.compose.runtime.CompositionLocalProvider(br.com.estudario.ui.components.LocalReaderBook provides bookFont) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            blocks.forEachIndexed { index, block ->
                item(key = index) {
                    val blockMarks = marks.filter { it.blockIndex == index }
                    // Grifo do parágrafo inteiro (feito tocando nele) ou de trechos (feito selecionando).
                    val mark = blockMarks.firstOrNull { it.quote == block }
                    val partial = blockMarks.filter { it.quote != block }.map { it.quote }
                    val isSelected = selected == index
                    val highlighter = estudarioHighlighter()
                    // Grifo = faixa de marca-texto na lateral e fundo bem leve; seleção = contorno.
                    Row(
                        Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when {
                                    isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = .5f)
                                    mark != null -> highlighter.copy(alpha = .16f)
                                    else -> MaterialTheme.colorScheme.surface
                                },
                            )
                            .border(if (isSelected) 1.5.dp else 0.dp, if (isSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent, RoundedCornerShape(10.dp))
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Box(Modifier.width(4.dp).fillMaxHeight().background(if (blockMarks.isNotEmpty()) highlighter else androidx.compose.ui.graphics.Color.Transparent))
                        val callout = br.com.estudario.ui.components.calloutKindOf(block)
                        val markdown: @Composable (Modifier, String) -> Unit = { m, text ->
                            br.com.estudario.ui.components.StudyMarkdown(
                                text,
                                m,
                                textSizeSp = MaterialTheme.typography.bodyLarge.fontSize.value * textScale,
                                onTap = { selected = if (selected == index) null else index },
                                highlights = partial,
                                notedHighlights = blockMarks.filter { it.note.isNotBlank() }.mapTo(hashSetOf()) { it.quote },
                                onHighlightTap = { q ->
                                    val m = blockMarks.firstOrNull { it.quote == q }
                                    if (m != null) { if (m.note.isNotBlank()) viewingId = m.id else editing = index to q }
                                },
                                highlightColor = highlighter.copy(alpha = .45f),
                                onSelectText = { quote, withNote ->
                                    markHintSeen = true
                                    readerPrefs.edit().putBoolean(MARK_HINT_SEEN, true).apply()
                                    selected = null
                                    if (withNote) editing = index to quote
                                    else if (blockMarks.none { it.quote == quote }) {
                                        // grifar de novo por cima de um grifo (faltou uma letra, trecho maior ou menor)
                                        // troca o antigo pelo novo, e a nota que ele tinha vem junto
                                        val old = blockMarks.filter { br.com.estudario.text.MarkOverlap.overlaps(block, it.quote, quote) }
                                        old.forEach { viewModel.deleteTheoryMark(it) }
                                        viewModel.saveTheoryMark(TheoryMarkEntity(theoryId = theoryId, blockIndex = index, quote = quote, note = old.firstOrNull { it.note.isNotBlank() }?.note.orEmpty()))
                                        scope.launch { snackbar.showSnackbar(if (old.isNotEmpty()) "Grifo atualizado no Caderno de estudo." else "Trecho grifado. Fica no Caderno de estudo, no tópico desta teoria.") }
                                    }
                                },
                            )
                        }
                        if (callout != null) {
                            br.com.estudario.ui.components.CalloutFrame(callout, Modifier.weight(1f).padding(vertical = 4.dp)) {
                                markdown(Modifier.padding(start = 10.dp, end = 12.dp, bottom = 8.dp), br.com.estudario.ui.components.calloutBody(block))
                            }
                        } else {
                            markdown(Modifier.weight(1f).padding(start = 10.dp, end = 12.dp, top = 10.dp, bottom = 10.dp), block)
                        }
                    }
                    blockMarks.filter { it.note.isNotBlank() }.forEach { m ->
                        Surface(onClick = { viewingId = m.id }, color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(start = 12.dp, top = 4.dp)) {
                            Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { Icon(Icons.Outlined.EditNote, null); Text(m.note, Modifier.weight(1f)) }
                        }
                    }
                }
            }
            item {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        br.com.estudario.ui.assistant.Folha(110.dp, mood = br.com.estudario.ui.assistant.FolhaMood.HAPPY)
                        Text("Teoria concluída!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Leitura registrada como 100%. Agora vale treinar com as questões do tópico para fixar.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
            item { Spacer(Modifier.height(70.dp)) }
        }
        }
        }
    }
}

private const val MARK_HINT_SEEN = "reader_mark_hint_seen"

/** Cor de marca-texto: amarelo quente no claro, âmbar no escuro (legível nos dois). */
@Composable
private fun estudarioHighlighter(): androidx.compose.ui.graphics.Color =
    if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) androidx.compose.ui.graphics.Color(0xFFFFC857) else androidx.compose.ui.graphics.Color(0xFFF2A900)

private fun androidx.compose.ui.graphics.Color.luminance(): Float = 0.2126f * red + 0.7152f * green + 0.0722f * blue

/** Barra que sobe quando um parágrafo está selecionado. */
@Composable
private fun SelectionBar(highlighted: Boolean, pieces: Int, onRemovePieces: () -> Unit, onHighlight: () -> Unit, onNote: () -> Unit, onCopy: () -> Unit, onReport: () -> Unit, onClose: () -> Unit) {
    Surface(tonalElevation = 6.dp, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().navigationBarsPadding()) {
        if (pieces > 0) Text(
            "Para grifar outro trecho, segure uma palavra e arraste até onde quiser.",
            Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp),
            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            SelectionAction(if (highlighted) Icons.Outlined.FormatColorReset else Icons.Outlined.BorderColor, if (highlighted) "Tirar grifo" else "Grifar tudo", Modifier.weight(1f), onHighlight)
            if (pieces > 0) SelectionAction(Icons.Outlined.FormatColorReset, if (pieces > 1) "Tirar trechos" else "Tirar trecho", Modifier.weight(1f), onRemovePieces)
            SelectionAction(Icons.Outlined.EditNote, "Anotar", Modifier.weight(1f), onNote)
            SelectionAction(Icons.Outlined.ContentCopy, "Copiar", Modifier.weight(1f), onCopy)
            SelectionAction(Icons.Outlined.Flag, "Reportar", Modifier.weight(1f), onReport)
            IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Cancelar seleção") }
        }
        }
    }
}

@Composable
private fun SelectionAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, modifier: Modifier, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = modifier, contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        }
    }
}

/** Linha fina de progresso da leitura, em degradê da marca (no lugar da barra comum). */
@Composable
private fun ReadLine(progress: Float) {
    Box(Modifier.fillMaxWidth().height(3.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))) {
        Box(
            Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxHeight()
                .background(androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, androidx.compose.ui.graphics.Color(0xFFFFB421)))),
        )
    }
}

/** Ajustes de leitura: quatro tamanhos de letra e a fonte (Livro ou Simples). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderSettingsSheet(scale: Float, book: Boolean, onScale: (Float) -> Unit, onBook: (Boolean) -> Unit, onDismiss: () -> Unit) {
    val steps = listOf(0.9f, 1.05f, 1.2f, 1.38f)
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Leitura", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("TAMANHO DA LETRA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                steps.forEachIndexed { i, v ->
                    val on = kotlin.math.abs(scale - v) < 0.04f
                    Surface(
                        onClick = { onScale(v) },
                        shape = RoundedCornerShape(14.dp),
                        color = if (on) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(2.dp, if (on) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent),
                        modifier = Modifier.weight(1f).height(52.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("A", fontSize = androidx.compose.ui.unit.TextUnit((13 + i * 4).toFloat(), androidx.compose.ui.unit.TextUnitType.Sp), fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Serif) }
                    }
                }
            }
            Text("FONTE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(true to "Livro", false to "Simples").forEach { (value, label) ->
                    val on = book == value
                    Surface(
                        onClick = { onBook(value) },
                        shape = RoundedCornerShape(14.dp),
                        color = if (on) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(2.dp, if (on) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent),
                        modifier = Modifier.weight(1f).height(52.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text(label, fontWeight = FontWeight.Bold, fontFamily = if (value) androidx.compose.ui.text.font.FontFamily.Serif else null) }
                    }
                }
            }
        }
    }
}
