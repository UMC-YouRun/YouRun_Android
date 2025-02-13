package com.example.yourun.model.data.response

data class CreateCrewChallengeResponse(
      val status: Int,
      val code: String,
      val message: String,
      val data: CreateCrewChallenge
)

data class CreateCrewChallenge
      ( val challengeId : Long,
      val crewName : String,
      val startDate : String,
      val endDate : String,
      val challengePeriod : Int,
      val tendency : String )