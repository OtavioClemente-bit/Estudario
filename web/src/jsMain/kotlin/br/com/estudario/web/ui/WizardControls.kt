package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.SubjectTuning
import br.com.estudario.web.data.Queries
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/*
 * Os controles da montagem do plano, iguais aos do app (ui/setup/SetupPlannerSteps.kt e
 * RoutineControls.kt): barras arredondadas que enchem com a cor do eixo, o valor numa pílula ao lado
 * e nada que role para o lado ou corte em tela estreita. Estilos em styles.css (.lvl, .wk, .blk).
 */

/** Cores dos eixos, as mesmas do app. */
internal object AxisColor {
    const val PRIORITY = "#5B4BF5"
    const val EFFORT = "#FF7A45"
    const val KNOWLEDGE = "#1FB574"
    const val WEEK = "#5B4BF5"
    const val WEEKEND = "#9563FF"
    const val BLOCK = "#FF7A45"
}

/**
 * Medidor de níveis, igual ao do app (SetupPlannerSteps.LevelMeter): barrinhas crescentes na largura
 * toda, cada uma mais forte que a anterior (clarinha no nível 1, cheia no último, pela mesma
 * [SubjectTuning.intensity]); o nível escolhido numa pílula na cor do nível; um pulinho na barrinha
 * clicada; e o "?" que abre ali mesmo a explicação de uma ou duas frases.
 */
