package br.com.estudario.ui.ai

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiApiException
import br.com.estudario.data.ai.AiAuthenticationRequiredException
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiJobStatus
import br.com.estudario.data.ai.AiProcessTimeoutException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/** Onde a geração de texto está, do acesso até o resultado. */
sealed interface AiTextJobState {
    data object Checking : AiTextJobState
    data object NeedsLogin : AiTextJobState
    data class Unavailable(val message: String) : AiTextJobState
    /** Cota do dia usada; [resetLabel] diz quando volta ("amanhã às 00:00"). */
    data class QuotaUsed(val resetLabel: String?) : AiTextJobState
    data class Ready(val quotaLabel: String) : AiTextJobState
    data object Generating : AiTextJobState
    data class Failed(val message: String, val canRetry: Boolean) : AiTextJobState
    /** [context] é o que foi guardado junto com o pedido (ex.: a tabela de ids curtos do plano). */
    data class Done(val proposal: JsonObject, val context: String?) : AiTextJobState
}

/**
 * Motor das telas de conteúdo e de plano da IA do Estudário. Um job por alvo ([targetKey], ex.:
 * "content:42"): o id em andamento fica salvo, então fechar a tela ou o app não perde a geração
 * nem cria outra, ao voltar ele retoma o mesmo job.
 */
internal const val RATE_LIMITED = "OPENAI_RATE_LIMITED"
internal val RATE_LIMIT_DELAYS_SECONDS = listOf(20L, 45L, 90L, 180L)

