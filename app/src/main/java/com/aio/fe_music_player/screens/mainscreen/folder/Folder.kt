package com.aio.fe_music_player.screens.mainscreen.folder

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
import com.aio.fe_music_player.R
import com.aio.fe_music_player.data.model.MusicFolder

// 1. 폴더 - Folder -> Detail
@Composable
fun Folder(folderList: List<MusicFolder>, onFolderClick: (MusicFolder) -> Unit) {
    LazyColumn {
        items(folderList.size) { folderIndex ->
            val folder = folderList[folderIndex]
            FolderItem(folder, onClick = { onFolderClick(folder) })
        }
    }
}

/**
 * 1. 폴더 아이콘, 2. 폴더 제목, 3. 폴더 내 음악 파일 갯수
 */
@Composable
fun FolderItem(folder: MusicFolder, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 폴더 아이콘
        Image(
            painter = painterResource(id = R.drawable.folder),
            contentDescription = "Folder Icon",
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .padding(12.dp), // 아이콘이 너무 크면 조절
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            // 1. 폴더 이름
            Text(
                text = folder.folderName,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(3.dp))

            // 2. 폴더 내 음악 파일 갯수
            Text(
                text = folder.musicFile.size.toString(),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}