package br.com.estudario.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.window

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
    data class Flashcards(val topicId: Long?) : Route(if (topicId == null) "caderno/flashcards" else "caderno/flashcards/$topicId")
    data object Reviews : Route("caderno/revisoes")
    data object Errors : Route("caderno/erros")
    data object Focus : Route("foco")
    data object FocusHistory : Route("foco/historico")
    data object Profile : Route("perfil")
    data object Stats : Route("perfil/desempenho")
    data object Achievements : Route("perfil/conquistas")
    data object Sources : Route("perfil/fontes")
    data object Notifications : Route("perfil/notificacoes")
    data object PlanLimits : Route("perfil/plano")
    data object Settings : Route("perfil/ajustes")
    data object Setup : Route("configurar")

    /** Item do menu ao qual esta rota pertence. */
    val section: Route
        get() = when (this) {
            is Topic, Setup -> Edital
            PlanSettings -> Plan
            is Quiz, Simulations, is Simulation -> Train
            is Flashcards -> Notebook
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
                    "revisoes" -> Reviews
                    "erros" -> Errors
                    else -> Notebook
                }
                "revisoes" -> Reviews
                "erros" -> Errors
                "simulados" -> Simulations
                "foco" -> if (second == "historico") FocusHistory else Focus
                "perfil", "mais" -> when (second) {
                    "desempenho" -> Stats
                    "conquistas" -> Achievements
                    "fontes" -> Sources
                    "notificacoes" -> Notifications
                    "plano" -> PlanLimits
                    "ajustes" -> Settings
                    else -> Profile
                }
                "desempenho" -> Stats
                "configurar" -> Setup
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
        })
    }

    fun go(route: Route) {
        window.location.hash = "#/${route.path}"
    }

    fun back() = window.history.back()
}
