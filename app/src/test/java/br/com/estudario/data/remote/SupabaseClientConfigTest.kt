package br.com.estudario.data.remote

import java.util.Base64
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class SupabaseClientConfigTest {
    @Test
    fun emptyValuesKeepTheConfigurationGateClosed() {
        val config = SupabaseClientConfig.from("", "")

        assertFalse(config.isConfigured)
    }

    @Test
    fun configuredClientUsesOnlyAValidHttpsProjectUrlAndPublishableKey() {
        val config = SupabaseClientConfig.from(
            projectUrl = "https://project.example.invalid",
            publishableKey = "public-client-key",
        )

        assertTrue(config.isConfigured)
    }

    @Test
    fun partialConfigurationIsRejectedWithoutRevealingAValue() {
        assertThrows(SupabaseConfigurationException::class.java) {
            SupabaseClientConfig.from("https://project.example.invalid", "")
        }
    }

    @Test
    fun serviceRoleJwtIsRejectedEvenWhenItsPayloadIsEncoded() {
        val serviceRoleJwt = fakeJwt("{\"role\":\"service_role\"}")

        assertThrows(SupabaseConfigurationException::class.java) {
            SupabaseClientConfig.from("https://project.example.invalid", serviceRoleJwt)
        }
    }

    @Test
    fun openAiLookingKeyIsRejected() {
        assertThrows(SupabaseConfigurationException::class.java) {
            SupabaseClientConfig.from("https://project.example.invalid", "sk-test-placeholder")
        }
    }

    @Test
    fun supabaseSecretKeyShapeIsRejected() {
        assertThrows(SupabaseConfigurationException::class.java) {
            SupabaseClientConfig.from("https://project.example.invalid", "sb_secret_test-placeholder")
        }
    }

    @Test
    fun nonHttpsRemoteUrlIsRejected() {
        assertThrows(SupabaseConfigurationException::class.java) {
            SupabaseClientConfig.from("http://project.example.invalid", "public-client-key")
        }
    }

    private fun fakeJwt(payload: String): String {
        val encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.toByteArray())
        return "header.$encoded.signature"
    }
}
