package br.com.estudario.data

import androidx.room.withTransaction
import br.com.estudario.data.local.*
import br.com.estudario.domain.ReviewIntervals
import br.com.estudario.domain.StudyQueueRules
import br.com.estudario.domain.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class StudyRepository(private val db: AppDatabase) {
    private val dao = db.dao()

    val competitions = dao.competitions()
    val subjects = dao.subjects()
    val topics = dao.topics()
    val sources = dao.sources()
    val importPackages = dao.importPackages()
    val summaries = dao.summaries()
    val snippets = dao.snippets()
    val theories = dao.theories()
    val theoryMarks = dao.theoryMarks()
    val questions = dao.questions()
    val attempts = dao.attempts()
    val errors = dao.errors()
    val errorConcepts = dao.errorConcepts()
    val errorConceptEntries = dao.errorConceptEntries()
    val reviews = dao.reviews()
    val reviewHistory = dao.reviewHistory()
    val reviewSessions = dao.reviewSessions()
    val queue = dao.queue()
    val queueEvents = dao.queueEvents()
    val studySessions = dao.sessions()
    val questionSessions = dao.questionSessions()

    suspend fun addCompetition(name: String) = dao.insertCompetition(CompetitionEntity(name = name.trim(), isPrimary = dao.competitionsOnce().isEmpty()))
    suspend fun setPrimary(id: Long) = dao.setPrimaryCompetition(id)
    suspend fun setCompetitionPriorityOverride(id: Long, override: PriorityLevel?) {
        dao.competitionsOnce().firstOrNull { it.id == id }?.let { dao.updateCompetition(it.copy(userPriorityOverride = override)) }
    }
    suspend fun deleteCompetition(value: CompetitionEntity) = db.withTransaction {
        val subjectIds = dao.subjectsFor(value.id).map { it.id }.toSet()
        val topicIds = dao.topicsOnce().filter { it.subjectId in subjectIds }.map { it.id }
        cleanupTopicReferences(topicIds)
        dao.deleteCompetition(value)
        val remaining = dao.competitionsOnce()
        if (remaining.isNotEmpty() && remaining.none { it.isPrimary }) {
            dao.setPrimaryCompetition(remaining.first().id)
        }
    }
    suspend fun addSubject(competitionId: Long, name: String) = dao.insertSubject(SubjectEntity(competitionId = competitionId, name = name.trim(), position = dao.subjectsFor(competitionId).size))
    suspend fun setSubjectPriorityOverride(id: Long, override: PriorityLevel?) {
        dao.subjectsOnce().firstOrNull { it.id == id }?.let { dao.updateSubject(it.copy(userPriorityOverride = override)) }
    }
    suspend fun deleteSubject(value: SubjectEntity) = db.withTransaction {
        val topicIds = dao.topicsFor(value.id).map { it.id }
        cleanupTopicReferences(topicIds)
        dao.deleteSubject(value)
    }
    suspend fun addTopic(subjectId: Long, title: String, parentId: Long? = null) = dao.insertTopic(TopicEntity(subjectId = subjectId, title = title.trim(), parentTopicId = parentId, position = dao.topicsFor(subjectId).size))
    suspend fun updateTopic(value: TopicEntity) = dao.updateTopic(value)
    suspend fun setTopicPriorityOverride(id: Long, override: PriorityLevel?) {
        dao.topicsOnce().firstOrNull { it.id == id }?.let { dao.updateTopic(it.copy(userPriorityOverride = override)) }
    }
    suspend fun deleteTopic(value: TopicEntity) = db.withTransaction {
        val allTopics = dao.topicsOnce()
        val ids = mutableSetOf(value.id)
        var changed: Boolean
        do {
            changed = false
            allTopics.forEach { topic ->
                if (topic.parentTopicId in ids && ids.add(topic.id)) changed = true
            }
        } while (changed)
        cleanupTopicReferences(ids.toList())
        dao.deleteTopic(value)
    }

    private suspend fun cleanupTopicReferences(topicIds: List<Long>) {
        if (topicIds.isEmpty()) return
        dao.deleteQueueForTopics(topicIds)
        dao.deleteReviewHistoryForTopics(topicIds)
        dao.deleteStudySessionsForTopics(topicIds)
        dao.deleteNotesForTopics(topicIds)
        dao.deleteQuestionTagsForTopics(topicIds)
    }
    suspend fun addSummary(topicId: Long, title: String, markdown: String) = dao.insertSummary(SummaryEntity(topicId = topicId, title = title.trim(), markdown = markdown))
    suspend fun updateSummary(value: SummaryEntity) = dao.updateSummary(value.copy(updatedAt = System.currentTimeMillis()))
    suspend fun deleteSummary(value: SummaryEntity) = dao.deleteSummary(value)
    suspend fun saveSnippet(value: TopicSnippetEntity) {
        if (value.id == 0L) dao.insertSnippet(value) else dao.updateSnippet(value.copy(updatedAt = System.currentTimeMillis()))
    }
    suspend fun deleteSnippet(value: TopicSnippetEntity) = dao.deleteSnippet(value)
    suspend fun updateTheoryProgress(value: TheoryDocumentEntity, block: Int) = dao.updateTheory(value.copy(lastReadBlock = block, updatedAt = System.currentTimeMillis()))
    suspend fun saveTheoryMark(value: TheoryMarkEntity) {
        if (value.id == 0L) dao.insertTheoryMark(value) else dao.updateTheoryMark(value)
    }
    suspend fun deleteTheoryMark(value: TheoryMarkEntity) = dao.deleteTheoryMark(value)

    suspend fun markStudied(topic: TopicEntity, intervals: List<Long> = ReviewIntervals.days) = db.withTransaction {
        val now = System.currentTimeMillis()
        dao.updateTopic(topic.copy(status = TopicStatus.ESTUDADO, firstStudiedAt = topic.firstStudiedAt ?: now, lastStudiedAt = now))
        val zone = ZoneId.systemDefault()
        val base = LocalDate.now(zone)
        val schedules = intervals.ifEmpty { ReviewIntervals.days }.mapIndexed { index, days ->
            ReviewScheduleEntity(topicId = topic.id, stage = index + 1, dueAt = base.plusDays(days).atStartOfDay(zone).toInstant().toEpochMilli())
        }
        dao.insertReviews(schedules)
    }

    /**
     * Desfaz o "marcar estudado". Tira o tópico do estado ESTUDADO e cancela as revisões que ainda
     * não foram feitas. O que já aconteceu de verdade, sessões de estudo e revisões concluídas,
     * não é apagado: se houver histórico, as datas de estudo são preservadas.
     */
    suspend fun unmarkStudied(topic: TopicEntity) = db.withTransaction {
        val temHistorico = dao.studySessionCount(topic.id) > 0 || dao.completedReviewCount(topic.id) > 0
        dao.deletePendingReviews(topic.id)
        dao.updateTopic(
            topic.copy(
                status = TopicStatus.NAO_ESTUDADO,
                firstStudiedAt = if (temHistorico) topic.firstStudiedAt else null,
                lastStudiedAt = if (temHistorico) topic.lastStudiedAt else null,
            ),
        )
    }

    suspend fun completeReview(
        review: ReviewScheduleEntity,
        difficulty: ReviewDifficulty = ReviewDifficulty.NORMAL,
        correct: Int = 0,
        total: Int = 0,
        recalled: Int = 0,
        forgotten: Int = 0,
        startedAt: Long = System.currentTimeMillis(),
    ) = db.withTransaction {
        val now = System.currentTimeMillis()
        dao.updateReview(review.copy(completedAt = now, perceivedDifficulty = difficulty, questionCorrect = correct, questionTotal = total))
        val doTopico = dao.reviewsOnce().filter { it.topicId == review.topicId }
        val next = doTopico
            .filter { it.stage > review.stage && it.completedAt == null && it.ignoredAt == null }
            .minByOrNull { it.stage }
        if (next != null) {
            val remaining = (next.dueAt - now).coerceAtLeast(86_400_000L)
            val adjustedDue = when (difficulty) {
                ReviewDifficulty.DIFICIL -> now + 86_400_000L
                ReviewDifficulty.NORMAL -> next.dueAt
                ReviewDifficulty.FACIL -> now + (remaining * 1.35).toLong()
            }
            if (adjustedDue != next.dueAt) dao.updateReview(next.copy(dueAt = adjustedDue))
        } else {
            // Revisão perpétua: sem este trecho o tópico saía do ciclo para sempre depois do D+30.
            // O intervalo dobra a cada estágio (60, 120, 240) e ainda responde à dificuldade sentida
            // e ao aproveitamento nas questões da própria revisão.
            val ordenadas = doTopico.sortedBy { it.stage }
            val nextStage = (ordenadas.lastOrNull()?.stage ?: review.stage) + 1
            // O intervalo anterior sai da própria agenda do tópico, então o ciclo intensivo
            // (D+1/D+3/D+7/D+14/D+30) continua daqui sem pular etapas.
            val intervaloAnterior = if (ordenadas.size >= 2) {
                (ordenadas[ordenadas.lastIndex].dueAt - ordenadas[ordenadas.lastIndex - 1].dueAt) / 86_400_000L
            } else {
                ReviewIntervals.days.last()
            }
            val dias = ReviewIntervals.adjustedIntervalDays(intervaloAnterior, difficulty, correct, total)
            val zone = ZoneId.systemDefault()
            val dueAt = LocalDate.now(zone).plusDays(dias).atStartOfDay(zone).toInstant().toEpochMilli()
            dao.insertReviews(listOf(ReviewScheduleEntity(topicId = review.topicId, stage = nextStage, dueAt = dueAt)))
        }
        dao.insertReviewHistory(ReviewHistoryEntity(topicId = review.topicId, reviewedAt = now))
        dao.insertReviewSession(ReviewSessionEntity(reviewId = review.id, topicId = review.topicId, startedAt = startedAt, completedAt = now, recalled = recalled, forgotten = forgotten, questionCorrect = correct, questionTotal = total, perceivedDifficulty = difficulty))
        dao.topic(review.topicId)?.let { dao.updateTopic(it.copy(status = TopicStatus.REVISANDO, lastReviewedAt = now)) }
    }

    suspend fun ignoreReview(review: ReviewScheduleEntity) = dao.updateReview(review.copy(ignoredAt = System.currentTimeMillis()))

    suspend fun enqueue(topicId: Long) = db.withTransaction {
        dao.insertQueue(StudyQueueEntity(topicId = topicId, position = dao.maxQueuePosition() + 1))
        dao.insertQueueEvent(QueueEventEntity(topicId = topicId, type = QueueEventType.ADICIONADO))
    }
    suspend fun updateQueue(item: StudyQueueEntity) = db.withTransaction {
        dao.updateQueue(item)
        dao.insertQueueEvent(QueueEventEntity(topicId = item.topicId, type = if (item.paused) QueueEventType.PAUSADO else QueueEventType.RETOMADO))
    }
    suspend fun removeQueue(item: StudyQueueEntity) = dao.deleteQueue(item)
    suspend fun completeQueue(item: StudyQueueEntity) = db.withTransaction {
        completeStudyInternal(item.topicId, System.currentTimeMillis(), "")
    }

    suspend fun completeStudy(topicId: Long, startedAt: Long, notes: String = "") = db.withTransaction {
        completeStudyInternal(topicId, startedAt, notes)
    }

    /**
     * Registra uma sessão de estudo medida (modo foco) sem mexer no status do tópico nem no plano.
     * É tempo real cronometrado, então entra na sequência e no histórico; XP continua vindo das
     * atividades concluídas, não do relógio correndo.
     */
    suspend fun logStudySession(topicId: Long?, startedAt: Long, completedAt: Long = System.currentTimeMillis(), notes: String = "") = db.withTransaction {
        val topic = topicId?.let { dao.topic(it) }
        val subject = topic?.let { current -> dao.subjectsOnce().firstOrNull { it.id == current.subjectId } }
        val topicQuestionIds = if (topic == null) emptySet() else dao.questionsOnce().filter { it.question.topicId == topic.id }.map { it.question.id }.toSet()
        val attempts = dao.attemptsOnce().filter { it.answeredAt in startedAt..completedAt && it.questionId in topicQuestionIds }
        dao.insertStudySession(StudySessionEntity(
            topicId = topic?.id ?: 0L,
            startedAt = startedAt,
            completedAt = completedAt,
            competitionId = subject?.competitionId,
            subjectId = subject?.id,
            durationSeconds = ((completedAt - startedAt) / 1000).coerceAtLeast(0),
            questionCount = attempts.size,
            correctCount = attempts.count { it.correct },
            wrongCount = attempts.count { !it.correct },
            notes = notes,
        ))
        if (topic != null && topic.status == TopicStatus.NAO_ESTUDADO) {
            dao.updateTopic(topic.copy(status = TopicStatus.EM_ESTUDO, firstStudiedAt = topic.firstStudiedAt ?: startedAt, lastStudiedAt = completedAt))
        } else if (topic != null) {
            dao.updateTopic(topic.copy(lastStudiedAt = completedAt))
        }
    }

    private suspend fun completeStudyInternal(topicId: Long, startedAt: Long, notes: String) {
        val now = System.currentTimeMillis()
        val topic = dao.topic(topicId) ?: return
        // Concluir é uma transição única. Depois que o tópico já foi estudado, um toque repetido
        // não cria outra sessão, não concede XP novamente e limpa qualquer item antigo que tenha
        // permanecido na fila.
        if (topic.status in setOf(TopicStatus.ESTUDADO, TopicStatus.REVISANDO, TopicStatus.DOMINADO)) {
            dao.queueOnce().firstOrNull { it.topicId == topicId }?.let { item ->
                dao.deleteQueue(item)
                dao.insertQueueEvent(QueueEventEntity(topicId = topicId, type = QueueEventType.CONCLUIDO))
            }
            return
        }
        val firstCompletion = dao.studySessionCount(topicId) == 0
        val subject = dao.subjectsOnce().firstOrNull { it.id == topic.subjectId }
        val topicQuestionIds = dao.questionsOnce().filter { it.question.topicId == topicId }.map { it.question.id }.toSet()
        val attempts = dao.attemptsOnce().filter { it.answeredAt in startedAt..now && it.questionId in topicQuestionIds }
        if (firstCompletion) {
            dao.insertStudySession(StudySessionEntity(
                topicId = topicId,
                startedAt = startedAt,
                completedAt = now,
                competitionId = subject?.competitionId,
                subjectId = subject?.id,
                durationSeconds = ((now - startedAt) / 1000).coerceAtLeast(0),
                questionCount = attempts.size,
                correctCount = attempts.count { it.correct },
                wrongCount = attempts.count { !it.correct },
                notes = notes,
            ))
        }
        dao.updateTopic(topic.copy(status = TopicStatus.ESTUDADO, firstStudiedAt = topic.firstStudiedAt ?: now, lastStudiedAt = now))
        val zone = ZoneId.systemDefault()
        val base = LocalDate.now(zone)
        if (firstCompletion) {
            dao.insertReviews(ReviewIntervals.days.mapIndexed { index, days -> ReviewScheduleEntity(topicId = topicId, stage = index + 1, dueAt = base.plusDays(days).atStartOfDay(zone).toInstant().toEpochMilli()) })
        }
        dao.queueOnce().firstOrNull { it.topicId == topicId }?.let { item ->
            dao.deleteQueue(item)
            dao.insertQueueEvent(QueueEventEntity(topicId = topicId, type = QueueEventType.CONCLUIDO))
        }
    }
    suspend fun postponeQueue(item: StudyQueueEntity, reason: String) = db.withTransaction {
        dao.updateQueue(item.copy(postponements = item.postponements + 1))
        dao.insertQueueEvent(QueueEventEntity(topicId = item.topicId, type = QueueEventType.ADIADO, reason = reason))
    }

    suspend fun answer(questionWithOptions: QuestionWithOptions, selectedKey: String, sessionId: String? = null): Boolean = db.withTransaction {
        val correct = questionWithOptions.options.firstOrNull { it.key == selectedKey }?.isCorrect == true
        val question = questionWithOptions.question
        dao.insertAttempt(QuestionAttemptEntity(questionId = question.id, selectedKey = selectedKey, correct = correct, sessionId = sessionId))
        dao.updateQuestion(question.copy(
            answerCount = question.answerCount + 1,
            correctCount = question.correctCount + if (correct) 1 else 0,
            errorCount = question.errorCount + if (correct) 0 else 1,
            lastAnswer = selectedKey,
            lastAnsweredAt = System.currentTimeMillis(),
        ))
        val existing = dao.errorFor(question.id)
        val correctKey = questionWithOptions.options.firstOrNull { it.isCorrect }?.key
        if (!correct) {
            val now = System.currentTimeMillis()
            // Errar (de novo) zera a escada: a questão volta sozinha em 3 dias.
            val streakDoErro = ErrorRetryLadder.nextStreak(existing?.retryStreak ?: 0, correct = false)
            val voltaEm = ErrorRetryLadder.nextRetryAt(streakDoErro, now)
            val errorEntryId = if (existing == null) dao.insertError(
                ErrorNotebookEntryEntity(
                    questionId = question.id,
                    selectedAnswer = selectedKey,
                    correctAnswer = correctKey,
                    status = ErrorStatus.NOVO,
                    retryStreak = streakDoErro,
                    nextRetryAt = voltaEm,
                ),
            )
            else {
                dao.updateError(existing.copy(
                    errorCount = existing.errorCount + 1,
                    lastErrorAt = now,
                    pending = true,
                    selectedAnswer = selectedKey,
                    correctAnswer = correctKey,
                    status = if (existing.lastReviewedAt != null || existing.retryCorrectCount > 0 || existing.status == ErrorStatus.CORRIGIDO) ErrorStatus.RECORRENTE else existing.status,
                    retryStreak = streakDoErro,
                    nextRetryAt = voltaEm,
                ))
                existing.id
            }
            // Quando a questão diz qual conceito testa, o caderno usa esse; senão cai no antigo
            // chute pela primeira tag ou pelo título do tópico.
            val declared = question.errorConceptExternalId?.takeIf { it.isNotBlank() }?.let { dao.errorConceptByExternalId(it) }
            val conceptName = declared?.title
                ?: question.tagsText.split(',').firstOrNull()?.trim().takeUnless { it.isNullOrBlank() }
                ?: dao.topic(question.topicId)?.title ?: "Conceito da questão"
            val concept = declared ?: dao.errorConceptByTitle(question.topicId, conceptName)
            val conceptId = if (concept == null) dao.insertErrorConcept(ErrorConceptEntity(topicId = question.topicId, title = conceptName, errorCount = 1, lastErrorAt = now, priority = Priority.ALTA))
            else {
                dao.updateErrorConcept(concept.copy(errorCount = concept.errorCount + 1, lastErrorAt = now, priority = if (concept.errorCount >= 1) Priority.ALTA else Priority.NORMAL, mastered = false, updatedAt = now))
                concept.id
            }
            dao.insertErrorConceptEntry(ErrorConceptEntryCrossRef(conceptId, errorEntryId))
        } else if (existing != null) {
            val now = System.currentTimeMillis()
            // Acertar avança um degrau (3 -> 10 -> 30 dias); no terceiro acerto seguido a questão
            // sai da escada e só volta se for errada outra vez.
            val streakDoAcerto = ErrorRetryLadder.nextStreak(existing.retryStreak, correct = true)
            dao.updateError(existing.copy(
                retryCorrectCount = existing.retryCorrectCount + 1,
                lastReviewedAt = now,
                pending = false,
                status = ErrorStatus.CORRIGIDO,
                retryStreak = streakDoAcerto,
                nextRetryAt = ErrorRetryLadder.nextRetryAt(streakDoAcerto, now),
            ))
            dao.conceptsForError(existing.id).forEach { concept ->
                dao.updateErrorConcept(concept.copy(correctAfterErrorCount = concept.correctAfterErrorCount + 1, lastReviewedAt = now, mastered = concept.correctAfterErrorCount + 1 >= concept.errorCount, updatedAt = now))
            }
        }
        correct
    }

    suspend fun smartQuestions(count: Int, seed: Long = LocalDate.now().toEpochDay()): List<QuestionWithOptions> {
        val questions = dao.questionsPage(2_000, 0)
        val topics = dao.topicsOnce().associateBy { it.id }
        val attempts = dao.attemptsOnce().groupBy { it.questionId }
        val reviews = dao.reviewsOnce()
        val errors = dao.errorsOnce().associateBy { it.questionId }
        val questionsByTopic = questions.groupBy { it.question.topicId }
        val attemptsByTopic = questionsByTopic.mapValues { (_, rows) -> rows.flatMap { attempts[it.question.id].orEmpty() }.sortedByDescending { it.answeredAt } }
        val errorsByTopic = questionsByTopic.mapValues { (_, rows) -> rows.mapNotNull { errors[it.question.id] } }
        val now = System.currentTimeMillis()
        val candidates = questions.map { row ->
            val questionAttempts = attempts[row.question.id].orEmpty().sortedByDescending { it.answeredAt }
            val topic = topics[row.question.topicId]
            val topicQuestions = questionsByTopic[row.question.topicId].orEmpty()
            val answered = topicQuestions.sumOf { it.question.answerCount }
            val correct = topicQuestions.sumOf { it.question.correctCount }
            val recent = attemptsByTopic[row.question.topicId].orEmpty().take(20)
            val topicErrors = errorsByTopic[row.question.topicId].orEmpty()
            val mastery = MasteryCalculator.percent(MasteryInput(topic?.status ?: TopicStatus.NAO_ESTUDADO, answered, correct, recent.count { !it.correct }, reviews.count { it.topicId == row.question.topicId && it.completedAt != null }, recent.size, recent.count { it.correct }, topicErrors.count { it.status == ErrorStatus.RECORRENTE }, reviews.count { it.topicId == row.question.topicId && ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA }, ((now - (topic?.lastReviewedAt ?: topic?.lastStudiedAt ?: now)) / 86_400_000L).toInt()))
            SmartQuestionCandidate(row.question.id, row.question.topicId, mastery, row.question.errorCount, errors[row.question.id]?.status == ErrorStatus.RECORRENTE, reviews.any { it.topicId == row.question.topicId && ReviewPolicy.status(it, now) == ComputedReviewStatus.ATRASADA }, row.question.answerCount, row.question.lastAnsweredAt?.let { ((now - it) / 86_400_000L).toInt() } ?: 90)
        }
        val ids = SmartQuestionSelector.select(candidates, count, seed).map { it.questionId }
        return ids.mapNotNull { id -> questions.firstOrNull { it.question.id == id } }
    }

    suspend fun saveQuestionSession(value: QuestionSessionEntity) = dao.insertQuestionSession(value)
    suspend fun toggleQuestionFavorite(value: QuestionEntity) = dao.updateQuestion(value.copy(isFavorite = !value.isFavorite))

    suspend fun deleteError(id: Long) = dao.deleteError(id)
    suspend fun updateError(value: ErrorNotebookEntryEntity) = dao.updateError(value)
    suspend fun saveErrorConcept(value: ErrorConceptEntity): Long = if (value.id == 0L) dao.insertErrorConcept(value) else { dao.updateErrorConcept(value.copy(updatedAt = System.currentTimeMillis())); value.id }
    suspend fun deleteErrorConcept(value: ErrorConceptEntity) = dao.deleteErrorConcept(value)
    suspend fun linkErrorConcept(conceptId: Long, errorEntryId: Long) = dao.insertErrorConceptEntry(ErrorConceptEntryCrossRef(conceptId, errorEntryId))

    suspend fun loadDemoData() = db.withTransaction {
        val existingCompetitions = dao.competitionsOnce()
        val competitionId = existingCompetitions.firstOrNull { it.name == "TRT, Tecnologia da Informação" }?.id
            ?: dao.insertCompetition(CompetitionEntity(name = "TRT, Tecnologia da Informação", isPrimary = existingCompetitions.isEmpty()))
        val subjectNames = listOf("Português", "Banco de Dados", "Redes", "Segurança da Informação", "Matemática/RLM")
        val existingSubjects = dao.subjectsFor(competitionId)
        val subjectIds = subjectNames.mapIndexed { index, name ->
            existingSubjects.firstOrNull { it.name == name }?.id
                ?: dao.insertSubject(SubjectEntity(competitionId = competitionId, name = name, position = index))
        }
        val securityId = subjectIds[3]
        val topics = listOf("CIA", "Ameaça, vulnerabilidade e risco", "Autenticação e autorização", "Hash", "Criptografia", "Certificados / assinatura / PKI", "Controle de acesso")
        val existingTopics = dao.topicsFor(securityId)
        val topicIds = topics.mapIndexed { index, title ->
            existingTopics.firstOrNull { it.parentTopicId == null && it.title == title }?.id
                ?: dao.insertTopic(TopicEntity(subjectId = securityId, title = title, position = index, status = if (index < 4) TopicStatus.ESTUDADO else TopicStatus.NAO_ESTUDADO))
        }
        if (dao.summaryByExternalId("demo-resumo-hash") == null) dao.insertSummary(SummaryEntity(
            topicId = topicIds[3], externalId = "demo-resumo-hash", title = "Resumo rápido, Hash",
            markdown = "# Hash em 2 minutos\n\n- Transforma entrada de tamanho variável em **digest de tamanho fixo**.\n- Propriedades: resistência à pré-imagem, à segunda pré-imagem e a colisões.\n- Usos: integridade, assinatura digital e armazenamento de senhas com salt e função apropriada.\n\n> Hash não cifra: não existe operação de descriptografia.", kind = SummaryKind.RAPIDO,
        ))
        if (dao.theoryByExternalId("demo-teoria-hash") == null) dao.insertTheory(TheoryDocumentEntity(
            topicId = topicIds[3], externalId = "demo-teoria-hash", title = "Livro demonstrativo, Funções hash",
            markdown = """# Funções hash criptográficas

## 1. A ideia de impressão digital

Uma função hash recebe uma mensagem de tamanho arbitrário e produz um valor curto, chamado **resumo**, **digest** ou impressão digital. Alterar um único bit da entrada tende a modificar amplamente a saída, comportamento conhecido como efeito avalanche.

O algoritmo é determinístico: a mesma entrada sempre gera a mesma saída. Isso permite recalcular o digest de um arquivo e compará-lo com o valor esperado. Se forem diferentes, existe evidência de alteração; se forem iguais, há uma forte indicação de integridade, limitada pela segurança do algoritmo utilizado.

## 2. Propriedades de segurança

Resistência à pré-imagem significa ser computacionalmente inviável descobrir uma entrada a partir de um hash escolhido. Resistência à segunda pré-imagem dificulta encontrar outra mensagem com o mesmo resumo de uma mensagem conhecida.

Resistência a colisões é a dificuldade de encontrar quaisquer duas entradas distintas com o mesmo resultado. Como o conjunto de entradas é maior que o de saídas, colisões necessariamente existem; a segurança exige que encontrá-las seja impraticável.

> Pegadinha de prova: “não existem colisões” é falso. O correto é dizer que colisões são difíceis de encontrar em um algoritmo seguro.

## 3. Integridade, autenticação e assinatura

Um hash isolado ajuda a detectar alterações, mas não prova quem calculou o valor. Um invasor capaz de trocar o arquivo também pode trocar um hash público. Para autenticação e integridade com segredo compartilhado, utiliza-se um MAC, como o HMAC.

Na assinatura digital, primeiro é calculado o hash da mensagem e a operação criptográfica é aplicada ao resumo. Isso torna o processo eficiente e relaciona a assinatura ao conteúdo completo.

## 4. Senhas e boas práticas

Senhas não devem ser guardadas com hash rápido e simples. O armazenamento adequado emprega salt aleatório e funções deliberadamente custosas, dificultando ataques por tentativa e tabelas pré-computadas.

Algoritmos obsoletos não devem ser escolhidos apenas porque ainda produzem um digest. A decisão deve considerar o tipo de uso, o tamanho da saída, ataques conhecidos e recomendações atuais.

## Fechamento do capítulo

- Hash fornece resumo de tamanho fixo.
- Colisões existem, mas devem ser impraticáveis de encontrar.
- HMAC acrescenta autenticação baseada em segredo.
- Senhas exigem salt e função específica e custosa.""",
        ))
        if (dao.summaryByExternalId("demo-resumo-controle-acesso") == null) dao.insertSummary(SummaryEntity(
            topicId = topicIds[6], externalId = "demo-resumo-controle-acesso", title = "Resumo rápido, Controle de acesso",
            markdown = "# Controle de acesso\n\n- **DAC:** o proprietário decide.\n- **MAC:** política central baseada em rótulos.\n- **RBAC:** permissões agrupadas por papéis.\n- **ABAC:** decisão por atributos e contexto.\n\n> Autenticação confirma identidade; autorização decide o que ela pode fazer.", kind = SummaryKind.RAPIDO,
        ))
        if (dao.theoryByExternalId("demo-teoria-controle-acesso") == null) dao.insertTheory(TheoryDocumentEntity(
            topicId = topicIds[6], externalId = "demo-teoria-controle-acesso", title = "Livro demonstrativo, Controle de acesso",
            markdown = """# Controle de acesso: fundamentos e modelos

## 1. Do reconhecimento à permissão

Controle de acesso responde quem pode realizar determinada ação sobre um recurso e em quais condições. O fluxo costuma envolver identificação, autenticação, autorização e auditoria. Cada etapa resolve um problema diferente e pode aparecer separadamente em questões de prova.

Identificação é a declaração de uma identidade. Autenticação é a verificação dessa declaração por senha, token, biometria ou combinação de fatores. Autorização ocorre depois e determina as operações permitidas. Auditoria registra o que aconteceu para responsabilização e investigação.

## 2. Princípios de projeto

O menor privilégio concede somente as permissões necessárias pelo tempo necessário. A necessidade de conhecer limita informações ao escopo do trabalho, enquanto a separação de funções divide etapas críticas entre pessoas ou papéis diferentes.

Negação por padrão significa que um acesso não expressamente autorizado deve ser recusado. Revisões periódicas removem privilégios acumulados quando pessoas mudam de função ou deixam a organização.

## 3. Modelo discricionário, DAC

No DAC, o proprietário do recurso pode conceder ou retirar acesso. É um modelo flexível e comum em sistemas de arquivos, mas a delegação sucessiva pode dificultar o controle central.

Listas de controle de acesso podem representar quais sujeitos possuem quais permissões sobre um objeto. ACL é um mecanismo; o contexto da política é que indica se o controle é discricionário.

## 4. Modelo obrigatório, MAC

No MAC, uma autoridade central estabelece classificações e credenciais. O usuário comum não pode alterar livremente a política. A decisão compara níveis e categorias segundo regras obrigatórias.

Ambientes militares e governamentais são exemplos clássicos, mas a característica cobrada é a presença de rótulos e controle central, não o setor onde o sistema é usado.

## 5. Papéis e atributos

No RBAC, permissões são associadas a papéis organizacionais, e usuários recebem esses papéis. A abordagem simplifica concessão, revogação, auditoria e separação de funções.

No ABAC, a política avalia atributos do sujeito, objeto, ação e ambiente. Departamento, localização, horário, sensibilidade e tipo de operação podem participar da mesma decisão.

> RBAC e ABAC não precisam ser rivais: o papel pode ser um dos atributos avaliados por uma política contextual.

## 6. Como reconhecer na prova

Pergunte qual elemento dirige a decisão. Proprietário aponta para DAC; rótulos e níveis, para MAC; função exercida, para RBAC; conjunto de características e contexto, para ABAC.

Não confunda autenticação multifator com autorização. Usar senha e biometria fortalece a prova de identidade, mas não define quais arquivos ou operações serão liberados.

## Síntese final

- DAC: escolha do proprietário.
- MAC: regras e classificações obrigatórias.
- RBAC: papéis representam funções.
- ABAC: atributos permitem decisões contextuais.""",
        ))
        listOf(
            Triple(SnippetKind.BIZU, "Hash comprova integridade; HMAC acrescenta autenticação com uma chave secreta.", "demo-snippet-hash-bizu"),
            Triple(SnippetKind.PEGADINHA, "Colisões existem matematicamente. A propriedade desejada é que seja computacionalmente inviável encontrá-las.", "demo-snippet-hash-trap"),
            Triple(SnippetKind.RECUPERACAO, "Sem consultar: qual é a diferença entre resistência à pré-imagem e resistência a colisões?", "demo-snippet-hash-recall-1"),
            Triple(SnippetKind.RECUPERACAO, "Por que um hash público isolado não autentica quem enviou um arquivo?", "demo-snippet-hash-recall-2"),
        ).forEachIndexed { index, (kind, text, externalId) ->
            if (dao.snippetByExternalId(externalId) == null) dao.insertSnippet(TopicSnippetEntity(topicId = topicIds[3], kind = kind, text = text, externalId = externalId, position = index))
        }
        if (dao.summaryByExternalId("demo-quick-hash-v2") == null) dao.insertSummary(SummaryEntity(
            topicId = topicIds[3], externalId = "demo-quick-hash-v2", title = "Revisão de 3 minutos, Hash", kind = SummaryKind.RAPIDO,
            markdown = "# Hash, revisão rápida\n\n- Entrada variável → digest fixo.\n- É determinístico e não foi feito para ser reversível.\n- Propriedades: pré-imagem, segunda pré-imagem e colisão.\n- Senhas: salt + função de derivação lenta.\n\n> Integridade não é confidencialidade.",
        ))
        if (dao.errorConceptByExternalId("demo-error-hash-v2") == null) dao.insertErrorConcept(ErrorConceptEntity(
            topicId = topicIds[3], externalId = "demo-error-hash-v2", title = "Hash não é criptografia reversível",
            summary = "A função hash produz um resumo unidirecional. Para confidencialidade, use cifra; para autenticação com segredo compartilhado, use HMAC.",
        ))
        val samples = listOf(
            Triple(topicIds[0], "Qual princípio garante que a informação seja acessível apenas a pessoas autorizadas?", "Confidencialidade"),
            Triple(topicIds[3], "Qual propriedade é desejável em uma função hash criptográfica?", "Resistência a colisões"),
            Triple(topicIds[4], "Na criptografia simétrica, como ocorre o uso de chaves?", "A mesma chave cifra e decifra"),
            Triple(topicIds[5], "O que uma assinatura digital fornece diretamente?", "Autenticidade e integridade"),
            Triple(topicIds[6], "Qual modelo concede permissões segundo a função exercida?", "RBAC"),
        )
        samples.forEachIndexed { index, (topicId, statement, correctText) ->
            if (dao.questionByExternalId("demo-q-${index + 1}") != null) return@forEachIndexed
            val qId = dao.insertQuestion(QuestionEntity(topicId = topicId, externalId = "demo-q-${index + 1}", board = "Fictícia", difficulty = Difficulty.MEDIA, statement = statement, explanation = "A resposta correta é **$correctText**. O conceito está diretamente relacionado ao tópico estudado."))
            val distractors = when (index) {
                0 -> listOf(correctText, "Disponibilidade", "Integridade", "Não repúdio", "Auditabilidade")
                1 -> listOf("Ser reversível", correctText, "Produzir saída variável", "Usar chave pública", "Ocultar o algoritmo")
                2 -> listOf("Uma chave pública e outra privada", "Nenhuma chave", correctText, "Uma chave por bloco", "Somente chave pública")
                3 -> listOf("Confidencialidade apenas", correctText, "Disponibilidade", "Anonimato", "Compactação")
                else -> listOf("DAC", "MAC", correctText, "ABAC", "ACL")
            }
            dao.insertOptions(distractors.mapIndexed { position, text -> QuestionOptionEntity(questionId = qId, key = ('A' + position).toString(), text = text, isCorrect = text == correctText, position = position) })
        }
        val queued = dao.queueOnce().map { it.topicId }.toSet()
        if (topicIds[6] !in queued) dao.insertQueue(StudyQueueEntity(topicId = topicIds[6], position = dao.maxQueuePosition() + 1))
        if (topicIds[4] !in queued) dao.insertQueue(StudyQueueEntity(topicId = topicIds[4], position = dao.maxQueuePosition() + 1))
    }
}
