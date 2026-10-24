package br.com.estudario.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import br.com.estudario.web.Route
import br.com.estudario.web.Router
import br.com.estudario.web.data.AiJobs
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.KeyboardEvent

const val PLAY_URL = "https://play.google.com/store/apps/details?id=br.com.estudario"

/**
 * O que levou a pessoa até a oferta (igual ao app, UpgradeDialog.kt). Os números são os dos planos
 * de verdade (ai_plan_limits); se mudarem lá, mudam aqui, no app e em site/index.html.
 */
enum class UpgradeReason(val title: String, val body: String) {
    CONTENT("Seus materiais deste mês acabaram", "Cada material traz teoria, resumo, flashcards, dicas e questões comentadas do tópico. Com um plano, você segue estudando hoje mesmo."),
    SYLLABUS("Mande o edital do seu concurso", "O edital pelo seu PDF é dos planos Essencial e Pro: as matérias e os tópicos saem organizados para você revisar e estudar."),
    SIMULATION("Simulados no estilo da sua banca", "Treine com cronômetro e nota por matéria, no formato da prova. Os simulados completos são dos planos Essencial e Pro."),
    QUESTIONS("Seus lotes de questões acabaram", "Mais questões comentadas no estilo da banca, para treinar o tópico até ele ficar firme."),
    EDITAIS("Você chegou ao limite de editais salvos", "No Grátis você guarda até 5 editais. No Essencial são 15, e no Pro não há limite."),
    PLANS("Mais planos de estudo", "No Grátis você tem um plano e pode ajustá-lo quando quiser. Com o Essencial ou o Pro, dá para montar planos novos todo mês."),
    GENERIC("Estude sem parar no meio", "Os planos Essencial e Pro liberam mais materiais, questões, simulados e o edital pelo seu PDF."),
    EXTRAS("Esta matéria ainda não está no seu plano", "No Grátis, você estuda as matérias que o seu plano traz a cada dia e pode adiantar as próximas. Com o Essencial, você abre qualquer matéria do edital, na hora que quiser, e adianta o plano sem anúncio."),
    ADVANCE("Adiante o seu plano", "Seu adiantamento grátis desta semana já foi usado. No app do Estudário, um anúncio curto libera a próxima matéria (até 2 por dia). Com o Essencial, você adianta quantas quiser, sem anúncio."),
    PLAN_DAY("O plano de hoje já está com você", "As matérias de hoje já saíram. Amanhã chegam as próximas. Com um plano, você estuda no seu ritmo."),
    NOT_READY("Esta matéria está em produção", "Nossa equipe está preparando esta matéria, e ela aparece aqui assim que ficar pronta. Com o Essencial, o Estudário escreve agora mesmo, com fontes oficiais."),
}

/** A recusa que vira oferta, pela mensagem (as telas só têm o texto); null quando é erro de verdade. */
fun upgradeReasonFor(message: String?, questionsOnly: Boolean = false): UpgradeReason? = when (message) {
    null -> null
    AiJobs.message("FREE_EXTRA_LIMIT") -> UpgradeReason.EXTRAS
    AiJobs.message("FREE_ADVANCE_NEEDS_AD") -> UpgradeReason.ADVANCE
    AiJobs.message("FREE_PLAN_DAY_LIMIT") -> UpgradeReason.PLAN_DAY
    AiJobs.message("PLAN_AI_MATERIAL_LIMIT") -> UpgradeReason.NOT_READY
    else -> if (isQuotaMessage(message)) (if (questionsOnly) UpgradeReason.QUESTIONS else UpgradeReason.CONTENT) else null
}

/** A mensagem de erro é a de cota esgotada? (as telas só têm o texto, não o código) */
fun isQuotaMessage(message: String?): Boolean =
    message != null && (message == AiJobs.message("QUOTA_EXHAUSTED") || message == AiJobs.message("DEVICE_QUOTA_EXHAUSTED") || "do seu plano" in message)

private class OfferPlan(val tier: String, val name: String, val price: String, val glyph: String, val lines: List<Pair<String, String>>)

private val offerPlans = listOf(
    OfferPlan("essencial", "Essencial", "R$ 24,90", "star", listOf("25" to "matérias extras por mês", "10" to "lotes de questões", "3" to "simulados por mês", "2" to "editais pelo PDF")),
    OfferPlan("pro", "Pro", "R$ 49,90", "diamond", listOf("40" to "matérias extras por mês", "25" to "lotes de questões", "6" to "simulados por mês", "5" to "editais pelo PDF")),
)

/**
 * A oferta de plano em tela cheia, como no app: fundo de tinta do Folha com brilho dourado, o Folha
 * acenando, os dois planos lado a lado e o botão dourado que leva à escolha de planos. "Agora não"
 * (ou Esc) fecha sem insistir. A assinatura é feita no app, pela Google Play, e vale aqui.
 */
@Composable
fun UpgradeModal(reason: UpgradeReason, onDismiss: () -> Unit) {
    DisposableEffect(Unit) {
        val listener: (org.w3c.dom.events.Event) -> Unit = { if ((it as KeyboardEvent).key == "Escape") onDismiss() }
        window.addEventListener("keydown", listener)
        onDispose { window.removeEventListener("keydown", listener) }
    }
    Div({ classes("offer"); attr("role", "dialog"); attr("aria-modal", "true") }) {
        Div({ classes("offer-glow") })
        Div({ classes("offer-in") }) {
            Button({ classes("offer-x"); attr("aria-label", "Fechar"); onClick { onDismiss() } }) { Text("✕") }
            Folha("wave", 110)
            H2({ classes("offer-title") }) { Text(reason.title) }
            P({ classes("offer-body") }) { Text(reason.body) }
            Div({ classes("offer-cards") }) {
                offerPlans.forEach { plan ->
                    Div({ classes("offer-card", "tier-" + plan.tier) }) {
                        Div({ classes("row"); attr("style", "gap:8px") }) { Icon(plan.glyph); B { Text(plan.name) } }
                        B({ classes("offer-price") }) { Text(plan.price) }
                        Span({ classes("offer-unit") }) { Text("por mês") }
                        plan.lines.forEach { (value, label) ->
                            Div({ classes("offer-line") }) { Span({ classes("offer-num") }) { Text(value) }; Span { Text(label) } }
                        }
                    }
                }
            }
            P({ classes("offer-note") }) { Text("No Grátis continuam a matéria do plano de hoje, as revisões, o treino e o caderno.") }
            Button({ classes("offer-cta"); onClick { onDismiss(); Router.go(Route.PlanLimits) } }) { Text("Conhecer os planos") }
            Button({ classes("offer-later"); onClick { onDismiss() } }) { Text("Agora não") }
            P({ classes("offer-fine") }) { Text("Assinatura pelo app, na Google Play. Vale aqui na mesma conta. Cancela quando quiser.") }
        }
    }
}
