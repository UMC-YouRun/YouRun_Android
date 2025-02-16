package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.ChallengeItem
import com.example.yourun.view.adapters.ChallengeAdapter

class PersonalChallengeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_personal_challenge, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_personal)
        challengeAdapter = ChallengeAdapter(getSampleData().toMutableList())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = challengeAdapter

        return view
    }

    private fun getSampleData(): List<ChallengeItem> {
        return listOf(
            ChallengeItem(
                badgeImage = R.drawable.img_crew_badge_count,
                title = "3일 연속 3km 러닝!",
                description = "챌린지 메이트 루시와 함께!",
                members = listOf(R.drawable.img_mini2_pacemaker),
                remaining = "남은 1명!"
            )
        )
    }
}
