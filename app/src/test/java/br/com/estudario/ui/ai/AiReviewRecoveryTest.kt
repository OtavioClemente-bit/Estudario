package br.com.estudario.ui.ai

import org.junit.Assert.assertEquals
import org.junit.Test

class AiReviewRecoveryTest {
    @Test
    fun ordinaryTimeoutKeepsJobAndIdempotencyKeyInsteadOfCreatingAnotherRequest() {
        val original = AiReviewRequestIdentity(requestId = "request-1", jobId = "job-1", idempotencyKey = "idem-1")

        val recovered = AiReviewRecovery.afterTimeout(original)

        assertEquals(original, recovered)
    }
}
