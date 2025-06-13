package com.aio.fe_music_player.service

import android.media.AudioManager
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

/**
 * AudioFocusListener
 *
 * Android 오디오 포커스(Audio Focus) 변화를 감지하고,
 * 이에 따라 ExoPlayer의 재생 상태와 볼륨을 제어하는 역할을 합니다.
 *
 * @param player ExoPlayer 인스턴스. 포커스 변화에 따라 제어됩니다.
 */
class AudioFocusListener(
    private val player: ExoPlayer
) : AudioManager.OnAudioFocusChangeListener {

    /**
     * 현재 오디오 포커스를 보유하고 있는지 여부를 저장합니다.
     * 이 값을 통해 앱이 현재 오디오를 재생해도 되는 상태인지 알 수 있습니다.
     */
    var hasAudioFocus = false

    /**
     * 오디오 포커스 상태가 변할 때 시스템이 호출하는 콜백 메서드입니다.
     * focusChange 값에 따라 플레이어의 재생 상태나 볼륨을 적절히 변경합니다.
     *
     * @param focusChange 시스템에서 전달하는 오디오 포커스 상태 코드
     */
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // 오디오 포커스를 다시 얻었을 때 호출됨
                // - 일시정지된 재생을 재개하고,
                // - 볼륨을 원래 상태로 복구합니다.
                hasAudioFocus = true
                player.volume = 1.0f
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // 오디오 포커스를 완전히 잃었을 때 호출됨
                // - 다른 앱이 오디오를 장기간 사용하는 경우로,
                // - 즉시 재생을 중지합니다.
                hasAudioFocus = false
                player.playWhenReady = false
                Log.d("AudioFocusListener", "AUDIOFOCUS_LOSS")

            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // 일시적으로 오디오 포커스를 잃었을 때 호출됨
                // - 잠시 다른 앱이 오디오를 사용하므로,
                // - 재생을 일시정지합니다.
                hasAudioFocus = false
                player.playWhenReady = false
                Log.d("AudioFocusListener", "AUDIOFOCUS_LOSS_TRANSIENT")

            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                player.volume = 0.2f
                Log.d("AudioFocusListener", "AUDIOFOCUS_GAIN")

            }
        }
    }
}