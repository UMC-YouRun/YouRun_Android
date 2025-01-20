package com.example.yourun.model.data

data class ResultData(
    val userName: String,         // 사용자 이름
    val userType: String,         // 결과 유형
    val characterImageRes: Int,   // 캐릭터 이미지 리소스 ID
    val description: String       // 결과 설명
)