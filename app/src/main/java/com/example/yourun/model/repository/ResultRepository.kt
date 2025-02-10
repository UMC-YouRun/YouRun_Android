package com.example.yourun.model.repository

import com.example.yourun.R
import com.example.yourun.model.data.ResultData

object ResultRepository {

    val results = listOf(
        ResultData(
            userName = "사용자님", // 나중에 실제 회원으로 할당받아야 하는 부분
            userType = "페이스메이커",
            characterImageRes = R.drawable.img_facemaker,
            description = "어떤 상황에서도 속도를 조절하며 목표에 도달하는 팀의 중심, 페이스 메이커!"
        ),
        ResultData(
            userName = "사용자님",
            userType = "스프린터",
            characterImageRes = R.drawable.img_sprinter,
            description = "단거리에서도 최선을 다하며 속도감 있게 목표를 달성하는 당신은 스프린터!"
        ),
        ResultData(
            userName = "사용자님",
            userType = "트레일러너",
            characterImageRes = R.drawable.img_trailrunner,
            description = "끈기와 인내로 꾸준히 달리는 당신은 장거리의 강자, 마라토너!"
        )
    )

    fun getResultByType(type: String): ResultData? {
        return results.find { it.userType == type }
    }
}
