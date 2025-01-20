package com.example.yourun.model.repository

import com.example.yourun.R
import com.example.yourun.model.data.ResultData

object ResultRepository {

    // 결과 데이터 목록
    val results = listOf(
        ResultData(
            userName = "제제님", // 나중에 실제 회원으로 할당받아야 하는 부분
            userType = "페이스 메이커",
            characterImageRes = R.drawable.img_facemaker,
            description = "어떤 상황에서도 속도를 조절하며 목표에 도달하는 팀의 중심, 페이스 메이커!"
        ),
        ResultData(
            userName = "제제님",
            userType = "스프린터",
            characterImageRes = R.drawable.img_sprinter,
            description = "단거리에서도 최선을 다하며 속도감 있게 목표를 달성하는 당신은 스프린터!"
        ),
        ResultData(
            userName = "제제님",
            userType = "마라토너",
            characterImageRes = R.drawable.img_trailrunner,
            description = "끈기와 인내로 꾸준히 달리는 당신은 장거리의 강자, 마라토너!"
        )
    )

    // 선택에 따라 타입 변경되게 수정
    fun getResultByType(type: String): ResultData? {
        return results.find { it.userType == type }
    }
}