package com.example.yourun.model.repository

import com.example.yourun.model.data.UserInfo
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.network.ApiService

import android.util.Log

class MyRunRepository(private val apiService: ApiService) {
    suspend fun getMyRunData(): ApiResponse<UserInfo>? {
        return try {
            Log.d("MyRunRepository", "getMyRunData() 호출됨") // 요청 시작 로그
            val response = apiService.getMyRunData()

            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d("MyRunRepository", "API 응답 성공: $responseData") // 성공 로그
                responseData
            } else {
                Log.e("MyRunRepository", "API 응답 실패: ${response.errorBody()?.string()}") // 실패 로그
                null
            }
        } catch (e: Exception) {
            Log.e("MyRunRepository", "API 요청 중 예외 발생", e) // 예외 로그
            null
        }
    }
}
