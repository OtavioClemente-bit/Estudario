package br.com.estudario.data

import androidx.room.withTransaction
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.Difficulty
import br.com.estudario.data.local.ErrorConceptEntity
import br.com.estudario.data.local.ErrorConceptEntryCrossRef
import br.com.estudario.data.local.ErrorNotebookEntryEntity
import br.com.estudario.data.local.ErrorStatus
import br.com.estudario.data.local.ExamProfileEntity
import br.com.estudario.data.local.FocusSessionEntity
import br.com.estudario.data.local.FocusSessionOrigin
import br.com.estudario.data.local.Priority
import br.com.estudario.data.local.QuestionAttemptEntity
import br.com.estudario.data.local.QuestionEntity
import br.com.estudario.data.local.QuestionOptionEntity
import br.com.estudario.data.local.QuestionSessionEntity
import br.com.estudario.data.local.QuestionSessionType
import br.com.estudario.data.local.ReviewHistoryEntity
import br.com.estudario.data.local.ReviewScheduleEntity
import br.com.estudario.data.local.SimulationEntity
import br.com.estudario.data.local.SnippetKind
import br.com.estudario.data.local.StudySessionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.SummaryEntity
import br.com.estudario.data.local.SummaryKind
import br.com.estudario.data.local.TheoryDocumentEntity
import br.com.estudario.data.local.TheoryMarkEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.data.local.TopicSnippetEntity
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.data.local.UserNoteEntity
import br.com.estudario.data.local.planner.AvailabilityMode
import br.com.estudario.data.local.planner.PlanTaskEntity
import br.com.estudario.data.local.planner.StudyAvailabilityEntity
import br.com.estudario.data.local.planner.StudyTaskExecutionEntity
import br.com.estudario.data.planner.CreatePlanInput
import br.com.estudario.data.planner.PlanSubjectInput
import br.com.estudario.domain.ReviewIntervals
import br.com.estudario.domain.planner.InitialKnowledge
import br.com.estudario.domain.planner.PersonalDifficulty
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType
import br.com.estudario.domain.planner.ReplanReason
import br.com.estudario.domain.setup.InitialSetupStatus
import br.com.estudario.domain.setup.InitialSetupStep
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneId
import java.util.Random

/**
 * Massa de teste "de verdade" para o app Estudário Teste: o concurso do dono (TRT 3ª Região, TI),
 * seis semanas de estudo, teorias grandes com grifos, ~400 respostas, caderno de erros, revisões,
 * plano ativo com a missão do dia e simulados. Serve para medir desempenho como um usuário real.
 *
 * Disparo: `adb shell am start -n br.com.estudario.teste/br.com.estudario.MainActivity --ez seedReal true`.
 * Pode rodar de novo: tudo o que ele cria leva externalId "seed:" e é apagado antes de recriar.
 */
object RealSeed {
    private const val COMPETITION_ID = "seed:trt3-ti"
    private const val DAY = 86_400_000L
    private const val FOCUS_COUNT = 14

