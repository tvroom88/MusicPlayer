package com.aio.fe_music_player.screens.bottomplay

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController

@Composable
fun BottomPlayer(
    controller: MediaController,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val curMediaItem = controller.currentMediaItem
    Surface(
        modifier = modifier,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = curMediaItem?.mediaMetadata?.title.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                controller.let {
                    if (it.isPlaying) {
                        it.pause()
                    } else {
                        it.play()
                    }
                }
            }) {
                Text(if (isPlaying) "⏸" else "▶️")
            }
        }
    }
}
