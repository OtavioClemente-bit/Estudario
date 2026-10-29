package br.com.estudario.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.StudyNotebook
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.screenPadding
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

/*
 * O Caderno de estudo que se escreve sozinho (a regra em StudyNotebook, módulo comum; o site faz
 * igual, web/.../ui/StudyNotebookPages.kt): a entrada é um fichário com as matérias e a "Folheada do
 * dia"; cada tópico estudado é uma página de caderno. O que a pessoa guardou fica em "Grifos, notas e
 * salvos" (o Caderno de antes).
 */

/** Tema escuro: papel azul-noite e cores suaves (o papel creme claro ficava forte demais). */
@Composable private fun nbDark() = MaterialTheme.colorScheme.background.luminance() < 0.5f
private val Ink: Color @Composable get() = if (nbDark()) Color(0xFFECE9FF) else Color(0xFF1B1464)
private val Muted: Color @Composable get() = if (nbDark()) Color(0xFFB3ADDF) else Color(0xFF6B6790)
private val PaperColor: Color @Composable get() = if (nbDark()) Color(0xFF1E1A4A) else Color(0xFFFFFDF7)
private val CardBg: Color @Composable get() = if (nbDark()) Color(0xFF262158) else Color.White
private val SoftBg: Color @Composable get() = if (nbDark()) Color(0xFF2A2562) else Color(0xFFF4F0FF)
private val LineC: Color @Composable get() = if (nbDark()) Color(0xFF3A3480) else Color(0xFFEEE8D6)
private val Rule: Color @Composable get() = if (nbDark()) Color.White.copy(alpha = 0.055f) else Color(0xFFDCE6F7)
private val MarginRed: Color @Composable get() = if (nbDark()) Color(0x59F38C91) else Color(0xFFF3B2B5)
private val Gold = Color(0xFFFFB421)
private val Mint = Color(0xFF2FBF8A)
private val Coral = Color(0xFFFF8A80)
private val Highlight: Color @Composable get() = if (nbDark()) Color(0x47FFD633) else Color(0x8CFFD633)
private val Postit = Color(0xFFFFF3A0)
private val Stamp = Color(0xFFE5484D)
private val Hand = FontFamily.Cursive

private const val PREFS = "estudario_caderno"

/** Os tópicos estudados do concurso principal, com o que cada um tem, no formato do StudyNotebook. */
@Composable
private fun rememberNotebookTopics(viewModel: AppViewModel): List<StudyNotebook.Topic> {
    val competitions by viewModel.competitions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val marks by viewModel.theoryMarks.collectAsState()
    val snippets by viewModel.snippets.collectAsState()
    val summaries by viewModel.summaries.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val errors by viewModel.errors.collectAsState()
    return remember(competitions, subjects, topics, theories, marks, snippets, summaries, notes, errors) {
        val primary = competitions.firstOrNull { it.isPrimary } ?: competitions.firstOrNull()
        val subjectIds = subjects.filter { primary == null || it.competitionId == primary.id }.mapTo(hashSetOf()) { it.id }
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val theoriesByTopic = theories.groupBy { it.topicId }
        val marksByTheory = marks.groupBy { it.theoryId }
        val wrongByTopic = errors.filter { it.entry.pending }.groupBy { it.question.topicId }
        val snippetsByTopic = snippets.groupBy { it.topicId }
        val summariesByTopic = summaries.groupBy { it.topicId }
        val notesByTopic = notes.filter { it.topicId != null }.groupBy { it.topicId }
        val cardRegex = Regex("""###\s*(.+?)\n+([\s\S]+?)(?=\n###|$)""")
        topics.filter { it.subjectId in subjectIds && it.id !in parents && it.status != TopicStatus.NAO_ESTUDADO }.map { t ->
            val mine = snippetsByTopic[t.id].orEmpty()
            val sums = summariesByTopic[t.id].orEmpty()
            val quickCards = sums.filter { it.kind == SummaryKind.RAPIDO }.flatMap { s -> cardRegex.findAll(s.markdown).map { it.groupValues[1].trim() to it.groupValues[2].trim() }.toList() }
            StudyNotebook.Topic(
                id = t.id, subjectId = t.subjectId, title = t.title, status = t.status.name,
                lastSeenAt = listOfNotNull(t.lastReviewedAt, t.lastStudiedAt).maxOrNull(),
                summary = (sums.firstOrNull { it.kind == SummaryKind.COMPLETO } ?: sums.firstOrNull { it.kind != SummaryKind.RAPIDO })?.markdown,
                tips = mine.filter { it.kind == SnippetKind.BIZU }.map { it.text },
                traps = mine.filter { it.kind == SnippetKind.PEGADINHA }.map { it.text },
                recall = mine.filter { it.kind == SnippetKind.RECUPERACAO && !it.answer.isNullOrBlank() }.map { it.text to it.answer!! } + quickCards,
                marks = theoriesByTopic[t.id].orEmpty().flatMap { th -> val b = br.com.estudario.ui.components.studyBlocks(th.markdown); marksByTheory[th.id].orEmpty().map { it.copy(quote = StudyNotebook.restoreQuote(it.quote, b, it.blockIndex)) } }.filter { it.quote.isNotBlank() }.distinctBy { it.quote }.map { StudyNotebook.Mark(it.quote, it.note, it.createdAt) },
                wrongQuestions = wrongByTopic[t.id].orEmpty().map { it.entry.questionId },
                notes = notesByTopic[t.id].orEmpty().map { it.text },
            )
        }
    }
}

