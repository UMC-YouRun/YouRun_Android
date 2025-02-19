package com.example.yourun.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.util.Log
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
import java.util.Locale

class RunningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory(RunningRepository(ApiClient.getApiService()))
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mateName: String = ""
    private var mateRunningDistanceMeters: Int = 0

    // 위치 업데이트 콜백: 새 위치가 들어올 때마다 distance 업데이트
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                /*
                viewModel.updateLocationData(location)
                 */
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 테스트용 데이터
        val targetTime = 15

        // 초기 데이터 설정
        mateName = intent.getStringExtra("mate_nickname") ?: "닉네임"
        // val targetTime = intent.getIntExtra("target_time", 0)
        mateRunningDistanceMeters = intent.getIntExtra("mate_running_distance", 0)
        Log.d("mateRunningDistance", mateRunningDistanceMeters.toString())
        val matePaceInt = intent.getIntExtra("mate_running_pace", 0)
        Log.d("matePaceInt", matePaceInt.toString())
        // matePace를 평균 속도로 변환 (km/h = 60 / pace)
        val mateSpeed = if (matePaceInt != 0) 60.0 / matePaceInt else 0.0

        viewModel.mateName.value = mateName
        viewModel.targetTime.value = targetTime
        viewModel.mateRunningDistance.value = mateRunningDistanceMeters
        viewModel.mateRunningPace.value = matePaceInt

        binding.txtTopBarWithBackButton.text = "러닝"
        binding.txtTimeToRun.text = "${targetTime}분 러닝하기!"
        binding.txtMateRunningPace.text = "$mateName ${String.format(Locale.US, "%.2f/km", mateSpeed)}"
        binding.txtMateRunningDistance.text = String.format(Locale.US, "%.2fkm", mateRunningDistanceMeters / 1000.0)

        binding.loadingRunningAnimation.setAnimation(R.raw.loading_running)
        binding.loadingRunningAnimation.playAnimation()

        // 백 버튼 클릭 시, 홈 화면으로 이동
        binding.backButton.setOnClickListener {
            moveTaskToBack(true)
        }

        // 캘린더 화면으로 이동
        binding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 러닝 상태 변화에 따른 UI 및 위치 업데이트 제어
        viewModel.isRunning.observe(this) { isRunning ->
            binding.btnRunningPlayPause.isSelected = isRunning
            if (isRunning) {
                startLocationUpdates()
            } else {
                stopLocationUpdates()
            }
        }

        // 버튼 누를 시 러닝 시작, 중지
        binding.btnRunningPlayPause.setOnClickListener {
            /*
            viewModel.toggleRunningState()
             */
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
            val kmDistance = distance / 1000.0
            binding.txtRunningDistance.text = applySpannable(
                String.format(Locale.US, "%.2f km", kmDistance),
                42,
                25)
            updateProgressBar()
        }

        viewModel.totalTimeFormatted.observe(this) { time ->
            binding.txtRunningTime.text = applySpannable("$time 분", 16, 11)
        }

        viewModel.averageSpeed.observe(this) { speed ->
            binding.txtAverageSpeed.text = applySpannable(speed, 16, 11)
        }

        // 유저의 평균 속도(userSpeed)를 관찰하여, 메이트의 속도와 비교해 하단 텍스트 업데이트
        viewModel.userSpeed.observe(this) { userSpeedValue ->
            updateRunningComment(userSpeedValue, mateSpeed)
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

    /**
     * 사용자의 현재 누적 거리와 메이트의 러닝 거리를 비교하여
     * 프로그레스 바의 진행률을 업데이트하고,
     * 프로그레스 바 위의 말풍선(txtDistanceDiff)의 텍스트와 위치를 조정합니다.
     */
    private fun updateProgressBar() {
        val userDistanceMeters = viewModel.totalDistance.value ?: 0.0
        val userDistanceKm = userDistanceMeters / 1000.0
        val mateDistanceKm = mateRunningDistanceMeters / 1000.0

        // 메이트의 거리가 0보다 크면 진행률 계산 (최대 100%)
        val progress = if (mateDistanceKm > 0) {
            ((userDistanceKm / mateDistanceKm) * 100).toInt().coerceAtMost(100)
        } else 0
        binding.progressBarDistance.progress = progress

        // 사용자 거리와 메이트 거리 차이 (사용자 - 메이트)
        val diff = userDistanceKm - mateDistanceKm
        val diffText = if (diff > 0) {
            String.format(Locale.US, "+%.2f", diff)
        } else {
            String.format(Locale.US, "-%.2f", diff)
        }
        binding.txtDistanceDiff.text = diffText

        // 말풍선 위치를 프로그레스 바의 채워진 영역 중앙에 맞춥니다
        binding.progressBarDistance.post {
            val progressBarWidth = binding.progressBarDistance.width.toFloat()
            val progressFraction = progress.toFloat() / binding.progressBarDistance.max
            val bubbleWidth = binding.txtDistanceDiff.width.toFloat()
            // bubble의 오른쪽 끝이 채워진 영역 끝과 맞닿도록 translationX 계산
            val newX = progressBarWidth * progressFraction - bubbleWidth
            binding.txtDistanceDiff.translationX = newX
        }
    }

    /**
     * 유저와 메이트의 평균 속도(모두 km/h)를 비교하여, 텍스트를 업데이트합니다.
     *
     * - 유저 속도가 느리면: "페이스 업! 속도를 올려주세요"
     * - 유저 속도가 같으면: "비슷한 속도로 러닝 중!"
     * - 유저 속도가 빠르면: "페이스 다운! 속도를 낮춰주세요"
     */
    private fun updateRunningComment(userSpeed: Double, mateSpeed: Double) {
        val comment = when {
            userSpeed < mateSpeed -> "페이스 업! 속도를 올려주세요"
            userSpeed > mateSpeed -> "페이스 다운! 속도를 낮춰주세요"
            else -> "비슷한 속도로 러닝 중!"
        }
        binding.txtRunningComment.text = comment
    }

    // 러닝 종료 후 결과 화면으로 이동
    private fun navigateToRunningResult() {
        // 사용자의 총 러닝 거리 (미터) -> km로 변환
        val userDistanceMeters = viewModel.totalDistance.value ?: 0.0
        val userDistanceKm = userDistanceMeters / 1000.0

        // 러닝 시간과 평균 속도
        val runningTime = viewModel.targetTime.value ?: 0
        val avgSpeed = viewModel.averageSpeed.value ?: "0.00 /km"

        // 메이트의 러닝 거리 (미터) -> km로 변환
        val mateDistanceKm = mateRunningDistanceMeters / 1000.0

        // 사용자가 메이트보다 얼마나 더 뛰었는지 계산 (양수: 초과, 음수: 부족)
        val distanceDifference = userDistanceKm - mateDistanceKm

        // Intent에 데이터를 넣어 RunningResultActivity로 전환
        val intent = Intent(this, RunningResultActivity::class.java).apply {
            putExtra("user_running_distance", userDistanceKm)
            putExtra("running_time", runningTime)
            putExtra("average_speed", avgSpeed)
            putExtra("mate_nickname", mateName)
            putExtra("distance_difference", distanceDifference)
            putExtra("startTime", viewModel.startTime.value ?: "")
            putExtra("endTime", viewModel.endTime.value ?: "")
            putExtra("targetTime", viewModel.targetTime.value ?: 0)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.loadingRunningAnimation.pauseAnimation()
        stopLocationUpdates() // 액티비티 종료 시 위치 업데이트 중지
        if (viewModel.isStopped.value != true) {
            /*
            viewModel.stopTracking()
             */
        }
    }
}