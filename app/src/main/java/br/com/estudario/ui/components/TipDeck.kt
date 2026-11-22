package br.com.estudario.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import br.com.estudario.ui.brand.IconButton
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.brand.Icon
import kotlinx.coroutines.launch

// ------------------------------------------------------------------ leitura do texto (o mesmo do site, TipCards.kt)

/**
 * Uma dica ou pegadinha reorganizada para ler fácil, sem mudar o texto: título curto (negrito do começo,
 * rótulo antes dos dois pontos ou primeira frase curta), parágrafos de uma frase e, quando o texto já traz
 * "Enunciado: '...'. Erro: ...", o par afirmação da banca e correção.
 */
data class TipShape(val title: String?, val paras: List<String>, val claim: String? = null, val fix: String? = null)

private val claimRe = Regex("""^\s*(?:\*\*)?(?:Enunciado|Afirma[çc][ãa]o|Assertiva|Item|Frase)(?:\*\*)?\s*:\s*(?:\*\*)?\s*['"“‘]([\s\S]+?)['"”’]\s*[.;]?\s*(?:\*\*)?\s*(?:Erro|Errado|Falso)(?:\*\*)?\s*:\s*(?:\*\*)?\s*([\s\S]+)$""", RegexOption.IGNORE_CASE)
private val errRe = Regex("""^([\s\S]{12,}?[.!?])\s+(?:\*\*)?(?:Erro|Errado)(?:\*\*)?\s*:\s*(?:\*\*)?\s*([\s\S]{8,})$""")
private val boldTitleRe = Regex("""^\s*\*\*([^*]{2,70})\*\*\s*[:.]?\s*""")
private val colonTitleRe = Regex("""^([^:.;!?*\n]{6,56}):\s+(\S[\s\S]*)$""")
private val sentenceEndRe = Regex("""[.!?…]["”’)]?\s+(?=[A-ZÁÉÍÓÚÂÊÔÃÕÀÇ“"'(])""")
private val listLineRe = Regex("""^\d+[.)]\s""")
private val abbreviations = setOf("art", "arts", "inc", "n", "nº", "p", "pág", "pg", "al", "dr", "dra", "sr", "sra", "ex", "obs", "cf", "etc", "fls", "min", "prof", "profa", "s.a", "ltda", "lc", "ec", "cp", "cpp", "cc", "cf/88")

private fun upperFirst(s: String) = s.replaceFirstChar { if (it.isLowerCase()) it.uppercaseChar() else it }

/** Frases de um parágrafo; lista e tabela ficam inteiras. "art." e outras abreviações não cortam. */
fun tipSentences(text: String): List<String> {
    val t = text.trim()
    if (t.isEmpty()) return emptyList()
    if (t.lines().any { val l = it.trimStart(); l.startsWith("- ") || l.startsWith("* ") || l.startsWith("|") || l.startsWith("#") || listLineRe.containsMatchIn(l) }) return listOf(t)
    val out = mutableListOf<String>()
    var start = 0
    for (m in sentenceEndRe.findAll(t)) {
        val piece = t.substring(start, m.range.first + 1)
        val last = piece.trimEnd().takeLastWhile { !it.isWhitespace() }.trimEnd('.', '!', '?', '…').lowercase()
        if (last in abbreviations) continue
        out += t.substring(start, m.range.last + 1).trim()
        start = m.range.last + 1
    }
    out += t.substring(start).trim()
    return out.filter { it.isNotBlank() }
}

fun tipShape(text: String, answer: String? = null): TipShape {
    val raw = text.trim()
    val extra = answer?.trim()?.takeIf { it.isNotBlank() }?.let { listOf(it) }.orEmpty()
    claimRe.find(raw)?.let { m -> return TipShape(null, extra, m.groupValues[1].trim(), upperFirst(m.groupValues[2].trim())) }
    var title: String? = null
    var rest = raw
    boldTitleRe.find(raw)?.let { m ->
        val after = raw.substring(m.range.last + 1).trim()
        if (after.isNotBlank()) { title = m.groupValues[1].trim().trimEnd(':', '.').trim(); rest = after }
    }
    if (title == null) colonTitleRe.find(raw)?.let { m -> title = m.groupValues[1].trim(); rest = upperFirst(m.groupValues[2].trim()) }
    errRe.find(rest)?.let { m -> return TipShape(title, extra, m.groupValues[1].trim(), upperFirst(m.groupValues[2].trim())) }
    var paras = tipSentences(rest)
    if (title == null && paras.size >= 2 && paras[0].length <= 90) { title = paras[0].trimEnd('.'); paras = paras.drop(1) }
    return TipShape(title, paras + extra)
}

