package com.ffpro.settings

import android.app.Application
import com.ffpro.settings.core.AppContainer

class App : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer.getInstance(this)
    }
}
