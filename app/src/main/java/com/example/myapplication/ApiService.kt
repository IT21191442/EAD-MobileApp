package com.example.myapplication

import retrofit2.http.*

interface ApiService {
    @POST("api/auth/register")
    suspend fun registerUser(@Body user: UserDto): ApiResponse<UserDto>


    @POST("api/auth/login")
    suspend fun loginUser(@Body loginDto: LoginDto): ApiResponse<String>

    @GET("api/users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): ApiResponse<UserDto>

    @PUT("api/users/profile")
    suspend fun updateUserProfile(@Header("Authorization") token: String, @Body user: UserDto): ApiResponse<UserDto>

    @PUT("api/users/update-status/{userId}")
    suspend fun updateUserStatus(@Header("Authorization") token: String, @Path("userId") userId: String, @Body status: Map<String, String>): ApiResponse<UserDto>
}