package com.ffpro.settings.domain.model

enum class GraphicsQuality { LOW, MEDIUM, HIGH, ULTRA }
enum class Resolution { LOW, MEDIUM, HIGH, ULTRA_HD }

data class GraphicsSettings(
    val resolution: Resolution,
    val quality: GraphicsQuality,
    val targetFrameRate: Int,
    val shadowsEnabled: Boolean
)
