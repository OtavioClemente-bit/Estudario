package br.com.meuconcurso.data.local

import androidx.room.TypeConverter
import br.com.meuconcurso.data.local.planner.AvailabilityMode
import br.com.meuconcurso.data.local.planner.PerceivedDifficulty
import br.com.meuconcurso.domain.planner.PlanOrigin
import br.com.meuconcurso.domain.planner.PlanPriority
import br.com.meuconcurso.domain.planner.PlanTaskStatus
import br.com.meuconcurso.domain.planner.PlanTaskType

class Converters {
    @TypeConverter fun topicStatus(value: TopicStatus): String = value.name
    @TypeConverter fun topicStatus(value: String): TopicStatus = TopicStatus.valueOf(value)
    @TypeConverter fun priority(value: Priority): String = value.name
    @TypeConverter fun priority(value: String): Priority = Priority.valueOf(value)
    @TypeConverter fun difficulty(value: Difficulty?): String? = value?.name
    @TypeConverter fun difficulty(value: String?): Difficulty? = value?.let(Difficulty::valueOf)
    @TypeConverter fun summaryKind(value: SummaryKind): String = value.name
    @TypeConverter fun summaryKind(value: String): SummaryKind = SummaryKind.valueOf(value)
    @TypeConverter fun snippetKind(value: SnippetKind): String = value.name
    @TypeConverter fun snippetKind(value: String): SnippetKind = SnippetKind.valueOf(value)
    @TypeConverter fun reviewDifficulty(value: ReviewDifficulty?): String? = value?.name
    @TypeConverter fun reviewDifficulty(value: String?): ReviewDifficulty? = value?.let(ReviewDifficulty::valueOf)
    @TypeConverter fun errorStatus(value: ErrorStatus): String = value.name
    @TypeConverter fun errorStatus(value: String): ErrorStatus = ErrorStatus.valueOf(value)
    @TypeConverter fun sessionType(value: QuestionSessionType): String = value.name
    @TypeConverter fun sessionType(value: String): QuestionSessionType = QuestionSessionType.valueOf(value)
    @TypeConverter fun queueEventType(value: QueueEventType): String = value.name
    @TypeConverter fun queueEventType(value: String): QueueEventType = QueueEventType.valueOf(value)
    @TypeConverter fun contentOriginType(value: ContentOriginType): String = value.name
    @TypeConverter fun contentOriginType(value: String): ContentOriginType = ContentOriginType.valueOf(value)
    @TypeConverter fun questionSourceType(value: QuestionSourceType): String = value.name
    @TypeConverter fun questionSourceType(value: String): QuestionSourceType = QuestionSourceType.valueOf(value)
    @TypeConverter fun planPriority(value: PlanPriority): String = value.name
    @TypeConverter fun planPriority(value: String): PlanPriority = PlanPriority.valueOf(value)
    @TypeConverter fun planTaskType(value: PlanTaskType): String = value.name
    @TypeConverter fun planTaskType(value: String): PlanTaskType = PlanTaskType.valueOf(value)
    @TypeConverter fun planTaskStatus(value: PlanTaskStatus): String = value.name
    @TypeConverter fun planTaskStatus(value: String): PlanTaskStatus = PlanTaskStatus.valueOf(value)
    @TypeConverter fun planOrigin(value: PlanOrigin): String = value.name
    @TypeConverter fun planOrigin(value: String): PlanOrigin = PlanOrigin.valueOf(value)
    @TypeConverter fun availabilityMode(value: AvailabilityMode): String = value.name
    @TypeConverter fun availabilityMode(value: String): AvailabilityMode = AvailabilityMode.valueOf(value)
    @TypeConverter fun perceivedDifficulty(value: PerceivedDifficulty): String = value.name
    @TypeConverter fun perceivedDifficulty(value: String): PerceivedDifficulty = PerceivedDifficulty.valueOf(value)
}
