package br.com.estudario.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import br.com.estudario.ui.brand.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.ButtonDefaults
import br.com.estudario.ui.brand.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.Text
import br.com.estudario.ui.brand.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.estudario.ui.components.SkeletonBlock
import br.com.estudario.ui.components.SubjectBar
import br.com.estudario.ui.planner.minutesLabelPtBr
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.EstudarioSpacing

/**
 * Nível 1, AGORA. A seção de maior peso visual da Home: um painel só, nunca uma lista de cards.
 * A borda arredondada [EstudarioShapes.spotlight] e o fundo `surfaceContainerHigh` aparecem só
 * aqui na tela, é o que faz esse painel se destacar sem depender de gradiente ou sombra.
 */
@Composable
fun CurrentStudySection(
    state: CurrentStudyUiState,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    practiceAvailable: Boolean = false,
    onPractice: () -> Unit = {},
    onGenerate: (() -> Unit)? = null,
    generateModifier: Modifier = Modifier,
    /** O que o Folha tem de pessoal para dizer (saudade, prova chegando, o que você estudou ontem). */
    memory: br.com.estudario.domain.FolhaMemory.Line? = null,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(EstudarioShapes.spotlight)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(EstudarioSpacing.comfortable),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(140)) },
            label = "current-study",
        ) { current ->
            when (current) {
                is CurrentStudyUiState.Loading -> LoadingBody()
                is CurrentStudyUiState.NoPlan -> NoPlanBody(onPrimaryAction, practiceAvailable, onPractice)
                is CurrentStudyUiState.NoTaskToday -> NoTaskTodayBody(practiceAvailable, onPractice)
                is CurrentStudyUiState.DayComplete -> DayCompleteBody(current)
                is CurrentStudyUiState.Ready -> ReadyBody(current, onPrimaryAction, onGenerate, generateModifier, memory)
            }
        }
    }
}

