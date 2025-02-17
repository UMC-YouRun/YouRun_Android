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
import com.example.yourun.model.data.ChallengeItem
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.ChallengeRepository
import com.example.yourun.view.adapters.ChallengeAdapter
import com.example.yourun.viewmodel.ChallengeViewModel
import com.example.yourun.viewmodel.ChallengeViewModelFactory

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
            Log.d("UI_DEBUG", "받은 크루 챌린지 데이터 전체: $challenges")
            challenges?.forEachIndexed { index, crewChallengeRes ->
                Log.d("UI_DEBUG", "크루 챌린지 $index: challengeId=${crewChallengeRes.challengeId}, crewName=${crewChallengeRes.crewName}, remaining=${crewChallengeRes.remaining}")
            }
            if (challenges.isNullOrEmpty()) {
                Log.e("UI_DEBUG", "데이터가 null 또는 비어 있음")
                challengeAdapter.updateList(mutableListOf()) // RecyclerView를 비우기
                return@observe
            }

            Log.d("UI_DEBUG", "받은 데이터: $challenges")  // 정상적으로 데이터 오는지 로그 확인

            // CrewChallengeRes -> ChallengeItem으로 변환
            val challengeItems = challenges.map { crewChallengeRes ->
                ChallengeItem(
                    badgeImage = R.drawable.img_crew_badge_count_2, // 필요에 따라 수정 가능
                    title = crewChallengeRes.crewName,
                    description = "챌린지 기간: ${crewChallengeRes.challengePeriod}일",
                    members = crewChallengeRes.participantIdsInfo.map {
                        it.memberTendencyRaw.hashCode() // 🔥 `String`을 `Int`로 변환
                    },
                    remaining = "남은 인원: ${crewChallengeRes.remaining}명"
                )
            }

            // 🔥 변환된 리스트 전달
            challengeAdapter.updateList(challengeItems)
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d("FRAGMENT_DEBUG", "onResume 호출됨 - 크루 챌린지 API 요청 실행")
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