private fun daySeed(): Long = LocalDate.now().toEpochDay()
private fun firmColor(f: StudyNotebook.Firmness) = when (f) { StudyNotebook.Firmness.FIRM -> Mint; StudyNotebook.Firmness.MID -> Gold; StudyNotebook.Firmness.COLD -> Coral }
private fun since(at: Long, now: Long): String { val d = ((now - at) / 86_400_000L).toInt(); return when { d <= 0 -> "hoje"; d == 1 -> "ontem"; else -> "há $d dias" } }
private fun kindOf(p: StudyNotebook.Page): Pair<String, Color> = when (p) {
    is StudyNotebook.Page.Key -> "Ponto-chave" to Color(0xFF5B4BE8)
    is StudyNotebook.Page.MarkPage -> "Seu grifo" to Color(0xFFFFD43B)
    is StudyNotebook.Page.Trap -> "Pegadinha" to Stamp
    is StudyNotebook.Page.Recall -> "Lembrar" to Mint
    is StudyNotebook.Page.Question -> "Questão" to Color(0xFFFF8A3D)
}

/** Papel pautado com margem vermelha (o fundo das páginas do caderno). */
private fun Modifier.notebookPaper(marginDp: Float = 30f): Modifier = composed {
    val paper = PaperColor
    val rule = Rule
    val margin = MarginRed
    this.clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp, topEnd = 20.dp, bottomEnd = 20.dp))
        .background(paper)
        .drawBehind {
            val step = 28.dp.toPx(); var y = 60.dp.toPx()
            while (y < size.height) { drawLine(rule, Offset(0f, y), Offset(size.width, y), 1.dp.toPx()); y += step }
            val mx = marginDp.dp.toPx(); drawLine(margin, Offset(mx, 0f), Offset(mx, size.height), 2.dp.toPx())
        }
}

/**
 * Um grifo: o trecho como citação (barra amarela, fundo leve) em markdown de verdade (a tabela
 * colada vira tabela, a lista vira lista; ver StudyNotebook.quoteMarkdown) e a nota da pessoa junto,
 * para ficar claro de que trecho ela é. Grifo longo vem recolhido.
 */
@Composable
private fun MarkQuote(mark: StudyNotebook.Mark) {
    val md = remember(mark.quote) { StudyNotebook.quoteMarkdown(mark.quote) }
    val long = md.length > 360 || md.count { it == '\n' } > 6
    var open by remember(mark.quote) { mutableStateOf(false) }
    val bar = Color(0xFFF5C400)
    Column(
        Modifier.fillMaxWidth().padding(vertical = 6.dp).clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
            .background(bar.copy(alpha = if (nbDark()) 0.07f else 0.12f))
            .drawBehind { drawRect(bar.copy(alpha = 0.8f), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)) }
            .padding(start = 14.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(if (long && !open) Modifier.heightIn(max = 150.dp).clipToBounds() else Modifier) { br.com.estudario.ui.components.MarkdownText(md) }
        if (long) Text(if (open) "recolher" else "ver o trecho inteiro", Modifier.clickable { open = !open }, color = Color(0xFF5B4BE8), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        if (mark.note.isNotBlank()) Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(if (nbDark()) Color(0xFFE8DC8A) else Postit).padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text("✎ SUA NOTA", fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = Color(0xFF3E3300).copy(alpha = 0.65f))
            Text(mark.note, fontFamily = Hand, fontSize = 19.sp, lineHeight = 22.sp, color = Color(0xFF3E3300))
        }
    }
}

