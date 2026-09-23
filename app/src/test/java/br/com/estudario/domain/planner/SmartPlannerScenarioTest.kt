package br.com.estudario.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Cenários de ponta a ponta do Smart Planner: revisão adaptativa, volume de questões, estimativa
 * de carga e viabilidade. São os testes que garantem que o plano se comporta como um planejador,
 * e não como uma divisão de matérias pelos dias.
 */
class SmartPlannerScenarioTest {

    private val hoje: LocalDate = LocalDate.of(2026, 3, 2)

    private fun need(
        priority: ExamPriority = ExamPriority.MEDIUM,
        difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL,
        knowledge: InitialKnowledge = InitialKnowledge.NONE,
        evidence: StudyEvidence = StudyEvidence.EMPTY,
        subjectId: Long = 1L,
        topicId: Long? = null,
    ) = StudyNeedCalculator.evaluate(
        subjectId = subjectId,
        topicId = topicId,
        dimensions = StudyDimensions(priority, difficulty, knowledge),
        evidence = evidence,
    )

    private val config = StudyMethodConfig(blockMinutes = 50, questionsPerTopic = 15)

    private fun topics(count: Int, subjectId: Long = 1L, studied: Boolean = false) =
        (1..count).map { index ->
            BlueprintTopic(
                topicId = index.toLong(),
                subjectId = subjectId,
                title = "Tópico $index",
                position = index,
                studied = studied,
            )
        }

    // --- Revisão adaptativa ---------------------------------------------------------------------

    @Test
    fun `topico dificil com desempenho baixo volta antes de topico facil e dominado`() {
        val fraco = need(
            ExamPriority.HIGH, PersonalDifficulty.VERY_HARD,
            evidence = StudyEvidence(answered = 80, correct = 36),
        )
        val forte = need(
            ExamPriority.HIGH, PersonalDifficulty.EASY,
            knowledge = InitialKnowledge.SOLID,
            evidence = StudyEvidence(answered = 80, correct = 74),
        )

        val revisaoFraco = RevisionScheduler.next(stage = 2, lastContact = hoje, need = fraco, today = hoje)
        val revisaoForte = RevisionScheduler.next(stage = 2, lastContact = hoje, need = forte, today = hoje)

        assertTrue(
            "conteúdo fraco precisa voltar bem antes",
            revisaoFraco.intervalDays < revisaoForte.intervalDays,
        )
        assertTrue(revisaoFraco.dueDate.isBefore(revisaoForte.dueDate))
        assertTrue(RevisionScheduler.ease(fraco) < 1.0)
        assertTrue(RevisionScheduler.ease(forte) > 1.0)
    }

    @Test
    fun `a facilidade fica sempre dentro dos limites`() {
        ExamPriority.entries.forEach { priority ->
            PersonalDifficulty.entries.forEach { difficulty ->
                InitialKnowledge.entries.forEach { knowledge ->
                    val ease = RevisionScheduler.ease(need(priority, difficulty, knowledge))
                    assertTrue("ease fora dos limites: $ease", ease in 0.55..1.80)
                }
            }
        }
    }

    @Test
    fun `revisao atrasada nao ganha intervalo maior`() {
        val alvo = need(ExamPriority.HIGH, PersonalDifficulty.HARD)
        val emDia = RevisionScheduler.next(stage = 3, lastContact = hoje, need = alvo, today = hoje)
        val atrasada = RevisionScheduler.next(
            stage = 3, lastContact = hoje.minusDays(30), need = alvo, today = hoje, overdue = true,
        )
        assertTrue("atrasar não pode render intervalo maior", atrasada.intervalDays <= emDia.intervalDays)
        assertEquals("o estágio não avança quando a revisão venceu", 2, atrasada.stage)
        assertTrue(atrasada.reasons.any { it.code == PlannerReasonCode.REVISION_OVERDUE })
    }

