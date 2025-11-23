package com.example.tcgstore.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidNombre(nombre: String): Boolean {
        return !nombre.any { it.isDigit() }
    }

    fun isValidApellido(apellido: String): Boolean {
        return !apellido.any { it.isDigit() }
    }

    fun isValidRut(rut: String): Boolean {
        return rut.matches(Regex("^[0-9]{7,8}-[0-9kK]$|^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]$"))
    }

    fun isValidCorreo(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }

    fun isValidTelefono(telefono: String): Boolean {
        return telefono.matches(Regex("^[0-9]{9}$"))
    }

    fun isValidContrasena(contrasena: String): Boolean {
        return contrasena.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&._-]).{8,}$"))
    }

    fun contrasenasIguales(contrasena1: String, contrasena2: String): Boolean {
        return contrasena1 == contrasena2
    }
}
