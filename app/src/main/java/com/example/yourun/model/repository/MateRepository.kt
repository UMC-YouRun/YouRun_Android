package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.R
import com.example.yourun.model.data.MateData
import com.example.yourun.model.data.response.UserInfo
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.network.ApiService

class MateRepository(private val apiService: ApiService) {

    suspend fun getMyRunData(): UserInfo? {
        return try {
            val response = apiService.getMyRunData() // ✅ 변경된 함수명 반영
            if (response.isSuccessful) {
                response.body()?.data?.let { myPageData ->
                    UserInfo(
                        id = myPageData.id,
                        nickname = myPageData.nickname,
                        tendency = myPageData.tendency,
                        tags = myPageData.tags,
                        crewReward = myPageData.crewReward,
                        personalReward = myPageData.personalReward,
                        mvp = myPageData.mvp
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ 메이트(친구) 정보 조회 API
    suspend fun getMates(): List<MateData> {
        return try {
            val response = apiService.getMates()


            // ✅ API 응답 데이터 변환
            response.data?.mapIndexed { index, apiData ->
                Log.d("MateRepository", "메이트 거리 값: ${apiData.totalDistance}")

                MateData(
                    mateId = apiData.id,
                    rank = index + 1,
                    profileImageResId = getProfileImageByTendency(apiData.tendency),
                    nickname = apiData.nickname ?: "이름 없음",
                    tags = apiData.tags ?: emptyList(),
                    countDay = apiData.countDay ?: 0,
                    totalDistance = apiData.totalDistance ?: 0,
                    change = 0, // 이 부분은 지난 주? 지난 달?과 비교해서 순위변동 알려주는 란인데 api에 필드가 없음ㅅ
                    tendency = apiData.tendency ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // API 호출 실패 시 빈 리스트 반환
        }
    }

    private fun getProfileImageByTendency(tendency: String?): Int {
        return when (tendency) {
            "페이스메이커" -> R.drawable.img_profile_pacemaker
            "트레일러너" -> R.drawable.img_profile_trailrunner_red
            "스프린터" -> R.drawable.img_profile_sprinter_yellow
            else -> R.drawable.img_profile_pacemaker
        }
    }

    suspend fun deleteMate(mateId: Long): ApiResponse<Boolean> {
        return apiService.deleteMate(mateId)
    }
}