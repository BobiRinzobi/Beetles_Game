package com.phoenix.beetles.feature.core.infrastructure

import android.content.Context
import android.media.MediaPlayer
import com.phoenix.beetles.R
import com.phoenix.beetles.feature.core.domain.port.SfxPlayer

class AndroidSfxPlayer(
    private val context: Context,
) : SfxPlayer {

    private var screamPlayer: MediaPlayer? = null

    override fun startBugScream() {
        val current = screamPlayer
        if (current?.isPlaying == true) return
        stopBugScream()
        screamPlayer = MediaPlayer.create(context, R.raw.beetle_scream)?.apply {
            isLooping = true
            start()
        }
    }

    override fun stopBugScream() {
        screamPlayer?.run {
            runCatching {
                if (isPlaying) stop()
            }
            release()
        }
        screamPlayer = null
    }
}
