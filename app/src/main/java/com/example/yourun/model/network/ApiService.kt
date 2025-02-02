package com.example.yourun.model.network

import com.example.yourun.model.data.EmailduplicateResponse
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.data.SignUpRequest
import com.example.yourun.model.data.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("users")
    suspend fun signUp(@Body request: SignUpRequest): ApiResponse<SignUpResponse>

    @POST("users/duplicate")  // POST 방식으로 이메일 중복 확인
    suspend fun checkEmailDuplicate(@Query("email") email: String): EmailduplicateResponse
}

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
