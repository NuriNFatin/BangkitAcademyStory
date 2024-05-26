package com.example.bangkitacademystory.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bangkitacademystory.Source.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_NAME] = user.name
            preferences[KEY_USER_EMAIL] = user.email
            preferences[KEY_USER_TOKEN] = user.token
            preferences[KEY_USER_LOGIN_STATUS] = user.isLogin
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            val name = preferences[KEY_USER_NAME] ?: ""
            val email = preferences[KEY_USER_EMAIL] ?: ""
            val token = preferences[KEY_USER_TOKEN] ?: ""
            val isLogin = preferences[KEY_USER_LOGIN_STATUS] ?: false
            User(name, email, token, isLogin)
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_USER_NAME)
            preferences.remove(KEY_USER_EMAIL)
            preferences.remove(KEY_USER_TOKEN)
            preferences.remove(KEY_USER_LOGIN_STATUS)
        }
    }

    companion object {
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_TOKEN = stringPreferencesKey("user_token")
        private val KEY_USER_LOGIN_STATUS = booleanPreferencesKey("user_login_status")

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}