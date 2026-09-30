package br.com.estudario.data.remote

import br.com.estudario.BuildConfig
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

enum class ReportKind { THEORY, SUMMARY, FLASHCARD, QUESTION, TIP, OTHER }

enum class ReportReason(val label: String) {
    WRONG_FACT("Informação errada"),
    OUTDATED_LAW("Lei desatualizada"),
    WRONG_ANSWER("Gabarito errado"),
    GENERIC("Muito genérico"),
    OFF_TOPIC("Fora do tópico"),
    OTHER("Outro"),
}

data class ContentReport(
    val kind: ReportKind,
    val reason: ReportReason,
    val excerpt: String,
    val comment: String = "",
    val competition: String? = null,
    val topic: String? = null,
)

sealed interface ReportResult {
    data object Sent : ReportResult
    data object SignedOut : ReportResult
    data object TooMany : ReportResult
    data object Failed : ReportResult
}

/** Grava o reporte direto na tabela content_reports; o RLS só deixa cada pessoa inserir o seu. */
class ContentReportClient(
    private val config: SupabaseClientConfig,
    private val authRepository: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
) {
    suspend fun send(report: ContentReport): ReportResult {
        if (!config.isConfigured) return ReportResult.Failed
        val token = authRepository.accessToken() ?: return ReportResult.SignedOut
        val body = JSONObject()
            .put("kind", report.kind.name)
            .put("reason", report.reason.name)
            .put("excerpt", report.excerpt.trim().take(4_000).ifBlank { "(sem trecho)" })
            .put("comment", report.comment.trim().take(1_000))
            .put("competition", report.competition?.take(200) ?: JSONObject.NULL)
            .put("topic", report.topic?.take(500) ?: JSONObject.NULL)
            .put("app_version", BuildConfig.VERSION_NAME.take(40))
            .toString()
        return try {
            val response = withTimeout(20_000) {
                transport.execute(
                    AiHttpRequest(
                        method = "POST",
                        path = "/rest/v1/content_reports",
                        headers = mapOf(
                            "Authorization" to "Bearer $token",
                            "apikey" to config.publishableKey,
                            "Content-Type" to "application/json; charset=utf-8",
                            "Prefer" to "return=minimal",
                        ),
                        body = body.toByteArray(Charsets.UTF_8),
                    ),
                )
            }
            when {
                response.status in 200..299 -> ReportResult.Sent
                response.status == 401 -> ReportResult.SignedOut
                response.body.contains("CONTENT_REPORT_RATE_LIMITED") -> ReportResult.TooMany
                else -> ReportResult.Failed
            }
        } catch (error: CancellationException) {
            if (error is kotlinx.coroutines.TimeoutCancellationException) ReportResult.Failed else throw error
        } catch (_: Throwable) {
            ReportResult.Failed
        }
    }
}
