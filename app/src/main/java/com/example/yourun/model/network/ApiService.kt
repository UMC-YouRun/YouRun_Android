package com.example.yourun.model.network

import com.example.yourun.model.data.response.ChallengeDataResponse
import com.example.yourun.model.data.response.ChallengeResultResponse
import com.example.yourun.model.data.response.EmailduplicateResponse
import com.example.yourun.model.data.request.LoginRequest
import com.example.yourun.model.data.response.LoginResponse
import com.example.yourun.model.data.CreateCrewChallengeRequest
import com.example.yourun.model.data.CreateCrewChallengeResponse
import com.example.yourun.model.data.CreateSoloChallengeRequest
import com.example.yourun.model.data.CreateSoloChallengeResponse
import com.example.yourun.model.data.EmailduplicateResponse
import com.example.yourun.model.data.NicknameduplicateResponse
import com.example.yourun.model.data.response.RunningStatsResponse
import com.example.yourun.model.data.request.SignUpRequest
import com.example.yourun.model.data.request.SignUpResponse
import com.example.yourun.model.data.UserInfo
import com.example.yourun.model.data.UpdateUserRequest
import com.example.yourun.model.data.UpdateUserResponse
import com.example.yourun.model.data.request.RunningResultRequest
import com.example.yourun.model.data.response.ApiResponseBoolean
import com.example.yourun.model.data.response.HomeChallengeResponse
import com.example.yourun.model.data.response.MateResponse
import com.example.yourun.model.data.response.RunningDataResponse
import com.example.yourun.model.data.response.RunningResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("users")
    suspend fun signUp(@Body request: SignUpRequest): ApiResponse<SignUpResponse>

    @POST("users/duplicate")
    suspend fun checkEmailDuplicate(@Query("email") email: String): EmailduplicateResponse

    @POST("users/check-nickname") //닉네임 중복 확인
    suspend fun checkNicknameDuplicate(@Query("nickname") nickname : String) : NicknameduplicateResponse

    @GET("users/runnings/{year}/{month}")
    suspend fun getRunningStats(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<ApiResponse<List<RunningStatsResponse>>>

    @GET("/challenges/solo/matching")
    suspend fun getChallengeData(): Response<ApiResponse<ChallengeDataResponse>>

    @GET("/challenge/solo/running-result")
    suspend fun getSoloChallengeResultData(): Response<ApiResponse<ChallengeResultResponse>>

    @GET("mypage")
    suspend fun getMyRunData() : Response<ApiResponse<UserInfo>>

    @GET("users/home/challenges")
    suspend fun getHomeChallengesInfo(): Response<HomeChallengeResponse>

    @GET("users/mates")
    suspend fun getMatesList(): Response<MateResponse>

    @POST("users/mates/{mateId}")
    suspend fun addMate(
        @Path("mateId") mateId: Long
    ): Response<ApiResponseBoolean>

    @GET("users/mates/recommend")
    suspend fun getRecommendMate(): Response<MateResponse>

    @GET("users/runnings/{id}")
    suspend fun getRunningData(
        @Path("id") id: Long
    ): Response<RunningDataResponse>

    @POST("users/runnings")
    suspend fun sendRunningResult(
        @Body request: RunningResultRequest
    ): Response<RunningResultResponse>

    @PATCH("mypage")
    suspend fun updateUserTagsAndNickname(
        @Body request: UpdateUserRequest
    ): UpdateUserResponse

    @POST("challenges/crew")
    suspend fun createcrewchallenge(@Body request: CreateCrewChallengeRequest): CreateCrewChallengeResponse

    @POST("challenges/solo")
    suspend fun createsolochallenge(@Body request: CreateSoloChallengeRequest): CreateSoloChallengeResponse

}

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
