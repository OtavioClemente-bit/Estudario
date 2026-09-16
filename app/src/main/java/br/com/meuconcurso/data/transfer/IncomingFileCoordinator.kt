package br.com.meuconcurso.data.transfer

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong

class IncomingFileCoordinator(private val detector: MeuConcursoFileDetector = MeuConcursoFileDetector()) {
    private val requestSequence = AtomicLong()
    private val _pending = MutableStateFlow<IncomingMeuConcursoFile?>(null)
    val pending = _pending.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    suspend fun open(resolver: ContentResolver, uri: Uri, hintedMimeType: String? = null, expected: MeuConcursoFileFormat? = null) {
        val request = requestSequence.incrementAndGet()
        _pending.value = null
        _error.value = null
        try {
            val payload = resolver.readIncomingFile(uri, hintedMimeType)
            if (request != requestSequence.get()) return
            when (val result = detector.detect(payload)) {
                is FileDetectionResult.Match -> {
                    if (expected != null && expected != result.format) throw FileReadException("O arquivo escolhido não corresponde ao formato esperado.")
                    _pending.value = IncomingMeuConcursoFile(payload, result.format)
                }
                is FileDetectionResult.ExtensionMismatch -> throw FileReadException("A extensão do arquivo não corresponde ao conteúdo do Meu Concurso.")
                FileDetectionResult.InvalidJson -> throw FileReadException("O arquivo não contém JSON válido.")
                FileDetectionResult.Unknown -> throw FileReadException("Arquivo não reconhecido. Escolha um arquivo .estudo, .plano ou backup do Meu Concurso.")
            }
        } catch (e: Exception) {
            if (request == requestSequence.get()) _error.value = e.message ?: "Não foi possível abrir o arquivo."
        }
    }

    fun consume(token: Long) { if (_pending.value?.token == token) _pending.value = null }
    fun consumeError() { _error.value = null }
}
