package br.com.estudario.ui.studio

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import br.com.estudario.ui.theme.EstudarioTheme
import kotlin.math.roundToInt

/**
 * O estúdio (como o gx-* do site): a cara de console escuro do Perfil de jogo, usada ao gerar
 * material e mais questões, ao montar o treino, nos simulados e nos ajustes. Tudo que se toca afunda
 * e volta com mola, a escolha acende na cor dela e os desenhos são os da turma do Folha, que se mexem
 * ao toque. Os painéis rodam no tema escuro do app para os controles do Material ficarem certos.
 */
object Gx {
    private val d: Boolean get() = br.com.estudario.ui.theme.CxTheme.dark
    val bg: Color get() = if (d) Color(0xFF12143A) else Color(0xFFF3F1FF)
    val bg2: Color get() = if (d) Color(0xFF1C1B52) else Color(0xFFFFFFFF)
    val bg3: Color get() = if (d) Color(0xFF262467) else Color(0xFFECE8FF)
    val bg0: Color get() = if (d) Color(0xFF0B0C26) else Color(0xFFE9E5FF)
    val accentText: Color get() = if (d) Color(0xFFC9BEFF) else Color(0xFF5B44E0)
    val soft: Color get() = if (d) Color(0xFFC9C5F0) else Color(0xFF48447A)
    /** Fundo de botão desligado. */
    val off: Color get() = if (d) Color(0xFF2A2860) else Color(0xFFDCD8F2)
    val ink: Color get() = if (d) Color(0xFFF4F2FF) else Color(0xFF1F1B4D)
    val strong: Color get() = if (d) Color.White else Color(0xFF14113A)
    val mute: Color get() = if (d) Color(0xFFA9A6D6) else Color(0xFF6B6894)
    val line: Color get() = if (d) Color(0x33A78BFF) else Color(0x2E5B4BF5)
    val track: Color get() = tint(0.08f)
    fun tint(alpha: Float): Color = br.com.estudario.ui.theme.CxTheme.tint(alpha)
    /** No claro, a cor do destaque um tom mais funda, para texto ler bem no fundo branco. */
    fun readable(c: Color): Color = if (d) c else androidx.compose.ui.graphics.lerp(c, Color(0xFF14113A), 0.38f)
    val violet = Color(0xFFA78BFF)
    val mint = Color(0xFF2FD39A)
    val gold = Color(0xFFFFC94D)
    val red = Color(0xFFFF5A7A)
    val cyan = Color(0xFF22D3EE)
    val orange = Color(0xFFFF8A3D)
    val blue = Color(0xFF6F9BFF)
    val pink = Color(0xFFF472B6)
}

/** Uma opção do estúdio: título, dica, desenho da turma e a cor dela. */
data class GxOption<T>(val key: T, val title: String, val hint: String = "", val glyph: Glyph? = null, val color: Color = Gx.violet)

/** Toque de botão de controle: afunda ao apertar e volta com mola. */
@Composable
fun Modifier.gxClick(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, spring(dampingRatio = 0.42f, stiffness = 700f), label = "gx-toque")
    return this.graphicsLayer { scaleX = scale; scaleY = scale }.clickable(source, LocalIndication.current, enabled = enabled, onClick = onClick)
}

/** O fundo do estúdio: degradê escuro com a grade fina, no tema escuro do app. */
@Composable
fun StudioBackdrop(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    run {
        Box(
            modifier
                .background(Brush.verticalGradient(listOf(Gx.bg2, Gx.bg, Gx.bg0)))
                .drawBehind {
                    val step = 26.dp.toPx()
                    var x = 0f
                    while (x < size.width) { drawLine(Gx.tint(0.03f), Offset(x, 0f), Offset(x, size.height)); x += step }
                    var y = 0f
                    while (y < size.height) { drawLine(Gx.tint(0.03f), Offset(0f, y), Offset(size.width, y)); y += step }
                    drawCircle(Brush.radialGradient(listOf(Color(0x387C5CFF), Color.Transparent), Offset(size.width, 0f), size.width * 0.8f), size.width * 0.8f, Offset(size.width, 0f))
                },
        ) { content() }
    }
}

/** Painel do estúdio (cartão escuro com borda fina), para usar dentro de telas claras também. */
@Composable
fun GxPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(26.dp)
    run {
        Column(
            modifier.fillMaxWidth().clip(shape)
                .background(Brush.linearGradient(listOf(Gx.bg2, Gx.bg)))
                .drawBehind { drawCircle(Brush.radialGradient(listOf(Color(0x387C5CFF), Color.Transparent), Offset(size.width, 0f), size.width * 0.7f), size.width * 0.7f, Offset(size.width, 0f)) }
                .border(1.dp, Gx.line, shape),
            content = content,
        )
    }
}

