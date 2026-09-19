package br.com.estudario.data.transfer

import org.json.JSONObject

enum class IncomingFileFormat {
    ESTUDO,
    PLANO,
    BACKUP;

    companion object {
        /**
         * Reconhece o conteúdo mesmo quando a IA não escreve o campo "format" (caso comum do
         * .estudo v2), quando a resposta vem dentro de ```json ... ``` ou com texto antes/depois.
         */
        fun detect(text: String): IncomingFileFormat? = runCatching {
            val root = JSONObject(IncomingText.clean(text))
            when (root.optString("format")) {
                // "meu-concurso-*" é o nome antigo do app: arquivos gerados antes da troca
                // continuam sendo reconhecidos.
                "estudario-estudo", "meu-concurso-estudo", "estudo" -> ESTUDO
                "estudario-plano", "meu-concurso-plano", "plano" -> PLANO
                "estudario-backup", "meu-concurso-backup", "backup" -> BACKUP
                "" -> when {
                    root.has("planId") && (root.has("tarefas") || root.has("configuracao")) -> PLANO
                    root.has("materias") || root.has("packageId") -> ESTUDO
                    root.has("schemaVersion") && root.has("competition") -> ESTUDO
                    (root.has("competition") || root.has("concurso")) && (root.has("topic") || root.has("topico")) -> ESTUDO
                    else -> null
                }
                else -> null
            }
        }.getOrNull()
    }
}

/** Limpa o texto que chega de arquivos, da área de transferência ou de apps de IA antes do parse. */
object IncomingText {
    fun clean(raw: String): String {
        var text = raw.removePrefix("﻿").trim()
        // Remove cercas de Markdown (```json ... ```), comuns quando a resposta é copiada da conversa.
        if (text.startsWith("```")) {
            text = text.substringAfter('\n', "").trim()
            if (text.endsWith("```")) text = text.removeSuffix("```").trim()
        }
        if (!text.startsWith("{")) {
            val start = text.indexOf('{')
            val end = text.lastIndexOf('}')
            if (start >= 0 && end > start) text = text.substring(start, end + 1)
        } else if (!text.endsWith("}")) {
            val end = text.lastIndexOf('}')
            if (end > 0) text = text.substring(0, end + 1)
        }
        return text
    }
}
