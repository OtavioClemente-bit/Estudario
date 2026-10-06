package br.com.estudario.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.browser.window

/** Endereços do app: #/inicio, #/plano, #/edital/12 ... (hash para funcionar em hospedagem estática). */
sealed class Route(val path: String) {
    data object Home : Route("inicio")
    data object Plan : Route("plano")
    data object Edital : Route("edital")
    data class Topic(val id: Long) : Route("edital/$id")
    data object Questions : Route("questoes")
    data class Quiz(val scope: String) : Route("questoes/treino/$scope")
    data object Reviews : Route("revisoes")
    data object Errors : Route("erros")
    data object Simulations : Route("simulados")
    data object Stats : Route("desempenho")
    data object Profile : Route("perfil")

    /** Item do menu ao qual esta rota pertence. */
    val section: Route
        get() = when (this) {
            is Topic -> Edital
            is Quiz -> Questions
            else -> this
        }

    companion object {
        fun parse(hash: String): Route {
            val parts = hash.removePrefix("#").removePrefix("/").split('/').filter { it.isNotEmpty() }
            return when (parts.firstOrNull()) {
                null, "inicio" -> Home
                "plano" -> Plan
                "edital" -> parts.getOrNull(1)?.toLongOrNull()?.let(::Topic) ?: Edital
                "questoes" -> if (parts.getOrNull(1) == "treino") Quiz(parts.getOrNull(2) ?: "todas") else Questions
                "revisoes" -> Reviews
                "erros" -> Errors
                "simulados" -> Simulations
                "desempenho" -> Stats
                "perfil" -> Profile
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
