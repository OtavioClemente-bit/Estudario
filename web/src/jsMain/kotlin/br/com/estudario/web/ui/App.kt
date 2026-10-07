package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.LoadState
import br.com.estudario.web.data.Progress
import br.com.estudario.web.data.SaveState
import br.com.estudario.web.data.Store
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
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
private data class NavSection(val title: String, val entries: List<NavEntry>)

/** O menu lateral do app (EstudarioDrawer): Estudos, Acompanhamento e Aplicativo. */
private val menu = listOf(
    NavSection(
        "Estudos",
        listOf(
            NavEntry(Route.Home, "Início", "home"),
            NavEntry(Route.Edital, "Meus concursos", "folder_open"),
            NavEntry(Route.Plan, "Plano de estudos", "calendar_month"),
            NavEntry(Route.Train, "Treinar questões", "gps_fixed"),
            NavEntry(Route.Notebook, "Caderno de estudo", "bookmarks"),
            NavEntry(Route.Reviews, "Revisões espaçadas", "autorenew"),
            NavEntry(Route.Errors, "Caderno de erros", "error"),
            NavEntry(Route.Focus, "Modo foco", "timer"),
        ),
    ),
    NavSection(
        "Acompanhamento",
        listOf(
            NavEntry(Route.Stats, "Desempenho", "query_stats"),
            NavEntry(Route.Achievements, "Conquistas", "emoji_events"),
            NavEntry(Route.Sources, "Histórico e fontes", "fact_check"),
            NavEntry(Route.FocusHistory, "Histórico do foco", "history"),
        ),
    ),
    NavSection(
        "Aplicativo",
        listOf(
            NavEntry(Route.Settings, "Ajustes", "tune"),
            NavEntry(Route.Notifications, "Notificações", "notifications_active"),
            NavEntry(Route.PlanLimits, "Planos e uso", "workspace_premium"),
        ),
    ),
)

/** As quatro abas de baixo do app, mais o menu. */
private val tabs = listOf(
    NavEntry(Route.Home, "Início", "home"),
    NavEntry(Route.Edital, "Edital", "checklist"),
    NavEntry(Route.Plan, "Plano", "calendar_month"),
    NavEntry(Route.Train, "Treinar", "school"),
)

private fun titleOf(route: Route): String = when (route) {
    Route.Home -> "Início"
    Route.Plan -> "Plano de estudos"
    Route.PlanSettings -> "Ajustar plano"
    Route.Edital, is Route.Topic -> "Meus concursos"
    Route.Train -> "Treinar questões"
    is Route.Quiz -> "Questões"
    Route.Simulations, is Route.Simulation -> "Simulados"
    Route.Notebook -> "Caderno de estudo"
    is Route.Flashcards -> "Flashcards"
    Route.Reviews -> "Revisões espaçadas"
    Route.Errors -> "Caderno de erros"
    Route.Focus -> "Modo foco"
    Route.FocusHistory -> "Histórico do foco"
    Route.Profile -> "Perfil"
    Route.Stats -> "Desempenho"
    Route.Achievements -> "Conquistas"
    Route.Sources -> "Histórico e fontes"
    Route.Notifications -> "Notificações"
    Route.PlanLimits -> "Planos e uso"
    Route.Settings -> "Ajustes"
    Route.Setup -> "Configurar estudos"
}

private var drawerOpen by mutableStateOf(false)

