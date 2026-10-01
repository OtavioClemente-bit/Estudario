package br.com.estudario.data.ai

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.security.MessageDigest

/**
 * Identificador do aparelho para o limite grátis por celular, somando todas as contas.
 *
 * Vem do ANDROID_ID, que no Android 8+ é próprio deste app e desta chave de assinatura: continua o
 * mesmo ao desinstalar e instalar de novo e só muda se o celular for restaurado de fábrica. O
 * servidor recebe só um SHA-256 dele, nunca o valor original.
 */
object DeviceIdentity {
    @Volatile var hash: String? = null
        private set

    @SuppressLint("HardwareIds")
    fun init(context: Context) {
        val androidId = runCatching { Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) }.getOrNull()
        if (androidId.isNullOrBlank()) return
        hash = MessageDigest.getInstance("SHA-256")
            .digest("estudario-device|$androidId".toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}

/** Mesma frase em todas as telas que falam com a IA. */
const val DEVICE_QUOTA_MESSAGE = "As gerações grátis deste celular já foram usadas, mesmo trocando de conta. Assine um plano em Perfil > Planos e uso para continuar."

/** Explicit refusals; 429 rate limits and transport errors must remain recoverable. */
internal val AI_QUOTA_REJECTION_CODES = setOf("DEVICE_QUOTA_EXHAUSTED", "QUOTA_EXHAUSTED")
