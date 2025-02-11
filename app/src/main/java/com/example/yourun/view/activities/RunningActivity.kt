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
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.RunningRepository
import com.example.yourun.viewmodel.RunningViewModel
import com.example.yourun.viewmodel.RunningViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class RunningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory(RunningRepository(ApiClient.getApiService()))
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                viewModel.updateLocationData(location) // 위치 변경 시 거리 업데이트
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 초기 데이터 설정
        val mateName = intent.getStringExtra("mate_nickname") ?: "닉네임"
        val targetTime = intent.getIntExtra("target_time", 0)
        val mateRunningDistance = intent.getIntExtra("mate_running_distance", 0)
        val mateRunningPace = intent.getIntExtra("mate_running_pace", 0)

        viewModel.mateName.value = mateName
        viewModel.targetTime.value = targetTime
        viewModel.mateRunningDistance.value = mateRunningDistance
        viewModel.mateRunningPace.value = mateRunningPace

        binding.txtTimeToRun.text = "${targetTime}분 러닝하기!"

        binding.txtTopBarWithBackButton.text = "러닝"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 러닝 상태에 따라 버튼 UI 업데이트
        viewModel.isRunning.observe(this) { isRunning ->
            binding.btnRunningPlayPause.isSelected = isRunning
            if (isRunning) {
                startLocationUpdates() // 러닝 시작 시 위치 업데이트 시작
            } else {
                stopLocationUpdates() // 일시 중지 시 위치 업데이트 중지
            }
        }

        // 버튼 누를 시 러닝 시작, 중지
        binding.btnRunningPlayPause.setOnClickListener {
            viewModel.toggleRunningState()
        }

        // 목표 시간 도달 또는 수동 종료 시 RunningResultActivity로 이동
        viewModel.isStopped.observe(this) { isStopped ->
            if (isStopped) {
                stopLocationUpdates() // 종료 시 위치 업데이트 중지
                navigateToRunningResult()
            }
        }

        // 러닝 중 실시간 거리, 시간, 속도 업데이트 + Spannable 적용
        viewModel.totalDistance.observe(this) { distance ->
            binding.txtRunningDistance.text = applySpannable("$distance km", 42, 25)
        }

        viewModel.totalTimeFormatted.observe(this) { time ->
            binding.txtRunningTime.text = applySpannable("$time 분", 16, 11)
        }

        viewModel.averageSpeed.observe(this) { speed ->
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
        startActivity(Intent(this, RunningResultActivity::class.java))
        finish() // RunningActivity 종료
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates() // 액티비티 종료 시 위치 업데이트 중지
        if (viewModel.isStopped.value != true) {
            viewModel.stopTracking()
        }
    }
}