package br.com.estudario.data.ai

import android.content.Context
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.StandardIntegrityException
import com.google.android.play.core.integrity.StandardIntegrityManager.PrepareIntegrityTokenRequest
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenProvider
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenRequest
import com.google.android.play.core.integrity.model.StandardIntegrityErrorCode
import java.security.MessageDigest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await

/** Fornece o token do Play Integrity para um pedido. Null quando não foi possível obter. */
fun interface IntegrityTokenSource {
    suspend fun token(requestHash: String): String?
}

/**
 * Mesmo cálculo do servidor (supabase/functions/_shared/play-integrity.ts): amarra o token a este
 * pedido, então um token capturado não serve para outro pedido.
 */
object IntegrityRequestHash {
    fun of(feature: String, idempotencyKey: String, sourceHash: String?): String {
        val input = "estudario|$feature|$idempotencyKey|${sourceHash.orEmpty().lowercase()}"
        return MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}

/**
 * API padrão do Play Integrity. O provedor é preparado uma vez (aquecimento) e reaproveitado;
 * se o Google invalidar o provedor, ele é preparado de novo. Nenhum veredito é avaliado aqui:
 * quem decide é o servidor.
 */
class PlayIntegrityTokenSource(
    context: Context,
    private val cloudProjectNumber: Long,
) : IntegrityTokenSource {
    private val manager = IntegrityManagerFactory.createStandard(context.applicationContext)
    private val mutex = Mutex()
    private var provider: StandardIntegrityTokenProvider? = null

    override suspend fun token(requestHash: String): String? {
        if (cloudProjectNumber <= 0L) return null
        return try {
            request(requestHash)
        } catch (error: StandardIntegrityException) {
            if (error.errorCode != StandardIntegrityErrorCode.INTEGRITY_TOKEN_PROVIDER_INVALID) return null
            mutex.withLock { provider = null }
            runCatching { request(requestHash) }.getOrNull()
        } catch (error: CancellationException) {
            throw error
        } catch (_: Throwable) {
            null
        }
    }

    private suspend fun request(requestHash: String): String {
        val current = mutex.withLock {
            provider ?: manager.prepareIntegrityToken(
                PrepareIntegrityTokenRequest.builder().setCloudProjectNumber(cloudProjectNumber).build(),
            ).await().also { provider = it }
        }
        return current.request(StandardIntegrityTokenRequest.builder().setRequestHash(requestHash).build()).await().token()
    }
}
