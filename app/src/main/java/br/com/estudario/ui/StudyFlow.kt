package br.com.estudario.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

/**
 * Uma rodada de estudo em andamento (bateria de questões, revisão, simulado): as comemorações
 * (sequência, emblema, nível, peça nova) esperam o fim dela, para não interromper o estudo no meio.
 * Quem está numa rodada chama [HoldCelebrations]. O modo foco contando também segura (EstudarioApp).
 */
object StudyFlow {
    var holds by mutableIntStateOf(0)
    val busy: Boolean get() = holds > 0
}

/** Segura as comemorações enquanto este pedaço da tela estiver aberto. */
@Composable
fun HoldCelebrations() {
    DisposableEffect(Unit) { StudyFlow.holds++; onDispose { StudyFlow.holds-- } }
}
