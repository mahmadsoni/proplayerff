package com.ffpro.settings.domain.engine

import com.ffpro.settings.domain.model.DeviceProfile
import com.ffpro.settings.domain.model.DeviceTier
import com.ffpro.settings.domain.model.PlayStyle
import com.ffpro.settings.domain.model.SensitivitySettings
import kotlin.math.roundToInt

/**
 * Produces sensitivity recommendations on Free Fire's current 1-200
 * sensitivity scale, tied directly to THIS device's actual physical
 * screen DPI (read from the display driver, not guessed from a tier
 * table) so two different phones in the same performance tier — which
 * can still have very different pixel density — get different, correct
 * numbers instead of an identical generic answer.
 *
 * Reasoning: the same finger swipe travels more physical pixels on a
 * higher-DPI screen. To keep the actual turning speed (cm-per-360)
 * consistent with what the baseline was tuned for, sensitivity is scaled
 * down slightly as real DPI rises above the reference point, and scaled
 * up slightly as it falls below it.
 */
class SensitivityEngine {

    companion object {
        private const val REFERENCE_DPI = 400.0
        // How much to adjust sensitivity (in scale points) per 100 DPI of
        // difference from the reference point. Small and capped, since DPI
        // is a secondary correction on top of tier/playstyle, not the main driver.
        private const val DPI_ADJUSTMENT_PER_100 = 6.0
    }

    fun recommend(profile: DeviceProfile, playStyle: PlayStyle): SensitivitySettings {
        // Baseline "balanced" values on the 1-200 scale.
        var general = 185.0
        var redDot = 175.0
        var scope2x = 125.0
        var scope4x = 85.0
        var awmScope = 58.0
        var freeLook = 185.0

        when (playStyle) {
            PlayStyle.RUSH -> {
                general += 10
                redDot += 12
                scope2x -= 8
                scope4x -= 6
                awmScope -= 4
                freeLook += 6
            }
            PlayStyle.SNIPER -> {
                general -= 10
                redDot -= 6
                scope2x += 12
                scope4x += 10
                awmScope += 8
                freeLook -= 4
            }
            PlayStyle.BALANCED -> { /* keep baseline */ }
        }

        val tierBonus = when (profile.tier) {
            DeviceTier.FLAGSHIP -> 8.0
            DeviceTier.HIGH -> 4.0
            DeviceTier.MID -> 0.0
            DeviceTier.LOW -> -8.0
        }

        general += tierBonus
        redDot += tierBonus
        scope2x += tierBonus / 2
        scope4x += tierBonus / 2
        awmScope += tierBonus / 2
        freeLook += tierBonus

        // Device-specific DPI correction: real screen density vs. the
        // reference point, clamped so one unusual panel can't push the
        // recommendation to an extreme.
        val dpiDelta = (REFERENCE_DPI - profile.screenDpi) / 100.0
        val dpiBonus = (dpiDelta * DPI_ADJUSTMENT_PER_100).coerceIn(-18.0, 18.0)

        general += dpiBonus
        redDot += dpiBonus
        scope2x += dpiBonus * 0.6
        scope4x += dpiBonus * 0.6
        awmScope += dpiBonus * 0.5
        freeLook += dpiBonus

        return SensitivitySettings(
            general = general.coerceIn(1.0, 200.0).roundToInt(),
            redDot = redDot.coerceIn(1.0, 200.0).roundToInt(),
            scope2x = scope2x.coerceIn(1.0, 200.0).roundToInt(),
            scope4x = scope4x.coerceIn(1.0, 200.0).roundToInt(),
            awmScope = awmScope.coerceIn(1.0, 200.0).roundToInt(),
            freeLook = freeLook.coerceIn(1.0, 200.0).roundToInt(),
            // The recommended in-game DPI IS this device's real measured
            // screen DPI — entering this exact number in Free Fire's DPI
            // field makes the game's own touch-sampling match the panel
            // it's actually running on.
            recommendedDpi = profile.screenDpi
        )
    }
}
