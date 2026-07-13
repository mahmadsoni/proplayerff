package com.ffpro.settings.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ffpro.settings.i18n.Language
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.components.GlassCard
import com.ffpro.settings.ui.components.SelectableChip
import com.ffpro.settings.ui.theme.NeonGreen

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(strings.settingsTitle, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Language, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(strings.languageLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Language.entries.forEach { language ->
                    SelectableChip(
                        label = language.nativeName,
                        selected = uiState.language == language,
                        onClick = { viewModel.setLanguage(language) }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(strings.aboutTitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(10.dp))
            Text(strings.aboutBody, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
            Spacer(Modifier.height(14.dp))

            Text("Kotlin  ·  Jetpack Compose  ·  MVVM  ·  Coroutines & Flow  ·  DataStore  ·  Material 3",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
            Spacer(Modifier.height(14.dp))

            Text(strings.developedBy, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(2.dp))
            Text("Маҳмадсони", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
        }

        Spacer(Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Shield, contentDescription = null, tint = NeonGreen, modifier = Modifier.height(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(strings.permissionsTitle, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(6.dp))
            Text(strings.permissionsBody, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(16.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.height(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(strings.disclaimerTitle, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(6.dp))
            Text(strings.disclaimerBody, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
