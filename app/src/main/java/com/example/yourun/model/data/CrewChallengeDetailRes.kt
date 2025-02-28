package com.example.yourun.model.data

import com.google.gson.annotations.SerializedName

data class CrewChallengeDetailRes(
    @SerializedName("crewName") val crewName: String,  // 크루 이름
    @SerializedName("startDate") val startDate: String,  // 시작일
    @SerializedName("endDate") val endDate: String,  // 종료일
    @SerializedName("challengePeriod") val challengePeriod: Int,  // 챌린지 기간
    @SerializedName("joinCount") val joinCount: Int,  // 참여 인원
    @SerializedName("reward") val reward: Int,  // 보상 개수
    @SerializedName("participantIdsInfo") val participantIdInfos: List<ParticipantIdInfo>,  // 참여자 정보 리스트
    @SerializedName("slogan") val slogan: String  // 크루 구호
)

data class ParticipantIdInfo(
    val userId: Long,          // 유저 ID
    @SerializedName("memberTendency") val userTendency: Tendency   // 유저 성향 (예: "페이스메이커")
)