/** Dica, pegadinha, atenção ou resumo dentro da teoria: rótulo (vira cabeçalho) e o texto sem ele. */
data class CalloutInfo(val kind: CalloutKind, val label: String, val body: String)

private fun kindOfLabel(label: String): CalloutKind? {
    val l = label.trim().lowercase()
    return when {
        listOf("pegadinha", "cuidado", "não confunda", "nao confunda", "erro comum", "armadilha").any { l.startsWith(it) } -> CalloutKind.TRAP
        l.startsWith("atenção") || l.startsWith("atencao") -> CalloutKind.WARN
        l.startsWith("resumo") || l.startsWith("resumindo") || l.startsWith("na prova") -> CalloutKind.SUMMARY
        listOf("dica", "bizu", "macete", "lembre", "memorize").any { l.startsWith(it) } -> CalloutKind.TIP
        else -> null
    }
}

fun calloutName(kind: CalloutKind) = when (kind) { CalloutKind.TRAP -> "Pegadinha"; CalloutKind.WARN -> "Atenção"; CalloutKind.SUMMARY -> "Resumo"; CalloutKind.TIP -> "Dica" }

private val calloutLabelRe = Regex("""^\*\*([^*]{2,40}?)\s*:?\s*\*\*\s*:?\s*""")

fun calloutInfoOf(block: String): CalloutInfo? {
    val start = block.trimStart()
    val quoted = start.startsWith(">")
    val text = if (quoted) calloutBody(start).trim() else start
    if (text.lines().any { it.trimStart().startsWith("|") } && !quoted) return null
    calloutLabelRe.find(text)?.let { m ->
        val kind = kindOfLabel(m.groupValues[1])
        val body = text.substring(m.range.last + 1).trim()
        if (kind != null && (text.substring(0, m.range.last + 1).contains(":") || quoted) && body.isNotBlank()) return CalloutInfo(kind, m.groupValues[1].trim().trimEnd(':').trim(), body)
    }
    if (!quoted) return null
    val kind = kindOfLabel(text.trimStart('*', '_', ' ', '#').take(40)) ?: return null
    return CalloutInfo(kind, calloutName(kind), text)
}

/** O texto da caixa em frases curtas, uma por parágrafo (só reorganiza, não muda nada). */
fun calloutParagraphs(info: CalloutInfo): String = tipSentences(info.body).joinToString("\n\n") { upperFirst(it) }

// ------------------------------------------------------------------ cores e desenhos

data class TipPalette(val bg: Color, val line: Color, val ink: Color, val row: Color)

@Composable
fun tipPalette(kind: CalloutKind): TipPalette {
    val dark = MaterialTheme.colorScheme.background.red < 0.5f
    return when (kind) {
        CalloutKind.TIP -> if (dark) TipPalette(Color(0xFF18221E), Color(0xFF2D463B), Color(0xFF92DDBD), Color(0xFF1E2B26)) else TipPalette(Color(0xFFF1FAF5), Color(0xFFC3E7D4), Color(0xFF0B6A4A), Color.White)
        CalloutKind.TRAP -> if (dark) TipPalette(Color(0xFF251D1A), Color(0xFF4C3229), Color(0xFFFFB49C), Color(0xFF2E2420)) else TipPalette(Color(0xFFFFF5F0), Color(0xFFF6CDBD), Color(0xFFB03C22), Color.White)
        CalloutKind.WARN -> if (dark) TipPalette(Color(0xFF24201A), Color(0xFF4A3F29), Color(0xFFF2CD7E), Color(0xFF2D281F)) else TipPalette(Color(0xFFFFF8EA), Color(0xFFF0D9A4), Color(0xFF855400), Color.White)
        CalloutKind.SUMMARY -> if (dark) TipPalette(Color(0xFF1E1F2B), Color(0xFF383B58), Color(0xFFBEC1FF), Color(0xFF252736)) else TipPalette(Color(0xFFF4F3FF), Color(0xFFD5D1F6), Color(0xFF4A3EB3), Color.White)
    }
}

private val BAD = Color(0xFFEE6A4B)
private val GOOD = Color(0xFF1FA97F)

/** Ratoeira da turma (pegadinha), lâmpada com carinha (dica) e o Folha apontando (atenção e resumo). */
@Composable
fun TipArt(kind: CalloutKind, size: Dp) {
    when (kind) {
        CalloutKind.TRAP -> BrandIcon(Glyph.Trap, size = size)
        CalloutKind.TIP -> BrandIcon(Glyph.Bulb, size = size)
        else -> {
            val px = with(LocalDensity.current) { size.roundToPx() }
            val bmp = remember(px) { br.com.estudario.ui.assistant.folhaBitmap(px, br.com.estudario.ui.assistant.FolhaMood.POINT).asImageBitmap() }
            Image(bmp, null, Modifier.size(size))
        }
    }
}

