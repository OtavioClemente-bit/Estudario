package br.com.estudario.ui.plans

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.assistant.Folha
import br.com.estudario.ui.assistant.FolhaMood
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Icon

/**
 * O que levou a pessoa até a oferta: muda o título e a frase. Os números são os dos planos de
 * verdade (ai_plan_limits); se mudarem lá, mudam aqui e em site/index.html.
 */
enum class UpgradeReason(val title: String, val body: String) {
    CONTENT("Seus materiais deste mês acabaram", "Cada material traz teoria, resumo, flashcards, dicas e questões comentadas do tópico. Com um plano, você segue estudando hoje mesmo."),
    SYLLABUS("Mande o edital do seu concurso", "O edital pelo seu PDF é dos planos Essencial e Pro: as matérias e os tópicos saem organizados para você revisar e estudar."),
    SIMULATION("Simulados no estilo da sua banca", "Treine com cronômetro e nota por matéria, no formato da prova. Os simulados completos são dos planos Essencial e Pro."),
    QUESTIONS("Seus lotes de questões acabaram", "Mais questões comentadas no estilo da banca, para treinar o tópico até ele ficar firme."),
    EDITAIS("Você chegou ao limite de editais salvos", "No Grátis você guarda até 5 editais. No Essencial são 15, e no Pro não há limite."),
    PLANS("Mais planos de estudo", "No Grátis você tem um plano e pode ajustá-lo quando quiser. Com o Essencial ou o Pro, dá para montar planos novos todo mês."),
    GENERIC("Estude sem parar no meio", "Os planos Essencial e Pro liberam mais materiais, questões, simulados e o edital pelo seu PDF."),
    EXTRAS("Suas 10 matérias extras do mês acabaram", "No Grátis, o plano do dia é sempre livre e você estuda mais 10 matérias por mês fora dele. Com um plano, você estuda qualquer matéria do edital, quando quiser."),
    ADVANCE("Adiante seu plano", "No Grátis, cada anúncio curto adianta uma matéria do plano (até 2 por dia). Com um plano, você adianta quantos dias quiser, sem anúncio."),
    PLAN_DAY("O plano de hoje já está com você", "As matérias de hoje já saíram. Amanhã chegam as próximas, ou adiante um dia. Com um plano, você estuda no seu ritmo."),
    NOT_READY("Esta matéria ainda não está pronta", "Ela entrou na fila da nossa equipe e você recebe um aviso quando ficar pronta. Com o Essencial, o Estudário escreve a matéria agora mesmo, com fontes oficiais.");

    companion object {
        fun forFeature(feature: String?): UpgradeReason = when (feature) {
            "CONTENT_GENERATION" -> CONTENT
            "SYLLABUS_GENERATION" -> SYLLABUS
            "SIMULATION_GENERATION" -> SIMULATION
            "QUESTION_BATCH" -> QUESTIONS
            "PLAN_GENERATION" -> PLANS
            else -> GENERIC
        }
    }
}

/** A recusa do servidor que vira oferta (em vez de erro), pela frase que o gerador devolve. */
fun upgradeReasonOf(message: String, questionsOnly: Boolean): UpgradeReason? = when (message) {
    br.com.estudario.data.ai.QUOTA_ENDED_MESSAGE -> if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT
    br.com.estudario.data.ai.FREE_EXTRAS_ENDED_MESSAGE -> UpgradeReason.EXTRAS
    br.com.estudario.data.ai.FREE_ADVANCE_AD_MESSAGE -> UpgradeReason.ADVANCE
    br.com.estudario.data.ai.FREE_PLAN_DAY_DONE_MESSAGE -> UpgradeReason.PLAN_DAY
    br.com.estudario.data.ai.NOT_READY_MESSAGE -> UpgradeReason.NOT_READY
    br.com.estudario.data.ai.DEVICE_QUOTA_MESSAGE -> UpgradeReason.CONTENT
    else -> null
}

/** Os dois planos pagos como aparecem na oferta (valores do banco, conferidos em 14/10/2026). */
private data class OfferPlan(val tier: String, val name: String, val price: String, val lines: List<Pair<String, String>>)

