package br.com.estudario.ui.plans

import br.com.estudario.ui.components.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.WorkspacePremium
import br.com.estudario.ui.brand.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import br.com.estudario.ui.brand.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import br.com.estudario.ui.brand.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiPlanCatalogEntry
import br.com.estudario.data.ai.AiPlanLimit
import br.com.estudario.data.ai.AiPlanSummary
import br.com.estudario.data.ai.AiPlanUsage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

private val saoPaulo = ZoneId.of("America/Sao_Paulo")
private val dateFormat = DateTimeFormatter.ofPattern("dd/MM").withZone(saoPaulo)

/** Nome exibido de cada plano. Preços só entram quando a cobrança for aberta. */
fun planDisplayName(tier: String): String = when (tier) {
    "ESSENCIAL" -> "Essencial"
    "PRO" -> "Pro"
    else -> "Grátis"
}

/** Para quem é cada plano, numa frase. */
private fun planTagline(tier: String): String = when (tier) {
    "ESSENCIAL" -> "Para estudar todo dia, matéria por matéria"
    "PRO" -> "Para a reta final, com folga para estudar tudo"
    else -> "Para conhecer o Estudário"
}

private fun featureLabel(feature: String): String = when (feature) {
    "SYLLABUS_GENERATION" -> "Editais organizados"
    "PLAN_GENERATION" -> "Planos de estudo"
    "CONTENT_GENERATION" -> "Materiais completos"
    "QUESTION_BATCH" -> "Lotes de questões extras"
    "AD_REWARD" -> "Bônus por anúncio"
    "SIMULATION_GENERATION" -> "Simulados no estilo da banca"
    else -> feature
}

private fun featureIcon(feature: String): ImageVector = when (feature) {
    "SYLLABUS_GENERATION" -> Icons.Outlined.Description
    "CONTENT_GENERATION" -> Icons.Outlined.MenuBook
    "QUESTION_BATCH" -> Icons.Outlined.Quiz
    "SIMULATION_GENERATION" -> Icons.Outlined.WorkspacePremium
    "AD_REWARD" -> Icons.Outlined.OndemandVideo
    else -> Icons.Outlined.AutoAwesome
}

/** O plano de estudo é feito pelo app (sem IA), então não aparece como recurso com limite. */
private val VISIBLE_FEATURES = setOf("SYLLABUS_GENERATION", "CONTENT_GENERATION", "SIMULATION_GENERATION", "QUESTION_BATCH", "AD_REWARD")

private fun featureOrder(feature: String): Int = listOf(
    "CONTENT_GENERATION", "SYLLABUS_GENERATION", "SIMULATION_GENERATION", "QUESTION_BATCH", "AD_REWARD",
).indexOf(feature).let { if (it < 0) Int.MAX_VALUE else it }

private fun formatDate(iso: String?): String? = iso?.let { runCatching { dateFormat.format(Instant.parse(it)) }.getOrNull() }

/** Uma linha do catálogo, ex.: "10 por mês", "1 no total", "até 10 questões por lote". */
private fun limitCopy(limit: AiPlanLimit): String {
    val base = when (limit.periodKind) {
        "LIFETIME" -> "${limit.quotaLimit} no total"
        "DAILY" -> "${limit.quotaLimit} por dia"
        else -> "${limit.quotaLimit} por mês"
    }
    val extras = listOfNotNull(
        limit.maxPerRequest?.let { "até $it questões por lote" },
        limit.maxPerTopicMonth?.let { "$it por tópico no mês" },
    )
    return (listOf(base) + extras).joinToString(" · ")
}

private sealed interface PlansState {
    data object Loading : PlansState
    data class Ready(val summary: AiPlanSummary) : PlansState
    data object SignedOut : PlansState
    data object Unavailable : PlansState
}

