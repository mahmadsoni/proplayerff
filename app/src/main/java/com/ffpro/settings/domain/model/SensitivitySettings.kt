package com.ffpro.settings.domain.model

data class SensitivitySettings(
    val general: Int,
    val redDot: Int,
    val scope2x: Int,
    val scope4x: Int,
    val awmScope: Int,
    val freeLook: Int,
    val recommendedDpi: Int
)
