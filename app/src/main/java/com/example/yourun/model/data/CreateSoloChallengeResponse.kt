package com.example.yourun.model.data

import java.time.LocalDate

data class CreateSoloChallengeResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: CreateSoloChallenge)

data class CreateSoloChallenge (
    val challengeId : Long,
    val startDate : String,
    val endDate : String,
    val challengePeriod : Int,
    val tendency : String

)

