package com.example.tcgstore.data.network

import com.example.tcgstore.data.IntentoLogin
import com.example.tcgstore.data.network.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ============ AUTH ENDPOINTS ============

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // ============ USER ENDPOINTS ============

    @GET("api/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>

    @GET("api/user/dashboard")
    suspend fun getUserDashboard(
        @Header("Authorization") token: String
    ): Response<Map<String, String>>

    // ============ ADMIN ENDPOINTS ============

    @GET("api/admin")
    suspend fun getAdminPanel(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("api/admin/users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<UserListResponse>>

    @GET("api/admin/login-attempts")
    suspend fun getLoginAttempts(
        @Header("Authorization") token: String
    ): Response<List<IntentoLogin>>

    @GET("api/admin/stats")
    suspend fun getAdminStats(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Long
    ): Response<Map<String, String>>

    // ============ PRODUCT ENDPOINTS ============

    @GET("api/productos")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @GET("api/productos/{id}")
    suspend fun getProductById(
        @Path("id") productId: Long
    ): Response<ProductResponse>

    @Multipart
    @POST("api/productos/con-imagen")
    suspend fun createProduct(
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part imagen: MultipartBody.Part,
        @Part("oferta") oferta: RequestBody?,
        @Part hover: MultipartBody.Part?
    ): Response<ProductResponse>

    @Multipart
    @PUT("api/productos/{id}/con-imagen")
    suspend fun updateProduct(
        @Path("id") productId: Long,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("oferta") oferta: RequestBody?,
        @Part imagen: MultipartBody.Part?,
        @Part hover: MultipartBody.Part?
    ): Response<ProductResponse>

    @DELETE("api/productos/{id}")
    suspend fun deleteProduct(
        @Path("id") productId: Long
    ): Response<Void>

}