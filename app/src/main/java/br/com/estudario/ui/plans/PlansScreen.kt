package br.com.estudario.ui.plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.EstudarioApplication
import br.com.estudario.data.ai.AiPlanCatalogEntry
import br.com.estudario.data.ai.AiPlanLimit
import br.com.estudario.data.ai.AiPlanSummary
import br.com.estudario.data.ai.AiPlanUsage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

private val saoPaulo = ZoneId.of("America/Sao_Paulo")
private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(saoPaulo)

/** Nome exibido de cada plano. Preços só entram quando a cobrança for aberta. */
fun planDisplayName(tier: String): String = when (tier) {
    "ESSENCIAL" -> "Essencial"
    "PRO" -> "Pro"
    else -> "Grátis"
}

private fun featureLabel(feature: String): String = when (feature) {
    "SYLLABUS_GENERATION" -> "Editais"
    "PLAN_GENERATION" -> "Planos de estudo"
    "CONTENT_GENERATION" -> "Gerações de conteúdo"
    "QUESTION_BATCH" -> "Lotes de questões extras"
    "AD_REWARD" -> "Bônus por anúncio"
    else -> feature
}

private fun featureOrder(feature: String): Int = listOf(
    "SYLLABUS_GENERATION", "PLAN_GENERATION", "CONTENT_GENERATION", "QUESTION_BATCH", "AD_REWARD",
).indexOf(feature).let { if (it < 0) Int.MAX_VALUE else it }

private fun formatDate(iso: String?): String? = iso?.let { runCatching { dateFormat.format(Instant.parse(it)) }.getOrNull() }

/** Uma linha do catálogo, ex.: "10 por mês", "1 no total", "Até 10 por lote · 20 por tópico/mês". */
private fun limitCopy(limit: AiPlanLimit): String {
    val base = when (limit.periodKind) {
        "LIFETIME" -> "${limit.quotaLimit} no total"
        "DAILY" -> "${limit.quotaLimit} por dia"
        else -> "${limit.quotaLimit} por mês"
    }
    val extras = listOfNotNull(
        limit.maxPerRequest?.let { "até $it questões por lote" },
        limit.maxPerTopicMonth?.let { "$it por tópico no mês" },
    )
    return (listOf(base) + extras).joinToString(" · ")
}

private fun usageCopy(usage: AiPlanUsage): String {
    val base = "Restam ${usage.remaining} de ${usage.limit}"
    return when (usage.periodKind) {
        "MONTHLY" -> formatDate(usage.resetAt)?.let { "$base neste mês · renova em $it" } ?: "$base neste mês"
        else -> "$base · saldo único, não renova"
    }
}

private sealed interface PlansState {
    data object Loading : PlansState
    data class Ready(val summary: AiPlanSummary) : PlansState
    data object SignedOut : PlansState
    data object Unavailable : PlansState
}

@Composable
fun PlansDialog(onDismiss: () -> Unit, onSignIn: (() -> Unit)? = null) {
    val app = LocalContext.current.applicationContext as EstudarioApplication
    val repository = app.aiPlanRepository
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<PlansState>(PlansState.Loading) }
    var reload by remember { mutableIntStateOf(0) }
    var adBusy by remember { mutableStateOf(false) }
    var adMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(reload) {
        state = PlansState.Loading
        state = when (val result = repository.load()) {
            is AiPlanLoadResult.Available -> PlansState.Ready(result.summary)
            AiPlanLoadResult.SignedOut -> PlansState.SignedOut
            AiPlanLoadResult.Unavailable -> PlansState.Unavailable
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Planos e uso", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Gerações com a IA do Estudário", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar") }
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    when (val current = state) {
                        PlansState.Loading -> Row(Modifier.fillMaxWidth().padding(32.dp), horizontalArrangement = Arrangement.Center) { CircularProgressIndicator() }
                        PlansState.SignedOut -> Notice(
                            "Entre na sua conta",
                            "O saldo fica vinculado à sua conta Estudário, não ao aparelho. Entre pela opção Gerar com IA do Estudário para ver seu plano e quanto ainda pode gerar.",
                            actionLabel = if (onSignIn != null) "Entrar" else null,
                            onAction = { onSignIn?.invoke() },
                        )
                        PlansState.Unavailable -> Notice(
                            "Não foi possível carregar seu plano",
                            "Verifique a conexão e tente novamente. Seus dados de estudo continuam no aparelho.",
                            actionLabel = "Tentar novamente",
                            onAction = { reload++ },
                        )
                        is PlansState.Ready -> {
                            CurrentPlanCard(current.summary)
                            UsageSection(current.summary.usage)
                            AdRewardCard(
                                visible = current.summary.planTier == "FREE",
                                busy = adBusy,
                                onClick = {
                                    adBusy = true
                                    scope.launch {
                                        val result = repository.registerAdInterest()
                                        adBusy = false
                                        adMessage = if (result?.recorded == true) {
                                            "Os anúncios recompensados chegam depois do teste fechado. Registramos seu interesse; nenhum anúncio foi exibido e nenhum crédito foi adicionado."
                                        } else {
                                            "Não foi possível registrar agora. Tente novamente mais tarde."
                                        }
                                    }
                                },
                            )
                            PlanCatalog(current.summary.plans, current.summary.planTier)
                            HowQuotaWorks()
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    adMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { adMessage = null },
            title = { Text("Gerações extras com anúncio") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { adMessage = null }) { Text("Entendi") } },
        )
    }
}

