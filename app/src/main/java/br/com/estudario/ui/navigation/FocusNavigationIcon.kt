package br.com.estudario.ui.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun FocusNavigationIcon(active: Boolean, elapsedLabel: String, onClick: () -> Unit) {
    val description = if (active) "Foco ativo, $elapsedLabel" else "Modo foco"

    Box(
        modifier = Modifier
            .size(56.dp)
            .testTag("focus-navigation-button")
            .semantics(mergeDescendants = true) {
                contentDescription = description
                onClick(label = description) { onClick(); true }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (active) {
            Box(
                Modifier
                    .size(48.dp)
                    .testTag("focus-active-outline")
                    .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.48f), CircleShape),
            )
        }
        Surface(
            modifier = Modifier.size(46.dp),
            shape = CircleShape,
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = if (active) 1.dp else 3.dp,
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
}