@Composable
private fun Eyebrow(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun ReadyBody(state: CurrentStudyUiState.Ready, onStart: () -> Unit, onGenerate: (() -> Unit)?, generateModifier: Modifier, memory: br.com.estudario.domain.FolhaMemory.Line? = null) {
    val task = state.task
    Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.medium)) {
        // O Folha conversa com a pessoa: ele à esquerda, o balão saindo dele. Tocar nele já começa.
        // Uma frase nova cada vez que o Início abre (a semente fica guardada enquanto a tela vive).
        val seed = androidx.compose.runtime.remember { System.currentTimeMillis() / 1000 }
        val fala = memory?.takeIf { onGenerate == null }?.text ?: br.com.estudario.ui.assistant.FolhaLines.forHome(
            continuing = state.progressFraction != null,
            needsMaterial = onGenerate != null,
            hour = java.time.LocalTime.now().hour,
            seed = seed,
        )
        // tocando nele, a frase do toque aparece neste mesmo balão por uns segundos
        var quip by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
        androidx.compose.runtime.LaunchedEffect(quip) { val q = quip ?: return@LaunchedEffect; kotlinx.coroutines.delay(q.length * 45L + 2600L); quip = null }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val rest = when (memory?.takeIf { onGenerate == null }?.mood) { "sad" -> br.com.estudario.ui.assistant.FolhaMood.SAD; "happy" -> br.com.estudario.ui.assistant.FolhaMood.HAPPY; "proud" -> br.com.estudario.ui.assistant.FolhaMood.PROUD; else -> br.com.estudario.ui.assistant.FolhaMood.IDLE }
            br.com.estudario.ui.assistant.FolhaTalking(88.dp, fala, onClick = onGenerate ?: onStart, onQuip = { quip = it }, rest = rest)
            Spacer(Modifier.width(4.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Eyebrow((if (state.progressFraction != null) "Continuando" else "Agora").uppercase())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // A pontinha do balão, virada para o Folha.
                    val bubble = MaterialTheme.colorScheme.primaryContainer
                    androidx.compose.foundation.Canvas(Modifier.size(width = 10.dp, height = 16.dp)) {
                        drawPath(androidx.compose.ui.graphics.Path().apply { moveTo(size.width, 0f); lineTo(0f, size.height / 2); lineTo(size.width, size.height); close() }, bubble)
                    }
                    Text(
                        quip ?: fala,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bubble)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
            SubjectBar(task.subjectName, height = 48.dp)
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    task.subjectName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    task.topicName,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Text(
            if (task.durationLabel.isBlank()) task.activityLabel else "${task.activityLabel} · ${task.durationLabel}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (state.progressFraction != null) {
            val animated by animateFloatAsState(state.progressFraction.coerceIn(0f, 1f), tween(400), label = "task-progress")
            LinearProgressIndicator(
                progress = { animated },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .semantics { contentDescription = "${(animated * 100).toInt()}% concluído" },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            )
        }
        if (onGenerate != null) {
            // Tópico ainda sem teoria nem questões: o primeiro passo é gerar o material dele.
            Text(
                "Este tópico ainda não tem material. Gere a teoria, os flashcards e as questões em um toque.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(kind = br.com.estudario.ui.brand.NoteKind.Study,
                onClick = onGenerate,
                modifier = generateModifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                br.com.estudario.ui.assistant.Folha(26.dp)
                Spacer(Modifier.width(8.dp))
                Text("Gerar o material deste tópico", style = MaterialTheme.typography.labelLarge)
            }
            OutlinedButton(kind = br.com.estudario.ui.brand.NoteKind.Next, onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                Text(task.ctaLabel, style = MaterialTheme.typography.labelLarge)
            }
        } else {
            Button(kind = br.com.estudario.ui.brand.NoteKind.Next,
                onClick = onStart,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text(task.ctaLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun NoPlanBody(onCreatePlan: () -> Unit, practiceAvailable: Boolean, onPractice: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.medium)) {
        Eyebrow("Comece por aqui")
        Text(
            "Vamos montar seu plano",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // O Folha no Início, falando (como no site): aqui ele convida a montar o plano.
        MissionFolha("Você não precisa organizar tudo sozinho. Me diz seu concurso e seu tempo que eu cuido da agenda!", br.com.estudario.ui.assistant.FolhaMood.WAVE, onCreatePlan)
        Button(kind = br.com.estudario.ui.brand.NoteKind.Primary, glyph = br.com.estudario.ui.brand.Glyph.Calendar,
            onClick = onCreatePlan,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text("Criar meu plano", style = MaterialTheme.typography.labelLarge)
        }
        if (practiceAvailable) {
            TextButton(kind = br.com.estudario.ui.brand.NoteKind.Practice, onClick = onPractice, modifier = Modifier.fillMaxWidth()) {
                Text("Ou responda 10 questões enquanto isso", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/** Frases do Folha no dia livre, sorteadas pelo dia (não repete a toda hora). */
private fun freeDayLine(): String {
    val lines = listOf(
        "Dia livre no plano! Descansar também faz parte. Se bater vontade, umas questões mantêm o ritmo.",
        "Hoje é folga. Uma revisão rápida mantém tudo fresco, mas sem pressão!",
        "Plano em dia! Aproveita para recarregar. Amanhã a gente segue de onde parou.",
        "Nada agendado hoje. Que tal 10 questões só para não perder o embalo?",
    )
    return lines[Math.floorMod(java.time.LocalDate.now().toEpochDay().toInt(), lines.size)]
}

@Composable
private fun NoTaskTodayBody(practiceAvailable: Boolean, onPractice: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.medium)) {
        Eyebrow("Hoje")
        Text(
            "Nada agendado para hoje",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        MissionFolha(freeDayLine(), br.com.estudario.ui.assistant.FolhaMood.HAPPY, if (practiceAvailable) onPractice else null)
        if (practiceAvailable) {
            OutlinedButton(kind = br.com.estudario.ui.brand.NoteKind.Practice, onClick = onPractice, modifier = Modifier.fillMaxWidth()) {
                Text("Responder 10 questões", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun DayCompleteBody(state: CurrentStudyUiState.DayComplete) {
    Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
        Eyebrow("Dia concluído")
        Text(
            "Missão cumprida por hoje",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val missoes = state.missionsToday
        MissionFolha(
            "${missoes} missõe${if (missoes == 1) "" else "s"} concluída${if (missoes == 1) "" else "s"} e ${minutesLabelPtBr(state.minutesToday)} de estudo. Tô orgulhoso! Amanhã a gente segue.",
            br.com.estudario.ui.assistant.FolhaMood.PROUD, null,
        )
    }
}

@Composable
private fun LoadingBody() {
    Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.medium)) {
        SkeletonBlock(Modifier.fillMaxWidth(0.3f), height = 14)
        SkeletonBlock(Modifier.fillMaxWidth(0.8f), height = 30)
        SkeletonBlock(Modifier.fillMaxWidth(0.5f), height = 14)
        Spacer(Modifier.height(2.dp))
        SkeletonBlock(Modifier.fillMaxWidth(), height = 44, cornerRadius = 14)
    }
}
