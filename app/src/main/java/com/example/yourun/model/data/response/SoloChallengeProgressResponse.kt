package com.example.yourun.model.data.response

data class SoloChallengeProgressResponse(
    val challengePeriod: Int,
    val challengeDistance: Int,
    val dayCount: Int,
    val now: String,
    val DayResultInfo: List<DayResult>,
    val challengeMateInfo: ChallengeMateProgressInfo,
    val isSuccess: Boolean,
    val tendency: String
)

data class DayResult(
    val day: Int,
    val distance: Int
)

data class ChallengeMateProgressInfo(
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val successDay: Int,
    val isSuccess: Boolean,
    val distance: Int
)
