package com.example.yourun.model.data

data class LoginResponse(
    val status: Int,
    val code: String?,
    val message: String,
    val data: TokenData?
)

data class TokenData(
    val access_token: String
)
