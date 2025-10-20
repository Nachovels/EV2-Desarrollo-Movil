package com.example.tcgstore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserStorage(private val context: Context) {

    companion object {
        val USERS_KEY = stringPreferencesKey("users")
        val LOGIN_ATTEMPTS_KEY = stringPreferencesKey("login_attempts")
    }

    // USERS

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            val users = try {
                val currentUsersJson = preferences[USERS_KEY]
                if (currentUsersJson != null) {
                    Json.decodeFromString<MutableList<User>>(currentUsersJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                // If we fail to read/decode, start with a fresh list
                mutableListOf()
            }
            users.add(user)
            preferences[USERS_KEY] = Json.encodeToString(users)
        }
    }

    val usersFlow: Flow<List<User>> = context.dataStore.data.map { preferences ->
        try {
            val usersJson = preferences[USERS_KEY]
            if (usersJson != null) {
                Json.decodeFromString<List<User>>(usersJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // If we fail to read/decode, return an empty list
            emptyList()
        }
    }

    // LOGIN ATTEMPTS

    suspend fun addLoginAttempt(attempt: LoginAttempt) {
        context.dataStore.edit { preferences ->
            val attempts = try {
                // This will read the JSON string. If the key holds an old data type (like Int),
                // it will throw a ClassCastException, which we catch.
                val currentAttemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
                if (currentAttemptsJson != null) {
                    Json.decodeFromString<MutableList<LoginAttempt>>(currentAttemptsJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                // If we fail to read/decode (e.g., due to old data format), start with a fresh list.
                mutableListOf()
            }
            attempts.add(attempt)
            preferences[LOGIN_ATTEMPTS_KEY] = Json.encodeToString(attempts)
        }
    }

    val loginAttemptsFlow: Flow<List<LoginAttempt>> = context.dataStore.data.map { preferences ->
        try {
            // This will read the JSON string. If the key holds an old data type (like Int),
            // it will throw a ClassCastException, which we catch.
            val attemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
            if (attemptsJson != null) {
                Json.decodeFromString<List<LoginAttempt>>(attemptsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // If we fail to read/decode (e.g., due to old data format), return an empty list.
            emptyList()
        }
    }
}
