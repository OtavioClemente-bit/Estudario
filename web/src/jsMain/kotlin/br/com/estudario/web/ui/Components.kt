package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
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

/** Ícone do Material Symbols (a mesma família de ícones do app). */
@Composable
fun Icon(name: String, filled: Boolean = false, extraClass: String? = null) {
    Span({
        classes(*listOfNotNull("ms", if (filled) "fill" else null, extraClass).toTypedArray())
        attr("aria-hidden", "true")
    }) { Text(name) }
}

@Composable
fun Btn(
    label: String,
    onClick: () -> Unit,
    style: String = "primary",
    icon: String? = null,
    small: Boolean = false,
    block: Boolean = false,
    enabled: Boolean = true,
) {
    Button({
        type(ButtonType.Button)
        classes(*listOfNotNull("btn", style, if (small) "small" else null, if (block) "block" else null).toTypedArray())
        if (!enabled) disabled()
        onClick { if (enabled) onClick() }
    }) {
        if (icon != null) Icon(icon)
        Text(label)
    }
}

@Composable
fun IconButton(icon: String, label: String, onClick: () -> Unit) {
    Button({
        type(ButtonType.Button)
        classes("icon-btn")
        attr("aria-label", label)
        attr("title", label)
        onClick { onClick() }
    }) { Icon(icon) }
}

@Composable
fun Card(extra: String? = null, attrs: AttrBuilderContext<HTMLDivElement>? = null, content: @Composable () -> Unit) {
    Div({
        classes(*listOfNotNull("card", extra).toTypedArray())
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
fun Modal(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Div({
        classes("modal-back")
        onClick { event -> if (event.target == event.currentTarget) onDismiss() }
    }) {
        Div({ classes("modal"); attr("role", "dialog"); attr("aria-modal", "true") }) { content() }
    }
}

fun percent(part: Int, total: Int): Int = if (total <= 0) 0 else (part * 100 / total)
