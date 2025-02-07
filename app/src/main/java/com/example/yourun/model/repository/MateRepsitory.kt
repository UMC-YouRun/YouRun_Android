package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.R
import com.example.yourun.model.data.MateApiData
import com.example.yourun.model.data.MateData
import com.example.yourun.model.data.MateResponse
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.network.ApiService

class MateRepository(private val apiService: ApiService) {

    suspend fun getMates(token: String): List<MateData> {
        val response: MateResponse = apiService.getMates(token)


        // API 응답 데이터를 로컬 데이터로 변환
        return response.data?.mapIndexed { index, apiData: MateApiData ->
            MateData(
                rank = index + 1,
                profileImageResId = getProfileImageByTendency(apiData.tendency),
                nickname = apiData.nickname ?: "이름 없음", // null 방지
                tags = apiData.tags ?: emptyList(),
                countDay = apiData.countDay ?: (5..30).random(), // API에서 countDay 활용
                totalDistance = apiData.totalDistance ?: (10..50).random(), // API 값 없으면 기본값 사용
                change = apiData.countDay ?: (-2..2).random() // 변화 값 추가
            )

        } ?: emptyList()  // data가 null일 경우 빈 리스트 반환
    }

    /*
    private fun getRandomProfileImage(): Int {
        val images = listOf(
            R.drawable.img_profile_pacemaker_purple,
            R.drawable.img_profile_trailrunner_red,
            R.drawable.img_profile_sprinter_yellow
        )
        return images.random()
    }
    */

    private fun getProfileImageByTendency(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_profile_pacemaker_purple
            "트레일러너" -> R.drawable.img_profile_trailrunner_red
            "스프린터" -> R.drawable.img_profile_sprinter_yellow
            else -> R.drawable.img_profile_pacemaker_purple  // 경향 없을시 기본 이미지 불러오기
        }
    }

}
