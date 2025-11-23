package com.example.tcgstore.data.network.models

import com.google.gson.annotations.SerializedName

// Request Models
data class LoginRequest(
    @SerializedName("correoElectronico")
    val correoElectronico: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("nombreCompleto")
    val nombreCompleto: String,
    @SerializedName("correoElectronico")
    val correoElectronico: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confirmarPassword")
    val confirmarPassword: String
)

// Response Models
data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("tipo")
    val tipo: String = "Bearer",
    @SerializedName("correoElectronico")
    val correoElectronico: String,
    @SerializedName("nombreCompleto")
    val nombreCompleto: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("mensaje")
    val mensaje: String
)

data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("errores")
    val errores: Map<String, String>? = null
)

data class UserProfileResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nombreCompleto")
    val nombreCompleto: String,
    @SerializedName("correoElectronico")
    val correoElectronico: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("fechaRegistro")
    val fechaRegistro: String
)

data class UserListResponse(
    val id: Long,
    val nombreCompleto: String,
    val correoElectronico: String,
    val role: String,
    val fechaRegistro: String,
    val enabled: Boolean
)