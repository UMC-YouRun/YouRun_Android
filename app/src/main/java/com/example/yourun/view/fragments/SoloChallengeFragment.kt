package com.example.yourun.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.SoloChallengeRes
import com.example.yourun.model.data.response.ChallengeItem
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.ChallengeRepository
import com.example.yourun.view.adapters.CrewChallengeAdapter
import com.example.yourun.view.adapters.SoloChallengeAdapter
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.ChallengeViewModelFactory

class SoloChallengeFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: SoloChallengeAdapter
    private var challengeItems: MutableList<ChallengeItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_personal_challenge, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_personal)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화
        challengeAdapter = SoloChallengeAdapter(challengeItems)
        recyclerView.adapter = challengeAdapter

        viewModel = ViewModelProvider(
            this,
            ChallengeViewModelFactory(ChallengeRepository(ApiClient.getApiService()))
        ).get(ChallengeViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycler_view_personal)
        recyclerView.layoutManager = LinearLayoutManager(context)

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.pendingPersonalChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("UI_DEBUG", "받은 솔로 챌린지 데이터 전체: $challenges")

            if (challenges.isNullOrEmpty()) {
                Log.e("UI_DEBUG", "데이터가 없음")
                challengeItems.clear()
                challengeAdapter.updateData(emptyList()) // 데이터가 없을 경우 어댑터 업데이트
                return@observe
            }

            val newChallengeItems = challenges.map { soloChallengeRes ->
                val correctPostposition = getCorrectPostposition(soloChallengeRes.challengeCreatorNickname) // 조사 결정
                ChallengeItem(
                    challengeId = soloChallengeRes.challengeId,
                    badgeImage = when (soloChallengeRes.reward) {
                        1 -> R.drawable.img_crew_badge_count_1
                        2 -> R.drawable.img_crew_badge_count_2
                        3 -> R.drawable.img_crew_badge_count_3
                        else -> R.drawable.img_crew_badge_count_1
                    },
                    title = "${soloChallengeRes.challengePeriod}일 연속 3km 러닝!",
                    description = "${soloChallengeRes.challengeCreatorNickname}$correctPostposition 함께!",
                    members = soloChallengeRes.challengeCreatorHashtags?.map { it.hashCode() } ?: emptyList(),
                    remaining = "${soloChallengeRes.challengeCreatorHashtags}",
                    isCrewChallenge = false
                )
            }

            Log.d("UI_DEBUG", "변환된 챌린지 리스트: $challengeItems")

            challengeAdapter.updateData(newChallengeItems) // 어댑터에 새 데이터 전달
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPendingPersonalChallenges() // API 호출
    }
}

private fun getCorrectPostposition(crewName: String): String {
    return if (crewName.last().code in 0xAC00..0xD7A3) { // 한글 범위 체크
        val lastChar = (crewName.last().code - 0xAC00) % 28 // 종성 확인
        if (lastChar == 0) "와" else "과" // 받침이 없으면 "와", 있으면 "과"
    } else "과" // 한글이 아닐 경우 기본 "과"
}
