package com.example.yourun.model.data.response

data class HomeChallengeResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: ChallengeData?
)

data class ChallengeData(
    val tendency: String,
    val homeNickName: String,
    val soloReward: Long,
    val crewReward: Long,
    val soloChallenge: UserSoloChallengeInfo?,
    val crewChallenge: UserCrewChallengeInfo?
)