@Composable
private fun Notice(title: String, body: String, actionLabel: String?, onAction: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (actionLabel != null) Button(onClick = onAction) { Text(actionLabel) }
        }
    }
}

@Composable
private fun CurrentPlanCard(summary: AiPlanSummary) {
    ElevatedCard(Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("SEU PLANO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(planDisplayName(summary.planTier), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            val renew = formatDate(summary.planRenewsAt)
            Text(
                when {
                    summary.planTier == "FREE" -> "Teste fechado: as assinaturas ainda não estão à venda."
                    renew != null -> "Ativo até $renew."
                    else -> "Ativo."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun UsageSection(usage: List<AiPlanUsage>) {
    if (usage.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("SALDO ATUAL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        usage.sortedBy { featureOrder(it.feature) }.forEach { item ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Text(featureLabel(item.feature), Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Text("${item.used}/${item.limit}", style = MaterialTheme.typography.bodyMedium)
                }
                LinearProgressIndicator(
                    progress = { if (item.limit == 0) 1f else (item.used.toFloat() / item.limit).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(usageCopy(item), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AdRewardCard(visible: Boolean, busy: Boolean, onClick: () -> Unit) {
    if (!visible) return
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.OndemandVideo, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Text("Ganhe gerações extras", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Text(
                "Assista a um anúncio curto e opcional para liberar +1 geração de conteúdo. Até 2 por dia e 10 por mês. O crédito só entra depois que o anúncio termina.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onClick, enabled = !busy) {
                if (busy) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp) else Text("Assistir anúncio (+1 geração)")
            }
        }
    }
}

@Composable
private fun PlanCatalog(plans: List<AiPlanCatalogEntry>, currentTier: String) {
    if (plans.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("COMPARE OS PLANOS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        plans.forEach { plan ->
            val isCurrent = plan.planTier == currentTier
            OutlinedCard(
                Modifier.fillMaxWidth(),
                border = BorderStroke(if (isCurrent) 2.dp else 1.dp, if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(planDisplayName(plan.planTier), Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (isCurrent) Text("Plano atual", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                    plan.limits
                        .groupBy { it.feature }
                        .toSortedMap(compareBy<String> { featureOrder(it) })
                        .forEach { (feature, limits) ->
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Outlined.CheckCircle, null, Modifier.size(18.dp).padding(top = 2.dp), tint = MaterialTheme.colorScheme.secondary)
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(featureLabel(feature), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                    Text(limits.joinToString(" · ") { limitCopy(it) }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    if (!isCurrent && plan.planTier != "FREE") {
                        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("Disponível após o teste fechado") }
                    }
                }
            }
        }
    }
}

@Composable
private fun HowQuotaWorks() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("COMO O SALDO FUNCIONA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        listOf(
            "Cada pedido concluído com sucesso consome 1 unidade: um edital, um plano, o material de um tópico ou um lote de questões.",
            "Se a geração falhar ou for cancelada antes de entregar o resultado, nada é descontado.",
            "Repetir o mesmo pedido após um erro técnico não cobra de novo.",
            "O saldo é da sua conta e vale em qualquer aparelho. Usar sua própria IA pelo prompt não consome saldo.",
        ).forEach { line ->
            Row {
                Text("•", Modifier.width(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(line, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
