package com.example.yourun.model.data.response

data class UserSoloChallengeInfo(
    val challengeId: Long,
    val status: String,
    val challengeDistance: Int,
    val challengePeriod: Int,
    val challengeMateId: Long,
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val userId: Long,
    val userNickName: String,
    val userTendency: String,
    val soloDayCount: Int,
    val soloStartDate: String
)
