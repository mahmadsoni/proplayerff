package com.ffpro.settings.domain.engine

import com.ffpro.settings.domain.model.DeviceProfile
import com.ffpro.settings.domain.model.DeviceTier

/**
 * Heuristic device tier classifier. There is no single official "gaming
 * tier" API on Android, so this combines RAM, CPU core count, Android
 * version, and the OS's own isLowRamDevice signal into one composite score.
 */
class DeviceTierClassifier {

    fun classify(profile: DeviceProfile): DeviceTier {
        if (profile.isLowRamDevice) return DeviceTier.LOW

        val ramGb = profile.totalRamBytes / (1024.0 * 1024.0 * 1024.0)

        val ramScore = when {
            ramGb >= 8.0 -> 40
            ramGb >= 6.0 -> 30
            ramGb >= 4.0 -> 20
            ramGb >= 3.0 -> 10
            else -> 0
        }

        val coreScore = when {
            profile.cpuCoreCount >= 8 -> 30
            profile.cpuCoreCount >= 6 -> 20
            profile.cpuCoreCount >= 4 -> 10
            else -> 0
        }

        val sdkScore = when {
            profile.androidSdkInt >= 33 -> 20
            profile.androidSdkInt >= 30 -> 14
            profile.androidSdkInt >= 27 -> 8
            else -> 0
        }

        val refreshScore = when {
            profile.refreshRateHz >= 120 -> 10
            profile.refreshRateHz >= 90 -> 6
            else -> 0
        }

        val total = ramScore + coreScore + sdkScore + refreshScore

        return when {
            total >= 80 -> DeviceTier.FLAGSHIP
            total >= 55 -> DeviceTier.HIGH
            total >= 30 -> DeviceTier.MID
            else -> DeviceTier.LOW
        }
    }
}
