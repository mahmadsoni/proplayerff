package com.ffpro.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ffpro.settings.core.AppContainer
import com.ffpro.settings.domain.model.DeviceProfile
import com.ffpro.settings.domain.model.GraphicsSettings
import com.ffpro.settings.domain.model.HudRecommendation
import com.ffpro.settings.domain.model.PlayStyle
import com.ffpro.settings.domain.model.SensitivityOverrides
import com.ffpro.settings.domain.model.SensitivitySettings
import com.ffpro.settings.i18n.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val deviceProfile: DeviceProfile? = null,
    val playStyle: PlayStyle = PlayStyle.BALANCED,
    val language: Language = Language.ENGLISH,
    val antiLagMode: Boolean = false,
    val baseSensitivity: SensitivitySettings? = null,
    val overrides: SensitivityOverrides = SensitivityOverrides(),
    val sensitivity: SensitivitySettings? = null,
    val graphics: GraphicsSettings? = null,
    val hud: HudRecommendation? = null,
    val isScanning: Boolean = true
)

class MainViewModel(private val container: AppContainer) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            container.preferencesStore.languageFlow.collect { language ->
                _uiState.value = _uiState.value.copy(language = language)
            }
        }
        viewModelScope.launch {
            container.preferencesStore.playStyleFlow.collect { playStyle ->
                _uiState.value = _uiState.value.copy(playStyle = playStyle)
                recomputeRecommendations()
            }
        }
        viewModelScope.launch {
            container.preferencesStore.antiLagFlow.collect { antiLag ->
                _uiState.value = _uiState.value.copy(antiLagMode = antiLag)
                recomputeRecommendations()
            }
        }
        viewModelScope.launch {
            container.preferencesStore.sensitivityOverridesFlow.collect { overrides ->
                _uiState.value = _uiState.value.copy(overrides = overrides)
                applyOverrides()
            }
        }
        rescan()
    }

    fun rescan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            val profile = container.deviceScanner.scan()
            _uiState.value = _uiState.value.copy(deviceProfile = profile, isScanning = false)
            recomputeRecommendations()
        }
    }

    fun setPlayStyle(playStyle: PlayStyle) {
        viewModelScope.launch {
            container.preferencesStore.setPlayStyle(playStyle)
        }
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            container.preferencesStore.setLanguage(language)
        }
    }

    fun setAntiLagMode(enabled: Boolean) {
        viewModelScope.launch {
            container.preferencesStore.setAntiLag(enabled)
        }
    }

    fun nudgeGeneral(delta: Int) = nudge(delta, current = { it.general }) { container.preferencesStore.setOverrideGeneral(it) }
    fun nudgeRedDot(delta: Int) = nudge(delta, current = { it.redDot }) { container.preferencesStore.setOverrideRedDot(it) }
    fun nudge2x(delta: Int) = nudge(delta, current = { it.scope2x }) { container.preferencesStore.setOverride2x(it) }
    fun nudge4x(delta: Int) = nudge(delta, current = { it.scope4x }) { container.preferencesStore.setOverride4x(it) }
    fun nudgeAwm(delta: Int) = nudge(delta, current = { it.awmScope }) { container.preferencesStore.setOverrideAwm(it) }
    fun nudgeFreeLook(delta: Int) = nudge(delta, current = { it.freeLook }) { container.preferencesStore.setOverrideFreeLook(it) }

    fun resetOverrides() {
        viewModelScope.launch {
            container.preferencesStore.resetOverrides()
        }
    }

    private fun nudge(
        delta: Int,
        current: (SensitivitySettings) -> Int,
        persist: suspend (Int) -> Unit
    ) {
        val sens = _uiState.value.sensitivity ?: return
        val newValue = (current(sens) + delta).coerceIn(1, 200)
        viewModelScope.launch { persist(newValue) }
    }

    private fun recomputeRecommendations() {
        val profile = _uiState.value.deviceProfile ?: return
        val playStyle = _uiState.value.playStyle
        val antiLag = _uiState.value.antiLagMode

        val baseSensitivity = container.sensitivityEngine.recommend(profile, playStyle)
        val graphics = container.graphicsEngine.recommend(profile, antiLag)
        val hud = container.hudAdvisor.recommend(profile, playStyle)

        _uiState.value = _uiState.value.copy(
            baseSensitivity = baseSensitivity,
            graphics = graphics,
            hud = hud
        )
        applyOverrides()
    }

    private fun applyOverrides() {
        val base = _uiState.value.baseSensitivity ?: return
        val overrides = _uiState.value.overrides

        val finalSensitivity = base.copy(
            general = overrides.general ?: base.general,
            redDot = overrides.redDot ?: base.redDot,
            scope2x = overrides.scope2x ?: base.scope2x,
            scope4x = overrides.scope4x ?: base.scope4x,
            awmScope = overrides.awmScope ?: base.awmScope,
            freeLook = overrides.freeLook ?: base.freeLook
        )

        _uiState.value = _uiState.value.copy(sensitivity = finalSensitivity)
    }
}

class MainViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(container) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