    private val SUBJECTS: List<Triple<String, PlanPriority, List<String>>> = listOf(
        Triple("Língua Portuguesa", PlanPriority.HIGH, listOf(
            "Compreensão e interpretação de textos", "Tipologia e gêneros textuais", "Ortografia oficial", "Acentuação gráfica",
            "Emprego das classes de palavras", "Concordância verbal e nominal", "Regência verbal e nominal", "Emprego do sinal indicativo de crase",
            "Colocação pronominal", "Pontuação", "Coesão e coerência textual", "Reescrita de frases e parágrafos", "Significação das palavras",
        )),
        Triple("Raciocínio Lógico-Matemático", PlanPriority.MEDIUM, listOf(
            "Proposições e conectivos", "Tabelas-verdade", "Equivalências lógicas", "Negação de proposições", "Argumentação lógica",
            "Diagramas lógicos", "Princípio fundamental da contagem", "Arranjos e combinações", "Probabilidade",
            "Razão e proporção", "Porcentagem", "Sequências numéricas", "Problemas de lógica",
        )),
        Triple("Direito Administrativo", PlanPriority.MEDIUM, listOf(
            "Princípios da Administração Pública", "Organização administrativa", "Poderes administrativos", "Atos administrativos",
            "Agentes públicos", "Lei 8.112/1990: provimento e vacância", "Lei 8.112/1990: direitos e vantagens", "Regime disciplinar",
            "Processo administrativo (Lei 9.784/1999)", "Licitações (Lei 14.133/2021)", "Contratos administrativos", "Improbidade administrativa",
            "Responsabilidade civil do Estado",
        )),
        Triple("Direito Constitucional", PlanPriority.MEDIUM, listOf(
            "Princípios fundamentais", "Direitos e deveres individuais e coletivos", "Direitos sociais", "Nacionalidade", "Direitos políticos",
            "Organização político-administrativa", "Administração Pública na Constituição", "Poder Legislativo", "Poder Executivo",
            "Poder Judiciário", "Justiça do Trabalho na Constituição", "Funções essenciais à Justiça", "Controle de constitucionalidade",
        )),
        Triple("Direito do Trabalho", PlanPriority.LOW, listOf(
            "Princípios do Direito do Trabalho", "Relação de emprego", "Empregado e empregador", "Contrato individual de trabalho",
            "Jornada de trabalho", "Férias", "Remuneração e salário", "Alteração do contrato", "Suspensão e interrupção",
            "Extinção do contrato", "Aviso prévio", "FGTS", "Estabilidade e garantias de emprego",
        )),
        Triple("Direito Processual do Trabalho", PlanPriority.LOW, listOf(
            "Organização da Justiça do Trabalho", "Competência da Justiça do Trabalho", "Partes e procuradores", "Atos e prazos processuais",
            "Petição inicial e audiência", "Defesa do reclamado", "Provas no processo do trabalho", "Sentença", "Recursos trabalhistas",
            "Recurso ordinário", "Recurso de revista", "Execução trabalhista", "Procedimento sumaríssimo",
        )),
        Triple("Engenharia de Software", PlanPriority.CRITICAL, listOf(
            "Processos de software", "Metodologias ágeis: Scrum", "Kanban e Lean", "Engenharia de requisitos", "UML: diagramas estruturais",
            "UML: diagramas comportamentais", "Padrões de projeto (GoF)", "Arquitetura em camadas e microsserviços", "Testes de software",
            "DevOps e integração contínua", "Qualidade de software (ISO 25010)", "Gerência de configuração com Git", "Métricas e ponto de função",
        )),
        Triple("Banco de Dados", PlanPriority.CRITICAL, listOf(
            "Modelo entidade-relacionamento", "Modelo relacional", "Normalização", "Álgebra relacional", "SQL: DDL", "SQL: DML e consultas",
            "SQL: junções e subconsultas", "Transações e propriedades ACID", "Controle de concorrência", "Índices e otimização de consultas",
            "Bancos NoSQL", "Data Warehouse e OLAP", "Big Data e Data Lake",
        )),
        Triple("Redes de Computadores", PlanPriority.HIGH, listOf(
            "Modelo OSI", "Arquitetura TCP/IP", "Endereçamento IPv4 e sub-redes", "IPv6", "Protocolos de roteamento", "TCP e UDP",
            "DNS", "HTTP e HTTPS", "Ethernet e switches", "VLAN", "Redes sem fio (802.11)", "Gerenciamento de redes (SNMP)", "Cabeamento estruturado",
        )),
        Triple("Segurança da Informação", PlanPriority.HIGH, listOf(
            "Princípios de segurança (CID)", "Criptografia simétrica", "Criptografia assimétrica", "Funções hash", "Certificação e assinatura digital",
            "Ataques e malwares", "Firewall, IDS e IPS", "VPN e IPsec", "Autenticação e controle de acesso", "Gestão de riscos (ISO 27005)",
            "ISO 27001 e 27002", "LGPD", "Backup e continuidade de negócios",
        )),
        Triple("Governança e Gestão de TI", PlanPriority.MEDIUM, listOf(
            "Governança de TI: conceitos", "COBIT 2019", "ITIL 4: conceitos e práticas", "ITIL 4: cadeia de valor de serviço",
            "Gerenciamento de projetos (PMBOK)", "Planejamento estratégico de TI (PDTI)", "Contratação de soluções de TI", "Gestão de processos (BPM)",
            "Indicadores e SLA", "Gestão de portfólio", "Transformação digital no setor público", "Estratégia Nacional de Governo Digital", "Arquitetura corporativa (TOGAF)",
        )),
    )

