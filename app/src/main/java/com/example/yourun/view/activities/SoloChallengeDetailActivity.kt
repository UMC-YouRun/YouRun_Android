package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.yourun.R
import com.example.yourun.databinding.ActivitySoloChallengeDetailBinding
import com.example.yourun.model.data.CrewChallengeDetailRes
import com.example.yourun.model.data.ParticipantIdInfo
import com.example.yourun.model.data.SoloChallengeDetailRes
import com.example.yourun.model.data.Tendency
import com.example.yourun.viewmodel.CrewChallengeDetailViewModel
import com.example.yourun.viewmodel.SoloChallengeDetailViewModel

class SoloChallengeDetailActivity : AppCompatActivity() {

    private val viewModel: SoloChallengeDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_challenge_detail) // XML 파일 지정

        val challengeId = intent.getLongExtra("challengeId", -1)
        if (challengeId != -1L) {
            viewModel.fetchSoloChallengeDetail(challengeId.toString())
        } else {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.soloChallengeDetail.observe(this) { detail ->
            detail?.let { updateUI(it) }
        }
    }

    private fun updateUI(detail: SoloChallengeDetailRes) {
        val startToEndDate = findViewById<TextView>(R.id.tv_peroid)
        val challengeTitle = findViewById<TextView>(R.id.challenge_personal_title)
        val creatorNickName = findViewById<TextView>(R.id.challenge_creator_name)
        val creatorHashTag1 = findViewById<TextView>(R.id.hashtag1)
        val creatorHashTag2 = findViewById<TextView>(R.id.hashtag2)
        val creatorCharacter = findViewById<ImageView>(R.id.challenge_mate_character)
        val reward = findViewById<TextView>(R.id.tv_reward)
        val countDay = findViewById<TextView>(R.id.tv_count_day)



        startToEndDate.text = "${detail.startDate} ~ ${detail.endDate}"
        challengeTitle.text = "${detail.challengePeriod}일 동안 ${detail.challengeDistance}km 거리 러닝!"
        creatorNickName.text = "${detail.challengeCreatorNickName}"
        if (detail.challengeCreatorHashTags.isNotEmpty()) {
            creatorHashTag1.text =
                "#${detail.challengeCreatorHashTags.getOrNull(0) ?: ""}" // 첫 번째 해시태그
            creatorHashTag2.text =
                "#${detail.challengeCreatorHashTags.getOrNull(1) ?: ""}" // 두 번째 해시태그
        } else {
            creatorHashTag1.text = ""
            creatorHashTag2.text = ""
        }
        val tendencyImageRes = getTendencyImage(detail.tendency)
        creatorCharacter.setImageResource(tendencyImageRes)
        reward.text =
            String.format("미룬이 말고 꾸준히 %d개, MVP 달성 시 %d개", detail.reward, detail.reward * 2)
        countDay.text = "${detail.countDay}일째"
    }

    private fun getTendencyImage(tendency: Tendency): Int {
        return when (tendency) {
            Tendency.SPRINTER -> R.drawable.img_sprinter_challenge
            Tendency.PACEMAKER -> R.drawable.img_pacemaker_challenge
            Tendency.TRAIL_RUNNER -> R.drawable.img_trailrunner_challenge
        }

    }
}