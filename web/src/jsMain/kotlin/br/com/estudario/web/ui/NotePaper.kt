package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

/**
 * As etiquetas de uma anotação, no começo do texto ("[Macete] ..."), iguais às do app: valem no
 * app, no site e no backup sem mudar o banco.
 */
object NoteTags {
    val all = listOf("Macete", "Resumo", "Dúvida", "Pegadinha", "Letra da lei")
    private val prefix = Regex("""^\[(Macete|Resumo|Dúvida|Pegadinha|Letra da lei|Lei seca)\]\s*""")
    fun split(text: String): Pair<String?, String> {
        val m = prefix.find(text) ?: return null to text
        // "Lei seca" foi o nome antigo de "Letra da lei".
        return m.groupValues[1].replace("Lei seca", "Letra da lei") to text.substring(m.range.last + 1)
    }
    fun join(tag: String?, body: String): String = if (tag == null) body.trim() else "[$tag] ${body.trim()}"
    fun cls(tag: String?): String = when (tag) {
        "Macete" -> "t-macete"; "Resumo" -> "t-resumo"; "Dúvida" -> "t-duvida"; "Pegadinha" -> "t-pegadinha"; "Letra da lei" -> "t-lei"; else -> "t-none"
    }
}

private const val NOTE_LIMIT = 4000

fun noteDate(ms: Long): String {
    val d = kotlin.js.Date(ms.toDouble())
    val months = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")
    return "${d.getDate()} de ${months[d.getMonth()]}, ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}"
}

/**
 * O editor de anotação do Estudário: uma folha de caderno pautada, com o tópico (busca), o trecho
 * grifado quando a nota é sobre ele, etiquetas rápidas e o contador. O mesmo do app.
 */
@Composable
fun NotePaperEditor(
    initialText: String,
    isNew: Boolean,
    createdAt: Long?,
    onDismiss: () -> Unit,
    onSave: (text: String, topicId: Long?) -> Unit,
    onDelete: (() -> Unit)? = null,
    quote: String? = null,
    topics: List<Pair<Long, String>>? = null,
    initialTopicId: Long? = null,
    fixedPlace: String? = null,
    deleteLabel: String = "Apagar anotação",
) {
    val (tag0, body0) = remember(initialText) { NoteTags.split(initialText) }
    var tag by remember(initialText) { mutableStateOf(tag0) }
    var body by remember(initialText) { mutableStateOf(body0) }
    var topicId by remember(initialTopicId) { mutableStateOf(initialTopicId) }
    var picking by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var confirmDelete by remember { mutableStateOf(false) }
    Modal(onDismiss = onDismiss, extraClass = "note-sheet") {
        Div({ classes("note-head") }) {
            Div({ classes("grow") }) {
                B({ classes("note-title") }) { Text(if (isNew) "Nova anotação" else "Sua anotação") }
                Div({ classes("xs", "muted") }) { Text(if (createdAt != null && !isNew) noteDate(createdAt) else "Fica no Caderno de estudo, no app e no site") }
            }
            IconButton("close", "Fechar") { onDismiss() }
        }
        val place = fixedPlace ?: topics?.firstOrNull { it.first == topicId }?.second ?: "Geral (sem tópico)"
        Div({ classes("note-place-row") }) {
            Button({
                classes("note-place")
                if (topics == null || fixedPlace != null) attr("disabled", "")
                onClick { picking = !picking }
            }) { Icon("sell"); Span({ classes("clamp-1") }) { Text(place) }; if (topics != null && fixedPlace == null) Span({ classes("xs") }) { Text("trocar") } }
        }
        if (picking && topics != null) Div({ classes("note-picker") }) {
            Input(org.jetbrains.compose.web.attributes.InputType.Search) { classes("input"); attr("placeholder", "Buscar tópico"); value(search); onInput { search = it.value } }
            Div({ classes("note-picker-list") }) {
                val accents = Regex("[\\u0300-\\u036f]")
                val fold = { s: String -> (s.lowercase().asDynamic().normalize("NFD") as String).replace(accents, "") }
                Button({ classes(*listOfNotNull("note-pick", if (topicId == null) "on" else null).toTypedArray()); onClick { topicId = null; picking = false } }) { Text("Geral (sem tópico)") }
                topics.filter { search.isBlank() || fold(it.second).contains(fold(search.trim())) }.sortedBy { it.second }.take(80).forEach { (id, title) ->
                    Button({ classes(*listOfNotNull("note-pick", if (topicId == id) "on" else null).toTypedArray()); onClick { topicId = id; picking = false } }) { Text(title) }
                }
            }
        }
        if (!quote.isNullOrBlank()) Div({ classes("note-quote") }) { Text("“${quote.trim().take(320)}${if (quote.trim().length > 320) "…" else ""}”") }
        Div({ classes("note-tags") }) {
            NoteTags.all.forEach { t ->
                Button({ classes(*listOfNotNull("note-tag", NoteTags.cls(t), if (tag == t) "on" else null).toTypedArray()); onClick { tag = if (tag == t) null else t } }) { Text(t) }
            }
        }
        TextArea(body, {
            classes("note-paper")
            attr("maxlength", "$NOTE_LIMIT")
            attr("placeholder", if (quote != null) "Por que esse trecho importa? Escreva com suas palavras…" else "Escreva com suas palavras: o macete, a dúvida, o resumo…")
            if (isNew) attr("autofocus", "")
            onInput { body = it.value }
        })
        Div({ classes("note-foot") }) {
            if (onDelete != null) IconButton("delete", deleteLabel) { confirmDelete = true }
            Span({ classes("xs", "muted", "grow") }) { Text("${body.length}/$NOTE_LIMIT") }
            Btn("Salvar no caderno", { onSave(NoteTags.join(tag, body), topicId) }, style = "primary", icon = "save", enabled = body.isNotBlank())
        }
        if (confirmDelete && onDelete != null) Div({ classes("note-confirm") }) {
            Span({ classes("grow", "small") }) { Text("$deleteLabel? Ela sai do app, do site e do Caderno.") }
            Btn("Manter", { confirmDelete = false }, style = "ghost", small = true)
            Btn("Apagar", { confirmDelete = false; onDelete() }, style = "danger", small = true)
        }
    }
}

