package com.example.yourun.model.data

data class UpdateUserResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: UserInfo
)