@Composable
fun App() {
    when (val load = Store.load) {
        LoadState.Loading -> CenterMessage { ProcessScene(220); B({ classes("loading-title") }) { Text("Abrindo seus estudos…") } }
        LoadState.SignedOut -> LoginScreen()
        is LoadState.Failed -> CenterMessage {
            Folha(mood = "thinking", size = 110)
            Text(load.message)
            Btn("Tentar de novo", { Store.start() })
        }
        LoadState.NoData -> Shell { if (Router.current == Route.Setup) SetupScreen() else WelcomeScreen() }
        LoadState.Ready -> Shell { Screen(Router.current) }
    }
    Store.conflict?.let { head -> ConflictDialog(head.deviceLabel) }
    if (Store.load == LoadState.Ready) GenerationBanner()
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
        Route.Simulations -> SimulationsScreen()
        is Route.Simulation -> SimulationScreen(route.id)
        Route.Notebook -> NotebookScreen()
        is Route.Flashcards -> FlashcardsScreen(route.topicId)
        Route.Reviews -> ReviewsScreen()
        Route.Errors -> ErrorsScreen()
        Route.Focus -> FocusScreen()
        Route.FocusHistory -> FocusHistoryScreen()
        Route.Profile -> ProfileScreen()
        Route.Stats -> StatsScreen()
        Route.Achievements -> AchievementsScreen()
        Route.Sources -> SourcesScreen()
        Route.Notifications -> NotificationsScreen()
        Route.PlanLimits -> PlanLimitsScreen()
        Route.Settings -> SettingsScreen()
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
        Aside({ classes(*listOfNotNull("sidebar", if (drawerOpen) "open" else null).toTypedArray()) }) { SidebarContent(section) }
        if (drawerOpen) Div({ classes("drawer-scrim"); onClick { drawerOpen = false } })
        Div({ classes("main") }) {
            Div({ classes("topbar") }) {
                Button({ classes("icon-btn", "menu-btn"); attr("aria-label", "Abrir menu"); onClick { drawerOpen = true } }) { Icon("menu") }
                Span({ classes("title") }) { Text(titleOf(route)) }
                Div({ classes("spacer") })
                SaveIndicator()
                A(href = "#/perfil", { classes("avatar"); attr("aria-label", "Perfil") }) {
                    val session = Store.session
                    val photo = session?.avatarUrl
                    if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
                }
            }
            val narrow = route is Route.Topic || route is Route.Quiz || route is Route.Flashcards || route == Route.Focus || route == Route.Setup
            Main({ classes(*listOfNotNull("content", if (narrow) "narrow" else null).toTypedArray()); id("conteudo") }) {
                if (Store.demo) Div({ classes("banner", "info") }) {
                    Icon("visibility")
                    Span({ attr("style", "flex:1;min-width:200px") }) { Text("Você está vendo uma demonstração com dados de exemplo. Nada é salvo.") }
                    Btn("Entrar com minha conta", { Store.leaveDemo() }, style = "primary", small = true)
                }
                content()
            }
        }
        Nav({ classes("bottomnav"); attr("aria-label", "Seções") }) {
            tabs.forEach { entry -> NavLink(entry, entry.route == section) }
            Button({ classes("nav-item"); onClick { drawerOpen = true } }) { Icon("menu"); Span { Text("Menu") } }
        }
    }
}

@Composable
private fun SidebarContent(section: Route) {
    val session = Store.session
    val progress = Progress.of(Store.data).progress
    // Cabeçalho do menu: o perfil, com nível e XP (como no app).
    A(href = "#/perfil", { classes("drawer-profile"); onClick { drawerOpen = false } }) {
        Span({ classes("avatar"); attr("style", "width:44px;height:44px;font-size:16px") }) {
            val photo = session?.avatarUrl
            if (photo != null) Img(src = photo, alt = "") else Text(initials(session?.name ?: session?.email))
        }
        Div({ attr("style", "min-width:0;flex:1") }) {
            B({ classes("clamp-2") }) { Text(session?.name ?: if (Store.demo) "Demonstração" else "Estudante") }
            Div({ classes("xs", "muted") }) { Text("Nível ${progress.level} · ${progress.totalXp} XP") }
        }
        Icon("chevron_right", extraClass = "faint")
    }
    Nav({ attr("aria-label", "Menu") }) {
        menu.forEach { group ->
            Div({ classes("nav-label") }) { Text(group.title) }
            group.entries.forEach { entry -> NavLink(entry, entry.route == section) }
        }
    }
    Div({ classes("sidebar-foot") }) {
        A(href = "#/inicio", { classes("brand"); attr("style", "padding:8px 12px 0;font-size:15px") }) {
            Img(src = "icon.png", alt = "", attrs = { attr("style", "width:24px;height:24px;border-radius:7px") })
            Text("estudário")
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
        onClick { drawerOpen = false }
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
