package com.example.yourun.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.yourun.R
import com.example.yourun.databinding.ActivityRunningBinding
import com.example.yourun.viewmodel.RunningViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class RunningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private val runningViewModel: RunningViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                runningViewModel.updateLocationData(location) // 위치 변경 시 거리 업데이트
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running)
        binding.viewModel = runningViewModel
        binding.lifecycleOwner = this

        binding.txtTopBarWithBackButton.text = "러닝"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 이전 액티비티에서 받은 목표 시간 설정
        val targetTime = intent.getIntExtra("targetTime", 15) // 기본값 15분
        runningViewModel.targetTime.value = targetTime
        binding.txtTimeToRun.text = "${targetTime}분 러닝하기!"

        // 이전 액티비티에서 받은 메이트 이름 설정
        val mateName = intent.getStringExtra("mateName")
        runningViewModel.mateName.value = mateName

        // 러닝 상태에 따라 버튼 UI 업데이트
        runningViewModel.isRunning.observe(this) { isRunning ->
            binding.btnRunningPlayPause.isSelected = isRunning
            if (isRunning) {
                startLocationUpdates() // 러닝 시작 시 위치 업데이트 시작
            } else {
                stopLocationUpdates() // 일시 중지 시 위치 업데이트 중지
            }
        }

        // 버튼 누를 시 러닝 시작, 중지
        binding.btnRunningPlayPause.setOnClickListener {
            runningViewModel.toggleRunningState()
        }

        // 목표 시간 도달 또는 수동 종료 시 RunningResultActivity로 이동
        runningViewModel.isStopped.observe(this) { isStopped ->
            if (isStopped) {
                stopLocationUpdates() // 종료 시 위치 업데이트 중지
                navigateToRunningResult()
            }
        }

        // 러닝 중 실시간 거리, 시간, 속도 업데이트 + Spannable 적용
        runningViewModel.totalDistance.observe(this) { distance ->
            binding.txtRunningDistance.text = applySpannable("$distance km", 42, 25)
        }

        runningViewModel.totalTimeFormatted.observe(this) { time ->
            binding.txtRunningTime.text = applySpannable("$time 분", 16, 11)
        }

        runningViewModel.averageSpeed.observe(this) { speed ->
            binding.txtAverageSpeed.text = applySpannable(speed, 16, 11)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(2000) // 2초마다 위치 업데이트
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(1000) // 1초마다 최소 업데이트
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun applySpannable(text: String, numberSize: Int, unitSize: Int): SpannableString {
        val spannable = SpannableString(text)
        val splitIndex = text.indexOf(" ")

        if (splitIndex > 0) {
            spannable.setSpan(AbsoluteSizeSpan(numberSize, true), 0, splitIndex, SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(AbsoluteSizeSpan(unitSize, true), splitIndex, text.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannable
    }

    // 러닝 종료 후 결과 화면으로 이동
    private fun navigateToRunningResult() {
        val intent = Intent(this, RunningResultActivity::class.java).apply {
            putExtra("startTime", runningViewModel.startTime.value)
            putExtra("endTime", runningViewModel.endTime.value)
            putExtra("totalDistance", runningViewModel.totalDistance.value)
            putExtra("totalTime", runningViewModel.totalTimeFormatted.value)
        }
        startActivity(intent)
        finish() // RunningActivity 종료
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates() // 액티비티 종료 시 위치 업데이트 중지
        if (runningViewModel.isStopped.value != true) {
            runningViewModel.stopTracking()
        }
    }
}