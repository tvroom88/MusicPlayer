package com.aio.fe_music_player.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.aio.fe_music_player.R


class MusicPlayerService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private var mediaLibrarySession: MediaLibrarySession? = null

    private lateinit var audioFocusListener: AudioFocusListener
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest


    // MediaLibrarySession 콜백 정의 (Controller 연결 허용)
    private val mediaLibrarySessionCallback = object : MediaLibrarySession.Callback {
        @OptIn(UnstableApi::class)
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            return MediaSession.ConnectionResult.accept(
                MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS,
                MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        // 플레이어 및 세션 초기화
        player = ExoPlayer.Builder(this).build()
        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, mediaLibrarySessionCallback).build()


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


        // 포그라운드 알림 설정
        setMediaNotification()
    }

    @OptIn(UnstableApi::class)
    private fun setMediaNotification() {

        // ✅ NotificationChannel 생성 (Android 8.0 이상 필수)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel", // ID는 NotificationCompat.Builder에 사용한 것과 일치해야 함
                "음악 재생",     // 사용자에게 보여질 채널 이름
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "음악 재생 상태를 표시하는 채널"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val mediaStyle = mediaLibrarySession?.let { MediaStyleNotificationHelper.MediaStyle(it) }

        val notification = NotificationCompat.Builder(this, "music_channel")
            .setSmallIcon(R.drawable.music)
            .setContentTitle("음악 재생 중")
            .setContentText("Media3 플레이어")
            .setStyle(mediaStyle)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        mediaLibrarySession?.player?.let { player ->
            if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
                stopSelf()
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