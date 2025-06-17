package com.aio.fe_music_player.service

import android.os.Bundle
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM
import androidx.media3.common.Player.COMMAND_SEEK_TO_PREVIOUS
import androidx.media3.common.Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.aio.fe_music_player.R
import com.google.common.collect.ImmutableList


@UnstableApi
class MusicPlayerSessionCallback : MediaLibraryService.MediaLibrarySession.Callback {

    private val customCommandSeekBackward = SessionCommand("CUSTOM_SEEK_BACKWARD", Bundle.EMPTY)
    private val customCommandSeekForward = SessionCommand("CUSTOM_SEEK_FORWARD", Bundle.EMPTY)

    private val sessionCommands =
        MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
            .add(customCommandSeekBackward)
            .add(customCommandSeekForward)
            .add(customCommandSeekForward)
            .build()

    private fun createSeekBackwardButton(command: SessionCommand): CommandButton {
        return CommandButton.Builder()
            .setSessionCommand(command)
            .setIconResId(R.drawable.folder) // 아이콘 리소스는 프로젝트에 있는 걸로 교체
            .setDisplayName("뒤로 10초")
            .build()
    }

    private fun createSeekForwardButton(command: SessionCommand): CommandButton {
        return CommandButton.Builder()
            .setSessionCommand(command)
            .setIconResId(R.drawable.music) // 아이콘 리소스는 프로젝트에 있는 걸로 교체
            .setDisplayName("앞으로 10초")
            .build()
    }

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        // Default commands with default button preferences for
        val sessionCommands =
            MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
                // 여기에 세션 관련 명령 추가 가능
                .build()

        val playerCommands = MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
            .add(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
            .add(Player.COMMAND_PLAY_PAUSE)
            .add(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
            .build()

        val buttonList = ImmutableList.of(
            CommandButton.Builder()
                .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                .setIconResId(R.drawable.folder)
                .setDisplayName("이전곡")
                .build(),
            CommandButton.Builder()
                .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
                .setIconResId(R.drawable.music)
                .setDisplayName("재생/일시정지")
                .build(),
            CommandButton.Builder()
                .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                .setIconResId(R.drawable.ic_launcher_background)
                .setDisplayName("다음곡")
                .build()
        )

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
            .setAvailableSessionCommands(sessionCommands)
            .setAvailablePlayerCommands(playerCommands)
            .setMediaButtonPreferences(buttonList)
            .build()

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()


//        if (session.isMediaNotificationController(controller)) {
//            val sessionCommands =
//                MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
//                    .add(customCommandSeekBackward)
//                    .add(customCommandSeekForward)
//                    .build()
//            val playerCommands =
//                MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
//                    .remove(COMMAND_SEEK_TO_PREVIOUS)
//                    .remove(COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
//                    .remove(COMMAND_SEEK_TO_NEXT)
//                    .remove(COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
//                    .build()
//            // Custom button preferences and commands to configure the platform session.
//            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
//                .setMediaButtonPreferences(
//                    ImmutableList.of(
//                        createSeekBackwardButton(customCommandSeekBackward),
//                        createSeekForwardButton(customCommandSeekForward)
//                    )
//                )
//                .setAvailablePlayerCommands(playerCommands)
//                .setAvailableSessionCommands(sessionCommands)
//                .build()
//        } else if(session.isAutoCompanionController(controller)){
//            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
//                .setAvailableSessionCommands(sessionCommands)
//                .build()
//        } else {
//            // Default commands with default button preferences for all other controllers.
//            return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
//        }
    }
        // Default commands with default button preferences for
//        val sessionCommands =
//            MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
//                // 여기에 세션 관련 명령 추가 가능
//                .build()
//
//        val playerCommands = MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
//            .add(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
//            .add(Player.COMMAND_PLAY_PAUSE)
//            .add(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
//            .build()
//
//        val buttonList = ImmutableList.of(
//            CommandButton.Builder()
//                .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
//                .setIconResId(R.drawable.folder)
//                .setDisplayName("이전곡")
//                .build(),
//            CommandButton.Builder()
//                .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
//                .setIconResId(R.drawable.music)
//                .setDisplayName("재생/일시정지")
//                .build(),
//            CommandButton.Builder()
//                .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
//                .setIconResId(R.drawable.ic_launcher_background)
//                .setDisplayName("다음곡")
//                .build()
//        )
//
//        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
//            .setAvailableSessionCommands(sessionCommands)
//            .setAvailablePlayerCommands(playerCommands)
//            .setMediaButtonPreferences(buttonList)
//            .build()

//        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()


}