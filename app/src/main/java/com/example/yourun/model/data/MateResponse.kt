package com.example.yourun.model.data

import com.example.yourun.model.data.UserInfo

data class MateResponse<T>(
    val status: Int,
    val code: String?,
    val message: String?,
    val data: T?
)

data class MateApiData(
    val id: Int,
    val nickname: String?,
    val tendency: String?,
    val tags: List<String>?,
    val totalDistance: Int?,
    val countDay: Int?
)