    @Test
    fun `revisao nunca cai no passado nem depois da prova`() {
        val alvo = need(ExamPriority.HIGH)
        val prova = hoje.plusDays(20)
        val plano = RevisionScheduler.next(
            stage = 5, lastContact = hoje.minusDays(90), need = alvo, today = hoje, examDate = prova,
        )
        assertFalse("revisão não pode ser agendada no passado", plano.dueDate.isBefore(hoje))
        assertTrue("revisão não pode cair depois da prova", plano.dueDate.isBefore(prova))
        assertTrue(plano.reasons.any { it.code == PlannerReasonCode.EXAM_APPROACHING })
    }

    @Test
    fun `sem data de prova a revisao continua sendo agendada`() {
        val plano = RevisionScheduler.next(stage = 0, lastContact = hoje, need = need(), today = hoje)
        assertTrue(plano.intervalDays >= 1)
        assertTrue(plano.dueDate.isAfter(hoje) || plano.dueDate == hoje)
    }

    @Test
    fun `a duracao da revisao e sempre valida`() {
        listOf(25, 50, 90).forEach { bloco ->
            ExamPriority.entries.forEach { priority ->
                val minutos = RevisionScheduler.minutesFor(need(priority), bloco)
                assertTrue("duração inválida: $minutos", minutos in 10..bloco)
            }
        }
    }

    // --- Questões -------------------------------------------------------------------------------

    @Test
    fun `o volume de questoes acompanha a maturidade do conteudo`() {
        val novo = QuestionAllocator.allocate(15, need(), StudyEvidence.EMPTY, covered = false)
        val consolidando = QuestionAllocator.allocate(
            15, need(), StudyEvidence(answered = 40, correct = 30, coveredTopics = 1, totalTopics = 1), covered = true,
        )
        val fraco = QuestionAllocator.allocate(
            15,
            need(evidence = StudyEvidence(answered = 60, correct = 24)),
            StudyEvidence(answered = 60, correct = 24),
            covered = true,
        )
        val forte = QuestionAllocator.allocate(
            15,
            need(evidence = StudyEvidence(answered = 200, correct = 190)),
            StudyEvidence(answered = 200, correct = 190),
            covered = true,
        )

        assertEquals(ContentMaturity.NEW, novo.maturity)
        assertEquals(ContentMaturity.CONSOLIDATING, consolidando.maturity)
        assertEquals(ContentMaturity.WEAK, fraco.maturity)
        assertEquals(ContentMaturity.STRONG, forte.maturity)

        assertTrue("recém-aprendido recebe bateria menor", novo.questions < consolidando.questions)
        assertTrue("ponto fraco recebe mais questões", fraco.questions > consolidando.questions)
        assertTrue("conteúdo dominado entra só em manutenção", forte.questions < consolidando.questions)
    }

    @Test
    fun `amostra pequena nao classifica ninguem como fraco nem como dominado`() {
        val poucasErradas = StudyEvidence(answered = 3, correct = 0)
        val poucasCertas = StudyEvidence(answered = 3, correct = 3)
        assertEquals(
            ContentMaturity.CONSOLIDATING,
            QuestionAllocator.maturityOf(need(evidence = poucasErradas), poucasErradas, covered = true),
        )
        assertEquals(
            ContentMaturity.CONSOLIDATING,
            QuestionAllocator.maturityOf(need(evidence = poucasCertas), poucasCertas, covered = true),
        )
    }

    @Test
    fun `conteudo fraco volta antes e conteudo dominado nao e antecipado`() {
        val fraco = need(evidence = StudyEvidence(answered = 60, correct = 24))
        assertNotNull(QuestionAllocator.returnIntervalDays(ContentMaturity.WEAK, fraco))
        assertNull(QuestionAllocator.returnIntervalDays(ContentMaturity.STRONG, fraco))
        assertTrue(
            QuestionAllocator.returnIntervalDays(ContentMaturity.WEAK, fraco)!! <
                QuestionAllocator.returnIntervalDays(ContentMaturity.CONSOLIDATING, fraco)!!,
        )
    }

