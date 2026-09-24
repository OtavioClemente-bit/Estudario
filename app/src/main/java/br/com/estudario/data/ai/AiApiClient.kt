package br.com.estudario.data.ai

import br.com.estudario.data.remote.AiAccessTokenProvider
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class AiSourceMetadata(
    val fileName: String,
    val mimeType: String,
    val sourceHash: String,
    val sourceBytes: Long,
    val objectPath: String? = null,
)

data class AiUploadTarget(
    val path: String,
    val signedUrl: String?,
)

data class AiCreateJob(
    val jobId: String,
    val status: AiJobStatus,
    val uploadTarget: AiUploadTarget,
    val sourceBound: Boolean,
)

data class AiPollingPolicy(
    val timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    val initialDelayMillis: Long = 1000,
    val maxDelayMillis: Long = 8000,
    val multiplier: Double = 2.0,
) {
    init {
        require(timeoutMillis > 0)
        require(initialDelayMillis > 0)
        require(maxDelayMillis >= initialDelayMillis)
        require(multiplier >= 1.0)
    }

    companion object {
        const val DEFAULT_TIMEOUT_MILLIS: Long = 120_000
    }
}

data class AiHttpRequest(
    val method: String,
    val path: String,
    val headers: Map<String, String>,
    val body: ByteArray? = null,
)

data class AiHttpResponse(
    val status: Int,
    val body: String = "",
    val headers: Map<String, String> = emptyMap(),
)

fun interface AiHttpTransport {
    suspend fun execute(request: AiHttpRequest): AiHttpResponse
}

interface AiApiClient {
    suspend fun createOrGetJob(idempotencyKey: String, source: AiSourceMetadata, sourceReady: Boolean): AiCreateJob

    suspend fun uploadSource(target: AiUploadTarget, source: PdfSource)

    suspend fun processJob(jobId: String): AiJobStatus

    suspend fun getJob(jobId: String, timeoutMillis: Long? = null): AiJob

    suspend fun awaitJob(
        jobId: String,
        policy: AiPollingPolicy = AiPollingPolicy(),
        sleeper: suspend (Long) -> Unit = { delay(it) },
        clockMillis: () -> Long = { System.currentTimeMillis() },
    ): AiJob
}

class AiAuthenticationRequiredException : IllegalStateException("Supabase authentication is required for AI jobs.")

class AiProcessTimeoutException(val jobId: String) : IllegalStateException("AI job processing timed out.")

class AiApiException(
    val code: String,
    val status: Int,
) : IllegalStateException("AI API request failed: $code")