private val offerPlans = listOf(
    OfferPlan("ESSENCIAL", "Essencial", "R$ 24,90", listOf("25" to "matérias extras por mês", "10" to "lotes de questões", "3" to "simulados por mês", "2" to "editais pelo PDF")),
    OfferPlan("PRO", "Pro", "R$ 49,90", listOf("40" to "matérias extras por mês", "25" to "lotes de questões", "6" to "simulados por mês", "5" to "editais pelo PDF")),
)

/**
 * A oferta de plano, em tela cheia: aparece quando o saldo acaba, quando um recurso é dos planos
 * pagos e quando a pessoa tenta mandar o PDF do edital no Grátis. Fundo de tinta do Folha, os dois
 * planos lado a lado e um botão que leva à escolha de planos. "Agora não" fecha sem insistir.
 */
@Composable
fun UpgradeDialog(reason: UpgradeReason, onDismiss: () -> Unit, onSignIn: (() -> Unit)? = null) {
    var plans by remember { mutableStateOf(false) }
    if (plans) {
        PlansDialog(onDismiss = { plans = false; onDismiss() }, onSignIn = onSignIn)
        return
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BackHandler(onBack = onDismiss)
        val enter = remember { Animatable(0f) }
        LaunchedEffect(Unit) { enter.animateTo(1f, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow)) }
        val pulse = rememberInfiniteTransition(label = "offer")
        val halo by pulse.animateFloat(0.85f, 1.12f, infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "halo")
        val sweep by pulse.animateFloat(-0.4f, 1.4f, infiniteRepeatable(tween(2200, delayMillis = 500, easing = LinearEasing)), label = "sweep")
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF2E2799), Color(0xFF1B1464), Color(0xFF0E0C24)))),
        ) {
            // Brilho dourado no alto, a cor do capelo.
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .size(360.dp)
                    .graphicsLayer { scaleX = halo; scaleY = halo }
                    .background(Brush.radialGradient(listOf(Color(0x33F5B83D), Color.Transparent)), CircleShape),
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp)
                    .graphicsLayer { alpha = enter.value; translationY = (1f - enter.value) * 60.dp.toPx() },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar", tint = Color.White.copy(alpha = 0.8f)) }
                }
                Folha(110.dp, mood = FolhaMood.WAVE)
                Spacer(Modifier.height(12.dp))
                Text(reason.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White, textAlign = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                Text(reason.body, style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.82f), textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    offerPlans.forEach { plan -> OfferCard(plan, Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "No Grátis continuam a matéria do plano de hoje, as revisões, o treino e o caderno.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(22.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF5B83D))
                        .drawBehind {
                            val x = size.width * sweep
                            drawRect(Brush.linearGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.45f), Color.Transparent), start = Offset(x - 120f, 0f), end = Offset(x + 120f, size.height)))
                        }
                        .clickable { plans = true },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Conhecer os planos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFF1B1464))
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "Agora não",
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.75f),
                )
                Text(
                    "Assinatura pela Google Play. Cancela quando quiser.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun OfferCard(plan: OfferPlan, modifier: Modifier = Modifier) {
    val look = tierLook(plan.tier)
    Column(
        modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.verticalGradient(listOf(look.color, look.deep)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandIcon(look.glyph, size = 30.dp)
            Spacer(Modifier.width(8.dp))
            Text(plan.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
        }
        Column {
            Text(plan.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
            Text("por mês", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.75f))
        }
        plan.lines.forEach { (value, label) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(26.dp).clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
                    Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

/** Botão "Ver planos" que abre a oferta; cuida do próprio estado. */
@Composable
fun UpgradeCta(reason: UpgradeReason, modifier: Modifier = Modifier, label: String = "Ver planos", onSignIn: (() -> Unit)? = null, openAtStart: Boolean = false) {
    var open by remember { mutableStateOf(openAtStart) }
    br.com.estudario.ui.brand.Button(onClick = { open = true }, modifier = modifier.fillMaxWidth().height(52.dp)) { Text(label, fontWeight = FontWeight.Bold) }
    if (open) UpgradeDialog(reason, onDismiss = { open = false }, onSignIn = onSignIn)
}