/** O rótulo em caixa alta do estúdio. */
@Composable
fun GxKicker(text: String, color: Color = Gx.accentText) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Gx.readable(color), letterSpacing = 1.8.sp)
}

/** Uma parte do pedido: desenho na cor da parte, título, dica e o conteúdo. */
@Composable
fun GxSection(glyph: Glyph, title: String, hint: String? = null, color: Color = Gx.violet, aside: String? = null, error: String? = null, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        Modifier.fillMaxWidth().clip(shape)
            .background(Brush.linearGradient(listOf(Gx.tint(0.045f), Gx.tint(0.015f))))
            .border(1.dp, if (error != null) Gx.red.copy(alpha = 0.6f) else Gx.tint(0.07f), shape)
            .drawWithContent {
                drawContent()
                drawLine(Brush.horizontalGradient(listOf(Color.Transparent, color, Color.Transparent)), Offset(16.dp.toPx(), 0f), Offset(size.width - 16.dp.toPx(), 0f), 2.dp.toPx())
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(Gx.tint(0.06f)).border(1.5.dp, color.copy(alpha = 0.55f), RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center,
            ) { BrandIcon(glyph, size = 32.dp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Gx.strong)
                hint?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Gx.mute) }
            }
            if (aside != null) Text(
                aside, Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.14f)).border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color,
            )
        }
        content()
        error?.let { Text(it, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFFFF9AAE)) }
    }
}

/** Rótulo pequeno dentro de uma parte. */
@Composable
fun GxLabel(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Gx.mute, letterSpacing = 1.4.sp, modifier = Modifier.padding(top = 2.dp))
}

