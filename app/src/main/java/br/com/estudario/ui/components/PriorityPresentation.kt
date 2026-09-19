package br.com.estudario.ui.components

import br.com.estudario.data.local.PriorityEvidenceCodec
import br.com.estudario.domain.PriorityAssessment
import br.com.estudario.domain.PriorityEvidence
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PriorityResolver
import br.com.estudario.domain.PrioritySource

data class PriorityEditorState(
    val assessment: PriorityAssessment,
    val hasAssessedPriority: Boolean,
    val userOverride: PriorityLevel?,
    val effectivePriority: PriorityLevel,
)

object PriorityPresentation {
    fun label(level: PriorityLevel): String = when (level) {
        PriorityLevel.VERY_HIGH -> "Muito alta"
        PriorityLevel.HIGH -> "Alta"
        PriorityLevel.MEDIUM -> "Média"
        PriorityLevel.LOW -> "Baixa"
        PriorityLevel.VERY_LOW -> "Muito baixa"
    }

    fun sourceLabel(source: PrioritySource, hasOverride: Boolean): String =
        if (hasOverride) "Definida por você" else "Sugerida automaticamente"

    fun state(
        score: Int,
        source: PrioritySource,
        confidence: Float,
        rationale: String?,
        evidenceJson: String,
        hasAssessedPriority: Boolean,
        userOverride: PriorityLevel?,
        parent: PriorityLevel?,
    ): PriorityEditorState {
        val assessment = PriorityAssessment(
            score = PriorityResolver.normalizeScore(score),
            source = source,
            confidence = PriorityResolver.normalizeConfidence(confidence),
            rationale = rationale,
            evidence = PriorityEvidenceCodec.decode(evidenceJson),
        )
        return PriorityEditorState(assessment, hasAssessedPriority, userOverride, PriorityResolver.effectivePriority(br.com.estudario.domain.PriorityState(hasAssessedPriority, assessment, userOverride), parent))
    }

    fun confidenceLabel(confidence: Float): String = "${(confidence * 100).toInt()}% de confiança"

    fun evidenceLabel(evidence: List<PriorityEvidence>): String =
        evidence.joinToString("\n") { item -> "• ${item.description}" }.ifBlank { "Nenhuma evidência declarada; trate como estimativa neutra." }
}
