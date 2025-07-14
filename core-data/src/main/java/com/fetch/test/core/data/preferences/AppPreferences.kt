package com.fetch.test.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

/**
 * Manages application preferences using DataStore.
 */
class AppPreferences(private val context: Context) {

    private companion object {
        val LIST_MODE_KEY = booleanPreferencesKey("list_mode_enabled")
    }

    /**
     * Retrieves the current preference as a Flow.
     * @return A Flow emitting true if list mode is enabled, false otherwise.
     */
    fun getListMode(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LIST_MODE_KEY] ?: true
        }
    }

    /**
     * Saves the list mode preference.
     * @param isList True to enable list, false for grid.
     */
    suspend fun setListMode(isList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LIST_MODE_KEY] = isList
        }
    }
}