/** Ladrilhos grandes: cada um com a sua cor; [multi] marca vários (com ✓), senão escolhe um. */
@Composable
fun <T> GxTiles(options: List<GxOption<T>>, isOn: (T) -> Boolean, columns: Int = 2, multi: Boolean = true, showHint: Boolean = true, onToggle: (T) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.chunked(columns).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { option -> GxTile(option, isOn(option.key), multi, showHint, Modifier.weight(1f)) { onToggle(option.key) } }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun <T> GxTile(option: GxOption<T>, on: Boolean, multi: Boolean, showHint: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    val c = option.color
    val tilt by animateFloatAsState(if (on) -6f else 0f, spring(dampingRatio = 0.4f, stiffness = 400f), label = "gx-ic")
    val tick by animateFloatAsState(if (on) 1f else 0.6f, spring(dampingRatio = 0.35f, stiffness = 500f), label = "gx-tick")
    Box(
        modifier.heightIn(min = 108.dp).gxClick(onClick = onClick).clip(shape)
            .background(if (on) Brush.linearGradient(listOf(c.copy(alpha = 0.24f), Gx.tint(0.03f))) else SolidColor(Gx.tint(0.035f)))
            .border(1.5.dp, if (on) c else Gx.tint(0.09f), shape)
            .padding(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            option.glyph?.let { g ->
                Box(
                    Modifier.size(44.dp).graphicsLayer { rotationZ = tilt }.clip(RoundedCornerShape(13.dp)).background(c.copy(alpha = if (on) 0.28f else 0.16f)),
                    contentAlignment = Alignment.Center,
                ) { BrandIcon(g, size = 36.dp) }
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(option.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = Gx.ink, maxLines = 2)
                if (showHint && option.hint.isNotBlank()) Text(option.hint, style = MaterialTheme.typography.bodySmall, color = Gx.mute, maxLines = 3)
            }
        }
        Box(
            Modifier.align(Alignment.TopEnd).size(24.dp).graphicsLayer { scaleX = tick; scaleY = tick }.clip(CircleShape)
                .background(if (on) c else Color.Transparent).border(2.dp, if (on) c else Gx.tint(0.22f), CircleShape),
            contentAlignment = Alignment.Center,
        ) { if (on) Icon(Icons.Rounded.Check, null, Modifier.size(16.dp), tint = Color(0xFF111111)) }
    }
}

/** Escolha única em cartões (com dica embaixo do título) e um desenho opcional em cima. */
@Composable
fun <T> GxSeg(options: List<GxOption<T>>, selected: T, columns: Int = 2, onPick: (T) -> Unit, extra: @Composable (GxOption<T>) -> Unit = {}) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.chunked(columns).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { option ->
                    val on = option.key == selected
                    val shape = RoundedCornerShape(16.dp)
                    Column(
                        Modifier.weight(1f).gxClick { onPick(option.key) }.clip(shape)
                            .background(if (on) Brush.linearGradient(listOf(option.color.copy(alpha = 0.24f), Gx.tint(0.03f))) else SolidColor(Gx.tint(0.035f)))
                            .border(1.5.dp, if (on) option.color else Gx.tint(0.09f), shape)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        extra(option)
                        Text(option.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = if (on) Color.White else Gx.ink)
                        if (option.hint.isNotBlank()) Text(option.hint, style = MaterialTheme.typography.bodySmall, color = Gx.mute)
                    }
                }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

/** Atalhos em pílula (receitas: Pacote completo, Só questões...). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> GxPresets(options: List<GxOption<T>>, isOn: (T) -> Boolean, onPick: (T) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val on = isOn(option.key)
            Row(
                Modifier.gxClick { onPick(option.key) }.clip(RoundedCornerShape(50))
                    .background(if (on) Brush.verticalGradient(listOf(Color(0xFFE4DCFF), Gx.violet)) else SolidColor(Gx.tint(0.05f)))
                    .border(1.dp, if (on) Color.Transparent else Gx.tint(0.12f), RoundedCornerShape(50))
                    .padding(start = 8.dp, end = 14.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                option.glyph?.let { BrandIcon(it, size = 24.dp); Spacer(Modifier.width(6.dp)) }
                Text(option.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = if (on) Color(0xFF1A1440) else Gx.ink)
            }
        }
    }
}

/** Chips do estúdio (filtros de uma escolha). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> GxChips(options: List<Pair<T, String>>, selected: T, onPick: (T) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        options.forEach { (key, label) ->
            val on = key == selected
            Row(
                Modifier.gxClick { onPick(key) }.clip(RoundedCornerShape(50))
                    .background(if (on) Brush.verticalGradient(listOf(Color.White, Color(0xFFCFC4FF))) else SolidColor(Gx.tint(0.05f)))
                    .border(1.dp, if (on) Color.Transparent else Gx.tint(0.12f), RoundedCornerShape(50))
                    .padding(horizontal = 13.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (on) { Icon(Icons.Rounded.Check, null, Modifier.size(15.dp), tint = Color(0xFF1A1440)); Spacer(Modifier.width(4.dp)) }
                Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = if (on) Color(0xFF1A1440) else Gx.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

/** Campo de texto do estúdio, com um desenho à esquerda e o botão de limpar. */
@Composable
fun GxField(value: String, onChange: (String) -> Unit, placeholder: String, glyph: Glyph = Glyph.Lupa, maxLength: Int = 60) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        Modifier.fillMaxWidth().height(48.dp).clip(shape).background(Color.Black.copy(alpha = 0.25f)).border(1.5.dp, Gx.tint(0.12f), shape).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BrandIcon(glyph, size = 26.dp)
        Spacer(Modifier.width(8.dp))
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) Text(placeholder, color = Gx.mute, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            BasicTextField(
                value, { onChange(it.take(maxLength)) }, singleLine = true,
                textStyle = TextStyle(color = Gx.strong, fontSize = 15.sp, fontWeight = FontWeight.Bold),
                cursorBrush = SolidColor(Gx.violet), modifier = Modifier.fillMaxWidth(),
            )
        }
        if (value.isNotEmpty()) Box(Modifier.size(28.dp).clip(CircleShape).background(Gx.tint(0.1f)).clickable { onChange("") }, contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Close, "Limpar", Modifier.size(16.dp), tint = Color.White)
        }
    }
}

/**
 * Escolha única numa lista que pode ser enorme (matérias, tópicos, bancas): as primeiras [limit]
 * (a escolhida sempre entre elas), uma busca quando a lista é grande e "Ver todas" com o resto.
 */
