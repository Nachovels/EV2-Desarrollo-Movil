package com.example.tcgstore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Serializable
data class ItemCarrito(val producto: Producto, val cantidad: Int)

class UserStorage(private val context: Context) {

    companion object {
        val USERS_KEY = stringPreferencesKey("usuarios")
        val LOGIN_ATTEMPTS_KEY = stringPreferencesKey("intento_login")
        val PRODUCTS_KEY = stringPreferencesKey("productos")
        val LOGGED_IN_USER_EMAIL_KEY = stringPreferencesKey("email_usuario_logueado")
        val CART_KEY = stringPreferencesKey("carrito")
    }

    suspend fun guardarCarrito(carrito: List<ItemCarrito>) {
        context.dataStore.edit { preferences ->
            preferences[CART_KEY] = Json.encodeToString(carrito)
        }
    }

    val carritoFlow: Flow<List<ItemCarrito>> = context.dataStore.data.map { preferences ->
        try {
            val cartJson = preferences[CART_KEY]
            if (cartJson != null) {
                Json.decodeFromString<List<ItemCarrito>>(cartJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun vaciarCarrito() {
        context.dataStore.edit { preferences ->
            preferences.remove(CART_KEY)
        }
    }

    suspend fun guardarEmailUsuarioLogueado(correo: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_EMAIL_KEY] = correo
        }
    }

    val emailUsuarioLogueadoFlow: Flow<String?> = context.dataStore.data.map {
        it[LOGGED_IN_USER_EMAIL_KEY]
    }


    suspend fun guardarUsuario(usuario: Usuario) {
        context.dataStore.edit { preferences ->
            val usuarios = try {
                val currentUsersJson = preferences[USERS_KEY]
                if (currentUsersJson != null) {
                    Json.decodeFromString<MutableList<Usuario>>(currentUsersJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                mutableListOf()
            }
            usuarios.add(usuario)
            preferences[USERS_KEY] = Json.encodeToString(usuarios)
        }
    }

    suspend fun actualizarUsuario(actualizarUsuario: Usuario) {
        context.dataStore.edit { preferences ->
            val usuarios = try {
                val currentUsersJson = preferences[USERS_KEY]
                if (currentUsersJson != null) {
                    Json.decodeFromString<MutableList<Usuario>>(currentUsersJson)
                } else {
                    return@edit
                }
            } catch (e: Exception) {
                return@edit
            }
            val usuarioIndex = usuarios.indexOfFirst { it.correo == actualizarUsuario.correo }
            if (usuarioIndex != -1) {
                usuarios[usuarioIndex] = actualizarUsuario
                preferences[USERS_KEY] = Json.encodeToString(usuarios)
            }
        }
    }

    val usuariosFlow: Flow<List<Usuario>> = context.dataStore.data.map { preferences ->
        try {
            val usersJson = preferences[USERS_KEY]
            if (usersJson != null) {
                Json.decodeFromString<List<Usuario>>(usersJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun agregarIntentoLogin(intento: IntentoLogin) {
        context.dataStore.edit { preferences ->
            val attempts = try {
                val currentAttemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
                if (currentAttemptsJson != null) {
                    Json.decodeFromString<MutableList<IntentoLogin>>(currentAttemptsJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                mutableListOf()
            }
            attempts.add(intento)
            preferences[LOGIN_ATTEMPTS_KEY] = Json.encodeToString(attempts)
        }
    }

    val intentoLoginFlow: Flow<List<IntentoLogin>> = context.dataStore.data.map { preferences ->
        try {
            val attemptsJson = preferences[LOGIN_ATTEMPTS_KEY]
            if (attemptsJson != null) {
                Json.decodeFromString<List<IntentoLogin>>(attemptsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun guardarProducto(producto: Producto) {
        context.dataStore.edit { preferences ->
            val products = try {
                val currentProductsJson = preferences[PRODUCTS_KEY]
                if (currentProductsJson != null) {
                    Json.decodeFromString<MutableList<Producto>>(currentProductsJson)
                } else {
                    mutableListOf()
                }
            } catch (e: Exception) {
                mutableListOf()
            }
            products.add(producto)
            preferences[PRODUCTS_KEY] = Json.encodeToString(products)
        }
    }

    val productosFlow: Flow<List<Producto>> = context.dataStore.data.map { preferences ->
        try {
            val productsJson = preferences[PRODUCTS_KEY]
            if (productsJson != null) {
                Json.decodeFromString<List<Producto>>(productsJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
