package br.com.meuconcurso.data.transfer.planner

import br.com.meuconcurso.data.local.*
import org.json.JSONObject
import java.text.Normalizer
import java.util.Locale

enum class LinkResolutionKind { DIVERGENT_ID, AMBIGUOUS, NOT_FOUND, WITHOUT_OFFICIAL_ID, CONTEXT_MISMATCH }
data class ImportLinkCandidate(val localId: Long, val name: String, val externalId: String)
data class ImportLinkIssue(val key: String, val label: String, val kind: LinkResolutionKind, val candidates: List<ImportLinkCandidate>, val message: String)
data class PreparedStudyPlanImport(val normalizedText: String, val resolved: ResolvedStudyPlanImport?, val issues: List<ImportLinkIssue>)
data class ResolvedStudyPlanImport(val file: StudyPlanFileV1, val competition: CompetitionEntity?, val subjects: Map<String, SubjectEntity>, val topics: Map<String, TopicEntity>, val unresolvedReferences: List<String>)

class StudyPlanImportResolver(private val db: AppDatabase, private val codec: StudyPlanCodec = StudyPlanCodec()) {
    suspend fun prepare(text: String, selections: Map<String, Long> = emptyMap()): PreparedStudyPlanImport {
        val root = try { JSONObject(text) } catch (_: Exception) { throw StudyPlanValidationException("O arquivo .plano não contém JSON válido.") }
        if (root.optString("format") != StudyPlanCodec.FORMAT) throw StudyPlanValidationException("Este arquivo não parece ser um Plano do Meu Concurso.")
        if (root.optInt("version", -1) != StudyPlanCodec.VERSION) throw StudyPlanValidationException("Este plano foi criado em uma versão de formato ainda não suportada.")
        val competitionJson = root.optJSONObject("concurso") ?: throw StudyPlanValidationException("concurso: objeto obrigatório ausente.")
        val competitions = db.dao().competitionsOnce(); val allSubjects = db.dao().subjectsOnce(); val allTopics = db.dao().topicsOnce()
        val issues = mutableListOf<ImportLinkIssue>()
        val competition = resolveCompetition(competitionJson.optText("externalId"), competitionJson.optText("nome"), competitions, selections["competition"], issues)
        if (competition != null) competitionJson.put("externalId", competition.externalId)
        val priorities = root.optJSONArray("prioridades")
        if (competition != null && priorities != null) for (index in 0 until priorities.length()) {
            val item = priorities.getJSONObject(index); val key = "priority:$index"
            resolveSubject(key, item.optText("externalId"), item.optText("nome"), competition, allSubjects, selections[key], issues, true)?.let { item.put("externalId", it.externalId) }
        }
        val tasks = root.optJSONArray("tarefas")
        if (competition != null && tasks != null) for (index in 0 until tasks.length()) {
            val item = tasks.getJSONObject(index); val subjectKey = "task:$index:subject"
            val subject = resolveSubject(subjectKey, item.optText("materiaExternalId"), item.optText("materiaNome"), competition, allSubjects, selections[subjectKey], issues, false)
            if (subject != null) {
                item.put("materiaExternalId", subject.externalId)
                val topicKey = "task:$index:topic"
                resolveTopic(topicKey, item.optText("topicoExternalId"), item.optText("topicoNome"), subject, allTopics, selections[topicKey], issues)?.let { item.put("topicoExternalId", it.externalId) }
            } else if (item.optText("topicoExternalId") != null || item.optText("topicoNome") != null) {
                issues += issue("task:$index:topic", item.optText("topicoNome") ?: "tópico", LinkResolutionKind.CONTEXT_MISMATCH, emptyList(), "Não é possível vincular o tópico sem resolver primeiro a matéria da tarefa.")
            }
        }
        val normalized = root.toString()
        if (issues.isNotEmpty()) return PreparedStudyPlanImport(normalized, null, issues)
        return PreparedStudyPlanImport(normalized, resolve(codec.decode(normalized)), emptyList())
    }

    suspend fun resolve(file: StudyPlanFileV1): ResolvedStudyPlanImport {
        val competition = db.dao().competitionByExternalId(file.competition.externalId)
        val requestedSubjects = buildSet { file.subjects.forEach { add(it.externalId) }; file.tasks.mapNotNullTo(this) { it.subjectExternalId } }
        val requestedTopics = file.tasks.mapNotNullTo(linkedSetOf()) { it.topicExternalId }
        val subjects = requestedSubjects.mapNotNull { id -> db.dao().subjectByExternalId(id)?.let { id to it } }.toMap()
        val topics = requestedTopics.mapNotNull { id -> db.dao().topicByExternalId(id)?.let { id to it } }.toMap()
        val unresolved = buildList { if (competition == null) add("concurso:${file.competition.externalId}"); requestedSubjects.filterNot(subjects::containsKey).forEach { add("materia:$it") }; requestedTopics.filterNot(topics::containsKey).forEach { add("topico:$it") } }
        return ResolvedStudyPlanImport(file, competition, subjects, topics, unresolved)
    }

