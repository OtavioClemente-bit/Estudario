package br.com.estudario.data.local

import br.com.estudario.domain.PriorityEvidence
import br.com.estudario.domain.PriorityEvidenceType
import org.json.JSONArray
import org.json.JSONObject

object PriorityEvidenceCodec {
    fun encode(values: List<PriorityEvidence>): String = JSONArray().apply {
        values.forEach { evidence ->
            put(JSONObject().apply {
                put("type", evidence.type.name)
                put("description", evidence.description)
                evidence.value?.let { put("value", it) }
            })
        }
    }.toString()

    fun decode(json: String): List<PriorityEvidence> = runCatching {
        val array = JSONArray(json)
        buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                val type = PriorityEvidenceType.entries.firstOrNull { it.name == item.optString("type") } ?: continue
                val description = item.optString("description").trim()
                if (description.isBlank()) continue
                add(type to description to item.optDouble("value").takeUnless { it.isNaN() })
            }
        }.map { (typeAndDescription, value) ->
            val (type, description) = typeAndDescription
            PriorityEvidence(type, description, value)
        }
    }.getOrDefault(emptyList())
}
