package com.example.yourun.model.data.response

import com.google.gson.annotations.SerializedName

data class MemberTendencyInfo(
    val userId: Long,
    @SerializedName("memberTendency") // JSON에서 넘어오는 원본 그대로 매핑
    val memberTendencyRaw: String // ✅ Tendency가 아니라 String으로 받음

) {
    val memberTendency: Tendency
        get() = Tendency.fromValue(memberTendencyRaw) // ✅ `Tendency` 변환 함수 사용
}