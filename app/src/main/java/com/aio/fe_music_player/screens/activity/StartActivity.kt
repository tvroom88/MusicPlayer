package com.aio.fe_music_player.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aio.fe_music_player.screens.FeMpViewModel
import com.aio.fe_music_player.screens.main.MainScreen
import com.aio.fe_music_player.screens.theme.FE_Music_PlayerTheme

class StartActivity : ComponentActivity() {

    private val feMpViewModel: FeMpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            FE_Music_PlayerTheme {
                MainScreen()
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