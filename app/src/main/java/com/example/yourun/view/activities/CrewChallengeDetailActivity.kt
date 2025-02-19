package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.data.ParticipantIdInfo
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.ChallengeRepository
import com.example.yourun.viewmodel.CrewChallengeDetailViewModel
import com.example.yourun.viewmodel.CrewChallengeDetailViewModelFactory

class CrewChallengeDetailActivity : AppCompatActivity() {

    // ✅ Repository 및 ViewModel을 올바르게 생성
    private val repository by lazy { ChallengeRepository(ApiClient.getApiService()) }
    private val viewModel: CrewChallengeDetailViewModel by viewModels {
        CrewChallengeDetailViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crew_challenge_detail) // XML 파일 지정

        val challengeId = intent.getStringExtra("challengeId") ?: ""
        if (challengeId.isNotEmpty()) {
            viewModel.fetchCrewChallengeDetail(challengeId)
        } else {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.crewChallengeDetail.observe(this) { detail ->
            Log.d("DEBUG", "API에서 받은 startDate: ${detail?.startDate}, endDate: ${detail?.endDate}")
            Log.d("DEBUG", "ViewModel에서 받은 데이터: $detail")
            Log.d("DEBUG", "ViewModel에서 받은 startDate: ${detail?.startDate}, endDate: ${detail?.endDate}")
            detail?.let { updateUI(it) }
        }
    }

    private fun updateUI(detail: CrewChallengeDetailRes) {
        Log.d("DEBUG", "UI 업데이트 - 받은 startDate: ${detail.startDate}, endDate: ${detail.endDate}")
        val crewName = findViewById<TextView>(R.id.crew_name)
        val startToEndDate = findViewById<TextView>(R.id.tv_peroid)
        val challengePeriod = findViewById<TextView>(R.id.challenge_crew_title)
        val joinCount = findViewById<TextView>(R.id.joincount)
        val subtitle = findViewById<TextView>(R.id.challenge_crew_subtitle)
        val reward = findViewById<TextView>(R.id.tv_reward)
        val participantImage1 = findViewById<ImageView>(R.id.participant_1_profile)
        val participantImage2 = findViewById<ImageView>(R.id.participant_2_profile)
        val participantImage3 = findViewById<ImageView>(R.id.participant_3_profile)
        val creatorCharacterImage = findViewById<ImageView>(R.id.participant_1_character)
        val crewSlogan = findViewById<TextView>(R.id.crew_slogan)

        crewName.text = "${detail.crewName}\n크루"
        startToEndDate.text = "${detail.startDate} ~ ${detail.endDate}"
        challengePeriod.text = "${detail.challengePeriod}일 동안 최대 거리 러닝!"
        joinCount.text = "${detail.joinCount}/4명"
        subtitle.text = "${detail.crewName} 크루와 함께!"
        reward.text = String.format("내가 더 잘 나가 %d개, MVP 달성 시 %d개", detail.reward, detail.reward * 2)

        // 🚀 Null 체크 추가
        val participants = detail.participantIdInfos ?: emptyList()

        setCreatorCharacterImage(participants, creatorCharacterImage)
        updateParticipantImages(participants, listOf(participantImage1, participantImage2, participantImage3))

        crewSlogan.text = detail.slogan ?: "슬로건 없음"
    }

    private fun setCreatorCharacterImage(participants: List<ParticipantIdInfo>?, imageView: ImageView) {
        if (participants.isNullOrEmpty()) {
            imageView.visibility = ImageView.INVISIBLE // 🚀 참여자가 없으면 숨김 처리
            return
        }

        val creator = participants.first()
        imageView.setImageResource(getCharacterImageResource(creator.userTendency.toString()))
        imageView.visibility = ImageView.VISIBLE
    }

    private fun updateParticipantImages(participants: List<ParticipantIdInfo>?, imageViews: List<ImageView>) {
        if (participants.isNullOrEmpty()) {
            imageViews.forEach { it.visibility = ImageView.INVISIBLE } // 🚀 참여자가 없으면 전부 숨김 처리
            return
        }

        imageViews.forEachIndexed { index, imageView ->
            if (index < participants.size) {
                val participant = participants[index]
                imageView.setImageResource(getProfileImageResource(participant.userTendency.toString()))
                imageView.visibility = ImageView.VISIBLE
            } else {
                imageView.visibility = ImageView.INVISIBLE // 참여자가 부족하면 숨김 처리
            }
        }
    }

    private fun getCharacterImageResource(userTendency: String): Int {
        return when (userTendency) {
            "스프린터" -> R.drawable.img_sprinter_challenge
            "페이스메이커" -> R.drawable.img_pacemaker_challenge
            "트레일러너" -> R.drawable.img_trailrunner_challenge
            else -> R.drawable.img_pacemaker_challenge
        }
    }

    private fun getProfileImageResource(userTendency: String): Int {
        return when (userTendency) {
            "스프린터" -> R.drawable.img_mini_sprinter
            "페이스메이커" -> R.drawable.img_mini_pacemaker
            "트레일러너" -> R.drawable.img_mini_trailrunner
            else -> R.drawable.img_mini_pacemaker
        }
    }
}