@Composable
fun PlansDialog(onDismiss: () -> Unit, onSignIn: (() -> Unit)? = null) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val repository = app.aiPlanRepository
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<PlansState>(PlansState.Loading) }
    var reload by remember { mutableIntStateOf(0) }
    var adBusy by remember { mutableStateOf(false) }
    var adMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(reload) {
        state = PlansState.Loading
        state = when (val result = repository.load()) {
            is AiPlanLoadResult.Available -> PlansState.Ready(result.summary)
            AiPlanLoadResult.SignedOut -> PlansState.SignedOut
            AiPlanLoadResult.Unavailable -> PlansState.Unavailable
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Hero(
                    currentTier = (state as? PlansState.Ready)?.summary?.planTier,
                    renewsAt = (state as? PlansState.Ready)?.summary?.planRenewsAt,
                    onDismiss = onDismiss,
                )
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 20.dp).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    when (val current = state) {
                        PlansState.Loading -> Row(Modifier.fillMaxWidth().padding(32.dp), horizontalArrangement = Arrangement.Center) { CircularProgressIndicator() }
                        PlansState.SignedOut -> Notice(
                            "Entre na sua conta",
                            "O saldo fica vinculado à sua conta Estudário, não ao aparelho. Entre com o Google para ver seu plano e quanto ainda pode gerar.",
                            actionLabel = if (onSignIn != null) "Entrar com o Google" else null,
                            onAction = { onSignIn?.invoke() },
                        )
                        PlansState.Unavailable -> Notice(
                            "Não foi possível carregar seu plano",
                            "Verifique a conexão e tente novamente. Seus dados de estudo continuam no aparelho.",
                            actionLabel = "Tentar novamente",
                            onAction = { reload++ },
                        )
                        is PlansState.Ready -> {
                            UsageSection(current.summary.usage)
                            AdRewardCard(
                                visible = current.summary.planTier == "FREE",
                                busy = adBusy,
                                onClick = {
                                    adBusy = true
                                    scope.launch {
                                        val result = repository.registerAdInterest()
                                        adBusy = false
                                        adMessage = if (result?.recorded == true) {
                                            "Os anúncios recompensados chegam depois do teste fechado. Registramos seu interesse; nenhum anúncio foi exibido e nenhum crédito foi adicionado."
                                        } else {
                                            "Não foi possível registrar agora. Tente novamente mais tarde."
                                        }
                                    }
                                },
                            )
                            PlanCatalog(current.summary.plans, current.summary.planTier)
                            EveryPlanHas()
                            HowQuotaWorks()
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }

    adMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { adMessage = null },
            icon = { Icon(Icons.Outlined.OndemandVideo, null) },
            title = { Text("Gerações extras com anúncio") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { adMessage = null }) { Text("Entendi") } },
        )
    }
}

/** Violeta que fecha o gradiente da marca (o terciário do tema é quente e sujava a mistura). */
private val BRAND_VIOLET = Color(0xFF9F5BF5)

/** Gradiente da marca, do índigo para o violeta. */
@Composable
private fun brandBrush(): Brush = Brush.linearGradient(
    listOf(MaterialTheme.colorScheme.primary, BRAND_VIOLET),
)

@Composable
private fun Hero(currentTier: String?, renewsAt: String?, onDismiss: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(brandBrush()),
    ) {
        // Brilho decorativo no canto: dá profundidade sem imagem.
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp)
                .size(220.dp)
                .background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent)), CircleShape),
        )
        Column(Modifier.fillMaxWidth().statusBarsPadding().padding(start = 24.dp, end = 12.dp, top = 8.dp, bottom = 28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.18f)) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.AutoAwesome, null, Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Estudário", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar", tint = Color.White) }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "Do edital à aprovação,\num dia de cada vez",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Teoria, flashcards e questões no estilo da banca para cada item do seu edital, com fonte conferida.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.88f),
                modifier = Modifier.padding(end = 12.dp),
            )
            if (currentTier != null) {
                Spacer(Modifier.height(18.dp))
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.WorkspacePremium, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Seu plano: ${planDisplayName(currentTier)}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            val renew = formatDate(renewsAt)
                            Text(
                                when {
                                    currentTier == "FREE" -> "Assinaturas abrem depois do teste fechado"
                                    renew != null -> "Ativo até $renew"
                                    else -> "Ativo"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Notice(title: String, body: String, actionLabel: String?, onAction: () -> Unit) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (actionLabel != null) Button(onClick = onAction, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) { Text(actionLabel, fontWeight = FontWeight.Bold) }
        }
    }
}

/** Saldo do mês: um anel por recurso, com o que resta em destaque. */
@Composable
private fun UsageSection(usage: List<AiPlanUsage>) {
    val items = usage.filter { it.feature in VISIBLE_FEATURES && it.feature != "AD_REWARD" }.sortedBy { featureOrder(it.feature) }
    if (items.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("Seu saldo", items.firstNotNullOfOrNull { formatDate(it.resetAt) }?.let { "Renova em $it" })
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
            Column(Modifier.padding(vertical = 8.dp)) {
                items.forEachIndexed { index, item ->
                    if (index > 0) Box(Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))
                    UsageRow(item)
                }
            }
        }
    }
}

