package com.aio.fe_music_player.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aio.fe_music_player.R

class MainActivity : AppCompatActivity() {

    private lateinit var feMpViewModel: FeMpViewModel

    private lateinit var loadBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        getPermission()

        init()
    }

    private fun getPermission(){
        checkAndRequestAudioPermission()
    }

    private fun init() {
        feMpViewModel = ViewModelProvider(this)[FeMpViewModel::class.java]

        loadBtn = findViewById(R.id.btn_main_load)
        loadBtn.setOnClickListener {
            Log.d("TestTestTest", "TestTestTest")
            feMpViewModel.loadMusicData(this)
        }

    }

    fun checkAndRequestAudioPermission() {
        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.READ_MEDIA_AUDIO
            }
            else -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                1001
            )
        } else {
            // 권한 이미 있음
//            loadMusicFiles() // 예시 함수
        }
    }
}