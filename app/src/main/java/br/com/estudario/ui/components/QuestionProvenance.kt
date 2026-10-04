package br.com.estudario.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.Icons
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionSourceType

/**
 * De onde a questão vem, num selo só. Questão inédita do Estudário é apresentada pelo que é (feita
 * no estilo da banca, conferida com as fontes da explicação), sem aviso de desconfiança no topo:
 * as fontes ficam na explicação, que é onde a pessoa vai procurá-las.
 */
@Composable
fun QuestionProvenance(question: QuestionEntity, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val board = question.board?.takeIf { it.isNotBlank() }
    val details = listOfNotNull(board, question.agency, question.year?.toString()).distinct().joinToString(" · ")
    val (glyph, label) = when (question.questionSourceType) {
        QuestionSourceType.REAL -> br.com.estudario.ui.brand.Glyph.Shield to ("Prova real" + if (details.isBlank()) "" else " · $details")
        QuestionSourceType.REAL_ADAPTED -> br.com.estudario.ui.brand.Glyph.Shield to ("Adaptada de prova real" + if (details.isBlank()) "" else " · $details")
        QuestionSourceType.AUTHORIAL -> br.com.estudario.ui.brand.Glyph.Pencil to ("Inédita do Estudário" + if (board == null) "" else " · no estilo $board")
    }
    val sourceUrl = question.sourceUrl?.takeIf(::isSafeWebUrl)
    androidx.compose.foundation.layout.Row(modifier, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        br.com.estudario.ui.brand.BrandIcon(glyph, size = 18.dp)
        androidx.compose.foundation.layout.Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f, fill = false))
        if (sourceUrl != null) TextButton(onClick = { uriHandler.openUri(sourceUrl) }) {
            Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
            androidx.compose.foundation.layout.Spacer(Modifier.width(4.dp))
            Text("Fonte")
        }
    }
}
