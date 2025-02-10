package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.viewmodel.ChallengeResultViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResultSoloActivity : AppCompatActivity() {

    private val viewModel: ChallengeResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_solo)

        val btnConfirm = findViewById<ImageButton>(R.id.btn_confirm)
        val imgRunner: ImageView = findViewById(R.id.img_runner)
        val imgMateProfile: ImageView = findViewById(R.id.img_mate_profile)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvChallengeResult: TextView = findViewById(R.id.tv_challenge_result)
        val tvLucyName: TextView = findViewById(R.id.tv_lucy_name)
        val tvLucyStatus: TextView = findViewById(R.id.tv_lucy_status)
        val tvLucyDays: TextView = findViewById(R.id.tv_lucy_days)
        val tvLucyDistance: TextView = findViewById(R.id.tv_lucy_distance)

        // ✅ 5개의 스탬프 이미지뷰 리스트
        val imgStamps = listOf(
            findViewById<ImageView>(R.id.img_stamp_1),
            findViewById<ImageView>(R.id.img_stamp_2),
            findViewById<ImageView>(R.id.img_stamp_3),
            findViewById<ImageView>(R.id.img_stamp_4),
            findViewById<ImageView>(R.id.img_stamp_5)
        )

        viewModel.fetchSoloChallengeResultData()

        lifecycleScope.launch {
            viewModel.challengeData.collectLatest { challengeData ->
                challengeData?.let {
                    // ✅ 러닝 결과 이미지 변경
                    imgRunner.setImageResource(
                        if (it.userIsSuccess) R.drawable.img_running_success else R.drawable.img_running_fail
                    )

                    // ✅ 챌린지 결과 업데이트
                    tvRunningMessage.text = "${it.challengeDistance}km 달리기 완료!"
                    tvChallengeResult.text = if (it.userIsSuccess) "성공!" else "실패!"

                    tvLucyName.text = it.challengeMateInfo.challengeMateNickName
                    tvLucyStatus.text = if (it.challengeMateInfo.challengeMateIsSuccess) "성공" else "실패"
                    tvLucyDays.text = "${it.challengeMateInfo.successDay}일째!"
                    tvLucyDistance.text = "${it.challengeMateInfo.distance}km"

                    // ✅ 프로필 이미지 업데이트
                    updateProfileImage(imgMateProfile, it.challengeMateInfo.challengeMateTendency)

                    // ✅ 5일 도전 스탬프 업데이트
                    updateStamps(imgStamps, it.dayCount, it.challengeMateInfo.successDay)
                }
            }
        }

        btnConfirm.setOnClickListener {
            finish()
        }
    }

    // ✅ 상대방 프로필 이미지 업데이트
    private fun updateProfileImage(imageView: ImageView, tendency: String) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.img_crew_facemaker,
            "스프린터" to R.drawable.img_crew_sprinter,
            "트레일러너" to R.drawable.img_crew_trailrunner
        )
        imageView.setImageResource(tendencyMap[tendency] ?: R.drawable.img_crew_facemaker)
    }

    // ✅ 5일 스탬프 업데이트 로직
    private fun updateStamps(imageViews: List<ImageView>, dayCount: Int, successDay: Int) {
        imageViews.forEachIndexed { index, imageView ->
            when {
                index < successDay -> imageView.setImageResource(R.drawable.ic_stamp_success) // 성공한 날
                index < dayCount -> imageView.setImageResource(R.drawable.ic_stamp_fail) // 진행했지만 실패한 날
                else -> imageView.setImageResource(R.drawable.ic_stamp_default) // 아직 도전 안 한 날
            }
        }
    }
}
