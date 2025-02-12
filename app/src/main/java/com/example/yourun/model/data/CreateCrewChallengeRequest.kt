package com.example.yourun.model.data

import java.time.LocalDate

data class CreateCrewChallengeRequest(
    val crewName : String,
    val slogan : String,
    val endDate: String  )