@Composable
fun <T> GxPicker(options: List<Pair<T, String>>, selected: T, limit: Int = 8, searchHint: String = "Buscar", onPick: (T) -> Unit) {
    if (options.size <= limit + 1) { GxChips(options, selected, onPick); return }
    var query by remember { mutableStateOf("") }
    var open by remember { mutableStateOf(false) }
    val q = query.trim().lowercase()
    val shown = when {
        q.isNotEmpty() -> options.filter { it.second.lowercase().contains(q) }
        open -> options
        else -> options.take(limit).let { head -> if (head.any { it.first == selected }) head else head.dropLast(1) + options.filter { it.first == selected } }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GxField(query, { query = it }, searchHint)
        GxChips(shown, selected) { onPick(it); query = "" }
        if (shown.isEmpty()) Text("Nada encontrado para “$query”.", style = MaterialTheme.typography.bodySmall, color = Gx.mute)
        if (q.isEmpty()) Row(
            Modifier.gxClick { open = !open }.clip(RoundedCornerShape(50)).border(1.dp, Gx.violet.copy(alpha = 0.45f), RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(if (open) "Mostrar menos" else "Ver todas (${options.size})", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Gx.accentText)
            Icon(if (open) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, null, Modifier.size(18.dp), tint = Gx.accentText)
        }
    }
}

/** O número grande: − e + redondos, a régua para arrastar e os atalhos. */
@Composable
fun GxCounter(
    value: Int,
    onChange: (Int) -> Unit,
    range: IntRange,
    step: Int = 1,
    unit: String = "",
    quick: List<Int> = emptyList(),
    mood: (Int) -> String? = { null },
    slider: Boolean = true,
) {
    val current = value.coerceIn(range)
    val haptic = LocalHapticFeedback.current
    fun set(next: Int) {
        val clamped = next.coerceIn(range)
        if (clamped != current) { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onChange(clamped) }
    }
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color.Black.copy(alpha = 0.22f)).border(1.dp, Gx.tint(0.06f), RoundedCornerShape(18.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GxRound("−", current > range.first) { set(current - step) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedContent(current, transitionSpec = { (scaleIn(initialScale = 0.75f) + fadeIn()) togetherWith fadeOut() }, label = "gx-num") { shown ->
                    Text("$shown", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Gx.strong, letterSpacing = (-1).sp)
                }
                if (unit.isNotBlank()) Text(unit.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Gx.mute, letterSpacing = 1.4.sp)
            }
            GxRound("+", current < range.last) { set(current + step) }
        }
        val span = range.last - range.first
        if (slider && span in 1..400) Slider(
            value = current.toFloat(),
            onValueChange = { set((((it - range.first) / step).roundToInt() * step + range.first)) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF7C5CFF), inactiveTrackColor = Gx.track, activeTickColor = Color.Transparent, inactiveTickColor = Color.Transparent),
        )
        mood(current)?.let { Text(it, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = Gx.mute, textAlign = TextAlign.Center) }
        val chips = quick.filter { it in range }
        if (chips.isNotEmpty()) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)) {
            chips.forEach { n ->
                val on = n == current
                Text(
                    "$n",
                    Modifier.widthIn(min = 48.dp).gxClick { set(n) }.clip(RoundedCornerShape(10.dp))
                        .background(if (on) Gx.mint else Gx.tint(0.06f))
                        .border(1.dp, if (on) Gx.mint else Gx.tint(0.12f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center,
                    color = if (on) Color(0xFF0B3B29) else Gx.ink,
                )
            }
        }
    }
}

@Composable
private fun GxRound(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.size(52.dp).gxClick(enabled = enabled, onClick = onClick).clip(CircleShape)
            .background(if (enabled) Brush.verticalGradient(listOf(Color(0xFFE4DCFF), Gx.violet)) else SolidColor(Gx.tint(0.1f))),
        contentAlignment = Alignment.Center,
    ) { Text(label, fontSize = 26.sp, fontWeight = FontWeight.Black, color = if (enabled) Color(0xFF1A1440) else Gx.mute) }
}

/** Uma linha de liga e desliga, grande. */
@Composable
fun GxSwitchRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val knob by animateFloatAsState(if (checked) 1f else 0f, spring(dampingRatio = 0.5f, stiffness = 500f), label = "gx-sw")
    Row(
        Modifier.fillMaxWidth().gxClick { onChange(!checked) }.clip(shape)
            .background(if (checked) Brush.horizontalGradient(listOf(Gx.mint.copy(alpha = 0.12f), Gx.tint(0.02f))) else SolidColor(Gx.tint(0.035f)))
            .border(1.5.dp, if (checked) Gx.mint.copy(alpha = 0.45f) else Gx.tint(0.08f), shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Gx.ink)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Gx.mute)
        }
        Spacer(Modifier.width(12.dp))
        Box(Modifier.size(54.dp, 32.dp).clip(RoundedCornerShape(50)).background(if (checked) Gx.mint else Gx.tint(0.14f)).padding(4.dp)) {
            Box(Modifier.graphicsLayer { translationX = knob * 22.dp.toPx() }.size(24.dp).clip(CircleShape).background(Color.White))
        }
    }
}

