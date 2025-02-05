package com.example.yourun.model.data

data class SignUpRequest(
    val email: String,
    val password: String,
    val passwordcheck: String,
    val nickname: String,
    val tendency: String,
    val tag1: String,
    val tag2: String
)

data class SignUpResponse(
    val status: Int,
    val message: String,
    val data: Any?
)
