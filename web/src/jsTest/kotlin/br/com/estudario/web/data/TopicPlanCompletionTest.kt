package br.com.estudario.web.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopicPlanCompletionTest {
    private fun snapshot(theoryStatus: String = "PLANEJADA", topicStatus: String = "NAO_ESTUDADO") = Snapshot.parse("""
        {
          "subjects":[{"id":1,"competitionId":1,"name":"Informática"}],
          "topics":[{"id":10,"subjectId":1,"title":"Redes","status":"$topicStatus"}],
          "studyPlans":[{"id":"plan","competitionId":1,"name":"Plano","start":${Queries.todayEpoch()},"active":true}],
          "planTasks":[
            {"id":"questions","planId":"plan","competitionId":1,"subjectId":1,"topicId":10,"day":${Queries.todayEpoch()},"type":"QUESTIONS","minutes":22,"questions":15,"status":"PLANEJADA","origin":"IMPORTED"},
            {"id":"theory","planId":"plan","competitionId":1,"subjectId":1,"topicId":10,"day":${Queries.todayEpoch()},"type":"THEORY","minutes":50,"status":"$theoryStatus","origin":"IMPORTED"}
          ]
        }
    """.trimIndent())

    @Test
    fun completingFiftyMinutesOfTheoryLeavesTwentyTwoMinutesOfQuestionsPending() {
        val done = Actions.completeStudyWithPlan(snapshot(), 10)

        assertEquals("CONCLUIDA", done.tasks.single { it.id == "theory" }.status)
        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
        assertEquals("ESTUDADO", done.topics.single().status)
        assertEquals(listOf("theory"), done.executions.map { it.taskId })
        assertTrue(done.attempts.isEmpty())
    }

    @Test
    fun repeatingTopicCompletionDoesNotCompleteQuestionsOrAwardAnotherExecution() {
        val done = Actions.completeStudyWithPlan(snapshot(), 10)
        val repeated = Actions.completeStudyWithPlan(done, 10)

        assertEquals("PLANEJADA", repeated.tasks.single { it.id == "questions" }.status)
        assertEquals(1, repeated.executions.size)
        assertEquals(done.encode(), repeated.encode())
    }

    @Test
    fun alreadyStudiedTopicWithOnlyPendingQuestionsDoesNotCompleteThem() {
        val data = snapshot(theoryStatus = "CONCLUIDA", topicStatus = "ESTUDADO")

        val done = Actions.completeStudyWithPlan(data, 10)

        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
        assertTrue(done.executions.isEmpty())
    }

    @Test
    fun topicCompletionCannotCloseAnExplicitQuestionTask() {
        val data = snapshot()

        val done = Actions.completeTask(data, "questions", 50, fromTopic = true)

        assertEquals(data.encode(), done.encode())
    }

    @Test
    fun questionsDoNotBecomeAReviewButtonAfterStudyingTheory() {
        val data = snapshot(theoryStatus = "CONCLUIDA", topicStatus = "ESTUDADO")

        assertEquals(null, Queries.pendingTopicReview(data, 10))
    }

    @Test
    fun actualReviewRemainsAvailableAlongsideQuestions() {
        val data = snapshot(theoryStatus = "CONCLUIDA", topicStatus = "ESTUDADO").append(Keys.TASKS, jsonOf(
            "id" to "review", "planId" to "plan", "competitionId" to 1, "topicId" to 10,
            "day" to Queries.todayEpoch(), "type" to "REVIEW", "minutes" to 15, "status" to "PLANEJADA",
        ))

        assertEquals("review", Queries.pendingTopicReview(data, 10)?.id)
    }

    @Test
    fun focusTimerCannotCompleteQuestionsWithoutAnsweringThem() {
        val data = snapshot()

        val done = Actions.completeFocusWithPlan(data, 10, "questions", 0, 50)

        assertEquals(data.encode(), done.encode())
    }

    @Test
    fun topicFocusWithoutTaskIdCompletesTheoryAndLeavesQuestionsPending() {
        val done = Actions.completeFocusWithPlan(snapshot(), 10, null, 0, 50)

        assertEquals("CONCLUIDA", done.tasks.single { it.id == "theory" }.status)
        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
    }

    @Test
    fun topicAlreadyInProgressCanStillCompleteItsTheory() {
        val done = Actions.completeStudyWithPlan(snapshot(topicStatus = "EM_ESTUDO"), 10)

        assertEquals("ESTUDADO", done.topics.single().status)
        assertEquals("CONCLUIDA", done.tasks.single { it.id == "theory" }.status)
        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
    }

    @Test
    fun staleTheoryReferenceDoesNotCompleteAnotherTheoryBlock() {
        val data = snapshot(theoryStatus = "CONCLUIDA").append(Keys.TASKS, jsonOf(
            "id" to "future-theory", "planId" to "plan", "competitionId" to 1, "topicId" to 10,
            "day" to Queries.todayEpoch() + 1, "type" to "THEORY", "minutes" to 50,
            "status" to "PLANEJADA", "origin" to "IMPORTED",
        ))

        val done = Actions.completeStudyWithPlan(data, 10, preferredTaskId = "theory")

        assertEquals("PLANEJADA", done.tasks.single { it.id == "future-theory" }.status)
        assertTrue(done.executions.isEmpty())
    }

    @Test
    fun theoryFocusUsesMeasuredMinutesAndDoesNotCompleteQuestions() {
        val done = Actions.completeFocusWithPlan(snapshot(), 10, "theory", 0, 9)

        assertEquals("CONCLUIDA", done.tasks.single { it.id == "theory" }.status)
        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
        assertEquals(9, done.executions.single().minutes)
    }

    @Test
    fun completingReviewFocusDoesNotCompleteQuestionsOrStudyTheTopicAgain() {
        val data = snapshot(theoryStatus = "CONCLUIDA", topicStatus = "ESTUDADO")
            .append(Keys.TASKS, jsonOf(
                "id" to "review", "planId" to "plan", "competitionId" to 1, "topicId" to 10,
                "day" to Queries.todayEpoch(), "type" to "REVIEW", "minutes" to 15, "status" to "PLANEJADA",
            ))
            .append(Keys.REVIEWS, jsonOf("id" to 1, "topicId" to 10, "stage" to 1, "dueAt" to 0))

        val done = Actions.completeFocusWithPlan(data, 10, "review", 0, 9)
        val repeated = Actions.completeFocusWithPlan(done, 10, "review", 0, 9)

        assertEquals("CONCLUIDA", done.tasks.single { it.id == "review" }.status)
        assertEquals("PLANEJADA", done.tasks.single { it.id == "questions" }.status)
        assertTrue(done.reviews.single { it.id == 1L }.completedAt != null)
        assertTrue(done.sessions.isEmpty())
        assertEquals(done.encode(), repeated.encode())
    }

    @Test
    fun answeringQuestionQuizStillCompletesTheQuestionTask() {
        val done = Actions.completeTask(snapshot(), "questions", 5, questions = 15, correct = 10, fromQuiz = true)

        assertEquals("CONCLUIDA", done.tasks.single { it.id == "questions" }.status)
        assertEquals(15, done.executions.single().questions)
        assertEquals(10, done.executions.single().correct)
    }
}
