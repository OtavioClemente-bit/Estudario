package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * As peças do "estúdio" (gx-*): a mesma cara de console escuro do Perfil de jogo, usada ao gerar
 * material e mais questões, ao montar o treino e nos ajustes da revisão. Tudo que se toca afunda e
 * volta com mola, a escolha acende na cor dela e o número grande tem − e + e uma régua para arrastar.
 */
data class GxOption<T>(val key: T, val title: String, val hint: String = "", val icon: String? = null, val color: String = "#A78BFF")

/** Uma parte do pedido: ícone na cor da parte, título, dica e o conteúdo. */
@Composable
fun GxSection(icon: String, title: String, hint: String? = null, color: String = "#A78BFF", aside: String? = null, content: @Composable () -> Unit) {
    Div({ classes("gx-sec"); attr("style", "--c:$color") }) {
        Div({ classes("gx-sec-head") }) {
            Span({ classes("gx-sec-ic") }) { Icon(icon, filled = true) }
            Div({ classes("gx-sec-txt") }) {
                B { Text(title) }
                hint?.let { Span { Text(it) } }
            }
            aside?.let { Span({ classes("gx-sec-aside") }) { Text(it) } }
        }
        content()
    }
}

/** Ladrilhos grandes: cada um com a sua cor; [multi] marca vários (com ✓), senão escolhe um. */
@Composable
fun <T> GxTiles(options: List<GxOption<T>>, isOn: (T) -> Boolean, cols: Int = 3, multi: Boolean = true, onToggle: (T) -> Unit) {
    val latest = rememberUpdatedState(onToggle)
    Div({ classes("gx-tiles", "cols-$cols") }) {
        options.forEach { option ->
            key(option.key) {
                val on = isOn(option.key)
                Button({
                    classes(*listOfNotNull("gx-tile", if (on) "on" else null, if (multi) "multi" else null).toTypedArray())
                    attr("style", "--c:${option.color}"); attr("aria-pressed", "$on"); attr("type", "button")
                    onClick { latest.value(option.key) }
                }) {
                    option.icon?.let { Span({ classes("gx-tile-ic") }) { Icon(it, filled = true) } }
                    Span({ classes("gx-tile-txt") }) {
                        B { Text(option.title) }
                        if (option.hint.isNotBlank()) Span { Text(option.hint) }
                    }
                    Span({ classes("gx-tile-tick") }) { Icon("check", plain = true) }
                }
            }
        }
    }
}

/** Escolha única em pílulas largas (com dica embaixo do título). */
@Composable
fun <T> GxSeg(options: List<GxOption<T>>, selected: T, onPick: (T) -> Unit, extra: @Composable (GxOption<T>) -> Unit = {}) {
    val latest = rememberUpdatedState(onPick)
    Div({ classes("gx-seg"); attr("style", "--n:${options.size}") }) {
        options.forEach { option ->
            key(option.key) {
                val on = option.key == selected
                Button({
                    classes(*listOfNotNull("gx-seg-btn", if (on) "on" else null).toTypedArray())
                    attr("style", "--c:${option.color}"); attr("aria-pressed", "$on"); attr("type", "button")
                    onClick { latest.value(option.key) }
                }) {
                    extra(option)
                    B { Text(option.title) }
                    if (option.hint.isNotBlank()) Span { Text(option.hint) }
                }
            }
        }
    }
}

/** Chips de filtro do estúdio (rolam para o lado no celular). */
@Composable
fun <T> GxChips(options: List<Pair<T, String>>, selected: T, onPick: (T) -> Unit) {
    val latest = rememberUpdatedState(onPick)
    Div({ classes("gx-chips") }) {
        options.forEach { (k, label) ->
            key(k) {
                val on = k == selected
                Button({
                    classes(*listOfNotNull("gx-chip", if (on) "on" else null).toTypedArray()); attr("type", "button"); attr("aria-pressed", "$on")
                    onClick { latest.value(k) }
                }) {
                    if (on) Icon("check", plain = true)
                    Span { Text(label) }
                }
            }
        }
    }
}

/** O número grande: − e +, a régua para arrastar e os atalhos. [mood] diz o que o número significa. */
@Composable
fun GxCounter(
    value: Int,
    onChange: (Int) -> Unit,
    min: Int,
    max: Int,
    step: Int = 1,
    unit: String = "",
    chips: List<Int> = emptyList(),
    mood: (Int) -> String? = { null },
    label: String = "",
) {
    val latest = rememberUpdatedState(onChange)
    val current = rememberUpdatedState(value)
    val span = (max - min).coerceAtLeast(1)
    val pct = ((value - min) * 100 / span).coerceIn(0, 100)
    Div({ classes("gx-counter"); attr("role", "group"); if (label.isNotBlank()) attr("aria-label", label) }) {
        Div({ classes("gx-counter-row") }) {
            Button({
                classes("gx-round"); attr("type", "button"); attr("aria-label", "Diminuir")
                if (value <= min) disabled()
                onClick { latest.value((current.value - step).coerceAtLeast(min)) }
            }) { Icon("remove", plain = true) }
            key(value) { Div({ classes("gx-big"); attr("aria-live", "polite") }) { B { Text("$value") }; if (unit.isNotBlank()) Span { Text(unit) } } }
            Button({
                classes("gx-round"); attr("type", "button"); attr("aria-label", "Aumentar")
                if (value >= max) disabled()
                onClick { latest.value((current.value + step).coerceAtMost(max)) }
            }) { Icon("add", plain = true) }
        }
        if (max > min) Input(InputType.Range) {
            classes("gx-range")
            attr("min", "$min"); attr("max", "$max"); attr("step", "$step")
            attr("style", "--gxp:$pct%")
            attr("aria-label", label.ifBlank { "Quantidade" })
            value(value)
            onInput { e -> (e.value?.toInt())?.let { latest.value(it.coerceIn(min, max)) } }
        }
        mood(value)?.let { Div({ classes("gx-mood") }) { Text(it) } }
        val quick = chips.filter { it in min..max }
        if (quick.isNotEmpty()) Div({ classes("gx-quick") }) {
            quick.forEach { n ->
                Button({ classes(*listOfNotNull("gx-qchip", if (n == value) "on" else null).toTypedArray()); attr("type", "button"); onClick { latest.value(n) } }) { Text("$n") }
            }
        }
    }
}

/** Uma linha de liga e desliga. */
@Composable
fun GxSwitchRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val latest = rememberUpdatedState(onChange)
    val current = rememberUpdatedState(checked)
    Button({
        classes(*listOfNotNull("gx-switch-row", if (checked) "on" else null).toTypedArray()); attr("type", "button")
        attr("role", "switch"); attr("aria-checked", "$checked")
        onClick { latest.value(!current.value) }
    }) {
        Span({ classes("gx-switch-txt") }) { B { Text(title) }; Span { Text(subtitle) } }
        Span({ classes("gx-switch") }) { I {} }
    }
}

/** O botão de lançar: grande, com brilho correndo. */
@Composable
fun GxLaunch(label: String, icon: String = "auto_awesome", enabled: Boolean = true, onClick: () -> Unit) {
    val latest = rememberUpdatedState(onClick)
    Button({
        classes("gx-launch"); attr("type", "button")
        if (!enabled) disabled()
        onClick { latest.value() }
    }) {
        Icon(icon, filled = true, plain = true)
        Span { Text(label) }
        I({ classes("gx-launch-shine") })
    }
}
