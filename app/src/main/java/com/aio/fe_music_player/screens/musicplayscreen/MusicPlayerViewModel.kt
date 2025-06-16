package com.aio.fe_music_player.screens.musicplayscreen

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import androidx.concurrent.futures.await
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.service.MusicPlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller: StateFlow<MediaController?> = _controller.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentUri = MutableStateFlow<Uri?>(null)
    val currentUri: StateFlow<Uri?> = _currentUri.asStateFlow()

    private val context = getApplication<Application>().applicationContext

    suspend fun initController(musicList: List<MusicData>, startMusic: MusicData) {
        if (_controller.value == null) {
            val mediaItems = musicList.map { music ->
                MediaItem.Builder()
                    .setUri(Uri.parse(music.uriString))
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(music.name)
                            .setArtist(music.artist)
                            .build()
                    )
                    .build()
            }

            val sessionToken = SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
            val newController = MediaController.Builder(context, sessionToken).buildAsync().await()
            _controller.value = newController

            newController.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                    _isPlaying.value = isPlayingNow
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    _currentUri.value = mediaItem?.localConfiguration?.uri
                }
            })

            val startIndex = musicList.indexOfFirst { it.uriString == startMusic.uriString }
            if (startIndex != -1) {
                newController.setMediaItems(mediaItems, startIndex, C.TIME_UNSET)
                newController.prepare()
                newController.playWhenReady = true
            }
        } else {
            // 이미 컨트롤러 있으면 그냥 재생할 곡 바꾸기
            playMusic(musicList, startMusic)
        }
    }

    fun playMusic(musicList: List<MusicData>, musicData: MusicData) {
        val ctrl = _controller.value ?: return
        val mediaItems = musicList.map {
            MediaItem.Builder()
                .setUri(Uri.parse(it.uriString))
                .build()
        }
        val startIndex = musicList.indexOfFirst { it.uriString == musicData.uriString }
        if (startIndex != -1) {
            ctrl.setMediaItems(mediaItems, startIndex, C.TIME_UNSET)
            ctrl.prepare()
            ctrl.playWhenReady = true
        }
    }

    fun releaseController() {
        _controller.value?.release()
        _controller.value = null
    }
}
