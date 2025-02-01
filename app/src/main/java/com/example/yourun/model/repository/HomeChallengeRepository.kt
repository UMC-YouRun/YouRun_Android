package com.example.yourun.model.repository

import com.example.yourun.model.data.HomeChallengeResponse
import com.example.yourun.model.network.HomeApiService

class HomeChallengeRepository(private val apiService: HomeApiService) {
    suspend fun getHomeChallengeData(token: String): HomeChallengeResponse {
        return apiService.getHomeChallengesInfo(token)
    }
}