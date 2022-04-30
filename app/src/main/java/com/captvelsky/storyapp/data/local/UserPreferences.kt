package com.captvelsky.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private var USER_TOKEN = stringPreferencesKey("user_token")

    suspend fun setUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    fun getUserToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN]
        }
    }
}