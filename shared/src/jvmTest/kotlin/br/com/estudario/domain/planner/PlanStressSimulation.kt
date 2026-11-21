package br.com.estudario.domain.planner

import br.com.estudario.time.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.io.File
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Teste de estresse do plano: alunos de comportamentos difíceis (lê a teoria inteira de uma vez, lê só uma
 * parte, some por dias, pula tarefa, adianta, muda as horas, marca folga, usa dois aparelhos sem sincronizar,
 * reorganiza várias vezes no mesmo dia) seguem o plano por semanas ou meses, e a cada reorganização o
 * simulador confere as regras que dão tranquilidade ao aluno:
 *
 * (a) teoria de tópico já concluída não reaparece; (b) questão de um tópico nunca antes da teoria dele;
 * (c) nenhum dia passa das horas da pessoa; (d) a semana já vista não muda à toa; (e) o edital é coberto
 * (ou o que fica de fora é o que menos cai); (f) a estimativa de teoria fica numa faixa plausível e o ritmo
 * pessoal não cresce em ciclo; (g) revisões e simulados não tomam o tempo da teoria; (h) nada duplicado no
 * mesmo dia, tópico e tipo.
 *
 * Os cenários curtos rodam sempre (com assert). Com AUDIT=1 rodam todos os longos e o relatório vai para
 * build/plan-stress.txt (AUDIT_ONLY=A,C escolhe pelo prefixo do nome).
 */
class PlanStressSimulation {
    enum class TheoryMode {
        /** Lê a matéria inteira na primeira parte e conclui o tópico. */
        WHOLE,
        /** Cumpre o tempo de cada parte no cronômetro e conclui a parte. */
        TIMER,
        /** Marca "fiz uma parte" (em andamento) e continua depois. */
        PART,
        /** Relê a matéria inteira a cada parte que aparece (o ciclo do ritmo pessoal). */
        REREAD,
    }

    enum class Overdue { FIT, WAIT, PUSH }

    data class Sub(val name: String, val topics: Int, val priority: PlanPriority, val knowledge: InitialKnowledge = InitialKnowledge.NONE, val difficulty: PersonalDifficulty = PersonalDifficulty.NORMAL)

    data class Scenario(
        val name: String,
        val subjects: List<Sub>,
        val weeklyMinutes: Int,
        val examInDays: Int,
        val profile: StudyProfile = StudyProfile.DO_ZERO,
        val theory: TheoryMode = TheoryMode.WHOLE,
        /** Tempo real da teoria sobre o padrão (1.5 = lento). */
        val theorySpeed: Double = 1.0,
        val questionMinutes: Int? = null,
        /** Dias (contados do início) em que a pessoa nem abre o app. */
        val absences: List<IntRange> = emptyList(),
        /** Chance de abrir o app num dia qualquer. */
        val openChance: Double = 1.0,
        /** Chance, a cada tarefa, de parar o dia ali (o resto fica atrasado). */
        val leaveChance: Double = 0.0,
        val skipChance: Double = 0.0,
        /** Chance de, terminado o dia, adiantar a primeira tarefa de amanhã. */
        val earlyChance: Double = 0.0,
        /** Dia -> novas horas da semana (minutos). */
        val hours: Map<Int, Int> = emptyMap(),
        /** Dias marcados como "hoje não vou conseguir" de manhã. */
        val daysOff: Set<Int> = emptySet(),
        /** Reorganizações a mais no mesmo dia (abrir de novo, encaixar de novo). */
        val extraReplans: Int = 0,
        /** A cada N dias, dois aparelhos estudam sem sincronizar e juntam no fim do dia (0 = nunca). */
        val twoDevicesEvery: Int = 0,
        val overdue: Overdue = Overdue.FIT,
        val free: Boolean = false,
        /** Chance, no fim do dia, de estudar um tópico ainda não lido pela prática livre (fora do plano). */
        val freePracticeChance: Double = 0.0,
        /** Dia -> edição do plano no meio do caminho. */
        val edits: Map<Int, Edit> = emptyMap(),
        /** O edital cabe com folga: todo tópico tem de ser visto antes da prova. */
        val mustCoverAll: Boolean = false,
        /** Cenário da guarda de lançamento: confere também plano estável (mesma foto, mesmo plano). */
        val guard: Boolean = false,
        val seed: Int = 42,
    )

    enum class Edit { ADD_SUBJECT, REMOVE_SUBJECT, PAUSE_SUBJECT, PRIORITY_UP }

    class Metrics {
        var replans = 0
        var theoryAgainPlanned = 0; var theoryAgainSeen = 0
        var questionBeforeTheoryPlanned = 0; var questionBeforeTheoryDone = 0; var questionOrphan = 0; var questionBeforeTheorySameDay = 0
        val overDates = hashMapOf<LocalDate, Int>(); val overByReason = hashMapOf<String, Int>(); var zeroDayTasks = 0
        var weekSlots = 0; var weekChanged = 0; val changedByReason = hashMapOf<String, Int>(); val slotsByReason = hashMapOf<String, Int>()
        var repeatReplans = 0; var repeatChangedWeek = 0; var repeatChangedAll = 0; var repeatAddedWeek = 0
        /** O que se vê logo depois de juntar dois aparelhos ou de "empurrar tudo", antes de o motor reorganizar. */
        var soft: Metrics? = null
        var theoryMax = 0; var theoryMin = Int.MAX_VALUE; var theoryOutOfRange = 0
        var paceMax = 1.0; var paceMin = 1.0; var mpqMax = 0
        var reviewTasks = 0; var simulationMinutes = 0; var theoryMinutes = 0; var questionMinutes = 0
        var duplicates = 0
        var fragments = 0
        val splitTopics = hashSetOf<Long>(); var maxParts = 1
        var theoryShort = 0; var theoryRegrown = 0
        var retakesDone = 0; var retakeChurn = 0
        var covered = 0; var total = 0; var coveredByDeadline = 0; var leftIncidence = 0; var allIncidence = 0
        var overdueMax = 0; var emptyDays = 0; var studyDays = 0
        /** Rodar o motor duas vezes na mesma foto deu planos diferentes. */
        var nondeterministic = 0
        /** Depois de "encaixar" (DAY_MISSED), ainda sobrou atrasada; ou a contagem do simulador e a de PlanRules divergem. */
        var overdueLeft = 0; var overdueMismatch = 0
        /** Tarefa viva de matéria que a pessoa tirou do plano. */
        var removedSubjectTasks = 0
        val examples = linkedMapOf<String, MutableList<String>>()
        fun example(kind: String, text: String) { val l = examples.getOrPut(kind) { mutableListOf() }; if (l.size < 4) l += text }
    }

    private class State(
        var tasks: MutableList<PlannerTask> = mutableListOf(),
        val executions: MutableList<TaskExecution> = mutableListOf(),
        /** Tópico -> dia em que a teoria foi concluída pela primeira vez. */
        val studied: MutableMap<Long, LocalDate> = mutableMapOf(),
        /** Tópico -> último dia em que uma teoria dele foi concluída. */
        val theoryOn: MutableMap<Long, LocalDate> = mutableMapOf(),
        val attempts: MutableList<PlannerSnapshotBuilder.Attempt> = mutableListOf(),
        val reviews: MutableList<PlannerSnapshotBuilder.PendingReview> = mutableListOf(),
        val partial: MutableSet<Long> = mutableSetOf(),
        val readMinutes: MutableMap<Long, Int> = mutableMapOf(),
        val usedQ: MutableMap<Long, Int> = mutableMapOf(),
        var revision: Long = 1,
    ) {
        fun copy() = State(tasks.toMutableList(), executions.toMutableList(), studied.toMutableMap(), theoryOn.toMutableMap(), attempts.toMutableList(), reviews.toMutableList(), partial.toMutableSet(), readMinutes.toMutableMap(), usedQ.toMutableMap(), revision)
    }

