package br.com.meuconcurso.data.transfer.planner

import br.com.meuconcurso.data.local.AppDatabase
import br.com.meuconcurso.data.local.CompetitionEntity
import br.com.meuconcurso.data.local.SubjectEntity
import br.com.meuconcurso.data.local.TopicEntity

data class ResolvedStudyPlanImport(
    val file: StudyPlanFileV1,
    val competition: CompetitionEntity?,
    val subjects: Map<String, SubjectEntity>,
    val topics: Map<String, TopicEntity>,
    val unresolvedReferences: List<String>,
)

class StudyPlanImportResolver(private val db: AppDatabase) {
    suspend fun resolve(file: StudyPlanFileV1): ResolvedStudyPlanImport {
        val competition = db.dao().competitionByExternalId(file.competition.externalId)
        val requestedSubjects = buildSet {
            file.subjects.forEach { add(it.externalId) }
            file.tasks.mapNotNullTo(this) { it.subjectExternalId }
        }
        val requestedTopics = file.tasks.mapNotNullTo(linkedSetOf()) { it.topicExternalId }
        val subjects = requestedSubjects.mapNotNull { id -> db.dao().subjectByExternalId(id)?.let { id to it } }.toMap()
        val topics = requestedTopics.mapNotNull { id -> db.dao().topicByExternalId(id)?.let { id to it } }.toMap()
        val unresolved = buildList {
            if (competition == null) add("concurso:${file.competition.externalId}")
            requestedSubjects.filterNot(subjects::containsKey).forEach { add("materia:$it") }
            requestedTopics.filterNot(topics::containsKey).forEach { add("topico:$it") }
        }
        return ResolvedStudyPlanImport(file, competition, subjects, topics, unresolved)
    }
}
