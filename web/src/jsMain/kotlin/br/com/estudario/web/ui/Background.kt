package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.AiContent
import br.com.estudario.web.data.Store
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * Geração de material que continua sozinha (BackgroundAiTasks do app): começa no diálogo, mas não
 * depende dele. A pessoa pode fechar a janela e seguir estudando; quando o material chega, ele é
 * salvo no tópico e o Estudário avisa (notificação do computador, se ela permitiu, e um aviso aqui).
 */
object Generation {
    val state = mutableStateMapOf<Long, AiContent.Progress>()
    private val scope = MainScope()

    fun running(topicId: Long): Boolean = state[topicId].let { it != null && it !is AiContent.Progress.Done && it !is AiContent.Progress.Failed }

    val active: List<Long> get() = state.keys.filter { running(it) }

    fun start(topicId: Long, options: AiContent.Options) {
        if (running(topicId)) return
        state[topicId] = AiContent.Progress.Checking
        scope.launch {
            val result = AiContent.generate(Store.data, topicId, options) { state[topicId] = it }
            if (result is AiContent.Progress.Done) Store.update { AiContent.apply(it, topicId, result.proposal) }
            state[topicId] = result
            val title = Store.data.topics.firstOrNull { it.id == topicId }?.title ?: "seu tópico"
            if (result is AiContent.Progress.Done) Notify.materialReady(title, topicId)
            else if (result is AiContent.Progress.Failed) Toast.show("Não deu para gerar o material de $title.")
        }
    }

    fun clear(topicId: Long) { if (!running(topicId)) state.remove(topicId) }
}

/**
 * Avisos do computador: só um, o que vale a pena, o material pronto. Ninguém deixa o site aberto
 * esperando o lembrete de estudar; esse fica com o app do celular.
 */
object Notify {
    private const val KEY = "estudario.notify.material"
    var materialReadyOn: Boolean
        get() = runCatching { localStorage.getItem(KEY) }.getOrNull() != "0"
        set(value) { runCatching { localStorage.setItem(KEY, if (value) "1" else "0") } }

    fun permission(): String = runCatching { js("typeof Notification === 'undefined' ? 'unsupported' : Notification.permission") as String }.getOrDefault("unsupported")

    fun request(onResult: (String) -> Unit) {
        if (permission() == "unsupported") { onResult("unsupported"); return }
        runCatching { js("Notification.requestPermission()").then { result: dynamic -> onResult(result as String); null } }
    }

    private var baseTitle: String? = null

    fun materialReady(topicTitle: String, topicId: Long) {
        Toast.show("Material pronto: $topicTitle")
        if (!materialReadyOn) return
        val hidden = document.asDynamic().hidden == true || !document.hasFocus()
        if (hidden) flashTitle("Material pronto · Estudário")
        if (permission() == "granted" && hidden) {
            runCatching {
                val options = js("({})")
                options.body = "\"$topicTitle\" já tem teoria, flashcards e questões. Clique para estudar."
                options.icon = "icon.png"
                options.tag = "material-$topicId"
                val make = js("(function (t, o) { return new Notification(t, o); })")
                val notification = make("Seu material está pronto", options)
                notification.onclick = {
                    window.focus()
                    Router.go(Route.Topic(topicId))
                    notification.close()
                }
            }
        }
    }

    /** Título da aba piscando até a pessoa voltar para ela. */
    private fun flashTitle(text: String) {
        if (baseTitle != null) return
        baseTitle = document.title
        var on = true
        val timer = window.setInterval({ document.title = if (on) text else (baseTitle ?: "Estudário"); on = !on }, 1200)
        val restore: (org.w3c.dom.events.Event) -> Unit = {
            window.clearInterval(timer)
            document.title = baseTitle ?: "Estudário"
            baseTitle = null
        }
        window.addEventListener("focus", restore, js("({ once: true })"))
    }
}

/** Faixa no canto enquanto há material sendo gerado (o BackgroundAiBanner do app). */
@Composable
fun GenerationBanner() {
    val active = Generation.active
    val doneIds = Generation.state.filterValues { it is AiContent.Progress.Done }.keys
    if (active.isEmpty() && doneIds.isEmpty()) return
    Div({ classes("gen-dock") }) {
        active.forEach { id ->
            androidx.compose.runtime.key(id) {
                val title = Store.data.topics.firstOrNull { it.id == id }?.title ?: "Tópico"
                Button({ classes("gen-pill", "busy"); onClick { Router.go(Route.Topic(id)) } }) {
                    Folha("thinking", 34)
                    Span({ classes("grow") }) { B { Text("Gerando material…") }; Span({ classes("xs", "clamp-2") }) { Text(title) } }
                    Span({ classes("dots") }) { Span(); Span(); Span() }
                }
            }
        }
        doneIds.forEach { id ->
            androidx.compose.runtime.key("done-$id") {
                val title = Store.data.topics.firstOrNull { it.id == id }?.title ?: "Tópico"
                Button({ classes("gen-pill", "ready"); onClick { Generation.clear(id); Router.go(Route.Topic(id)) } }) {
                    Folha("happy", 34)
                    Span({ classes("grow") }) { B { Text("Material pronto!") }; Span({ classes("xs", "clamp-2") }) { Text(title) } }
                    Icon("arrow_forward", plain = true)
                }
            }
        }
    }
}
