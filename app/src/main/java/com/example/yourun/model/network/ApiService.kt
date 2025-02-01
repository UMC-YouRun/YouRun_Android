package com.example.yourun.model.network

import com.example.yourun.model.data.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    // 회원가입 - Step 1 (이메일 & 비밀번호)
    @POST("users")
    suspend fun signUp1(@Body request: SignUpRequest1): ApiResponse<SignUpResponse>

    // 회원가입 - Step 3 (닉네임 & 성향 태그)
    @POST("users")
    suspend fun signUp3(@Body request: SignUpRequest3): ApiResponse<SignUpResponse>

    @POST("users/duplicate")  // POST 방식으로 이메일 중복 확인
    suspend fun checkEmailDuplicate(@Query("email") email: String): EmailduplicateResponse
}

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
