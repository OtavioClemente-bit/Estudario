package br.com.estudario.ui.plans

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.estudario.BuildConfig
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiFreeAdAdvances
import br.com.estudario.ui.brand.Icon
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Anúncio premiado do Grátis: adianta uma matéria do plano. Só abre quando a pessoa toca no botão
 * (nada abre sozinho). O crédito não é dado pelo app: o AdMob avisa o servidor (ad-reward-ssv, com a
 * assinatura do Google) e só então a matéria pode sair. O app só espera o crédito aparecer.
 */
object RewardedAds {
    private var initialized = false

    private fun Context.activity(): Activity? {
        var current: Context? = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
    }

    /** Mostra o anúncio; true quando a pessoa assistiu até ganhar a recompensa. */
    suspend fun show(context: Context, userId: String): Boolean {
        val activity = context.activity() ?: return false
        if (!initialized) {
            suspendCancellableCoroutine { cont -> MobileAds.initialize(activity) { if (cont.isActive) cont.resume(Unit) } }
            initialized = true
        }
        val ad = suspendCancellableCoroutine<RewardedAd?> { cont ->
            RewardedAd.load(activity, BuildConfig.ADMOB_REWARDED_UNIT, AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) { if (cont.isActive) cont.resume(ad) }
                override fun onAdFailedToLoad(error: LoadAdError) { if (cont.isActive) cont.resume(null) }
            })
        } ?: return false
        // A conta vai junto: é por ela que o servidor sabe para quem é o crédito.
        ad.setServerSideVerificationOptions(ServerSideVerificationOptions.Builder().setUserId(userId).setCustomData("adiantar").build())
        var earned = false
        return suspendCancellableCoroutine { cont ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() { if (cont.isActive) cont.resume(earned) }
                override fun onAdFailedToShowFullScreenContent(error: AdError) { if (cont.isActive) cont.resume(false) }
            }
            ad.show(activity) { earned = true }
        }
    }

    /** Espera o servidor receber a confirmação do Google (segundos). true quando o crédito chegou. */
    suspend fun awaitCredit(context: Context, before: Int): Boolean {
        val repository = (context.applicationContext as EstudarioApplication).aiPlanRepository
        repeat(12) {
            delay(2_500)
            val summary = (repository.load() as? AiPlanLoadResult.Available)?.summary ?: return@repeat
            if ((summary.freeAllowance?.adAdvances?.available ?: 0) > before) return true
        }
        return false
    }
}

/**
 * Cartão "Adiantar com um anúncio": os limites à vista (2 por dia, 5 por mês) e o botão. Depois do
 * anúncio, espera o crédito confirmado e chama [onCredit] para pedir a matéria de novo.
 */
@Composable
fun RewardedAdvanceCard(ads: AiFreeAdAdvances, onCredit: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF3B2FC9), Color(0xFF6A4BE0))))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFF5B83D)), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.OndemandVideo, null, tint = Color(0xFF1B1464))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Adiantar com um anúncio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
                Text("Um vídeo curto libera a próxima matéria do seu plano.", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AdCounter("Hoje", ads.today, ads.dailyLimit, Modifier.weight(1f))
            AdCounter("No mês", ads.month, ads.monthlyLimit, Modifier.weight(1f))
        }
        val userId = remember { (context.applicationContext as EstudarioApplication).supabaseAuthRepository.currentUserId() }
        val label = when {
            busy -> state ?: "Abrindo o anúncio…"
            !ads.canWatch -> if (ads.today >= ads.dailyLimit) "Os anúncios de hoje acabaram. Volte amanhã!" else "Os anúncios deste mês acabaram."
            else -> "Assistir e adiantar"
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (ads.canWatch && !busy) Color(0xFFF5B83D) else Color.White.copy(alpha = 0.16f))
                .then(
                    if (ads.canWatch && !busy && userId != null) Modifier.clickableNoRipple {
                        busy = true
                        state = null
                        scope.launch {
                            val before = ads.available
                            val watched = RewardedAds.show(context, userId)
                            if (!watched) { state = "O anúncio não terminou. Tente de novo."; busy = false; return@launch }
                            state = "Confirmando com o Google…"
                            if (RewardedAds.awaitCredit(context, before)) { busy = false; onCredit() }
                            else { state = "A confirmação está demorando. Tente adiantar de novo em instantes."; busy = false }
                        }
                    } else Modifier,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (busy) { CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White); Spacer(Modifier.width(10.dp)) }
                Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = if (ads.canWatch && !busy) Color(0xFF1B1464) else Color.White)
            }
        }
        if (!busy && state != null) Text(state!!, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
    }
}

@Composable
private fun AdCounter(label: String, used: Int, limit: Int, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.12f)).padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f))
        Text("$used de $limit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
    }
}

private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = this.clickable(onClick = onClick)
