/*package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
// import com.example.yourun.model.data.ChallengeItem
import com.example.yourun.model.data.SoloChallengeRes
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.ChallengeRepository
import com.example.yourun.view.activities.SoloChallengeDetailActivity
// import com.example.yourun.view.adapters.CrewChallengeAdapter
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.ChallengeViewModelFactory

/*class CrewChallengeFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var recyclerView: RecyclerView
    //private lateinit var challengeAdapter: CrewChallengeAdapter
    // private var challengeItems: MutableList<ChallengeItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_crew_challenge, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_crew)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화
        //challengeAdapter = CrewChallengeAdapter(challengeItems)
        //recyclerView.adapter = challengeAdapter

        viewModel = ViewModelProvider(
            this,
            ChallengeViewModelFactory(ChallengeRepository(ApiClient.getApiService()))
        ).get(ChallengeViewModel::class.java)

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.pendingCrewChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("UI_DEBUG", "받은 크루 챌린지 데이터 전체: $challenges")

            if (challenges.isNullOrEmpty()) {
                Log.e("UI_DEBUG", "데이터가 없음")
                challengeItems.clear()
                challengeAdapter.updateData(emptyList()) // 데이터가 없을 경우 어댑터 업데이트
                return@observe
            }

            val newChallengeItems= challenges.map { crewChallengeRes ->
                ChallengeItem(
                    challengeId = crewChallengeRes.challengeId,
                    badgeImage = when (crewChallengeRes.reward) {
                        1 -> R.drawable.img_crew_badge_count_1
                        2 -> R.drawable.img_crew_badge_count_2
                        3 -> R.drawable.img_crew_badge_count_3
                        else -> R.drawable.img_crew_badge_count_1
                    },
                    title = "${crewChallengeRes.challengePeriod}일 연속 3km 러닝!",
                    description = "${crewChallengeRes.crewName} 크루와 함께!",
                    members = crewChallengeRes.participantIdsInfo?.map { it.memberTendencyRaw.hashCode() } ?: emptyList(),
                    remaining = "남은 ${crewChallengeRes.remaining}명!",
                    isCrewChallenge = true
                )
            }

            Log.d("UI_DEBUG", "변환된 크루 챌린지 리스트: $challengeItems")

            challengeAdapter.updateData(newChallengeItems) // 어댑터에 새 데이터 전달
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPendingCrewChallenges() // API 호출
    }
}*/