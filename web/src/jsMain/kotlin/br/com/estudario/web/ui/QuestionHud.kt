package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Prefs
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

/**
 * O placar de questões, igual em todas as telas que fazem questões (Treinar, questões do conteúdo,
 * revisão espaçada, missão do caderno de erros e folheada do caderno de estudo): a barra de XP de jogo,
 * uma casa por questão que fica verde (certa) ou vermelha (errada), a atual brilhando, o "3/10" e o XP
 * subindo a cada resposta; ao lado, o relógio e as certas/erradas, que a pessoa pode esconder nos Ajustes.
 * O que vem depois de responder (virada, lembrei, explicação) continua sendo de cada tela.
 */
@Composable
fun QuestionHud(index: Int, total: Int, results: List<Boolean>, startedAt: Long, title: String? = null, exitLabel: String = "Sair", onExit: (() -> Unit)? = null) {
    val showTimer = Prefs.hudTimer
    val showScore = Prefs.hudScore
    var elapsed by remember(startedAt) { mutableStateOf(0L) }
    if (showTimer) LaunchedEffect(startedAt) { while (true) { elapsed = (Date.now().toLong() - startedAt) / 1000; kotlinx.coroutines.delay(1000) } }
    val right = results.count { it }
    val wrong = results.size - right
    val shown = (index + 1).coerceAtMost(total)
    // XP do jogo: 5 por questão respondida e +5 por certa (a mesma conta do resultado)
    val xp = results.size * 5 + right * 5
    Div({ classes("qhud") }) {
        Div({ classes("qhud-top") }) {
            if (onExit != null) Btn(exitLabel, onExit, style = "ghost", small = true, icon = "close")
            Div({ classes("qhud-title") }) {
                B { Text(if (index >= total) "Pronto" else "Questão $shown de $total") }
                if (title != null) Span { Text(title) }
            }
            Div({ classes("qhud-chips") }) {
                if (showTimer) Span({ classes("qhud-chip", "time"); attr("title", "Tempo desta sessão (conta no Desempenho)") }) {
                    Icon("timer"); Text("${(elapsed / 60).toString().padStart(2, '0')}:${(elapsed % 60).toString().padStart(2, '0')}")
                }
                if (showScore) {
                    // os contadores saltam quando mudam (a chave nova recria o elemento e a animação roda)
                    key("ok-$right") { Span({ classes("qhud-chip", "ok", "q-bump"); attr("title", "Certas") }) { Icon("check"); Text("$right") } }
                    key("no-$wrong") { Span({ classes("qhud-chip", "no", "q-bump"); attr("title", "Erradas") }) { Icon("close"); Text("$wrong") } }
                }
            }
        }
        Div({ classes("qhud-bar"); attr("role", "progressbar"); attr("aria-valuemin", "0"); attr("aria-valuemax", total.toString()); attr("aria-valuenow", results.size.toString()) }) {
            Div({ classes("qhud-track") }) {
                // a parte cheia corre por baixo das casas, com o brilho de barra de XP
                Span({ classes("qhud-fill"); attr("style", "width:${if (total == 0) 0 else results.size * 100 / total}%") })
                Div({ classes("qhud-cells") }) {
                    repeat(total) { i ->
                        val state = when { i < results.size -> if (results[i]) "ok" else "no"; i == index -> "now"; else -> null }
                        I({ classes(*listOfNotNull("qhud-cell", state).toTypedArray()) })
                    }
                }
            }
            Div({ classes("qhud-count") }) {
                B { Text("${results.size}") }; Span { Text("/$total") }
            }
            key("xp-$xp") { Span({ classes("qhud-xp", if (results.isEmpty()) "zero" else "pop") }) { Icon("bolt", filled = true); Text("$xp XP") } }
        }
    }
}
