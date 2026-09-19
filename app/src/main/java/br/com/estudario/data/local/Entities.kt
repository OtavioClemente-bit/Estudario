package br.com.estudario.data.local

import androidx.room.*
import br.com.estudario.domain.PriorityLevel
import br.com.estudario.domain.PrioritySource

enum class TopicStatus { NAO_ESTUDADO, EM_ESTUDO, ESTUDADO, REVISANDO, DOMINADO }
enum class Priority { BAIXA, NORMAL, ALTA }
enum class Difficulty { FACIL, MEDIA, DIFICIL }
enum class SummaryKind { COMPLETO, RAPIDO }
enum class SnippetKind { BIZU, PEGADINHA, RECUPERACAO }
enum class ReviewDifficulty { FACIL, NORMAL, DIFICIL }
enum class ErrorStatus { NOVO, REVISANDO, CORRIGIDO, RECORRENTE }
enum class QuestionSessionType { QUICK, TOPIC, SUBJECT, SMART, ERROR_REVIEW, SIMULATION, DAILY_CHALLENGE, REVIEW }
enum class QueueEventType { ADICIONADO, CONCLUIDO, ADIADO, PAUSADO, RETOMADO }
enum class ContentOriginType { EDITAL, DIDACTIC_SUBDIVISION, AUXILIARY_CONTENT }
enum class QuestionSourceType { REAL, REAL_ADAPTED, AUTHORIAL }
enum class SourceKind { OFICIAL, COMPLEMENTAR }

@Entity(tableName = "competitions", indices = [Index(value = ["externalId"], unique = true)])
data class CompetitionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val externalId: String? = null,
    @ColumnInfo(defaultValue = "50") val assessedPriorityScore: Int = 50,
    @ColumnInfo(defaultValue = "DEFAULT") val assessedPrioritySource: PrioritySource = PrioritySource.DEFAULT,
    @ColumnInfo(defaultValue = "0") val assessedPriorityConfidence: Float = 0f,
    val assessedPriorityRationale: String? = null,
    @ColumnInfo(defaultValue = "[]") val assessedPriorityEvidenceJson: String = "[]",
    @ColumnInfo(defaultValue = "0") val hasAssessedPriority: Boolean = false,
    val userPriorityOverride: PriorityLevel? = null,
)

@Entity(
    tableName = "subjects",
    foreignKeys = [ForeignKey(entity = CompetitionEntity::class, parentColumns = ["id"], childColumns = ["competitionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("competitionId"), Index(value = ["externalId"], unique = true)],
)
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val competitionId: Long,
    val name: String,
    val position: Int = 0,
    val externalId: String? = null,
    @ColumnInfo(defaultValue = "50") val assessedPriorityScore: Int = 50,
    @ColumnInfo(defaultValue = "DEFAULT") val assessedPrioritySource: PrioritySource = PrioritySource.DEFAULT,
    @ColumnInfo(defaultValue = "0") val assessedPriorityConfidence: Float = 0f,
    val assessedPriorityRationale: String? = null,
    @ColumnInfo(defaultValue = "[]") val assessedPriorityEvidenceJson: String = "[]",
    @ColumnInfo(defaultValue = "0") val hasAssessedPriority: Boolean = false,
    val userPriorityOverride: PriorityLevel? = null,
)

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["parentTopicId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("subjectId"), Index("parentTopicId"), Index(value = ["externalId"], unique = true)],
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val parentTopicId: Long? = null,
    val title: String,
    val description: String = "",
    val position: Int = 0,
    val status: TopicStatus = TopicStatus.NAO_ESTUDADO,
    val firstStudiedAt: Long? = null,
    val lastStudiedAt: Long? = null,
    val lastReviewedAt: Long? = null,
    val notes: String = "",
    val priority: Priority = Priority.NORMAL,
    val externalId: String? = null,
    val contentOriginType: ContentOriginType = ContentOriginType.EDITAL,
    /**
     * Recorte declarado por quem gerou o conteúdo: o que este item do edital cobra e o que fica de
     * fora. Fica visível no tópico para a pessoa conferir contra o edital dela — é a defesa contra
     * estudar 40 páginas de um assunto que o edital pediu em uma linha.
     */
    val scopeCovers: String? = null,
    val scopeExcludes: String? = null,
    @ColumnInfo(defaultValue = "50") val assessedPriorityScore: Int = 50,
    @ColumnInfo(defaultValue = "DEFAULT") val assessedPrioritySource: PrioritySource = PrioritySource.DEFAULT,
    @ColumnInfo(defaultValue = "0") val assessedPriorityConfidence: Float = 0f,
    val assessedPriorityRationale: String? = null,
    @ColumnInfo(defaultValue = "[]") val assessedPriorityEvidenceJson: String = "[]",
    @ColumnInfo(defaultValue = "0") val hasAssessedPriority: Boolean = false,
    val userPriorityOverride: PriorityLevel? = null,
)

@Entity(
    tableName = "summaries",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index(value = ["externalId"], unique = true)],
)
data class SummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val title: String,
    val markdown: String,
    val isFavorite: Boolean = false,
    val ownNotes: String = "",
    val externalId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val kind: SummaryKind = SummaryKind.COMPLETO,
)

