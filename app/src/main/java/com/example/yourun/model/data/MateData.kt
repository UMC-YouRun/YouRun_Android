package com.example.yourun.model.data

data class MateData(
    val rank: Int,               // 순위 (로컬에서 계산)
    val profileImageResId: Int,  // 로컬에서 랜덤으로 설정
    val name: String,            // nickname 값 사용
    val tendency: String,
    val tags: List<String>,
    val runday: Int,             // 러닝 일수 (API에서 안 주면 임의 처리)
    val distance: Int,           // 거리 (API 확장 시 활용, 현재는 로컬 처리)
    val change: Int              // 순위 변동 (로컬에서 계산 가능)
)