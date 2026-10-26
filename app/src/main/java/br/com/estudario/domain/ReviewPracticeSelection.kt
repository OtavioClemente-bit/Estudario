package br.com.estudario.domain

import br.com.estudario.data.local.QuestionWithOptions
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.TheoryDocumentEntity
import br.com.estudario.data.local.TheoryMarkEntity

data class ReviewPracticeMaterial(
    val snippets: List<TopicSnippetEntity>,
    val summaries: List<SummaryEntity>,
    val questions: List<QuestionWithOptions>,
    val theories: List<TheoryDocumentEntity>,
    val marks: List<TheoryMarkEntity>,
)

fun reviewPracticeQuestions(questions: List<QuestionWithOptions>, topicId: Long, options: ReviewPracticeOptions, seed: Long): List<QuestionWithOptions> {
    val available = questions.filter { it.question.topicId == topicId && !it.question.isHidden && it.question.simulationId == null }
    val byId = available.associateBy { it.question.id }
    return ReviewPractice.select(available.map { ReviewPracticeQuestion(it.question.id, it.question.answerCount, it.question.errorCount) }, options, seed).mapNotNull(byId::get)
}
