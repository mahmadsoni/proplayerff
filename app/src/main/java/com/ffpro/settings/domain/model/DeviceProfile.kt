package com.ffpro.settings.domain.model

enum class DeviceTier { LOW, MID, HIGH, FLAGSHIP }

data class DeviceProfile(
    val manufacturer: String,
    val model: String,
    val totalRamBytes: Long,
    val cpuCoreCount: Int,
    val androidSdkInt: Int,
    val androidVersionName: String,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val screenDpi: Int,
    val refreshRateHz: Int,
    val isLowRamDevice: Boolean,
    val tier: DeviceTier
)
