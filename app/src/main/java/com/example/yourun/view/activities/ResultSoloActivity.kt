package com.example.yourun.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "솔로 챌린지 결과"
        val calendarButton: ImageButton = findViewById(R.id.CalanderButton)
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }


        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        val imgRunner: ImageView = findViewById(R.id.img_runner)
        val imgMateProfile: ImageView = findViewById(R.id.img_mate_profile)
        val tvChallengeMate: TextView = findViewById(R.id.tv_challenge_mate)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvChallengeResult: TextView = findViewById(R.id.tv_challenge_result)
        val tvChallengeName: TextView = findViewById(R.id.tv_lucy_name)
        val tvChallengeStatus: TextView = findViewById(R.id.tv_lucy_status)
        val tvChallengeDays: TextView = findViewById(R.id.tv_lucy_days)
        val tvChallengeDistance: TextView = findViewById(R.id.tv_lucy_distance)

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
                    val isUserSuccess = it.userIsSuccess
                    val isMateSuccess = it.challengeMateInfo.challengeMateIsSuccess
                    val mateName = it.challengeMateInfo.challengeMateNickName
                    val dayCount = it.dayCount
                    val challengePeriod = it.challengePeriod

                    // ✅ 챌린지 정보 표시
                    tvRunningMessage.text = "${it.challengeDistance}km 달리기 완료!"
                    tvChallengeMate.text = "$mateName 과(와)의 러닝 챌린지"
                    tvChallengeResult.text = if (isUserSuccess) "${dayCount}일째 성공!" else "${dayCount}일째 실패!"

                    val textColor = if (isUserSuccess) R.color.purple_700 else R.color.red
                    tvChallengeResult.setTextColor(ContextCompat.getColor(this@ResultSoloActivity, textColor))

                    imgRunner.setImageResource(if (isUserSuccess) R.drawable.img_running_success else R.drawable.img_running_fail)

                    tvChallengeName.text = mateName
                    tvChallengeName.setTextColor(ContextCompat.getColor(this@ResultSoloActivity, R.color.purple_700))

                    tvChallengeStatus.text = if (isMateSuccess) "챌린지 성공" else "챌린지 실패"
                    tvChallengeStatus.setTextSize(18f)
                    tvChallengeStatus.setTypeface(null, android.graphics.Typeface.BOLD)
                    tvChallengeStatus.setTextColor(ContextCompat.getColor(this@ResultSoloActivity, R.color.black))

                    tvChallengeDays.text = "$dayCount / $challengePeriod 일째!"
                    tvChallengeDistance.text = "${it.challengeMateInfo.distance}km 러닝"

                    updateProfileImage(imgMateProfile, it.challengeMateInfo.challengeMateTendency)
                    updateStamps(imgStamps, dayCount, it.challengeMateInfo.successDay)
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
            "트레일러너" to R.drawable.profile_trailrunner,
        )
        imageView.setImageResource(tendencyMap[tendency] ?: R.drawable.img_crew_facemaker)
    }

    private fun updateStamps(imageViews: List<ImageView>, dayCount: Int, successDay: Int) {
        val todayIndex = dayCount - 1

        imageViews.forEachIndexed { index, imageView ->
            when {
                index < todayIndex -> imageView.setImageResource(R.drawable.ic_stamp_success)
                index == todayIndex -> {
                    if (successDay >= dayCount) {
                        imageView.setImageResource(R.drawable.ic_stamp_success)
                    } else {
                        imageView.setImageResource(R.drawable.ic_stamp_fail)
                    }
                }
                else -> imageView.setImageResource(R.drawable.ic_stamp_default)
            }
        }
    }
}

