package com.example.yourun.model.data

data class SoloChallengeDetailRes(
    val startDate: String,
    val endDate: String,
    val challengeDistance: Int,
    val challengePeriod: Int,
    val challengeCreatorNickName: String,
    val challengeCreatorHashTags: List<String>,
    val tendency: Tendency,
    val reward: Int,
    val countDay: Int
)