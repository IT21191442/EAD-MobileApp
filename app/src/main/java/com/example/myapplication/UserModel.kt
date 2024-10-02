package com.example.myapplication

data class UserDto(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int,
    val password: String,
    val role: String,
    val status: String
)

data class LoginDto(
    val email: String,
    val password: String
)

data class ApiResponse<T>(
    val isSuccessful: Boolean,
    val timeStamp: String,
    val message: String,
    val data: T?
)