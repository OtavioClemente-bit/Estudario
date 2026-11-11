package br.com.estudario.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewQueueTest {
    private val today = 1000L

    @Test fun freshFirstReviewJumpsTheBacklog() {
        // 20 atrasadas e, por último, o D+1 do tópico estudado ontem; teto de 4 por dia.
        val late = (1L..20L).map { ReviewQueue.Item(it, today - 5, 2) }
        val fresh = ReviewQueue.Item(99, today, 1)
        val placed = ReviewQueue.spread(late + fresh, today) { 4 }
        assertEquals(today, placed[99])
        assertEquals(3, placed.count { (id, day) -> id != 99L && day == today })
    }

    @Test fun firstReviewMissedYesterdayStillComesFirst() {
        val late = (1L..10L).map { ReviewQueue.Item(it, today - 6, 1) }
        val placed = ReviewQueue.spread(late + ReviewQueue.Item(50, today - 1, 1), today) { 4 }
        assertEquals(today, placed[50])
    }

    @Test fun backlogStillSpreadsOldestFirst() {
        val items = listOf(ReviewQueue.Item(1, today - 1, 3), ReviewQueue.Item(2, today - 9, 2), ReviewQueue.Item(3, today - 4, 2))
        val placed = ReviewQueue.spread(items, today) { 1 }
        assertEquals(today, placed[2])
        assertEquals(today + 1, placed[3])
        assertEquals(today + 2, placed[1])
    }

    @Test fun todayListShowsEveryDueReviewInOrder() {
        // 20 atrasadas, o D+1 de ontem e uma de amanhã: hoje aparecem as 21, com o D+1 na frente
        val late = (1L..20L).map { ReviewQueue.Item(it, today - 5 + (it % 3), 2) }
        val order = ReviewQueue.dueOrder(late + ReviewQueue.Item(99, today, 1) + ReviewQueue.Item(77, today + 1, 1), today)
        assertEquals(21, order.size)
        assertEquals(99L, order.first())
        assertEquals(false, 77L in order)
    }

    @Test fun futureReviewsStayOnTheirDay() {
        val placed = ReviewQueue.spread(listOf(ReviewQueue.Item(1, today + 3, 1)), today) { 4 }
        assertEquals(today + 3, placed[1])
    }
}
