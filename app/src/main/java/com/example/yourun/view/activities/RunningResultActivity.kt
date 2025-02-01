package com.example.yourun.view.activities

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.databinding.ActivityRunningResultBinding
import com.example.yourun.model.data.RunningResultRequest
import com.example.yourun.viewmodel.RunningViewModel
import com.example.yourun.viewmodel.RunningViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class RunningResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningResultBinding
    private lateinit var runningViewModel: RunningViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running_result)

        // ViewModel 초기화 (ViewModelFactory 사용)
        val factory = RunningViewModelFactory(application)
        runningViewModel = ViewModelProvider(this, factory)[RunningViewModel::class.java]

        // ViewModel을 바인딩 객체에 연결
        binding.viewModel = runningViewModel
        binding.lifecycleOwner = this  // LiveData를 UI에서 자동으로 갱신

        // Intent에서 데이터 받기
        intent?.let {
            runningViewModel.targetTime.value = it.getIntExtra("targetTime", 0)
            runningViewModel.startTime.value = it.getStringExtra("startTime") ?: ""
            runningViewModel.endTime.value = it.getStringExtra("endTime") ?: ""
            runningViewModel.totalDistance.value = it.getIntExtra("totalDistance", 0) // m 단위
            runningViewModel.mateName.value = it.getStringExtra("mateName") ?: ""
        }

        // 서버로 데이터 전송
        binding.btnOk.setOnClickListener {
            sendRunningResult()
        }

        binding.runningResultTopBar.txtTopBarWithBackButton.text = "러닝 결과"

        // UI 업데이트 (Spannable 적용)
        applySpannableFormatting()
    }

    private fun sendRunningResult() {
        // userId를 다른데서 받아와야함
        val request = RunningResultRequest(
            1,
            runningViewModel.targetTime.value ?: 0,
            runningViewModel.startTime.value ?: "",
            runningViewModel.endTime.value ?: "",
            runningViewModel.totalDistance.value ?: 0)

        lifecycleScope.launch {
            try {
                val response = runningViewModel.sendRunningResult(request)
                if (response.isSuccessful) {
                    val resultData = response.body()
                    //navigateToChallengeResult(resultData)
                } else {
                    Toast.makeText(this@RunningResultActivity, "결과 전송 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RunningResultActivity", "API 요청 중 오류 발생", e)
                Toast.makeText(this@RunningResultActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* private fun navigateToChallengeResult(resultData: RunningResultResponse?) {
        val intent = Intent(this, ChallengeResultActivity::class.java).apply {
            putExtra("resultData", resultData)
        }
        startActivity(intent)
        finish()
    } */

    private fun applySpannableFormatting() {
        // 거리 데이터 Spannable 설정
        runningViewModel.totalDistance.observe(this) { distanceInMeters ->
            val distanceInKm = distanceInMeters / 1000.0 // m → km 변환
            val formattedDistance = String.format(Locale.US, "%.1f", distanceInKm) // 소수점 1자리까지

            // "km 러닝 완료!" 형태로 텍스트 생성
            val fullText = "$formattedDistance km 러닝 완료!"
            val spannable = SpannableString(fullText)

            // 적용된 SpannableString을 TextView에 설정
            binding.txtRunningComplete.text = spannable
        }

        // 총 시간 설정
        runningViewModel.updateTotalTime()
        runningViewModel.totalTimeFormatted.observe(this) { formattedTime ->
            val fullText = "$formattedTime\""
            val spannable = SpannableString(fullText)

            // 적용된 SpannableString을 TextView에 설정
            binding.txtResultTime.text = spannable
        }

        // 평균 속도 업데이트 및 Spannable 설정
        runningViewModel.updateAverageSpeed()
        runningViewModel.averageSpeed.observe(this) { averageSpeed ->
            val formattedText = "$averageSpeed /km"
            val spannable = SpannableString(formattedText)

            spannable.setSpan(
                AbsoluteSizeSpan(16, true), // 숫자 부분 크기
                0, formattedText.indexOf("/"),
                SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                AbsoluteSizeSpan(11, true), // "/km" 부분 크기
                formattedText.indexOf("/"),
                formattedText.length,
                SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.txtResultDistance.text = spannable
        }

        // Mate 이름 색상 변경
        runningViewModel.mateName.observe(this) { mateName ->
            val formattedMate = "$mateName 보다"
            val spannableString = SpannableString(formattedMate)
            val startIndex = formattedMate.indexOf(mateName)
            val endIndex = startIndex + mateName.length

            if (startIndex >= 0) {
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this, R.color.text_purple)),
                    startIndex,
                    endIndex,
                    SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            binding.txtResultNameMate.text = spannableString
        }
    }
}