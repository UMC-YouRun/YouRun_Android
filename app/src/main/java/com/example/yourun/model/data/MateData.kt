package com.example.yourun.model.data

data class MateData(
    val mateId: Long,
    var rank: Int,               // 순위 (로컬에서 계산)
    val profileImageResId: Int,  // tendancy 값으로 매칭하기
    val nickname: String,
    val tags: List<String>,
    val countDay: Int,             // 러닝 일수
    var totalDistance: Int,           // 거리
    var change: Int,              // 순위 변동 (로컬에서 계산)
    val tendency: String?,
)