package com.ffpro.settings.core

import android.content.Context
import com.ffpro.settings.data.DeviceScanner
import com.ffpro.settings.data.PreferencesStore
import com.ffpro.settings.domain.engine.DeviceTierClassifier
import com.ffpro.settings.domain.engine.GraphicsEngine
import com.ffpro.settings.domain.engine.HudAdvisor
import com.ffpro.settings.domain.engine.SensitivityEngine

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val deviceScanner = DeviceScanner(appContext)
    val preferencesStore = PreferencesStore(appContext)

    val deviceTierClassifier = DeviceTierClassifier()
    val sensitivityEngine = SensitivityEngine()
    val graphicsEngine = GraphicsEngine()
    val hudAdvisor = HudAdvisor()

    companion object {
        @Volatile
        private var INSTANCE: AppContainer? = null

        fun getInstance(context: Context): AppContainer =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppContainer(context).also { INSTANCE = it }
            }
    }
}
