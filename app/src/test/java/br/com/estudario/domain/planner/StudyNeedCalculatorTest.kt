package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes do motor de necessidade.
 *
 * Um planejador sem IA só é confiável se for verificável. Estes testes cobrem a matriz
 * prioridade × dificuldade, os cenários de início e de reta final, e as invariantes que o plano
 * não pode violar em nenhuma combinação de entrada.
 */
class StudyNeedCalculatorTest {

    private fun need(
        priority: ExamPriority = ExamPriority.MEDIUM,
        difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL,
        knowledge: InitialKnowledge = InitialKnowledge.NONE,
        evidence: StudyEvidence = StudyEvidence.EMPTY,
        weights: PlannerWeights = PlannerWeights.DEFAULT,
        daysUntilExam: Int? = null,
        subjectId: Long = 1L,
    ) = StudyNeedCalculator.evaluate(
        subjectId = subjectId,
        dimensions = StudyDimensions(priority, difficulty, knowledge),
        evidence = evidence,
        weights = weights,
        daysUntilExam = daysUntilExam,
    )

    // --- Matriz prioridade × dificuldade -------------------------------------------------------

    @Test
    fun `prioridade alta com dificuldade alta produz a maior necessidade da matriz`() {
        val altaAlta = need(ExamPriority.VERY_HIGH, PersonalDifficulty.VERY_HARD)
        val altaBaixa = need(ExamPriority.VERY_HIGH, PersonalDifficulty.VERY_EASY)
        val baixaAlta = need(ExamPriority.VERY_LOW, PersonalDifficulty.VERY_HARD)
        val baixaBaixa = need(ExamPriority.VERY_LOW, PersonalDifficulty.VERY_EASY)

        assertTrue(altaAlta.score > altaBaixa.score)
        assertTrue(altaAlta.score > baixaAlta.score)
        assertTrue(baixaAlta.score > baixaBaixa.score)
        assertTrue(altaBaixa.score > baixaBaixa.score)
    }

    @Test
    fun `prioridade da prova vence dificuldade pessoal quando os dois eixos se opoem`() {
        // Banco de Dados: muito importante, fácil para a pessoa.
        val bancoDeDados = need(ExamPriority.VERY_HIGH, PersonalDifficulty.EASY)
        // Português: importância média, muito difícil para a pessoa.
        val portugues = need(ExamPriority.MEDIUM, PersonalDifficulty.VERY_HARD)

        // As duas precisam de espaço real, mas o edital continua mandando na fila.
        assertTrue(bancoDeDados.score > portugues.score)
        assertTrue(portugues.score > 0.3)
    }

    @Test
    fun `os tres eixos sao independentes e nenhum anula os outros`() {
        val base = need(ExamPriority.HIGH, PersonalDifficulty.NORMAL, InitialKnowledge.NONE)
        val maisDificil = need(ExamPriority.HIGH, PersonalDifficulty.VERY_HARD, InitialKnowledge.NONE)
        val maisConhecida = need(ExamPriority.HIGH, PersonalDifficulty.NORMAL, InitialKnowledge.STRONG)

        assertTrue("dificuldade precisa aumentar a necessidade", maisDificil.score > base.score)
        assertTrue("conhecimento prévio precisa reduzir a necessidade", maisConhecida.score < base.score)
    }

    // --- Evidência supera declaração ------------------------------------------------------------

    @Test
    fun `desempenho consistente reduz o peso da dificuldade declarada`() {
        val declarada = need(ExamPriority.HIGH, PersonalDifficulty.VERY_HARD)
        val comEvidencia = need(
            ExamPriority.HIGH,
            PersonalDifficulty.VERY_HARD,
            evidence = StudyEvidence(answered = 200, correct = 190, recentAnswered = 40, recentCorrect = 38),
        )

        assertTrue(
            "acertar 95% em 200 questões precisa derrubar a dificuldade declarada",
            comEvidencia.effectiveDifficulty < declarada.effectiveDifficulty * 0.5,
        )
        assertTrue(comEvidencia.score < declarada.score)
        assertTrue(
            comEvidencia.reasons.any { it.code == PlannerReasonCode.DIFFICULTY_RELAXED_BY_EVIDENCE },
        )
    }

