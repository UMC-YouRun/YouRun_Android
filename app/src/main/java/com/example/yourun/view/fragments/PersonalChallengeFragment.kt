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

class PersonalChallengeFragment : Fragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_personal_challenge, container, false)

        viewModel = ViewModelProvider(
            this,
            ChallengeViewModelFactory(ChallengeRepository(ApiClient.getApiService()))
        ).get(ChallengeViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycler_view_personal)
        challengeAdapter = ChallengeAdapter(mutableListOf())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = challengeAdapter

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.pendingPersonalChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("UI_DEBUG", "받은 솔로 챌린지 데이터 전체: $challenges")
            challenges?.forEachIndexed { index, soloChallengeRes ->
                Log.d("UI_DEBUG", "솔로 챌린지 $index: challengeId=${soloChallengeRes.challengeId}, challengeDistance=${soloChallengeRes.challengeDistance}, reward=${soloChallengeRes.reward}")
            }
            if (challenges.isNullOrEmpty()) {
                Log.e("UI_DEBUG", "데이터가 null 또는 비어 있음")
                challengeAdapter.updateList(mutableListOf()) // RecyclerView를 비우기
                return@observe
            }

            Log.d("UI_DEBUG", "받은 데이터: $challenges")

            val challengeItems = challenges.map { soloChallengeRes ->
                ChallengeItem(
                    badgeImage = R.drawable.img_personal_badge_count_1,
                    title = "${soloChallengeRes.challengeDistance}KM 챌린지",
                    description = "기간: ${soloChallengeRes.challengePeriod}일",
                    members = soloChallengeRes.challengeCreatorHashtags.map { it.hashCode() },
                    remaining = "보상: ${soloChallengeRes.reward}개"
                )
            }

            challengeAdapter.updateList(challengeItems)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("FRAGMENT_DEBUG", "onResume 호출됨 - 개인 챌린지 API 요청 실행")
        viewModel.fetchPendingPersonalChallenges() // API 호출
    }
}
