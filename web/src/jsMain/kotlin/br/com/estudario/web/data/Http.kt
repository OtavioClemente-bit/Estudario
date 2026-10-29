package br.com.estudario.web.data

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.js.json

class HttpResponse(val status: Int, val body: String) {
    val ok: Boolean get() = status in 200..299
}

class HttpException(val status: Int, val body: String) : Exception("HTTP $status")

/**
 * fetch() do navegador com corpo de texto; erros de rede viram status 0.
 * [timeoutMillis]: desiste depois desse tempo (também status 0, corpo "timeout"). Sem ele, uma conexão
 * que trava no meio deixava quem chamou esperando para sempre (o site parado em "Salvando…").
 */
suspend fun httpRequest(
    method: String,
    url: String,
    headers: Map<String, String> = emptyMap(),
    body: String? = null,
    /** Corpo em bytes, enviado exatamente como está (o tamanho e a assinatura batem com ele). */
    bytes: ByteArray? = null,
    timeoutMillis: Int? = null,
): HttpResponse {
    val init = RequestInit(
        method = method,
        headers = json(*headers.map { it.key to it.value }.toTypedArray()),
        body = bytes?.unsafeCast<org.khronos.webgl.Int8Array>() ?: body ?: undefined,
    )
    var timedOut = false
    var timer: Int? = null
    if (timeoutMillis != null) {
        val controller: dynamic = js("new AbortController()")
        init.asDynamic().signal = controller.signal
        timer = window.setTimeout({ timedOut = true; controller.abort() }, timeoutMillis)
    }
    return try {
        val response = window.fetch(url, init).await()
        HttpResponse(response.status.toInt(), response.text().await())
    } catch (error: Throwable) {
        HttpResponse(0, if (timedOut) "timeout" else error.message ?: "network")
    } finally {
        timer?.let { window.clearTimeout(it) }
    }
}
