package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * O canto da mesa (telas largas): no lugar de rabiscos genéricos, um post-it do Folha colado no
 * caderno, com fita, a "dica do Folha" escrita à mão e a assinatura dele; ao lado, o lápis e o
 * marca-texto da turma. Muda de dica todo dia. Fica atrás do conteúdo e não pega clique.
 */
@Composable
fun DeskNote() {
    // Clicou no post-it: ele descola, cai e some de todas as telas até a página ser recarregada.
    if (DeskNoteState.gone) return
    var falling by remember { mutableStateOf(false) }
    LaunchedEffect(falling) { if (falling) { kotlinx.coroutines.delay(1100); DeskNoteState.gone = true } }
    val tip = remember { DESK_TIPS[((kotlin.js.Date.now() / 86_400_000).toLong() % DESK_TIPS.size).toInt()] }
    Div({ classes(*listOfNotNull("desk-note", if (falling) "falling" else null).toTypedArray()); attr("aria-hidden", "true") }) {
        Div({ classes("desk-postit"); attr("title", "Descolar"); onClick { falling = true } }) {
            Span({ classes("desk-tape") })
            B { Text("dica do Folha") }
            Div({ classes("desk-tip") }) { Text(tip) }
            Div({ classes("desk-sign") }) { Folha("wink", 34, live = false); Span { Text("Folha") } }
        }
        Span({ classes("desk-pencil") }) { Icon("Pencil") }
        Span({ classes("desk-bulb") }) { Icon("Bulb") }
    }
}

private val DESK_TIPS = listOf(
    "Revisar no dia certo vale mais que reler tudo.",
    "Errou? Ótimo: erro anotado vira ponto na prova.",
    "Leia o enunciado até o fim. A banca esconde o \"exceto\".",
    "Grife pouco. Grifo demais é livro pintado.",
    "Questão comentada ensina mais que teoria repetida.",
    "Um tópico por vez. É assim que o edital acaba.",
    "Dormir bem também é estudar.",
    "Melhor 40 minutos todo dia que 6 horas no domingo.",
    "Explique em voz alta: se travou, é ali que falta.",
    "Simulado é treino de prova, não de ansiedade.",
)

/** Só na memória: um F5 traz o post-it de volta. */
private object DeskNoteState { var gone by mutableStateOf(false) }
