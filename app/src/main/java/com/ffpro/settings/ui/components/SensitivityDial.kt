package com.ffpro.settings.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Small circular dial showing a 0-100 sensitivity value. The composable
 * parameter is named `dialSize` (never `size`) to avoid shadowing the
 * DrawScope's own `size: Size` property inside the Canvas draw lambda.
 */
@Composable
fun SensitivityDial(
    value: Int,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    maxValue: Int = 200,
    dialSize: Dp = 78.dp,
    strokeWidth: Dp = 7.dp
) {
    val animated = remember { Animatable(0f) }
    LaunchedEffect(value) {
        animated.animateTo((value.toFloat() / maxValue).coerceIn(0f, 1f), animationSpec = tween(600))
    }

    androidx.compose.foundation.layout.Column(
        modifier = modifier.width(dialSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.width(dialSize).height(dialSize), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.width(dialSize).height(dialSize)) {
                val strokePx = strokeWidth.toPx()
                val diameter = this.size.minDimension - strokePx
                val topLeft = Offset(strokePx / 2, strokePx / 2)
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                val stroke = Stroke(width = strokePx, cap = StrokeCap.Round)

                drawArc(
                    color = accentColor.copy(alpha = 0.18f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = stroke,
                    size = arcSize,
                    topLeft = topLeft
                )
                drawArc(
                    color = accentColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animated.value,
                    useCenter = false,
                    style = stroke,
                    size = arcSize,
                    topLeft = topLeft
                )
            }
            Text("$value", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Text(
            label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
