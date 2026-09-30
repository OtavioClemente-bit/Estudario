package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Deslizar para o lado (como no e-mail) para tirar um item da tela. O fundo mostra um "X" do lado
 * para onde o item vai sumindo. Quem chama cuida de esconder de verdade e de oferecer "Desfazer".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToHide(onHide: () -> Unit, modifier: Modifier = Modifier, label: String = "Ocultar", content: @Composable () -> Unit) {
    val hide by rememberUpdatedState(onHide)
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) hide()
            // Volta ao lugar: se o item continuar na tela (ex.: desfazer), ele reaparece inteiro.
            false
        },
        positionalThreshold = { total -> total * 0.35f },
    )
    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        backgroundContent = {
            val toLeft = state.dismissDirection == SwipeToDismissBoxValue.EndToStart
            Box(
                Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.errorContainer).padding(horizontal = 20.dp),
                contentAlignment = if (toLeft) Alignment.CenterEnd else Alignment.CenterStart,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Close, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(Modifier.width(6.dp))
                    Text(label, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.labelLarge)
                }
            }
        },
        content = { content() },
    )
}

/** Mostra "Questão ocultada · Desfazer" e chama [onUndo] se a pessoa tocar em desfazer. */
suspend fun SnackbarHostState.offerUndo(message: String, onUndo: () -> Unit) {
    currentSnackbarData?.dismiss()
    if (showSnackbar(message, actionLabel = "Desfazer", withDismissAction = true, duration = SnackbarDuration.Long) == SnackbarResult.ActionPerformed) onUndo()
}
