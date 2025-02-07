package com.example.yourun.model.network

import android.content.Context
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
                val request = chain.request().newBuilder()
                    //.addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                    .build()
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
        fun getToken(): String {
            val context = MyApplication.instance.applicationContext
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("access_token", "") ?: ""
        }
    }
}
