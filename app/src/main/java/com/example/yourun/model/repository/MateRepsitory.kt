package com.example.yourun.model.repository

import android.widget.Toast
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
                profileImageResId = getRandomProfileImage(),
                name = apiData.nickname,
                tendency = apiData.tendency,
                tags = apiData.tags,
                runday = (5..30).random(),   // 임시 랜덤 값 (나중에 API 연동 시 수정)
                distance = (10..50).random(),// 임시 랜덤 값 (나중에 API 연동 시 수정)
                change = (-2..2).random()    // 임시 순위 변동 값
            )
        } ?: emptyList()  // data가 null일 경우 빈 리스트 반환
    }

        /* return if (response.status == 200) {
            response.data?.mapIndexed { index, apiData: MateApiData ->
            MateData(
                rank = index + 1,
                profileImageResId = getRandomProfileImage(),
                name = apiData.nickname,
                tendency = apiData.tendency,
                tags = apiData.tags,
                runday = (5..30).random(),   // 임시 랜덤 값 (나중에 API 연동 시 수정)
                distance = (10..50).random(),// 임시 랜덤 값 (나중에 API 연동 시 수정)
                change = (-2..2).random()    // 임시 순위 변동 값
            )
        } ?: emptyList()  // data가 null일 경우 빈 리스트 반환
        } else {
            // 예외 상황 처리
            when (response.code) {
                "S001" -> throw Exception("잘못된 입력값입니다.")
                "S002" -> throw Exception("서버 에러가 발생했습니다.")
                "U001" -> throw Exception("존재하지 않는 사용자입니다.")
            }
            emptyList()
        }
    } */

    private fun getRandomProfileImage(): Int {
        val images = listOf(
            R.drawable.img_mate_pacemaker,
            R.drawable.img_mate_trailrunner,
            R.drawable.img_mate_sprinter
        )
        return images.random()
    }

    /* private fun showToast(message: String) {
        // Toast 메시지로 사용자에게 오류 알림 (Activity/Fragment에서 호출 시)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    } */

}
