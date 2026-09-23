package br.com.estudario.data.local

object RemoteSyllabusSyncError {
    const val AUTH = "AUTH_REQUIRED"
    const val NETWORK = "NETWORK_ERROR"
    const val TIMEOUT = "NETWORK_TIMEOUT"
    const val REMOTE = "REMOTE_ERROR"
    const val UNKNOWN = "SYNC_FAILED"

    fun safe(raw: String?): String {
        val value = raw.orEmpty().trim()
        if (value.isEmpty()) return UNKNOWN
        val normalized = value.lowercase()
        return when {
            normalized.contains("401") || normalized.contains("unauthorized") || normalized.contains("forbidden") || normalized.contains("authorization") || normalized.contains("bearer") -> AUTH
            normalized.contains("timeout") || normalized.contains("timed out") -> TIMEOUT
            normalized.contains("network") || normalized.contains("connect") || normalized.contains("socket") || normalized.contains("dns") -> NETWORK
            normalized.contains("http") || normalized.contains("remote") || normalized.contains("server") -> REMOTE
            else -> UNKNOWN
        }
    }
}
