package com.example.yourun.viewmodel

import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourun.model.data.request.RunningResultRequest
import com.example.yourun.model.data.response.RunningDataResponse
import com.example.yourun.model.data.response.RunningResultResponse
import com.example.yourun.model.data.response.UserMateInfo
import com.example.yourun.model.repository.RunningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeUnit

class RunningViewModel(private val repository: RunningRepository) : ViewModel() {

    private val _recommendMates = MutableLiveData<List<UserMateInfo>>()
    val recommendMates: LiveData<List<UserMateInfo>> get() = _recommendMates

    private val _mateList = MutableLiveData<List<UserMateInfo>>()
    val mateList: LiveData<List<UserMateInfo>> get() = _mateList

    private val _mateRunningData = MutableLiveData<RunningDataResponse?>()
    val mateRunningData: LiveData<RunningDataResponse?> get() = _mateRunningData

    private val _runningResult = MutableLiveData<RunningResultResponse?>()
    val runningResult: LiveData<RunningResultResponse?> get() = _runningResult

    val targetTime = MutableLiveData<Int>()  // 목표 시간 (분)
    val totalDistance = MutableLiveData(0.0)  // 이동 거리 (m)
    val totalTimeFormatted = MutableLiveData("0.00")  // 총 러닝 시간 (분)
    val averageSpeed = MutableLiveData("0.00 /km")  // 평균 속도
    val isRunning = MutableLiveData(false)  // 러닝 중 여부
    val isStopped = MutableLiveData(false) // 러닝 종료 여부
    val userSpeed = MutableLiveData<Double>(0.0) // 유저 평균 속도 (km/h)

    val mateName = MutableLiveData<String>() // 메이트 닉네임
    val mateTendency = MutableLiveData<String>() // 메이트 성향
    val mateId = MutableLiveData<Long>() // 메이트 ID
    val mateRunningDistance = MutableLiveData<Int>() // 메이트 러닝 거리
    val mateRunningPace = MutableLiveData<Int>() // 메이트 러닝 속도

    private var lastLocation: Location? = null
    private var startTimeMillis: Long = 0
    private var elapsedTimeMillis: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    // 추천 메이트 가져오기
    fun fetchRecommendMates() {
        viewModelScope.launch {
            try {
                Log.d("RunningViewModel", "추천 메이트 요청 시작")

                val response = repository.getRecommendMates()

                if (response == null) {
                    Log.e("RunningViewModel", "response가 null입니다. 빈 리스트 반환")
                    _recommendMates.value = emptyList()
                    return@launch
                }

                response.data.let { mateList ->
                    _recommendMates.value = if (mateList.isEmpty()) {
                        Log.e("RunningViewModel", "추천 메이트 데이터 없음")
                        emptyList()
                    } else {
                        Log.d("RunningViewModel", "추천 메이트 목록 가져오기 성공: ${mateList.size}명")
                        mateList.take(5) // 최대 5명만 표시
                    }
                }
            } catch (e: Exception) {
                Log.e("RunningViewModel", "추천 메이트 가져오는 중 오류 발생", e)
                _recommendMates.value = emptyList() // 네트워크 오류 발생 시 빈 리스트 할당
            }
        }
    }

    // 메이트 목록 가져오기
    fun fetchMateList() {
        viewModelScope.launch {
            try {
                Log.d("RunningViewModel", "메이트 목록 조회 요청 시작")

                val response = repository.getMatesList()

                if (response == null) {
                    Log.e("RunningViewModel", "response가 null입니다. 빈 리스트 반환")
                    _mateList.value = emptyList()
                    return@launch
                }

                response.data.let { mateList ->
                    _mateList.value = if (mateList.isEmpty()) {
                        Log.e("RunningViewModel", "메이트 목록 데이터 없음")
                        emptyList()
                    } else {
                        Log.d("RunningViewModel", "메이트 목록 가져오기 성공: ${mateList.size}명")
                        mateList.take(5) // 최대 5명만 표시
                    }
                }
            } catch (e: Exception) {
                Log.e("RunningViewModel", "메이트 목록 가져오는 중 오류 발생", e)
                _mateList.value = emptyList() // 네트워크 오류 발생 시 빈 리스트 할당
            }
        }
    }

    // 메이트 러닝 데이터 가져오기
    fun fetchMateRunningData(mateId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.getMateRunningData(mateId)
                if (response != null) {
                    _mateRunningData.postValue(response)
                } else {
                    Log.e("RunningViewModel", "메이트 러닝 데이터가 null입니다.")
                }
            } catch (e: Exception) {
                Log.e("RunningViewModel", "메이트 러닝 데이터를 가져오는 중 오류 발생", e)
            }
        }
    }

    // 러닝 결과 서버 전송
    fun sendRunningResult(request: RunningResultRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.sendRunningResult(request)
            withContext(Dispatchers.Main) {
                _runningResult.value = result // UI 업데이트
            }
        }
    }

    // 1초마다 총 시간, 평균 속도 업데이트 및 목표 시간 체크
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (isRunning.value == true) {
                updateTotalTime()
                updateAverageSpeed()
                checkIfTargetTimeReached()
                handler.postDelayed(this, 1000)
            }
        }
    }

    // 러닝 상태 토글: 시작 또는 일시 정지
    fun toggleRunningState() {
        if (isStopped.value == true) return // 종료된 상태면 다시 시작 X

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
    }

    // 위치 업데이트를 통해 총 이동 거리 갱신 (1m 이하 오차 무시)
    fun updateLocationData(location: Location) {
        lastLocation?.let { previous ->
            val distance = previous.distanceTo(location)
            if (distance > 1) {
                totalDistance.value = (totalDistance.value ?: 0.0) + distance
            }
        }
        lastLocation = location // 현재 위치 저장 (다음 비교를 위해)
    }

    // 경과 시간(분) 계산 및 UI 업데이트
    private fun updateTotalTime() {
        val elapsedMillis = System.currentTimeMillis() - startTimeMillis
        val elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis).toDouble() +
                (TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60) / 100.0
        totalTimeFormatted.value = String.format(Locale.US, "%.1f", elapsedMinutes)
    }

    // 평균 속도 계산하여 포맷팅
    private fun updateAverageSpeed() {
        val distanceMeters = totalDistance.value ?: 0.0
        val timeFormatted = totalTimeFormatted.value ?: "0.00"

        if (distanceMeters == 0.0 || timeFormatted == "0.00") {
            averageSpeed.value = "0.00 /km"
            userSpeed.value = 0.0
            return
        }

        try {
            val minutes = timeFormatted.toDouble()
            if (minutes == 0.0) {
                averageSpeed.value = "0.00 /km"
                userSpeed.value = 0.0
                return
            }
            val distanceKm = distanceMeters / 1000.0
            val speedKmh = (distanceKm / minutes) * 60
            averageSpeed.value = String.format(Locale.US, "%.2f /km", speedKmh)
            userSpeed.value = speedKmh
        } catch (e: Exception) {
            Log.e("RunningViewModel", "평균 속도 계산 오류", e)
            averageSpeed.value = "0.00 /km"
            userSpeed.value = 0.0
        }
    }

    // 목표 시간 도달 시 러닝 종료
    private fun checkIfTargetTimeReached() {
        val targetMinutes = targetTime.value ?: return
        val elapsedMinutes = totalTimeFormatted.value?.toDoubleOrNull() ?: return

        if (elapsedMinutes >= targetMinutes) {
            stopTracking()
            Log.d("RunningViewModel", "목표 시간 도달! 러닝 종료")
        }
    }
}

