package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "크루 챌린지 결과"

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val btnConfirm: Button = findViewById(R.id.btn_next)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvOverlayText:TextView = findViewById(R.id.tv_overlay_text)
        val tvOverlayText2:TextView = findViewById(R.id.tv_overlay_text_2)
        val tvChallengeResult: TextView = findViewById(R.id.tv_challenge_result)
        val tvMyCrewName: TextView = findViewById(R.id.tv_name)
        val tvMyCrewDistance: TextView = findViewById(R.id.tv_mate_status)
        val imgMateProfile: ImageView = findViewById(R.id.img_mate_profile)

        val imgRunner: ImageView = findViewById(R.id.img_runner)

        val images = arrayOf(
            R.drawable.img_crew_success_01,
            R.drawable.img_crew_success_02,
            R.drawable.img_crew_success_03
        )

        imgRunner.setImageResource(images.random())

        viewModel.fetchCrewChallengeResult()

        lifecycleScope.launch {
            viewModel.challengeData.collectLatest { data ->
                if (data == null) {
                    Log.e("ResultCrewActivity", "⚠️ 데이터 없음, 기본값 설정")
                    tvRunningMessage.text = "N/A"
                    tvChallengeResult.text = "N/A"
                    tvMyCrewName.text = "N/A"
                    tvMyCrewDistance.text = "N/A"
                    tvOverlayText.text = "0.00km"
                    tvOverlayText2.text = "0.00km"
                    imgMateProfile.setImageResource(R.drawable.profile_facemaker)
                    return@collectLatest
                }

                val myCrewName = data.myCrewName ?: "N/A"
                val beforeDistance = data.beforeDistance ?: 0.0
                val afterDistance = data.afterDistance ?: 0.0
                val matchedCrewName = data.matchedCrewName ?: "N/A"
                val matchedCrewDistance = data.matchedCrewDistance ?: 0.0

                tvRunningMessage.text = "${data.challengePeriod ?: "N/A"}일 동안 크루 챌린지!"

                tvOverlayText.text = "${beforeDistance}km"
                tvOverlayText2.text = "${afterDistance}km"

                tvChallengeResult.text = "${myCrewName} 크루\n총 ${afterDistance}km 러닝!"

                tvMyCrewName.text = myCrewName
                tvMyCrewName.setTextColor(ContextCompat.getColor(this@ResultCrewActivity, R.color.black))

                tvMyCrewDistance.text = "총 ${afterDistance}km 러닝"

                // 상대 크루
                val opponentText = " ${matchedCrewName}\n총 ${matchedCrewDistance}km 러닝"
                Log.d("ResultCrewActivity", opponentText)
                updateProfileImage(imgMateProfile, data.matchedCrewCreator ?: "N/A")
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
