package com.example.yourun.model.data.response

data class ResultContributionResponse(
    val challengePeriod: Int,
    val reward: Int,
    val crewName: String,
    val crewMemberDistance: List<CrewMember>,
    val mvpId: Int,
    val win: Boolean
)

data class CrewMember(
    val userId: Int,
    val runningDistance: Double,
    val userTendency: String,
    val rank: Int
)
