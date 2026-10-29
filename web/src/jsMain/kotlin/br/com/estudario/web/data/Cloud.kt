package br.com.estudario.web.data

import br.com.estudario.text.Sha256
import br.com.estudario.text.toHex
import br.com.estudario.web.WebConfig
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.js.Date

data class CloudHead(val revision: Long, val objectPath: String?, val sha256: String?, val deviceLabel: String?, val updatedAt: String?)

class CloudConflictException(val head: CloudHead) : Exception("sync_conflict")
class SignedOutException : Exception("signed_out")

/**
 * A mesma sincronização do app Android, do lado do navegador: lê a revisão atual da conta, baixa a
 * foto, e publica fotos novas com a revisão esperada (o servidor recusa se outro aparelho publicou
 * antes). Ver supabase/migrations/20261006120000_user_sync.sql.
 */
object Cloud {
    private const val BUCKET = "user-sync"
    /** Tempo limite das chamadas pequenas e das que levam a foto inteira (alguns MB numa conexão lenta). */
    private const val SHORT = 30_000
    private const val LONG = 180_000
    private const val DEVICE_KEY = "estudario.device"

    val deviceId: String
        get() = localStorage.getItem(DEVICE_KEY) ?: ("web-" + Date.now().toLong().toString(36) + "-" + (kotlin.random.Random.nextLong() and 0xffffffL).toString(36))
            .also { localStorage.setItem(DEVICE_KEY, it) }

    private val deviceLabel: String
        get() {
            val agent = window.navigator.userAgent
            val browser = when {
                "Edg/" in agent -> "Edge"
                "Chrome/" in agent -> "Chrome"
                "Firefox/" in agent -> "Firefox"
                "Safari/" in agent -> "Safari"
                else -> "navegador"
            }
            return "App web ($browser)"
        }

