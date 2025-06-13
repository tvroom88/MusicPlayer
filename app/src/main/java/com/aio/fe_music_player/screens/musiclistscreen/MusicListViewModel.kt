package com.aio.fe_music_player.screens.musiclistscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.aio.fe_music_player.service.MusicPlayerService

class MusicListViewModel : ViewModel() {

    // Test
    fun startMusicService(context: Context, uri: Uri) {
        val intent = Intent(context, MusicPlayerService::class.java).apply {
            action = Intent.ACTION_VIEW
            data = uri
        }
        ContextCompat.startForegroundService(context, intent)
    }
}