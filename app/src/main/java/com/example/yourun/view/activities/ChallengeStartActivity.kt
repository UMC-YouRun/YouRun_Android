package com.example.yourun.view.activities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.model.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChallengeStartActivity : AppCompatActivity() {

    private lateinit var tvFacemakerName: TextView
    private lateinit var tvFacemakerDays: TextView
    private lateinit var tvFacemakerRole: TextView
    private lateinit var tvFacemakerTags: TextView
    private lateinit var tvTrailrunnerName: TextView
    private lateinit var tvTrailrunnerDays: TextView
    private lateinit var tvTrailrunnerRole: TextView
    private lateinit var tvTrailrunnerTags: TextView
    private lateinit var tvRunningMessage: TextView
    private lateinit var imgCrew1: ImageView
    private lateinit var imgCrew2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_challenge)

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "러닝 챌린지"

        tvFacemakerName = findViewById(R.id.tv_facemaker_name)
        tvFacemakerDays = findViewById(R.id.tv_facemaker_days)
        tvFacemakerRole = findViewById(R.id.tv_facemaker_role)
        tvFacemakerTags = findViewById(R.id.tv_facemaker_tags)
        tvTrailrunnerName = findViewById(R.id.tv_trailrunner_name)
        tvTrailrunnerDays = findViewById(R.id.tv_trailrunner_days)
        tvTrailrunnerRole = findViewById(R.id.tv_trailrunner_role)
        tvTrailrunnerTags = findViewById(R.id.tv_trailrunner_tags)
        tvRunningMessage = findViewById(R.id.tv_running_message)
        imgCrew1 = findViewById(R.id.img_crew1)
        imgCrew2 = findViewById(R.id.img_crew2)

        fetchChallengeData()

        Handler(Looper.getMainLooper()).postDelayed({
            showRewardPopup(this, findViewById(android.R.id.content))
        }, 2000)

    }

    private fun fetchChallengeData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.getChallengeApiService().getChallengeData()

                if (response.isSuccessful) {
                    val challengeData = response.body()?.data

                    Log.d("ChallengeStartActivity", "API Response: $challengeData")

                    withContext(Dispatchers.Main) {
                        if (challengeData != null) {
                            tvFacemakerName.text = challengeData.userNickName
                            tvFacemakerDays.text = "${challengeData.userCountDay}일째!"
                            tvFacemakerRole.text = challengeData.userTendency
                            tvFacemakerTags.text = challengeData.userHashTags.joinToString(", ")

                            tvTrailrunnerName.text = challengeData.challengeMateNickName
                            tvTrailrunnerDays.text = "${challengeData.challengeMateCountDay}일째!"
                            tvTrailrunnerRole.text = challengeData.challengeMateTendency
                            tvTrailrunnerTags.text = challengeData.challengeMateHashTags.joinToString(", ")

                            tvRunningMessage.text = "${challengeData.challengeDistance}km 달리기 도전!"

                            updateCrewImages(challengeData.userTendency, challengeData.challengeMateTendency)
                        } else {
                            handleNullData()
                            Log.e("ChallengeStartActivity", "서버에서 데이터가 null을 반환함.")
                        }
                    }
                } else {
                    Log.e("ChallengeStartActivity", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ChallengeStartActivity", "Exception: ${e.message}")
            }
        }
    }

    private fun updateCrewImages(userTendency: String, mateTendency: String) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.img_crew_facemaker,
            "스프린터" to R.drawable.img_crew_sprinter,
            "트레일러너" to R.drawable.img_crew_trailrunner
        )

        imgCrew1.setImageResource(tendencyMap[userTendency] ?: R.drawable.img_crew_facemaker)
        imgCrew2.setImageResource(tendencyMap[mateTendency] ?: R.drawable.img_crew_trailrunner)
    }

    private fun handleNullData() {
        tvFacemakerName.text = "정보 없음"
        tvFacemakerDays.text = "0일째!"
        tvFacemakerRole.text = "정보 없음"
        tvFacemakerTags.text = "정보 없음"

        tvTrailrunnerName.text = "정보 없음"
        tvTrailrunnerDays.text = "0일째!"
        tvTrailrunnerRole.text = "정보 없음"
        tvTrailrunnerTags.text = "정보 없음"

        tvRunningMessage.text = "도전 정보 없음"
    }

    fun showRewardPopup(context: Context, anchorView: View) {
        // 팝업 레이아웃 불러오기
        val popupView = LayoutInflater.from(context).inflate(R.layout.dialog_reward_solo, null)

        // 팝업 배경 어둡게 설정
        val backgroundDim = View(context)
        backgroundDim.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        backgroundDim.setBackgroundColor(Color.parseColor("#80000000")) // 반투명 검정색
        val parent = (context as AppCompatActivity).window.decorView as ViewGroup
        parent.addView(backgroundDim)

        // PopupWindow 설정
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // 클릭 시 자동 닫힘 설정
        ).apply {
            isOutsideTouchable = true // 바깥 클릭 가능
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 설정
        }

        // 팝업 창을 화면 중앙에 표시
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)

        // 팝업 외부 클릭 시 닫고 액티비티 종료
        popupView.setOnClickListener {
            popupWindow.dismiss()
            parent.removeView(backgroundDim) // 배경 어둡게 한 뷰 제거
            (context as AppCompatActivity).finish() // 액티비티 종료
        }

        // 팝업이 닫힐 때 배경도 제거
        popupWindow.setOnDismissListener {
            parent.removeView(backgroundDim)
            (context as AppCompatActivity).finish()
        }
    }

}
