package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import com.example.yourun.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.yourun.view.activities.CreateChallengeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChallengeListFragment : Fragment() {

    private lateinit var crewButton: ImageButton
    private lateinit var personalButton: ImageButton
    // 초기 선택 상태 설정 (기본값: 크루 선택)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_challenge_list, container, false)

        crewButton = view.findViewById(R.id.btn_crew)
        personalButton = view.findViewById(R.id.btn_personal)

        setSelectedButton(true)

        // 기본적으로 크루 챌린지를 먼저 표시
       // replaceFragment(CrewChallengeFragment())

        crewButton.setOnClickListener {
            Log.d("ChallengeListFragment", "크루 버튼 클릭됨")
            setSelectedButton(true)
        //    replaceFragment(CrewChallengeFragment())
        }

        personalButton.setOnClickListener {
            Log.d("ChallengeListFragment", "개인 버튼 클릭됨")
            setSelectedButton(false)
           // replaceFragment(SoloChallengeFragment())
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_plus)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), CreateChallengeActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    // 프래그먼트 전환 함수
    private fun replaceFragment(fragment: Fragment) {
        Log.d("ChallengeListFragment", "프래그먼트 변경: ${fragment::class.java.simpleName}")
        childFragmentManager.beginTransaction()
            .replace(R.id.challenge_fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    private fun setSelectedButton(isCrewSelected: Boolean) {
        Log.d("ChallengeListFragment", "setSelectedButton 호출됨: isCrewSelected=$isCrewSelected")

        if (isCrewSelected) {
            crewButton.setImageResource(R.drawable.btn_crew_selected) // 크루 버튼 활성화 이미지
            personalButton.setImageResource(R.drawable.btn_personal_default) // 개인 버튼 기본 이미지
        } else {
            crewButton.setImageResource(R.drawable.btn_crew_default) // 크루 버튼 기본 이미지
            personalButton.setImageResource(R.drawable.btn_personal_selected) // 개인 버튼 활성화 이미지
        }
        crewButton.invalidate()
        personalButton.invalidate()
    }

}