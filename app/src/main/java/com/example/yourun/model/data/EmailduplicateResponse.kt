package com.example.yourun.model.data

data class EmailduplicateResponse(
    val status: Int,
    val code: String?,
    val message: String,
    val data: Boolean
)