/** A anotação no Caderno: um pedaço de folha pautada com a etiqueta, o trecho (se houver) e a data. */
@Composable
fun NotePaperCard(text: String, createdAt: Long, onClick: () -> Unit, quote: String? = null, footer: (@Composable () -> Unit)? = null) {
    val (tag, body) = NoteTags.split(text)
    Div({ classes("note-card", NoteTags.cls(tag)) }) {
        Div({ classes("note-card-main"); attr("role", "button"); attr("tabindex", "0"); onClick { onClick() } }) {
            Div({ classes("note-card-top") }) {
                if (tag != null) Span({ classes("note-pill") }) { Text(tag.uppercase()) }
                if (quote != null) Span({ classes("note-kind") }) { Text("NOTA NUM TRECHO") }
                Span({ classes("grow") })
                Span({ classes("xs", "muted") }) { Text(noteDate(createdAt)) }
            }
            if (!quote.isNullOrBlank()) Div({ classes("note-card-quote", "clamp-3") }) { Text("“${quote.trim()}”") }
            P({ classes("note-card-body") }) { Text(body) }
        }
        footer?.invoke()
    }
}

/**
 * A nota aberta para ler: a folha pautada com a etiqueta, o lugar, o trecho (se for de um trecho), o
 * texto inteiro e as ações Editar e Excluir (com confirmação).
 */
@Composable
fun NoteView(text: String, createdAt: Long, place: String, quote: String?, onEdit: () -> Unit, onDelete: () -> Unit, onClose: () -> Unit, onOpen: (() -> Unit)? = null) {
    val (tag, body) = NoteTags.split(text)
    var confirm by remember { mutableStateOf(false) }
    Modal(onDismiss = onClose, extraClass = "note-sheet") {
        Div({ classes("note-head") }) {
            Div({ classes("grow") }) {
                Div({ classes("row"); attr("style", "gap:8px") }) {
                    B({ classes("note-title") }) { Text(if (quote != null) "Nota do trecho" else "Sua anotação") }
                    if (tag != null) Span({ classes("note-pill", NoteTags.cls(tag)) }) { Text(tag.uppercase()) }
                }
                Div({ classes("xs", "muted") }) { Text("$place · ${noteDate(createdAt)}") }
            }
            IconButton("close", "Fechar") { onClose() }
        }
        if (!quote.isNullOrBlank()) Div({ classes("note-quote") }) { Text("“${quote.trim()}”") }
        Div({ classes("note-read") }) { Text(body) }
        if (confirm) Div({ classes("note-confirm") }) {
            Span({ classes("grow", "small") }) { Text(if (quote != null) "Excluir esta nota? O trecho continua grifado." else "Excluir esta anotação? Ela sai do app, do site e do Caderno.") }
            Btn("Manter", { confirm = false }, style = "ghost", small = true)
            Btn("Excluir", { onDelete() }, style = "danger", small = true)
        }
        Div({ classes("note-foot") }) {
            Btn("Excluir", { confirm = true }, style = "ghost", icon = "delete")
            Span({ classes("grow") })
            if (onOpen != null) Btn("Abrir na teoria", onOpen, style = "ghost", icon = "menu_book")
            Btn("Editar", onEdit, style = "primary", icon = "edit")
        }
    }
}
