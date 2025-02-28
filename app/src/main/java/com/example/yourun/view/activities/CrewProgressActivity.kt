package com.example.yourun.view.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.viewmodel.CrewProgressViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CrewProgressActivity : AppCompatActivity() {

    private val viewModel: CrewProgressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_crew_progress)

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "크루 챌린지 진행도"

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val tvTitle: TextView = findViewById(R.id.tv_title)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val tvRunningMessage: TextView = findViewById(R.id.tv_running_message)
        val tvMyCrewName: TextView = findViewById(R.id.tv_image_inner_text_top)
        val tvMyCrewDistance: TextView = findViewById(R.id.tv_image_inner_text_bottom)
        val tvMyCrewSlogan: TextView = findViewById(R.id.tv_image_top_text)
        val tvOpponentCrewName: TextView = findViewById(R.id.tv_opponent_crew_name)
        val tvOpponentCrewSlogan: TextView = findViewById(R.id.tv_opponent_slogan)
        val tvOpponentCrewDistance: TextView = findViewById(R.id.tv_opponent_km)
        val imgOpponentCharacter: ImageView = findViewById(R.id.img_opponent_character)

        val crewMembersUI = listOf(
            Triple(
                findViewById<TextView>(R.id.tv_crew_name_1),
                findViewById<TextView>(R.id.tv_crew_km_1),
                findViewById<ImageView>(R.id.img_crew_1)
            ),
            Triple(
                findViewById<TextView>(R.id.tv_crew_name_2),
                findViewById<TextView>(R.id.tv_crew_km_2),
                findViewById<ImageView>(R.id.img_crew_2)
            ),
            Triple(
                findViewById<TextView>(R.id.tv_crew_name_3),
                findViewById<TextView>(R.id.tv_crew_km_3),
                findViewById<ImageView>(R.id.img_crew_3)
            ),
            Triple(
                findViewById<TextView>(R.id.tv_crew_name_4),
                findViewById<TextView>(R.id.tv_crew_km_4),
                findViewById<ImageView>(R.id.img_crew_4)
            )
        )

        viewModel.fetchCrewChallengeProgress()

        lifecycleScope.launch {
            viewModel.crewProgressData.collectLatest { data ->
                if (data != null) {

                    tvTitle.text = "챌린지 ${data.challengePeriod}일차!"
                    tvDate.text = "${data.now} 기준"
                    tvRunningMessage.text = "${data.challengePeriod}일 동안 최대 거리 러닝!"


                    tvMyCrewName.text = data.myCrewName
                    tvMyCrewDistance.text = "${data.myCrewDistance}km 러닝 중!"
                    tvMyCrewSlogan.text = data.myCrewSlogan

                    tvOpponentCrewName.text = data.matchedCrewName
                    tvOpponentCrewSlogan.text = data.matchedCrewSlogan
                    tvOpponentCrewDistance.text = "${data.matchedCrewDistance}km 러닝 중!"

                    val tendencyMap = mapOf(
                        "페이스메이커" to R.drawable.img_challenge_trailrunner,
                        "스프린터" to R.drawable.img_challenge_sprinter
                    )
                    imgOpponentCharacter.setImageResource(
                        tendencyMap[data.matchedCrewCreatorTendency] ?: R.drawable.img_challenge_facemaker
                    )

                    val members = data.myCrewMembers
                    crewMembersUI.forEachIndexed { index, (nameView, distanceView, imageView) ->
                        if (index < members.size) {
                            val member = members[index]

                            nameView.text = "멤버 ${member.userId}"
                            distanceView.text = "${member.runningDistance}km"

                            imageView.setImageResource(
                                when (member.userTendency) {
                                    "페이스메이커" -> R.drawable.profile_facemaker
                                    "스프린터" -> R.drawable.profile_sprinter
                                    else -> R.drawable.profile_trailrunner
                                }
                            )
                        } else {
                            nameView.text = "N/A"
                            distanceView.text = "0.0km"
                            imageView.setImageResource(R.drawable.profile_facemaker)
                        }
                    }
                }
            }
        }
    }
}
