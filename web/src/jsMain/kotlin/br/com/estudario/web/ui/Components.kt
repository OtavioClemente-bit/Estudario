package br.com.estudario.web.ui

import org.jetbrains.compose.web.svg.Svg
import org.jetbrains.compose.web.svg.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.type
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement

/**
 * Ícone. Os de significado (casa, calendário, troféu, chama...) são os desenhos do Estudário
 * (glyphs.js, os mesmos de BrandGlyphs.kt no app); os de controle (setas, fechar, mais) continuam
 * o traço do Material Symbols. "faint" deixa o desenho em cinza, como o tint apagado do app.
 */
@Composable
fun Icon(name: String, filled: Boolean = false, extraClass: String? = null, plain: Boolean = false) {
    val muted = extraClass == "faint"
    val uri = if (plain) null else androidx.compose.runtime.remember(name, muted) { glyphUri(name, muted) }
    if (uri != null) {
        Span({
            classes(*listOfNotNull("ms", "gl", extraClass).toTypedArray())
            attr("aria-hidden", "true")
        }) { org.jetbrains.compose.web.dom.Img(src = uri, alt = "") }
        return
    }
    Span({
        classes(*listOfNotNull("ms", if (filled) "fill" else null, extraClass).toTypedArray())
        attr("aria-hidden", "true")
    }) { Text(name) }
}

fun glyphUri(name: String, muted: Boolean = false): String? =
    runCatching { kotlinx.browser.window.asDynamic().estudarioGlyphUri(name, muted) as? String }.getOrNull()

@Composable
fun Btn(
    label: String,
    onClick: () -> Unit,
    style: String = "primary",
    icon: String? = null,
    small: Boolean = false,
    block: Boolean = false,
    size: String? = null,
    enabled: Boolean = true,
) {
    // O mesmo elemento pode ser reaproveitado com outra ação (ex.: Continuar de um passo para o outro).
    val latestClick = androidx.compose.runtime.rememberUpdatedState(onClick)
    val latestEnabled = androidx.compose.runtime.rememberUpdatedState(enabled)
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("btn", style, if (small) "small" else null, size, if (block) "block" else null).toTypedArray())
        if (!enabled) disabled()
        onClick { if (latestEnabled.value) latestClick.value() }
    }) {
        if (icon != null) Icon(icon)
        Text(label)
    }
}

@Composable
fun IconButton(icon: String, label: String, onClick: () -> Unit) {
    val latestClick = androidx.compose.runtime.rememberUpdatedState(onClick)
    Button({
        type(ButtonType.Button)
        classes("icon-btn")
        attr("aria-label", label)
        attr("title", label)
        onClick { latestClick.value() }
    }) { Icon(icon) }
}

@Composable
fun Card(extra: String? = null, attrs: AttrBuilderContext<HTMLDivElement>? = null, content: @Composable () -> Unit) {
    Div({
        classes(*(listOf("card") + extra.orEmpty().split(' ').filter { it.isNotBlank() }).toTypedArray())
        attrs?.invoke(this)
    }) { content() }
}

@Composable
fun CardHead(title: String, action: (@Composable () -> Unit)? = null) {
    Div({ classes("card-head") }) {
        org.jetbrains.compose.web.dom.H2 { Text(title) }
        action?.invoke()
    }
}

@Composable
fun Chip(text: String, tone: String? = null, icon: String? = null) {
    Span({ classes(*listOfNotNull("chip", tone).toTypedArray()) }) {
        if (icon != null) Icon(icon)
        Text(text)
    }
}

@Composable
fun ProgressBar(fraction: Double, tone: String? = null) {
    Div({ classes(*listOfNotNull("bar", tone).toTypedArray()); attr("role", "progressbar"); attr("aria-valuenow", "${(fraction * 100).toInt()}") }) {
        Span({ style { width((fraction.coerceIn(0.0, 1.0) * 100).percent) } })
    }
}

@Composable
fun PageHead(title: String, subtitle: String? = null, actions: (@Composable () -> Unit)? = null) {
    Div({ classes("page-head") }) {
        Div {
            H1 { Text(title) }
            if (subtitle != null) P({ classes("sub") }) { Text(subtitle) }
        }
        if (actions != null) Div({ classes("row", "wrap") }) { actions() }
    }
}

@Composable
fun Stat(value: String, label: String) {
    Div({ classes("stat") }) {
        Span({ classes("value") }) { Text(value) }
        Span({ classes("label") }) { Text(label) }
    }
}

