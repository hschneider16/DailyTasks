package com.example.dailytasks.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.settingsDataStore by preferencesDataStore(
    name = "settings"
)

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val THEME = stringPreferencesKey("theme")
    val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
}

class SettingsData(
    private val context: Context
) {
    val isDarkMode: Flow<Boolean> =
        context.settingsDataStore.data.map { preferences ->
            preferences[SettingsKeys.DARK_MODE] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.DARK_MODE] = enabled
        }
    }

    val isHideCompletedOn: Flow<Boolean> =
        context.settingsDataStore.data.map { preferences ->
            preferences[SettingsKeys.HIDE_COMPLETED] ?: false
        }

    suspend fun setHideCompleted(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.HIDE_COMPLETED] = enabled
        }
    }
}
