package br.com.estudario.web.data

import br.com.estudario.data.local.ReviewDifficulty
import br.com.estudario.time.plusDays
import br.com.estudario.time.startOfDay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReviewStageProgressionTest {
    private fun due(days: Long) = Queries.todayDate().plusDays(days).startOfDay(Queries.zone).toEpochMilliseconds()
    private fun scheduled() = Snapshot.parse("""
        {"topics":[{"id":10,"subjectId":1,"title":"Redes","status":"ESTUDADO"}],
         "reviews":[
           {"id":1,"topicId":10,"stage":1,"dueAt":${due(0)}},
           {"id":2,"topicId":10,"stage":2,"dueAt":${due(6)}},
           {"id":3,"topicId":10,"stage":3,"dueAt":${due(29)}}]}
    """.trimIndent())

    @Test
    fun difficultReviewRepeatsSameStageTomorrowAndPreservesFutureSpacing() {
        val done = Actions.completeReview(scheduled(), 1, ReviewDifficulty.DIFICIL, correct = 0, total = 3)

        assertNull(done.reviews.single { it.id == 1L }.completedAt)
        assertEquals(1, done.reviews.single { it.id == 1L }.stage)
        assertEquals(due(1), done.reviews.single { it.id == 1L }.dueAt)
        assertEquals(due(7), done.reviews.single { it.id == 2L }.dueAt)
        assertEquals(due(30), done.reviews.single { it.id == 3L }.dueAt)
        assertEquals(3, done.reviews.size)
        assertEquals(1, done.array(Keys.REVIEW_SESSIONS).size)
        assertEquals(1, done.reviewHistory.size)
        val restored = Snapshot.parse(done.encode())
        assertEquals(6L, restored.reviews.single { it.id == 2L }.intervalDays)
        assertTrue(restored.reviews.single { it.id == 1L }.updatedAt > 0)
    }

    @Test
    fun difficultLastStageDoesNotCreateANewStage() {
        val data = scheduled().edit(Keys.REVIEWS) { row ->
            when (row["id"].toString()) {
                "1", "2" -> row.with("completedAt" to kotlinx.serialization.json.JsonPrimitive(due(-1)))
                else -> row.with("dueAt" to kotlinx.serialization.json.JsonPrimitive(due(0)))
            }
        }

        val done = Actions.completeReview(data, 3, ReviewDifficulty.DIFICIL)

        assertEquals(3, done.reviews.size)
        assertNull(done.reviews.single { it.id == 3L }.completedAt)
        assertEquals(due(1), done.reviews.single { it.id == 3L }.dueAt)
    }

    @Test
    fun futureOrLaterStageCannotBeCompletedBeforeTheCurrentStage() {
        val data = scheduled()

        val done = Actions.completeReview(data, 2, ReviewDifficulty.NORMAL)

        assertEquals(data.encode(), done.encode())
        assertTrue(done.reviewHistory.isEmpty())
    }

    @Test
    fun skippedReviewCannotCreateAFakeCompletionOrRepeat() {
        val data = scheduled().edit(Keys.REVIEWS) { row ->
            if (row["id"].toString() == "1") row.with("ignoredAt" to kotlinx.serialization.json.JsonPrimitive(due(0))) else row
        }

        assertEquals(data.encode(), Actions.completeReview(data, 1, ReviewDifficulty.DIFICIL).encode())
    }
}
