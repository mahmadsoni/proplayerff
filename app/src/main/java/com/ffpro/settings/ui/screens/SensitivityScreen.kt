package com.ffpro.settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.components.GlassCard
import com.ffpro.settings.ui.components.SensitivityDial
import com.ffpro.settings.ui.components.StatRow
import com.ffpro.settings.ui.theme.NeonGreen
import com.ffpro.settings.ui.theme.NeonOrange

@Composable
fun SensitivityScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val sens = uiState.sensitivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(strings.sensitivityTitle, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
                Text(strings.sensitivitySubtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (uiState.overrides.hasAny) {
                IconButton(onClick = { viewModel.resetOverrides() }) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = strings.resetToRecommended, tint = NeonOrange)
                }
            }
        }
        Spacer(Modifier.height(20.dp))

        if (sens == null) {
            Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(strings.fineTuneHint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.height(16.dp))

            AdjustableRow(strings.sensGeneral, sens.general, NeonGreen, onMinus = { viewModel.nudgeGeneral(-1) }, onPlus = { viewModel.nudgeGeneral(1) })
            Spacer(Modifier.height(12.dp))
            AdjustableRow(strings.sensRedDot, sens.redDot, NeonGreen, onMinus = { viewModel.nudgeRedDot(-1) }, onPlus = { viewModel.nudgeRedDot(1) })
            Spacer(Modifier.height(12.dp))
            AdjustableRow(strings.sens2x, sens.scope2x, NeonOrange, onMinus = { viewModel.nudge2x(-1) }, onPlus = { viewModel.nudge2x(1) })
            Spacer(Modifier.height(12.dp))
            AdjustableRow(strings.sens4x, sens.scope4x, NeonOrange, onMinus = { viewModel.nudge4x(-1) }, onPlus = { viewModel.nudge4x(1) })
            Spacer(Modifier.height(12.dp))
            AdjustableRow(strings.sensAwm, sens.awmScope, NeonOrange, onMinus = { viewModel.nudgeAwm(-1) }, onPlus = { viewModel.nudgeAwm(1) })
            Spacer(Modifier.height(12.dp))
            AdjustableRow(strings.sensFreeLook, sens.freeLook, NeonGreen, onMinus = { viewModel.nudgeFreeLook(-1) }, onPlus = { viewModel.nudgeFreeLook(1) })

            Spacer(Modifier.height(16.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                StatRow(strings.dpiLabel, "${sens.recommendedDpi}")
                Spacer(Modifier.height(10.dp))
                Text(strings.sensitivityNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AdjustableRow(
    label: String,
    value: Int,
    accentColor: Color,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 16.dp, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SensitivityDial(value = value, label = label, accentColor = accentColor, dialSize = 56.dp, strokeWidth = 5.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledIconButton(onClick = onMinus, modifier = Modifier.height(40.dp).width(40.dp)) {
                    Icon(Icons.Filled.Remove, contentDescription = "-")
                }
                Spacer(Modifier.width(14.dp))
                FilledIconButton(onClick = onPlus, modifier = Modifier.height(40.dp).width(40.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = "+")
                }
            }
        }
    }
}
