package br.com.estudario.domain

/**
 * Atualização do edital que a pessoa escolheu no catálogo, sem apagar nada (o mesmo no app e no site).
 *
 * Compara as matérias e tópicos da pessoa com a versão nova do catálogo e diz o que fazer:
 *  - tópico novo no catálogo: entra na matéria;
 *  - tópico renomeado (texto parecido com um que saiu): só troca o nome, o progresso fica;
 *  - matéria nova: entra com os tópicos; matéria renomeada (mesmos tópicos): só troca o nome;
 *  - tópico que saiu do edital: NUNCA é apagado, fica marcado "saiu do edital" (fora do plano);
 *  - tópico que voltou ao catálogo: desmarca;
 *  - tópico que a pessoa criou: nunca é tocado.
 *
 * Para saber o que veio do catálogo, a plataforma guarda a [Plan.baseline] (os tópicos do catálogo
 * na última aplicação). Sem ela (primeira vez), só acrescenta e renomeia: nada é marcado como saído.
 */
object EditalSync {
    data class CatalogSubject(val name: String, val topics: List<String>)
    data class Topic(val id: Long, val title: String)
    data class Subject(val id: Long, val name: String, val topics: List<Topic>)

    data class Plan(
        /** Matérias novas, com os tópicos delas. */
        val newSubjects: List<CatalogSubject>,
        /** Tópicos novos em matérias que a pessoa já tem: (id da matéria, título, posição no catálogo). */
        val newTopics: List<Triple<Long, String, Int>>,
        val renamedSubjects: List<Pair<Long, String>>,
        val renamedTopics: List<Pair<Long, String>>,
        /** Tópicos que saíram do edital (marcar, nunca apagar). */
        val removed: Set<Long>,
        /** Tópicos que estão no edital (desmarcar, se estavam marcados como saídos). */
        val inEdital: Set<Long>,
        /** A lista do catálogo aplicada, para guardar e comparar na próxima vez. */
        val baseline: Map<String, List<String>>,
    ) {
        val changes: Int get() = newSubjects.size + newTopics.size + renamedSubjects.size + renamedTopics.size + newSubjects.sumOf { it.topics.size }
    }

    /** Minúsculas, sem acento, sem pontuação nem numeração ("1.2 Crase." → "crase"). */
    fun norm(value: String): String {
        val plain = buildString {
            for (c in value.lowercase()) append(ACCENTS[c] ?: c)
        }
        return plain.replace(Regex("[^a-z0-9]+"), " ").trim().replace(Regex("^(\\d+ )+"), "").trim()
    }

    private val ACCENTS = mapOf(
        'á' to 'a', 'à' to 'a', 'â' to 'a', 'ã' to 'a', 'ä' to 'a', 'é' to 'e', 'ê' to 'e', 'è' to 'e', 'ë' to 'e',
        'í' to 'i', 'î' to 'i', 'ì' to 'i', 'ï' to 'i', 'ó' to 'o', 'ô' to 'o', 'õ' to 'o', 'ò' to 'o', 'ö' to 'o',
        'ú' to 'u', 'û' to 'u', 'ù' to 'u', 'ü' to 'u', 'ç' to 'c', 'ñ' to 'n',
    )

    private val STOP = setOf("de", "da", "do", "das", "dos", "e", "a", "o", "as", "os", "em", "na", "no", "nas", "nos", "para", "com", "por", "um", "uma")
    private fun words(value: String): Set<String> = norm(value).split(' ').filter { it.length > 1 && it !in STOP }.toSet()

    /** Parecido: mesmas palavras em boa parte (Jaccard), para reconhecer um tópico renomeado. */
    fun similarity(a: String, b: String): Double {
        val x = words(a)
        val y = words(b)
        if (x.isEmpty() || y.isEmpty()) return 0.0
        return (x intersect y).size.toDouble() / (x union y).size
    }

    /**
     * O edital da pessoa veio mesmo deste edital do catálogo? Para concursos reconhecidos só pelo nome
     * (sem o vínculo gravado na escolha): 60% ou mais dos tópicos dela estão, idênticos, no catálogo.
     * Um edital montado por PDF com o mesmo nome não recebe os tópicos do catálogo.
     */
    fun cameFrom(current: List<Subject>, catalog: List<CatalogSubject>): Boolean {
        val mine = current.flatMap { it.topics }.map { norm(it.title) }
        if (mine.isEmpty()) return false
        val known = catalog.flatMap { it.topics }.map(::norm).toSet()
        return mine.count { it in known } * 10 >= mine.size * 6
    }

