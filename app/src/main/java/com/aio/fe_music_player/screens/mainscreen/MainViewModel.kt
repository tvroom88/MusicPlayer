package com.aio.fe_music_player.screens.mainscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aio.fe_music_player.data.local.DeviceMusicDataSource
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val musicRepository: MusicRepository) : ViewModel() {

    // 음악 권한 불러오는 부분
    private val _hasAudioPermission = MutableStateFlow(false)
    val hasAudioPermission: StateFlow<Boolean> = _hasAudioPermission.asStateFlow()

    // 음악 데이터를 예시로 List<String>으로 처리 (파일명, 제목 등)
    private val _musicList = MutableStateFlow<List<MusicData>>(emptyList())
    val musicList: StateFlow<List<MusicData>> = _musicList

    // 검색어를 위한 상태
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // 선택된 음악
    private val _selectedMusic = MutableStateFlow<MusicData?>(null)
    val selectedMusic: StateFlow<MusicData?> = _selectedMusic

    // Query에 의해 걸러진 음악
    private val _searchedMusicList = MutableStateFlow<List<MusicData>>(emptyList())
    val searchedMusicList: StateFlow<List<MusicData>> = _searchedMusicList

    // MusicList가 변경되었는지 확인하기 위한 부분 -> BottomPlayer 클릭시는 음악 재생이 그대로 진행되어야 한다.
    private val _isComeFromBottomPlayer = MutableStateFlow(true)
    val isComeFromBottomPlayer: StateFlow<Boolean> = _isComeFromBottomPlayer

    fun updateAudioPermission(granted: Boolean) {
        _hasAudioPermission.value = granted
    }

    fun loadMusic() {
        val data = musicRepository.loadMusicData()
        _musicList.value = data
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedMusic(music: MusicData) {
        _selectedMusic.value = music
    }

    fun setSearchedMusicList(searchedMusicList: List<MusicData>) {
        _searchedMusicList.value = searchedMusicList
    }

    fun setIsComeFromBottomPlayer(isComeFromBottomPlayer:Boolean){
        _isComeFromBottomPlayer.value = isComeFromBottomPlayer
    }

}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MusicRepository(DeviceMusicDataSource(context))
        return MainViewModel(repository) as T
    }
}
