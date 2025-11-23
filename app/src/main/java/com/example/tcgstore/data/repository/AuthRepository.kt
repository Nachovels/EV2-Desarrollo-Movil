package com.example.tcgstore.data.repository

import android.content.Context
import com.example.tcgstore.data.UserStorage
import com.example.tcgstore.data.network.RetrofitClient
import com.example.tcgstore.data.network.models.LoginRequest
import com.example.tcgstore.data.network.models.RegisterRequest
import com.example.tcgstore.data.network.models.AuthResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthRepository(private val context: Context) {

    private val apiService = RetrofitClient.apiService
    private val userStorage = UserStorage(context)

    // Guardar token en SharedPreferences
    private suspend fun saveToken(token: String) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("jwt_token", token).apply()
    }

    // Obtener token guardado
    fun getToken(): String? {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("jwt_token", null)
    }

    // Eliminar token (logout)
    suspend fun clearToken() {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().remove("jwt_token").apply()
        userStorage.guardarEmailUsuarioLogueado("")
    }

    // Registro
    suspend fun register(
        nombreCompleto: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ): ApiResult<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(
                nombreCompleto = nombreCompleto,
                correoElectronico = correo,
                password = password,
                confirmarPassword = confirmarPassword
            )

            val response = apiService.register(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveToken(authResponse.token)
                userStorage.guardarEmailUsuarioLogueado(authResponse.correoElectronico)
                ApiResult.Success(authResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error de conexión: ${e.message}")
        }
    }

    // Login
    suspend fun login(
        correo: String,
        password: String
    ): ApiResult<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(
                correoElectronico = correo,
                password = password
            )

            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveToken(authResponse.token)
                userStorage.guardarEmailUsuarioLogueado(authResponse.correoElectronico)
                ApiResult.Success(authResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error de conexión: ${e.message}")
        }
    }

    // Obtener perfil de usuario
    suspend fun getUserProfile(): ApiResult<Any> = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            if (token == null) {
                return@withContext ApiResult.Error("No hay sesión activa")
            }

            val response = apiService.getUserProfile("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error de conexión: ${e.message}")
        }
    }

    // Obtener lista de usuarios (solo admin)
    suspend fun getAllUsers(): ApiResult<List<Any>> = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            if (token == null) {
                return@withContext ApiResult.Error("No hay sesión activa")
            }

            val response = apiService.getAllUsers("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error de conexión: ${e.message}")
        }
    }

    // Parsear mensaje de error del backend
    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody != null) {
                val jsonObject = Gson().fromJson(errorBody, JsonObject::class.java)
                jsonObject.get("mensaje")?.asString ?: "Error desconocido"
            } else {
                "Error desconocido"
            }
        } catch (e: Exception) {
            "Error al procesar respuesta del servidor"
        }
    }
}