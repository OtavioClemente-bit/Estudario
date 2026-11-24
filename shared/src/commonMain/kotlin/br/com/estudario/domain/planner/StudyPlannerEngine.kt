package br.com.estudario.domain.planner

import br.com.estudario.text.Sha256
import br.com.estudario.text.toHex
import kotlinx.datetime.LocalDate
import br.com.estudario.time.*

class StudyPlannerEngine(
    private val scoring: PlannerScoringPolicy = PlannerScoringPolicy(),
) {
    fun plan(snapshot: StudyPlannerSnapshot, reason: ReplanReason): PlanningProposal {
        val dates = (0 until snapshot.policy.horizonDays).map { snapshot.today.plusDays(it.toLong()) }
        val rawCapacity = dates.associateWith { date -> capacityFor(snapshot, date) }
        // Grátis: se hoje já tem estudo, hoje fica como está (trocar edital, horas ou matérias não
        // muda a matéria do dia); o replanejamento vale de amanhã em diante.
        // "Hoje não vou conseguir" (o dia marcado sem estudo) vale também no grátis: o dia sai do plano.
        val todayOff = snapshot.dayOverrides[snapshot.today]?.unavailable == true
        // Reorganização automática (freezeToday): hoje fica como está também nos pagos, e nada do dia sai nem muda.
        val frozenToday = snapshot.policy.freezeToday && !todayOff && reason != ReplanReason.INITIAL
        val keepToday = (snapshot.policy.keepTodayTasks || snapshot.policy.freezeToday) && !todayOff && reason != ReplanReason.INITIAL && snapshot.tasks.any {
            it.date == snapshot.today && it.status !in terminalInactiveStatuses
        }
        // Semana estável: concluir, pular, fazer parte de uma tarefa ou virar o dia não refaz os próximos
        // dias. Antes cada conclusão apagava e recriava o mês inteiro (6 a 16 vezes por dia, 1.500
        // tarefas remarcadas em 4 dias numa conta real) e o que a pessoa via ontem mudava hoje. Só o
        // que vem depois da janela se reorganiza; mudar horas, matérias ou recriar o plano refaz tudo.
        val stableUntil = if (reason in stableReasons) snapshot.today.plusDays(STABLE_DAYS.toLong()) else null
        // Concluir, pular ou fazer parte de uma tarefa não mexe no resto do dia nem nas atrasadas: o que
        // falta hoje fica hoje, e a atrasada espera a pessoa escolher (fazer agora, encaixar ou empurrar).
        // Antes, concluir a teoria do dia puxava a teoria atrasada para hoje e as questões do dia iam
        // para a semana seguinte, sem ninguém pedir (caso real de 09/11).
        val dayKeeping = reason in dayKeepingReasons
        // Tópico com a teoria atrasada (de um dia que passou e não foi feita): as questões dele não ficam
        // presas na semana estável, voltam a ser encaixadas depois da teoria. Questão antes da teoria não.
        val theoryLate = snapshot.tasks.filter {
            it.type == PlanTaskType.THEORY && it.topicId != null && it.date < snapshot.today &&
                it.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        }.mapTo(hashSetOf()) { it.topicId }
        // Teoria concluída é o tópico lido: as outras partes da mesma teoria (o plano dividia a leitura em 2 a 4
        // dias) saem do plano. Antes a pessoa lia a matéria inteira e a "parte 2" e a "parte 3" continuavam nos
        // dias seguintes, parecendo o plano repetindo a mesma teoria (caso real de 18/11). Quem leu só um pedaço
        // marca "fiz uma parte" (em andamento), e aí o resto continua.
        // Só as partes da mesma leitura. A teoria de retomada, que volta 30 dias ou mais depois para o tópico em
        // que a pessoa segue errando, é outra leitura e fica: sem essa distinção ela era tirada e recriada a cada
        // reorganização, mudando de dia e sumindo do dia de hoje.
        val theoryDoneOn = snapshot.tasks.filter {
            it.type == PlanTaskType.THEORY && it.topicId != null && it.status == PlanTaskStatus.CONCLUIDA
        }.groupBy({ it.topicId }, { it.date })
        // Tópico estudado fora do plano (marcado como estudado no edital, lido na biblioteca, prática livre): a
        // teoria dele que o plano ainda tinha marcada também já foi lida. Antes ela ficava na semana estável e a
        // pessoa via de novo a teoria do que acabou de estudar. Só quando nenhuma teoria do plano foi concluída
        // (aí vale a regra de cima) e só a teoria marcada até 30 dias depois da leitura (a retomada fica).
        val leftoverTheory: (PlannerTask) -> Boolean = { task ->
            task.type == PlanTaskType.THEORY && task.status == PlanTaskStatus.PLANEJADA && !task.locked && (
                theoryDoneOn[task.topicId].orEmpty().any { daysBetween(it, task.date) < LEFTOVER_THEORY_DAYS } ||
                    (task.topicId !in theoryDoneOn && snapshot.topicStudiedOn[task.topicId]?.let { daysBetween(it, task.date) in 0 until LEFTOVER_THEORY_DAYS } == true)
                )
        }
        val liveStatuses = setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        val executionsByTask = snapshot.executions.filter { it.taskId != null }.groupBy { it.taskId }
        // Sobras de dois aparelhos: cada um reorganizou o plano sem ver o outro e, ao sincronizar, as duas cópias
        // da mesma tarefa ficaram vivas (e dobravam de novo a cada vez). De um tópico fica uma leitura só (até o
        // tamanho da teoria dele), as questões até o que o tópico tem, um cartão por dia e tipo, e um simulado.
        val surplus = hashSetOf<String>()
        run {
            // Só o que o próprio motor marcou e ninguém tocou (tarefa criada ou importada pela pessoa não entra).
            val untouched = snapshot.tasks.filter {
                it.status == PlanTaskStatus.PLANEJADA && it.origin == PlanOrigin.ENGINE && !it.locked && executionsByTask[it.id].isNullOrEmpty() && !leftoverTheory(it)
            }
            val byDay = compareBy<PlannerTask>({ it.date }, { it.sequence }, { it.id })
            // Fica um cartão por dia e, somando os dias, até o limite (minutos de teoria ou número de questões).
            fun trim(list: List<PlannerTask>, limit: Int?, amount: (PlannerTask) -> Int) {
                if (list.size < 2) return
                var sum = 0
                val days = hashSetOf<LocalDate>()
                list.sortedWith(byDay).forEach { task ->
                    if (!days.add(task.date) || (limit != null && sum >= limit)) surplus += task.id else sum += amount(task)
                }
            }
            untouched.filter { it.topicId != null && it.type == PlanTaskType.THEORY }.groupBy { it.topicId!! }
                .forEach { (topic, list) -> trim(list, snapshot.theoryNeedByTopic[topic]) { it.plannedMinutes } }
            untouched.filter { it.topicId != null && it.type == PlanTaskType.QUESTIONS }.groupBy { it.topicId!! }
                .forEach { (topic, list) -> trim(list, snapshot.questionCapByTopic[topic]) { it.plannedQuestions } }
            // Revisão de matéria (questões sem tópico): uma por matéria.
            untouched.filter { it.topicId == null && it.subjectId != null && it.type == PlanTaskType.QUESTIONS }.groupBy { it.subjectId!! }
                .forEach { (_, list) -> trim(list, StudyPlanBlueprint.SUBJECT_MIX_QUESTIONS) { it.plannedQuestions } }
            untouched.filter { it.type == PlanTaskType.SIMULATION }.sortedWith(byDay).drop(1).forEach { surplus += it.id }
        }
        /** Sai do plano sem voltar: a parte que sobrou de teoria já lida e a cópia a mais. */
        // Matéria pausada ou que saiu do plano (concurso removido de um plano com vários): o que estava marcado dela
        // e ainda não começou sai, também na semana estável. Antes só deixava de entrar coisa nova e a semana
        // seguia com as tarefas da matéria tirada.
        val subjectsById = snapshot.subjects.associateBy { it.subjectId }
        val outOfPlan: (PlannerTask) -> Boolean = { task ->
            task.status == PlanTaskStatus.PLANEJADA && !task.locked && task.origin == PlanOrigin.ENGINE && task.subjectId != null && subjectsById.isNotEmpty() &&
                executionsByTask[task.id].isNullOrEmpty() && subjectsById[task.subjectId]?.paused != false
        }
        // Reorganização automática não tira nada de hoje (nem a cópia a mais, que sai amanhã).
        val dropped: (PlannerTask) -> Boolean = { task -> !(frozenToday && task.date == snapshot.today) && (task.id in surplus || leftoverTheory(task) || outOfPlan(task)) }
        // Dia da semana estável acima das horas (dois aparelhos, "empurrar tudo", plano antigo): as últimas tarefas
        // do dia, questões antes da teoria, deixam de ficar presas e vão para um dia com espaço.
        val overflowing = hashSetOf<String>()
        if (stableUntil != null) {
            snapshot.tasks.filter { it.date >= snapshot.today && it.date < stableUntil && it.date in rawCapacity.keys && !dropped(it) && (it.status == PlanTaskStatus.CONCLUIDA || it.status in liveStatuses) }
                .groupBy { it.date }.forEach { (date, list) ->
                    var excess = list.sumOf { it.plannedMinutes } - rawCapacity.getValue(date)
                    if (excess <= 0 || (date == snapshot.today && (dayKeeping || keepToday))) return@forEach
                    list.filter { it.status == PlanTaskStatus.PLANEJADA && !it.locked }
                        .sortedWith(compareBy<PlannerTask> { it.type == PlanTaskType.THEORY }.thenByDescending { it.sequence })
                        .forEach { task -> if (excess > 0) { overflowing += task.id; excess -= task.plannedMinutes } }
                }
        }
        val keeps: (PlannerTask) -> Boolean = { task ->
            task.status == PlanTaskStatus.CONCLUIDA ||
                (dayKeeping && task.date <= snapshot.today && task.status == PlanTaskStatus.PLANEJADA) ||
                // Inclui o próprio dia de hoje: o que a pessoa viu ontem para hoje é o que ela encontra ao
                // abrir. Antes a reorganização da manhã remontava o dia (as revisões atrasadas reservavam o
                // tempo e as questões encolhiam), e um dia de 2 h virava 1h13 (caso real de 12/11).
                // Dia marcado como folga solta o que estava nele.
                (stableUntil != null && task.status == PlanTaskStatus.PLANEJADA && task.date >= snapshot.today && task.date < stableUntil &&
                    snapshot.dayOverrides[task.date]?.unavailable != true && task.topicId !in theoryLate && task.id !in overflowing) ||
                task.locked ||
                // Dia travado que depois virou folga não segura mais nada: folga esvazia o dia.
                (snapshot.dayOverrides[task.date]?.locked == true && snapshot.dayOverrides[task.date]?.unavailable != true) ||
                task.subjectId == null ||
                (keepToday && task.date == snapshot.today)
        }
        // Tópico com a teoria sem dia certo nesta reorganização (vai ser remarcada, ou volta como demanda nova
        // porque foi pulada): as questões dele que ainda não começaram não ficam presas na semana estável nem
        // no dia de hoje do Grátis, são encaixadas de novo, depois da teoria. Antes elas ficavam no dia antigo
        // e a teoria ia parar semanas depois: questão antes da teoria.
        // A teoria que volta como demanda nova (pulada hoje, por exemplo) leva junto até as questões de hoje.
        val theoryBack = snapshot.demands.filter { it.type == PlanTaskType.THEORY && it.topicId != null }.mapTo(hashSetOf<Long?>()) { it.topicId }
        val theoryFloating = snapshot.tasks.filter {
            it.type == PlanTaskType.THEORY && it.topicId != null && it.status in liveStatuses && !dropped(it) && !keeps(it)
        }.mapTo(hashSetOf()) { it.topicId } + theoryBack
        // Teoria fixada pela pessoa num dia mais à frente (arrastou a leitura para depois): as questões do tópico
        // que ficaram antes dela voltam a ser encaixadas, e só depois da teoria. Antes ficavam no dia antigo.
        val pinnedTheoryDay: Map<Long, LocalDate> = snapshot.tasks.filter {
            it.type == PlanTaskType.THEORY && it.topicId != null && it.locked && it.status in liveStatuses
        }.groupBy { it.topicId!! }.mapValues { (_, l) -> l.maxOf { it.date } }
        val waitsForTheory: (PlannerTask) -> Boolean = { task ->
            !(frozenToday && task.date == snapshot.today) && task.type != PlanTaskType.THEORY && task.status == PlanTaskStatus.PLANEJADA && !task.locked && snapshot.dayOverrides[task.date]?.locked != true && (
                (task.topicId in theoryFloating && (task.topicId in theoryBack || !(dayKeeping && task.date <= snapshot.today))) ||
                    (task.topicId != null && pinnedTheoryDay[task.topicId]?.let { task.date < it } == true)
                )
        }
        val fixedTasks = snapshot.tasks.filter { task -> !dropped(task) && keeps(task) && !waitsForTheory(task) }
        // Por id: a previsão roda o motor semana a semana com centenas de tarefas, e procurar cada uma
        // na lista (comparando campo a campo) travava o Início do site por segundos.
        val fixedIds = fixedTasks.mapTo(hashSetOf()) { it.id }
        // Tarefa concluída continua ocupando o tempo do dia dela. Sem isso, cada conclusão "liberava"
        // o dia de novo e o replanejamento puxava as matérias de amanhã: o dia nunca terminava.
        val lockedUsage = fixedTasks
            .filter { it.date in rawCapacity.keys && (it.status == PlanTaskStatus.CONCLUIDA || it.status !in terminalInactiveStatuses) }
            .groupBy { it.date }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }
        // O que já foi estudado numa tarefa em andamento também gastou o dia dela; só o que falta
        // volta para o replanejamento.
        val partialUsage = if (reason == ReplanReason.INITIAL) emptyMap() else snapshot.tasks
            .filter { it.id !in fixedIds && it.status == PlanTaskStatus.EM_ANDAMENTO && it.date in rawCapacity.keys }
            .groupBy { it.date }
            .mapValues { (_, tasks) -> tasks.sumOf { task -> minOf(task.plannedMinutes, executionsByTask[task.id].orEmpty().sumOf { it.minutes }) } }
        // Revisão espaçada é extra (aba Revisões, quando a pessoa quiser): não toma tempo da teoria e das
        // questões do plano. Antes até 35% do dia ficava separado para elas e as atrasadas encolhiam o dia.
        val reserve = emptyMap<LocalDate, Int>()
        val allocatableByDate = rawCapacity.mapValues { (date, minutes) ->
            if (snapshot.dayOverrides[date]?.locked == true || (keepToday && date == snapshot.today)) 0
            else (minutes - lockedUsage.getOrElse(date) { 0 } - partialUsage.getOrElse(date) { 0 } - reserve.getOrElse(date) { 0 }).coerceAtLeast(0)
        }
        val freeByDate = allocatableByDate.toMutableMap()
        val replanCandidates = if (reason == ReplanReason.INITIAL) emptyList() else snapshot.tasks.filter { task ->
            task.id !in fixedIds && task.status in setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)
        }
        // Concluir, pular ou fazer parte de uma tarefa não traz matéria nova para hoje: o dia fica com
        // o que já estava nele. Sem isso, cada conclusão abria espaço e o plano puxava as de amanhã,
        // e o estudo do dia não acabava nunca.
        if (reason in dayKeepingReasons && snapshot.today in freeByDate) {
            val pendingToday = replanCandidates.filter { it.date == snapshot.today && !dropped(it) && !waitsForTheory(it) }.sumOf { task ->
                (task.plannedMinutes - executionsByTask[task.id].orEmpty().sumOf { it.minutes }).coerceAtLeast(0)
            }
            freeByDate[snapshot.today] = minOf(freeByDate.getValue(snapshot.today), pendingToday)
        }
        // Revisão espaçada não é mais tarefa do plano (fica na aba Revisões, com o tempo reservado no
        // dia): as que sobraram de planos antigos saem na primeira reorganização, em vez de voltar.
        // Tarefa ainda não começada ganha o tamanho do ritmo real da pessoa (teoria e minutos por questão):
        // sem isso, as 4 semanas já marcadas seguiam no tamanho antigo e o dia de quem é mais lento estourava.
        // Conta todas as partes vivas da teoria do tópico, também as que ficam onde estão (semana estável). Antes
        // contava só as remarcadas: a teoria de 75 min dividida em 50 + 25, com os 50 dentro da semana estável,
        // tinha a parte de 25 como "única" e ela ganhava o tamanho da teoria inteira (75). No dia seguinte a conta
        // se repetia com o novo pedaço, e o tópico chegava a 180 min em 4 partes (caso real de 18/11).
        val liveTheoryCount = snapshot.tasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.status in liveStatuses && !dropped(it) }
            .groupingBy { it.topicId!! }.eachCount()
        // Posição de cada tarefa remarcada dentro do dia dela (a sequência crua cresce a cada reorganização).
        val rankInDay = replanCandidates.groupBy { it.date }.flatMap { (_, list) -> list.sortedBy { it.sequence }.mapIndexed { index, task -> task.id to index } }.toMap()
        // Teoria com alguma parte já feita (concluída ou com tempo registrado) não é redimensionada: a parte
        // que falta viraria a teoria inteira de novo.
        val theoryStarted = snapshot.tasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && (it.status == PlanTaskStatus.CONCLUIDA || executionsByTask[it.id].orEmpty().isNotEmpty()) }
            .mapTo(hashSetOf()) { it.topicId!! }
        val derivedParts = replanCandidates.filter { it.type != PlanTaskType.REVIEW && !dropped(it) }.mapNotNull { task ->
            val executedMinutes = executionsByTask[task.id].orEmpty().sumOf { it.minutes }
            val executedQuestions = executionsByTask[task.id].orEmpty().sumOf { it.questions }
            val remainingMinutes = (task.plannedMinutes - executedMinutes).coerceAtLeast(0)
            // Feita quase toda (falta menos que um bloco mínimo): não volta como caco de poucos minutos.
            // A teoria é exceção: o resto dela continua marcado, mesmo curto. Sem tarefa viva nem concluída o
            // tópico voltava a ser "teoria por fazer" e a leitura inteira era marcada de novo semanas depois.
            val leftover = executedMinutes > 0 && remainingMinutes < snapshot.policy.minimumBlockMinutes && !(task.type == PlanTaskType.THEORY && task.topicId != null)
            // Questões que sobram, até o que o tópico tem salvo (a tarefa antiga podia pedir 20 de 10).
            val leftQuestions = (task.plannedQuestions - executedQuestions).coerceAtLeast(0)
            val cap = task.topicId?.let { snapshot.questionCapByTopic[it] }
            val questions = if (cap != null && task.type in cappedTypes) minOf(leftQuestions, (cap - executedQuestions).coerceAtLeast(0)) else leftQuestions
            val minutes = if (questions < leftQuestions && leftQuestions > 0) {
                (remainingMinutes * questions / leftQuestions).coerceAtLeast(snapshot.policy.minimumBlockMinutes)
            } else remainingMinutes
            val untouched = executedMinutes == 0 && task.status == PlanTaskStatus.PLANEJADA
            val sized = when {
                !untouched -> minutes
                task.type == PlanTaskType.THEORY && task.topicId != null && liveTheoryCount[task.topicId] == 1 && task.topicId !in theoryStarted ->
                    snapshot.theoryMinutesByTopic[task.topicId] ?: minutes
                task.type == PlanTaskType.QUESTIONS && questions > 0 && snapshot.minutesPerQuestion != null ->
                    questions * snapshot.minutesPerQuestion
                else -> minutes
            }
            remainingMinutes.takeIf { it > 0 && !leftover && !(questions == 0 && leftQuestions > 0) }?.let {
                TaskDemand(
                    id = "replan:${task.id}",
                    subjectId = task.subjectId ?: return@mapNotNull null,
                    topicId = task.topicId,
                    type = task.type,
                    minutes = sized,
                    questions = questions,
                    priority = task.priority,
                    deadline = task.deadline,
                    subjectPosition = task.subjectPosition,
                    topicPosition = task.topicPosition,
                    replannedFromTaskId = task.id,
                    // O que ficou para trás volta na frente (colocar em dia vem antes de avançar), e o que já
                    // estava marcado mantém a ordem do calendário em que estava. Antes todas iam com a mesma
                    // ordem e o desempate era a pontuação: a teoria de matéria de pouco peso perdia o lugar a
                    // cada replanejamento e andava para a frente (de dezembro a março) sem nunca chegar.
                    order = (daysBetween(snapshot.today, task.date).toInt().coerceIn(-1, 999) - 1_001) * 1_000 + rankInDay.getValue(task.id).coerceIn(0, 999),
                    // Tarefa de um dia que ainda não chegou nunca é adiantada pelo replanejamento: fica no
                    // dia dela ou depois. Sem isso, concluir hoje puxava as tarefas de amanhã para hoje.
                    anchorDate = task.date.takeIf { it > snapshot.today },
                )
            }
        }
        val keptTheoryMinutes = fixedTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.status in liveStatuses }
            .groupBy { it.topicId!! }.mapValues { (_, list) -> list.sumOf { it.plannedMinutes } }
        // As partes ainda não começadas da teoria de um tópico voltam a ser uma leitura só ao remarcar: ela vai
        // inteira para um dia em que caiba. Antes cada parte era remarcada sozinha, e as duas metades da mesma
        // teoria iam parar a semanas uma da outra.
        val derivedDemands = derivedParts.groupBy { if (it.type == PlanTaskType.THEORY && it.topicId != null && it.topicId !in theoryStarted) "theory:${it.topicId}" else it.id }
            .values.mapNotNull { parts ->
                val first = parts.minBy { it.order }
                if (first.type != PlanTaskType.THEORY || first.topicId == null || first.topicId in theoryStarted) return@mapNotNull first
                // Nunca mais do que a leitura do tópico pede, contando as partes que ficam onde estão (sobra de
                // dois aparelhos ou de plano antigo inflado).
                val sum = parts.sumOf { it.minutes }
                val room = snapshot.theoryNeedByTopic[first.topicId]?.let { it - (keptTheoryMinutes[first.topicId] ?: 0) }
                val minutes = when {
                    room == null -> sum
                    room <= 0 -> 0
                    else -> minOf(sum, maxOf(room, snapshot.policy.minimumBlockMinutes))
                }
                if (minutes == 0) null
                else first.copy(minutes = minutes, anchorDate = parts.map { it.anchorDate }.takeIf { null !in it }?.minOfOrNull { it!! })
            }
        // Teoria que volta como demanda nova depois de lida em parte (o resto foi pulado): desconta o que a pessoa
        // já leu do tópico, em vez de marcar a leitura inteira de novo.
        val readByTopic = snapshot.tasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.topicId !in theoryDoneOn }
            .groupBy { it.topicId!! }.mapValues { (_, list) -> list.sumOf { task -> executionsByTask[task.id].orEmpty().sumOf { it.minutes } } }
        val newDemands = snapshot.demands.map { demand ->
            val read = if (demand.type == PlanTaskType.THEORY) demand.topicId?.let { readByTopic[it] } ?: 0 else 0
            if (read == 0) demand else demand.copy(minutes = (demand.minutes - read).coerceAtLeast(minOf(demand.minutes, snapshot.policy.minimumBlockMinutes)))
        }
        // Refazer do zero: o que não foi feito sai (vira reprogramado) e nada entra agora; o DAY_MISSED
        // seguinte monta o plano pelo método atual, sem carregar a estrutura antiga.
        val allDemands = if (reason == ReplanReason.REBUILD) emptyList() else newDemands + derivedDemands
        val eligible = allDemands
            .filterNot { demand -> snapshot.subjects.firstOrNull { it.subjectId == demand.subjectId }?.paused == true }
            .filter { it.dependsOnTaskIds.all(snapshot.completedDependencyIds::contains) }
            .sortedWith(demandComparator(snapshot))
        val blocked = allDemands.filterNot { it in eligible }
        val remaining = eligible.associate { it.id to it.minutes }.toMutableMap()
        val remainingQuestions = eligible.associate { it.id to it.questions }.toMutableMap()
        val generated = mutableListOf<PlannerTask>()
        // Blocos da mesma demanda no mesmo dia viram uma tarefa só ("Teoria · 50 min"), em vez de
        // dois cartões iguais seguidos.
        // A chave é tópico + tipo (quando há tópico): duas demandas do mesmo assunto no mesmo dia,
        // como a sobra remarcada e a nova, também viram um cartão só.
        val generatedIndex = mutableMapOf<Pair<String, LocalDate>, Int>()
        fun mergeKey(demand: TaskDemand) = demand.topicId?.let { "topic:$it:${demand.type}" } ?: if (demand.type == PlanTaskType.QUESTIONS) "subject:${demand.subjectId}" else demand.id
        val scheduledBySubject = mutableMapOf<Long, Int>()
        // As tarefas novas entram no dia depois das que já estavam nele: a tela ordena o dia pela sequência, e
        // começando do zero as questões novas de um tópico apareciam acima da teoria dele que já estava marcada.
        var sequence = ((fixedTasks.filter { it.status in liveStatuses && it.date >= snapshot.today } + replanCandidates).maxOfOrNull { it.sequence } ?: -1) + 1
        val candidateById = replanCandidates.associateBy { it.id }
        // De qual demanda saiu cada tarefa gerada (para devolver os minutos quando a tarefa não fica no plano).
        val demandOfTask = hashMapOf<String, String>()
        fun drop(task: PlannerTask) {
            generated.remove(task)
            demandOfTask[task.id]?.let { id -> remaining[id] = remaining.getValue(id) + task.plannedMinutes; remainingQuestions[id] = remainingQuestions.getValue(id) + task.plannedQuestions }
        }

        val usedBySubjectPerDate = mutableMapOf<Pair<LocalDate, Long>, Int>()
        val dailyShare = snapshot.policy.dailySubjectSharePercent
        // Tópicos com teoria em cada dia (os que já ficam e os novos), para o teto de matérias por dia.
        val topicCap = snapshot.policy.maxTheoryTopicsPerDay
        val theoryTopicsByDate = mutableMapOf<LocalDate, MutableSet<Long>>()
        fixedTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && (it.status == PlanTaskStatus.CONCLUIDA || it.status !in terminalInactiveStatuses) }
            .forEach { theoryTopicsByDate.getOrPut(it.date) { mutableSetOf() } += it.topicId!! }
        // Cartões que ficam onde estão (dia, tópico, tipo): nada novo igual entra no mesmo dia, para não haver
        // dois cartões da mesma teoria ou das mesmas questões no dia.
        val keptCards = fixedTasks.filter { it.topicId != null && it.status in liveStatuses }.mapTo(hashSetOf()) { Triple(it.date, it.topicId!!, it.type) }
        fun topicFits(demand: TaskDemand, date: LocalDate): Boolean {
            if (demand.topicId != null && Triple(date, demand.topicId, demand.type) in keptCards) return false
            if (topicCap == null || demand.type != PlanTaskType.THEORY || demand.topicId == null) return true
            val day = theoryTopicsByDate[date] ?: return true
            return demand.topicId in day || day.size < topicCap
        }
        // Teto por dia e por matéria: evita quatro horas seguidas da mesma coisa. Quando nenhum dia
        // aceita a matéria, a segunda passada roda sem o teto para não desperdiçar capacidade.
        fun dateFor(demand: TaskDemand, respectShare: Boolean, skip: Set<LocalDate> = emptySet(), atLeast: Int = 1): LocalDate? {
            // Questões de um tópico com a teoria fixada mais à frente só entram do dia da teoria em diante.
            val afterTheory = if (demand.type != PlanTaskType.THEORY) demand.topicId?.let { pinnedTheoryDay[it] } else null
            val from = listOfNotNull(demand.anchorDate, afterTheory).maxOrNull()
            val window = if (from == null) dates else dates.filterNot { it.isBefore(from) }.ifEmpty { if (afterTheory != null) emptyList() else dates }
            return window.firstOrNull { date ->
                if (date in skip || freeByDate.getValue(date) < atLeast) return@firstOrNull false
                if (!topicFits(demand, date)) return@firstOrNull false
                if (!respectShare || dailyShare >= 100) return@firstOrNull true
                val capacity = rawCapacity.getValue(date)
                val cap = (capacity * dailyShare / 100).coerceAtLeast(snapshot.policy.preferredBlockMinutes)
                usedBySubjectPerDate.getOrElse(date to demand.subjectId) { 0 } < cap
            }
        }

        fun allocate(demand: TaskDemand, maximum: Int, respectShare: Boolean = true, wholeOn: LocalDate? = null): Int {
            var left = minOf(maximum, remaining.getValue(demand.id))
            var allocated = 0
            // Dias em que esta tarefa não entra porque só caberia um caco dela.
            val skipped = hashSetOf<LocalDate>()
            // A teoria de um tópico é uma leitura só: vai inteira para o primeiro dia em que cabe toda. Antes a
            // teoria de 50 min era cortada em 25 + 25 para completar um dia com 30 min livres; a pessoa lia tudo
            // no primeiro dia e a "parte 2" aparecia no dia seguinte. Só é dividida quando nenhum dia a comporta.
            val wholeTheory = wholeOn == null && demand.type == PlanTaskType.THEORY && demand.topicId != null
            while (left > 0) {
                val wholeDate = if (wholeTheory) dateFor(demand, respectShare, skipped, atLeast = left) else null
                val date = wholeOn ?: wholeDate ?: dateFor(demand, respectShare, skipped) ?: break
                val free = freeByDate.getValue(date)
                val minimum = snapshot.policy.minimumBlockMinutes
                var chunk = if (wholeOn != null || wholeDate != null) left else minOf(left, free, snapshot.policy.preferredBlockMinutes.coerceAtLeast(minimum))
                // Nunca deixa um caco para outro dia ("Java · 2 min"): a sobra menor que um bloco mínimo
                // vai junto, mesmo passando um pouco do dia (até um bloco mínimo); se nem assim cabe, o
                // corte vem antes, para o pedaço seguinte ter ao menos um bloco mínimo.
                val rest = left - chunk
                if (rest in 1 until minimum) {
                    when {
                        // Junta a sobra só se couber no dia: sem a reserva de revisão os dias ficam cheios
                        // e passar "um pouquinho" virava +20 min em quase todo dia.
                        left <= free && left <= snapshot.policy.preferredBlockMinutes + minimum -> chunk = left
                        chunk - (minimum - rest) >= minimum -> chunk -= (minimum - rest)
                        // Não há corte que deixe os dois pedaços com um bloco mínimo: a tarefa não entra neste
                        // dia (antes entrava assim mesmo e sobrava "Teoria · 6 min" ou "Questões · 1 min").
                        else -> { skipped += date; continue }
                    }
                }
                // Pedaço menor que um bloco só entra se for a tarefa inteira (pequena de nascença): o resto de uma
                // tarefa maior não vira "Questões · 4 min" no último dia antes da prova.
                if (chunk < minimum && (chunk < left || chunk < demand.minutes)) {
                    skipped += date
                    continue
                }
                val questionLeft = remainingQuestions.getValue(demand.id)
                val minuteLeftBefore = remaining.getValue(demand.id)
                val chunkQuestions = if (chunk == minuteLeftBefore) questionLeft
                else if (minuteLeftBefore == 0) 0
                else questionLeft * chunk / minuteLeftBefore
                val sameDay = generatedIndex[mergeKey(demand) to date]
                if (sameDay != null) {
                    val previous = generated[sameDay]
                    generated[sameDay] = previous.copy(
                        plannedMinutes = previous.plannedMinutes + chunk,
                        plannedQuestions = previous.plannedQuestions + chunkQuestions,
                    )
                } else {
                    // O resto de uma tarefa feita em parte que continua no próprio dia fica na mesma posição do
                    // dia: antes o resto da teoria ia para o fim, depois das questões do mesmo tópico.
                    val source = demand.replannedFromTaskId?.let(candidateById::get)
                    val position = if (source != null && source.date == date && !executionsByTask[source.id].isNullOrEmpty()) source.sequence else sequence++
                    generatedIndex[mergeKey(demand) to date] = generated.size
                    val taskId = stableId(snapshot.planId, snapshot.revision.toString(), demand.id, date.toString(), position.toString())
                    demandOfTask[taskId] = demand.id
                    generated += PlannerTask(
                        id = taskId,
                        planId = snapshot.planId,
                        subjectId = demand.subjectId,
                        topicId = demand.topicId,
                        date = date,
                        type = demand.type,
                        plannedMinutes = chunk,
                        plannedQuestions = chunkQuestions,
                        priority = demand.priority,
                        status = PlanTaskStatus.PLANEJADA,
                        locked = false,
                        deadline = demand.deadline,
                        subjectPosition = demand.subjectPosition,
                        topicPosition = demand.topicPosition,
                        replannedFromTaskId = demand.replannedFromTaskId,
                        sequence = position,
                    )
                }
                freeByDate[date] = (free - chunk).coerceAtLeast(0)
                if (demand.type == PlanTaskType.THEORY) demand.topicId?.let { theoryTopicsByDate.getOrPut(date) { mutableSetOf() } += it }
                usedBySubjectPerDate[date to demand.subjectId] = usedBySubjectPerDate.getOrElse(date to demand.subjectId) { 0 } + chunk
                remaining[demand.id] = minuteLeftBefore - chunk
                remainingQuestions[demand.id] = questionLeft - chunkQuestions
                left -= chunk
                allocated += chunk
                scheduledBySubject[demand.subjectId] = scheduledBySubject.getOrElse(demand.subjectId) { 0 } + chunk
            }
            return allocated
        }

        // Simulado do plano: entra primeiro, inteiro e num dia só (no dia marcado ou no primeiro dia
        // seguinte com espaço para ele todo). Antes ele disputava o espaço com o resto, como se fosse da
        // primeira matéria (cabia no teto dela), era partido em blocos e escorregava semanas: na reta
        // final saía um a cada mês em vez de um por semana.
        eligible.filter { it.type == PlanTaskType.SIMULATION }.forEach { demand ->
            val from = demand.anchorDate ?: snapshot.today
            val day = dates.firstOrNull { !it.isBefore(from) && freeByDate.getValue(it) >= demand.minutes }
                ?: dates.filter { !it.isBefore(from) && daysBetween(from, it) < 7 && freeByDate.getValue(it) >= snapshot.policy.minimumBlockMinutes }
                    .maxByOrNull { freeByDate.getValue(it) }
            if (day != null) allocate(demand, demand.minutes, respectShare = false, wholeOn = day)
        }

        snapshot.subjects
            .filterNot { it.paused }
            .sortedWith(compareBy<SubjectDemand>({ it.position }, { it.subjectId }))
            .forEach { subject ->
                var maintenanceLeft = subject.minimumMaintenanceMinutes
                eligible.filter { it.subjectId == subject.subjectId }.forEach { demand ->
                    if (maintenanceLeft > 0) maintenanceLeft -= allocate(demand, maintenanceLeft)
                }
            }

        val totalAllocatable = allocatableByDate.values.sum()
        val maintenanceAllocated = generated.sumOf { it.plannedMinutes }
        val flexiblePool = (totalAllocatable - maintenanceAllocated).coerceAtLeast(0)
        val eligibleSubjects = eligible.filter { remaining.getValue(it.id) > 0 }.map { it.subjectId }.distinct()
        val capEnabled = eligibleSubjects.size > 1
        val flexibleCap = flexiblePool * snapshot.policy.maxFlexibleSharePercent / 100
        val maintenanceBySubject = generated.groupBy { it.subjectId }
            .mapValues { (_, tasks) -> tasks.sumOf { it.plannedMinutes } }
        val flexibleBySubject = mutableMapOf<Long, Int>()

        fun flexiblePass(respectShare: Boolean) {
            var madeProgress: Boolean
            do {
                madeProgress = false
                eligible.forEach { demand ->
                    val demandRemaining = remaining.getValue(demand.id)
                    if (demandRemaining == 0) return@forEach
                    val capLeft = if (capEnabled) {
                        (flexibleCap - flexibleBySubject.getOrElse(demand.subjectId) { 0 }).coerceAtLeast(0)
                    } else demandRemaining
                    if (capLeft == 0) return@forEach
                    val before = scheduledBySubject.getOrElse(demand.subjectId) { 0 }
                    // A demanda inteira de uma vez (em blocos, dentro do teto do dia): antes ia um
                    // bloco por volta do rodízio, e as questões de um tópico entravam entre as duas
                    // metades da teoria dele.
                    val amount = allocate(demand, minOf(demandRemaining, capLeft), respectShare)
                    if (amount > 0) {
                        val after = scheduledBySubject.getOrElse(demand.subjectId) { 0 }
                        val maintenance = maintenanceBySubject.getOrElse(demand.subjectId) { 0 }
                        flexibleBySubject[demand.subjectId] = (after - maintenance).coerceAtLeast(0)
                        madeProgress = madeProgress || after > before
                    }
                }
            } while (madeProgress && freeByDate.values.any { it > 0 } && remaining.values.any { it > 0 })
        }
        flexiblePass(respectShare = true)
        if (dailyShare < 100) flexiblePass(respectShare = false)

        // Dia com tempo sobrando e nada mais para fazer: o próximo tópico novo (teoria e as questões dele)
        // vem para cá. O ritmo do edital é um piso, não um teto: quem tem tempo livre adianta em vez de
        // ficar parado. Hoje, depois de concluir alguma coisa, não ganha tópico novo (freeByDate já veio
        // limitado ao que estava no dia).
        run {
            val minimum = snapshot.policy.minimumBlockMinutes
            dates.forEach { date ->
                var guard = 0
                while (guard++ < 8) {
                    val free = freeByDate.getValue(date)
                    if (free < minimum) break
                    if (topicCap != null && (theoryTopicsByDate[date]?.size ?: 0) >= topicCap) break
                    val theory = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.date > date }
                        .minWithOrNull(compareBy({ it.date }, { it.sequence })) ?: break
                    val bundle = generated.filter { it.topicId == theory.topicId && it.date == theory.date && it.type in setOf(PlanTaskType.THEORY, PlanTaskType.QUESTIONS) }
                    val need = bundle.sumOf { it.plannedMinutes }
                    if (need > free || bundle.any { Triple(date, it.topicId!!, it.type) in keptCards }) break
                    val from = theory.date
                    bundle.forEach { task ->
                        // Já há um cartão do mesmo tópico e tipo no dia de destino (outra parte da teoria): junta
                        // nele, em vez de dois cartões iguais no mesmo dia.
                        val twin = generated.indexOfFirst { it.date == date && it.topicId == task.topicId && it.type == task.type }
                        if (twin >= 0) {
                            val previous = generated[twin]
                            generated[twin] = previous.copy(plannedMinutes = previous.plannedMinutes + task.plannedMinutes, plannedQuestions = previous.plannedQuestions + task.plannedQuestions)
                            generated.remove(task)
                        } else generated[generated.indexOf(task)] = task.copy(date = date, sequence = sequence++)
                    }
                    freeByDate[date] = (free - need).coerceAtLeast(0)
                    freeByDate[from] = freeByDate.getValue(from) + need
                    theoryTopicsByDate.getOrPut(date) { mutableSetOf() } += theory.topicId!!
                    theoryTopicsByDate[from]?.remove(theory.topicId)
                }
            }
        }

        // Teoria que não coube no período (o conteúdo está apertado) e ainda há um buraco num dia: só aí a próxima
        // teoria vem para o buraco, inteira se couber ou só o começo dela (o resto fica no dia em que estava).
        // Com folga no plano a teoria nunca é cortada; com o prazo apertado, o tempo do dia não fica parado.
        // "Apertado" é a teoria ter ficado sem lugar mesmo com mais de uma semana para entrar (a que só não coube
        // na ponta do período não conta: ela entra quando o período andar).
        val edge = dates.last().minusDays(STABLE_DAYS)
        if (eligible.any { it.type == PlanTaskType.THEORY && it.topicId != null && remaining.getValue(it.id) > 0 && (it.anchorDate ?: snapshot.today) <= edge }) {
            val minimum = snapshot.policy.minimumBlockMinutes
            dates.forEach { date ->
                var guard = 0
                while (guard++ < 6) {
                    val free = freeByDate.getValue(date)
                    if (free < minimum) break
                    val theory = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.date > date }
                        .minWithOrNull(compareBy({ it.date }, { it.sequence })) ?: break
                    val topic = theory.topicId!!
                    val alreadyHere = generated.any { it.date == date && it.topicId == topic && it.type == PlanTaskType.THEORY }
                    if (Triple(date, topic, PlanTaskType.THEORY) in keptCards) break
                    if (!alreadyHere && topicCap != null && (theoryTopicsByDate[date]?.size ?: 0) >= topicCap) break
                    val whole = theory.plannedMinutes <= free
                    val amount = if (whole) theory.plannedMinutes else minOf(free, theory.plannedMinutes - minimum)
                    if (!whole && amount < minimum) break
                    val origin = theory.date
                    if (whole) generated.remove(theory) else generated[generated.indexOf(theory)] = theory.copy(plannedMinutes = theory.plannedMinutes - amount)
                    val here = generated.indexOfFirst { it.date == date && it.topicId == topic && it.type == PlanTaskType.THEORY }
                    when {
                        here >= 0 -> generated[here] = generated[here].copy(plannedMinutes = generated[here].plannedMinutes + amount)
                        whole -> generated += theory.copy(date = date, sequence = sequence++)
                        else -> {
                            val partId = stableId(snapshot.planId, snapshot.revision.toString(), "parte", theory.id, date.toString())
                            demandOfTask[theory.id]?.let { demandOfTask[partId] = it }
                            generated += theory.copy(id = partId, date = date, plannedMinutes = amount, sequence = sequence++)
                        }
                    }
                    freeByDate[date] = free - amount
                    freeByDate[origin] = freeByDate.getValue(origin) + amount
                    theoryTopicsByDate.getOrPut(date) { mutableSetOf() } += topic
                    if (whole && generated.none { it.date == origin && it.topicId == topic && it.type == PlanTaskType.THEORY }) theoryTopicsByDate[origin]?.remove(topic)
                }
            }
            // O espaço que abriu lá na frente recebe o que tinha ficado de fora (o índice dos cartões por dia
            // é refeito antes, porque as tarefas mudaram de dia e de posição).
            generatedIndex.clear()
            generated.forEachIndexed { index, task ->
                val key = task.topicId?.let { "topic:$it:${task.type}" } ?: if (task.type == PlanTaskType.QUESTIONS) "subject:${task.subjectId}" else demandOfTask[task.id]
                if (key != null) generatedIndex[key to task.date] = index
            }
            flexiblePass(respectShare = false)
        }

        // A teoria de um tópico vem antes das questões e revisões dele. Ao remarcar o atrasado, a
        // teoria podia cair num dia depois das questões do mesmo tópico; elas vão para o dia da
        // (última parte da) teoria, logo depois dela (juntando com o cartão do mesmo tipo, se houver).
        run {
            val lastTheory = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null }
                .groupBy { it.topicId!! }.mapValues { (_, l) -> l.maxWith(compareBy({ it.date }, { it.sequence })) }
            // A teoria que fica onde está (semana estável) também conta: a questão nova do tópico não entra antes dela.
            val keptTheoryDay = fixedTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.status in liveStatuses }
                .groupBy { it.topicId!! }.mapValues { (_, l) -> l.maxOf { it.date } }
            val fixed = mutableListOf<PlannerTask>()
            generated.forEach { task ->
                val theory = task.topicId?.let { lastTheory[it] }
                val kept = task.topicId?.let { keptTheoryDay[it] }
                val moved = when {
                    task.type == PlanTaskType.THEORY -> task
                    kept != null && task.date < kept && (theory == null || theory.date <= kept) -> task.copy(date = kept, sequence = sequence++)
                    theory != null && task.date < theory.date -> task.copy(date = theory.date, sequence = theory.sequence)
                    else -> task
                }
                val twin = fixed.indexOfFirst { it.topicId != null && it.topicId == moved.topicId && it.type == moved.type && it.date == moved.date }
                if (twin >= 0) {
                    val prev = fixed[twin]
                    fixed[twin] = prev.copy(plannedMinutes = prev.plannedMinutes + moved.plannedMinutes, plannedQuestions = prev.plannedQuestions + moved.plannedQuestions)
                } else fixed += moved
            }
            generated.clear(); generated += fixed
        }

        // Teoria que não coube inteira no período (fim do horizonte, prazo apertado): as questões do tópico
        // esperam com ela. Antes as questões entravam sozinhas e, quando a teoria ganhava um dia, ele era depois.
        run {
            val unplaced = (eligible.filter { remaining.getValue(it.id) > 0 } + blocked)
                .filter { it.type == PlanTaskType.THEORY && it.topicId != null }.mapTo(hashSetOf()) { it.topicId }
            if (unplaced.isNotEmpty()) generated.filter { it.type != PlanTaskType.THEORY && it.topicId in unplaced }.forEach(::drop)
        }

        // Nenhum dia passa das horas que a pessoa escolheu (contando as revisões reservadas e o que já
        // está fixo nele). A sobra de bloco e o "questões no dia da teoria" acima podiam passar do dia
        // (2h18 num dia de 2h). O que passa vai para o próximo dia com espaço: primeiro as questões mais
        // novas do dia, sempre depois da teoria do mesmo tópico.
        run {
            fun load(date: LocalDate) = lockedUsage.getOrElse(date) { 0 } + partialUsage.getOrElse(date) { 0 } + reserve.getOrElse(date) { 0 } +
                generated.filter { it.date == date }.sumOf { it.plannedMinutes }
            val theoryDay = generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null }
                .groupBy { it.topicId!! }.mapValues { (_, l) -> l.maxOf { it.date } }
            dates.forEach { date ->
                val cap = rawCapacity.getValue(date)
                if (cap <= 0) return@forEach
                var guard = 0
                while (load(date) > cap && guard++ < 12) {
                    val victim = generated.filter { it.date == date && it.type != PlanTaskType.THEORY }
                        .maxByOrNull { it.sequence } ?: break
                    val earliest = maxOf(date.plusDays(1), victim.topicId?.let { theoryDay[it] } ?: date.plusDays(1))
                    val target = dates.firstOrNull { d -> d >= earliest && rawCapacity.getValue(d) > 0 && load(d) + victim.plannedMinutes <= rawCapacity.getValue(d) && (victim.topicId == null || Triple(d, victim.topicId, victim.type) !in keptCards) }
                    // Sem dia com espaço até o fim do período: a tarefa fica de fora agora (volta na próxima
                    // reorganização, quando o período anda). Antes ela ficava no dia mesmo assim, acima das horas.
                    val twin = if (target == null || victim.topicId == null) -1 else generated.indexOfFirst { it.date == target && it.topicId == victim.topicId && it.type == victim.type }
                    when {
                        target == null -> drop(victim)
                        // Já há um cartão igual (tópico e tipo) no dia de destino: junta nele.
                        twin >= 0 -> {
                            val previous = generated[twin]
                            generated[twin] = previous.copy(plannedMinutes = previous.plannedMinutes + victim.plannedMinutes, plannedQuestions = previous.plannedQuestions + victim.plannedQuestions)
                            generated.remove(victim)
                        }
                        else -> generated[generated.indexOf(victim)] = victim.copy(date = target, sequence = sequence++)
                    }
                }
            }
        }

        // Ordem do dia: as questões novas de um tópico ficam depois da teoria dele marcada no mesmo dia (a que
        // já estava e a nova). A tela ordena o dia pela sequência.
        run {
            val theorySequence = (fixedTasks.filter { it.type == PlanTaskType.THEORY && it.topicId != null && it.status in liveStatuses } +
                generated.filter { it.type == PlanTaskType.THEORY && it.topicId != null })
                .groupBy { it.date to it.topicId!! }.mapValues { (_, list) -> list.maxOf { it.sequence } }
            generated.indices.forEach { index ->
                val task = generated[index]
                val theory = task.topicId?.let { theorySequence[task.date to it] } ?: return@forEach
                if (task.type != PlanTaskType.THEORY && task.sequence <= theory) generated[index] = task.copy(sequence = sequence++)
            }
        }

        val dependencyUnallocated = blocked.map {
            UnallocatedDemand(it.id, it.minutes, "Dependência ainda não concluída.")
        }
        val capacityUnallocated = eligible.mapNotNull { demand ->
            remaining.getValue(demand.id).takeIf { it > 0 }?.let { minutes ->
                val reasonText = if (capEnabled && freeByDate.values.sum() > 0) {
                    "Limite semanal da matéria atingido."
                } else "Capacidade disponível insuficiente."
                UnallocatedDemand(demand.id, minutes, reasonText)
            }
        }
        val unallocated = dependencyUnallocated + capacityUnallocated
        val required = allDemands.sumOf { it.minutes }
        val scheduled = generated.sumOf { it.plannedMinutes }
        val weeklyCapacity = snapshot.availability.values.sumOf { it.effectiveMinutes }
        val forecast = StudyPlanForecastCalculator.forecast(
            start = snapshot.today,
            remainingMinutes = snapshot.remainingPhaseMinutes,
            weeklyCapacityMinutes = weeklyCapacity,
            recurringReservedMinutes = snapshot.recurringReservedMinutes,
        )
        val explanations = buildList {
            snapshot.notes.forEach { add(PlanningExplanation("METHOD", it)) }
            if (required > scheduled) add(PlanningExplanation("CAPACITY_DEFICIT", "${required - scheduled} minuto(s) de demanda não couberam no horizonte."))
            if (capEnabled && capacityUnallocated.any { it.reason.startsWith("Limite") }) {
                add(PlanningExplanation("SUBJECT_CAP", "O limite flexível por matéria preservou a diversidade semanal."))
            }
        }
        val normalized = buildString {
            append(snapshot.planId).append('|').append(snapshot.revision).append('|').append(reason.name)
            generated.forEach { append('|').append(it.id).append(':').append(it.plannedMinutes) }
            unallocated.forEach { append('|').append(it.demandId).append(':').append(it.remainingMinutes) }
        }
        return PlanningProposal(
            id = stableId(normalized),
            planId = snapshot.planId,
            baseRevision = snapshot.revision,
            preservedTaskIds = fixedTasks.mapTo(linkedSetOf()) { it.id },
            transitions = replanCandidates.map { task ->
                TaskTransition(
                    taskId = task.id,
                    from = task.status,
                    to = if (task.date < snapshot.today && task.status == PlanTaskStatus.PLANEJADA) {
                        PlanTaskStatus.NAO_REALIZADA
                    } else PlanTaskStatus.REPROGRAMADA,
                    reason = reason.name,
                )
            },
            newTasks = generated,
            capacity = CapacityReport(
                requiredMinutes = required,
                availableMinutes = totalAllocatable,
                deficitMinutes = (required - scheduled).coerceAtLeast(0),
                availableByDate = allocatableByDate,
                unallocated = unallocated,
            ),
            explanations = explanations,
            forecast = forecast,
        )
    }

    /**
     * Tempo de cada dia separado para as revisões espaçadas da aba Revisões: uma por tópico que vence no
     * dia (as atrasadas contam hoje), a [REVIEW_MINUTES] cada, até [REVIEW_SHARE] do dia. O resto do
     * dia é do plano; revisão demais num dia só não engole o estudo novo, ela espera na aba.
     */
    fun reviewReserve(snapshot: StudyPlannerSnapshot, capacity: Map<LocalDate, Int>): Map<LocalDate, Int> {
        val byDate = snapshot.reviews
            .groupBy { it.topicId }
            .map { (_, list) -> list.minOf { it.dueDate } }
            .map { if (it.isBefore(snapshot.today)) snapshot.today else it }
            .groupingBy { it }.eachCount()
        return byDate.mapNotNull { (date, count) ->
            val day = capacity[date] ?: return@mapNotNull null
            date to minOf(count * REVIEW_MINUTES, day * REVIEW_SHARE / 100)
        }.toMap()
    }

    private fun capacityFor(snapshot: StudyPlannerSnapshot, date: LocalDate): Int {
        val override = snapshot.dayOverrides[date]
        if (override?.unavailable == true) return 0
        if (override?.minutes != null) return override.minutes
        return snapshot.availability[date.dayOfWeek]?.effectiveMinutes ?: 0
    }

    private fun demandComparator(snapshot: StudyPlannerSnapshot): Comparator<TaskDemand> {
        val performance = snapshot.topicPerformance.associateBy { it.topicId }
        // A ordem escolhida por quem montou a demanda vence a pontuação: o plano sem IA já decidiu
        // o rodízio das matérias e a sequência teoria → questões, e não quer que ela seja refeita.
        return compareBy<TaskDemand> { it.order }.thenByDescending {
            scoring.score(it, snapshot.today, it.topicId?.let(performance::get), snapshot.policy)
        }.thenBy { it.deadline ?: MAX_DATE }
            .thenBy { it.subjectPosition }
            .thenBy { it.topicPosition }
            .thenBy { it.id }
    }

    private fun stableId(vararg values: String): String {
        val bytes = Sha256.digest(values.joinToString("|").encodeToByteArray())
        return bytes.copyOf(16).toHex()
    }

    companion object {
        /** Minutos de uma revisão espaçada (o cartão da aba Revisões estima o mesmo por revisão). */
        const val REVIEW_MINUTES = 4
        /** Teto do dia que as revisões podem reservar, em porcento. */
        const val REVIEW_SHARE = 35
        /** Tarefas cujo tamanho vem de questões do tópico (e por isso respeitam questionCapByTopic). */
        val cappedTypes = setOf(PlanTaskType.QUESTIONS, PlanTaskType.ACTIVE_RECALL)
        val dayKeepingReasons = setOf(ReplanReason.TASK_COMPLETED, ReplanReason.TASK_PARTIAL, ReplanReason.TASK_SKIPPED)
        /** Dias à frente que ficam como estão nas reorganizações do dia a dia (ver "Semana estável"). */
        const val STABLE_DAYS = 7
        /**
         * Até quantos dias da teoria concluída uma teoria ainda marcada do mesmo tópico é parte da mesma leitura
         * (e sai do plano). A teoria de retomada só volta 30 dias ou mais depois.
         */
        const val LEFTOVER_THEORY_DAYS = 30
        val stableReasons = dayKeepingReasons + ReplanReason.DAY_MISSED
        val terminalInactiveStatuses = setOf(
            PlanTaskStatus.CONCLUIDA,
            PlanTaskStatus.REPROGRAMADA,
            PlanTaskStatus.NAO_REALIZADA,
            PlanTaskStatus.PAUSADA,
        )
    }
}
