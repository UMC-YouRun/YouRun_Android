package com.example.yourun.model.data.response

data class RunningDataResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: RunningData?
)

data class RunningData(
    val id: Long,
    val totalDistance: Int,
    val totalTime: Int,
    val pace: Int,
    val createdAt: String
)