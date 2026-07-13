package com.ffpro.settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ffpro.settings.domain.model.GraphicsQuality
import com.ffpro.settings.domain.model.Resolution
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.i18n.Strings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.components.GlassCard
import com.ffpro.settings.ui.components.StatRow
import com.ffpro.settings.ui.theme.NeonGreen

@Composable
fun GraphicsScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val graphics = uiState.graphics

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(strings.graphicsTitle, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))
        Text(strings.graphicsSubtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = com.ffpro.settings.ui.theme.NeonOrange, modifier = Modifier.height(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.antiLagTitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Switch(
                    checked = uiState.antiLagMode,
                    onCheckedChange = { viewModel.setAntiLagMode(it) }
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(strings.antiLagDesc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(16.dp))

        if (graphics == null) {
            Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.HighQuality, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.resolutionLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(Modifier.height(6.dp))
                Text(resolutionLabel(graphics.resolution), style = MaterialTheme.typography.bodyLarge, color = NeonGreen)
            }

            Spacer(Modifier.height(14.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.graphicsQualityLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(Modifier.height(6.dp))
                Text(qualityLabel(graphics.quality, strings), style = MaterialTheme.typography.bodyLarge, color = NeonGreen)
            }

            Spacer(Modifier.height(14.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Speed, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.frameRateLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(Modifier.height(6.dp))
                Text("${graphics.targetFrameRate} FPS", style = MaterialTheme.typography.bodyLarge, color = NeonGreen)
            }

            Spacer(Modifier.height(14.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                StatRow(strings.shadowsLabel, if (graphics.shadowsEnabled) strings.on else strings.off)
            }
        }
    }
}

private fun resolutionLabel(resolution: Resolution): String = when (resolution) {
    Resolution.LOW -> "720p"
    Resolution.MEDIUM -> "900p"
    Resolution.HIGH -> "1080p"
    Resolution.ULTRA_HD -> "1080p+ (native)"
}

private fun qualityLabel(quality: GraphicsQuality, strings: Strings): String = when (quality) {
    GraphicsQuality.LOW -> strings.qualityLow
    GraphicsQuality.MEDIUM -> strings.qualityMid
    GraphicsQuality.HIGH -> strings.qualityHigh
    GraphicsQuality.ULTRA -> strings.qualityUltra
}
