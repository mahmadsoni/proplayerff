package com.ffpro.settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ffpro.settings.domain.model.DeviceTier
import com.ffpro.settings.domain.model.PlayStyle
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.components.GlassCard
import com.ffpro.settings.ui.components.SelectableChip
import com.ffpro.settings.ui.components.StatRow
import com.ffpro.settings.ui.components.TierBadge
import com.ffpro.settings.ui.theme.AlertRed
import com.ffpro.settings.ui.theme.NeonGreen
import com.ffpro.settings.ui.theme.NeonOrange
import com.ffpro.settings.ui.theme.WarningAmber

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val profile = uiState.deviceProfile

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(strings.appName, style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onBackground)
            Text(strings.appTagline, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Smartphone, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(strings.deviceScanTitle, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = { viewModel.rescan() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = strings.rescanButton, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(strings.deviceScanSubtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                if (uiState.isScanning || profile == null) {
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                } else {
                    Spacer(Modifier.height(14.dp))
                    val (tierLabel, tierColor, tierDesc) = tierInfo(profile.tier, strings)
                    TierBadge(label = "${strings.tierLabel}: $tierLabel", color = tierColor)
                    Spacer(Modifier.height(8.dp))
                    Text(tierDesc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                    Spacer(Modifier.height(8.dp))

                    val ramGb = profile.totalRamBytes / (1024.0 * 1024.0 * 1024.0)
                    StatRow(strings.deviceModelLabel, "${profile.manufacturer} ${profile.model}".trim())
                    StatRow(strings.ramLabel, "%.1f GB".format(ramGb))
                    StatRow(strings.cpuCoresLabel, "${profile.cpuCoreCount}")
                    StatRow(strings.androidVersionLabel, "Android ${profile.androidVersionName}")
                    StatRow(strings.screenLabel, "${profile.screenWidthPx} × ${profile.screenHeightPx}")
                    StatRow(strings.refreshRateLabel, "${profile.refreshRateHz} Hz")
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(strings.playstyleTitle, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SelectableChip(
                        label = strings.playstyleRush,
                        selected = uiState.playStyle == PlayStyle.RUSH,
                        onClick = { viewModel.setPlayStyle(PlayStyle.RUSH) }
                    )
                    SelectableChip(
                        label = strings.playstyleBalanced,
                        selected = uiState.playStyle == PlayStyle.BALANCED,
                        onClick = { viewModel.setPlayStyle(PlayStyle.BALANCED) }
                    )
                    SelectableChip(
                        label = strings.playstyleSniper,
                        selected = uiState.playStyle == PlayStyle.SNIPER,
                        onClick = { viewModel.setPlayStyle(PlayStyle.SNIPER) }
                    )
                }
                Spacer(Modifier.height(10.dp))
                val desc = when (uiState.playStyle) {
                    PlayStyle.RUSH -> strings.playstyleRushDesc
                    PlayStyle.BALANCED -> strings.playstyleBalancedDesc
                    PlayStyle.SNIPER -> strings.playstyleSniperDesc
                }
                Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

private fun tierInfo(
    tier: DeviceTier,
    strings: com.ffpro.settings.i18n.Strings
): Triple<String, Color, String> {
    return when (tier) {
        DeviceTier.LOW -> Triple(strings.tierLow, AlertRed, strings.tierLowDesc)
        DeviceTier.MID -> Triple(strings.tierMid, WarningAmber, strings.tierMidDesc)
        DeviceTier.HIGH -> Triple(strings.tierHigh, NeonGreen, strings.tierHighDesc)
        DeviceTier.FLAGSHIP -> Triple(strings.tierFlagship, NeonOrange, strings.tierFlagshipDesc)
    }
}
