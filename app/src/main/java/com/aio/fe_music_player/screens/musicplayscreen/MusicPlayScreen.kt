package com.aio.fe_music_player.screens.musicplayscreen

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.aio.fe_music_player.R
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayScreen(
    viewModel: MusicPlayerViewModel,
    mainViewModel: MainViewModel
) {
    /**
     *     musicData: MusicData,
     *     musicList: List<MusicData>,
     */
    val musicData by mainViewModel.selectedMusic.collectAsState()
    val musicList by mainViewModel.searchedMusicList.collectAsState()

    val controller by viewModel.controller.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState() // 현재 play 중인지 체크 (Play 혹은 Pause 버튼 표시에 사용)
    val currentUri by viewModel.currentUri.collectAsState() // 현재 재생되고 있는 음원의 Uri 저장

    val currentMusic = remember(currentUri) {
        musicList.find { it.uriString == currentUri?.toString() } ?: musicData
    }

    // 재생 시간 상태
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    val isFirst by viewModel.isFirst.collectAsState()

    LaunchedEffect(musicData) {
        // Music Player에 Music 세팅
        musicData?.let { viewModel.setMusic(musicList, it) }
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

    val lifecycleOwner = LocalLifecycleOwner.current

    // 화면에서 벗어날 때 isFirst = true로 복구
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("CheckCheckCheck", "event : ${event.name}")
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.setIsFirstTrue()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // Title : Music Name
        Text(
            text = currentMusic?.name ?: "",
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

        MusicTimerSlicer(controller, currentPosition, duration)

        Spacer(modifier = Modifier.width(30.dp))


        MusicControlButtons(
            isPlaying = isPlaying,
            onPrevClick = { controller?.seekToPrevious() },
            onPlayPauseClick = {
                controller?.let {
                    if (it.isPlaying) it.pause() else it.play()
                }
            },
            onNextClick = { controller?.seekToNext() },
            isFirst
        )

    }
}

// 음악 재생중인 시간과 전체 시간
@Composable
fun MusicTimerSlicer(controller: MediaController?, currentPosition: Long, duration: Long) {
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

@Composable
fun MusicControlButtons(
    isPlaying: Boolean,
    onPrevClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    isFirst: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onPrevClick,
            shape = CircleShape,
            contentPadding = PaddingValues(2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.music_previous),
                contentDescription = "동그란 버튼 이미지",
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Button(
            onClick = onPlayPauseClick,
            shape = CircleShape,
            contentPadding = PaddingValues(2.dp),
        ) {
            // 처음에는 isFirst가 true
            if (isFirst || isPlaying) {
                Log.d("CheckCheckCheck", "111. isFirst : $isFirst, isPlaying : $isPlaying")
                Image(
                    painter = painterResource(id = R.drawable.music_pause),
                    contentDescription = "동그란 버튼 이미지",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
            } else {

                Log.d("CheckCheckCheck", "222. isFirst : $isFirst, isPlaying : $isPlaying")

                Image(
                    painter = painterResource(id = R.drawable.music_play),
                    contentDescription = "동그란 버튼 이미지",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Button(
            onClick = onNextClick,
            shape = CircleShape,
            contentPadding = PaddingValues(2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.music_next),
                contentDescription = "동그란 버튼 이미지",
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
