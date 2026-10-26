package br.com.estudario.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import kotlinx.coroutines.launch

/** Endereços do app: #/inicio, #/plano, #/edital/12 ... (hash para funcionar em hospedagem estática). */
sealed class Route(val path: String) {
    data object Home : Route("inicio")
    data object Plan : Route("plano")
    data object PlanSettings : Route("plano/ajustar")
    data object Edital : Route("edital")
    data class Topic(val id: Long) : Route("edital/$id")
    data object Train : Route("treinar")
    data class Quiz(val scope: String) : Route("treinar/questoes/$scope")
    data object Simulations : Route("treinar/simulados")
    data class Simulation(val id: Long) : Route("treinar/simulados/$id")
    data object Notebook : Route("caderno")
    /** A Folheada do dia: poucas páginas do que está esfriando. */
    data object NotebookFlip : Route("caderno/folheada")
    /** A página de um tópico estudado, no Caderno. */
    data class NotebookPage(val topicId: Long) : Route("caderno/pagina/$topicId")
    /** O que a pessoa guardou (grifos, notas, salvos): o Caderno de antes. */
    data object NotebookSaved : Route("caderno/guardados")
    /** [saved]: só os cartões guardados no Caderno (baralhos marcados e perguntas com estrela), como no app. */
    /** [deckId]: só aquele baralho (o "Praticar" de um baralho no Caderno). */
    data class Flashcards(val topicId: Long?, val saved: Boolean = false, val deckId: Long? = null) : Route(
        when {
            deckId != null -> "caderno/baralho/$deckId"
            saved -> "caderno/praticar/${topicId ?: -1}"
            topicId == null -> "caderno/flashcards"
            else -> "caderno/flashcards/$topicId"
        },
    )
    data object Reviews : Route("caderno/revisoes")
    data class ReviewSession(val id: Long) : Route("caderno/revisoes/$id")
    data object Errors : Route("caderno/erros")
    /** A missão da Virada: os erros que vencem hoje, para virar. */
    data object ErrorMission : Route("caderno/erros/missao")
    data object Focus : Route("foco")
    data object FocusHistory : Route("foco/historico")
    data object Profile : Route("perfil")
    data object Stats : Route("perfil/desempenho")
    data object Achievements : Route("perfil/conquistas")
    data object Wardrobe : Route("perfil/folha")
    data object Sources : Route("perfil/fontes")
    data object Notifications : Route("perfil/notificacoes")
    data object PlanLimits : Route("perfil/plano")
    data object Settings : Route("perfil/ajustes")
    data object Setup : Route("configurar")
    /** Reportes e comentários (só a conta do administrador). */
    data object AdminReports : Route("equipe/reportes")
    data object AdminCoverage : Route("equipe/cobertura")

    /** Item do menu ao qual esta rota pertence. */
    val section: Route
        get() = when (this) {
            is Topic, Setup -> Edital
            PlanSettings -> Plan
            is Quiz, Simulations, is Simulation -> Train
            is Flashcards, NotebookFlip, is NotebookPage, NotebookSaved -> Notebook
            ErrorMission -> Errors
            is ReviewSession -> Reviews
            else -> this
        }

    companion object {
        fun parse(hash: String): Route {
            val parts = hash.removePrefix("#").removePrefix("/").split('/').filter { it.isNotEmpty() }
            val second = parts.getOrNull(1)
            return when (parts.firstOrNull()) {
                null, "inicio", "demo" -> Home
                "plano" -> if (second == "ajustar") PlanSettings else Plan
                "edital" -> second?.toLongOrNull()?.let(::Topic) ?: Edital
                "treinar", "questoes" -> when (second) {
                    "questoes", "treino" -> Quiz(parts.getOrNull(2) ?: "rapido-10")
                    "simulados" -> parts.getOrNull(2)?.toLongOrNull()?.let(::Simulation) ?: Simulations
                    "flashcards" -> Flashcards(parts.getOrNull(2)?.toLongOrNull())
                    "revisoes" -> Reviews
                    "erros" -> Errors
                    else -> Train
                }
                "caderno" -> when (second) {
                    "flashcards" -> Flashcards(parts.getOrNull(2)?.toLongOrNull())
                    "praticar" -> Flashcards(parts.getOrNull(2)?.toLongOrNull()?.takeIf { it >= 0 }, saved = true)
                    "baralho" -> parts.getOrNull(2)?.toLongOrNull()?.let { Flashcards(null, saved = true, deckId = it) } ?: Notebook
                    "folheada" -> NotebookFlip
                    "pagina" -> parts.getOrNull(2)?.toLongOrNull()?.let(::NotebookPage) ?: Notebook
                    "guardados" -> NotebookSaved
                    "revisoes" -> parts.getOrNull(2)?.toLongOrNull()?.let(::ReviewSession) ?: Reviews
                    "erros" -> if (parts.getOrNull(2) == "missao") ErrorMission else Errors
                    else -> Notebook
                }
                "revisoes" -> Reviews
                "erros" -> Errors
                "simulados" -> Simulations
                "foco" -> if (second == "historico") FocusHistory else Focus
                "perfil", "mais" -> when (second) {
                    "desempenho" -> Stats
                    "conquistas" -> Achievements
                    "folha" -> Wardrobe
                    "fontes" -> Sources
                    "notificacoes" -> Notifications
                    "plano" -> PlanLimits
                    "ajustes" -> Settings
                    else -> Profile
                }
                "desempenho" -> Stats
                "configurar" -> Setup
                "equipe" -> if (second == "cobertura") AdminCoverage else AdminReports
                else -> Home
            }
        }
    }
}

object Router {
    var current by mutableStateOf(Route.parse(window.location.hash))
        private set

    init {
        window.addEventListener("hashchange", {
            current = Route.parse(window.location.hash)
            window.scrollTo(0.0, 0.0)
            // Trocar de tela confere se o celular publicou algo novo (plano, tarefas concluídas).
            kotlinx.coroutines.MainScope().launch { br.com.estudario.web.data.Store.refreshIfRemoteChanged() }
        })
    }

    fun go(route: Route) {
        window.location.hash = "#/${route.path}"
    }

    fun back() = window.history.back()
}
