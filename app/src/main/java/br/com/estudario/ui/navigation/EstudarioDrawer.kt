package br.com.estudario.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import br.com.estudario.ui.theme.EstudarioTheme
import br.com.estudario.ui.components.EstudarioGlyph
import br.com.estudario.ui.components.ProfileAvatar
import br.com.estudario.ui.profile.UserProfile
import br.com.estudario.ui.theme.EstudarioShapes
import br.com.estudario.ui.theme.EstudarioSpacing

/**
 * O menu lateral do Estudário: tudo que é importante mas não é diário.
 *
 * A navegação de baixo guarda os quatro lugares onde se estuda; aqui ficam as ferramentas de
 * acompanhamento e as configurações do app. A separação é essa, e é por isso que não existe mais
 * uma aba "Mais" — aba é para destino frequente, não para o que sobrou.
 */
data class DrawerEntry(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val route: String? = null,
)

data class DrawerSection(val title: String, val entries: List<DrawerEntry>)

@Composable
fun EstudarioDrawerContent(
    profile: UserProfile,
    level: Int?,
    totalXp: Int?,
    currentRoute: String?,
    sections: List<DrawerSection>,
    appVersion: String,
    onOpenProfile: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .width(304.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .verticalScroll(rememberScrollState())
            .padding(vertical = EstudarioSpacing.large),
    ) {
        DrawerHeader(profile, level, totalXp, onOpenProfile)
        Spacer(Modifier.height(EstudarioSpacing.large))
        sections.forEachIndexed { index, section ->
            Text(
                section.title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = EstudarioSpacing.large, bottom = EstudarioSpacing.tight),
            )
            section.entries.forEach { entry ->
                DrawerRow(entry, selected = entry.route != null && entry.route == currentRoute)
            }
            if (index != sections.lastIndex) Spacer(Modifier.height(EstudarioSpacing.comfortable))
        }
        Spacer(Modifier.height(EstudarioSpacing.large))
        Text(
            appVersion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = EstudarioSpacing.large),
        )
    }
}

@Composable
private fun DrawerHeader(profile: UserProfile, level: Int?, totalXp: Int?, onOpenProfile: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenProfile)
            .padding(horizontal = EstudarioSpacing.large, vertical = EstudarioSpacing.small),
        verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
            EstudarioGlyph(size = 18.dp)
            Text("ESTUDÁRIO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(EstudarioSpacing.tight))
        ProfileAvatar(profile.photoPath, profile.initials, 56.dp)
        Text(
            profile.displayName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Text(
            when {
                level != null && totalXp != null -> "Nível $level · ${totalXp} XP"
                profile.signedIn -> profile.email
                else -> "Conta local"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
        )
    }
}

@Composable
private fun DrawerRow(entry: DrawerEntry, selected: Boolean) {
    val background = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
    val content = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = EstudarioSpacing.small, vertical = 2.dp)
            .clip(EstudarioShapes.row)
            .background(background)
            .clickable(onClick = entry.onClick)
            .padding(horizontal = EstudarioSpacing.small, vertical = EstudarioSpacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small),
    ) {
        Icon(entry.icon, contentDescription = null, tint = content, modifier = Modifier.size(22.dp))
        Text(entry.label, style = MaterialTheme.typography.titleSmall, color = content)
    }
}

/**
 * As seções reais do app. Só entra aqui o que existe de verdade — o menu não é vitrine de planos
 * futuros. [onHelp] abre o mesmo seletor de guias que já existia em "Como usar o app".
 */