    @Test
    fun `quantidade de questoes nunca fica invalida`() {
        listOf(0, 5, 15, 40).forEach { base ->
            ExamPriority.entries.forEach { priority ->
                PersonalDifficulty.entries.forEach { difficulty ->
                    val alocacao = QuestionAllocator.allocate(
                        base, need(priority, difficulty), StudyEvidence.EMPTY, covered = false,
                    )
                    assertTrue("quantidade negativa", alocacao.questions >= 0)
                    if (base == 0) assertEquals(0, alocacao.questions)
                }
            }
        }
    }

    // --- Carga ------------------------------------------------------------------------------------

    @Test
    fun `a carga de um topico inclui teoria consolidacao e revisao`() {
        val estimativa = WorkloadEstimator.estimate(topics(1), config, emptyMap())
        val item = estimativa.items.single()
        assertEquals(50, item.theoryMinutes)
        assertEquals(30, item.questionMinutes)
        assertEquals(75, item.revisionMinutes)
        assertEquals(155, item.totalMinutes)
        assertFalse(estimativa.calibrated)
    }

    @Test
    fun `a calibracao so entra com historico suficiente`() {
        assertEquals(1.0, WorkloadEstimator.calibrationFactor(50, 70, sampleSize = 2), 0.0)
        val comHistorico = WorkloadEstimator.calibrationFactor(50, 70, sampleSize = 20)
        assertEquals(1.4, comHistorico, 0.001)

        val estimativa = WorkloadEstimator.estimate(topics(1), config, emptyMap(), calibrationFactor = comHistorico)
        assertTrue(estimativa.calibrated)
        assertTrue(
            "a projeção precisa crescer quando a pessoa leva mais tempo",
            estimativa.totalMinutes > WorkloadEstimator.estimate(topics(1), config, emptyMap()).totalMinutes,
        )
    }

    @Test
    fun `nenhum topico se perde na estimativa`() {
        val lista = topics(137)
        val estimativa = WorkloadEstimator.estimate(lista, config, emptyMap())
        assertEquals(137, estimativa.items.size)
        assertEquals(lista.map { it.topicId }.toSet(), estimativa.items.map { it.topicId }.toSet())
    }

    // --- Viabilidade ------------------------------------------------------------------------------

    private fun analise(
        topicCount: Int,
        weeklyMinutes: Int,
        examInDays: Long?,
    ): FeasibilityReport {
        val lista = topics(topicCount)
        val carga = WorkloadEstimator.estimate(lista, config, emptyMap())
        return PlanFeasibilityAnalyzer.analyze(
            today = hoje,
            examDate = examInDays?.let { hoje.plusDays(it) },
            planStart = hoje,
            weeklyCapacityMinutes = weeklyMinutes,
            workload = carga,
            topicCount = topicCount,
        )
    }

    @Test
    fun `tempo folgado reserva fase de consolidacao`() {
        val relatorio = analise(topicCount = 20, weeklyMinutes = 840, examInDays = 180)
        assertEquals(FeasibilityVerdict.COMFORTABLE, relatorio.verdict)
        assertEquals(0, relatorio.deficitMinutes)
        assertTrue(relatorio.options.isEmpty())
        assertNotNull(relatorio.targetCoverageDate)
        assertTrue(
            "a meta de conteúdo precisa ficar antes da prova",
            relatorio.targetCoverageDate!!.isBefore(relatorio.examDate!!),
        )
    }

    @Test
    fun `agenda apertada avisa e oferece caminhos sem decidir sozinha`() {
        val relatorio = analise(topicCount = 50, weeklyMinutes = 360, examInDays = 180)
        assertEquals(FeasibilityVerdict.TIGHT, relatorio.verdict)
        assertTrue(relatorio.deficitMinutes > 0)
        assertTrue(relatorio.options.isNotEmpty())
        assertTrue(relatorio.options.any { it is FeasibilityOption.IncreaseWeeklyLoad })
        assertTrue(relatorio.options.any { it is FeasibilityOption.PrioritizeByWeight })
    }

