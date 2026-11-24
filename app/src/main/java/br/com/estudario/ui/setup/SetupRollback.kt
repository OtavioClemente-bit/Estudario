package br.com.estudario.ui.setup

/*
 * Cancelar o "Novo concurso"/"Novo plano" desfaz o que o assistente gravou.
 *
 * O assistente grava no banco ao longo dos passos (escolher o edital no catálogo já cria o concurso,
 * as matérias e os tópicos; montar o plano cria o plano). Antes, Cancelar só devolvia a etapa da
 * configuração e o concurso escolhido ficava em "Meus concursos". Agora, ao abrir o assistente pelo
 * app, guardamos um retrato do que já existia; ao cancelar, sai só o que não estava no retrato.
 * O retrato mora nas preferências (não na memória), então fechar o app no meio e voltar depois
 * continua a mesma configuração, e Cancelar ainda desfaz tudo.
 */

/** O que existia antes de abrir o assistente. */
internal data class SetupBaseline(
    val competitionIds: Set<Long> = emptySet(),
    val subjectIds: Set<Long> = emptySet(),
    val planIds: Set<String> = emptySet(),
    val primaryCompetitionId: Long? = null,
    val activePlanIds: Set<String> = emptySet(),
    val masterPlanIds: Set<String> = emptySet(),
) {
    fun encode(): String = listOf(
        competitionIds.joinToString(","),
        subjectIds.joinToString(","),
        planIds.joinToString(SEP),
        primaryCompetitionId?.toString().orEmpty(),
        activePlanIds.joinToString(SEP),
        masterPlanIds.joinToString(SEP),
    ).joinToString(FIELD)

    companion object {
        private const val FIELD = "\n"
        private const val SEP = "\t"

        fun decode(raw: String?): SetupBaseline? {
            if (raw.isNullOrEmpty()) return null
            val parts = raw.split(FIELD)
            if (parts.size != 6) return null
            fun longs(s: String) = s.split(",").mapNotNull { it.trim().toLongOrNull() }.toSet()
            fun strings(s: String) = s.split(SEP).filter { it.isNotBlank() }.toSet()
            return SetupBaseline(
                competitionIds = longs(parts[0]),
                subjectIds = longs(parts[1]),
                planIds = strings(parts[2]),
                primaryCompetitionId = parts[3].toLongOrNull(),
                activePlanIds = strings(parts[4]),
                masterPlanIds = strings(parts[5]),
            )
        }
    }
}

/** O que sai ao cancelar. Matéria de concurso novo vai junto com o concurso (não entra em [subjectIds]). */
internal data class SetupRollbackPlan(
    val competitionIds: List<Long>,
    val subjectIds: List<Long>,
    val planIds: List<String>,
) {
    val isEmpty: Boolean get() = competitionIds.isEmpty() && subjectIds.isEmpty() && planIds.isEmpty()
}

internal object SetupRollback {
    /**
     * Compara o retrato com o banco agora. Só conta como "criado pelo assistente" o id acima do maior
     * do retrato (os ids do banco só crescem): um concurso que já existia nunca é apagado, nem se a
     * lista mudar de ordem.
     */
    fun plan(
        baseline: SetupBaseline,
        competitionIds: Collection<Long>,
        subjects: Collection<Pair<Long, Long>>,
        planIds: Collection<String>,
    ): SetupRollbackPlan {
        val maxCompetition = baseline.competitionIds.maxOrNull() ?: 0L
        val maxSubject = baseline.subjectIds.maxOrNull() ?: 0L
        val newCompetitions = competitionIds.filter { it !in baseline.competitionIds && it > maxCompetition }.sorted()
        val newCompetitionSet = newCompetitions.toSet()
        val newSubjects = subjects
            .filter { (id, competitionId) -> id !in baseline.subjectIds && id > maxSubject && competitionId !in newCompetitionSet }
            .map { it.first }
            .sorted()
        val newPlans = planIds.filter { it !in baseline.planIds }.sorted()
        return SetupRollbackPlan(newCompetitions, newSubjects, newPlans)
    }
}

/** Preferência com o retrato acima (no mesmo arquivo do modo "novo plano"). */
const val PLAN_MODE_BASELINE = "plan_mode_baseline"
/** A configuração inteira de antes, para Cancelar devolver o concurso e o plano que estavam escolhidos. */
const val PLAN_MODE_PREVIOUS_SNAPSHOT = "plan_mode_previous_snapshot"

/**
 * Qual configuração a tela do assistente segue. A das preferências chega na hora; a calculada pelo
 * ViewModel (junto com o banco de questões) chega depois e, logo após "+ Novo", ainda é a da
 * configuração anterior (etapa final, concluída). A tela segue sempre a mais nova.
 */
internal object SetupFreshState {
    fun effectiveSnapshot(
        live: br.com.estudario.domain.setup.InitialSetupSnapshot?,
        computed: br.com.estudario.domain.setup.InitialSetupSnapshot,
    ): br.com.estudario.domain.setup.InitialSetupSnapshot = live ?: computed

    /** O estado calculado ainda é o de antes (as matérias e o concurso da tela podem estar velhos). */
    fun isStale(
        live: br.com.estudario.domain.setup.InitialSetupSnapshot?,
        computed: br.com.estudario.domain.setup.InitialSetupSnapshot,
    ): Boolean = live != null && (live.status != computed.status || live.competitionId != computed.competitionId)
}
