package com.ffpro.settings.domain.engine

import com.ffpro.settings.domain.model.DeviceProfile
import com.ffpro.settings.domain.model.DeviceTier
import com.ffpro.settings.domain.model.GraphicsQuality
import com.ffpro.settings.domain.model.GraphicsSettings
import com.ffpro.settings.domain.model.Resolution

/** Maps device tier + refresh rate to a graphics profile that keeps FPS stable. */
class GraphicsEngine {

    fun recommend(profile: DeviceProfile, antiLagMode: Boolean = false): GraphicsSettings {
        if (antiLagMode) {
            // Forced lowest-load profile: overrides the tier guess entirely,
            // for devices that still experience lag/heating despite being
            // classified as mid/high tier (thermal throttling, background
            // load, or an inaccurate tier guess all lead to the same fix).
            return GraphicsSettings(
                resolution = Resolution.LOW,
                quality = GraphicsQuality.LOW,
                targetFrameRate = 30,
                shadowsEnabled = false
            )
        }

        return when (profile.tier) {
            DeviceTier.LOW -> GraphicsSettings(
                resolution = Resolution.LOW,
                quality = GraphicsQuality.LOW,
                targetFrameRate = 30,
                shadowsEnabled = false
            )
            DeviceTier.MID -> GraphicsSettings(
                resolution = Resolution.MEDIUM,
                quality = GraphicsQuality.MEDIUM,
                targetFrameRate = 60,
                shadowsEnabled = false
            )
            DeviceTier.HIGH -> GraphicsSettings(
                resolution = Resolution.HIGH,
                quality = GraphicsQuality.HIGH,
                targetFrameRate = if (profile.refreshRateHz >= 90) 90 else 60,
                shadowsEnabled = true
            )
            DeviceTier.FLAGSHIP -> GraphicsSettings(
                resolution = Resolution.ULTRA_HD,
                quality = GraphicsQuality.ULTRA,
                targetFrameRate = profile.refreshRateHz.coerceAtMost(120),
                shadowsEnabled = true
            )
        }
    }
}
