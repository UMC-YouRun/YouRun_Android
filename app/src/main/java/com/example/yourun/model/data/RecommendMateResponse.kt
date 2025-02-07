package com.example.yourun.model.data

data class RecommendMateResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: List<UserMateInfo>
)

data class UserMateInfo(
    val id: Long,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val totalDistance: Int,
    val countDay: Int
)
