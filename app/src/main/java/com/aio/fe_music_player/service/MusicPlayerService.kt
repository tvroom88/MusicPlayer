package com.aio.fe_music_player.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.aio.fe_music_player.screens.activity.StartActivity


@UnstableApi
class MusicPlayerService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private var mediaLibrarySession: MediaLibrarySession? = null

    private lateinit var audioFocusListener: AudioFocusListener
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest

    private val mediaLibrarySessionCallback = object : MediaLibrarySession.Callback {    }


    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        // ExoPlayer & Media Session
        player = ExoPlayer.Builder(this).build()

        val intent = Intent(this, StartActivity::class.java).apply {
            // 새 액티비티 스택을 만듭니다
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

//        mediaLibrarySession =
//            MediaLibrarySession.Builder(this, player, MusicPlayerSessionCallback())
//                .setSessionActivity(pendingIntent)  // 알림 클릭 시 실행될 Activity Intent 등록 )
//                .build()

        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, mediaLibrarySessionCallback)
                .setSessionActivity(pendingIntent)  // 알림 클릭 시 실행될 Activity Intent 등록 )
                .build()

        // 현재 Audio에 Focus가 맞춰져있는지 아닌지 (다른 미디어가 재생될 경우 정지를 위해)
        audioFocusListener = AudioFocusListener(player)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setOnAudioFocusChangeListener(audioFocusListener)
            .build()

        val result = audioManager.requestAudioFocus(audioFocusRequest)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            audioFocusListener.hasAudioFocus = true
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        mediaLibrarySession?.player?.let { player ->
            if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
//                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }
}
