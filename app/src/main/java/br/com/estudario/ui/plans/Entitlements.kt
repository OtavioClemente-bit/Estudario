package br.com.estudario.ui.plans

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import br.com.estudario.EstudarioApplication
import java.util.Calendar

/**
 * O que o plano da conta permite no próprio aparelho: editais salvos e planos de estudo (montados
 * aqui, sem passar pelo servidor). Os números vêm da função ai-plan, os mesmos do site. Sem conta
 * ou sem resposta, nada é barrado: a geração pela IA continua conferida no servidor.
 */
data class Entitlements(
    val tier: String,
    /** Editais (concursos) salvos ao mesmo tempo. */
    val savedEditais: Int?,
    /** Planos de estudo que dá para criar no período (LIFETIME no Grátis, MONTHLY nos pagos). */
    val plans: Int?,
    val plansPeriod: String?,
) {
    val free: Boolean get() = tier == "FREE"

    fun canAddEdital(competitions: Int): Boolean = savedEditais == null || competitions < savedEditais

    /** createdAt dos planos que existem; no Grátis contam todos (ele não apaga plano). */
    fun canCreatePlan(planCreatedAts: List<Long>): Boolean {
        val max = plans ?: return true
        if (plansPeriod != "MONTHLY") return planCreatedAts.size < max
        val monthStart = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return planCreatedAts.count { it >= monthStart } < max
    }

    /** No Grátis o plano é um só: ajusta, mas não apaga nem duplica (isso é dos pagos). */
    val canDeletePlan: Boolean get() = !free
}

/** Carrega uma vez por tela; null enquanto carrega ou sem conta (nada é barrado). */
@Composable
fun rememberEntitlements(): Entitlements? {
    val context = LocalContext.current
    var value by remember { mutableStateOf<Entitlements?>(null) }
    LaunchedEffect(Unit) {
        val app = context.applicationContext as? EstudarioApplication ?: return@LaunchedEffect
        val summary = (app.aiPlanRepository.load() as? AiPlanLoadResult.Available)?.summary ?: return@LaunchedEffect
        val limits = summary.plans.firstOrNull { it.planTier == summary.planTier }?.limits.orEmpty()
        val editais = limits.firstOrNull { it.feature == "SAVED_EDITAIS" }
        val plans = limits.firstOrNull { it.feature == "PLAN_GENERATION" }
        value = Entitlements(summary.planTier, editais?.quotaLimit, plans?.quotaLimit, plans?.periodKind)
    }
    return value
}
