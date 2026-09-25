package br.com.estudario.data.remote

import androidx.room.withTransaction
import br.com.estudario.data.ai.AiAuthenticationRequiredException
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpResponse
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.local.RemoteSyllabusSyncEntity
import br.com.estudario.data.local.RemoteSyllabusSyncOperation
import br.com.estudario.data.transfer.EstudoPackageService
import br.com.estudario.data.transfer.ImportMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.UUID

open class PrivateSyllabusApiException(val code: String, val status: Int) : IllegalStateException("Private syllabus API failed: $code")

class PrivateSyllabusNotFoundException(remoteSyllabusId: String) : PrivateSyllabusApiException("NOT_FOUND", 404)

class PrivateSyllabusCanonicalPayloadMissingException(remoteSyllabusId: String) :
    IllegalStateException("Private syllabus $remoteSyllabusId has no canonical package snapshot.")

interface PrivateSyllabusRemoteApi {
    suspend fun list(): List<PrivateSyllabus>
    suspend fun get(remoteSyllabusId: String): PrivateSyllabus?
    suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement
    suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement
}

/** Narrow boundary for library operations; keeps UI tests independent of HTTP/auth. */
interface PrivateSyllabusLibraryRepository {
    suspend fun listRemote(): List<PrivateSyllabus>
    suspend fun download(remoteSyllabusId: String): Long
    suspend fun deleteRemote(remoteSyllabusId: String)
}

class HttpPrivateSyllabusRemoteApi(
    private val baseUrl: String,
    private val publishableKey: String,
    private val accessTokenProvider: AiAccessTokenProvider,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransportForPrivateSyllabi(baseUrl),
) : PrivateSyllabusRemoteApi {
    override suspend fun list(): List<PrivateSyllabus> {
        val response = execute("GET", "", emptyMap(), null)
        return try {
            Json.parseToJsonElement(response.body).jsonArray.map { RemoteSyllabusContractJson.decodePrivateSyllabus(it.toString()) }
        } catch (_: Throwable) {
            throw PrivateSyllabusApiException("INVALID_RESPONSE", 502)
        }
    }

    override suspend fun get(remoteSyllabusId: String): PrivateSyllabus? {
        val response = try {
            execute("GET", "/${encode(remoteSyllabusId)}", emptyMap(), null)
        } catch (error: PrivateSyllabusApiException) {
            if (error.status == 404) return null
            throw error
        }
        return try {
            RemoteSyllabusContractJson.decodePrivateSyllabus(response.body)
        } catch (_: Throwable) {
            throw PrivateSyllabusApiException("INVALID_RESPONSE", 502)
        }
    }

    override suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement {
        val response = execute(
            method = "PUT",
            suffix = "/${encode(syllabus.remoteSyllabusId)}",
            extraHeaders = mapOf("Idempotency-Key" to mutationId, "X-Payload-Hash" to payloadHash),
            body = RemoteSyllabusContractJson.encodePrivateSyllabus(syllabus),
        )
        return decodeAcknowledgement(response.body)
    }

    override suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement = decodeAcknowledgement(
        execute(
            method = "DELETE",
            suffix = "/${encode(remoteSyllabusId)}",
            extraHeaders = mapOf("Idempotency-Key" to mutationId, "X-Payload-Hash" to payloadHash),
            body = null,
        ).body,
    )

    private suspend fun execute(method: String, suffix: String, extraHeaders: Map<String, String>, body: String?): AiHttpResponse {
        val token = accessTokenProvider.accessToken() ?: throw AiAuthenticationRequiredException()
        val path = "/functions/v1/user-syllabi$suffix"
        val request = AiHttpRequest(
            method = method,
            path = path,
            headers = buildMap {
                put("Authorization", "Bearer $token")
                if (publishableKey.isNotBlank()) put("apikey", publishableKey)
                put("Accept", "application/json")
                if (body != null) put("Content-Type", "application/json; charset=utf-8")
                putAll(extraHeaders)
            },
            body = body?.toByteArray(StandardCharsets.UTF_8),
        )
        val response = try {
            transport.execute(request)
        } catch (_: Throwable) {
            throw PrivateSyllabusApiException("NETWORK_ERROR", 503)
        }
        if (response.status !in 200..299) throw PrivateSyllabusApiException(errorCode(response), response.status)
        return response
    }

    private fun decodeAcknowledgement(body: String): RemoteSyllabusSyncAcknowledgement = try {
        RemoteSyllabusContractJson.decodeSyncAcknowledgement(body)
    } catch (_: Throwable) {
        throw PrivateSyllabusApiException("INVALID_RESPONSE", 502)
    }

    private fun errorCode(response: AiHttpResponse): String = runCatching {
        Json.parseToJsonElement(response.body).jsonObject["error"]?.jsonObject?.get("code")?.toString()?.trim('"')
    }.getOrNull()?.takeIf { it.isNotBlank() } ?: if (response.status == 404) "NOT_FOUND" else "REMOTE_ERROR"

    private fun encode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20")
}

