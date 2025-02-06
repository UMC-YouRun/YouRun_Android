package com.example.yourun.model.network

import com.example.yourun.model.data.ApiResponseBoolean
import com.example.yourun.model.data.HomeChallengeResponse
import com.example.yourun.model.data.RecommendMateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApiService {
    @GET("users/home/challenges")
    suspend fun getHomeChallengesInfo(
        @Header("Authorization") token: String
    ): Response<HomeChallengeResponse>

    @GET("user/mates/recommend")
    suspend fun getRecommendMate(
        @Header("Authorization") token: String
    ): Response<RecommendMateResponse>

    @POST("user/mates/{mateId}")
    suspend fun addMate(
        @Header("Authorization") token: String,
        @Path("mateId") mateId: Long
    ): Response<ApiResponseBoolean>
}