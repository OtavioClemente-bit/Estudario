package br.com.estudario.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.estudario.data.ai.AiAccess
import br.com.estudario.data.ai.AiFeature
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AiFeatureDisplay(
    val availabilityCopy: String,
    val quotaCopy: String,
    val canUse: Boolean,
)

data class AiAccessUiState(
    val items: Map<AiFeature, AiFeatureDisplay> = emptyMap(),
    val localFallbackAvailable: Boolean = true,
)

class AiAccessViewModel(private val repository: AiAccessRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(AiAccessUiState())
    val state: StateFlow<AiAccessUiState> = mutableState

    suspend fun refresh() {
        val items = AiFeature.entries.associateWith { feature ->
            when (val result = repository.loadAccess(feature)) {
                is AiAccessLoadResult.Available -> result.access.toDisplay()
                is AiAccessLoadResult.Failed -> AiFeatureDisplay("Acesso online indisponível", "", false)
            }
        }
        mutableState.value = AiAccessUiState(items)
    }

    class Factory(private val repository: AiAccessRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AiAccessViewModel(repository) as T
    }
}

fun AiAccess.toDisplay(): AiFeatureDisplay {
    val availability = when {
        !authenticated -> "Entre para gerar com o Estudário"
        !betaAccess || reasonCode == "BETA_DISABLED" -> "Beta indisponível para esta conta"
        !featureEnabled -> "Recurso desativado"
        canUse -> "Disponível"
        reasonCode == "QUOTA_EXHAUSTED" -> "Cota utilizada"
        reasonCode == "QUOTA_RESERVED" -> "Geração em andamento"
        else -> "Geração indisponível agora"
    }
    val copy = when {
        quota == null -> "Cota não disponível"
        reasonCode == "QUOTA_RESERVED" -> "Geração em andamento"
        // Acesso negado por conta ou recurso: não anunciar saldo que a pessoa não pode usar.
        !canUse && reasonCode != "QUOTA_EXHAUSTED" -> "Cota indisponível"
        else -> {
            val base = "Restam ${quota.remaining} de ${quota.limit}"
            val reset = quota.resetAt?.let { runCatching {
                DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("America/Sao_Paulo")).format(Instant.parse(it))
            }.getOrNull() }
            if (reset == null) "$base · saldo único" else "$base neste mês · renova em $reset"
        }
    }
    return AiFeatureDisplay(availability, copy, canUse)
}
