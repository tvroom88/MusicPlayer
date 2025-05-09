package com.aio.fe_music_player

import android.app.Application
import android.content.Context

class FeMpApplication : Application() {

    companion object {
        private lateinit var instance: FeMpApplication

        fun getInstance(): FeMpApplication = instance
        fun getAppContext(): Context = instance.applicationContext
    }
}