package com.ffpro.settings.domain.model

data class HudRecommendation(
    val fireButtonSizePercent: Int, // relative scale, 100 = default
    val gyroscopeRecommended: Boolean,
    val quickReloadRecommended: Boolean,
    val clawLayoutRecommended: Boolean
)
