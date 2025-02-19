package com.example.yourun.model.data.response

data class ChallengeResultResponse(
    val challengePeriod: Int,
    val challengeDistance: Int,
    val dayCount: Int,
    val challengeMateInfo: ChallengeMateInfo,
    val userIsSuccess: Boolean,
    val userTendency: String
)

data class ChallengeMateInfo(
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val successDay: Int,
    val challengeMateIsSuccess: Boolean,
    val distance: Int
)