    private fun resolveCompetition(id: String?, name: String?, rows: List<CompetitionEntity>, selected: Long?, issues: MutableList<ImportLinkIssue>): CompetitionEntity? {
        selected?.let { return rows.firstOrNull { row -> row.id == it && row.externalId != null } }
        id?.let { value -> rows.firstOrNull { it.externalId == value }?.let { return it } }
        return decide("competition", "concurso", id, nameMatches(name, rows) { it.name }, issues) { it.id to (it.externalId ?: "") }
    }
    private fun resolveSubject(key: String, id: String?, name: String?, competition: CompetitionEntity, rows: List<SubjectEntity>, selected: Long?, issues: MutableList<ImportLinkIssue>, required: Boolean): SubjectEntity? {
        val scoped = rows.filter { it.competitionId == competition.id }
        selected?.let { return scoped.firstOrNull { row -> row.id == it && row.externalId != null } }
        val byId = id?.let { value -> rows.firstOrNull { it.externalId == value } }
        if (byId != null && byId.competitionId != competition.id) {
            val candidates = nameMatches(name, scoped) { it.name }.filter { it.externalId != null }.map { candidate(it) { row -> row.id to (row.externalId ?: "") } }
            issues += issue(key, name ?: id, LinkResolutionKind.CONTEXT_MISMATCH, candidates, "A matéria informada pertence a outro concurso. Revise o vínculo dentro do concurso correto.")
            return null
        }
        if (byId != null) return byId
        if (!required && id == null && name == null) return null
        return decide(key, "matéria ${name.orEmpty()}", id, nameMatches(name, scoped) { it.name }, issues) { it.id to (it.externalId ?: "") }
    }
    private fun resolveTopic(key: String, id: String?, name: String?, subject: SubjectEntity, rows: List<TopicEntity>, selected: Long?, issues: MutableList<ImportLinkIssue>): TopicEntity? {
        val scoped = rows.filter { it.subjectId == subject.id }
        selected?.let { return scoped.firstOrNull { row -> row.id == it && row.externalId != null } }
        val byId = id?.let { value -> rows.firstOrNull { it.externalId == value } }
        if (byId != null && byId.subjectId != subject.id) {
            val candidates = nameMatches(name, scoped) { it.title }.filter { it.externalId != null }.map { candidate(it) { row -> row.id to (row.externalId ?: "") } }
            issues += issue(key, name ?: id, LinkResolutionKind.CONTEXT_MISMATCH, candidates, "O tópico informado pertence a outra matéria. Revise o vínculo dentro da matéria correta.")
            return null
        }
        if (byId != null) return byId
        if (id == null && name == null) return null
        return decide(key, "tópico ${name.orEmpty()}", id, nameMatches(name, scoped) { it.title }, issues) { it.id to (it.externalId ?: "") }
    }
    private fun <T> decide(key: String, label: String, suppliedId: String?, matches: List<T>, issues: MutableList<ImportLinkIssue>, identity: (T) -> Pair<Long, String>): T? {
        val official = matches.filter { identity(it).second.isNotBlank() }
        if (matches.size == 1 && official.isEmpty()) { issues += issue(key, label, LinkResolutionKind.WITHOUT_OFFICIAL_ID, emptyList(), "Encontramos $label, mas ele não possui identificador oficial."); return null }
        if (official.size == 1 && suppliedId == null) return official.single()
        if (official.size == 1 && suppliedId != null) { issues += issue(key, label, LinkResolutionKind.DIVERGENT_ID, official.map { candidate(it, identity) }, "O identificador informado não existe, mas encontramos $label pelo nome. Revise o vínculo."); return null }
        if (official.size > 1) { issues += issue(key, label, LinkResolutionKind.AMBIGUOUS, official.map { candidate(it, identity) }, "Encontramos mais de um registro compatível para $label."); return null }
        issues += issue(key, label, LinkResolutionKind.NOT_FOUND, emptyList(), "Não encontramos $label no aplicativo com identificador oficial."); return null
    }
    private fun <T> candidate(row: T, identity: (T) -> Pair<Long, String>): ImportLinkCandidate { val (localId, externalId) = identity(row); val name = when (row) { is CompetitionEntity -> row.name; is SubjectEntity -> row.name; is TopicEntity -> row.title; else -> externalId }; return ImportLinkCandidate(localId, name, externalId) }
    private fun issue(key: String, label: String, kind: LinkResolutionKind, candidates: List<ImportLinkCandidate>, message: String) = ImportLinkIssue(key, label, kind, candidates, message)
    private fun <T> nameMatches(name: String?, rows: List<T>, title: (T) -> String): List<T> = name?.let { wanted -> rows.filter { normalize(title(it)) == normalize(wanted) } }.orEmpty()
    private fun normalize(value: String): String = Normalizer.normalize(value.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD).replace("\\p{M}+".toRegex(), "").replace("\\s+".toRegex(), " ")
}
private fun JSONObject.optText(key: String): String? = if (!has(key) || isNull(key)) null else optString(key).trim().takeIf(String::isNotEmpty)
