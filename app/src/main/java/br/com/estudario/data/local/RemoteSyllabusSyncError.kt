package br.com.estudario.data.local

object RemoteSyllabusSyncError {
    const val AUTH = "AUTH_REQUIRED"
    const val NETWORK = "NETWORK_ERROR"
    const val TIMEOUT = "NETWORK_TIMEOUT"
    const val REMOTE = "REMOTE_ERROR"
    const val UNKNOWN = "SYNC_FAILED"
    const val SUPERSEDED = "SUPERSEDED"
}
