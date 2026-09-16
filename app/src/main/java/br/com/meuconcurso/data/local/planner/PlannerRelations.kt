package br.com.meuconcurso.data.local.planner

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "annual_phase_subjects",
    primaryKeys = ["phaseId", "subjectId"],
    foreignKeys = [
        ForeignKey(entity = AnnualPhaseEntity::class, parentColumns = ["id"], childColumns = ["phaseId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = br.com.meuconcurso.data.local.SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("phaseId"), Index("subjectId")],
)
data class AnnualPhaseSubjectEntity(val phaseId: String, val subjectId: Long)

@Entity(
    tableName = "annual_phase_topics",
    primaryKeys = ["phaseId", "topicId"],
    foreignKeys = [
        ForeignKey(entity = AnnualPhaseEntity::class, parentColumns = ["id"], childColumns = ["phaseId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = br.com.meuconcurso.data.local.TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("phaseId"), Index("topicId")],
)
data class AnnualPhaseTopicEntity(val phaseId: String, val topicId: Long)

@Entity(
    tableName = "monthly_plan_subjects",
    primaryKeys = ["monthlyPlanId", "subjectId"],
    foreignKeys = [
        ForeignKey(entity = MonthlyPlanEntity::class, parentColumns = ["id"], childColumns = ["monthlyPlanId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = br.com.meuconcurso.data.local.SubjectEntity::class, parentColumns = ["id"], childColumns = ["subjectId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("monthlyPlanId"), Index("subjectId")],
)
data class MonthlyPlanSubjectEntity(val monthlyPlanId: String, val subjectId: Long, val maintenance: Boolean = false)

@Entity(
    tableName = "monthly_plan_topics",
    primaryKeys = ["monthlyPlanId", "topicId"],
    foreignKeys = [
        ForeignKey(entity = MonthlyPlanEntity::class, parentColumns = ["id"], childColumns = ["monthlyPlanId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = br.com.meuconcurso.data.local.TopicEntity::class, parentColumns = ["id"], childColumns = ["topicId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("monthlyPlanId"), Index("topicId")],
)
data class MonthlyPlanTopicEntity(val monthlyPlanId: String, val topicId: Long)

@Entity(
    tableName = "plan_task_dependencies",
    primaryKeys = ["taskId", "dependsOnTaskId"],
    foreignKeys = [
        ForeignKey(entity = PlanTaskEntity::class, parentColumns = ["id"], childColumns = ["taskId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = PlanTaskEntity::class, parentColumns = ["id"], childColumns = ["dependsOnTaskId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("taskId"), Index("dependsOnTaskId")],
)
data class PlanTaskDependencyEntity(val taskId: String, val dependsOnTaskId: String)
