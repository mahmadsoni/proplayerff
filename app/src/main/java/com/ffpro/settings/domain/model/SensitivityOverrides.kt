package com.ffpro.settings.domain.model

/**
 * Manual fine-tuning on top of the calculated recommendation. Null means
 * "use the calculated value" — sensitivity that actually lands headshots
 * consistently comes from small personal adjustments plus practice, not
 * from a single formula, so this lets the player nudge each scope from
 * the calculated starting point and keep what works for their hand.
 */
data class SensitivityOverrides(
    val general: Int? = null,
    val redDot: Int? = null,
    val scope2x: Int? = null,
    val scope4x: Int? = null,
    val awmScope: Int? = null,
    val freeLook: Int? = null
) {
    val hasAny: Boolean
        get() = general != null || redDot != null || scope2x != null ||
            scope4x != null || awmScope != null || freeLook != null
}
