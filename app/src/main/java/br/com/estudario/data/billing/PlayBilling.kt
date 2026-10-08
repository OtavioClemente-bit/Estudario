package br.com.estudario.data.billing

import android.app.Activity
import android.content.Context
import br.com.estudario.data.ai.AiHttpRequest
import br.com.estudario.data.ai.AiHttpTransport
import br.com.estudario.data.ai.UrlConnectionAiHttpTransport
import br.com.estudario.data.remote.SupabaseAuthRepository
import br.com.estudario.data.remote.SupabaseClientConfig
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume

/** O que aconteceu com uma compra, para a tela reagir. */
sealed interface PurchaseEvent {
    /** O servidor confirmou com o Google e o plano da conta já mudou. */
    data class Activated(val planTier: String) : PurchaseEvent
    /** Compra em aprovação (boleto, por exemplo): o plano muda quando o pagamento cair. */
    data object Pending : PurchaseEvent
    data object Cancelled : PurchaseEvent
    data class Failed(val message: String) : PurchaseEvent
}

/**
 * Assinaturas pelo Google Play.
 *
 * O app nunca decide sozinho que alguém é assinante: cada compra (nova ou já existente ao abrir o
 * app) vai para a função play-billing, que confere o token com a API do Google e troca o plano da
 * conta. Só depois disso a compra é reconhecida; sem reconhecer em 3 dias o Google estorna.
 */
class PlayBilling(
    context: Context,
    private val config: SupabaseClientConfig,
    private val auth: SupabaseAuthRepository,
    private val transport: AiHttpTransport = UrlConnectionAiHttpTransport(config.projectUrl),
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _events = MutableStateFlow<PurchaseEvent?>(null)
    val events: StateFlow<PurchaseEvent?> = _events.asStateFlow()

    private val listener = PurchasesUpdatedListener { result, purchases ->
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> purchases.orEmpty().forEach { purchase -> scope.launch { handle(purchase) } }
            BillingClient.BillingResponseCode.USER_CANCELED -> _events.value = PurchaseEvent.Cancelled
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> scope.launch { restore() }
            else -> _events.value = PurchaseEvent.Failed("A compra não foi concluída (${result.debugMessage.ifBlank { result.responseCode.toString() }}).")
        }
    }

    private val client: BillingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(listener)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    fun consumeEvent() { _events.value = null }

    private suspend fun connect(): Boolean {
        if (client.isReady) return true
        return suspendCancellableCoroutine { continuation ->
            client.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (continuation.isActive) continuation.resume(result.responseCode == BillingClient.BillingResponseCode.OK)
                }
                override fun onBillingServiceDisconnected() {
                    if (continuation.isActive) continuation.resume(false)
                }
            })
        }
    }

    /** Detalhes (preço formatado pela loja, ofertas) dos produtos de assinatura. Vazio se a loja não respondeu. */
    suspend fun products(productIds: List<String>): List<ProductDetails> {
        if (productIds.isEmpty() || !connect()) return emptyList()
        val params = QueryProductDetailsParams.newBuilder().setProductList(
            productIds.map { QueryProductDetailsParams.Product.newBuilder().setProductId(it).setProductType(BillingClient.ProductType.SUBS).build() },
        ).build()
        val result = runCatching { client.queryProductDetails(params) }.getOrNull() ?: return emptyList()
        if (result.billingResult.responseCode != BillingClient.BillingResponseCode.OK) return emptyList()
        return result.productDetailsList.orEmpty()
    }

    /** Abre a tela de compra do Google para o plano base ([basePlanId]) do produto. */
    suspend fun subscribe(activity: Activity, product: ProductDetails, basePlanId: String): Boolean {
        if (!connect()) { _events.value = PurchaseEvent.Failed("A Google Play não está disponível neste aparelho."); return false }
        val offer = product.subscriptionOfferDetails.orEmpty()
            .filter { it.basePlanId == basePlanId }
            // Oferta com desconto (teste grátis, promoção) vem antes da base pura.
            .maxByOrNull { if (it.offerId != null) 1 else 0 }
            ?: run { _events.value = PurchaseEvent.Failed("Este plano não está à venda agora."); return false }
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(product).setOfferToken(offer.offerToken).build()))
            .build()
        val result = client.launchBillingFlow(activity, params)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    /** Ao abrir o app e ao entrar na conta: confirma com o servidor as assinaturas que o Google já tem. */
    suspend fun restore(): PurchaseEvent? {
        if (!config.isConfigured || auth.accessToken() == null || !connect()) return null
        val result = runCatching {
            client.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build())
        }.getOrNull() ?: return null
        var last: PurchaseEvent? = null
        result.purchasesList.forEach { purchase -> last = handle(purchase) }
        return last
    }

    private suspend fun handle(purchase: Purchase): PurchaseEvent? {
        val event = when (purchase.purchaseState) {
            Purchase.PurchaseState.PENDING -> PurchaseEvent.Pending
            Purchase.PurchaseState.PURCHASED -> verify(purchase)
            else -> null
        }
        if (event != null) _events.value = event
        return event
    }

    private suspend fun verify(purchase: Purchase): PurchaseEvent = withContext(Dispatchers.IO) {
        val token = auth.accessToken() ?: return@withContext PurchaseEvent.Failed("Entre na sua conta para ligar a assinatura a ela.")
        val productId = purchase.products.firstOrNull() ?: return@withContext PurchaseEvent.Failed("Compra sem produto.")
        val body = JSONObject().put("purchaseToken", purchase.purchaseToken).put("productId", productId).toString()
        val response = runCatching {
            transport.execute(
                AiHttpRequest(
                    method = "POST",
                    path = "/functions/v1/play-billing",
                    headers = mapOf("Authorization" to "Bearer $token", "apikey" to config.publishableKey, "Content-Type" to "application/json; charset=utf-8", "Accept" to "application/json"),
                    body = body.toByteArray(Charsets.UTF_8),
                ),
            )
        }.getOrNull() ?: return@withContext PurchaseEvent.Failed("Sem conexão para confirmar a compra. Ela fica guardada e tentamos de novo ao abrir o app.")
        if (response.status !in 200..299) {
            val message = runCatching { JSONObject(response.body).getJSONObject("error").optString("message") }.getOrNull()
            return@withContext PurchaseEvent.Failed(message?.takeIf { it.isNotBlank() } ?: "Não deu para confirmar a compra agora (${response.status}).")
        }
        val json = JSONObject(response.body)
        if (!purchase.isAcknowledged) {
            runCatching { client.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()) }
        }
        if (json.optBoolean("active")) PurchaseEvent.Activated(json.optString("planTier", "FREE"))
        else PurchaseEvent.Failed("A assinatura não está ativa na Google Play.")
    }
}
