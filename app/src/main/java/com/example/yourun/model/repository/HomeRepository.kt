package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.response.ChallengeData
import com.example.yourun.model.data.response.RecommendMateResponse
import com.example.yourun.model.network.HomeApiService

class HomeRepository(private val apiService: HomeApiService) {
    suspend fun getHomeChallengeData(): ChallengeData? {
        return try {
            val response = apiService.getHomeChallengesInfo() // ✅ token을 넘기지 않음 (Interceptor 처리)

            Log.d("HomeRepository", "서버 응답 코드: ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data == null) {
                    Log.e("HomeRepository", "🚨 서버 응답이 null 또는 data가 null입니다.")
                    null // ✅ 서버에서 data가 null이면 예외 발생하지 않고 null 반환
                } else {
                    body.data // ✅ 정상 데이터 반환
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "알 수 없는 서버 오류"
                Log.e("HomeRepository", "🚨 서버 오류: $errorBody")
                null // ✅ 예외를 던지는 대신 null 반환
            }
        } catch (e: Exception) {
            Log.e("HomeRepository", "🚨 네트워크 오류 발생", e)
            null // ✅ 예외 발생 시 null 반환
        }
    }

    suspend fun getRecommendMates(): RecommendMateResponse? {
        return try {
            val response = apiService.getRecommendMate() // ✅ token을 넘기지 않음 (Interceptor 처리)

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("HomeRepository", "🚨 추천 메이트 요청 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("HomeRepository", "🚨 추천 메이트 가져오는 중 오류 발생", e)
            null
        }
    }

    suspend fun addMate(mateId: Long): Boolean {
        return try {
            val response = apiService.addMate(mateId) // ✅ token을 넘기지 않음 (Interceptor 처리)

            if (response.isSuccessful) {
                response.body()?.data ?: false
            } else {
                Log.e("HomeRepository", "🚨 좋아요 요청 실패: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("HomeRepository", "🚨 좋아요 요청 중 오류 발생", e)
            false
        }
    }
}