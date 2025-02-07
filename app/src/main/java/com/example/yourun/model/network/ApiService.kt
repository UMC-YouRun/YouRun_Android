package com.example.yourun.model.network

import com.example.yourun.model.data.EmailduplicateResponse
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.data.MateResponse
import com.example.yourun.model.data.SignUpRequest
//import com.example.yourun.model.data.SignUpRequest3
import com.example.yourun.model.data.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /*
    // 회원가입 - Step 1 (이메일 & 비밀번호)
    @POST("users")
    suspend fun signUp1(@Body request: SignUpRequest1): ApiResponse<SignUpResponse>

    // 회원가입 - Step 3 (닉네임 & 성향 태그)
    @POST("users")
    suspend fun signUp3(@Body request: SignUpRequest3): ApiResponse<SignUpResponse>
     */

    @POST("users/duplicate")  // POST 방식으로 이메일 중복 확인
    suspend fun checkEmailDuplicate(@Query("email") email: String): EmailduplicateResponse

    // 러닝 메이트 (리스트) 조회
    /*
    @GET("users/mates")
    suspend fun getMates(
        @Header("Authorization") token: String
    ): MateResponse
     */
    @GET("users/mates")
    suspend fun getMates(@Header("Authorization") token: String): MateResponse

    // 러닝 메이트 삭제
    /* @DELETE("mates/{id}")
    suspend fun deleteMate(@Path("id") mateId: Int): ApiResponse<Unit> */

}


data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
