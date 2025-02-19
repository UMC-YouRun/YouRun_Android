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

    private val repository by lazy { ChallengeRepository(ApiClient.getApiService()) }
    private val viewModel: CrewChallengeDetailViewModel by viewModels {
        CrewChallengeDetailViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crew_challenge_detail)

        val challengeId = intent.getStringExtra("challengeId") ?: ""
        if (challengeId.isNotEmpty()) {
            viewModel.fetchCrewChallengeDetail(challengeId)
        } else {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.crewChallengeDetail.observe(this) { detail ->
            Log.d("DEBUG", "API에서 받은 데이터: $detail")
            detail?.let { updateUI(it) }
        }
    }

    private fun updateUI(detail: CrewChallengeDetailRes) {
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

        val participants = detail.participantIdInfos ?: emptyList()

        // 🚀 크루 생성자의 성향을 기반으로 캐릭터 이미지 설정
        setCreatorCharacterImage(participants, creatorCharacterImage)

        // 🚀 모든 참여자의 성향을 기반으로 미니 프로필 이미지 설정
        updateParticipantImages(participants, listOf(participantImage1, participantImage2, participantImage3))

        crewSlogan.text = detail.slogan ?: "슬로건 없음"
    }

    /** 🚀 크루 챌린지 생성자의 성향을 기반으로 캐릭터 이미지 설정 */
    private fun setCreatorCharacterImage(participants: List<ParticipantIdInfo>?, imageView: ImageView) {
        if (participants.isNullOrEmpty()) {
            Log.d("DEBUG", "🚨 참여자 목록이 비어 있음. 생성자 캐릭터 설정 불가")
            imageView.visibility = ImageView.INVISIBLE
            return
        }

        val creator = participants.first() // 첫 번째 참여자를 챌린지 생성자로 간주
        Log.d("DEBUG", "✅ 크루 생성자 성향: ${creator.userTendency}")
        val characterResId = getCharacterImageResource(creator.userTendency.toString())
        imageView.setImageResource(characterResId)
        imageView.invalidate()
        imageView.visibility = ImageView.VISIBLE
    }

    /** 🚀 모든 참여자의 성향을 기반으로 미니 프로필 이미지 설정 */
    private fun updateParticipantImages(participants: List<ParticipantIdInfo>?, imageViews: List<ImageView>) {
        if (participants.isNullOrEmpty()) {
            Log.d("DEBUG", "🚨 참여자 목록이 없음. 미니 프로필 설정 불가")
            imageViews.forEach { it.visibility = ImageView.INVISIBLE }
            return
        }

        Log.d("DEBUG", "✅ 참여자 성향 리스트: ${participants.map { it.userTendency }}")

        imageViews.forEachIndexed { index, imageView ->
            if (index < participants.size) {
                val participant = participants[index]
                val profileResId = getProfileImageResource(participant.userTendency.toString())

                Log.d("DEBUG", "🔹 참여자 $index: ${participant.userTendency} → 아이콘 설정")
                imageView.setImageResource(profileResId)
                imageView.invalidate()
                imageView.visibility = ImageView.VISIBLE
            } else {
                imageView.visibility = ImageView.INVISIBLE
            }
        }
    }

    /** 🚀 성향(Tendency)에 따른 캐릭터 이미지 반환 */
    private fun getCharacterImageResource(userTendency: String): Int {
        return when (userTendency.lowercase()) {
            "sprinter", "스프린터" -> R.drawable.img_sprinter_challenge
            "pacemaker", "페이스메이커" -> R.drawable.img_pacemaker_challenge
            "trail_runner", "트레일러너" -> R.drawable.img_trailrunner_challenge
            else -> R.drawable.img_pacemaker_challenge
        }
    }

    /** 🚀 성향(Tendency)에 따른 미니 프로필 이미지 반환 */
    private fun getProfileImageResource(userTendency: String): Int {
        return when (userTendency.lowercase()) {
            "sprinter", "스프린터" -> R.drawable.img_mini_sprinter
            "pacemaker", "페이스메이커" -> R.drawable.img_mini_pacemaker
            "trail_runner", "트레일러너" -> R.drawable.img_mini_trailrunner
            else -> R.drawable.img_mini_pacemaker
        }
    }

}