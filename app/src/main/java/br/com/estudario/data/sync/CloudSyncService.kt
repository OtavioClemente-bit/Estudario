package br.com.estudario.data.sync

import android.content.Context
import android.os.Build
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.local.AppDatabase
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import br.com.estudario.data.transfer.BackupService
import br.com.estudario.text.Sha256
import br.com.estudario.text.toHex
import java.io.File
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/** Estado da sincronização mostrado nas telas. */
sealed interface CloudSyncStatus {
    data object Idle : CloudSyncStatus
    data object Running : CloudSyncStatus
    data class Done(val at: Long, val action: String) : CloudSyncStatus
    /** Celular e nuvem mudaram desde a última vez: a pessoa escolhe. */
    data class Conflict(val remoteDevice: String?, val remoteUpdatedAt: String?) : CloudSyncStatus
    data object SignedOut : CloudSyncStatus
    data class Failed(val message: String) : CloudSyncStatus
}

enum class ConflictChoice { KEEP_THIS_DEVICE, KEEP_CLOUD }

class SyncConflictException(val remoteDevice: String?, val remoteUpdatedAt: String?) : Exception("sync_conflict")

/**
 * Sincroniza os dados de estudo com a conta (app Android ⇄ app web).
 *
 * A foto enviada é o próprio backup do app (mesmo formato, validado e restaurado pelo mesmo
 * código), sem o horário de exportação para que o hash só mude quando o conteúdo muda. Antes de
 * substituir os dados deste aparelho por uma foto da nuvem, a versão local é guardada em
 * files/sync-backups, então escolher errado num conflito nunca apaga nada de vez.
 */
