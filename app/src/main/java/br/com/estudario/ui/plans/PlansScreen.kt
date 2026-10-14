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
import br.com.estudario.ui.brand.Surface
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
import br.com.estudario.data.ai.AiPlanPricing
import br.com.estudario.data.ai.AiPlanUsage
import br.com.estudario.data.ai.formatBrl
import br.com.estudario.data.billing.PurchaseEvent
import com.android.billingclient.api.ProductDetails
import androidx.compose.runtime.collectAsState
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

/** O que o saldo mostra, inclusive os planos de estudo (o Grátis tem um; os pagos, novos todo mês). */
private val VISIBLE_FEATURES = setOf("SYLLABUS_GENERATION", "CONTENT_GENERATION", "SIMULATION_GENERATION", "QUESTION_BATCH", "PLAN_GENERATION", "AD_REWARD")

private fun featureOrder(feature: String): Int = listOf(
    "CONTENT_GENERATION", "PLAN_GENERATION", "SYLLABUS_GENERATION", "SIMULATION_GENERATION", "QUESTION_BATCH", "AD_REWARD",
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
    val context = LocalContext.current
    val app = context.applicationContext as EstudarioApplication
    val repository = app.aiPlanRepository
    val billing = app.playBilling
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<PlansState>(PlansState.Loading) }
    var reload by remember { mutableIntStateOf(0) }
    var products by remember { mutableStateOf<List<ProductDetails>>(emptyList()) }
    var yearly by remember { mutableStateOf(false) }
    var buying by remember { mutableStateOf(false) }
    var selectedTier by remember { mutableStateOf<String?>(null) }
    val event by billing.events.collectAsState()

    LaunchedEffect(reload) {
        state = PlansState.Loading
        state = when (val result = repository.load()) {
            is AiPlanLoadResult.Available -> PlansState.Ready(result.summary)
            AiPlanLoadResult.SignedOut -> PlansState.SignedOut
            AiPlanLoadResult.Unavailable -> PlansState.Unavailable
        }
        val summary = (state as? PlansState.Ready)?.summary
        if (summary != null && summary.billing.playEnabled) {
            products = billing.products(summary.pricing.mapNotNull { it.playProductId })
        }
    }
    // Compra confirmada pelo servidor: recarrega para mostrar o plano novo.
    LaunchedEffect(event) {
        if (event is PurchaseEvent.Activated) { buying = false; reload++ }
        if (event is PurchaseEvent.Cancelled || event is PurchaseEvent.Failed || event is PurchaseEvent.Pending) buying = false
    }

    val subscribe: (AiPlanPricing) -> Unit = { pricing ->
        val activity = context.findActivity()
        val product = products.firstOrNull { it.productId == pricing.playProductId }
        val basePlan = if (yearly) pricing.playBasePlanYear else pricing.playBasePlanMonth
        if (activity != null && product != null && basePlan != null) {
            buying = true
            scope.launch { if (!billing.subscribe(activity, product, basePlan)) buying = false }
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Box(Modifier.fillMaxSize()) {
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
                            "O plano fica na sua conta Estudário, não no aparelho. Entre com o Google para ver seu plano, o que ainda pode gerar e assinar.",
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
                            PlanPickerSection(
                                summary = current.summary,
                                products = products,
                                yearly = yearly,
                                onYearly = { yearly = it },
                                onSelected = { selectedTier = it },
                            )
                            UsageSection(current.summary.usage)
                            EveryPlanHas()
                        }
                    }
                    // Espaço para o botão fixo embaixo não cobrir o fim da lista.
                    Spacer(Modifier.height(if (state is PlansState.Ready) 110.dp else 12.dp))
                }
            }
            val ready = (state as? PlansState.Ready)?.summary
            val tier = selectedTier
            if (ready != null && tier != null) {
                val pricing = ready.pricingOf(tier) ?: AiPlanPricing(tier)
                val product = products.firstOrNull { it.productId == pricing.playProductId }
                val basePlan = if (yearly) pricing.playBasePlanYear else pricing.playBasePlanMonth
                val available = ready.billing.playEnabled && product != null && basePlan != null
                val isCurrent = tier == ready.planTier
                val price = shownPrice(pricing, product, yearly)
                val name = pricing.name.ifBlank { planDisplayName(tier) }
                Box(Modifier.align(Alignment.BottomCenter).navigationBarsPadding()) {
                    PlanActionBar(
                        label = when {
                            isCurrent -> "Este é o seu plano"
                            !pricing.isPaid -> "Continuar no Grátis"
                            available -> "Assinar $name · ${price.main}"
                            else -> "Em breve na Google Play"
                        },
                        sublabel = when {
                            isCurrent || !pricing.isPaid -> null
                            available -> "Cobrado pela Google Play. Cancela quando quiser, sem multa."
                            else -> null
                        },
                        color = tierLook(tier).color,
                        enabled = !isCurrent && (!pricing.isPaid || available),
                        busy = buying,
                        onClick = { if (pricing.isPaid) subscribe(pricing) else onDismiss() },
                    )
                }
            }
          }
        }
    }

    when (val current = event) {
        is PurchaseEvent.Activated -> AlertDialog(
            onDismissRequest = { billing.consumeEvent() },
            icon = { Icon(Icons.Outlined.WorkspacePremium, null) },
            title = { Text("Assinatura ativa") },
            text = { Text("Seu plano ${planDisplayName(current.planTier)} já está valendo nesta conta, no celular e no computador. Bons estudos!") },
            confirmButton = { TextButton(onClick = { billing.consumeEvent() }) { Text("Começar") } },
        )
        is PurchaseEvent.Pending -> AlertDialog(
            onDismissRequest = { billing.consumeEvent() },
            title = { Text("Pagamento em análise") },
            text = { Text("A Google Play ainda está confirmando o pagamento. Assim que cair, o plano é liberado sozinho.") },
            confirmButton = { TextButton(onClick = { billing.consumeEvent() }) { Text("Entendi") } },
        )
        is PurchaseEvent.Failed -> AlertDialog(
            onDismissRequest = { billing.consumeEvent() },
            title = { Text("A compra não foi concluída") },
            text = { Text(current.message) },
            confirmButton = { TextButton(onClick = { billing.consumeEvent() }) { Text("Fechar") } },
        )
        else -> Unit
    }
}

