package br.com.estudario.ui.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import br.com.estudario.ui.AppViewModel
import br.com.estudario.ui.theme.EstudarioMotion

/**
 * A primeira abertura inteira: apresentação e, em seguida, a entrada.
 *
 * [onDone] marca o onboarding como concluído, daí em diante o app abre direto na Home. Pular a
 * apresentação leva para a mesma entrada: ninguém é obrigado a ler cinco telas para usar o app,
 * mas todo mundo passa pela escolha de conta uma vez.
 */
@Composable
fun OnboardingFlow(viewModel: AppViewModel, onDone: () -> Unit) {
    var showWelcome by rememberSaveable { mutableStateOf(false) }
    Crossfade(targetState = showWelcome, animationSpec = EstudarioMotion.standard(), label = "onboarding-flow") { welcome ->
        if (welcome) {
            WelcomeScreen(viewModel, onContinue = onDone)
        } else {
            OnboardingScreen(onFinish = { showWelcome = true })
        }
    }
}