// ------------------------------------------------------------------ cartões

/** Uma dica ou pegadinha (do tópico), sem depender do banco. */
data class TipItem(val id: Long, val text: String, val answer: String?, val trap: Boolean, val favorite: Boolean)

private val bodySize = 18.sp
private val bodyLine = 29.sp

@Composable
private fun TipText(text: String, size: androidx.compose.ui.unit.TextUnit = bodySize, weight: FontWeight? = null, color: Color = MaterialTheme.colorScheme.onSurface) {
    StudyInlineText(text, style = MaterialTheme.typography.bodyLarge.copy(fontSize = size, lineHeight = size * 1.62f), fontWeight = weight, color = color)
}

@Composable
private fun ContrastRows(claim: String, fix: String?, pal: TipPalette) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        listOfNotNull("✘  PEGADINHA" to "“$claim”", fix?.let { "✔  CERTO" to it }).forEachIndexed { i, (label, text) ->
            val accent = if (i == 0) BAD else GOOD
            Row(Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min).clip(RoundedCornerShape(16.dp)).background(pal.row)) {
                Box(Modifier.width(5.dp).fillMaxHeight().background(accent))
                Column(Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = if (i == 0) (if (pal.row == Color.White) Color(0xFFC94A2C) else Color(0xFFFFB49C)) else (if (pal.row == Color.White) Color(0xFF128A62) else Color(0xFF92DDBD)), letterSpacing = 1.2.sp)
                    TipText(text, 17.5.sp)
                }
            }
        }
    }
}


@Composable
private fun TipBody(shape: TipShape, pal: TipPalette) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        shape.title?.let { TipText(it, 21.sp, FontWeight.ExtraBold) }
        if (shape.claim != null) ContrastRows(shape.claim, shape.fix, pal)
        shape.paras.forEachIndexed { i, p ->
            if (i == 0 && shape.title == null && shape.claim == null) TipText(p, 20.sp, FontWeight.SemiBold) else TipText(p)
        }
    }
}

@Composable
private fun TipTop(kind: CalloutKind, label: String, counter: String?, pal: TipPalette, trailing: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TipArt(kind, 46.dp)
        Spacer(Modifier.width(12.dp))
        Text(label.uppercase(), Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = pal.ink, letterSpacing = 1.4.sp)
        counter?.let { Text(it, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline) }
        trailing()
    }
}

@Composable
private fun TipFrame(pal: TipPalette, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier.fillMaxWidth().clip(shape).background(pal.bg).border(2.dp, pal.line, shape).padding(start = 20.dp, end = 12.dp, top = 16.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) { content() }
}

@Composable
private fun Star(item: TipItem, onToggleFavorite: (TipItem) -> Unit) {
    IconButton(onClick = { onToggleFavorite(item) }) {
        Icon(if (item.favorite) Icons.Outlined.Star else Icons.Outlined.StarBorder, if (item.favorite) "Tirar do Caderno" else "Guardar no Caderno", tint = if (item.favorite) Color.Unspecified else MaterialTheme.colorScheme.outline)
    }
}

/** Um cartão grande, uma ideia só. Pegadinha com a frase da banca vira desafio de virar o cartão. */
@Composable
fun TipBigCard(item: TipItem, counter: String?, onToggleFavorite: (TipItem) -> Unit, modifier: Modifier = Modifier) {
    val shape = remember(item.text, item.answer) { tipShape(item.text, item.answer) }
    val kind = if (item.trap) CalloutKind.TRAP else CalloutKind.TIP
    val pal = tipPalette(kind)
    if (item.trap && shape.claim != null) { TrapChallenge(item, shape, counter, pal, onToggleFavorite, modifier); return }
    TipFrame(pal, modifier) {
        TipTop(kind, calloutName(kind), counter, pal) { Star(item, onToggleFavorite) }
        Box(Modifier.padding(end = 8.dp)) { TipBody(shape, pal) }
    }
}

