package com.aio.fe_music_player.screens.mainscreen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.aio.fe_music_player.data.model.MusicFolder
import com.aio.fe_music_player.screens.bottomplay.BottomPlayer
import com.aio.fe_music_player.screens.mainscreen.folder.Folder
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayerViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * 1. Folder 상태인지
 * 2. Detail 상태인지
 */
@Composable
fun MainContent(
    mainViewModel: MainViewModel,
    mainPlayerViewModel: MusicPlayerViewModel,
    selectedTab: String,
    navController: NavController
) {

    val musicList by mainViewModel.musicList.collectAsState()
    val controller by mainPlayerViewModel.controller.collectAsState()
    val isPlaying by mainPlayerViewModel.isPlaying.collectAsState() // 현재 play 중인지 체크 (Play 혹은 Pause 버튼 표시에 사용)


    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mainPlayerViewModel.startTrackingPlayback()
        } else {
            mainPlayerViewModel.stopTrackingPlayback() // 직접 멈추는 함수 필요 (Job 취소용)
        }
    }


    val folderList = remember(musicList) {
        musicList.groupBy { it.bucketId to it.bucketName } // folderId와 folderName 기준으로 그룹화
            .map { (key, musics) ->
                val (folderId, folderName) = key
                MusicFolder(
                    folderId = folderId,
                    folderName = folderName,
                    musicFile = ArrayList(musics.map { it })
                )
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                "폴더" -> Folder(
                    folderList = folderList,
                    onFolderClick = { folder ->
                        val jsonList = Uri.encode(Json.encodeToString(folder.musicFile.toList()))
                        navController.navigate("musicList/$jsonList")
                    })

                "노래" -> Text(color = Color.White, text = "노래")
                else -> Text(color = Color.White, text = "Unknown tab")
            }
        }

        if (controller?.currentMediaItem != null) {
            controller?.let {
                BottomPlayer(
                    controller = it,
                    isPlaying = isPlaying,
                    viewModel = mainPlayerViewModel,
                    modifier = Modifier
                        .fillMaxWidth(),
                    clickEvent = {
                        mainViewModel.setIsComeFromBottomPlayer(true)
                        navController.navigate("musicPlay")
                    }
                )
            }
        }
    }
}

