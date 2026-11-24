package br.com.estudario.ui.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.ui.brand.Glyph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.estudario.data.catalog.ExamCatalogEntry
import br.com.estudario.domain.catalog.ExamCategory
import br.com.estudario.ui.brand.BrandChoice
import br.com.estudario.ui.brand.Icon
import br.com.estudario.ui.brand.IconButton
import br.com.estudario.ui.brand.OutlinedButton
import br.com.estudario.ui.brand.TextButton
import br.com.estudario.ui.theme.estudarioColors

/*
 * Escolha do edital no primeiro uso. Antes: selo com o total, atalhos, abas, chips de área e de
 * banca e cartões que só marcavam (o botão do rodapé é que seguia). Com fonte grande isso virava
 * uma parede de filtros. Agora: um campo de busca, cartões grandes que já seguem ao toque, o cargo
 * numa lista simples só quando o edital tem vários, e os filtros guardados atrás de "Filtrar".
 */

/** Um edital do catálogo com os seus cargos (no catálogo, cada cargo é uma entrada). */
internal data class ExamGroup(
    val key: String,
    val shortName: String,
    val agency: String,
    val board: String?,
    val year: Int?,
    val entries: List<ExamCatalogEntry>,
) {
    val single: Boolean get() = entries.size == 1
}

/** Mesmo concurso, mesmo órgão, mesma banca e mesmo ano: é o mesmo edital, com outro cargo. */
internal fun examGroupKey(entry: ExamCatalogEntry): String = listOf(
    br.com.estudario.data.catalog.ExamCatalogSearch.normalize(entry.shortName),
    br.com.estudario.data.catalog.ExamCatalogSearch.normalize(entry.agency),
    br.com.estudario.data.catalog.ExamCatalogSearch.normalize(entry.board.orEmpty()),
    entry.year?.toString().orEmpty(),
).joinToString("|")

/** Agrupa os cargos por edital, mantendo a ordem em que cada edital aparece primeiro. */
internal fun groupExams(entries: List<ExamCatalogEntry>): List<ExamGroup> {
    val groups = LinkedHashMap<String, MutableList<ExamCatalogEntry>>()
    entries.forEach { groups.getOrPut(examGroupKey(it)) { mutableListOf() }.add(it) }
    return groups.map { (key, list) ->
        val first = list.first()
        ExamGroup(key, first.shortName, first.agency, first.board?.trim()?.takeIf(String::isNotBlank), first.year, list)
    }
}

/** Os filtros, todos guardados atrás do botão "Filtrar". */
internal data class ExamPickerFilter(
    val recentOnly: Boolean = true,
    val category: ExamCategory? = null,
    val board: String? = null,
) {
    /** Quantos filtros mudaram do padrão (para escrever no botão). */
    val activeCount: Int get() = listOf(!recentOnly, category != null, board != null).count { it }

    fun matches(entry: ExamCatalogEntry): Boolean =
        (category == null || entry.category() == category) && (board == null || entry.board?.trim() == board)
}

/** "Recente" é deste ano ou do anterior: são os editais que valem agora. */
internal fun isRecentExam(entry: ExamCatalogEntry, thisYear: Int): Boolean = (entry.year ?: 0) >= thisYear - 1

/**
 * A lista antes de digitar: os editais mais novos primeiro e, entre eles, os mais completos.
 * Na busca o período não vale (quem digita "TRT 2019" quer achar o de 2019).
 */
internal fun browseExams(entries: List<ExamCatalogEntry>, filter: ExamPickerFilter, thisYear: Int): List<ExamGroup> =
    groupExams(
        entries.asSequence()
            .filter { (!filter.recentOnly || isRecentExam(it, thisYear)) && filter.matches(it) }
            .sortedWith(compareByDescending<ExamCatalogEntry> { it.year ?: 0 }.thenByDescending { it.readyPercent })
            .toList(),
    )

/** Resultado da busca já filtrado e agrupado, na ordem de relevância da busca. */
internal fun searchExams(results: List<ExamCatalogEntry>, filter: ExamPickerFilter): List<ExamGroup> =
    groupExams(results.filter(filter::matches))

