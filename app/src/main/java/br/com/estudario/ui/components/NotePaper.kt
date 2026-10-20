package br.com.estudario.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.TopicEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * As etiquetas de uma anotação, guardadas no começo do texto ("[Macete] ...") para valer igual no
 * app, no site e no backup sem mudar o banco.
 */
object NoteTags {
    val all = listOf("Macete", "Resumo", "Dúvida", "Pegadinha", "Letra da lei")
    private val prefix = Regex("""^\[(Macete|Resumo|Dúvida|Pegadinha|Letra da lei|Lei seca)\]\s*""")

    fun split(text: String): Pair<String?, String> {
        val m = prefix.find(text) ?: return null to text
        // "Lei seca" foi o nome antigo de "Letra da lei".
        return m.groupValues[1].replace("Lei seca", "Letra da lei") to text.substring(m.range.last + 1)
    }

    fun join(tag: String?, body: String): String = if (tag == null) body.trim() else "[$tag] ${body.trim()}"

    /** Cor de cada etiqueta, a mesma do site. */
    fun color(tag: String?): Color = when (tag) {
        "Macete" -> Color(0xFF5B4BF5)
        "Resumo" -> Color(0xFF1FA97F)
        "Dúvida" -> Color(0xFF2F8FE0)
        "Pegadinha" -> Color(0xFFE5484D)
        "Letra da lei" -> Color(0xFFD98A00)
        else -> Color(0xFF8A8FA3)
    }
}

private const val NOTE_LIMIT = 4000
private val noteDate = SimpleDateFormat("dd 'de' MMM, HH:mm", Locale("pt", "BR"))