    @Test
    fun `desempenho ruim aumenta a dificuldade mesmo quando a pessoa declarou facil`() {
        val declarada = need(ExamPriority.HIGH, PersonalDifficulty.VERY_EASY)
        val comEvidencia = need(
            ExamPriority.HIGH,
            PersonalDifficulty.VERY_EASY,
            evidence = StudyEvidence(answered = 120, correct = 48, recentAnswered = 30, recentCorrect = 12),
        )

        assertTrue(comEvidencia.effectiveDifficulty > declarada.effectiveDifficulty)
        assertTrue(comEvidencia.score > declarada.score)
        assertTrue(comEvidencia.reasons.any { it.code == PlannerReasonCode.DIFFICULTY_RAISED_BY_EVIDENCE })
    }

    @Test
    fun `um acerto isolado nao vira dominio`() {
        val umDeUm = StudyNeedCalculator.wilsonLowerBound(1, 1)
        val muitos = StudyNeedCalculator.wilsonLowerBound(190, 200)

        assertTrue("1 de 1 não pode valer como 100%", umDeUm < 0.40)
        assertTrue("190 de 200 precisa valer quase o que aparenta", muitos > 0.85)
        assertTrue(umDeUm < muitos)
    }

    @Test
    fun `amostra pequena mantem a confianca baixa e sinaliza o motivo`() {
        val pouco = need(
            ExamPriority.HIGH,
            evidence = StudyEvidence(answered = 4, correct = 4),
        )
        assertTrue("4 respostas não podem dar confiança alta", pouco.performanceConfidence < 0.20)
        assertTrue(pouco.reasons.any { it.code == PlannerReasonCode.INSUFFICIENT_SAMPLE })
    }

    @Test
    fun `um erro isolado nao muda materialmente o plano`() {
        val semErro = need(ExamPriority.HIGH, evidence = StudyEvidence(answered = 40, correct = 34))
        val comUmErro = need(ExamPriority.HIGH, evidence = StudyEvidence(answered = 41, correct = 34))

        assertTrue(
            "um erro a mais em 41 questões não pode reordenar a fila",
            kotlin.math.abs(comUmErro.score - semErro.score) < 0.03,
        )
    }

    // --- Cobertura x domínio --------------------------------------------------------------------

    @Test
    fun `cobertura e dominio sao coisas diferentes`() {
        // Coberto por inteiro, mas com desempenho ruim: continua precisando de atenção.
        val cobertoFraco = need(
            ExamPriority.HIGH,
            evidence = StudyEvidence(
                answered = 80, correct = 40, coveredTopics = 10, totalTopics = 10,
            ),
        )
        // Nada coberto, sem desempenho medido.
        val naoIniciado = need(
            ExamPriority.HIGH,
            evidence = StudyEvidence(coveredTopics = 0, totalTopics = 10),
        )

        assertEquals(0.0, cobertoFraco.remainingCoverage, 0.001)
        assertEquals(1.0, naoIniciado.remainingCoverage, 0.001)
        assertTrue("100% coberto com 50% de acerto ainda tem déficit de domínio", cobertoFraco.masteryDeficit > 0.4)
        assertTrue(cobertoFraco.reasons.any { it.code == PlannerReasonCode.CONTENT_COVERED })
        assertTrue(naoIniciado.reasons.any { it.code == PlannerReasonCode.CONTENT_NOT_STARTED })
    }

    @Test
    fun `usuario comecando do zero e usuario avancado recebem necessidades diferentes`() {
        val doZero = need(
            ExamPriority.HIGH,
            knowledge = InitialKnowledge.NONE,
            evidence = StudyEvidence(coveredTopics = 0, totalTopics = 20),
        )
        val avancado = need(
            ExamPriority.HIGH,
            knowledge = InitialKnowledge.STRONG,
            evidence = StudyEvidence(
                answered = 300, correct = 270, recentAnswered = 60, recentCorrect = 55,
                coveredTopics = 20, totalTopics = 20,
            ),
        )
        assertTrue(doZero.score > avancado.score)
        assertTrue(avancado.effectiveMastery > 0.8)
        assertTrue(doZero.effectiveMastery < 0.2)
    }

