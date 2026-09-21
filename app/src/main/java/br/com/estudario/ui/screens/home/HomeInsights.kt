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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.planner.forecastDateLabelPtBr
import br.com.estudario.ui.theme.EstudarioMotion
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.estudarioColors

/**
 * A previsão de conclusão — a informação que só um app com plano de estudos consegue dar.
 *
 * A data vem do planejador; a leitura do ritmo vem de `StudyPaceEvaluator`, no domínio. Aqui só se
 * decide como falar: uma frase curta, sem alarme e sem culpa, e um caminho para ajustar o plano
 * quando o ritmo não fecha. "239 tópicos restantes" em vermelho não ajuda ninguém a estudar.
 */
private data class PaceCopy(val marker: Color, val title: String, val detail: String)

@Composable
fun PaceForecast(pace: PaceUi, onOpenPlan: () -> Unit, modifier: Modifier = Modifier) {
    val colors = estudarioColors()
    val copy = when (pace) {
        is PaceUi.Comfortable -> PaceCopy(
            colors.completed,
            "Bom ritmo.",
            "Nesse ritmo, o edital fecha ${pace.daysBeforeExam} dias antes da prova — em ${forecastDateLabelPtBr(pace.forecast)}.",
        )
        is PaceUi.Tight -> PaceCopy(
            colors.attention,
            "Ritmo apertado.",
            "A previsão de cobertura é ${forecastDateLabelPtBr(pace.forecast)}, perto demais da prova para sobrar tempo de revisão.",
        )
        is PaceUi.Behind -> PaceCopy(
            MaterialTheme.colorScheme.error,
            "O ritmo atual não fecha o edital.",
            "Sobra conteúdo para ${pace.daysAfterExam} dias depois da prova. Ajustar as horas da semana ou as prioridades no plano muda essa conta.",
        )
        is PaceUi.NoExamDate -> PaceCopy(
            MaterialTheme.colorScheme.primary,
            "Edital coberto até ${forecastDateLabelPtBr(pace.forecast)}.",
            "Cadastre a data da prova no plano para ver quanto tempo sobra para revisão.",
        )
        PaceUi.Complete -> PaceCopy(
            colors.completed,
            "Conteúdo previsto coberto.",
            "Daqui para frente o plano vira revisão e questões.",
        )
        PaceUi.Unknown -> PaceCopy(
            colors.upcoming,
            "Sem previsão ainda.",
            "Informe quantas horas por semana você tem, no plano, e o Estudário calcula quando o edital fecha.",
        )
    }

    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onOpenPlan),
        horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(50))
                .background(copy.marker),
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Previsão",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(copy.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(copy.detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Desempenho e constância lado a lado: o que as questões dizem, e o que a rotina diz. Dois números
 * escolhidos — não um painel de indicadores.
 */
@Composable
fun PerformanceAndStanding(
    performance: PerformanceUi?,
    standing: StandingUi,
    onOpenStatistics: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.large)) {
        Column(
            Modifier
                .weight(1f)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onOpenStatistics),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("Desempenho", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (performance == null) {
                Text(
                    "Ainda sem histórico de questões.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = onOpenStatistics) {
                    Text("Resolver questões →", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Text(
                    "${performance.recentAccuracy}%",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "de acerto nas últimas ${performance.answeredRecently} questões",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                performance.delta?.let { delta -> AccuracyDelta(delta) }
            }
        }

        Column(
            Modifier
                .weight(1f)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onOpenProfile),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("Constância", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (standing.streakDays == 0) {
                Text(
                    "Comece hoje.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Estude para formar sua sequência.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    "${standing.streakDays} ${if (standing.streakDays == 1) "dia" else "dias"}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "em sequência",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (standing.bestStreakDays > standing.streakDays) {
                    Text(
                        "melhor: ${standing.bestStreakDays} dias",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun AccuracyDelta(delta: Int) {
    val colors = estudarioColors()
    val (cor, texto) = when {
        delta > 0 -> colors.completed to "↑ $delta pontos nas últimas semanas"
        delta < 0 -> colors.attention to "↓ ${-delta} pontos nas últimas semanas"
        else -> MaterialTheme.colorScheme.onSurfaceVariant to "estável nas últimas semanas"
    }
    Text(texto, style = MaterialTheme.typography.labelSmall, color = cor)
}

/**
 * O nível do Estudário: uma linha, no fim da tela, com a barra do próximo nível. Discreto de
 * propósito — é evolução dentro do app, não um troféu disputando espaço com o edital.
 */
@Composable
fun LevelRow(standing: StandingUi, onOpenProfile: () -> Unit, modifier: Modifier = Modifier) {
    val fraction by animateFloatAsState(
        standing.levelFraction.coerceIn(0f, 1f),
        EstudarioMotion.progress(),
        label = "level-progress",
    )
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onOpenProfile),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Nível ${standing.level}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.width(EstudarioSpacing.tight))
            Text(
                standing.levelTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.weight(1f))
            Text(
                "${standing.xpIntoLevel} / ${standing.xpForNextLevel} XP",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

/** Divisor da Home: fino, curto e sem peso — separa bandas de informação sem virar moldura. */
@Composable
fun HomeDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(vertical = EstudarioSpacing.hairline)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
    )
}
