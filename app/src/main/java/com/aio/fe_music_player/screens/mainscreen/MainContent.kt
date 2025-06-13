package com.aio.fe_music_player.screens.mainscreen

import android.net.Uri
import androidx.compose.foundation.layout.Box
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
import com.aio.fe_music_player.screens.mainscreen.folder.Folder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * 1. Folder 상태인지
 * 2. Detail 상태인지
 */
@Composable
fun MainContent(
    mainViewModel: MainViewModel,
    selectedTab: String,
    navController: NavController
) {

    val musicList by mainViewModel.musicList.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.loadMusic()
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
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
}

