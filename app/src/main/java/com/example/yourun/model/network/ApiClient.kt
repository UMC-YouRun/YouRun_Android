package com.example.yourun.model.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.yourun.BuildConfig
import com.example.yourun.MyApplication
import okhttp3.*
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
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestUrl = originalRequest.url.toString()

                if (requestUrl.contains("/api/v1/users/login")) {
                    Log.d("Interceptor", "로그인 요청이므로 Authorization 헤더를 추가하지 않음")
                    return@addInterceptor chain.proceed(originalRequest)
                }

                // 로그인 이외의 요청에만 토큰 추가
                val token = TokenManager.getToken()
                if (token.isNotEmpty()) {
                    val requestWithAuth = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $token") // 올바른 형식으로 추가
                        .build()
                    Log.d("Interceptor", "Authorization 헤더 추가됨: Bearer $token")
                    return@addInterceptor chain.proceed(requestWithAuth)
                }

                return@addInterceptor chain.proceed(originalRequest)
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

    fun getChallengeApiService(): ChallengeApiService {
        Log.d("API_BASE_URL", "현재 BASE_URL: $BASE_URL")
        return retrofit.create(ChallengeApiService::class.java)
    }

    object TokenManager {
        private val context: Context
            get() = MyApplication.instance.applicationContext

        private val prefs: SharedPreferences
            get() = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

        fun saveToken(token: String) {
            val cleanedToken = token.trim()
            val editor = prefs.edit()
            editor.putString("access_token", cleanedToken)
            val success = editor.commit() // 즉시 저장

            if (success) {
                Log.d("TOKEN_MANAGER", "새로운 토큰 저장 완료: $cleanedToken")
            } else {
                Log.e("TOKEN_MANAGER", "토큰 저장 실패")
            }

            // 저장 후 즉시 불러와서 확인
            val savedToken = getToken()
            Log.d("TOKEN_MANAGER", "저장된 JWT 확인: $savedToken")

            if (cleanedToken == savedToken) {
                Log.d("TOKEN_MANAGER", "저장된 토큰이 서버에서 받은 토큰과 동일함")
            } else {
                Log.e("TOKEN_MANAGER", "저장된 토큰이 서버에서 받은 토큰과 다름! 저장 과정에서 변형됨!")
            }
        }

        fun getToken(): String {
            val token = prefs.getString("access_token", "") ?: ""

            Log.d("TOKEN_MANAGER", "SharedPreferences에서 불러온 최신 JWT: $token")
            return token
        }

        fun clearToken() {
            prefs.edit().remove("access_token").apply()
            Log.d("TOKEN_MANAGER", "토큰 삭제됨")
        }
    }
}
