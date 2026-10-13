package br.com.estudario.web.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.estudario.web.WebConfig
import kotlin.js.Date
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject

/**
 * O que o plano da conta permite no próprio navegador: editais salvos e planos de estudo (montados
 * aqui, sem passar pelo servidor). Os números vêm da função ai-plan, os mesmos do app. Sem conta ou
 * sem resposta, nada é barrado: a geração pela IA continua conferida no servidor.
 */
object Entitlements {
    data class Limits(
        val tier: String,
        /** Editais (concursos) salvos ao mesmo tempo. */
        val savedEditais: Int?,
        /** Planos de estudo que dá para criar no período (LIFETIME no Grátis, MONTHLY nos pagos). */
        val plans: Int?,
        val plansPeriod: String?,
    )

    var current by mutableStateOf<Limits?>(null)
        private set

    suspend fun load(): Limits? {
        current?.let { return it }
        if (Store.demo) return null
        val token = Auth.accessToken() ?: return null
        val response = runCatching {
            httpRequest("GET", "${WebConfig.SUPABASE_URL}/functions/v1/ai-plan", mapOf("Authorization" to "Bearer $token", "apikey" to WebConfig.SUPABASE_KEY, "Accept" to "application/json", "x-estudario-client" to "web"))
        }.getOrNull()?.takeIf { it.ok } ?: return null
        val limits = runCatching { parse(response.body) }.getOrNull() ?: return null
        current = limits
        return limits
    }

    private fun parse(body: String): Limits {
        val root = Json.parseToJsonElement(body).jsonObject
        fun JsonObject.s(k: String) = (this[k] as? JsonPrimitive)?.contentOrNull
        fun JsonObject.i(k: String) = (this[k] as? JsonPrimitive)?.intOrNull
        val tier = root.s("planTier") ?: "FREE"
        val rows = (root["plans"] as? JsonArray).orEmpty().map { it.jsonObject }.firstOrNull { it.s("planTier") == tier }
            ?.let { (it["limits"] as? JsonArray).orEmpty().map { l -> l.jsonObject } }.orEmpty()
        val editais = rows.firstOrNull { it.s("feature") == "SAVED_EDITAIS" }
        val plans = rows.firstOrNull { it.s("feature") == "PLAN_GENERATION" }
        return Limits(tier, editais?.i("quotaLimit"), plans?.i("quotaLimit"), plans?.s("periodKind"))
    }

    val free: Boolean get() = current?.tier == "FREE"

    /** Ainda cabe mais um edital salvo? */
    fun canAddEdital(data: Snapshot): Boolean {
        val max = current?.savedEditais ?: return true
        return data.competitions.size < max
    }

    /** Planos criados no período do limite (no Grátis, todos os que existem; ele não apaga plano). */
    private fun plansInPeriod(data: Snapshot): Int {
        val limits = current ?: return 0
        if (limits.plansPeriod != "MONTHLY") return data.plans.size
        val now = Date()
        val monthStart = Date(now.getFullYear(), now.getMonth(), 1).getTime().toLong()
        return data.array(Keys.PLANS).count { ((it as? JsonObject)?.get("createdAt") as? JsonPrimitive)?.contentOrNull?.toLongOrNull()?.let { at -> at >= monthStart } ?: false }
    }

    /** Dá para criar um plano novo? Se não, o concurso novo entra no plano que já existe. */
    fun canCreatePlan(data: Snapshot): Boolean {
        val max = current?.plans ?: return true
        return plansInPeriod(data) < max
    }

    /** No Grátis o plano é um só: ajusta, mas não apaga (apagar e criar outro é dos pagos). */
    fun canDeletePlan(): Boolean = !free
}
