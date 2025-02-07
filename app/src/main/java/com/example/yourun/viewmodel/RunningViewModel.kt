package com.example.yourun.viewmodel

import android.app.Application
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.yourun.model.data.RunningResultRequest
import com.example.yourun.model.data.RunningResultResponse
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.Instant
import java.util.Locale
import java.util.concurrent.TimeUnit

class RunningViewModel(application: Application) : AndroidViewModel(application) {

    private val runningApiService = ApiClient.getRunningApiService()

    val targetTime = MutableLiveData<Int>()  // 목표 시간 (분)
    val startTime = MutableLiveData<String>()  // 러닝 시작 시간
    val endTime = MutableLiveData<String>()  // 러닝 종료 시간
    val totalDistance = MutableLiveData(0)  // 이동 거리 (m)
    val mateName = MutableLiveData<String>()
    val totalTimeFormatted = MutableLiveData("0.00")  // 총 러닝 시간 (분)
    val averageSpeed = MutableLiveData("0.00 /km")  // 평균 속도
    val isRunning = MutableLiveData(false)  // 러닝 중 여부
    val isStopped = MutableLiveData(false) // 러닝 종료 여부

    private var lastLocation: Location? = null
    private var startTimeMillis: Long = 0
    private var elapsedTimeMillis: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    // 서버로 러닝 결과 전송
    suspend fun sendRunningResult(request: RunningResultRequest): Response<RunningResultResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = "Bearer " + (ApiClient.getApiService() ?: "")
                runningApiService.sendRunningResult(token, request)
            } catch (e: Exception) {
                Log.e("RunningViewModel", "API 요청 실패", e)
                throw e
            }
        }
    }

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (isRunning.value == true) {
                updateTotalTime()
                updateAverageSpeed()
                checkIfTargetTimeReached()
                handler.postDelayed(this, 1000) // 1초마다 실행
            }
        }
    }

    fun toggleRunningState() {
        if (isStopped.value == true) return  // 종료된 상태면 다시 시작 X

        if (isRunning.value == true) {
            pauseTracking()
        } else {
            startTracking()
        }
    }

    private fun startTracking() {
        if (isStopped.value == true) return

        isRunning.value = true
        startTimeMillis = System.currentTimeMillis() - elapsedTimeMillis

        // 러닝 시작 시간 설정 (ISO 8601 형식)
        startTime.value = Instant.ofEpochMilli(startTimeMillis).toString()
        handler.post(updateTimeRunnable)
    }

    private fun pauseTracking() {
        isRunning.value = false
        elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun stopTracking() {
        isRunning.value = false
        isStopped.value = true
        handler.removeCallbacks(updateTimeRunnable)

        // 러닝 종료 시간 설정 (ISO 8601 형식)
        endTime.value = System.currentTimeMillis().toString()
    }

    fun updateLocationData(location: Location) {
        if (lastLocation != null) {
            val distance = lastLocation!!.distanceTo(location).toInt()

            // GPS 오차 방지: 1m 이하 변화는 무시
            if (distance > 1) {
                totalDistance.value = (totalDistance.value ?: 0) + distance
            }
        }
        lastLocation = location // 현재 위치 저장 (다음 비교를 위해)
    }

    private fun updateTotalTime() {
        val elapsedMillis = System.currentTimeMillis() - startTimeMillis
        val elapsedTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis).toDouble() +
                (TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60) / 100.0

        totalTimeFormatted.value = String.format(Locale.US, "%.1f", elapsedTimeMinutes)
    }

    private fun updateAverageSpeed() {
        val distanceMeters = totalDistance.value ?: 0
        val timeFormatted = totalTimeFormatted.value ?: "0.00"

        if (distanceMeters == 0 || timeFormatted == "0.00") {
            averageSpeed.value = "0.00 /km"
            return
        }

        try {
            val minutes = timeFormatted.toDouble()
            if (minutes == 0.0) {
                averageSpeed.value = "0.00 /km"
                return
            }

            val distanceKm = distanceMeters / 1000.0
            val speedPerKm = distanceKm / minutes

            averageSpeed.value = String.format(Locale.US, "%.2f /km", speedPerKm)
        } catch (e: Exception) {
            Log.e("RunningViewModel", "평균 속도 계산 오류", e)
            averageSpeed.value = "0.00 /km"
        }
    }

    private fun checkIfTargetTimeReached() {
        val targetMinutes = targetTime.value ?: return
        val elapsedMinutes = totalTimeFormatted.value?.toDoubleOrNull() ?: return

        if (elapsedMinutes >= targetMinutes) {
            stopTracking()
            Log.d("RunningViewModel", "목표 시간 도달! 러닝 종료")
        }
    }
}

