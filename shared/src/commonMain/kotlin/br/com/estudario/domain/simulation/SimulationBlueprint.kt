package br.com.estudario.domain.simulation

import br.com.estudario.text.stripAccents
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

/** Tipos de simulado. A ordem é a da tela. */
enum class SimulationMode(val title: String, val description: String) {
    DIAGNOSTIC("Diagnóstico", "20 questões de todo o edital para descobrir seu nível em cada matéria. Pode fazer antes de estudar."),
    STUDIED("Do que você estudou", "Só os tópicos que você já estudou, na proporção do edital. Mostra se o estudo está fixando."),
    FULL("Prova completa", "O edital inteiro, no tamanho e no tempo de uma prova de verdade."),
    REMATCH("Revanche", "As questões que você errou voltam com outro cenário e outros dados. Só acerta quem entendeu."),
}

/** Um tópico-folha do edital (o que gera material), com o que a planta precisa saber dele. */
data class BlueprintTopic(
    val topicId: Long,
    val subjectId: Long,
    val subjectName: String,
    val path: List<String>,
    /** Peso para a prova (0 a 100), vindo da prioridade do tópico. */
    val weight: Int,
    val studied: Boolean,
    val hasContent: Boolean,
    val scope: String? = null,
)

data class BlueprintItem(
    val topicId: Long,
    val subjectId: Long,
    val subjectName: String,
    val path: List<String>,
    val count: Int,
    val scope: String? = null,
)

data class BlueprintPart(val items: List<BlueprintItem>) {
    val size: Int get() = items.sumOf { it.count }
}

data class Blueprint(val mode: SimulationMode, val parts: List<BlueprintPart>) {
    val total: Int get() = parts.sumOf { it.size }
    /** Questões por matéria, na ordem da prova. */
    val perSubject: List<Pair<String, Int>>
        get() = parts.flatMap { it.items }.groupBy { it.subjectName }.map { (name, items) -> name to items.sumOf { it.count } }
}

/**
 * Planta da prova: quantas questões de cada matéria e de cada tópico, dividida em partes que o
 * servidor gera em paralelo. Matérias com mais tópicos e tópicos de prioridade maior recebem mais
 * questões, como numa prova de verdade; dentro da matéria a distribuição passa por todos os tópicos
 * antes de repetir, para o simulado cobrir o máximo do edital.
 */
object SimulationBlueprint {
    const val MAX_PER_PART = 30
    const val MAX_PER_ITEM = 10

    fun eligible(mode: SimulationMode, topics: List<BlueprintTopic>): List<BlueprintTopic> = when (mode) {
        SimulationMode.STUDIED -> topics.filter { it.studied || it.hasContent }
        else -> topics
    }

    fun build(mode: SimulationMode, topics: List<BlueprintTopic>, total: Int): Blueprint {
        val pool = eligible(mode, topics)
        if (pool.isEmpty() || total <= 0) return Blueprint(mode, emptyList())
        val bySubject = pool.groupBy { it.subjectId }
        // Ordem de prova: a ordem em que as matérias aparecem no edital.
        val subjectOrder = pool.map { it.subjectId }.distinct()
        val subjectWeight = subjectOrder.associateWith { id -> bySubject.getValue(id).sumOf { topicWeight(it) } }
        val allocation = allocate(total, subjectOrder, subjectWeight)
        val items = subjectOrder.flatMap { subjectId ->
            val count = allocation[subjectId] ?: 0
            if (count == 0) emptyList() else spread(bySubject.getValue(subjectId), count)
        }
        return Blueprint(mode, split(items))
    }

    private fun topicWeight(topic: BlueprintTopic): Double = 1.0 + topic.weight.coerceIn(0, 100) / 50.0

