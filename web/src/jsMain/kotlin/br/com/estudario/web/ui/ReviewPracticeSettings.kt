package br.com.estudario.web.ui

import androidx.compose.runtime.*
import br.com.estudario.domain.ReviewPracticeOptions
import br.com.estudario.domain.ReviewQuestionOrder
import br.com.estudario.web.data.ReviewPreferences
import org.jetbrains.compose.web.dom.*

/** Janelas na raiz: o cabeçalho fixo nunca cobre a configuração ou o leitor de grifos. */
internal object ReviewPracticePanels {
    var settings by mutableStateOf<(() -> Unit)?>(null)
    var highlights by mutableStateOf<Pair<String, List<NbCard>>?>(null)
    fun openSettings(onSaved: () -> Unit = {}) { settings = onSaved }
}

@Composable
internal fun ReviewPracticePanelsHost() {
    LaunchedEffect(br.com.estudario.web.Router.current) { ReviewPracticePanels.settings = null; ReviewPracticePanels.highlights = null }
    if (ReviewPracticePanels.settings != null || ReviewPracticePanels.highlights != null) DisposableEffect(Unit) {
        val keys: (org.w3c.dom.events.Event) -> Unit = { event ->
            if ((event as org.w3c.dom.events.KeyboardEvent).key == "Escape") {
                ReviewPracticePanels.settings = null; ReviewPracticePanels.highlights = null
            }
        }
        kotlinx.browser.window.addEventListener("keydown", keys)
        onDispose { kotlinx.browser.window.removeEventListener("keydown", keys) }
    }
    ReviewPracticePanels.settings?.let { saved -> ReviewPracticeSettings(onSaved = saved) { ReviewPracticePanels.settings = null } }
    ReviewPracticePanels.highlights?.let { (title, cards) -> NotebookReview(title, cards, allowNavigation = false) { ReviewPracticePanels.highlights = null } }
}

@Composable
internal fun ReviewPracticeSettings(onSaved: () -> Unit = {}, onClose: () -> Unit) {
    var draft by remember { mutableStateOf(ReviewPreferences.value) }
    Modal(onDismiss = onClose, extraClass = "rv-settings") {
        Div({ classes("row", "between"); attr("style", "align-items:flex-start") }) {
            Div({ classes("row"); attr("style", "gap:12px") }) {
                Icon("settings")
                Div { H2 { Text("Seu ritmo de revisão") }; P({ classes("small", "muted") }) { Text("Personalize as próximas sessões") } }
            }
            IconButton("close", "Fechar configurações da revisão", onClose)
        }
        Div({ classes("stack", "loose") }) {
            H3 { Text("Questões por revisão") }
            BrandCounter(draft.questionCount, { draft = draft.copy(questionCount = it) }, min = 0, max = 50, unit = "questões", chips = listOf(0, 5, 10, 20), label = "Questões por revisão",
                mood = { if (it == 0) "Sem questões nesta revisão" else "Usa até $it questões disponíveis no tópico" })
            H3 { Text("Flashcards para lembrar") }
            BrandCounter(draft.flashcardCount, { draft = draft.copy(flashcardCount = it) }, min = 0, max = 30, unit = "flashcards", chips = listOf(0, 4, 8, 16), label = "Flashcards por revisão",
                mood = { if (it == 0) "Sem flashcards nesta revisão" else "Tente lembrar antes de consultar o material" })
            H3 { Text("Ordem das questões") }
            FilterChips(listOf(ReviewQuestionOrder.ERRORS_FIRST to "Erros primeiro", ReviewQuestionOrder.UNSEEN_FIRST to "Inéditas primeiro", ReviewQuestionOrder.MIXED to "Tudo misturado"), draft.order) { draft = draft.copy(order = it) }
            ReviewOption("Embaralhar flashcards", "Desligado: segue a ordem do material.", draft.shuffleFlashcards) { draft = draft.copy(shuffleFlashcards = it) }
            ReviewOption("Resumo rápido", "Consultar depois de tentar lembrar.", draft.showSummary) { draft = draft.copy(showSummary = it) }
            ReviewOption("Oferecer meus grifos", "Leitor opcional, sem sair da revisão ou editar.", draft.offerHighlights) { draft = draft.copy(offerHighlights = it) }
            if (draft.offerHighlights) ReviewOption("Notas junto dos grifos", "Mostra também o que você anotou no trecho.", draft.showHighlightNotes) { draft = draft.copy(showHighlightNotes = it) }
            P({ classes("small", "muted") }) { Text("A sessão em andamento mantém suas escolhas iniciais. Difícil continua repetindo a mesma etapa amanhã.") }
        }
        Div({ classes("row", "between", "wrap"); attr("style", "gap:10px;margin-top:20px") }) {
            Btn("Restaurar padrão", { draft = ReviewPracticeOptions() }, style = "ghost")
            Btn("Salvar configurações", {
                runCatching { ReviewPreferences.save(draft) }
                    .onSuccess { Toast.show("Configurações salvas para as próximas revisões"); onSaved(); onClose() }
                    .onFailure { Toast.show("Não consegui guardar as configurações neste navegador. Tente novamente.") }
            }, style = "primary", icon = "check")
        }
    }
}

@Composable
private fun ReviewOption(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Div({ classes("row", "between"); attr("style", "gap:16px") }) {
        Div { B { Text(title) }; P({ classes("small", "muted"); attr("style", "margin:3px 0") }) { Text(subtitle) } }
        Switch(checked, title, onChange)
    }
}
