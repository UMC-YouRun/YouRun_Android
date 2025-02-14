package com.example.yourun.model.data.response

data class CrewChallengeResultResponse(
    val status: Int,
    val message: String,
    val data: CrewChallengeResultData?
)

data class CrewChallengeResultData(
    val challengePeriod: Int = 0,
    val myCrewName: String = "",
    val beforeDistance: Double = 0.0,
    val userDistance: Double = 0.0,
    val afterDistance: Double = 0.0,
    val matchedCrewName: String = "",
    val matchedCrewCreator: String = "",
    val matchedCrewDistance: Double = 0.0
)