@Entity(
    tableName = "topic_snippets",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index("kind"), Index(value = ["externalId"], unique = true), Index("isFavorite")],
)
data class TopicSnippetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val kind: SnippetKind,
    val text: String,
    val isFavorite: Boolean = false,
    val externalId: String? = null,
    val position: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "theory_documents",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index(value = ["externalId"], unique = true)],
)
data class TheoryDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val title: String,
    val markdown: String,
    val externalId: String? = null,
    val lastReadBlock: Int = -1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "theory_marks",
    foreignKeys = [ForeignKey(entity = TheoryDocumentEntity::class, parentColumns = ["id"], childColumns = ["theoryId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("theoryId"), Index(value = ["theoryId", "blockIndex"], unique = true)],
)
data class TheoryMarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val theoryId: Long,
    val blockIndex: Int,
    val quote: String,
    val note: String = "",
    val color: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index(value = ["externalId"], unique = true), Index("sourceId"), Index("normalizedHash"), Index("questionSourceType")],
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val externalId: String? = null,
    val board: String? = null,
    val agency: String? = null,
    val year: Int? = null,
    val difficulty: Difficulty? = null,
    val source: String? = null,
    val statement: String,
    val explanation: String,
    val notes: String = "",
    val tagsText: String = "",
    val importedAt: Long = System.currentTimeMillis(),
    val answerCount: Int = 0,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val lastAnswer: String? = null,
    val lastAnsweredAt: Long? = null,
    val isFavorite: Boolean = false,
    val questionSourceType: QuestionSourceType = QuestionSourceType.AUTHORIAL,
    val sourceId: String? = null,
    val sourceUrl: String? = null,
    val normalizedHash: String? = null,
    /**
     * Título da seção da teoria/resumo que responde esta questão, escrito pela própria IA que a
     * gerou. É o que permite, ao errar, abrir o material exatamente no ponto certo em vez de
     * adivinhar por palavra-chave.
     */
    val reviewAnchor: String? = null,
    /**
     * Conceito de erro que esta questão testa, apontado por quem a gerou. Ao errar, o caderno liga
     * o erro a esse conceito em vez de adivinhar pelo título do tópico — é o que faz o caderno
     * mostrar o padrão ("confundo competência com atribuição") em vez de uma lista de questões.
     */
    val errorConceptExternalId: String? = null,
)

@Entity(
    tableName = "question_options",
    foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId")],
)
data class QuestionOptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val key: String,
    val text: String,
    val isCorrect: Boolean = false,
    val position: Int = 0,
)

@Entity(
    tableName = "question_attempts",
    foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId")],
)
data class QuestionAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val selectedKey: String,
    val correct: Boolean,
    val answeredAt: Long = System.currentTimeMillis(),
    val sessionId: String? = null,
)

@Entity(
    tableName = "error_notebook",
    foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["questionId"], unique = true)],
)
data class ErrorNotebookEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val errorCount: Int = 1,
    val retryCorrectCount: Int = 0,
    val firstErrorAt: Long = System.currentTimeMillis(),
    val lastErrorAt: Long = System.currentTimeMillis(),
    val lastReviewedAt: Long? = null,
    val comment: String = "",
    val concept: String = "",
    val pending: Boolean = true,
    val selectedAnswer: String? = null,
    val correctAnswer: String? = null,
    val status: ErrorStatus = ErrorStatus.NOVO,
    /** Acertos seguidos depois do erro: 0 acabou de errar, 3 saiu da escada de reencontro. */
    @ColumnInfo(defaultValue = "0") val retryStreak: Int = 0,
    /** Quando a questão errada volta sozinha. Null = fora da escada (dominada ou nunca agendada). */
    val nextRetryAt: Long? = null,
)

@Entity(
    tableName = "error_concepts",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index(value = ["externalId"], unique = true), Index("isFavorite")],
)
data class ErrorConceptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val title: String,
    val summary: String = "",
    val isFavorite: Boolean = false,
    val externalId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val errorCount: Int = 0,
    val correctAfterErrorCount: Int = 0,
    val lastErrorAt: Long? = null,
    val lastReviewedAt: Long? = null,
    val priority: Priority = Priority.NORMAL,
    val mastered: Boolean = false,
)

