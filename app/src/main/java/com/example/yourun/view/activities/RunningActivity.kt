package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.yourun.R
import com.example.yourun.databinding.ActivityRunningBinding
import com.example.yourun.viewmodel.RunningViewModel

class RunningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private val runningViewModel: RunningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running)
        binding.viewModel = runningViewModel
        binding.lifecycleOwner = this

        binding.txtTopBarWithBackButton.text = "러닝"

        // 이전 액티비티에서 받은 목표 시간 설정
        val targetTime = intent.getIntExtra("targetTime", 15) // 기본값 15분
        runningViewModel.targetTime.value = targetTime

        // 러닝 상태에 따라 버튼 UI 업데이트
        runningViewModel.isRunning.observe(this) { isRunning ->
            binding.btnRunningPlayPause.isSelected = isRunning
        }

        // 버튼 누를 시 러닝 시작, 중지
        binding.btnRunningPlayPause.setOnClickListener {
            runningViewModel.toggleRunningState()
        }

        // 목표 시간 도달 또는 수동 종료 시 RunningResultActivity로 이동
        runningViewModel.isStopped.observe(this) { isStopped ->
            if (isStopped) {
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

        runningViewModel.averageSpeed.observe(this, Observer { speed ->
            binding.txtAverageSpeed.text = applySpannable("$speed /km", 16, 11)
        })
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
        if (runningViewModel.isStopped.value != true) {
            runningViewModel.stopTracking()
        }
    }
}