    /** Maiores restos: a soma bate exatamente com o total; com mais matérias que questões, ficam as de maior peso. */
    private fun allocate(total: Int, order: List<Long>, weights: Map<Long, Double>): Map<Long, Int> {
        val chosen = if (order.size <= total) order else order.sortedByDescending { weights.getValue(it) }.take(total)
        val sum = chosen.sumOf { weights.getValue(it) }
        val exact = chosen.associateWith { weights.getValue(it) / sum * total }
        // Toda matéria escolhida recebe pelo menos uma questão.
        val result = chosen.associateWith { floor(exact.getValue(it)).toInt().coerceAtLeast(1) }.toMutableMap()
        var assigned = result.values.sum()
        val byRemainder = chosen.sortedByDescending { exact.getValue(it) - floor(exact.getValue(it)) }
        var index = 0
        while (assigned < total) {
            val id = byRemainder[index % byRemainder.size]
            result[id] = result.getValue(id) + 1
            assigned++
            index++
        }
        while (assigned > total) {
            val id = result.filterValues { it > 1 }.maxByOrNull { it.value.toDouble() - exact.getValue(it.key) }?.key ?: break
            result[id] = result.getValue(id) - 1
            assigned--
        }
        return result
    }

    /** Passa por todos os tópicos (os de maior peso primeiro) antes de dar a segunda questão a algum. */
    private fun spread(topics: List<BlueprintTopic>, count: Int): List<BlueprintItem> {
        val ordered = topics.sortedWith(compareByDescending<BlueprintTopic> { it.weight }.thenBy { it.topicId })
        val counts = LinkedHashMap<Long, Int>()
        var left = count
        while (left > 0) {
            var progressed = false
            for (topic in ordered) {
                if (left == 0) break
                val current = counts[topic.topicId] ?: 0
                if (current >= MAX_PER_ITEM) continue
                counts[topic.topicId] = current + 1
                left--
                progressed = true
            }
            if (!progressed) break
        }
        // Volta para a ordem do edital, que é a ordem em que as questões aparecem na prova.
        return topics.filter { it.topicId in counts }.map { topic ->
            BlueprintItem(topic.topicId, topic.subjectId, topic.subjectName, topic.path, counts.getValue(topic.topicId), topic.scope)
        }
    }

    /** Partes de até 30 questões; um item que não cabe inteiro é dividido entre duas partes. */
    private fun split(items: List<BlueprintItem>): List<BlueprintPart> {
        val parts = mutableListOf<BlueprintPart>()
        var current = mutableListOf<BlueprintItem>()
        var size = 0
        for (item in items) {
            var remaining = item.count
            while (remaining > 0) {
                val room = MAX_PER_PART - size
                val take = minOf(room, remaining)
                current += item.copy(count = take)
                size += take
                remaining -= take
                if (size == MAX_PER_PART) {
                    parts += BlueprintPart(current)
                    current = mutableListOf()
                    size = 0
                }
            }
        }
        if (current.isNotEmpty()) parts += BlueprintPart(current)
        return parts
    }
}

/** Formato das questões pela banca: Cebraspe e Quadrix são Certo/Errado; as demais, A a E. */
object BoardStyle {
    private fun key(value: String) = stripAccents(value.lowercase())

    fun styleFor(board: String?): String {
        val name = board?.let(::key) ?: return "FIVE_OPTIONS"
        return if (listOf("cebraspe", "cespe", "quadrix").any { it in name }) "TRUE_FALSE" else "FIVE_OPTIONS"
    }

    fun styleLabel(style: String): String = when (style) {
        "TRUE_FALSE" -> "Certo ou Errado"
        "FOUR_OPTIONS" -> "Múltipla escolha, A a D"
        "MIXED" -> "Misto"
        else -> "Múltipla escolha, A a E"
    }

    /** Tempo de prova: 3 min por questão de múltipla escolha, 2 min por item Certo/Errado. */
    fun minutesFor(style: String, questions: Int): Int =
        (questions * if (style == "TRUE_FALSE") 2.0 else 3.0).roundToInt().coerceAtLeast(10)

