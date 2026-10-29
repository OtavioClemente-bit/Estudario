package br.com.estudario.domain

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * A atualização do edital passa por TODOS os editais do catálogo (conteudo/editais): uma cópia idêntica
 * ao catálogo não pode gerar nenhuma mudança (nem tópico novo, nem nome trocado, nem "saiu do edital").
 */
class EditalSyncCatalogTest {
    private fun catalog(file: File): List<EditalSync.CatalogSubject> {
        val root = Json.parseToJsonElement(file.readText()).jsonObject
        return root["disciplinas"]!!.jsonArray.map { d ->
            val o = d.jsonObject
            EditalSync.CatalogSubject(
                o["nome"]!!.jsonPrimitive.content.trim(),
                o["topicos"]!!.jsonArray.map { t -> ((t as? JsonObject)?.get("texto") ?: t).jsonPrimitive.content.trim() }.filter { it.isNotBlank() },
            )
        }.filter { it.name.isNotBlank() && it.topics.isNotEmpty() }
    }

    /** A cópia da pessoa como o app e o site montam: matérias por nome e tópicos sem repetir (minúsculas). */
    private fun copyOf(catalog: List<EditalSync.CatalogSubject>): List<EditalSync.Subject> {
        var id = 1L
        val subjects = linkedMapOf<String, Pair<Long, MutableList<EditalSync.Topic>>>()
        for (s in catalog) {
            val (sid, topics) = subjects.getOrPut(s.name.lowercase()) { id++ to mutableListOf() }
            val seen = topics.mapTo(hashSetOf()) { it.title.lowercase() }
            s.topics.forEach { if (seen.add(it.lowercase())) topics += EditalSync.Topic(id++, it) }
            subjects[s.name.lowercase()] = sid to topics
        }
        return subjects.map { (key, v) -> EditalSync.Subject(v.first, catalog.first { it.name.lowercase() == key }.name, v.second) }
    }

    @Test
    fun copiaIgualAoCatalogoNaoMudaNada() {
        val dir = listOf("../conteudo/editais", "conteudo/editais", "../../conteudo/editais").map(::File).first { it.isDirectory || it.path == "conteudo/editais" }
        val files = dir.listFiles { f -> f.name.endsWith(".json") }.orEmpty().sortedBy { it.name }
        assertTrue("editais não encontrados em ${dir.absolutePath}", files.size > 100)
        val problems = mutableListOf<String>()
        for (file in files) {
            val cat = runCatching { catalog(file) }.getOrNull() ?: continue
            val mine = copyOf(cat)
            for (baseline in listOf(null, cat.associate { EditalSync.norm(it.name) to it.topics })) {
                val plan = EditalSync.plan(mine, cat, baseline)
                if (plan.changes > 0 || plan.removed.isNotEmpty()) {
                    problems += "${file.name} (${if (baseline == null) "sem lista" else "com lista"}): novos=${plan.newTopics.map { it.second }.take(3)} matérias=${plan.newSubjects.map { it.name }} renomeados=${plan.renamedTopics.take(3)} saíram=${plan.removed.size}"
                }
            }
            assertTrue(file.name, EditalSync.cameFrom(mine, cat))
        }
        assertEquals("${problems.size} editais com mudança falsa", emptyList<String>(), problems.take(20))
    }
}
