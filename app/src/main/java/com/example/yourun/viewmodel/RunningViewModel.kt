package com.example.yourun.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.yourun.model.data.RunningResultRequest
import com.example.yourun.model.data.RunningResultResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.Duration
import java.time.Instant
import java.util.Locale

class RunningViewModel(application: Application) : AndroidViewModel(application) {

    private val runningApiService = ApiClient.getRunningApiService(application.applicationContext)

    val targetTime = MutableLiveData<Int>()
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()
    val totalDistance = MutableLiveData<Int>()
    val mateName = MutableLiveData<String>()
    val totalTimeFormatted = MutableLiveData<String>()
    val averageSpeed = MutableLiveData<String>()

    suspend fun sendRunningResult(request: RunningResultRequest): Response<RunningResultResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = "Bearer " + (ApiClient.getAccessTokenFromSharedPreferences(getApplication()) ?: "")
                runningApiService.sendRunningResult(token, request)
            } catch (e: Exception) {
                Log.e("RunningViewModel", "API 요청 실패", e)
                throw e
            }
        }
    }

    private fun calculateRunningTime(startTime: String, endTime: String): String {
        // ISO 8601 문자열을 Instant 객체로 변환
        val startInstant = Instant.parse(startTime)
        val endInstant = Instant.parse(endTime)

        // 두 시간의 차이를 초 단위로 계산
        val durationInSeconds = Duration.between(startInstant, endInstant).seconds

        // 분과 초 변환
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 60

        // "분.초" 형식의 문자열로 변환 (소수점 2자리까지)
        return String.format(Locale.US, "%.2f", minutes + seconds / 100.0)
    }

    fun updateTotalTime() {
        val start = startTime.value ?: return
        val end = endTime.value ?: return

        totalTimeFormatted.value = calculateRunningTime(start, end)
    }

    private fun calculateAverageSpeed(totalDistance: Int, totalTimeFormatted: String): String {
        // "분.초" 형식의 totalTimeFormatted ("23.02")을 분과 초로 변환
        val timeParts = totalTimeFormatted.split(".")
        val minutes = timeParts[0].toInt()
        val seconds = timeParts[1].toInt()

        // 총 시간(초 단위) 계산
        val totalTimeSeconds = (minutes * 60) + seconds

        // 시간 값이 0이면 속도는 0
        if (totalTimeSeconds == 0) return "0.0 km/h"

        // 평균 속도 계산 (m/s → km/h 변환)
        val avgSpeed = (totalDistance.toDouble() / totalTimeSeconds) * 3.6

        // 소수점 1자리까지 포맷
        return String.format(Locale.US, "%.2f", avgSpeed)
    }

    fun updateAverageSpeed() {
        val distance = totalDistance.value ?: 0
        val time = totalTimeFormatted.value ?: "0.00"

        averageSpeed.value = calculateAverageSpeed(distance, time)
    }
}

