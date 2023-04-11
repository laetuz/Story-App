package com.neotica.storyapp.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getUser(): Flow<UserLogin> {
        return dataStore.data.map {
            UserLogin(
                it[TOKEN] ?: "",
                it[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(login: UserLogin) {
        dataStore.edit {
            it[TOKEN] ?: ""
            it[STATE_KEY] ?: false
        }
    }

    suspend fun login() {
        dataStore.edit { it[STATE_KEY] = true }
    }

    suspend fun logout() {
        dataStore.edit {
            it[TOKEN] ?: ""
            it[STATE_KEY] ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null
        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}