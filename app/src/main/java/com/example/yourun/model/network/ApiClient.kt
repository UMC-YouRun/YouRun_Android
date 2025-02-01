package com.example.yourun.model.network

import android.content.Context
import com.example.yourun.BuildConfig
import com.google.firebase.appdistribution.gradle.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = BuildConfig.BASE_URL

object ApiClient {

    // Context를 매개변수로 받아 사용
    fun getApiService(context: Context): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .apply {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                addInterceptor(logging)  // 로그 인터셉터 추가
            }
            .addInterceptor { chain ->
                val token = getAccessTokenFromSharedPreferences(context) // SharedPreferences에서 토큰 가져오기
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token") // Authorization 헤더에 토큰 추가
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // OkHttpClient 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getAccessTokenFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}