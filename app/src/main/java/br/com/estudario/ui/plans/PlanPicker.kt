package br.com.estudario.ui.plans

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.estudario.data.ai.AiPlanCatalogEntry
import br.com.estudario.data.ai.AiPlanPricing
import br.com.estudario.data.ai.formatBrl
import br.com.estudario.ui.assistant.FolhaTalking
import br.com.estudario.ui.brand.BrandIcon
import br.com.estudario.ui.brand.Glyph
import com.android.billingclient.api.ProductDetails
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/*
 * A escolha do plano: um carrossel em que o plano escolhido vem para a frente com mola e leve giro
 * em 3D, borda viva girando, faíscas no toque e vibração. Preço, comparação e botão acompanham a
 * escolha com animação. As cores de cada plano são fixas (como a turma do Folha), não do tema.
 */

/** A identidade de cada plano no seletor. */
internal data class TierLook(val color: Color, val deep: Color, val accent: Color, val glyph: Glyph, val folha: String)

internal fun tierLook(tier: String): TierLook = when (tier) {
    "PRO" -> TierLook(Color(0xFF7C3AED), Color(0xFF4C1D95), Color(0xFFF5B83D), Glyph.Gem, "Reta final? O Pro tem folga para gerar tudo o que você precisar, sem contar material.")
    "ESSENCIAL" -> TierLook(Color(0xFF4F46E5), Color(0xFF2E2799), Color(0xFF22C59A), Glyph.Star, "O Essencial dá conta de um tópico novo por dia, com questões e simulados todo mês.")
    else -> TierLook(Color(0xFF16A37F), Color(0xFF0B6B53), Color(0xFFF5B83D), Glyph.Books, "Começando? No Grátis, a matéria do seu plano de hoje é sempre livre, pronta da biblioteca, e você adianta as próximas com um anúncio curto.")
}

/** Ordem do carrossel: do grátis ao mais completo. */
internal fun pickerOrder(plans: List<AiPlanCatalogEntry>): List<AiPlanCatalogEntry> =
    plans.sortedBy { listOf("FREE", "ESSENCIAL", "PRO").indexOf(it.planTier).let { i -> if (i < 0) 9 else i } }

/** Preço mostrado: o da loja (na moeda da pessoa) quando a Google Play respondeu; senão o do servidor. */
internal data class ShownPrice(val main: String, val unit: String, val perMonth: String?)

internal fun shownPrice(pricing: AiPlanPricing, product: ProductDetails?, yearly: Boolean): ShownPrice {
    if (!pricing.isPaid) return ShownPrice("R$ 0", "para sempre", null)
    val basePlan = if (yearly) pricing.playBasePlanYear else pricing.playBasePlanMonth
    val store = product?.subscriptionOfferDetails.orEmpty()
        .firstOrNull { it.basePlanId == basePlan && it.offerId == null }
        ?.pricingPhases?.pricingPhaseList?.lastOrNull()?.formattedPrice
    val main = store ?: formatBrl(if (yearly) pricing.priceYearCents else pricing.priceMonthCents)
    return ShownPrice(main, if (yearly) "por ano" else "por mês", if (yearly) formatBrl(pricing.priceYearCents / 12) else null)
}

/** Mensal ou anual: um seletor que desliza, com o selo do desconto pulando. */
@Composable
internal fun SlidingPeriodToggle(yearly: Boolean, onYearly: (Boolean) -> Unit, accent: Color) {
    val haptic = LocalHapticFeedback.current
    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp),
    ) {
        val half = maxWidth / 2
        val thumbX by animateDpAsState(if (yearly) half else 0.dp, spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow), label = "thumb")
        val thumbColor by animateColorAsState(accent, tween(400), label = "thumbColor")
        Box(Modifier.offset(x = thumbX).width(half).fillMaxHeight().clip(RoundedCornerShape(14.dp)).background(thumbColor))
        Row(Modifier.fillMaxSize()) {
            listOf(false to "Mensal", true to "Anual").forEach { (value, label) ->
                val selected = yearly == value
                val ink by animateColorAsState(if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, tween(250), label = "ink")
                Row(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { if (!selected) { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove); onYearly(value) } },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = ink)
                    if (value) {
                        val pop by animateFloatAsState(if (yearly) 1.12f else 1f, spring(dampingRatio = 0.35f, stiffness = Spring.StiffnessMedium), label = "pop")
                        Spacer(Modifier.width(6.dp))
                        Box(
                            Modifier
                                .graphicsLayer { scaleX = pop; scaleY = pop }
                                .clip(RoundedCornerShape(50))
                                .background(if (yearly) Color.White else Color(0xFF22C59A))
                                .padding(horizontal = 7.dp, vertical = 2.dp),
                        ) {
                            Text("2 meses grátis", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = if (yearly) accent else Color.White)
                        }
                    }
                }
            }
        }
    }
}

