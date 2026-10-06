package br.com.estudario.domain.setup

import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.planner.ExamPriority
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.toExamPriority
import br.com.estudario.domain.planner.toPlanPriority

/**
 * Tradução entre o que o assistente coleta e o que o motor consome.
 *
 * O ponto central deste arquivo é o que ele **deixou de fazer**: até a versão anterior existia aqui
 * um `effectivePriority(official, difficulty)` que elevava a prioridade da prova quando a pessoa
 * marcava a matéria como difícil, gravando os dois conceitos numa propriedade só.
 *
 * Isso destruía informação. Depois de fundir, o plano não conseguia mais distinguir:
 *
 * - Banco de Dados, prioridade muito alta, dificuldade baixa; e
 * - Português, prioridade média, dificuldade muito alta,
 *
 * porque as duas chegavam ao motor como "prioridade alta". O resultado era gastar com Português o
 * tempo que a prova cobra em Banco de Dados, e não haver como explicar por quê.
 *
 * Agora os eixos viajam separados até o fim: a prioridade da prova define o peso do rodízio, e a
 * dificuldade pessoal entra no NeedScore, onde ela pode ser revista pela evidência. O que a
 * dificuldade nunca mais faz é mudar o peso que o edital dá à matéria.
 */

/** Converte a dificuldade do setup antigo (3 níveis) para a escala de 5 do motor. */
fun SubjectDifficulty.toPersonalDifficulty(): PersonalDifficulty = when (this) {
    SubjectDifficulty.EASY -> PersonalDifficulty.EASY
    SubjectDifficulty.MEDIUM -> PersonalDifficulty.NORMAL
    SubjectDifficulty.HARD -> PersonalDifficulty.HARD
}

/** Caminho inverso, para telas que ainda mostram os três níveis. */
fun PersonalDifficulty.toSubjectDifficulty(): SubjectDifficulty = when (this) {
    PersonalDifficulty.VERY_EASY, PersonalDifficulty.EASY -> SubjectDifficulty.EASY
    PersonalDifficulty.NORMAL -> SubjectDifficulty.MEDIUM
    PersonalDifficulty.HARD, PersonalDifficulty.VERY_HARD -> SubjectDifficulty.HARD
}

fun PlanPriority.planningRank(): Int = when (this) {
    PlanPriority.LOW -> 1
    PlanPriority.MEDIUM -> 2
    PlanPriority.HIGH -> 3
    PlanPriority.CRITICAL -> 4
}

fun PriorityLevel.toPlanPriority(): PlanPriority = toExamPriority().toPlanPriority()

/**
 * A prioridade **da prova**, e só ela.
 *
 * Existe para deixar explícito no código que nada além do edital entra nesta conta. Se em algum
 * momento aparecer um parâmetro de dificuldade nesta função, a regressão voltou.
 */
fun examPriorityOf(official: PriorityLevel): ExamPriority = official.toExamPriority()

@Deprecated(
    message = "Fundia prioridade da prova com dificuldade pessoal e apagava a diferença entre os " +
        "dois eixos. Passe os dois separados: examPriority para o peso do edital e " +
        "personalDifficulty para o StudyNeedCalculator.",
    replaceWith = ReplaceWith("official"),
    level = DeprecationLevel.ERROR,
)
fun effectivePriority(official: PlanPriority, difficulty: SubjectDifficulty): PlanPriority = official
