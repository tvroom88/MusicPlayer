package com.aio.fe_music_player.screens.bottomplay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import com.aio.fe_music_player.R
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayerViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun BottomPlayer(
    controller: MediaController,
    isPlaying: Boolean,
    viewModel: MusicPlayerViewModel,
    modifier: Modifier = Modifier,
    clickEvent: () -> Unit
) {
    val curMediaItem = controller.currentMediaItem
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .clickable { clickEvent() }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // 스와이프 거리 기준 판단
                        if (offsetX.value > 200f || offsetX.value < -200f) {
                            // 애니메이션으로 화면 밖으로 이동
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = if (offsetX.value > 0) screenWidth else -screenWidth,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                controller.stop() // 음악 종료 처리
                                viewModel.resetPlayerState()
                            }
                        } else {
                            // 원래 위치로 복귀
                            scope.launch {
                                offsetX.animateTo(0f, animationSpec = tween(durationMillis = 300))
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                    }
                )
            },
        color = Color.DarkGray,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f) // ✅ 텍스트 영역 최대 폭 제한
                        .height(50.dp)
                ) {
                    Text(
                        text = curMediaItem?.mediaMetadata?.title.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        color = Color.White
                    )

                    Text(
                        text = curMediaItem?.mediaMetadata?.artist.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Column과 Button 사이 여백

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // 배경색 제거
                        contentColor = Color.White          // 텍스트/아이콘 색
                    ),
                    contentPadding = PaddingValues(0.dp),   // 내부 패딩 제거
                    elevation = ButtonDefaults.buttonElevation(0.dp), // 그림자 제거
                    onClick = {
                        controller.let {
                            if (it.isPlaying) {
                                it.pause()
                            } else {
                                it.play()
                            }
                        }
                    },
                ) {
                    Image(
                        painter = painterResource(id = if (isPlaying) R.drawable.music_pause else R.drawable.music_play),
                        contentDescription = "동그란 버튼 이미지",
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            LinearProgressIndicator(
                progress = {
                    currentPosition.toFloat() / duration.coerceAtLeast(
                        1
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp), // 아주 얇게
                color = Color(0xFFFF9800), // 주황색 (Material 오렌지 500)
                trackColor = Color.DarkGray,
            )
        }
    }
}

