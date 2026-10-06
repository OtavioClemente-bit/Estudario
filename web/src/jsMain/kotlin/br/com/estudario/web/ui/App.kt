package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.Auth
import br.com.estudario.web.data.LoadState
import br.com.estudario.web.data.SaveState
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text


private data class NavEntry(val route: Route, val label: String, val icon: String)

/** As cinco abas do app, na mesma ordem. */
private val navEntries = listOf(
    NavEntry(Route.Home, "Início", "home"),
    NavEntry(Route.Edital, "Edital", "checklist"),
    NavEntry(Route.Plan, "Plano", "calendar_month"),
    NavEntry(Route.Train, "Treinar", "school"),
    NavEntry(Route.More, "Mais", "more_horiz"),
)

/** Atalhos que no app ficam dentro de Treinar e Mais; no computador cabem no menu lateral. */
private val shortcutEntries = listOf(
    NavEntry(Route.Reviews, "Revisões", "replay"),
    NavEntry(Route.Errors, "Caderno de erros", "error_med"),
    NavEntry(Route.Flashcards(null), "Flashcards", "style"),
    NavEntry(Route.Simulations, "Simulados", "timer"),
    NavEntry(Route.Stats, "Desempenho", "monitoring"),
)

private fun titleOf(route: Route): String = when (route) {
    Route.Home -> "Início"
    Route.Plan -> "Plano"
    Route.PlanSettings -> "Ajustar plano"
    Route.Edital, is Route.Topic -> "Edital"
    Route.Train -> "Treinar"
    is Route.Quiz -> "Questões"
    is Route.Flashcards -> "Flashcards"
    Route.Reviews -> "Revisões"
    Route.Errors -> "Caderno de erros"
    Route.Simulations -> "Simulados"
    is Route.Simulation -> "Simulado"
    Route.More -> "Mais"
    Route.Stats -> "Desempenho"
    Route.Achievements -> "Conquistas"
    Route.Settings -> "Ajustes"
    Route.Focus -> "Modo foco"
    Route.Setup -> "Configurar estudos"
}

@Composable
fun App() {
    when (val load = Store.load) {
        LoadState.Loading -> CenterMessage { Spinner(); Text("Carregando seus estudos…") }
        LoadState.SignedOut -> LoginScreen()
        is LoadState.Failed -> CenterMessage {
            Icon("cloud_off", extraClass = "big")
            Text(load.message)
            Btn("Tentar de novo", { Store.start() })
        }
        LoadState.NoData -> Shell { if (Router.current == Route.Setup) SetupScreen() else WelcomeScreen() }
        LoadState.Ready -> Shell { Screen(Router.current) }
    }
    Store.conflict?.let { head -> ConflictDialog(head.deviceLabel) }
    ToastHost()
}

@Composable
private fun Screen(route: Route) {
    when (route) {
        Route.Home -> HomeScreen()
        Route.Plan -> PlanScreen()
        Route.PlanSettings -> PlanSettingsScreen()
        Route.Edital -> EditalScreen()
        is Route.Topic -> TopicScreen(route.id)
        Route.Train -> TrainScreen()
        is Route.Quiz -> QuizScreen(route.scope)
        is Route.Flashcards -> FlashcardsScreen(route.topicId)
        Route.Reviews -> ReviewsScreen()
        Route.Errors -> ErrorsScreen()
        Route.Simulations -> SimulationsScreen()
        is Route.Simulation -> SimulationScreen(route.id)
        Route.More -> MoreScreen()
        Route.Stats -> StatsScreen()
        Route.Achievements -> AchievementsScreen()
        Route.Settings -> SettingsScreen()
        Route.Focus -> FocusScreen()
        Route.Setup -> SetupScreen()
    }
}

@Composable
fun CenterMessage(content: @Composable () -> Unit) {
    Div({ classes("center-page") }) { content() }
}

@Composable
private fun Shell(content: @Composable () -> Unit) {
    val route = Router.current
    val section = route.section
    A(href = "#conteudo", { classes("skip") }) { Text("Pular para o conteúdo") }
    Div({ classes("shell") }) {
        Aside({ classes("sidebar") }) {
            A(href = "#/inicio", { classes("brand") }) {
                Img(src = "icon.png", alt = "")
                Text("estudário")
            }
            Nav({ attr("aria-label", "Seções") }) {
                navEntries.forEach { entry -> NavLink(entry, entry.route == section) }
                Div({ classes("nav-label") }) { Text("Atalhos") }
                shortcutEntries.forEach { entry -> NavLink(entry, entry.route == route || (entry.route is Route.Flashcards && route is Route.Flashcards)) }
            }
            Div({ classes("sidebar-foot") }) {
                A(href = "#/foco", { classes("focus-cta") }) {
                    Icon("center_focus_strong", filled = true)
                    Div {
                        Text("Modo foco")
                        org.jetbrains.compose.web.dom.Small { Text("Cronômetro para estudar sem distração") }
                    }
                }
            }
        }
        Div({ classes("main") }) {
            Div({ classes("topbar") }) {
                Span({ classes("title") }) { Text(titleOf(route)) }
                Div({ classes("spacer") })
                SaveIndicator()
                A(href = "#/mais", { classes("avatar"); attr("aria-label", "Perfil") }) {
                    val session = Store.session
                    val photo = session?.avatarUrl
                    if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
                }
            }
            Main({ classes(*listOfNotNull("content", if (route is Route.Topic || route is Route.Quiz || route is Route.Flashcards || route == Route.Focus || route == Route.Setup) "narrow" else null).toTypedArray()); id("conteudo") }) {
                if (Store.demo) Div({ classes("banner", "info") }) {
                    Icon("visibility")
                    Span({ attr("style", "flex:1;min-width:200px") }) { Text("Você está vendo uma demonstração com dados de exemplo. Nada é salvo.") }
                    Btn("Entrar com minha conta", { Store.leaveDemo() }, style = "primary", small = true)
                }
                content()
            }
        }
        Nav({ classes("bottomnav"); attr("aria-label", "Seções") }) {
            navEntries.forEach { entry -> NavLink(entry, entry.route == section) }
        }
    }
}

