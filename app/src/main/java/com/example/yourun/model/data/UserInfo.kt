package com.example.yourun.model.data

data class UserInfo(
    val id: Int,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val crewReward: Int,
    val personalReward: Int,
    val mvp: Int
)