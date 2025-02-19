package com.example.yourun.model.repository

import com.example.yourun.model.network.ApiService
import com.example.yourun.model.data.request.LoginRequest
import com.example.yourun.model.data.response.LoginResponse
import retrofit2.Response

class LoginRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }

    // 카카오 로그인
    /*suspend fun loginWithKakao(kakaoAccessToken: String): Response<LoginResponse> {
        return apiService.loginWithKakao(KakaoLoginRequest(kakaoAccessToken))
    }*/
}
