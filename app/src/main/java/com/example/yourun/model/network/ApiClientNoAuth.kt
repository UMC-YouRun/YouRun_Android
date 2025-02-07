package com.example.yourun.model.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientNoAuth {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .apply {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(logging) // ✅ 로그인 & 회원가입은 인증 헤더 없음
        }
        .build()

    fun getApiService(): ApiService {
        val gson = GsonBuilder()
            .serializeNulls() // ✅ null 값도 JSON에 포함 (기본값 방지)
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)) // ✅ null 처리 추가
            .build()
            .create(ApiService::class.java)
    }
}
