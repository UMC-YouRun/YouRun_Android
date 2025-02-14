package com.example.yourun.model.repository

import com.example.yourun.R
import com.example.yourun.model.data.*
import com.example.yourun.model.data.MateApiData
import com.example.yourun.model.data.MateData
import com.example.yourun.model.data.MateResponse
import com.example.yourun.model.data.MyPageData
import com.example.yourun.model.data.UserInfo
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
                MateData(
                    rank = index + 1,
                    profileImageResId = getProfileImageByTendency(apiData.tendency),
                    nickname = apiData.nickname ?: "이름 없음",
                    tags = apiData.tags ?: emptyList(),
                    countDay = apiData.countDay ?: 0,
                    totalDistance = apiData.totalDistance ?: 0,
                    change = apiData.countDay ?: 0,
                    tendency = apiData.tendency ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // API 호출 실패 시 빈 리스트 반환
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
            else -> R.drawable.img_profile_pacemaker_purple
        }
    }
}