package br.com.estudario.data.ai

import br.com.estudario.data.remote.AiAccessTokenProvider
import java.time.Instant
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiApiClientTest {
    @Test
    fun processAccepts202AndSendsSupabaseJwtAndIdempotencyKey() = runTest {
        val transport = FakeAiHttpTransport(
            AiHttpResponse(202, "{\"jobId\":\"job-1\",\"status\":\"PROCESSING\"}"),
        )
        val client = HttpAiApiClient(
            baseUrl = "",
            publishableKey = "",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
        )

        val result = client.processJob("job-1")

        assertEquals(AiJobStatus.PROCESSING, result)
        assertEquals("Bearer supabase-jwt", transport.requests.single().headers["Authorization"])
        assertTrue(transport.requests.single().path.endsWith("/job-1/process"))
        assertFalse(transport.requests.single().path.contains("openai", ignoreCase = true))
        assertFalse(transport.requests.single().headers.values.any { it.contains("sk-") })
    }

    @Test
    fun pollsJobWithExponentialBackoffUntilSucceeded() = runTest {
        val transport = FakeAiHttpTransport(
            AiHttpResponse(200, jobJson("PROCESSING")),
            AiHttpResponse(200, jobJson("PROCESSING")),
            AiHttpResponse(200, succeededJobJson()),
        )
        val delays = mutableListOf<Long>()
        val client = HttpAiApiClient(
            baseUrl = "",
            publishableKey = "",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
        )

        val result = client.awaitJob(
            "job-1",
            policy = AiPollingPolicy(timeoutMillis = 30_000, initialDelayMillis = 1000, maxDelayMillis = 4000),
            sleeper = { delays += it },
        )

        assertEquals(AiJobStatus.SUCCEEDED, result.status)
        assertEquals(listOf(1000L, 2000L), delays)
        assertTrue(transport.requests.all { it.path == "/functions/v1/ai-syllabus/jobs/job-1" })
    }

    @Test
    fun rejectsSucceededJobWhenProposalOrMetadataIsInvalid() = runTest {
        val transport = FakeAiHttpTransport(AiHttpResponse(200, jobJson("SUCCEEDED")))
        val client = HttpAiApiClient(
            baseUrl = "",
            publishableKey = "",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
        )

        assertTrue(runCatching { client.awaitJob("job-1") }.exceptionOrNull() is AiApiException)
    }

    @Test
    fun boundsSleepAndRejectsTerminalResponseAfterPollingDeadline() = runTest {
        var calls = 0
        var now = 0L
        val transport = AiHttpTransport {
            calls += 1
            if (calls == 2) now = 100
            AiHttpResponse(200, if (calls == 1) jobJson("PROCESSING") else succeededJobJson())
        }
        val delays = mutableListOf<Long>()
        val client = HttpAiApiClient(
            baseUrl = "",
            publishableKey = "",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
        )

        assertTrue(runCatching {
            client.awaitJob(
                "job-1",
                policy = AiPollingPolicy(timeoutMillis = 100, initialDelayMillis = 500, maxDelayMillis = 500),
                sleeper = { delayMillis -> delays += delayMillis; now += delayMillis - 1 },
                clockMillis = { now },
            )
        }.exceptionOrNull() is AiProcessTimeoutException)
        assertEquals(listOf(100L), delays)
        assertEquals(2, calls)
    }

    @Test
    fun appliesGlobalTimeoutToEachHttpCall() = runTest {
        val transport = AiHttpTransport { delay(10_000); AiHttpResponse(202, "{}") }
        val client = HttpAiApiClient(
            baseUrl = "",
            publishableKey = "",
            accessTokenProvider = AiAccessTokenProvider { "supabase-jwt" },
            transport = transport,
            httpTimeoutMillis = 100,
        )

        val error = runCatching { client.processJob("job-1") }.exceptionOrNull() as AiApiException
        assertEquals("HTTP_TIMEOUT", error.code)
    }

    @Test
    fun preservesRateLimitCodeAndServerRetryAfter() = runTest {
        val transport = FakeAiHttpTransport(
            AiHttpResponse(
                status = 429,
                body = "{\"error\":{\"code\":\"AI_RATE_LIMIT_EXCEEDED\",\"retryAfterSeconds\":347}}",
                headers = mapOf("Retry-After" to "347"),
            ),
        )
        val client = HttpAiApiClient("", "", AiAccessTokenProvider { "jwt" }, transport)

        val error = runCatching { client.processJob("job-1") }.exceptionOrNull() as AiApiException

        assertEquals(429, error.status)
        assertEquals("AI_RATE_LIMIT_EXCEEDED", error.code)
        assertEquals(347L, error.retryAfterSeconds)
    }

    private fun jobJson(status: String): String = """
        {
          "jobId":"job-1","feature":"SYLLABUS_GENERATION","status":"$status",
          "schemaVersion":null,"promptVersion":null,"modelVersion":null,"proposal":null,
          "warnings":[],"errorCode":null,"errorMessage":null,
          "createdAt":"${Instant.parse("2026-09-24T10:00:00Z")}",
          "updatedAt":"2026-09-24T10:00:01Z","finishedAt":null,"providerExecutionStartedAt":null
        }
    """.trimIndent()

    private fun succeededJobJson(): String = """
        {
          "jobId":"job-1","feature":"SYLLABUS_GENERATION","status":"SUCCEEDED",
          "schemaVersion":1,"promptVersion":"syllabus-v1","modelVersion":"gpt-6-luna",
          "proposal":{"schemaVersion":1,"promptVersion":"syllabus-v1","modelVersion":"gpt-6-luna","documentTitle":"Edital",
            "subjects":[{"name":"Direito","position":0,"suggestedPriority":"NORMAL","topics":[{"name":"Constituição","position":0,"children":[],"sourcePages":[1]}],"sourcePages":[1]}],
            "warnings":[],"ambiguities":[]},
          "warnings":[],"errorCode":null,"errorMessage":null,
          "createdAt":"2026-09-24T10:00:00Z","updatedAt":"2026-09-24T10:00:01Z",
          "finishedAt":"2026-09-24T10:00:01Z","providerExecutionStartedAt":"2026-09-24T10:00:00Z"
        }
    """.trimIndent()
}

private data class FakeAiHttpTransport(
    private val responses: MutableList<AiHttpResponse>,
) : AiHttpTransport {
    val requests = mutableListOf<AiHttpRequest>()

    constructor(vararg responses: AiHttpResponse) : this(responses.toMutableList())

    override suspend fun execute(request: AiHttpRequest): AiHttpResponse {
        requests += request
        return responses.removeAt(0)
    }
}
