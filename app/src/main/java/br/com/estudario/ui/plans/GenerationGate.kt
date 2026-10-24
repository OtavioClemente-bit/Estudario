package br.com.estudario.ui.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiFreeAdAdvances
import br.com.estudario.data.ai.AiPlanSummary

/**
 * O que acontece quando a pessoa toca em gerar: seguir para o assistente, oferecer o anúncio que
 * adianta (Grátis) ou mostrar a oferta porque o saldo acabou. A conta é feita com o saldo lido do
 * servidor na hora do toque, para ninguém preencher o assistente inteiro e só no fim descobrir que
 * não tinha saldo. Isto é só a cara: o servidor confere tudo de novo ao criar o pedido.
 */
sealed interface GenerationGate {
    data object Go : GenerationGate
    data class NeedsAd(val ads: AiFreeAdAdvances) : GenerationGate
    data class Blocked(val reason: UpgradeReason) : GenerationGate
    /** Pagos: já foram as matérias do plano livres de hoje; esta gasta 1 das [remaining] do mês. */
    data class OverDailyPlan(val limit: Int, val remaining: Int) : GenerationGate
}

/** [purpose]: PLAN_DAY, ADVANCE ou EXTRA (AppViewModel.contentPurposeFor). */
fun generationGate(summary: AiPlanSummary, purpose: String, questionsOnly: Boolean): GenerationGate {
    fun remaining(feature: String) = summary.usage.firstOrNull { it.feature == feature }?.remaining
    if (questionsOnly) return if (remaining("QUESTION_BATCH") == 0) GenerationGate.Blocked(UpgradeReason.QUESTIONS) else GenerationGate.Go
    val free = summary.freeAllowance
        ?: return paidGate(summary, purpose, remaining("CONTENT_GENERATION"))
    return when (purpose) {
        "PLAN_DAY" -> if (free.planDay.left == 0) GenerationGate.Blocked(UpgradeReason.PLAN_DAY) else GenerationGate.Go
        // Adiantar: crédito de anúncio já confirmado ou o adiantamento grátis da semana seguem; sem
        // eles, o anúncio (se ainda houver hoje e no mês); sem anúncio, a oferta.
        "ADVANCE" -> when {
            free.adAdvances.available > 0 || free.weeklyAdvance.left > 0 -> GenerationGate.Go
            free.adAdvances.canWatch -> GenerationGate.NeedsAd(free.adAdvances)
            else -> GenerationGate.Blocked(UpgradeReason.ADVANCE)
        }
        else -> GenerationGate.Blocked(UpgradeReason.EXTRAS)
    }
}

/**
 * Pagos. Fora do plano gasta a cota do mês. A matéria do plano (do dia ou adiantada) não gasta, até
 * 4 por dia; mesmo sem cota no mês ela sai, se estiver pronta na biblioteca (o servidor confere).
 * Da 5ª em diante, gasta a cota: o Folha avisa antes e a pessoa escolhe.
 */
private fun paidGate(summary: AiPlanSummary, purpose: String, contentLeft: Int?): GenerationGate {
    if (purpose != "PLAN_DAY" && purpose != "ADVANCE") return if (contentLeft == 0) GenerationGate.Blocked(UpgradeReason.CONTENT) else GenerationGate.Go
    val today = summary.planToday ?: return GenerationGate.Go
    if (today.used < today.limit) return GenerationGate.Go
    return if (contentLeft == 0) GenerationGate.Blocked(UpgradeReason.CONTENT) else GenerationGate.OverDailyPlan(today.limit, contentLeft ?: 0)
}

/**
 * Confere o saldo e devolve true quando o assistente pode abrir. Enquanto confere, mostra uma
 * espera curta; sem saldo, mostra a oferta (ou o anúncio, no adiantar do Grátis) no lugar do
 * assistente. Sem resposta do servidor, segue: o servidor decide ao criar o pedido.
 */
