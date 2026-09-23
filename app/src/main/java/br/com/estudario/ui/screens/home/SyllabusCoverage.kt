package br.com.estudario.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.subjectAccentColor
import br.com.estudario.ui.theme.EstudarioMotion
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.estudarioColors

/**
 * O elemento-assinatura do Estudário: **o edital como uma barra segmentada por matéria**.
 *
 * Não é uma ProgressBar com uma porcentagem em cima. Cada matéria ocupa uma fatia proporcional ao
 * seu tamanho no edital, e dentro de cada fatia a parte cheia é o que já foi estudado. Em uma
 * olhada a pessoa vê três coisas que nenhum número isolado conta: quanto do edital anda, quais
 * matérias estão puxando, e quais nem começaram.
 *
 * Abaixo, a distinção que o produto inteiro defende: **cobertura não é domínio**. Ter passado pelo
 * tópico e ter desempenho nele são medidas diferentes, e a Home mostra as duas lado a lado sem
 * fingir que a primeira vale pela segunda.
 */
@Composable
fun SyllabusCoverage(
    coverage: SyllabusCoverageUi,
    onOpenSyllabus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onOpenSyllabus),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Text(
            "Cobertura do edital",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
            Text(
                "${coverage.percent}%",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "${coverage.studiedTopics} de ${coverage.totalTopics} tópicos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        SegmentedCoverageBar(coverage.subjects)

        if (coverage.subjects.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
                coverage.subjects.forEach { subject -> SubjectCoverageRow(subject) }
            }
        }

        if (coverage.masteryPercent != null) {
            MasteryNote(coverage.percent, coverage.masteryPercent)
        }
    }
}

/**
 * A barra segmentada. Cada matéria recebe largura proporcional ao número de tópicos que tem, e cada
 * segmento é preenchido pela sua própria cor de matéria, a mesma que identifica a matéria em todo
 * o app, vinda da paleta controlada do design system.
 */
@Composable
internal fun SegmentedCoverageBar(subjects: List<SubjectCoverageUi>) {
    val palette = estudarioColors().subjectPalette
    val track = MaterialTheme.colorScheme.surfaceContainerHighest
    if (subjects.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(50)).background(track))
        return
    }
    val total = subjects.sumOf { it.totalTopics }.coerceAtLeast(1)
    Row(
        Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(RoundedCornerShape(50))
            .background(track),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        subjects.forEach { subject ->
            val weight = (subject.totalTopics.toFloat() / total).coerceAtLeast(0.02f)
            val color = subjectAccentColor(subject.name, palette)
            val fraction by animateFloatAsState(
                subject.fraction,
                EstudarioMotion.progress(),
                label = "coverage-${subject.name}",
            )
            Box(
                Modifier
                    .weight(weight)
                    .height(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.18f)),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color),
                )
            }
        }
    }
}

@Composable
private fun SubjectCoverageRow(subject: SubjectCoverageUi) {
    val palette = estudarioColors().subjectPalette
    val color = subjectAccentColor(subject.name, palette)
    Column(
        Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "${subject.name}: ${subject.percent}% do conteúdo estudado" },
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.hairline),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
        ) {
            Text(
                subject.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                "${subject.percent}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.width(42.dp),
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.18f)),
        ) {
            Box(
                Modifier
                    .fillMaxWidth(subject.fraction)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color),
            )
        }
    }
}

/**
 * A frase que separa cobertura de domínio. Não é um segundo indicador competindo: é a leitura
 * honesta do primeiro. Só aparece quando existe base de questões/revisões suficiente para o domínio
 * significar alguma coisa, caso contrário, mostrar um número seria inventar precisão.
 */
@Composable
private fun MasteryNote(coverage: Int, mastery: Int) {
    val colors = estudarioColors()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = EstudarioSpacing.hairline),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight),
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(50))
                .background(if (mastery >= coverage - 10) colors.completed else colors.attention),
        )
        Spacer(Modifier.width(2.dp))
        Text(
            "Domínio médio de $mastery% no que você já estudou, estudado não é o mesmo que dominado.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
