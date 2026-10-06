package br.com.estudario.ui.setup

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

/** Campo de busca do catálogo de editais. */
@Composable
internal fun CatalogSearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar concurso") },
        placeholder = { Text("Ex.: PMMG, Polícia Federal, PRF") },
        leadingIcon = { Icon(Icons.Outlined.Search, null) },
        trailingIcon = {
            if (query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Outlined.Close, "Limpar busca") }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
    )
}

/** Atalhos com os concursos do catálogo, para quem ainda não sabe o que digitar. */
@Composable
internal fun CatalogShortcuts(entries: List<ExamCatalogEntry>, onPick: (String) -> Unit) {
    val names = entries.groupBy { it.shortName }.entries
        .sortedByDescending { (_, list) -> list.maxOf { it.year ?: 0 } }
        .map { it.key }
        .take(10)
    if (names.isEmpty()) return
    Text("Editais disponíveis", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        names.forEach { name -> FilterChip(selected = false, onClick = { onPick(name) }, label = { Text(name) }) }
    }
}

/**
 * Um edital do resultado da busca: sigla e cargo em destaque, órgão, banca e ano embaixo, e o
 * tamanho do edital com quanto dele já tem matéria pronta.
 */
@Composable
internal fun ExamResultCard(entry: ExamCatalogEntry, selected: Boolean, onClick: () -> Unit) {
    br.com.estudario.ui.brand.BrandChoice(selected, onClick) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(entry.shortName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(entry.role, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 3, overflow = TextOverflow.Ellipsis)
            if (entry.details.isNotBlank()) {
                Text(entry.details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "${entry.subjectCount} matérias · ${entry.topicCount} tópicos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (entry.readyPercent >= 50) ReadyBadge(entry.readyPercent)
            }
        }
        Spacer(Modifier.width(8.dp))
        br.com.estudario.ui.brand.BrandChoiceMark(selected)
    }
}

@Composable
private fun ReadyBadge(percent: Int) {
    Text(
        "$percent% com material pronto",
        Modifier.clip(RoundedCornerShape(50)).background(estudarioColors().completed.copy(alpha = 0.14f)).padding(horizontal = 8.dp, vertical = 2.dp),
        style = MaterialTheme.typography.labelSmall,
        color = estudarioColors().completed,
        fontWeight = FontWeight.SemiBold,
    )
}

/** Resumo do edital escolhido, com o aviso do edital quando houver. */
@Composable
internal fun SelectedExamSummary(entry: ExamCatalogEntry) {
    SetupCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Outlined.Verified, null, Modifier.size(22.dp), tint = estudarioColors().completed)
            Text("Edital oficial pronto", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }
        Text(
            "As matérias e os tópicos vêm do edital publicado, na ordem oficial. Não precisa anexar PDF: você confere tudo no próximo passo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        entry.editalRef?.let { Text(it, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        entry.notice?.let { Notice(Icons.Outlined.Info, it, MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

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
            "Nenhum edital pronto para “${query.trim()}” ainda. Você pode seguir com o PDF logo abaixo.",
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