/** O botão de lançar: grande, em degradê, com brilho correndo e o desenho da turma. */
@Composable
fun GxLaunch(label: String, glyph: Glyph = Glyph.Pencil, enabled: Boolean = true, modifier: Modifier = Modifier.fillMaxWidth(), colors: List<Color> = listOf(Color(0xFF7C5CFF), Color(0xFF5B4BF5), Color(0xFF2FD39A)), ink: Color = Color.White, onClick: () -> Unit) {
    val run by rememberInfiniteTransition(label = "gx-brilho").animateFloat(-0.3f, 1.3f, infiniteRepeatable(tween(3_200, easing = LinearEasing)), label = "gx-brilho-x")
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier.heightIn(min = 58.dp).gxClick(enabled = enabled, onClick = onClick).clip(shape)
            .background(if (enabled) Brush.horizontalGradient(colors) else SolidColor(Gx.off))
            .drawWithContent {
                drawContent()
                if (enabled) {
                    val x = size.width * run
                    drawRect(Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent), Offset(x - 60f, 0f), Offset(x + 60f, size.height)))
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 20.dp)) {
            BrandIcon(glyph, size = 30.dp, muted = !enabled)
            Spacer(Modifier.width(10.dp))
            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = if (enabled) ink else Gx.mute, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

/** A grossura do livro: cresce com a profundidade (1, 2 ou 3). */
@Composable
fun GxBook(level: Int, color: Color, on: Boolean) {
    Column(Modifier.height(26.dp).padding(bottom = 4.dp), verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Bottom)) {
        repeat(level) { i -> Box(Modifier.width((30 + i * 4).dp).height(6.dp).clip(RoundedCornerShape(2.dp)).background(color.copy(alpha = if (on) 1f else 0.5f))) }
    }
}

/** O nível em barrinhas (1, 2, 3; 0 = misturado, uma cor por barra). */
@Composable
fun GxMeter(bars: Int, color: Color) {
    Row(Modifier.height(22.dp).padding(bottom = 4.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        val mixed = listOf(Gx.mint, Gx.gold, Gx.red)
        for (i in 1..3) Box(
            Modifier.width(7.dp).height((2 + i * 5).dp).clip(RoundedCornerShape(2.dp))
                .background(if (bars == 0) mixed[i - 1] else if (i <= bars) color else Gx.tint(0.15f)),
        )
    }
}

/** Miniatura do formato: alternativas de A a E, de A a D, o Certo/Errado ou os dois. */
@Composable
fun GxFormatMock(letters: String, certoErrado: Boolean, color: Color, on: Boolean) {
    Column(
        Modifier.fillMaxWidth().padding(bottom = 6.dp).clip(RoundedCornerShape(9.dp)).background(Color.Black.copy(alpha = 0.25f)).padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        letters.forEachIndexed { i, l ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(13.dp, 10.dp).clip(RoundedCornerShape(3.dp)).background(if (on && i == 0) color else Gx.tint(0.12f)), contentAlignment = Alignment.Center) {
                    Text("$l", fontSize = 7.sp, fontWeight = FontWeight.Black, color = if (on && i == 0) Color(0xFF111111) else Color.White, lineHeight = 8.sp)
                }
                Spacer(Modifier.width(4.dp))
                Box(Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Gx.tint(0.14f)))
            }
        }
        if (certoErrado) Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("C", Modifier.weight(1f).clip(RoundedCornerShape(4.dp)).background(Gx.mint.copy(alpha = 0.25f)).padding(vertical = 1.dp), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF8BF0CB), textAlign = TextAlign.Center)
            Text("E", Modifier.weight(1f).clip(RoundedCornerShape(4.dp)).background(Gx.red.copy(alpha = 0.25f)).padding(vertical = 1.dp), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF9AAE), textAlign = TextAlign.Center)
        }
    }
}

/** Barra de progresso fina do estúdio. */
@Composable
fun GxBar(fraction: Float, color: Color, height: Dp = 8.dp) {
    val shown by animateFloatAsState(fraction.coerceIn(0f, 1f), tween(800, easing = FastOutSlowInEasing), label = "gx-bar")
    Box(Modifier.fillMaxWidth().height(height).clip(RoundedCornerShape(50)).background(Gx.track)) {
        Box(Modifier.fillMaxWidth(shown).fillMaxHeight().clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(Color(0xFF7C5CFF), color))))
    }
}
