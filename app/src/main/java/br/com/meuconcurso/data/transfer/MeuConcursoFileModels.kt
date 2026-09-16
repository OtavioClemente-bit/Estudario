package br.com.meuconcurso.data.transfer

data class IncomingFilePayload(val displayName: String?, val mimeType: String?, val text: String)

enum class MeuConcursoFileFormat { ESTUDO, PLANO, BACKUP }

sealed interface FileDetectionResult {
    data class Match(val format: MeuConcursoFileFormat) : FileDetectionResult
    data class ExtensionMismatch(val extension: String, val detected: MeuConcursoFileFormat) : FileDetectionResult
    data object InvalidJson : FileDetectionResult
    data object Unknown : FileDetectionResult
}

data class IncomingMeuConcursoFile(
    val payload: IncomingFilePayload,
    val format: MeuConcursoFileFormat,
    val token: Long = System.nanoTime(),
)

class FileReadException(message: String) : IllegalArgumentException(message)