class CloudSyncService(
    private val context: Context,
    private val database: AppDatabase,
    private val config: SupabaseClientConfig,
    private val auth: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl, readTimeoutMillis = 120_000),
) {
    private val prefs = context.getSharedPreferences("cloud_sync", Context.MODE_PRIVATE)
    private val backup = BackupService(database)
    private val mutex = Mutex()
    private val _status = MutableStateFlow<CloudSyncStatus>(CloudSyncStatus.Idle)
    val status: StateFlow<CloudSyncStatus> = _status.asStateFlow()

    /** Ligada por padrão: com conta, o celular e o app web são um só. Dá para pausar nos ajustes. */
    var enabled: Boolean
        get() = prefs.getBoolean(KEY_ENABLED, true)
        set(value) { prefs.edit().putBoolean(KEY_ENABLED, value).apply() }

    val lastSyncAt: Long get() = prefs.getLong(KEY_LAST_SYNC, 0L)

    /** Quando este aparelho mudou algo pela última vez (qualquer tabela), fora da própria restauração. */
    @Volatile private var restoring = false
    private val changeObserver = object : androidx.room.InvalidationTracker.Observer(allTables(database)) {
        override fun onInvalidated(tables: Set<String>) {
            if (restoring) return
            prefs.edit().putLong(KEY_LOCAL_CHANGED_AT, System.currentTimeMillis()).apply()
            // Mudou algo: sobe para a conta daqui a pouco (cada nova mudança adia, para não enviar a cada toque).
            if (enabled) CloudSyncWorker.syncSoon(context, delaySeconds = 20)
        }
    }
    init { runCatching { database.invalidationTracker.addObserver(changeObserver) } }

    private val deviceId: String
        get() = prefs.getString(KEY_DEVICE, null) ?: UUID.randomUUID().toString().also { prefs.edit().putString(KEY_DEVICE, it).apply() }

    private val deviceLabel: String get() = "${Build.MANUFACTURER} ${Build.MODEL}".trim().take(100)

    /** Sincroniza se estiver ligada e com conta. Seguro de chamar várias vezes (uma por vez). */
    suspend fun syncIfEnabled(): CloudSyncStatus = if (enabled) sync() else _status.value

    suspend fun sync(choice: ConflictChoice? = null): CloudSyncStatus = mutex.withLock {
        if (!config.isConfigured) return@withLock CloudSyncStatus.Failed("Sincronização indisponível nesta versão.").also { _status.value = it }
        if (auth.accessToken() == null && !auth.refreshSession()) return@withLock CloudSyncStatus.SignedOut.also { _status.value = it }
        _status.value = CloudSyncStatus.Running
        val result = runCatching { runSync(choice) }.getOrElse { error ->
            when (error) {
                is SyncConflictException -> CloudSyncStatus.Conflict(error.remoteDevice, error.remoteUpdatedAt)
                is SyncAuthException -> CloudSyncStatus.SignedOut
                else -> CloudSyncStatus.Failed(error.message?.take(200) ?: "Falha na sincronização.")
            }
        }
        _status.value = result
        result
    }

    private suspend fun runSync(choice: ConflictChoice?): CloudSyncStatus = withContext(Dispatchers.IO) {
        val userId = auth.currentUserId() ?: throw SyncAuthException()
        // Trocar de conta no mesmo aparelho começa do zero: a base da outra conta não vale aqui.
        if (prefs.getString(KEY_USER, null) != userId) {
            prefs.edit().putString(KEY_USER, userId).remove(KEY_BASE_REVISION).remove(KEY_BASE_SHA).apply()
        }
        val snapshot = canonicalSnapshot()
        val localSha = Sha256.digest(snapshot.encodeToByteArray()).toHex()
        val head = fetchHead()
        val decision = SyncPlanner.decide(
            SyncPlanner.Input(
                baseRevision = prefs.getLong(KEY_BASE_REVISION, -1L).takeIf { it >= 0 },
                baseSha = prefs.getString(KEY_BASE_SHA, null),
                localSha = localSha,
                localEmpty = isLocalEmpty(),
                remoteRevision = head.revision,
                remoteSha = head.sha256,
            ),
        )
        val action = when (decision) {
            SyncPlanner.Action.Nothing -> "nada"
            SyncPlanner.Action.AdoptRemote -> { remember(head.revision, localSha); "igual" }
            SyncPlanner.Action.Upload -> { upload(userId, snapshot, localSha, head.revision); "enviado" }
            SyncPlanner.Action.Download -> { download(head); "recebido" }
            // Os dois lados mudaram: vence o que mudou por último, sem perguntar (a outra versão fica
            // guardada: na conta, as fotos anteriores; aqui, a cópia em sync-backups).
            SyncPlanner.Action.Conflict -> when (choice ?: autoChoice(head)) {
                null -> throw SyncConflictException(head.deviceLabel, head.updatedAt)
                ConflictChoice.KEEP_THIS_DEVICE -> { upload(userId, snapshot, localSha, head.revision); "enviado" }
                ConflictChoice.KEEP_CLOUD -> { download(head); "recebido" }
            }
        }
        val now = System.currentTimeMillis()
        prefs.edit().putLong(KEY_LAST_SYNC, now).apply()
        CloudSyncStatus.Done(now, action)
    }

    private suspend fun canonicalSnapshot(): String = canonicalize(backup.export())

    private suspend fun isLocalEmpty(): Boolean =
        database.dao().competitionsOnce().isEmpty() && database.plannerDao().plansOnce().isEmpty()

    private fun remember(revision: Long, sha: String) {
        prefs.edit().putLong(KEY_BASE_REVISION, revision).putString(KEY_BASE_SHA, sha).apply()
    }

    private suspend fun upload(userId: String, snapshot: String, sha: String, expectedRevision: Long) {
        val path = "$userId/${System.currentTimeMillis()}-${sha.take(12)}.json"
        val bytes = snapshot.encodeToByteArray()
        val put = request("POST", "/storage/v1/object/$BUCKET/$path", bytes, "application/json", mapOf("x-upsert" to "false"))
        if (put.status !in 200..299) error("Envio recusado (${put.status}).")
        val body = JSONObject()
            .put("p_expected_revision", expectedRevision)
            .put("p_object_path", path)
            .put("p_sha256", sha)
            .put("p_size_bytes", bytes.size.toLong())
            .put("p_device_id", deviceId)
            .put("p_device_label", deviceLabel)
        val commit = request("POST", "/rest/v1/rpc/sync_commit", body.toString().encodeToByteArray(), "application/json")
        if (commit.body.contains("sync_conflict")) {
            // Outro aparelho publicou entre a leitura e o envio: a foto enviada fica guardada no
            // histórico da conta e a pessoa decide na próxima rodada.
            val head = fetchHead()
            throw SyncConflictException(head.deviceLabel, head.updatedAt)
        }
        if (commit.status !in 200..299) error("A nuvem não aceitou a versão (${commit.status}).")
        val revision = JSONObject(commit.body).getLong("revision")
        remember(revision, sha)
        runCatching { prune(userId, path) }
    }

    /**
     * Apaga as fotos antigas da conta, deixando a atual e as 4 mais recentes antes dela. Cada envio
     * cria um arquivo novo; sem isso a pasta da conta só cresce. Melhor esforço.
     */
    private suspend fun prune(userId: String, currentPath: String, keep: Int = 4) {
        val listBody = JSONObject()
            .put("prefix", userId)
            .put("limit", 200)
            .put("sortBy", JSONObject().put("column", "name").put("order", "desc"))
        val listed = request("POST", "/storage/v1/object/list/$BUCKET", listBody.toString().encodeToByteArray(), "application/json")
        if (listed.status !in 200..299) return
        val rows = JSONArray(listed.body)
        val names = (0 until rows.length())
            .mapNotNull { rows.optJSONObject(it)?.optString("name")?.takeIf { name -> name.isNotBlank() } }
            .map { "$userId/$it" }
            .filter { it != currentPath }
            .sortedDescending() // o nome começa pelo horário do envio
        names.drop(keep).forEach { old -> request("DELETE", "/storage/v1/object/$BUCKET/$old", null, null) }
    }

    private suspend fun download(head: RemoteHead) {
        val path = head.objectPath ?: error("A nuvem não tem foto.")
        val response = request("GET", "/storage/v1/object/authenticated/$BUCKET/$path", null, null)
        if (response.status !in 200..299) error("Não deu para baixar os dados (${response.status}).")
        val text = response.body
        val sha = Sha256.digest(text.encodeToByteArray()).toHex()
        if (sha != head.sha256) error("A cópia baixada veio incompleta. Tente de novo.")
        keepLocalCopy()
        restoring = true
        try { backup.restore(text) } finally { restoring = false }
        // A base é o que este aparelho exporta depois de restaurar (a ordem das linhas pode sair
        // diferente da foto recebida); assim a próxima rodada não confunde isso com uma edição.
        val restoredSha = Sha256.digest(canonicalSnapshot().encodeToByteArray()).toHex()
        remember(head.revision, restoredSha)
    }

    private fun autoChoice(head: RemoteHead): ConflictChoice {
        val remoteAt = head.updatedAt?.let { runCatching { java.time.OffsetDateTime.parse(it).toInstant().toEpochMilli() }.getOrNull() } ?: 0L
        // Sem registro de quando mudou aqui, a mudança local é tratada como agora (quem está usando o
        // aparelho tem a versão mais fresca).
        val localAt = prefs.getLong(KEY_LOCAL_CHANGED_AT, 0L).takeIf { it > 0L } ?: System.currentTimeMillis()
        return if (localAt >= remoteAt) ConflictChoice.KEEP_THIS_DEVICE else ConflictChoice.KEEP_CLOUD
    }

    /** Guarda a versão deste aparelho antes de substituí-la (mantém as 5 mais recentes). */
    private suspend fun keepLocalCopy() {
        val dir = File(context.filesDir, "sync-backups").apply { mkdirs() }
        File(dir, "antes-de-sincronizar-${System.currentTimeMillis()}.json").writeText(backup.export())
        dir.listFiles()?.sortedByDescending { it.name }?.drop(5)?.forEach { it.delete() }
    }

    private suspend fun fetchHead(): RemoteHead {
        val response = request("GET", "/rest/v1/sync_heads?select=*", null, null, mapOf("Accept" to "application/json"))
        if (response.status == 401) throw SyncAuthException()
        if (response.status !in 200..299) error("Não deu para falar com a nuvem (${response.status}).")
        val row = JSONArray(response.body).optJSONObject(0) ?: return RemoteHead(0, null, null, null, null)
        return RemoteHead(
            revision = row.optLong("revision"),
            objectPath = row.optString("object_path").takeIf { !row.isNull("object_path") && it.isNotBlank() },
            sha256 = row.optString("sha256").takeIf { !row.isNull("sha256") && it.isNotBlank() },
            deviceLabel = row.optString("device_label").takeIf { !row.isNull("device_label") },
            updatedAt = row.optString("updated_at").takeIf { !row.isNull("updated_at") },
        )
    }

    private suspend fun request(method: String, path: String, body: ByteArray?, contentType: String?, extra: Map<String, String> = emptyMap()) =
        transport.execute(
            AiHttpRequest(
                method = method,
                path = path,
                headers = buildMap {
                    put("Authorization", "Bearer ${auth.accessToken() ?: throw SyncAuthException()}")
                    put("apikey", config.publishableKey)
                    if (contentType != null) put("Content-Type", contentType)
                    putAll(extra)
                },
                body = body,
            ),
        ).also { if (it.status == 401) throw SyncAuthException() }

    private data class RemoteHead(val revision: Long, val objectPath: String?, val sha256: String?, val deviceLabel: String?, val updatedAt: String?)

    private class SyncAuthException : Exception("signed_out")

    companion object {
        const val BUCKET = "user-sync"
        private const val KEY_ENABLED = "enabled"
        private const val KEY_DEVICE = "device_id"
        private const val KEY_USER = "user_id"
        private const val KEY_BASE_REVISION = "base_revision"
        private const val KEY_BASE_SHA = "base_sha"
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_LOCAL_CHANGED_AT = "local_changed_at"

        private fun allTables(database: AppDatabase): Array<String> = runCatching {
            database.openHelper.readableDatabase.query("select name from sqlite_master where type = 'table' and name not like 'sqlite_%' and name not like 'room_%' and name not like 'android_%'").use { cursor ->
                buildList { while (cursor.moveToNext()) add(cursor.getString(0)) }
            }.toTypedArray()
        }.getOrDefault(emptyArray())

        /** O backup sem o horário de exportação, compacto: mesmo conteúdo, mesmo hash. */
        fun canonicalize(backupJson: String): String = JSONObject(backupJson).apply { remove("exportedAt") }.toString()
    }
}