@Composable
fun generationAllowed(purpose: String, questionsOnly: Boolean, onDismiss: () -> Unit): Boolean {
    val context = LocalContext.current
    var gate by remember(purpose, questionsOnly) { mutableStateOf<GenerationGate?>(null) }
    LaunchedEffect(purpose, questionsOnly) {
        val app = context.applicationContext as? EstudarioApplication
        val summary = (app?.aiPlanRepository?.load() as? AiPlanLoadResult.Available)?.summary
        gate = when {
            summary != null -> generationGate(summary, purpose, questionsOnly)
            // Sem internet: no Grátis, fora do plano continua fechado (a regra é fixa).
            !questionsOnly && purpose == "EXTRA" && br.com.estudario.data.planner.LibraryGaps.free(context) -> GenerationGate.Blocked(UpgradeReason.EXTRAS)
            else -> GenerationGate.Go
        }
    }
    when (val current = gate) {
        null -> CheckingBalance(onDismiss)
        GenerationGate.Go -> return true
        is GenerationGate.Blocked -> UpgradeDialog(current.reason, onDismiss = onDismiss)
        is GenerationGate.NeedsAd -> AdvanceWithAdDialog(current.ads, onCredit = { gate = GenerationGate.Go }, onDismiss = onDismiss)
        is GenerationGate.OverDailyPlan -> QualityOverQuantityDialog(current, onContinue = { gate = GenerationGate.Go }, onDismiss = onDismiss)
    }
    return false
}

@Composable
private fun CheckingBalance(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)).padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CircularProgressIndicator(Modifier.size(32.dp))
            Text("Conferindo o seu saldo…", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Grátis adiantando o plano sem crédito: o anúncio aparece logo, antes do assistente. Só com o
 * crédito confirmado pelo Google (ad-reward-ssv) o assistente abre; fechar o anúncio antes do fim
 * não libera nada.
 */
@Composable
private fun AdvanceWithAdDialog(ads: AiFreeAdAdvances, onCredit: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF2E2799), Color(0xFF1B1464), Color(0xFF0E0C24))))) {
            Column(
                Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(Modifier.height(24.dp))
                br.com.estudario.ui.assistant.Folha(120.dp, mood = br.com.estudario.ui.assistant.FolhaMood.TALKING)
                Text(UpgradeReason.ADVANCE.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White, textAlign = TextAlign.Center)
                Text(
                    "Seu adiantamento grátis desta semana já foi usado. Assista a um anúncio curto e estude a próxima matéria do plano agora.",
                    style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f), textAlign = TextAlign.Center,
                )
                RewardedAdvanceCard(ads, onCredit = onCredit)
                UpgradeCta(UpgradeReason.ADVANCE, label = "Adiantar sem anúncio com o Essencial")
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Agora não", color = Color.White.copy(alpha = 0.8f)) }
            }
        }
    }
}

/**
 * Pagos, da 5ª matéria do plano no dia em diante: o Folha lembra que quantidade não vence
 * qualidade. Dá para seguir, mas essa matéria gasta 1 das do mês.
 */
@Composable
private fun QualityOverQuantityDialog(gate: GenerationGate.OverDailyPlan, onContinue: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            br.com.estudario.ui.assistant.Folha(104.dp, mood = br.com.estudario.ui.assistant.FolhaMood.THINKING)
            Text("Respira um pouco!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(
                "Você já estudou ${gate.limit} matérias novas hoje. Quantidade não vence qualidade: o que fica é o que você revisa e treina com questões. " +
                    "Que tal fechar o dia revisando?",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
            )
            Text(
                "Se quiser seguir, esta matéria usa 1 das ${gate.remaining} que ainda restam no seu mês.",
                style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
            )
            br.com.estudario.ui.brand.Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Deixar para amanhã") }
            TextButton(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Seguir mesmo assim (usa 1 do mês)") }
        }
    }
}

/**
 * A conta está no Grátis? Começa pela marcação do aparelho (LibraryGaps, instantânea; sem marcação, Grátis) e confirma com o
 * servidor logo em seguida: a marcação podia estar velha (ex.: app aberto antes da última consulta) e
 * aí o assistente mostrava as escolhas que o Grátis não tem.
 */
@Composable
fun rememberFreeTier(): Boolean {
    val context = LocalContext.current
    // Só mostra as escolhas dos pagos com o pago confirmado: sem resposta do servidor, fica como Grátis.
    var free by remember { mutableStateOf(!br.com.estudario.data.planner.LibraryGaps.knownPaid(context)) }
    LaunchedEffect(Unit) {
        val app = context.applicationContext as? EstudarioApplication ?: return@LaunchedEffect
        val summary = (app.aiPlanRepository.load() as? AiPlanLoadResult.Available)?.summary ?: return@LaunchedEffect
        br.com.estudario.data.planner.LibraryGaps.rememberTier(context, summary.planTier)
        free = summary.planTier == "FREE"
    }
    return free
}
