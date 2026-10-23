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
fun FolhaScene(attrs: List<Pair<String, String>>, modifier: Modifier = Modifier, stageWidthDp: Int = 0, bleedDp: Int = 0) {
    val context = LocalContext.current
    val html = remember(attrs, stageWidthDp, bleedDp) {
        val look = JSONObject().apply {
            FolhaSlot.entries.forEach { slot -> put(slot.key, FolhaLookStore.read(context, slot) ?: FolhaWardrobe.default(slot).id) }
        }
        val sets = attrs.joinToString("") { (k, v) -> "c.setAttribute(${JSONObject.quote(k)},${JSONObject.quote(v)});" }
        """<!doctype html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>html,body{margin:0;padding:0;background:transparent;overflow:hidden;font-family:sans-serif}estudario-cena{${if (stageWidthDp > 0) "width:${stageWidthDp}px;margin:${bleedDp}px auto 0" else "width:100%"}}</style></head>
<body><script src="turma.js"></script><script src="folha-live.js"></script><script>folhaSetLook($look);var c=document.createElement('estudario-cena');$sets${if (bleedDp > 0) "c.setAttribute('bleed','" + (bleedDp * 100 / stageWidthDp) + "');" else ""} c.setAttribute('som','0');document.body.appendChild(c);</script></body></html>"""
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
                // a trilha da cena toca pelo app (SoundPool), começando junto com a página
                val tipo = attrs.firstOrNull { it.first == "tipo" }?.second
                webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        if (tipo == null) return
                        br.com.estudario.ui.brand.EstudarioSom.play(ctx, "cena_" + tipo, 0.9f)
                        // no presente, o "achou!" do Folha entra quando ele aparece vestido (depende de quantas cartas)
                        if (tipo == "peca") {
                            val count = attrs.firstOrNull { it.first == "pieces" }?.second?.let { runCatching { org.json.JSONArray(it).length() }.getOrNull() } ?: 1
                            val at = ((FolhaWardrobe.giftFlashAt(count) - 0.62) * 1000).toLong()
                            view?.postDelayed({ if (view.isAttachedToWindow) br.com.estudario.ui.brand.EstudarioSom.play(ctx, "conquista", 0.9f) }, at)
                        }
                    }
                }
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
            val screenW = maxWidth
            Column(
                Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                androidx.compose.runtime.key(sceneKey) {
                    // a cena ocupa o lugar do palco, mas a WebView é maior (a tela toda de largura e uma
                    // sobra em cima e embaixo): a luz se espalha além da caixa e some suave no fundo
                    val bleed = stageW * 0.34f
                    Box(Modifier.width(stageW).aspectRatio(1f / 1.1f), contentAlignment = Alignment.Center) {
                        FolhaScene(
                            scene, Modifier.requiredSize(screenW, stageW * 1.1f + bleed * 2),
                            stageWidthDp = stageW.value.toInt(), bleedDp = bleed.value.toInt(),
                        )
                    }
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
