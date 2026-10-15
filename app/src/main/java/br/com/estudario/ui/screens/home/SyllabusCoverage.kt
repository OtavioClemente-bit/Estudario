package br.com.estudario.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import br.com.estudario.ui.brand.marker
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
import androidx.compose.runtime.setValue
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
    var showAll by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf(false) }
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Seu edital",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 3.dp).marker(),
            )
            Spacer(Modifier.weight(1f))
            Text(
                "${coverage.subjects.size} matérias · ${coverage.totalTopics} tópicos",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        // Dois números, cada um com o que significa: ter passado pelo tópico não é saber o tópico.
        Row(horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
            CoverageStat("${coverage.percent}%", "estudado", "${coverage.studiedTopics} de ${coverage.totalTopics} tópicos vistos", Modifier.weight(1f))
            coverage.masteryPercent?.let { CoverageStat("$it%", "dominado", "acerto nas questões do que já viu", Modifier.weight(1f)) }
        }

        SegmentedCoverageBar(coverage.subjects)

        if (coverage.subjects.isNotEmpty()) {
            // Só o que pede ação agora: as matérias em andamento e as próximas a começar.
            // Fechado: as que pedem ação agora. Aberto: todas, na ordem do edital.
            val ordered = coverage.subjects.sortedWith(compareBy<SubjectCoverageUi>({ it.fraction >= 1f }, { it.fraction <= 0f }, { -it.fraction }))
            val shown = if (showAll) coverage.subjects.sortedBy { it.position } else ordered.take(4)
            Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                shown.forEach { subject -> SubjectCoverageRow(subject) }
            }
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (coverage.subjects.size > 4) Box(
                    Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { showAll = !showAll }.padding(vertical = 11.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (showAll) "Mostrar menos" else "Ver todas as ${coverage.subjects.size} matérias",
                        style = MaterialTheme.typography.labelLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Box(
                    Modifier.then(if (coverage.subjects.size > 4) Modifier else Modifier.weight(1f)).clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                        .clickable(onClick = onOpenSyllabus).padding(horizontal = 16.dp, vertical = 11.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Abrir o edital", style = MaterialTheme.typography.labelLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                }
            }
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
    val (stateLabel, stateColor) = when {
        subject.fraction >= 1f -> "Concluída" to androidx.compose.ui.graphics.Color(0xFF2FBF8A)
        subject.fraction > 0f -> "Em andamento" to MaterialTheme.colorScheme.primary
        else -> "A começar" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "${subject.name}: ${subject.percent}% do conteúdo estudado" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // O selo da matéria: o desenho do assunto na cor dela (livro, martelo, computador...).
        br.com.estudario.ui.brand.SubjectArtTile(br.com.estudario.domain.catalog.SubjectArt.iconFor(subject.name), color, 40.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    subject.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    "${subject.percent}%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End,
                )
            }
            Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.18f))) {
                Box(Modifier.fillMaxWidth(subject.fraction).height(6.dp).clip(RoundedCornerShape(50)).background(color))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${subject.studiedTopics} de ${subject.totalTopics} tópicos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text(stateLabel, style = MaterialTheme.typography.labelSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = stateColor)
            }
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

@Composable
private fun CoverageStat(value: String, label: String, hint: String, modifier: Modifier = Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Black)
            Text(" $label", style = MaterialTheme.typography.titleSmall, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
        }
        Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
