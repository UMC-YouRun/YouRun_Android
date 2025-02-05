package com.example.yourun.model.data

data class UserCrewChallengeInfo(
    val challengeId: Long,
    val crewName: String,
    val challengeStatus: String,
    val challengePeriod: Int,
    val myParticipantIdsInfo: List<MemberTendencyInfo>,
    val crewDayCount: Int,
    val crewStartDate: String
)