private fun android.content.Context.findActivity(): android.app.Activity? {
    var current: android.content.Context = this
    while (current is android.content.ContextWrapper) {
        if (current is android.app.Activity) return current
        current = current.baseContext
    }
    return null
}

/** Fundo do topo: índigo chapado, como o resto do app (sem degradê). */
@Composable
private fun brandBrush(): Brush = Brush.linearGradient(
    listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary),
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
                                    currentTier == "FREE" -> "Plano grátis · assine para gerar mais"
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
            Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) { Icon(Icons.Outlined.OndemandVideo, null, tint = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(43.dp)) }
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

/** A escolha do plano: Folha, seletor mensal/anual, carrossel e comparação, tudo reagindo junto. */
@Composable
private fun PlanPickerSection(
    summary: AiPlanSummary,
    products: List<ProductDetails>,
    yearly: Boolean,
    onYearly: (Boolean) -> Unit,
    onSelected: (String) -> Unit,
) {
    val plans = remember(summary) { pickerOrder(summary.plans) }
    if (plans.isEmpty()) return
    // Começa no plano da pessoa; quem está no Grátis começa no recomendado.
    val start = remember(plans) {
        val current = plans.indexOfFirst { it.planTier == summary.planTier }
        val featured = plans.indexOfFirst { summary.pricingOf(it.planTier)?.featured == true }
        (if (summary.planTier == "FREE" && featured >= 0) featured else current).coerceAtLeast(0)
    }
    val pager = androidx.compose.foundation.pager.rememberPagerState(initialPage = start) { plans.size }
    val selected = plans[pager.currentPage.coerceIn(0, plans.lastIndex)]
    LaunchedEffect(selected.planTier) { onSelected(selected.planTier) }
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        SectionTitle("Escolha seu plano", "Arraste ou toque. O conteúdo é o mesmo em todos; muda quanto você pode gerar.")
        // No Grátis não há preço: a chave mensal/anual some (com animação) e volta nos pagos.
        androidx.compose.animation.AnimatedVisibility(
            visible = summary.pricingOf(selected.planTier)?.isPaid == true,
            enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut(),
        ) { SlidingPeriodToggle(yearly, onYearly, tierLook(selected.planTier).color) }
    }
    PlanCarousel(
        plans = plans,
        pricingOf = { summary.pricingOf(it) ?: AiPlanPricing(it) },
        productOf = { pricing -> products.firstOrNull { it.productId == pricing.playProductId } },
        currentTier = summary.planTier,
        yearly = yearly,
        pager = pager,
    )
    PlanCompare(selected, plans)
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

/** O que vem em qualquer plano, inclusive o grátis. */
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
        Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
