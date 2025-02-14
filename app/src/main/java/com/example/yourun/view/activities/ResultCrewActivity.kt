package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.viewmodel.CrewChallengeResultViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResultCrewActivity : AppCompatActivity() {

    private val viewModel: CrewChallengeResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_crew)

        val btnConfirm: Button = findViewById(R.id.btn_next)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvChallengeResult: TextView = findViewById(R.id.tv_challenge_result)
        val tvMateName: TextView = findViewById(R.id.tv_name)
        val tvMateStatus: TextView = findViewById(R.id.tv_mate_status)
        val imgMateProfile: ImageView = findViewById(R.id.img_runner)

        // ViewModel에서 데이터 요청
        viewModel.fetchCrewChallengeResult()

        // 데이터 관찰 및 UI 업데이트
        lifecycleScope.launch {
            viewModel.challengeData.collectLatest { data ->
                data?.let {
                    tvRunningMessage.text = "${it.challengePeriod}일 동안 크루 챌린지!"
                    tvChallengeResult.text = "도전 완료!"
                    tvMateName.text = it.matchedCrewName
                    tvMateStatus.text = "대표자: ${it.matchedCrewCreator}"

                    updateProfileImage(imgMateProfile, it.matchedCrewCreator)
                }
            }
        }

        btnConfirm.setOnClickListener {
            finish()
        }
    }

    private fun updateProfileImage(imageView: ImageView, tendency: String) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.profile_facemaker,
            "스프린터" to R.drawable.profile_sprinter,
            "트레일러너" to R.drawable.profile_trailrunner
        )
        imageView.setImageResource(tendencyMap[tendency] ?: R.drawable.profile_facemaker)
    }
}
