package br.com.estudario.ui.profile

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.estudario.data.FolhaLookStore
import br.com.estudario.domain.FolhaSlot
import br.com.estudario.domain.FolhaWardrobe
import kotlinx.coroutines.delay
import org.json.JSONObject

internal val CelTop = Color(0xFF3B2FC9)
internal val CelMid = Color(0xFF1D1670)
internal val CelBottom = Color(0xFF110F26)
internal val CelGold = Color(0xFFFFB421)

/** A página que carrega pela WebView leva uns décimos: os textos esperam isso além do roteiro da cena. */
private const val LOAD_SLACK = 0.35f

/**
 * A cena de comemoração desenhada, a mesma do site: folha-live.js (copiado para os assets pelo Gradle)
 * com o elemento <estudario-cena>, o Folha no visual escolhido no armário. Fundo transparente.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun FolhaScene(attrs: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val html = remember(attrs) {
        val look = JSONObject().apply {
            FolhaSlot.entries.forEach { slot -> put(slot.key, FolhaLookStore.read(context, slot) ?: FolhaWardrobe.default(slot).id) }
        }
        val sets = attrs.joinToString("") { (k, v) -> "c.setAttribute(${JSONObject.quote(k)},${JSONObject.quote(v)});" }
        """<!doctype html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>html,body{margin:0;padding:0;background:transparent;overflow:hidden;font-family:sans-serif}estudario-cena{width:100%}</style></head>
<body><script src="folha-live.js"></script><script>folhaSetLook($look);var c=document.createElement('estudario-cena');$sets document.body.appendChild(c);</script></body></html>"""
    }
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                setBackgroundColor(0)
                settings.javaScriptEnabled = true
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                overScrollMode = View.OVER_SCROLL_NEVER
                importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
                loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null)
            }
        },
        onRelease = { it.destroy() },
        modifier = modifier,
    )
}

/** Fica 0 até o instante `at` (segundos) e então sobe para 1 em meio segundo. */
@Composable
fun revealAlpha(at: Float): Float {
    val shown by produceState(false) { delay(((at + LOAD_SLACK) * 1000).toLong()); value = true }
    val a by animateFloatAsState(if (shown) 1f else 0f, tween(500), label = "revelar")
    return a
}

/**
 * Tela cheia de comemoração: a cena em cima, os textos (que entram quando a cena chega no momento
 * principal, `reveal`) e os botões embaixo. Usada pelo nível, emblema, sequência e peça nova.
 */
@Composable
fun SceneCelebration(
    scene: List<Pair<String, String>>,
    reveal: Float,
    onDismiss: () -> Unit,
    sceneKey: Any = Unit,
    text: @Composable ColumnScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        BoxWithConstraints(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(CelTop, CelMid, CelBottom)))) {
            val stageW = min(360.dp, min(maxWidth - 32.dp, (maxHeight - 320.dp) / 1.1f)).coerceAtLeast(220.dp)
            Column(
                Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                androidx.compose.runtime.key(sceneKey) {
                    FolhaScene(scene, Modifier.width(stageW).aspectRatio(1f / 1.1f))
                    val a = revealAlpha(reveal)
                    Column(
                        Modifier.graphicsLayer { alpha = a; translationY = (1f - a) * 14.dp.toPx() },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), content = text,
                    )
                    Spacer(Modifier.height(18.dp))
                    val b = revealAlpha(reveal + 0.45f)
                    Row(
                        Modifier.graphicsLayer { alpha = b; translationY = (1f - b) * 14.dp.toPx() },
                        horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, content = actions,
                    )
                }
            }
        }
    }
}

@Composable
fun CelEyebrow(text: String, color: Color = CelGold) =
    Text(text, color = color, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.6.sp, textAlign = TextAlign.Center)

@Composable
fun CelTitle(text: String) =
    Text(text, color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp, lineHeight = 32.sp, textAlign = TextAlign.Center)

@Composable
fun CelBody(text: String) =
    Text(text, color = Color.White.copy(alpha = 0.82f), fontSize = 15.sp, lineHeight = 21.sp, textAlign = TextAlign.Center)

@Composable
fun CelChip(text: String, background: Color = CelGold.copy(alpha = 0.18f), color: Color = Color(0xFFFFD27A)) =
    Text(text, Modifier.clip(RoundedCornerShape(50)).background(background).padding(horizontal = 14.dp, vertical = 6.dp), color = color, fontWeight = FontWeight.Black)
