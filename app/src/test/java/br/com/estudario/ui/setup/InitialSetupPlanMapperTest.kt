package br.com.estudario.ui.setup

import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PersonalDifficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test

class InitialSetupPlanMapperTest {
    @Test
    fun `prompt receives topic progress and keeps exam priority separate from difficulty`() {
        val competition = CompetitionEntity(id = 9, name = "Concurso")
        val highSubject = SubjectEntity(id = 10, competitionId = 9, name = "Constitucional", externalId = "subject-constitutional", assessedPriorityScore = 75, hasAssessedPriority = true)
        val lowSubject = SubjectEntity(id = 11, competitionId = 9, name = "Português", externalId = "subject-portuguese", assessedPriorityScore = 30, hasAssessedPriority = true)
        val studiedTopic = TopicEntity(id = 20, subjectId = 10, title = "Direitos fundamentais", status = TopicStatus.ESTUDADO, externalId = "topic-rights")
        val unansweredTopic = TopicEntity(id = 21, subjectId = 10, title = "Controle de constitucionalidade", externalId = "topic-control")
        val questions = listOf(
            QuestionWithOptions(QuestionEntity(id = 30, topicId = 20, statement = "Q1", explanation = "E1"), emptyList()),
            QuestionWithOptions(QuestionEntity(id = 31, topicId = 20, statement = "Q2", explanation = "E2"), emptyList()),
        )
        val attempts = listOf(
            QuestionAttemptEntity(id = 40, questionId = 30, selectedKey = "A", correct = true),
            QuestionAttemptEntity(id = 41, questionId = 31, selectedKey = "B", correct = false),
        )

        val result = InitialSetupPlanMapper.map(
            competition = competition,
            subjects = listOf(highSubject, lowSubject),
            topics = listOf(studiedTopic, unansweredTopic),
            questions = questions,
            attempts = attempts,
            difficulties = mapOf("10" to PersonalDifficulty.EASY, "11" to PersonalDifficulty.HARD),
        )

        assertEquals(listOf("topic-rights", "topic-control"), result.promptSubjects.first().topics.map { it.id })
        assertTrue(result.promptSubjects.first().topics.first().studied)
        assertEquals(2, result.promptSubjects.first().answered)
        assertEquals(50, result.promptSubjects.first().accuracyPercent)
        assertEquals(PlanPriority.HIGH, result.officialPrioritiesBySubjectId[10L])
        assertEquals(PlanPriority.HIGH, result.planningPrioritiesBySubjectId[10L])
        assertEquals(PlanPriority.LOW, result.officialPrioritiesBySubjectId[11L])
        assertEquals(PlanPriority.LOW, result.planningPrioritiesBySubjectId[11L])
        assertEquals(PersonalDifficulty.EASY, result.difficultiesBySubjectId[10L])
        assertEquals(PersonalDifficulty.HARD, result.difficultiesBySubjectId[11L])
        assertEquals(75, highSubject.assessedPriorityScore)
        assertEquals(30, lowSubject.assessedPriorityScore)
        assertNotSame(result.officialPrioritiesBySubjectId, result.planningPrioritiesBySubjectId)
    }

    @Test
    fun `missing difficulty defaults to normal and unknown topic associations are ignored`() {
        val subject = SubjectEntity(id = 10, competitionId = 9, name = "Português", externalId = "subject-portuguese")
        val topic = TopicEntity(id = 20, subjectId = 10, title = "Interpretação", externalId = "topic-reading")

        val result = InitialSetupPlanMapper.map(
            competition = null,
            subjects = listOf(subject),
            topics = listOf(topic),
            questions = emptyList(),
            attempts = listOf(QuestionAttemptEntity(questionId = 99, selectedKey = "A", correct = true)),
            difficulties = emptyMap(),
        )

        assertEquals(PlanPriority.MEDIUM, result.planningPrioritiesBySubjectId[10L])
        assertEquals(PersonalDifficulty.DEFAULT, result.difficultiesBySubjectId[10L])
        assertEquals(0, result.promptSubjects.single().answered)
        assertEquals(null, result.promptSubjects.single().accuracyPercent)
    }
}
