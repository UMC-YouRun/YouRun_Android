package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.R
import com.example.yourun.model.data.MateApiData
import com.example.yourun.model.data.MateData
import com.example.yourun.model.data.MateResponse
import com.example.yourun.model.network.ApiService
import com.example.yourun.utils.TokenManager

class MateRepository(private val apiService: ApiService) {

    suspend fun getMates(token: String): List<MateData> {
        return try {
            Log.d("MateRepository", "API 요청 시작: Authorization 헤더 포함")

            val response: MateResponse = apiService.getMates(token)

            // 응답 데이터 로그 출력 (디버깅용)
            Log.d("MateRepository", "API 응답 데이터: $response")

            // API 응답이 null이면 빈 리스트 반환
            response.data?.mapIndexed { index, apiData: MateApiData ->
                MateData(
                    rank = index + 1,
                    profileImageResId = getProfileImageByTendency(apiData.tendency),
                    nickname = apiData.nickname ?: "이름 없음", // null 방지
                    tags = apiData.tags ?: emptyList(),
                    countDay = apiData.countDay ?: (5..30).random(),   // API에서 totalDistance 활용
                    totalDistance = apiData.totalDistance ?: (10..50).random(),// 기존 임시 랜덤값에서 API 값 사용
                    change = apiData.countDay ?: (-2..2).random()  // 로컬에서 계산 필요
                )
            } ?: emptyList() // data가 null이면 빈 리스트 반환

        } catch (e: Exception) {
            Log.e("MateRepository", "API 호출 오류: ${e.message}", e)
            emptyList()
        }
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