@Composable
private fun PostIt(text: String, color: Color = Postit) {
    Box(Modifier.fillMaxWidth().padding(start = 40.dp, top = 10.dp, bottom = 6.dp)) {
        Text(inkInline(text), Modifier.rotate(-1.4f).background(color).padding(horizontal = 14.dp, vertical = 12.dp), fontFamily = Hand, fontSize = 20.sp, lineHeight = 23.sp, color = Color(0xFF3E3300))
    }
}

@Composable
private fun StampText(text: String) {
    Text(text, Modifier.padding(vertical = 10.dp).rotate(-4f).border(3.dp, Stamp, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 4.dp), color = Stamp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 14.sp)
}

@Composable
private fun Highlighted(text: String) {
    Text(inkInline(text, SpanStyle(background = Highlight)), color = Ink, fontSize = 16.sp, lineHeight = 28.sp)
}

/**
 * O markdown em linha das páginas (resumo, dicas, pegadinhas, cartões): **negrito**, `código` e
 * *itálico*. Antes ia como texto puro e as marcas apareciam na página (como no site, agora não).
 */
/** **negrito** | `código` | *itálico* | __negrito__ (o padrão é testado em StudyNotebookInlineTest). */
internal val INLINE_TOKEN = Regex("""\*\*([^*]+)\*\*|`([^`]+)`|(?<![\w*])\*([^*\s][^*]*)\*(?!\w)|__([^_]+)__""")

private fun inkInline(text: String, base: SpanStyle? = null): androidx.compose.ui.text.AnnotatedString = buildAnnotatedString {
    if (base != null) pushStyle(base)
    val token = INLINE_TOKEN
    var i = 0
    for (m in token.findAll(text)) {
        append(text.substring(i, m.range.first))
        when {
            m.groupValues[1].isNotEmpty() -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(m.groupValues[1]) }
            m.groupValues[2].isNotEmpty() -> withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = CodeBg, fontSize = 14.sp)) { append(" " + m.groupValues[2] + " ") }
            m.groupValues[3].isNotEmpty() -> withStyle(SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) { append(m.groupValues[3]) }
            else -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(m.groupValues[4]) }
        }
        i = m.range.last + 1
    }
    append(text.substring(i))
    if (base != null) pop()
}

/** O fundo do código em linha: um índigo bem transparente, que serve no papel claro e no escuro. */
private val CodeBg = Color(0x225B4BE8)

// ------------------------------------------------------------------ a entrada (o fichário)

