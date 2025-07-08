package com.aio.fe_music_player.screens.musicplayscreen

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.concurrent.futures.await
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.service.MusicPlayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private var _controller = MutableStateFlow<MediaController?>(null)
    val controller: StateFlow<MediaController?> = _controller.asStateFlow()

    // 현재 진행중인지 체크
    private var _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var _currentUri = MutableStateFlow<Uri?>(null)
    val currentUri: StateFlow<Uri?> = _currentUri.asStateFlow()

    private val context = getApplication<Application>().applicationContext

    private var _isFirst = MutableStateFlow(true)
    val isFirst: StateFlow<Boolean> = _isFirst.asStateFlow()

    // --------- 현재 시간 재는 부분을 구현하기 위해 ---------
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private var trackingJob: Job? = null

    fun setIsFirstTrue() {
        _isFirst.value = true
    }

    // Controller 초기화
    @OptIn(UnstableApi::class)
    suspend fun initController() {

        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
        val newController = MediaController.Builder(context, sessionToken).buildAsync().await()
        _controller.value = newController

        newController.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                _isPlaying.value = isPlayingNow

                // 첫번째를 체크해주기 위해 && _isPlaying가 false 에서 true로 변경
                if (_isFirst.value && _isPlaying.value) {
                    _isFirst.value = false
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentUri.value = mediaItem?.localConfiguration?.uri
            }
        })
    }

    // 음악 데이터 세팅 + 재생까지 함께 되는 듯
    fun setMusic(musicList: List<MusicData>, startMusic: MusicData) {
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

        val startIndex = musicList.indexOfFirst { it.uriString == startMusic.uriString }
        if (startIndex != -1) {
            val newController = controller.value
            newController?.setMediaItems(mediaItems, startIndex, C.TIME_UNSET)
            newController?.prepare()
            newController?.playWhenReady = true
        }
    }



    fun startTrackingPlayback() {
        if (trackingJob?.isActive == true) return

        trackingJob = viewModelScope.launch {
            while (true) {
                _controller.value?.let { mediaController ->
                    _currentPosition.value = mediaController.currentPosition
                    _duration.value = mediaController.duration.takeIf { it > 0 } ?: 0L
                }

                delay(500)
            }
        }
    }

    fun stopTrackingPlayback() {
        trackingJob?.cancel()
        trackingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        controller.value?.release()
        _controller.value = null
    }

    fun resetPlayerState() {
        _currentPosition.value = 0L
        _duration.value = 0L
        // 추가로 필요한 상태 초기화
    }
}
