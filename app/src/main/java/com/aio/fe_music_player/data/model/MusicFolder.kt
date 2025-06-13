package com.aio.fe_music_player.data.model

data class MusicFolder(
    val folderId: Long,
    val folderName: String,
    val musicFile: List<MusicData> = arrayListOf()
)