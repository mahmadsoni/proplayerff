package com.ffpro.settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.06f), Color.White.copy(alpha = 0.02f))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(cornerRadius))
            .padding(contentPadding)
    ) {
        content()
    }
}
