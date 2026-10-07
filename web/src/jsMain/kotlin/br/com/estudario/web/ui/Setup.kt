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
    Modal(onDismiss = { if (!running) onClose() }) {
        H2 { Text("Montar edital com IA") }
        val result = preview
        when {
            result != null -> {
                P({ classes("muted") }) { Text("A IA encontrou ${result.size} matérias e ${result.sumOf { s -> s.topics.size }} tópicos. Confira e continue.") }
                Div({ attr("style", "max-height:40vh;overflow:auto;border:1px solid var(--line);border-radius:14px;padding:8px 14px") }) {
                    result.forEach { s ->
                        Div({ attr("style", "padding:8px 0;border-bottom:1px solid var(--line)") }) {
                            org.jetbrains.compose.web.dom.B { Text(s.name) }
                            P({ classes("small", "muted") }) { Text(s.topics.joinToString(" · ") { it.name }.take(400)) }
                        }
                    }
                }
                Div({ classes("row", "end") }) {
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn("Usar este edital", { onDone(name.ifBlank { file?.name?.substringBeforeLast('.') ?: "Meu concurso" }, role, result) }, icon = "check")
                }
            }
            running -> ProcessView(
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
                footer = { P({ classes("small", "muted"); attr("style", "text-align:center") }) { Text("Pode levar alguns minutos. Deixe esta aba aberta.") } },
            )
            else -> {
                P({ classes("muted", "small") }) { Text("Escolha o PDF oficial do edital. O Estudário lê o conteúdo programático e cria as matérias e os tópicos na ordem do edital.") }
                Div({ classes("field") }) {
                    org.jetbrains.compose.web.dom.Label { Text("PDF do edital") }
                    Input(InputType.File) {
                        classes("input"); attr("accept", "application/pdf"); attr("style", "padding-top:10px")
                        onChange { event -> file = (event.target.asDynamic().files?.item(0)) as? org.w3c.files.File }
                    }
                }
                Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Nome do concurso") }; Input(InputType.Text) { classes("input"); value(name); placeholder("Ex.: TJSP Escrevente 2026"); onInput { name = it.value } } }
                Div({ classes("grid", "cols-2") }) {
                    Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Cargo (opcional)") }; Input(InputType.Text) { classes("input"); value(role); onInput { role = it.value } } }
                    Div({ classes("field") }) { org.jetbrains.compose.web.dom.Label { Text("Banca (opcional)") }; Input(InputType.Text) { classes("input"); value(board); onInput { board = it.value } } }
                }
                error?.let { Div({ classes("banner", "error") }) { Text(it) } }
                Div({ classes("row", "end") }) {
                    Btn("Cancelar", onClose, style = "ghost")
                    Btn("Montar edital", {
                        val chosenFile = file ?: run { error = "Escolha o PDF do edital."; return@Btn }
                        error = null
                        step = br.com.estudario.web.data.EditalAi.Step.Reading
                        scope.launch {
                            try {
                                val proposal = br.com.estudario.web.data.EditalAi.generate(chosenFile, br.com.estudario.web.data.EditalAi.Options(name, role, board, year)) { step = it }
                                val nodes = br.com.estudario.web.data.EditalAi.parse(proposal)
                                if (nodes.isEmpty()) error = "A IA não encontrou matérias neste PDF. Confira se é o edital com o conteúdo programático."
                                else { if (name.isBlank()) name = proposal.str("documentTitle")?.take(120) ?: ""; preview = nodes }
                            } catch (e: Throwable) {
                                error = e.message ?: "Não deu para montar o edital."
                            } finally { if (preview == null) step = null }
                        }
                    }, icon = "auto_awesome", enabled = file != null)
                }
            }
        }
    }
}