    @Test
    fun `quando o edital nao cabe o app diz e nao finge`() {
        val relatorio = analise(topicCount = 100, weeklyMinutes = 360, examInDays = 180)
        assertEquals(FeasibilityVerdict.INFEASIBLE, relatorio.verdict)

        val aumento = relatorio.options.filterIsInstance<FeasibilityOption.IncreaseWeeklyLoad>().single()
        assertTrue("o aumento sugerido precisa ser concreto", aumento.extraWeeklyMinutes > 0)
        assertEquals("a sugestão vem arredondada em quartos de hora", 0, aumento.extraWeeklyMinutes % 15)

        val priorizar = relatorio.options.filterIsInstance<FeasibilityOption.PrioritizeByWeight>().single()
        assertTrue(priorizar.coveredShare > 0.0 && priorizar.coveredShare < 1.0)
    }

    @Test
    fun `a margem antes da prova e calculada e nao um numero fixo`() {
        val curta = PlanFeasibilityAnalyzer.consolidationDays(totalPreparationDays = 60, topicCount = 30)
        val longa = PlanFeasibilityAnalyzer.consolidationDays(totalPreparationDays = 500, topicCount = 30)
        val editalGrande = PlanFeasibilityAnalyzer.consolidationDays(totalPreparationDays = 200, topicCount = 300)
        val editalPequeno = PlanFeasibilityAnalyzer.consolidationDays(totalPreparationDays = 200, topicCount = 10)

        assertTrue("preparação longa merece mais consolidação", longa > curta)
        assertTrue("edital maior merece mais consolidação", editalGrande > editalPequeno)
        assertTrue(curta >= PlanFeasibilityAnalyzer.MIN_CONSOLIDATION_DAYS)
        assertTrue(longa <= PlanFeasibilityAnalyzer.MAX_CONSOLIDATION_DAYS)
        assertTrue("nada de 30 dias fixos", curta != longa)
    }

    @Test
    fun `sem data de prova o plano continua valido`() {
        val relatorio = analise(topicCount = 40, weeklyMinutes = 600, examInDays = null)
        assertEquals(FeasibilityVerdict.NO_EXAM_DATE, relatorio.verdict)
        assertNull(relatorio.targetCoverageDate)
        assertNotNull("a projeção de conclusão continua existindo", relatorio.projectedCoverageDate)
        assertTrue(relatorio.options.isEmpty())
    }

    @Test
    fun `um unico dia disponivel e sete dias produzem projecoes coerentes`() {
        val umDia = analise(topicCount = 30, weeklyMinutes = 180, examInDays = 300)
        val seteDias = analise(topicCount = 30, weeklyMinutes = 1_260, examInDays = 300)

        assertNotNull(umDia.projectedCoverageDate)
        assertNotNull(seteDias.projectedCoverageDate)
        assertTrue(
            "mais capacidade precisa antecipar a conclusão",
            seteDias.projectedCoverageDate!!.isBefore(umDia.projectedCoverageDate!!),
        )
        assertEquals(umDia.estimatedRemainingWorkloadMinutes, seteDias.estimatedRemainingWorkloadMinutes)
    }

    @Test
    fun `sem capacidade nenhuma o motor nao inventa uma data`() {
        val relatorio = analise(topicCount = 30, weeklyMinutes = 0, examInDays = 200)
        assertNull(relatorio.projectedCoverageDate)
        assertEquals(FeasibilityVerdict.INFEASIBLE, relatorio.verdict)
    }

