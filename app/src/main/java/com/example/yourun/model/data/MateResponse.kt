package com.example.yourun.model.data

data class MateResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: List<MateApiData>?
)

data class MateApiData(
    val id: Int,
    val nickname: String?,
    val tendency: String?,
    val tags: List<String>?,
    val totalDistance: Int?,
    val countDay: Int?
)