fun initials(name: String?): String {
    val parts = name.orEmpty().substringBefore('@').split(' ', '.', '_').filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().first()}${parts.last().first()}".uppercase()
    }
}

@Composable
private fun NavLink(entry: NavEntry, active: Boolean) {
    A(href = "#/${entry.route.path}", {
        classes(*listOfNotNull("nav-item", if (active) "active" else null).toTypedArray())
        if (active) attr("aria-current", "page")
    }) {
        Icon(entry.icon)
        Span { Text(entry.label) }
    }
}

@Composable
private fun SaveIndicator() {
    val (css, text) = when {
        Store.demo -> "pending" to "Demonstração"
        Store.save == SaveState.Saved -> "" to "Sincronizado"
        Store.save == SaveState.Offline -> "offline" to "Sem conexão"
        else -> "pending" to "Salvando…"
    }
    Span({ classes(*listOfNotNull("save-state", css.ifEmpty { null }).toTypedArray()); attr("aria-live", "polite"); attr("title", text) }) {
        Span({ classes("led") })
        Span({ classes("txt") }) { Text(text) }
    }
}

@Composable
private fun ConflictDialog(device: String?) {
    Modal(onDismiss = {}) {
        H2 { Text("Seus dados mudaram em outro aparelho") }
        P({ classes("muted") }) {
            Text(
                "Enquanto você usava o site, ${device ?: "outro aparelho"} enviou mudanças. " +
                    "Escolha qual versão manter: a outra continua guardada no histórico da sua conta.",
            )
        }
        Div({ classes("stack") }) {
            Btn("Carregar a versão da conta", { Store.takeTheirs() }, style = "primary", block = true)
            Btn("Manter o que fiz aqui", { Store.keepMine() }, style = "outline", block = true)
        }
    }
}

/** Conta nova (ou sem nada sincronizado): começa por aqui, como o primeiro acesso do app. */
@Composable
private fun WelcomeScreen() {
    val name = Store.session?.name?.substringBefore(' ')
    Div({ classes("hero") }) {
        Div({ classes("stack") }) {
            Span({ classes("eyebrow"); attr("style", "color:rgba(255,255,255,.8)") }) { Text("Comece com um plano") }
            H1 { Text(if (name != null) "Bem-vindo, $name!" else "Bem-vindo ao Estudário!") }
            P({ classes("muted"); attr("style", "max-width:560px") }) {
                Text("Em poucos passos você escolhe seu concurso, a data da prova e as horas de cada dia. O Estudário monta um plano que cabe na sua vida, com teoria, questões e revisões.")
            }
            Div({ classes("row", "wrap"); attr("style", "margin-top:6px") }) {
                Btn("Configurar meus estudos", { Store.startEmpty(); Router.go(Route.Setup) }, style = "white", icon = "auto_awesome", )
            }
        }
    }
    Div({ classes("grid", "cols-3") }) {
        listOf(
            Triple("checklist", "Seu edital organizado", "Matérias e tópicos do edital oficial, com o progresso de cada um."),
            Triple("calendar_month", "Um plano que se ajusta", "Se você atrasar ou adiantar, o plano se reorganiza sozinho."),
            Triple("school", "Treine com intenção", "Questões, revisões espaçadas, flashcards e caderno de erros."),
        ).forEach { (icon, title, text) ->
            Card {
                Div({ classes("stack", "tight") }) {
                    Icon(icon, extraClass = "big")
                    org.jetbrains.compose.web.dom.H3 { Text(title) }
                    P({ classes("muted", "small") }) { Text(text) }
                }
            }
        }
    }
    Card(extra = "soft") {
        Div({ classes("row", "wrap", "between") }) {
            Div({ classes("grow") }) {
                org.jetbrains.compose.web.dom.H3 { Text("Já usa o app no celular?") }
                P({ classes("muted", "small") }) { Text("No app, abra Mais › Ajustes › Conta e app web e ligue a sincronização. Seus estudos aparecem aqui.") }
            }
            Btn("Já liguei, atualizar", { Store.start() }, style = "outline", icon = "refresh")
        }
    }
}
