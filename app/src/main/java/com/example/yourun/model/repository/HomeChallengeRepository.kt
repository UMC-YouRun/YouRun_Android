package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.ChallengeData
import com.example.yourun.model.network.HomeApiService

class HomeChallengeRepository(private val apiService: HomeApiService) {
    suspend fun getHomeChallengeData(token: String): ChallengeData? {
        val response = apiService.getHomeChallengesInfo(token)

        Log.d("HomeChallengeRepository", "서버 응답 코드: ${response.code()}")

        return if (response.isSuccessful) {
            val body = response.body()
            if (body?.data == null) {
                Log.e("HomeChallengeRepository", "서버 응답이 null 또는 data가 null입니다.")
                null // 서버에서 data가 null이면 예외 발생하지 않고 null 반환
            } else {
                body.data // 정상 데이터 반환
            }
        } else {
            val errorBody = response.errorBody()?.string() ?: "알 수 없는 서버 오류"
            Log.e("HomeChallengeRepository", "서버 오류: $errorBody")
            throw Exception("서버 오류 (${response.code()}): $errorBody")
        }
    }
}