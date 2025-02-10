package com.example.yourun.model.data

data class UpdateUserResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val id: Int,
    val nickname: String,
    val tendency: String,
    val tags: String,
    val crewReward: Int,
    val personalReward: Int,
    val mvp: Int
)
