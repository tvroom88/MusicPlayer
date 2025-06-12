package com.aio.fe_music_player.screens.mainscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aio.fe_music_player.data.local.DeviceMusicDataSource
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val musicRepository: MusicRepository) : ViewModel() {

    // 음악 데이터를 예시로 List<String>으로 처리 (파일명, 제목 등)
    private val _musicList = MutableStateFlow<List<MusicData>>(emptyList())
    val musicList: StateFlow<List<MusicData>> = _musicList

    fun loadMusic() {
        val data = musicRepository.loadMusicData()
        _musicList.value = data
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MusicRepository(DeviceMusicDataSource(context))
        return MainViewModel(repository) as T
    }
}