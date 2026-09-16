package br.com.meuconcurso.data.transfer

import org.json.JSONObject

class MeuConcursoFileDetector {
    fun detect(payload: IncomingFilePayload): FileDetectionResult {
        val root = try { JSONObject(payload.text) } catch (_: Exception) { return FileDetectionResult.InvalidJson }
        val detected = when (root.optString("format")) {
            "meu-concurso-plano" -> MeuConcursoFileFormat.PLANO
            "meu-concurso-backup" -> MeuConcursoFileFormat.BACKUP
            "meu-concurso-estudo", "estudo" -> MeuConcursoFileFormat.ESTUDO
            "" -> if ((root.has("schemaVersion") && root.has("competition")) || root.has("packageId")) MeuConcursoFileFormat.ESTUDO else null
            else -> null
        } ?: return FileDetectionResult.Unknown
        val extension = payload.displayName?.substringAfterLast('.', "")?.lowercase().orEmpty()
        val expected = when (extension) {
            "plano" -> MeuConcursoFileFormat.PLANO
            "estudo" -> MeuConcursoFileFormat.ESTUDO
            else -> null
        }
        return if (expected != null && expected != detected) FileDetectionResult.ExtensionMismatch(extension, detected)
        else FileDetectionResult.Match(detected)
    }
}
