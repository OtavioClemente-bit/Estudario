package br.com.estudario.ui.brand

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.HowToVote
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** O ImageVector do desenho escolhido em SubjectArt (nomes do Material Symbols). */
fun subjectArtVector(key: String): ImageVector = when (key) {
    "translate" -> Icons.Outlined.Translate
    "menu_book" -> Icons.AutoMirrored.Outlined.MenuBook
    "calculate" -> Icons.Outlined.Calculate
    "computer" -> Icons.Outlined.Computer
    "account_balance" -> Icons.Outlined.AccountBalance
    "gavel" -> Icons.Outlined.Gavel
    "payments" -> Icons.Outlined.Payments
    "trending_up" -> Icons.AutoMirrored.Outlined.TrendingUp
    "business_center" -> Icons.Outlined.BusinessCenter
    "folder_open" -> Icons.Outlined.FolderOpen
    "policy" -> Icons.Outlined.Policy
    "balance" -> Icons.Outlined.Balance
    "public" -> Icons.Outlined.Public
    "medical_services" -> Icons.Outlined.MedicalServices
    "school" -> Icons.Outlined.School
    "shield" -> Icons.Outlined.Shield
    "science" -> Icons.Outlined.Science
    "sailing" -> Icons.Outlined.Sailing
    "fact_check" -> Icons.Outlined.FactCheck
    "receipt_long" -> Icons.AutoMirrored.Outlined.ReceiptLong
    "how_to_vote" -> Icons.Outlined.HowToVote
    else -> Icons.Outlined.AutoStories
}

/**
 * O selo de uma matéria ou de um concurso: o desenho do assunto na cor dele, num quadrado com
 * degradê suave e borda fina. Substitui a letra/sigla solta, que parecia genérica.
 */
@Composable
fun SubjectArtTile(key: String, color: Color, size: Dp = 40.dp, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(size * 0.32f)
    Box(
        modifier
            .size(size)
            .clip(shape)
            .background(Brush.linearGradient(listOf(color.copy(alpha = 0.26f), color.copy(alpha = 0.10f))))
            .border(1.5.dp, color.copy(alpha = 0.35f), shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(subjectArtVector(key), contentDescription = null, modifier = Modifier.size(size * 0.55f), tint = color)
    }
}
