package com.example.yourun.model.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.yourun.BuildConfig
import com.example.yourun.MyApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = BuildConfig.BASE_URL

object ApiClient {

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()

                // 로그인 요청에는 Authorization 헤더를 추가하지 않음
                if (!originalRequest.url.toString().contains("/login")) {
                    val token = TokenManager.getToken()
                    if (token.isNotEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
    fun getHomeApiService(): HomeApiService = retrofit.create(HomeApiService::class.java)
    fun getRunningApiService(): RunningApiService = retrofit.create(RunningApiService::class.java)

    object TokenManager {
        private val context: Context
            get() = MyApplication.instance.applicationContext

        private val prefs: SharedPreferences
            get() = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        fun saveToken(token: String) {
            prefs.edit().putString("access_token", token).apply()
            Log.d("TOKEN_MANAGER", "토큰 저장: $token") // 로그 추가
        }

        fun getToken(): String {
            val token = prefs.getString("access_token", "") ?: ""
            Log.d("TOKEN_MANAGER", "토큰 가져오기: $token") // 로그 추가
            return token
        }

        fun clearToken() {
            prefs.edit().remove("access_token").apply()
            Log.d("TOKEN_MANAGER", "토큰 삭제됨")
        }
    }
}