    val KNOWN_BOARDS = listOf("Cebraspe", "FGV", "FCC", "Vunesp", "Cesgranrio", "Quadrix", "IBFC", "Instituto AOCP", "Idecan", "Consulplan", "Fundatec", "Iades")
}

/** O que a pessoa já fez, para decidir o que liberar. */
data class SimulationReadiness(
    val leafTopics: Int,
    val studiedTopics: Int,
    val coveredTopics: Int,
    val studyDays: Int,
    val wrongInFinished: Int,
) {
    val coverage: Double get() = if (leafTopics == 0) 0.0 else coveredTopics.toDouble() / leafTopics
}

data class SimulationLock(val unlocked: Boolean, val progress: Float, val requirement: String)

/**
 * Regras de liberação. Simulado com pouca matéria não diz nada, então o do que foi estudado exige
 * base mínima, e a prova completa exige ter passado pela maior parte do edital.
 */
object SimulationUnlock {
    const val STUDIED_MIN_TOPICS = 5
    const val STUDIED_MIN_DAYS = 3
    const val FULL_MIN_COVERAGE = 0.5
    const val FULL_MIN_DAYS = 10

    fun lock(mode: SimulationMode, readiness: SimulationReadiness): SimulationLock = when (mode) {
        SimulationMode.DIAGNOSTIC -> SimulationLock(readiness.leafTopics > 0, 1f, "Importe o edital para liberar")
        SimulationMode.STUDIED -> {
            val topics = readiness.coveredTopics.coerceAtMost(STUDIED_MIN_TOPICS)
            val days = readiness.studyDays.coerceAtMost(STUDIED_MIN_DAYS)
            SimulationLock(
                readiness.coveredTopics >= STUDIED_MIN_TOPICS && readiness.studyDays >= STUDIED_MIN_DAYS,
                (topics.toFloat() / STUDIED_MIN_TOPICS + days.toFloat() / STUDIED_MIN_DAYS) / 2f,
                "Estude $STUDIED_MIN_TOPICS tópicos em $STUDIED_MIN_DAYS dias diferentes (${readiness.coveredTopics}/$STUDIED_MIN_TOPICS tópicos · ${readiness.studyDays}/$STUDIED_MIN_DAYS dias)",
            )
        }
        SimulationMode.FULL -> {
            val coverage = (readiness.coverage / FULL_MIN_COVERAGE).coerceAtMost(1.0)
            val days = readiness.studyDays.coerceAtMost(FULL_MIN_DAYS).toDouble() / FULL_MIN_DAYS
            val needTopics = ceil(readiness.leafTopics * FULL_MIN_COVERAGE).toInt()
            SimulationLock(
                readiness.coverage >= FULL_MIN_COVERAGE && readiness.studyDays >= FULL_MIN_DAYS,
                ((coverage + days) / 2).toFloat(),
                "Passe por metade do edital em $FULL_MIN_DAYS dias de estudo (${readiness.coveredTopics}/$needTopics tópicos · ${readiness.studyDays}/$FULL_MIN_DAYS dias)",
            )
        }
        SimulationMode.REMATCH -> SimulationLock(
            readiness.wrongInFinished > 0,
            if (readiness.wrongInFinished > 0) 1f else 0f,
            "Faça um simulado: as questões que você errar viram a revanche",
        )
    }

    fun sizes(mode: SimulationMode, maxPerMonthQuestions: Int?): List<Int> {
        val base = when (mode) {
            SimulationMode.DIAGNOSTIC -> listOf(20)
            SimulationMode.STUDIED -> listOf(20, 30, 60)
            SimulationMode.FULL -> listOf(60, 90, 120)
            SimulationMode.REMATCH -> listOf(30)
        }
        return if (maxPerMonthQuestions == null) base else base.filter { it <= maxPerMonthQuestions }.ifEmpty { base.take(1) }
    }

    /** Uma parte do servidor = até 30 questões. */
    fun partsFor(questions: Int): Int = ceil(questions / SimulationBlueprint.MAX_PER_PART.toDouble()).toInt()
}
