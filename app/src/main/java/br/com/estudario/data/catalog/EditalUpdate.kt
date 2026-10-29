package br.com.estudario.data.catalog

import android.content.Context
import androidx.room.withTransaction
import br.com.estudario.EstudarioApplication
import br.com.estudario.domain.EditalSync
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

/**
 * O edital escolhido no catálogo se atualiza sozinho (o mesmo no site; a regra em EditalSync): uma vez
 * por dia o app confere a data de atualização dos editais da pessoa e, se a equipe corrigiu algum,
 * acrescenta tópicos e matérias novos, renomeia os que mudaram de nome e marca "saiu do edital" os que
 * o catálogo tirou (nunca apaga: progresso, matérias geradas e erros ficam; o tópico só sai do plano).
 *
 * Guardado aqui, por concurso: a data da versão aplicada, a lista de tópicos dela (que diz o que veio
 * do catálogo e o que a pessoa criou) e os tópicos que saíram.
 */
object EditalUpdate {
    private const val PREFS = "edital_atualizacao"
    private const val DAY = "dia"

    data class Applied(val competition: String, val added: Int, val renamed: Int, val removed: Int)

    private fun prefs(context: Context) = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Os tópicos que saíram do edital (fora do plano, com tudo guardado). */
    fun outOfEdital(context: Context): Set<Long> =
        prefs(context).all.filterKeys { it.startsWith("fora:") }.values.flatMap { (it as? Set<*>).orEmpty() }.mapNotNullTo(hashSetOf()) { (it as? String)?.toLongOrNull() }

    /** Na escolha do edital: guarda a lista desta versão (a próxima atualização compara com ela). */
    fun remember(context: Context, competitionId: Long, subjects: List<ExamCatalogSubject>) {
        prefs(context).edit().putString("base:$competitionId", baselineJson(subjects.associate { EditalSync.norm(it.name) to it.topics })).apply()
    }

    private fun baselineJson(baseline: Map<String, List<String>>): String =
        JSONObject().apply { baseline.forEach { (k, v) -> put(k, JSONArray(v)) } }.toString()

    private fun baselineOf(context: Context, competitionId: Long): Map<String, List<String>>? = runCatching {
        val json = JSONObject(prefs(context).getString("base:$competitionId", null) ?: return null)
        json.keys().asSequence().associateWith { key -> json.getJSONArray(key).let { a -> (0 until a.length()).map { a.getString(it) } } }
    }.getOrNull()

    suspend fun runDaily(app: EstudarioApplication, force: Boolean = false): List<Applied> {
        val today = LocalDate.now().toString()
        if (!force && prefs(app).getString(DAY, null) == today) return emptyList()
        val dao = app.database.dao()
        val catalog = ExamCatalogProvider.get(app)
        if (!catalog.isConfigured) return emptyList()
        val competitions = dao.competitionsOnce()
        if (competitions.isEmpty()) return emptyList()
        val linked = competitions.filter { prefs(app).getString("ignorar:${it.id}", null) == null }.mapNotNull { c -> runCatching { catalog.catalogIdFor(c.name, CatalogLinks.get(app, c.id)) }.getOrNull()?.let { c to it } }
        if (linked.isEmpty()) { prefs(app).edit().putString(DAY, today).apply(); return emptyList() }
        // Só as datas de atualização (barato); o edital inteiro só quando mudou.
        val stamps = runCatching { catalog.updatedAt(linked.map { it.second }) }.getOrNull() ?: return emptyList()
        val applied = mutableListOf<Applied>()
        for ((competition, catalogId) in linked) {
            val stamp = stamps[catalogId] ?: continue
            if (prefs(app).getString("stamp:${competition.id}", null) == stamp) continue
            val subjects = runCatching { catalog.subjects(catalogId) }.getOrNull() ?: continue
            val trusted = CatalogLinks.get(app, competition.id) != null || baselineOf(app, competition.id) != null
            val result = apply(app, competition.id, competition.name, subjects, trusted)
            if (result == null) { prefs(app).edit().putString("ignorar:${competition.id}", catalogId).apply(); continue }
            CatalogLinks.save(app, competition.id, catalogId)
            prefs(app).edit().putString("stamp:${competition.id}", stamp).apply()
            if (result.added + result.renamed + result.removed > 0) applied += result
        }
        prefs(app).edit().putString(DAY, today).apply()
        return applied
    }

    /** Nulo quando o concurso, reconhecido só pelo nome, não veio deste edital (montado por PDF, por exemplo). */
    private suspend fun apply(app: EstudarioApplication, competitionId: Long, name: String, catalog: List<ExamCatalogSubject>, trusted: Boolean): Applied? {
        val dao = app.database.dao()
        val repository = app.repository
        val subjects = dao.subjectsFor(competitionId)
        val topicsBySubject = subjects.associate { s -> s.id to dao.topicsFor(s.id) }
        val current = subjects.map { s -> EditalSync.Subject(s.id, s.name, topicsBySubject[s.id].orEmpty().filter { it.parentTopicId == null }.map { EditalSync.Topic(it.id, it.title) }) }
        val catalogSubjects = catalog.map { EditalSync.CatalogSubject(it.name, it.topics) }
        if (!trusted && !EditalSync.cameFrom(current, catalogSubjects)) return null
        val plan = EditalSync.plan(current, catalogSubjects, baselineOf(app, competitionId))
        app.database.withTransaction {
            plan.renamedSubjects.forEach { (id, newName) -> subjects.firstOrNull { it.id == id }?.let { dao.updateSubject(it.copy(name = newName)) } }
            val allTopics = topicsBySubject.values.flatten().associateBy { it.id }
            plan.renamedTopics.forEach { (id, title) -> allTopics[id]?.let { dao.updateTopic(it.copy(title = title)) } }
            plan.newTopics.forEach { (subjectId, title, _) -> repository.addTopic(subjectId, title) }
            plan.newSubjects.forEach { subject ->
                val subjectId = repository.addSubject(competitionId, subject.name)
                subject.topics.forEach { repository.addTopic(subjectId, it) }
            }
        }
        // Peso da prova atualizado (sem mexer no que a pessoa trocou à mão).
        runCatching { ExamWeightsApplier.apply(dao, competitionId, catalog) }
        // Saíram do edital: os marcados agora; os que voltaram saem da marca.
        val key = "fora:$competitionId"
        val out = prefs(app).getStringSet(key, emptySet()).orEmpty().mapNotNullTo(hashSetOf()) { it.toLongOrNull() }
        out.removeAll(plan.inEdital)
        out.addAll(plan.removed)
        prefs(app).edit().putStringSet(key, out.mapTo(hashSetOf()) { it.toString() }).putString("base:$competitionId", baselineJson(plan.baseline)).apply()
        val added = plan.newTopics.size + plan.newSubjects.sumOf { it.topics.size }
        return Applied(name, added, plan.renamedTopics.size + plan.renamedSubjects.size, plan.removed.size)
    }

}