@Entity(
    tableName = "error_concept_entries",
    primaryKeys = ["conceptId", "errorEntryId"],
    foreignKeys = [
        ForeignKey(entity = ErrorConceptEntity::class, parentColumns = ["id"], childColumns = ["conceptId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ErrorNotebookEntryEntity::class, parentColumns = ["id"], childColumns = ["errorEntryId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("conceptId"), Index("errorEntryId")],
)
data class ErrorConceptEntryCrossRef(val conceptId: Long, val errorEntryId: Long)

@Entity(
    tableName = "review_schedule",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index(value = ["topicId", "stage"], unique = true)],
)
data class ReviewScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val stage: Int,
    val dueAt: Long,
    val completedAt: Long? = null,
    val ignoredAt: Long? = null,
    val perceivedDifficulty: ReviewDifficulty? = null,
    val questionCorrect: Int = 0,
    val questionTotal: Int = 0,
)

@Entity(
    tableName = "review_sessions",
    foreignKeys = [
        ForeignKey(entity = ReviewScheduleEntity::class, parentColumns = ["id"], childColumns = ["reviewId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("reviewId"), Index("topicId"), Index("completedAt")],
)
data class ReviewSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reviewId: Long,
    val topicId: Long,
    val startedAt: Long,
    val completedAt: Long = System.currentTimeMillis(),
    val recalled: Int = 0,
    val forgotten: Int = 0,
    val questionCorrect: Int = 0,
    val questionTotal: Int = 0,
    val perceivedDifficulty: ReviewDifficulty = ReviewDifficulty.NORMAL,
)

@Entity(tableName = "review_history", indices = [Index("topicId")])
data class ReviewHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val reviewedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "study_queue", indices = [Index("topicId")])
data class StudyQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val position: Int,
    val paused: Boolean = false,
    val enqueuedAt: Long = System.currentTimeMillis(),
    val postponements: Int = 0,
)

@Entity(
    tableName = "queue_events",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index("occurredAt")],
)
data class QueueEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val type: QueueEventType,
    val occurredAt: Long = System.currentTimeMillis(),
    val reason: String = "",
)

@Entity(tableName = "study_sessions", indices = [Index("topicId")])
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val startedAt: Long,
    val completedAt: Long = System.currentTimeMillis(),
    val competitionId: Long? = null,
    val subjectId: Long? = null,
    val durationSeconds: Long = 0,
    val questionCount: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val notes: String = "",
    val sourcePackageId: String? = null,
)

@Entity(tableName = "question_sessions", indices = [Index("startedAt"), Index("completedAt"), Index("type")])
data class QuestionSessionEntity(
    @PrimaryKey val id: String,
    val type: QuestionSessionType,
    val startedAt: Long,
    val completedAt: Long,
    val durationSeconds: Long,
    val questionCount: Int,
    val correctCount: Int,
    val subjectIdsText: String = "",
    val topicIdsText: String = "",
)

@Entity(tableName = "import_packages", indices = [Index("packageId"), Index("importedAt")])
data class ImportPackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageId: String,
    val schemaVersion: Int,
    val importedAt: Long = System.currentTimeMillis(),
    val contentHash: String,
    val fileName: String = "",
    val createdCount: Int = 0,
    val updatedCount: Int = 0,
    val ignoredCount: Int = 0,
)

/**
 * De onde a IA tirou o conteúdo. Fica guardado junto com o material para a pessoa poder conferir
 * depois — sem isso, "a IA disse" é a única garantia que ela tem.
 *
 * [topicId] nulo = fonte do pacote inteiro, não de um tópico específico.
 */
@Entity(
    tableName = "content_sources",
    foreignKeys = [ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("topicId"), Index("packageId"), Index(value = ["externalId"], unique = true)],
)
data class ContentSourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long? = null,
    val packageId: String = "",
    val kind: SourceKind = SourceKind.COMPLEMENTAR,
    val title: String,
    val publisher: String = "",
    /** Artigo, seção, página ou capítulo citado. */
    val reference: String = "",
    val url: String? = null,
    /** Data de acesso como a IA informou (AAAA-MM-DD), texto livre de propósito. */
    val accessedAt: String = "",
    val externalId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "user_notes", indices = [Index("topicId"), Index("questionId")])
data class UserNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long? = null,
    val questionId: Long? = null,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "tags", indices = [Index(value = ["name"], unique = true)])
data class TagEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)

@Entity(primaryKeys = ["questionId", "tagId"], tableName = "question_tags")
data class QuestionTagCrossRef(val questionId: Long, val tagId: Long)

data class SubjectWithTopics(
    @Embedded val subject: SubjectEntity,
    @Relation(parentColumn = "id", entityColumn = "subjectId") val topics: List<TopicEntity>,
)

data class QuestionWithOptions(
    @Embedded val question: QuestionEntity,
    @Relation(parentColumn = "id", entityColumn = "questionId") val options: List<QuestionOptionEntity>,
)

data class ErrorWithQuestion(
    @Embedded val entry: ErrorNotebookEntryEntity,
    @Relation(parentColumn = "questionId", entityColumn = "id", entity = QuestionEntity::class) val question: QuestionEntity,
)

data class QueueWithTopic(
    @Embedded val item: StudyQueueEntity,
    @Relation(parentColumn = "topicId", entityColumn = "id", entity = TopicEntity::class) val topic: TopicEntity,
)
