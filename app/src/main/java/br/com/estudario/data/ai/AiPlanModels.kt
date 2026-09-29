package br.com.estudario.data.ai

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Resposta de GET /functions/v1/ai-plan. Os limites vêm do servidor; o app só exibe. */
@Serializable
data class AiPlanSummary(
    val planTier: String,
    val planRenewsAt: String? = null,
    val betaAccess: Boolean = false,
    val plans: List<AiPlanCatalogEntry> = emptyList(),
    val usage: List<AiPlanUsage> = emptyList(),
)

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
data class AiAdInterestResult(
    val recorded: Boolean,
    val rewardGranted: Boolean = false,
    val tapsToday: Int = 0,
)

internal val aiPlanJson = Json { ignoreUnknownKeys = true; explicitNulls = false }
