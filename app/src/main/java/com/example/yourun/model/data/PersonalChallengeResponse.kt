package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class PersonalChallengeResponse(
    val userId: Long,
    val userTendency: Tendency?,
    val userCrewReward: Long,
    val userSoloReward: Long,
    @SerializedName("soloChallengeList")
    val soloChallengeList: List<SoloChallengeRes>? = emptyList()
)

data class SoloChallengeRes(
    val challengeId: Long,
    val challengeDistance: Int,
    val challengePeriod: Int,
    val challengeCreatorNickname: String,
    val challengeCreatorHashtags: List<String>,
    val reward: Int,
    val challengeCreatorTendency: Tendency
)