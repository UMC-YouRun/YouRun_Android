package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.ChallengeItem
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.network.ApiService
import com.example.yourun.model.repository.ChallengeRepository
import com.example.yourun.view.adapters.ChallengeAdapter
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.ChallengeViewModelFactory
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory

class CrewChallengeFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_crew_challenge, container, false)

        viewModel = ViewModelProvider(
            this,
            ChallengeViewModelFactory(ChallengeRepository(ApiClient.getApiService()))
        ).get(ChallengeViewModel::class.java)
        recyclerView = view.findViewById(R.id.recycler_view_crew)
        challengeAdapter = ChallengeAdapter(mutableListOf())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = challengeAdapter

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.pendingCrewChallenges.observe(viewLifecycleOwner) { challenges ->
            challenges?.let {
                val challengeItems = it.map { crewChallengeRes ->
                    ChallengeItem(
                        badgeImage = R.drawable.img_crew_badge_count, // 이 부분은 필요에 따라 수정
                        title = crewChallengeRes.crewName,
                        description = "챌린지 기간: ${crewChallengeRes.challengePeriod}일",
                        members = listOf(), // 필요하면 crewChallengeRes에서 멤버 정보를 매핑
                        remaining = "남은 인원: ${crewChallengeRes.remaining}명"
                    )
                }
                challengeAdapter.updateList(challengeItems) // 🔥 변환된 리스트 전달
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPendingCrewChallenges() // API 호출
    }

    /*
    private fun getSampleData(): List<ChallengeItem> {
        return listOf(
            ChallengeItem(
                badgeImage = R.drawable.img_crew_badge_count,
                title = "4일 동안 최대 거리 러닝!",
                description = "관악산사슴 크루와 함께!",
                members = listOf(R.drawable.img_mini2_pacemaker),
                remaining = "남은 2명!"
            )
        )
    }
    */

}
