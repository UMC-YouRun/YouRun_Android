package com.example.yourun.model.network

import android.content.Context
import com.example.yourun.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    @Volatile
    private var INSTANCE: ApiService? = null

    fun create(context: Context): ApiService {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildRetrofit(context).also { INSTANCE = it }
        }
    }

    private fun buildRetrofit(context: Context): ApiService {
        val tokenManager = TokenManager.getInstance(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
