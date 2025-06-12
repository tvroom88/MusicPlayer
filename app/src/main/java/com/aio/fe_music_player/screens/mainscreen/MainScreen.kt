package com.aio.fe_music_player.screens.mainscreen

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.aio.fe_music_player.screens.mainscreen.tag.MyTag
import com.aio.fe_music_player.screens.mainscreen.toolbar.MyToolbar

/**
 *  - MyToolbar
 *  - MyTag
 *  - MainContent
 */
@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavController) {

    val context = LocalContext.current
    val permissionState = rememberAudioPermissionState(context) // Permission 승낙 상태

    // Status Bar 아이콘을 흰색으로 처리하는 부분
    SetSystemBarsDarkIcons(useDarkIcons = false)

    // Tab의 List
    val tabs = listOf("폴더", "나를 위한", "노래", "재생 목록")
    var selectedTab by remember { mutableStateOf("폴더") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            // Toolbar 부분
            MyToolbar()
            Spacer(modifier = Modifier.height(20.dp)) // 위쪽 마진 효과

            // Tag 부분
            MyTag(
                tabs,
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (permissionState.hasPermission) {
                MainContent(mainViewModel, selectedTab, navController)

//                AppNavGraph(navController = navController, mainViewModel = mainViewModel, selectedTab = selectedTab)

//                fun AppNavGraph(
//                    navController: NavHostController,
//                    mainViewModel: MainViewModel,
//                    selectedTab: String,
//                ) {
            } else {
                RequestPermissionScreen(onRequestPermission = {
                    permissionState.requestPermission()
                })
            }
            // 여기에 Permission 여부에 따라 변경
        }
    }
}

/**
 * 음악 파일을 불러오는 권한이 없을 경우 보여주는 화면
 */
@Composable
fun RequestPermissionScreen(onRequestPermission: () -> Unit) {
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
//            Button(onClick = onRequestPermission) {
//                Text("권한 요청")
//            }
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

@Preview
@Composable
fun showMain() {
//    FE_Music_PlayerTheme {
//        MainScreen(mainViewModel = null)
//    }
}
