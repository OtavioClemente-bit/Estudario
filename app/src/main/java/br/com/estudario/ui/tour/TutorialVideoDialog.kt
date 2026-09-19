package br.com.estudario.ui.tour

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.estudario.R

enum class TutorialVideo(val title: String, val description: String, @RawRes val resource: Int) {
    EDITAL("Montar o edital", "Do PDF à importação do edital, em menos de 30 segundos.", R.raw.tutorial_edital),
    CONTENT("Gerar conteúdo", "Escolha um tópico, envie o pedido e importe o conteúdo.", R.raw.tutorial_conteudo),
}

@Composable
fun TutorialVideoDialog(tutorial: TutorialVideo, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var playbackError by remember(tutorial) { mutableStateOf(false) }
    val currentPlaybackError by rememberUpdatedState(playbackError)
    val videoView = remember(tutorial) {
        VideoView(context).apply {
            setOnPreparedListener { player ->
                player.isLooping = true
                player.setVolume(0f, 0f)
                start()
            }
            setOnErrorListener { _, _, _ -> playbackError = true; true }
            setVideoURI(Uri.parse("android.resource://${context.packageName}/${tutorial.resource}"))
        }
    }
    DisposableEffect(videoView, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> videoView.pause()
                Lifecycle.Event.ON_RESUME -> if (!currentPlaybackError) videoView.start()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            videoView.stopPlayback()
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(tutorial.title, style = MaterialTheme.typography.titleLarge)
                        Text(tutorial.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Outlined.Close, "Fechar vídeo") }
                }
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    if (playbackError) {
                        Text("Não foi possível reproduzir o vídeo neste aparelho. Use o guia em Mais › Como usar o app.")
                    } else {
                        AndroidView(
                            factory = { videoView },
                            modifier = Modifier.fillMaxHeight().widthIn(max = 360.dp).aspectRatio(360f / 890f),
                        )
                    }
                }
                Button(
                    onClick = { videoView.seekTo(0); videoView.start() },
                    enabled = !playbackError,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Outlined.Replay, null)
                    Text("Ver novamente", Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
