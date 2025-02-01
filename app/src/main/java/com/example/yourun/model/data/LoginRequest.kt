package com.example.yourun.model.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: Int,
    val code: String?,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val accessToken: String
)