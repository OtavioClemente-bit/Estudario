package br.com.estudario.data.remote

import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.local.RemoteSyllabusSyncState as LocalRemoteSyllabusSyncState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private fun gatewayAck(row: RemoteSyllabusSyncEntity, hash: String) = RemoteSyllabusSyncAcknowledgement(
    remoteSyllabusId = row.remoteSyllabusId,
    jobId = row.jobId,
    payloadHash = hash,
    state = RemoteSyllabusSyncState.SYNCED,
    attemptCount = 1,
    nextAttemptAt = null,
    safeError = null,
    createdAt = "2026-09-24T00:00:00Z",
    updatedAt = "2026-09-24T00:00:00Z",
    attemptToken = null,
)

class RemoteSyllabusSyncWorkerTest {
    @Test
    fun marksSyncedOnlyAfterMatchingServerAcknowledgement() = runBlocking {
        val row = row()
        val gateway = FakeGateway(row, RemoteSyllabusSyncAcknowledgement(
            remoteSyllabusId = "remote-1",
            jobId = "job-1",
            payloadHash = row.payloadHash,
            state = RemoteSyllabusSyncState.SYNCED,
            attemptCount = 1,
            nextAttemptAt = null,
            safeError = null,
            createdAt = "2026-09-24T00:00:00Z",
            updatedAt = "2026-09-24T00:00:00Z",
            attemptToken = "token-1",
        ))

        val result = RemoteSyllabusSyncRunner(gateway, now = { 100L }, tokenFactory = { "token-1" }).run()

        assertEquals(1, result.synced)
        assertEquals(1, gateway.synced.size)
        assertTrue(gateway.failed.isEmpty())
        assertEquals("job-1", gateway.synced.single().jobId)
    }

    @Test
    fun failedRemoteAttemptRemainsAvailableAndIsRetryableWithoutDeletingLocalPayload() = runBlocking {
        val row = row().copy(payloadJson = "canonical-official-estudo")
        val gateway = FakeGateway(row, failure = PrivateSyllabusApiException("NETWORK_ERROR", 503))

        val result = RemoteSyllabusSyncRunner(gateway, now = { 100L }, tokenFactory = { "token-1" }).run()

        assertEquals(0, result.synced)
        assertEquals(1, result.failed)
        assertTrue(result.shouldRetry)
        assertEquals("NETWORK_ERROR", gateway.failed.single().error)
        assertEquals("canonical-official-estudo", gateway.failed.single().row.payloadJson)
    }

    @Test
    fun aDifferentAcknowledgedHashCannotMarkTheMutationSynced() = runBlocking {
        val row = row()
        val gateway = FakeGateway(row, acknowledgement = gatewayAck(row, "b".repeat(64)))

        RemoteSyllabusSyncRunner(gateway, now = { 100L }, tokenFactory = { "token-1" }).run()

        assertTrue(gateway.synced.isEmpty())
        assertEquals(1, gateway.failed.size)
        assertEquals("SYNC_FAILED", gateway.failed.single().error)
    }

    @Test
    fun anAcknowledgementWithoutIdentityCannotMarkTheMutationSynced() = runBlocking {
        val row = row()
        val gateway = FakeGateway(row, acknowledgement = gatewayAck(row, row.payloadHash).copy(remoteSyllabusId = null))

        RemoteSyllabusSyncRunner(gateway, now = { 100L }, tokenFactory = { "token-1" }).run()

        assertTrue(gateway.synced.isEmpty())
        assertEquals(1, gateway.failed.size)
        assertEquals("SYNC_FAILED", gateway.failed.single().error)
    }

    private fun row() = RemoteSyllabusSyncEntity(
        operation = RemoteSyllabusSyncOperation.UPSERT,
        localSyllabusId = 41L,
        remoteSyllabusId = "remote-1",
        jobId = "job-1",
        payloadHash = "a".repeat(64),
        payloadJson = "canonical-payload",
        state = LocalRemoteSyllabusSyncState.PENDING,
        nextAttemptAt = 0L,
    )

    private class FakeGateway(
        private val source: RemoteSyllabusSyncEntity,
        private val acknowledgement: RemoteSyllabusSyncAcknowledgement = gatewayAck(source, source.payloadHash),
        private val failure: Throwable? = null,
    ) : RemoteSyllabusSyncGateway {
        val synced = mutableListOf<RemoteSyllabusSyncEntity>()
        val failed = mutableListOf<FailedRow>()
        private var claimed: RemoteSyllabusSyncEntity? = null

        override suspend fun pending(now: Long): List<RemoteSyllabusSyncEntity> = listOf(source)
        override suspend fun failed(now: Long): List<RemoteSyllabusSyncEntity> = emptyList()
        override suspend fun requeue(row: RemoteSyllabusSyncEntity, attemptToken: String, now: Long, updatedAt: Long): Boolean = false
        override suspend fun claim(row: RemoteSyllabusSyncEntity, attemptToken: String, nextAttemptAt: Long, updatedAt: Long): Boolean {
            claimed = row.copy(attemptToken = attemptToken, attemptCount = row.attemptCount + 1)
            return true
        }
        override suspend fun expectedRemoteSyllabusId(row: RemoteSyllabusSyncEntity): String = row.remoteSyllabusId ?: "remote-1"
        override suspend fun sync(row: RemoteSyllabusSyncEntity): RemoteSyllabusSyncAcknowledgement {
            failure?.let { throw it }
            return acknowledgement
        }
        override suspend fun markSynced(row: RemoteSyllabusSyncEntity, remoteSyllabusId: String, attemptToken: String, updatedAt: Long): Boolean {
            synced += row
            return true
        }
        override suspend fun markFailed(row: RemoteSyllabusSyncEntity, attemptToken: String, error: String, nextAttemptAt: Long, updatedAt: Long): Boolean {
            failed += FailedRow(row, error)
            return true
        }
    }

    private data class FailedRow(val row: RemoteSyllabusSyncEntity, val error: String)
}
