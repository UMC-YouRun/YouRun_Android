package com.example.yourun.model.repository

import com.example.yourun.model.data.UserInfo
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.network.ApiService

class MyRunRepository(private val apiService: ApiService) {
    suspend fun getMyRunData(): ApiResponse<UserInfo>? {
        return try {
            val response = apiService.getMyRunData()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
