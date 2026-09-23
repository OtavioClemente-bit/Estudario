package br.com.estudario.data.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.Instant

data class GoogleUser(val id: String, val name: String, val email: String, val pictureUrl: String?)

data class DriveBackupFile(val id: String, val name: String, val modifiedAt: Long, val bytes: Long)

class GoogleBackupException(message: String) : Exception(message)

/**
 * Conversa direto com a API do Google por HTTPS, sem a biblioteca cliente inteira do Drive.
 *
 * O backup vai para a pasta privada do app no Drive (appDataFolder): ela não aparece no Meu Drive,
 * não ocupa a visão de arquivos da pessoa e só este app consegue ler. O token de acesso vem da
 * autorização feita na tela de perfil e vale cerca de uma hora, por isso cada ação pede o token
 * de novo (quando a permissão já foi dada, isso acontece sem mostrar nada).
 */
class GoogleDriveBackupService {

    suspend fun userInfo(token: String): GoogleUser = withContext(Dispatchers.IO) {
        val json = JSONObject(get("https://www.googleapis.com/oauth2/v3/userinfo", token))
        GoogleUser(
            id = json.optString("sub"),
            name = json.optString("name").ifBlank { json.optString("given_name") },
            email = json.optString("email"),
            pictureUrl = json.optString("picture").takeIf { it.isNotBlank() },
        )
    }

    /** Backups guardados na pasta privada do app, do mais novo para o mais antigo. */
    suspend fun listBackups(token: String): List<DriveBackupFile> = withContext(Dispatchers.IO) {
        val fields = URLEncoder.encode("files(id,name,modifiedTime,size)", "UTF-8")
        val url = "https://www.googleapis.com/drive/v3/files" +
            "?spaces=appDataFolder&pageSize=20&orderBy=modifiedTime%20desc&fields=$fields"
        val files = JSONObject(get(url, token)).optJSONArray("files") ?: return@withContext emptyList()
        (0 until files.length()).mapNotNull { index ->
            val item = files.optJSONObject(index) ?: return@mapNotNull null
            DriveBackupFile(
                id = item.optString("id"),
                name = item.optString("name"),
                modifiedAt = runCatching { Instant.parse(item.optString("modifiedTime")).toEpochMilli() }.getOrDefault(0L),
                bytes = item.optString("size").toLongOrNull() ?: 0L,
            )
        }
    }

    suspend fun upload(token: String, name: String, content: String): DriveBackupFile = withContext(Dispatchers.IO) {
        val boundary = "estudario" + System.currentTimeMillis()
        val metadata = JSONObject().put("name", name).put("parents", org.json.JSONArray().put("appDataFolder"))
        val body = buildString {
            append("--$boundary\r\n")
            append("Content-Type: application/json; charset=UTF-8\r\n\r\n")
            append(metadata.toString()).append("\r\n")
            append("--$boundary\r\n")
            append("Content-Type: application/json; charset=UTF-8\r\n\r\n")
            append(content).append("\r\n")
            append("--$boundary--\r\n")
        }.toByteArray(Charsets.UTF_8)

        val connection = open("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart&fields=id,name,modifiedTime,size", token, "POST")
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "multipart/related; boundary=$boundary")
        connection.setFixedLengthStreamingMode(body.size)
        connection.outputStream.use { it.write(body) }
        val json = JSONObject(readResponse(connection))
        DriveBackupFile(
            id = json.optString("id"),
            name = json.optString("name"),
            modifiedAt = runCatching { Instant.parse(json.optString("modifiedTime")).toEpochMilli() }.getOrDefault(System.currentTimeMillis()),
            bytes = json.optString("size").toLongOrNull() ?: body.size.toLong(),
        )
    }

    suspend fun download(token: String, fileId: String): String = withContext(Dispatchers.IO) {
        get("https://www.googleapis.com/drive/v3/files/$fileId?alt=media", token)
    }

    /** Baixa a foto da conta para dentro do app, para ela continuar valendo offline. */
    suspend fun downloadPhoto(url: String, target: File): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = 15_000
                readTimeout = 20_000
                instanceFollowRedirects = true
            }
            if (connection.responseCode !in 200..299) return@runCatching false
            connection.inputStream.use { input -> target.outputStream().use { input.copyTo(it) } }
            true
        }.getOrDefault(false)
    }

    private fun open(url: String, token: String, method: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15_000
            readTimeout = 60_000
            setRequestProperty("Authorization", "Bearer $token")
            setRequestProperty("Accept", "application/json")
        }

    private fun get(url: String, token: String): String = readResponse(open(url, token, "GET"))

    private fun readResponse(connection: HttpURLConnection): String {
        val code = connection.responseCode
        if (code in 200..299) return connection.inputStream.bufferedReader().use { it.readText() }
        val error = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        val detail = runCatching { JSONObject(error).getJSONObject("error").optString("message") }.getOrNull()
        throw GoogleBackupException(
            when (code) {
                401 -> "A autorização do Google expirou. Entre novamente."
                403 -> "O Google recusou o acesso ao Drive. Confirme que a Google Drive API está ativada no projeto. ${detail.orEmpty()}".trim()
                404 -> "Backup não encontrado na sua conta do Google."
                else -> detail?.takeIf { it.isNotBlank() } ?: "O Google respondeu com erro $code."
            },
        )
    }
}
