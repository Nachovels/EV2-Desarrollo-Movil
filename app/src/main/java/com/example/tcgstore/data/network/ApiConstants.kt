package com.example.tcgstore.data.network

object ApiConstants {
    // Servidor AWS
    private const val SERVER_IP = "13.216.97.34"

    // URLs de los microservicios
    const val AUTH_BASE_URL = "http://$SERVER_IP:8081/"
    const val MAIN_BASE_URL = "http://$SERVER_IP:8080/"

    // Helper para construir URLs de imágenes
    fun getImageUrl(imagePath: String): String {
        return "$MAIN_BASE_URL${imagePath.removePrefix("/")}"
    }
}