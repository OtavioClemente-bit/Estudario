package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.data.Competition
import br.com.estudario.web.data.Queries
import br.com.estudario.web.data.Snapshot
import br.com.estudario.web.data.Store
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * Qual concurso está em foco nas telas de estudo (Treinar, Caderno, Revisões, Erros, Histórico,
 * Desempenho). Com um concurso só, nada aparece; com dois ou mais, uma aba por concurso no topo
 * da tela, lembrada entre visitas.
 */
object CompetitionFilter {
    private const val KEY = "estudario.competition"
    var selectedId by mutableStateOf(localStorage.getItem(KEY)?.toLongOrNull())

    fun select(id: Long) { selectedId = id; localStorage.setItem(KEY, id.toString()) }

    /** O concurso escolhido, ou o principal quando a escolha não existe mais. */
    fun current(data: Snapshot = Store.data): Competition? =
        data.competitions.firstOrNull { it.id == selectedId } ?: Queries.primaryCompetition(data)

    fun subjectIds(data: Snapshot = Store.data): Set<Long> {
        val id = current(data)?.id ?: return data.subjects.mapTo(hashSetOf()) { it.id }
        return data.subjects.filter { it.competitionId == id }.mapTo(hashSetOf()) { it.id }
    }

    fun topicIds(data: Snapshot = Store.data): Set<Long> {
        val subjects = subjectIds(data)
        return data.topics.filter { it.subjectId in subjects }.mapTo(hashSetOf()) { it.id }
    }
}

@Composable
fun CompetitionTabs() {
    val data = Store.data
    if (data.competitions.size < 2) return
    val current = CompetitionFilter.current(data)?.id
    Div({ classes("filter-chips", "competition-tabs") }) {
        data.competitions.forEach { c ->
            key(c.id) {
                Button({ classes(*listOfNotNull("fchip", if (c.id == current) "on" else null).toTypedArray()); onClick { CompetitionFilter.select(c.id) } }) {
                    Icon(if (c.id == current) "check" else "flag", plain = c.id == current)
                    Text(c.name)
                }
            }
        }
    }
}
