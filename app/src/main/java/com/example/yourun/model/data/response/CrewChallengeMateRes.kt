package com.example.yourun.model.data.response

/** ✅ 챌린지 참여 응답 데이터 모델 */
data class CrewChallengeMateRes(
    val challengeId: Long,
    val participantIds: List<Long>
)

/** ✅ 공통 API 응답을 감싸는 제네릭 클래스 */
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)