@Composable
internal fun LevelMeter(title: String, labels: List<String>, selected: Int, color: String, help: String? = null, onSelect: (Int) -> Unit) {
    val latest = rememberUpdatedState(onSelect)
    var helpOpen by remember(title) { mutableStateOf(false) }
    // Troca a cada clique para reiniciar a animação do pulinho (classe alterna entre duas iguais).
    var bounce by remember { mutableStateOf(-1 to false) }
    fun tone(i: Int) = "color-mix(in srgb, $color ${(SubjectTuning.intensity(i, labels.size) * 100).toInt()}%, var(--card))"
    val strong = selected >= labels.size - 2
    Div({ classes("lvl"); attr("style", "--c:$color") }) {
        Div({ classes("lvl-head") }) {
            Span({ classes("lvl-title") }) { Text(title) }
            if (help != null) Button({
                classes("lvl-help"); attr("type", "button"); attr("aria-expanded", "$helpOpen")
                attr("aria-label", if (helpOpen) "Fechar explicação de $title" else "O que é $title?")
                onClick { helpOpen = !helpOpen }
            }) { Text("?") }
        }
        Span({ classes("pill-v", "lvl-pill"); attr("style", "background:${tone(selected)};color:${if (strong) "#fff" else color}"); attr("aria-live", "polite") }) { Text(labels[selected]) }
        if (helpOpen && help != null) Div({ classes("lvl-help-text") }) { Text(help) }
        Div({ classes("lvl-bars"); attr("role", "radiogroup"); attr("aria-label", title) }) {
            labels.forEachIndexed { i, label ->
                key(i) {
                    Button({
                        classes(*listOfNotNull(
                            if (i <= selected) "on" else null, if (i == selected) "cur" else null,
                            if (bounce.first == i) (if (bounce.second) "bnc-a" else "bnc-b") else null,
                        ).toTypedArray())
                        attr("type", "button")
                        attr("style", "height:${14 + i * (20 / (labels.size - 1).coerceAtLeast(1))}px;--t:${tone(i)}")
                        attr("role", "radio"); attr("aria-checked", (i == selected).toString()); attr("aria-label", "$title: $label"); attr("title", label)
                        onClick { bounce = i to !bounce.second; latest.value(i) }
                        // Setas do teclado trocam o nível, como num grupo de rádio.
                        onKeyDown { e ->
                            val next = when (e.key) { "ArrowRight", "ArrowUp" -> selected + 1; "ArrowLeft", "ArrowDown" -> selected - 1; else -> null }
                            if (next != null && next in labels.indices) { e.preventDefault(); bounce = next to !bounce.second; latest.value(next) }
                        }
                    })
                }
            }
        }
        Div({ classes("lvl-ends") }) { Span { Text(labels.first()) }; Span { Text(labels.last()) } }
    }
}

/** O que cada medidor quer dizer, uma vez só no topo (a mesma escala do clarinho ao forte). */
@Composable
internal fun LevelLegend() {
    Div({ classes("card", "lvl-legend") }) {
        B { Text("Como ler") }
        listOf(
            AxisColor.PRIORITY to "Peso na prova: quanto espaço a matéria ocupa no plano.",
            AxisColor.EFFORT to "Esforço: o quanto ela costuma dar trabalho para você.",
            AxisColor.KNOWLEDGE to "O quanto já sabe: matérias que você domina vão mais para revisão e questões.",
        ).forEach { (c, t) ->
            Div({ classes("lvl-legend-row") }) {
                Span({ classes("sw3") }) {
                    listOf(0, 2, 4).forEach { i -> Span({ attr("style", "height:${6 + i * 1.5}px;background:color-mix(in srgb, $c ${(SubjectTuning.intensity(i, 5) * 100).toInt()}%, var(--card))") }) }
                }
                Span { Text(t) }
            }
        }
        Span({ classes("xs", "muted") }) { Text("Clique no \"?\" de cada medidor para ver o que ele muda no seu plano.") }
    }
}

private val dayShort = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")

/**
 * Horas de cada dia: barra que enche (clique na barra marca direto ali), pílula com o valor e − / +
 * de 15 em 15 minutos; atalhos em células de largura igual. Zero é folga.
 */
@Composable
internal fun RoutineWeek(minutes: List<Int>, onChange: (List<Int>) -> Unit, maxMinutes: Int = 720) {
    val current = rememberUpdatedState(minutes)
    val latest = rememberUpdatedState(onChange)
    fun set(index: Int, value: Int) = latest.value(current.value.toMutableList().also { it[index] = value.coerceIn(0, maxMinutes) })
    val visualMax = 480.0
    Div({ classes("wk") }) {
        Span({ classes("xs", "muted") }) { Text("Atalho: o mesmo tempo de segunda a sexta") }
        Div({ classes("cells") }) {
            listOf(60, 120, 180, 240).forEach { m ->
                val on = (0..4).all { current.value[it] == m }
                Button({
                    classes(*listOfNotNull("cell", if (on) "on" else null).toTypedArray()); attr("style", "--c:${AxisColor.WEEK}")
                    onClick { latest.value(current.value.mapIndexed { i, v -> if (i <= 4) m else v }) }
                }) { Text("${m / 60}h") }
            }
        }
        minutes.forEachIndexed { day, value ->
            key(day) {
                val color = if (day >= 5) AxisColor.WEEKEND else AxisColor.WEEK
                Div({ classes(*listOfNotNull("wk-row", if (value == 0) "off" else null).toTypedArray()); attr("style", "--c:$color") }) {
                    B({ classes("wk-day") }) { Text(dayShort[day]) }
                    Div({
                        classes("wk-track"); attr("role", "slider"); attr("aria-label", "Tempo de ${Queries.weekdayLong[day]}")
                        attr("aria-valuenow", value.toString()); attr("aria-valuemin", "0"); attr("aria-valuemax", maxMinutes.toString())
                        onClick { e ->
                            val ev = e.nativeEvent.asDynamic()
                            val rect = ev.currentTarget.getBoundingClientRect()
                            val frac = (((ev.clientX as Number).toDouble() - (rect.left as Number).toDouble()) / (rect.width as Number).toDouble()).coerceIn(0.0, 1.0)
                            set(day, ((frac * visualMax / 15.0).toInt()) * 15)
                        }
                    }) { Div({ classes("wk-fill"); attr("style", "width:${(value / visualMax * 100).coerceIn(0.0, 100.0)}%") }) }
                    Span({ classes("pill-v", "wk-v") }) { Text(if (value == 0) "Folga" else Queries.minutesLabel(value)) }
                    Button({ classes("nb-ic", "step-btn"); attr("aria-label", "Menos 15 minutos"); if (value <= 0) attr("disabled", ""); onClick { set(day, value - 15) } }) { Text("−") }
                    Button({ classes("nb-ic", "step-btn"); attr("aria-label", "Mais 15 minutos"); if (value >= maxMinutes) attr("disabled", ""); onClick { set(day, value + 15) } }) { Text("+") }
                }
            }
        }
        Div({ classes("wk-total") }) {
            Span({ classes("muted") }) { Text("Total na semana") }
            Span({ classes("pill-v"); attr("style", "--c:${AxisColor.WEEK}") }) { Text(Queries.minutesLabel(minutes.sum())) }
        }
    }
}

/** Tamanho de cada bloco: cinco tamanhos comuns em barrinhas crescentes e − / + de 5 em 5. */
@Composable
internal fun BlockSizePicker(value: Int, onChange: (Int) -> Unit, range: IntRange = 15..120) {
    val latest = rememberUpdatedState(onChange)
    val presets = listOf(25, 45, 50, 60, 90)
    Div({ classes("blk"); attr("style", "--c:${AxisColor.BLOCK}") }) {
        Div({ classes("blk-head") }) {
            Span({ classes("muted", "grow") }) { Text("Cada tarefa do plano dura") }
            Button({ classes("nb-ic", "step-btn"); attr("aria-label", "Menos 5 minutos"); if (value <= range.first) attr("disabled", ""); onClick { latest.value((value - 5).coerceAtLeast(range.first)) } }) { Text("−") }
            Span({ classes("pill-v") }) { Text("$value min") }
            Button({ classes("nb-ic", "step-btn"); attr("aria-label", "Mais 5 minutos"); if (value >= range.last) attr("disabled", ""); onClick { latest.value((value + 5).coerceAtMost(range.last)) } }) { Text("+") }
        }
        Div({ classes("lvl-bars", "blk-bars") }) {
            presets.forEachIndexed { i, m ->
                key(m) {
                    Button({
                        classes(*listOfNotNull(if (m <= value) "on" else null, if (m == value) "cur" else null).toTypedArray())
                        attr("style", "height:${22 + i * 5}px"); attr("aria-label", "Bloco de $m minutos")
                        onClick { latest.value(m) }
                    }) { Text("$m") }
                }
            }
        }
        Span({ classes("xs", "muted") }) { Text("Num dia de 2h com blocos de $value min, cabem ${(120 / value.coerceAtLeast(1)).coerceAtLeast(1)} tarefa(s). É o tamanho-base, não o total do dia.") }
    }
}
