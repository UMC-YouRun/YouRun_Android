package com.example.yourun.model.data

data class RunningResultResponse(
    val id: Long,
    val userName: String,
    val startTime: String,
    val endTime: String,
    val totalDistance: Int,
    val totalTime: Int
)
