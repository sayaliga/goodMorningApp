package com.sayali.wishmate.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsRepository(private val context: Context) {

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    val languageFlow: Flow<AppLanguage?> = context.dataStore.data.map { prefs ->
        val code = prefs[LANGUAGE_KEY]
        AppLanguage.fromCode(code)
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language.code
        }
    }
}
