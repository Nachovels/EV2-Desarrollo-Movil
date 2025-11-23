package com.example.tcgstore.data.network

import com.example.tcgstore.data.network.models.*
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

    @GET("api/admin/stats")
    suspend fun getAdminStats(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Long
    ): Response<Map<String, String>>
}