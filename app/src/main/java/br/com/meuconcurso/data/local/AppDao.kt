package br.com.meuconcurso.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM competitions ORDER BY isPrimary DESC, createdAt") fun competitions(): Flow<List<CompetitionEntity>>
    @Query("SELECT * FROM competitions ORDER BY isPrimary DESC, createdAt") suspend fun competitionsOnce(): List<CompetitionEntity>
    @Insert suspend fun insertCompetition(value: CompetitionEntity): Long
    @Update suspend fun updateCompetition(value: CompetitionEntity)
    @Delete suspend fun deleteCompetition(value: CompetitionEntity)
    @Query("UPDATE competitions SET isPrimary = CASE WHEN id = :id THEN 1 ELSE 0 END") suspend fun setPrimaryCompetition(id: Long)
    @Query("SELECT * FROM competitions WHERE externalId = :externalId LIMIT 1") suspend fun competitionByExternalId(externalId: String): CompetitionEntity?

    @Query("SELECT * FROM subjects ORDER BY competitionId, position, name") fun subjects(): Flow<List<SubjectEntity>>
    @Query("SELECT * FROM subjects ORDER BY competitionId, position, name") suspend fun subjectsOnce(): List<SubjectEntity>
    @Query("SELECT * FROM subjects WHERE competitionId = :competitionId ORDER BY position, name") suspend fun subjectsFor(competitionId: Long): List<SubjectEntity>
    @Insert suspend fun insertSubject(value: SubjectEntity): Long
    @Update suspend fun updateSubject(value: SubjectEntity)
    @Delete suspend fun deleteSubject(value: SubjectEntity)
    @Query("SELECT * FROM subjects WHERE externalId = :externalId LIMIT 1") suspend fun subjectByExternalId(externalId: String): SubjectEntity?

    @Query("SELECT * FROM topics ORDER BY subjectId, position, title") fun topics(): Flow<List<TopicEntity>>
    @Query("SELECT * FROM topics ORDER BY subjectId, position, title") suspend fun topicsOnce(): List<TopicEntity>
    @Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY position, title") suspend fun topicsFor(subjectId: Long): List<TopicEntity>
    @Query("SELECT * FROM topics WHERE id = :id") suspend fun topic(id: Long): TopicEntity?
    @Insert suspend fun insertTopic(value: TopicEntity): Long
    @Update suspend fun updateTopic(value: TopicEntity)
    @Delete suspend fun deleteTopic(value: TopicEntity)
    @Query("SELECT * FROM topics WHERE externalId = :externalId LIMIT 1") suspend fun topicByExternalId(externalId: String): TopicEntity?

    @Query("SELECT * FROM summaries ORDER BY updatedAt DESC") fun summaries(): Flow<List<SummaryEntity>>
    @Query("SELECT * FROM summaries ORDER BY updatedAt DESC") suspend fun summariesOnce(): List<SummaryEntity>
    @Query("SELECT * FROM summaries WHERE topicId = :topicId ORDER BY updatedAt DESC") suspend fun summariesFor(topicId: Long): List<SummaryEntity>
    @Query("SELECT * FROM summaries WHERE externalId = :externalId LIMIT 1") suspend fun summaryByExternalId(externalId: String): SummaryEntity?
    @Insert suspend fun insertSummary(value: SummaryEntity): Long
    @Update suspend fun updateSummary(value: SummaryEntity)
    @Delete suspend fun deleteSummary(value: SummaryEntity)

    @Query("SELECT * FROM topic_snippets ORDER BY topicId, kind, position, id") fun snippets(): Flow<List<TopicSnippetEntity>>
    @Query("SELECT * FROM topic_snippets ORDER BY topicId, kind, position, id") suspend fun snippetsOnce(): List<TopicSnippetEntity>
    @Query("SELECT * FROM topic_snippets WHERE externalId = :externalId LIMIT 1") suspend fun snippetByExternalId(externalId: String): TopicSnippetEntity?
    @Insert suspend fun insertSnippet(value: TopicSnippetEntity): Long
    @Update suspend fun updateSnippet(value: TopicSnippetEntity)
    @Delete suspend fun deleteSnippet(value: TopicSnippetEntity)

    @Query("SELECT * FROM theory_documents ORDER BY updatedAt DESC") fun theories(): Flow<List<TheoryDocumentEntity>>
    @Query("SELECT * FROM theory_documents ORDER BY updatedAt DESC") suspend fun theoriesOnce(): List<TheoryDocumentEntity>
    @Query("SELECT * FROM theory_documents WHERE externalId = :externalId LIMIT 1") suspend fun theoryByExternalId(externalId: String): TheoryDocumentEntity?
    @Insert suspend fun insertTheory(value: TheoryDocumentEntity): Long
    @Update suspend fun updateTheory(value: TheoryDocumentEntity)
    @Delete suspend fun deleteTheory(value: TheoryDocumentEntity)

    @Query("SELECT * FROM theory_marks ORDER BY createdAt DESC") fun theoryMarks(): Flow<List<TheoryMarkEntity>>
    @Query("SELECT * FROM theory_marks ORDER BY createdAt DESC") suspend fun theoryMarksOnce(): List<TheoryMarkEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTheoryMark(value: TheoryMarkEntity): Long
    @Update suspend fun updateTheoryMark(value: TheoryMarkEntity)
    @Delete suspend fun deleteTheoryMark(value: TheoryMarkEntity)

    @Transaction @Query("SELECT * FROM questions ORDER BY importedAt DESC") fun questions(): Flow<List<QuestionWithOptions>>
    @Transaction @Query("SELECT * FROM questions ORDER BY importedAt DESC") suspend fun questionsOnce(): List<QuestionWithOptions>
    @Transaction @Query("SELECT * FROM questions ORDER BY importedAt DESC LIMIT :limit OFFSET :offset") suspend fun questionsPage(limit: Int, offset: Int): List<QuestionWithOptions>
    @Query("SELECT COUNT(*) FROM questions") suspend fun questionCount(): Int
    @Transaction @Query("SELECT * FROM questions WHERE id = :id") suspend fun question(id: Long): QuestionWithOptions?
    @Query("SELECT * FROM questions WHERE externalId = :externalId LIMIT 1") suspend fun questionByExternalId(externalId: String): QuestionEntity?
    @Query("SELECT * FROM questions WHERE sourceId = :sourceId LIMIT 1") suspend fun questionBySourceId(sourceId: String): QuestionEntity?
    @Query("SELECT * FROM questions WHERE normalizedHash = :hash LIMIT 1") suspend fun questionByNormalizedHash(hash: String): QuestionEntity?
    @Insert suspend fun insertQuestion(value: QuestionEntity): Long
    @Insert suspend fun insertOptions(values: List<QuestionOptionEntity>)
    @Query("DELETE FROM question_options WHERE questionId = :questionId") suspend fun deleteOptionsFor(questionId: Long)
    @Update suspend fun updateQuestion(value: QuestionEntity)

    @Insert suspend fun insertAttempt(value: QuestionAttemptEntity)
    @Query("SELECT * FROM question_attempts ORDER BY answeredAt DESC") suspend fun attemptsOnce(): List<QuestionAttemptEntity>
    @Query("SELECT * FROM question_attempts ORDER BY answeredAt DESC") fun attempts(): Flow<List<QuestionAttemptEntity>>

    @Transaction @Query("SELECT * FROM error_notebook ORDER BY pending DESC, errorCount DESC, lastErrorAt DESC") fun errors(): Flow<List<ErrorWithQuestion>>
    @Query("SELECT * FROM error_notebook ORDER BY id") suspend fun errorsOnce(): List<ErrorNotebookEntryEntity>
    @Query("SELECT * FROM error_notebook WHERE questionId = :questionId LIMIT 1") suspend fun errorFor(questionId: Long): ErrorNotebookEntryEntity?
    @Insert suspend fun insertError(value: ErrorNotebookEntryEntity): Long
    @Update suspend fun updateError(value: ErrorNotebookEntryEntity)
    @Query("DELETE FROM error_notebook WHERE id = :id") suspend fun deleteError(id: Long)

    @Query("SELECT * FROM error_concepts ORDER BY updatedAt DESC") fun errorConcepts(): Flow<List<ErrorConceptEntity>>
    @Query("SELECT * FROM error_concepts ORDER BY updatedAt DESC") suspend fun errorConceptsOnce(): List<ErrorConceptEntity>
    @Query("SELECT * FROM error_concepts WHERE externalId = :externalId LIMIT 1") suspend fun errorConceptByExternalId(externalId: String): ErrorConceptEntity?
    @Query("SELECT * FROM error_concepts WHERE topicId = :topicId AND lower(title) = lower(:title) LIMIT 1") suspend fun errorConceptByTitle(topicId: Long, title: String): ErrorConceptEntity?
    @Query("SELECT ec.* FROM error_concepts ec INNER JOIN error_concept_entries x ON x.conceptId = ec.id WHERE x.errorEntryId = :errorEntryId") suspend fun conceptsForError(errorEntryId: Long): List<ErrorConceptEntity>
    @Insert suspend fun insertErrorConcept(value: ErrorConceptEntity): Long
    @Update suspend fun updateErrorConcept(value: ErrorConceptEntity)
    @Delete suspend fun deleteErrorConcept(value: ErrorConceptEntity)
    @Query("SELECT * FROM error_concept_entries") fun errorConceptEntries(): Flow<List<ErrorConceptEntryCrossRef>>
    @Query("SELECT * FROM error_concept_entries") suspend fun errorConceptEntriesOnce(): List<ErrorConceptEntryCrossRef>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertErrorConceptEntry(value: ErrorConceptEntryCrossRef)

    @Query("SELECT * FROM review_schedule ORDER BY dueAt") fun reviews(): Flow<List<ReviewScheduleEntity>>
    @Query("SELECT * FROM review_schedule ORDER BY dueAt") suspend fun reviewsOnce(): List<ReviewScheduleEntity>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertReviews(values: List<ReviewScheduleEntity>)
    @Update suspend fun updateReview(value: ReviewScheduleEntity)
    @Insert suspend fun insertReviewHistory(value: ReviewHistoryEntity)
    @Query("SELECT * FROM review_history ORDER BY reviewedAt DESC") suspend fun reviewHistoryOnce(): List<ReviewHistoryEntity>
    @Query("SELECT * FROM review_history ORDER BY reviewedAt DESC") fun reviewHistory(): Flow<List<ReviewHistoryEntity>>
    @Insert suspend fun insertReviewSession(value: ReviewSessionEntity): Long
    @Query("SELECT * FROM review_sessions ORDER BY completedAt DESC") fun reviewSessions(): Flow<List<ReviewSessionEntity>>
    @Query("SELECT * FROM review_sessions ORDER BY completedAt DESC") suspend fun reviewSessionsOnce(): List<ReviewSessionEntity>

    // The join also makes databases created by older versions resilient to orphaned queue rows.
    @Transaction @Query("SELECT q.* FROM study_queue q INNER JOIN topics t ON t.id = q.topicId ORDER BY q.paused, q.position") fun queue(): Flow<List<QueueWithTopic>>
    @Query("SELECT * FROM study_queue ORDER BY position") suspend fun queueOnce(): List<StudyQueueEntity>
    @Query("SELECT COALESCE(MAX(position), -1) FROM study_queue") suspend fun maxQueuePosition(): Int
    @Insert suspend fun insertQueue(value: StudyQueueEntity)
    @Update suspend fun updateQueue(value: StudyQueueEntity)
    @Delete suspend fun deleteQueue(value: StudyQueueEntity)
    @Insert suspend fun insertStudySession(value: StudySessionEntity)
    @Query("SELECT * FROM study_sessions ORDER BY completedAt DESC") suspend fun sessionsOnce(): List<StudySessionEntity>
    @Query("SELECT * FROM study_sessions ORDER BY completedAt DESC") fun sessions(): Flow<List<StudySessionEntity>>
    @Insert suspend fun insertQueueEvent(value: QueueEventEntity): Long
    @Query("SELECT * FROM queue_events ORDER BY occurredAt DESC") fun queueEvents(): Flow<List<QueueEventEntity>>
    @Query("SELECT * FROM queue_events ORDER BY occurredAt DESC") suspend fun queueEventsOnce(): List<QueueEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertQuestionSession(value: QuestionSessionEntity)
    @Query("SELECT * FROM question_sessions ORDER BY completedAt DESC") fun questionSessions(): Flow<List<QuestionSessionEntity>>
    @Query("SELECT * FROM question_sessions ORDER BY completedAt DESC") suspend fun questionSessionsOnce(): List<QuestionSessionEntity>

    @Query("SELECT * FROM import_packages WHERE packageId = :packageId ORDER BY importedAt DESC LIMIT 1") suspend fun importPackage(packageId: String): ImportPackageEntity?
    @Query("SELECT * FROM import_packages ORDER BY importedAt DESC") suspend fun importPackagesOnce(): List<ImportPackageEntity>
    @Insert suspend fun insertImportPackage(value: ImportPackageEntity): Long

    @Query("SELECT * FROM user_notes ORDER BY createdAt DESC") suspend fun notesOnce(): List<UserNoteEntity>
    @Query("SELECT * FROM tags ORDER BY name") suspend fun tagsOnce(): List<TagEntity>
    @Query("SELECT * FROM question_tags") suspend fun questionTagsOnce(): List<QuestionTagCrossRef>

    @Query("DELETE FROM study_queue WHERE topicId IN (:topicIds)") suspend fun deleteQueueForTopics(topicIds: List<Long>)
    @Query("DELETE FROM review_history WHERE topicId IN (:topicIds)") suspend fun deleteReviewHistoryForTopics(topicIds: List<Long>)
    @Query("DELETE FROM study_sessions WHERE topicId IN (:topicIds)") suspend fun deleteStudySessionsForTopics(topicIds: List<Long>)
    @Query("DELETE FROM user_notes WHERE topicId IN (:topicIds) OR questionId IN (SELECT id FROM questions WHERE topicId IN (:topicIds))") suspend fun deleteNotesForTopics(topicIds: List<Long>)
    @Query("DELETE FROM question_tags WHERE questionId IN (SELECT id FROM questions WHERE topicId IN (:topicIds))") suspend fun deleteQuestionTagsForTopics(topicIds: List<Long>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreCompetitions(values: List<CompetitionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreSubjects(values: List<SubjectEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreTopics(values: List<TopicEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreSummaries(values: List<SummaryEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreSnippets(values: List<TopicSnippetEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreTheories(values: List<TheoryDocumentEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreTheoryMarks(values: List<TheoryMarkEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreQuestions(values: List<QuestionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreOptions(values: List<QuestionOptionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreAttempts(values: List<QuestionAttemptEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreErrors(values: List<ErrorNotebookEntryEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreErrorConcepts(values: List<ErrorConceptEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreErrorConceptEntries(values: List<ErrorConceptEntryCrossRef>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreReviews(values: List<ReviewScheduleEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreReviewHistory(values: List<ReviewHistoryEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreReviewSessions(values: List<ReviewSessionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreQueue(values: List<StudyQueueEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreSessions(values: List<StudySessionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreQueueEvents(values: List<QueueEventEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreQuestionSessions(values: List<QuestionSessionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreImportPackages(values: List<ImportPackageEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreNotes(values: List<UserNoteEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreTags(values: List<TagEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun restoreQuestionTags(values: List<QuestionTagCrossRef>)

    @Query("DELETE FROM question_tags") suspend fun clearQuestionTags()
    @Query("DELETE FROM tags") suspend fun clearTags()
    @Query("DELETE FROM user_notes") suspend fun clearNotes()
    @Query("DELETE FROM study_sessions") suspend fun clearSessions()
    @Query("DELETE FROM question_sessions") suspend fun clearQuestionSessions()
    @Query("DELETE FROM queue_events") suspend fun clearQueueEvents()
    @Query("DELETE FROM study_queue") suspend fun clearQueue()
    @Query("DELETE FROM review_history") suspend fun clearReviewHistory()
    @Query("DELETE FROM review_sessions") suspend fun clearReviewSessions()
    @Query("DELETE FROM review_schedule") suspend fun clearReviews()
    @Query("DELETE FROM error_notebook") suspend fun clearErrors()
    @Query("DELETE FROM error_concept_entries") suspend fun clearErrorConceptEntries()
    @Query("DELETE FROM error_concepts") suspend fun clearErrorConcepts()
    @Query("DELETE FROM question_attempts") suspend fun clearAttempts()
    @Query("DELETE FROM questions") suspend fun clearQuestions()
    @Query("DELETE FROM summaries") suspend fun clearSummaries()
    @Query("DELETE FROM topic_snippets") suspend fun clearSnippets()
    @Query("DELETE FROM theory_marks") suspend fun clearTheoryMarks()
    @Query("DELETE FROM theory_documents") suspend fun clearTheories()
    @Query("DELETE FROM topics") suspend fun clearTopics()
    @Query("DELETE FROM subjects") suspend fun clearSubjects()
    @Query("DELETE FROM competitions") suspend fun clearCompetitions()
    @Query("DELETE FROM import_packages") suspend fun clearImportPackages()
}
