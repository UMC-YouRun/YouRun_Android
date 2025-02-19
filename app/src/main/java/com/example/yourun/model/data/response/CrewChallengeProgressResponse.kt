package com.example.yourun.model.data.response

data class CrewChallengeProgressResponse(
    val challengePeriod: Int,
    val myCrewName: String,
    val myCrewSlogan: String,
    val myCrewMembers: List<CrewProgressMember>,
    val myCrewDistance: Double,
    val matchedCrewName: String,
    val matchedCrewSlogan: String,
    val matchedCrewCreatorTendency: String,
    val matchedCrewDistance: Double,
    val now: String
)

data class CrewProgressMember(
    val userId: Int,
    val runningDistance: Double,
    val userTendency: String
)
