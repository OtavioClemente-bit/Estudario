package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import br.com.estudario.domain.planner.StudyMethodConfig
import br.com.estudario.domain.planner.StudyProfile
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Catalog
import br.com.estudario.web.data.CatalogEntry
import br.com.estudario.web.data.CatalogSubject
import br.com.estudario.web.data.Planning
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Store
import br.com.estudario.web.data.str
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/** Enviar o PDF do edital: a IA monta matérias e tópicos (como "Montar edital com IA" no app). */
@Composable
internal fun PdfEditalDialog(onClose: () -> Unit, onDone: (String, String, List<br.com.estudario.web.data.EditalAi.SubjectNode>) -> Unit) {
    val scope = rememberCoroutineScope()
    var file by remember { mutableStateOf<org.w3c.files.File?>(null) }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var board by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var step by remember { mutableStateOf<br.com.estudario.web.data.EditalAi.Step?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var preview by remember { mutableStateOf<List<br.com.estudario.web.data.EditalAi.SubjectNode>?>(null) }
    val running = step != null && preview == null && error == null
    Modal(onDismiss = { if (!running) onClose() }, wide = true, extraClass = "gen-modal") {
        val result = preview
        if (running) {
            Div({ classes("gen-body", "gen-working") }) {
                ProcessView(
                    title = "Lendo o seu edital",
                    eyebrow = file?.name?.take(50),
                    stages = listOf("Lendo o PDF no seu navegador", "Enviando para o Estudário", "Encontrando o conteúdo programático", "Separando matérias e tópicos", "Conferindo a ordem do edital"),
                    stageIndex = when (step) {
                        br.com.estudario.web.data.EditalAi.Step.Reading -> 0
                        br.com.estudario.web.data.EditalAi.Step.Uploading -> 1
                        else -> null
                    },
                    stageOffset = 2,
                    stageMillis = 12_000,
                    sceneSize = 260,
                    scene = "edital",
                    footer = { P({ classes("small", "muted"); attr("style", "text-align:center") }) { Text("Pode levar alguns minutos. Deixe esta aba aberta.") } },
                )
            }
            return@Modal
        }
        Div({ classes("gen-head") }) {
            Div({ classes("gen-head-row") }) {
                Div({ classes("gen-head-title") }) {
                    Span({ classes("gen-eyebrow") }) { Text(if (result != null) "Confira o que eu encontrei" else "Edital pelo PDF") }
                    org.jetbrains.compose.web.dom.H2 { Text("Montar edital com IA") }
                    P({ classes("gen-topic") }) {
                        Text(if (result != null) "${result.size} matérias e ${result.sumOf { s -> s.topics.size }} tópicos, na ordem do edital." else "Eu leio o conteúdo programático e monto as matérias e os tópicos na ordem do edital.")
                    }
                }
                IconButton("close", "Fechar", onClose)
            }
        }
        if (result != null) {
            Div({ classes("gen-body") }) {
                Div({ classes("pdf-result") }) {
                    result.forEachIndexed { i, s ->
                        Div({ classes("pdf-subject") }) {
                            Span({ classes("pdf-num") }) { Text("${i + 1}") }
                            Div({ classes("grow") }) {
                                org.jetbrains.compose.web.dom.B { Text(s.name) }
                                P({ classes("small", "muted") }) { Text(s.topics.joinToString(" · ") { it.name }.take(400)) }
                            }
                            Span({ classes("pdf-count") }) { Text("${s.topics.size} tópicos") }
                        }
                    }
                }
            }
            Div({ classes("gen-foot") }) {
                Btn("Cancelar", onClose, style = "ghost")
                Btn("Usar este edital", { onDone(name.ifBlank { file?.name?.substringBeforeLast('.') ?: "Meu concurso" }, role, result) }, icon = "check")
            }
            return@Modal
        }
        Div({ classes("gen-body") }) {
            // Área de soltar o PDF (ou clicar para escolher), no lugar do campo de arquivo cru.
            var dragging by remember { mutableStateOf(false) }
            Label(attrs = {
                classes(*listOfNotNull("pdf-drop", if (file != null) "has-file" else null, if (dragging) "dragging" else null).toTypedArray())
                addEventListener("dragover") { e -> e.preventDefault(); dragging = true }
                addEventListener("dragleave") { dragging = false }
                addEventListener("drop") { e ->
                    e.preventDefault(); dragging = false
                    (e.nativeEvent.asDynamic().dataTransfer?.files?.item(0) as? org.w3c.files.File)?.takeIf { it.name.lowercase().endsWith(".pdf") }?.let { file = it }
                }
            }) {
                Input(InputType.File) {
                    classes("pdf-input"); attr("accept", "application/pdf")
                    onChange { event -> file = (event.target.asDynamic().files?.item(0)) as? org.w3c.files.File }
                }
                Span({ classes("pdf-ico") }) { Icon(if (file != null) "Check" else "Doc") }
                org.jetbrains.compose.web.dom.B { Text(file?.name ?: "Solte o PDF do edital aqui") }
                Span({ classes("small", "muted") }) { Text(if (file != null) "Clique para trocar o arquivo" else "ou clique para escolher. Use o edital oficial, com o conteúdo programático.") }
            }
            Div({ classes("field") }) { Label { Text("Nome do concurso") }; Input(InputType.Text) { classes("input"); value(name); placeholder("Ex.: TJSP Escrevente 2026"); attr("maxlength", "120"); onInput { name = it.value } } }
            Div({ classes("grid", "cols-2") }) {
                Div({ classes("field") }) { Label { Text("Cargo (opcional)") }; Input(InputType.Text) { classes("input"); value(role); attr("maxlength", "80"); onInput { role = it.value } } }
                Div({ classes("field") }) { Label { Text("Banca (opcional)") }; Input(InputType.Text) { classes("input"); value(board); attr("maxlength", "40"); onInput { board = it.value } } }
            }
            error?.let {
                Div({ classes("banner", "error") }) { Text(it) }
                var upgradeSeen by remember(it) { mutableStateOf(false) }
                // Edital pelo PDF sem saldo (no Grátis, sempre): a oferta certa é a do edital.
                if (isQuotaMessage(it) && !upgradeSeen) UpgradeModal(UpgradeReason.SYLLABUS) { upgradeSeen = true }
            }
        }
        Div({ classes("gen-foot") }) {
            Btn("Cancelar", onClose, style = "ghost")
            Btn("Montar edital", {
                val chosenFile = file ?: run { error = "Escolha o PDF do edital."; return@Btn }
                error = null
                step = br.com.estudario.web.data.EditalAi.Step.Reading
                scope.launch {
                    try {
                        val proposal = br.com.estudario.web.data.EditalAi.generate(chosenFile, br.com.estudario.web.data.EditalAi.Options(name, role, board, year)) { step = it }
                        val nodes = br.com.estudario.web.data.EditalAi.parse(proposal)
                        if (nodes.isEmpty()) error = "Não encontrei matérias neste PDF. Confira se é o edital com o conteúdo programático."
                        else { if (name.isBlank()) name = proposal.str("documentTitle")?.take(120) ?: ""; preview = nodes }
                    } catch (e: Throwable) {
                        error = e.message ?: "Não deu para montar o edital."
                    } finally { if (preview == null) step = null }
                }
            }, icon = "auto_awesome", enabled = file != null)
        }
    }
}
