package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.response.MateResponse
import com.example.yourun.model.data.response.RunningDataResponse
import com.example.yourun.model.network.ApiService

class RunningRepository(private val apiService: ApiService) {

    // 서버에서 추천 메이트 데이터를 가져오는 함수
    suspend fun getRecommendMates(): MateResponse<Any?>? {
        return try {
            val response = apiService.getRecommendMate()

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RunningRepository", "추천 메이트 요청 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "추천 메이트 가져오는 중 오류 발생", e)
            null
        }
    }

    // 서버에서 메이트 목록을 조회하는 함수
    suspend fun getMatesList(): MateResponse<Any?>? {
        return try {
            val response = apiService.getMatesList()

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RunningRepository", "메이트 목록 조회 요청 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "메이트 목록 가져오는 중 오류 발생", e)
            null
        }
    }

    // 메이트의 러닝 데이터를 조회하는 함수
    suspend fun getMateRunningData(mateId: Long): RunningDataResponse? {
        return try {
            val response = apiService.getRunningData(mateId)

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RunningRepository", "메이트 러닝 데이터 조회 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "메이트 러닝 데이터 가져오는 중 오류 발생", e)
            null
        }
    }
}
