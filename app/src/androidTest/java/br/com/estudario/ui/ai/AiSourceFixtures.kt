package br.com.estudario.ui.ai

import br.com.estudario.data.ai.PreparedSyllabusSource

internal fun preparedFixture(target: AiReviewTarget) = PreparedSyllabusSource(
    requireNotNull(target.sourceUri), target.sourceName ?: "edital.pdf", "private/fixture.pdf",
    "a".repeat(64), 100L, emptyList(), target.entryId,
)
