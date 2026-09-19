package br.com.estudario.domain

import br.com.estudario.domain.planner.PlanTaskType
import java.time.LocalDate

enum class BadgeCategory(val label: String) {
    PLANO("Plano de estudos"),
    SEQUENCIA("Sequência"),
    METAS("Metas diárias"),
    QUESTOES("Questões"),
    PRECISAO("Pontaria"),
    TOPICOS("Tópicos"),
    EDITAL("Edital"),
    REVISOES("Revisões"),
    SIMULADOS("Simulados"),
    DISCURSIVAS("Discursivas"),
    MARATONA("Maratona"),
}

data class Badge(
    val id: String,
    val category: BadgeCategory,
    val tier: Int,
    val name: String,
    val requirement: String,
    val target: Int,
    /** Sufixo mostrado no progresso ("dias", "questões", "%"). */
    val unit: String,
)

data class BadgeProgress(
    val badge: Badge,
    val current: Int,
    val hint: String? = null,
) {
    val target: Int get() = badge.target
    val earned: Boolean get() = current >= target
    val percent: Float get() = if (target <= 0) 1f else (current.toFloat() / target).coerceIn(0f, 1f)
    val remaining: Int get() = (target - current).coerceAtLeast(0)
}

/**
 * Emblemas. Toda conquista sai do histórico real, então nada é perdido ao trocar de aparelho nem
 * ganho por engano — se o backup voltar, os emblemas voltam junto.
 *
 * As faixas sobem devagar de propósito: o primeiro emblema de cada categoria é fácil (serve de
 * convite), o último é coisa de quem levou a preparação inteira até o fim.
 */
object BadgeCatalog {

    private fun tiers(
        category: BadgeCategory,
        prefix: String,
        unit: String,
        entries: List<Triple<Int, String, String>>,
    ) = entries.mapIndexed { index, (target, name, requirement) ->
        Badge("$prefix-$target", category, index + 1, name, requirement, target, unit)
    }

    val plano = tiers(
        BadgeCategory.PLANO, "plano", "tarefas",
        listOf(
            Triple(10, "Saiu do papel", "Concluir 10 tarefas do plano"),
            Triple(50, "No ritmo", "Concluir 50 tarefas do plano"),
            Triple(200, "Cronograma na veia", "Concluir 200 tarefas do plano"),
            Triple(500, "Máquina de cumprir", "Concluir 500 tarefas do plano"),
            Triple(1_000, "Plano é lei", "Concluir 1.000 tarefas do plano"),
        ),
    )

    val sequencia = tiers(
        BadgeCategory.SEQUENCIA, "seq", "dias",
        listOf(
            Triple(3, "Três de três", "3 dias seguidos batendo a meta"),
            Triple(7, "Semana cheia", "7 dias seguidos batendo a meta"),
            Triple(14, "Quinzena firme", "14 dias seguidos"),
            Triple(30, "Um mês sem falhar", "30 dias seguidos"),
            Triple(60, "Dois meses de pé", "60 dias seguidos"),
            Triple(100, "Cem dias", "100 dias seguidos"),
            Triple(365, "Um ano inteiro", "365 dias seguidos"),
        ),
    )

    val metas = tiers(
        BadgeCategory.METAS, "meta", "dias",
        listOf(
            Triple(1, "Primeiro dia", "Bater a meta do dia uma vez"),
            Triple(10, "Dez dias no alvo", "Bater a meta em 10 dias"),
            Triple(50, "Cinquenta no alvo", "Bater a meta em 50 dias"),
            Triple(100, "Centena de metas", "Bater a meta em 100 dias"),
            Triple(250, "Rotina blindada", "Bater a meta em 250 dias"),
        ),
    )

    val questoes = tiers(
        BadgeCategory.QUESTOES, "quest", "questões",
        listOf(
            Triple(100, "Primeira centena", "Responder 100 questões"),
            Triple(500, "Meio milhar", "Responder 500 questões"),
            Triple(2_000, "Dois mil", "Responder 2.000 questões"),
            Triple(5_000, "Cinco mil", "Responder 5.000 questões"),
            Triple(10_000, "Dez mil", "Responder 10.000 questões"),
        ),
    )

    val precisao = tiers(
        BadgeCategory.PRECISAO, "acerto", "%",
        listOf(
            Triple(70, "Mão firme", "70% de acerto (mínimo 200 questões)"),
            Triple(80, "Pontaria boa", "80% de acerto (mínimo 200 questões)"),
            Triple(90, "Quase infalível", "90% de acerto (mínimo 200 questões)"),
        ),
    )

