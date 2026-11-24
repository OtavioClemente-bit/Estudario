package br.com.estudario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PestControlRodent
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.estudario.data.local.TopicStatus
import br.com.estudario.domain.NotebookSearch
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.brand.Button
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.IconButton
import br.com.estudario.ui.brand.OutlinedButton

/*
 * A busca geral do Caderno de estudo, no topo da entrada: a pessoa escreve uma matéria, um tópico ou
 * um pedaço do que guardou e acha (a regra, sem acento e com pedaço de palavra, é NotebookSearch, no
 * módulo comum; o site faz igual em web/.../ui/NotebookSearch.kt). Vazia, a tela normal aparece.
 */

/** Para onde leva cada resultado. */
private sealed class SearchRef {
    /** Um tópico do edital (ou de matéria própria): [studied] já é página do caderno; [hasMaterial] tem teoria. */
    data class Topic(val id: Long, val studied: Boolean, val hasMaterial: Boolean) : SearchRef()
    /** Algo guardado: abre a página do tópico onde mora (NOTEBOOK_GENERAL = Geral); grifo abre também na teoria. */
    data class Saved(val page: Long, val theoryId: Long? = null, val block: Int = 0) : SearchRef()
}

/** O campo de busca do topo. */
@Composable
fun NotebookSearchField(query: String, onQuery: (String) -> Unit) {
    OutlinedTextField(
        query, onQuery, Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Outlined.Search, null) },
        trailingIcon = if (query.isNotEmpty()) ({ IconButton(onClick = { onQuery("") }) { Icon(Icons.Outlined.Close, "Limpar a busca") } }) else null,
        placeholder = { Text("Buscar no caderno: matéria, tópico, grifo ou anotação") },
        singleLine = true, shape = RoundedCornerShape(16.dp),
    )
}