fun estudarioDrawerSections(
    onSyllabus: () -> Unit,
    onPlan: () -> Unit,
    onTrain: () -> Unit,
    onReviews: () -> Unit,
    onErrors: () -> Unit,
    onFocus: () -> Unit,
    onStatistics: () -> Unit,
    onBadges: () -> Unit,
    onSources: () -> Unit,
    onSettings: () -> Unit,
    onNotifications: () -> Unit,
    onHelp: () -> Unit,
): List<DrawerSection> = listOf(
    DrawerSection(
        "Estudos",
        listOf(
            DrawerEntry("Edital", Icons.Outlined.Checklist, onSyllabus, "syllabus"),
            DrawerEntry("Plano de estudos", Icons.Outlined.CalendarMonth, onPlan, "plan"),
            DrawerEntry("Treinar questões", Icons.Outlined.School, onTrain, "train"),
            DrawerEntry("Revisões espaçadas", Icons.Outlined.Autorenew, onReviews, "reviews"),
            DrawerEntry("Caderno de erros", Icons.Outlined.ErrorOutline, onErrors, "errors"),
            DrawerEntry("Modo foco", Icons.Outlined.Timer, onFocus, "focus"),
        ),
    ),
    DrawerSection(
        "Acompanhamento",
        listOf(
            DrawerEntry("Desempenho", Icons.Outlined.QueryStats, onStatistics, "statistics"),
            DrawerEntry("Conquistas", Icons.Outlined.EmojiEvents, onBadges, "badges"),
            DrawerEntry("Histórico e fontes", Icons.Outlined.FactCheck, onSources, "sources"),
        ),
    ),
    DrawerSection(
        "Aplicativo",
        listOf(
            DrawerEntry("Ajustes", Icons.Outlined.Tune, onSettings, "more"),
            DrawerEntry("Notificações", Icons.Outlined.NotificationsActive, onNotifications, "notifications"),
            DrawerEntry("Como usar o app", Icons.Outlined.HelpOutline, onHelp),
        ),
    ),
)

/**
 * A barra de identidade do app: aparece nos quatro destinos principais e é o único lugar onde a
 * marca, a busca e o perfil moram. As telas abaixo dela não repetem o nome do app — por isso aqui
 * fica o logotipo, e não o título da aba.
 */
@Composable
fun EstudarioTopBar(
    profile: UserProfile,
    onOpenMenu: () -> Unit,
    onSearch: () -> Unit,
    onProfile: () -> Unit,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    profileModifier: Modifier = Modifier,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = EstudarioSpacing.small, vertical = EstudarioSpacing.tight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconAction(Icons.Outlined.Menu, "Abrir menu", onOpenMenu)
        Spacer(Modifier.width(EstudarioSpacing.hairline))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.tight)) {
            EstudarioGlyph(size = 20.dp)
            Text("ESTUDÁRIO", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.weight(1f))
        IconAction(Icons.Outlined.Search, "Pesquisar", onSearch)
        Spacer(Modifier.width(EstudarioSpacing.hairline))
        Box(
            profileModifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .semantics { contentDescription = "Abrir perfil" }
                .clickable(onClick = onProfile),
            contentAlignment = Alignment.Center,
        ) {
            ProfileAvatar(profile.photoPath, profile.initials, 34.dp)
        }
    }
}

// ---------------------------------------------------------------- previews

@Preview(name = "Menu lateral", showBackground = true, widthDp = 320, heightDp = 780)
@Composable
private fun DrawerPreview() {
    EstudarioTheme {
        EstudarioDrawerContent(
            profile = UserProfile(name = "Otávio Clemente", email = "otavio@exemplo.com"),
            level = 14,
            totalXp = 1840,
            currentRoute = "plan",
            sections = estudarioDrawerSections({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}),
            appVersion = "Estudário 2.2.0 · Local-first",
            onOpenProfile = {},
        )
    }
}

@Preview(name = "Menu lateral — conta local (escuro)", showBackground = true, widthDp = 320, heightDp = 780, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DrawerLocalDarkPreview() {
    EstudarioTheme {
        EstudarioDrawerContent(
            profile = UserProfile(name = "Otávio"),
            level = null,
            totalXp = null,
            currentRoute = "home",
            sections = estudarioDrawerSections({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}),
            appVersion = "Estudário 2.2.0 · Local-first",
            onOpenProfile = {},
        )
    }
}

@Preview(name = "Barra de identidade", showBackground = true, widthDp = 400, heightDp = 80)
@Composable
private fun TopBarPreview() {
    EstudarioTheme {
        EstudarioTopBar(
            profile = UserProfile(name = "Otávio Clemente"),
            onOpenMenu = {},
            onSearch = {},
            onProfile = {},
        )
    }
}

@Composable
private fun IconAction(icon: ImageVector, description: String, onClick: () -> Unit) {
    Box(
        Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = description, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
