package br.com.estudario.domain.simulation

import br.com.estudario.text.stripAccents

/**
 * Em que tópico do edital da pessoa mora cada questão da prova oficial: o tópico com o mesmo texto;
 * senão, o tópico da mesma matéria com mais palavras em comum com o assunto e o enunciado; senão, o
 * primeiro tópico da matéria. Assim o acerto e o erro caem no tópico certo (estatística, revisões,
 * caderno de erros), mesmo quando a pessoa editou o edital. Igual no app e no site.
 */
object OfficialTopicMatcher {
    private val stop = setOf("de", "da", "do", "das", "dos", "e", "a", "o", "em", "no", "na", "nos", "nas", "para", "por", "com", "as", "os", "ao", "aos", "lei", "que", "se", "um", "uma")

    private fun normalize(value: String) = stripAccents(value.lowercase()).replace(Regex("[^a-z0-9]+"), " ").trim()

    private fun words(value: String): Set<String> = normalize(value).split(' ').filter { it.length > 2 && it !in stop }.toSet()

    fun topicFor(subject: String, topic: String?, statement: String, leaves: List<BlueprintTopic>): Long {
        val topicNorm = topic?.let(::normalize).orEmpty()
        if (topicNorm.isNotEmpty()) leaves.firstOrNull { normalize(it.path.last()) == topicNorm }?.let { return it.topicId }
        val subjectNorm = normalize(subject)
        val sameSubject = leaves.filter {
            val name = normalize(it.subjectName)
            subjectNorm.isNotEmpty() && (name == subjectNorm || name.contains(subjectNorm) || subjectNorm.contains(name))
        }
        val pool = sameSubject.ifEmpty { leaves }
        val target = words("${topic.orEmpty()} ${statement.take(600)}")
        // Empate (ou nenhuma palavra em comum): fica o primeiro, na ordem do edital.
        return pool.maxBy { leaf -> words(leaf.path.joinToString(" ")).count { it in target } }.topicId
    }
}
