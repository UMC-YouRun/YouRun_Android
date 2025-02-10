package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.request.RunningResultRequest
import com.example.yourun.model.data.response.RecommendMateResponse
import com.example.yourun.model.data.response.RunningResultResponse
import com.example.yourun.model.network.ApiService

class RunningRepository(private val apiService: ApiService) {

    // 서버에서 추천 메이트 데이터를 가져오는 함수
    suspend fun getRecommendMates(): RecommendMateResponse? {
        return try {
            val response = apiService.getRecommendMate()

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("HomeRepository", "추천 메이트 요청 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("HomeRepository", "추천 메이트 가져오는 중 오류 발생", e)
            null
        }
    }

    // 서버로 러닝 결과 전송
    suspend fun sendRunningResult(request: RunningResultRequest): RunningResultResponse? {
        return try {
            val response = apiService.sendRunningResult(request)
            if (response.isSuccessful) {
                response.body() // 성공 시 응답 반환
            } else {
                Log.e("RunningRepository", "서버 응답 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "API 요청 실패", e)
            null
        }
    }
}