    fun plan(current: List<Subject>, catalog: List<CatalogSubject>, baseline: Map<String, List<String>>?): Plan {
        val newSubjects = mutableListOf<CatalogSubject>()
        val newTopics = mutableListOf<Triple<Long, String, Int>>()
        val renamedSubjects = mutableListOf<Pair<Long, String>>()
        val renamedTopics = mutableListOf<Pair<Long, String>>()
        val inEdital = hashSetOf<Long>()
        val renamedIds = hashSetOf<Long>()
        val catalogNorms = catalog.map { norm(it.name) }.toSet()
        val byName = current.associateBy { norm(it.name) }
        val usedSubjects = hashSetOf<Long>()
        // Tópicos que o catálogo tinha antes e não tem mais, por matéria (só com a lista anterior).
        val newTitlesBySubject = catalog.associate { norm(it.name) to it.topics.map(::norm).toSet() }

        for (subject in catalog) {
            val key = norm(subject.name)
            var mine = byName[key]?.takeIf { it.id !in usedSubjects }
            if (mine == null) {
                // Matéria renomeada: uma matéria da pessoa que não está mais no catálogo e tem metade ou
                // mais dos tópicos desta.
                val wanted = subject.topics.map(::norm).toSet()
                mine = current.filter { it.id !in usedSubjects && norm(it.name) !in catalogNorms }
                    .map { s -> s to s.topics.count { norm(it.title) in wanted } }
                    .filter { (s, hits) -> hits > 0 && hits * 2 >= minOf(wanted.size, s.topics.size) }
                    .maxByOrNull { it.second }?.first
                if (mine != null) renamedSubjects += mine.id to subject.name
            }
            if (mine == null) { newSubjects += subject; continue }
            usedSubjects += mine.id
            val mineByNorm = mine.topics.associateBy { norm(it.title) }
            val oldTitles = baseline?.get(norm(mine.name))?.map(::norm)?.toSet() ?: baseline?.get(key)?.map(::norm)?.toSet()
            val wanted = subject.topics.map(::norm).toSet()
            // Quem pode ter sido renomeado: com a lista anterior, só os que o catálogo tinha e tirou; sem
            // ela, qualquer tópico da pessoa fora do catálogo, com uma exigência maior de parecença.
            val candidates = mine.topics.filter { norm(it.title) !in wanted && (oldTitles == null || norm(it.title) in oldTitles) }.toMutableList()
            subject.topics.forEachIndexed { position, title ->
                val n = norm(title)
                val hit = mineByNorm[n]
                if (hit != null) { inEdital += hit.id; return@forEachIndexed }
                val minimum = if (oldTitles == null) 0.75 else 0.5
                val best = candidates.map { it to similarity(it.title, title) }.filter { it.second >= minimum }.maxByOrNull { it.second }?.first
                if (best != null) {
                    candidates.remove(best)
                    renamedTopics += best.id to title
                    renamedIds += best.id
                    inEdital += best.id
                } else newTopics += Triple(mine.id, title, position)
            }
        }
        // Saíram do edital: os que o catálogo tinha na última aplicação e não tem mais (nunca os da pessoa).
        val removed = hashSetOf<Long>()
        if (baseline != null) {
            for (subject in current) {
                val old = (baseline[norm(subject.name)] ?: continue).map(::norm).toSet()
                val now = newTitlesBySubject[norm(subject.name)] ?: renamedSubjects.firstOrNull { it.first == subject.id }?.let { newTitlesBySubject[norm(it.second)] } ?: emptySet()
                subject.topics.forEach { t -> if (norm(t.title) in old && norm(t.title) !in now && t.id !in renamedIds && t.id !in inEdital) removed += t.id }
            }
        }
        return Plan(newSubjects, newTopics, renamedSubjects, renamedTopics, removed, inEdital, catalog.associate { norm(it.name) to it.topics })
    }
}
