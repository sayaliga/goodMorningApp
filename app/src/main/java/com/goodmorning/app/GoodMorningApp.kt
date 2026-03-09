package com.goodmorning.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoodMorningApp : Application() {

    override fun onCreate() {
        super.onCreate()
        StartupConsole.log("Application created")
    }
}
