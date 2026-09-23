package br.com.estudario.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import br.com.estudario.R
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.TransferState
import br.com.estudario.ui.components.EstudarioBookLoader
import br.com.estudario.ui.components.EstudarioWordmark
import br.com.estudario.ui.profile.GoogleAction
import br.com.estudario.ui.profile.rememberGoogleAuthorizer
import br.com.estudario.ui.theme.EstudarioSpacing
import br.com.estudario.ui.theme.EstudarioTheme

/**
 * A entrada no Estudário, depois da apresentação.
 *
 * A conta Google é apresentada como conveniência, nunca como pedágio: o app é local-first e
 * funciona inteiro sem conta nenhuma. Quem preferir entrar depois vincula pelo Perfil, sem perder
 * nada do que estudou até lá.
 */
@Composable
fun WelcomeScreen(viewModel: AppViewModel, onContinue: () -> Unit) {
    val transfer by viewModel.transfer.collectAsState()
    var aguardandoGoogle by remember { mutableStateOf(false) }

    val authorize = rememberGoogleAuthorizer(
        onToken = { token ->
            aguardandoGoogle = true
            viewModel.handleGoogleToken(GoogleAction.SIGN_IN, token)
        },
        onError = { message ->
            aguardandoGoogle = false
            viewModel.reportGoogleError(message)
        },
    )

    // Entrou: segue direto para o app. O aviso de "conectado como…" não precisa segurar a pessoa
    // numa tela de boas-vindas que ela nunca mais vai ver.
    LaunchedEffect(transfer, aguardandoGoogle) {
        if (aguardandoGoogle && transfer is TransferState.Success) {
            viewModel.clearTransfer()
            aguardandoGoogle = false
            onContinue()
        }
        if (transfer is TransferState.Error) aguardandoGoogle = false
    }

    WelcomeContent(
        loading = aguardandoGoogle && transfer is TransferState.Loading,
        errorMessage = (transfer as? TransferState.Error)?.message,
        onGoogle = authorize,
        onContinue = onContinue,
    )
}

@Composable
private fun WelcomeContent(
    loading: Boolean,
    errorMessage: String?,
    onGoogle: () -> Unit,
    onContinue: () -> Unit,
) {
    val carregando = loading

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        // Rola quando não cabe (tela baixa, fonte grande) e continua distribuída como antes quando cabe.
        BoxWithConstraints(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(horizontal = EstudarioSpacing.screenGutter),
        ) {
            Spacer(Modifier.weight(1f))

            EstudarioWordmark()
            Spacer(Modifier.height(EstudarioSpacing.comfortable))
            Text(
                "Prepare seu espaço de estudo.",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(EstudarioSpacing.medium))
            Text(
                "Seus dados permanecem neste aparelho. Ao entrar com o Google, você adiciona um backup no Drive e pode restaurar seu histórico em outro aparelho.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(EstudarioSpacing.section))

            Column(verticalArrangement = Arrangement.spacedBy(EstudarioSpacing.medium)) {
                BenefitRow(Icons.Rounded.CloudDone, "Backup do seu histórico na sua conta")
                BenefitRow(Icons.Rounded.SettingsBackupRestore, "Restauração em outro aparelho")
                BenefitRow(Icons.Rounded.AccountCircle, "Nome e foto no seu perfil")
            }

            Spacer(Modifier.weight(1f))

            if (errorMessage != null) {
                Text(
                    errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = EstudarioSpacing.small),
                )
            }

            GoogleSignInButton(loading = carregando, onClick = onGoogle)

            TextButton(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().padding(top = EstudarioSpacing.hairline),
            ) {
                Text("Continuar sem uma conta", style = MaterialTheme.typography.labelLarge)
            }

            Text(
                "Você poderá vincular uma conta depois, na área Perfil.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(bottom = EstudarioSpacing.large),
            )
        }
        }
    }
}

@Composable
private fun BenefitRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EstudarioSpacing.small)) {
        Box(
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(17.dp),
            )
        }
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
    }
}

/**
 * O botão de entrar com o Google no formato que o próprio Google especifica para Sign-In: fundo
 * neutro (branco no claro, quase preto no escuro), borda fina, o "G" colorido oficial à esquerda e
 * o texto "Continuar com o Google". Nada de pintar o botão com a cor do app, a pessoa reconhece
 * esse botão de outros apps, e é esse reconhecimento que passa confiança.
 *
 * Conectando, o "G" dá lugar ao livro folheando: a espera tem a cara do Estudário.
 */
@Composable
private fun GoogleSignInButton(loading: Boolean, onClick: () -> Unit) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val container = if (dark) Color(0xFF131314) else Color(0xFFFFFFFF)
    val border = if (dark) Color(0xFF8E918F) else Color(0xFF747775)
    val content = if (dark) Color(0xFFE3E3E3) else Color(0xFF1F1F1F)

    OutlinedButton(
        onClick = { if (!loading) onClick() },
        enabled = !loading,
        shape = RoundedCornerShape(percent = 50),
        border = BorderStroke(1.dp, border),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container,
            disabledContentColor = content.copy(alpha = 0.72f),
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .semantics { contentDescription = if (loading) "Conectando com o Google" else "Continuar com o Google" },
    ) {
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            if (loading) {
                EstudarioBookLoader(size = 24.dp)
            } else {
                Image(painterResource(R.drawable.ic_google_g), contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            if (loading) "Conectando…" else "Continuar com o Google",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
        )
    }
}

// ---------------------------------------------------------------- previews

@Preview(name = "Entrada, escolha de conta", showBackground = true, heightDp = 760)
@Composable
private fun WelcomePreview() {
    EstudarioTheme {
        WelcomeContent(loading = false, errorMessage = null, onGoogle = {}, onContinue = {})
    }
}

@Preview(name = "Entrada, conectando", showBackground = true, heightDp = 760)
@Composable
private fun WelcomeLoadingPreview() {
    EstudarioTheme {
        WelcomeContent(loading = true, errorMessage = null, onGoogle = {}, onContinue = {})
    }
}

@Preview(name = "Entrada, Google recusou", showBackground = true, heightDp = 760, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeErrorPreview() {
    EstudarioTheme {
        WelcomeContent(
            loading = false,
            errorMessage = "Login cancelado.",
            onGoogle = {},
            onContinue = {},
        )
    }
}
