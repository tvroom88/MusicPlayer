package com.aio.fe_music_player.screens.musiclistscreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aio.fe_music_player.R
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayerViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MusicListScreen(
    musicList: List<MusicData>,
    musicListViewModel: MusicListViewModel,
    navController: NavController,
    viewModel: MusicPlayerViewModel
) {
    Column {
        LazyColumn {
            items(musicList.size) { musicIdx ->
                val selectedMusic = musicList[musicIdx]

                MusicListItem(
                    musicData = selectedMusic,
                    musicItemClick = {
                        val musicJson = Uri.encode(Json.encodeToString(selectedMusic))
                        val musicListJson = Uri.encode(Json.encodeToString(musicList))

                        navController.navigate("musicPlay/$musicJson/$musicListJson")
                    })
            }
        }
        BottomPlayer()
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
            // 1. 폴더 이름
            Text(
                text = musicData.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(3.dp))

            // 2. 폴더 내 음악 파일 갯수
            Text(
                text = musicData.duration,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


//@Composable
//fun BottomPlayer(music: MusicData, onClick: () -> Unit) {
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//            .background(Color.DarkGray)
//            .clickable { onClick() },
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Start
//    ) {
//        Text(music.name, color = Color.White, modifier = Modifier.padding(start = 16.dp))
//        // 재생/일시정지 버튼 등 추가 가능
//    }
//}

@Composable
fun BottomPlayer() {
    Text(text = "hihihi", color = Color.White)
}