    private val trt = listOf(
        Sub("Português", 14, PlanPriority.CRITICAL), Sub("Dir. Constitucional", 12, PlanPriority.HIGH),
        Sub("Dir. Administrativo", 14, PlanPriority.HIGH), Sub("Dir. do Trabalho", 16, PlanPriority.CRITICAL),
        Sub("Proc. do Trabalho", 12, PlanPriority.HIGH), Sub("Engenharia de Software", 12, PlanPriority.CRITICAL, difficulty = PersonalDifficulty.VERY_HARD),
        Sub("Banco de Dados", 10, PlanPriority.HIGH, difficulty = PersonalDifficulty.HARD), Sub("Redes", 10, PlanPriority.MEDIUM),
        Sub("Segurança da Informação", 10, PlanPriority.HIGH), Sub("Governança de TI", 8, PlanPriority.MEDIUM),
        Sub("Raciocínio Lógico", 6, PlanPriority.LOW, difficulty = PersonalDifficulty.EASY), Sub("Legislação TRT", 4, PlanPriority.LOW),
    )
    private val small = listOf(
        Sub("Português", 8, PlanPriority.CRITICAL), Sub("Dir. Constitucional", 6, PlanPriority.HIGH),
        Sub("Informática", 5, PlanPriority.MEDIUM, difficulty = PersonalDifficulty.VERY_HARD), Sub("Legislação", 4, PlanPriority.LOW, difficulty = PersonalDifficulty.EASY),
    )
    private val big = trt + listOf(
        Sub("Dir. Penal", 18, PlanPriority.HIGH), Sub("Proc. Penal", 16, PlanPriority.HIGH), Sub("Dir. Civil", 20, PlanPriority.MEDIUM, difficulty = PersonalDifficulty.HARD),
        Sub("Proc. Civil", 18, PlanPriority.MEDIUM), Sub("Dir. Tributário", 14, PlanPriority.LOW), Sub("Arquivologia", 8, PlanPriority.LOW),
    )

    /** Curtos: rodam sempre, com assert. */
    private fun quick() = listOf(
        Scenario("Q1. Pequeno, 10h/sem, 8 semanas; lê a teoria inteira de uma vez", small, 600, 56),
        Scenario("Q2. Pequeno, 6h/sem, 8 semanas; fiz uma parte, falta 1 dia em 4, replans repetidos", small, 360, 56, theory = TheoryMode.PART, openChance = 0.75, extraReplans = 2, seed = 7),
        Scenario("Q3. Pequeno, 8h/sem, 8 semanas; lento, relê a matéria a cada parte, pula e larga o dia", small, 480, 56, theory = TheoryMode.REREAD, theorySpeed = 1.6, skipChance = 0.08, leaveChance = 0.1, seed = 11),
        Scenario("Q5. Pequeno, 8h/sem, 8 semanas; ritmo normal, mas relê a matéria a cada parte que aparece", small, 480, 56, theory = TheoryMode.REREAD, seed = 19),
        Scenario("Q4. Pequeno, 8h/sem, 8 semanas; dois aparelhos a cada 3 dias, folgas e mudança de horas", small, 480, 56, twoDevicesEvery = 3, daysOff = setOf(9, 23, 24), hours = mapOf(20 to 300, 40 to 720), seed = 3),
    )

    /**
     * Guarda de lançamento (scripts/guardas.sh): alunos de verdade por 60 a 120 dias, com assert em tudo.
     * Cada perfil cobre uma das falhas que o dono viu: teoria repetida dia após dia, tarefa duplicada, atrasada
     * só no celular, dois aparelhos, plano editado no meio do caminho, estudo fora do plano.
     */
    private fun guards() = listOf(
        Scenario("G1. Poucos minutos (25 min/dia), pequeno, 90 dias", small, 150, 90, guard = true, seed = 101),
        Scenario("G2. Muitas matérias (18, 220 tópicos), 25h/sem, 120 dias", big, 1500, 120, theory = TheoryMode.PART, openChance = 0.9, guard = true, seed = 102),
        Scenario("G3. Prova logo (30 dias), pequeno, 10h/sem", small, 600, 30, guard = true, seed = 103),
        Scenario("G4. Prova longe com folga (120 dias), pequeno, 15h/sem: cobre tudo", small, 900, 120, mustCoverAll = true, guard = true, seed = 104),
        Scenario("G5. Pula dias (abre 7 em 10, some 9 dias), TRT, 15h/sem, 100 dias", trt, 900, 100, openChance = 0.7, absences = listOf(30..38), skipChance = 0.05, guard = true, seed = 105),
        Scenario("G6. Faz só parte das tarefas e larga o dia, TRT, 12h/sem, 90 dias", trt, 720, 90, theory = TheoryMode.PART, leaveChance = 0.15, guard = true, seed = 106),
        Scenario("G7. Adianta o dia seguinte quase sempre, pequeno, 10h/sem, 90 dias: cobre tudo", small, 600, 90, earlyChance = 0.7, mustCoverAll = true, guard = true, seed = 107),
        Scenario("G8. Estuda pela prática livre (fora do plano) 1 dia em 3, TRT, 15h/sem, 100 dias", trt, 900, 100, freePracticeChance = 0.33, guard = true, seed = 108),
        Scenario("G9. Edita o plano no meio (põe matéria, tira matéria, sobe prioridade, muda horas), TRT, 15h/sem, 100 dias", trt, 900, 100,
            edits = mapOf(20 to Edit.ADD_SUBJECT, 45 to Edit.REMOVE_SUBJECT, 60 to Edit.PAUSE_SUBJECT, 70 to Edit.PRIORITY_UP), hours = mapOf(55 to 600), guard = true, seed = 109),
        Scenario("G10. Dois aparelhos em dias alternados, cada um conclui tarefas diferentes, com prática livre, TRT, 15h/sem, 90 dias", trt, 900, 90, twoDevicesEvery = 2, freePracticeChance = 0.2, guard = true, seed = 110),
    )