@Composable
fun Empty(icon: String, title: String, text: String, action: (@Composable () -> Unit)? = null) {
    Div({ classes("empty") }) {
        Icon(icon, extraClass = "big")
        org.jetbrains.compose.web.dom.H3 { Text(title) }
        P { Text(text) }
        action?.invoke()
    }
}

@Composable
fun Spinner() { Div({ classes("spinner") }) }

@Composable
fun Modal(onDismiss: () -> Unit, wide: Boolean = false, content: @Composable () -> Unit) {
    Div({
        classes("modal-back")
        onClick { event -> if (event.target == event.currentTarget) onDismiss() }
    }) {
        Div({ classes(*listOfNotNull("modal", if (wide) "wide" else null).toTypedArray()); attr("role", "dialog"); attr("aria-modal", "true") }) { content() }
    }
}

fun percent(part: Int, total: Int): Int = if (total <= 0) 0 else (part * 100 / total)

/** Contador com menos e mais, como os seletores do app (horas por dia, quantidade de questões...). */
@Composable
fun Stepper(value: Int, onChange: (Int) -> Unit, step: Int = 1, min: Int = 0, max: Int = Int.MAX_VALUE, format: (Int) -> String = { it.toString() }, label: String = "") {
    val latest = androidx.compose.runtime.rememberUpdatedState(onChange)
    val current = androidx.compose.runtime.rememberUpdatedState(value)
    Div({ classes("stepper"); attr("role", "group"); if (label.isNotBlank()) attr("aria-label", label) }) {
        Button({
            type(ButtonType.Button)
            attr("aria-label", "Diminuir")
            if (value <= min) disabled()
            onClick { latest.value((current.value - step).coerceAtLeast(min)) }
        }) { Icon("remove") }
        Span({ classes("value"); attr("aria-live", "polite") }) { Text(format(value)) }
        Button({
            type(ButtonType.Button)
            attr("aria-label", "Aumentar")
            if (value >= max) disabled()
            onClick { latest.value((current.value + step).coerceAtMost(max)) }
        }) { Icon("add") }
    }
}

/** Anel de progresso (Missão de hoje). [fraction] de 0 a 1. */
@Composable
fun Ring(fraction: Double, content: @Composable () -> Unit) {
    val radius = 52.0
    val circumference = 2 * kotlin.math.PI * radius
    Div({ classes("ring") }) {
        Svg(viewBox = "0 0 120 120") {
            Circle(60, 60, radius, { classes("track"); attr("fill", "none"); attr("stroke-width", "11") })
            Circle(60, 60, radius, {
                classes("value"); attr("fill", "none"); attr("stroke-width", "11"); attr("stroke-linecap", "round")
                attr("stroke-dasharray", "$circumference"); attr("stroke-dashoffset", "${circumference * (1 - fraction.coerceIn(0.0, 1.0))}")
            })
        }
        Div({ classes("center") }) { content() }
    }
}

@Composable
fun Xp(amount: Int, green: Boolean = false) {
    Span({ classes(*listOfNotNull("xp", if (green) "green" else null).toTypedArray()) }) {
        Icon("bolt")
        Text("${if (amount > 0) "+" else ""}$amount XP")
    }
}

@Composable
fun Switch(checked: Boolean, label: String, onChange: (Boolean) -> Unit) {
    val latest = androidx.compose.runtime.rememberUpdatedState(onChange)
    val current = androidx.compose.runtime.rememberUpdatedState(checked)
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("switch", if (checked) "on" else null).toTypedArray())
        attr("role", "switch"); attr("aria-checked", checked.toString()); attr("aria-label", label)
        onClick { latest.value(!current.value) }
    }) {}
}

/** Chips de filtro (como os do Treinar no app). */
@Composable
fun <T> FilterChips(options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    val latest = androidx.compose.runtime.rememberUpdatedState(onSelect)
    Div({ classes("filter-chips") }) {
        options.forEach { (key, label) ->
            androidx.compose.runtime.key(key) {
                Button({
                    type(ButtonType.Button)
                    classes(*listOfNotNull("fchip", if (key == selected) "on" else null).toTypedArray())
                    attr("aria-pressed", (key == selected).toString())
                    onClick { latest.value(key) }
                }) {
                    if (key == selected) Icon("check")
                    Text(label)
                }
            }
        }
    }
}

object Toast {
    var message by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set
    private var serial = 0

    fun show(text: String) {
        message = text
        val mine = ++serial
        kotlinx.browser.window.setTimeout({ if (serial == mine) message = null }, 3200)
    }
}

@Composable
fun ToastHost() {
    Toast.message?.let { text ->
        Div({ classes("toast"); attr("role", "status") }) { Icon("check_circle", filled = true); Text(text) }
    }
}
