package com.example.yourun.model.data.response

data class SoloChallengeItem(
    val challengeId: Long,
    val badgeImage: Int,
    val title: String,
    val description: String,
    val hashtags: String,
    val challengeCreatorTendency: String
)