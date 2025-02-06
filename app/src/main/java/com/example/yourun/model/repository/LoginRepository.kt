package com.example.yourun.model.repository

import com.example.yourun.model.network.ApiService
import com.example.yourun.model.data.LoginRequest
import com.example.yourun.model.data.LoginResponse
import com.example.yourun.model.network.ApiClient
import retrofit2.Response

class LoginRepository(private val apiService: ApiService, private val tokenManager: ApiClient.TokenManager) {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }
}
