package com.aio.fe_music_player.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicData(
    var uriString: String,
    var name: String,
    var duration: String = "",
    var bucketId: Long = 0,
    var bucketName: String = ""
)