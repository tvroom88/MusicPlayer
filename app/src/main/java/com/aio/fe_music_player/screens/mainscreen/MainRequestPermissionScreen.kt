package com.aio.fe_music_player.screens.mainscreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat


@Composable
fun rememberAudioPermissionState(context: Context): PermissionState {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isChecked by remember { mutableStateOf(hasPermission) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted: Boolean ->
            hasPermission = granted
            isChecked = true
            Toast.makeText(
                context,
                if (granted) "퍼미션이 승인되었습니다." else "퍼미션이 거부되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(permission)
        }
    }

    return PermissionState(
        hasPermission,
        isPermissionChecked = isChecked,
        requestPermission = { launcher.launch(permission) },
        permissionName = permission
    )
}

data class PermissionState(
    val hasPermission: Boolean,
    val isPermissionChecked: Boolean, // ✅ 퍼미션 체크 완료 여부
    val requestPermission: () -> Unit,
    val permissionName: String
)