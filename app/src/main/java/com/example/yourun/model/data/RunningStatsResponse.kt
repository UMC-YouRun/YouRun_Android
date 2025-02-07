package com.example.yourun.model.data

data class RunningStatsResponse(
    val id: Int,
    val totalDistance: Int,
    val totalTime: Int,
    val pace: Int,
    val createdAt: String
)
