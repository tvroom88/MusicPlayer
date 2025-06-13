package com.aio.fe_music_player.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.aio.fe_music_player.navigation.AppNavGraph
import com.aio.fe_music_player.screens.FeMpViewModel
import com.aio.fe_music_player.screens.mainscreen.MainViewModel
import com.aio.fe_music_player.screens.mainscreen.MainViewModelFactory
import com.aio.fe_music_player.screens.theme.FE_Music_PlayerTheme
import kotlinx.coroutines.delay

class StartActivity : ComponentActivity() {

    private val feMpViewModel: FeMpViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(this)
        )[MainViewModel::class.java]


        enableEdgeToEdge()
        setContent {

            var isSplashShown by remember { mutableStateOf(true) }

            // Navigation From MainScreen to MusicListScreen
            val navController = rememberNavController()

            // Splash 화면 유지 시간 (예: 2초)
            LaunchedEffect(Unit) {
                delay(1000) // 2초 동안 Splash 화면 보여줌
                isSplashShown = false
            }

            FE_Music_PlayerTheme {
                if (isSplashShown) {
                    Text(color = Color.White, text = "splash")
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Black
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .padding(paddingValues)
                        ) {
                            AppNavGraph(mainViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FE_Music_PlayerTheme {
        Greeting("Android")
    }
}