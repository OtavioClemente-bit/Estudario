package br.com.estudario.data.simulation

import br.com.estudario.data.catalog.ExamCatalogSearch
import br.com.estudario.data.catalog.OfficialQuestion
import br.com.estudario.domain.simulation.BlueprintTopic

/**
 * Em que tópico do edital da pessoa mora cada questão da prova oficial: o tópico com o mesmo texto;
 * senão, o tópico da mesma matéria com mais palavras em comum com o assunto e o enunciado; senão, o
 * primeiro tópico da matéria. Assim o acerto e o erro caem no tópico certo (estatística, revisões,
 * caderno de erros), mesmo quando a pessoa editou o edital.
 */
object OfficialTopicMatcher {
    private val stop = setOf("de", "da", "do", "das", "dos", "e", "a", "o", "em", "no", "na", "nos", "nas", "para", "por", "com", "as", "os", "ao", "aos", "lei", "que", "se", "um", "uma")

    private fun words(value: String): Set<String> =
        ExamCatalogSearch.normalize(value).split(' ').filter { it.length > 2 && it !in stop }.toSet()

    fun topicFor(question: OfficialQuestion, leaves: List<BlueprintTopic>): Long {
        val topic = question.topic?.let(ExamCatalogSearch::normalize).orEmpty()
        if (topic.isNotEmpty()) leaves.firstOrNull { ExamCatalogSearch.normalize(it.path.last()) == topic }?.let { return it.topicId }
        val subject = ExamCatalogSearch.normalize(question.subject)
        val sameSubject = leaves.filter {
            val name = ExamCatalogSearch.normalize(it.subjectName)
            subject.isNotEmpty() && (name == subject || name.contains(subject) || subject.contains(name))
        }
        val pool = sameSubject.ifEmpty { leaves }
        val target = words("${question.topic.orEmpty()} ${question.topic.orEmpty()} ${question.statement.take(600)}")
        // Empate (ou nenhuma palavra em comum): fica o primeiro, na ordem do edital.
        return pool.maxBy { leaf -> words(leaf.path.joinToString(" ")).count { it in target } }.topicId
    }
}
