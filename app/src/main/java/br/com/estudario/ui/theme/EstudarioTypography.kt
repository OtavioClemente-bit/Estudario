package br.com.estudario.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import br.com.estudario.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * A tipografia é a principal assinatura visual do Estudário nesta refatoração: títulos e números
 * em peso pesado e rastreamento fechado (a "voz" confiante do app), rótulos de contexto em peso
 * pesado e rastreamento bem aberto (o estilo "versalete" usado como eyebrow, AGORA, HOJE, PLANO),
 * e corpo de texto discreto para não competir com o que importa.
 *
 * Antes disso a tela inteira usava o [Typography] padrão do Material 3 (tudo W400/W500, sem
 * personalidade), e cada composable compensava isso escrevendo `fontWeight = FontWeight.Black`
 * manualmente. Formalizar o peso na escala elimina essa repetição e garante consistência.
 */
private val BaseTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 40.sp, lineHeight = 44.sp, letterSpacing = (-0.5).sp),
    displayMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp, letterSpacing = (-0.3).sp),
    displaySmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.2).sp),

    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 27.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 25.sp),

    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),

    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.2.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.15.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.1.sp),

    // labelLarge segue para botões: peso alto, rastreamento moderado, ainda legível em caixa normal.
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.3.sp),
    // labelMedium/labelSmall são o estilo "eyebrow" do app: use com .uppercase() nos textos curtos de
    // contexto (AGORA, HOJE, SEU PROGRESSO). O rastreamento aberto é o que dá o efeito versalete.
    labelMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.1.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.9.sp),
)

/**
 * Nunito, a letra do Estudário: arredondada, firme no peso alto e calma no corpo de texto. Vem
 * embutida no app (um arquivo por peso, para o negrito ser negrito de verdade), então aparece igual em qualquer
 * celular, inclusive nos que trocam a fonte do sistema e ignoram o negrito.
 * Licença SIL OFL 1.1, cópia em docs/licencas/Nunito-OFL.txt.
 */
val Nunito = FontFamily(
    Font(R.font.nunito_400, FontWeight.Light),
    Font(R.font.nunito_400, FontWeight.Normal),
    Font(R.font.nunito_500, FontWeight.Medium),
    Font(R.font.nunito_600, FontWeight.SemiBold),
    Font(R.font.nunito_700, FontWeight.Bold),
    Font(R.font.nunito_800, FontWeight.ExtraBold),
    Font(R.font.nunito_900, FontWeight.Black),
)

private fun TextStyle.nunito() = copy(fontFamily = Nunito)

val EstudarioTypography = BaseTypography.run {
    Typography(
        displayLarge = displayLarge.nunito(), displayMedium = displayMedium.nunito(), displaySmall = displaySmall.nunito(),
        headlineLarge = headlineLarge.nunito(), headlineMedium = headlineMedium.nunito(), headlineSmall = headlineSmall.nunito(),
        titleLarge = titleLarge.nunito(), titleMedium = titleMedium.nunito(), titleSmall = titleSmall.nunito(),
        bodyLarge = bodyLarge.nunito(), bodyMedium = bodyMedium.nunito(), bodySmall = bodySmall.nunito(),
        labelLarge = labelLarge.nunito(), labelMedium = labelMedium.nunito(), labelSmall = labelSmall.nunito(),
    )
}
