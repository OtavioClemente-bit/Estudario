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
        "Suas matérias deste mês acabaram",
        "No Essencial são 25 matérias por mês, com teoria, flashcards, dicas e até 20 questões no estilo da sua banca. No Pro, 40, com até 30 questões.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    SYLLABUS(
        "Edital pelo seu PDF é dos planos pagos",
        "No Grátis você usa os editais do catálogo. Para mandar o PDF do seu concurso, assine: 2 por mês no Essencial, 5 no Pro.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    SIMULATION(
        "Simulados completos são dos planos pagos",
        "No Grátis você faz o simulado diagnóstico. No Essencial são 3 simulados por mês no estilo da sua banca; no Pro, 6.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    QUESTIONS(
        "Seus lotes de questões acabaram",
        "No Grátis cada matéria tem 1 lote extra. No Essencial são 10 lotes de 10 questões por mês; no Pro, 25, sem limite por matéria.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    EDITAIS(
        "Você chegou ao limite de editais salvos",
        "No Grátis você guarda até 5 editais. No Essencial são 15 e no Pro não há limite.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    PLANS(
        "Novos planos de estudo são dos planos pagos",
        "No Grátis você tem um plano de estudo e pode ajustá-lo quando quiser. No Essencial dá para criar 3 planos por mês, e no Pro, 10.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    GENERIC(
        "Seu plano chegou no limite",
        "Os planos pagos liberam mais matérias, lotes de questões, simulados e o seu próprio edital em PDF.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
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
