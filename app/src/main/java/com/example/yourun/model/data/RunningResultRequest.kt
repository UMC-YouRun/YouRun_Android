package com.example.yourun.model.data

data class RunningResultRequest(
    val targetTime: Int,
    val startTime: String,
    val endTime: String,
    val totalDistance: Int
)