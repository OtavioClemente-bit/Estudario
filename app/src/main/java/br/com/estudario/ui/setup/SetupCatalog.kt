package br.com.estudario.ui.setup

import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.remember
import br.com.estudario.domain.catalog.ExamCategory
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import br.com.estudario.ui.brand.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.data.ai.SyllabusPreflightKind
import br.com.estudario.data.catalog.ExamCatalogEntry
import br.com.estudario.ui.brand.FilterChip
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface
import br.com.estudario.ui.theme.estudarioColors

/** A área de cada edital (Segurança pública, Tribunais...), calculada uma vez por edital. */
internal fun ExamCatalogEntry.category(): ExamCategory = categoryCache.getOrPut(id) { ExamCategory.of(shortName, agency, role) }

// A área sai de uma expressão de texto: calculada a cada cartão e a cada redesenho, com 4 mil editais,
// travava a tela de escolher o edital. Agora uma vez por edital.
private val categoryCache = java.util.concurrent.ConcurrentHashMap<String, ExamCategory>()

/** Busca sem resultado, ou falha ao carregar o catálogo. */
@Composable
internal fun CatalogEmptyState(query: String, loading: Boolean, error: String?, onRetry: () -> Unit) {
    when {
        loading -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            Text("Buscando editais…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        error != null -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Notice(Icons.Outlined.ErrorOutline, error, MaterialTheme.colorScheme.error)
            TextButton(onClick = onRetry) { Text("Tentar de novo") }
        }
        query.isNotBlank() -> Text(
            "Nenhum edital pronto para “${query.trim()}” ainda. Confira o nome ou toque em “Não achei meu concurso”, no fim da tela.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Caminho de quem não achou o concurso: nome, cargo e o PDF do edital. Explica o anexo separado,
 * que é a causa mais comum de "a IA não achou as matérias".
 */
@Composable
internal fun NotFoundIntro() {
    Text("Não encontrou o seu concurso?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
    Text(
        "Informe o concurso e o cargo e anexe o edital em PDF. O Estudário lê o documento e monta as matérias para você revisar.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun SeparateAnnexHint() {
    Notice(
        Icons.Outlined.Info,
        "Em alguns concursos, a lista de matérias (conteúdo programático) fica num anexo ou num edital complementar, separado do edital de abertura. Se for o caso, anexe esse arquivo.",
        MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Resultado da conferência do PDF, feita no aparelho assim que ele é escolhido. */
@Composable
internal fun EditalCheckCard(check: EditalAttachmentCheck) {
    val attention = estudarioColors().attention
    when (check) {
        EditalAttachmentCheck.None -> Unit
        EditalAttachmentCheck.Checking -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            Text("Conferindo o PDF…", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        EditalAttachmentCheck.Unreadable -> Notice(
            Icons.Outlined.Info,
            "Não consegui ler o texto deste PDF no aparelho (pode ser digitalizado ou protegido). Você pode seguir; a leitura completa acontece na geração.",
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
        is EditalAttachmentCheck.Done -> {
            val result = check.result
            when (result.kind) {
                SyllabusPreflightKind.VALID -> Notice(
                    Icons.Outlined.CheckCircle,
                    buildString {
                        append("Edital reconhecido")
                        result.contentPages?.let { append(". Matérias nas páginas $it") }
                        append('.')
                    },
                    estudarioColors().completed,
                )
                SyllabusPreflightKind.VALID_WITH_WARNING -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    result.warnings.forEach { Notice(Icons.Outlined.WarningAmber, it, attention) }
                }
                SyllabusPreflightKind.NOT_AN_EDITAL -> Notice(
                    Icons.Outlined.ErrorOutline,
                    "Este arquivo não parece ser um edital de concurso. Confira se escolheu o PDF certo.",
                    MaterialTheme.colorScheme.error,
                )
                SyllabusPreflightKind.CONTENT_NOT_FOUND -> Notice(
                    Icons.Outlined.WarningAmber,
                    "Este PDF parece ser do concurso, mas não encontrei a lista de matérias nele. Procure o anexo com o conteúdo programático ou o edital completo.",
                    attention,
                )
                SyllabusPreflightKind.CANNOT_VALIDATE -> Notice(
                    Icons.Outlined.Info,
                    "Não deu para confirmar no aparelho se este PDF tem as matérias. Você pode seguir e conferir o resultado depois.",
                    MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun Notice(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.padding(top = 1.dp)) { Icon(icon, null, Modifier.size(18.dp), tint = color) }
            Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
