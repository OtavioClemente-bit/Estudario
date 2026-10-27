package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StudyNudgesTest {
    @Test
    fun situacaoDoDiaPelaOrdemDeImportancia() {
        assertEquals(StudyNudges.Kind.COMEBACK, StudyNudges.dailyKind(StudyNudges.Facts(daysAway = 3, dueReviews = 2)))
        assertEquals(StudyNudges.Kind.EXAM_COUNTDOWN, StudyNudges.dailyKind(StudyNudges.Facts(daysToExam = 30, dueReviews = 2)))
        assertEquals(StudyNudges.Kind.ALMOST_GOAL, StudyNudges.dailyKind(StudyNudges.Facts(missing = 4, doneToday = 6, dueReviews = 2)))
        assertEquals(StudyNudges.Kind.REVIEWS_DUE, StudyNudges.dailyKind(StudyNudges.Facts(dueReviews = 2)))
        assertEquals(StudyNudges.Kind.DAILY, StudyNudges.dailyKind(StudyNudges.Facts()))
    }

    @Test
    fun frasesGiramECarregamODadoDoDia() {
        val f = StudyNudges.Facts(name = "Otavio Clemente", streak = 12, dueReviews = 1, firstReview = "Crase")
        val titles = (0 until 5).map { StudyNudges.pick(StudyNudges.Kind.REVIEW_MORNING, f, it).title }.toSet()
        assertTrue(titles.size >= 4)
        assertTrue(titles.all { "Crase" in it })
        val risk = StudyNudges.pick(StudyNudges.Kind.STREAK_RISK, f, 0)
        assertTrue("12 dias" in risk.title + risk.body)
        assertEquals("SURPRISED", risk.mood)
        // nada de chaves sobrando nem "null" no texto
        StudyNudges.Kind.entries.forEach { k ->
            (0 until 6).forEach { s ->
                val n = StudyNudges.pick(k, f.copy(todayTask = "Licitações", taskMinutes = 30, weakSubject = "Português", weakAccuracy = 48, daysToExam = 7, daysAway = 3, missing = 3, doneToday = 7), s)
                assertTrue("null" !in n.title + n.body && "$" !in n.title + n.body, "$k/$s: ${n.title} ${n.body}")
            }
        }
    }
}
