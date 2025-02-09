package com.example.yourun.model.data

data class MyPageResponse<T>(
    val status: Int,
    val code: String?,
    val message: String?,
    val data: T?
)

data class MyPageData(
    val id: Int,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val crewReward: Int,
    val personalReward: Int,
    val mvp: Int
)