/** Tudo o que dá para achar: os tópicos (de todas as matérias da pessoa) e tudo o que ela guardou. */
@Composable
private fun rememberSearchItems(viewModel: AppViewModel): List<NotebookSearch.Item<SearchRef>> {
    val topics by viewModel.topics.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val theories by viewModel.theories.collectAsState()
    val saved = rememberSavedByTopic(viewModel)
    return remember(topics, subjects, theories, saved) {
        val subjectName = subjects.associate { it.id to it.name }
        val parents = topics.mapNotNullTo(hashSetOf()) { it.parentTopicId }
        val withTheory = theories.mapTo(hashSetOf()) { it.topicId }
        val topicById = topics.associateBy { it.id }
        val out = ArrayList<NotebookSearch.Item<SearchRef>>()
        topics.filter { it.id !in parents }.forEach { t ->
            out += NotebookSearch.Item(NotebookSearch.Kind.TOPIC, t.title, subjectName[t.subjectId], ref = SearchRef.Topic(t.id, t.status != TopicStatus.NAO_ESTUDADO, t.id in withTheory))
        }
        saved.forEach { (page, s) ->
            val t = topicById[page]
            val where = listOfNotNull(t?.title ?: "Geral", t?.let { subjectName[it.subjectId] }).joinToString(" · ")
            s.marks.forEach { m ->
                val quote = plainPreview(m.quote)
                out += NotebookSearch.Item(NotebookSearch.Kind.MARK, quote.take(90), where, "$quote ${m.note}", SearchRef.Saved(page, m.theoryId, m.blockIndex))
            }
            s.notes.forEach { n -> out += NotebookSearch.Item(NotebookSearch.Kind.NOTE, n.text.take(90), where, n.text, SearchRef.Saved(page)) }
            s.tips.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.TIP, plainPreview(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
            s.traps.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.TRAP, plainPreview(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
            s.cards.forEach { x -> out += NotebookSearch.Item(NotebookSearch.Kind.CARD, plainPreview(x.text).take(90), where, "${x.text} ${x.answer.orEmpty()}", SearchRef.Saved(page)) }
            s.decks.forEach { d -> out += NotebookSearch.Item(NotebookSearch.Kind.DECK, d.title.ifBlank { "Flashcards" }, where, d.markdown, SearchRef.Saved(page)) }
            s.questions.forEach { q -> val text = plainPreview(q.question.statement); out += NotebookSearch.Item(NotebookSearch.Kind.QUESTION, text.take(90), where, text, SearchRef.Saved(page)) }
        }
        out
    }
}

private fun kindLabel(k: NotebookSearch.Kind) = when (k) {
    NotebookSearch.Kind.TOPIC -> "TÓPICO"; NotebookSearch.Kind.MARK -> "GRIFO"; NotebookSearch.Kind.NOTE -> "ANOTAÇÃO"
    NotebookSearch.Kind.TIP -> "DICA"; NotebookSearch.Kind.TRAP -> "PEGADINHA"; NotebookSearch.Kind.CARD -> "FLASHCARD"
    NotebookSearch.Kind.DECK -> "BARALHO"; NotebookSearch.Kind.QUESTION -> "QUESTÃO"
}

private fun kindIcon(k: NotebookSearch.Kind): ImageVector = when (k) {
    NotebookSearch.Kind.TOPIC -> Icons.Outlined.MenuBook; NotebookSearch.Kind.MARK -> Icons.Outlined.BorderColor; NotebookSearch.Kind.NOTE -> Icons.Outlined.EditNote
    NotebookSearch.Kind.TIP -> Icons.Outlined.Lightbulb; NotebookSearch.Kind.TRAP -> Icons.Outlined.PestControlRodent
    NotebookSearch.Kind.CARD, NotebookSearch.Kind.DECK -> Icons.Outlined.Style; NotebookSearch.Kind.QUESTION -> Icons.Outlined.Quiz
}

/** Quantos de cada grupo aparecem antes do "Ver mais". */
private const val FIRST = 8

/**
 * Os resultados, em três grupos: matérias e tópicos (tocar abre a página do caderno, ou o tópico para
 * estudar), grifos e anotações, e questões/flashcards/dicas salvos (tocar abre a página do tópico onde
 * estão). Sem nada, uma mensagem e o caminho para o edital.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotebookSearchResults(
    viewModel: AppViewModel, query: String,
    onPage: (Long) -> Unit, onTopic: (Long) -> Unit, onOpenTheory: (Long, Int) -> Unit, onEdital: () -> Unit,
) {
    val items = rememberSearchItems(viewModel)
    val hits = remember(items, query) { NotebookSearch.search(query, items) }
    val groups = remember(hits) { NotebookSearch.grouped(hits) }
    val scheme = MaterialTheme.colorScheme
    val showAll = remember(query) { mutableStateOf(setOf<NotebookSearch.Group>()) }
    Column(Modifier.fillMaxWidth().padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            if (hits.isEmpty()) "Nenhum resultado" else "${hits.size} ${if (hits.size == 1) "resultado" else "resultados"} para “${query.trim()}”",
            fontSize = 13.sp, fontWeight = FontWeight.Bold, color = scheme.onSurfaceVariant,
        )
        if (hits.isEmpty()) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(scheme.surface).border(2.dp, scheme.outlineVariant, RoundedCornerShape(18.dp)).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.SearchOff, null, Modifier.size(28.dp), tint = scheme.onSurfaceVariant)
                    Text("Não achei “${query.trim()}” no seu caderno", Modifier.weight(1f), fontWeight = FontWeight.Black, fontSize = 17.sp, color = scheme.onSurface)
                }
                Text("Confira a grafia ou tente uma palavra só (por exemplo, \"crase\" ou \"licitação\"). Se a matéria ainda não está no seu edital, procure no edital ou crie uma matéria sua.", fontSize = 15.sp, lineHeight = 22.sp, color = scheme.onSurfaceVariant)
                OutlinedButton(kind = br.com.estudario.ui.brand.NoteKind.Study, onClick = onEdital, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Outlined.MenuBook, null); Spacer(Modifier.width(6.dp)); Text("Ir para o edital") }
            }
            return@Column
        }
        // Achou só tópico sem material: avisa que dá para gerar a matéria dele.
        val topicsOnly = groups.size == 1 && groups[0].first == NotebookSearch.Group.TOPICS
        val noMaterial = groups.firstOrNull { it.first == NotebookSearch.Group.TOPICS }?.second.orEmpty().count { (it.item.ref as SearchRef.Topic).let { r -> !r.hasMaterial && !r.studied } }
        if (topicsOnly && noMaterial > 0) Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(scheme.surfaceVariant.copy(alpha = 0.6f)).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Outlined.AutoAwesome, null, tint = Color(0xFF2F5BD3))
            Text("Nada guardado sobre isso ainda. ${if (noMaterial == 1) "O tópico abaixo ainda não tem material" else "Alguns tópicos abaixo ainda não têm material"}: toque em \"Gerar material\" para estudar.", fontSize = 14.sp, lineHeight = 20.sp, color = scheme.onSurface)
        }
        groups.forEach { (group, list) ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(group.label.uppercase(), Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, color = scheme.onSurfaceVariant)
                    Text("${list.size}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = scheme.onSurfaceVariant)
                }
                val all = group in showAll.value
                list.take(if (all) list.size else FIRST).forEach { hit -> SearchHitRow(hit, onPage, onTopic, onOpenTheory) }
                if (!all && list.size > FIRST) OutlinedButton(onClick = { showAll.value = showAll.value + group }, modifier = Modifier.fillMaxWidth()) { Text("Ver mais ${list.size - FIRST}") }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchHitRow(hit: NotebookSearch.Hit<SearchRef>, onPage: (Long) -> Unit, onTopic: (Long) -> Unit, onOpenTheory: (Long, Int) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val item = hit.item
    val ref = item.ref
    val shape = RoundedCornerShape(14.dp)
    val open: () -> Unit = when (ref) {
        is SearchRef.Topic -> ({ if (ref.studied) onPage(ref.id) else onTopic(ref.id) })
        is SearchRef.Saved -> ({ onPage(ref.page) })
    }
    Column(
        Modifier.fillMaxWidth().clip(shape).background(scheme.surface).border(2.dp, scheme.outlineVariant, shape).clickable(onClick = open).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(kindIcon(item.kind), null, Modifier.size(22.dp), tint = Color(0xFF2F5BD3))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(if (item.kind == NotebookSearch.Kind.TOPIC) (item.context ?: "Matéria").uppercase() else "${kindLabel(item.kind)} · ${item.context.orEmpty()}", fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 0.6.sp, color = scheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp, color = scheme.onSurface, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = scheme.onSurfaceVariant)
        }
        hit.excerpt?.takeIf { it != item.title }?.let { Text(it, fontSize = 14.sp, lineHeight = 20.sp, color = scheme.onSurfaceVariant, maxLines = 4, overflow = TextOverflow.Ellipsis) }
        when (ref) {
            is SearchRef.Topic -> FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when {
                    ref.studied -> Status("No seu caderno", Color(0xFF0E5C3F), Color(0xFFE3F8EE))
                    ref.hasMaterial -> Status("Tem material, ainda não estudado", Color(0xFF6E4700), Color(0xFFFFF5D6))
                    else -> Status("Sem material ainda", Color(0xFF8A2A1F), Color(0xFFFFE9E6))
                }
                if (!ref.studied) Button(kind = br.com.estudario.ui.brand.NoteKind.Study, onClick = { onTopic(ref.id) }) {
                    Icon(if (ref.hasMaterial) Icons.Outlined.MenuBook else Icons.Outlined.AutoAwesome, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp))
                    Text(if (ref.hasMaterial) "Estudar" else "Gerar material")
                }
            }
            is SearchRef.Saved -> if (ref.theoryId != null) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(kind = br.com.estudario.ui.brand.NoteKind.Study, onClick = { onOpenTheory(ref.theoryId, ref.block) }) { Icon(Icons.Outlined.MenuBook, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Abrir na teoria") }
            }
        }
    }
}

@Composable
private fun Status(text: String, ink: Color, bg: Color) {
    Text(text, Modifier.clip(RoundedCornerShape(50)).background(bg).padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Black, color = ink)
}
