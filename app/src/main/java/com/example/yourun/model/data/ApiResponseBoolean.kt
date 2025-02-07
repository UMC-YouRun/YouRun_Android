package com.example.yourun.model.data

data class ApiResponseBoolean(
    val status: Int,
    val code: String,
    val message: String,
    val data: Boolean
)