/** O carrossel dos planos. Arrastar ou tocar escolhe; o escolhido vem para a frente. */
@Composable
internal fun PlanCarousel(
    plans: List<AiPlanCatalogEntry>,
    pricingOf: (String) -> AiPlanPricing,
    productOf: (AiPlanPricing) -> ProductDetails?,
    currentTier: String,
    yearly: Boolean,
    pager: PagerState,
) {
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(pager.currentPage) { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 44.dp),
            pageSpacing = 4.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val plan = plans[page]
            val offset = (pager.currentPage - page) + pager.currentPageOffsetFraction
            val distance = abs(offset).coerceIn(0f, 1f)
            val density = LocalDensity.current.density
            PlanTile(
                plan = plan,
                pricing = pricingOf(plan.planTier),
                product = productOf(pricingOf(plan.planTier)),
                yearly = yearly,
                selected = page == pager.currentPage,
                isCurrent = plan.planTier == currentTier,
                modifier = Modifier
                    .graphicsLayer {
                        val scale = 1f - distance * 0.13f
                        scaleX = scale; scaleY = scale
                        alpha = 1f - distance * 0.45f
                        rotationY = offset * 14f
                        translationY = distance * 18.dp.toPx()
                        cameraDistance = 14f * density
                    }
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                        scope.launch { pager.animateScrollToPage(page, animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow)) }
                    },
            )
        }
        // Os pontinhos: o do plano escolhido estica, na cor dele.
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            plans.forEachIndexed { index, plan ->
                val active = index == pager.currentPage
                val w by animateDpAsState(if (active) 26.dp else 8.dp, spring(dampingRatio = 0.6f), label = "dot")
                val c by animateColorAsState(if (active) tierLook(plan.planTier).color else MaterialTheme.colorScheme.outlineVariant, label = "dotColor")
                Box(Modifier.padding(horizontal = 3.dp).width(w).height(8.dp).clip(RoundedCornerShape(50)).background(c))
            }
        }
    }
}

@Composable
private fun PlanTile(
    plan: AiPlanCatalogEntry,
    pricing: AiPlanPricing,
    product: ProductDetails?,
    yearly: Boolean,
    selected: Boolean,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
) {
    val look = tierLook(plan.planTier)
    val shape = RoundedCornerShape(30.dp)
    val lift by animateDpAsState(if (selected) 0.dp else 6.dp, spring(dampingRatio = 0.5f), label = "lift")
    val spin = rememberInfiniteTransition(label = "spin")
    val angle by spin.animateFloat(0f, 360f, infiniteRepeatable(tween(3600, easing = LinearEasing)), label = "angle")
    val glow by spin.animateFloat(0.55f, 1f, infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "glow")
    val sparks = remember { Animatable(1f) }
    LaunchedEffect(selected) { if (selected) { sparks.snapTo(0f); sparks.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) } }

    Box(modifier.padding(top = 18.dp).offset(y = lift), contentAlignment = Alignment.TopCenter) {
        // A borda viva: um degradê girando atrás do cartão, só no escolhido.
        Box(
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .drawBehind {
                    if (selected) rotate(angle) {
                        val brush = Brush.sweepGradient(listOf(look.color, look.accent, Color.White, look.color))
                        drawRect(brush, topLeft = Offset(-size.width, -size.height), size = Size(size.width * 3, size.height * 3))
                    }
                }
                .then(if (selected) Modifier else Modifier.border(2.dp, MaterialTheme.colorScheme.outlineVariant, shape))
                .padding(if (selected) 3.dp else 0.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(27.dp))
                    .background(Brush.verticalGradient(listOf(look.color, look.deep)))
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // O emblema do plano, com um halo pulsando quando escolhido.
                Box(contentAlignment = Alignment.Center) {
                    if (selected) Box(Modifier.size(84.dp).graphicsLayer { scaleX = glow; scaleY = glow; alpha = 1.1f - glow }.clip(CircleShape).background(Color.White.copy(alpha = 0.35f)))
                    Box(Modifier.size(70.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
                        BrandIcon(look.glyph, size = 52.dp)
                    }
                }
                Text(pricing.name.ifBlank { planDisplayName(plan.planTier) }, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White)
                Text(
                    pricing.tagline.ifBlank { "" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.86f),
                    textAlign = TextAlign.Center,
                )
                val price = shownPrice(pricing, product, yearly)
                AnimatedContent(
                    targetState = price,
                    transitionSpec = { (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut()) },
                    label = "price",
                ) { shown ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(shown.main, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = Color.White)
                        Text(
                            listOfNotNull(shown.unit, shown.perMonth?.let { "$it por mês" }).joinToString(" · "),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.85f),
                        )
                    }
                }
                if (isCurrent || pricing.featured) {
                    Box(Modifier.clip(RoundedCornerShape(50)).background(if (isCurrent) Color.White else look.accent).padding(horizontal = 12.dp, vertical = 5.dp)) {
                        Text(if (isCurrent) "Seu plano agora" else "Mais escolhido", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = if (isCurrent) look.deep else Color.White)
                    }
                }
            }
        }
        // Faíscas: saem do emblema quando o plano é escolhido.
        if (sparks.value < 1f) Canvas(Modifier.fillMaxWidth().height(150.dp)) {
            val p = sparks.value
            val center = Offset(size.width / 2f, 24.dp.toPx() + 35.dp.toPx())
            repeat(14) { i ->
                val a = (i / 14f) * 2f * PI.toFloat() + 0.3f
                val r = (30.dp.toPx() + p * 70.dp.toPx())
                val dot = if (i % 3 == 0) look.accent else Color.White
                drawCircle(dot.copy(alpha = (1f - p).coerceIn(0f, 1f)), radius = (1f - p) * 5.dp.toPx() + 1.dp.toPx(), center = Offset(center.x + cos(a) * r, center.y + sin(a) * r))
            }
        }
    }
}

