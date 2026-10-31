package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.simulation.BoardStyle
import br.com.estudario.domain.simulation.SimulationInsights
import br.com.estudario.domain.simulation.SimulationMode
import br.com.estudario.domain.simulation.SimulationUnlock
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Simulation
import br.com.estudario.web.data.Simulations
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.str
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date

private fun modeIcon(mode: SimulationMode) = when (mode) {
    SimulationMode.DIAGNOSTIC -> "Lupa"
    SimulationMode.STUDIED -> "check_circle"
    SimulationMode.FULL -> "workspace_premium"
    SimulationMode.REMATCH -> "autorenew"
}

/** Um número da faixa de números do topo. */
@Composable
private fun SimStat(value: String, label: String, tone: String? = null) {
    Div({ classes(*listOfNotNull("sx-stat", tone).toTypedArray()) }) { B { Text(value) }; Span { Text(label) } }
}

/** Cabeçalho de seção da página (título curto e, se houver, uma linha de explicação). */
@Composable
private fun SimSection(kicker: String, title: String, note: String? = null) {
    Div({ classes("sx-sec-head") }) {
        Span({ classes("gx-kicker") }) { Text(kicker) }
        B { Text(title) }
        note?.let { P { Text(it) } }
    }
}

/** Situação de um simulado ainda não entregue, em texto curto. */
private fun pendingStatus(simulation: Simulation, partsDone: Int, partsTotal: Int): String = when (simulation.status) {
    "GENERATING" -> if (partsTotal > 0) "Montando… $partsDone de $partsTotal partes prontas" else "Montando…"
    "PARTIAL" -> "Quase pronto"
    "READY" -> "Pronto para começar"
    "IN_PROGRESS" -> "Em andamento · ${simulation.answeredCount}/${simulation.questionCount.takeIf { it > 0 } ?: simulation.plannedQuestions} respondidas"
    "FAILED" -> "Não deu para montar"
    else -> ""
}

private fun isPending(simulation: Simulation) = simulation.status in setOf("GENERATING", "PARTIAL", "READY", "IN_PROGRESS")

