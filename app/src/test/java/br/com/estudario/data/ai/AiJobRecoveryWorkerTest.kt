package br.com.estudario.data.ai

import androidx.work.NetworkType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiJobRecoveryWorkerTest {
    @Test
    fun recoveryUsesConnectedNetworkAndStopsAfterBoundedAttempts() {
        assertEquals(NetworkType.CONNECTED, AiJobRecoveryWorker.recoveryConstraints().requiredNetworkType)
        assertTrue(AiJobRecoveryWorker.shouldRetry(0))
        assertTrue(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS - 2))
        assertFalse(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS - 1))
        assertFalse(AiJobRecoveryWorker.shouldRetry(AiJobRecoveryWorker.MAX_ATTEMPTS))
    }
}
