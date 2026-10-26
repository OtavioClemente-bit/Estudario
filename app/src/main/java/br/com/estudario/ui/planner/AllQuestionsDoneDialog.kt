package br.com.estudario.ui.planner

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import br.com.estudario.data.local.planner.PlanTaskEntity

/**
 * A bateria do plano não tem o que mostrar sem repetir acerto: tudo do tópico já foi feito e certo, e
 * não há erro para refazer na matéria. Em vez de refazer as mesmas, o Folha avisa e a pessoa pula a
 * tarefa (o plano se reorganiza).
 */
@Composable
fun AllQuestionsDoneDialog(task: PlanTaskEntity, onSkip: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Você já fez todas") },
        text = {
            Text("Você já respondeu e acertou todas as questões de ${task.topicNameSnapshot ?: task.subjectNameSnapshot}, e não há erro para refazer nesta matéria. Repetir as mesmas não treina nada: pode pular esta tarefa que o plano se reorganiza.")
        },
        confirmButton = { TextButton(onClick = onSkip) { Text("Pular tarefa") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Agora não") } },
    )
}
