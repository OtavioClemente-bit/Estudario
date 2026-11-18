package br.com.estudario.domain.performance

/** Um tópico do edital para a rosca "Seu edital". */
data class CoverageTopic(val id: Long, val parentTopicId: Long?, val studied: Boolean)

/** A rosca "Seu edital", igual no app e no site: só os tópicos-folha (os que não têm subtópicos) contam. */
data class EditalCoverage(val total: Int, val studied: Int, val withMaterial: Int) {
    val remaining: Int get() = (total - studied - withMaterial).coerceAtLeast(0)
    val studiedPercent: Int get() = if (total <= 0) 0 else studied * 100 / total
    val withMaterialPercent: Int get() = if (total <= 0) 0 else withMaterial * 100 / total

    companion object {
        /** [topics] já filtrados pelas matérias do edital; [theoryTopicIds] = tópicos que têm material. */
        fun of(topics: List<CoverageTopic>, theoryTopicIds: Set<Long>): EditalCoverage {
            val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
            val leaves = topics.filter { it.id !in parents }
            val studied = leaves.count { it.studied }
            val withMaterial = leaves.count { !it.studied && it.id in theoryTopicIds }
            return EditalCoverage(leaves.size, studied, withMaterial)
        }
    }
}
