package br.com.estudario.web.data

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** A foto nova herda o que não mudou, e o texto enviado continua idêntico ao de antes. */
class SnapshotCacheTest {
    private fun sample(): Snapshot = Snapshot.parse(
        """
        {"format":"estudario-backup","version":8,
         "questions":[{"id":1,"topicId":5,"statement":"Questão \"um\" é",
           "options":[{"id":11,"key":"A","text":"á","correct":true}],"answerCount":0,"correctCount":0,"errorCount":0},
          {"id":2,"topicId":5,"statement":"Dois","options":[],"answerCount":3,"correctCount":1,"errorCount":2}],
         "attempts":[],
         "planTasks":[{"id":"t1","planId":"p","topicId":5,"day":10,"status":"PLANEJADA","type":"TEORIA"}],
         "visual":{"hat":"x"}}
        """.trimIndent(),
    )

    @Test fun encodingIsTheSameAsBefore() {
        val data = sample()
        assertEquals(snapshotJson.encodeToString(JsonObject.serializer(), data.root), data.encode())
        val next = Actions.answer(data, 1, "A").first
        assertEquals(snapshotJson.encodeToString(JsonObject.serializer(), next.root), next.encode())
    }

    @Test fun answeringKeepsTheOtherTablesDecoded() {
        val data = sample()
        val tasks = data.tasks
        val second = data.questions[1]
        val next = Actions.answer(data, 1, "A").first
        // a tabela do plano não muda: a lista decodificada é a mesma
        assertSame(tasks, next.tasks)
        // a questão que não foi respondida também é reaproveitada; a respondida vem atualizada
        assertSame(second, next.questions[1])
        assertEquals(1, next.questions[0].answerCount)
        assertEquals(1, next.attempts.size)
        assertTrue(next.attempts.first().correct)
    }

    @Test fun appendAndRemoveStayAligned() {
        var data = sample()
        data.questions
        data = data.append(Keys.QUESTIONS, JsonObject(mapOf("id" to JsonPrimitive(3), "topicId" to JsonPrimitive(5), "statement" to JsonPrimitive("Três"), "options" to kotlinx.serialization.json.JsonArray(emptyList()))))
        assertEquals(listOf(1L, 2L, 3L), data.questions.map { it.id })
        data = data.edit(Keys.QUESTIONS) { if ((it["id"] as? JsonPrimitive)?.content == "2") null else it }
        assertEquals(listOf(1L, 3L), data.questions.map { it.id })
        assertEquals("Três", data.questions[1].statement)
    }
}
