package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class CrewChallengeResponse(
    val userId: Long,  // 유저 아이디
    val userTendency: Tendency?,  // 유저 성향 (Tendency 타입이 뭔지 확인 필요)
    val userCrewReward: Long,  // 유저 크루 챌린지 보상 개수
    val userSoloReward: Long,  // 유저 솔로 챌린지 보상 개수
    @SerializedName("crewChallengeResList") // JSON의 필드명을 정확히 매핑
    val crewChallengeRes: List<CrewChallengeRes> // 대기 중인 크루 챌린지 리스트
)

data class CrewChallengeRes(
    val challengeId: Long,
    val crewName: String,
    val challengePeriod: Int,
    val remaining: Int,
    val reward: Int,
    val participantIdsInfo: List<MemberTendencyInfo>
)