@Composable
fun SimulationsScreen() {
    val data = Store.data
    val competition = Queries.primaryCompetition(data)
    if (competition == null) {
        PageHead("Simulados")
        Card { Empty("timer", "Configure seus estudos primeiro", "Com o edital no lugar, o Estudário monta simulados no estilo da sua banca.", action = { Btn("Configurar estudos", { Router.go(Route.Setup) }, icon = "auto_awesome") }) }
        return
    }
    val readiness = Simulations.readiness(data, competition.id)
    val profile = Simulations.examProfile(data, competition.id)
    val list = data.simulations.filter { it.competitionId == competition.id }.sortedByDescending { it.createdAt }
    val finished = list.filter { it.status == "FINISHED" }
    var editingProfile by remember { mutableStateOf(false) }
    var studiedSize by remember { mutableStateOf(20) }
    // O simulado que a pessoa pediu para descartar; o aviso fica no nível da página (fora dos cartões),
    // senão o Modal herda o transform do cartão e aparece transparente no meio da lista.
    var discarding by remember { mutableStateOf<Simulation?>(null) }
    // As provas do edital (prova oficial + Simuladões do mês); null enquanto busca.
    var exams by remember(competition.id) { mutableStateOf<List<br.com.estudario.web.data.OfficialSimulation>?>(null) }
    LaunchedEffect(competition.id, competition.name) { exams = br.com.estudario.web.data.Catalog.editalSimulations(competition.name) }
    var officialError by remember { mutableStateOf<String?>(null) }
    // No Grátis o simulado vem do plano: na aba, montar prova abre a oferta (o histórico continua aberto).
    val free = br.com.estudario.web.data.LibraryGaps.free()
    var freeOffer by remember { mutableStateOf(false) }
    if (freeOffer) UpgradeModal(UpgradeReason.SIMULATION_TAB) { freeOffer = false }

    // Retoma partes que estavam gerando quando a página foi fechada.
    LaunchedEffect(list.map { it.id to it.status }) {
        if (!Store.demo) list.filter { it.status == "GENERATING" }.forEach { Simulations.generate(it.id) }
    }

    // A posição no ranking de cada Simuladão já entregue (por id da prova).
    val finishedExams = finished.mapNotNull { s -> Simulations.examIdOf(data, s.id)?.let { it to s.id } }
    var rankings by remember(competition.id) { mutableStateOf<Map<String, br.com.estudario.web.data.ExamRanking>>(emptyMap()) }
    LaunchedEffect(finishedExams) {
        if (Store.demo) return@LaunchedEffect
        val found = mutableMapOf<String, br.com.estudario.web.data.ExamRanking>()
        finishedExams.forEach { (examId, simId) -> try { Simulations.ranking(Store.data, simId)?.let { found[examId] = it } } catch (_: Throwable) { } }
        rankings = found
    }

    val scoresAll = finished.sortedBy { it.finishedAt ?: it.createdAt }.mapNotNull { it.scorePercent }
    val best = scoresAll.maxOrNull()
    val lastScore = scoresAll.lastOrNull()

    // 1. Cabeçalho compacto: título, edital, banca e meta, e a faixa de números.
    Div({ classes("gx-panel", "sx-head") }) {
        Div({ classes("sx-head-top") }) {
            Div({ classes("grow") }) {
                Span({ classes("gx-kicker") }) { Text("SALA DE PROVA") }
                H2 { Text("Simulados") }
                P({ classes("gx-topic") }) { Text(competition.name + (profile.board?.let { " · banca $it" } ?: "")) }
            }
            Button({ classes("gx-ghost", "sim-tune"); attr("type", "button"); onClick { editingProfile = true } }) { Icon("tune"); Span { Text("Banca e meta") } }
        }
        Div({ classes("sx-stats") }) {
            SimStat("${finished.size}", if (finished.size == 1) "entregue" else "entregues")
            SimStat(best?.let { "$it%" } ?: "—", "melhor nota")
            SimStat(lastScore?.let { "$it%" } ?: "—", "última · meta ${profile.targetPercent}%", lastScore?.let { if (it >= profile.targetPercent) "good" else "warn" })
            SimStat(if (scoresAll.size >= 2) "${if (scoresAll.last() >= scoresAll.first()) "+" else ""}${scoresAll.last() - scoresAll.first()}" else "—", "pontos desde o 1º")
        }
    }

    val profileRow = data.array("examProfiles").firstOrNull { ((it as? kotlinx.serialization.json.JsonObject)?.get("competitionId") as? kotlinx.serialization.json.JsonPrimitive)?.content?.toLongOrNull() == competition.id } as? kotlinx.serialization.json.JsonObject
    val foundBoard = profile.board?.takeIf { (profileRow?.get("boardPending") as? kotlinx.serialization.json.JsonPrimitive)?.content == "true" }
    if (foundBoard != null) Div({ classes("sim-found") }) {
        Icon("Lupa")
        Div({ classes("grow") }) {
            B { Text("O Estudário encontrou a banca $foundBoard na internet. Está certo?") }
            Div({ classes("row"); attr("style", "gap:8px;margin-top:8px;flex-wrap:wrap") }) {
                Btn("Confirmar", { Store.update { Simulations.saveExamProfile(it, competition.id, foundBoard, profile.targetPercent) } }, style = "primary", small = true, icon = "check")
                Btn("Não é essa", { editingProfile = true }, style = "outline", small = true)
                profileRow?.str("boardUrl")?.let { url -> org.jetbrains.compose.web.dom.A(href = url, { attr("target", "_blank"); attr("rel", "noopener"); classes("btn", "ghost", "small") }) { Text("Ver fonte") } }
            }
        }
    }

    // 2. Continuar: o simulado mais recente ainda não entregue, em destaque.
    list.firstOrNull { isPending(it) }?.let { current ->
        val parts = Simulations.decodeParts(Simulations.row(data, current.id)?.str("partsJson"))
        Div({ classes("sx-continue") }) {
            Span({ classes("sx-continue-ic") }) { Icon(if (current.status == "GENERATING") "hourglass_top" else "play_circle", filled = true) }
            Div({ classes("grow") }) {
                Span({ classes("gx-kicker") }) { Text("CONTINUAR") }
                B { Text(current.title) }
                P { Text(pendingStatus(current, parts.count { it.status == "DONE" }, parts.size) + " · ${current.timeLimitMinutes} min") }
            }
            GxLaunch(if (current.status == "IN_PROGRESS") "Continuar" else "Abrir", icon = "play_circle") { Router.go(Route.Simulation(current.id)) }
        }
    }

    if (free) {
        // 6. Grátis: o simulado vem no plano; a aba mostra a oferta dos pagos.
        Div({ classes("sx-note") }) {
            Icon("event_note")
            Div({ classes("grow") }) {
                B { Text("Seu simulado vem no plano de estudos") }
                P { Text("Ele aparece como tarefa no dia certo e fica no histórico abaixo.") }
            }
            Btn("Abrir o plano", { Router.go(Route.Plan) }, style = "outline", small = true, icon = "arrow_forward")
        }
        Button({ classes("sx-upsell"); attr("type", "button"); onClick { freeOffer = true } }) {
            Span({ classes("sx-upsell-ic") }) { Icon("workspace_premium", filled = true) }
            Div({ classes("grow") }) {
                B { Text("Simuladão do edital com ranking") }
                P { Text("A mesma prova para todos do seu edital, uma nova por mês, e veja à frente de quantos você ficou. Também: simulados do que você estudou, quando quiser.") }
                Span({ classes("sx-upsell-cta") }) { Text("Conhecer os planos"); Icon("arrow_forward") }
            }
        }
    } else {
        // 3. Simuladão do edital: a prova oficial (nº 1) e um Simuladão novo por mês, igual para todos e com ranking.
        Div({ classes("gx-panel", "sx-panel") }) {
            SimSection("SIMULADÃO DO EDITAL", "Provas do edital", "A mesma prova para todos do seu edital, com ranking. Uma nova por mês.")
            if (exams == null) P({ classes("gx-note") }) { Text("Buscando as provas do edital…") }
            else if (exams.isNullOrEmpty()) P({ classes("gx-note") }) { Text("Este edital ainda não tem Simuladão pronto. Ele aparece aqui assim que sair.") }
            else Div({ classes("sx-list") }) {
                exams.orEmpty().forEach { prova ->
                    androidx.compose.runtime.key(prova.id) {
                        val taken = list.firstOrNull { Simulations.examIdOf(data, it.id) == prova.id }
                        val minutes = BoardStyle.minutesFor(if (prova.format == "CERTO_ERRADO") "TRUE_FALSE" else "FIVE_OPTIONS", prova.questions.size)
                        Div({ classes("sx-exam") }) {
                            Div({ classes("grow", "sx-exam-info") }) {
                                B { Text(Simulations.examTitle(prova.number)) }
                                Span({ classes("sx-meta") }) {
                                    Text("${prova.questions.size} questões · $minutes min" + (profile.board?.let { " · $it" } ?: ""))
                                }
                                if (prova.format == "CERTO_ERRADO") Span({ classes("sx-meta") }) { Text("Regra Cebraspe: cada item errado anula um certo") }
                            }
                            Div({ classes("sx-exam-act") }) {
                                when {
                                    taken == null -> Btn("Fazer a prova", {
                                        try {
                                            var id = 0L
                                            Store.update { val (next, created) = Simulations.createOfficial(it, competition.id, prova); id = created; next }
                                            Router.go(Route.Simulation(id))
                                        } catch (e: Throwable) { officialError = e.message }
                                    }, icon = "play_circle", enabled = !Store.demo)
                                    taken.status == "FINISHED" -> {
                                        val score = taken.scorePercent ?: 0
                                        Div({ classes("sx-exam-score") }) {
                                            Span({ classes("sx-badge", if (score >= profile.targetPercent) "good" else "warn") }) { Text("$score%") }
                                            rankings[prova.id]?.aheadPercent?.let { Span({ classes("sx-meta") }) { Text("à frente de $it%") } }
                                        }
                                        Btn("Ver resultado", { Router.go(Route.Simulation(taken.id)) }, style = "outline", small = true)
                                    }
                                    else -> Btn("Continuar", { Router.go(Route.Simulation(taken.id)) }, icon = "play_circle", small = true)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Do que você estudou: só o banco e as questões da própria pessoa.
        val lock = SimulationUnlock.lock(SimulationMode.STUDIED, readiness)
        Div({ classes("gx-panel", "sx-panel") }) {
            SimSection("DO QUE VOCÊ ESTUDOU", "Simulado dos seus tópicos", "Questões do banco e as suas, só do que você já estudou. Fica pronto na hora.")
            if (lock.unlocked) Div({ classes("sx-build") }) {
                FilterChips(listOf(20 to "20 questões", 30 to "30 questões"), studiedSize) { studiedSize = it }
                Btn("Montar", {
                    try {
                        var id = 0L
                        Store.update { val (next, created) = Simulations.createStudied(it, competition.id, studiedSize); id = created; next }
                        Simulations.generate(id)
                        Router.go(Route.Simulation(id))
                    } catch (e: Throwable) { officialError = e.message }
                }, icon = "play_circle", enabled = !Store.demo)
            } else Div({ classes("sim-lock") }) {
                Div({ classes("sim-lock-bar"); attr("style", "--c:#2FD39A") }) { Span({ attr("style", "width:${(lock.progress * 100).toInt().coerceIn(0, 100)}%") }) }
                Span { Icon("lock"); Text(" " + lock.requirement) }
            }
        }
    }
    officialError?.let { Div({ classes("banner", "error") }) { Text(it) } }
    if (Store.demo) Div({ classes("banner", "info") }) { Icon("info"); Text("Na demonstração os simulados não são gerados. Entre com sua conta para montar o seu.") }

    if (scoresAll.size >= 2) Div({ classes("gx-panel", "sx-panel") }) {
        SimSection("SUA EVOLUÇÃO", scoresAll.let { val first = it.first(); val last = it.last(); when { last > first -> "Você subiu ${last - first} pontos desde o primeiro simulado."; last < first -> "Caiu ${first - last} pontos desde o primeiro. A revisão ajuda a recuperar."; else -> "Nota estável desde o primeiro simulado." } })
        EvolutionChart(scoresAll, profile.targetPercent)
        P({ classes("gx-note") }) { Text("Linha tracejada: sua meta de ${profile.targetPercent}%") }
    }

    // 5. Histórico: todos os simulados, do mais novo ao mais antigo.
    Div({ classes("gx-panel", "sx-panel") }) {
        SimSection("HISTÓRICO", "Seus simulados")
        if (list.isEmpty()) P({ classes("gx-note") }) { Text(if (free) "Quando você fizer o simulado do plano, ele aparece aqui." else "Nenhum simulado ainda. Comece por uma prova do edital.") }
        else Div({ classes("sx-list") }) {
            list.forEach { simulation -> androidx.compose.runtime.key(simulation.id) { SimulationRow(simulation, profile.targetPercent) { discarding = simulation } } }
        }
    }

    discarding?.let { simulation ->
        Modal(onDismiss = { discarding = null }) {
            H2 { Text("Descartar este simulado?") }
            P({ classes("muted") }) { Text(if (simulation.mode == Simulations.OFFICIAL_MODE) "As respostas dele somem e a prova volta a aparecer no Simuladão do edital. No ranking, vale só a primeira entrega." else "As questões e as respostas dele somem. Não dá para desfazer.") }
            Div({ classes("row", "end", "sx-modal-actions") }) {
                Btn("Manter", { discarding = null }, style = "ghost")
                Btn("Descartar", { discarding = null; Store.update { Simulations.delete(it, simulation.id) } }, style = "danger", icon = "delete")
            }
        }
    }
    if (editingProfile) ProfileDialog(competition.id, profile.board, profile.targetPercent) { editingProfile = false }
}

/** Uma linha do histórico: abre o simulado; a lixeira fica dentro da linha. */
@Composable
private fun SimulationRow(simulation: Simulation, target: Int, onDiscard: () -> Unit) {
    val parts = Simulations.decodeParts(Simulations.row(Store.data, simulation.id)?.str("partsJson"))
    Div({ classes("sx-row") }) {
        Button({ classes("sx-row-main"); attr("type", "button"); onClick { Router.go(Route.Simulation(simulation.id)) } }) {
            Div({ classes("sx-row-body") }) {
                B { Text(simulation.title) }
                Span({ classes("sx-meta") }) {
                    Text("${Queries.shortDate(Queries.dateOf(simulation.createdAt))} · ${simulation.timeLimitMinutes} min")
                    if (simulation.status != "FINISHED") Text(" · " + pendingStatus(simulation, parts.count { it.status == "DONE" }, parts.size))
                }
            }
            when (simulation.status) {
                "FINISHED" -> { val s = simulation.scorePercent ?: 0; Span({ classes("sx-badge", if (s >= target) "good" else "warn") }) { Text("$s%") } }
                "FAILED" -> Span({ classes("sx-badge", "bad") }) { Text("Falhou") }
                else -> Span({ classes("sx-badge", "info") }) { Text(if (simulation.status == "IN_PROGRESS") "Em andamento" else if (simulation.status == "GENERATING") "Montando" else "Pronto") }
            }
        }
        IconButton("delete", "Descartar simulado") { onDiscard() }
    }
}

/** A posição no ranking do Simuladão (igual ao RankingCard do app). */
@Composable
private fun RankingCard(ranking: br.com.estudario.web.data.ExamRanking) {
    Div({ classes("gx-panel", "sim-panel") }) {
        Div({ classes("sim-panel-head") }) {
            Span({ classes("gx-kicker") }) { Text("RANKING DO SIMULADÃO") }
            val ahead = ranking.aheadPercent
            B { Text(if (ranking.participants <= 1) "Ninguém tinha feito esta prova antes de você" else if (ahead != null) "À frente de $ahead% dos alunos" else "${ranking.participants} alunos fizeram") }
        }
        P({ classes("gx-note") }) { Text("${ranking.participants} aluno(s) do Estudário fizeram esta prova · média de ${ranking.average}%" + (ranking.score?.let { " · sua nota: $it%" } ?: "")) }
    }
}

/** A linha das notas dos simulados, com a meta tracejada (como no app). */
@Composable
private fun EvolutionChart(scores: List<Int>, target: Int) {
    val w = 600.0; val h = 140.0
    val step = if (scores.size > 1) w / (scores.size - 1) else w
    fun y(v: Int) = h - v / 100.0 * h
    val points = scores.mapIndexed { i, v -> "${(i * step).toInt()},${y(v).toInt()}" }.joinToString(" ")
    val svg = """<svg viewBox="-8 -8 616 156" preserveAspectRatio="none" style="width:100%;height:150px;display:block">
        <line x1="0" x2="600" y1="${y(50)}" y2="${y(50)}" stroke="var(--line)" stroke-width="1"/>
        <line x1="0" x2="600" y1="${y(target)}" y2="${y(target)}" stroke="#1FA97F" stroke-width="3" stroke-dasharray="12 10"/>
        <polyline points="$points" fill="none" stroke="var(--primary)" stroke-width="5" stroke-linecap="round" stroke-linejoin="round" vector-effect="non-scaling-stroke"/>
        ${scores.mapIndexed { i, v -> "<circle cx='${(i * step).toInt()}' cy='${y(v).toInt()}' r='6' fill='var(--primary)'/>" }.joinToString("")}
    </svg>"""
    androidx.compose.runtime.key(svg) { Div({ ref { el -> el.innerHTML = svg; onDispose { } } }) }
}

@Composable
private fun CreateSimulationDialog(competitionId: Long, mode: SimulationMode, onClose: () -> Unit) {
    val sizes = SimulationUnlock.sizes(mode, null)
    var size by remember { mutableStateOf(sizes.first()) }
    var error by remember { mutableStateOf<String?>(null) }
    val profile = Simulations.examProfile(Store.data, competitionId)
    Modal(onDismiss = onClose) {
        Div {
            Span({ classes("eyebrow") }) { Text("Novo simulado") }
            H2 { Text(mode.title) }
            P({ classes("small", "muted") }) { Text(mode.description) }
        }
        if (sizes.size > 1) Div({ classes("field") }) {
            org.jetbrains.compose.web.dom.Label { Text("Tamanho") }
            FilterChips(sizes.map { it to "$it questões" }, size) { size = it }
        }
        Div({ classes("card", "soft") }) {
            Div({ classes("stack", "tight") }) {
                Div({ classes("row", "between") }) { Span({ classes("small") }) { Text("Banca") }; B({ classes("small") }) { Text(profile.board ?: "a IA procura a banca do edital") } }
                Div({ classes("row", "between") }) { Span({ classes("small") }) { Text("Formato") }; B({ classes("small") }) { Text(BoardStyle.styleLabel(profile.style)) } }
                Div({ classes("row", "between") }) { Span({ classes("small") }) { Text("Tempo de prova") }; B({ classes("small") }) { Text("${BoardStyle.minutesFor(profile.style, if (mode == SimulationMode.REMATCH) 30 else size)} min") } }
            }
        }
        P({ classes("xs", "muted") }) { Text("Com as questões prontas da biblioteca, o simulado fica pronto em segundos; tópicos sem questão pronta podem levar alguns minutos. Pode navegar pelo site enquanto isso; deixe esta aba aberta.") }
        error?.let {
            Div({ classes("banner", "error") }) { Text(it) }
            var upgradeSeen by remember(it) { mutableStateOf(false) }
            if (isQuotaMessage(it) && !upgradeSeen) UpgradeModal(UpgradeReason.SIMULATION) { upgradeSeen = true }
        }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Gerar simulado", {
                try {
                    var id = 0L
                    Store.update { val (next, created) = Simulations.create(it, competitionId, mode, size); id = created; next }
                    Simulations.generate(id)
                    onClose()
                    Router.go(Route.Simulation(id))
                } catch (e: Throwable) { error = e.message }
            }, icon = "auto_awesome")
        }
    }
}

@Composable
private fun ProfileDialog(competitionId: Long, board: String?, target: Int, onClose: () -> Unit) {
    var name by remember { mutableStateOf(board.orEmpty()) }
    var percent by remember { mutableStateOf(target) }
    Modal(onDismiss = onClose) {
        H2 { Text("Banca e meta") }
        Div({ classes("field") }) {
            org.jetbrains.compose.web.dom.Label { Text("Banca do concurso") }
            Input(InputType.Text) { classes("input"); value(name); placeholder("Ex.: Cebraspe, FGV, FCC, Vunesp"); attr("maxlength", "40"); onInput { name = it.value } }
            Span({ classes("xs", "muted") }) { Text("Formato das questões: ${BoardStyle.styleLabel(BoardStyle.styleFor(name.ifBlank { null }))}") }
        }
        Div({ classes("day-row") }) {
            Div({ classes("grow") }) { Div({ classes("name") }) { Text("Meta de acerto") }; Div({ classes("hint") }) { Text("Usada para dizer se o simulado passou") } }
            Stepper(percent, { percent = it }, step = 5, min = 30, max = 100, format = { "$it%" }, label = "Meta de acerto")
        }
        Div({ classes("row", "end") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Salvar", { Store.update { Simulations.saveExamProfile(it, competitionId, name, percent) }; onClose() }, icon = "check")
        }
    }
}

// ------------------------------------------------------------------ um simulado

@Composable
fun SimulationScreen(id: Long) {
    val data = Store.data
    val simulation = data.simulations.firstOrNull { it.id == id }
    if (simulation == null) {
        Card { Empty("timer", "Simulado não encontrado", "Ele pode ter sido apagado.", action = { Btn("Simulados", { Router.go(Route.Simulations) }) }) }
        return
    }
    when (simulation.status) {
        "GENERATING", "FAILED" -> GeneratingView(simulation)
        "READY", "PARTIAL" -> ReadyView(simulation)
        "IN_PROGRESS" -> ExamView(simulation)
        else -> ResultView(simulation)
    }
}

@Composable
private fun GeneratingView(simulation: Simulation) {
    val parts = Simulations.decodeParts(Simulations.row(Store.data, simulation.id)?.str("partsJson"))
    LaunchedEffect(simulation.id) { if (simulation.status == "GENERATING" && !Store.demo) Simulations.generate(simulation.id) }
    Div({ classes("row") }) { Btn("Simulados", { Router.go(Route.Simulations) }, style = "ghost", small = true, icon = "arrow_back") }
    Card(extra = "pad-lg") {
        Div({ classes("stack"); attr("style", "align-items:center;text-align:center") }) {
            if (simulation.status == "FAILED" || parts.any { it.status == "FAILED" }) Icon("error", extraClass = "big") else Spinner()
            H2 { Text(simulation.title) }
            P({ classes("muted") }) {
                Text(if (simulation.status == "FAILED") "Não deu para gerar. Nenhuma cota foi usada nas partes que falharam." else "Montando sua prova com questões que você ainda não viu, no estilo da banca. Costuma levar segundos; tópicos sem questão pronta podem levar alguns minutos. Deixe esta aba aberta.")
            }
            Div({ classes("stack", "tight"); attr("style", "width:100%;max-width:420px") }) {
                parts.forEachIndexed { index, part ->
                    Div({ classes("row", "between") }) {
                        Span({ classes("small") }) { Text("Parte ${index + 1}") }
                        when (part.status) {
                            "DONE" -> Chip("Pronta", "green", icon = "check")
                            "FAILED" -> Chip("Falhou", "red")
                            "RUNNING" -> Chip("Gerando", "primary")
                            else -> Chip("Na fila")
                        }
                    }
                }
            }
            parts.firstOrNull { it.error != null }?.error?.let { Div({ classes("banner", "error") }) { Text(it) } }
            if (parts.any { it.status == "FAILED" } && !Simulations.isRunning(simulation.id)) Btn("Tentar de novo as partes que falharam", { Simulations.retryFailed(simulation.id) }, icon = "refresh")
            if (simulation.status == "FAILED") Btn("Apagar", { Store.update { Simulations.delete(it, simulation.id) }; Router.go(Route.Simulations) }, style = "ghost")
        }
    }
}

@Composable
private fun ReadyView(simulation: Simulation) {
    val count = Store.data.questions.count { it.simulationId == simulation.id }
    Div({ classes("row") }) { Btn("Simulados", { Router.go(Route.Simulations) }, style = "ghost", small = true, icon = "arrow_back") }
    Div({ classes("hero") }) {
        Div({ classes("stack") }) {
            Span({ classes("eyebrow"); attr("style", "color:rgba(255,255,255,.8)") }) { Text(if (simulation.status == "PARTIAL") "Quase pronto" else "Pronto") }
            org.jetbrains.compose.web.dom.H1 { Text(simulation.title) }
            P({ classes("muted") }) { Text("$count questões · ${simulation.timeLimitMinutes} minutos. O cronômetro começa quando você iniciar; dá para pausar saindo da página.") }
            Div({ classes("row", "wrap") }) {
                Btn("Começar a prova", { Store.update { Simulations.start(it, simulation.id) } }, style = "white", icon = "play_arrow")
                if (simulation.status == "PARTIAL") Btn("Gerar o que faltou", { Simulations.retryFailed(simulation.id); Router.go(Route.Simulation(simulation.id)) }, style = "outline", icon = "refresh")
            }
        }
    }
}

@Composable
private fun ExamView(simulation: Simulation) {
    HoldCelebrations()
    val questions = Store.data.questions.filter { it.simulationId == simulation.id }.sortedBy { it.id }
    val row = Simulations.row(Store.data, simulation.id)
    var answers by remember(simulation.id) { mutableStateOf(Simulations.answersOf(row)) }
    var flagged by remember(simulation.id) { mutableStateOf(Simulations.flaggedOf(row)) }
    var index by remember(simulation.id) { mutableStateOf(0) }
    val openedAt = remember(simulation.id) { Date.now().toLong() }
    val baseElapsed = remember(simulation.id) { (row?.get("elapsedSeconds") as? kotlinx.serialization.json.JsonPrimitive)?.content?.toLongOrNull() ?: 0L }
    var now by remember { mutableStateOf(Date.now().toLong()) }
    var confirm by remember { mutableStateOf(false) }
    DisposableEffect(simulation.id) {
        val handle = window.setInterval({ now = Date.now().toLong() }, 1000)
        onDispose { window.clearInterval(handle) }
    }
    val elapsed = baseElapsed + (now - openedAt) / 1000
    val remaining = (simulation.timeLimitMinutes * 60L - elapsed).coerceAtLeast(0)
    fun save() = Store.update { Simulations.saveProgress(it, simulation.id, answers, flagged, elapsed) }
    fun submit() { Store.update { Simulations.submit(it, simulation.id, answers, elapsed) }; Toast.show("Simulado entregue!") }
    // Salva o andamento a cada 30 segundos e ao sair.
    DisposableEffect(simulation.id) {
        val handle = window.setInterval({ save() }, 30_000)
        onDispose { window.clearInterval(handle); save() }
    }
    if (remaining == 0L && questions.isNotEmpty()) { submit(); return }
    if (questions.isEmpty()) { Card { Empty("timer", "Sem questões", "Este simulado não tem questões prontas.") }; return }
    val question = questions[index.coerceIn(0, questions.lastIndex)]
    Div({ classes("row", "between", "wrap") }) {
        Div({ classes("row") }) {
            Chip("${answers.size}/${questions.size} respondidas", "primary")
            if (flagged.isNotEmpty()) Chip("${flagged.size} para revisar", "amber", icon = "flag")
        }
        Div({ classes("row") }) {
            Span({ classes("strong"); attr("style", "font-variant-numeric:tabular-nums;font-size:20px;color:${if (remaining < 300) "var(--red)" else "var(--ink)"}") }) {
                Text("${remaining / 3600}:${((remaining % 3600) / 60).toString().padStart(2, '0')}:${(remaining % 60).toString().padStart(2, '0')}")
            }
            Btn("Entregar", { confirm = true }, small = true, icon = "send")
        }
    }
    Card(extra = "pad-lg") {
        Div({ classes("stack") }) {
            Div({ classes("row", "between") }) {
                B { Text("Questão ${index + 1} de ${questions.size}") }
                IconButton(if (question.id in flagged) "flag" else "outlined_flag", "Marcar para revisar") {
                    flagged = if (question.id in flagged) flagged - question.id else flagged + question.id
                }
            }
            QuestionStatement(question.statement)
            Div({ classes("stack", "tight") }) {
                question.options.sortedBy { it.position }.forEach { option ->
                    androidx.compose.runtime.key(question.id, option.key) {
                        Button({
                            classes(*listOfNotNull("option", if (answers[question.id] == option.key) "chosen" else null).toTypedArray())
                            onClick { answers = answers + (question.id to option.key) }
                        }) {
                            Span({ classes("key") }) { Text(option.key) }
                            Span { Inline(option.text) }
                        }
                    }
                }
            }
        }
    }
    Div({ classes("row", "between") }) {
        Btn("Anterior", { index = (index - 1).coerceAtLeast(0) }, style = "outline", icon = "arrow_back", enabled = index > 0)
        Btn(if (index < questions.lastIndex) "Próxima" else "Revisar e entregar", { if (index < questions.lastIndex) index++ else confirm = true }, icon = "arrow_forward")
    }
    Card {
        CardHead("Mapa da prova")
        Div({ attr("style", "display:grid;grid-template-columns:repeat(auto-fill,minmax(40px,1fr));gap:6px") }) {
            questions.forEachIndexed { i, q ->
                val style = when {
                    i == index -> "background:var(--primary);color:var(--on-primary)"
                    q.id in flagged -> "background:var(--amber-soft);color:var(--on-amber-soft)"
                    q.id in answers -> "background:var(--primary-soft);color:var(--on-primary-soft)"
                    else -> "background:var(--soft-2)"
                }
                Button({ attr("style", "border:0;border-radius:10px;height:40px;font-weight:800;cursor:pointer;$style"); onClick { index = i } }) { Text("${i + 1}") }
            }
        }
    }
    if (confirm) Modal(onDismiss = { confirm = false }) {
        H2 { Text("Entregar o simulado?") }
        P({ classes("muted") }) {
            val blank = questions.size - answers.size
            Text(if (blank > 0) "Você deixou $blank questão(ões) em branco. Questão em branco conta como erro." else "Todas respondidas. Depois de entregar não dá para mudar as respostas.")
        }
        Div({ classes("row", "end") }) {
            Btn("Continuar a prova", { confirm = false }, style = "ghost")
            Btn("Entregar", { confirm = false; submit() }, icon = "send")
        }
    }
}

@Composable
private fun ResultView(simulation: Simulation) {
    val data = Store.data
    val target = Simulations.examProfile(data, simulation.competitionId).targetPercent
    val score = simulation.scorePercent ?: 0
    val scores = Simulations.subjectScores(data, simulation.id)
    val answers = Simulations.answersOf(Simulations.row(data, simulation.id))
    val questions = data.questions.filter { it.simulationId == simulation.id }.sortedBy { it.id }
    var reviewWrongOnly by remember { mutableStateOf(true) }
    // Regra Cebraspe nos itens Certo/Errado: certo +1, errado −1, em branco 0.
    val ceItems = questions.filter { q -> q.options.map { it.key }.toSet() == setOf("C", "E") }
    val ceRight = ceItems.count { q -> answers[q.id] == q.options.firstOrNull { it.correct }?.key }
    val ceWrong = ceItems.count { q -> q.id in answers && answers[q.id] != q.options.firstOrNull { it.correct }?.key }
    val reading = SimulationInsights.read(questions.map { q ->
        SimulationInsights.Answered(q.topicId, q.difficulty, q.explanation, answers[q.id]?.let { it == q.options.firstOrNull { o -> o.correct }?.key })
    })
    Div({ classes("row") }) { Btn("Simulados", { Router.go(Route.Simulations) }, style = "ghost", small = true, icon = "arrow_back") }
    Card(extra = "pad-lg") {
        Div({ classes("mission") }) {
            Ring(score / 100.0) { B { Text("$score%") }; Span { Text("de acerto") } }
            Div({ classes("stack", "tight", "grow") }) {
                H2 { Text(if (score >= target) "Passou da meta!" else "Abaixo da meta de $target%") }
                P({ classes("muted") }) { Text("${simulation.correctCount} de ${simulation.questionCount} certas · ${simulation.title}") }
                if (ceItems.isNotEmpty()) P({ classes("small") }) {
                    B { Text("Regra Cebraspe: ${ceRight - ceWrong} ponto(s) líquido(s) em ${ceItems.size} itens. ") }
                    Text("Cada erro anula um acerto: na dúvida real, deixar em branco pode valer mais.")
                }
                // Simulado mede: as erradas ficam aqui no resultado, fora do caderno de erros (sem Revanche).
                P({ classes("small", "muted") }) { Text("Veja abaixo a explicação das que você errou e revise os tópicos mais fracos.") }
            }
        }
    }
    // Ranking do Simuladão: manda a entrega ao abrir o resultado (só a primeira conta no servidor).
    var ranking by remember(simulation.id) { mutableStateOf<br.com.estudario.web.data.ExamRanking?>(null) }
    LaunchedEffect(simulation.id) { if (!Store.demo) ranking = Simulations.ranking(Store.data, simulation.id) }
    ranking?.takeIf { it.participants > 0 }?.let { RankingCard(it) }
    Div({ classes("grid", "cols-2") }) {
        Card {
            CardHead("Por matéria")
            Div({ classes("stack") }) {
                scores.forEach { s ->
                    Div({ classes("stack", "tight") }) {
                        Div({ classes("row", "between") }) { B({ classes("small", "clamp-2") }) { Text(s.subjectName) }; Span({ classes("small", "muted", "nowrap") }) { Text("${s.correct}/${s.total} · ${s.percent}%") } }
                        ProgressBar(s.percent / 100.0, if (s.percent >= target) "green" else null)
                    }
                }
            }
        }
        Card {
            CardHead("Onde você perdeu pontos")
            Div({ classes("stack") }) {
                if (reading.levels.size > 1) B({ classes("small") }) { Text(reading.levels.joinToString(" · ") { "${it.label} ${it.right}/${it.total}" }) }
                reading.advice?.let { P({ classes("small") }) { Text(it) } }
                reading.lostTopics.forEach { lost ->
                    val topic = data.topics.firstOrNull { it.id == lost.topicId }
                    Div({ classes("stack", "tight") }) {
                        Div({ classes("row", "between") }) {
                            B({ classes("small", "clamp-2") }) { Text(topic?.title ?: "Tópico") }
                            Span({ classes("small", "nowrap"); attr("style", "color:var(--danger)") }) { Text("−${lost.lost} de ${lost.total}") }
                        }
                        lost.trap?.let { P({ classes("xs", "muted") }) { Text("Armadilha: $it") } }
                        topic?.let { t -> Div({ classes("row") }) { Btn("Estudar o tópico", { Router.go(Route.Topic(t.id)) }, style = "ghost", small = true) } }
                    }
                }
                if (reading.lostTopics.isEmpty()) P({ classes("small", "muted") }) { Text("Nenhum ponto perdido. Prova fechada!") }
                P({ classes("xs", "muted") }) { Text("Tempo de prova: ${simulation.timeLimitMinutes} min · entregue em ${Queries.shortDate(Queries.dateOf(simulation.finishedAt ?: simulation.createdAt))}.") }
            }
        }
    }
    Card {
        CardHead("Correção") {
            Div({ classes("segmented") }) {
                listOf(true to "Só as erradas", false to "Todas").forEach { (key, label) ->
                    Button({ classes(*listOfNotNull(if (reviewWrongOnly == key) "on" else null).toTypedArray()); onClick { reviewWrongOnly = key } }) { Text(label) }
                }
            }
        }
        Div({ classes("stack", "loose") }) {
            questions.forEachIndexed { i, q ->
                val correctKey = q.options.firstOrNull { it.correct }?.key
                val chosen = answers[q.id]
                if (reviewWrongOnly && chosen == correctKey) return@forEachIndexed
                androidx.compose.runtime.key(q.id) {
                    Div({ classes("stack", "tight"); attr("style", "border-top:1px solid var(--line);padding-top:14px") }) {
                        B { Text("Questão ${i + 1}") }
                        QuestionStatement(q.statement); StatementSource(q.statement)
                        q.options.sortedBy { it.position }.forEach { o ->
                            Div({ classes(*listOfNotNull("option", if (o.key == correctKey) "right" else if (o.key == chosen) "wrong" else null).toTypedArray()) }) {
                                Span({ classes("key") }) { Text(o.key) }
                                Span { Inline(o.text) }
                            }
                        }
                        if (q.explanation.isNotBlank()) Div({ classes("card", "soft") }) { Markdown(q.explanation) }
                    }
                }
            }
        }
    }
}
