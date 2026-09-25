package br.com.estudario.ui.ai

import br.com.estudario.data.ai.AiAccess
import br.com.estudario.data.ai.AiFeature
import br.com.estudario.data.ai.AiQuota
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiAccessViewModelTest {
    private fun access(feature: AiFeature, remaining: Int, reason: String? = null, reset: String? = null) = AiAccess(
        authenticated = true, betaAccess = reason != "BETA_ACCESS_REQUIRED", feature = feature,
        featureEnabled = reason != "FEATURE_DISABLED",
        quota = AiQuota(feature, 1, 1 - remaining, 0, remaining, "1970-01-01", 1 - remaining, reset),
        canUse = reason == null, reasonCode = reason,
    )

    @Test fun rendersAvailableAndExhaustedSyllabusWithoutEpochRenewal() = runBlocking {
        val responses = mutableMapOf(
            AiFeature.SYLLABUS_GENERATION to AiAccessLoadResult.Available(access(AiFeature.SYLLABUS_GENERATION, 1)),
        )
        val vm = AiAccessViewModel(object : AiAccessRepository {
            override suspend fun loadAccess(feature: AiFeature) = responses[feature] ?: AiAccessLoadResult.Failed(AiAccessFailure(AiAccessFailureCode.OFFLINE))
        })
        vm.refresh()
        assertEquals("1 geração disponível no Beta", vm.state.value.items[AiFeature.SYLLABUS_GENERATION]?.quotaCopy)
        responses[AiFeature.SYLLABUS_GENERATION] = AiAccessLoadResult.Available(access(AiFeature.SYLLABUS_GENERATION, 0, "QUOTA_EXHAUSTED"))
        vm.refresh()
        assertEquals("Geração do Beta utilizada", vm.state.value.items[AiFeature.SYLLABUS_GENERATION]?.quotaCopy)
        assertFalse(vm.state.value.items[AiFeature.SYLLABUS_GENERATION]!!.canUse)
        assertFalse(vm.state.value.items[AiFeature.SYLLABUS_GENERATION]!!.quotaCopy.contains("1970"))
    }

    @Test fun showsServerFlagsAndOnlyServerResetDate() = runBlocking {
        val vm = AiAccessViewModel(object : AiAccessRepository {
            override suspend fun loadAccess(feature: AiFeature) = AiAccessLoadResult.Available(when (feature) {
                AiFeature.SYLLABUS_GENERATION -> access(feature, 1, "FEATURE_DISABLED")
                AiFeature.PLAN_GENERATION -> access(feature, 1, "BETA_ACCESS_REQUIRED")
                AiFeature.CONTENT_GENERATION -> access(feature, 0, "QUOTA_EXHAUSTED", "2026-09-24T03:00:00Z")
            })
        })
        vm.refresh()
        assertEquals("Recurso desativado", vm.state.value.items[AiFeature.SYLLABUS_GENERATION]?.availabilityCopy)
        assertFalse(vm.state.value.items[AiFeature.SYLLABUS_GENERATION]!!.quotaCopy.contains("geração disponível"))
        assertEquals("Beta indisponível para esta conta", vm.state.value.items[AiFeature.PLAN_GENERATION]?.availabilityCopy)
        assertTrue(vm.state.value.items[AiFeature.CONTENT_GENERATION]!!.quotaCopy.contains("24/09/2026"))
        assertFalse(vm.state.value.items[AiFeature.PLAN_GENERATION]!!.quotaCopy.contains("1970"))
    }

    @Test fun doesNotAdvertiseRemainingSyllabusQuotaWhenServerDeniesAccess() = runBlocking {
        val deniedReasons = listOf("FEATURE_DISABLED", "BETA_ACCESS_REQUIRED")
        for (reason in deniedReasons) {
            val display = access(AiFeature.SYLLABUS_GENERATION, 1, reason).toDisplay()
            assertFalse(display.canUse)
            assertFalse(display.quotaCopy.contains("geração disponível"))
        }
    }

    @Test fun offlineAndProviderFailureKeepLocalFallbackVisible() = runBlocking {
        val vm = AiAccessViewModel(object : AiAccessRepository {
            override suspend fun loadAccess(feature: AiFeature) = AiAccessLoadResult.Failed(AiAccessFailure(AiAccessFailureCode.OFFLINE))
        })
        vm.refresh()
        assertTrue(vm.state.value.localFallbackAvailable)
        assertEquals("Acesso online indisponível", vm.state.value.items[AiFeature.SYLLABUS_GENERATION]?.availabilityCopy)
    }

    @Test fun httpUnavailableProviderFailureKeepsLocalImportFallbackVisible() = runBlocking {
        val vm = AiAccessViewModel(object : AiAccessRepository {
            override suspend fun loadAccess(feature: AiFeature) =
                AiAccessLoadResult.Failed(AiAccessFailure(AiAccessFailureCode.HTTP_UNAVAILABLE, status = 503))
        })
        vm.refresh()
        assertTrue(vm.state.value.localFallbackAvailable)
        assertEquals("Acesso online indisponível", vm.state.value.items[AiFeature.SYLLABUS_GENERATION]?.availabilityCopy)
        assertFalse(vm.state.value.items[AiFeature.SYLLABUS_GENERATION]!!.canUse)
    }
}
