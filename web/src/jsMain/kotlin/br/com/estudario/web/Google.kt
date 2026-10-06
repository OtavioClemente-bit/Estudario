package br.com.estudario.web

import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.js.json

/**
 * "Fazer login com o Google" (Google Identity Services). O botão oficial é desenhado dentro do
 * elemento indicado e devolve um ID token, trocado por uma sessão no Supabase.
 */
object GoogleSignIn {
    private var initialized = false

    val available: Boolean get() = WebConfig.GOOGLE_CLIENT_ID.isNotBlank()

    fun renderButton(elementId: String, onToken: (String) -> Unit, attempt: Int = 0) {
        val google = window.asDynamic().google
        val element = document.getElementById(elementId) ?: return
        if (google == null || google.accounts == null) {
            // O script do Google carrega de forma assíncrona; tenta de novo por alguns segundos.
            if (attempt < 50) window.setTimeout({ renderButton(elementId, onToken, attempt + 1) }, 100)
            return
        }
        if (!initialized) {
            google.accounts.id.initialize(
                json(
                    "client_id" to WebConfig.GOOGLE_CLIENT_ID,
                    "callback" to { response: dynamic -> onToken(response.credential as String) },
                    "ux_mode" to "popup",
                    "auto_select" to false,
                    "itp_support" to true,
                ),
            )
            initialized = true
        }
        val width = element.clientWidth.takeIf { it > 0 } ?: 360
        google.accounts.id.renderButton(
            element,
            json("theme" to "outline", "size" to "large", "shape" to "pill", "text" to "continue_with", "locale" to "pt-BR", "width" to width.coerceAtMost(400)),
        )
    }
}
