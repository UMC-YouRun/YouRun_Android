package com.example.yourun.model.network

import com.example.yourun.model.data.RunningResultRequest
import com.example.yourun.model.data.RunningResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RunningApiService {
    @POST("users/runnings")
    suspend fun sendRunningResult(
        @Header("Authorization") token: String,
        @Body request: RunningResultRequest
    ): Response<RunningResultResponse>
}