    // --- Prova: perto, longe, ausente ------------------------------------------------------------

    @Test
    fun `prova proxima aumenta a separacao entre prioridades`() {
        val longeAlta = need(ExamPriority.VERY_HIGH, daysUntilExam = 400)
        val longeBaixa = need(ExamPriority.VERY_LOW, daysUntilExam = 400)
        val pertoAlta = need(ExamPriority.VERY_HIGH, daysUntilExam = 10)
        val pertoBaixa = need(ExamPriority.VERY_LOW, daysUntilExam = 10)

        val distanciaLonge = longeAlta.score - longeBaixa.score
        val distanciaPerto = pertoAlta.score - pertoBaixa.score
        assertTrue("perto da prova o peso do edital precisa separar mais", distanciaPerto > distanciaLonge)
        assertTrue(pertoAlta.reasons.any { it.code == PlannerReasonCode.EXAM_APPROACHING })
    }

    @Test
    fun `sem data de prova o motor continua funcionando`() {
        val semData = need(ExamPriority.HIGH, daysUntilExam = null)
        assertTrue(semData.score > 0.0)
        assertTrue(semData.reasons.none { it.code == PlannerReasonCode.EXAM_APPROACHING })
    }

    // --- Fases -----------------------------------------------------------------------------------

    @Test
    fun `a fase muda a enfase sem trocar o algoritmo`() {
        val naoIniciado = StudyEvidence(coveredTopics = 0, totalTopics = 10)
        val cobertoFraco = StudyEvidence(answered = 100, correct = 55, coveredTopics = 10, totalTopics = 10)

        val baseNovo = need(evidence = naoIniciado, weights = PlannerWeights.forPhase(PlanPhaseKind.BASE))
        val baseFraco = need(evidence = cobertoFraco, weights = PlannerWeights.forPhase(PlanPhaseKind.BASE))
        val finalNovo = need(evidence = naoIniciado, weights = PlannerWeights.forPhase(PlanPhaseKind.RETA_FINAL))
        val finalFraco = need(evidence = cobertoFraco, weights = PlannerWeights.forPhase(PlanPhaseKind.RETA_FINAL))

        assertTrue("na base, conteúdo novo domina", baseNovo.score > baseFraco.score)
        assertTrue("na reta final, ponto fraco domina", finalFraco.score > finalNovo.score)
    }

    // --- Atrasos e revisões ----------------------------------------------------------------------

    @Test
    fun `revisao vencida e tarefa perdida elevam a necessidade sem estourar o limite`() {
        val normal = need(ExamPriority.MEDIUM)
        val atrasado = need(
            ExamPriority.MEDIUM,
            evidence = StudyEvidence(overdueReviews = 6, missedTasks = 9, daysSinceContact = 120),
        )
        assertTrue(atrasado.score > normal.score)
        assertTrue(atrasado.score <= 1.0)
        assertTrue(atrasado.reasons.any { it.code == PlannerReasonCode.REVISION_OVERDUE })
        assertTrue(atrasado.reasons.any { it.code == PlannerReasonCode.TASKS_MISSED })
        assertTrue(atrasado.reasons.any { it.code == PlannerReasonCode.LONG_TIME_WITHOUT_CONTACT })
    }

    // --- Invariantes ------------------------------------------------------------------------------

