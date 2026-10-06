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
    data class Flashcards(val topicId: Long?) : Route(if (topicId == null) "treinar/flashcards" else "treinar/flashcards/$topicId")
    data object Reviews : Route("treinar/revisoes")
    data object Errors : Route("treinar/erros")
    data object Simulations : Route("treinar/simulados")
    data class Simulation(val id: Long) : Route("treinar/simulados/$id")
    data object More : Route("mais")
    data object Stats : Route("mais/desempenho")
    data object Achievements : Route("mais/conquistas")
    data object Settings : Route("mais/ajustes")
    data object Focus : Route("foco")
    data object Setup : Route("configurar")

    /** Item do menu ao qual esta rota pertence. */
    val section: Route
        get() = when (this) {
            is Topic -> Edital
            PlanSettings -> Plan
            is Quiz, is Flashcards, Reviews, Errors, Simulations, is Simulation -> Train
            Stats, Achievements, Settings, Setup -> More
            else -> this
        }

    companion object {
        fun parse(hash: String): Route {
            val parts = hash.removePrefix("#").removePrefix("/").split('/').filter { it.isNotEmpty() }
            return when (parts.firstOrNull()) {
                null, "inicio", "demo" -> Home
                "plano" -> if (parts.getOrNull(1) == "ajustar") PlanSettings else Plan
                "edital" -> parts.getOrNull(1)?.toLongOrNull()?.let(::Topic) ?: Edital
                "treinar", "questoes" -> when (parts.getOrNull(1)) {
                    "questoes", "treino" -> Quiz(parts.getOrNull(2) ?: "rapido-10")
                    "flashcards" -> Flashcards(parts.getOrNull(2)?.toLongOrNull())
                    "revisoes" -> Reviews
                    "erros" -> Errors
                    "simulados" -> parts.getOrNull(2)?.toLongOrNull()?.let(::Simulation) ?: Simulations
                    else -> Train
                }
                "revisoes" -> Reviews
                "erros" -> Errors
                "simulados" -> Simulations
                "mais", "perfil" -> when (parts.getOrNull(1)) {
                    "desempenho" -> Stats
                    "conquistas" -> Achievements
                    "ajustes" -> Settings
                    else -> More
                }
                "desempenho" -> Stats
                "foco" -> Focus
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
