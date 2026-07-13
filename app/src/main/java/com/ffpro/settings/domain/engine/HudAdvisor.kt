package com.ffpro.settings.domain.engine

import com.ffpro.settings.domain.model.DeviceProfile
import com.ffpro.settings.domain.model.DeviceTier
import com.ffpro.settings.domain.model.HudRecommendation
import com.ffpro.settings.domain.model.PlayStyle

/** Suggests HUD/control layout adjustments based on screen size, tier, and playstyle. */
class HudAdvisor {

    fun recommend(profile: DeviceProfile, playStyle: PlayStyle): HudRecommendation {
        val screenInches = approximateScreenInches(profile)

        val fireButtonSize = when {
            screenInches < 5.5 -> 115
            screenInches < 6.3 -> 105
            else -> 95
        }

        val gyroscope = playStyle == PlayStyle.SNIPER || playStyle == PlayStyle.BALANCED

        val clawRecommended = profile.tier == DeviceTier.HIGH || profile.tier == DeviceTier.FLAGSHIP

        return HudRecommendation(
            fireButtonSizePercent = fireButtonSize,
            gyroscopeRecommended = gyroscope,
            quickReloadRecommended = true,
            clawLayoutRecommended = clawRecommended
        )
    }

    private fun approximateScreenInches(profile: DeviceProfile): Double {
        // Rough diagonal estimate assuming ~400-450 dpi typical phone density band;
        // used only to bucket "small / normal / large" screens, not for precision.
        val diagonalPx = kotlin.math.sqrt(
            (profile.screenWidthPx.toDouble() * profile.screenWidthPx) +
                (profile.screenHeightPx.toDouble() * profile.screenHeightPx)
        )
        return diagonalPx / 420.0
    }
}
