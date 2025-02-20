package com.example.yourun.model.data.response

data class ChallengeItem(
    val challengeId: Long,
    val badgeImage: Int,
    val title: String,
    val description: String,
    val members: List<Int>,
    val memberTendencies: List<String>,
    val remaining: String,
    val isCrewChallenge: Boolean
)