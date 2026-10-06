package br.com.estudario.web.data

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.js.json

class HttpResponse(val status: Int, val body: String) {
    val ok: Boolean get() = status in 200..299
}

class HttpException(val status: Int, val body: String) : Exception("HTTP $status")

/** fetch() do navegador com corpo de texto; erros de rede viram status 0. */
suspend fun httpRequest(
    method: String,
    url: String,
    headers: Map<String, String> = emptyMap(),
    body: String? = null,
): HttpResponse {
    val init = RequestInit(
        method = method,
        headers = json(*headers.map { it.key to it.value }.toTypedArray()),
        body = body ?: undefined,
    )
    return try {
        val response = window.fetch(url, init).await()
        HttpResponse(response.status.toInt(), response.text().await())
    } catch (error: Throwable) {
        HttpResponse(0, error.message ?: "network")
    }
}
