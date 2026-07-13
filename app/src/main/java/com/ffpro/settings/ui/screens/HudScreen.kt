package com.ffpro.settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.components.GlassCard
import com.ffpro.settings.ui.theme.NeonGreen
import com.ffpro.settings.ui.theme.NeonOrange

@Composable
fun HudScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val hud = uiState.hud

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(strings.hudTitle, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))
        Text(strings.hudSubtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))

        if (hud == null) {
            Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            HudTipCard(
                icon = Icons.Filled.TouchApp,
                title = strings.fireButtonSize,
                value = "${hud.fireButtonSizePercent}%",
                description = strings.fireButtonSizeDesc
            )
            Spacer(Modifier.height(14.dp))
            HudTipCard(
                icon = Icons.Filled.Gesture,
                title = strings.gyroscope,
                value = if (hud.gyroscopeRecommended) strings.on else strings.off,
                description = strings.gyroscopeDesc
            )
            Spacer(Modifier.height(14.dp))
            HudTipCard(
                icon = Icons.Filled.Replay,
                title = strings.quickReload,
                value = if (hud.quickReloadRecommended) strings.on else strings.off,
                description = strings.quickReloadDesc
            )
            Spacer(Modifier.height(14.dp))
            HudTipCard(
                icon = Icons.Filled.RadioButtonChecked,
                title = strings.twoFingerClaw,
                value = if (hud.clawLayoutRecommended) strings.on else strings.off,
                description = strings.twoFingerClawDesc
            )
        }
    }
}

@Composable
private fun HudTipCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    description: String
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = NeonOrange, modifier = Modifier.height(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Text(value, style = MaterialTheme.typography.titleMedium, color = NeonOrange)
        }
        Spacer(Modifier.height(6.dp))
        Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