/** O campo da pergunta principal. A pergunta fica num título acima (rótulo flutuante corta com fonte grande). */
@Composable
internal fun ExamSearchField(query: String, onQueryChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Qual concurso você vai fazer?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
            placeholder = { Text("Ex.: TRT, PM, Polícia Federal") },
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Outlined.Close, "Limpar busca") }
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
    }
}

/** "Filtrar" (com quantos filtros estão ligados) e o resumo dos filtros, com um jeito só de limpar. */
@Composable
internal fun ExamFilterBar(filter: ExamPickerFilter, thisYear: Int, onOpen: () -> Unit, onClear: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedButton(onClick = onOpen) {
            Text(if (filter.activeCount == 0) "Filtrar" else "Filtrar (${filter.activeCount})")
        }
        val summary = listOfNotNull(
            if (filter.recentOnly) "Editais de ${thisYear - 1} e $thisYear" else "Todos os anos",
            filter.category?.label,
            filter.board,
        ).joinToString(" · ")
        Text(summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (filter.activeCount > 0) TextButton(onClick = onClear) { Text("Limpar filtros") }
    }
}

/** Um edital como cartão grande: tudo quebra linha, nada é cortado; o toque já segue. */
@Composable
internal fun ExamGroupCard(group: ExamGroup, thisYear: Int, onClick: () -> Unit) {
    BrandChoice(selected = false, onClick = onClick, modifier = Modifier.heightIn(min = 64.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(group.shortName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            if (!group.agency.equals(group.shortName, true)) Text(group.agency, style = MaterialTheme.typography.bodyMedium)
            Text(
                if (group.single) group.entries.first().role else "${group.entries.size} cargos · você escolhe o seu a seguir",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            val old = group.year != null && group.year < thisYear - 2
            val details = listOfNotNull(
                group.board?.let { "Banca $it" },
                group.year?.let { if (old) "$it (edital antigo)" else "$it" },
            ).joinToString(" · ")
            if (details.isNotBlank()) Text(
                details,
                style = MaterialTheme.typography.bodyMedium,
                color = if (old) estudarioColors().attention else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Outlined.ArrowForward, null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
    }
}

/** Lista de editais com um título curto em cima. */
@Composable
internal fun ExamGroupList(title: String, groups: List<ExamGroup>, thisYear: Int, onPick: (ExamGroup) -> Unit) {
    if (groups.isEmpty()) return
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        groups.forEach { group -> ExamGroupCard(group, thisYear) { onPick(group) } }
    }
}

/** O edital tem vários cargos: uma lista simples, um cargo por cartão. */
@Composable
internal fun ExamCargoPicker(group: ExamGroup, onPick: (ExamCatalogEntry) -> Unit, onBack: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextButton(onClick = onBack) { Text("Escolher outro concurso") }
        Text("Qual é o seu cargo?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            listOfNotNull(group.shortName, group.board?.let { "Banca $it" }, group.year?.toString()).joinToString(" · "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        group.entries.sortedBy { it.role.lowercase() }.forEach { entry ->
            BrandChoice(selected = false, onClick = { onPick(entry) }, modifier = Modifier.heightIn(min = 56.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(entry.role, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    if (entry.subjectCount > 0) Text(
                        "${entry.subjectCount} matérias",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.ArrowForward, null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/** A ação de quem não achou: um botão só, que cresce com a fonte. */
@Composable
internal fun ExamNotFoundAction(freeTier: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        kind = br.com.estudario.ui.brand.NoteKind.Upload,
        glyph = br.com.estudario.ui.brand.Glyph.Doc,
    ) {
        Text(if (freeTier) "Não achei meu concurso" else "Não achei meu concurso · enviar o PDF do edital")
    }
}

/** O personagem da turma de cada área, o mesmo traço do Folha (no lugar de uma bolinha de rádio). */
internal fun ExamCategory.glyph(): Glyph = when (this) {
    ExamCategory.SECURITY -> Glyph.Shield
    ExamCategory.COURTS -> Glyph.Balanca
    ExamCategory.CONTROL -> Glyph.Lupa
    ExamCategory.FISCAL -> Glyph.Recibo
    ExamCategory.BANKS -> Glyph.Cofrinho
    ExamCategory.LEGISLATIVE -> Glyph.Urna
    ExamCategory.HEALTH -> Glyph.KitMedico
    ExamCategory.EDUCATION -> Glyph.Cap
    ExamCategory.ADMINISTRATIVE -> Glyph.Maleta
}

/**
 * Quantos editais (não cargos) aparecem com este filtro, para o botão dizer "Mostrar N editais".
 * [searching]: na busca o período não vale (igual a [searchExams]).
 */
internal fun countExams(pool: List<ExamCatalogEntry>, filter: ExamPickerFilter, thisYear: Int, searching: Boolean): Int =
    pool.asSequence()
        .filter { (searching || !filter.recentOnly || isRecentExam(it, thisYear)) && filter.matches(it) }
        .map(::examGroupKey)
        .distinct()
        .count()

/** O que o Folha comenta no topo dos filtros, conforme quantos editais sobraram. */
internal fun examFilterQuip(count: Int?): String = when {
    count == null -> "Contando os editais…"
    count == 0 -> "Hmm, nenhum edital assim. Tira um filtro?"
    count == 1 -> "Achei 1 edital com esse jeitinho."
    count <= 20 -> "Achei $count editais. Bem certeiro!"
    else -> "Achei $count editais pra você."
}

/**
 * Os filtros numa janela com a cara do Estudário: o Folha no topo contando quantos editais sobram,
 * escolhas em pílula (cada área com o personagem dela) e um botão claro "Mostrar N editais".
 * As escolhas valem só ao tocar no botão; fechar pelo X ou por fora deixa o filtro como estava.
 * Com fonte 2x as pílulas quebram linha e o rodapé empilha; nada corta.
 * [pool]: os editais da lista atual (a busca, quando há texto; o catálogo inteiro, sem texto).
 */
@Composable
internal fun ExamFilterDialog(
    entries: List<ExamCatalogEntry>,
    filter: ExamPickerFilter,
    thisYear: Int,
    onChange: (ExamPickerFilter) -> Unit,
    onDismiss: () -> Unit,
    pool: List<ExamCatalogEntry> = entries,
    searching: Boolean = false,
) {
    var draft by rememberSaveable(stateSaver = ExamPickerFilterSaver) { mutableStateOf(filter) }
    val categories = remember(entries) { ExamCategory.present(entries.mapTo(hashSetOf()) { it.category() }) }
    val boards = remember(entries) {
        entries.mapNotNull { it.board?.trim()?.takeIf(String::isNotBlank) }.groupingBy { it }.eachCount()
            .entries.sortedByDescending { it.value }.take(8).map { it.key }
    }
    // Contar fora da thread que desenha (são milhares de editais); o número antigo fica até o novo chegar.
    val count by produceState<Int?>(null, pool, draft, thisYear, searching) {
        value = withContext(Dispatchers.Default) { countExams(pool, draft, thisYear, searching) }
    }
    val haptic = LocalHapticFeedback.current
    val pick: (ExamPickerFilter) -> Unit = {
        if (it != draft) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        draft = it
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        br.com.estudario.ui.brand.Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Cabeçalho: o Folha conta o resultado, com o título ao lado e o X para fechar.
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    br.com.estudario.ui.assistant.FolhaTalking(56.dp, examFilterQuip(count))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Filtrar editais", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                        Text(
                            examFilterQuip(count),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                        )
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar sem mudar") }
                }
                Column(
                    Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    FilterSection("Ano do edital")
                    FilterChoices {
                        FilterPill("Só recentes (${thisYear - 1} e $thisYear)", draft.recentOnly, Glyph.Calendar) { pick(draft.copy(recentOnly = true)) }
                        FilterPill("Todos os anos", !draft.recentOnly, Glyph.Books) { pick(draft.copy(recentOnly = false)) }
                    }
                    if (searching && draft.recentOnly) Text(
                        "Na busca, todos os anos aparecem.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (categories.size > 1) {
                        FilterSection("Área")
                        FilterChoices {
                            FilterPill("Todas as áreas", draft.category == null, Glyph.Globo) { pick(draft.copy(category = null)) }
                            categories.forEach { c -> FilterPill(c.label, draft.category == c, c.glyph()) { pick(draft.copy(category = c)) } }
                        }
                    }
                    if (boards.size > 1) {
                        FilterSection("Banca")
                        FilterChoices {
                            FilterPill("Todas as bancas", draft.board == null, null) { pick(draft.copy(board = null)) }
                            boards.forEach { b -> FilterPill(b, draft.board == b, null) { pick(draft.copy(board = b)) } }
                        }
                    }
                }
                // Rodapé: a ação principal larga, com o número; limpar fica embaixo, discreto.
                val total = count
                br.com.estudario.ui.brand.Button(
                    onClick = { onChange(draft); onDismiss() },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    enabled = total != 0,
                    kind = br.com.estudario.ui.brand.NoteKind.Primary,
                    glyph = Glyph.Check,
                ) {
                    Text(
                        when (total) {
                            null -> "Mostrar editais"
                            0 -> "Nenhum edital assim"
                            1 -> "Mostrar 1 edital"
                            else -> "Mostrar $total editais"
                        },
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
                if (draft != ExamPickerFilter()) {
                    TextButton(onClick = { pick(ExamPickerFilter()) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Limpar filtros")
                    }
                }
            }
        }
    }
}

/** Guarda o rascunho do filtro ao girar a tela. */
private val ExamPickerFilterSaver = Saver<ExamPickerFilter, List<Any?>>(
    save = { listOf(it.recentOnly, it.category?.name, it.board) },
    restore = { saved ->
        ExamPickerFilter(
            recentOnly = saved[0] as Boolean,
            category = (saved[1] as String?)?.let { name -> ExamCategory.entries.firstOrNull { it.name == name } },
            board = saved[2] as String?,
        )
    },
)

@Composable
private fun FilterSection(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterChoices(content: @Composable () -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) { content() }
}

/** Uma escolha em pílula (a do app), com o personagem da turma à esquerda quando há um. Toque de 48 dp. */
@Composable
private fun FilterPill(label: String, selected: Boolean, glyph: Glyph?, onClick: () -> Unit) {
    br.com.estudario.ui.brand.FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .semantics { this.selected = selected; role = Role.RadioButton },
        leadingIcon = glyph?.let { g -> { br.com.estudario.ui.brand.BrandIcon(g, size = 22.dp) } },
    )
}

// ---- Prévias com fonte grande (acessibilidade): nada pode cortar nem sobrepor. ----

private val previewEntries = listOf(
    ExamCatalogEntry("1", "TRT-3", "Tribunal Regional do Trabalho da 3ª Região", "Técnico Judiciário - Área Administrativa", "FCC", 2025, subjectCount = 12),
    ExamCatalogEntry("2", "TRT-3", "Tribunal Regional do Trabalho da 3ª Região", "Analista Judiciário - Área Judiciária", "FCC", 2025, subjectCount = 14),
    ExamCatalogEntry("3", "PMMG", "Polícia Militar de Minas Gerais", "Soldado (CFSd QPPM)", "IDECAN", 2026, subjectCount = 9),
    ExamCatalogEntry("4", "INSS", "Instituto Nacional do Seguro Social", "Técnico do Seguro Social", "Cebraspe", 2022, subjectCount = 10),
)

@Preview(name = "Escolha do edital, fonte 2x", showBackground = true, fontScale = 2f, widthDp = 360, heightDp = 1800)
@Composable
private fun ExamPickerLargeFontPreview() {
    br.com.estudario.ui.theme.EstudarioTheme(darkTheme = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ExamSearchField("") {}
            ExamFilterBar(ExamPickerFilter(), 2026, {}, {})
            ExamGroupList("Editais recentes", groupExams(previewEntries), 2026) {}
            ExamNotFoundAction(freeTier = false) {}
        }
    }
}

@Preview(name = "Escolha do cargo, fonte 2x", showBackground = true, fontScale = 2f, widthDp = 360, heightDp = 1200)
@Composable
private fun ExamCargoLargeFontPreview() {
    br.com.estudario.ui.theme.EstudarioTheme(darkTheme = false) {
        Column(Modifier.padding(16.dp)) { ExamCargoPicker(groupExams(previewEntries).first(), {}, {}) }
    }
}

@Preview(name = "Filtros, fonte 2x", showBackground = true, fontScale = 2f, widthDp = 360, heightDp = 900)
@Composable
private fun ExamFilterLargeFontPreview() {
    br.com.estudario.ui.theme.EstudarioTheme(darkTheme = false) {
        ExamFilterDialog(previewEntries, ExamPickerFilter(recentOnly = false), 2026, {}, {})
    }
}
