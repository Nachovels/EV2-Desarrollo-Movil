package com.example.tcgstore.data

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
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
data class IntentoLogin(
    val correo: String,
    val exito: Boolean,
    val horario: Long = System.currentTimeMillis()
)

@Serializable
data class Producto(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imageUri: String
)

@Serializable
data class Pedido(
    val id: String,
    val fecha: String,
    val total: Double,
    val estado: String,
    val productos: List<Producto>
)
