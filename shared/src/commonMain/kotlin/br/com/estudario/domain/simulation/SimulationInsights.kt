package br.com.estudario.domain.simulation

/**
 * Leitura do resultado do simulado além da nota: onde a pessoa perdeu pontos (os tópicos com mais
 * erros e a armadilha de cada um) e em que nível de dificuldade. Igual no app e no site.
 */
object SimulationInsights {
    /** Uma questão corrigida. [correct] nulo = em branco. */
    data class Answered(val topicId: Long, val difficulty: String?, val explanation: String, val correct: Boolean?)

    data class LevelScore(val level: String, val label: String, val right: Int, val total: Int) {
        val percent: Int get() = if (total == 0) 0 else right * 100 / total
    }

    data class LostTopic(val topicId: Long, val lost: Int, val total: Int, val trap: String?)

    data class Reading(val levels: List<LevelScore>, val lostTopics: List<LostTopic>, val advice: String?)

    private val LEVELS = listOf("FACIL" to "Fáceis", "MEDIA" to "Médias", "DIFICIL" to "Difíceis")
    private val TRAP = Regex("""\*\*(?:Armadilha|Pegadinha):\*\*\s*(.+)""")

    /** A armadilha que a explicação traz ("Armadilha:" das questões da IA, "Pegadinha:" das da biblioteca). */
    fun trapOf(explanation: String): String? =
        TRAP.find(explanation)?.groupValues?.get(1)?.trim()?.trimEnd('.')?.takeIf { it.length >= 8 }?.take(220)

    fun read(answers: List<Answered>): Reading {
        val levels = LEVELS.mapNotNull { (level, label) ->
            val list = answers.filter { it.difficulty == level }
            if (list.isEmpty()) null else LevelScore(level, label, list.count { it.correct == true }, list.size)
        }
        // Errada e em branco contam igual como ponto perdido; o tópico com mais pontos perdidos vem primeiro.
        val lostTopics = answers.groupBy { it.topicId }
            .map { (topicId, list) ->
                val missed = list.filter { it.correct != true }
                LostTopic(topicId, missed.size, list.size, missed.firstNotNullOfOrNull { trapOf(it.explanation) })
            }
            .filter { it.lost > 0 }
            .sortedWith(compareByDescending<LostTopic> { it.lost }.thenByDescending { it.lost.toDouble() / it.total })
            .take(5)
        return Reading(levels, lostTopics, advice(levels))
    }

    /** Errar fácil é atenção e base; errar só difícil é aprofundar. */
    private fun advice(levels: List<LevelScore>): String? {
        val easy = levels.firstOrNull { it.level == "FACIL" } ?: return null
        val hard = levels.firstOrNull { it.level == "DIFICIL" }
        return when {
            easy.total >= 3 && easy.percent < 70 ->
                "Você perdeu pontos em questões fáceis: são as mais baratas de recuperar. Revise a base desses tópicos antes de avançar."
            hard != null && hard.total >= 3 && hard.percent < 50 && easy.percent >= 80 ->
                "A base está firme; o que separa você da aprovação são as difíceis. Treine exceções e casos-limite dos tópicos abaixo."
            else -> null
        }
    }
}
