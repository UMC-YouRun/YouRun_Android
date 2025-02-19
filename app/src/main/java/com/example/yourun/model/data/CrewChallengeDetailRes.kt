package com.example.yourun.model.data

data class CrewChallengeDetailRes(
    val crewName: String,  // 크루 이름
    val startDate: String,  // 시작일
    val endDate: String,  // 종료일
    val challengePeriod: Int,  // 챌린지 기간
    val joinCount: Int,  // 참여 인원
    val reward: Int,  // 보상 개수
    val participantIdInfos: List<ParticipantIdInfo>,  // 참여자 정보 리스트
    val slogan: String  // 크루 구호
)

data class ParticipantIdInfo(
    val userId: Long,          // 유저 ID
    val userTendency: Tendency   // 유저 성향 (예: "페이스메이커")
)