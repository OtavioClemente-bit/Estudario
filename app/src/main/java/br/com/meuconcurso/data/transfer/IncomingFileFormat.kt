package br.com.meuconcurso.data.transfer

enum class IncomingFileFormat {
    ESTUDO,
    PLANO,
    BACKUP;

    companion object {
        fun detect(text: String): IncomingFileFormat? = when (val result = MeuConcursoFileDetector().detect(IncomingFilePayload(null, null, text))) {
            is FileDetectionResult.Match -> valueOf(result.format.name)
            else -> null
        }
    }
}
