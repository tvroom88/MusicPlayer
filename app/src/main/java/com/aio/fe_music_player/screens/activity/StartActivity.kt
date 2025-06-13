package com.aio.fe_music_player.screens.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
    private lateinit var audioPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var postNotificationPermissionLauncher: ActivityResultLauncher<String>

    private var permissionRequested = false // 권한 요청 중복 방지용
    private var postNotificationPermissionRequested = false

    // Notification
    private val CHANNEL_ID = "test_channel"
    private val NOTIFICATION_ID = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        showTestNotification()

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(this)
        )[MainViewModel::class.java]


        // 권한 검사 및 요청
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val postNotificationPermission = Manifest.permission.POST_NOTIFICATIONS

        // 권한 요청 콜백 등록
        audioPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                mainViewModel.updateAudioPermission(granted)
            }

        postNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> }


        enableEdgeToEdge()
        setContent {

            var isSplashShown by remember { mutableStateOf(true) }
            val context = LocalContext.current

            // Navigation From MainScreen to MusicListScreen
            val navController = rememberNavController()

            // Splash 화면 유지 시간 (예: 2초)
            LaunchedEffect(Unit) {
                delay(1000) // 2초 동안 Splash 화면 보여줌
                isSplashShown = false
            }

            // Splash 끝나면 권한 요청 처리
            LaunchedEffect(isSplashShown) {
                if (!isSplashShown && !permissionRequested) {
                    permissionRequested = true

                    if (ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mainViewModel.updateAudioPermission(true)
                    } else {
                        audioPermissionLauncher.launch(permission)
                    }
                }

                if (!postNotificationPermissionRequested && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    postNotificationPermissionRequested = true
                    if (ContextCompat.checkSelfPermission(context, postNotificationPermission) != PackageManager.PERMISSION_GRANTED) {
                        postNotificationPermissionLauncher.launch(postNotificationPermission)
                    }
                }
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "테스트 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "테스트용 알림 채널"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showTestNotification() {
        val intent = Intent(this, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("테스트 알림")
            .setContentText("이 알림을 누르면 StartActivity가 열립니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
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