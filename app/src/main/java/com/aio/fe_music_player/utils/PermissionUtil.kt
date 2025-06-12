package com.aio.fe_music_player.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionUtil(
    private val activity: Activity,
    private val permission: String,
    private val launcher: ActivityResultLauncher<String>
) {

    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(activity, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(permission)
        } else {
            Toast.makeText(activity, "퍼미션 이미 승인됨", Toast.LENGTH_SHORT).show()
        }
    }
}