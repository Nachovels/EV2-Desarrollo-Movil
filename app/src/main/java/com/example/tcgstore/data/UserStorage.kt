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
        val PRODUCTS_KEY = stringPreferencesKey("products")
        val LOGGED_IN_USER_EMAIL_KEY = stringPreferencesKey("logged_in_user_email")
    }

    // --- LOGGED IN USER ---

    suspend fun saveLoggedInUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_EMAIL_KEY] = email
        }
    }

    val loggedInUserEmailFlow: Flow<String?> = context.dataStore.data.map {
        it[LOGGED_IN_USER_EMAIL_KEY]
    }

    suspend fun clearLoggedInUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(LOGGED_IN_USER_EMAIL_KEY)
        }
    }

    // --- USERS ---

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
                mutableListOf()
            }
            users.add(user)
            preferences[USERS_KEY] = Json.encodeToString(users)
        }
    }

    suspend fun updateUser(updatedUser: User) {
        context.dataStore.edit { preferences ->
            val users = try {
                val currentUsersJson = preferences[USERS_KEY]
                if (currentUsersJson != null) {
                    Json.decodeFromString<MutableList<User>>(currentUsersJson)
                } else {
                    return@edit
                }
            } catch (e: Exception) {
                return@edit
            }
            val userIndex = users.indexOfFirst { it.correo == updatedUser.correo }
            if (userIndex != -1) {
                users[userIndex] = updatedUser
                preferences[USERS_KEY] = Json.encodeToString(users)
            }
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
            emptyList()
        }
    }

    // LOGIN ATTEMPTS

    suspend fun addLoginAttempt(attempt: LoginAttempt) {
        context.dataStore.edit { preferences ->
            val attempts = try {
                val currentAttemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
                if (currentAttemptsJson != null) {
                    Json.decodeFromString<MutableList<LoginAttempt>>(currentAttemptsJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                mutableListOf()
            }
            attempts.add(attempt)
            preferences[LOGIN_ATTEMPTS_KEY] = Json.encodeToString(attempts)
        }
    }

    val loginAttemptsFlow: Flow<List<LoginAttempt>> = context.dataStore.data.map { preferences ->
        try {
            val attemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
            if (attemptsJson != null) {
                Json.decodeFromString<List<LoginAttempt>>(attemptsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // PRODUCTS

    suspend fun saveProduct(product: Product) {
        context.dataStore.edit { preferences ->
            val products = try {
                val currentProductsJson = preferences[PRODUCTS_KEY]
                if (currentProductsJson != null) {
                    Json.decodeFromString<MutableList<Product>>(currentProductsJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                mutableListOf()
            }
            products.add(product)
            preferences[PRODUCTS_KEY] = Json.encodeToString(products)
        }
    }

    val productsFlow: Flow<List<Product>> = context.dataStore.data.map { preferences ->
        try {
            val productsJson = preferences[PRODUCTS_KEY]
            if (productsJson != null) {
                Json.decodeFromString<List<Product>>(productsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
