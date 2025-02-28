package com.example.yourun.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yourun.R
import com.example.yourun.model.data.response.CrewMember
import com.example.yourun.viewmodel.ResultContributionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResultContributionActivity : AppCompatActivity() {

    private val viewModel: ResultContributionViewModel by viewModels()
    private var isListVisible = false // 리스트 보임 여부를 저장하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_crew_contribution)

        val topBarTitle: TextView = findViewById(R.id.txtTopBarWithBackButton)
        topBarTitle.text = "크루 챌린지 결과"

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

//        val topBarTitle: TextView = findViewById(R.id.txt_top_bar_with_back_button)
        val tvWin: TextView = findViewById(R.id.tv_win)
        val layoutContributionList: LinearLayout = findViewById(R.id.layout_contribution_list)
        val layoutToggle: LinearLayout = findViewById(R.id.layout_toggle)
        val imgToggleArrow: ImageView = findViewById(R.id.img_toggle_arrow)
        val btnNext: Button = findViewById(R.id.btn_next)


        layoutToggle.setOnClickListener {
            isListVisible = !isListVisible // 토글 상태 반전
            layoutContributionList.visibility = if (isListVisible) View.VISIBLE else View.GONE
            imgToggleArrow.setImageResource(if (isListVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)

//            Log.d("ResultContributionActivity", "Toggle clicked, isListVisible: $isListVisible")
        }

        btnNext.setOnClickListener {
            finish()
        }

        // API 호출
        viewModel.fetchResultContributionData()

        lifecycleScope.launch {
            viewModel.contributionData.collectLatest { resultList ->
                if (resultList.isNotEmpty()) {
                    val result = resultList[0]
                    //topBarTitle.text = result.crewName
                    tvWin.text = if (result.win) "WIN!" else "LOSE"
                    tvWin.setTextColor(getColor(if (result.win) R.color.text_purple_900 else R.color.red))

                    // 크루원 리스트
                    updateContributionList(layoutContributionList, result.crewMemberDistance)
                }
            }
        }
    }

    // 리스트 동적 업데이트 함수
    private fun updateContributionList(parentLayout: LinearLayout, members: List<CrewMember>) {
        parentLayout.removeAllViews() // 기존 데이터 초기화

        for (member in members) {
            val itemView = layoutInflater.inflate(R.layout.item_crew_contribution, parentLayout, false)

            val tvName = itemView.findViewById<TextView>(R.id.tv_name)
            val tvDistance = itemView.findViewById<TextView>(R.id.tv_distance)
            val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
            val imgTendency = itemView.findViewById<ImageView>(R.id.img_profile)
            val tvRank = itemView.findViewById<TextView>(R.id.tv_rank)

            tvName.text = "ID ${member.userId}"
            tvDistance.text = "${member.runningDistance} km"
            progressBar.progress = (member.runningDistance * 10).toInt() // 10배 변환
            tvRank.text = "Rank: ${member.rank}"

            when (member.userTendency) {
                "스프린터" -> imgTendency.setImageResource(R.drawable.profile_sprinter)
                "트레일러너" -> imgTendency.setImageResource(R.drawable.profile_trailrunner)
                else -> imgTendency.setImageResource(R.drawable.profile_facemaker)
            }

            parentLayout.addView(itemView)
        }
    }
}
