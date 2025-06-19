package com.aio.fe_music_player.screens.mainscreen.toolbar.inside

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aio.fe_music_player.R
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(mainViewModel: MainViewModel, navController: NavController) {
    val musicList by mainViewModel.musicList.collectAsState()
    val query by mainViewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 검색창
        OutlinedTextField(
            value = query,
            onValueChange = { mainViewModel.updateSearchQuery(it) },
            label = { Text("검색", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White), // ✅ 텍스트 색상은 여기서 설정
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검색된 음악만 필터링
        val filteredMusic = musicList.filter {
            it.name.contains(query, ignoreCase = true)
        }

        // 리스트 출력
        LazyColumn {
            items(filteredMusic.size) { idx ->
                val selectedMusic = filteredMusic[idx]
                MusicListItem(musicData = selectedMusic, musicItemClick = {
                    val musicJson = Uri.encode(Json.encodeToString(selectedMusic))
                    val musicListJson = Uri.encode(Json.encodeToString(musicList))
                    navController.navigate("musicPlay/$musicJson/$musicListJson") {
                        popUpTo("search") { inclusive = true }
                    }
                })
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


//val musicJson = Uri.encode(Json.encodeToString(musicData))
//val musicListJson = Uri.encode(Json.encodeToString(musicList))
//
//navController.navigate("musicPlay/$musicJson/$musicListJson") {
//    popUpTo("search") { inclusive = true } // ✅ SearchScreen을 스택에서 제거
//}