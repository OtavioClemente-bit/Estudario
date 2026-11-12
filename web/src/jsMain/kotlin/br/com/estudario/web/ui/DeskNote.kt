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
    // Todo dia um post-it diferente: a dica, a cor do papel, a fita, os adesivos da turma em volta e a
    // carinha do Folha mudam juntos (antes só a frase mudava e o adesivo era sempre o mesmo).
    val day = remember { (kotlin.js.Date.now() / 86_400_000).toLong() }
    val tip = remember { DESK_TIPS[(day % DESK_TIPS.size).toInt()] }
    val look = remember { DESK_LOOKS[((day * 7 + 3) % DESK_LOOKS.size).toInt()] }
    Div({ classes(*listOfNotNull("desk-note", "dl-${look.key}", if (falling) "falling" else null).toTypedArray()); attr("aria-hidden", "true") }) {
        Div({ classes("desk-postit"); attr("title", "Descolar"); onClick { falling = true } }) {
            Span({ classes("desk-tape") })
            B { Text(look.kicker) }
            Div({ classes("desk-tip") }) { Text(tip) }
            Div({ classes("desk-sign") }) { Folha(look.mood, 34, live = false); Span { Text("Folha") } }
        }
        Span({ classes("desk-pencil", "tu-hover") }) { Icon(look.left) }
        Span({ classes("desk-bulb", "tu-hover") }) { Icon(look.right) }
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

/** O visual do post-it do dia: cor do papel (classe dl-*), título, humor do Folha e os dois adesivos. */
private class DeskLook(val key: String, val kicker: String, val mood: String, val left: String, val right: String)

private val DESK_LOOKS = listOf(
    DeskLook("sun", "dica do Folha", "wink", "edit_note", "lightbulb"),
    DeskLook("mint", "lembrete do dia", "happy", "check_circle", "local_fire_department"),
    DeskLook("pink", "recado do Folha", "proud", "format_ink_highlighter", "favorite"),
    DeskLook("sky", "papo de prova", "thinking", "push_pin", "gps_fixed"),
    DeskLook("lilac", "bizu do dia", "wink", "style", "star"),
    DeskLook("peach", "anota aí", "happy", "bookmark", "coffee"),
    DeskLook("lime", "dica do Folha", "proud", "emoji_events", "bolt"),
)

/** Só na memória: um F5 traz o post-it de volta. */
private object DeskNoteState { var gone by mutableStateOf(false) }
