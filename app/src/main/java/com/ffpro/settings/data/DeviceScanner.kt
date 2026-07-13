package com.ffpro.settings.data

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.ffpro.settings.domain.engine.DeviceTierClassifier
import com.ffpro.settings.domain.model.DeviceProfile

/**
 * Reads real, public Android APIs to build a DeviceProfile. No permissions
 * required — everything here is standard, unprivileged system information.
 */
class DeviceScanner(private val context: Context) {

    fun scan(): DeviceProfile {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)

        val cpuCores = Runtime.getRuntime().availableProcessors()

        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getRealMetrics(metrics)

        @Suppress("DEPRECATION")
        val refreshRate = windowManager.defaultDisplay.refreshRate.toInt().coerceAtLeast(60)

        // Real physical DPI reported by the display driver for THIS exact
        // panel — this is what actually determines how many pixels your
        // thumb moves across per unit of finger travel, so it's what the
        // sensitivity recommendation should be built from, not a tier guess.
        val realDpi = ((metrics.xdpi + metrics.ydpi) / 2f).toInt().coerceIn(120, 700)

        val partial = DeviceProfile(
            manufacturer = Build.MANUFACTURER?.replaceFirstChar { it.uppercase() } ?: "",
            model = Build.MODEL ?: "",
            totalRamBytes = memInfo.totalMem,
            cpuCoreCount = cpuCores,
            androidSdkInt = Build.VERSION.SDK_INT,
            androidVersionName = Build.VERSION.RELEASE ?: "?",
            screenWidthPx = metrics.widthPixels,
            screenHeightPx = metrics.heightPixels,
            screenDpi = realDpi,
            refreshRateHz = refreshRate,
            isLowRamDevice = activityManager.isLowRamDevice,
            tier = com.ffpro.settings.domain.model.DeviceTier.MID // placeholder, replaced below
        )

        val tier = DeviceTierClassifier().classify(partial)
        return partial.copy(tier = tier)
    }
}
