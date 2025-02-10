package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.data.response.CrewChallengeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrewChallengeStartActivity : AppCompatActivity() {

    private lateinit var tvCrewName: TextView
    private lateinit var tvCrewSlogan: TextView
    private lateinit var tvMatchedCrewName: TextView
    private lateinit var tvMatchedCrewSlogan: TextView
    private lateinit var tvRunningMessage: TextView
    private lateinit var imgCrewProfiles: List<ImageView>
    private lateinit var imgMatchedCrewProfiles: List<ImageView>
    private lateinit var imgCrewLeader: ImageView
    private lateinit var imgMatchedCrewLeader: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_crew_challenge)

        tvCrewName = findViewById(R.id.tv_crew1_name)
        tvCrewSlogan = findViewById(R.id.tv_crew1_title)
        tvMatchedCrewName = findViewById(R.id.tv_crew2_name)
        tvMatchedCrewSlogan = findViewById(R.id.tv_crew2_title)
        tvRunningMessage = findViewById(R.id.tv_running_message)
        imgCrewLeader = findViewById(R.id.img_crew1)
        imgMatchedCrewLeader = findViewById(R.id.img_crew2)

        imgCrewProfiles = listOf(
            findViewById(R.id.img_crew1_profile1),
            findViewById(R.id.img_crew1_profile2),
            findViewById(R.id.img_crew1_profile3),
            findViewById(R.id.img_crew1_profile4)
        )

        imgMatchedCrewProfiles = listOf(
            findViewById(R.id.img_crew2_profile1),
            findViewById(R.id.img_crew2_profile2),
            findViewById(R.id.img_crew2_profile3),
            findViewById(R.id.img_crew2_profile4)
        )

        fetchChallengeData()
    }

    private fun fetchChallengeData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.getChallengeApiService().getCrewMatchingChallenge()
                if (response.isSuccessful) {
                    val challengeData = response.body()?.data

                    Log.d("CrewChallengeStart", "API Response: $challengeData")

                    withContext(Dispatchers.Main) {
                        if (challengeData != null) {
                            tvCrewName.text = challengeData.crewName
                            tvCrewSlogan.text = challengeData.myCrewSlogan
                            tvMatchedCrewName.text = challengeData.matchedCrewName
                            tvMatchedCrewSlogan.text = challengeData.matchedCrewSlogan
                            tvRunningMessage.text = "${challengeData.period}일 동안 챌린지!"

                            updateProfileImages(imgCrewProfiles, challengeData.myParticipantIdsInfo)
                            updateProfileImages(imgMatchedCrewProfiles, challengeData.matchedParticipantIdsInfo)

                            updateLeaderImage(imgCrewLeader, challengeData.myParticipantIdsInfo)
                            updateLeaderImage(imgMatchedCrewLeader, challengeData.matchedParticipantIdsInfo)
                        } else {
                            handleNullData()
                            Log.e("CrewChallengeStart", "서버에서 데이터가 null을 반환함.")
                        }
                    }
                } else {
                    Log.e("CrewChallengeStart", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CrewChallengeStart", "Exception: ${e.message}")
            }
        }
    }

    private fun updateProfileImages(imageViews: List<ImageView>, participants: List<CrewChallengeData.Participant>) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.profile_facemaker,
            "스프린터" to R.drawable.profile_sprinter,
            "트레일러너" to R.drawable.profile_trailrunner
        )

        participants.forEachIndexed { index, participant ->
            if (index < imageViews.size) {
                imageViews[index].setImageResource(tendencyMap[participant.memberTendency] ?: R.drawable.profile_facemaker)
            }
        }
    }

    private fun updateLeaderImage(imageView: ImageView, participants: List<CrewChallengeData.Participant>) {
        val tendencyMap = mapOf(
            "페이스메이커" to R.drawable.img_crew_facemaker,
            "스프린터" to R.drawable.img_crew_sprinter,
            "트레일러너" to R.drawable.img_crew_trailrunner
        )

        if (participants.isNotEmpty()) {
            val leader = participants.first()
            imageView.setImageResource(tendencyMap[leader.memberTendency] ?: R.drawable.img_crew_facemaker)
        }
    }

    private fun handleNullData() {
        tvCrewName.text = "정보 없음"
        tvCrewSlogan.text = "정보 없음"
        tvMatchedCrewName.text = "정보 없음"
        tvMatchedCrewSlogan.text = "정보 없음"
        tvRunningMessage.text = "도전 정보 없음"
    }
}
