package br.com.estudario.web

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout
import kotlin.js.json

/**
 * Verificação anti-robô (Cloudflare Turnstile, modo invisível) pedida antes de cada geração com IA
 * no site. O servidor confere o token; sem ele a geração é recusada.
 */
object Turnstile {
    private const val SITE_KEY = "0x4AAAAAAFPVzGdmJH0HKXNU"
    private const val SCRIPT = "https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit"
    private const val BLOCKED = "Não deu para carregar a verificação de segurança do Cloudflare. Se você usa bloqueador de anúncios ou a proteção do navegador está no máximo, libere challenges.cloudflare.com para este site e tente de novo."

    private suspend fun ensureLoaded() {
        if (window.asDynamic().turnstile != null) return
        val loaded = CompletableDeferred<Unit>()
        // Uma tentativa anterior que falhou deixa o <script> morto na página: sem tirar, nenhuma outra
        // tentativa carregaria de novo até recarregar a página.
        val existing = document.querySelector("script[data-turnstile]")
        if (existing == null) {
            val script = document.createElement("script")
            script.setAttribute("src", SCRIPT)
            script.setAttribute("async", "")
            script.setAttribute("data-turnstile", "")
            script.addEventListener("load", { loaded.complete(Unit) })
            script.addEventListener("error", { loaded.completeExceptionally(IllegalStateException(BLOCKED)) })
            document.head?.appendChild(script)
        } else {
            existing.addEventListener("load", { loaded.complete(Unit) })
        }
        try {
            withTimeout(20_000) {
                while (window.asDynamic().turnstile == null) {
                    if (loaded.isCompleted && loaded.getCompletionExceptionOrNull() != null) loaded.await()
                    kotlinx.coroutines.delay(100)
                }
            }
        } catch (error: Throwable) {
            document.querySelector("script[data-turnstile]")?.remove()
            throw IllegalStateException(BLOCKED)
        }
    }

    /** Um token novo (cada token vale para um pedido só). */
    suspend fun token(): String {
        ensureLoaded()
        val holder = document.createElement("div")
        holder.setAttribute("style", "position:fixed;bottom:12px;right:12px;z-index:60")
        document.body?.appendChild(holder)
        val result = CompletableDeferred<String>()
        val turnstile = window.asDynamic().turnstile
        val widgetId = turnstile.render(
            holder,
            json(
                "sitekey" to SITE_KEY,
                "size" to "flexible",
                "appearance" to "interaction-only",
                "callback" to { token: String -> result.complete(token) },
                "error-callback" to { _: dynamic -> result.completeExceptionally(IllegalStateException("A verificação de segurança falhou. Recarregue a página e tente de novo.")) },
                "expired-callback" to { result.completeExceptionally(IllegalStateException("A verificação de segurança expirou. Tente de novo.")) },
            ),
        )
        return try {
            withTimeout(60_000) { result.await() }
        } catch (error: kotlinx.coroutines.TimeoutCancellationException) {
            throw IllegalStateException("A verificação de segurança demorou demais. Tente de novo.")
        } finally {
            runCatching { turnstile.remove(widgetId) }
            holder.remove()
        }
    }
}
