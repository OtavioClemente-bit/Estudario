package br.com.estudario.ui.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.Surface

/** Qual limite estourou: muda o título e o que a janela promete. */
enum class UpgradeReason(val title: String, val body: String, val perks: List<String>) {
    CONTENT(
        "Seus materiais deste mês acabaram",
        "No Essencial são 25 materiais completos por mês, teoria, flashcards, dicas e questões no estilo da sua banca. No Pro, 50.",
        listOf("25 ou 50 materiais por mês", "Lotes de questões extras", "Simulados no estilo da banca", "Seu próprio edital em PDF"),
    ),
    SYLLABUS(
        "Edital pelo seu PDF é dos planos pagos",
        "No Grátis você usa os editais do catálogo. Para mandar o PDF do seu concurso e ter as matérias e tópicos na ordem exata do edital, assine.",
        listOf("Seu edital em PDF, organizado em minutos", "25 ou 50 materiais por mês", "Simulados no estilo da banca", "Lotes de questões extras"),
    ),
    SIMULATION(
        "Seus simulados acabaram",
        "No Essencial são 2 simulados por mês no estilo da sua banca, com nota por matéria. No Pro, 4.",
        listOf("2 ou 4 simulados por mês", "25 ou 50 materiais por mês", "Lotes de questões extras", "Seu próprio edital em PDF"),
    ),
    QUESTIONS(
        "Seus lotes de questões acabaram",
        "No Essencial são 10 lotes por mês, com até 20 questões cada. No Pro, 20 lotes de até 30.",
        listOf("10 ou 20 lotes por mês", "25 ou 50 materiais por mês", "Simulados no estilo da banca", "Seu próprio edital em PDF"),
    ),
    GENERIC(
        "Seu plano chegou no limite",
        "Os planos pagos liberam mais materiais, questões, simulados e o seu próprio edital em PDF.",
        listOf("25 ou 50 materiais por mês", "Lotes de questões extras", "Simulados no estilo da banca", "Seu próprio edital em PDF"),
    );

    companion object {
        fun forFeature(feature: String?): UpgradeReason = when (feature) {
            "CONTENT_GENERATION" -> CONTENT
            "SYLLABUS_GENERATION" -> SYLLABUS
            "SIMULATION_GENERATION" -> SIMULATION
            "QUESTION_BATCH" -> QUESTIONS
            else -> GENERIC
        }
    }
}

/**
 * A janela que aparece quando o limite do plano estoura: diz o que acabou, o que o plano pago dá,
 * e abre a tela de planos com um toque. "Agora não" fecha sem insistir.
 */
@Composable
fun UpgradeDialog(reason: UpgradeReason, onDismiss: () -> Unit, onSignIn: (() -> Unit)? = null) {
    var plans by remember { mutableStateOf(false) }
    if (plans) {
        PlansDialog(onDismiss = { plans = false; onDismiss() }, onSignIn = onSignIn)
        return
    }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Folha(64.dp, mood = FolhaMood.TALKING)
                    Spacer(Modifier.width(14.dp))
                    Text(reason.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
                }
                Text(reason.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    reason.perks.forEach { perk ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Check, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.width(10.dp))
                            Text(perk, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Text("Essencial a partir de R$ 24,90 por mês. Cancela quando quiser, pela Google Play.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(2.dp))
                Button(onClick = { plans = true }, modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("Ver planos", fontWeight = FontWeight.Bold) }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Agora não") }
            }
        }
    }
}

/** Botão "Ver planos" que abre a janela de upgrade; cuida do próprio estado. */
@Composable
fun UpgradeCta(reason: UpgradeReason, modifier: Modifier = Modifier, label: String = "Ver planos", onSignIn: (() -> Unit)? = null) {
    var open by remember { mutableStateOf(false) }
    Button(onClick = { open = true }, modifier = modifier.fillMaxWidth().height(52.dp)) { Text(label, fontWeight = FontWeight.Bold) }
    if (open) UpgradeDialog(reason, onDismiss = { open = false }, onSignIn = onSignIn)
}
