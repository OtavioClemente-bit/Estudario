package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.estudario.domain.catalog.SubjectArt
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Subject
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * "Seu edital" no Início, como no app (SyllabusCoverage): cada matéria com o seu personagem da turma
 * do Folha, a barra do que já foi estudado e o estado. Fechado mostra as que pedem ação agora;
 * aberto, todas na ordem do edital.
 */
@Composable
fun HomeSyllabus(data: Snapshot, subjects: List<Subject>) {
    if (subjects.isEmpty()) return
    var showAll by remember { mutableStateOf(false) }
    val rows = subjects.map { subject ->
        val leaves = Queries.leafTopics(data, setOf(subject.id))
        Triple(subject, leaves.count(Queries::isStudied), leaves.size)
    }
    val totalTopics = rows.sumOf { it.third }
    val ordered = rows.sortedWith(compareBy({ it.third > 0 && it.second >= it.third }, { it.second == 0 }, { -(if (it.third == 0) 0.0 else it.second.toDouble() / it.third) }))
    val shown = if (showAll) rows else ordered.take(4)

    Card(extra = "home-syllabus") {
        Div({ classes("hs-head") }) {
            H3({ classes("hs-title") }) { Text("Seu edital") }
            Span({ classes("hs-count") }) { Text("${subjects.size} matérias · $totalTopics tópicos") }
        }
        Div({ classes("hs-list") }) {
            shown.forEach { (subject, studied, total) ->
                key(subject.id) {
                    val pct = if (total == 0) 0 else studied * 100 / total
                    val color = subjectColor(subject.name)
                    val (state, tone) = when {
                        total > 0 && studied >= total -> "Concluída" to "var(--green)"
                        studied > 0 -> "Em andamento" to "var(--primary)"
                        else -> "A começar" to "var(--faint)"
                    }
                    Div({ classes("hs-row"); attr("style", "--c:$color"); onClick { Router.go(Route.Edital) } }) {
                        Span({ classes("hs-art") }) { Icon(SubjectArt.iconFor(subject.name)) }
                        Div({ classes("hs-body") }) {
                            Div({ classes("hs-top") }) {
                                B({ classes("clamp-2") }) { Text(subject.name) }
                                B({ classes("hs-pct") }) { Text("$pct%") }
                            }
                            Div({ classes("hs-bar") }) { Span({ attr("style", "width:${pct.coerceAtLeast(if (studied > 0) 3 else 0)}%") }) }
                            Div({ classes("hs-meta") }) {
                                Span { Text("$studied de $total tópicos") }
                                Span({ attr("style", "color:$tone;font-weight:800") }) { Text(state) }
                            }
                        }
                    }
                }
            }
        }
        Div({ classes("hs-actions") }) {
            if (subjects.size > 4) Button({ classes("hs-more"); onClick { showAll = !showAll } }) { Text(if (showAll) "Mostrar menos" else "Ver todas as ${subjects.size} matérias") }
            Btn("Abrir o edital", { Router.go(Route.Edital) }, style = "outline", small = true)
        }
    }
}
