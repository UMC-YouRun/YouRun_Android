package com.example.yourun.model.network

import com.example.yourun.model.data.EmailduplicateResponse
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.data.RunningStatsResponse
import com.example.yourun.model.data.SignUpRequest
import com.example.yourun.model.data.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("users/runnings/{year}/{month}")
    suspend fun getRunningStats(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<ApiResponse<List<RunningStatsResponse>>>
}


data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
