package com.example.yourun.view.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.viewmodel.SoloProgressViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SoloProgressActivity : AppCompatActivity() {

    private val viewModel: SoloProgressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_progress)

        val tvChallengeBubbleMessage : TextView = findViewById(R.id.tv_running_message)
        val tvChallengeDay: TextView = findViewById(R.id.tv1_solo_progress)
        val tvChallengeMate: TextView = findViewById(R.id.tv_lucy_name)
        val tvChallengeStatus: TextView = findViewById(R.id.tv_lucy_status)
        val tvChallengeDays: TextView = findViewById(R.id.tv_lucy_days)
        val tvChallengeDistance: TextView = findViewById(R.id.tv_lucy_distance)
        val tvChallengeMessage: TextView = findViewById(R.id.tv_10)
        val imgMateProfile: ImageView = findViewById(R.id.img_mate_profile)
        val imgChallengeStatus: ImageView = findViewById(R.id.img_challenge_status)

        val imgStamps = listOf(
            findViewById<ImageView>(R.id.img_stamp1),
            findViewById<ImageView>(R.id.img_stamp2),
            findViewById<ImageView>(R.id.img_stamp3),
            findViewById<ImageView>(R.id.img_stamp4),
            findViewById<ImageView>(R.id.img_stamp5)
        )

        viewModel.fetchSoloChallengeProgress()

        lifecycleScope.launch {
            viewModel.challengeProgress.collectLatest { progressData ->
                progressData?.let { data ->
                    val dayCount = data.dayCount
                    val challengePeriod = data.challengePeriod
                    val challengeDistance = data.challengeDistance
                    val isSuccess = data.isSuccess
                    val mate = data.challengeMateInfo

                    tvChallengeBubbleMessage.text = "${dayCount}일 연속 ${challengeDistance}km 러닝"
                    tvChallengeDay.text = "챌린지 ${dayCount}일차!"
                    tvChallengeMate.text = mate.challengeMateNickName
                    tvChallengeStatus.text = if (mate.isSuccess) "챌린지 성공" else "챌린지 실패"
                    tvChallengeDays.text = "${dayCount}/${challengePeriod}일째!"
                    tvChallengeDistance.text = "${mate.distance}km 러닝"


                    tvChallengeMessage.text = if (isSuccess) "오늘의 챌린지도 성공!" else "조금만 더 뛰어볼까요?"
                    tvChallengeMessage.setTextColor(
                        ContextCompat.getColor(
                            this@SoloProgressActivity,
                            if (isSuccess) R.color.text_purple else R.color.text_purple
                        )
                    )


                    imgChallengeStatus.setImageResource(
                        if (isSuccess) R.drawable.img_solo_progress_success
                        else R.drawable.img_solo_progress_fail
                    )

                    updateProfileImage(imgMateProfile, mate.challengeMateTendency)

                    updateStamps(imgStamps, dayCount, mate.successDay)
                }
            }
        }
    }

    private fun updateProfileImage(imageView: ImageView, tendency: String) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.profile_facemaker,
            "스프린터" to R.drawable.profile_sprinter,
            "트레일러너" to R.drawable.profile_trailrunner,
        )
        imageView.setImageResource(tendencyMap[tendency] ?: R.drawable.img_crew_facemaker)
    }

    private fun updateStamps(imageViews: List<ImageView>, dayCount: Int, successDay: Int) {
        val todayIndex = dayCount - 1

        imageViews.forEachIndexed { index, imageView ->
            when {
                index < todayIndex -> imageView.setImageResource(R.drawable.ic_stamp_success) // ✅ 오늘 이전 성공
                index == todayIndex -> {
                    if (successDay >= dayCount) {
                        imageView.setImageResource(R.drawable.img_stamp_success) // 성공
                    } else {
                        imageView.setImageResource(R.drawable.img_stamp_fail) // 실패
                    }
                }
                else -> imageView.setImageResource(R.drawable.img_stamp_default) // 기본
            }
        }
    }
}
