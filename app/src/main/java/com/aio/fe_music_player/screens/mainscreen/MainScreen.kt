package com.aio.fe_music_player.screens.mainscreen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.aio.fe_music_player.screens.mainscreen.tag.MyTag
import com.aio.fe_music_player.screens.mainscreen.toolbar.MyToolbar
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayerViewModel
import kotlinx.coroutines.launch

/**
 *  - MyToolbar
 *  - MyTag
 *  - MainContent
 */
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    mainPlayerViewModel: MusicPlayerViewModel,
    navController: NavController
) {

    val hasAudioPermission by mainViewModel.hasAudioPermission.collectAsState()

    // 음악을 한 번만 로드했는지 체크
    var loadedOnce by rememberSaveable { mutableStateOf(false) }

    // Status Bar 아이콘을 흰색으로 처리하는 부분
    SetSystemBarsDarkIcons(useDarkIcons = false)

    // Tab의 List
    val tabs = listOf("폴더", "나를 위한", "노래", "재생 목록")
    var selectedTab by remember { mutableStateOf("폴더") }


    val scope = rememberCoroutineScope()
    LaunchedEffect(hasAudioPermission) {
        if (hasAudioPermission && !loadedOnce) {
            Log.d("HereHereHere", "hasAudioPermission : $hasAudioPermission")

            loadedOnce = true // 다시 들어와도 재실행 방지

            // 음악 로드
            mainViewModel.loadMusic()

            // controller 초기화
            scope.launch {
                mainPlayerViewModel.initController()
            }
        }

    }


    Column {
        // Toolbar 부분
        MyToolbar(onSearchClick = {
            navController.navigate("search")
        })
        Spacer(modifier = Modifier.height(20.dp)) // 위쪽 마진 효과

        // Tag 부분
        MyTag(
            tabs,
            selectedTab = selectedTab,
            onTabSelected = { tab -> selectedTab = tab }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (hasAudioPermission) {
            MainContent(
                mainViewModel,
                mainPlayerViewModel,
                selectedTab,
                navController
            )
        } else {
            RequestPermissionScreen()
        }
    }
}


/**
 * 음악 파일을 불러오는 권한이 없을 경우 보여주는 화면
 */
@Composable
fun RequestPermissionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "앱 사용을 위해 오디오 접근 권한이 필요합니다.",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


// Status Bar 아이콘을 흰색으로 처리하는 부분
@Composable
fun SetSystemBarsDarkIcons(useDarkIcons: Boolean) {
    val view = LocalView.current
    val activity = view.context as? Activity

    SideEffect {
        activity?.window?.let { window ->
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = useDarkIcons // false = white icons
            controller.isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}
