package br.com.estudario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * O diálogo do Estudário. Mesmos parâmetros do AlertDialog do Material, para trocar só a
 * importação, mas com a cara do app: fundo claro do app em vez da caixa cinza, cantos grandes,
 * ícone num círculo da cor da marca e título em destaque.
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(28.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    iconContentColor: Color = MaterialTheme.colorScheme.primary,
    titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
    textContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    tonalElevation: Dp = 0.dp,
    properties: DialogProperties = DialogProperties(),
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        icon = icon?.let { content ->
            {
                Box(
                    Modifier.size(52.dp).clip(CircleShape).background(iconContentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) { CompositionLocalProvider(LocalContentColor provides iconContentColor) { content() } }
            }
        },
        title = title?.let { content ->
            { ProvideTextStyle(MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)) { content() } }
        },
        text = text?.let { content ->
            { ProvideTextStyle(MaterialTheme.typography.bodyMedium) { content() } }
        },
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}