@Composable
fun NotebookHomeScreen(viewModel: AppViewModel, onFlip: () -> Unit, onPage: (Long) -> Unit, onSaved: () -> Unit, onEdital: () -> Unit) {
    val topics = rememberNotebookTopics(viewModel)
    val subjects by viewModel.subjects.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val now = remember { System.currentTimeMillis() }
    val pages = remember(topics) { StudyNotebook.folheada(topics, now, daySeed()) }
    val prefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
    val flippedToday = prefs.getLong("folheada", -1) == daySeed()
    val last = prefs.getLong("ultima", -1).let { id -> topics.firstOrNull { it.id == id } }
    var open by remember { mutableStateOf<Long?>(null) }
    val bySubject = topics.groupBy { it.subjectId }.entries.sortedByDescending { it.value.size }
    val hour = java.time.LocalTime.now().hour
    val hello = when { hour < 12 -> "Bom dia"; hour < 18 -> "Boa tarde"; else -> "Boa noite" }
    val first = profile.name.trim().split(" ").firstOrNull()?.takeIf { it.isNotBlank() }

    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
        item {
            Column(Modifier.fillMaxWidth().notebookPaper(24f).padding(start = 34.dp, end = 12.dp, top = 18.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    br.com.estudario.ui.assistant.Folha(60.dp, mood = if (topics.isEmpty()) br.com.estudario.ui.assistant.FolhaMood.TALKING else br.com.estudario.ui.assistant.FolhaMood.HAPPY)
                    Column(Modifier.weight(1f)) {
                        Text("$hello${first?.let { ", $it" } ?: ""}!", fontWeight = FontWeight.Black, fontSize = 21.sp, color = Ink)
                        Text(if (topics.isEmpty()) "Seu caderno se escreve sozinho: cada tópico que você estudar vira página aqui." else "Você já estudou ${topics.size} ${if (topics.size == 1) "tópico" else "tópicos"}. Bora não deixar esfriar?", color = Muted, fontSize = 14.sp)
                    }
                }
                // A Folheada do dia
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(Color(0xFF2E2799), Ink))).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(if (pages.isEmpty()) "FOLHEADA DO DIA" else if (flippedToday) "FOLHEADA DE HOJE · FEITA" else "FOLHEADA DO DIA · ~${StudyNotebook.minutes(pages)} MIN", color = Gold, fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 1.5.sp)
                    Text(if (pages.isEmpty()) "Estude o primeiro tópico" else if (flippedToday) "Feita! Quer folhear de novo?" else "${pages.size} ${if (pages.size == 1) "página separada" else "páginas separadas"} para você", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text(if (pages.isEmpty()) "Leia a teoria e marque como estudado: o resumo, as dicas e os cartões viram páginas daqui." else "Do que você viu e está começando a esquecer.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = { if (pages.isEmpty()) onEdital() else onFlip() }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF3B2300)),
                    ) {
                        Icon(if (pages.isEmpty()) Icons.Outlined.MenuBook else Icons.Outlined.AutoStories, null); Spacer(Modifier.width(8.dp))
                        Text(if (pages.isEmpty()) "Ir para o edital" else if (flippedToday) "Folhear de novo" else "Folhear agora", fontWeight = FontWeight.Black)
                    }
                }
                if (last != null) Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(SoftBg).clickable { onPage(last.id) }.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(Modifier.width(12.dp).height(32.dp).background(Stamp))
                    Column { Text("Continuar de onde parou", fontWeight = FontWeight.Bold, color = Ink); Text(last.title, fontSize = 12.sp, color = Muted, maxLines = 1) }
                }
                if (bySubject.isNotEmpty()) Text("Suas matérias", fontWeight = FontWeight.Black, color = Ink)
                bySubject.forEach { (subjectId, list) ->
                    val name = subjects.firstOrNull { it.id == subjectId }?.name ?: "Matéria"
                    val firm = list.map { StudyNotebook.firmness(it, now) }
                    val cold = firm.count { it == StudyNotebook.Firmness.COLD }
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(CardBg).border(2.dp, LineC, RoundedCornerShape(16.dp))) {
                        Row(Modifier.fillMaxWidth().clickable { open = if (open == subjectId) null else subjectId }.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(name), br.com.estudario.ui.components.subjectAccentColor(name, br.com.estudario.ui.theme.estudarioColors().subjectPalette), 42.dp)
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(name, fontWeight = FontWeight.Bold, color = Ink)
                                Text("${list.size} ${if (list.size == 1) "tópico visto" else "tópicos vistos"} · ${firm.count { it == StudyNotebook.Firmness.FIRM }} firmes", fontSize = 12.sp, color = Muted)
                                Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) { firm.sortedBy { it.ordinal }.take(20).forEach { f -> Box(Modifier.width(18.dp).height(5.dp).clip(RoundedCornerShape(3.dp)).background(firmColor(f))) } }
                            }
                            if (cold > 0) Text("$cold esfriando", Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFFFE9D6)).padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFB5560F))
                            Icon(if (open == subjectId) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, tint = Muted)
                        }
                        if (open == subjectId) list.sortedBy { it.lastSeenAt ?: 0 }.forEach { t ->
                            Row(Modifier.fillMaxWidth().clickable { onPage(t.id) }.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(Modifier.size(9.dp).clip(CircleShape).background(firmColor(StudyNotebook.firmness(t, now))))
                                Text(t.title, Modifier.weight(1f), color = Ink, fontSize = 14.sp)
                                t.lastSeenAt?.let { Text(since(it, now), fontSize = 11.sp, color = Muted) }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ a Folheada do dia

@Composable
fun NotebookFlipScreen(viewModel: AppViewModel, onClose: () -> Unit, onPage: (Long) -> Unit) {
    val topics = rememberNotebookTopics(viewModel)
    val subjects by viewModel.subjects.collectAsState()
    val now = remember { System.currentTimeMillis() }
    // a lista é fixada na primeira leitura com tópicos: responder a questão não troca as páginas
    var frozen by remember { mutableStateOf<List<StudyNotebook.Page>?>(null) }
    if (frozen == null && topics.isNotEmpty()) frozen = StudyNotebook.folheada(topics, now, daySeed())
    val pages = frozen.orEmpty()
    var index by remember { mutableIntStateOf(0) }
    val sessionId = remember { "caderno-${java.util.UUID.randomUUID()}" }
    val startedAt = remember { System.currentTimeMillis() }
    val prefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
    val byId = topics.associateBy { it.id }
    // as questões da folheada contam no Desempenho (tempo e acertos), como no site; saiu no meio também vale
    val answers = remember { mutableStateListOf<Pair<Long, Boolean>>() }
    var saved by remember { mutableStateOf(false) }
    fun saveSession() {
        if (saved || answers.isEmpty()) return
        saved = true
        val end = System.currentTimeMillis()
        val topicIds = answers.map { it.first }.distinct()
        val subjectIds = viewModel.topics.value.filter { it.id in topicIds }.map { it.subjectId }.distinct()
        val session = br.com.estudario.data.local.QuestionSessionEntity(
            id = sessionId, type = br.com.estudario.data.local.QuestionSessionType.QUICK,
            startedAt = startedAt, completedAt = end, durationSeconds = (end - startedAt) / 1_000,
            questionCount = answers.size, correctCount = answers.count { it.second },
            subjectIdsText = subjectIds.joinToString(","), topicIdsText = topicIds.joinToString(","),
        )
        viewModel.viewModelScope.launch { viewModel.saveQuestionSession(session) }
    }
    DisposableEffect(Unit) { onDispose { saveSession() } }
    // a página rola: questão longa empurrava a resposta (acertou ou errou) para fora da tela
    val scroll = rememberScrollState()
    LaunchedEffect(index) { scroll.scrollTo(0) }
    LaunchedEffect(answers.size) { if (answers.isNotEmpty()) { kotlinx.coroutines.delay(120); scroll.animateScrollTo(scroll.maxValue) } }
    Column(Modifier.fillMaxWidth().verticalScroll(scroll).padding(screenPadding()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val qTotal = pages.count { it is StudyNotebook.Page.Question }
        if (qTotal > 0) br.com.estudario.ui.components.QuestionHud(
            pages.take(index).count { it is StudyNotebook.Page.Question }, qTotal, answers.map { it.second }, startedAt,
            title = if (index >= pages.size) "Folheada do dia" else "Folheada do dia · página ${index + 1} de ${pages.size}", exitLabel = "Fechar", onExit = onClose,
        ) else Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, "Fechar") }
            Text("Folheada do dia", Modifier.weight(1f), fontWeight = FontWeight.Black, fontSize = 17.sp)
            if (pages.isNotEmpty()) Text(if (index >= pages.size) "pronto" else "${index + 1} de ${pages.size}", fontFamily = Hand, fontSize = 21.sp, color = MaterialTheme.colorScheme.primary)
        }
        if (pages.isEmpty()) { Text("Nada para folhear ainda: estude um tópico e ele vira página do seu caderno.", color = MaterialTheme.colorScheme.onSurfaceVariant); return@Column }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) { pages.indices.forEach { i -> Box(Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(if (i <= index) Gold else MaterialTheme.colorScheme.surfaceVariant)) } }
        if (index >= pages.size) {
            LaunchedEffect(Unit) { prefs.edit().putLong("folheada", daySeed()).apply(); saveSession() }
            Column(Modifier.fillMaxWidth().padding(top = 24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                br.com.estudario.ui.assistant.Folha(110.dp, mood = br.com.estudario.ui.assistant.FolhaMood.PROUD)
                Text("Folheada feita!", fontWeight = FontWeight.Black, fontSize = 24.sp)
                val minutes = ((System.currentTimeMillis() - startedAt) / 60_000L).toInt().coerceAtLeast(1)
                val names = pages.map { it.topicId }.distinct().mapNotNull { byId[it]?.title }.take(3)
                Text("${pages.size} páginas em $minutes ${if (minutes == 1) "minuto" else "minutos"}. ${names.joinToString(", ")} voltaram para a memória.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = onClose) { Text("Voltar ao caderno", fontWeight = FontWeight.Black) }
            }
            return@Column
        }
        val page = pages[index]
        val topic = byId[page.topicId]
        androidx.compose.runtime.key(index) {
            // a página nova entra girando pela margem, como folha virada
            var shown by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { shown = true }
            val turn by animateFloatAsState(if (shown) 1f else 0f, tween(450), label = "folhear")
            Column(
                Modifier.fillMaxWidth().heightIn(min = 360.dp).graphicsLayer { alpha = turn; rotationY = (1 - turn) * 24f; transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f) }
                    .notebookPaper(24f).padding(start = 34.dp, end = 12.dp, top = 20.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val (label, color) = kindOf(page)
                Text(label.uppercase(), Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.18f)).padding(horizontal = 10.dp, vertical = 3.dp), fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 1.sp, color = Ink)
                Text(listOfNotNull(topic?.let { t -> subjects.firstOrNull { it.id == t.subjectId }?.name }, topic?.title).joinToString(" · "), fontSize = 12.sp, color = Muted)
                when (page) {
                    is StudyNotebook.Page.Key -> {
                        Text(if (page.lines.size == 1) "Dica de prova" else "O que não pode esquecer", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
                        page.lines.forEach { Text(inkInline(it), color = Ink, fontSize = 16.sp, lineHeight = 28.sp) }
                    }
                    is StudyNotebook.Page.MarkPage -> {
                        Text("O que você destacou", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
                        MarkQuote(page.mark)
                    }
                    is StudyNotebook.Page.Trap -> {
                        Text("A banca tenta te pegar aqui", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
                        Text(inkInline(page.text), color = Ink, fontSize = 16.sp, lineHeight = 28.sp)
                        StampText("PEGADINHA")
                    }
                    is StudyNotebook.Page.Recall -> FlipRecall(page)
                    is StudyNotebook.Page.Question -> FlipQuestion(viewModel, page.questionId, sessionId) { ok -> answers += page.topicId to ok }
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.weight(1f).clickable { onPage(page.topicId) }.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Description, null, Modifier.size(18.dp), tint = Color(0xFF5B4BE8)); Spacer(Modifier.width(6.dp))
                        Text("Abrir a página do tópico", color = Color(0xFF5B4BE8), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    br.com.estudario.ui.components.ReportErrorButton(br.com.estudario.data.remote.ReportKind.OTHER, { StudyNotebook.pageText(page) }, topic?.title)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)) {
            if (index > 0) OutlinedButton(onClick = { index-- }) { Text("Voltar") }
            Button(onClick = { index++ }) { Text(if (index == pages.lastIndex) "Terminar" else "Virar página", fontWeight = FontWeight.Black) }
        }
    }
}

@Composable
private fun ColumnScope.FlipRecall(page: StudyNotebook.Page.Recall) {
    var flipped by remember(page) { mutableStateOf(false) }
    val angle by animateFloatAsState(if (flipped) 180f else 0f, tween(500), label = "ficha")
    Text("Tente lembrar antes de virar", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
    Box(
        Modifier.fillMaxWidth().heightIn(min = 200.dp).graphicsLayer { rotationX = angle; cameraDistance = 14 * density }
            .clip(RoundedCornerShape(16.dp)).background(if (angle < 90f) CardBg else Color(0xFF332D78)).border(2.dp, LineC, RoundedCornerShape(16.dp))
            .clickable { flipped = !flipped }.padding(18.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (angle < 90f) Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(inkInline(page.question), fontWeight = FontWeight.Black, fontSize = 17.sp, color = Ink, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text("toque na ficha para ver a resposta", fontSize = 12.sp, color = Muted)
        } else Text(inkInline(page.answer), Modifier.graphicsLayer { rotationX = 180f }, color = Color.White, fontSize = 15.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
private fun ColumnScope.FlipQuestion(viewModel: AppViewModel, questionId: Long, sessionId: String, onAnswered: (Boolean) -> Unit) {
    val questions by viewModel.questions.collectAsState()
    val question = questions.firstOrNull { it.question.id == questionId }
    if (question == null) { Text("Esta questão não está mais no banco.", color = Muted); return }
    var picked by remember(questionId) { mutableStateOf<String?>(null) }
    var right by remember(questionId) { mutableStateOf<Boolean?>(null) }
    var crossed by remember(questionId) { mutableStateOf(setOf<String>()) }
    var sending by remember(questionId) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    Text("Uma que você errou: e agora?", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
    br.com.estudario.ui.components.MarkdownText(question.question.statement)
    br.com.estudario.ui.components.AnswerOptions(
        options = question.options,
        picked = picked,
        crossed = crossed,
        answered = right,
        onPick = { picked = it },
        onToggleCross = { k -> crossed = if (k in crossed) crossed - k else crossed + k; if (picked == k) picked = null },
        surface = CardBg, ink = Ink, line = LineC,
    )
    if (right == null) br.com.estudario.ui.components.ConfirmAnswerButton(enabled = picked != null && !sending) {
        val key = picked ?: return@ConfirmAnswerButton
        sending = true
        scope.launch { val ok = viewModel.answer(question, key, sessionId); right = ok; onAnswered(ok); br.com.estudario.ui.brand.EstudarioSom.play(context, if (ok) "acerto" else "erro") }
    }
    if (right != null) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            br.com.estudario.ui.assistant.Folha(64.dp, mood = if (right == true) br.com.estudario.ui.assistant.FolhaMood.HAPPY else br.com.estudario.ui.assistant.FolhaMood.SAD)
            Text(if (right == true) "Acertou!" else "Errou", fontWeight = FontWeight.Black, fontSize = 22.sp, color = if (right == true) Color(0xFF1E9E5A) else Color(0xFFD5432F))
        }
        PostIt(if (right == true) "Agora foi! Acertando de novo depois, ela sai do caderno de erros." else "Ainda não. Leia a explicação: ela volta depois.", if (right == true) Color(0xFFC8F5DD) else Color(0xFFFFD9D2))
        if (question.question.explanation.isNotBlank()) br.com.estudario.ui.components.MarkdownText(question.question.explanation)
    }
}

// ------------------------------------------------------------------ a página do tópico

@Composable
fun NotebookPageScreen(viewModel: AppViewModel, topicId: Long, onBack: () -> Unit, onTheory: (Long) -> Unit, onTrain: (Long) -> Unit) {
    val topics = rememberNotebookTopics(viewModel)
    val subjects by viewModel.subjects.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val prefs = androidx.compose.ui.platform.LocalContext.current.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
    LaunchedEffect(topicId) { prefs.edit().putLong("ultima", topicId).apply() }
    val topic = topics.firstOrNull { it.id == topicId }
    val now = remember { System.currentTimeMillis() }
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Voltar") }; Text("Caderno", fontWeight = FontWeight.Black) } }
        if (topic == null) {
            item { Text("Esta página ainda está em branco: estude o tópico (leia a teoria e marque como estudado) e ele vira página do seu caderno.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            item { Button(onClick = { onTheory(topicId) }) { Text("Abrir o tópico") } }
            return@LazyColumn
        }
        item {
            val lines = StudyNotebook.summaryLines(topic.summary, max = 14)
            val quotes = topic.marks.map { it.quote.trim() }.filter { it.length >= 8 }
            val thin = lines.size < 3 && topic.tips.size < 3 && topic.recall.size < 4
            Column(Modifier.fillMaxWidth().notebookPaper(24f).padding(start = 34.dp, end = 12.dp, top = 18.dp, bottom = 22.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text((subjects.firstOrNull { it.id == topic.subjectId }?.name ?: "").uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF5B4BE8), letterSpacing = 0.5.sp)
                Text(topic.title, fontWeight = FontWeight.Black, fontSize = 23.sp, color = Ink)
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val chip = @Composable { t: String, warn: Boolean -> Text(t, Modifier.clip(RoundedCornerShape(50)).background(if (warn) Color(0xFFFFE9D6) else Color(0xFFF1ECFF)).padding(horizontal = 9.dp, vertical = 3.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (warn) Color(0xFFB5560F) else Color(0xFF4232C9)) }
                    topic.lastSeenAt?.let { chip("visto ${since(it, now)}", false) }
                    if (topic.marks.isNotEmpty()) chip("${topic.marks.size} ${if (topic.marks.size == 1) "grifo" else "grifos"}", false)
                    if (topic.notes.isNotEmpty()) chip("${topic.notes.size} ${if (topic.notes.size == 1) "nota" else "notas"}", false)
                    if (topic.wrongQuestions.isNotEmpty()) chip("${topic.wrongQuestions.size} ${if (topic.wrongQuestions.size == 1) "erro" else "erros"} para refazer", true)
                }
                if (thin) Text("Este tópico veio com pouco material. Gerar de novo traz a matéria completa.", Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFF5D6)).clickable { onTheory(topic.id) }.padding(10.dp), color = Color(0xFF6E4700), fontSize = 13.sp)
                if (lines.isNotEmpty()) {
                    Section("O ESSENCIAL")
                    lines.forEach { line -> if (quotes.any { q -> line.contains(q.take(60), ignoreCase = true) }) Highlighted(line) else Text(inkInline(line), color = Ink, fontSize = 16.sp, lineHeight = 28.sp) }
                }
                if (topic.marks.isNotEmpty()) {
                    Section("SEUS GRIFOS")
                    topic.marks.take(8).forEach { m -> MarkQuote(m) }
                }
                topic.notes.take(3).forEach { PostIt(it) }
                if (topic.tips.isNotEmpty()) {
                    Section("DICAS DE PROVA")
                    topic.tips.take(6).forEach { Text(inkInline("• $it"), color = Ink, fontSize = 15.sp, lineHeight = 26.sp) }
                }
                topic.traps.take(3).forEach { trap -> StampText("PEGADINHA"); Text(inkInline(trap), color = Ink, fontSize = 15.sp, lineHeight = 26.sp) }
                if (topic.recall.isNotEmpty()) {
                    Section("PARA LEMBRAR · TOQUE PARA VIRAR")
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) { topic.recall.take(10).forEach { (q, a) -> MiniCard(q, a) } }
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onTheory(topic.id) }, Modifier.weight(1f)) { Icon(Icons.Outlined.MenuBook, null); Spacer(Modifier.width(6.dp)); Text("Ler a teoria") }
                    if (questions.any { it.question.topicId == topic.id && !it.question.isHidden }) Button(onClick = { onTrain(topic.id) }, Modifier.weight(1f)) { Icon(Icons.Outlined.Quiz, null); Spacer(Modifier.width(6.dp)); Text("Treinar 5") }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tem erro nesta página?", Modifier.weight(1f), fontSize = 13.sp, color = Muted)
                    br.com.estudario.ui.components.ReportErrorButton(br.com.estudario.data.remote.ReportKind.SUMMARY, { lines.take(3).joinToString(" ") }, topic.title)
                }
            }
        }
    }
}

@Composable
private fun Section(title: String) {
    Text(title, Modifier.padding(top = 10.dp), fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = Color(0xFFC98A0B))
}

@Composable
private fun MiniCard(front: String, back: String) {
    var on by remember { mutableStateOf(false) }
    val angle by animateFloatAsState(if (on) 180f else 0f, tween(450), label = "mini")
    Box(
        Modifier.width(170.dp).height(130.dp).graphicsLayer { rotationY = angle; cameraDistance = 12 * density }.clip(RoundedCornerShape(12.dp))
            .background(if (angle < 90f) CardBg else Color(0xFF332D78)).border(2.dp, LineC, RoundedCornerShape(12.dp)).clickable { on = !on }.padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (angle < 90f) Text(inkInline(front), fontWeight = FontWeight.Bold, color = Ink, fontSize = 13.sp, maxLines = 6, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        else Text(inkInline(back), Modifier.graphicsLayer { rotationY = 180f }, color = Color.White, fontSize = 12.5.sp, maxLines = 7, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

/**
 * As abas do Caderno: o caderno que se escreve sozinho e o que a pessoa guardou (grifos, notas,
 * questões e cartões salvos). Antes o segundo ficava num link no fim da página (o site faz igual).
 */
@Composable
fun NotebookTabs(saved: Boolean, onNotebook: () -> Unit, onSaved: () -> Unit, viewModel: AppViewModel) {
    val marks by viewModel.theoryMarks.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val count = marks.size + notes.size
    val scheme = MaterialTheme.colorScheme
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clip(RoundedCornerShape(18.dp)).background(scheme.surfaceContainerHigh).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        @Composable
        fun Tab(on: Boolean, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, badge: Int, onClick: () -> Unit) {
            Row(
                Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(if (on) scheme.surface else Color.Transparent).clickable(enabled = !on, onClick = onClick).padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(icon, null, Modifier.size(18.dp), tint = if (on) Color(0xFFC98A0B) else scheme.onSurfaceVariant)
                Spacer(Modifier.width(6.dp))
                Text(label, fontWeight = FontWeight.Black, fontSize = 13.sp, color = if (on) scheme.onSurface else scheme.onSurfaceVariant, maxLines = 1)
                if (badge > 0) { Spacer(Modifier.width(6.dp)); Text("$badge", Modifier.clip(RoundedCornerShape(50)).background(Gold).padding(horizontal = 6.dp, vertical = 1.dp), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF3B2300)) }
            }
        }
        Tab(!saved, "Caderno", Icons.Outlined.AutoStories, 0, onNotebook)
        Tab(saved, "Grifos e salvos", Icons.Outlined.Bookmarks, count, onSaved)
    }
}
