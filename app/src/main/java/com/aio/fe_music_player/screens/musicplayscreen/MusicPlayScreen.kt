package com.aio.fe_music_player.screens.musicplayscreen

import android.content.ComponentName
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.service.MusicPlayerService

@Composable
fun MusicPlayScreen(musicData: MusicData) {
//    Text(text = "music play screen : $musicData", color = Color.White)
    val context = LocalContext.current
    var controller by remember { mutableStateOf<MediaController?>(null) }

    LaunchedEffect(Unit) {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
        controller = MediaController.Builder(context, sessionToken).buildAsync().await()
        controller?.apply {
            setMediaItem(MediaItem.fromUri(musicData.uriString))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            controller?.release()
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = musicData.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = musicData.uriString,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { controller?.play() }) {
                Text("▶️ Play")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { controller?.pause() }) {
                Text("⏸ Pause")
            }
        }
    }
}