class HttpAiApiClient(
    private val baseUrl: String,
    private val publishableKey: String,
    private val accessTokenProvider: AiAccessTokenProvider,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(baseUrl),
    private val httpTimeoutMillis: Long = DEFAULT_HTTP_TIMEOUT_MILLIS,
) : AiApiClient {
    init {
        require(httpTimeoutMillis > 0)
    }

    override suspend fun createOrGetJob(
        idempotencyKey: String,
        source: AiSourceMetadata,
        sourceReady: Boolean,
    ): AiCreateJob {
        val response = execute(
            buildRequest(
                method = "POST",
                path = FUNCTIONS_JOBS_PATH,
                headers = authHeaders(idempotencyKey),
                body = buildJsonObject {
                    put("feature", JsonPrimitive(AI_FEATURE))
                    put("source", buildJsonObject {
                        put("fileName", JsonPrimitive(source.fileName))
                        put("mimeType", JsonPrimitive(source.mimeType))
                        put("sourceHash", JsonPrimitive(source.sourceHash))
                        put("sourceBytes", JsonPrimitive(source.sourceBytes))
                        if (sourceReady) {
                            put("objectPath", JsonPrimitive(source.objectPath ?: error("Source path is required.")))
                            put("ready", JsonPrimitive(true))
                        }
                    })
                }.toString().toJsonBytes(),
                contentType = JSON_CONTENT_TYPE,
            ),
            acceptedStatuses = setOf(200, 201),
        )
        val body = response.body.parseJsonObject()
        val jobId = body.requiredString("jobId")
        val uploadPath = body.requiredString("uploadPath")
        return AiCreateJob(
            jobId = jobId,
            status = body.requiredStatus("status"),
            uploadTarget = AiUploadTarget(uploadPath, body.optionalString("uploadUrl")),
            sourceBound = body.optionalBoolean("sourceBound") ?: false,
        )
    }

    override suspend fun uploadSource(target: AiUploadTarget, source: PdfSource) {
        val path = target.signedUrl?.also(::validateSignedUploadUrl) ?: storageObjectPath(target.path)
        execute(
            buildRequest(
                method = "POST",
                path = path,
                headers = authHeaders().plus(
                    mapOf(
                        "Content-Type" to source.mimeType,
                        "Content-Length" to source.bytes.size.toString(),
                        "x-upsert" to "true",
                        "x-source-sha256" to source.sha256,
                    ),
                ),
                body = source.bytes,
                contentType = source.mimeType,
            ),
            acceptedStatuses = setOf(200, 201, 204),
        )
    }

    override suspend fun processJob(jobId: String): AiJobStatus {
        val response = execute(
            buildRequest(
                method = "POST",
                path = "$FUNCTIONS_JOBS_PATH/${encode(jobId)}/process",
                headers = authHeaders(),
                body = null,
                contentType = null,
            ),
            acceptedStatuses = setOf(202),
        )
        return response.body.parseJsonObject().requiredStatus("status")
    }

    override suspend fun getJob(jobId: String, timeoutMillis: Long?): AiJob {
        val response = execute(
            buildRequest(
                method = "GET",
                path = "$FUNCTIONS_JOBS_PATH/${encode(jobId)}",
                headers = authHeaders(),
            ),
            acceptedStatuses = setOf(200),
            timeoutMillis = timeoutMillis,
        )
        return response.body.toAiJob()
    }

    override suspend fun awaitJob(
        jobId: String,
        policy: AiPollingPolicy,
        sleeper: suspend (Long) -> Unit,
        clockMillis: () -> Long,
    ): AiJob {
        val startedAt = clockMillis()
        val deadline = startedAt + policy.timeoutMillis
        var delayMillis = policy.initialDelayMillis
        while (true) {
            val remainingBeforeRequest = deadline - clockMillis()
            if (remainingBeforeRequest <= 0) throw AiProcessTimeoutException(jobId)
            val job = getJob(jobId, remainingBeforeRequest.coerceAtMost(httpTimeoutMillis))
            if (clockMillis() >= deadline) throw AiProcessTimeoutException(jobId)
            if (job.status.isTerminal()) return job
            val remainingBeforeSleep = deadline - clockMillis()
            if (remainingBeforeSleep <= 0) throw AiProcessTimeoutException(jobId)
            sleeper(delayMillis.coerceAtMost(remainingBeforeSleep))
            delayMillis = (delayMillis * policy.multiplier).toLong().coerceAtMost(policy.maxDelayMillis)
        }
    }

    private suspend fun execute(
        request: AiHttpRequest,
        acceptedStatuses: Set<Int>,
        timeoutMillis: Long? = null,
    ): AiHttpResponse {
        if (baseUrl.isBlank() && request.path.startsWith("/")) {
            // Test doubles may exercise request construction without a project URL. The real
            // transport rejects an empty URL before any network call.
        }
        val response = try {
            withTimeout((timeoutMillis ?: httpTimeoutMillis).coerceAtMost(httpTimeoutMillis)) {
                transport.execute(request)
            }
        } catch (_: TimeoutCancellationException) {
            throw AiApiException("HTTP_TIMEOUT", 504)
        } catch (error: AiApiException) {
            throw error
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            throw AiApiException("NETWORK_UNAVAILABLE", 503)
        }
        if (response.status !in acceptedStatuses) throw AiApiException(response.errorCode(), response.status)
        return response
    }

    private fun authHeaders(idempotencyKey: String? = null): Map<String, String> {
        val accessToken = accessTokenProvider.accessToken() ?: throw AiAuthenticationRequiredException()
        return buildMap {
            put("Authorization", "Bearer $accessToken")
            if (publishableKey.isNotBlank()) put("apikey", publishableKey)
            put("Accept", JSON_CONTENT_TYPE)
            idempotencyKey?.let { put("Idempotency-Key", it) }
        }
    }

    private fun storageObjectPath(path: String): String = "/storage/v1/object/$STORAGE_BUCKET/${path.split('/').joinToString("/") { encode(it) }}"

    private fun validateSignedUploadUrl(value: String) {
        val signed = runCatching { URL(value) }.getOrNull()
        val base = runCatching { URL(baseUrl) }.getOrNull()
        if (signed == null || base == null || signed.protocol != base.protocol || signed.host != base.host || signed.port != base.port) {
            throw AiApiException("UPLOAD_TARGET_INVALID", 502)
        }
    }

    private fun encode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20")

    private fun String.toJsonBytes(): ByteArray = toByteArray(StandardCharsets.UTF_8)

    private fun buildRequest(
        method: String,
        path: String,
        headers: Map<String, String>,
        body: ByteArray? = null,
        contentType: String? = JSON_CONTENT_TYPE,
    ): AiHttpRequest = AiHttpRequest(
        method = method,
        path = path,
        headers = if (contentType == null) headers else headers + ("Content-Type" to contentType),
        body = body,
    )

    companion object {
        private const val AI_FEATURE = "SYLLABUS_GENERATION"
        private const val JSON_CONTENT_TYPE = "application/json; charset=utf-8"
        private const val FUNCTIONS_JOBS_PATH = "/functions/v1/ai-syllabus/jobs"
        private const val STORAGE_BUCKET = "ai-syllabus-sources"
    }
}

