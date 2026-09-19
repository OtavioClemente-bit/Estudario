package br.com.estudario.data.local.planner

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import br.com.estudario.data.local.CompetitionEntity
import br.com.estudario.data.local.SubjectEntity
import br.com.estudario.data.local.TopicEntity
import br.com.estudario.domain.planner.PlanOrigin
import br.com.estudario.domain.planner.PlanPriority
import br.com.estudario.domain.planner.PlanTaskStatus
import br.com.estudario.domain.planner.PlanTaskType

enum class AvailabilityMode { SIMPLE, ADVANCED }
enum class PerceivedDifficulty { EASY, NORMAL, HARD }

@Entity(
    tableName = "study_plans",
    foreignKeys = [ForeignKey(entity = CompetitionEntity::class, parentColumns = ["id"], childColumns = ["competitionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("competitionId"), Index(value = ["competitionId", "active"]), Index(value = ["competitionId", "masterPlan"]), Index("archived")],
)
data class StudyPlanEntity(
    @androidx.room.PrimaryKey val id: String,
    val competitionId: Long,
    val name: String,
    val objective: String,
    val startEpochDay: Long,
    val examEpochDay: Long? = null,
    val active: Boolean = false,
    val masterPlan: Boolean = false,
    val archived: Boolean = false,
    val revision: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    // Método escolhido no assistente. Fica gravado para que replanejar siga as mesmas regras.
    @androidx.room.ColumnInfo(defaultValue = "DO_ZERO") val profile: String = "DO_ZERO",
    @androidx.room.ColumnInfo(defaultValue = "50") val blockMinutes: Int = 50,
    @androidx.room.ColumnInfo(defaultValue = "100") val weeklyQuestionsTarget: Int = 100,
    @androidx.room.ColumnInfo(defaultValue = "15") val questionsPerTopic: Int = 15,
    @androidx.room.ColumnInfo(defaultValue = "2") val simulationsPerMonth: Int = 2,
    @androidx.room.ColumnInfo(defaultValue = "0") val discursivesPerMonth: Int = 0,
    @androidx.room.ColumnInfo(defaultValue = "1") val interleaveSubjects: Boolean = true,
) {
    fun methodConfig(): br.com.estudario.domain.planner.StudyMethodConfig =
        br.com.estudario.domain.planner.StudyMethodConfig(
            profile = runCatching { br.com.estudario.domain.planner.StudyProfile.valueOf(profile) }
                .getOrDefault(br.com.estudario.domain.planner.StudyProfile.DO_ZERO),
            blockMinutes = blockMinutes.coerceIn(15, 120),
            weeklyQuestionsTarget = weeklyQuestionsTarget.coerceAtLeast(0),
            questionsPerTopic = questionsPerTopic.coerceAtLeast(0),
            simulationsPerMonth = simulationsPerMonth.coerceIn(0, 8),
            discursivesPerMonth = discursivesPerMonth.coerceIn(0, 12),
            interleaveSubjects = interleaveSubjects,
        )
}

@Entity(
    tableName = "study_plan_revisions",
    primaryKeys = ["planId", "revision"],
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId"), Index("createdAt")],
)
data class StudyPlanRevisionEntity(
    val planId: String,
    val revision: Long,
    val baseRevision: Long,
    val reason: String,
    val proposalId: String? = null,
    val summary: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "study_availability",
    primaryKeys = ["planId", "dayOfWeek"],
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId")],
)
data class StudyAvailabilityEntity(
    val planId: String,
    val dayOfWeek: Int,
    val availableMinutes: Int,
    val unavailable: Boolean = false,
    val mode: AvailabilityMode = AvailabilityMode.ADVANCED,
)

@Entity(
    tableName = "study_day_overrides",
    primaryKeys = ["planId", "epochDay"],
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId"), Index("epochDay")],
)
data class StudyDayOverrideEntity(
    val planId: String,
    val epochDay: Long,
    val availableMinutes: Int? = null,
    val unavailable: Boolean = false,
    val locked: Boolean = false,
)

@Entity(
    tableName = "plan_subjects",
    primaryKeys = ["planId", "subjectId"],
    foreignKeys = [
        ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("planId"), Index("subjectId")],
)
data class PlanSubjectEntity(
    val planId: String,
    val subjectId: Long,
    val subjectNameSnapshot: String,
    val priority: PlanPriority,
    val paused: Boolean = false,
    val minimumMaintenanceMinutes: Int = 0,
    val weightOverride: Int? = null,
    val position: Int = 0,
)

@Entity(
    tableName = "annual_phases",
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId"), Index(value = ["planId", "startEpochDay", "endEpochDay"]), Index(value = ["planId", "validUntilRevision"])],
)
data class AnnualPhaseEntity(
    @androidx.room.PrimaryKey val id: String,
    val planId: String,
    val position: Int,
    val name: String,
    val objective: String,
    val completionCriteria: String,
    val startEpochDay: Long,
    val endEpochDay: Long,
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
    val targetPercent: Int,
    val validFromRevision: Long,
    val validUntilRevision: Long? = null,
)

@Entity(
    tableName = "monthly_plans",
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId"), Index(value = ["planId", "yearMonth"]), Index(value = ["planId", "validUntilRevision"])],
)
data class MonthlyPlanEntity(
    @androidx.room.PrimaryKey val id: String,
    val planId: String,
    val yearMonth: String,
    val focus: String,
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
    val targetPercent: Int,
    val validFromRevision: Long,
    val validUntilRevision: Long? = null,
)

@Entity(
    tableName = "weekly_plans",
    foreignKeys = [ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId"), Index(value = ["planId", "weekStartEpochDay"]), Index(value = ["planId", "validUntilRevision"])],
)
data class WeeklyPlanEntity(
    @androidx.room.PrimaryKey val id: String,
    val planId: String,
    val weekStartEpochDay: Long,
    val objective: String = "",
    val targetMinutes: Int,
    val targetQuestions: Int,
    val targetDiscursives: Int,
    val validFromRevision: Long,
    val validUntilRevision: Long? = null,
)

@Entity(
    tableName = "plan_tasks",
    foreignKeys = [
        ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CompetitionEntity::class, parentColumns = ["id"], childColumns = ["competitionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = AnnualPhaseEntity::class, parentColumns = ["id"], childColumns = ["annualPhaseId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = MonthlyPlanEntity::class, parentColumns = ["id"], childColumns = ["monthlyPlanId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = WeeklyPlanEntity::class, parentColumns = ["id"], childColumns = ["weeklyPlanId"], onDelete = ForeignKey.SET_NULL),
    ],
    indices = [Index("planId"), Index("competitionId"), Index("subjectId"), Index("topicId"), Index("annualPhaseId"), Index("monthlyPlanId"), Index("weeklyPlanId"), Index(value = ["planId", "scheduledEpochDay"]), Index(value = ["planId", "status"])],
)
data class PlanTaskEntity(
    @androidx.room.PrimaryKey val id: String,
    val planId: String,
    val competitionId: Long,
    val annualPhaseId: String? = null,
    val monthlyPlanId: String? = null,
    val weeklyPlanId: String? = null,
    val subjectId: Long? = null,
    val topicId: Long? = null,
    val subjectNameSnapshot: String,
    val topicNameSnapshot: String? = null,
    val scheduledEpochDay: Long,
    val type: PlanTaskType,
    val plannedMinutes: Int,
    val plannedQuestions: Int = 0,
    val priority: PlanPriority,
    val status: PlanTaskStatus = PlanTaskStatus.PLANEJADA,
    val origin: PlanOrigin = PlanOrigin.ENGINE,
    val notes: String = "",
    val locked: Boolean = false,
    val progressNote: String = "",
    val replannedFromTaskId: String? = null,
    val createdRevision: Long,
    val updatedRevision: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "study_task_executions",
    foreignKeys = [
        ForeignKey(entity = StudyPlanEntity::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = PlanTaskEntity::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = CompetitionEntity::class, parentColumns = ["id"], childColumns = ["competitionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.SET_NULL),
    ],
    indices = [Index("planId"), Index("taskId"), Index("competitionId"), Index("subjectId"), Index("topicId"), Index("completedAt")],
)
data class StudyTaskExecutionEntity(
    @androidx.room.PrimaryKey val id: String,
    val planId: String,
    val taskId: String? = null,
    val competitionId: Long,
    val subjectId: Long? = null,
    val topicId: Long? = null,
    val startedAt: Long,
    val completedAt: Long,
    val actualMinutes: Int,
    val questionsDone: Int = 0,
    val correctAnswers: Int = 0,
    val notes: String = "",
    val perceivedDifficulty: PerceivedDifficulty = PerceivedDifficulty.NORMAL,
    val createdAt: Long = System.currentTimeMillis(),
)
