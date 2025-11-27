package com.example.tcgstore.data.repository

import android.content.Context
import android.net.Uri
import com.example.tcgstore.data.network.RetrofitClient
import com.example.tcgstore.data.network.models.ProductResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductRepository(private val context: Context) {

    private val apiService = RetrofitClient.mainApiService  // Usar servicio principal

    // Obtener todos los productos
    suspend fun getAllProducts(): ApiResult<List<ProductResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllProducts()

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

    // Obtener producto por ID
    suspend fun getProductById(productId: Long): ApiResult<ProductResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProductById(productId)

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

    // Crear producto con imagen
    suspend fun createProduct(
        nombre: String,
        descripcion: String,
        precio: Int,
        imagenUri: Uri,
        oferta: String? = null,
        hoverUri: Uri? = null
    ): ApiResult<ProductResponse> = withContext(Dispatchers.IO) {
        try {
            // Convertir Uri a File
            val imagenFile = uriToFile(imagenUri, "product_image.jpg")
            val imagenPart = MultipartBody.Part.createFormData(
                "imagen",
                imagenFile.name,
                imagenFile.asRequestBody("image/*".toMediaTypeOrNull())
            )

            // Preparar otros campos
            val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val descripcionBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioBody = precio.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val ofertaBody = oferta?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Imagen hover opcional
            val hoverPart = hoverUri?.let {
                val hoverFile = uriToFile(it, "product_hover.jpg")
                MultipartBody.Part.createFormData(
                    "hover",
                    hoverFile.name,
                    hoverFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            val response = apiService.createProduct(
                nombreBody,
                descripcionBody,
                precioBody,
                imagenPart,
                ofertaBody,
                hoverPart
            )

            // Limpiar archivos temporales
            imagenFile.delete()
            hoverPart?.let { uriToFile(hoverUri!!, "temp.jpg").delete() }

            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error al crear producto: ${e.message}")
        }
    }

    // Eliminar producto
    suspend fun deleteProduct(productId: Long): ApiResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteProduct(productId)

            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Error al eliminar producto: ${e.message}")
        }
    }

    // Convertir Uri a File temporal
    private fun uriToFile(uri: Uri, fileName: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
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