class UrlConnectionAiHttpTransport(
    private val baseUrl: String,
    private val connectTimeoutMillis: Int = 15_000,
    private val readTimeoutMillis: Int = 30_000,
) : AiHttpTransport {
    override suspend fun execute(request: AiHttpRequest): AiHttpResponse = withContext(Dispatchers.IO) {
        val target = if (request.path.startsWith("http://") || request.path.startsWith("https://")) {
            request.path
        } else {
            require(baseUrl.isNotBlank()) { "Supabase project URL is required for AI API calls." }
            "${baseUrl.trimEnd('/')}${request.path}"
        }
        val connection = (URL(target).openConnection() as HttpURLConnection).apply {
            requestMethod = request.method
            connectTimeout = connectTimeoutMillis
            readTimeout = readTimeoutMillis
            doInput = true
            request.headers.forEach { (name, value) -> setRequestProperty(name, value) }
            if (request.body != null) {
                doOutput = true
                setFixedLengthStreamingMode(request.body.size)
            }
        }
        try {
            request.body?.let { connection.outputStream.use { output -> output.write(it) } }
            val stream = if (connection.responseCode >= 400) connection.errorStream else connection.inputStream
            val body = stream?.let { BufferedInputStream(it).use(::readBody) } ?: ByteArray(0)
            AiHttpResponse(
                status = connection.responseCode,
                body = body.toString(StandardCharsets.UTF_8),
                headers = connection.headerFields
                    .filterKeys { it != null }
                    .mapKeys { it.key!! }
                    .mapValues { it.value.joinToString(",") },
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun readBody(input: BufferedInputStream): ByteArray {
        val output = ByteArrayOutputStream()
        input.copyTo(output)
        return output.toByteArray()
    }
}

private val jsonForApiJobs = Json { ignoreUnknownKeys = true; explicitNulls = true }

private fun String.parseJsonObject(): JsonObject = jsonForApiJobs.parseToJsonElement(this).jsonObject

private fun JsonObject.requiredString(name: String): String = optionalString(name)
    ?: throw AiApiException("INVALID_RESPONSE", 502)

private fun JsonObject.optionalString(name: String): String? = this[name]?.jsonPrimitive?.contentOrNull
    ?.takeIf(String::isNotBlank)

private fun JsonObject.optionalBoolean(name: String): Boolean? = this[name]?.jsonPrimitive?.booleanOrNull

private fun JsonObject.requiredStatus(name: String): AiJobStatus = runCatching {
    AiJobStatus.valueOf(requiredString(name).uppercase(Locale.US))
}.getOrElse { throw AiApiException("INVALID_RESPONSE", 502) }

private fun AiHttpResponse.errorCode(): String = runCatching {
    body.parseJsonObject()["error"]?.jsonObject?.get("code")?.jsonPrimitive?.content
}.getOrNull()?.takeIf(String::isNotBlank) ?: "AI_API_ERROR"

private fun String.toAiJob(): AiJob {
    return try {
        EstudarioContractJson.decodeJob(this).also { job ->
            if (job.feature != AiFeature.SYLLABUS_GENERATION) throw ContractValidationException("job.feature: unsupported for syllabus client")
        }
    } catch (_: Throwable) {
        throw AiApiException("INVALID_RESPONSE", 502)
    }
}

private const val DEFAULT_HTTP_TIMEOUT_MILLIS = 30_000L

private fun AiJobStatus.isTerminal(): Boolean = this in setOf(
    AiJobStatus.SUCCEEDED,
    AiJobStatus.FAILED,
    AiJobStatus.EXPIRED,
    AiJobStatus.CANCELLED,
)
