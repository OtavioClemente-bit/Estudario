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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

open class PrivateSyllabusApiException(val code: String, val status: Int) : IllegalStateException("Private syllabus API failed: $code")

class PrivateSyllabusNotFoundException(remoteSyllabusId: String) : PrivateSyllabusApiException("NOT_FOUND", 404)

interface PrivateSyllabusRemoteApi {
    suspend fun list(): List<PrivateSyllabus>
    suspend fun get(remoteSyllabusId: String): PrivateSyllabus?
    suspend fun upsert(syllabus: PrivateSyllabus, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement
    suspend fun delete(remoteSyllabusId: String, mutationId: String, payloadHash: String): RemoteSyllabusSyncAcknowledgement
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
) {
    private val dao = database.dao()

    suspend fun listRemote(): List<PrivateSyllabus> = api.list()

    suspend fun getRemote(remoteSyllabusId: String): PrivateSyllabus? = api.get(remoteSyllabusId)

    suspend fun syncOutbox(row: RemoteSyllabusSyncEntity): RemoteSyllabusSyncAcknowledgement {
        val mutationId = row.jobId?.takeIf { it.isNotBlank() } ?: "local-mutation-${row.id}"
        return when (row.operation) {
            RemoteSyllabusSyncOperation.UPSERT -> {
                val competition = dao.competitionsOnce().firstOrNull { it.id == row.localSyllabusId }
                    ?: throw IllegalStateException("Local syllabus was deleted before synchronization.")
                require(row.payloadJson.isNotBlank()) { "Canonical syllabus snapshot is required for synchronization." }
                api.upsert(RemoteSyllabusMapper.fromLocal(competition, row.payloadJson, row.payloadHash), mutationId, row.payloadHash)
            }
            RemoteSyllabusSyncOperation.DELETE -> api.delete(
                remoteSyllabusId = row.remoteSyllabusId ?: throw IllegalArgumentException("Remote syllabus identity is required for deletion."),
                mutationId = mutationId,
                payloadHash = row.payloadHash,
            )
        }
    }

    /** Downloads without AI and associates the same remote identity with the restored local row. */
    suspend fun download(remoteSyllabusId: String, replaceExisting: Boolean = false): Long {
        val remote = api.get(remoteSyllabusId) ?: throw PrivateSyllabusNotFoundException(remoteSyllabusId)
        val packageJson = RemoteSyllabusMapper.toOfficialPackage(remote)
        return database.withTransaction {
            val existing = dao.competitionByRemoteSyllabusId(remote.remoteSyllabusId)
            if (existing != null && !replaceExisting && dao.subjectsFor(existing.id).isNotEmpty()) {
                throw IllegalStateException("The local syllabus already has content; replacement must be explicit.")
            }
            if (existing != null && replaceExisting) dao.deleteSubjectsForCompetition(existing.id)
            EstudoPackageService(database).importInTransaction(packageJson, ImportMode.SKIP, existing?.id)
            val restored = dao.competitionByRemoteSyllabusId(remote.remoteSyllabusId)
                ?: dao.competitionsOnce().firstOrNull {
                    it.externalId == remote.metadata["localSyllabusExternalId"]?.toString()?.trim('"') ||
                        it.externalId == remote.remoteSyllabusId
                }
                ?: throw IllegalStateException("Restored syllabus was not found after import.")
            dao.updateCompetition(restored.copy(remoteSyllabusId = remote.remoteSyllabusId))
            restored.id
        }
    }
}

private class UrlConnectionAiHttpTransportForPrivateSyllabi(private val baseUrl: String) : AiHttpTransport {
    private val delegate = br.com.estudario.data.ai.UrlConnectionAiHttpTransport(baseUrl)
    override suspend fun execute(request: AiHttpRequest): AiHttpResponse = delegate.execute(request)
}
