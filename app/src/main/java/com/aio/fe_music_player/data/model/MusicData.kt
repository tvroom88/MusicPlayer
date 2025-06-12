package com.aio.fe_music_player.data.model

import android.net.Uri

data class MusicData(
    var uri: Uri,
    var name: String,
    var bucketId: Long = 0,
    var bucketName: String = ""
)