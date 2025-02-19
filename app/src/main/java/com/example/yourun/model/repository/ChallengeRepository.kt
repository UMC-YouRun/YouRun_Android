package com.example.yourun.model.repository

import android.util.Log
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.data.CrewChallengeRes
import com.example.yourun.model.data.CrewChallengeResponse
import com.example.yourun.model.data.SoloChallengeDetailRes
import com.example.yourun.model.data.SoloChallengeRes
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.network.ApiResponse
import com.example.yourun.model.network.ApiService
import com.google.android.gms.common.api.Api
import retrofit2.Response

class ChallengeRepository(private val apiService: ApiService = ApiClient.getApiService()) {

    suspend fun getPendingCrewChallenges(): List<CrewChallengeRes>? {
        return try {
            val response = apiService.getPendingCrewChallenges()

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("API_SUCCESS", "Raw Response Body: ${response.body()}") // 전체 응답 로그
                Log.d("API_SUCCESS", "BaseResponse data: ${body?.data}")

                body?.data?.crewChallengeResList?.also {
                    it.forEach { challenge ->
                        challenge.participantIdsInfo.forEach { member ->
                            Log.d("DEBUG", "memberTendency: ${member.memberTendency}")  // 🔥 여기서 타입 확인
                        }
                    }
                } ?: emptyList()
            } else {
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${e.message}")
            null
        }
    }

    suspend fun getPendingPersonalChallenges(): List<SoloChallengeRes>? {
        return try {
            val response = apiService.getPendingPersonalChallenges()

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("API_SUCCESS", "Raw Response Body: ${response.body()}")
                Log.d("API_SUCCESS", "BaseResponse data: ${body?.data}")

                body?.data?.soloChallengeList ?: emptyList()
            } else {
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${e.message}")
            null
        }
    }

    suspend fun getCrewChallengeDetail(challengeId: String): Response<ApiResponse<CrewChallengeDetailRes>> {
        return apiService.getCrewChallengeDetail(challengeId)
    }

    suspend fun getSoloChallengeDetail(challengeId: String): Response<ApiResponse<SoloChallengeDetailRes>> {
        return apiService.getSoloChallengeDetail(challengeId.toLong())
    }
}