package com.example.yourun.model.data

data class ChallengeDataResponse(
    val period: Int,
    val challengeDistance: Int,
    val userTendency: String,
    val userNickName: String,
    val userCountDay: Int,
    val userHashTags: List<String>,
    val challengeMateTendency: String,
    val challengeMateNickName: String,
    val challengeMateCountDay: Int,
    val challengeMateHashTags: List<String>
)

