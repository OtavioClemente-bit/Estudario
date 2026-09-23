package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Coluna de tela cheia que se comporta bem em qualquer aparelho: quando o conteúdo cabe, fica
 * distribuído/centralizado como no desenho original; quando não cabe (tela baixa, fonte grande,
 * "tamanho da tela" aumentado no Android), ela rola em vez de cortar o começo e o fim.
 *
 * O truque é `heightIn(min = altura disponível)` depois do `verticalScroll`: a coluna nunca é menor
 * que a tela (então `Arrangement.Center` e `Spacer(Modifier.weight(...))` continuam funcionando),
 * mas pode ser maior.
 */
@Composable
fun FitOrScrollColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(contentPadding),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = content,
        )
    }
}
