package com.example.yourun.model.network

import com.example.yourun.model.data.response.ChallengeDataResponse
import com.example.yourun.model.data.response.ChallengeResultResponse
import com.example.yourun.model.data.response.CrewChallengeResponse
import com.example.yourun.model.data.response.CrewChallengeResultResponse
import com.example.yourun.model.data.response.ResultContributionResponse
import retrofit2.Response
import retrofit2.http.GET

interface ChallengeApiService {
    @GET("/challenges/solo/matching")
    suspend fun getChallengeData(): Response<ApiResponse<ChallengeDataResponse>>

    @GET("/challenges/crew/matching")
    suspend fun getCrewMatchingChallenge(): Response<CrewChallengeResponse>

    @GET("/challenge/solo/running-result")
    suspend fun getSoloChallengeResultData(): Response<ApiResponse<ChallengeResultResponse>>

    @GET("/challenges/crew/running-result")
    suspend fun getCrewChallengeResult(): Response<CrewChallengeResultResponse>

    @GET("/challenges/crew/ranking-result")
    suspend fun getResultContribution(): Response<List<ResultContributionResponse>>
}
