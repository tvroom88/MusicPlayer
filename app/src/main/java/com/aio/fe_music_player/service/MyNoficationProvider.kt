package com.aio.fe_music_player.service

import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.aio.fe_music_player.R
import com.google.common.collect.ImmutableList

@UnstableApi
class CustomMediaNotificationProvider(context: Context) :
    DefaultMediaNotificationProvider(context) {

    override fun addNotificationActions(
        mediaSession: MediaSession,
        mediaButtons: ImmutableList<CommandButton>,
        builder: NotificationCompat.Builder,
        actionFactory: MediaNotification.ActionFactory
    ): IntArray {
        /* Retrieving notification default play/pause button from mediaButtons list. */
        val defaultPlayPauseCommandButton = mediaButtons.getOrNull(0)
        val notificationMediaButtons = if (defaultPlayPauseCommandButton != null) {
            /* Overriding received mediaButtons list to ensure required buttons order: [rewind15, play/pause, forward15]. */
            ImmutableList.builder<CommandButton>().apply {
                add(NotificationPlayerCustomCommandButton.REWIND)
                add(defaultPlayPauseCommandButton)
                add(NotificationPlayerCustomCommandButton.FORWARD)
            }.build()
        } else {
            /* Fallback option to handle nullability, in case retrieving default play/pause button fails for some reason (should never happen). */
            mediaButtons
        }
        return super.addNotificationActions(
            mediaSession,
            notificationMediaButtons,
            builder,
            actionFactory
        )
    }
}

object NotificationPlayerCustomCommandButton {

    // 고유 커맨드 정의
    val REWIND_COMMAND = SessionCommand("CUSTOM_REWIND_15", Bundle.EMPTY)
    val FORWARD_COMMAND = SessionCommand("CUSTOM_FORWARD_15", Bundle.EMPTY)

    // CommandButton 정의
    val REWIND: CommandButton = CommandButton.Builder()
        .setSessionCommand(REWIND_COMMAND)
        .setIconResId(R.drawable.music) // 리와인드 아이콘
        .setDisplayName("10초 되감기")
        .setEnabled(true)
        .build()

    val FORWARD: CommandButton = CommandButton.Builder()
        .setSessionCommand(FORWARD_COMMAND)
        .setIconResId(R.drawable.folder) // 포워드 아이콘
        .setDisplayName("10초 빨리감기")
        .setEnabled(true)
        .build()
}