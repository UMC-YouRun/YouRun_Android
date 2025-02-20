package com.example.yourun.model.network

import com.example.yourun.model.data.response.ChallengeDataResponse
import com.example.yourun.model.data.response.ChallengeMatchingResponse
import com.example.yourun.model.data.response.ChallengeResultResponse
import com.example.yourun.model.data.response.CrewChallengeProgressResponse
import com.example.yourun.model.data.response.CrewChallengeResponse
import com.example.yourun.model.data.response.CrewChallengeResultResponse
import com.example.yourun.model.data.response.ResultContributionResponse
import com.example.yourun.model.data.response.SoloChallengeProgressResponse
import retrofit2.Response
import retrofit2.http.GET

interface ChallengeApiService {
    @GET("challenges/solo/matching")
    suspend fun getChallengeData(): Response<ApiResponse<ChallengeDataResponse>>

    @GET("challenges/crew/matching")
    suspend fun getCrewMatchingChallenge(): Response<CrewChallengeResponse>

    @GET("challenges/solo/running-result")
    suspend fun getSoloChallengeResultData(): Response<ApiResponse<ChallengeResultResponse>>

    @GET("challenges/crew/running-result")
    suspend fun getCrewChallengeResult(): Response<CrewChallengeResultResponse>

    @GET("challenges/crew/ranking-result")
    suspend fun getResultContribution(): Response<List<ResultContributionResponse>>

    @GET("challenges/solo/progress")
    suspend fun getSoloChallengeProgress(): Response<ApiResponse<SoloChallengeProgressResponse>>

    @GET("challenges/crew/detail-progress")
    suspend fun getCrewChallengeProgress(): Response<ApiResponse<CrewChallengeProgressResponse>>

    @GET("users/challenges/check-matching")
    suspend fun checkMatching(): Response<ApiResponse<ChallengeMatchingResponse>>


}
