package br.com.estudario.ui.setup

import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty

/*
 * Textos e regras do passo "Ajuste só o que for diferente para você" (peso, esforço e quanto já
 * sabe, por matéria). Muita gente não entendia o que cada medidor mudava; aqui moram a explicação
 * do "?", a frase do Folha quando um valor muda e a prévia "No plano: …". Sem Compose, para testar.
 */

/** Os três medidores de cada matéria. */
internal enum class TuningAxis(val title: String, val help: String) {
    PRIORITY(
        "Peso na prova",
        "Quanto essa matéria vale na sua prova. Peso maior faz ela aparecer mais vezes na semana; peso menor libera tempo para as outras.",
    ),
    EFFORT(
        "Esforço para você",
        "O quanto ela costuma dar trabalho para você. Difícil ganha mais tempo e mais questões por tópico; fácil anda mais rápido.",
    ),
    KNOWLEDGE(
        "O quanto você já sabe",
        "O que você já estudou dela. Quem nunca viu começa pela teoria completa; quem já domina vai direto para questões e revisão.",
    ),
}

internal object SubjectTuning {
    /**
     * A força da cor de cada barrinha (0 a 1): a 1ª é clarinha e a 5ª é a cor cheia, para o nível
     * ler de longe, sem precisar contar barras. [count] barrinhas no total.
     */
    fun intensity(index: Int, count: Int): Float {
        if (count <= 1) return 1f
        val i = index.coerceIn(0, count - 1)
        return 0.28f + 0.72f * i / (count - 1)
    }

    /** A frase curta do Folha quando a pessoa muda um medidor. */
    fun reaction(axis: TuningAxis, level: Int, subject: String): String = when (axis) {
        TuningAxis.PRIORITY -> when (level) {
            0, 1 -> "$subject com peso baixo: sobra tempo para o que vale mais."
            2 -> "$subject com peso médio, no ritmo do edital."
            else -> "$subject pesa muito! Ela vai aparecer mais na sua semana."
        }
        TuningAxis.EFFORT -> when (level) {
            0, 1 -> "Fácil para você? Então $subject anda mais rápido."
            2 -> "Esforço normal em $subject, sem mudança no tempo."
            else -> "Combinado: $subject ganha mais tempo e mais questões."
        }
        TuningAxis.KNOWLEDGE -> when (level) {
            0, 1 -> "Tudo bem! Em $subject a gente começa pela teoria."
            2 -> "Boa! Em $subject a teoria vem mais curtinha."
            else -> "Que beleza! Em $subject você vai direto para as questões."
        }
    }

    /** A parte do peso na prévia "No plano: …"; o resto vem de StudyPlanBlueprint.knowledgeEffect. */
    fun priorityEffect(priority: ExamPriority): String = when (priority) {
        ExamPriority.VERY_LOW, ExamPriority.LOW -> "Aparece menos vezes na semana."
        ExamPriority.MEDIUM -> "Aparece no ritmo normal da semana."
        ExamPriority.HIGH, ExamPriority.VERY_HIGH -> "Aparece mais vezes na semana."
    }

    /** A prévia inteira de uma matéria, atualizada a cada toque. */
    fun planPreview(priority: ExamPriority, difficulty: PersonalDifficulty, knowledge: InitialKnowledge): String =
        br.com.estudario.domain.planner.StudyPlanBlueprint.knowledgeEffect(knowledge, difficulty) + " " + priorityEffect(priority)
}
