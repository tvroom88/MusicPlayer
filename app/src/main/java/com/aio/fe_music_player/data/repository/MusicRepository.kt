package com.aio.fe_music_player.data.repository

import com.aio.fe_music_player.data.local.DeviceMusicDataSource
import com.aio.fe_music_player.data.model.MusicData

class MusicRepository(private val deviceMusicDataSource: DeviceMusicDataSource) {

    fun loadMusicData(): List<MusicData> {
        return deviceMusicDataSource.loadMusicData()
    }
}