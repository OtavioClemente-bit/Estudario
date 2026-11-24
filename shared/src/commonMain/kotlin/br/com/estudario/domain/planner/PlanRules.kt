package br.com.estudario.domain.planner

/**
 * Regras do plano que o site e o app precisam contar igual (antes cada um tinha a sua cópia e o
 * celular mostrava "atividade atrasada" que o site não mostrava).
 *
 * Os status chegam como texto (o site guarda o nome do enum na foto); o app passa `status.name`.
 */
object PlanRules {
    private val PENDING = setOf(PlanTaskStatus.PLANEJADA.name, PlanTaskStatus.EM_ANDAMENTO.name)

    /** Ainda por fazer: planejada ou começada. Reprogramada, cancelada, pausada, não realizada e concluída ficam de fora. */
    fun isPending(status: String): Boolean = status in PENDING

    /** Vale como carga do dia: tudo menos a cópia antiga de um replanejamento e a cancelada. */
    fun isCurrent(status: String): Boolean = status != PlanTaskStatus.REPROGRAMADA.name && status != "CANCELADA"

    /** Atrasada: de um dia que já passou (antes de hoje, no fuso do aparelho) e ainda por fazer. */
    fun isOverdue(day: Long, status: String, todayEpoch: Long): Boolean = day < todayEpoch && isPending(status)

    /**
     * Tarefas que já têm continuação (outra tarefa nasceu delas num replanejamento, em qualquer aparelho):
     * não são "atrasadas" mesmo se o status ficou planejado (a junção de dois aparelhos pode deixar a
     * original aberta enquanto a cópia segue no plano). [replannedFrom] são os replannedFrom de todas as
     * tarefas do plano.
     */
    fun replaced(replannedFrom: Collection<String?>): Set<String> = replannedFrom.filterNotNullTo(hashSetOf())

    /** 31/10/2026 00:00 em Brasília: o método novo do plano (30/10) já estava no app e no site. */
    const val METHOD2_SINCE_MS = 1_793_415_600_000L

    /**
     * O plano ainda é do método antigo e precisa ser refeito uma vez? Só se nenhuma reorganização dele foi
     * gravada depois do método novo, em qualquer aparelho. Antes valia só a marca local (preferência do app,
     * localStorage do site): cada navegador novo ou dados do site limpos refaziam o plano inteiro do zero
     * (o "0 tarefa(s) planejada(s)" seguido de 83 no log real).
     */
    fun needsMethodUpgrade(proposalRevisionTimes: Collection<Long>): Boolean = proposalRevisionTimes.none { it >= METHOD2_SINCE_MS }

    /** Plano visto como candidato a "ativo". */
    data class PlanRef<T>(val value: T, val active: Boolean, val archived: Boolean, val competitionId: Long?)

    /**
     * O plano ativo: o ativo do concurso principal; senão o primeiro ativo. Antes o site pegava o
     * primeiro ativo (ou qualquer um não arquivado) e o app o do concurso principal: com dois planos
     * ativos cada tela mostrava as tarefas de um plano diferente.
     */
    fun <T> activePlan(plans: List<PlanRef<T>>, primaryCompetitionId: Long?): T? =
        (plans.firstOrNull { it.active && !it.archived && (primaryCompetitionId == null || it.competitionId == primaryCompetitionId) }
            ?: plans.firstOrNull { it.active && !it.archived })?.value
}