@Composable
private fun UsageRow(item: AiPlanUsage) {
    val fraction = if (item.limit == 0) 0f else (item.remaining.toFloat() / item.limit).coerceIn(0f, 1f)
    val low = item.limit > 0 && fraction <= 0.2f
    val ring = if (low) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(52.dp)) {
            CircularProgressIndicator(progress = { 1f }, Modifier.size(52.dp), color = ring.copy(alpha = 0.14f), strokeWidth = 5.dp, strokeCap = StrokeCap.Round)
            CircularProgressIndicator(progress = { fraction }, Modifier.size(52.dp), color = ring, strokeWidth = 5.dp, strokeCap = StrokeCap.Round, trackColor = Color.Transparent)
            Icon(featureIcon(item.feature), null, Modifier.size(22.dp), tint = ring)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(featureLabel(item.feature), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(
                if (item.periodKind == "MONTHLY") "neste mês" else "saldo único, não renova",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${item.remaining}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = ring)
            Text("de ${item.limit}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdRewardCard(visible: Boolean, busy: Boolean, onClick: () -> Unit) {
    if (!visible) return
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(MaterialTheme.colorScheme.secondary), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.OndemandVideo, null, tint = MaterialTheme.colorScheme.onSecondary)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("+1 material com anúncio", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Text("Anúncio curto e opcional. Até 2 por dia.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = onClick,
                enabled = !busy,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = MaterialTheme.colorScheme.onSecondary),
            ) {
                if (busy) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onSecondary) else Text("Assistir", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PlanCatalog(plans: List<AiPlanCatalogEntry>, currentTier: String) {
    if (plans.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionTitle("Escolha seu ritmo", "Todos os planos têm o mesmo conteúdo, com as mesmas fontes conferidas.")
        // O recomendado primeiro, depois o mais completo e por fim o grátis.
        plans.sortedBy { listOf("ESSENCIAL", "PRO", "FREE").indexOf(it.planTier).let { i -> if (i < 0) 9 else i } }.forEach { plan ->
            PlanCard(plan, isCurrent = plan.planTier == currentTier, featured = plan.planTier == "ESSENCIAL")
        }
    }
}

@Composable
private fun PlanCard(plan: AiPlanCatalogEntry, isCurrent: Boolean, featured: Boolean) {
    val premium = plan.planTier != "FREE"
    val highlight = plan.limits.firstOrNull { it.feature == "CONTENT_GENERATION" }
    val shape = RoundedCornerShape(28.dp)
    val border = when {
        featured -> Modifier.border(2.dp, brandBrush(), shape)
        isCurrent -> Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
        else -> Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
    }
    Box(Modifier.fillMaxWidth()) {
        Surface(
            Modifier.fillMaxWidth().padding(top = if (featured) 14.dp else 0.dp).then(border),
            shape = shape,
            color = if (featured) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = if (featured) 2.dp else 0.dp,
        ) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(if (premium) brandBrush() else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            if (plan.planTier == "PRO") Icons.Outlined.Bolt else if (premium) Icons.Outlined.WorkspacePremium else Icons.Outlined.AutoAwesome,
                            null,
                            tint = if (premium) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(planDisplayName(plan.planTier), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(planTagline(plan.planTier), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (isCurrent) {
                        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primary) {
                            Text("Atual", Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if (highlight != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${highlight.quotaLimit}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (highlight.periodKind == "LIFETIME") "materiais completos\npara experimentar" else "materiais completos\npor mês",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    plan.limits
                        .filter { it.feature in VISIBLE_FEATURES && it.feature != "CONTENT_GENERATION" }
                        .groupBy { it.feature }
                        .toSortedMap(compareBy<String> { featureOrder(it) })
                        .forEach { (feature, limits) -> Benefit(featureLabel(feature), limits.joinToString(" · ") { limitCopy(it) }) }
                    if (premium) Benefit("Teoria, flashcards, dicas e questões", "em cada material, com explicação de cada alternativa")
                }
                if (!isCurrent && premium) {
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) { Text("Disponível após o teste fechado", fontWeight = FontWeight.Bold) }
                }
            }
        }
        if (featured) {
            Box(
                Modifier.align(Alignment.TopCenter).clip(RoundedCornerShape(50)).background(brandBrush()).padding(horizontal = 14.dp, vertical = 6.dp),
            ) {
                Text("MAIS ESCOLHIDO", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun Benefit(title: String, detail: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.padding(top = 2.dp).size(20.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Check, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** O que vem em qualquer plano, inclusive o grátis: o app não é só a IA. */
@Composable
private fun EveryPlanHas() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("Em todos os planos")
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surfaceContainerLow) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Perk(Icons.Outlined.EventRepeat, "Plano até a prova", "Cronograma, revisões espaçadas e missão do dia, sem gastar saldo.")
                Perk(Icons.Outlined.Quiz, "Treino e caderno de erros", "Questões, simulados e o que você errou organizado por conceito.")
                Perk(Icons.Outlined.Devices, "Seu estudo no aparelho", "Funciona sem internet. Seu saldo vale em qualquer celular.")
            }
        }
    }
}

@Composable
private fun Perk(icon: ImageVector, title: String, detail: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun HowQuotaWorks() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("Como o saldo funciona")
        listOf(
            Triple(Icons.Outlined.MenuBook, "1 pedido concluído, 1 unidade", "Um edital, o material de um tópico, um lote de questões ou uma parte de simulado."),
            Triple(Icons.Outlined.Shield, "Falhou? Não desconta", "Se a geração falhar ou for cancelada antes do resultado, o saldo volta."),
            Triple(Icons.Outlined.Replay, "Sem cobrança em dobro", "Repetir o mesmo pedido depois de um erro técnico não cobra de novo."),
        ).forEach { (icon, title, detail) -> Perk(icon, title, detail) }
        Text(
            "Os preços aparecem aqui quando as assinaturas abrirem.",
            Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
