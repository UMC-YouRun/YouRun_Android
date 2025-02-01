package com.example.yourun.model.data

data class ChallengeItem(
    val badgeImage: Int,
    val title: String,
    val description: String,
    val members: List<Int>,
    val remaining: String
)