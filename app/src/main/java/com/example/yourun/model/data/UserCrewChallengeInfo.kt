package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class UserCrewChallengeInfo(
    @SerializedName("challengeId") val challengeId: Long,
    @SerializedName("crewName") val crewName: String,
    @SerializedName("challengeStatus") val challengeStatus: String,
    @SerializedName("challengePeriod") val challengePeriod: Int,
    @SerializedName("myParticipantIdsInfo") val myParticipantIdsInfo: List<MemberTendencyInfo>,
    @SerializedName("crewDayCount") val crewDayCount: Int,
    @SerializedName("crewStartDate") val crewStartDate: String
)