/** "Verdadeiro ou pegadinha?": a frase da banca (sempre falsa), a escolha, e o cartão vira mostrando o erro. */
@Composable
private fun TrapChallenge(item: TipItem, shape: TipShape, counter: String?, pal: TipPalette, onToggleFavorite: (TipItem) -> Unit, modifier: Modifier) {
    var answer by rememberSaveable(item.id) { mutableIntStateOf(0) }
    val squash = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val somCtx = androidx.compose.ui.platform.LocalContext.current
    fun choose(v: Int) {
        scope.launch {
            squash.animateTo(0f, tween(130))
            answer = v
            squash.animateTo(1f, tween(170))
        }
    }
    TipFrame(pal, modifier.graphicsLayer { scaleX = squash.value.coerceAtLeast(0.02f) }) {
        if (answer == 0) {
            TipTop(CalloutKind.TRAP, "Verdadeiro ou pegadinha?", counter, pal) {}
            Column(Modifier.padding(end = 8.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                TipText("“${shape.claim}”", 21.sp, FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = { br.com.estudario.ui.brand.EstudarioSom.play(somCtx, "nao_lembrei"); choose(1) }, Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(16.dp)) { Text("✔ Verdadeiro", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) }
                    OutlinedButton(onClick = { br.com.estudario.ui.brand.EstudarioSom.play(somCtx, "lembrei"); choose(2) }, Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(16.dp)) { Text("✘ Pegadinha", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) }
                }
            }
        } else {
            TipTop(CalloutKind.TRAP, if (answer == 2) "Isso! Era pegadinha" else "Caiu! Era pegadinha", counter, pal) { Star(item, onToggleFavorite) }
            Column(Modifier.padding(end = 8.dp)) {
                TipBody(shape, pal)
                TextButton(onClick = { choose(0) }, Modifier.padding(top = 6.dp)) { Text("Ver a frase de novo", color = pal.ink, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

/** Um cartão por vez: "3 de 8", setas, deslizar para os lados e bolinhas de cada cartão. */
@Composable
fun TipDeck(items: List<TipItem>, onToggleFavorite: (TipItem) -> Unit, modifier: Modifier = Modifier) {
    if (items.isEmpty()) return
    var index by rememberSaveable(items.size) { mutableIntStateOf(0) }
    var dir by remember { mutableIntStateOf(1) }
    val i = index.coerceIn(0, items.lastIndex)
    // Lê o índice na hora do gesto (o detector de arraste guarda esta função de quando foi criado).
    fun go(d: Int) { dir = d; index = (index.coerceIn(0, items.lastIndex) + d + items.size) % items.size }
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
        val drag = remember { floatArrayOf(0f) }
        AnimatedContent(
            targetState = i,
            transitionSpec = { (slideInHorizontally { w -> dir * w / 3 } + fadeIn(tween(220))) togetherWith (slideOutHorizontally { w -> -dir * w / 3 } + fadeOut(tween(160))) },
            modifier = Modifier.fillMaxWidth().pointerInput(items.size) {
                detectHorizontalDragGestures(
                    onDragStart = { drag[0] = 0f },
                    onDragEnd = { if (drag[0] < -120f) go(1) else if (drag[0] > 120f) go(-1) },
                    onHorizontalDrag = { _, amount -> drag[0] += amount },
                )
            },
            label = "tip-deck",
        ) { at ->
            val item = items[at.coerceIn(0, items.lastIndex)]
            TipBigCard(item, "${at + 1} de ${items.size}", onToggleFavorite)
        }
        if (items.size > 1) Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            DeckArrow(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, "Anterior") { go(-1) }
            Row(Modifier.widthIn(max = 220.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                val shown = if (items.size <= 12) items.indices.toList() else ((i - 5).coerceAtLeast(0)..(i + 6).coerceAtMost(items.lastIndex)).toList()
                shown.forEach { j ->
                    Box(
                        Modifier.height(9.dp).width(if (j == i) 24.dp else 9.dp).clip(CircleShape)
                            .background((if (items[j].trap) BAD else GOOD).copy(alpha = if (j == i) 1f else 0.32f))
                            .clickable { dir = if (j > i) 1 else -1; index = j },
                    )
                }
            }
            DeckArrow(Icons.AutoMirrored.Outlined.KeyboardArrowRight, "Próxima") { go(1) }
        }
    }
}

@Composable
private fun DeckArrow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Box(
        Modifier.size(50.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape).background(MaterialTheme.colorScheme.surface).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { androidx.compose.material3.Icon(icon, label, Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onSurface) }
}

/** Escolha entre um cartão por vez e todas em lista. */
@Composable
fun TipModeSwitch(all: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(false to "Uma por vez", true to "Ver todas").forEach { (value, label) ->
            androidx.compose.material3.FilterChip(selected = all == value, onClick = { onChange(value) }, label = { Text(label, fontWeight = FontWeight.Bold) })
        }
    }
}
