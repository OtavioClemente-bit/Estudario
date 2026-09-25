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
        !authenticated -> "Entre para usar a IA"
        !betaAccess || reasonCode == "BETA_DISABLED" -> "Beta indisponível para esta conta"
        !featureEnabled -> "Recurso desativado"
        canUse -> "Disponível"
        reasonCode == "QUOTA_EXHAUSTED" -> "Cota utilizada"
        reasonCode == "QUOTA_RESERVED" -> "Geração em andamento"
        else -> "IA indisponível agora"
    }
    val copy = when {
        quota == null -> "Cota não disponível"
        feature == AiFeature.SYLLABUS_GENERATION && canUse && quota.remaining > 0 -> "1 geração disponível no Beta"
        feature == AiFeature.SYLLABUS_GENERATION && reasonCode == "QUOTA_EXHAUSTED" -> "Geração do Beta utilizada"
        feature == AiFeature.SYLLABUS_GENERATION && reasonCode == "QUOTA_RESERVED" -> "Geração do Beta em andamento"
        feature == AiFeature.SYLLABUS_GENERATION -> "Geração do Beta indisponível"
        else -> {
            val base = "${quota.remaining} de ${quota.limit} disponíveis"
            val reset = quota.resetAt?.let { runCatching {
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("America/Sao_Paulo")).format(Instant.parse(it))
            }.getOrNull() }
            if (reset == null) base else "$base · Renova em $reset"
        }
    }
    return AiFeatureDisplay(availability, copy, canUse)
}
