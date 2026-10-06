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
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private data class NavEntry(val route: Route, val label: String, val icon: String, val mobile: Boolean = false)

private val navEntries = listOf(
    NavEntry(Route.Home, "Início", "home", mobile = true),
    NavEntry(Route.Plan, "Plano", "calendar_month", mobile = true),
    NavEntry(Route.Edital, "Edital", "menu_book", mobile = true),
    NavEntry(Route.Questions, "Questões", "quiz", mobile = true),
    NavEntry(Route.Reviews, "Revisões", "replay"),
    NavEntry(Route.Errors, "Caderno de erros", "error_med"),
    NavEntry(Route.Simulations, "Simulados", "timer"),
    NavEntry(Route.Stats, "Desempenho", "monitoring"),
)

@Composable
fun App() {
    when (val load = Store.load) {
        LoadState.Loading -> CenterMessage { Spinner(); Text("Carregando seus estudos…") }
        LoadState.SignedOut -> LoginScreen()
        is LoadState.Failed -> CenterMessage {
            Icon("cloud_off")
            Text(load.message)
            Btn("Tentar de novo", { Store.start() })
        }
        LoadState.NoData -> Shell { NoDataScreen() }
        LoadState.Ready -> Shell { Screen(Router.current) }
    }
    Store.conflict?.let { head -> ConflictDialog(head.deviceLabel) }
}

@Composable
private fun Screen(route: Route) {
    when (route) {
        Route.Home -> HomeScreen()
        Route.Plan -> PlanScreen()
        Route.Edital -> EditalScreen()
        is Route.Topic -> TopicScreen(route.id)
        Route.Questions -> QuestionsScreen()
        is Route.Quiz -> QuizScreen(route.scope)
        Route.Reviews -> ReviewsScreen()
        Route.Errors -> ErrorsScreen()
        Route.Simulations -> SimulationsScreen()
        Route.Stats -> StatsScreen()
        Route.Profile -> ProfileScreen()
        Route.Setup -> SetupScreen()
    }
}

@Composable
fun CenterMessage(content: @Composable () -> Unit) {
    Div({ classes("center-page") }) { content() }
}

@Composable
private fun Shell(content: @Composable () -> Unit) {
    val section = Router.current.section
    A(href = "#conteudo", { classes("skip") }) { Text("Pular para o conteúdo") }
    Div({ classes("shell") }) {
        Aside({ classes("sidebar") }) {
            A(href = "#/inicio", { classes("brand") }) {
                Img(src = "icon.png", alt = "")
                Text("Estudário")
            }
            Nav({ attr("aria-label", "Seções") }) {
                navEntries.forEach { entry -> NavLink(entry, entry.route == section) }
            }
            Div({ classes("sidebar-foot") }) {
                Div({ classes("nav-sep") })
                NavLink(NavEntry(Route.Profile, "Perfil e ajustes", "account_circle"), section == Route.Profile)
            }
        }
        Div({ classes("main") }) {
            Div({ classes("topbar") }) {
                Span({ classes("title") }) { Text(navEntries.firstOrNull { it.route == section }?.label ?: "Perfil") }
                Div({ classes("spacer") })
                SaveIndicator()
                A(href = "#/perfil", { classes("avatar"); attr("aria-label", "Perfil") }) {
                    val session = Store.session
                    val photo = session?.avatarUrl
                    if (photo != null) Img(src = photo, alt = "") else Text((session?.name ?: session?.email ?: "?").take(1).uppercase())
                }
            }
            Main({ classes("content"); id("conteudo") }) {
                if (Store.demo) Div({ classes("banner", "info") }) {
                    Icon("visibility")
                    Span({ attr("style", "flex:1") }) { Text("Você está vendo uma demonstração com dados de exemplo. Nada é salvo.") }
                    Btn("Entrar com minha conta", { Store.leaveDemo() }, style = "primary", small = true)
                }
                content()
            }
        }
        Nav({ classes("bottomnav"); attr("aria-label", "Seções") }) {
            navEntries.filter { it.mobile }.forEach { entry -> NavLink(entry, entry.route == section) }
            NavLink(NavEntry(Route.Profile, "Perfil", "account_circle"), section == Route.Profile)
        }
    }
}

@Composable
private fun NavLink(entry: NavEntry, active: Boolean) {
    A(href = "#/${entry.route.path}", {
        classes(*listOfNotNull("nav-item", if (active) "active" else null).toTypedArray())
        if (active) attr("aria-current", "page")
    }) {
        Icon(entry.icon, extraClass = "ico")
        Span { Text(entry.label) }
    }
}

@Composable
private fun SaveIndicator() {
    if (Store.demo) { Span({ classes("save-state", "pending") }) { Span({ classes("led") }); Text("Demonstração") }; return }
    val (css, text) = when (Store.save) {
        SaveState.Saved -> "" to "Sincronizado"
        SaveState.Pending -> "pending" to "Salvando…"
        SaveState.Saving -> "pending" to "Salvando…"
        SaveState.Offline -> "offline" to "Sem conexão: tentando de novo"
    }
    Span({ classes(*listOfNotNull("save-state", css.ifEmpty { null }).toTypedArray()); attr("aria-live", "polite") }) {
        Span({ classes("led") })
        Text(text)
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

@Composable
private fun NoDataScreen() {
    PageHead("Bem-vindo ao Estudário na web", Auth.session?.email?.let { "Conectado como $it" })
    Card {
        Div({ classes("stack") }) {
            H2 { Text("Traga seus estudos do celular") }
            P({ classes("muted") }) {
                Text("No app do Estudário, abra Ajustes › Conta e app web e ligue \"Sincronizar com o app web\". Em instantes seu plano, edital e questões aparecem aqui.")
            }
            Div({ classes("row", "wrap") }) {
                Btn("Já liguei, atualizar", { Store.start() }, icon = "refresh")
                Btn("Começar do zero aqui", { Store.startEmpty(); Router.go(Route.Setup) }, style = "outline")
            }
        }
    }
}
