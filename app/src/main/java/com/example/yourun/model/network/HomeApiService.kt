package com.example.yourun.model.network

import com.example.yourun.model.data.HomeChallengeResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiService {
    @GET("users/home/challenges")
    suspend fun getHomeChallengesInfo(
        @Header("Authorization") token: String
    ): HomeChallengeResponse
}