package com.example.tcgstore.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nombre: String,
    val apellido: String,
    val rut: String,
    val correo: String,
    val direccion: String,
    val telefono: String,
    val contrasena: String,
    val photoUri: String? = null
)

@Serializable
data class LoginAttempt(
    val correo: String,
    val success: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class Product(
    val name: String,
    val description: String,
    val price: Int,
    val imageUri: String
)

@Serializable
data class Order(
    val id: String,
    val date: String,
    val total: Double,
    val status: String,
    val items: List<Product>
)
