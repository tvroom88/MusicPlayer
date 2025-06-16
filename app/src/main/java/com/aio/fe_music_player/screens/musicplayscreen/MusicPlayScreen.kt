package com.aio.fe_music_player.screens.musicplayscreen

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.aio.fe_music_player.data.model.MusicData
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayScreen(
    musicData: MusicData,
    musicList: List<MusicData>,
    viewModel: MusicPlayerViewModel
) {

    val context = LocalContext.current

    val controller by viewModel.controller.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState() // 현재 play 중인지 체크 (Play 혹은 Pause 버튼 표시에 사용)
    val currentUri by viewModel.currentUri.collectAsState() // 현재 재생되고 있는 음원의 Uri 저장

    val currentMusic = remember(currentUri) {
        musicList.find { it.uriString == currentUri?.toString() } ?: musicData
    }

    // 재생 시간 상태
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    // controller 초기화
//    val scope = rememberCoroutineScope()
//
    LaunchedEffect(musicData) {
        viewModel.setMusic(musicList, musicData)
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                val pos = controller?.currentPosition ?: 0L
                val dur = controller?.duration?.takeIf { it > 0 } ?: 0L
                currentPosition = pos
                duration = dur
                delay(500)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // Title : Music Name
        Text(
            text = currentMusic.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Uri : 곧 삭제 예정
        Text(
            text = currentUri?.toString() ?: "",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 이전, Play, 다음곡 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { controller?.seekToPrevious() }) {
                Text("⏮")
            }

            Spacer(modifier = Modifier.width(13.dp))

            Button(onClick = {
                controller?.let {
                    if (it.isPlaying) {
                        it.pause()
                    } else {
                        it.play()
                    }
                }
            }) {
                Text(if (isPlaying) "⏸" else "▶️")
            }
            Spacer(modifier = Modifier.width(13.dp))

            Button(onClick = { controller?.seekToNext() }) {
                Text("⏭")
            }
        }

        Spacer(modifier = Modifier.width(30.dp))


        // 슬라이더
        Slider(
            value = currentPosition.coerceAtMost(duration).toFloat(),
            onValueChange = { newPos ->
                controller?.seekTo(newPos.toLong())
            },
            valueRange = 0f..(duration.coerceAtLeast(0)).toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        // 재생 시간 표시
        Text(
            text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}


//    LaunchedEffect(Unit) {
//        val mediaItems: List<MediaItem> = musicList.map { music ->
//            MediaItem.Builder()
//                .setUri(Uri.parse(music.uriString))
//                .setMediaMetadata(
//                    MediaMetadata.Builder()
//                        .setTitle(music.name)
//                        .setArtist(music.artist) // 필요시 music.artist 필드로 교체
//                        .build()
//                )
//                .build()
//        }
//
//        val sessionToken =
//            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
//        controller = MediaController.Builder(context, sessionToken).buildAsync().await()
//
//        controller?.apply {
//            val startIndex = musicList.indexOfFirst { it.uriString == musicData.uriString }
//            if (startIndex != -1) {
//                setMediaItems(mediaItems, startIndex, C.TIME_UNSET)
//                prepare()
//                playWhenReady = true
//            }
//        }
//    }
//
//    LaunchedEffect(controller) {
//        controller?.addListener(object : Player.Listener {
//            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
//                isPlaying = isPlayingNow
//            }
//
//            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                currentUri = mediaItem?.localConfiguration?.uri
//            }
//
//        })
//
//        // 최초 설정도 적용
//        currentUri = controller?.currentMediaItem?.localConfiguration?.uri
//    }