class PrivateSyllabusRepository(
    private val database: AppDatabase,
    private val api: PrivateSyllabusRemoteApi,
) : PrivateSyllabusLibraryRepository {
    private val dao = database.dao()

    override suspend fun listRemote(): List<PrivateSyllabus> = api.list()

    suspend fun getRemote(remoteSyllabusId: String): PrivateSyllabus? = api.get(remoteSyllabusId)

    /** Explicitly deletes only the private account copy; local Room data is untouched. */
    override suspend fun deleteRemote(remoteSyllabusId: String) {
        require(remoteSyllabusId.isNotBlank()) { "Remote syllabus identity is required for deletion." }
        val payloadHash = MessageDigest.getInstance("SHA-256")
            .digest("delete:$remoteSyllabusId".toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        val acknowledgement = try {
            api.delete(
                remoteSyllabusId = remoteSyllabusId,
                mutationId = "library-delete-${UUID.randomUUID()}",
                payloadHash = payloadHash,
            )
        } catch (error: PrivateSyllabusApiException) {
            if (error.code == "NOT_FOUND") return
            throw error
        }
        check(acknowledgement.state == RemoteSyllabusSyncState.SYNCED && acknowledgement.remoteSyllabusId == remoteSyllabusId) {
            "The remote account deletion was not acknowledged."
        }
    }

    suspend fun syncOutbox(row: RemoteSyllabusSyncEntity): RemoteSyllabusSyncAcknowledgement {
        val mutationId = row.jobId?.takeIf { it.isNotBlank() } ?: "local-mutation-${row.id}"
        return when (row.operation) {
            RemoteSyllabusSyncOperation.UPSERT -> {
                val competition = dao.competitionById(row.localSyllabusId)
                    ?: throw IllegalStateException("Local syllabus was deleted before synchronization.")
                require(row.payloadJson.isNotBlank()) { "Canonical syllabus snapshot is required for synchronization." }
                val remote = RemoteSyllabusMapper.fromLocal(competition, row.payloadJson, row.payloadHash)
                check(row.remoteSyllabusId == null || row.remoteSyllabusId == remote.remoteSyllabusId) {
                    "Outbox and local syllabus identities disagree."
                }
                api.upsert(remote, mutationId, row.payloadHash)
            }
            RemoteSyllabusSyncOperation.DELETE -> api.delete(
                remoteSyllabusId = row.remoteSyllabusId ?: throw IllegalArgumentException("Remote syllabus identity is required for deletion."),
                mutationId = mutationId,
                payloadHash = row.payloadHash,
            )
        }
    }

    suspend fun expectedRemoteSyllabusId(row: RemoteSyllabusSyncEntity): String = when (row.operation) {
        RemoteSyllabusSyncOperation.DELETE -> row.remoteSyllabusId
            ?: throw IllegalStateException("Remote syllabus identity is required for deletion.")
        RemoteSyllabusSyncOperation.UPSERT -> {
            val competition = dao.competitionById(row.localSyllabusId)
                ?: throw IllegalStateException("Local syllabus was deleted before synchronization.")
            require(row.payloadJson.isNotBlank()) { "Canonical syllabus snapshot is required for synchronization." }
            RemoteSyllabusMapper.fromLocal(competition, row.payloadJson, row.payloadHash).remoteSyllabusId
        }
    }

    /** Downloads without AI and associates the same remote identity with the restored local row. */
    override suspend fun download(remoteSyllabusId: String): Long = download(remoteSyllabusId, replaceExisting = false)

    suspend fun download(remoteSyllabusId: String, replaceExisting: Boolean): Long {
        val remote = api.get(remoteSyllabusId) ?: throw PrivateSyllabusNotFoundException(remoteSyllabusId)
        val canonicalPayload = (remote.metadata["canonicalPayload"] as? JsonPrimitive)?.contentOrNull
        if (canonicalPayload.isNullOrBlank() || canonicalPayload == "null") {
            throw PrivateSyllabusCanonicalPayloadMissingException(remote.remoteSyllabusId)
        }
        val packageJson = RemoteSyllabusMapper.toOfficialPackage(remote)
        return database.withTransaction {
            val existing = dao.competitionByRemoteSyllabusId(remote.remoteSyllabusId)
            if (existing != null && !replaceExisting && dao.subjectsFor(existing.id).isNotEmpty()) {
                throw IllegalStateException("The local syllabus already has content; replacement must be explicit.")
            }
            if (existing != null && replaceExisting) dao.deleteSubjectsForCompetition(existing.id)
            val importResult = EstudoPackageService(database).importInTransaction(packageJson, ImportMode.SKIP, existing?.id)
            val restored = dao.competitionById(importResult.competitionId)
                ?: throw IllegalStateException("Restored syllabus was not found after import.")
            dao.updateCompetition(restored.copy(remoteSyllabusId = remote.remoteSyllabusId))
            importResult.competitionId
        }
    }
}

private class UrlConnectionAiHttpTransportForPrivateSyllabi(private val baseUrl: String) : AiHttpTransport {
    private val delegate = br.com.estudario.data.ai.UrlConnectionAiHttpTransport(baseUrl)
    override suspend fun execute(request: AiHttpRequest): AiHttpResponse = delegate.execute(request)
}