    suspend fun run(app: EstudarioApplication) {
        val db = app.database
        val dao = db.dao()
        val planner = db.plannerDao()
        val random = Random(31)
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val now = System.currentTimeMillis()
        fun dayAt(back: Int, hour: Int, minute: Int = 0): Long =
            today.minusDays(back.toLong()).atStartOfDay(zone).toInstant().toEpochMilli() + hour * 3_600_000L + minute * 60_000L

        // Rodar de novo: apaga o que a semente anterior criou (o resto cai em cascata com o concurso).
        dao.competitionByExternalId(COMPETITION_ID)?.let { app.repository.deleteCompetition(it) }
        repeat(FOCUS_COUNT) { dao.deleteFocusSession("seed:focus:$it") }

        // 1. Concurso, matérias e tópicos, pelos mesmos caminhos do assistente.
        val competitionId = dao.insertCompetition(CompetitionEntity(name = "TRT 3ª Região — Analista Judiciário (TI)", externalId = COMPETITION_ID))
        app.repository.setPrimary(competitionId)
        db.simulationDao().saveExamProfile(ExamProfileEntity(competitionId = competitionId, board = "FCC", boardSource = "USER", style = "FIVE_OPTIONS", targetPercent = 75))
        val subjects = mutableListOf<SubjectEntity>()
        val topicsBySubject = linkedMapOf<Long, List<TopicEntity>>()
        SUBJECTS.forEachIndexed { s, (name, _, titles) ->
            val subjectId = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = name, position = s, externalId = "seed:s:$s"))
            subjects += SubjectEntity(id = subjectId, competitionId = competitionId, name = name, position = s)
            topicsBySubject[subjectId] = titles.mapIndexed { t, title ->
                val id = dao.insertTopic(TopicEntity(subjectId = subjectId, title = title, position = t, externalId = "seed:t:$s:$t"))
                TopicEntity(id = id, subjectId = subjectId, title = title, position = t, externalId = "seed:t:$s:$t")
            }
        }
        val allTopics = topicsBySubject.values.flatten()

        // 2. ~25 tópicos estudados nas últimas seis semanas (os primeiros de cada matéria, como no plano).
        val studied = subjects.flatMapIndexed { s, subject ->
            topicsBySubject.getValue(subject.id).take(if (s in setOf(6, 7, 0)) 3 else 2)
        }.take(25)
        val studiedBack = studied.indices.associate { i -> studied[i].id to (41 - i * 41 / studied.size).coerceAtLeast(1) }
        val subjectName = subjects.associate { it.id to it.name }

        // Questões: 10 por tópico estudado, 3 nos 35 primeiros não estudados.
        val seededQuestions = mutableListOf<SeedQuestion>()
        db.withTransaction {
            studied.forEachIndexed { i, topic ->
                val back = studiedBack.getValue(topic.id)
                val studiedAt = dayAt(back, 19, 30)
                val subject = subjectName.getValue(topic.subjectId)
                val theory = theoryMarkdown(topic.title, subject, Random(topic.id))
                val theoryId = dao.insertTheory(TheoryDocumentEntity(topicId = topic.id, title = topic.title, markdown = theory, externalId = "seed:theory:${topic.id}", lastReadBlock = if (i % 4 == 0) 12 else -1, createdAt = studiedAt, updatedAt = studiedAt))
                if (i % 3 != 2) {
                    val blocks = theory.split("\n\n")
                    val markCount = 2 + (i % 5)
                    repeat(markCount) { m ->
                        val index = (6 + m * 9 + i).coerceAtMost(blocks.lastIndex)
                        dao.insertTheoryMark(TheoryMarkEntity(theoryId = theoryId, blockIndex = index, quote = blocks[index].take(400), note = if (m % 2 == 0) MARK_NOTES[(i + m) % MARK_NOTES.size] else "", color = m % 3, createdAt = studiedAt + m * 60_000L))
                    }
                }
                dao.insertSummary(SummaryEntity(topicId = topic.id, title = "Resumo: ${topic.title}", markdown = summaryMarkdown(topic.title, subject, false), externalId = "seed:sum:${topic.id}", isFavorite = i % 5 == 0, createdAt = studiedAt, updatedAt = studiedAt))
                dao.insertSummary(SummaryEntity(topicId = topic.id, title = "Resumo rápido: ${topic.title}", markdown = summaryMarkdown(topic.title, subject, true), externalId = "seed:sumr:${topic.id}", kind = SummaryKind.RAPIDO, createdAt = studiedAt, updatedAt = studiedAt))
                dao.insertSnippet(TopicSnippetEntity(topicId = topic.id, kind = SnippetKind.BIZU, text = "Em ${topic.title}, a banca costuma cobrar a regra geral antes da exceção: decore a regra e as duas exceções clássicas.", isFavorite = i % 2 == 0, externalId = "seed:bizu:${topic.id}"))
                dao.insertSnippet(TopicSnippetEntity(topicId = topic.id, kind = SnippetKind.PEGADINHA, text = "Pegadinha em ${topic.title}: trocar \"sempre\" por \"em regra\" deixa a assertiva errada.", isFavorite = i % 3 == 0, externalId = "seed:peg:${topic.id}", position = 1))
                repeat(3) { r ->
                    dao.insertSnippet(TopicSnippetEntity(topicId = topic.id, kind = SnippetKind.RECUPERACAO, text = "Pergunta ${r + 1} de ${topic.title}: qual o ponto central do item ${r + 1}?", answer = "O item ${r + 1} de ${topic.title} define o conceito, a regra geral e a exceção cobrada.", externalId = "flashcard:seed:${topic.id}:$r", position = 2 + r))
                }
                if (i % 4 == 0) dao.insertNote(UserNoteEntity(text = "Revisar ${topic.title} antes do simulado: errei a parte das exceções.", topicId = topic.id))
                repeat(10) { q -> seededQuestions += insertQuestion(dao, topic, subject, q, studiedAt, random) }
            }
            allTopics.filter { t -> studied.none { it.id == t.id } }.take(35).forEach { topic ->
                repeat(3) { q -> seededQuestions += insertQuestion(dao, topic, subjectName.getValue(topic.subjectId), q, now - 50 * DAY, random) }
            }
        }

        // 3. ~400 respostas ao longo de seis semanas, ~60% de acerto, e o caderno de erros que elas geram.
        val byTopic = seededQuestions.groupBy { it.topicId }
        data class Answer(val q: SeedQuestion, val key: String, val correct: Boolean, val at: Long)
        val answers = mutableListOf<Answer>()
        studied.forEach { topic ->
            val back = studiedBack.getValue(topic.id)
            val questions = byTopic.getValue(topic.id)
            repeat(16) { n ->
                val q = questions[n % questions.size]
                val daysBack = (back - n * back / 16).coerceAtLeast(0)
                if (daysBack in setOf(5, 12, 19, 26)) return@repeat // dias sem estudo, para a sequência ter buracos reais
                val chance = 0.48 + (41 - daysBack) * 0.006
                val correct = random.nextDouble() < chance
                val key = if (correct) q.correctKey else listOf("A", "B", "C", "D", "E").filter { it != q.correctKey }[random.nextInt(4)]
                answers += Answer(q, key, correct, dayAt(daysBack, 20, n * 3 % 60).coerceAtMost(now - 60_000L))
            }
        }
        db.withTransaction {
            answers.sortedBy { it.at }.forEach { a ->
                dao.insertAttempt(QuestionAttemptEntity(questionId = a.q.id, selectedKey = a.key, correct = a.correct, answeredAt = a.at, sessionId = "seed:treino:${a.at / DAY}"))
            }
            val concepts = mutableMapOf<Long, Long>()
            answers.groupBy { it.q.id }.forEach { (_, list) ->
                val ordered = list.sortedBy { it.at }
                val q = ordered.first().q
                val last = ordered.last()
                val wrong = ordered.filter { !it.correct }
                dao.updateQuestion(q.question.copy(id = q.id, answerCount = ordered.size, correctCount = ordered.size - wrong.size, errorCount = wrong.size, lastAnswer = last.key, lastAnsweredAt = last.at))
                if (wrong.isEmpty()) return@forEach
                val fixed = last.correct
                val entryId = dao.insertError(ErrorNotebookEntryEntity(
                    questionId = q.id, errorCount = wrong.size, retryCorrectCount = if (fixed) 1 else 0,
                    firstErrorAt = wrong.first().at, lastErrorAt = wrong.last().at, lastReviewedAt = if (fixed) last.at else null,
                    pending = !fixed, selectedAnswer = wrong.last().key, correctAnswer = q.correctKey,
                    status = when { fixed -> ErrorStatus.CORRIGIDO; wrong.size > 1 -> ErrorStatus.RECORRENTE; else -> ErrorStatus.NOVO },
                    retryStreak = if (fixed) 1 else 0,
                    nextRetryAt = if (fixed) last.at + 10 * DAY else wrong.last().at + 3 * DAY,
                    comment = if (wrong.size > 1) "Confundi a regra com a exceção de novo." else "",
                ))
                val conceptId = concepts.getOrPut(q.topicId) {
                    val title = allTopics.first { it.id == q.topicId }.title
                    dao.insertErrorConcept(ErrorConceptEntity(topicId = q.topicId, title = "Exceções de $title", summary = "Regra geral x exceções cobradas em $title.", externalId = "seed:conceito:${q.topicId}", priority = Priority.ALTA))
                }
                dao.insertErrorConceptEntry(ErrorConceptEntryCrossRef(conceptId, entryId))
            }
            concepts.forEach { (topicId, conceptId) ->
                val wrongs = answers.filter { it.q.topicId == topicId && !it.correct }
                val concept = dao.errorConceptByExternalId("seed:conceito:$topicId") ?: return@forEach
                dao.updateErrorConcept(concept.copy(id = conceptId, errorCount = wrongs.size, lastErrorAt = wrongs.maxOfOrNull { it.at }))
            }

            // 4. Status dos tópicos e revisões espaçadas: umas atrasadas, umas para hoje, umas futuras.
            studied.forEachIndexed { i, topic ->
                val back = studiedBack.getValue(topic.id)
                val studiedAt = dayAt(back, 19, 30)
                val studiedDay = today.minusDays(back.toLong())
                var reviewedAt: Long? = null
                val schedules = ReviewIntervals.days.mapIndexed { index, days ->
                    var due = studiedDay.plusDays(days)
                    // Alguns caem exatamente hoje, para a fila de revisão do dia ter o que mostrar.
                    if (i % 5 == 1 && due.isBefore(today) && index == ReviewIntervals.days.indexOfLast { !studiedDay.plusDays(it).isAfter(today) }) due = today
                    val dueAt = due.atStartOfDay(zone).toInstant().toEpochMilli()
                    // A cada três tópicos, a última revisão vencida fica pendente (atrasada).
                    val lastPast = index == ReviewIntervals.days.indexOfLast { studiedDay.plusDays(it).isBefore(today) }
                    val done = due.isBefore(today) && !(lastPast && i % 3 == 0)
                    val completedAt = if (done) dueAt + 20 * 3_600_000L else null
                    if (completedAt != null) reviewedAt = completedAt
                    ReviewScheduleEntity(topicId = topic.id, stage = index + 1, dueAt = dueAt, completedAt = completedAt, intervalDays = days, updatedAt = completedAt ?: studiedAt, questionCorrect = if (done) 3 else 0, questionTotal = if (done) 5 else 0)
                }
                dao.insertReviews(schedules)
                schedules.mapNotNull { it.completedAt }.forEach { dao.insertReviewHistory(ReviewHistoryEntity(topicId = topic.id, reviewedAt = it)) }
                dao.updateTopic(topic.copy(
                    status = if (reviewedAt != null) TopicStatus.REVISANDO else TopicStatus.ESTUDADO,
                    firstStudiedAt = studiedAt, lastStudiedAt = studiedAt, lastReviewedAt = reviewedAt,
                ))
                // Sessão de estudo do dia em que o tópico foi visto.
                val dayAnswers = answers.filter { it.q.topicId == topic.id && it.at / DAY == studiedAt / DAY }
                dao.insertStudySession(StudySessionEntity(
                    topicId = topic.id, startedAt = studiedAt - 75 * 60_000L, completedAt = studiedAt, competitionId = competitionId, subjectId = topic.subjectId,
                    durationSeconds = (45L + random.nextInt(60)) * 60L, questionCount = dayAnswers.size, correctCount = dayAnswers.count { it.correct }, wrongCount = dayAnswers.count { !it.correct },
                ))
            }
            // Treinos registrados como sessões de questões (Desempenho/histórico).
            answers.groupBy { it.at / DAY }.forEach { (day, list) ->
                dao.insertQuestionSession(QuestionSessionEntity(
                    id = "seed:treino:$day", type = QuestionSessionType.TOPIC, startedAt = list.minOf { it.at } - 120_000L, completedAt = list.maxOf { it.at },
                    durationSeconds = list.size * 95L, questionCount = list.size, correctCount = list.count { it.correct },
                    topicIdsText = list.map { it.q.topicId }.distinct().joinToString(","),
                ))
            }
        }

        // 7. Modo foco: sessões espalhadas, para sequência e estatísticas.
        repeat(FOCUS_COUNT) { n ->
            val topic = studied[(n * 2) % studied.size]
            val back = n * 3
            val start = dayAt(back, 7, 10)
            val minutes = 25L + (n % 4) * 15L
            dao.insertFocusSessionIfAbsent(FocusSessionEntity(id = "seed:focus:$n", title = topic.title, startedAt = start, completedAt = start + minutes * 60_000L, durationSeconds = minutes * 60L, subjectIdsText = topic.subjectId.toString(), origin = FocusSessionOrigin.MATERIA, topicId = topic.id))
        }

        // 6. Simulados: dois entregues e um em andamento.
        val simDao = db.simulationDao()
        listOf(Triple("Simulado 1 — diagnóstico", 30, 24), Triple("Simulado 2 — matérias estudadas", 30, 9), Triple("Simulado 3 — prova completa", 40, 0)).forEachIndexed { s, (title, count, back) ->
            val finished = s < 2
            val startedAt = if (finished) dayAt(back, 9) else now - 25 * 60_000L
            val simulationId = simDao.insertSimulation(SimulationEntity(
                competitionId = competitionId, mode = if (s == 0) "DIAGNOSTIC" else if (s == 1) "STUDIED" else "FULL", title = title, board = "FCC",
                plannedQuestions = count, timeLimitMinutes = count * 3, status = "READY", createdAt = startedAt - 5 * 60_000L, startedAt = startedAt,
            ))
            val picked = mutableMapOf<Long, String>()
            var correct = 0
            db.withTransaction {
                repeat(count) { n ->
                    val topic = if (s == 0) allTopics[(n * 7) % allTopics.size] else studied[(n * 3 + s) % studied.size]
                    val seeded = insertQuestion(dao, topic, subjectName.getValue(topic.subjectId), 100 + n, startedAt, random, simulationId)
                    val answered = finished || n < 14
                    if (!answered) return@repeat
                    val right = random.nextDouble() < (if (s == 0) 0.45 else 0.62)
                    val key = if (right) seeded.correctKey else listOf("A", "B", "C", "D", "E").filter { it != seeded.correctKey }[random.nextInt(4)]
                    picked[seeded.id] = key
                    if (finished) {
                        if (right) correct++
                        val at = startedAt + (n + 1) * 150_000L
                        dao.insertAttempt(QuestionAttemptEntity(questionId = seeded.id, selectedKey = key, correct = right, answeredAt = at, sessionId = "simulado-$simulationId"))
                        dao.updateQuestion(seeded.question.copy(id = seeded.id, answerCount = 1, correctCount = if (right) 1 else 0, errorCount = if (right) 0 else 1, lastAnswer = key, lastAnsweredAt = at))
                    }
                }
            }
            val simulation = simDao.simulation(simulationId) ?: return@forEachIndexed
            val elapsed = if (finished) count * 150L else 25 * 60L
            simDao.updateSimulation(simulation.copy(
                status = if (finished) "FINISHED" else "IN_PROGRESS",
                answersJson = JSONObject(picked.mapKeys { it.key.toString() }).toString(),
                finishedAt = if (finished) startedAt + elapsed * 1000L else null,
                elapsedSeconds = elapsed, answeredCount = picked.size, questionCount = count,
                correctCount = if (finished) correct else 0, scorePercent = if (finished) correct * 100 / count else null,
            ))
            if (finished) {
                val topics = dao.questionsOnce().filter { it.question.simulationId == simulationId }.map { it.question.topicId }.toSet()
                dao.insertQuestionSession(QuestionSessionEntity(
                    id = "simulado-$simulationId", type = QuestionSessionType.SIMULATION, startedAt = startedAt, completedAt = startedAt + elapsed * 1000L,
                    durationSeconds = elapsed, questionCount = count, correctCount = correct,
                    subjectIdsText = allTopics.filter { it.id in topics }.map { it.subjectId }.distinct().joinToString(","), topicIdsText = topics.joinToString(","),
                ))
            }
        }

        // 5. Plano ativo como o do assistente: 120 min seg–sex, 180 no sábado, domingo livre, prova em ~5 meses.
        val availability = listOf(120, 120, 120, 120, 120, 180, 0)
        val planId = app.planService.createPlan(CreatePlanInput(
            competitionId = competitionId,
            name = "Plano de TRT 3ª Região",
            objective = "Analista Judiciário — Apoio Especializado — Tecnologia da Informação",
            startDate = today.minusDays(21),
            examDate = today.plusDays(150),
            availability = availability.mapIndexed { index, minutes -> StudyAvailabilityEntity("pending", index + 1, minutes, unavailable = minutes <= 0, mode = AvailabilityMode.SIMPLE) },
            subjects = SUBJECTS.mapIndexed { index, (name, priority, _) ->
                PlanSubjectInput(
                    subjectId = subjects[index].id, name = name, priority = priority, minimumMaintenanceMinutes = 0, paused = false, position = index,
                    weight = when (priority) { PlanPriority.CRITICAL -> 5; PlanPriority.HIGH -> 4; PlanPriority.MEDIUM -> 3; PlanPriority.LOW -> 2 },
                    personalDifficulty = when (index) { 1 -> PersonalDifficulty.HARD; 5 -> PersonalDifficulty.HARD; 7 -> PersonalDifficulty.EASY; else -> PersonalDifficulty.NORMAL },
                    initialKnowledge = when (index) { 0 -> InitialKnowledge.BASIC; 6, 7 -> InitialKnowledge.SOLID; 4, 5 -> InitialKnowledge.NONE; else -> InitialKnowledge.CONTACT },
                )
            },
            active = true,
            masterPlan = true,
        ))
        app.planService.activate(planId)
        app.planService.markMaster(planId)
        app.planService.replan(planId, ReplanReason.INITIAL)

        // Duas tarefas dos dias anteriores já feitas, com execução, como quem segue o plano.
        val plan = planner.plan(planId)
        if (plan != null) {
            listOf(1, 2).forEach { back ->
                val topic = studied[studied.size - back]
                val start = dayAt(back, 19)
                val taskId = "seed:task:$back"
                planner.insertTasks(listOf(PlanTaskEntity(
                    id = taskId, planId = planId, competitionId = competitionId, subjectId = topic.subjectId, topicId = topic.id,
                    subjectNameSnapshot = subjectName.getValue(topic.subjectId), topicNameSnapshot = topic.title,
                    scheduledEpochDay = today.minusDays(back.toLong()).toEpochDay(), type = if (back == 1) PlanTaskType.QUESTIONS else PlanTaskType.THEORY,
                    plannedMinutes = 50, plannedQuestions = if (back == 1) 10 else 0, priority = PlanPriority.HIGH, status = PlanTaskStatus.CONCLUIDA,
                    createdRevision = plan.revision, updatedRevision = plan.revision, createdAt = start - DAY, updatedAt = start + 50 * 60_000L,
                )))
                planner.insertExecution(StudyTaskExecutionEntity(
                    id = "seed:exec:$back", planId = planId, taskId = taskId, competitionId = competitionId, subjectId = topic.subjectId, topicId = topic.id,
                    startedAt = start, completedAt = start + 52 * 60_000L, actualMinutes = 52, questionsDone = if (back == 1) 10 else 0, correctAnswers = if (back == 1) 7 else 0,
                ))
            }
        }

        // O app abre direto no plano, sem assistente nem guia.
        app.preferences.updateInitialSetup {
            it.copy(
                status = InitialSetupStatus.COMPLETED, step = InitialSetupStep.READY, competitionId = competitionId,
                competitionName = "TRT 3ª Região", role = "Analista Judiciário — TI", examDate = today.plusDays(150).toString(),
                availabilityMinutes = availability, lastValidPlanId = planId,
            )
        }
        app.preferences.setOnboardingCompleted(true)
    }

    private val MARK_NOTES = listOf(
        "Cai muito na FCC.", "Errei isso no simulado 1.", "Decorar a exceção.", "Comparar com o tópico anterior.", "Bizu: regra geral primeiro.",
    )

    private suspend fun insertQuestion(dao: br.com.estudario.data.local.AppDao, topic: TopicEntity, subject: String, n: Int, at: Long, random: Random, simulationId: Long? = null): SeedQuestion {
        val keys = listOf("A", "B", "C", "D", "E")
        val correctKey = keys[(topic.id.toInt() + n * 3) % 5]
        val prefix = if (simulationId == null) "seed:q" else "seed:sim:$simulationId"
        val question = QuestionEntity(
            topicId = topic.id, externalId = "$prefix:${topic.id}:$n", board = "FCC", agency = "TRT 3ª Região", year = 2015 + (n % 10),
            difficulty = Difficulty.entries[(n + topic.position) % 3], source = "Questão autoral de teste",
            statement = "($subject — ${topic.title}) Questão ${n + 1}. Considere a situação a seguir, comum em provas de tribunal: um servidor precisa aplicar a regra de ${topic.title.lowercase()} a um caso concreto em que há uma exceção expressa. Assinale a alternativa correta.",
            explanation = "A alternativa $correctKey está correta porque aplica a regra geral de ${topic.title} e respeita a exceção prevista. As demais trocam a regra pela exceção, ampliam o alcance da norma ou invertem a consequência jurídica/técnica.",
            importedAt = at, tagsText = topic.title, reviewAnchor = "Conceito", simulationId = simulationId,
        )
        val id = dao.insertQuestion(question)
        dao.insertOptions(keys.mapIndexed { index, key ->
            QuestionOptionEntity(questionId = id, key = key, isCorrect = key == correctKey, position = index, text = if (key == correctKey) {
                "Aplica-se a regra geral de ${topic.title.lowercase()}, ressalvada a exceção expressa, que afasta a consequência no caso descrito."
            } else OPTION_WRONG[(index + n + random.nextInt(3)) % OPTION_WRONG.size].replace("%s", topic.title.lowercase()))
        })
        return SeedQuestion(id, topic.id, correctKey, question)
    }

    private val OPTION_WRONG = listOf(
        "A exceção de %s se aplica a todos os casos, inclusive aos não previstos.",
        "Em %s, a regra geral só vale quando houver autorização prévia e expressa.",
        "Não há exceção em %s: a consequência é sempre a mesma, em qualquer hipótese.",
        "A consequência em %s depende apenas da vontade do agente, sem limite legal ou técnico.",
        "O caso descrito está fora do alcance de %s, pois trata de matéria diversa.",
        "Em %s, a exceção prevalece sobre a regra geral sempre que houver dúvida.",
    )

    private fun summaryMarkdown(topic: String, subject: String, quick: Boolean): String = buildString {
        appendLine("# $topic").appendLine()
        appendLine("Resumo de $subject para revisar em ${if (quick) "5" else "15"} minutos.").appendLine()
        val items = if (quick) 6 else 18
        repeat(items) { appendLine("- **Ponto ${it + 1}:** ${SENTENCES[it % SENTENCES.size].replace("%s", topic.lowercase())}") }
        appendLine()
        appendLine("> **Na prova:** a banca troca a regra geral pela exceção; leia o comando duas vezes.")
    }

    /** Teoria sintética de ~15–30 KB, com a cara das matérias da biblioteca: títulos, listas, tabela e quadros. */
    private fun theoryMarkdown(topic: String, subject: String, random: Random): String = buildString {
        val t = topic.lowercase()
        fun paragraph(sentences: Int) = (0 until sentences).joinToString(" ") { SENTENCES[random.nextInt(SENTENCES.size)].replace("%s", t) }
        appendLine("# $topic").appendLine()
        appendLine("Esta matéria de $subject cobre $t como o edital do TRT 3ª Região pede. ${paragraph(3)}").appendLine()
        val sections = 9 + random.nextInt(6)
        for (s in 1..sections) {
            appendLine("## $s. ${SECTION_TITLES[(s - 1) % SECTION_TITLES.size]}").appendLine()
            repeat(2 + random.nextInt(2)) { appendLine(paragraph(5 + random.nextInt(4))).appendLine() }
            when (s % 4) {
                0 -> {
                    appendLine("| Aspecto | Regra geral | Exceção |")
                    appendLine("|---|---|---|")
                    repeat(4) { appendLine("| Item ${it + 1} de $t | Vale para todos os casos comuns | Só quando a norma prevê expressamente |") }
                    appendLine()
                }
                1 -> {
                    repeat(5) { appendLine("- **Característica ${it + 1}:** ${SENTENCES[random.nextInt(SENTENCES.size)].replace("%s", t)}") }
                    appendLine()
                }
                2 -> {
                    appendLine("> **Atenção:** ${paragraph(2)}").appendLine()
                    repeat(4) { appendLine("${it + 1}. ${SENTENCES[random.nextInt(SENTENCES.size)].replace("%s", t)}") }
                    appendLine()
                }
                else -> appendLine("> **Dica:** ${paragraph(2)}").appendLine()
            }
            if (s % 3 == 0) {
                appendLine("### Exemplo comentado").appendLine()
                appendLine(paragraph(6)).appendLine()
            }
        }
        appendLine("## Para fechar").appendLine()
        appendLine(paragraph(5))
    }

    private val SECTION_TITLES = listOf(
        "Conceito e finalidade", "Elementos essenciais", "Classificações cobradas", "Regra geral", "Exceções e casos especiais",
        "Comparação com institutos próximos", "Jurisprudência e entendimento da banca", "Aplicação prática", "Erros comuns",
        "Como a FCC cobra", "Questões típicas", "Ligação com outros tópicos", "Revisão em tópicos", "Pontos de atenção",
    )

    private val SENTENCES = listOf(
        "O estudo de %s começa pela finalidade: entender para que a regra existe facilita lembrar seus limites na hora da prova.",
        "Em %s, a regra geral alcança a maior parte dos casos, e as exceções aparecem sempre de forma expressa no texto normativo ou técnico.",
        "A banca costuma explorar a diferença entre o que é obrigatório e o que é apenas recomendado em %s.",
        "Quando o enunciado traz uma palavra absoluta como sempre, nunca ou exclusivamente, desconfie: %s raramente admite absolutos.",
        "Um bom caminho para fixar %s é montar um quadro com conceito, requisitos, efeitos e exceções, e revisá-lo em voz alta.",
        "Na prática do tribunal, %s aparece em rotinas do dia a dia, o que explica a frequência com que o tema é cobrado.",
        "Os requisitos de %s são cumulativos: faltando qualquer um deles, a consequência prevista não se produz.",
        "Há uma distinção clássica em %s entre o plano da validade e o plano da eficácia, e as questões gostam de misturar os dois.",
        "Vale lembrar que %s se relaciona com tópicos vizinhos do edital, e muitas questões cobram a ligação entre eles.",
        "Ao resolver questões de %s, identifique primeiro o comando da questão, depois a regra aplicável e só então as alternativas.",
        "A doutrina majoritária trata %s de forma sistemática, e a banca segue essa linha na maior parte das provas recentes.",
        "Erros comuns em %s envolvem confundir prazos, competências e a ordem das etapas do procedimento.",
        "Em termos de tecnologia, %s exige compreender a camada em que o conceito atua e quais garantias ele oferece.",
        "O desempenho e a segurança andam juntos em %s: uma escolha que melhora um aspecto costuma ter custo no outro.",
        "Guarde a ideia central de %s em uma frase curta; ela resolve boa parte das assertivas de certo ou errado.",
        "Os exemplos de %s mais cobrados são os que aparecem na própria norma ou na documentação oficial do padrão.",
        "Releia os quadros de %s antes de cada simulado: a repetição espaçada reduz o esquecimento de forma consistente.",
        "Quando duas alternativas parecem corretas em %s, a mais completa e fiel ao texto oficial costuma ser a resposta.",
    )

    private data class SeedQuestion(val id: Long, val topicId: Long, val correctKey: String, val question: QuestionEntity)
}
