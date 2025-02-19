package com.example.yourun.model.network

import com.example.yourun.model.data.BaseResponse
import com.example.yourun.model.data.ChallengeDataResponse
import com.example.yourun.model.data.ChallengeResultResponse
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.data.CrewChallengeResponse
import com.example.yourun.model.data.EmailduplicateResponse
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.data.MateApiData
import com.example.yourun.model.data.MateResponse
import com.example.yourun.model.data.MyPageResponse
import com.example.yourun.model.data.SignUpRequest
import com.example.yourun.model.data.UserInfo
import com.example.yourun.model.data.NicknameduplicateResponse
import com.example.yourun.model.data.PersonalChallengeResponse
import com.example.yourun.model.data.RunningStatsResponse
import com.example.yourun.model.data.SignUpResponse
import com.example.yourun.model.data.SoloChallengeDetailRes
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

    @GET("users/mates")
    suspend fun getMates(): MateResponse<List<MateApiData>>

    @GET("challenges/crew/pending")
    suspend fun getPendingCrewChallenges(): Response<BaseResponse<CrewChallengeResponse>>

    @GET("challenges/solo/pending")
    suspend fun getPendingPersonalChallenges(): Response<BaseResponse<PersonalChallengeResponse>>

    @GET("challenges/crew/pending/{challengeId}")
    suspend fun getCrewChallengeDetail(
        @Path("challengeId") challengeId: String
    ): Response<BaseResponse<CrewChallengeDetailRes>>

    @GET("challenges/solo/pending/{challengeId}")
    suspend fun getSoloChallengeDetail(
        @Path("challengeId") challengeId: String
    ): Response<BaseResponse<SoloChallengeDetailRes>>
}


data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