/** As pautas da folha de caderno e a margem vermelha, desenhadas atrás do texto. */
private fun Modifier.ruledPaper(line: Color, margin: Color, lineHeightPx: Float, topPx: Float, marginPx: Float) = drawBehind {
    var y = topPx + lineHeightPx
    while (y < size.height) {
        drawLine(line, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
        y += lineHeightPx
    }
    drawLine(margin, Offset(marginPx, 0f), Offset(marginPx, size.height), strokeWidth = 1.5.dp.toPx())
}

/**
 * O editor de anotação do Estudário: uma folha de caderno pautada que sobe de baixo, com o tópico
 * (escolhido numa busca), o trecho grifado quando a nota é sobre ele, etiquetas rápidas e o
 * contador de letras. Serve para a anotação livre e para a nota de um trecho da teoria.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotePaperEditor(
    initialText: String,
    isNew: Boolean,
    createdAt: Long?,
    onDismiss: () -> Unit,
    onSave: (text: String, topicId: Long?) -> Unit,
    onDelete: (() -> Unit)? = null,
    quote: String? = null,
    topics: List<TopicEntity>? = null,
    initialTopicId: Long? = null,
    fixedPlace: String? = null,
    deleteLabel: String = "Apagar anotação",
) {
    val (tag0, body0) = remember(initialText) { NoteTags.split(initialText) }
    var tag by remember(initialText) { mutableStateOf(tag0) }
    var body by remember(initialText) { mutableStateOf(body0) }
    var topicId by remember(initialTopicId) { mutableStateOf(initialTopicId) }
    var picking by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.red < 0.5f
    val paper = if (dark) Color(0xFF1E1D2B) else Color(0xFFFFFDF6)
    val line = if (dark) Color(0x2E9AA4FF) else Color(0x335B8DEF)
    val margin = if (dark) Color(0x66FF7A85) else Color(0x80E5484D)
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { if (isNew) runCatching { focus.requestFocus() } }

    if (confirmDelete && onDelete != null) AlertDialog(
        onDismissRequest = { confirmDelete = false },
        title = { Text("$deleteLabel?") },
        text = { Text("Ela sai do app, do site e do Caderno de estudo.") },
        confirmButton = { TextButton(onClick = { confirmDelete = false; onDelete() }) { Text("Apagar", color = scheme.error) } },
        dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Manter") } },
    )
    if (picking && topics != null) TopicPickerDialog(topics, topicId, onPick = { topicId = it; picking = false }, onDismiss = { picking = false })

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet, containerColor = paper, dragHandle = { BottomSheetDefaults.DragHandle(color = margin) }) {
        Column(Modifier.fillMaxWidth().imePadding().navigationBarsPadding().padding(bottom = 12.dp)) {
            // Cabeçalho: a fita de marca-texto com o nome, a data e fechar.
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        if (isNew) "Nova anotação" else "Sua anotação",
                        Modifier.drawBehind {
                            drawRect(Color(0x73FFC857), topLeft = Offset(-4.dp.toPx(), size.height * 0.45f), size = androidx.compose.ui.geometry.Size(size.width + 8.dp.toPx(), size.height * 0.5f))
                        },
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = scheme.onSurface,
                    )
                    Text(
                        if (createdAt != null && !isNew) noteDate.format(Date(createdAt)) else "Fica no Caderno de estudo, no app e no site",
                        style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
            }
            // Onde a nota mora: o tópico (trocável) ou o lugar fixo do trecho grifado.
            Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                val place = fixedPlace ?: topics?.firstOrNull { it.id == topicId }?.title ?: "Geral (sem tópico)"
                Surface(
                    onClick = { if (topics != null && fixedPlace == null) picking = true },
                    enabled = topics != null && fixedPlace == null,
                    shape = RoundedCornerShape(50), color = scheme.primaryContainer.copy(alpha = .7f),
                ) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Sell, null, Modifier.size(16.dp), tint = scheme.primary)
                        Text(place, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, color = scheme.onPrimaryContainer, modifier = Modifier.widthIn(max = 260.dp))
                        if (topics != null && fixedPlace == null) Text("trocar", style = MaterialTheme.typography.labelSmall, color = scheme.primary)
                    }
                }
            }
            // O trecho grifado, quando a nota é sobre ele.
            if (!quote.isNullOrBlank()) Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x33FFC857)).height(IntrinsicSize.Min),
            ) {
                Box(Modifier.width(4.dp).fillMaxHeight().background(Color(0xFFF2A900)))
                Text(
                    "“" + quote.trim().take(320) + (if (quote.trim().length > 320) "…" else "") + "”",
                    Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium, color = scheme.onSurface, maxLines = 5, overflow = TextOverflow.Ellipsis,
                )
            }
            // Etiquetas rápidas: tocar marca (e de novo desmarca).
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NoteTags.all.forEach { t ->
                    val on = tag == t
                    val c = NoteTags.color(t)
                    Surface(
                        onClick = { tag = if (on) null else t },
                        shape = RoundedCornerShape(50),
                        color = if (on) c else Color.Transparent,
                        border = BorderStroke(1.5.dp, c.copy(alpha = if (on) 1f else .55f)),
                    ) {
                        Text(t, Modifier.padding(horizontal = 12.dp, vertical = 5.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = if (on) Color.White else c)
                    }
                }
            }
            // A folha pautada: o texto cai certinho nas linhas.
            val density = androidx.compose.ui.platform.LocalDensity.current
            val lineSp = 28.sp
            val linePx = with(density) { lineSp.toPx() }
            val topPad = 10.dp
            val marginX = 34.dp
            Box(
                Modifier.fillMaxWidth().heightIn(min = 260.dp, max = 420.dp).padding(top = 4.dp)
                    .ruledPaper(line, margin, linePx, with(density) { topPad.toPx() } - linePx * 0.22f, with(density) { marginX.toPx() })
                    .verticalScroll(rememberScrollState()),
            ) {
                BasicTextField(
                    value = body,
                    onValueChange = { if (it.length <= NOTE_LIMIT) body = it },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 260.dp).padding(start = marginX + 10.dp, end = 20.dp, top = topPad, bottom = 12.dp).focusRequester(focus),
                    textStyle = TextStyle(fontSize = 17.sp, lineHeight = lineSp, color = scheme.onSurface, fontFamily = br.com.estudario.ui.theme.Nunito),
                    cursorBrush = SolidColor(scheme.primary),
                    decorationBox = { inner ->
                        Box {
                            if (body.isEmpty()) Text(
                                if (quote != null) "Por que esse trecho importa? Escreva com suas palavras…" else "Escreva com suas palavras: o macete, a dúvida, o resumo…",
                                style = TextStyle(fontSize = 17.sp, lineHeight = lineSp, fontFamily = br.com.estudario.ui.theme.Nunito), color = scheme.onSurfaceVariant.copy(alpha = .6f),
                            )
                            inner()
                        }
                    },
                )
            }
            // Rodapé: apagar, contador e salvar.
            Row(Modifier.fillMaxWidth().padding(start = 12.dp, end = 20.dp, top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                if (onDelete != null) IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Outlined.DeleteOutline, deleteLabel, tint = scheme.error) }
                Text("${body.length}/$NOTE_LIMIT", Modifier.weight(1f).padding(start = 8.dp), style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                Button(onClick = { onSave(NoteTags.join(tag, body), topicId) }, enabled = body.isNotBlank(), shape = RoundedCornerShape(14.dp)) {
                    Text("Salvar no caderno", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/** Busca de tópico para a anotação: digitar filtra, sem rolar uma lista de cem itens num menu. */
@Composable
private fun TopicPickerDialog(topics: List<TopicEntity>, current: Long?, onPick: (Long?) -> Unit, onDismiss: () -> Unit) {
    var q by remember { mutableStateOf("") }
    val fold = { s: String -> java.text.Normalizer.normalize(s.lowercase(), java.text.Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "") }
    val list = remember(q, topics) { topics.filter { q.isBlank() || fold(it.title).contains(fold(q.trim())) }.sortedBy { it.title } }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ligar a um tópico") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(q, { q = it }, Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Outlined.Search, null) }, placeholder = { Text("Buscar tópico") }, shape = RoundedCornerShape(14.dp))
                LazyColumn(Modifier.heightIn(max = 360.dp)) {
                    item { PickRow("Geral (sem tópico)", current == null) { onPick(null) } }
                    items(list, key = { it.id }) { t -> PickRow(t.title, current == t.id) { onPick(t.id) } }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
    )
}

@Composable
private fun PickRow(label: String, on: Boolean, onClick: () -> Unit) {
    Text(
        label,
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (on) MaterialTheme.colorScheme.primaryContainer else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 10.dp),
        fontWeight = if (on) FontWeight.Bold else FontWeight.Normal, maxLines = 2, overflow = TextOverflow.Ellipsis,
    )
}

