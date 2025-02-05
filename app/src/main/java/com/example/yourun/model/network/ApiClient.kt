package com.example.yourun.model.network

import android.content.Context
import android.util.Log
import com.example.yourun.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = BuildConfig.BASE_URL

object ApiClient {

    private var retrofit: Retrofit? = null

    private fun getRetrofit(context: Context): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .apply {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    addInterceptor(logging)
                }
                .addInterceptor { chain ->
                    val token = getAccessTokenFromSharedPreferences(context)
                    if (token.isNullOrEmpty()) {
                        Log.e("AuthError", "Access Token is missing or empty")
                    }
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        }
        return retrofit!!


    }


    fun getApiService(context: Context): ApiService {
        return getRetrofit(context).create(ApiService::class.java)
    }

    fun getHomeApiService(context: Context): HomeApiService {
        return getRetrofit(context).create(HomeApiService::class.java)
    }

    fun getRunningApiService(context: Context): RunningApiService {
        return getRetrofit(context).create(RunningApiService::class.java)
    }

    fun getAccessTokenFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token",null)

    }
}
