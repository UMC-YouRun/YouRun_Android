package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.CrewChallengeRes
import com.example.yourun.model.data.CrewChallengeResponse
import com.example.yourun.model.network.ApiService

class ChallengeRepository(private val apiService: ApiService) {

    suspend fun getPendingCrewChallenges(): List<CrewChallengeRes>? {
        return try {
            val response = apiService.getPendingCrewChallenges()
            if (response.isSuccessful) {
                Log.d("API_SUCCESS", "Response Body: ${response.body()}")
                response.body()?.crewChallengeRes
            } else {
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${e.message}")
            null
        }
    }
}