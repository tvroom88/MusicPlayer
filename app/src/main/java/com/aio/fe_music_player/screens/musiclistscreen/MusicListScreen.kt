package com.aio.fe_music_player.screens.musiclistscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aio.fe_music_player.R
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.screens.bottomplay.BottomPlayer
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayerViewModel

@Composable
fun MusicListScreen(
    musicList: List<MusicData>,
    mainViewModel: MainViewModel,
    navController: NavController,
    viewModel: MusicPlayerViewModel
) {

    val controller by viewModel.controller.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState() // 현재 play 중인지 체크 (Play 혹은 Pause 버튼 표시에 사용)

    LaunchedEffect(isPlaying) {
        if(isPlaying){
            viewModel.startTrackingPlayback()
        }else{
            viewModel.stopTrackingPlayback() // 직접 멈추는 함수 필요 (Job 취소용)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(musicList.size) { musicIdx ->
                val selectedMusic = musicList[musicIdx]

                MusicListItem(
                    musicData = selectedMusic,
                    musicItemClick = {
                        mainViewModel.setSelectedMusic(selectedMusic)
                        mainViewModel.setSearchedMusicList(musicList)
                        navController.navigate("musicPlay")
                    })
            }
        }

        // 하단 고정 플레이어
        if (controller?.currentMediaItem != null) {
            controller?.let {
                BottomPlayer(
                    controller = it,
                    isPlaying = isPlaying,
                    viewModel = viewModel,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun MusicListItem(musicData: MusicData, musicItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { musicItemClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 폴더 아이콘
        Image(
            painter = painterResource(id = R.drawable.music),
            contentDescription = "Folder Icon",
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .padding(12.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            // 1. 음악 이름
            Text(
                text = musicData.name,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(3.dp))

            // 2. 음악 길이
            Text(
                text = musicData.duration,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