    @Test
    fun releaseGuardScenariosKeepTheInvariants() {
        val out = StringBuilder()
        val failures = mutableListOf<String>()
        guards().forEach { sc ->
            val m = run(sc)
            out.append(report(sc, m)).append("nao-determinismo ${m.nondeterministic} · atrasadas que sobraram depois de encaixar ${m.overdueLeft} (contagem divergente ${m.overdueMismatch}) · tarefa de matéria tirada ${m.removedSubjectTasks}\n")
            m.examples.filterKeys { it in setOf("det", "atrasada", "tirada") }.forEach { (k, l) -> out.append("  ex. $k: ${l.take(2)}\n") }
            out.append("\n")
            fun check(ok: Boolean, what: String, key: String) { if (!ok) failures += "${sc.name}: $what ${m.examples[key].orEmpty().take(2)}" }
            check(m.theoryAgainPlanned + m.theoryAgainSeen == 0, "teoria concluída reapareceu", "a")
            check(m.questionBeforeTheoryPlanned + m.questionBeforeTheoryDone + m.questionOrphan == 0, "questão antes da teoria", "b")
            check(m.overDates.isEmpty() && m.zeroDayTasks == 0, "dia acima das horas", "c")
            check(m.duplicates == 0, "tarefa duplicada", "h")
            check(m.repeatChangedWeek == 0, "reorganizar de novo mudou a semana", "d2")
            check(m.reviewTasks == 0, "revisão virou tarefa do plano", "-")
            check(m.nondeterministic == 0, "mesma foto deu planos diferentes", "det")
            check(m.overdueLeft == 0 && m.overdueMismatch == 0, "atrasada sobrou depois de encaixar", "atrasada")
            check(m.removedSubjectTasks == 0, "matéria tirada continua no plano", "tirada")
            if (sc.mustCoverAll) check(m.covered == m.total, "edital não coberto (${m.covered} de ${m.total})", "-")
            // Logo depois de juntar dois aparelhos (antes de o motor rodar) pode haver duas partes da mesma teoria no
            // mesmo dia, vindas de reorganizações diferentes em cada aparelho: o próximo replanejamento tira (conferido
            // acima em "duplicadas", que vale depois de cada reorganização). Por isso aqui não é falha.
        }
        File("build/plan-guard.txt").writeText(out.toString() + "\nfalhas:\n" + failures.joinToString("\n"))
        println(out)
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    /** Longos: só com AUDIT=1. */
    private fun long() = listOf(
        Scenario("A. TRT, 20h/sem, 6 meses; lê a teoria inteira de uma vez", trt, 1200, 180),
        Scenario("B. TRT, 12h/sem, 6 meses; cumpre o cronômetro de cada parte, lento (1,5x) e 4 min por questão", trt, 720, 180, theory = TheoryMode.TIMER, theorySpeed = 1.5, questionMinutes = 4),
        Scenario("C. TRT, 12h/sem, 6 meses; fiz uma parte (em andamento) e continua depois", trt, 720, 180, theory = TheoryMode.PART, seed = 5),
        Scenario("D. TRT, 10h/sem, 6 meses; relê a matéria inteira a cada parte, muito lento (1,8x)", trt, 600, 180, theory = TheoryMode.REREAD, theorySpeed = 1.8),
        Scenario("E. TRT, 20h/sem, 6 meses; rápido (0,6x), adianta a tarefa de amanhã em metade dos dias", trt, 1200, 180, theorySpeed = 0.6, earlyChance = 0.5),
        Scenario("F. TRT, 15h/sem, 5 meses; some 2 semanas no dia 20 e 10 dias no dia 80, falta 1 dia em 5", trt, 900, 150, absences = listOf(20..33, 80..89), openChance = 0.8, seed = 9),
        Scenario("G. TRT, 15h/sem, 5 meses; atrasada espera (não encaixa), larga o dia no meio em 15% das tarefas", trt, 900, 150, overdue = Overdue.WAIT, leaveChance = 0.15, seed = 13),
        Scenario("H. TRT, 15h/sem, 5 meses; empurra tudo quando atrasa, pula 10% das tarefas", trt, 900, 150, overdue = Overdue.PUSH, leaveChance = 0.1, skipChance = 0.1, seed = 17),
        Scenario("I. TRT, 20h/sem, 6 meses; horas mudam 4 vezes (20h, 8h, 25h, 12h) e 8 folgas", trt, 1200, 180, hours = mapOf(25 to 480, 60 to 1500, 100 to 720, 130 to 1200), daysOff = setOf(5, 6, 31, 44, 45, 46, 90, 121)),
        Scenario("J. TRT, 15h/sem, 5 meses; dois aparelhos sem sincronizar a cada 2 dias", trt, 900, 150, twoDevicesEvery = 2, seed = 21),
        Scenario("K. TRT, 15h/sem, 5 meses; 3 reorganizações a mais por dia, falta 1 dia em 4", trt, 900, 150, extraReplans = 3, openChance = 0.75, seed = 23),
        Scenario("L. Edital grande (18 matérias, 220 tópicos), 12h/sem, 4 meses (não cabe)", big, 720, 120),
        Scenario("M. Edital grande, 30h/sem, 8 meses; fiz uma parte, falta 1 dia em 6", big, 1800, 240, theory = TheoryMode.PART, openChance = 0.85, seed = 29),
        Scenario("N. Pequeno, 5h/sem, prova em 30 dias", small, 300, 30),
        Scenario("O. Pequeno, 4h/sem, prova em 12 meses (longe)", small, 240, 365, theory = TheoryMode.TIMER),
        Scenario("P. TRT no Grátis (2 matérias novas por dia, hoje não muda), 10h/sem, 5 meses, falta 1 dia em 4", trt, 600, 150, free = true, openChance = 0.75, seed = 31),
        Scenario("R. TRT, 15h/sem, 5 meses; caos: fiz uma parte, pula, larga, adianta, folgas, horas, dois aparelhos, replans repetidos", trt, 900, 150, theory = TheoryMode.PART, theorySpeed = 1.3, questionMinutes = 3,
            absences = listOf(40..46), openChance = 0.85, leaveChance = 0.08, skipChance = 0.05, earlyChance = 0.2, hours = mapOf(30 to 600, 75 to 1080), daysOff = setOf(12, 55, 56, 101),
            extraReplans = 1, twoDevicesEvery = 5, seed = 37),
        Scenario("S. TRT já sabe 4 matérias (diagnóstico), 15h/sem, 5 meses; 14 dias seguidos sem abrir duas vezes", trt.mapIndexed { i, s -> if (i < 4) s.copy(knowledge = InitialKnowledge.STRONG) else s }, 900, 150,
            profile = StudyProfile.APROFUNDANDO, absences = listOf(10..23, 60..73), seed = 41),
    )

    @Test
    fun quickScenariosKeepTheInvariants() {
        quick().forEach { sc ->
            val m = run(sc)
            println(report(sc, m))
            assertEquals(0, m.theoryAgainPlanned + m.theoryAgainSeen, "${sc.name}: teoria concluída reapareceu ${m.examples["a"]}")
            assertEquals(0, m.questionBeforeTheoryPlanned + m.questionBeforeTheoryDone + m.questionOrphan, "${sc.name}: questão antes da teoria ${m.examples["b"]}")
            assertEquals(0, m.overDates.size + m.zeroDayTasks, "${sc.name}: dia acima das horas ${m.examples["c"]}")
            assertEquals(0, m.duplicates, "${sc.name}: tarefa duplicada ${m.examples["h"]}")
            assertEquals(0, m.theoryOutOfRange, "${sc.name}: teoria fora da faixa ${m.examples["f"]}")
            assertEquals(0, m.theoryRegrown, "${sc.name}: teoria lida em parte cresceu ${m.examples["f2"]}")
            assertEquals(0, m.fragments, "${sc.name}: caco de poucos minutos ${m.examples["caco"]}")
            assertEquals(0, m.repeatChangedWeek, "${sc.name}: reorganizar de novo mudou a semana ${m.examples["d2"]}")
            assertEquals(0, m.reviewTasks, "${sc.name}: revisão virou tarefa do plano")
            assertTrue(m.paceMax <= 1.6 && m.paceMin >= 0.7, "${sc.name}: ritmo fora dos limites ${m.paceMin}..${m.paceMax}")
        }
    }

    @Test
    fun audit() {
        if (System.getenv("AUDIT") != "1") return
        val only = System.getenv("AUDIT_ONLY")?.split(",")?.map { it.trim() }
        val out = StringBuilder()
        val rows = mutableListOf<Pair<Scenario, Metrics>>()
        (quick() + long()).filter { s -> only == null || only.any { s.name.startsWith("$it.") } }.forEach { sc ->
            val m = run(sc)
            rows += sc to m
            out.append(report(sc, m)).append("\n")
        }
        val table = buildString {
            appendLine("cenário | a teoria de novo | b questão antes | c dias acima (máx min) | d semana mudou % | d2 replan repetido tirou semana/mês (só acrescentou) | e coberto | f teoria min..máx (fora) ritmo | g simulado % / revisão no plano | h duplicadas | cacos | teoria partida (máx partes) | leitura cortada | antes de reorganizar (juntar/empurrar): dias acima, duplicadas")
            rows.forEach { (sc, m) ->
                appendLine("${sc.name.substringBefore('.')} | ${m.theoryAgainPlanned}/${m.theoryAgainSeen} | ${m.questionBeforeTheoryPlanned}/${m.questionBeforeTheoryDone}/${m.questionOrphan} | ${m.overDates.size + m.zeroDayTasks} (${m.overDates.values.maxOrNull() ?: 0}) | ${pct(m.weekChanged, m.weekSlots)} | ${m.repeatChangedWeek}/${m.repeatChangedAll} (+${m.repeatAddedWeek}) de ${m.repeatReplans} | ${m.covered}/${m.total} | ${if (m.theoryMin == Int.MAX_VALUE) 0 else m.theoryMin}..${m.theoryMax} (${m.theoryOutOfRange}) ${"%.2f".format(m.paceMin)}..${"%.2f".format(m.paceMax)} | ${pct(m.simulationMinutes, m.simulationMinutes + m.theoryMinutes + m.questionMinutes)} / ${m.reviewTasks} | ${m.duplicates} | ${m.fragments} | ${m.splitTopics.size} (${m.maxParts}) | ${m.theoryShort} | ${(m.soft?.overDates?.size ?: 0) + (m.soft?.zeroDayTasks ?: 0)}, ${m.soft?.duplicates ?: 0}")
            }
        }
        File("build/plan-stress.txt").writeText(table + "\n" + out)
        println(table)
    }

    private fun pct(a: Int, b: Int) = if (b == 0) "0%" else "${a * 1000 / b / 10.0}%"

    private fun report(sc: Scenario, m: Metrics) = buildString {
        appendLine("=== ${sc.name}")
        appendLine("reorganizações: ${m.replans} · dias estudados: ${m.studyDays} · dias vazios (com horas e sem tarefa): ${m.emptyDays} · maior fila de atrasadas: ${m.overdueMax}")
        appendLine("(a) teoria concluída de novo no plano: ${m.theoryAgainPlanned} observações · o aluno viu: ${m.theoryAgainSeen}")
        appendLine("(b) questão antes da teoria: marcada antes ${m.questionBeforeTheoryPlanned} · feita antes ${m.questionBeforeTheoryDone} · questão sem teoria marcada ${m.questionOrphan} · no mesmo dia antes da teoria ${m.questionBeforeTheorySameDay}")
        appendLine("(c) dias acima das horas: ${m.overDates.size} (máx +${m.overDates.values.maxOrNull() ?: 0} min) · tarefa em dia sem estudo: ${m.zeroDayTasks} · onde nasce: ${m.overByReason}")
        appendLine("(d) semana vista mudou: ${pct(m.weekChanged, m.weekSlots)} (${m.weekChanged} de ${m.weekSlots}) por motivo ${m.changedByReason.map { (k, v) -> "$k ${pct(v, m.slotsByReason[k] ?: 0)}" }} · reorganizar de novo tirou algo: ${m.repeatChangedWeek} vezes na semana, ${m.repeatChangedAll} no mês (só acrescentou na semana: ${m.repeatAddedWeek}), em ${m.repeatReplans}")
        m.soft?.let { s -> appendLine("    antes de o motor reorganizar (logo depois de juntar dois aparelhos ou de empurrar tudo): dias acima das horas ${s.overDates.size} (máx +${s.overDates.values.maxOrNull() ?: 0}) · em dia sem estudo ${s.zeroDayTasks} · duplicadas ${s.duplicates} · teoria fora da faixa ${s.theoryOutOfRange} · questão antes da teoria ${s.questionBeforeTheoryPlanned + s.questionOrphan}"); s.examples.forEach { (k, l) -> appendLine("      ex. $k: " + l.take(2).joinToString(" || ")) } }
        appendLine("(e) edital: ${m.covered}/${m.total} vistos até a prova, ${m.coveredByDeadline} antes da reta final · incidência média do que ficou de fora ${m.leftIncidence} contra ${m.allIncidence} do edital")
        appendLine("(f) teoria por tópico: ${if (m.theoryMin == Int.MAX_VALUE) 0 else m.theoryMin} a ${m.theoryMax} min (fora da faixa: ${m.theoryOutOfRange}; cresceu depois de lida em parte: ${m.theoryRegrown}) · ritmo ${"%.2f".format(m.paceMin)} a ${"%.2f".format(m.paceMax)} · min por questão máx ${m.mpqMax}")
        appendLine("(g) tempo feito: teoria ${m.theoryMinutes} min · questões ${m.questionMinutes} min · simulado ${m.simulationMinutes} min · tarefas de revisão no plano: ${m.reviewTasks}")
        appendLine("(h) duplicadas: ${m.duplicates} · cacos: ${m.fragments} · teoria partida: ${m.splitTopics.size} tópicos (máx ${m.maxParts} partes) · leitura cortada (parte concluída tirou o resto): ${m.theoryShort} · retomadas feitas ${m.retakesDone}, retomada trocada ${m.retakeChurn}")
        m.examples.forEach { (k, l) -> appendLine("  ex. $k: " + l.joinToString(" || ")) }
    }

    private val pending = setOf(PlanTaskStatus.PLANEJADA, PlanTaskStatus.EM_ANDAMENTO)

    fun run(sc: Scenario): Metrics {
        val main = Metrics()
        val soft = Metrics().also { main.soft = it }
        var m = main
        val rnd = Random(sc.seed)
        val zone = TimeZone.UTC
        val start = LocalDate(2026, 11, 2)
        val exam = start.plusDays(sc.examInDays.toLong())
        val retaFinal = StudyMethod.phases(start, exam, sc.profile).firstOrNull { it.kind == PlanPhaseKind.RETA_FINAL }?.start ?: exam
        val dayMs = 86_400_000L
        var nextId = 1L
        val config = StudyMethodConfig.forProfile(sc.profile)
        val engine = StudyPlannerEngine()
        var subjects = sc.subjects.mapIndexed { i, s -> PlannerSnapshotBuilder.PlanSubject(i + 1L, s.name, s.priority, false, 0, null, i, s.difficulty, s.knowledge) }
        val subOf = sc.subjects.mapIndexed { i, s -> (i + 1L) to s }.toMap().toMutableMap()
        val questionsByTopic = mutableMapOf<Long, Set<Long>>()
        var qid = 1L
        fun topicsOf(i: Int, s: Sub) = (0 until s.topics).map { p ->
            val id = 1000L * (i + 1) + p
            questionsByTopic[id] = (0 until 40).map { qid++ }.toSet()
            PlannerSnapshotBuilder.Topic(id, i + 1L, null, "${s.name} ${p + 1}", p, false, null, incidence = ((p * 37 + i * 11) % 60) + 1)
        }
        var baseTopics = sc.subjects.flatMapIndexed { i, s -> topicsOf(i, s) }
        val subjectOfTopic = baseTopics.associate { it.id to it.subjectId }.toMutableMap()
        val titleOf = baseTopics.associate { it.id to it.title }.toMutableMap()
        val removedSubjects = hashSetOf<Long>()
        var availability = week(sc.weeklyMinutes)
        val overrides = mutableListOf<PlannerSnapshotBuilder.DayOverride>()
        fun capacity(d: LocalDate): Int {
            overrides.firstOrNull { it.epochDay == d.toEpochDays().toLong() }?.let { if (it.unavailable) return 0 }
            return availability.first { it.isoDayOfWeek == d.dayOfWeek.ordinal + 1 }.let { if (it.unavailable) 0 else it.minutes }
        }
        fun baseTheory(topicId: Long) = StudyPlanBlueprint.baseTheoryMinutes(subOf.getValue(subjectOfTopic.getValue(topicId)).difficulty)
        fun fullTrack(topicId: Long) = subOf.getValue(subjectOfTopic.getValue(topicId)).knowledge <= InitialKnowledge.CONTACT
        /** Quanto de teoria o tópico tinha marcado quando a pessoa fez uma parte dele. */
        val plannedAtPartial = hashMapOf<Long, Int>()

        fun topics(s: State) = baseTopics.map { t -> s.studied[t.id]?.let { t.copy(studied = true, lastStudiedAt = it.toEpochDays() * dayMs) } ?: t }
        fun snapshot(s: State, day: LocalDate) = PlannerSnapshotBuilder.build(
            PlannerSnapshotBuilder.Input(
                PlannerSnapshotBuilder.Plan("p", s.revision, start.toEpochDays().toLong(), exam.toEpochDays().toLong(), config),
                subjects, topics(s), questionsByTopic, s.attempts.toList(), s.reviews.toList(), s.tasks.toList(), s.executions.toList(),
                availability, overrides.toList(), day, zone, freeRules = sc.free,
            ),
        )

        // O que a pessoa vê marcado. A parte que sobrou de uma teoria já concluída não conta: ela tem de sair mesmo.
        fun view(s: State, from: LocalDate, days: Int) = s.tasks.filter { it.status == PlanTaskStatus.PLANEJADA && !it.date.isBefore(from) && daysBetween(from, it.date) < days }
            .filterNot { t -> t.type == PlanTaskType.THEORY && s.theoryOn[t.topicId]?.let { daysBetween(it, t.date) < 30 } == true }
            .map { "${it.date}|${it.topicId?.let(titleOf::get) ?: "matéria ${it.subjectId}"}|${it.type}|${it.plannedMinutes}" }.toSet()

        fun observe(s: State, day: LocalDate, label: String) {
            val live = s.tasks.filter { it.status in pending }
            val concluded = s.tasks.filter { it.status == PlanTaskStatus.CONCLUIDA }
            // (a) teoria de tópico já concluído (não é a retomada, que só volta 30 dias depois).
            live.filter { it.type == PlanTaskType.THEORY && it.topicId != null && !it.date.isBefore(day) }.forEach { t ->
                val done = s.theoryOn[t.topicId!!] ?: return@forEach
                if (daysBetween(done, t.date) < 30) { m.theoryAgainPlanned++; m.example("a", "$label em $day: ${titleOf[t.topicId]} lida em $done, teoria de ${t.plannedMinutes} min em ${t.date}") }
            }
            // (b) questão marcada antes da teoria do mesmo tópico, ou sem teoria nenhuma marcada.
            val liveTheory = live.filter { it.type == PlanTaskType.THEORY && it.topicId != null }.groupBy { it.topicId!! }
            live.filter { it.type == PlanTaskType.QUESTIONS && it.topicId != null && !it.date.isBefore(day) }.forEach { q ->
                val tid = q.topicId!!
                val theory = liveTheory[tid]
                if (tid in s.theoryOn) return@forEach // treino de tópico já lido (a teoria marcada é a retomada)
                if (theory != null) {
                    val last = theory.maxOf { it.date }
                    if (last > q.date) { m.questionBeforeTheoryPlanned++; m.example("b", "$label em $day: questões de ${titleOf[tid]} em ${q.date}, teoria até $last") }
                    else if (last == q.date && theory.filter { it.date == last }.any { it.sequence > q.sequence }) m.questionBeforeTheorySameDay++
                } else if (day < retaFinal && fullTrack(tid) && tid !in s.studied && tid !in s.partial && s.tasks.none { it.topicId == tid && it.type == PlanTaskType.THEORY && it.status == PlanTaskStatus.CONCLUIDA }) {
                    m.questionOrphan++; m.example("b", "$label em $day: questões de ${titleOf[tid]} em ${q.date} sem teoria marcada")
                }
            }
            // (c) dias acima das horas (o que está marcado mais o que já foi feito no dia).
            val byDate = (live + concluded).filter { !it.date.isBefore(day) }.groupBy { it.date }
            byDate.forEach { (d, l) ->
                val open = l.filter { it.status in pending }
                if (open.isEmpty()) return@forEach
                val cap = capacity(d)
                val load = l.sumOf { it.plannedMinutes }
                if (cap == 0) { m.zeroDayTasks++; m.example("c", "$label em $day: ${open.size} tarefa(s) em $d, dia sem estudo: " + open.joinToString(",") { "${it.type.name.take(4)}${it.plannedMinutes}/${it.status.name.take(4)}/de:${it.replannedFromTaskId?.take(6)}/${it.id.take(6)}" }) }
                else if (load > cap) {
                    if (d !in m.overDates) m.overByReason[label] = (m.overByReason[label] ?: 0) + 1
                    m.overDates[d] = maxOf(m.overDates[d] ?: 0, load - cap)
                    m.example("c", "$label em $day: $d com $load de $cap min: " + l.joinToString(",") { "${it.type.name.take(4)}${it.plannedMinutes}${if (it.status == PlanTaskStatus.CONCLUIDA) "ok" else ""}" })
                }
            }
            // (h) duplicadas no mesmo dia, tópico e tipo.
            live.filter { !it.date.isBefore(day) }.groupBy { Triple(it.date, it.topicId ?: -(it.subjectId ?: 0), it.type) }.forEach { (k, l) ->
                if (l.size > 1) { m.duplicates++; m.example("h", "$label em $day: ${l.size}x ${k.third} de ${titleOf[k.second] ?: k.second} em ${k.first} (${l.joinToString("+") { it.plannedMinutes.toString() }} min)") }
            }
            // (f) tamanho da teoria de cada tópico ainda não começado (soma das partes marcadas).
            liveTheory.forEach { (tid, parts) ->
                if (tid in s.theoryOn) return@forEach
                // Lido em parte: o que já leu conta junto com o que ainda está marcado.
                val partIds = parts.mapTo(hashSetOf()) { it.id }
                val totalMinutes = parts.sumOf { it.plannedMinutes } + (if (tid in s.partial) s.executions.filter { it.topicId == tid && it.taskId !in partIds }.sumOf { it.minutes } else 0)
                m.theoryMax = maxOf(m.theoryMax, totalMinutes); m.theoryMin = minOf(m.theoryMin, totalMinutes)
                val days = parts.map { it.date }.distinct().size
                if (days > 1) { m.splitTopics += tid; m.maxParts = maxOf(m.maxParts, days) }
                plannedAtPartial[tid]?.let { planned ->
                    if (totalMinutes > planned + 10) { m.theoryRegrown++; m.example("f2", "$label em $day: ${titleOf[tid]} tinha $planned min de teoria quando a pessoa fez uma parte; agora soma $totalMinutes") }
                }
                if (totalMinutes > 125 || totalMinutes < 20) { m.theoryOutOfRange++; m.example("f", "$label em $day: teoria de ${titleOf[tid]} com $totalMinutes min em ${parts.size} parte(s) " + parts.sortedBy { it.date }.joinToString(",") { "${it.date.dayOfMonth}:${it.plannedMinutes}" }) }
            }
            m.reviewTasks += live.count { it.type == PlanTaskType.REVIEW }
            live.filter { it.subjectId in removedSubjects && !it.date.isBefore(day) }.forEach {
                m.removedSubjectTasks++; m.example("tirada", "$label em $day: ${it.type} de matéria tirada do plano em ${it.date}")
            }
        }

        fun replan(s: State, day: LocalDate, reason: ReplanReason, label: String = reason.name): PlanningProposal {
            val stable = reason in StudyPlannerEngine.stableReasons
            // O que a pessoa já tinha visto para os próximos dias (em DAY_MISSED conta hoje também).
            val from = if (reason == ReplanReason.DAY_MISSED) day else day.plusDays(1)
            val before = if (stable) view(s, from, 7 - (if (reason == ReplanReason.DAY_MISSED) 0 else 1)) else emptySet()
            fun retakes() = s.tasks.filter { t -> t.status == PlanTaskStatus.PLANEJADA && t.type == PlanTaskType.THEORY && s.theoryOn[t.topicId]?.let { daysBetween(it, t.date) >= 30 } == true }.map { "${it.topicId}|${it.date}" }.toSet()
            val liveRetakes = retakes()
            val snap = snapshot(s, day)
            val proposal = engine.plan(snap, reason)
            // Estável: a mesma foto tem de dar o mesmo plano (senão celular e site, que reorganizam cada um o seu,
            // chegam a planos diferentes e a sincronização junta os dois).
            if (sc.guard) {
                val again = engine.plan(snapshot(s, day), reason)
                if (again.newTasks != proposal.newTasks || again.transitions != proposal.transitions) {
                    m.nondeterministic++; m.example("det", "$label em $day: ${proposal.newTasks.size} x ${again.newTasks.size} tarefas novas, ${(proposal.newTasks - again.newTasks.toSet()).take(2).map { "${it.date} ${it.type} ${it.plannedMinutes}" }}")
                }
            }
            s.revision++
            val moved = proposal.transitions.associateBy { it.taskId }
            s.tasks = s.tasks.map { t -> moved[t.id]?.let { t.copy(status = it.to) } ?: t }.toMutableList()
            s.tasks += proposal.newTasks
            m.replans++
            proposal.newTasks.filter { capacity(it.date) == 0 }.forEach { m.example("zero", "$label em $day: ${it.type} novo em ${it.date} (${it.date.dayOfWeek}), dia sem estudo, de ${it.replannedFromTaskId?.let { id -> s.tasks.firstOrNull { t -> t.id == id }?.date }}") }
            // Caco: teoria de menos de 15 min (fora o resto de uma leitura feita em parte), ou questões de menos de 6 min (nenhuma tarefa inteira é tão curta).
            proposal.newTasks.filter { (it.type == PlanTaskType.THEORY && it.plannedMinutes < 15 && it.topicId !in s.partial) || (it.type == PlanTaskType.QUESTIONS && it.plannedMinutes < 6) }.forEach {
                m.fragments++; m.example("caco", "$label em $day: ${it.type} de ${it.plannedMinutes} min em ${it.date} (${it.topicId?.let(titleOf::get) ?: "matéria ${it.subjectId}"})")
            }
            snap.minutesPerQuestion?.let { m.mpqMax = maxOf(m.mpqMax, it) }
            observe(s, day, label)
            if (stable) {
                val after = view(s, from, 7 - (if (reason == ReplanReason.DAY_MISSED) 0 else 1))
                val gone = before - after
                m.weekSlots += before.size; m.weekChanged += gone.size
                m.slotsByReason[label] = (m.slotsByReason[label] ?: 0) + before.size
                m.changedByReason[label] = (m.changedByReason[label] ?: 0) + gone.size
                if (gone.isNotEmpty()) m.example("d $label", "em $day saiu ${gone.take(3)} · entrou ${(after - before).take(3)}")
                val lost = liveRetakes - retakes()
                m.retakeChurn += lost.size
                if (lost.isNotEmpty()) m.example("retomada", "$label em $day: saiu ${lost.take(2).map { titleOf[it.substringBefore('|').toLong()] + " " + it.substringAfter('|') }}")
            }
            return proposal
        }

        fun pCorrect(s: State, topicId: Long): Double {
            val sub = subOf.getValue(subjectOfTopic.getValue(topicId))
            return (0.30 + 0.45 * sub.knowledge.normalized + (if (topicId in s.studied) 0.22 else 0.0) - 0.20 * (sub.difficulty.normalized - 0.5)).coerceIn(0.15, 0.95)
        }

        fun conclude(s: State, task: PlannerTask, day: LocalDate, minutes: Int, questions: Int = 0, correct: Int = 0) {
            val idx = s.tasks.indexOfFirst { it.id == task.id }
            s.tasks[idx] = s.tasks[idx].copy(status = PlanTaskStatus.CONCLUIDA)
            // Tempo igual ao planejado não é medida (o app descarta); o aluno de verdade nunca crava o minuto.
            val real = if (minutes == task.plannedMinutes) minutes + 1 else minutes
            s.executions += TaskExecution("e${nextId++}", task.id, task.subjectId, task.topicId, day, real.coerceAtLeast(1), questions, correct)
        }

        /** Faz uma tarefa. Devolve false quando a pessoa para o dia ali. */
        fun doTask(s: State, task: PlannerTask, day: LocalDate): Boolean {
            val tid = task.topicId
            when (task.type) {
                PlanTaskType.THEORY -> {
                    tid!!
                    val last = s.theoryOn[tid]
                    val need = kotlin.math.round(baseTheory(tid) * sc.theorySpeed).toInt()
                    if (last != null && daysBetween(last, day) < 30) {
                        // Já leu este tópico e a teoria apareceu de novo: a falha que o dono viu.
                        m.theoryAgainSeen++; m.example("a", "aluno em $day: teoria de ${titleOf[tid]} de novo (${task.plannedMinutes} min), lida em $last")
                        conclude(s, task, day, if (sc.theory == TheoryMode.REREAD) need else 2)
                    } else if (last != null) {
                        m.retakesDone++
                        conclude(s, task, day, task.plannedMinutes + 3)
                        s.theoryOn[tid] = minOf(day, task.date)
                    } else {
                        val read = s.readMinutes[tid] ?: 0
                        val otherParts = s.tasks.count { it.id != task.id && it.topicId == tid && it.type == PlanTaskType.THEORY && it.status in pending }
                        when {
                            sc.theory == TheoryMode.PART && tid !in s.partial && rnd.nextDouble() < 0.6 -> {
                                val minutes = (task.plannedMinutes * (0.3 + rnd.nextDouble() * 0.4)).toInt().coerceIn(5, (task.plannedMinutes - 1).coerceAtLeast(5))
                                val idx = s.tasks.indexOfFirst { it.id == task.id }
                                s.tasks[idx] = s.tasks[idx].copy(status = PlanTaskStatus.EM_ANDAMENTO)
                                s.executions += TaskExecution("e${nextId++}", task.id, task.subjectId, tid, day, minutes, 0, 0)
                                plannedAtPartial[tid] = task.plannedMinutes + s.tasks.filter { it.id != task.id && it.topicId == tid && it.type == PlanTaskType.THEORY && it.status in pending }.sumOf { it.plannedMinutes }
                                s.partial += tid; s.readMinutes[tid] = read + minutes
                                m.theoryMinutes += minutes
                                replan(s, day, ReplanReason.TASK_PARTIAL)
                                return rnd.nextDouble() < 0.5
                            }
                            sc.theory == TheoryMode.TIMER && otherParts > 0 && need - read > task.plannedMinutes -> {
                                // Cumpriu o tempo da parte e ainda não terminou de ler: conclui a parte.
                                conclude(s, task, day, task.plannedMinutes)
                                s.readMinutes[tid] = read + task.plannedMinutes
                            }
                            else -> {
                                // Termina a leitura: o tempo que faltava para ler o tópico inteiro.
                                val minutes = (need - read).coerceAtLeast(5)
                                conclude(s, task, day, minutes)
                                s.readMinutes[tid] = read + minutes
                            }
                        }
                        m.theoryMinutes += task.plannedMinutes
                        if (s.tasks.first { it.id == task.id }.status == PlanTaskStatus.CONCLUIDA && tid !in s.theoryOn) {
                            // O app marca o tópico como estudado quando a teoria é concluída.
                            // O plano conta a leitura pelo dia da tarefa (a retomada volta 30 dias depois dele).
                            s.theoryOn[tid] = minOf(day, task.date); s.studied[tid] = day
                            listOf(1, 7, 30).forEach { o -> s.reviews += PlannerSnapshotBuilder.PendingReview(nextId++, tid, day.plusDays(o.toLong()).toEpochDays() * dayMs, 0) }
                        }
                    }
                }
                else -> {
                    // Na reta final não há teoria nova: as questões do que não foi visto entram assim mesmo, de propósito.
                    if (day < retaFinal && task.type == PlanTaskType.QUESTIONS && tid != null && fullTrack(tid) && tid !in s.studied) {
                        m.questionBeforeTheoryDone++; m.example("b", "aluno em $day: questões de ${titleOf[tid]} (tarefa de ${task.date}) sem a teoria concluída")
                    }
                    var correct = 0
                    val seen = baseTopics.filter { (task.type == PlanTaskType.SIMULATION || it.subjectId == task.subjectId) && it.id in s.studied }.map { it.id }
                    val session = if (task.type == PlanTaskType.SIMULATION) "simulado-${task.id}" else null
                    repeat(task.plannedQuestions) {
                        val t = tid ?: seen.randomOrNull(rnd) ?: return@repeat
                        val n = s.usedQ.getOrElse(t) { 0 }
                        val q = if (session != null) 9_000_000L + nextId++ else questionsByTopic.getValue(t).elementAt(n % 40)
                        s.usedQ[t] = n + 1
                        val ok = rnd.nextDouble() < pCorrect(s, t)
                        if (ok) correct++
                        s.attempts += PlannerSnapshotBuilder.Attempt(q, ok, day.toEpochDays() * dayMs + 3_600_000L + nextId++, session, if (session != null) t else null)
                    }
                    val minutes = if (task.type == PlanTaskType.QUESTIONS && sc.questionMinutes != null) task.plannedQuestions * sc.questionMinutes else task.plannedMinutes
                    conclude(s, task, day, minutes, task.plannedQuestions, correct)
                    if (task.type == PlanTaskType.SIMULATION) m.simulationMinutes += task.plannedMinutes else m.questionMinutes += task.plannedMinutes
                }
            }
            replan(s, day, ReplanReason.TASK_COMPLETED)
            return true
        }

        /** A próxima tarefa do dia: pela ordem do plano, com a teoria do tópico antes das questões dele. */
        fun nextTask(s: State, day: LocalDate, mine: (PlannerTask) -> Boolean): PlannerTask? {
            val pool = s.tasks.filter { it.status in pending && (it.date == day || (sc.overdue == Overdue.WAIT && it.date < day)) && mine(it) }
                .sortedWith(compareBy({ it.date }, { it.sequence }))
            val first = pool.firstOrNull() ?: return null
            if (first.type == PlanTaskType.THEORY || first.topicId == null) return first
            return pool.firstOrNull { it.type == PlanTaskType.THEORY && it.topicId == first.topicId } ?: first
        }

        fun session(s: State, day: LocalDate, mine: (PlannerTask) -> Boolean = { true }) {
            var guard = 0
            while (guard++ < 40) {
                val task = nextTask(s, day, mine) ?: break
                if (rnd.nextDouble() < sc.leaveChance) break
                if (rnd.nextDouble() < sc.skipChance && !task.locked) {
                    val idx = s.tasks.indexOfFirst { it.id == task.id }
                    s.tasks[idx] = task.copy(status = PlanTaskStatus.NAO_REALIZADA)
                    replan(s, day, ReplanReason.TASK_SKIPPED)
                    continue
                }
                if (!doTask(s, task, day)) break
            }
            if (rnd.nextDouble() < sc.earlyChance && nextTask(s, day, mine) == null) {
                val tomorrow = s.tasks.filter { it.status == PlanTaskStatus.PLANEJADA && it.date > day }.minOfOrNull { it.date }
                val task = tomorrow?.let { d -> s.tasks.filter { it.status == PlanTaskStatus.PLANEJADA && it.date == d }.sortedBy { it.sequence }.let { l -> l.firstOrNull { it.type == PlanTaskType.THEORY } ?: l.firstOrNull() } }
                if (task != null && (task.type == PlanTaskType.THEORY || task.topicId == null || !fullTrack(task.topicId!!) || task.topicId in s.studied)) doTask(s, task, day)
            }
        }

        fun doReviews(s: State, day: LocalDate) {
            val due = s.reviews.groupBy { it.topicId }.mapNotNull { (_, l) -> l.minByOrNull { it.dueAt } }.filter { it.dueAt <= day.toEpochDays() * dayMs }
            due.forEach { r ->
                s.reviews.remove(r)
                if (s.reviews.none { it.topicId == r.topicId }) s.reviews += PlannerSnapshotBuilder.PendingReview(nextId++, r.topicId, day.plusDays(60).toEpochDays() * dayMs, 0)
            }
        }

        /** Junta dois aparelhos como a sincronização do app: por id, decisão final vence; cópia velha sai. */
        fun merge(base: State, a: State, b: State): State {
            val rank = mapOf(PlanTaskStatus.CONCLUIDA to 7, PlanTaskStatus.NAO_REALIZADA to 6, PlanTaskStatus.REPROGRAMADA to 5, PlanTaskStatus.EM_ANDAMENTO to 3, PlanTaskStatus.PAUSADA to 2, PlanTaskStatus.PLANEJADA to 1)
            val out = a.copy()
            val byId = a.tasks.associateBy { it.id }.toMutableMap()
            b.tasks.forEach { t -> val mine = byId[t.id]; if (mine == null || rank.getValue(t.status) > rank.getValue(mine.status)) byId[t.id] = t }
            val order = (a.tasks.map { it.id } + b.tasks.map { it.id }).distinct()
            val merged = order.map { byId.getValue(it) }
            val stale = StaleCopies.ids(merged.map { StaleCopies.Row(it.id, it.replannedFromTaskId, it.status.name, "${it.date}|${it.type}") })
            out.tasks = merged.filter { it.id !in stale }.toMutableList()
            val execIds = a.executions.mapTo(hashSetOf()) { it.id }
            out.executions += b.executions.filter { it.id !in execIds }
            b.studied.forEach { (k, v) -> if (k !in out.studied) out.studied[k] = v }
            b.theoryOn.forEach { (k, v) -> if (k !in out.theoryOn || out.theoryOn.getValue(k) < v) out.theoryOn[k] = v }
            out.partial += b.partial
            b.readMinutes.forEach { (k, v) -> out.readMinutes[k] = maxOf(out.readMinutes[k] ?: 0, v) }
            b.usedQ.forEach { (k, v) -> out.usedQ[k] = maxOf(out.usedQ[k] ?: 0, v) }
            val seenAttempts = a.attempts.toHashSet()
            out.attempts += b.attempts.filter { it !in seenAttempts }
            val baseReviews = base.reviews.mapTo(hashSetOf()) { it.id }
            val removedByB = baseReviews - b.reviews.mapTo(hashSetOf()) { it.id }
            out.reviews.removeAll { it.id in removedByB }
            out.reviews += b.reviews.filter { it.id !in baseReviews && out.reviews.none { r -> r.topicId == it.topicId && r.dueAt == it.dueAt } }
            out.revision = maxOf(a.revision, b.revision) + 1
            return out
        }

        fun morning(s: State, day: LocalDate) {
            val late = s.tasks.filter { it.date < day && it.status in pending }
            m.overdueMax = maxOf(m.overdueMax, late.size)
            when {
                late.isEmpty() || sc.overdue == Overdue.FIT -> {
                    replan(s, day, ReplanReason.DAY_MISSED)
                    // "Encaixar": nada fica para trás; e a conta de atrasadas do app/site (PlanRules) bate com a do simulador.
                    val left = s.tasks.filter { it.date < day && it.status in pending }
                    val rules = s.tasks.count { PlanRules.isOverdue(it.date.toEpochDays().toLong(), it.status.name, day.toEpochDays().toLong()) }
                    if (rules != left.size) m.overdueMismatch++
                    if (left.isNotEmpty()) { m.overdueLeft++; m.example("atrasada", "em $day sobraram ${left.size} atrasadas: ${left.take(3).map { "${it.date} ${it.type} ${it.status} lock=${it.locked}" }}") }
                }
                sc.overdue == Overdue.WAIT -> if (daysBetween(late.minOf { it.date }, day) > 7) replan(s, day, ReplanReason.DAY_MISSED)
                else -> {
                    // "Empurrar tudo": da atrasada mais antiga em diante, tudo anda os mesmos dias.
                    val oldest = late.minOf { it.date }
                    val shift = daysBetween(oldest, day)
                    s.tasks = s.tasks.map { if (it.status in pending && !it.date.isBefore(oldest)) it.copy(date = it.date.plusDays(shift)) else it }.toMutableList()
                    s.revision++
                    // O app não reorganiza depois de empurrar (só na manhã seguinte).
                    m = soft; observe(s, day, "empurrar tudo"); m = main
                }
            }
            repeat(sc.extraReplans) {
                val week = view(s, day, 7); val all = view(s, day, 28)
                replan(s, day, ReplanReason.DAY_MISSED, "DAY_MISSED repetido")
                val weekAfter = view(s, day, 7); val allAfter = view(s, day, 28)
                m.repeatReplans++
                if ((weekAfter - week).isNotEmpty()) m.repeatAddedWeek++
                if ((week - weekAfter).isNotEmpty()) { m.repeatChangedWeek++; m.example("d2", "em $day saiu ${(week - weekAfter).take(3)} · entrou ${(weekAfter - week).take(3)}") }
                if ((all - allAfter).isNotEmpty()) { m.repeatChangedAll++; m.example("d3", "em $day saiu ${(all - allAfter).take(3)} · entrou ${(allAfter - all).take(3)}") }
            }
        }

        /**
         * Prática livre: a pessoa estuda pela biblioteca um tópico que o plano ainda ia marcar (lê a matéria e
         * faz questões fora do plano). O tópico fica estudado; a teoria dele que estava no plano tem de sair.
         */
        fun freePractice(s: State, day: LocalDate) {
            if (sc.freePracticeChance <= 0 || rnd.nextDouble() >= sc.freePracticeChance) return
            val tid = s.tasks.filter { it.status == PlanTaskStatus.PLANEJADA && it.type == PlanTaskType.THEORY && it.date > day && it.topicId != null && it.topicId !in s.studied && it.topicId !in s.partial }
                .sortedBy { it.date }.drop(2).firstOrNull()?.topicId ?: return
            s.studied[tid] = day; s.theoryOn[tid] = day; s.readMinutes[tid] = kotlin.math.round(baseTheory(tid) * sc.theorySpeed).toInt()
            s.executions += TaskExecution("e${nextId++}", null, subjectOfTopic.getValue(tid), tid, day, 30, 10, 7)
            repeat(10) { k ->
                val n = s.usedQ.getOrElse(tid) { 0 }; s.usedQ[tid] = n + 1
                s.attempts += PlannerSnapshotBuilder.Attempt(questionsByTopic.getValue(tid).elementAt(n % 40), k < 7, day.toEpochDays() * dayMs + 7_200_000L + nextId++, null, null)
            }
            listOf(1, 7, 30).forEach { o -> s.reviews += PlannerSnapshotBuilder.PendingReview(nextId++, tid, day.plusDays(o.toLong()).toEpochDays() * dayMs, 0) }
            replan(s, day, ReplanReason.TASK_COMPLETED, "prática livre")
        }

        var state = State()
        replan(state, start, ReplanReason.INITIAL)
        var day = start
        while (day.isBefore(exam)) {
            val dayNo = daysBetween(start, day).toInt()
            sc.hours[dayNo]?.let { availability = week(it); replan(state, day, ReplanReason.AVAILABILITY_CHANGED) }
            sc.edits[dayNo]?.let { edit ->
                when (edit) {
                    Edit.ADD_SUBJECT -> {
                        val i = subOf.size
                        val sub = Sub("Matéria nova ${i + 1}", 6, PlanPriority.HIGH)
                        subOf[i + 1L] = sub
                        subjects = subjects + PlannerSnapshotBuilder.PlanSubject(i + 1L, sub.name, sub.priority, false, 0, null, i, sub.difficulty, sub.knowledge)
                        val added = topicsOf(i, sub)
                        added.forEach { subjectOfTopic[it.id] = it.subjectId; titleOf[it.id] = it.title }
                        baseTopics = baseTopics + added
                    }
                    Edit.REMOVE_SUBJECT -> {
                        val gone = subjects.last { it.priority != PlanPriority.CRITICAL }.subjectId
                        removedSubjects += gone
                        subjects = subjects.filter { it.subjectId != gone }
                        baseTopics = baseTopics.filter { it.subjectId != gone }
                    }
                    Edit.PAUSE_SUBJECT -> {
                        val paused = subjects.first { it.priority != PlanPriority.CRITICAL && it.subjectId !in removedSubjects }.subjectId
                        removedSubjects += paused
                        subjects = subjects.map { if (it.subjectId == paused) it.copy(paused = true) else it }
                    }
                    Edit.PRIORITY_UP -> {
                        val up = subjects.first { (it.priority == PlanPriority.LOW || it.priority == PlanPriority.MEDIUM) && it.subjectId !in removedSubjects }
                        subjects = subjects.map { if (it.subjectId == up.subjectId) it.copy(priority = PlanPriority.CRITICAL) else it }
                    }
                }
                replan(state, day, ReplanReason.SUBJECT_CHANGED, "SUBJECT_CHANGED $edit")
            }
            val absent = sc.absences.any { dayNo in it } || rnd.nextDouble() >= sc.openChance
            if (!absent) {
                if (dayNo in sc.daysOff) {
                    overrides += PlannerSnapshotBuilder.DayOverride(day.toEpochDays().toLong(), null, true, false)
                    replan(state, day, ReplanReason.DAY_MISSED, "DAY_MISSED folga")
                } else {
                    val pace = PersonalPace.measure(state.tasks, state.executions) { if (fullTrack(it)) baseTheory(it) else null }
                    m.paceMax = maxOf(m.paceMax, pace.theoryFactor); m.paceMin = minOf(m.paceMin, pace.theoryFactor)
                    if (sc.twoDevicesEvery > 0 && dayNo > 0 && dayNo % sc.twoDevicesEvery == 0) {
                        // Celular e site abertos no mesmo dia, sem sincronizar entre eles: cada um reorganiza e
                        // estuda a sua metade; no fim do dia os dois se juntam.
                        val base = state.copy()
                        val a = state.copy(); val b = state.copy()
                        morning(a, day); doReviews(a, day); session(a, day) { (it.topicId ?: 0L) % 2 == 0L }
                        morning(b, day); session(b, day) { (it.topicId ?: 0L) % 2 == 1L }
                        state = merge(base, a, b)
                        m = soft; observe(state, day, "depois de juntar dois aparelhos"); m = main
                    } else {
                        if (dayNo > 0) morning(state, day)
                        if (capacity(day) > 0 && state.tasks.none { it.date == day && it.status in pending }) m.emptyDays++
                        if (state.tasks.any { it.date == day && it.status in pending }) m.studyDays++
                        doReviews(state, day)
                        session(state, day)
                        freePractice(state, day)
                    }
                }
            }
            // Tarefa reprogramada e nunca tocada não pesa em nada: sai da memória para a simulação andar.
            val touched = state.executions.mapNotNullTo(hashSetOf()) { it.taskId }
            state.tasks = state.tasks.filter { it.status != PlanTaskStatus.REPROGRAMADA || it.id in touched }.toMutableList()
            day = day.plusDays(1)
        }

        // (e) cobertura e (f) leitura cortada.
        val phases = StudyMethod.phases(start, exam, sc.profile)
        val deadline = phases.firstOrNull { it.kind == PlanPhaseKind.RETA_FINAL }?.start ?: exam
        val byQuestion = state.attempts.groupBy { it.questionId }
        fun seen(t: PlannerSnapshotBuilder.Topic) = t.id in state.studied || questionsByTopic.getValue(t.id).flatMap { byQuestion[it].orEmpty() }.let { it.size >= 8 && it.count { a -> a.correct } * 4 >= it.size * 3 }
        m.total = baseTopics.size
        m.covered = baseTopics.count { seen(it) }
        m.coveredByDeadline = state.studied.values.count { it.isBefore(deadline) }
        val left = baseTopics.filterNot { seen(it) }
        m.leftIncidence = if (left.isEmpty()) 0 else left.mapNotNull { it.incidence }.average().toInt()
        m.allIncidence = baseTopics.mapNotNull { it.incidence }.average().toInt()
        m.theoryShort = state.studied.keys.count { tid -> fullTrack(tid) && (state.readMinutes[tid] ?: 0) * 10 < kotlin.math.round(baseTheory(tid) * sc.theorySpeed).toInt() * 7 }
        return main
    }

    private fun week(minutes: Int) = (1..7).map { PlannerSnapshotBuilder.Availability(it, if (it == 7) 0 else minutes / 6, it == 7) }
}