/**
 * A anotação no Caderno: um pedaço de folha pautada com a etiqueta colorida, o trecho grifado
 * (quando a nota é de um trecho) e a data.
 */
@Composable
fun NotePaperCard(text: String, createdAt: Long, onClick: () -> Unit, quote: String? = null, footer: String? = null) {
    val (tag, body) = remember(text) { NoteTags.split(text) }
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.red < 0.5f
    val paper = if (dark) Color(0xFF1E1D2B) else Color(0xFFFFFDF6)
    val line = if (dark) Color(0x229AA4FF) else Color(0x265B8DEF)
    val accent = NoteTags.color(tag)
    val density = androidx.compose.ui.platform.LocalDensity.current
    Surface(onClick = onClick, shape = RoundedCornerShape(16.dp), color = paper, border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = .6f)), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Box(Modifier.width(5.dp).fillMaxHeight().background(accent))
            Column(Modifier.weight(1f).padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (tag != null) Surface(shape = RoundedCornerShape(50), color = accent) {
                        Text(tag.uppercase(), Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color.White)
                    }
                    if (quote != null) Text("NOTA NUM TRECHO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color(0xFFD98A00))
                    Spacer(Modifier.weight(1f))
                    Text(noteDate.format(Date(createdAt)), style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant)
                }
                if (!quote.isNullOrBlank()) Text(
                    "“" + quote.trim() + "”",
                    Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0x33FFC857)).padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall, maxLines = 3, overflow = TextOverflow.Ellipsis, color = scheme.onSurfaceVariant,
                )
                val lineSp = 24.sp
                val linePx = with(density) { lineSp.toPx() }
                Text(
                    body,
                    Modifier.fillMaxWidth().drawBehind {
                        var y = linePx - 2.dp.toPx()
                        while (y < size.height + 1) { drawLine(line, Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += linePx }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = lineSp), maxLines = 8, overflow = TextOverflow.Ellipsis, color = scheme.onSurface,
                )
                if (footer != null) Text(footer, style = MaterialTheme.typography.labelMedium, color = scheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * A nota aberta para ler, numa folha de caderno que sobe de baixo: a etiqueta, onde ela mora, o
 * trecho grifado (se for nota de um trecho), o texto inteiro (dá para selecionar e copiar) e as ações
 * Editar, Excluir (com confirmação) e, quando couber, Tirar grifo e Abrir na teoria.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteViewSheet(
    text: String,
    createdAt: Long,
    place: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    quote: String? = null,
    onRemoveHighlight: (() -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
) {
    val (tag, body) = remember(text) { NoteTags.split(text) }
    var confirm by remember { mutableStateOf(false) }
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scheme = MaterialTheme.colorScheme
    val dark = scheme.surface.red < 0.5f
    val paper = if (dark) Color(0xFF1E1D2B) else Color(0xFFFFFDF6)
    val line = if (dark) Color(0x2E9AA4FF) else Color(0x335B8DEF)
    val margin = if (dark) Color(0x66FF7A85) else Color(0x80E5484D)
    val density = androidx.compose.ui.platform.LocalDensity.current
    if (confirm) AlertDialog(
        onDismissRequest = { confirm = false },
        title = { Text(if (quote != null) "Excluir esta nota?" else "Excluir esta anotação?") },
        text = { Text(if (quote != null) "O trecho continua grifado." else "Ela sai do app, do site e do Caderno de estudo.") },
        confirmButton = { TextButton(onClick = { confirm = false; onDelete() }) { Text("Excluir", color = scheme.error) } },
        dismissButton = { TextButton(onClick = { confirm = false }) { Text("Manter") } },
    )
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet, containerColor = paper, dragHandle = { BottomSheetDefaults.DragHandle(color = margin) }) {
        Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 12.dp)) {
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            if (quote != null) "Nota do trecho" else "Sua anotação",
                            Modifier.drawBehind { drawRect(Color(0x73FFC857), topLeft = Offset(-4.dp.toPx(), size.height * 0.45f), size = androidx.compose.ui.geometry.Size(size.width + 8.dp.toPx(), size.height * 0.5f)) },
                            style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black,
                        )
                        if (tag != null) Surface(shape = RoundedCornerShape(50), color = NoteTags.color(tag)) {
                            Text(tag.uppercase(), Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                    Text("$place · ${noteDate.format(Date(createdAt))}", style = MaterialTheme.typography.labelSmall, color = scheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
            }
            if (!quote.isNullOrBlank()) Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x33FFC857)).height(IntrinsicSize.Min),
            ) {
                Box(Modifier.width(4.dp).fillMaxHeight().background(Color(0xFFF2A900)))
                Text("“" + quote.trim() + "”", Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium, maxLines = 6, overflow = TextOverflow.Ellipsis)
            }
            val lineSp = 28.sp
            val linePx = with(density) { lineSp.toPx() }
            Box(
                Modifier.fillMaxWidth().heightIn(min = 160.dp, max = 420.dp)
                    .ruledPaper(line, margin, linePx, with(density) { 10.dp.toPx() } - linePx * 0.22f, with(density) { 34.dp.toPx() })
                    .verticalScroll(rememberScrollState()),
            ) {
                androidx.compose.foundation.text.selection.SelectionContainer {
                    Text(body, Modifier.fillMaxWidth().padding(start = 44.dp, end = 20.dp, top = 10.dp, bottom = 12.dp), style = TextStyle(fontSize = 17.sp, lineHeight = lineSp, fontFamily = br.com.estudario.ui.theme.Nunito, color = scheme.onSurface))
                }
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp).horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = { confirm = true }) { Icon(Icons.Outlined.DeleteOutline, null, Modifier.size(18.dp), tint = scheme.error); Spacer(Modifier.width(4.dp)); Text("Excluir", color = scheme.error) }
                if (onRemoveHighlight != null) TextButton(onClick = onRemoveHighlight) { Text("Tirar grifo") }
                if (onOpen != null) TextButton(onClick = onOpen) { Text("Abrir na teoria") }
            }
            Button(onClick = onEdit, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(50.dp), shape = RoundedCornerShape(14.dp)) {
                Text("Editar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