    @Test
    fun `o score fica sempre dentro dos limites em toda a matriz de entradas`() {
        ExamPriority.entries.forEach { priority ->
            PersonalDifficulty.entries.forEach { difficulty ->
                InitialKnowledge.entries.forEach { knowledge ->
                    listOf(
                        StudyEvidence.EMPTY,
                        StudyEvidence(answered = 1, correct = 1),
                        StudyEvidence(answered = 500, correct = 0, coveredTopics = 30, totalTopics = 30),
                        StudyEvidence(
                            answered = 500, correct = 500, recentAnswered = 100, recentCorrect = 100,
                            coveredTopics = 30, totalTopics = 30, overdueReviews = 50, missedTasks = 50,
                            daysSinceContact = 900,
                        ),
                    ).forEach { evidence ->
                        val result = need(priority, difficulty, knowledge, evidence)
                        assertTrue(
                            "score fora dos limites para $priority/$difficulty/$knowledge",
                            result.score >= PlannerWeights.MINIMUM_ACTIVE_NEED && result.score <= 1.0,
                        )
                        assertTrue(result.effectiveDifficulty in 0.0..1.0)
                        assertTrue(result.effectiveMastery in 0.0..1.0)
                        assertTrue(result.masteryDeficit in 0.0..1.0)
                        assertTrue(result.performanceConfidence in 0.0..1.0)
                    }
                }
            }
        }
    }

    @Test
    fun `nenhuma materia ativa cai a zero`() {
        val irrelevante = need(
            ExamPriority.VERY_LOW,
            PersonalDifficulty.VERY_EASY,
            InitialKnowledge.STRONG,
            StudyEvidence(answered = 400, correct = 400, coveredTopics = 10, totalTopics = 10),
        )
        assertTrue(
            "matéria de peso baixo recebe menos atenção, nunca nenhuma",
            irrelevante.score >= PlannerWeights.MINIMUM_ACTIVE_NEED,
        )
    }

    @Test
    fun `a mesma entrada produz sempre o mesmo resultado`() {
        val evidence = StudyEvidence(answered = 77, correct = 51, recentAnswered = 21, recentCorrect = 12, coveredTopics = 4, totalTopics = 9)
        repeat(50) {
            val a = need(ExamPriority.HIGH, PersonalDifficulty.HARD, InitialKnowledge.BASIC, evidence, daysUntilExam = 63)
            val b = need(ExamPriority.HIGH, PersonalDifficulty.HARD, InitialKnowledge.BASIC, evidence, daysUntilExam = 63)
            assertEquals(a.score, b.score, 0.0)
            assertEquals(a.reasons.map { it.code }, b.reasons.map { it.code })
        }
    }

    @Test
    fun `a ordenacao da fila e determinista inclusive no empate`() {
        val identicos = (1L..20L).map { id ->
            StudyNeedCalculator.evaluate(subjectId = id, dimensions = StudyDimensions(ExamPriority.MEDIUM))
        }
        val primeira = identicos.shuffled(java.util.Random(1)).sortedWith(StudyNeedCalculator.queueComparator())
        val segunda = identicos.shuffled(java.util.Random(2)).sortedWith(StudyNeedCalculator.queueComparator())
        assertEquals(primeira.map { it.subjectId }, segunda.map { it.subjectId })
    }

    @Test
    fun `alterar prioridade ou dificuldade muda o plano de forma monotonica`() {
        val scores = ExamPriority.entries.map { need(it).score }
        assertEquals(scores.sorted(), scores)
        assertNotEquals(scores.first(), scores.last())

        val porDificuldade = PersonalDifficulty.entries.map { need(ExamPriority.MEDIUM, it).score }
        assertEquals(porDificuldade.sorted(), porDificuldade)

        // Mais conhecimento prévio, menos necessidade, na ordem inversa do enum.
        val porConhecimento = InitialKnowledge.entries.reversed()
            .map { need(ExamPriority.MEDIUM, PersonalDifficulty.NORMAL, it).score }
        assertEquals(porConhecimento.sorted(), porConhecimento)
    }

    @Test
    fun `a versao do algoritmo viaja com o resultado`() {
        assertEquals(PlannerWeights.ALGORITHM_VERSION, need().algorithmVersion)
    }

    @Test
    fun `sempre existe pelo menos um motivo explicavel`() {
        ExamPriority.entries.forEach { priority ->
            val result = need(priority)
            assertTrue("sem motivo para $priority", result.reasons.isNotEmpty())
            assertTrue(result.topReasons(3).size <= 3)
        }
    }
}
