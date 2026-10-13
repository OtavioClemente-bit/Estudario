package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.AiJobs
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

const val PLAY_URL = "https://play.google.com/store/apps/details?id=br.com.estudario"

/** Qual limite estourou: muda o título e o que a janela promete (igual ao app). */
enum class UpgradeReason(val title: String, val body: String, val perks: List<String>) {
    CONTENT(
        "Suas matérias deste mês acabaram",
        "No Essencial são 25 matérias por mês, com teoria, flashcards, dicas e até 20 questões no estilo da sua banca. No Pro, 40, com até 30 questões.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    SYLLABUS(
        "Edital pelo seu PDF é dos planos pagos",
        "No Grátis você usa os editais do catálogo. Para mandar o PDF do seu concurso e ter as matérias e tópicos na ordem exata do edital, assine: 2 por mês no Essencial, 5 no Pro.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    SIMULATION(
        "Simulados completos são dos planos pagos",
        "No Grátis você faz o simulado diagnóstico. No Essencial são 3 simulados por mês no estilo da sua banca, com nota por matéria. No Pro, 6.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    QUESTIONS(
        "Seus lotes de questões acabaram",
        "No Grátis cada matéria tem 1 lote extra. No Essencial são 10 lotes de 10 questões por mês; no Pro, 25, sem limite por matéria.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    EDITAIS(
        "Você chegou ao limite de editais salvos",
        "No Grátis você guarda até 5 editais. No Essencial são 15 e no Pro não há limite.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    PLANS(
        "Novos planos de estudo são dos planos pagos",
        "No Grátis você tem um plano de estudo e pode ajustá-lo quando quiser; o concurso novo entra nele. No Essencial dá para criar 3 planos por mês, e no Pro, 10.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    ),
    GENERIC(
        "Seu plano chegou no limite",
        "Os planos pagos liberam mais matérias, lotes de questões, simulados e o seu próprio edital em PDF.",
        listOf("25 ou 40 matérias por mês", "Lotes de 10 questões extras sem limite por matéria", "Simulados completos no estilo da banca", "Seu próprio edital em PDF"),
    );
}

/** A mensagem de erro é a de cota esgotada? (as telas só têm o texto, não o código) */
fun isQuotaMessage(message: String?): Boolean =
    message != null && (message == AiJobs.message("QUOTA_EXHAUSTED") || message == AiJobs.message("DEVICE_QUOTA_EXHAUSTED") || "do seu plano" in message)

/**
 * A janela de upgrade do site: diz o que acabou e o que o plano pago dá. A assinatura é feita no
 * app (Google Play) e vale na mesma conta aqui; o botão principal leva para a página de planos.
 */
@Composable
fun UpgradeModal(reason: UpgradeReason, onDismiss: () -> Unit) {
    Modal(onDismiss = onDismiss) {
        FolhaSays("talking", 72) { H2 { Text(reason.title) } }
        P({ classes("muted") }) { Text(reason.body) }
        Ul({ classes("perks") }) { reason.perks.forEach { perk -> Li { Icon("check", extraClass = "ok"); Span { Text(perk) } } } }
        P({ classes("small", "muted") }) { Text("Essencial a partir de R$ 24,90 por mês. Você assina pelo app, na Google Play, e o plano vale aqui na mesma conta.") }
        Div({ classes("stack", "tight") }) {
            Btn("Ver planos", { onDismiss(); Router.go(Route.PlanLimits) }, style = "primary", block = true, icon = "workspace_premium")
            Btn("Agora não", onDismiss, style = "ghost", block = true)
        }
    }
}
