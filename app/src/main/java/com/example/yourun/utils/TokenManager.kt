package com.example.yourun.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

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
    }
}

