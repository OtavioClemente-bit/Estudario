package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.simulation.BoardStyle
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
    SimulationMode.DIAGNOSTIC -> "explore"
    SimulationMode.STUDIED -> "fact_check"
    SimulationMode.FULL -> "workspace_premium"
    SimulationMode.REMATCH -> "sports_mma"
}

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
    var creating by remember { mutableStateOf<SimulationMode?>(null) }
    var editingProfile by remember { mutableStateOf(false) }

    // Retoma partes que estavam gerando quando a página foi fechada.
    LaunchedEffect(list.map { it.id to it.status }) {
        if (!Store.demo) list.filter { it.status == "GENERATING" }.forEach { Simulations.generate(it.id) }
    }

    PageHead("Simulados", "Provas inéditas no estilo da sua banca, com tempo de prova") {
        Btn("Banca e meta", { editingProfile = true }, style = "outline", icon = "tune", small = true)
    }

    if (finished.isNotEmpty()) Div({ classes("grid", "cols-3") }) {
        Card(extra = "flat") { Stat("${finished.size}", "simulados entregues") }
        Card(extra = "flat") { Stat("${finished.mapNotNull { it.scorePercent }.maxOrNull() ?: 0}%", "melhor nota") }
        Card(extra = "flat") { Stat("${profile.targetPercent}%", "sua meta de acerto") }
    }

    Div({ classes("grid", "cols-2") }) {
        SimulationMode.entries.forEach { mode ->
            androidx.compose.runtime.key(mode) {
                val lock = SimulationUnlock.lock(mode, readiness)
                Div({ classes("card"); attr("style", if (lock.unlocked) "" else "opacity:.9") }) {
                    Div({ classes("row") }) {
                        Span({ classes("task-mark"); attr("style", if (lock.unlocked) "background:var(--primary-soft);color:var(--on-primary-soft)" else "background:var(--soft-2);color:var(--faint)") }) { Icon(if (lock.unlocked) modeIcon(mode) else "lock") }
                        Div({ classes("grow") }) { H3 { Text(mode.title) }; P({ classes("small", "muted") }) { Text(mode.description) } }
                    }
                    Div({ attr("style", "margin-top:14px") }) {
                        if (lock.unlocked) Btn("Montar simulado", { creating = mode }, style = "tonal", icon = "add", block = true, enabled = !Store.demo)
                        else Div({ classes("stack", "tight") }) {
                            ProgressBar(lock.progress.toDouble())
                            Span({ classes("xs", "muted") }) { Text(lock.requirement) }
                        }
                    }
                }
            }
        }
    }
    if (Store.demo) Div({ classes("banner", "info") }) { Icon("info"); Text("Na demonstração os simulados não são gerados. Entre com sua conta para montar o seu.") }

    if (list.isNotEmpty()) Card {
        CardHead("Seus simulados")
        Div({ classes("stack") }) {
            list.forEach { simulation -> androidx.compose.runtime.key(simulation.id) { SimulationRow(simulation) } }
        }
    }

    creating?.let { mode -> CreateSimulationDialog(competition.id, mode) { creating = null } }
    if (editingProfile) ProfileDialog(competition.id, profile.board, profile.targetPercent) { editingProfile = false }
}

@Composable
private fun SimulationRow(simulation: Simulation) {
    val parts = Simulations.decodeParts(Simulations.row(Store.data, simulation.id)?.str("partsJson"))
    Button({ classes("task"); onClick { Router.go(Route.Simulation(simulation.id)) } }) {
        Span({ classes("task-mark"); attr("style", "background:var(--primary-soft);color:var(--on-primary-soft)") }) { Icon("timer") }
        Div({ classes("task-body") }) {
            Div({ classes("task-title") }) { Text(simulation.title) }
            Div({ classes("task-meta") }) {
                Text("${Queries.shortDate(Queries.dateOf(simulation.createdAt))} · ${simulation.timeLimitMinutes} min")
                if (simulation.status == "GENERATING") Text(" · ${parts.count { it.status == "DONE" }} de ${parts.size} partes prontas")
            }
        }
        when (simulation.status) {
            "FINISHED" -> Chip("${simulation.scorePercent ?: 0}%", if ((simulation.scorePercent ?: 0) >= 70) "green" else "amber")
            "IN_PROGRESS" -> Chip("Em andamento", "amber")
            "READY" -> Chip("Pronto", "primary")
            "PARTIAL" -> Chip("Quase pronto", "amber")
            "GENERATING" -> Chip("Gerando…", "primary")
            else -> Chip("Falhou", "red")
        }
    }
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
        P({ classes("xs", "muted") }) { Text("A geração leva alguns minutos e acontece em partes. Pode navegar pelo site enquanto isso; deixe esta aba aberta.") }
        error?.let { Div({ classes("banner", "error") }) { Text(it) } }
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
            Input(InputType.Text) { classes("input"); value(name); placeholder("Ex.: Cebraspe, FGV, FCC, Vunesp"); onInput { name = it.value } }
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
                Text(if (simulation.status == "FAILED") "Não deu para gerar. Nenhuma cota foi usada nas partes que falharam." else "A IA está escrevendo questões inéditas no estilo da banca. Leva alguns minutos; deixe esta aba aberta.")
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
            Div({ classes("statement") }) { Markdown(question.statement) }
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
    Div({ classes("row") }) { Btn("Simulados", { Router.go(Route.Simulations) }, style = "ghost", small = true, icon = "arrow_back") }
    Card(extra = "pad-lg") {
        Div({ classes("mission") }) {
            Ring(score / 100.0) { B { Text("$score%") }; Span { Text("de acerto") } }
            Div({ classes("stack", "tight", "grow") }) {
                H2 { Text(if (score >= target) "Passou da meta!" else "Abaixo da meta de $target%") }
                P({ classes("muted") }) { Text("${simulation.correctCount} de ${simulation.questionCount} certas · ${simulation.title}") }
                P({ classes("small", "muted") }) { Text("As erradas foram para o caderno de erros e entram na Revanche.") }
            }
        }
    }
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
            CardHead("Tempo")
            Stat("${simulation.timeLimitMinutes} min", "tempo de prova")
            P({ classes("small", "muted"); attr("style", "margin-top:8px") }) { Text("Entregue em ${Queries.shortDate(Queries.dateOf(simulation.finishedAt ?: simulation.createdAt))}.") }
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
                        Div({ classes("statement") }) { Markdown(q.statement) }
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