    val topicos = tiers(
        BadgeCategory.TOPICOS, "topico", "tópicos",
        listOf(
            Triple(10, "Dez tópicos", "Marcar 10 tópicos como estudados"),
            Triple(50, "Cinquenta tópicos", "Marcar 50 tópicos como estudados"),
            Triple(150, "Cento e cinquenta", "Marcar 150 tópicos como estudados"),
            Triple(400, "Enciclopédia", "Marcar 400 tópicos como estudados"),
        ),
    )

    val edital = tiers(
        BadgeCategory.EDITAL, "edital", "%",
        listOf(
            Triple(25, "Um quarto do caminho", "25% do edital estudado"),
            Triple(50, "Metade do edital", "50% do edital estudado"),
            Triple(75, "Três quartos", "75% do edital estudado"),
            Triple(100, "Edital fechado", "100% do edital estudado"),
        ),
    )

    val revisoes = tiers(
        BadgeCategory.REVISOES, "revisao", "revisões",
        listOf(
            Triple(25, "Revisor", "Concluir 25 revisões"),
            Triple(100, "Memória treinada", "Concluir 100 revisões"),
            Triple(400, "Nada se perde", "Concluir 400 revisões"),
        ),
    )

    val simulados = tiers(
        BadgeCategory.SIMULADOS, "simulado", "simulados",
        listOf(
            Triple(1, "Primeiro simulado", "Concluir 1 simulado do plano"),
            Triple(5, "Cinco simulados", "Concluir 5 simulados do plano"),
            Triple(20, "Vinte simulados", "Concluir 20 simulados do plano"),
            Triple(50, "Sala de prova é casa", "Concluir 50 simulados do plano"),
        ),
    )

    val discursivas = tiers(
        BadgeCategory.DISCURSIVAS, "discursiva", "discursivas",
        listOf(
            Triple(1, "Primeira folha", "Escrever 1 discursiva do plano"),
            Triple(5, "Cinco discursivas", "Escrever 5 discursivas do plano"),
            Triple(20, "Punho treinado", "Escrever 20 discursivas do plano"),
        ),
    )

    val maratona = tiers(
        BadgeCategory.MARATONA, "maratona", "min",
        listOf(
            Triple(120, "Duas horas", "2 horas de estudo em um único dia"),
            Triple(240, "Quatro horas", "4 horas de estudo em um único dia"),
            Triple(360, "Seis horas", "6 horas de estudo em um único dia"),
            Triple(480, "Dia inteiro", "8 horas de estudo em um único dia"),
        ),
    )

    val all: List<Badge> = plano + sequencia + metas + questoes + precisao + topicos +
        edital + revisoes + simulados + discursivas + maratona

    fun evaluate(
        input: ProgressEngine.ProgressInput,
        planByDate: Map<LocalDate, List<ProgressEngine.PlanWork>>,
    ): List<BadgeProgress> {
        val planWork = planByDate.values.flatten()
        val goalDays = input.days.count { StreakEngine.isDone(it, input.goal) }
        val accuracy = if (input.totalQuestions == 0) 0 else input.correctQuestions * 100 / input.totalQuestions
        val editalPercent = if (input.topicsTotal == 0) 0 else input.topicsStudied * 100 / input.topicsTotal
        val maiorDia = input.days.maxOfOrNull { it.minutes } ?: 0
        val simulados = planWork.count { it.type == PlanTaskType.SIMULATION }
        val discursivas = planWork.count { it.type == PlanTaskType.DISCURSIVE }
        val faltamParaValer = (200 - input.totalQuestions).coerceAtLeast(0)

        return all.map { badge ->
            when (badge.category) {
                BadgeCategory.PLANO -> BadgeProgress(badge, planWork.size)
                BadgeCategory.SEQUENCIA -> BadgeProgress(badge, input.bestStreak)
                BadgeCategory.METAS -> BadgeProgress(badge, goalDays)
                BadgeCategory.QUESTOES -> BadgeProgress(badge, input.totalQuestions)
                BadgeCategory.PRECISAO -> BadgeProgress(
                    badge,
                    if (input.totalQuestions >= 200) accuracy else 0,
                    hint = if (faltamParaValer > 0) "Responda mais $faltamParaValer questões para este emblema começar a contar." else null,
                )
                BadgeCategory.TOPICOS -> BadgeProgress(badge, input.topicsStudied)
                BadgeCategory.EDITAL -> BadgeProgress(
                    badge,
                    editalPercent,
                    hint = if (input.topicsTotal == 0) "Importe ou monte o edital para começar a contar." else null,
                )
                BadgeCategory.REVISOES -> BadgeProgress(badge, input.reviewsCompleted)
                BadgeCategory.SIMULADOS -> BadgeProgress(badge, simulados)
                BadgeCategory.DISCURSIVAS -> BadgeProgress(badge, discursivas)
                BadgeCategory.MARATONA -> BadgeProgress(badge, maiorDia)
            }
        }
    }
}
