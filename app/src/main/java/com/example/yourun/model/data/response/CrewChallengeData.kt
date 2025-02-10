package com.example.yourun.model.data.response

data class CrewChallengeData(
    val period: Int,
    val crewName: String,
    val myCrewSlogan: String,
    val myParticipantIdsInfo: List<Participant>,
    val matchedCrewName: String,
    val matchedCrewSlogan: String,
    val matchedParticipantIdsInfo: List<Participant>
) {
    data class Participant(
        val userId: Int,
        val memberTendency: String
    )
}
