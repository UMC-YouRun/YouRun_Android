package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import androidx.lifecycle.lifecycleScope
import com.example.yourun.viewmodel.ChallengeResultViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResultSoloActivity : AppCompatActivity() {

    private val viewModel: ChallengeResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_solo)

        val btnConfirm = findViewById<ImageButton>(R.id.btn_confirm)
        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvChallengeResult: TextView = findViewById(R.id.tv_challenge_result)
        val tvLucyName: TextView = findViewById(R.id.tv_lucy_name)
        val tvLucyStatus: TextView = findViewById(R.id.tv_lucy_status)
        val tvLucyDays: TextView = findViewById(R.id.tv_lucy_days)
        val tvLucyDistance: TextView = findViewById(R.id.tv_lucy_distance)

        // ✅ API 데이터 가져오기
        viewModel.fetchSoloChallengeResultData()

        // ✅ 데이터 변경 감지해서 UI 업데이트
        lifecycleScope.launch {
            viewModel.challengeData.collectLatest { challengeData ->
                challengeData?.let {
                    tvRunningMessage.text = "${it.challengeDistance}km 달리기 완료!"
                    tvChallengeResult.text = if (it.userIsSuccess) "성공!" else "실패!"
                    tvLucyName.text = it.challengeMateInfo.challengeMateNickName
                    tvLucyStatus.text = if (it.challengeMateInfo.challengeMateIsSuccess) "성공" else "실패"
                    tvLucyDays.text = "${it.challengeMateInfo.successDay}일째!"
                    tvLucyDistance.text = "${it.challengeMateInfo.distance}km"
                }
            }
        }

        // ✅ 버튼 클릭 시 액티비티 종료
        btnConfirm.setOnClickListener {
            finish()
        }

        // ✅ 상단바 제목 설정
        topBarTitle.text = "개인 챌린지 결과"
    }
}
