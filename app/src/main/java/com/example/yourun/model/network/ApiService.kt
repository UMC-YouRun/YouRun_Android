package com.example.yourun.model.network

import com.example.yourun.model.data.response.ChallengeDataResponse
import com.example.yourun.model.data.response.ChallengeResultResponse
import com.example.yourun.model.data.response.EmailduplicateResponse
import com.example.yourun.model.data.request.LoginRequest
import com.example.yourun.model.data.response.LoginResponse
import com.example.yourun.model.data.NicknameduplicateResponse
import com.example.yourun.model.data.response.RunningStatsResponse
import com.example.yourun.model.data.request.SignUpRequest
import com.example.yourun.model.data.request.SignUpResponse
import com.example.yourun.model.data.UserInfo
import com.example.yourun.model.data.request.RunningResultRequest
import com.example.yourun.model.data.response.ApiResponseBoolean
import com.example.yourun.model.data.response.HomeChallengeResponse
import com.example.yourun.model.data.response.RecommendMateResponse
import com.example.yourun.model.data.response.RunningResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("users/mates/recommend")
    suspend fun getRecommendMate(): Response<RecommendMateResponse>

    @POST("users/mates/{mateId}")
    suspend fun addMate(
        @Path("mateId") mateId: Long
    ): Response<ApiResponseBoolean>

    @POST("users/runnings")
    suspend fun sendRunningResult(
        @Body request: RunningResultRequest
    ): Response<RunningResultResponse>
}

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