class AiTextJobViewModel(
    private val app: EstudarioApplication,
    private val feature: AiFeature,
    private val targetKey: String,
) : ViewModel() {
    private val prefs = app.getSharedPreferences("ai_text_jobs", Context.MODE_PRIVATE)
    private val _state = MutableStateFlow<AiTextJobState>(AiTextJobState.Checking)
    val state: StateFlow<AiTextJobState> = _state.asStateFlow()
    private var work: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        work?.cancel()
        work = viewModelScope.launch {
            prefs.getString(targetKey, null)?.let { pending ->
                follow(pending)
                return@launch
            }
            _state.value = AiTextJobState.Checking
            _state.value = when (val result = app.aiAccessRepository.loadAccess(feature)) {
                is AiAccessLoadResult.Failed -> when (result.failure.code) {
                    AiAccessFailureCode.AUTH_REQUIRED, AiAccessFailureCode.AUTH_EXPIRED -> AiTextJobState.NeedsLogin
                    AiAccessFailureCode.OFFLINE, AiAccessFailureCode.TIMEOUT -> AiTextJobState.Unavailable("Sem conexão com o Estudário agora. Confira a internet e tente de novo.")
                    else -> AiTextJobState.Unavailable("A geração pelo Estudário está indisponível agora.")
                }
                is AiAccessLoadResult.Available -> {
                    val access = result.access
                    when {
                        !access.authenticated -> AiTextJobState.NeedsLogin
                        access.canUse -> AiTextJobState.Ready("1 geração disponível hoje")
                        access.reasonCode == "QUOTA_EXHAUSTED" || access.reasonCode == "QUOTA_RESERVED" ->
                            AiTextJobState.QuotaUsed(resetLabel(access.quota?.resetAt))
                        access.reasonCode == "BETA_ACCESS_REQUIRED" || access.reasonCode == "BETA_DISABLED" ->
                            AiTextJobState.Unavailable("Sua conta ainda não tem acesso à geração pelo Estudário.")
                        else -> AiTextJobState.Unavailable("Este recurso está temporariamente desativado.")
                    }
                }
            }
        }
    }

    fun start(input: JsonObject, context: String? = null) {
        if (_state.value is AiTextJobState.Generating) return
        work?.cancel()
        prefs.edit().putString("$targetKey:input", input.toString()).putInt("$targetKey:rateRetries", 0).apply()
        work = viewModelScope.launch { create(input, context) }
    }

    private suspend fun create(input: JsonObject, context: String?) {
            _state.value = AiTextJobState.Generating
            try {
                // Chave nova a cada tentativa: repetir a mesma devolveria o job antigo (inclusive
                // um que falhou). O id salvo é que impede gerar duas vezes para o mesmo alvo.
                val created = app.aiTextJobClient.create(feature, "${feature.name.lowercase()}-${UUID.randomUUID()}", input)
                prefs.edit().putString(targetKey, created.jobId).putString("$targetKey:ctx", context).apply()
                follow(created.jobId)
            } catch (error: Throwable) {
                if (error is kotlinx.coroutines.CancellationException) throw error
                _state.value = failureOf(error)
            }
    }

    /** O resultado já foi entregue à tela; esquece o job para a próxima vez começar do zero. */
    fun consumeResult() {
        prefs.edit().remove(targetKey).remove("$targetKey:ctx").remove("$targetKey:input").remove("$targetKey:rateRetries").apply()
    }

    private suspend fun follow(jobId: String) {
        _state.value = AiTextJobState.Generating
        try {
            val job = app.aiTextJobClient.await(jobId)
            if (job.status == AiJobStatus.SUCCEEDED && job.proposal != null) {
                _state.value = AiTextJobState.Done(job.proposal, prefs.getString("$targetKey:ctx", null))
            } else if (job.errorCode == RATE_LIMITED && retryAfterRateLimit()) {
                return
            } else {
                prefs.edit().remove(targetKey).remove("$targetKey:ctx").apply()
                _state.value = AiTextJobState.Failed(
                    "Não deu para entregar um resultado confiável desta vez. Sua geração do dia foi devolvida.",
                    canRetry = true,
                )
            }
        } catch (error: Throwable) {
            if (error is kotlinx.coroutines.CancellationException) throw error
            // Tempo esgotado não perde o job: ele continua no servidor e é retomado depois.
            _state.value = if (error is AiProcessTimeoutException) {
                AiTextJobState.Failed("Está demorando mais que o normal. A geração continua no servidor; volte aqui em alguns minutos.", canRetry = true)
            } else {
                failureOf(error)
            }
        }
    }

    /**
     * Muita gente gerando ao mesmo tempo: a OpenAI recusou por limite por minuto e a geração foi
     * devolvida. Espera um pouco (cada vez mais) e pede de novo, sem a pessoa fazer nada.
     */
    private suspend fun retryAfterRateLimit(): Boolean {
        val input = prefs.getString("$targetKey:input", null)?.let { runCatching { Json.parseToJsonElement(it) as JsonObject }.getOrNull() } ?: return false
        val attempt = prefs.getInt("$targetKey:rateRetries", 0)
        if (attempt >= RATE_LIMIT_DELAYS_SECONDS.size) return false
        val context = prefs.getString("$targetKey:ctx", null)
        prefs.edit().remove(targetKey).putInt("$targetKey:rateRetries", attempt + 1).apply()
        delay(RATE_LIMIT_DELAYS_SECONDS[attempt] * 1_000L)
        create(input, context)
        return true
    }

    private fun failureOf(error: Throwable): AiTextJobState = failureMessage(error).also {
        android.util.Log.w("AiTextJob", "falha em ${feature.name}: ${(error as? AiApiException)?.let { "${it.code}/${it.status}" } ?: error.javaClass.simpleName}")
    }

    private fun failureMessage(error: Throwable): AiTextJobState = when {
        error is AiAuthenticationRequiredException -> AiTextJobState.NeedsLogin
        error is AiApiException && error.code == "DEVICE_QUOTA_EXHAUSTED" -> AiTextJobState.Failed(br.com.estudario.data.ai.DEVICE_QUOTA_MESSAGE, canRetry = false)
        error is AiApiException && error.code.contains("QUOTA") -> AiTextJobState.QuotaUsed(null)
        error is AiApiException && error.status == 401 -> AiTextJobState.NeedsLogin
        error is AiApiException && (error.code == "NETWORK_UNAVAILABLE" || error.code == "HTTP_TIMEOUT") ->
            AiTextJobState.Failed("Sem conexão com o Estudário agora. Confira a internet e tente de novo.", canRetry = true)
        // O código vai junto: é o que permite descobrir a causa sem acesso ao aparelho.
        else -> AiTextJobState.Failed(
            "Não foi possível gerar agora. Tente novamente em instantes. (código: ${(error as? AiApiException)?.let { "${it.code} ${it.status}" } ?: error.javaClass.simpleName})",
            canRetry = true,
        )
    }

    private fun resetLabel(resetAt: String?): String? = resetAt?.let {
        runCatching {
            val local = Instant.parse(it).atZone(ZoneId.systemDefault())
            "Volta ${if (local.toLocalDate() == java.time.LocalDate.now().plusDays(1)) "amanhã" else local.format(DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR")))} às ${local.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }.getOrNull()
    }

    class Factory(
        private val app: EstudarioApplication,
        private val feature: AiFeature,
        private val targetKey: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AiTextJobViewModel(app, feature, targetKey) as T
    }
}