    private suspend fun headers(extra: Map<String, String> = emptyMap()): Map<String, String> {
        val token = Auth.accessToken() ?: throw SignedOutException()
        return mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY) + extra
    }

    suspend fun head(): CloudHead {
        val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}/rest/v1/sync_heads?select=*", headers(mapOf("Accept" to "application/json")), timeoutMillis = SHORT)
        if (response.status == 401) throw SignedOutException()
        if (!response.ok) throw HttpException(response.status, response.body)
        val row = (snapshotJson.parseToJsonElement(response.body) as? JsonArray)?.firstOrNull() as? JsonObject
            ?: return CloudHead(0, null, null, null, null)
        return CloudHead(
            revision = (row["revision"] as? JsonPrimitive)?.longOrNull ?: 0,
            objectPath = row.str("object_path"),
            sha256 = row.str("sha256"),
            deviceLabel = row.str("device_label"),
            updatedAt = row.str("updated_at"),
        )
    }

    /** Baixa e confere a foto atual. */
    suspend fun download(head: CloudHead): String {
        val path = head.objectPath ?: error("A conta ainda não tem dados.")
        val response = httpRequest("GET", "${WebConfig.SUPABASE_URL}/storage/v1/object/authenticated/$BUCKET/$path", headers(), timeoutMillis = LONG)
        if (response.status == 401) throw SignedOutException()
        if (!response.ok) throw HttpException(response.status, response.body)
        val sha = sha256(response.body)
        check(sha == head.sha256) { "Os dados vieram incompletos. Recarregue a página." }
        return response.body
    }

    /** Publica [text] como a nova versão; devolve a nova revisão. */
    suspend fun upload(text: String, expectedRevision: Long): Long {
        val userId = Auth.session?.userId ?: throw SignedOutException()
        // Os mesmos bytes são enviados, medidos e assinados. Antes o texto ia como texto e o tamanho
        // era calculado à parte; um caractere partido (emoji cortado ao meio) deixava os dois
        // diferentes e o servidor recusava todo envio ("size_mismatch"), para sempre.
        // O servidor aceita no máximo 25 arquivos por conta. A limpeza antes só rodava depois de um
        // envio aceito: com envios recusados acumulando, a pasta enchia e travava tudo (o celular
        // inclusive). Agora limpa antes de enviar, sempre guardando a foto vigente.
        runCatching { prune(currentPath = "", keep = 3) }
        val bytes = text.encodeToByteArray()
        val sha = Sha256.digest(bytes).toHex()
        val path = "$userId/${Date.now().toLong()}-${sha.take(12)}.json"
        val put = httpRequest(
            "POST",
            "${WebConfig.SUPABASE_URL}/storage/v1/object/$BUCKET/$path",
            headers(mapOf("Content-Type" to "application/json", "x-upsert" to "false")),
            bytes = bytes,
            timeoutMillis = LONG,
        )
        if (put.status == 401) throw SignedOutException()
        if (!put.ok) throw HttpException(put.status, put.body)
        val body = buildJsonObject {
            put("p_expected_revision", expectedRevision)
            put("p_object_path", path)
            put("p_sha256", sha)
            put("p_size_bytes", bytes.size.toLong())
            put("p_device_id", deviceId)
            put("p_device_label", deviceLabel)
        }.toString()
        val commit = httpRequest("POST", "${WebConfig.SUPABASE_URL}/rest/v1/rpc/sync_commit", headers(mapOf("Content-Type" to "application/json")), body, timeoutMillis = SHORT)
        if (commit.body.contains("sync_conflict")) throw CloudConflictException(head())
        if (commit.status == 401) throw SignedOutException()
        if (!commit.ok) {
            // Recusa sem a palavra "sync_conflict" (formato do erro mudou, proxy etc.): se a conta
            // está em outra revisão, é conflito de verdade e o site junta as mudanças em vez de
            // ficar "sem conexão" repetindo o mesmo envio velho.
            val current = runCatching { head() }.getOrNull()
            if (current != null && current.revision != expectedRevision) throw CloudConflictException(current)
            throw HttpException(commit.status, commit.body)
        }
        val revision = (snapshotJson.parseToJsonElement(commit.body).jsonObject["revision"] as JsonPrimitive).longOrNull ?: (expectedRevision + 1)
        runCatching { prune(path) }
        return revision
    }

    /**
     * Apaga as fotos antigas da conta, deixando a atual e as [keep] mais recentes antes dela. Cada
     * envio cria um arquivo novo; sem isso a pasta da conta só cresce. Melhor esforço: falhar aqui
     * não falha a sincronização.
     */
    suspend fun prune(currentPath: String, keep: Int = 4) {
        val userId = Auth.session?.userId ?: return
        // Sem saber qual é a foto vigente, não apaga nada.
        val currentHead = runCatching { head() }.getOrNull() ?: return
        val headPath = currentHead.objectPath
        val listBody = buildJsonObject {
            put("prefix", userId)
            put("limit", 200)
            put("sortBy", buildJsonObject { put("column", "name"); put("order", "desc") })
        }.toString()
        val listed = httpRequest("POST", "${WebConfig.SUPABASE_URL}/storage/v1/object/list/$BUCKET", headers(mapOf("Content-Type" to "application/json")), listBody, timeoutMillis = SHORT)
        if (!listed.ok) return
        val names = (runCatching { snapshotJson.parseToJsonElement(listed.body) as? JsonArray }.getOrNull() ?: return)
            .mapNotNull { (it as? JsonObject)?.str("name") }
            .map { "$userId/$it" }
            // Nunca apaga a foto vigente da conta, mesmo se envios recusados forem mais novos que ela.
            .filter { it != currentPath && it != headPath }
            .sortedDescending() // o nome começa pelo horário do envio
        names.drop(keep).forEach { path ->
            httpRequest("DELETE", "${WebConfig.SUPABASE_URL}/storage/v1/object/$BUCKET/$path", headers(), timeoutMillis = SHORT)
        }
    }

    fun sha256(text: String): String = Sha256.digest(text.encodeToByteArray()).toHex()
}