/** O que o plano escolhido inclui: números que sobem contando e barras que crescem, comparando com o Pro. */
@Composable
internal fun PlanCompare(plan: AiPlanCatalogEntry, all: List<AiPlanCatalogEntry>) {
    val look = tierLook(plan.planTier)
    val free = plan.planTier == "FREE"
    val rows = listOf(
        "CONTENT_GENERATION" to "Matérias fora do plano",
        "QUESTION_BATCH" to "Lotes de questões",
        "SIMULATION_GENERATION" to "Simulados",
        "SYLLABUS_GENERATION" to "Edital pelo PDF",
    )
    val tone by animateColorAsState(look.color, tween(450), label = "tone")
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FolhaTalking(56.dp, look.folha)
        // Em todos: a matéria do plano de hoje não gasta nada. No Grátis, as regras dele em texto.
        RuleRow("Matéria do plano de hoje", "livre", tone)
        RuleRow("Adiantar o plano", if (free) "com anúncio curto" else "livre", tone)
        RuleRow("Matérias novas por dia", if (free) "até 3" else "no seu ritmo", tone)
        RuleRow("Matérias fora do plano", if (free) "nos planos pagos" else "pelo saldo do mês", tone)
        // Só o que o plano tem: no Grátis, nada de linha dizendo "não tem" (a cota de matérias do
        // Grátis é só o teto interno; as regras dele estão acima).
        rows.filter { (feature, _) -> (plan.limits.firstOrNull { it.feature == feature }?.quotaLimit ?: 0) > 0 && !(free && feature == "CONTENT_GENERATION") }.forEach { (feature, label) ->
            val limit = plan.limits.firstOrNull { it.feature == feature }
            val max = all.mapNotNull { p -> p.limits.firstOrNull { it.feature == feature }?.quotaLimit }.maxOrNull()?.coerceAtLeast(1) ?: 1
            val value = limit?.quotaLimit ?: 0
            val shown by animateIntAsState(value, tween(650, easing = FastOutSlowInEasing), label = "count")
            val fill by animateFloatAsState(value.toFloat() / max, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow), label = "bar")
            val unit = when (limit?.periodKind) { null -> ""; "LIFETIME" -> "no total"; "DAILY" -> "por dia"; else -> "por mês" }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(label, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(if (value == 0) "não tem" else "$shown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = tone)
                    if (value > 0) Text(" $unit", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 3.dp))
                }
                Box(Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50)).background(tone.copy(alpha = 0.14f))) {
                    Box(Modifier.fillMaxWidth(fill.coerceIn(0f, 1f)).fillMaxHeight().clip(RoundedCornerShape(50)).background(Brush.horizontalGradient(listOf(tone, look.accent))))
                }
            }
        }
        Text(
            "Plano de estudo, revisões, treino e caderno em todos os planos. Teoria, flashcards, dicas e questões comentadas em todo material.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Uma regra em texto (sem barra): o nome e o valor. */
@Composable
private fun RuleRow(label: String, value: String, tone: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = tone)
    }
}

/** O botão fixo embaixo: muda de cor e de texto com o plano, com um brilho passando. */
@Composable
internal fun PlanActionBar(
    label: String,
    sublabel: String?,
    color: Color,
    enabled: Boolean,
    busy: Boolean,
    onClick: () -> Unit,
) {
    val tone by animateColorAsState(color, tween(450), label = "cta")
    val shine = rememberInfiniteTransition(label = "shine")
    val sweep by shine.animateFloat(-0.4f, 1.4f, infiniteRepeatable(tween(2200, delayMillis = 600, easing = LinearEasing)), label = "sweep")
    val press = remember { MutableInteractionSource() }
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (enabled) tone else tone.copy(alpha = 0.45f))
                .drawBehind {
                    if (enabled) {
                        val x = size.width * sweep
                        drawRect(
                            Brush.linearGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.28f), Color.Transparent),
                                start = Offset(x - 120f, 0f), end = Offset(x + 120f, size.height),
                            ),
                        )
                    }
                }
                .clickable(interactionSource = press, indication = null, enabled = enabled && !busy, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            if (busy) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.5.dp, color = Color.White)
            else AnimatedContent(
                targetState = label,
                transitionSpec = { (slideInVertically { it / 2 } + fadeIn()) togetherWith (slideOutVertically { -it / 2 } + fadeOut()) },
                label = "ctaLabel",
            ) { text -> Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) }
        }
        if (sublabel != null) Text(sublabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}
