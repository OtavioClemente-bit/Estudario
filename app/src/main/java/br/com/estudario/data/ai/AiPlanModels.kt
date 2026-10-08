package br.com.estudario.data.ai

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Resposta de GET /functions/v1/ai-plan. Os limites e os preços vêm do servidor; o app só exibe. */
@Serializable
data class AiPlanSummary(
    val planTier: String,
    val planRenewsAt: String? = null,
    val betaAccess: Boolean = false,
    val plans: List<AiPlanCatalogEntry> = emptyList(),
    val usage: List<AiPlanUsage> = emptyList(),
    /** Nome, frase e preço de cada plano (tabela plan_catalog). */
    val pricing: List<AiPlanPricing> = emptyList(),
    val billing: AiBillingInfo = AiBillingInfo(),
) {
    fun pricingOf(tier: String): AiPlanPricing? = pricing.firstOrNull { it.planTier == tier }
}

@Serializable
data class AiPlanCatalogEntry(
    val planTier: String,
    val limits: List<AiPlanLimit> = emptyList(),
)

@Serializable
data class AiPlanLimit(
    val feature: String,
    val periodKind: String,
    val quotaLimit: Int,
    val maxPerRequest: Int? = null,
    val maxPerTopicMonth: Int? = null,
)

@Serializable
data class AiPlanUsage(
    val feature: String,
    val periodKind: String? = null,
    val limit: Int,
    val used: Int,
    val remaining: Int,
    val periodStart: String,
    val resetAt: String? = null,
)

@Serializable
data class AiPlanPricing(
    val planTier: String,
    val name: String = "",
    val tagline: String = "",
    val priceMonthCents: Int = 0,
    val priceYearCents: Int = 0,
    val playProductId: String? = null,
    val playBasePlanMonth: String? = null,
    val playBasePlanYear: String? = null,
    val featured: Boolean = false,
) {
    val isPaid: Boolean get() = priceMonthCents > 0
}

/** As assinaturas estão abertas no servidor (conta de serviço do Play configurada). */
@Serializable
data class AiBillingInfo(val playEnabled: Boolean = false)

@Serializable
data class AiAdInterestResult(
    val recorded: Boolean,
    val rewardGranted: Boolean = false,
    val tapsToday: Int = 0,
)

/** "R$ 24,90" a partir de centavos. */
fun formatBrl(cents: Int): String {
    val reais = cents / 100
    val centavos = cents % 100
    return "R$ ${"%,d".format(reais).replace(',', '.')},${"%02d".format(centavos)}"
}

internal val aiPlanJson = Json { ignoreUnknownKeys = true; explicitNulls = false }
