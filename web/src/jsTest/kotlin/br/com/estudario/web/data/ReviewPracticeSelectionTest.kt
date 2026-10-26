package br.com.estudario.web.data

import br.com.estudario.domain.ReviewPracticeOptions
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewPracticeSelectionTest {
    @Test fun selectionUsesOnlyVisiblePracticeQuestionsFromTheReviewTopic() {
        val data = Snapshot.parse("""{"questions":[
          {"id":1,"topicId":10,"statement":"Fixture"},
          {"id":2,"topicId":20,"statement":"Fixture"},
          {"id":3,"topicId":10,"statement":"Fixture","hidden":true},
          {"id":4,"topicId":10,"statement":"Fixture","simulationId":9},
          {"id":5,"topicId":10,"statement":"Fixture"}
        ]}""")

        val selected = reviewPracticeQuestions(data, 10, ReviewPracticeOptions(questionCount = 50), 42)

        assertEquals(setOf(1L, 5L), selected.map { it.id }.toSet())
    }
}
