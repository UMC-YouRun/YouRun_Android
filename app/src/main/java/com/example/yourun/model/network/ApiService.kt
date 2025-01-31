package com.example.yourun.model.network


import com.example.yourun.model.data.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


const val BASE_URL = "http://43.201.121.165:8080/api/v1/"

interface ApiService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    // 회원가입 - Step 1 (이메일 & 비밀번호)
    @POST("users")
    suspend fun signUp1(@Body request: SignUpRequest1): ApiResponse<SignUpResponse>

    // 회원가입 - Step 3 (닉네임 & 성향 태그)
    @POST("users")
    suspend fun signUp3(@Body request: SignUpRequest3): ApiResponse<SignUpResponse>
}

data class ApiResponse<T> (
    val status: Int,
    val message: String,
    val data: T? = null
)


// Retrofit API 클라이언트
object ApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃 설정
        .readTimeout(30, TimeUnit.SECONDS)  // 읽기 타임아웃 설정
        .writeTimeout(30, TimeUnit.SECONDS)  // 쓰기 타임아웃 설정
        .apply {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(logging)  // 로그 인터셉터 추가
        }
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // OkHttpClient 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
