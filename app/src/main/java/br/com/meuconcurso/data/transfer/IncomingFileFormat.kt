package br.com.meuconcurso.data.transfer

import org.json.JSONObject

enum class IncomingFileFormat {
    ESTUDO,
    PLANO,
    BACKUP;

    companion object {
        fun detect(text: String): IncomingFileFormat? = runCatching {
            val root = JSONObject(text)
            when (root.optString("format")) {
                "meu-concurso-estudo", "estudo" -> ESTUDO
                "meu-concurso-plano" -> PLANO
                "meu-concurso-backup" -> BACKUP
                "" -> if (root.has("schemaVersion") && root.has("competition")) ESTUDO else null
                else -> null
            }
        }.getOrNull()
    }
}
