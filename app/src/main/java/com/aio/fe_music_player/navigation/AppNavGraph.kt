package com.aio.fe_music_player.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aio.fe_music_player.data.model.MusicData
import com.aio.fe_music_player.screens.mainscreen.MainScreen
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import com.aio.fe_music_player.screens.musiclistscreen.MusicListScreen
import com.aio.fe_music_player.screens.musiclistscreen.MusicListViewModel
import com.aio.fe_music_player.screens.musicplayscreen.MusicPlayScreen
import kotlinx.serialization.json.Json

@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainScreen(
                mainViewModel = mainViewModel,
                navController = navController
            )
        }

        composable(
            "musicList/{musicListJson}",
            arguments = listOf(navArgument("musicListJson") {
                type = NavType.StringType
            }) // List 넘겨주는 방식
        ) { backStackEntry ->

            val musicListViewModel: MusicListViewModel = viewModel()
            val json = backStackEntry.arguments?.getString("musicListJson") ?: "[]"
            val musicList = Json.decodeFromString<List<MusicData>>(Uri.decode(json))
            MusicListScreen(
                musicList = musicList,
                musicListViewModel,
                navController
            )
        }

        composable(
            "musicPlay/{musicJson}",
            arguments = listOf(navArgument("musicJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val musicJson = backStackEntry.arguments?.getString("musicJson") ?: "{}"
            val musicData = Json.decodeFromString<MusicData>(Uri.decode(musicJson))
            MusicPlayScreen(musicData = musicData)
        }
    }


}