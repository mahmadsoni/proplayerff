package com.ffpro.settings.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ffpro.settings.core.Constants
import com.ffpro.settings.domain.model.PlayStyle
import com.ffpro.settings.domain.model.SensitivityOverrides
import com.ffpro.settings.i18n.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = Constants.DATASTORE_NAME)

class PreferencesStore(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val PLAYSTYLE = stringPreferencesKey("playstyle")
        val ANTI_LAG = booleanPreferencesKey("anti_lag_mode")
        val OVERRIDE_GENERAL = intPreferencesKey("override_general")
        val OVERRIDE_RED_DOT = intPreferencesKey("override_red_dot")
        val OVERRIDE_2X = intPreferencesKey("override_2x")
        val OVERRIDE_4X = intPreferencesKey("override_4x")
        val OVERRIDE_AWM = intPreferencesKey("override_awm")
        val OVERRIDE_FREE_LOOK = intPreferencesKey("override_free_look")
    }

    val languageFlow: Flow<Language> = context.dataStore.data.map { prefs ->
        Language.fromCode(prefs[Keys.LANGUAGE] ?: Language.ENGLISH.code)
    }

    val playStyleFlow: Flow<PlayStyle> = context.dataStore.data.map { prefs ->
        runCatching { PlayStyle.valueOf(prefs[Keys.PLAYSTYLE] ?: PlayStyle.BALANCED.name) }
            .getOrDefault(PlayStyle.BALANCED)
    }

    val antiLagFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ANTI_LAG] ?: false
    }

    val sensitivityOverridesFlow: Flow<SensitivityOverrides> = context.dataStore.data.map { prefs ->
        SensitivityOverrides(
            general = prefs[Keys.OVERRIDE_GENERAL],
            redDot = prefs[Keys.OVERRIDE_RED_DOT],
            scope2x = prefs[Keys.OVERRIDE_2X],
            scope4x = prefs[Keys.OVERRIDE_4X],
            awmScope = prefs[Keys.OVERRIDE_AWM],
            freeLook = prefs[Keys.OVERRIDE_FREE_LOOK]
        )
    }

    suspend fun setLanguage(language: Language) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language.code }
    }

    suspend fun setPlayStyle(playStyle: PlayStyle) {
        context.dataStore.edit { it[Keys.PLAYSTYLE] = playStyle.name }
    }

    suspend fun setAntiLag(enabled: Boolean) {
        context.dataStore.edit { it[Keys.ANTI_LAG] = enabled }
    }

    suspend fun setOverrideGeneral(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_GENERAL] = value } }
    suspend fun setOverrideRedDot(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_RED_DOT] = value } }
    suspend fun setOverride2x(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_2X] = value } }
    suspend fun setOverride4x(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_4X] = value } }
    suspend fun setOverrideAwm(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_AWM] = value } }
    suspend fun setOverrideFreeLook(value: Int) { context.dataStore.edit { it[Keys.OVERRIDE_FREE_LOOK] = value } }

    suspend fun resetOverrides() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.OVERRIDE_GENERAL)
            prefs.remove(Keys.OVERRIDE_RED_DOT)
            prefs.remove(Keys.OVERRIDE_2X)
            prefs.remove(Keys.OVERRIDE_4X)
            prefs.remove(Keys.OVERRIDE_AWM)
            prefs.remove(Keys.OVERRIDE_FREE_LOOK)
        }
    }
}