    @Test
    fun `nenhuma materia e excluida em silencio quando o tempo nao da`() {
        val materias = (1L..4L).flatMap { subjectId ->
            (1..25).map { index ->
                BlueprintTopic(
                    topicId = subjectId * 100 + index,
                    subjectId = subjectId,
                    title = "Tópico $index",
                    position = index,
                )
            }
        }
        val carga = WorkloadEstimator.estimate(materias, config, emptyMap())
        val necessidades = mapOf(
            1L to need(ExamPriority.VERY_HIGH, subjectId = 1L),
            2L to need(ExamPriority.HIGH, subjectId = 2L),
            3L to need(ExamPriority.LOW, subjectId = 3L),
            4L to need(ExamPriority.VERY_LOW, subjectId = 4L),
        )
        val relatorio = PlanFeasibilityAnalyzer.analyze(
            today = hoje,
            examDate = hoje.plusDays(120),
            planStart = hoje,
            weeklyCapacityMinutes = 300,
            workload = carga,
            topicCount = materias.size,
            needsBySubject = necessidades,
        )

        val priorizar = relatorio.options.filterIsInstance<FeasibilityOption.PrioritizeByWeight>().single()
        assertTrue("o que não cabe precisa ficar visível", priorizar.atRiskSubjectIds.isNotEmpty())
        assertTrue(
            "a matéria de maior peso não pode ser a sacrificada",
            1L !in priorizar.atRiskSubjectIds,
        )
        assertTrue(
            "a de menor peso é a primeira a entrar em risco",
            4L in priorizar.atRiskSubjectIds,
        )
    }

    @Test
    fun `a analise de viabilidade e deterministica`() {
        repeat(20) {
            val a = analise(topicCount = 77, weeklyMinutes = 430, examInDays = 213)
            val b = analise(topicCount = 77, weeklyMinutes = 430, examInDays = 213)
            assertEquals(a.verdict, b.verdict)
            assertEquals(a.projectedCoverageDate, b.projectedCoverageDate)
            assertEquals(a.targetCoverageDate, b.targetCoverageDate)
            assertEquals(a.deficitMinutes, b.deficitMinutes)
        }
    }

    // --- Explicabilidade ---------------------------------------------------------------------------

    @Test
    fun `toda materia consegue explicar por que esta no plano`() {
        ExamPriority.entries.forEach { priority ->
            PersonalDifficulty.entries.forEach { difficulty ->
                val texto = PlanningExplanationBuilder.explain("Banco de Dados", need(priority, difficulty))
                assertTrue("explicação vazia para $priority/$difficulty", texto.isNotBlank())
                assertTrue(texto.startsWith("Banco de Dados"))
                assertTrue(texto.endsWith("."))
            }
        }
    }

    @Test
    fun `a devolutiva do assistente muda conforme a combinacao dos eixos`() {
        val dificilEImportante = PlanningExplanationBuilder.wizardFeedback(
            "Português", StudyDimensions(ExamPriority.VERY_HIGH, PersonalDifficulty.VERY_HARD, InitialKnowledge.NONE),
        )
        val facilEImportante = PlanningExplanationBuilder.wizardFeedback(
            "Banco de Dados", StudyDimensions(ExamPriority.VERY_HIGH, PersonalDifficulty.EASY, InitialKnowledge.SOLID),
        )
        assertTrue(dificilEImportante.contains("Português"))
        assertTrue(facilEImportante.contains("Banco de Dados"))
        assertTrue("as duas respostas não podem ser a mesma", dificilEImportante != facilEImportante)
    }

    @Test
    fun `o texto de viabilidade nunca promete o que nao cabe`() {
        val impossivel = analise(topicCount = 100, weeklyMinutes = 360, examInDays = 180)
        val texto = PlanningExplanationBuilder.feasibility(impossivel)
        assertTrue(texto.contains("ultrapassa a data da prova"))
        impossivel.options.forEach {
            assertTrue(PlanningExplanationBuilder.describeOption(it).isNotBlank())
        }
    }
}
