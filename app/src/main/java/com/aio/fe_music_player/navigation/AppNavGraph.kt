package com.aio.fe_music_player.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aio.fe_music_player.screens.mainscreen.MainScreen
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import com.aio.fe_music_player.screens.musiclistscreen.MusicListScreen

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
            "folderDetail/{folderName}",
            arguments = listOf(navArgument("folderName") { type = NavType.StringType })
        ) { backStackEntry ->
            val folderName = backStackEntry.arguments?.getString("folderName") ?: ""
//            MusicListScreen(folderName = folderName)
            MusicListScreen()
        }
    }
}