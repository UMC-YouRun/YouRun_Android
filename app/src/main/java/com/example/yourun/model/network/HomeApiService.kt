package com.example.yourun.model.network

import com.example.yourun.model.data.response.ApiResponseBoolean
import com.example.yourun.model.data.response.HomeChallengeResponse
import com.example.yourun.model.data.response.RecommendMateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApiService {
    @GET("users/home/challenges")
    suspend fun getHomeChallengesInfo(): Response<HomeChallengeResponse>

    @GET("users/mates/recommend")
    suspend fun getRecommendMate(): Response<RecommendMateResponse>

    @POST("users/mates/{mateId}")
    suspend fun addMate(
        @Path("mateId") mateId: Long
    ): Response<ApiResponseBoolean>
}